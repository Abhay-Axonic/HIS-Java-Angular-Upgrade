# Review: OTIMS module exception-handling fix

*2026-07-06. Applies the pattern already approved for Billing (BR-006 et seq.), Global (BR-021), ADT (BR-022), BloodBank (BR-023), CSSDMaster (BR-024), Pharmacy (BR-025), and DietAndKitchen (BR-026) to all 248 findings from `docs/AUDIT_OTIMS_exceptions.md`.*

## Summary

- **Total files touched:** 50 (48 fixed by 6 parallel batch sessions + 2 billing-critical files fixed directly by the lead session)
- **Total findings fixed:** 248 of 248 (100%) — **0 NEEDS_OWNER_DECISION, 0 held back**
- **Exception type used:** `ServiceException` (`com.param.global.common.ServiceException`) for 47 of the 50 files; `BillingException` (`com.param.global.billing.controller.BillingException`) for `OTReservationDaoImpl.java` only, matching that file's own pre-existing convention (it already threw `BillingException` from 2 non-finding methods before this fix)
- **Logger convention:** `org.apache.log4j.Logger`, class-level `private static final Logger logger = Logger.getLogger(<ClassName>.class)`, added to all 50 files — OTIMS had 0/92 files with a real logger before this fix (confirmed by the audit)
- **Compile result:** `mvn -pl OTIMS -am compile -DskipTests -o` (run from `legacy/`) — **BUILD SUCCESS on the first pass** (Entity → Global 2,669 files → OTIMS 92 files, 0 errors)
- **Verification:** `logger.error` call count across `legacy/OTIMS` = 248, exactly matching the finding count 1:1. `throw new ServiceException` = 230, `throw new BillingException` = 22 (18 new fixes in `OTReservationDaoImpl.java` + 4 pre-existing non-finding rethrows in the same file, unchanged) — 230 + 18 = 248.

## Pre-fix red-flag sweep (before any code was touched)

Per the task's explicit instruction to scrutinize the 2 billing-critical files and not assume ROUTINE by default, the lead session personally read both billing-critical files in full and ran a module-wide automated sweep before classifying anything:

