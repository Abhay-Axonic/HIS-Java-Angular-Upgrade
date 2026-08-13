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
