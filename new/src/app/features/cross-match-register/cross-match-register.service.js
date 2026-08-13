import { __decorate } from "tslib";
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE_URL } from '../../core/api-config';
/**
 * Cross-match register write (DQ-068: BUILDABLE — the full legacy payload
 * lands master + details rows). The reads (worklists) are session-gated
 * backend reads; this feature wires the verified write only.
 */
let CrossMatchRegisterService = class CrossMatchRegisterService {
    http = inject(HttpClient);
    base = inject(API_BASE_URL);
    saveCrossMatch(payload) {
        return this.http.post(`${this.base}/api/bloodbank/crossMatch/saveCrossMatchRegister`, payload);
    }
};
CrossMatchRegisterService = __decorate([
    Injectable({ providedIn: 'root' })
], CrossMatchRegisterService);
export { CrossMatchRegisterService };