- **`throw new Exception` (self-manufactured-exception-as-goto, the pattern behind Global's `LoyaltyCardValidationGoto` decision):** 0 matches anywhere in the module.
- **`SUCCESS` token appearing inside any catch block's body** (the fabricated-success-on-failure signal behind BR-008/BR-012/Global's `UnitServiceTariffMasterDaoImpl`/Pharmacy's `SalesReturnService`): checked via a brace-matching structural parse of every catch block in every `.java` file under the module (not a naive line-proximity grep, which produced false positives from nearby `return SUCCESS` statements placed *before* the catch) — **0 matches**.
- **Null-probe-as-null-check idiom** (the pattern behind Pharmacy's `NullProbeIdiom` decision — `flag`/`checkDoctor`-style try/catch used to test a lazy-loaded association rather than as real error handling): 0 matches.
- **Vendored third-party library code** (`net.oauth`, `org.apache.oltu`, the pattern behind Global's `VendoredOAuthLibraryCatches` decision): 0 matches — OTIMS has no third-party payment/OAuth integration code of its own.

Conclusion: **all 248 findings classified ROUTINE**, matching ADT/BloodBank/CSSDMaster/DietAndKitchen's precedent of 0 held-back findings, and unlike Global's 15 and Pharmacy's 15.

## The 2 billing-critical files — direct manual review

Per the task's specific instruction, both files were read start-to-finish by the lead session (not delegated) before any fix was applied, since this is the first module since Pharmacy with a nonzero billing-critical count, and the audit flagged both files as reaching billing-critical status only through the forward-import criterion (no filename/path keyword match).

- **`OTReservationDaoImpl.java` (18 findings):** every one of its 14 methods with a finding follows the same shape — the try block's own success/failure logic (either an unconditional `return SUCCESS`, or an explicit `if(count>0) SUCCESS else ERROR`) is entirely independent of the catch; every catch, empty before this fix, falls through to a hardcoded `ERROR` response. No catch body anywhere in the file returns a fabricated `SUCCESS` after a downstream failure. The file already threw `BillingException` from 2 non-finding methods (`saveProcedureReq`, `saveProcedureOrder`) — the fix matched that existing convention for consistency rather than defaulting to `ServiceException`.
- **`ProcedureTemplateServiceImpl.java` (5 findings):** the billing-relevant method, `getProcedureConfigureOnOTBillingCompletion` (imports `IContractConsumptionService`, calls `consumeServiceFromContract`/`getSerivceRateGlobal` to compute/consume a patient's OT-procedure contract tariff), was checked with particular care since it performs an actual billing side-effect mid-method. Its catch, empty before this fix, wraps the *entire* method body — including the contract-consumption call — and falls through to a hardcoded `ERROR` response on any exception, same as the other 4 findings in this file. **Observation flagged for the owner, not a fix-blocking issue:** if an exception occurs *after* `consumeServiceFromContract` has already succeeded (e.g., later in the same method's inventory-item loop), the method returns/throws an error even though a contract consumption already happened — a pre-existing partial-transaction risk in the method's own design, present identically before and after this fix. This fix only changes how the *already-existing* error path is surfaced (silently masked → logged and thrown); it does not change, worsen, or fix this pre-existing non-atomicity. No pre-existing `BillingException`/`ServiceException` convention existed in this file, so `ServiceException` (the task's instructed default) was used.

## Trailing unreachable-return cleanup

Per Java's definite-reachability rule (same mechanical consequence as every prior fix in this series, BR-006 through BR-026): once a catch block unconditionally throws, a trailing `return new Response(ERROR, ...)` statement placed after the try/catch that was only reachable via the old silent-fallthrough path becomes unreachable and must be deleted, or the module fails to compile. Every deletion was checked against the enclosing method's own control flow before compiling (not fixed up after a failed build) — confirmed by the from-scratch `mvn -pl OTIMS -am compile -DskipTests -o` succeeding on its first attempt.

**7 trailing returns were deliberately preserved** (not deleted) because they are reachable via a genuinely separate non-exceptional fall-through path (an `if` with no matching `else`, so the method can reach that line even when no exception occurs at all):

- `OTIMSDashboardServiceImpl.saveProcedureSchedulingDetails`
- `OTIMSDashboardServiceImpl.rescheduleProcedure`
- `OTIMSDashboardServiceImpl.getSurgeriesOfTheDayList`
- `OTIMSDashboardServiceImpl.getPreOparetiveDeta`
- `ChecklistMasterServiceImpl.getAllChecklistMasterList`
- `ChecklistItemMasterServiceImpl.getChecklistItemMasterList`
- `OTReservationServiceImpl.cancelOTReservation`

## Non-findings correctly left untouched

- `OTReservationServiceImpl.java`: 2 pre-existing catches (`getOTReservationListByPatientId`, `getOTReservationListByPatientIdForDeposite`, ~lines 504-519) already properly rethrow — not modified.
- `ProcedureTemplateDaoImpl.java`: 4 catches already do `throw e;` — not modified.
- `OtMealDaoImpl.java`, `OtDishDaoImpl.java`, `IntraOperativeDetailsDaoImpl.java`, `PostOperativeCareDaoImpl.java`, `PostOperativeDetailsDaoImpl.java`: catches on methods outside the audited findings list that already properly rethrow — not modified.
- Dead/commented-out `/* ... */`-fenced `addSample` scaffolding catches in `ChecklistItemMasterController.java`, `OTIMSDashboardController.java`, `OTReservationController.java` — left untouched, matching the audit's own exclusion of this dead code.
- `PreOperativeDetailsDaoImpl.java`'s 2 `System.out.println(exception)`-only catches — converted to the standard fix like any other finding (they were on the findings list); no other dead code disturbed.

## Patterns worth flagging to the owner (not fix-blocking)

- **`OTReservationDaoImpl.java` uses `BillingException` while the rest of the module uses `ServiceException`.** This is intentional (matching the file's own established convention, same reasoning as Global's 8 `BillingException` files in BR-021), not an inconsistency needing correction.
- **`ProcedureTemplateServiceImpl.getProcedureConfigureOnOTBillingCompletion`'s pre-existing partial-transaction risk** (contract consumption can succeed before a later, unrelated step throws) — see the billing-critical review section above. Not introduced or changed by this fix; flagged for future architectural attention (e.g., wrapping the contract-consumption call and the rest of the method in a single transactional boundary) outside this fix's exception-routing-only scope.
