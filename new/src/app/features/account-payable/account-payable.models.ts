export type PayableStatus = 'A' | 'I';

/**
 * Row shape returned by POST /api/global/getAccountPayable and
 * /api/global/getActiveAccountPayable (legacy billing/unit/
 * accountPayableMasterController.js getAccountPay + getActiveAccountPay;
 * GETACCOUNTPAYABLE = "/api/global/getAccountPayable").
 *
 * Verified live 2026-08-12 (DQ-2026-08-12-030): SUCCESS with 5 real rows
 * ("AP-LIST-TEST"/"List Test AP", "ABC123"/"TestAccountMaster", ...) using
 * the payload {orgId, orgUnitId} — REAL DATA. The legacy states
 * `accountPayable` (580) and `accountPayableStatus` (582) bind the generic
 * MasterController shell (app.js), but the real controller exists unbound —
 * the decoy-groundwork's "no controller anywhere" was wrong for this pair.
 */
export interface AccountPayableRow {
  accPayableMstId: number;
  accCode: string;
  accDesc: string;
  accPayableCode: string;
  accDay?: string;
  accEmail?: string;
  accMobile?: string;
  accWorkFromTime?: string;
  accWorkToTime?: string;
  accValidityFromDate?: string;
  accValidityToDate?: string;
  accCreditLimit?: number;
  accPaymentTerms?: string;
  accAddress?: string;
  accZipcode?: string;
  accContactNumber?: string;
  status: PayableStatus;
  countryName?: string;
  stateName?: string;
  cityName?: string;
  districtName?: string;
  areaName?: string;
}
