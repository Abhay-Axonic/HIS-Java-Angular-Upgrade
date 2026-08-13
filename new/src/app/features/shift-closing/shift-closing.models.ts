/**
 * Shift-closing (600) model shapes.
 *
 * LIVE-VERIFIED 2026-08-12: POST /api/global/getRefundList with
 * {organizationId, unitId, createdBy, openingDate, currentDate, offset,
 * noOfRecordsPerPage} -> HTTP 400 "Invalid parameters" (payload incomplete).
 * The DAO (ShiftClosingMasterDaoImpl.getRefundList, :529-563) requires
 * exactly {openingDate, currentDate, organizationId, unitId, createdBy}
 * (:546-550) and carries a stray-quote SQL syntax bug at :541-542 (`AND
 * :currentDate ' and prm.orgnisation_id …`), so the feed is not consumable
 * even with a complete payload. Screen 600 is classified write-path
 * cash-entry form; deferred to the transaction methodology.
 */

/** Payload probed live on 2026-08-12 (see service doc-comment). */
export interface RefundListProbe {
  organizationId: number;
  unitId: number;
  createdBy: number;
  /** 'YYYY-MM-DD HH:mm:ss' — legacy sends the shift-opening cash date. */
  openingDate: string;
  /** 'YYYY-MM-DD HH:mm:ss' — legacy sends `now` (moment 'YYYY-MM-DD HH:mm:ss'). */
  currentDate: string;
  offset: number;
  noOfRecordsPerPage: number;
}

/**
 * Intended row shape of POST /api/global/getRefundList — the exact SELECT
 * projection of ShiftClosingMasterDaoImpl.getRefundList (:531-551),
 * alias-to-bean ShiftClosingMasterDto:
 *   prm.payment_refund_voucher_no "voucherNo",
 *   to_char(prm.date_of_refund,'yyyy-MM-dd HH:mm:ss') "createdDate",
 *   concat(prefix,' ',first_name,' ',last_name) "pName",
 *   pr.uhid_number "uhidNumber",
 *   prm.payment_refund_id "paymentRefundId",
 *   prm.amount_refunded "amount"
 * from billing.t_payment_refund_master joined to patient.t_patient_registration,
 * filtered by date_of_refund BETWEEN :openingDate AND :currentDate,
 * orgnisation_id, unit_id, created_by, exported_to_shift_close = false.
 */
export interface ShiftClosingRefundRow {
  voucherNo?: string;
  createdDate?: string;
  pName?: string;
  uhidNumber?: string;
  paymentRefundId?: number;
  amount?: number;
}

/**
 * Save payload of POST /api/global/saveShiftCloseBalance (live-verified
 * 2026-08-13: HTTP 200 "Added Successfully." + billing.m_shift_close_balance
 * row landed, id-based verified; teardown clean — BUILDABLE, DQ-059).
 *
 * CONTRACT (park_sweep.py probes): the entity ShiftClosingMaster maps
 * billing.m_shift_close_balance (NOT m_shift_close_master). The Dozer
 * date-format is dd-M-yyyy: send dates as 'dd-M-yyyy' (no time) — the
 * smoke's '2099-01-01 00:00:00' was mangled into year 0006 (a fixture
 * trap). listOfIds is the receipt-update branch: an empty [] is safe
 * (the DAO calls size() BEFORE the null check — a null list NPEs,
 * ShiftClosingMasterDaoImpl:392).
 */
export interface ShiftCloseSave {
  organizationId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
  /** 'dd-M-yyyy' — the mapper's format; a time suffix mangles the date. */
  createdDate: string;
  updatedDate: string;
  status: string;
  difference?: number;
  refundAmount?: number;
  /** Receipt ids to flag as exported; [] skips the branch. */
  listOfIds: number[];
}

/** The saved row shape (the response object = the ShiftClosingMaster). */
export interface ShiftCloseBalanceRow {
  id?: number;
  closingNumber?: string;
  difference?: number;
  refundAmount?: number;
  createdDate?: string;
}
