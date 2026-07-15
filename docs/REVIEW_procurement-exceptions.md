# Review: Procurement module exception-handling fix

*Maker session, 2026-07-06. Same pattern already approved for Billing (BR-006 et seq.), Global (BR-021), ADT (BR-022), BloodBank (BR-023), CSSDMaster (BR-024), Pharmacy (BR-025), DietAndKitchen (BR-026), OTIMS (BR-027), Abdm (BR-028), and HIS-WAR (BR-029). Fixes 514 of the 515 findings from `docs/AUDIT_Procurement_exceptions.md`.*

## Scope

`docs/AUDIT_Procurement_exceptions.md` catalogued 561 structurally-parsed catch blocks across 511 `.java` files under `legacy/Procurement`, of which 515 were findings (503 silent-swallow + 10 `other`/printStackTrace-only + 2 status-masks-failure) and 46 properly rethrew (excluded, left untouched). Zero of 511 files imported a real logger. 78 findings (15.1%) were classified billing-critical, concentrated in 8 files — the highest billing-critical proportion of any module in this series since Pharmacy/Billing themselves, driven by two `invoice`/`consumption`-keyword file groups plus two work-order DAOs whose native SQL joins directly into the `billing` PostgreSQL schema.

## Classification pass (required before touching any code)

Every finding was checked for the fabricated-success-on-exception pattern found in Abdm (BR-028, 5 findings) and HIS-WAR (BR-029, 2 findings) — a catch block or fall-through path that returns a hardcoded `SUCCESS` response despite a real downstream failure:

- **Programmatic scan**: a brace-aware Python parser extracted every catch block's body across all 511 files and searched for the `SUCCESS` token anywhere inside it. **Zero matches.**
- **Manual confirmation**: every one of the 8 fixing agents independently re-confirmed this while reading each catch body live during the fix itself (per the instruction to stop and flag immediately if one was seen) — zero fabricated-success patterns encountered.

**Result: 514 of the 515 findings classified ROUTINE.** One finding, however, surfaced a different-shaped but related risk during the fix — see "Held back: batch-continuation risk" below. This differs from Abdm/HIS-WAR (which each had genuine fabricated-success findings requiring an asymmetric fix) and is closer to ADT/BloodBank/CSSDMaster/Pharmacy/DietAndKitchen/OTIMS (uniformly ROUTINE), except for this one additional judgment call.

## Fix applied

