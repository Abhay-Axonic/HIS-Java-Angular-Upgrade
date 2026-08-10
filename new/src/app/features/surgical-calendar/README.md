# Surgical Calendar — screen 22 (module 710, OTIMS)

Parity rebuild of the legacy
`views/master/GlobalMaster/OTIMS/surgical-calender.html` (note the legacy
'calender' spelling everywhere: state, templateUrl, controller, and the
backend URL mappings all use it). Menu node 710 'Surgical Calendar' under
514 OTIMS -> 369 Global Master.

## CLASSIFICATION — DANGLE (the 477/478/479 precedent), C7-verified

- State registration: REGISTERED — app.js:12670 `.state('surgicalCalendar')`,
  url /surgicalCalendar, templateUrl surgical-calender.html, controller
  surgicalCalendarMasterController, resolve gate `item.id == 710`.
- Menu node: node 710 'Surgical Calendar' EXISTS under 514 (privilege tree,
  psql-verified) but carries NO actionLink — the menu cannot navigate to it.
  The legacy menu advertises a screen the route system cannot reach.
- Controller loaded: index.html:1537
  (resources/assets/js/controller/master/global/surgicalCalendarMasterController.js).
- Backend: REAL — SurgicalCalendarMasterController
  (Global, com.param.global.otims) `@RequestMapping("/api/otims")`, 7
  endpoints: surgicalCalender (save), /list, /list/count,
  /{id}/status/{status}, /all/status, /details, /weekly — all with the
  legacy 'Calender' misspelling.
- LIVE-VERIFIED BY CALLING (body inspected, ENDPOINT-VERIFICATION STANDARD):
  POST /api/otims/surgicalCalender/list with the LEGACY payload
  {orgId,unitId,offset,recordPerPage,status:"A"} -> HTTP 200 body success
  listObject [] (dev-empty). NOTE: the list payload uses `recordPerPage`
  (NOT noOfRecordsPerPage) + status:"A" — an initial probe with
  {orgId,unitId,offset,noOfRecordsPerPage:20} 400'd (probe-payload class,
  caught before code). POST /list/count -> 200 success object 0.
  POST /adt/getActiveSpecialityList {organizationId:1,unitId:1} -> 200
  success, 2 REAL rows (specialityId 1...); POST
  /adt/getDoctorListAganistSpeciality {specialityId:1,organizationId:1,
  unitId:1} -> 200 success, 1 real row (doctorId 1). Both dropdown feeds
  LIVE with real dev data.

## Scoped backend check

- Phantom columns: PASS — 17-column DDL (public.m_surgical_calender) matches
  the entity; live 200s prove every referenced column resolves.
- INNER-JOIN drops: FINDING — GET_SURGICAL_CALENDER_LIST INNER JOINs
  public.m_day_master + doctor.m_doctor_master + doctor.m_speciality_master:
  a calendar row whose day/doctor/speciality is missing or other-org is
  DROPPED from the list. The walk's FOURTH list-path INNER-JOIN instance
  (after surgery-master, procedure-configuration, operation-theatre). NOT
  FIXED (Rule 10) — recorded.
- Dup-check: N/A — this is a slot generator (date-range x speciality x
  doctor -> time slots), not a name-keyed master; no existence-check query.
- Org-trust: FINDING (module-wide class, screen-17 record) —
  organisationId/unitId payload-carried, no VerifiedIdentityService on the
  write path. NOT FIXED (Rule 10).
