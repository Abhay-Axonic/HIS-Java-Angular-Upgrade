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
- LIVE-VERIFIED BY CALLING (body status): patientSearch WITH the legacy's
  contract ({...search fields, registrationTypeId: 1}) -> HTTP 200 body
  {"status":"success","listObject":[]} — the search endpoint is genuinely
  LIVE and WORKING. WITHOUT registrationTypeId the service falls through to
  its final `return new Response<>(ERROR, COMMON_ERROR_CODE, null, null,
  null)` (PatientRegistrationServiceImpl:753) — a null-message 501 envelope
  that is the service's MISSING-ROUTING-KEY response, not a server failure.
  The legacy always sets patientObj.registrationTypeId (1 normal / 2 OTC)
  before searching (patientRegistrationController.js:3265-3270), so the
  legacy screen works; the earlier 'honest 501' was the rebuild probe
  omitting the routing key — corrected, the screen sends registrationTypeId
  1.
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
