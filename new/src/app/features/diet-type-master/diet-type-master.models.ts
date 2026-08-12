/**
 * Row shape AS RETURNED by POST
 * /api/kitchen/master/dietTypeMaster/getDietTypeMasterPaginationList (legacy
 * dietTypeMasterController.js:119/155). Confirmed live 2026-08-12: SUCCESS
 * with 1 real row. Exact column aliases of the live row were not captured
 * during verification, so this interface uses the generic id/name/status
 * projection the legacy view reads (entity: dietTypeMasterId/dietTypeName).
 */
export interface DietTypeRow {
  id: number;
  name: string;
  status: string;
}
