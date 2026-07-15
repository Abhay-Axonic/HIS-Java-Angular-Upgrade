# Audit: Silent Exception-Swallow Patterns — legacy/CSSDMaster module

*Read-only audit, 2026-07-06. Same method as `docs/AUDIT_Billing_exceptions.md`, `docs/AUDIT_Global_exceptions.md`, `docs/AUDIT_ADT_exceptions.md`, and `docs/AUDIT_BloodBank_exceptions.md`; scope here is the CSSDMaster module (Central Sterile Supply Department — autoclave/sterilization-type/wrapper master data, kit master/indent/issue/return, load-batch and pre-sterilization-process tracking, sterilization requests, cross-referral to microbiology).*

**Scope:** every `.java` file under `legacy/CSSDMaster` (128 files, 512 parsed catch blocks). The module is split across two co-located source packages, `com/param/CSSD/*` and `com/param/CSSDMaster/*` (controller/dao/service/dto/model), both scanned in full.

**Method:** read-only. Automated structural parse (comment/string-aware brace matching) of every `catch` block, classified by body content, cross-checked by manual sampling (spot-checked findings across empty-catch controllers and DAOs, both exclusion categories — rethrow and printStackTrace/`other` — and the one file-level `return null;` hit, which turned out to be an unrelated unimplemented stub method, not a catch body). No file under `legacy/` was modified.

**Definition used (per task, identical to the Billing/Global/ADT/BloodBank audits):** a finding is any catch block that is (a) empty, (b) returns `null`/a generic hardcoded error with no logging of the real cause, or (c) neither logs (via a real logger call) nor rethrows. Catch blocks that call a logger (`logger.error(...)`, etc.) or that `throw`/rethrow are excluded as non-findings.

## Summary

- **Total catch blocks parsed:** 512
- **Total findings (silent-swallow pattern matches):** 510
- **Non-findings:** 2 properly rethrow (`throw`, both in `CSSDOpeningStockDaoImpl.java`); 0 properly log via a real logger call
- **By severity:** silent-swallow = 510, status-masks-failure = 0, other = 0
- **By path:** billing-critical = 0, peripheral = 510

| Severity | Billing-critical | Peripheral |
|---|---|---|
| status-masks-failure | 0 | 0 |
| silent-swallow | 0 | 510 |
| other | 0 | 0 |

**0 of 128 files import any real logging library** (`org.apache.log4j.Logger` or `org.slf4j.Logger`) — consistent with ADT (0/589) and BloodBank (0/161), worse than Billing (28/558, 5%) and Global (15/2669, 0.6%). Every non-rethrowing catch in the module is therefore structurally silent by construction: there is no logging exclusion category available at all in this module (0 catches excluded on that basis).

**Every single finding in this module is a plain empty catch block** (`silent-swallow`, "empty catch block — exception fully discarded"). Unlike Billing/ADT/BloodBank, no finding here falls into the "generic-error-response-that-references-the-exception-variable," `printStackTrace()`-only, or `return null;`-only sub-patterns — the module has zero occurrences of `printStackTrace`, zero live `System.out`/`System.err` (the four grep hits are commented-out debug lines, correctly excluded by the comment-aware parser), and its one `return null;` occurrence is an unrelated unimplemented stub method (`WrapperExpiryMasterDaoImpl`-style `// TODO Auto-generated method stub` in `PreSterilizationProcessDaoImpl.cssdStockLogUpdate`), not a catch body. Controllers uniformly follow the pattern `catch (Exception e) { }` followed by `return new Response(ERROR, ...)`; DAOs uniformly follow `catch (HibernateException he) { } catch (Exception e) { }` (both bodies empty) inside methods that separately return a fallback `Response(ERROR, ...)` after the try/catch/finally.

**Zero billing-critical findings.** CSSDMaster has no package, filename, or import that matches the billing heuristic — this module tracks CSSD (sterile-instrument-processing) kits, autoclave/wrapper masters, and load batches; it does not do payment/invoice/tariff calculation and is not imported by, nor does it import from, `legacy/Billing`.

