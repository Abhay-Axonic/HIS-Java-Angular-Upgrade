# Counter Worklist — screen 49 (module 701)

## Identity

- Legacy state: `counterWorklist` (`legacy/.../config/app.js`) — url `/counterWorklist`
- Legacy template: `legacy/HIS-WAR/src/main/webapp/views/master/GlobalMaster/Encounter/counter-worklist.html` (187 lines, 31 ng-bindings)
- Legacy controller: `counterWorklistController` — defined at `controller/opd/transection/counterWorklistController.js` (real code: search + save-appointment flows) but NOT loaded by index.html (legacy script-wiring gap: the menu advertises the screen, the state is registered, yet the controller script is never included — clicking it in legacy fails)
- Menu: node 701 "Counter Worklist" (parent 2 Opd) — **NO actionLink** — DANGLE class (owner's BUILD ALL SEVEN decision)
- Backend: `EncounterController` (`/api/opd/getSearchedListEncounterMaster`, `NursingStationServiceController` `/api/global/getListOfNursingStation`)
- Rebuild route: `counterWorklist` (moduleId 701)

## Class

DANGLE — state + controller + template + backend all real; menu node 701 without
actionLink. Additional legacy gap surfaced: the controller script is not loaded by
index.html, so legacy clicking fails before the backend is even reached — the rebuild
restores the screen regardless.

## Endpoint truth (live calls, dev token)

| Endpoint | Result |
|---|---|
| `POST /api/opd/getSearchedListEncounterMaster` (search) | masked `{"status":"error","code":null,"message":null}` — **PHANTOM-COLUMN class (Rule 10 finding, not fixed)**: the named query `GET_SEARCH_ENCOUNTERED_LIST` selects `enMst.latest_call` and joins on `enMst.nursing_station_id`; **neither column exists in `opd.t_encounter_master`** (information_schema verified; all 28 joined tables exist). The DAO catch (EncounterMasterDaoImpl.java:424) swallows the exception into the masked envelope. AMBER-permanent banner on the rebuild. |
| `GET /api/global/getListOfNursingStation` | **200 success, 1 real row** (NST1) — legacy-header pair required (screen-47 split class) |

## Rebuild scope

- Date-range + nursing-station search surface wired to the live endpoint
- The search's masked-error outcome surfaced honestly (banner + inline error)
- Nursing station reference feed rendered (real rows)
- AMBER-permanent banner naming the phantom columns + why-permanent

## Gates

- Preflight: C5–C8 PASS pre-build; final 9/9 with files present
- `ng build`: EXIT 0
- Probe: `tools/probe_counter_worklist.py` — PASS in real browser
