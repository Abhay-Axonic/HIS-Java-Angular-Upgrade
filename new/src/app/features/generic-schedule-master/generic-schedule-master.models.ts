/**
 * Row shape AS RETURNED by GET
 * /Pharmacy/global/listGenericScheduleMaster/{org}/{unit}/{offset}/{size}
 * (legacy genericScheduleMasterController.js:254/281). Confirmed live
 * 2026-08-12: SUCCESS with 1 real row. Exact column aliases of the live row
 * were not captured during verification, so this interface uses the generic
 * id/name/status projection the legacy view reads (entity:
 * genericScheduleId/genericScheduleName).
 */
export interface GenericScheduleRow {
  id: number;
  name: string;
  status: string;
}
