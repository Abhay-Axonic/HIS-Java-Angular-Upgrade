# Procedure Template — screen 25 (module 515, OTIMS)

Parity rebuild of the legacy
`views/master/GlobalMaster/OTIMS/procedure-template.html` (state registered
app.js:20447 — url /procedureTemplate, controller procedureTemplateController
loaded index.html:1743, title ' Procedure Configuration', params.commonObj).

## CLASSIFICATION — DANGLE-ORPHANED (C7-verified; the last OTIMS dangle-with-backend)

- State registration: REGISTERED — app.js:20447 `.state('procedureTemplate')`
  with a `params.commonObj` mechanism (a drill-down: the legacy controller
  reads `$scope.common` populated by the PREVIOUS screen).
- Menu node: NO node links #/procedureTemplate. Node 515 ' Procedure
  Configuration' (parent 514) links '#/procedureConfiguration' — a state
  that does NOT exist (dead link); node 696 'Procedure Configurations' has
  no actionLink. The parent screen (procedureConfiguration) is ALSO
  unrouted in the new app. The rebuild registers the route so the state is
  reachable by URL.
- Backend: ProcedureTemplateController (OTIMS, com.param.otims)
  `@RequestMapping("/api/otims")` — saveProcedureConfigTemplate,
  getProcedureConfigTemplate, getProcedureConfigList, getOTTThetreAndTableByServiceId,
  getProcedureConfigureOnOTBillingCompletion.

## PHANTOM-BROKEN CORE — the walk's SIXTH phantom finding

LIVE-VERIFIED BY CALLING (body inspected, ENDPOINT-VERIFICATION STANDARD):
GET /api/otims/getProcedureConfigList/1/1 -> 200 success [] (dev-empty — the
parent list works); POST /api/otims/getProcedureConfigTemplate {orgId:1,
unitId:1,procedureConfigId:1} -> HTTP 500 {"status":"error","code":"500",
"message":"Something went wrong. Reference: ERR-5daf79ff"}; POST
/api/otims/saveProcedureConfigTemplate -> 500 ERR-f314d544. Both 500s are
"could not extract ResultSet" (SQLGrammarException) at the controller lines.

ROOT CAUSE: GET_CHECKLIST_LIST_BY_PROCEDURE_CONFIG_ID
(ProcedureConfigurationChecklist.java:21-44) selects
`otimsRoleMaster.roleDesc as roleName` (line 27) via a 3-way INNER JOIN
(checklist -> checklistMaster -> roleMaster), but public.m_role_master's DDL
column is role_name — role_desc DOES NOT EXIST (RoleMaster.java:25 maps
roleDesc; the DDL never got a role_desc column). The phantom surfaces on
every getProcedureConfigTemplate call, and saveProcedureConfigTemplate calls
getProcedureConfigTemplate internally — so BOTH the read and the write 500.
FAILURE MODE: LOUD — HTTP 500 correlation-id envelopes, NOT the screen-17
masked shape. Same class as screens 17/24's phantoms. NOT FIXED (Rule 10) —
BR-190-class candidate (phantom columns).

SECONDARY: the OT/table feed getOTTThetreAndTableByServiceId is GET-mapped
and 405'd on a GET call in one probe (mapping quirk — the app calls it with
a different method; not live-verified).

## Scoped backend check

- Phantom columns: FINDING (above — SIXTH instance; role_desc in
  public.m_role_master).
- INNER-JOIN drops: the checklist query has a 3-way INNER JOIN (the
  comment-broken filter — `// AND mstr.procedureConfigId= :procedureConfigId`
  is COMMENTED OUT, so the query returns the whole unit's checklist, not the
  selected config — a logic defect in its own right, noted).
- Swallowed exceptions: PASS — the errors surface as correlation-id 500s
  (global handler), not swallowed.
- Org-trust: FINDING (module-wide payload-carried class).

## Build state

- Route: NEW at /procedureTemplate (moduleId 515).
- Gates: preflight C1-C7 (C7 PASS — genuinely new; C5 UNVALIDATED — no node
  links the state; the parent node 515 links a DIFFERENT dead state
  #/procedureConfiguration). ng build exit 0. Smoke: route serves, the
  phantom-defect banner renders honestly naming role_desc with the
  correlation reference, 0 page errors.
