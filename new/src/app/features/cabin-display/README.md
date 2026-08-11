# Cabin Display — screen 52 (module 760)

## Identity

- Legacy state: `cabinDisplay` (`legacy/.../config/app.js`) — url `/cabinDisplay/:orgId/:unitId/:doctorId` (path params + resolve block)
- Legacy template: `legacy/HIS-WAR/src/main/webapp/views/Nursing/cabin-display.html` (366 lines; corrected-regex check: **73 REAL Angular directives** + 39 `{{}}` bindings — genuinely data-bound, NOT the 702 CSS-class artifact)
- Legacy controller: `cabinDisplayController` (`controller/opd/transection/cabinDisplayController.js`)
- Menu: node 760 "Cabin Display" (parent 2 Opd) — **NO actionLink** — DANGLE class (owner's BUILD ALL SEVEN decision)
- Backend: `EncounterController` — `/api/opd/token/getDoctorTokenList` (also `/api/unit/applicationConfiguration/getUnitApplicationBillingConfigList`, `screen-images/{orgId}/3`)
- Rebuild route: `cabinDisplay` (moduleId 760)

## Class

DANGLE — state + controller + template + backend all real. Menu node 760 without
actionLink. Distinct module id from 747 Token Display (checkAccess(760) vs
checkAccess(747), separate states/controllers/urls) — NOT the SECOND-VIEW shape.

## Endpoint truth (live calls, dev token)

| Endpoint | Result |
|---|---|
| `POST /api/opd/token/getDoctorTokenList` `{organizationId, unitId, doctorId, currentDate}` | masked 501 "Something went wrong!!" — **PHANTOM-COLUMN class (Rule 10 finding, not fixed)**: the named query `GET_DOCTOR_TOKEN_LIST` (EncounterMaster.java:1176) selects `em.doctor_latest_call`, which **does not exist in `opd.t_encounter_master`** (information_schema verified; all 7 joined tables exist, every other column resolves). DAO catch (EncounterMasterDaoImpl.java:1926) swallows into the masked 501. Same family as screen 49's `latest_call` phantom on the same table. AMBER-permanent banner on the rebuild. |

## Rebuild scope

- Cabin token table (doctor / patient / token / cabin) wired to the live endpoint
- The feed's masked-error outcome surfaced honestly (banner + inline error)
- AMBER-permanent banner naming the phantom column + why-permanent

## Gates

- Preflight: C5–C8 PASS pre-build; final 9/9 with files present
- `ng build`: EXIT 0
- Probe: `tools/probe_cabin_display.py` — PASS in real browser
