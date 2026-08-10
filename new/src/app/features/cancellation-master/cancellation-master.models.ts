/**
 * cancellation-master — parity rebuild of the legacy
 *   views/master/GlobalMaster/OTIMS/cancellation-reason.html
 *   (menu node 518 'Cancellation Master', parent 514 OTIMS -> 369 Global
 *   Master, actionLink #/cancellationMaster).
 *
 * LIVE CLASS under the amended reachability rule: the state
 * `cancellationMaster` IS registered (app.js:12351, generic MasterController),
 * the menu node exists, and a REAL backend read exists — GET
 * /api/global/cancel/reasons (GlobalController:197-204, raw list of
 * public.m_cancel_reason rows). The legacy template is a DEAD SHELL (140
 * lines, ZERO ng-bindings/API calls — the static-shell shape), so the
 * rebuild implements the list from the real feed.
 *
 * NOTE (honest, per the static-shell rule): the ONLY endpoint for this
 * screen is the list. There is NO save/update/status/count endpoint for
 * cancel reasons anywhere (the m_cancel_reason entity is used as an FK
 * reference by AssetManagement only). Save is therefore reported honestly
 * as an error toast, never faked.
 */
export interface CancelReasonRow {
  id?: number | null;
  organizationId?: number | null;
  unitId?: number | null;
  code?: string | null;
  reason?: string | null;
  isActive?: boolean | null;
}
