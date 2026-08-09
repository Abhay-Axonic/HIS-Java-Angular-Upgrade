# allergy-master — parity rebuild of the legacy Allergy Master screen

Legacy source:
- template: `views/master/GlobalMaster/allergy-master.html` (243 lines)
- controller: `resources/assets/js/controller/master/unit/allergyMasterController.js` (350 lines — a REAL dedicated controller with full CRUD)
- legacy state: `allergyMaster`, module id **489** — hand-confirmed 2026-08-09:
  app.js:16795 `.state('allergyMaster', ...)` — REGISTERED, and the privilege
  tree carries node 489 'Allergy Master', parent 476 (Clinical Workstation) ->
  369 (Global Master), actionLink `#/allergyMaster` (matches the route). In the
  role-1 set (the guard's store holds it).

## A REAL screen (not a static shell)

The legacy has a dedicated controller (350 lines) with the full CRUD: the
allergy-type dropdown, the paginated list + count, the detail fetch for edit,
the save (two-step confirm modal), the update, and the status toggle
('A'/'I'). The rebuild reproduces all of it.

## Endpoints (all verified by CALLING with the cached token — QF-2026-08-09-E)

| Purpose | Endpoint | Verified |
|---|---|---|
| Allergy-type dropdown | GET /api/global/getAllergyTypeMaster | **HTTP 500** (ERR-b137d62b) |
| The paginated list | POST /api/global/getAllergyMasterList | 200, envelope **status:"error"** "Something went wrong!!" |
| The count (pagination) | POST /api/global/getAllergyMasterListCount | 200, envelope **"success"** (works) |
| Edit detail | GET /api/global/getAllergyMasterListById/{id}/{org}/{unit} | 200, envelope "error" |
| Save | POST /api/global/saveAllergyMaster | 200, envelope "error" |
| Update | POST /api/global/updateAllergyMaster | 200, envelope "error" |
| Status toggle | POST /api/global/updateAllergyMasterStatus | 200, envelope "error" |

## FINDINGS (logged, NOT fixed — Rule 10)

1. **PHANTOM-COLUMN DRIFT (the mechanism, named)**: the named query
   GET_ALLERGY_MASTER_LIST selects `cims_code` (and cims_type) but
   `emr.m_allergy_master` has NO such columns (the DDL's columns: allergy_id,
   allergy_name, allergy_desc, status, created_by/date, updated_by/date,
   marathi_allergy_name, allergy_type_id, unit_id, organization_id...).
   Live stack (catalina.out): `PSQLException: ERROR: column allergymas0_.cims_code
   does not exist` -> SQLGrammarException. The count query works because it
   does not select the phantom. Owner decision needed: add the cims columns to
   the DDL (schema change — owner's call) or drop them from the named query.
   Meanwhile the list/detail/save/update/status all fail.
2. **SWALLOWED EXCEPTIONS**: AllergyMasterController's handlers catch and
   return the generic "Something went wrong!!" envelope (the campaign's #1
   defect class on this screen's path — the actual SQLGrammarException is
   logged, never surfaced).
3. **The rebuild is honest about the state**: every failure surfaces the
   backend's own message; the count renders (the only working call). The data
   wiring is **NOT ERRORING is false here — it errors with the backend's
   defect**; nothing was seeded to look better.

## Deliberate divergences (flagged, not silent)

1. **Save is one step** — the legacy's two-step confirm modal (popUpFlag /
   showModal) is not rebuilt (the serviceMaster model — flagged, queued with
   the others).
2. **The cims columns render unconditionally** — the legacy gated them on the
   cookieObjectAppConfig `cimsOption == 'Y'` flag; the rebuild does not load
   that config.
3. The status toggle maps the legacy 'A'/'I' strings onto the house toggle.

## Rebuilt behaviours

- The form: Allergy Name (required), Allergy Type (select), Description,
  CIMMS Code, CIMMS Type.
- The grid: 7 columns (Allergy Type, Code, Description, CIMMS Code, CIMMS
  Type, Status, Action), the client-side search filter, the page-size select
  and pager (the count works), the status toggle, the edit, the save/update.

## Verification state — HONEST (2026-08-09)

Gates: preflight C1-C6 PASS (moduleId 489 in the privilege tree; C6 marker
scan clean), `ng build` exit 0, smoke standard PASS (FINAL-URL
/allergyMaster, store 827 has489=True). The interaction probe: the count call
200s, the list/type calls return the backend's error envelopes (surfaced as
toasts) — the honest state of finding 1. Nothing seeded.
