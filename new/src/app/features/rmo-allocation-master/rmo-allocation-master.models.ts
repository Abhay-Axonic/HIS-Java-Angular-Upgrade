export type RmoStatus = 'A' | 'I';

/**
 * Row shape returned by POST /api/unit/clinical/getRmoAllocationMasterList
 * (legacy rmoAllocationMasterController.js; GETRMODOCTORALLOCATIONMASTERLIST
 * = "/api/unit/clinical/getRmoAllocationMasterList" from
 * global.url.config.js:315). Confirmed live 2026-08-12: SUCCESS empty
 * (honest dev-empty).
 */
export interface RmoAllocationRow {
  id: number;
  rmoDoctorId: number;
  doctorName: string;
  specialityName: string;
  wardName: string;
  status: RmoStatus;
}