For 514 of the 515 findings (79 files — the other 432 files had no catch-block findings, per the audit's own file-level breakdown):

- Every silently-swallowed catch body was replaced with:
  ```java
  logger.error("<methodName> failed", <excVar>);
  throw new ServiceException(<excVar>.getMessage());
  ```
- This module's dominant finding shape is new to this series: **323 findings (63%) across 46 files** are `catch (Exception ex) { return new ResponseEntity<>(new ErrorResponse(code, message, ex), HttpStatus...); }`. `ErrorResponse` (`legacy/Entity/src/main/java/com/param/common/dto/ErrorResponse.java`) captures the exception's message and full stack trace into the HTTP response body via its constructor — richer than a bare hardcoded string, but still never logged server-side and never rethrown, so it was fixed the same way as every other silent-swallow finding: capturing an error in a response body is not equivalent to server-side visibility.
- 10 `printStackTrace()`-only and 1 `System.out.println()`-only catches were fixed the same way.
- 2 "status-masks-failure" catches whose body was exactly `return null;` (`GoodReceiptNoteDao.java:130`, `VendorNegotiationDao.java:192`) were fixed the same way.
- 1 unusual catch in `PatientConsumptionService.java:1096` (`patientConsumptionSave`) did `return e;` — returning the raw exception object, which only compiles because the method's declared return type is `Object`. Fixed the same way as every other finding.
- `ServiceException` was used throughout (not `BillingException`) — grep confirmed zero pre-existing `BillingException`/`ServiceException` usage anywhere in the module, so there was no existing convention to match, consistent with this task's instruction to default to `ServiceException` in that case (same default used by BloodBank/CSSDMaster/OTIMS).
- A class-level `org.apache.log4j.Logger` field (`final static Logger logger = Logger.getLogger(<ClassName>.class);`) and its import were added to all 79 touched files — Procurement had zero files with a real logger before this fix. `com.param.global.common.ServiceException` is already reachable via Procurement's existing dependency on the `Global` module (`Procurement/pom.xml`) — no dependency changes were needed.
- `ServiceException` is an unchecked `RuntimeException` already routed to the approved, enabled `BillingExceptionController`/`GlobalExceptionHandler` (BR-004, Global module) — no change to that routing was needed or made.

## Two files left structurally untouched: raw SQL into the billing schema

`NewWorkOrderCreationDaoImpl.java` (40 findings, the largest single-file count in this audit) and `ServiceWorkOrderCreationDaoImpl.java` (6 findings) both contain native/raw SQL (`createSQLQuery`) joining directly into the `billing` PostgreSQL schema (`billing.m_billing_master`, `billing.t_bill_payee_details`, `billing.t_billing_details`) to resolve a work order's linked bill — an architecturally interesting cross-module SQL coupling. Per the task's explicit scope, only the catch/logging pattern was fixed in both files; the underlying SQL coupling itself was deliberately left untouched, as restructuring it is a separate architectural question outside an exception-visibility-only fix.

## Held back: batch-continuation risk in `LoanInOutService.java`

`approveLoanInOut`'s single finding (an empty catch inside a nested per-item loop) does not literally return a hardcoded `SUCCESS`, but has the same practical effect: the method unconditionally marks each item's status as approved and persists it via `loanInOutDao.updateloanInOut(...)` regardless of whether that item's batch/asset-tracking insert actually succeeded, then returns a blanket "approved successfully" message for the whole request. Converting the catch to `throw` (the standard fix) would instead abort the entire `@Transactional` method the moment one item fails, rolling back every other item's genuinely-successful approval in the same request — the same batch-continuation regression already identified and fixed once in this project for `legacy/HIS-WAR` (commit `6ca3732d`, `docs/DECISION_NEEDED_HISWAR_BatchContinuation.md`).

This was **not** given the standard fix. An interim safe default was applied instead: `logger.error("approveLoanInOut failed", e)` was added, but no `throw` — the method's existing swallow-and-continue behavior (mark approved regardless, continue the loop, commit, return blanket success) is otherwise completely unchanged. This required no owner sign-off, since it is strictly no worse than current production behavior (adds server-side visibility for the first time, changes nothing else). Full reasoning and the owner's remaining options are in `docs/DECISION_NEEDED_LoanInOutService_ApproveBatchContinuation.md`.

## Other judgment calls worth flagging

- **`GoodReceiptNoteDao.java`'s 2 `printStackTrace()`-only findings** (`updateGrnNetAmt`) also called `tx.rollback()` inside the catch. The rollback call was deliberately preserved (not deleted) alongside the new logger/throw — dropping it would have left an open transaction before the method's `finally`-block `session.close()`, a correctness risk beyond this fix's logging-only scope.
- **`Response.java`'s 1 finding sits inside a `toString()` override** (the module's shared response DTO). Previously it silently fell back to `Object.toString()` on Jackson serialization failure; it now throws `ServiceException` instead of ever degrading gracefully. This is a materially different blast radius than the other 513 fixed findings (all in request-handling paths, not a `toString()` override) — any caller doing string concatenation or logging on a `Response` object would now get an exception instead of a degraded-but-safe string. Flagged here for the owner's awareness; the mechanical fix itself was applied per the pre-approved pattern (not held back, since it doesn't fabricate a success or mask a real failure — just changes a fallback into a throw, same as every other finding).

## Mechanical consequence: unreachable trailing returns

