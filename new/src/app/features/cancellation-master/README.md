# cancellation-master — parity rebuild of the legacy Cancellation Master screen

Legacy source:
- template: `views/master/GlobalMaster/OTIMS/cancellation-reason.html` (140
  lines, ZERO ng-bindings / ZERO API calls — a DEAD SHELL, the static-shell
  shape)
- state: `.state('cancellationMaster')` REGISTERED at app.js:12351 (generic
  MasterController, no resolve gate)
- menu node: 518 'Cancellation Master', parent 514 OTIMS -> 369 Global
  Master, actionLink `#/cancellationMaster`

## CLASS: LIVE (per the amended reachability rule)

State registered + menu node exists + a REAL backend read exists. The walk
continues in the OTIMS (514) subtree: 515 (screen 16) and 517 (screen 17)
done, 516 walked past under NOT-A-SCREEN-YET (no backend), 518 this screen.

## Backend — verified BY CALLING (cached token, 2026-08-10), body inspected per the new standard

- list: GET /api/global/cancel/reasons -> HTTP 200, RAW LIST body len=0
  (dev-empty). Per the ENDPOINT-VERIFICATION STANDARD (RULES.md 5c2432fd):
  the body was inspected — it is a raw list (the body IS the data), not the
  {listObject} envelope; the service is typed accordingly.
- table: public.m_cancel_reason exists (11 columns: id, organization_id,
  unit_id, code, reason, is_active, updated_by, updated_date, added_by,
  added_date, is_deleted).

## HONEST GAP (static-shell rule — every missing endpoint reported honestly)

The ONLY endpoint for this screen is the list read. There is NO save /
update / status / count endpoint for cancel reasons anywhere in the backend:
- the CancelReason entity (m_cancel_reason) is consumed as an FK REFERENCE
  by AssetManagement only (assetIndent/assetReturn/assetIndentApproval/
  fixedAssetOpeningBalance set a cancelReasonId on other tables);
- no controller maps saveCancelReason / updateCancelReason /
  updateCancelReasonStatus / a cancel-reason count;
- the legacy template's Save button is inert (the shell has zero bindings).
The rebuild's Save button therefore reports the gap honestly in plain
language ("there is no save endpoint for cancel reasons in the backend (only
the list read exists)") — never a raw HTTP error, never a silent no-op,
never a fake success.

## Scoped backend check (per the standing rule, every class)

- Phantom columns/tables: PASS — m_cancel_reason DDL (11 columns) resolves
  for the read; the raw-list body is the table's rows.
- Named-query INNER-JOIN null-FK drops: PASS (no joins — the read is a plain
  list of the table).
- Dup-check operator precedence: NOT PRESENT (no save path to carry one).
- Org-trust: NOTED — the list read is org-agnostic (GET /api/global/cancel/
  reasons takes no org/unit param; it returns ALL rows). Not a write path;
  recorded, not asserted.
- Twin queries: PASS.
- Swallowed exceptions on write paths: PASS (no write paths exist).

## Verification state — HONEST

- preflight C1-C6 PASS (C5 LIVE — state registered + node chain 518->514->369);
  `npm run build` exit 0; smoke standard PASS (FINAL-URL
  /cancellationMaster, moduleId 518 present, SERVE-BRANCH matches the branch
  under test); interaction probe PASS (grid renders empty-state, Save button
  shows the honest gap toast, 0 page errors).
- READ VERIFICATION: the list endpoint was CALLED and the BODY inspected per
  the new standard — HTTP 200 with a raw-list body (dev-empty), which IS
  success for a raw-list endpoint.
- WRITE VERIFICATION: none ran — no write endpoint exists to test. The Save
  button's gap toast is the honest surface of that absence (the static-shell
  rule: report every missing endpoint honestly, never claim "not erroring"
  for an untried write, never fake a save).

## Deliberate divergences (flagged, not silent)

1. READ-ONLY REBUILD: only the list read exists in the backend; Save is an
   honest gap toast (see above). The rebuild does not invent a save.
2. The legacy shell had no other behavior to restore (zero bindings).
