# Decision Needed: Fabricated-Success-on-Exception Findings — legacy/Global (peripheral)

**Status:** Resolved — Option B approved for both Group A and Group B (15 findings total), owner decision 2026-07-06. Applied 2026-07-07: see `docs/BUSINESS_RULE_MANIFEST.md` (`BR-PENDING-global-peripheral-exceptions`) for the applied-fix detail per file. `mvn -pl Global -am compile -DskipTests -o` (run from `legacy/`): BUILD SUCCESS.

**Status (superseded):** Pending owner decision — 2026-07-06
**Related:** `docs/AUDIT_Global_exceptions.md` (peripheral findings), `docs/REVIEW_global-peripheral-exceptions.md`, `docs/BUSINESS_RULE_MANIFEST.md` BR-PENDING-global-peripheral-exceptions
**Precedent:** BR-007/BR-008 (`collectPayment`), Global billing-critical's held-back findings (BR-021 cycle), BR-028/BR-028-followup (Abdm `AbdmCredentialDaoImpl`)

## Why this is held back

During the automated red-flag sweep of all 8,040 peripheral findings in `docs/AUDIT_Global_exceptions.md` (a brace-aware structural parser checking every flagged catch body for a `SUCCESS`-status token, cross-verified line-by-line against the audit and spot-checked by direct source read), **15 findings across 12 files** turned out to be genuine fabricated-success-on-exception: the catch block returns a `Response`/`ResponseTwilio` object with a `SUCCESS` status despite a real exception having just occurred, exactly the class of finding the project has repeatedly held back rather than force-fixed (BR-008, BR-012, BR-021's `UnitServiceTariffMasterDaoImpl`, BR-028's `AbdmCredentialDaoImpl`).

**None of these 15 lines have been touched.** They were explicitly excluded from every parallel batch-fix agent's assignment list in this fix cycle and independently re-verified untouched afterward.

Two sub-groups have materially different risk profiles and likely warrant different options, mirroring the asymmetric-by-risk-tier approach the owner already took in BR-008 and BR-012.

## Group A — downstream MyLife-sync push, primary save already succeeded (6 findings, 6 files)

In each of these, the *primary* local database write (saving allergy/vital/medication/family/medical/surgical history against a patient encounter) has **already completed successfully** by the time execution reaches the try/catch in question. The try/catch wraps only a *secondary*, best-effort push of that same data to an external "MyLife" system over HTTP. Today, if that secondary push fails, the method still reports `SUCCESS` back to its caller — arguably reasonable, since the encounter data the caller actually asked to save was in fact saved; the sync failure is a separate, currently-invisible problem.

| # | File | Line | Method (containing try) |
|---|------|------|--------------------------|
| 1 | `legacy/Global/src/main/java/com/param/opd/coversheet/dao/AllergyPatientMapperDaoImpl.java` | 433 | save…AllergyPatientMapper (MyLife sync push) |
| 2 | `legacy/Global/src/main/java/com/param/opd/coversheet/dao/AppointmentVitalMapperDaoImpl.java` | 278 | save…AppointmentVitalMapper (MyLife sync push) |
| 3 | `legacy/Global/src/main/java/com/param/opd/coversheet/dao/MedicationHistoryDaoImpl.java` | 209 | save…MedicationHistory (MyLife sync push) |
| 4 | `legacy/Global/src/main/java/com/param/opd/coversheet/dao/PatientFamilyHistoryDaoImpl.java` | 456 | save…PatientFamilyHistory (MyLife sync push) |
| 5 | `legacy/Global/src/main/java/com/param/opd/coversheet/dao/PatientMedicalHistoryDaoImpl.java` | 465 | save…PatientMedicalHistory (MyLife sync push) |
| 6 | `legacy/Global/src/main/java/com/param/opd/coversheet/dao/PatientSurgicalHistoryDaoImpl.java` | 247 | save…PatientSurgicalHistory (MyLife sync push; catch also has `System.out.println(e)`, console-only) |

Note: `whatsAppServiceImpl.java`'s 6 findings (Group B below) are a different shape — there the *entire* operation (the message send itself) is inside the try, not a secondary side-effect.

