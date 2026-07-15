# Review: Pharmacy module exception-handling fix

*Maker session, 2026-07-06. Same pattern already approved for Billing (BR-006 et seq.), Global (BR-021), ADT (BR-022), BloodBank (BR-023), and CSSDMaster (BR-024). Fixes 711 of the 726 findings from `docs/AUDIT_Pharmacy_exceptions.md`.*

## Scope

`docs/AUDIT_Pharmacy_exceptions.md` catalogued 773 parsed catch blocks across 758 `.java` files under `legacy/Pharmacy`, of which 726 were findings (711 silent-swallow, 4 status-masks-failure `return null`, 11 printStackTrace-only "other") across 134 files, and 47 properly excluded (26 rethrow, 21 already logged via a real logger). 99 of the 726 findings (13.6%) were classified billing-critical — the highest billing-critical share of any module audited in this series, since (unlike ADT/BloodBank/CSSDMaster) Pharmacy has its own walk-in/OP sale and bill-list flows and 23 files directly import `com.param.billing.*`/`com.param.global.billing.*`.

## Classification pass (required before touching any code)

Per the task's explicit instruction, every one of the 99 billing-critical findings was individually read in source — not just pattern-matched — before any fix was applied, with particular attention to the two files the task named directly (`WalkInSalesController.java`, 25 findings; `BillListController.java`, 8 findings) plus every other billing-critical file. A full-module grep for related red-flag patterns was also run to catch anything outside the billing-critical set that the same risk applies to:

