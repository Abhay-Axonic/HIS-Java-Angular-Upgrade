# surgery-master — parity rebuild of the legacy Surgery Master screen

Legacy source:
- template: `views/master/GlobalMaster/surgery-master.html` (190 lines)
- controller: `resources/assets/js/controller/opd/global/surgeryMasterController.js` (333 lines — a REAL dedicated controller with full CRUD)
- legacy state: `surgeryMaster`, module id **487** — hand-confirmed 2026-08-09:
  app.js:15542 `.state('surgeryMaster', ...)` — REGISTERED (the resolve gates
  on `item.id == 487`), and the privilege tree carries node 487 'Surgery
  Master', parent 476 (Clinical Workstation) -> 369 (Global Master),
  actionLink `#/surgeryMaster` (matches the route).
- Screen-8 selection: the next live master on the path after the allergy
  family (screens 6-7). Siblings already handled: allergyMaster (489),
  allergyTypeMaster (488), uomMaster (466), combinationMedicationMaster (472).

## A REAL screen (not a static shell)

A dedicated 333-line controller with the full CRUD: the (client-side
paginated) list, the detail fetch for edit, the save (two-step confirm
modal), the update, and the status toggle ('A'/'I'), plus the two dropdown
feeds (speciality, surgery grade). The rebuild reproduces all of it.

## Endpoints (all verified by CALLING with the cached token — 2026-08-09)

| Purpose | Endpoint | Verified |
|---|---|---|
| The list | POST /api/global/getSurgeryMasterList | 200, envelope **"success"** ([] dev) |
| Edit detail | POST /api/global/getSurgeryMasterById | 200, envelope **"success"** ([] dev) |
| Grade dropdown | POST /api/global/getSurgeryGradeMasterList | 200, envelope **"success"** (3 rows) |
| Speciality dropdown | POST /adt/getActiveSpecialityList | 200, envelope **"success"** (2 rows) |
| Save | POST /api/global/saveSurgeryMaster | mapped (SurgeryMasterController:59) — success UNVERIFIED |
| Update | POST /api/global/updateSurgeryMaster | mapped (:79) — success UNVERIFIED |
| Status toggle | POST /api/global/updateStatusForSurgeryMaster | mapped (:89) — success UNVERIFIED |

The read path is LIVE (list/detail/dropdowns all succeed). The write paths'
success with real data is **UNVERIFIED by the no-seed rule** (a real save
would write a row into public.m_surgery_master; per Q28 the owner sanctions
write tests). The rebuild sends the full legacy contract — the dates
formatted 'DD-MM-YYYY HH:mm:ss' and the user ids — so the
GlobalCommonDateUtils.getDate null-dateString NPE (the named allergyTypeMaster
mechanism) is gone by construction; the controllers' catch-and-generic-error
envelope (the campaign's #1 class) is still present on the write handlers.

## Scoped backend check (per class)

- Phantom columns/tables: **PASS** — the entity's mapped columns
  (surgery_id/name/code/code_cpt, speciality_id, surgery_grade_id, status,
  created/updated + the `crerated_date` typo the entity maps) are all in
  public.m_surgery_master (live `information_schema` confirmed); the live
  200s confirm the SELECT columns resolve.
- Named queries: GET_LIST_OF_SURGERY_MASTER and GET_SURGERY_MASTER_LIST_BY_ID
  both INNER JOIN specialityMaster and surgeryGradeMaster — a row with a
  null speciality_id/surgery_grade_id is dropped from list and detail (the
  QF-2026-08-07-L class: join drops rows with null FK). Not a phantom; the
  screens' dropdowns are required so a fully-saved row always carries both.
- Operator precedence: **FINDING (dup-check)** — GET_SURGERY_MASTER_BY_NAME
  is `unitId=:unitId AND organizationId=:orgId AND LOWER(surgeryName)=:name
  OR surgeryName=:name` — precedence binds org+unit only to the LOWER
  branch, so the exact-match branch is UNCONDITIONAL: a same-name row from
  ANOTHER org satisfies the save's dup-check (blocks a legitimate save with
  ALREADY_EXIST; also an existence oracle across orgs). Same class as
  BR-169 (Discharge Type). NOT FIXED (Rule 10) — queued as Q29.
- Org-trust: **not-checked** — the payloads carry the client org (the same
  pattern as the allergy family); the save/update/status paths have no
  VerifiedIdentityService rebind observed.
- Twin queries: **PASS** — single named query per operation.
- Swallowed exceptions on write paths: **FINDING** — the save/update/status
  handlers catch and return the generic envelope.

## Deliberate divergences (flagged, not silent)

1. **Save is one step** — the legacy's two-step confirm modal (popUpFlag /
   showModal) is not rebuilt (the serviceMaster model).
2. The status toggle maps the legacy 'A'/'I' strings onto the house toggle.
3. The legacy paginates the full list client-side (PaginationService over the
   controller's unpaginated response); the rebuild mirrors that (client-side
   page-size + pager + search filter).

## Rebuilt behaviours

- The form: Specialty (required, live dropdown), Surgery Code (required),
  Surgery Code CPT4 (required), Name (required), Grade (required, live
  dropdown); Save / Update with the visible validation messages (the
  standing help-block policy).
- The grid: 7 columns (Specialty, Surgery Code, Surgery Code CPT4, Name,
  Grade, Status, Action), the client-side search filter, the page-size
  select and pager, the status toggle, the edit.
- Dropdown load failure renders a visible load-error banner (never a silent
  empty dropdown).

## Verification state — HONEST (2026-08-09)

Gates: preflight C1-C6 (moduleId 487 in the privilege tree, C5 with the node
chain), `ng build` exit 0, smoke standard (FINAL-URL /surgeryMaster, moduleId
487 present in the store). The grid renders whatever public.m_surgery_master
holds — on dev data that is **"not erroring, usefulness UNVERIFIED"** (0
rows); nothing was seeded to look better. The write paths are wired and
their failure surfaces the backend's own envelope; their success with real
data is UNVERIFIED by the no-seed rule (Q28).
