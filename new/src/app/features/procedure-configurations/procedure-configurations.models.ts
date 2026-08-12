/**
 * Row shape AS RETURNED by GET /api/otims/getProcedureConfigList/{org}/{unit}
 * (legacy procedureConfigurationsController.js:50). Confirmed live
 * 2026-08-12: SUCCESS with an empty listObject — honest dev-empty. Exact
 * column aliases were not captured during verification, so this interface
 * uses the generic id/name/status projection the legacy view reads (entity:
 * procedureConfigId/procedureConfigName).
 */
export interface ProcedureConfigurationRow {
  id: number;
  name: string;
  status: string;
}
