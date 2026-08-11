/**
 * Row shape AS RETURNED by POST /api/otims/getSubSequentProcedureConfigList
 * (legacy subsequentSurgeryConfigurationController.js:86/122). Confirmed
 * live 2026-08-12: SUCCESS with an empty listObject — honest dev-empty.
 * Exact column aliases were not captured during verification, so this
 * interface uses the generic id/name/status projection the legacy view
 * reads (entity: procedureConfigId/procedureConfigName).
 */
export interface SubsequentSurgeryConfigurationRow {
  id: number;
  name: string;
  status: string;
}
