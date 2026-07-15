# Review: DietAndKitchen module exception-handling fix

*Maker session, 2026-07-06. Same pattern already approved for Billing (BR-006 et seq.), Global (BR-021), ADT (BR-022), BloodBank (BR-023), CSSDMaster (BR-024), and Pharmacy (BR-025). Fixes all 266 findings from `docs/AUDIT_DietAndKitchen_exceptions.md`.*

## Scope

`docs/AUDIT_DietAndKitchen_exceptions.md` catalogued 291 parsed catch blocks across 124 `.java` files under `legacy/DietAndKitchen`, of which 266 were findings (265 silent-swallow, 1 `other` — a `printStackTrace()`-only catch) across 42 files, and 25 properly excluded (all rethrow; 0 already logged via a real logger — 0 of 124 files imported a real logging library before this fix). **0 of the 266 findings were classified billing-critical** — the first module in this series to score zero on every billing-critical criterion used throughout the series (no `com/param/billing/` package, no filename/content match for billing keywords including `charge`, no import relationship in either direction against Billing/Global's billing packages).

## Classification pass (required before touching any code)

Per the task's explicit instruction, every finding was checked against the red-flag patterns surfaced by prior cycles before any fix was applied:

- **Fabricated success on downstream failure** (the `collectPayment`/`RazorPayment`/`UnitServiceTariffMasterDaoImpl` pattern): a brace-aware search of every catch body for a `SUCCESS` token found zero matches. No catch in this module returns or leaves in place an explicit success-labeled response.
- **Self-manufactured-exception-as-goto pattern** (the Global-module `LoyaltyCardValidationGoto` shape, recurring independently in Pharmacy's `BillListService.java`): `grep -rn "throw new Exception()"` across the module found **zero matches**.
- **"Null-probe" idiom** (the Pharmacy-discovered pattern where a `try` block does nothing but read a lazy-loaded association purely to test whether it throws, with the catch setting a boolean flag used for legitimate fallback control flow): `grep -rn "checkDoctor = true\|flag = true"` across the module found **zero matches**. This idiom does not recur in DietAndKitchen.
- **Vendored third-party library code**: no vendored integration code exists in this module. Zero matches.
- **Pre-existing `BillingException`/`ServiceException` convention**: `grep -rl "BillingException"` and `grep -rl "ServiceException"` across the module both returned **zero files**. No pre-existing convention to match, so `ServiceException` was used throughout per this task's instructed default, matching the precedent set by Global/ADT/BloodBank/CSSDMaster/Pharmacy.

**Result: all 266 findings classified ROUTINE and fixed. 0 findings classified NEEDS_OWNER_DECISION.** No decision documents were required for this module — the first module in this series with a clean sweep on both the billing-critical and held-back-pattern axes.

## Fix applied

For all 266 findings across all 42 files with findings:

- Every silently-swallowed catch body was replaced with:
  ```java
  logger.error(<excVar>.getMessage(), <excVar>);
  throw new ServiceException(<excVar>.getMessage());
  ```
  using each catch clause's actual declared exception variable name — `e` for `Exception` and `he` for `HibernateException` throughout; no non-standard variable names were encountered anywhere in this module.
- `import com.param.global.common.ServiceException;` was added to every touched file.
- A class-level `org.apache.log4j.Logger` field was added to all 42 touched files — DietAndKitchen had zero files with a real logger before this fix (confirmed by the audit, matching ADT/BloodBank/CSSDMaster's 0-real-logger baseline).
- The single `other`-classified finding (`DishMasterDaoImpl.java:363`, a bare `e.printStackTrace()` with a separate method-level fallback return) was fixed identically to every silent-swallow finding — replaced with the same `logger.error`/`throw` pattern, since console-only output is not a substitute for the centralized handler either.
- `ServiceException` is an unchecked `RuntimeException` already routed to the approved, enabled `BillingExceptionController`/`GlobalExceptionHandler` (BR-004, Global module) — no change to that routing was needed or made. DietAndKitchen already depends on the Global module and on `log4j` directly (confirmed in `legacy/DietAndKitchen/pom.xml`).

### Execution method

Given the scale (42 files, 266 findings), the fix was applied by 5 parallel Maker sub-sessions, each assigned a disjoint, pre-computed, load-balanced list of exact (file, line) pairs derived directly from the audit's findings table (bin-packed to ~53 findings per session). Each sub-session was instructed to leave every catch block not on its list — including pre-existing rethrows and catch blocks inside commented-out dead code — completely untouched. All 5 sessions reported back their per-file finding counts, which summed to exactly 266 and were cross-checked against `git diff --stat` and a full-module `grep -rc "throw new ServiceException"` / `grep -rc "logger.error"` count (both exactly 266) after all 5 completed.

## Mechanical consequence: unreachable trailing returns

Same mechanical consequence as every prior fix in this series: once every catch clause in a method unconditionally throws, any trailing `return` statement placed after the try/catch that was previously reached only via the silent-swallow fall-through becomes unreachable dead code under Java's definite-reachability rule.

Each of the 5 sub-sessions performed hand-derived, per-method reachability analysis at fix time (matching the BR-006/BR-009–BR-017/BR-022/BR-025 precedent, given the batch size). Each was instructed to delete a trailing statement only when it was genuinely unreachable as a direct result of its own edits, and to leave it in place when a separate non-exceptional fall-through path existed. Representative preserved cases reported back: `RejectedDietListDaoImpl.reIssueRejectedDietOrderByDietOrderId` (a non-returning `orderIds.isEmpty()` path inside the try), `DietAcceptanceServiceImpl.getDietAcceptanceByMultipleFields` (a non-returning outer `if (dietDate != null)` guard), and several `DietUnitMasterController` methods where the catch already returned inline with no separate trailing statement to begin with.

This hand-derived approach was independently confirmed correct by the compile result below: the module compiled to **BUILD SUCCESS on the first attempt**, with zero "unreachable statement" or "missing return statement" errors.

## Verification

- **`grep -rc "throw new ServiceException"` across `legacy/DietAndKitchen`**: exactly **266**, matching the finding count 1:1.
- **`grep -rc "logger.error"` across `legacy/DietAndKitchen`**: exactly **266** — all newly added (module had zero pre-existing real-logger calls per the audit).
- **`git diff --stat -- legacy/DietAndKitchen`**: exactly **42 files changed**, matching the number of files with at least one finding; 886 insertions, 658 deletions.
- **Dead/commented-out code confirmed untouched**: `DietPlanningDaoImpl.java`'s ~8 dead catches inside `/* ... */`-fenced methods, `DietTemplateMapperController.java`'s fenced dead method (lines ~137–145), and the pre-existing `catch (Exception e) { throw e; }` / `catch (HibernateException he) { throw he; }` rethrows in `DietIssueDaoImpl.java`, `DishMasterDaoImpl.java`, `DishMasterServiceImpl.java`, `KeyIngredientsMasterServiceImpl.java`, `DietIssueServiceImpl.java`, `KeyIngredientsMasterDaoImpl.java`, and `DietOrderDaoImpl.java` — all confirmed still present and unmodified by the fixing sub-sessions.
- **Compile**: `mvn -pl DietAndKitchen -am compile -DskipTests -o` (run from `legacy/`) — **BUILD SUCCESS** on the first pass (Entity → Global 2,669 files → DietAndKitchen 124 files, 0 errors, only one pre-existing unrelated warning: unchecked/unsafe operations in `DietPlanningDaoImpl.java`, present before this change and untouched by it).

## Files touched

All 42 of 42 files with findings, under `legacy/DietAndKitchen/src/main/java/com/param/global/master/{controller,dao,service}/` and `legacy/DietAndKitchen/src/main/java/com/param/kitchen/{controller,dao,service}/` — full per-file finding counts in `docs/AUDIT_DietAndKitchen_exceptions.md`.

## Outstanding

Same as every prior branch in this series: Maker-Checker review (RULES.md item 6) is still required before this branch merges. The manifest entry uses a provisional ID (`BR-PENDING-dietandkitchen-exceptions`) per RULES.md item 8 and is marked Owner Decision: Pending. Unlike Pharmacy's cycle, there are no outstanding decision documents for this module — every finding was classified ROUTINE and fixed.
