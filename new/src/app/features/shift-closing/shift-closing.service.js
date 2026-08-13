import { __decorate } from "tslib";
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE_URL } from '../../core/api-config';
/**
 * Endpoint + payload taken directly from the legacy
 * `shiftClosingController.js` ("To get Refund list", lines 266-302): the
 * URI is HARDCODED — "/api/global/getRefundList" at :278, no url.config
 * constant exists. Backed by ShiftClosingMasterController.java
 * (class-level @RequestMapping "/api/global"; POST /getRefundList at
 * :205-206). The legacy controller sends the 5-field subset {unitId,
 * organizationId, createdBy, openingDate, currentDate} (:269-276) where
 * openingDate is the shift-opening cash date and currentDate is now, both
 * formatted 'YYYY-MM-DD HH:mm:ss'.
 *
 * Verified live 2026-08-12 (backend localhost:8080, Authorization Bearer
 * tools/.token_cache.json + userId:1 unitId:1 organizationId:1): probe
 * payload {organizationId, unitId, createdBy, openingDate, currentDate,
 * offset, noOfRecordsPerPage} -> HTTP 400 "Invalid parameters" (payload
 * incomplete). Additionally, ShiftClosingMasterDaoImpl.getRefundList
 * (:529-563) has a stray-quote SQL syntax bug at :541-542 (`AND :currentDate
 * ' and prm.orgnisation_id …`) — the feed is not consumable as a list.
 * Screen 600 is a write-path cash-entry form; deferred to the transaction
 * methodology.
 */
let ShiftClosingService = class ShiftClosingService {
    http = inject(HttpClient);
    base = inject(API_BASE_URL);
    // ctrl:shiftClosingController.getCashHandoverToLoger (refund block) — POST
    // /api/global/getRefundList {organizationId, unitId, createdBy,
    // openingDate, currentDate, offset, noOfRecordsPerPage}.
    getRefundList(payload) {
        return this.http.post(`${this.base}/api/global/getRefundList`, payload);
    }
    // ctrl:shiftClosingController.js save-close block — POST
    // /api/global/saveShiftCloseBalance (ShiftClosingMasterController:146).
    // Live-verified 2026-08-13 (BUILDABLE, DQ-059): HTTP 200 "Added
    // Successfully." + billing.m_shift_close_balance row lands; honest
    // rethrow on failure (no masking — the controller rethrows).
    saveShiftCloseBalance(payload) {
        return this.http.post(`${this.base}/api/global/saveShiftCloseBalance`, payload);
    }
};
ShiftClosingService = __decorate([
    Injectable({ providedIn: 'root' })
], ShiftClosingService);
export { ShiftClosingService };
