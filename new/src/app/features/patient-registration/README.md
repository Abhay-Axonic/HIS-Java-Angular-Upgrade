# Patient Registration — screen 40 (module 3, Opd)

Parity rebuild of the legacy patient-registration screen (menu node 3
'Patient Registration', parent 2 Opd, controller patientRegistrationController
loaded index.html:897).

## CLASSIFICATION — LIVE (the OPD subtree's first candidate; the sweep
## classified it LIVE with state+node+backend; C7 verified genuinely new)

- State: REGISTERED app.js:11144 (C5 PASS — node chain 3 -> 2 Opd).
- The screen is the patient-registration TRANSACTION surface: patient
  search (the primary read), patient + kin-details registration (the save),
  update, convert.
- BACKEND BY ENDPOINT TRUTH: PatientRegistrationController (api/global):
  patientRegistration POST (save), patientSearch, getPatientDetails,
  convertPatient, updatePatient + kin-details CRUD. Controller catches
  return the honest ERROR envelope (COMMON_ERROR_MESSAGE) — clean, no
  swallow. patientSearch's service + DAO catches RETHROW (LOUD).
- LIVE-VERIFIED BY CALLING (body status): patientSearch with the legacy's
  empty-criteria payload -> HTTP 200 body {"status":"error","code":"501",
  "message":null} — the endpoint HANDLER is live (the request reached the
  service), but the empty-criteria path THROWS (root cause not yet traced —
  candidate classes: generateQueryForPatientSearch with all-empty params, or
  a phantom in GET_PATIENT_DATA). The 501-null-message envelope is the
  honest-error class (message lost in the rethrow chain), NOT a masked
  false-success.
- KNOWN SHAPE: GET_PATIENT_DATA carries the walk's INNER-JOIN drop class
  (patient.appointmentMastersList INNER JOIN — a patient with no appointment
  is dropped from search results).

## SCOPED BACKEND CHECK

The save (patientRegistration) was NOT live-probed — its payload is the full
patient+kin DTO contract; probing a patient write without the complete
contract risks a false-success reading. Honest position: 'save not
exercised, contract unknown' (flagged for the BR's scoped write-path check,
the screen-36 pattern).

## Build state

- Route: NEW at /patientRegistration (moduleId 3).
- Gates: preflight C1-C8 (C7 PASS — genuinely new; C5 PASS). ng build exit
  0. Smoke: route renders, the search surface works, the honest error state
  is shown when the backend returns its error envelope, 0 page errors.
