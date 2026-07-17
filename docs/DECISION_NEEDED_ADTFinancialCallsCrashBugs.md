# Decision needed: fix the 2 confirmed crash bugs from ADT's financial-call characterization

*Blueprint (docs only, no code applied). 2026-07-17. Covers both findings logged in `BR-PENDING-adt-financial-calls-batch1`, confirmed via passing tests. No code has been changed yet.*

---

## Bug 1: `convertServicesFromOPToIP` crashes when a service has no matching package/contract

**Where:** `legacy/Global/src/main/java/com/param/global/billing/service/BillingServiceImpl.java`, inside `convertServicesFromOPToIP`, lines 699-730.

**What happens today:**

```java
res = iMPackageMasterService.checkServiceAvailableInPackageContract(consumptionDto);
consumptionDto = (ContractConsumptionDto) res.getObject();   // line 700 -- can become null

if (res.getObject() != null)
{
    // ~20 lines copying rate/tax/self-payable/credit-payable/contract fields
    // from consumptionDto onto orderDetailsMasterDto
}

orderMasterIds.add(orderDetailsMasterDto.getOrderId());

iOrderDetailsMasterDao.saveOrderDetailsMaster(orderDetailsMasterDto,
        consumptionDto.getOrderDetailsPayeeMapperDtosList(),   // line 729 -- NPE if consumptionDto is null
        consumptionDto.getOrderDetailsPackageMapperDtosList());
```

When no package/contract is found for a service (a plausible, everyday case — e.g. a self-pay patient, or a service simply not covered by any active package), `consumptionDto` becomes `null` at line 700. The `if` block correctly skips the field-copy, but the unconditional call two lines later dereferences the same now-null `consumptionDto` regardless — throwing a `NullPointerException`, caught by the method's own outer `catch(Exception e)` and rethrown as `BillingException`.

**Confirmed live via a passing test** (`BillingServiceImplConvertServicesFromOPToIPCharacterizationTest#contractNotFound_wholeConversionThrows`).

**Blast radius — currently silent:** this method is called from two places, both of which swallow the resulting exception with an **empty catch block** (no logging at all):
- `BedAllocationServiceImpl.acceptNursingAcceptanceRequet` (lines 666-690) — the nursing-acceptance step (`iBedAllocationDao.acceptNursingAcceptanceRequet(admission)`) may have already committed before this crash fires; the caller has no way to know.
- `BedAllocationController.savePatientAdmission` — same shape, also empty `catch (Exception e) { }`.

## Bug 2: `addDepositAgainstAdmission` crashes when the deposit-master save doesn't succeed

**Where:** `legacy/Global/src/main/java/com/param/global/billing/service/DepositMasterServiceImpl.java`, inside `addDepositAgainstAdmission`, lines 444-535.

**What happens today:**

```java
DepositMaster depositMaster = null;                                    // line 447
...
Response depositRes = iDepositMasterDao.saveDeposit(deposit);
if (depositRes.getCode().equalsIgnoreCase(SUCCESS_CODE) && depositRes.getObject() != null) {
     depositMaster = (DepositMaster) depositRes.getObject();            // line 487 -- only assigned here
     // ... save payment-detail records
}
...
return new Response<>(SUCCESS, SUCCESS_CODE, DEPOSIT_ADDED_MSG + deposit.getDepositNo(), null,
        depositMaster.getDepositMasterId());                            // line 528 -- NPE if the save above didn't succeed
```

If the deposit-master save doesn't return a success code with a non-null object, `depositMaster` stays `null`, and the method's own success-return path unconditionally dereferences it — throwing an NPE, caught by the method's own `catch(Exception e)` and rethrown as `BillingException`. Unlike the already-known gap in Pharmacy's `saveBillDetails` (which silently continues and just skips creating a compensating record), this ADT version crashes outright — no response is ever returned.

**Confirmed live via a passing test** (`DepositMasterServiceImplAddDepositAgainstAdmissionCharacterizationTest#depositSaveFails_crashesInsteadOfReturning`).

**Blast radius:** called from `BedAllocationServiceImpl.savePatientAdmission` (a large method deeper in the admission-save flow). That method's own error handling wasn't traced line-by-line in this pass (out of scope, see `BR-PENDING-adt-financial-calls-batch1`), so this proposal doesn't claim to know whether that specific caller also swallows the exception silently — only that the crash itself is confirmed.

---

## Proposed fix — minimal, matches this codebase's own existing idioms, not a redesign

### Fix 1a: `convertServicesFromOPToIP` — guard the crash point

