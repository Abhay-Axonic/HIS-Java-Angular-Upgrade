/**
 * procedure-configuration — parity rebuild of the legacy
 *   views/master/GlobalMaster/OTIMS/procedure-configuration-master.html
 *   (menu node 515 'Procedure Configuration', parent 514 OTIMS ->
 *   369 Global Master, actionLink #/procedureConfiguration).
 *
 * THE DANGLE SHAPE (menu'd-unrouted — the drug-recall class, FOURTH
 * instance after 479/477/478): the node's actionLink points at
 * #/procedureConfiguration, but NO `.state('procedureConfiguration')` is
 * registered anywhere in app.js (grep = 0). The controller IS loaded
 * (index.html:1744 procedureConfigurationsController.js), the template
 * exists, and the backend is LIVE — the legacy menu navigates to NG04002.
 * Per the amended reachability rule (RULES.md four-way classification +
 * fifth bucket) a DANGLE screen with a real backend is BUILDABLE: the
 * rebuild RESTORES a menu-advertised screen the legacy cannot open.
 */
export interface ProcedureConfigurationRow {
  procedureConfigId: number;
  cptCode?: string | null;
  cptDescription?: string | null;
  duration?: string | null;
  specialityId?: number | null;
  serviceId?: number | null;
  procedureTypeId?: number | null;
  anesthesiaTypeId?: number | null;
  operationTheatreTypeId?: number | null;
  status?: string | null;
  orgId?: number | null;
  unitId?: number | null;
  procedureType?: string | null;
  anesthesiaType?: string | null;
  speciality?: string | null;
  service?: string | null;
}
