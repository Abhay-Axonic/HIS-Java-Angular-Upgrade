/**
 * nursing-order-master — parity rebuild of the legacy
 *   views/master/GlobalMaster/nursing-order-master.html
 *   (state `nursingOrderMaster`, module id 480, parent 476 Clinical
 *   Workstation -> 369 Global Master).
 *
 * THIS SCREEN IS DIFFERENT FROM SCREENS 9-11: it is a REAL full-CRUD
 * screen with a dedicated legacy controller (nursingOrderMasterController,
 * 563 lines) and a complete backend (NursingOrderMasterController,
 * /api/global/unit/* — save/update/status/list/count/search/byId, all
 * live-verified 200). The rebuild wires the real endpoints including the
 * save path.
 */
export interface NursingOrderMasterRow {
  nursingOrderId: number;
  nursingOrderCode: string;
  nursingOrderDesc: string;
  serviceId: number | null;
  specialityId: number | null;
  subSpecialityId: number | null;
  serviceStandardName?: string | null;
  specialityName?: string | null;
  subSpecialityName?: string | null;
  organizationId: number;
  unitId: number;
  status: string;
}

export interface NursingOrderSavePayload {
  nursingOrderDesc: string;
  nursingOrderCode: string;
  serviceId: number | null;
  specialityId: number | null;
  subSpecialityId: number | null;
  organizationId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
  status: string;
  createdDate: string;
  updatedDate: string;
}

export interface SpecialityRow {
  specialityId: number;
  specialityName: string;
}

export interface SubSpecialityRow {
  subSpecialityId: number;
  subSpecialityName?: string | null;
  specialityId: number;
}

export interface ServiceMasterRow {
  serviceMasterId: number;
  serviceStandardName?: string | null;
  serviceStandardCode?: string | null;
  specialityId?: number | null;
  specialityName?: string | null;
  subSpecialityId?: number | null;
  subSpecialityName?: string | null;
  isAppointable?: string | null;
  status?: string | null;
}