- Swallowed exceptions: PASS — the save (ServiceImpl:59-82) generates slots
  in a loop and returns SUCCESS at its natural end; the outer catch returns
  ERROR (ERR_500). The mid-loop business return (ServiceImpl:84 "Please
  enter accurate slot time !!") is a TRUE ERROR, not the false-success
  shape. NOT the BR-187 class (no DAO-envelope-guard bypass).
- SINGLE-DAY-RANGE SILENT NO-OP (NEW finding, the walk's eighth
  false-success-adjacent write finding, 2026-08-10): ServiceImpl:267
  `while (startDate.isBefore(endDate))` is STRICT — a same-day range
  (fromDate == toDate, the most common scheduling case) never enters the
  loop, so the batch insert runs with ZERO rows while the save reports
  "Added Successfully." (HTTP 200 success). The legacy app schedules
  nothing and says it did. DEMONSTRATED LIVE: a 2026-08-11 -> 2026-08-11
  save returned 200 success with 0 rows written (verified in psql); a
  2026-08-10 -> 2026-08-14 save wrote 64 real rows (30-min slots, verified,
  cleaned per Q28). NOT FIXED (Rule 10) — legacy defect, fixable in the
  legacy service independently (isBefore -> isBeforeOrEqual); the rebuild
  documents it and the probe uses a multi-day range.
- Slot-start truncation (secondary observation): the multi-day save's first
  slot began at 14:30 despite a 09:00 from-time — getTimeSlot's start
  handling (ServiceImpl:288+) appears to advance past the given start.
  Observed, not root-caused, not fixed.

## Validation-defect fix (owner click-through, 2026-08-10 — the fifth
probe-passed/browser-click-failed instance, and the fix mechanism)

The owner's click-through (speciality + doctor + dates + times typed, Save)
produced "Save blocked: from and to time are required" with a fully
populated form. ROOT CAUSE (captured, not inferred): the rebuild's original
`<input type="time">` never committed user-typed values — the browser's
segmented time entry (HH:MM:-- ) requires the seconds segment to resolve
before the value commits. The inputs DISPLAYED "09:00"/"12:00" (with a
trailing "--") but the DOM value and the component model stayed EMPTY
(captured via the Angular devtools global: fromTime_dom "", model fromTime
""). The probe's page.fill() passed because it sets the value property
directly, bypassing the segmented-commit path — the probe-vs-user difference
the owner named.

FIX: the time inputs are now `<input type="text">` with an HH:MM pattern +
placeholder (matching the LEGACY template, which uses plain text inputs —
surgical-calender.html:128,136 — never a native time picker), plus a
client-side HH:MM format guard in save(). The probe now REPRODUCES THE USER
INTERACTION: it types keystrokes (press_sequentially) into the real inputs
and asserts the typed value reaches the model before Save. The exact
interaction that failed the owner now passes: typed "09:00"/"12:00" -> model
'09:00'/'12:00' -> 1 save post -> 48 rows landed (verified, cleaned per
Q28).

SECOND DEFECT (the owner's item 2): the inline message was hardcoded to the
date-range text while the form can be invalid for THREE different reasons
(missing range / missing times / malformed time) — a shared boolean
(formValid) with a single hardcoded message showed the wrong reason. Fixed
with a formError signal set by each guard; the inline text now names the
actual blocking condition.

## Divergences from legacy (named)

- House conventions: signals + FormsModule, typed service responses,
  honest-error toasts (vs legacy growl + $scope).
- Date inputs: native <input type="date"> + <input type="time"> (vs the
  legacy uib-datepicker-popup + ui-mask stack).
- The rebuild sends fromTime/toTime when provided; the legacy's full
  slot-DTO expansion (surgicalCalendarDetailsDtoList) is backend-generated
  from the date range, so the rebuild sends the top-level range payload
  (specialityId, doctorId, fromDate, toDate, organisationId, unitId) — the
  save endpoint's own generator does the rest. Divergence flagged.
- Status: the legacy grid shows an Active/Inactive toggle; the rebuild's
  list feed is status-A-filtered (the legacy list payload sends status:"A")
  — the grid is a read of active schedules, matching the legacy default.

## Build state

- Route: NEW (registered for this screen — the state was unrouted on main,
  confirmed by C7 PASS at selection).
- Gates: preflight C1-C7 (C7 PASS — genuinely new; C5 DEAD-legacy flag
  expected — the dangle). ng build exit 0. Smoke: route serves, form +
  grid render, dropdowns live-populated, 0 page errors. Save probe:
  negative control (incomplete form rejected client-side, 0 wire posts) +
  positive (full range payload, response verified, cleaned per Q28).
