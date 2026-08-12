export type ConsentTypeStatus = 'A' | 'I';

/**
 * Row shape returned by POST /adt/getConsentTypeMasterList (legacy
 * consentTypeMasterController.js GETCONSENTTYPELIST = "/adt/
 * getConsentTypeMasterList", adt.url.config.js:242). Verified live
 * 2026-08-12: SUCCESS with 3 real rows (e.g. "Fresh Consent Modal Test",
 * code CNTDUP2, status A) — REAL DATA.
 *
 * Correction note (DQ-2026-08-12-028 follow-up): this screen was first built
 * as noData in batch 10 (the legacy state app.js:12585 binds the generic
 * MasterController shell, which is the DECOY shape — the decoy-groundwork
 * classified 528 as Group A: REAL backend + REAL controller
 * consentTypeMasterController.js, 396 lines). The noData build was wrong;
 * this is the corrected real-data build.
 */
export interface ConsentTypeRow {
  consentTypeMasterId: number;
  consentTypeMasterName: string;
  consentTypeMasterCode: string;
  status: ConsentTypeStatus;
  createdBy?: number;
  createdDate?: string;
  updatedBy?: number;
  updatedDate?: string;
}
