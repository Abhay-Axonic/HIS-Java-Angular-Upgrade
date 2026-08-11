# Encounter Queue EHC — screen 47 (module 686)

## Identity

- Legacy state: `encounterQueueEHC` (`legacy/.../config/app.js`) — url `/encounterQueueEHC`
- Legacy template: `legacy/HIS-WAR/src/main/webapp/views/master/GlobalMaster/Encounter/encounter-queue-ehc.html` (384 lines, 80 ng-bindings, 25 buttons)
- Legacy controller: `encounterQueueEHCController` (loaded in index.html)
- Menu: node 686 "Encounter Queue EHC" (parent 2 Opd) — **NO actionLink in the privilege tree** — DANGLE class (menu'd-unrouted, the 477/478/479 shape)
- Backend: `EncounterController` (`/api/opd`, getDailySlots / getSystemGeneratedVisitTypeId / getListOfAppointmentStatusMaster) + `NursingStationServiceController` (`/api/global/getListOfNursingStation`) + ADT speciality/doctor feeds
- Rebuild route: `encounterQueueEHC` (moduleId 686)

## Class

DANGLE — the owner's BUILD ALL SEVEN decision (2026-08-11): state + controller +
template + backend all real, the menu node carries no actionLink, and the rebuild
registers the route, restoring what the menu advertises.

## Endpoint truth (live calls, dev token)

| Endpoint | Result |
|---|---|
| `POST /adt/getActiveSpecialityList` | **200 success, 2 real rows** (cache feed) |
| `POST /adt/getDoctorListAganistSpeciality` | success, dev-empty |
| `POST /api/common/getActiveReasonMasterList` | **200 success, 1 real row** (Test Reason) |
| `GET /api/global/getListOfNursingStation` | **200 success, 1 real row** (NST1) — REQUIRES the legacy `unit_id`/`organization_id` headers in ADDITION to the JwtTokenFilter headers; userId/unitId alone → 500. The 500 is a probe-payload artifact, NOT a phantom column (table + columns exist; row NST1 returned). |
| `GET /api/opd/getListOfAppointmentStatusMaster` | **200 success, real row** (PEN / Pending) — same dual-header requirement |
| `POST /api/opd/getDailySlots` | 200 success, dev-empty — payload key is `appointmentDate` (NOT fromDate); empty criteria → 501 fall-through (screen-40 class) |
| `POST /api/opd/getSystemGeneratedVisitTypeId` | returns `1` |

## Rebuild scope

- Date + speciality + doctor slot-search surface against the live getDailySlots feed
- Reference feeds (nursing station, appointment status, reasons, specialities) rendered honestly
- Doctor list loads on speciality change
- Dev-empty feeds show "none (dev)" / "Slots not available !" — no fabricated rows

## Gates

- Preflight: C1–C9 (C3 UNVALIDATED = route on branch only; C5 DEAD-legacy = the
  preflight grep's state-name form, hand-confirmed by the two greps in the report)
- `ng build`: EXIT 0
- Probe: `tools/probe_encounter_queue_ehc.py` — PASS in real browser
