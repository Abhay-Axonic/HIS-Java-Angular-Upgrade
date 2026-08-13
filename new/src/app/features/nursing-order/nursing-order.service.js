import { __decorate } from "tslib";
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE_URL } from '../../core/api-config';
let NursingOrderService = class NursingOrderService {
    http = inject(HttpClient);
    base = inject(API_BASE_URL);
    save(payload) {
        return this.http.post(`${this.base}/api/opd/saveNursingOrderMapper`, payload);
    }
};
NursingOrderService = __decorate([
    Injectable({ providedIn: 'root' })
], NursingOrderService);
export { NursingOrderService };
