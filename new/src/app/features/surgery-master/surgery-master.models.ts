/**
 * surgery-master — parity rebuild of the legacy
 *   views/master/GlobalMaster/surgery-master.html +
 *   resources/assets/js/controller/opd/global/surgeryMasterController.js
 *   (state `surgeryMaster`, module id 487, parent 476 Clinical Workstation
 *   -> 369 Global Master).
 *
 * A REAL legacy screen (a dedicated 333-line controller with full CRUD).
 * Endpoints verified by CALLING with the cached token (2026-08-09):
 *   POST /api/global/getSurgeryMasterList          -> 200 "success" ([] dev)
 *   POST /api/global/getSurgeryMasterById          -> 200 "success" ([] dev)
 *   POST /api/global/getSurgeryGradeMasterList     -> 200 "success" (3 rows)
 *   POST /adt/getActiveSpecialityList              -> 200 "success" (2 rows)
 * The save/update/status endpoints are mapped (SurgeryMasterController) but
 * their success with real data is UNVERIFIED by the no-seed rule (a real
 * save would write a row into public.m_surgery_master).
 */

export interface SpecialityOption {
  specialityId: number;
  specialityName?: string;
  [key: string]: unknown;
}

export interface SurgeryGradeOption {
  surgeryGradeId: number;
  surgeryGrade?: string;
  surgeryGradeCode?: string;
  [key: string]: unknown;
}

export interface SurgeryRow {
  surgery_id: number;
  surgeryName?: string;
  surgeryCode?: string;
  surgeryCodeCpt?: string;
  status?: 'A' | 'I' | string;
  specialityId?: number;
  specialityName?: string;
  surgeryGradeId?: number;
  surgeryGrade?: string;
  [key: string]: unknown;
}

export interface SurgeryForm {
  surgery_id: number | null;
  specialityId: number | null;
  surgeryCode: string;
  surgeryCodeCpt: string;
  surgeryName: string;
  surgeryGradeId: number | null;
}

export const emptySurgeryForm: SurgeryForm = {
  surgery_id: null,
  specialityId: null,
  surgeryCode: '',
  surgeryCodeCpt: '',
  surgeryName: '',
  surgeryGradeId: null,
};

export interface SaveSurgeryPayload {
  specialityId: number | null;
  surgeryCode: string;
  surgeryCodeCpt: string;
  surgeryName: string;
  surgeryGradeId: number | null;
  unitId: number;
  organizationId: number;
  status?: 'A' | 'I';
  /** The legacy contract: the DAO parses createdDate/updatedDate via
   * GlobalCommonDateUtils.getDate(..., "dd-M-yyyy HH:mm:ss") and NPEs on a
   * null dateString (the same named mechanism as allergyTypeMaster) — the
   * dates MUST be sent, formatted "DD-MM-YYYY HH:mm:ss" (the legacy's moment
   * format). */
  createdDate?: string;
  updatedDate?: string;
  createdBy?: number;
  updatedBy?: number;
}

export interface UpdateSurgeryPayload {
  surgery_id: number;
  specialityId: number | null;
  surgeryCode: string;
  surgeryCodeCpt: string;
  surgeryName: string;
  surgeryGradeId: number | null;
  updatedBy?: number;
  updatedDate?: string;
}

export interface UpdateSurgeryStatusPayload {
  surgery_id: number;
  status: 'A' | 'I';
  updatedBy?: number;
  updatedDate?: string;
}

/** The app's response envelope — read the "success"/"error" flag, not the
 * HTTP status alone (the legacy returns 200 with an error envelope). */
export interface SurgeryApiEnvelope<T> {
  status?: string;
  message?: string | null;
  listObject?: T | null;
  object?: number | null;
  [key: string]: unknown;
}
