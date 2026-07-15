# Review: Abdm module exception-handling fix

*2026-07-06. Applies the pattern already approved for Billing (BR-006 et seq.), Global (BR-021), ADT (BR-022), BloodBank (BR-023), CSSDMaster (BR-024), Pharmacy (BR-025), DietAndKitchen (BR-026), and OTIMS (BR-027) to 305 of the 310 findings from `docs/AUDIT_Abdm_exceptions.md`.*

## Summary

- **Total files touched:** 10 of the module's 47 files (the only 10 with any findings at all, per the audit)
- **Total findings fixed:** 305 of 310 (98.4%) — **5 held back, full decision document written, no code touched at those 5 locations**
- **Exception type used:** `ServiceException` (`com.param.global.common.ServiceException`) for 9 of the 10 touched files; `BillingException` (`com.param.global.billing.controller.BillingException`) for `AbdmCredentialController.java` only (4 findings), matching that file's own pre-existing convention — it already imports `BillingException` and rethrows it via `catch (BillingException be) { throw be; }` in ~90 of its other, non-finding methods
- **Logger convention:** `org.apache.log4j.Logger`, class-level `private static final Logger logger = Logger.getLogger(<ClassName>.class)`, added to all 10 touched files — Abdm had 0/47 files with a real logger before this fix (confirmed by the audit)
- **Compile result:** `mvn -pl Abdm -am compile -DskipTests -o` (run from `legacy/`) — **BUILD SUCCESS**, after 18 iterative unreachable-statement fixes in `AbdmCredentialDaoImpl.java` (see below)
- **Verification:** `grep -rc "logger.error" legacy/Abdm` = 305, exactly matching the routine finding count 1:1. `throw new ServiceException` = 301, `throw new BillingException` (newly added) = 4 — 301 + 4 = 305.

## Per-file finding counts

| File | Findings | Routine fixed | Held back |
|---|---|---|---|
| `AbdmCredentialDaoImpl.java` | 185 | 181 | 4 (fabricated success) |
| `AbdmCredentialServiceImpl.java` | 111 | 111 | 0 |
| `AbdmCredentialController.java` | 4 | 4 | 0 |
| `AbdmCommonUtil.java` | 3 | 2 | 1 (fabricated success) |
| `DiagnosticReportImagingDcmSample.java` | 2 | 2 | 0 |
| `OPConsultNoteSample.java` | 1 | 1 | 0 |
| `.../fidelius/FideliusApplication.java` | 1 | 1 | 0 |
| `.../fidelius/Utils.java` | 1 | 1 | 0 |
| `.../fidelius/dercyprion/DecryptionController.java` | 1 | 1 | 0 |
| `.../fidelius/encryotion/EncryptionController.java` | 1 | 1 | 0 |
| **Total** | **310** | **305** | **5** |

## The 5 held-back fabricated-success findings

Per the task's explicit instruction, these 5 were not given the standard fix. Each catch block returns a hardcoded `Response(SUCCESS, SUCCESS_CODE, null, null, null)` on a real caught exception — a strictly worse pattern than the other 305 findings, since the caller cannot distinguish "succeeded with no data" from "silently failed." Converting a fake success into a real thrown error is a genuine behavior change on live ABDM credential/session/patient-lookup flows, so this needed the owner's judgment rather than the mechanical fix. Full analysis and options in `docs/DECISION_NEEDED_Abdm_FabricatedSuccess.md`:

- `AbdmCommonUtil.java:150` (renumbered by earlier edits in the same file; method `getCredentialDetails`)
- `AbdmCredentialDaoImpl.java:360` — `getCredentialDetails`
- `AbdmCredentialDaoImpl.java:907` — `onFetchModes`
- `AbdmCredentialDaoImpl.java:1084` — `getCredentialDetailsByHealthId`
- `AbdmCredentialDaoImpl.java:1548` — `checkPatientAlreadyExist`

All 5 were individually re-read in the current file state before this review was written, to confirm the code still fabricates `SUCCESS` exactly as the audit described and that nothing was accidentally touched by the mechanical fix pass applied to the surrounding catches in the same files.

## `AbdmCredentialDaoImpl.java` — the largest file in this entire audit series

At 25,382 lines with 185 findings, this file required a different, more careful process than a blind pattern-apply:

