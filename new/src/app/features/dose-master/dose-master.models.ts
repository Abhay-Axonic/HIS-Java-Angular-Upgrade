/**
 * Row shape AS RETURNED by POST /api/common/getDoseMasterList (legacy
 * doseMasterController.js:77/112). Confirmed live 2026-08-12: SUCCESS with
 * 1 real row. Exact column aliases of the live row were not captured during
 * verification, so this interface uses the generic id/name/status projection
 * the legacy view reads (entity: doseId/doseName).
 */
export interface DoseRow {
  id: number;
  name: string;
  status: string;
}