- **Fabricated success on downstream failure** (the `collectPayment`/`RazorPayment`/`UnitServiceTariffMasterDaoImpl` pattern — a catch block returns a SUCCESS-labeled response despite a real failure): a full read of all 99 billing-critical catch bodies (`WalkInSalesController.java` and `BillListController.java` in full detail; the remaining 19 billing-critical files via a targeted extraction of each finding's catch block and surrounding method) found **zero** instances of a catch returning or leaving in place an explicit `SUCCESS`-status response. The closest analogue found was **not** a fabricated success message but a **blank, unset-status `CommonResponse`** returned unchanged on exception in two methods of `SalesReturnService.java` (`cancelsalesReturnById`, `updateSalesReturnById`) — held back, see below.
- **Self-manufactured-exception-as-goto pattern** (the Global-module `LoyaltyCardValidationGoto` shape, `try { throw new Exception(); } catch (Exception e) { return X; }` used as control flow, not error handling): `grep -rn "throw new Exception()"` found **1 match** in the module, in `BillListService.java:723` — held back, see below (same shape as the already-resolved BR-021 precedent, recurring independently in this module).
- **"Null-probe" idiom** — a pattern not seen in any prior module in this series: a `try` block that does nothing but read a lazy-loaded association purely to test whether it throws (e.g. `issue.getDoctor().getFirstName()`), with the catch setting a boolean flag used for legitimate fallback control flow rather than signaling an error. Found via `grep -rn "checkDoctor = true"` (11 hits) plus a boolean-flag idiom grep (1 more, using variable name `flag`) — **12 total matches across 8 files** (5 billing-critical, 7 peripheral). Applying the standard fix here would not surface a hidden bug; it would introduce a new one (a common, benign data state — e.g. no doctor assigned yet — would start throwing HTTP 500 on live save paths). Held back, see below.
- **Vendored third-party library code** (the Pesapal `net.oauth` signal from Global's fix cycle): no vendored integration code exists in this module. **Zero matches.**
- **Pre-existing `BillingException`/`ServiceException` convention**: `grep -rl "BillingException"` found 8 files declaring `throws BillingException, Exception` on some method signatures, but `grep -rn "throw new BillingException"` across the entire module found **zero** actual throw sites — this declared-but-never-thrown pattern was judged vestigial, not an active convention (unlike Billing's `BillingMasterServiceImpl.java`, where the equivalent pattern was actively used ~29 times and set the file's real convention). `grep -rl "ServiceException"` found zero pre-existing hits. Per this task's instructed default, `ServiceException` was used throughout the module, matching Global/ADT/BloodBank/CSSDMaster's precedent where no real convention existed.

**Result: 711 of 726 findings classified ROUTINE and fixed. 15 findings classified NEEDS_OWNER_DECISION and left completely untouched**, written up as 3 separate decision documents (the highest held-back count of any module in this series after Global's 15 — matching the task's warning that Pharmacy's billing-critical share warranted the most scrutiny yet):

1. **`docs/DECISION_NEEDED_Pharmacy_NullProbeIdiom.md`** — 12 findings across 8 files (`PatientConsumableIssueService.java` ×5, `ReturnAcceptanceMedicationService.java` ×1, `CrashCartApprovalService.java` ×1, `BillListService.java` ×1, `PatientConsumableIssueListService.java` ×1, `PatientPendingIssuesRejectionService.java` ×1, `PendingIssuesAcceptanceConsumableService.java` ×1, `PatientIndentService.java` ×1).
2. **`docs/DECISION_NEEDED_BillListService_LoyaltyGoto.md`** — 1 finding (`BillListService.java:724`), the self-manufactured-exception-as-goto pattern, directly analogous to BR-021's already-resolved `LoyaltyCardValidationGoto`.
3. **`docs/DECISION_NEEDED_SalesReturnService_BlankResponse.md`** — 2 findings (`SalesReturnService.java:1071` and `:1211`), the blank-`CommonResponse`-masks-failure risk on a billing-critical sales-return cancel/update path.

As a direct consequence, `PatientConsumableIssueService.java` and `BillListService.java` each end up with **zero** findings fixed in this cycle — every audited finding in both files is held back for owner decision.

## Fix applied

For all 711 ROUTINE findings across 128 of the 134 files with findings (the other 6 — `PatientConsumableIssueService.java`, `ReturnAcceptanceMedicationService.java`, `CrashCartApprovalService.java`, `PatientConsumableIssueListService.java`, `PatientIndentService.java`, `BillListService.java` — have every one of their findings held back, so were left completely untouched):

- Every silently-swallowed catch body was replaced with:
  ```java
  logger.error(<excVar>.getMessage(), <excVar>);
  throw new ServiceException(<excVar>.getMessage());
  ```
  using each catch clause's actual declared exception variable name (including non-standard names already present in the source, e.g. `CustomeRespone` for `CustomeException` in several controllers, and `he` for `HibernateException`).
- `import com.param.global.common.ServiceException;` was added to every touched file that lacked it.
- A class-level `org.apache.log4j.Logger` field was added to 122 of the 128 touched files (Pharmacy had only 6 files with a real logger before this fix, per the audit). The other 6 already had one and reused their existing field name instead of adding a duplicate: `logger` in `DepartmentStoreMapperDaoImpl.java`, `GenericScheduleMasterrDaoImpl.java`, `ScheduleMasterDaoImpl.java`, `ItemDao.java`, and `DepartmentStoreMapperServiceImpl.java`; `log` in `ItemService.java`.
- `ServiceException` is an unchecked `RuntimeException` already routed to the approved, enabled `BillingExceptionController` (BR-004, Global module) — no change to that routing was needed or made. Pharmacy already depends on the Global module and on `log4j` directly (confirmed in `legacy/Pharmacy/pom.xml`).

### Execution method

Given the scale (128 files, 711 findings), the fix was applied by 11 parallel Maker sub-sessions, each assigned a disjoint, pre-computed list of exact (file, line) pairs to fix — derived directly from the audit's findings table with the 15 held-back (file, line) pairs already excluded — plus explicit instructions to leave every other catch block in those files (including the held-back ones, for the 3 files that mix routine and held-back findings: `SalesReturnService.java`, `PatientPendingIssuesRejectionService.java`, `PendingIssuesAcceptanceConsumableService.java`) completely untouched. Each sub-session reported its own per-file reachability decisions, which were spot-checked against `git diff` after all 11 completed.

## Mechanical consequence: unreachable trailing returns

Same mechanical consequence as every prior fix in this series: once every catch clause in a method unconditionally throws, any trailing `return` statement placed after the try/catch that was previously reached only via the silent-swallow fall-through becomes unreachable dead code under Java's definite-reachability rule.

Unlike BloodBank's and CSSDMaster's iterative compile-driven deletion (given their larger uniform scale), this cycle's 11 parallel sub-sessions each performed **hand-derived, per-method reachability analysis** at fix time — matching the smaller-batch precedent set by BR-006/BR-007/BR-009 through BR-017/BR-022. Each sub-session was instructed to delete a trailing statement only when it was genuinely unreachable as a direct result of its own edits, and to leave it in place when a separate non-exceptional fall-through path existed (e.g., an `if` with no `else` inside the try that can complete normally without returning). Representative preserved cases reported back: `QMSCounterMasterDaoImpl.getQMSCounterMasterById`, `QMSTokenMasterServiceImpl.saveToken`/`updateToken`/`transferToken`, `SalesReturnDao.getSaleIdForNotReturned`, `SalesReturnService.salesReturnGetBillById`/`getEpochTimeUsingPatientId`, `PharmacyTariffDaoImpl.savePharmacyTariff`, `ScheduleMasterController.updateScheduleMaster`/`activeInactiveSchedule`, `DepartmentStoreMapperController`'s 4 `activeInactive*`/`get*ById` methods, `FrequencyDao.inactiveTranslatedFrequency`/`updateTranslatedFrequency`, `CrashCartMedicationAdministrationApprovalDao`, `CrashCartApprovalDao`, `ItemDao.saveItemMaster`, `BillableConsumptionService.saveStoreStockDetails` (line ~688 — the trailing return sits outside the `if (quantity > 0)` guard entirely), and `ReturnBillableConsumptionService.saveStoreStockDetails`.

This hand-derived approach was independently confirmed correct by the compile result below: **the module compiled to BUILD SUCCESS on the first attempt**, with zero "unreachable statement" or "missing return statement" errors — meaning every one of the ~280 trailing-return deletion/preservation decisions made across the 11 sub-sessions was correct before compiling, not fixed up after a failed build (matching the standard set by BR-022's ADT fix).

## Verification

- **`grep -rc "throw new ServiceException"` across `legacy/Pharmacy`**: exactly **711**, matching the ROUTINE finding count 1:1.
- **`grep -rc "logger.error\|log.error"` across `legacy/Pharmacy`**: **732** — 711 newly added + 21 pre-existing (matching the audit's own count of 21 non-finding catches that already logged via a real logger before this fix).
- **`git diff --stat -- legacy/Pharmacy`**: exactly **128 files changed**, matching the number of files with at least one ROUTINE finding.
- **Held-back files independently re-verified untouched after all 11 sub-sessions completed**: `git diff --stat` shows zero diff for `PatientConsumableIssueService.java`, `ReturnAcceptanceMedicationService.java`, `CrashCartApprovalService.java`, `PatientConsumableIssueListService.java`, and `PatientIndentService.java`; `BillListService.java` shows zero `ServiceException` additions; `PatientPendingIssuesRejectionService.java` and `PendingIssuesAcceptanceConsumableService.java` each show exactly 1 `ServiceException` addition (their single routine finding), with their held-back null-probe catch confirmed still present and unmodified by direct read; `SalesReturnService.java`'s lines 1071 and 1211 confirmed still containing their original empty/`printStackTrace`-only catch bodies by direct read after the fix.
- **Compile**: `mvn -pl Pharmacy -am compile -DskipTests -o` (run from `legacy/`) — **BUILD SUCCESS** on the first pass (Entity → Global 2,669 files → Procurement 511 files → RabbitMQ 4 files → Pharmacy 758 files, 0 errors, only pre-existing unrelated warnings — deprecated/removal-marked API usage in `Global`'s `OrderDetailsPayeeMapperDto.java`/`CommonBillingItemsDto.java`, unchecked-operations warnings in `GlobalOrganizationMasterServiceImpl.java` and Pharmacy's own `ProductCategoryService.java`/`AssetCategoryDao.java` — all present before this change and untouched by it).

## Files touched

128 of 134 files with findings (full per-file finding counts in `docs/AUDIT_Pharmacy_exceptions.md`): controllers, DAOs, and services under `legacy/Pharmacy/src/main/java/com/param/pharmacy/`, spanning `inPatient/`, `ip/`, `master/`, `narcotics/`, `nursing/`, `op/`, `qms/`, and `theatreManagement/`. The 6 files with zero routine findings (every finding held back) are listed above and were not touched.

## Outstanding

Same as every prior branch in this series: Maker-Checker review (RULES.md item 6) is still required before this branch merges. The manifest entry uses a provisional ID (`BR-PENDING-pharmacy-exceptions`) per RULES.md item 8 and is marked Owner Decision: Pending. In addition, the 3 decision documents above need the owner's explicit choice before any of the 15 held-back findings can be touched.