Same mechanical consequence as every prior fix in this series: once every catch clause in a method unconditionally throws, any trailing `return` statement placed after the try/catch block that was previously reached only via the silent-swallow fall-through becomes unreachable dead code under Java's definite-reachability rule. Unlike BloodBank's compile-driven bulk automation (94 files, ~270 sites), this fix used per-method manual reachability analysis across all 79 files — matching the smaller-module precedent (Billing's BR-006 through BR-018) rather than BloodBank's scale-driven automation, since 79 files split across 8 parallel batches was tractable at that granularity.

A handful of trailing returns were confirmed genuinely reachable via a separate non-exceptional path (not just the old catch's fall-through) and were deliberately kept unchanged:
- `NewWorkOrderCreationDaoImpl.getCreationApprovalDetailsbyIdEdit`: an `if(list.size()>0){...return...;}` with no `else` — falls through to the trailing `return new Response(ERROR,...)` on a genuine `list.size()==0` path, independent of any exception.
- `ConsignmentStockPOInvoiceServicesImpl.updateConsignmentStockPOInvoice`: the try block itself has no internal return, so the trailing `return response;` is reached via the normal, non-exceptional completion path.
- `ConsignmentStockConversion.saveStoreStockDetails`: the trailing `return batchId;` is reachable via genuine non-exceptional paths (`conversionQuantity <= 0`, `batch == null`, or batch not a consignment), not just the old catch fall-through.
- `ConsignmentStockReturnService.saveConsignmentStockReturnDetails`: same shape as `updateConsignmentStockPOInvoice` — try block has no internal return, so the trailing `return response;` outside the try/catch is genuinely reached on the normal path.
- `PhysicalInspectionDao`'s two nested `updateStockTransaction`/`updateStockTransactionForBonusBatch` methods: an inner `catch(HibernateException he)` already rethrows (left untouched, not a finding) while the inner and outer `catch(Exception e)` were both empty findings and fixed independently.

All other trailing returns whose only path was the old catch's silent fall-through were deleted as unreachable dead code, method by method, across all 79 files.

## Verification

- **Files changed**: `git status --porcelain` reports exactly 79 modified files under `legacy/Procurement`, zero untracked/new files — matching the audit's 79-file scope exactly.
- **Added-line count cross-check** (`git diff` over `legacy/Procurement`): 515 lines containing `logger.error(` were added (matching the finding count 1:1 — including the 1 held-back finding, which got logging but no throw) and 514 lines containing `throw new ServiceException` were added (515 findings minus the 1 held-back finding).
- **Structural parser cross-check**: an independent brace-aware Python parser found 579 raw catch blocks (matching the audit's own raw `catch\s*(` grep count exactly) both before and after the fix. Of these, 10 remain with neither a log call nor a throw after the fix — all 10 were individually confirmed to be pre-existing dead/commented-out code, matching the audit's documented 12-file, 18-raw-occurrence dead-code gap (the discrepancy between 10 and 18 is because some commented-out catch bodies happen to contain the literal text `throw` inside their comments, a false negative in this crude text-only check, not a real issue — the code is unreachable comment text either way).
- **`grep -rn "printStackTrace()"` / `System.out.println` in the module**: zero remaining hits.
- **Compile**: `mvn -pl Procurement -am compile -DskipTests -o` (run from `legacy/`) — **BUILD SUCCESS** on the first attempt (Entity → Global 2,669 files → Procurement 511 files, 0 errors; only pre-existing unrelated warnings — an unchecked-operations warning in `GoodReceiptNoteDao.java`, present before this fix and untouched by it).

## Files touched

79 of 511 files (432 files in the module had no catch-block findings and were untouched), under `legacy/Procurement/src/main/java/com/param/procurement/{consignment,loanInOut,purchase,quotation,workOrder}/{controller,dao,service}` plus `legacy/Procurement/src/main/java/com/param/procurement/common/response/Response.java`. Full list is the diff of this branch against its base — every file also appears in `docs/AUDIT_Procurement_exceptions.md`'s per-finding table.

## Outstanding

Same as every prior branch in this series: Maker-Checker review (RULES.md item 6) is still required before this branch merges. The manifest entry uses a provisional ID (`BR-PENDING-procurement-exceptions`) per RULES.md item 8 and is marked Owner Decision: Pending. Two documents still await owner input specifically: `docs/DECISION_NEEDED_LoanInOutService_ApproveBatchContinuation.md` (the held-back finding's long-term fail-fast-vs-continue choice) and the `Response.java`/`GoodReceiptNoteDao.java` callouts noted in the manifest entry (no action required, just awareness).