## Top 15 files by finding count

| Rank | File | Findings |
|---|---|---|
| 1 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 55 |
| 2 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 30 |
| 3 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 27 |
| 4 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 22 |
| 5 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 19 |
| 6 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 19 |
| 7 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 18 |
| 8 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 16 |
| 9 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/KitMasterServiceImpl.java` | 15 |
| 10 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/CssdKitMasterController.java` | 15 |
| 11 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIndentServiceImpl.java` | 15 |
| 12 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/LoadBatchDaoImpl.java` | 15 |
| 13 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIndentController.java` | 15 |
| 14 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/WrapperExpiryMasterDaoImpl.java` | 14 |
| 15 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/LoadBatchServiceImpl.java` | 14 |

## Methodology caveats (read before acting on this list)

- **billing-critical vs peripheral is a path/filename/import heuristic**, not a data-flow analysis, identical approach to the Global/ADT/BloodBank audits. A file is marked billing-critical if: (a) it lives under a `com/param/billing/` or `com/param/global/billing/` package (none do — CSSDMaster has no such package); (b) its path/filename contains a payment/invoice/tariff/discount/deposit/fund/insurance/razorpay/pesapal/charge/voucher/refund/bill/consumption/collection/sanction/mpesa/ccavenue keyword; or (c) it is one of the classes directly imported by `legacy/Billing`. All three criteria contribute **zero** files here: a full case-insensitive filename search for every keyword in the list above returned no matches anywhere under `legacy/CSSDMaster` (criterion b), no file imports `com.param.billing.*` or `com.param.global.billing.*` and none implements `IBillingCommonConstants`/`IBillingConstants` (criterion a/c-adjacent), and `grep` of every `import com.param.CSSDMaster` / `import com.param.CSSD.` line across all of `legacy/Billing` returned no hits (criterion c) — confirmed in both directions.
- **No domain-word collision with the billing-keyword heuristic was found in this module**, unlike ADT's `charge`/`discharge` collision or BloodBank's `collection` collision. CSSD's own domain vocabulary — kit, autoclave, wrapper, sterilization, load/batch, indent (a hospital requisition/request term), issue, return — does not share a substring with any billing keyword (`bill`, `charge`, `collection`, `consumption`, `fund`, `sanction`, etc. were each individually checked against every path under the module; zero hits). This was checked carefully per the task's request but the result is a clean negative, not a correction like the prior two audits needed.
- **The parser is structural, not control-flow-aware**, identical caveat to the Billing/Global/ADT/BloodBank audits: a catch block containing `throw` anywhere in its body is treated as "properly rethrows" and excluded, even if a nested conditional path could still silently fall through without throwing. Spot-checked directly against source (see Method) — both of the 2 excluded blocks in this module (`CSSDOpeningStockDaoImpl.java:103` and `:171`) were confirmed as genuine unconditional rethrows on manual read.
- **`silent-swallow`** here is exclusively the fully-empty-catch-body pattern (`catch (Exception e) { }` / `catch (HibernateException he) { }` with nothing between the braces). No instance in this module returns a generic error response that references the exception variable, and no instance uses a console-only print as a substitute for logging — those two sub-patterns, seen in the Billing/ADT/BloodBank audits, simply don't occur in CSSDMaster's code style; every controller/DAO method here separates the (always-empty) catch body from its fallback `return new Response(ERROR, ...)` statement, which sits after the try/catch/finally block rather than inside the catch body itself.
- **`status-masks-failure`** (the `return null;`-only pattern from BR-003/BR-005) has **zero** instances in this module. The single `return null;` found by a raw grep (`PreSterilizationProcessDaoImpl.java:1185`) is a `cssdStockLogUpdate` method body marked `// TODO Auto-generated method stub` — an unimplemented stub, not inside any catch block — and is correctly excluded by the structural parser.
- **`other`** (`e.printStackTrace()`-only catches, the BloodBank sub-pattern) has **zero** instances in this module — no file contains `printStackTrace` anywhere, live or commented.
- **Multi-catch clauses** (`catch (TypeA | TypeB e)`) are recorded once, under the last-listed type in the clause, as a parsing simplification. None occurred in this module (no `|`-joined catch clause was found in CSSDMaster).
- **Scope has no `src/test/java` directory** — unlike the BloodBank audit's boilerplate `AppTest.java`, CSSDMaster has no test sources at all, so the 128-file count is 100% `src/main/java`.
- This audit does **not** re-scan `legacy/Billing`, `legacy/Global`, `legacy/ADT`, or `legacy/BloodBank` (already catalogued in their own audit docs) or any other module — it is CSSDMaster-only, matching this task's scope.

