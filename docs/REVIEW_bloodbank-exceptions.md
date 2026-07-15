# Review: BloodBank module exception-handling fix

*Maker session, 2026-07-06. Same pattern already approved for Billing (BR-006 et seq.), Global (BR-021), and ADT (BR-022). Fixes all findings from `docs/AUDIT_BloodBank_exceptions.md`.*

## Scope

`docs/AUDIT_BloodBank_exceptions.md` catalogued 818 parsed catch blocks across 161 `.java` files under `legacy/BloodBank`, of which 812 were findings (809 silent-swallow + 3 `other`/printStackTrace-only) and 6 properly rethrew (excluded, left untouched). Zero of 161 files imported a real logger. Zero findings were classified billing-critical (BloodBank has no billing/payment/tariff package of its own).

## Classification pass (required before touching any code)

Every finding was checked against the two red-flag patterns from the Global/ADT fix cycles before applying the standard fix:

- **Fabricated success on downstream failure** (the `collectPayment`/`UnitServiceTariffMasterDaoImpl` pattern, where a catch block or fall-through path returns a SUCCESS-labeled response despite a real failure): checked by extracting every catch block's body via a brace-aware parser and searching for the `SUCCESS` token anywhere inside it. **Zero matches** — no catch block in this module ever returns or references a SUCCESS-classified response.
- **Self-manufactured-exception-as-goto pattern** (the `LoyaltyCardValidationGoto` pattern): checked via `grep -rn "throw new Exception"` across the module. **Zero matches**.
- **Vendored third-party library code** (the Pesapal `net.oauth` pattern): checked via `find -iname "*oauth*"` and general inspection of package structure. **Zero matches** — BloodBank has no vendored integration code; it is entirely first-party donor/collection/component/cross-match/issue/transfusion domain code.

**Result: all 812 findings classified ROUTINE. Zero findings required a NEEDS_OWNER_DECISION document.** This differs from the Global fix cycle (15 held-back findings across 5 files) and matches the ADT fix cycle (0 held-back) — BloodBank, like ADT, is uniformly simple CRUD-style controller/DAO/service code with no asymmetric-outcome or vendored-code complications.

## Fix applied

For all 812 findings (94 of 161 files — the other 67 files had no catch blocks at all, per the audit's own file-level breakdown):

- Every silently-swallowed catch body (empty catch; catch returning a generic/hardcoded error without referencing the exception; the 3 `e.printStackTrace()`-only catches; the 2 `System.out.println("ERROR : "+he)`-only catches, all in `BloodBagIssueAcceptanceDaoImpl.java`) was replaced with:
  ```java
  logger.error("<methodName> failed", <excVar>);
  throw new ServiceException(<excVar>.getMessage());
  ```
- `ServiceException` was used throughout (not `BillingException`) — grep confirmed zero pre-existing `BillingException`/`ServiceException` usage anywhere in the module, so there was no existing convention to match, consistent with this task's instruction to default to `ServiceException` in that case (same default Global's BR-021 used for the 83 of its 91 files with no pre-existing convention).
- A class-level `org.apache.log4j.Logger` field (`final static Logger logger = Logger.getLogger(<ClassName>.class);`) and its import were added to all 94 touched files — BloodBank had zero files with a real logger before this fix. `log4j` is already a direct `compile`-scope dependency in `BloodBank/pom.xml`, and `com.param.global.common.ServiceException` is already reachable via BloodBank's existing dependency on the `Global` module (`BloodBank/pom.xml`) — no dependency changes were needed.
- `ServiceException` is an unchecked `RuntimeException` already routed to the approved, enabled `BillingExceptionController` (BR-004, Global module) — no change to that routing was needed or made.

## Mechanical consequence: unreachable trailing returns

Same mechanical consequence as every prior fix in this series (BR-006/007/009/010/011/013–017/021/022): once every catch clause in a method unconditionally throws, any trailing `return` statement placed after the try/catch block that was previously reached only via the silent-swallow fall-through becomes unreachable dead code under Java's definite-reachability rule, and the compiler refuses to build until it's removed.

