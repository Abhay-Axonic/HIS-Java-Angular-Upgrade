# ER MODULE ASSESSMENT — 2026-08-14 (PART 1 diagnosis + PART 2 workflow map)

SERVED-BRANCH throughout: `fix/patient-type-details-status` @ e654b8b1 (unchanged;
the legacy tests ran at :8080 through the app's own login flow).

## PART 1 — the symptom (registered in ER, saved, absent from the ER patient list)

**VERDICT: (a) the write never landed — PRIMARY — plus (c) the list would have
hidden it anyway. (b) the cache is NOT implicated.**

- **(a) psql arbiter:** patient.t_patient_registration max created_date
  **2026-08-07** (3 rows), patient.t_patient_details **2026-08-04** (1 row),
  patient.m_unknown_patient_registration **0** — NO row from the owner's click
  (today is 2026-08-14). The phantoms are live: `family_id` is ABSENT from
  patient.t_patient_details (phantom #6) and the phantom-#19 set
  (area_id/city_id/zip_code/phone_code/phone_number) is ABSENT from
  patient.t_patient_registration — the registration's INSERT cannot complete;
  the "saved" toast was the masked-501 class. The ER-Encounter's own save
  paths are saveUnknownPatient / saveNormalPatientRegistration (the shared
  registration service) — both land in the phantom-blocked tables.
- **(c) the list's filter — CONFIRMED, a second independent cause:**
  GET_LIST_OF_ER_PATIENTS (Discharge.java:1066) reads the ADMISSION rows with
  `visit_type_id=4 AND bed_number IS NOT NULL AND t_patient_id IS NULL` — a
  fresh registration is invisible BY DESIGN (registration → ER admission →
  bed happens later). The same query in psql: ER admissions (visit 4) = **0**;
  the endpoint's live call also returns nothing. The list is consistent with
  the DB — it filters correctly; there is nothing to list because (a) failed.
- **(b)** the no-expiry Guava cache class is real (proven today on the ward
  and patient-type lists) but NOT this symptom — there is no row to be stale.
- The live reproduction drive was attempted (the owner's click path at :8080)
  but the legacy's login-group rate limiter 429s consecutive logins — the
  psql arbiter + the live phantoms + the query analysis settle it without it.

## PART 2 — the ER workflows, step by step (the legacy's state sequence)

The legacy ER states (app.js): unknownPatientReg, triageDisplay, erPatientList,
mapOfBedForER, MLCReportingWorklist, ERdueForDischarge, erMarkForDischarge,
ERreadyForBilling, ERfinalDischarge, Er-PatientDashboard. The rebuild has TWO
of these: mlcReportingWorklist + erPatientDashboard (a stats screen — the
admission/discharge/death + OPD-visit counts — NOT the patient list).

| Workflow | Step | Legacy state + controller | Rebuilt route | Read/write T0 | Output visible to the next step? | Blocker |
|---|---|---|---|---|---|---|
| 1. Registration → ER list | register | unknownPatientReg (unknownPatientRegController: saveUnknownPatient / saveNormalPatientRegistration) | **NONE** | write 501s (family_id phantom) | NO — no row lands (a); and the list reads admissions+bed (c) — a registration alone is invisible by design | DRIFT (phantoms #6 + #19 — the backend owner's queue) + REBUILD-ABSENT |
| 1. …→ ER list | list | erPatientList (erPatientListController: /adt/getListOfERPatients) | **NONE** (erPatientDashboard is stats-only) | read = 0; psql same-query = 0 (visit-4 admissions 0) | the list is the ER census — feeds triage/discharge | DRIFT upstream + REBUILD-ABSENT |
| 2. Triage | triage | triageDisplay (triage controller; the list's current_triage_id column) | **NONE** | not smokeable (absent in the rebuild) | the triage category would flow into the ER list's triage column | REBUILD-ABSENT |
| 3. Assessment + notes | assess | the ER-Encounter's continuation (assessment/notes in the legacy's clinical workstation) | **NONE** | not smokeable | the assessment feeds the disposition | REBUILD-ABSENT |
| 4. Bed assignment | assign | mapOfBedForER (the ER bed map) | **NONE** | not smokeable | the bed is a precondition of the ER list's rows (bed_number NOT NULL) | REBUILD-ABSENT |
| 5. Discharge ladder | discharge | ERdueForDischarge → erMarkForDischarge → ERreadyForBilling → ERfinalDischarge (4 states) | **NONE** | the ladders read the ER admissions — 0 | each step's output (mark/release) flows to the next | REBUILD-ABSENT + DRIFT upstream |
| 6. MLC path | flag | the ER-Encounter's MLC dropdown (priorityListForMLC) | **NONE** (the flag's write is upstream) | — | the flag would flow into the MLC worklist | DRIFT upstream (no landed registration → no MLC row) |
| 6. …→ worklist | report | MLCReportingWorklist (MLCReportingWorklistController) | **mlcReportingWorklist ✓** | READ 200 (getPatientListMedicolegalDetailsMLCWorklist — live-captured); the worklist's data = 0 (no ER rows) | the worklist is a READ-ONLY island — the legacy's report-generation write is NOT rebuilt | REBUILD-PARTIAL (read-only) + DRIFT upstream |
| 7. Transfer to IP | transfer | the legacy's transfer flow | transfer-screen exists (generic) | not smokeable for the ER-specific path | the transfer closes the ER episode | REBUILD-PARTIAL (generic only) |

**Workflow verdicts (WIRED = a row created at step 1 is visible at step 2
through the UI):** registration→ER-list NOT WIRED; triage NOT WIRED;
assessment/notes NOT WIRED; bed assignment NOT WIRED; discharge ladder NOT
WIRED; MLC path NOT WIRED (the worklist's read works but nothing feeds it and
it has no write); transfers NOT WIRED. The ER module is the least rebuilt
module — 2 of 9 legacy ER states exist in the rebuild, both read-only.

## Fixes SPECIFIED (NOT applied — the owner decides)

1. The registration write: phantoms #6 (family_id) + #19 — already in the
   backend owner's queue (the 19-phantom handover); no frontend fix exists.
2. The ER module's rebuild gap: the 7 absent ER states (triage, ER list, bed
   map, the discharge ladder) — a scoping decision (the menu-generation
   feasibility's "groups never rebuilt" class). The one rebuilt ER workflow
   (MLC) needs its write + the upstream flag path to become a workflow.