1. **A comment/string-aware brace-matching parser** (Python, matching the audit's own stated methodology) was built to locate every `try`/`catch` boundary reliably — a naive brace counter miscounted due to braces inside comments (`// ... catch ...`) sitting between a try's closing brace and its catch keyword. The parser's output was cross-checked against the audit's own 185 line numbers and matched **exactly**, with zero discrepancies in either direction, before any edit was made.
2. **The catch-body replacement was applied in isolation** — only the text inside each of the 181 routine catch bodies was replaced with `logger.error(e.getMessage(), e); throw new ServiceException(e.getMessage());` (or `ex` where that was the catch's own declared variable name, 2 occurrences). No trailing code outside the catch bodies was touched in this pass, specifically to avoid guessing at reachability across a file this size.
3. **Compilation was run immediately after**, and every one of the resulting 18 "unreachable statement" errors was opened and read in its surrounding method context individually — not batch-deleted — before removing the now-dead trailing `return` statement. In every one of the 18 cases, the enclosing try block's own last statement was itself a `return` (or, for 2 nested try/catch pairs, the inner try/catch's only two possible outcomes were both a `return` or a `throw`), so the outer trailing return had become genuinely, provably unreachable once the catch started unconditionally throwing. No trailing return in this file was found to be reachable via a separate non-exceptional path requiring preservation (unlike BR-007/BR-011/BR-015/BR-022/BR-025's precedents in other modules) — every one of the 18 was deleted.
4. **A final full recompile** (`mvn -pl Abdm -am compile -DskipTests -o`) confirmed BUILD SUCCESS with zero remaining errors, and a second no-op recompile confirmed nothing further needed to change.

## `AbdmCredentialServiceImpl.java` — near-total uniformity

111 of the module's 310 findings live in this file, described by the audit as "~100 near-identical ABDM API wrapper methods, a copy-pasted template." Structural verification (the same brace-matching parser) confirmed this precisely: of 114 total `catch (Exception e)` blocks in the file, 111 are findings and 3 already properly rethrow (`saveConsentsHipNotify`, `saveHealthInformationHipRequest`, `searchByMobile` — left completely untouched). Every one of the 111 findings' try blocks consists of exactly one statement, `return iAbdmCredentialDao.<method>(...);` — meaning in every case, once the catch unconditionally throws, the immediately-following trailing `return` (either `return new Response(ERROR, ERROR_CODE, SERVER_ERROR, null, null);` in 108 methods, or `return null;` in the 8 methods whose catch was a bare `// TODO: handle exception` comment) becomes unreachable and was deleted — confirmed with a from-scratch compile, zero unreachable-statement errors on the first attempt for this file.

## `AbdmCredentialController.java` — the module's own "good" pattern, mostly intact

Of this file's 177 catch blocks, 173 already properly `throw`/rethrow (`catch (BillingException be) { throw be; } catch (Exception e) { throw e; }`), demonstrating the module's correct rethrow-to-caller pattern working almost everywhere it's used. Only 4 methods deviate — their `catch (Exception e)` was `e.printStackTrace();` with no throw, immediately followed by a `return null;` that also became unreachable once fixed and was deleted. Fixed using `BillingException` (not the module-wide default `ServiceException`) specifically to match this file's own dominant, already-established convention.

## Small peripheral files (6 files, 1–2 findings each)

`DiagnosticReportImagingDcmSample.java`, `OPConsultNoteSample.java`, and the 4 vendored `in.projecteka.fidelius` files (`FideliusApplication.java`, `Utils.java`, `DecryptionController.java`, `EncryptionController.java`) each received the standard fix at their 1–2 findings. Two of note:

- `Utils.readArgsFromFile`'s `FileNotFoundException` catch previously printed a message and fell through to `while (readFromFile.hasNextLine())` — since `readFromFile` is `null` when the exception fires, this was a pre-existing latent `NullPointerException` bug; the fix (throw immediately) incidentally prevents that NPE from ever being reached, as a side effect of the standard routing fix rather than a separate deliberate change.
- `DecryptionController.java`'s single finding is inside a `static { }` initializer block (loading the BouncyCastle security provider) — throwing an unchecked `ServiceException` there is valid Java and surfaces as a class-loading failure if it ever fires, consistent with "no error may be silently swallowed" rather than a new failure mode.

## Non-findings correctly left untouched

- The 3 properly-rethrowing catches in `AbdmCredentialServiceImpl.java` (listed above).
- The 173 properly-rethrowing catches in `AbdmCredentialController.java`.
- The 187 module-wide properly-rethrowing catches identified by the audit, none of which were touched by this fix.
- Dead/commented-out catch blocks (3 in `AbdmCredentialDaoImpl.java`'s abandoned scratch code, 1 in `DiagnosticReportImagingDcmSample.java`'s example snippet) — matching the audit's own exclusion of dead code.
- The 5 fabricated-success findings — see above.

## Patterns worth flagging to the owner (not fix-blocking)

- **189 billing-critical findings, the highest concentration of any module in this series (61% of the module's total)** — driven entirely by file-level propagation from `AbdmCredentialDaoImpl.java`'s `v3DataFlowHealthInformationHipOnRequest` method, which constructs real `ItemDetailsResponse`/`BillListResponse` billing DTOs to push an "Invoice" record into the ABDM health-information-exchange bundle. This fix does not change that method's billing computation — only its (and every other method's, by file-level convention) exception-routing.
- **The 5 fabricated-success findings** — see `docs/DECISION_NEEDED_Abdm_FabricatedSuccess.md`.
- **`Utils.readArgsFromFile`'s latent NPE** — see above; incidentally prevented, not separately fixed, by this pass.
