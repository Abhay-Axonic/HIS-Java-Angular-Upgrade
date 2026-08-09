/**
 * allergy-master — parity rebuild of the legacy
 *   views/master/GlobalMaster/allergy-master.html +
 *   resources/assets/js/controller/master/unit/allergyMasterController.js
 *   (state `allergyMaster`, module id 489, parent 476 Clinical Workstation
 *   -> 369 Global Master).
 *
 * A REAL legacy screen (a dedicated controller with full CRUD — not a static
 * shell). The endpoints all EXIST and were verified by calling with the
 * cached token; the backend's list/detail/save/update/status paths currently
 * FAIL with the app-level error ("Something went wrong!!") because the
 * GET_ALLERGY_MASTER_LIST named query selects cims_code/cims_type columns
 * that emr.m_allergy_master does not have (phantom-column drift —
 * SQLGrammarException, swallowed to the generic envelope; the count query
 * works because it does not select the phantom). Logged, not fixed (Rule 10).
 */

export interface AllergyTypeOption {
  allergyTypeId: number;
  allergyTypeDesc?: string;
  [key: string]: unknown;
}

export interface AllergyRow {
  allergyId: number;
  allergyTypeId?: number | null;
  allergyTypeDesc?: string;
  allergyName?: string;
  allergyDesc?: string;
  cimsCode?: string;
  cimsType?: string;
  status?: 'A' | 'I' | string;
  [key: string]: unknown;
}

export interface AllergyForm {
  allergyId: number | null;
  allergyName: string;
  allergyDesc: string;
  allergyTypeId: number | null;
  cimsCode: string;
  cimsType: string;
}

export const emptyAllergyForm: AllergyForm = {
  allergyId: null,
  allergyName: '',
  allergyDesc: '',
  allergyTypeId: null,
  cimsCode: '',
  cimsType: '',
};

export interface SaveAllergyPayload {
  allergyName: string;
  allergyDesc: string;
  cimsCode: string;
  cimsType: string;
  allergyTypeId: number | null;
  allergyId?: number | null;
  status?: 'A' | 'I';
  organizationId: number;
  unitId: number;
}

export interface UpdateAllergyStatusPayload {
  allergyId: number;
  status: 'A' | 'I';
}

/** The app's response envelope (the allergy endpoints return
 * {status, message, listObject, object} — the "success"/"error" flag must be
 * read, not the HTTP status alone). */
export interface AllergyApiEnvelope<T> {
  status?: string;
  message?: string | null;
  listObject?: T | null;
  object?: number | null;
  [key: string]: unknown;
}