```java
// after the existing if(res.getObject()!=null) block, unchanged:
orderMasterIds.add(orderDetailsMasterDto.getOrderId());

if (consumptionDto != null) {
    iOrderDetailsMasterDao.saveOrderDetailsMaster(orderDetailsMasterDto,
            consumptionDto.getOrderDetailsPayeeMapperDtosList(),
            consumptionDto.getOrderDetailsPackageMapperDtosList());
}
```

When no package/contract is found, this order-detail's IP-side save is skipped for that iteration instead of crashing the entire batch (every other order-detail in the same call still gets processed normally). This is a **judgment call worth naming explicitly**: it does not "fix" the not-found case in the sense of making it succeed — it just stops one not-found order-detail from taking down the whole conversion. Whether a not-found service should be handled some other way (e.g. falling back to its original OP-side pricing, or surfacing a warning to the caller) is a business question this fix does not attempt to answer — flagging this rather than silently deciding it. If you want different behavior here, say so before this is applied.

### Fix 1b: stop swallowing the exception silently in both ADT callers

Both `BedAllocationServiceImpl.acceptNursingAcceptanceRequet` and `BedAllocationController.savePatientAdmission` have a bare `catch (Exception e) { }` — a direct instance of the CLAUDE.md rule "no error may be silently swallowed, ever, anywhere." Neither file currently has a `Logger` at all. Minimal fix: add one, log the exception in the catch block, keep the existing return behavior unchanged (still returns the same generic `COMMON_ERROR_MESSAGE` response — this fix is about making the failure *visible*, not changing what the caller receives):

```java
// new field:
private static final Logger logger = Logger.getLogger(BedAllocationServiceImpl.class); // or BedAllocationController.class

// catch block:
} catch (Exception e) {
    logger.error("acceptNursingAcceptanceRequet failed", e);   // or "savePatientAdmission failed"
}
return new Response<>(ERROR, null, COMMON_ERROR_MESSAGE, null, null);
```

Uses `org.apache.log4j.Logger`, matching the prevailing convention already used throughout the rest of the ADT module (confirmed via grep — e.g. `BillNoUtils.java`, `DueForDischargeServiceImpl.java`).

### Fix 2: `addDepositAgainstAdmission` — guard the crash point, matching the file's own existing early-return idiom

```java
if (depositMaster == null) {
    return new Response<>(ERROR, ERROR_CODE, "Failed to save deposit.", null, null);
}
return new Response<>(SUCCESS, SUCCESS_CODE, DEPOSIT_ADDED_MSG + deposit.getDepositNo(), null,
        depositMaster.getDepositMasterId());
```

Mirrors the exact pattern already used a few lines earlier in the same method for the "Please Configure Deposit Number" case (`return new Response<>(ERROR, ERROR_CODE, "...", null, null);`) — same file, same idiom, no new pattern introduced.

---

## What these fixes deliberately do NOT do

- They do not change any tax/rate/self-payable/credit-payable calculation — both fixes are purely about what happens when the *inputs* to those calculations are missing, not the calculations themselves.
- They do not touch `BedAllocationServiceImpl.savePatientAdmission`'s (the deposit-flow caller's) own error handling — out of scope for this proposal, not traced.
- Fix 1a does not attempt to recover or retry the not-found case, only to stop it from crashing the whole batch.
- Fix 1b does not change what either ADT caller returns to its own caller — only that the failure is now logged, not silently discarded.

## Classification, per RULES.md

**Robustness/Reliability** for all four changes — no money calculation touched. Fix 1b is also a direct CLAUDE.md-rule compliance fix (no error may be silently swallowed).

## Verification plan, if approved

1. Apply fixes 1a, 1b (both call sites), and 2.
2. Update the 2 existing characterization tests that currently assert `BillingException`/crash behavior (`contractNotFound_wholeConversionThrows`, `depositSaveFails_crashesInsteadOfReturning`) to assert the new, non-crashing behavior instead.
3. Add 2 new tests confirming the ADT callers now log (not silently swallow) when `convertServicesFromOPToIP` fails.
4. Full `mvn -f legacy/pom.xml clean test` (all 19 modules), confirming no other test regresses.
5. Log the outcome in `docs/BUSINESS_RULE_MANIFEST.md` as a completed Robustness fix once approved and applied; update `BR-PENDING-adt-financial-calls-batch1` to `BR-FIXED-...`.

No code has been changed yet. This document is the proposal only, awaiting owner review before any fix is applied.
