# Data Truth Report

## Exception Handling Baseline (verified by direct sampling, not by prior reports)

**Why this section exists**: `/legacy-codewhale-attempt` (the prior failed upgrade attempt) contains unreliable, sometimes corrupted "certification" reports, some of which falsely claim completed work. This baseline was produced entirely by reading the current `/legacy` source directly — no in-repo report, comment, or prior claim of "already fixed" was consulted or trusted.

**Method**: Randomly sampled and fully read 21 files across 7 modules (Billing, ADT, Pharmacy, Global, Inventory, BloodBank, OTIMS) — 3 files per module, selected from all files in each module containing the word `catch`. Every catch block in every file was classified by hand (not by regex alone) as one of:

- **Silent swallow** — empty body, no logger call, no rethrow; the exception's message/stack trace never reaches any log or the caller (or is replaced by a generic hardcoded string).
- **Locally converted, not routed centrally** — the exception's message or the exception object itself is embedded in the response returned to the caller, but there is no server-side log entry and the exception never reaches `GlobalExceptionHandler`.
- **Compliant** — the catch rethrows (`throw e;` / `throw he;`), so the exception propagates to `GlobalExceptionHandler` as the constitution requires.

## Result

| Classification | Files | % of sample |
|---|---|---|
| Contains ≥1 silent swallow | 10 / 21 | **48%** |
| Locally converted, not centrally routed (no silent swallow, but no logging/routing either) | 7 / 21 | 33% |
| Fully compliant (every catch rethrows) | 3 / 21 | 14% |
| Mixed (file has both a silent swallow and a rethrow) | counted within the 10 above | — |

(One file, `OtMealDaoImpl.java`, has one of each — one silent, one rethrow — and is counted in the "≥1 silent swallow" bucket since one truly silent catch is enough to lose data.)

### Files with confirmed silent swallowing (read in full, not grepped)

| File | Module | Detail |
|---|---|---|
| `com/param/opd/coversheet/dao/InfusionSolutionDaoImpl.java` | Global | 7 methods, each with a **double** empty catch — `catch (HibernateException he) {}` then `catch (Exception e) {}` — 14 empty blocks in one file. Worst case found. |
| `com/param/bloodBank/controller/ReasonOfDiscardMasterController.java` | BloodBank | 7/7 methods swallow silently, falling through to `new Response(ERROR, null, null, null, null)` — error code and message are both `null`. |
| `com/param/adt/mortuary/controller/MortuaryController.java` | ADT | 8/8 methods, completely empty `catch (Exception e) {}`. |
| `com/param/ER/service/ERPatientListServiceImpl.java` | ADT | 5/5 methods, empty catch, falls through to a generic `Response` with null code/message. |
| `com/param/bloodBank/dao/TransfusionTransmittedDiseasesDaoImpl.java` | BloodBank | 6/6 catch blocks discard the exception; some fall through to a hardcoded generic error message with no trace of the real cause. |
| `com/param/pharmacy/narcotics/deliveryWorklist/dao/NarcoticsDeliveryWorklistDao.java` | Pharmacy | 5/5 methods return `null` on any exception, no trace kept anywhere. |
| `com/param/pharmacy/narcotics/acceptanceOfEmptyAmpoules/dao/AcceptanceEmptyAmpoulesDao.java` | Pharmacy | 4/4 present catch blocks empty, return `null`. |
| `com/param/otims/controller/ChecklistMasterController.java` | OTIMS | 4/4 methods, empty catch, generic fallback response. |
| `com/param/bloodBank/controller/CrossMatchRegisterController.java` | BloodBank | 5/5 methods, empty catch, generic hardcoded error message (no `e.getMessage()`, no logging). |
| `com/param/mrd/dao/MrdFileManagementDaoImpl.java` | ADT | Mixed: some catches use `e.printStackTrace()` (stderr only, not the app logger, not centrally routed) and embed `e.getMessage()` in the response — not empty, but still never reaches the centralized handler. |

### Most important finding: the centralized handler itself swallows errors

`HIS-WAR/src/main/java/com/param/exception/GlobalExceptionHandler.java` — the single centralized exception handler the constitution requires all exceptions to route to — contains:

- Line ~121–123: `catch (Exception e) { }` — empty, inside the branch that tries to write the error log via `backLogService.createErrorLog(...)`. If logging the original error throws, that secondary failure is discarded with zero trace.
- Line ~138–141: `catch (Exception e) { return null; }` — the handler's own top-level catch-all swallows and returns `null` rather than a `ResponseEntity`, meaning the client receives no response body/status on this path.

This means even exceptions that *are* correctly rethrown up to the centralized handler can still vanish silently if the handler's own logging path fails — a single point of failure for the constitution's "no error may be silently swallowed, ever, anywhere" rule.

### A related pattern worth flagging separately (not silent swallowing, but a different risk)

7 of the 21 files (Inventory's `StoreIndentPendingController`, `MaterialReturnController`, `MaterialPickerController`; Pharmacy's `PatientIssueReturnController`; OTIMS's `OTIMSDashboardController`; Global's `GrowthChartController` and `VitalsController`) catch the exception and embed it (or its message) directly into the HTTP response body returned to the API client, e.g. `new ErrorResponse("HIS-1441", "...", ex)`. This isn't silent — the caller does get error detail — but it (a) never produces a server-side log entry, (b) never reaches `GlobalExceptionHandler`, and for the three that embed the raw exception object, risks leaking internal stack traces to the client.

## Conclusion

The premise behind Mission Priority #1 in `CLAUDE.md` is confirmed by direct, independent sampling: roughly half of a random cross-module sample silently swallows exceptions today, and the centralized handler that is supposed to be the single safety net has its own silent-swallow bugs. This is a baseline measurement only — no remediation has been made, and this file describes `/legacy` (the clean pre-upgrade baseline), not `/legacy-codewhale-attempt`.
