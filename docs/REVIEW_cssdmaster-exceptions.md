# Review: CSSDMaster module exception-handling fix

*Maker session, 2026-07-06. Same pattern already approved for Billing (BR-006 et seq.), Global (BR-021), ADT (BR-022), and BloodBank (BR-023). Fixes all findings from `docs/AUDIT_CSSDMaster_exceptions.md`.*

## Scope

`docs/AUDIT_CSSDMaster_exceptions.md` catalogued 512 parsed catch blocks across 128 `.java` files under `legacy/CSSDMaster` (split across the co-located `com.param.CSSD` and `com.param.CSSDMaster` packages), of which 510 were findings (all plain empty-catch `silent-swallow` — no `printStackTrace`/`return null`/generic-error sub-patterns occur in this module) and 2 properly rethrew (both in `CSSDOpeningStockDaoImpl.java`, excluded, left untouched). Zero of 128 files imported a real logger. Zero findings were classified billing-critical (CSSDMaster has no billing/payment/tariff package or import of its own, confirmed both directions against `legacy/Billing`).

## Classification pass (required before touching any code)

Every finding was checked against the red-flag patterns from prior fix cycles before applying the standard fix:

- **Fabricated success on downstream failure** (the `collectPayment`/`UnitServiceTariffMasterDaoImpl` pattern, where a catch block or fall-through path returns a SUCCESS-labeled response despite a real failure): every catch body in this module is, per the audit, unconditionally empty — there is no code inside any catch body at all, let alone a SUCCESS-referencing one. The fallback response after every try/catch in this module is uniformly `Response(ERROR, ...)`, never `SUCCESS`. **Zero matches.**
- **Self-manufactured-exception-as-goto pattern**: checked via `grep -rn "throw new Exception"` across the module. **Zero matches.**
- **Vendored third-party library code**: checked via `find -iname "*oauth*"` and inspection of package structure. **Zero matches** — CSSDMaster is entirely first-party kit/autoclave/wrapper/load-batch/sterilization-request domain code.
- **Pre-existing `BillingException`/`ServiceException` convention**: checked via `grep -rl "BillingException\|ServiceException"`. **Zero matches** — no existing convention to match, so `ServiceException` was used throughout, per this task's default and consistent with Global's BR-021 and BloodBank's BR-023 (both defaulted to `ServiceException` where no prior convention existed).

**Result: all 510 findings classified ROUTINE. Zero findings required a NEEDS_OWNER_DECISION document.** Matches the ADT and BloodBank fix cycles (0 held-back each) — CSSDMaster, like ADT and BloodBank, is uniformly simple CRUD-style controller/DAO/service code with no asymmetric-outcome or vendored-code complications. In every touched method the pre-fix behavior on exception was already "swallow and fall through to a separate `return new Response(ERROR, ...)` statement after the try/catch" — never a fabricated success — so converting the silent swallow into a visible thrown exception only changes *how* the existing error path becomes visible, not what counts as success.

## Fix applied

For all 510 findings (45 of 128 files — the other 83 files had no catch blocks at all, or only the 2 excluded rethrows):

- Every empty catch body was replaced with:
  ```java
  logger.error("<methodName> failed", <excVar>);
  throw new ServiceException(<excVar>.getMessage());
  ```
  `<methodName>` is the enclosing Java method's actual name (including cases of legitimate method overloading, e.g. `CSSDOpeningStockController`'s two `saveOpeningStock` overloads each log under that same name — this reflects the source's own naming, not an artifact of the fix).
