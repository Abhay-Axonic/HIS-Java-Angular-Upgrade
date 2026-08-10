# Operation Theatre Type — screen 20 (module 521, OTIMS)

## IMPORTANT: THIS IS A REWRITE, NOT A NEW BUILD

The files in this directory were FIRST built by **Phase 4 batch 22**
(commit 32296f6f, 2026-07-27), reviewed by the owner in a full manual
Step 6 click-through against both legacy and the rebuild
(ANGULAR_PENDING_FINDINGS.md:3902, 97cdea73) with the verdict **"No
functional defects found"**, and merged — the screen is part of the 182
(now 239) screens-complete count. THIS BRANCH REWRITES THAT OWNER-APPROVED
SCREEN. It was not recognised as already present during the screen-20
selection (the preflight C5 checked only the legacy side; the C7
ALREADY-REBUILT check now added to preflight would have caught it) — the
selection failure is on the record (owner audit 2026-08-10).

## What changed FROM the owner-approved batch-22 build (named separately)

1. **CONVENTION SHIFT (the bulk of the diff — 776 insertions / 521
   deletions):** the batch-22 implementation used the pre-walk house
   conventions — `ApiResponse` generic envelope, the shared `StatusToggle`
   component, `LoaderService`, `popupArmed` confirm-modal, `ClockRow`-style
   DTO shapes. This rewrite uses the current screens-15-19 house pattern:
   signals + `FormsModule`, direct `{listObject}`-typed service responses,
   honest-error toasts that name the backend's message and correlation
   reference. Observable behaviour is otherwise unchanged on the read paths
   (same list/active/byId endpoints, same columns, same status display).
2. **update() NOW VALIDATES CLIENT-SIDE (behaviour change from the approved
   build):** the batch-22 version faithfully reproduced the legacy
   controller's missing validation on update (documented in its own header
   comment: "update() has NO client-side validation at all — faithfully
   reproduced"). This rewrite rejects an incomplete update form before
   sending. The legacy bug is not reproduced.
3. **STATUS TOGGLE NOW RELOADS THE LIST (behaviour change from the approved
   build):** the batch-22 version faithfully reproduced the legacy
   "toggle never refreshes" shape. This rewrite reloads the grid after a
   successful toggle so the row's state is visible immediately.
4. **SAVE SENDS THE FULL DTO CONTRACT + CLIENT-SIDE COMPLETENESS GUARD (the
   ONE genuinely new thing, defect-driven):** during this walk's scoped
   check, the save path was found to carry the **false-success class** —
   OperationTheatreTypeServiceImpl.saveOperationTheatreType's SUCCESS
   return (:56) sits OUTSIDE the `if(res.getObject()!=null)` guard and the
   DAO (OperationTheatreTypeDaoImpl:59-64) catch-and-returns without
   rethrowing; an incomplete DTO (updatedDate omitted) makes the DAO NPE at
   :52 and the service returns a false SUCCESS while writing NOTHING. This
   is the walk's SIXTH confirmed false-success instance, the identical
   BR-187 mechanism (fixed for the operation-theatre save as BR-187). The
   rewrite mitigates client-side: the form always sends both dates and
   rejects incomplete submissions. The backend fix is a BR candidate (same
   fix shape as BR-187 — service SUCCESS inside the guard + DAO rethrow).
   Items 1-3 are convention/modernization churn over owner-approved
   behaviour; item 4 is the only defect-driven change.

## Live verification (BY CALLING, body inspected — the ENDPOINT-VERIFICATION
STANDARD, RULES.md 5c2432fd)

- POST /api/otims/getOperationTheatreTypeList {orgId:1, unitId:1} -> HTTP 200
  body status 'success', listObject len=2 (REAL rows: id 12 'general' OT1,
  id 11 'test theatre' OT2, both status A)
- POST /api/otims/getActiveOperationTheatreTypeList -> 200 'success' len=2
- GET  /api/otims/getOperationTheatreTypeById/11/1/1 -> 200 'success' len=1
- save/update/status endpoints exist MAPPED; save is wired with the FULL DTO
  contract (both dates) because of the false-success finding above.

## Scoped backend check (per the standing rule)

- Phantom columns: PASS — all five named queries select only entity columns;
  single-table (no joins); live 200s prove resolution. DDL
  (otims.m_operation_theatre_type) matches the entity.
- INNER-JOIN drops: PASS — no joins in any query.
- Dup-check precedence + scoping: PASS — GET_OPERATION_THEATRE_TYPE_BY_NAME
  (save) and _BY_NAME_NOT_ID (update) are BOTH org+unit bound with the
  (desc OR name) branch parenthesized inside the AND chain. NOT the
  OR-precedence class; NOT cross-org reachable; scoped — unlike 519's
  BY_NAME_NOT_ID.
- Org-trust: FINDING (module-wide, already recorded with screen 17) —
  orgId/unitId are payload-carried, no VerifiedIdentityService; write paths
  payload-trusting. NOT FIXED (Rule 10).
- Swallowed exceptions: FINDING — the false-success shape on the SAVE path
  (detailed above), the walk's sixth confirmed instance, BR-187 mechanism.
  NOT FIXED (Rule 10); BR candidate.

## Divergences from the batch-22 build (the owner-approved baseline)

- House conventions (signal pattern, typed responses, honest toasts).
- update() client-side validation (legacy bug not reproduced).
- Status toggle reloads the grid.
- Full-DTO save + client-side completeness guard (the false-success
  mitigation).

## Build state

- Route: pre-registered by batch 22 (moduleId 521) — satisfied by this
  component.
- ng build: exit 0. Preflight: C1-C6 PASS (C7 ALREADY-REBUILT would now
  flag this screen — the check added after the audit; see above).
  Smoke: route serves, dev rows render, 0 page errors. Save probe:
  negative control (incomplete form rejected client-side, 0 wire posts) +
  positive (1 save post, row id 13 landed verified in psql, sequence
  12->13, cleaned per Q28 — table back to 2 rows).
