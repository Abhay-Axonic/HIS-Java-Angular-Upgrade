# Decision Needed: 6 exception-handling findings in UnitServiceTariffMasterDaoImpl.java (fabricated SUCCESS on failure)

**Status: RESOLVED 2026-07-05 — Owner approved Option A.** The standard fix (`logger.error(...); throw new ServiceException(...)`) was applied to all 6 findings across the 3 methods (`getServiceListBySubSpecialityId`, `getServiceListBySpecialityIdSubSpecialityList`, `getServiceListBySubSpecialityIdNotByTariff`), matching the pattern already used for the other 21 findings in this file. The now-unreachable trailing `return new Response(SUCCESS, null, null, null, null);` in each method was removed, same mechanical consequence as the rest of this fix cycle. See `docs/BUSINESS_RULE_MANIFEST.md` (`BR-PENDING-global-billing-critical`) for the updated consolidated entry.

---

**Status: OPEN — awaiting owner decision.** No code has been touched for these 6 findings. They were surfaced during the Global-module billing-critical exception-handling fix (`docs/AUDIT_Global_exceptions.md`, `docs/REVIEW_global-billing-critical.md`) and deliberately excluded from that fix's routine batch, matching the same held-back treatment already used for `docs/DECISION_NEEDED_collectPayment.md` and `docs/DECISION_NEEDED_RazorPayment.md`.

---

**Location:** `legacy/Global/src/main/java/com/param/global/dao/UnitServiceTariffMasterDaoImpl.java` — 3 methods, 2 findings each:

- `getServiceListBySubSpecialityId` — lines 433–437
- `getServiceListBySpecialityIdSubSpecialityList` — lines 491–495 (same shape, further down the file)
- `getServiceListBySubSpecialityIdNotByTariff` — same shape, further down the file

All three are read-only master-data/tariff lookup queries (list services available for a sub-speciality/tariff), not money-movement or payment code.

## What happens today

All three methods share an identical shape. Using `getServiceListBySubSpecialityId` as the concrete example (lines 420–438):

```java
try{
    List<UnitServiceTariffMasterDto> unitServiceTariffMasterDtosList = sessionFactory.getCurrentSession().getNamedQuery("GET_SERVICELIST_BY_SUBSPECIALITYID")
            .setParameter(...)
            ...
            .list();
    return new Response(SUCCESS, null, null, unitServiceTariffMasterDtosList, null);
}catch (HibernateException he) {
}catch (Exception e) {

    }
return new Response(SUCCESS, null, null, null, null);
```

Both catch blocks are completely empty. If the query throws for any reason (a bad parameter, a DB timeout, a schema mismatch), execution falls through both empty catches to a **shared trailing statement outside the try/catch**: `return new Response(SUCCESS, null, null, null, null);`

In other words: **on exception, the method returns a `Response` object with `status = SUCCESS` and a `null` data list** — not an ERROR response. This is functionally indistinguishable from the return on line 432, which is the genuine "query ran fine, found zero matching services" case. A caller checking `response.getStatus() == SUCCESS` cannot tell "nothing matched" apart from "the query blew up and nothing ran."

## What the standard fix (used everywhere else in this file and file group) would change

The standard fix already applied to every other finding in this file replaces the empty catches with `logger.error("<method> failed", he/e); throw new ServiceException(he/e.getMessage());`, which routes the failure to the centralized `BillingExceptionController` (BR-004) and returns a real, logged HTTP 500.

Applied here, that would mean: a failure of these 3 master-data/tariff lookups would flip from HTTP 200 with a fabricated `SUCCESS` body to HTTP 500. That is a direct SUCCESS→FAILURE flip in what the caller observes — exactly the class of change CLAUDE.md's Behavioral Change definition and RULES.md's owner sign-off requirement are meant to catch, even though the "success" being replaced was never a true success.

## Why this needs owner judgment rather than a mechanical fix

1. **The current behavior actively lies about failure**, unlike the majority of this fix cycle's ROUTINE findings (which only replaced silence with a masked-but-honest ERROR response). Any UI screen or downstream logic built on "if SUCCESS, render the list" will render an empty list and behave as if the tariff/service catalog is genuinely empty — for example, a service-selection dropdown appearing blank — with zero indication anything went wrong.
2. **It is not obviously higher blast-radius than the other 71 findings already fixed in this same file** (this file's other 21 findings all got the standard fix and compiled clean) — these 3 are flagged specifically because they are the only ones in the file whose failure path is indistinguishable from success, not because they touch money.
3. Per CLAUDE.md: "Any change in observable behavior... however small... must be logged... for the owner to approve. It is not a 'refactor' unless output is provably byte-identical." Turning today's `SUCCESS`-shaped failure into an `ERROR`-shaped one is a Behavioral Change by that definition, regardless of the direction of the fix.

## What we need from you

A decision on one of the following (or your own alternative) before any code is touched:

- **Option A:** Apply the standard fix as-is (empty catch → real HTTP 500) for all 6 findings across the 3 methods, treating any failure in these lookups as a genuine, visible error going forward — same as every other finding in this file.
- **Option B:** Keep the response always `SUCCESS`-shaped on this exception path (no visible change to callers/UI), but add real logging (`logger.error`) so the failure is at least visible in the centralized log without changing what the caller receives — the same asymmetric approach already used in BR-008 (`collectPayment`) and BR-012 (`RazorPaymentController` Group A).
- **Option C:** Something else — for example, change the fabricated `SUCCESS` to an honest `ERROR` response (not a thrown exception) plus logging, keeping the response shape (`Response` object, not an HTTP 500/exception) the frontend already expects for this endpoint family, while still making the failure visibly distinguishable from a real empty result.

No code will be touched here until you choose.