- A class-level `org.apache.log4j.Logger` field (`final static Logger logger = Logger.getLogger(<ClassName>.class);`) and its import were added to all 45 touched files — CSSDMaster had zero files with a real logger before this fix. `log4j` and `com.param.global.common.ServiceException` (via the existing `Global` module dependency) are already reachable from CSSDMaster's `pom.xml` — no dependency changes were needed.
- `ServiceException` is an unchecked `RuntimeException` already routed to the approved, enabled `BillingExceptionController`/`GlobalExceptionHandler` (BR-004, Global module) — no change to that routing was needed or made.
- The 2 genuine pre-existing rethrows in `CSSDOpeningStockDaoImpl.java` (both unconditional `throw e;`, manually spot-checked in the audit) were left completely untouched, as were the 10 empty-looking catch blocks that a naive text scan flags but that sit inside commented-out (`/* ... */`) dead code blocks in `LoadBatchServiceImpl.java`, `LoadBatchController.java`, `LoadBatchDaoImpl.java`, `KitReturnDaoImpl.java`, `KitIssueDaoImpl.java`, `PreSterilizationProcessInstrumentDaoImpl.java`, and `SterilizationRequestDaoImpl.java` — none of these 10 are live code and none appear in the audit's 510 findings.

## Mechanical consequence: unreachable trailing returns

Same mechanical consequence as every prior fix in this series (BR-006/007/009/010/011/013–017/021/022/023): once every catch clause in a method unconditionally throws, any trailing `return` statement placed after the try/catch block that was previously reached only via the silent-swallow fall-through becomes unreachable dead code under Java's definite-reachability rule, and the compiler refuses to build until it's removed.

This was resolved the same safe, compile-driven way as the BloodBank fix: apply the fix, run `mvn -pl CSSDMaster -am compile -DskipTests -o`, and for every `unreachable statement` error the compiler itself reported, delete exactly that flagged file:line (never a statement the compiler didn't flag) — then recompile and repeat. **442 trailing statements were removed this way before the module reached a clean build.** Every removed line was independently cross-checked afterward against the live diff (`git diff -- legacy/CSSDMaster`): all 442 are one of the five expected `return new Response(ERROR, ...)` / `return new Response("ERROR", ...)` / `return new Response<>(ERROR, ...)` fallback shapes already seen in prior fix cycles — no method in this module had a trailing return of any other shape (unlike BloodBank's 6 special-case `return id`/`return list`/`return "error"` variants), so no case-by-case exception was needed this cycle.

## Verification

- **`grep -rc "logger.error("`**: exactly 510 across the module, matching the finding count 1:1.
- **`grep -rc "throw new ServiceException("`**: exactly 510, matching 1:1.
- **`grep -rl "final static Logger logger"`**: exactly 45 files, matching the touched-file count.
- **`grep -rc "printStackTrace"`**: zero remaining hits (module had zero to begin with, per the audit).
- **Empty-catch-body re-scan**: an independent Python re-scan for `catch (...) { <blank> }` after the fix found only the same 10 pre-existing commented-out-code false positives noted above — zero live empty catch bodies remain.
- **Compile**: `mvn -pl CSSDMaster -am clean compile -DskipTests -o` (run from `legacy/`) — **BUILD SUCCESS** (Entity → Global 2,669 files → CSSDMaster 128 files, 0 errors, same pre-existing unrelated warnings in `Global` as before this change — `OrderDetailsPayeeMapperDto.java` deprecated-API usage, `CommonBillingItemsDto.java` removal-marked-API usage, `GlobalOrganizationMasterServiceImpl.java` unchecked-operations warning — all present before this change and untouched by it).

## Files touched

45 of 128 files (83 files in the module had no catch blocks, or only the 2 excluded rethrows, and were untouched): 14 controllers, 16 DAOs, 15 services under `legacy/CSSDMaster/src/main/java/com/param/{CSSD,CSSDMaster}/{controller,dao,service}/`. Full list is the diff of this branch against its base — every file also appears in `docs/AUDIT_CSSDMaster_exceptions.md`'s per-finding table.

## Outstanding

Same as every prior branch in this series: Maker-Checker review (RULES.md item 6) is still required before this branch merges. The manifest entry below uses a provisional ID (`BR-PENDING-cssdmaster-exceptions`) per RULES.md item 8 and is marked Owner Decision: Pending.