**Recommended options, same shape as BR-008 Group A:**
- **Option A (standard fix):** convert to `logger.error(...); throw new ServiceException(...)`. Changes today's "encounter data saved, HTTP 200" outcome into an HTTP 500 for a MyLife sync problem that has nothing to do with whether the caller's actual request succeeded — likely an over-correction and a genuine Behavioral Change with user-facing impact (clinical UI would show an error for data that in fact saved).
- **Option B (recommended):** keep the response exactly as-is (still reports the true outcome of the primary save), but replace the empty/console-only catch body with real logging (`logger.error(...)`) so the sync gap becomes visible in the centralized log for the first time, instead of disappearing silently (or to console-only in file #6). No response/behavior change — Refactoring (Safe) for the 5 empty-catch cases; for file #6 (`System.out.println(e)` removed and replaced by `logger.error`), also Refactoring (Safe), since no caller-visible behavior changes.
- **Option C:** same as B, but also add a distinct, greppable log marker (e.g. `MYLIFE_SYNC_GAP`) so staff could build a manual reconciliation report, matching the `PAYMENT_RECONCILIATION_GAP` precedent from BR-012 Group A.

## Group B — entire operation fails, still reports SUCCESS (9 findings, 4 files)

Here the try block *is* the whole operation — there is no already-succeeded primary step underneath. Reporting `SUCCESS` on exception here means the caller is told an action worked when it did not.

| # | File | Line | What actually failed |
|---|------|------|------------------------|
| 7 | `legacy/Global/src/main/java/com/param/global/controller/OrganizationWebRegistrationController.java` | 25 | `createOrganizationFromWeb` — org creation itself threw (`ClassCastException` caught specifically), still returns `Response(SUCCESS, SUCCESS_CODE, null, null, null)` |
| 8 | `legacy/Global/src/main/java/com/param/global/controller/TwilioController.java` | 217 | `deleteParticipants1` — disconnecting the Twilio video participants threw, still returns `ResponseTwilio(SUCCESS, SUCCESS_CODE, null, null)` |
| 9–14 | `legacy/Global/src/main/java/com/param/global/unit/service/whatsAppServiceImpl.java` | 121, 179, 238, 343, 508, 630 | 6 separate WhatsApp-send methods — the send itself threw, each still returns `Response(SUCCESS, SUCCESS_CODE, null, null, ...)` |
| 15 | `legacy/Global/src/main/java/com/param/global/unit/dao/UserRoleMasterDaoImpl.java` | 667 | a button-master/AI-prompt lookup query threw, still returns `Response(SUCCESS, null, errorMsg, null, null)` — the real error message is stuffed into the `message` field, but the `status` field callers actually branch on is still `SUCCESS` |

**Recommended options, same shape as BR-012 Groups B/C and the Abdm BR-028 follow-up:**
- **Option A (standard fix):** `logger.error(...); throw new ServiceException(...)`. Turns each of these into a real HTTP 500. This is a bigger blast-radius change than Group A since these operations have no other success signal to fall back on — but it is also the most honest fix, and none of the 6 whatsApp/Twilio/org-registration/button-lookup flows appear to be on a path the circuit-breaker in `RULES.md` treats as billing-critical.
- **Option B (recommended, matches Abdm's resolved BR-028-followup):** keep the `Response`-returning contract (no `throw`, no HTTP-500 change), but replace the fabricated `SUCCESS` with an honest `ERROR`/`ERROR_CODE` status and add `logger.error(e.getMessage(), e)`. Preserves today's "caller always gets a `Response` object back, never a 500" contract while making the status field trustworthy and the failure observable server-side. `UserRoleMasterDaoImpl.java:667` already half-does this (its `message` field carries a real error string) — Option B here just also flips its `status` from `SUCCESS` to `ERROR`.
- **Option C:** same as B, but only for the 6 WhatsApp findings (which appear to share one method-shape) as a batch, vs. handling Twilio/org-registration/button-lookup individually — offered in case the owner wants to treat WhatsApp notification failures as lower-stakes than the others (a failed WhatsApp send has no clinical/financial impact, unlike org registration).

## What has NOT changed (historical — see Resolved status above)

No code at any of the 15 lines above had been modified by this fix cycle as of 2026-07-06. All 15 remained exactly as audited, pending this decision.

## Resolution applied (2026-07-07)

Option B applied to all 15 findings, exactly as recommended and approved:

- **Group A (6 findings, 6 files):** response/behavior left exactly as-is (still returns `SUCCESS`/`COMMON_SUCCESS` for the primary save, which genuinely succeeded) — each empty or console-only (`System.out.println(e)`, `PatientSurgicalHistoryDaoImpl.java` only) catch body now calls `logger.error(...)` with the causing exception, so the MyLife sync gap is visible in the centralized log for the first time. No logger needed to be added — all 6 files already had a class-level `org.apache.log4j.Logger`.
- **Group B (9 findings, 4 files):** the `Response`/`ResponseTwilio`-returning contract is unchanged (no `throw`, no HTTP-status change), but the fabricated `SUCCESS`/`SUCCESS_CODE` status is now the honest `ERROR`/`ERROR_CODE` status, with `logger.error(e.getMessage(), e)` added at each site. `UserRoleMasterDaoImpl.java:697` (button-master/AI-prompt lookup) already carried the real error string in its `message` field — only its `status` field changed, from `SUCCESS` to `ERROR`. `OrganizationWebRegistrationController.java` had no logger before this fix; a class-level `org.apache.log4j.Logger` (matching the codebase's existing convention) was added. `TwilioController.java` and `whatsAppServiceImpl.java` already had one.

Verified via `mvn -pl Global -am compile -DskipTests -o` (run from `legacy/`): BUILD SUCCESS, no unreachable-statement errors (none of these 15 fixes removed a `throw` or `return`, only added logging or changed a status-field value in place, so no reachability analysis was triggered).