Rather than hand-deriving reachability for every one of the ~270 affected call sites across 94 files by inspection, this was resolved the safe way: apply the fix, run `mvn -pl BloodBank -am compile -DskipTests -o`, and for every `unreachable statement` error the compiler itself reports, delete exactly that flagged statement (never a statement the compiler didn't flag) — then recompile and repeat. This is mechanically identical to, but a scaled-up automation of, the manual "verified via compile, corrected two mistakes" process BR-021 documented. **272 trailing statements were removed this way, across 43 compile/fix iterations, before the module reached a clean build.** Every removed line was independently spot-checked afterward against the live diff (see below) — none were deleted speculatively, only in direct response to a compiler-proven-unreachable error at that exact file:line.

266 of the 272 removed lines were the expected `return new Response(ERROR, ...)` / `return new Response<>(ERROR, ...)` fallback pattern (matching BR-006/010/014/016 etc.). The other 6 were spot-checked individually and confirmed genuinely unreachable, not a different behavioral class:
- `BloodBagLoanInDaoImpl.getNextValBloodBagStockId` / `DonorRegistrationDaoImpl.getNextValDonorId` / `BloodBagCollectionDaoImpl.getNextValBagCollectionId`: each method's try block ends with an unconditional `return id;`, making the trailing `return null;` unreachable once both catches (`HibernateException`, `Exception`) throw.
- `BloodBagLoanOutServiceImpl.getFilterList`: try block ends with an unconditional `return list;`, making the trailing `return null;` unreachable.
- `DonorRegistrationDaoImpl.generateQueryForDonorSearch`: try block ends with an unconditional `return stringQuery;`, making the trailing `return stringQuery;` (same value, dead regardless) unreachable.
- `BloodBankStockServiceImpl.generateBloodBagLoanNo`: try block ends with an unconditional `return loanNo.toString();`, making the trailing `return "error";` unreachable. (This method returns a bare `String`, not a `Response` object, so its fallback literal differs in shape from the others — same underlying pattern, just a different return type.)

No trailing return in this module was found to be genuinely reachable via a separate non-exceptional path (unlike BR-007's `getBillNumber`/BR-011's `saveTariffMaster`/BR-015's `getCollectionList` precedents) — the compile-driven process would have left any such line untouched (the compiler simply wouldn't have flagged it), and a full second pass of `analyze.py` post-fix confirms zero remaining silent/masked catch bodies and zero remaining `printStackTrace`/`System.out.println` error-only catches.

## Verification

- **Structural parser cross-check**: an independent brace-aware Python parser (not reused from the audit's own tooling) found 818 total catch blocks both before and after the fix — 812 fixable + 6 already-rethrowing, matching the audit's own count exactly, both before the fix (812 silent/other) and after (0 silent/other, 818 rethrow-or-logged).
- **`grep -rn "printStackTrace"` / `System.out.println` in error context**: zero remaining hits.
- **`grep -rc "logger.error"`**: exactly 812 across the module, matching the finding count 1:1.
- **`grep -rl "Logger logger"`**: exactly 94 files, matching the touched-file count.
- **Compile**: `mvn -pl BloodBank -am clean compile -DskipTests -o` (run from `legacy/`) — **BUILD SUCCESS** (Entity → Global 2,669 files → BloodBank 160 files, 0 errors, same 2 pre-existing unrelated warnings as before this change — `DonorRegistrationServiceImpl.java`'s deprecated-API usage and `BloodComponentOrderMasterServiceImpl.java`'s unchecked-operations warning, both present in the original unmodified file and untouched by this fix).

## Files touched

94 of 161 files (67 files in the module had no catch blocks and were untouched): 30 controllers, 34 DAOs, 30 services under `legacy/BloodBank/src/main/java/com/param/bloodBank/{controller,dao,service}/`. Full list is the diff of this branch against its base — every file also appears in `docs/AUDIT_BloodBank_exceptions.md`'s per-finding table.

## Outstanding

Same as every prior branch in this series: Maker-Checker review (RULES.md item 6) is still required before this branch merges. The manifest entry below uses a provisional ID (`BR-PENDING-bloodbank-exceptions`) per RULES.md item 8 and is marked Owner Decision: Pending.
