# Review: Global module billing-critical exception-handling fix

*Maker session, 2026-07-05, branch `fix/global-billing-critical-exceptions`.* Fixes the 960 "billing-critical" findings from `docs/AUDIT_Global_exceptions.md` (a prior read-only audit of `legacy/Global`'s 9,000 silent-exception-swallow findings), using the same pattern already approved for the Billing module in `docs/BUSINESS_RULE_MANIFEST.md` BR-006 and every subsequent Billing exception-handling fix (BR-007/009/010/011/013/014/015/016/017).

## Summary

- **Total findings in scope:** 960, across 95 files.
- **Fixed as ROUTINE:** 945 findings, across 91 files (87 files fully fixed, 4 files partially fixed).
- **Held back for owner decision, code untouched:** 15 findings, across 6 files (2 of which are the same files that were partially fixed above) — see the three new decision documents below.
- **Compile result:** `mvn -pl Global -am compile -DskipTests -o` (run from `legacy/`) — **BUILD SUCCESS**, after fixing two mistakes the compiler itself caught during verification (see "Compile verification" below).
- **Business Rule Manifest:** one consolidated entry, `BR-PENDING-global-billing-critical`, added to `docs/BUSINESS_RULE_MANIFEST.md`, classified Behavioral Change, Owner Decision: Pending.

## Method

1. Read `docs/AUDIT_Global_exceptions.md`, extracted all 960 rows marked `billing-critical` (out of the audit's 9,000 total findings).
2. Grouped the 95 affected files into 12 batches (balanced by finding count, ~77–110 findings per batch) and fixed each batch with a dedicated agent working directly against the audit's per-file, per-line finding list — not re-deriving findings from scratch.
3. Each finding was classified individually, before any fix was applied:
   - **ROUTINE** — the catch block, as it stands today, results in a masked/silent ERROR-shaped outcome (empty catch, `printStackTrace`-only, generic masked-error response, `return null`/`false`/`0`) with nothing fabricating a false SUCCESS. Fixed with the standard pattern (below).
   - **NEEDS_OWNER_DECISION** — applying the standard fix would change what counts as success for the caller, or would risk breaking working functionality outright. Left completely untouched; a full decision document written instead (same format as the existing `docs/DECISION_NEEDED_collectPayment.md` / `docs/DECISION_NEEDED_RazorPayment.md`).
4. The standard fix, applied uniformly to every ROUTINE finding: replace the catch body with `logger.error("<methodName> failed", <exceptionVar>); throw new ServiceException(<exceptionVar>.getMessage());` (or `BillingException`, see convention rule below). Both are unchecked `RuntimeException`s that live in the Global module itself and already route to the existing, enabled `BillingExceptionController` (BR-004) — no change was needed or made to that routing.
5. Exception-type convention per file: if a file already used `BillingException` anywhere, all fixes in that file used `BillingException` for consistency; else if it already used `ServiceException`, all fixes used `ServiceException`; else (no prior usage of either), `ServiceException` was used by default.
6. Logger convention: reused an existing class-level `Logger` field where one was already present (2 files: `DocUploadUtility.java`, `AesUtil.java`); added a new one matching the Global module's existing convention (`import org.apache.log4j.Logger; final static Logger logger = Logger.getLogger(<ClassName>.class);`, already used in ~15 other Global files) where none existed (89 files).
7. Java's definite-reachability rule: once a catch block unconditionally `throw`s, any trailing statement after the try/catch that was only reachable via the old non-throwing catch becomes a compile error and was deleted. Where a trailing statement remained genuinely reachable via a separate non-exceptional path (an `if` with no covering `else`, etc.), it was deliberately preserved — matching the same nuance already documented in BR-007/BR-015.
8. A single, final `mvn -pl Global -am compile -DskipTests -o` was run after all fixes, per the task's explicit instruction to compile once — not per-batch.

## Per-file results

| File (relative to `com/param/global/`) | Total findings | Fixed (ROUTINE) | Held (NEEDS_OWNER_DECISION) |
|---|---|---|---|
| `billing/controller/APCodeMasterController.java` | 7 | 7 | — |
| `billing/controller/ARCodeMasterController.java` | 7 | 7 | — |
| `billing/controller/AutoPostingConfigController.java` | 3 | 3 | — |
| `billing/controller/BankBranchMasterController.java` | 7 | 7 | — |
| `billing/controller/ContractConsumptionController.java` | 11 | 11 | — |
| `billing/controller/CreditNoteReasonMasterController.java` | 7 | 7 | — |
| `billing/controller/CurrencyDenominationMasterController.java` | 6 | 6 | — |
| `billing/controller/CurrencyMasterController.java` | 7 | 7 | — |
| `billing/controller/DepositController.java` | 6 | 6 | — |
| `billing/controller/DoctorCategoryMasterController.java` | 7 | 7 | — |
| `billing/controller/DoctorGradeController.java` | 7 | 7 | — |
| `billing/controller/EmployeeCategoryMasterController.java` | 7 | 7 | — |
| `billing/controller/PaymentModeMasterController.java` | 7 | 7 | — |
| `billing/controller/POSMachineDeviceMasterController.java` | 1 | 1 | — |
| `billing/controller/VendorServiceRateConfigController.java` | 6 | 6 | — |
| `billing/dao/APCodeMasterDaoImpl.java` | 18 | 18 | — |
| `billing/dao/ARCodeMasterDaoImpl.java` | 18 | 18 | — |
| `billing/dao/BankBranchMasterDaoImpl.java` | 16 | 16 | — |
| `billing/dao/BillingDaoImpl.java` | 13 | 13 | — |
| `billing/dao/ContractConsumptionDaoImpl.java` | 5 | 5 | — |
| `billing/dao/CreditNoteReasonMasterDaoImpl.java` | 18 | 18 | — |
| `billing/dao/CurrencyDenominationMasterDaoImpl.java` | 16 | 16 | — |
| `billing/dao/CurrencyMasterDaoImpl.java` | 18 | 18 | — |
| `billing/dao/DepositMasterDaoImpl.java` | 6 | 6 | — |
| `billing/dao/DoctorCategoryMasterDaoImpl.java` | 17 | 17 | — |
| `billing/dao/DoctorGradeDaoImpl.java` | 18 | 18 | — |
| `billing/dao/EmployeeCategoryMasterDaoImpl.java` | 11 | 11 | — |
| `billing/dao/PaymentModeMasterDaoImpl.java` | 18 | 18 | — |
| `billing/dao/POSMachineDeviceMasterDaoImpl.java` | 2 | 2 | — |
| `billing/dao/POSMachineResponseMapperDaoImpl.java` | 9 | 9 | — |
| `billing/dao/ServiceBasePriceMasterDaoImpl.java` | 12 | 12 | — |
| `billing/dao/VendorServiceRateConfigDaoImpl.java` | 11 | 11 | — |
| `billing/service/APCodeMasterServiceImpl.java` | 9 | 9 | — |
| `billing/service/ARCodeMasterServiceImpl.java` | 9 | 9 | — |
| `billing/service/BankBranchMasterServiceImpl.java` | 7 | 7 | — |
| `billing/service/BillingServiceImpl.java` | 9 | 9 | — |
| `billing/service/ContractConsumptionForCostEstimationServiceImpl.java` | 1 | 1 | — |
| `billing/service/ContractConsumptionServiceImpl.java` | 10 | 10 | — |
| `billing/service/CreditNoteReasonMasterServiceImpl.java` | 9 | 9 | — |
| `billing/service/CurrencyDenominationMasterServiceImpl.java` | 6 | 6 | — |
| `billing/service/CurrencyMasterServiceImpl.java` | 7 | 7 | — |
| `billing/service/DepositMasterServiceImpl.java` | 2 | 1 | 1 |
| `billing/service/DoctorCategoryMasterServiceImpl.java` | 7 | 7 | — |
| `billing/service/DoctorGradeServiceImpl.java` | 9 | 9 | — |
| `billing/service/EmployeeCategoryMasterServiceImpl.java` | 8 | 8 | — |
| `billing/service/FrenchiseDepositServiceImpl.java` | 1 | 0 | 1 |
| `billing/service/PaymentModeMasterServiceImpl.java` | 7 | 7 | — |
| `billing/service/POSMachineDeviceMasterServiceImpl.java` | 3 | 3 | — |
| `billing/service/VendorServiceRateConfigServiceImpl.java` | 4 | 4 | — |
| `common/AesUtil.java` | 9 | 9 | — |
| `common/DocUploadUtility.java` | 110 | 110 | — |
| `common/JsonCheck.java` | 3 | 3 | — |
| `common/SmsUtility.java` | 22 | 22 | — |
| `controller/PaymentEntitlementController.java` | 8 | 8 | — |
| `controller/UnitApplicationBillingConfigurationController.java` | 11 | 11 | — |
| `dao/AmbulanceChargesDaoImpl.java` | 13 | 13 | — |
| `dao/IntrimBillDaoImpl.java` | 3 | 3 | — |
| `dao/OrderDetailsDiscountDaoImpl.java` | 5 | 5 | — |
| `dao/PackageConsumptionDaoImpl.java` | 1 | 1 | — |
| `dao/PaymentEntitlementMasterDaoImpl.java` | 20 | 20 | — |
| `dao/RazorPayDaoImpl.java` | 10 | 10 | — |
| `dao/TarrifPaymentCategoryDaoImpl.java` | 2 | 2 | — |
| `dao/UnitApplicationBillingConfigurationDaoImpl.java` | 35 | 35 | — |
| `dao/UnitServiceTariffMasterDaoImpl.java` | 27 | 21 | 6 |
| `mpesa/Controller/MpesaMasterController.java` | 7 | 7 | — |
| `mpesa/Dao/CCAvenueLogDaoImpl.java` | 14 | 14 | — |
| `mpesa/Dao/MpesaMasterDaoImpl.java` | 18 | 18 | — |
| `mpesa/Dao/MpesaTransctionDetailsDaoImpl.java` | 5 | 5 | — |
| `mpesa/service/MpesaMasterServiceImpl.java` | 8 | 8 | — |
| `otims/controller/ResourceChargeConfigurationController.java` | 6 | 6 | — |
| `otims/dao/ResourceChargeConfigurationDaoImpl.java` | 9 | 9 | — |
| `otims/service/ResourceChargeConfigurationServiceImpl.java` | 6 | 6 | — |
| `pesapal/dao/PesaPalIntegrationDao.java` | 10 | 10 | — |
| `pesapal/OAuthClient.java` | 1 | 0 | 1 |
| `pesapal/OAuthResponseMessage.java` | 3 | 0 | 3 |
| `pesapal/PesaPalIntegrationController.java` | 2 | 2 | — |
| `pesapal/service/PesaPalIntegrationServiceImpl.java` | 5 | 5 | — |
| `pesapal/URLConnectionResponse.java` | 3 | 0 | 3 |
| `procedure/controller/BillOfMaterialController.java` | 9 | 9 | — |
| `procedure/dao/BillOfMaterialDaoImpl.java` | 22 | 22 | — |
| `service/AmbulanceChargesServiceImpl.java` | 7 | 7 | — |
| `service/ItrimBillServiceImpl.java` | 2 | 2 | — |
| `service/OrderDetailsDiscountServiceImpl.java` | 2 | 2 | — |
| `service/OrderMasterServiceImpl.java` | 22 | 22 | — |
| `service/PaymentEntitlementMasterServiceImpl.java` | 8 | 8 | — |
| `service/RazorPayServiceImpl.java` | 12 | 12 | — |
| `service/UnitApplicationBillingConfigurationServiceImpl.java` | 13 | 13 | — |
| `service/UnitServiceTariffMasterServiceImpl.java` | 15 | 15 | — |
| `unit/controller/DiscountTypeMasterController.java` | 6 | 6 | — |
| `unit/controller/MpesaAppConfigController.java` | 4 | 4 | — |
| `unit/dao/DiscountTypeMasterDaoImpl.java` | 16 | 16 | — |
| `unit/dao/MpesaAppConfigDaoImpl.java` | 4 | 4 | — |
| `unit/service/DiscountTypeMasterServiceImpl.java` | 6 | 6 | — |
| `unit/service/MpesaAppConfigServiceImpl.java` | 4 | 4 | — |
| `utility/SerialNoUtility.java` | 2 | 2 | — |

**Totals: 960 findings, 945 fixed, 15 held. 91 of 95 files touched; 4 files (`FrenchiseDepositServiceImpl.java`, `OAuthClient.java`, `OAuthResponseMessage.java`, `URLConnectionResponse.java`) left entirely untouched pending their decision doc.**

## Held-back findings — three new decision documents

- **`docs/DECISION_NEEDED_UnitServiceTariffMasterDaoImpl.md`** (6 findings, 3 methods in `dao/UnitServiceTariffMasterDaoImpl.java`) — a genuine fabricated-SUCCESS-on-failure pattern: an empty catch falls through to a shared trailing `return new Response(SUCCESS, null, null, null, null)` outside the try/catch, indistinguishable from the genuine "found nothing" success case. Same class of issue as the held-back findings in BR-008 (`collectPayment`) and BR-012 (`RazorPaymentController` Group A), though these three are read-only master-data lookups, not payment/money-movement code.
- **`docs/DECISION_NEEDED_LoyaltyCardValidationGoto.md`** (2 findings, `billing/service/DepositMasterServiceImpl.java` and `billing/service/FrenchiseDepositServiceImpl.java`) — both use an identical self-manufactured `throw new Exception(); catch(Exception e){ return new Response(ERROR, ..., "Amount should be less than or equals to card amount", ...); }` construct as a goto-style device to return a specific, already-correct loyalty-card validation message. The standard fix would silently replace that message with `null` (since the manufactured exception has no message) and change the response from a normal `Response` object to an HTTP 500.
- **`docs/DECISION_NEEDED_VendoredOAuthLibraryCatches.md`** (7 findings, 3 files: `pesapal/OAuthClient.java`, `pesapal/OAuthResponseMessage.java`, `pesapal/URLConnectionResponse.java`) — vendored, unmodified-looking third-party `net.oauth` library code (Apache-licensed, Netflix copyright) used by the Pesapal payment-gateway integration. The empty/`null`-returning catches here are load-bearing (a documented platform-quirk workaround, the library's own "no body" contract, best-effort diagnostic enrichment that must not derail an already-correct `throw`) — applying the standard fix would very likely break working gateway-response handling rather than surface a real hidden bug. A technical-correctness risk, not a business-rule question, but still left for an explicit decision per CLAUDE.md's "no silent swallow" rule.

## Patterns and anomalies worth flagging

- **Two genuine mistakes were made and caught by the compiler, not by inspection** — see "Compile verification" below. This is the intended safety net for this class of mechanical fix (Java's definite-reachability and missing-return checks catch exactly the two ways this fix can go wrong), and it worked as designed.
- **8 files already had an established `BillingException` convention** (used it for consistency instead of the default `ServiceException`): `billing/dao/BillingDaoImpl.java`, `billing/dao/ContractConsumptionDaoImpl.java` (partially — this file's own convention is actually `ServiceException`, already used in ~15 other methods; only `BillingException` files are `BillingDaoImpl.java`, `dao/UnitServiceTariffMasterDaoImpl.java`'s sibling `billing/dao/DepositMasterDaoImpl.java`, `billing/service/BillingServiceImpl.java`, `billing/controller/DepositController.java`, `billing/controller/POSMachineDeviceMasterController.java`). All other 83 fixed files defaulted to `ServiceException`.
- **A pre-existing, out-of-scope control-flow bug was discovered (not fixed) in `billing/service/ContractConsumptionServiceImpl.java`**: two of its methods (`consumeServiceFromContract`, `consumePackageFromContract` — NOT `consumeServiceFromContractPackage`, which the compile-verification step confirmed always returns unconditionally) appear to have a code path where, if a contract fully covers a service's cost on the first pass, neither of two conditional success-return branches executes, and the try block falls through to the generic `ERROR`/`COMMON_ERROR_MESSAGE` fallback even though nothing failed and the DB writes already succeeded — a "fabricated failure on true success," the mirror image of the fabricated-success pattern in the held-back findings above. This lives entirely in the try body's business logic, not in any of this fix's 960 exception-handling findings, so it was deliberately left untouched; flagging it here for a future, separate investigation given the project's #1 billing-accuracy priority.
- **A pre-existing bug was found and left untouched in `dao/RazorPayDaoImpl.java`**: `updatePaymentLinkLog` references a field `sessionFactory` that is not declared in the class (the declared field is `sf`) — unrelated to exception handling, out of this fix's scope.
- **A pre-existing bug was found and left untouched in `dao/IntrimBillDaoImpl.java`**: `inActivePreviosRecords`'s try block never returns on its own success path, so its trailing `return new Response(ERROR, ...)` is reached both by the old catch-fallthrough (now fixed) and by ordinary successful completion — meaning this method reports ERROR even when it succeeds. The trailing return was correctly left in place (deleting it would have broken the compile, since it's genuinely still reachable), but the underlying "always-ERROR" bug itself is out of scope for an exception-handling fix.

## Compile verification

Ran `mvn -pl Global -am compile -DskipTests -o` (from `legacy/`) once, per the task's instruction. The first run failed with two errors, both caused by the fix's own dead-code-reachability judgment being wrong in two specific spots (out of ~130 similar judgment calls made across the 91 files) — not by the fix pattern itself:

1. `common/DocUploadUtility.java:2601` — "missing return statement" in `uploadMultilingualCoversheet`. This method's first catch (`AmazonServiceException`) only logs and does not throw (it was not itself a finding — it already logged, matching the audit's "properly log" exclusion), so a trailing `return null;` after the full catch chain was still required; it had been incorrectly deleted on the assumption that all three catches now throw. Restored.
2. `billing/service/ContractConsumptionServiceImpl.java:2628` — "unreachable statement" in `consumeServiceFromContractPackage`. This method's try block, unlike its two similarly-shaped siblings (`consumeServiceFromContract`, `consumePackageFromContract`), ends with its own unconditional `return new Response(SUCCESS, ...)` outside any conditional branch, so the trailing `return new Response(ERROR, ...)` after the catch was genuinely unreachable and had been incorrectly preserved. Deleted.

Both were fixed directly (not re-delegated), and every other file's dead-code judgment in the same category was independently re-checked by hand around both fixes before the second compile run. Second and final run: **BUILD SUCCESS** (2,669 source files compiled, only pre-existing unrelated deprecation/unchecked warnings, zero errors).
