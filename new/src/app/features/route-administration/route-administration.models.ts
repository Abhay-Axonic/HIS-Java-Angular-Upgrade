/**
 * route-administration — parity rebuild of the legacy
 *   views/master/GlobalMaster/AssetManagenent/Master/route-of-adminstration.html
 *   (menu node 477 'Route of Administration', parent 476 Clinical
 *   Workstation -> 369 Global Master, actionLink #/routeAdministration).
 *
 * THE DANGLE SHAPE (menu'd-unrouted — the drug-recall class, second
 * instance after 479): the node's actionLink points at #/routeAdministration,
 * but NO `.state('routeAdministration')` is registered anywhere in app.js
 * (grep = 0). The controller IS loaded (index.html:1006), the template
 * exists, and the backend is LIVE — so the legacy menu navigates to a
 * non-existent state (NG04002). Per the amended reachability rule (RULES.md,
 * four-way classification, owner directive 2026-08-10) a DANGLE screen with
 * a real backend is BUILDABLE: the rebuild RESTORES a menu-advertised
 * screen the legacy cannot open.
 */
export interface RouteOfAdministrationRow {
  id: number;
  code: string;
  description: string;
  status: boolean;
}

export interface RouteOfAdministrationSavePayload {
  code: string;
  description: string;
  status: boolean;
  organizationId: number;
  unitId: number;
  userId: number;
}
