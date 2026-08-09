/**
 * allergy-type-master — parity rebuild of the legacy
 *   views/master/GlobalMaster/allergy-type-master.html +
 *   resources/assets/js/controller/master/unit/allergyTypeMasterController.js
 *   (state `allergyTypeMaster`, module id 488, parent 476 Clinical Workstation
 *   -> 369 Global Master).
 *
 * A REAL legacy screen (a dedicated 316-line controller with full CRUD).
 * Endpoints verified by CALLING with the cached token (QF-2026-08-09-E):
 * the list, the count and the by-id detail all return 200 with the
 * "success" envelope; the save/update/status return the generic error
 * envelope on the EMPTY probe payload — their success with real data is
 * UNVERIFIED by the no-seed rule (a real save would write a row).
 */

export interface AllergyTypeRow {
  allergyTypeId: number;
  allergyTypeName?: string;
  allergyTypeDesc?: string;
  marathiAllergyTypeName?: string;
  isGeneric?: string;
  status?: 'A' | 'I' | string;
  [key: string]: unknown;
}

export interface AllergyTypeForm {
  allergyTypeId: number | null;
  allergyTypeName: string;
  allergyTypeDesc: string;
  isGeneric: string;
}

export const emptyAllergyTypeForm: AllergyTypeForm = {
  allergyTypeId: null,
  allergyTypeName: '',
  allergyTypeDesc: '',
  isGeneric: 'N',
};

export interface SaveAllergyTypePayload {
  allergyTypeName: string;
  allergyTypeDesc: string;
  isGeneric: string;
  allergyTypeId?: number | null;
  status?: 'A' | 'I';
  /** The legacy contract: the DAO parses createdDate/updatedDate via
   * GlobalCommonDateUtils.getDate(..., "dd-M-yyyy HH:mm:ss") and NPEs on a
   * null dateString (the named mechanism of the save's empty-probe error) —
   * the dates MUST be sent, formatted "DD-MM-YYYY HH:mm:ss" (the legacy's
   * moment format). */
  createdDate?: string;
  updatedDate?: string;
  createdBy?: number;
  updatedBy?: number;
  organizationId: number;
  unitId: number;
}

export interface UpdateAllergyTypeStatusPayload {
  allergyTypeId: number;
  status: 'A' | 'I';
}

/** The app's response envelope — read the "success"/"error" flag, not the
 * HTTP status alone (the allergy family returns 200 with an error envelope). */
export interface AllergyTypeApiEnvelope<T> {
  status?: string;
  message?: string | null;
  listObject?: T | null;
  object?: number | null;
  [key: string]: unknown;
}
