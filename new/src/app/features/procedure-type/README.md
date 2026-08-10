# Procedure Type — screen 21 (module 520, OTIMS)

Parity rebuild of the legacy
`views/master/GlobalMaster/OTIMS/procedure-type.html` (menu node 520
'Procedure Type', parent 514 OTIMS -> 369 Global Master, controller
procedureTypeMasterController, loaded index.html:1732).

## Selection note — the first screen picked under the C7 gate

Screen 21 was selected AFTER the owner-directed C7 sweep of the 19 OTIMS
states the walk's hand-list called "not rebuilt" (2026-08-10). The sweep
corrected the hand-list: THREE of the 19 were actually ALREADY-REBUILT under
kebab dirs the hand-list misnamed — operationTableType (523, dir
operation-table-type), anaesthesiaType (524, dir anaesthesia-type),
checklistMaster (526, dir checklist-master). procedureType was confirmed
GENUINELY NOT-REBUILT by C7 (no dir, no route on main) and selected. The
subtree's real remaining pool is 15 genuinely-not-rebuilt states (not 19).

## CLASSIFICATION (full reachability, owner-mandated order)

- State registration: REGISTERED — app.js:12385 `.state('procedureType')`,
  url /procedureType, templateUrl procedure-type.html, controller
  procedureTypeMasterController, resolve gate `item.id == 520` (app.js:12398).
- Menu node: node 520 'Procedure Type', actionLink '#/procedureType' (matches
  the state URL), parent 514 OTIMS -> 369 Global Master (privilege tree,
  psql-verified).
- Controller loaded: index.html:1732
  (resources/assets/js/controller/otims/global/procedureTypeMasterController.js).
- Backend: FULL CRUD — ProcedureTypeMasterConttroller (sic — typo'd class
  name in source, verified) `@RequestMapping("api/otims")` with
  save/update/status/list/count/byId (6 endpoints). Backend package is
  com.param.global.procedure.* (the "procedure" package, NOT otims).
- LIVE-VERIFIED BY CALLING (body inspected, ENDPOINT-VERIFICATION STANDARD):
  POST getListProcedureTypeMaster {orgId:1,unitId:1,offset:0,
  noOfRecordsPerPage:20} -> HTTP 200 body {"status":"success",...,"listObject":[]}
  (dev-empty); POST getProcedureTypeMasterCount -> 200 success object 0;
  GET byId/1/1/1 -> 200 success listObject [].
- REVISED CLASSIFICATION: the walk previously walked past 520 as
  NOT-A-SCREEN-YET — that was WRONG. 520 is LIVE: state registered, menu
  node, controller loaded, working backend (dev-empty data). The earlier
  classification is corrected here and on the record.

## Scoped backend check

- Phantom columns: PASS — all 5 named queries select only entity columns
  (ProcedureTypeMaster, otims schema); single-table FROM; live 200s prove
  resolution.
- INNER-JOIN drops: PASS — no joins in any query (list BY org+unit, status A).
- Dup-check precedence + scoping: PASS — GET_PROCEDURE_TYPE_MASTER_BY_NAME
  (save): name AND org AND unit all-AND; _BY_NAME_NOT_ID (update): id!= AND
  name AND org AND unit. Properly scoped — NOT the Q29/Q32/BR-169 class,
  NOT cross-org reachable.
- Org-trust: FINDING (module-wide class, already recorded with screen 17) —
  orgId/unitId payload-carried, no VerifiedIdentityService on the write path.
  NOT FIXED (Rule 10).
- Swallowed exceptions: FINDING — the save path has the BR-187 false-success
  shape: ProcedureTypeMasterServiceImpl:74 `return new Response(SUCCESS,
  SUCCESS_CODE, SUCCESS_MESSAGE, null, null)` sits OUTSIDE the
  `if(res.getObject()!=null)` guard (58-71); the DAO (save catch-and-return
  ERROR, no rethrow) makes an incomplete-DTO save (updatedDate omitted -> DAO
  NPE) return SUCCESS with NOTHING written. The walk's SEVENTH confirmed
  false-success instance (BR-187 mechanism). NOT FIXED (Rule 10) — backend
  BR candidate (same fix shape as BR-187: service SUCCESS inside the guard +
  DAO rethrow). The rebuild mitigates client-side: always sends both dates +
  rejects incomplete forms.
- Also noted: the dup-check at ServiceImpl:53 calls
  `response.getListObject().size()>0 && response.getListObject()!=null` —
  size() BEFORE the null check (latent NPE, caught by the outer catch -> true
  ERROR envelope, NOT a false-success).

## Divergences from legacy (named)

- House conventions: signals + FormsModule, typed service responses,
  honest-error toasts (vs the legacy growl + $scope pattern).
- update() validates client-side (legacy update has no validation — the
  batch-22 screen-20 precedent).
- Status toggle reloads the grid after a successful toggle.
- Full-DTO save (both dates) + client-side completeness guard (the
  false-success mitigation).

## Build state

- Route: NEW (registered for this screen — was absent on main, confirmed by
  C7).
- Gates: preflight C1-C7 (C7 PASS — genuinely new screen). ng build exit 0.
  Smoke: route serves, grid renders (dev-empty state), 0 page errors. Save
  probe: negative control (incomplete form rejected client-side, 0 wire
  posts) + positive (full-DTO save, row lands verified in psql, cleaned per
  Q28).