## Full findings table

| # | File | Line | Exception Type | Severity | Path | Notes |
|---|------|------|-----------------|----------|------|-------|
| 1 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/CSSDOpeningStockController.java` | 29 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 2 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/CSSDOpeningStockController.java` | 39 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 3 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/CSSDOpeningStockController.java` | 49 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 4 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/CSSDOpeningStockController.java` | 59 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 5 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/CSSDOpeningStockController.java` | 70 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 6 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/CSSDOpeningStockController.java` | 80 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 7 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIndentController.java` | 40 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 8 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIndentController.java` | 55 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 9 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIndentController.java` | 70 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 10 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIndentController.java` | 85 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 11 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIndentController.java` | 100 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 12 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIndentController.java` | 128 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 13 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIndentController.java` | 156 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 14 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIndentController.java` | 173 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 15 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIndentController.java` | 189 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 16 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIndentController.java` | 205 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 17 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIndentController.java` | 221 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 18 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIndentController.java` | 237 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 19 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIndentController.java` | 248 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 20 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIndentController.java` | 265 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 21 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIndentController.java` | 281 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 22 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 40 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 23 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 70 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 24 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 89 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 25 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 102 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 26 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 112 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 27 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 122 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 28 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 132 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 29 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 143 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 30 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 157 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 31 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 173 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 32 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 192 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 33 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 210 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 34 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 246 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 35 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 263 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 36 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 279 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 37 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 293 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 38 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 308 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 39 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 323 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 40 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitIssueController.java` | 339 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 41 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitReturnController.java` | 43 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 42 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitReturnController.java` | 59 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 43 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitReturnController.java` | 86 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 44 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitReturnController.java` | 103 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 45 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitReturnController.java` | 119 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 46 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitReturnController.java` | 135 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 47 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitReturnController.java` | 151 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 48 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitReturnController.java` | 168 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 49 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/KitReturnController.java` | 184 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 50 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/LoadBatchController.java` | 37 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 51 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/LoadBatchController.java` | 50 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 52 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/LoadBatchController.java` | 61 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 53 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/LoadBatchController.java` | 76 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 54 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/LoadBatchController.java` | 86 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 55 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/LoadBatchController.java` | 99 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 56 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/LoadBatchController.java` | 114 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 57 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/LoadBatchController.java` | 129 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 58 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/LoadBatchController.java` | 188 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 59 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/LoadBatchController.java` | 203 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 60 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/LoadBatchController.java` | 237 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 61 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/LoadBatchController.java` | 251 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 62 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/LoadBatchController.java` | 275 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 63 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 51 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 64 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 76 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 65 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 101 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 66 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 119 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 67 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 136 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 68 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 148 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 69 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 158 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 70 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 175 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 71 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 188 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 72 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 199 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 73 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 213 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 74 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 235 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 75 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 260 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 76 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 270 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 77 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 285 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 78 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 300 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 79 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 315 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 80 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 330 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 81 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 346 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 82 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 358 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 83 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 369 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 84 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessController.java` | 379 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 85 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessInstrumentController.java` | 36 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 86 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessInstrumentController.java` | 51 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 87 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessInstrumentController.java` | 66 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 88 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessInstrumentController.java` | 81 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 89 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessInstrumentController.java` | 96 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 90 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessInstrumentController.java` | 111 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 91 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessInstrumentController.java` | 126 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 92 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/PreSterilizationProcessInstrumentController.java` | 141 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 93 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/SendToMicrobiologyController.java` | 31 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 94 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/SterilizationRequestController.java` | 41 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 95 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/SterilizationRequestController.java` | 57 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 96 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/SterilizationRequestController.java` | 84 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 97 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/SterilizationRequestController.java` | 111 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 98 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/SterilizationRequestController.java` | 130 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 99 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/SterilizationRequestController.java` | 145 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 100 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/SterilizationRequestController.java` | 160 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 101 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/SterilizationRequestController.java` | 175 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 102 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/SterilizationRequestController.java` | 190 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 103 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/SterilizationRequestController.java` | 221 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 104 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/SterilizationRequestController.java` | 236 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 105 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/SterilizedBatchController.java` | 34 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 106 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/controller/SterilizedBatchController.java` | 49 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 107 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/CSSDOpeningStockDaoImpl.java` | 264 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 108 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/CSSDOpeningStockDaoImpl.java` | 265 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 109 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/CSSDOpeningStockDaoImpl.java` | 329 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 110 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/CSSDOpeningStockDaoImpl.java` | 330 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 111 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/CSSDOpeningStockDaoImpl.java` | 380 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 112 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/CSSDOpeningStockDaoImpl.java` | 381 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 113 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 66 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 114 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 107 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 115 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 131 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 116 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 153 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 117 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 172 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 118 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 270 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 119 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 339 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 120 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 388 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 121 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 421 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 122 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 453 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 123 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 509 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 124 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 573 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 125 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 596 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 126 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 618 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 127 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 642 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 128 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIndentDaoImpl.java` | 659 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 129 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 209 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 130 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 378 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 131 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 591 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 132 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 722 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 133 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 789 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 134 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 810 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 135 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 846 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 136 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 871 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 137 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 898 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 138 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 1017 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 139 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 1148 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 140 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 1273 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 141 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 1291 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 142 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 1308 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 143 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 1325 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 144 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 1374 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 145 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 1406 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 146 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitIssueDaoImpl.java` | 1438 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 147 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitMovementRecordDaoImpl.java` | 32 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 148 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitReturnDaoImpl.java` | 100 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 149 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitReturnDaoImpl.java` | 188 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 150 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitReturnDaoImpl.java` | 311 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 151 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitReturnDaoImpl.java` | 392 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 152 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitReturnDaoImpl.java` | 450 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 153 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitReturnDaoImpl.java` | 472 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 154 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitReturnDaoImpl.java` | 493 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 155 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitReturnDaoImpl.java` | 563 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 156 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/KitReturnDaoImpl.java` | 581 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 157 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/LoadBatchDaoImpl.java` | 91 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 158 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/LoadBatchDaoImpl.java` | 115 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 159 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/LoadBatchDaoImpl.java` | 170 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 160 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/LoadBatchDaoImpl.java` | 226 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 161 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/LoadBatchDaoImpl.java` | 303 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 162 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/LoadBatchDaoImpl.java` | 350 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 163 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/LoadBatchDaoImpl.java` | 400 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 164 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/LoadBatchDaoImpl.java` | 425 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 165 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/LoadBatchDaoImpl.java` | 541 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 166 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/LoadBatchDaoImpl.java` | 559 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 167 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/LoadBatchDaoImpl.java` | 576 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 168 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/LoadBatchDaoImpl.java` | 598 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 169 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/LoadBatchDaoImpl.java` | 615 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 170 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/LoadBatchDaoImpl.java` | 631 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 171 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/LoadBatchDaoImpl.java` | 652 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 172 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 106 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 173 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 107 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 174 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 131 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 175 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 132 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 176 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 214 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 177 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 215 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 178 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 289 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 179 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 290 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 180 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 397 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 181 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 398 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 182 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 506 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 183 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 507 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 184 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 566 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 185 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 567 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 186 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 601 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 187 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 734 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 188 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 735 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 189 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 812 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 190 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 813 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 191 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 841 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 192 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 842 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 193 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 928 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 194 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 929 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 195 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1003 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 196 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1004 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 197 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1023 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 198 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1024 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 199 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1042 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 200 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1043 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 201 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1061 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 202 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1062 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 203 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1080 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 204 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1081 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 205 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1099 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 206 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1100 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 207 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1117 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 208 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1131 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 209 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1149 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 210 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1150 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 211 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1172 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 212 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1173 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 213 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1246 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 214 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1247 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 215 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1307 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 216 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1308 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 217 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1379 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 218 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1380 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 219 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1440 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 220 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1441 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 221 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1532 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 222 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1533 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 223 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1562 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 224 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1563 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 225 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1593 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 226 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessDaoImpl.java` | 1594 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 227 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessInstrumentDaoImpl.java` | 48 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 228 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessInstrumentDaoImpl.java` | 49 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 229 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessInstrumentDaoImpl.java` | 66 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 230 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessInstrumentDaoImpl.java` | 78 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 231 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessInstrumentDaoImpl.java` | 164 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 232 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessInstrumentDaoImpl.java` | 216 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 233 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessInstrumentDaoImpl.java` | 266 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 234 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessInstrumentDaoImpl.java` | 286 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 235 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessInstrumentDaoImpl.java` | 287 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 236 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessInstrumentDaoImpl.java` | 306 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 237 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessInstrumentDaoImpl.java` | 307 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 238 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessInstrumentDaoImpl.java` | 327 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 239 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/PreSterilizationProcessInstrumentDaoImpl.java` | 328 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 240 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/SendToMicrobiologyDaoImpl.java` | 47 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 241 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/SendToMicrobiologyDaoImpl.java` | 64 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 242 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/SterilizationRequestDaoImpl.java` | 101 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 243 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/SterilizationRequestDaoImpl.java` | 146 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 244 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/SterilizationRequestDaoImpl.java` | 284 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 245 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/SterilizationRequestDaoImpl.java` | 423 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 246 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/SterilizationRequestDaoImpl.java` | 523 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 247 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/SterilizationRequestDaoImpl.java` | 562 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 248 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/SterilizationRequestDaoImpl.java` | 584 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 249 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/SterilizationRequestDaoImpl.java` | 618 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 250 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/SterilizationRequestDaoImpl.java` | 730 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 251 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/SterilizationRequestDaoImpl.java` | 800 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 252 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/SterilizationRequestDaoImpl.java` | 818 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 253 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/SterilizedBatchDaoImpl.java` | 51 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 254 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/dao/SterilizedBatchDaoImpl.java` | 68 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 255 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/CSSDOpeningStockServiceImpl.java` | 52 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 256 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/CSSDOpeningStockServiceImpl.java` | 63 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 257 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/CSSDOpeningStockServiceImpl.java` | 126 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 258 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/CSSDOpeningStockServiceImpl.java` | 186 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 259 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/CSSDOpeningStockServiceImpl.java` | 198 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 260 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/CSSDOpeningStockServiceImpl.java` | 209 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 261 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIndentServiceImpl.java` | 28 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 262 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIndentServiceImpl.java` | 39 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 263 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIndentServiceImpl.java` | 52 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 264 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIndentServiceImpl.java` | 63 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 265 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIndentServiceImpl.java` | 74 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 266 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIndentServiceImpl.java` | 85 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 267 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIndentServiceImpl.java` | 96 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 268 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIndentServiceImpl.java` | 107 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 269 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIndentServiceImpl.java` | 118 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 270 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIndentServiceImpl.java` | 131 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 271 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIndentServiceImpl.java` | 142 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 272 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIndentServiceImpl.java` | 153 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 273 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIndentServiceImpl.java` | 164 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 274 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIndentServiceImpl.java` | 175 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 275 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIndentServiceImpl.java` | 186 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 276 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 28 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 277 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 39 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 278 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 50 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 279 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 61 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 280 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 72 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 281 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 83 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 282 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 94 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 283 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 123 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 284 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 140 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 285 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 157 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 286 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 170 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 287 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 184 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 288 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 224 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 289 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 235 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 290 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 246 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 291 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 257 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 292 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 269 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 293 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 281 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 294 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitIssueServiceImpl.java` | 292 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 295 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitMovementRecordServiceImpl.java` | 26 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 296 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitReturnServiceImpl.java` | 28 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 297 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitReturnServiceImpl.java` | 39 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 298 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitReturnServiceImpl.java` | 50 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 299 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitReturnServiceImpl.java` | 61 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 300 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitReturnServiceImpl.java` | 73 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 301 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitReturnServiceImpl.java` | 84 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 302 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitReturnServiceImpl.java` | 97 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 303 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitReturnServiceImpl.java` | 129 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 304 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/KitReturnServiceImpl.java` | 140 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 305 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/LoadBatchServiceImpl.java` | 26 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 306 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/LoadBatchServiceImpl.java` | 37 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 307 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/LoadBatchServiceImpl.java` | 48 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 308 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/LoadBatchServiceImpl.java` | 59 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 309 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/LoadBatchServiceImpl.java` | 70 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 310 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/LoadBatchServiceImpl.java` | 82 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 311 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/LoadBatchServiceImpl.java` | 93 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 312 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/LoadBatchServiceImpl.java` | 104 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 313 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/LoadBatchServiceImpl.java` | 152 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 314 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/LoadBatchServiceImpl.java` | 163 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 315 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/LoadBatchServiceImpl.java` | 177 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 316 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/LoadBatchServiceImpl.java` | 201 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 317 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/LoadBatchServiceImpl.java` | 212 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 318 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/LoadBatchServiceImpl.java` | 223 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 319 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessInstrumentServiceImpl.java` | 26 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 320 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessInstrumentServiceImpl.java` | 37 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 321 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessInstrumentServiceImpl.java` | 48 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 322 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessInstrumentServiceImpl.java` | 59 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 323 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessInstrumentServiceImpl.java` | 71 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 324 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessInstrumentServiceImpl.java` | 82 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 325 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessInstrumentServiceImpl.java` | 93 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 326 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessInstrumentServiceImpl.java` | 104 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 327 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 45 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 328 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 56 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 329 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 67 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 330 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 79 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 331 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 90 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 332 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 101 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 333 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 155 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 334 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 167 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 335 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 178 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 336 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 189 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 337 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 200 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 338 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 212 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 339 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 224 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 340 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 269 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 341 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 282 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 342 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 293 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 343 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 305 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 344 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 316 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 345 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 327 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 346 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 338 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 347 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 349 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 348 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 360 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 349 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 371 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 350 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 382 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 351 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 393 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 352 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 404 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 353 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/PreSterilizationProcessServiceImpl.java` | 426 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 354 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/SendToMicrobiologyServiceImpl.java` | 24 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 355 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/SendToMicrobiologyServiceImpl.java` | 35 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 356 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/SterilizationRequestServiceImpl.java` | 28 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 357 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/SterilizationRequestServiceImpl.java` | 39 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 358 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/SterilizationRequestServiceImpl.java` | 50 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 359 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/SterilizationRequestServiceImpl.java` | 61 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 360 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/SterilizationRequestServiceImpl.java` | 72 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 361 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/SterilizationRequestServiceImpl.java` | 84 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 362 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/SterilizationRequestServiceImpl.java` | 95 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 363 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/SterilizationRequestServiceImpl.java` | 108 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 364 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/SterilizationRequestServiceImpl.java` | 120 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 365 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/SterilizationRequestServiceImpl.java` | 152 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 366 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/SterilizationRequestServiceImpl.java` | 163 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 367 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/SterilizedBatchServiceImpl.java` | 24 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 368 | `legacy/CSSDMaster/src/main/java/com/param/CSSD/service/SterilizedBatchServiceImpl.java` | 35 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 369 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/AutoclaveMasterController.java` | 30 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 370 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/AutoclaveMasterController.java` | 40 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 371 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/AutoclaveMasterController.java` | 50 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 372 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/AutoclaveMasterController.java` | 62 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 373 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/AutoclaveMasterController.java` | 73 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 374 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/AutoclaveMasterController.java` | 84 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 375 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/AutoclaveMasterController.java` | 95 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 376 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/CssdKitMasterController.java` | 31 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 377 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/CssdKitMasterController.java` | 41 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 378 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/CssdKitMasterController.java` | 51 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 379 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/CssdKitMasterController.java` | 65 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 380 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/CssdKitMasterController.java` | 76 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 381 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/CssdKitMasterController.java` | 87 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 382 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/CssdKitMasterController.java` | 98 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 383 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/CssdKitMasterController.java` | 109 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 384 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/CssdKitMasterController.java` | 120 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 385 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/CssdKitMasterController.java` | 131 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 386 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/CssdKitMasterController.java` | 142 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 387 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/CssdKitMasterController.java` | 153 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 388 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/CssdKitMasterController.java` | 168 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 389 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/CssdKitMasterController.java` | 179 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 390 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/CssdKitMasterController.java` | 190 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 391 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/SterilizationTypeMasterController.java` | 29 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 392 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/SterilizationTypeMasterController.java` | 39 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 393 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/SterilizationTypeMasterController.java` | 49 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 394 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/SterilizationTypeMasterController.java` | 61 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 395 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/SterilizationTypeMasterController.java` | 72 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 396 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/SterilizationTypeMasterController.java` | 83 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 397 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/WrapperExpiryMasterController.java` | 30 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 398 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/WrapperExpiryMasterController.java` | 40 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 399 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/WrapperExpiryMasterController.java` | 50 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 400 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/WrapperExpiryMasterController.java` | 64 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 401 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/WrapperExpiryMasterController.java` | 75 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 402 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/WrapperExpiryMasterController.java` | 86 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 403 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/WrapperExpiryMasterController.java` | 98 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 404 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/controller/WrapperExpiryMasterController.java` | 109 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 405 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/AutoclaveMasterDaoImpl.java` | 51 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 406 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/AutoclaveMasterDaoImpl.java` | 52 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 407 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/AutoclaveMasterDaoImpl.java` | 71 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 408 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/AutoclaveMasterDaoImpl.java` | 72 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 409 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/AutoclaveMasterDaoImpl.java` | 97 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 410 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/AutoclaveMasterDaoImpl.java` | 123 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 411 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/AutoclaveMasterDaoImpl.java` | 149 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 412 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/AutoclaveMasterDaoImpl.java` | 150 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 413 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/AutoclaveMasterDaoImpl.java` | 168 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 414 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/AutoclaveMasterDaoImpl.java` | 169 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 415 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/AutoclaveMasterDaoImpl.java` | 183 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 416 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/AutoclaveMasterDaoImpl.java` | 184 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 417 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitEquipmentMapperDaoImpl.java` | 47 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 418 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitEquipmentMapperDaoImpl.java` | 48 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 419 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitEquipmentMapperDaoImpl.java` | 69 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 420 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitEquipmentMapperDaoImpl.java` | 70 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 421 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 61 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 422 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 62 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 423 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 82 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 424 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 83 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 425 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 103 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 426 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 104 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 427 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 134 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 428 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 135 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 429 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 166 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 430 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 167 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 431 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 204 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 432 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 205 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 433 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 228 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 434 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 229 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 435 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 250 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 436 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 251 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 437 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 312 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 438 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 313 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 439 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 348 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 440 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 349 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 441 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 381 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 442 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 410 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 443 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 429 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 444 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 430 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 445 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 459 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 446 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 460 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 447 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 479 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 448 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 480 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 449 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 496 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 450 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/KitMasterDaoImpl.java` | 497 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 451 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/SterilizationTypeMasterDaoImpl.java` | 52 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 452 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/SterilizationTypeMasterDaoImpl.java` | 53 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 453 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/SterilizationTypeMasterDaoImpl.java` | 73 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 454 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/SterilizationTypeMasterDaoImpl.java` | 74 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 455 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/SterilizationTypeMasterDaoImpl.java` | 95 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 456 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/SterilizationTypeMasterDaoImpl.java` | 124 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 457 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/SterilizationTypeMasterDaoImpl.java` | 145 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 458 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/SterilizationTypeMasterDaoImpl.java` | 146 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 459 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/SterilizationTypeMasterDaoImpl.java` | 164 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 460 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/SterilizationTypeMasterDaoImpl.java` | 165 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 461 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/WrapperExpiryMasterDaoImpl.java` | 53 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 462 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/WrapperExpiryMasterDaoImpl.java` | 54 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 463 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/WrapperExpiryMasterDaoImpl.java` | 73 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 464 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/WrapperExpiryMasterDaoImpl.java` | 74 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 465 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/WrapperExpiryMasterDaoImpl.java` | 93 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 466 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/WrapperExpiryMasterDaoImpl.java` | 94 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 467 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/WrapperExpiryMasterDaoImpl.java` | 113 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 468 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/WrapperExpiryMasterDaoImpl.java` | 114 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 469 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/WrapperExpiryMasterDaoImpl.java` | 140 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 470 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/WrapperExpiryMasterDaoImpl.java` | 167 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 471 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/WrapperExpiryMasterDaoImpl.java` | 192 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 472 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/WrapperExpiryMasterDaoImpl.java` | 193 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 473 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/WrapperExpiryMasterDaoImpl.java` | 211 | `HibernateException` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 474 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/WrapperExpiryMasterDaoImpl.java` | 212 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 475 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/AutoclaveMasterServiceImpl.java` | 24 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 476 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/AutoclaveMasterServiceImpl.java` | 35 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 477 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/AutoclaveMasterServiceImpl.java` | 46 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 478 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/AutoclaveMasterServiceImpl.java` | 57 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 479 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/AutoclaveMasterServiceImpl.java` | 68 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 480 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/AutoclaveMasterServiceImpl.java` | 79 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 481 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/AutoclaveMasterServiceImpl.java` | 90 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 482 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/KitMasterServiceImpl.java` | 43 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 483 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/KitMasterServiceImpl.java` | 54 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 484 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/KitMasterServiceImpl.java` | 65 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 485 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/KitMasterServiceImpl.java` | 76 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 486 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/KitMasterServiceImpl.java` | 87 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 487 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/KitMasterServiceImpl.java` | 98 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 488 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/KitMasterServiceImpl.java` | 110 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 489 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/KitMasterServiceImpl.java` | 121 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 490 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/KitMasterServiceImpl.java` | 132 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 491 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/KitMasterServiceImpl.java` | 152 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 492 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/KitMasterServiceImpl.java` | 177 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 493 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/KitMasterServiceImpl.java` | 188 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 494 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/KitMasterServiceImpl.java` | 199 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 495 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/KitMasterServiceImpl.java` | 210 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 496 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/KitMasterServiceImpl.java` | 221 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 497 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/SterilizationTypeMasterServiceImpl.java` | 24 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 498 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/SterilizationTypeMasterServiceImpl.java` | 35 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 499 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/SterilizationTypeMasterServiceImpl.java` | 46 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 500 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/SterilizationTypeMasterServiceImpl.java` | 57 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 501 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/SterilizationTypeMasterServiceImpl.java` | 68 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 502 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/SterilizationTypeMasterServiceImpl.java` | 78 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 503 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/WrapperExpiryMasterServiceImpl.java` | 24 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 504 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/WrapperExpiryMasterServiceImpl.java` | 35 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 505 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/WrapperExpiryMasterServiceImpl.java` | 46 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 506 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/WrapperExpiryMasterServiceImpl.java` | 57 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 507 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/WrapperExpiryMasterServiceImpl.java` | 68 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 508 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/WrapperExpiryMasterServiceImpl.java` | 79 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 509 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/WrapperExpiryMasterServiceImpl.java` | 90 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
| 510 | `legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/service/WrapperExpiryMasterServiceImpl.java` | 100 | `Exception` | silent-swallow | peripheral | empty catch block — exception fully discarded |
