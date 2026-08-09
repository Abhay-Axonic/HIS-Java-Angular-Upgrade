# allergy-type-master — parity rebuild of the legacy Allergy Type Master screen

Legacy source:
- template: `views/master/GlobalMaster/allergy-type-master.html` (178 lines)
- controller: `resources/assets/js/controller/master/unit/allergyTypeMasterController.js` (316 lines — a REAL dedicated controller with full CRUD)
- legacy state: `allergyTypeMaster`, module id **488** — hand-confirmed 2026-08-09:
  app.js:16767 `.state('allergyTypeMaster', ...)` — REGISTERED, and the
  privilege tree carries node 488 'Allergy Type Master', parent 476 (Clinical
  Workstation) -> 369 (Global Master), actionLink `#/allergyTypeMaster`
  (matches the route). In the role-1 set (the guard's store holds it).
- Rejected candidates (screen 7 selection): KitTypeMaster — the app.js
  registration is COMMENTED (` * .state('KitTypeMaster', ...`) — DEAD, logged
  not rebuilt; IncubationMaster was registered but not chosen (the allergy
  family's sibling was the next live master on the path).

## A REAL screen (not a static shell)

A dedicated 316-line controller with the full CRUD: the paginated list +
count, the detail fetch for edit, the save (two-step confirm modal), the
update, and the status toggle ('A'/'I'). The rebuild reproduces all of it.

## Endpoints (all verified by CALLING with the cached token — QF-2026-08-09-E)

| Purpose | Endpoint | Verified |
|---|---|---|
| The paginated list | POST /api/global/getAllergyTypeMasterList | 200, envelope **"success"** |
| The count (pagination) | POST /api/global/getAllergyTypeMasterListCount | 200, envelope **"success"** |
| Edit detail | GET /api/global/getAllergyTypeMasterListById/{id}/{org}/{unit} | 200, envelope **"success"** |
| Save | POST /api/global/saveAllergyTypeMaster | 200, envelope "error" on the EMPTY probe |
| Update | POST /api/global/updateAllergyTypeMaster | 200, envelope "error" on the EMPTY probe |
| Status toggle | POST /api/global/updateAllergyTypeMasterStatus | 200, envelope "error" on the EMPTY probe |

The read path is LIVE (the list/count/detail all succeed). The write paths'
success with real data is **UNVERIFIED by the no-seed rule** (a real save
would write a row into emr.m_allergy_type_master). RETRACTION (mechanical
cause): the initial save probe's error ("Something went wrong!!") was NOT a
backend defect — my probe payload omitted createdDate/updatedDate, which the
legacy ALWAYS sends; the DAO's GlobalCommonDateUtils.getDate NPEs on a null
dateString (live stack: `NullPointerException: Cannot invoke "String.length()"
because "dateString" is null`, parsed with "dd-M-yyyy HH:mm:ss"). The rebuild
now sends the dates + the user ids (the legacy contract); the NPE trigger is
gone by construction. The catch-and-generic-error pattern (the campaign's #1
class) is still present on the write handlers — logged with
QF-2026-08-09-H's family.

## Scoped backend check (per class)

- Phantom columns/tables: **PASS** — the entity's mapped columns
  (allergy_type_id/name/desc, marathi_allergy_type_name) are all in
  emr.m_allergy_type_master; the live 200s confirm the SELECT columns
  resolve.
- Org-trust: **not-checked** — the payloads carry the client org (the same
  pattern as the allergy master).
- Twin queries: **PASS** — single named queries per operation.
- Operator precedence: **PASS** — the list query runs live with flat
  conditions.
- Swallowed exceptions on write paths: **FINDING** — the save/update/status
  handlers catch and return the generic envelope (the empty-payload probe
  returned "Something went wrong!!").

## Deliberate divergences (flagged, not silent)

1. **Save is one step** — the legacy's two-step confirm modal (popUpFlag /
   showModal) is not rebuilt (the serviceMaster model).
2. The status toggle maps the legacy 'A'/'I' strings onto the house toggle.

## Rebuilt behaviours

- The form: Allergy Type Name (required), Description, Generic (Yes/No).
- The grid: 4 columns (Code, Description, Status, Action), the client-side
  search filter, the page-size select and pager (the count works), the status
  toggle, the edit, the save/update.

## Verification state — HONEST (2026-08-09)

Gates: preflight C1-C6 PASS (moduleId 488 in the privilege tree, C5 PASS with
the node chain, C6 marker scan clean), `ng build` exit 0, smoke standard PASS
(FINAL-URL /allergyTypeMaster, store 827 has488=True), interaction probe: the
list + count 200 with the success envelope, no unexpected toasts. The grid
renders whatever emr.m_allergy_type_master holds — on dev data that is
**"not erroring, usefulness UNVERIFIED"**; nothing was seeded to look better.
