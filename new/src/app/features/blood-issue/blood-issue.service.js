import { __decorate } from "tslib";
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE_URL } from '../../core/api-config';
let BloodIssueService = class BloodIssueService {
    http = inject(HttpClient);
    base = inject(API_BASE_URL);
    saveIssue(payload) {
        return this.http.post(`${this.base}/global/bloodBank/saveIssueBloodBag`, payload);
    }
};
BloodIssueService = __decorate([
    Injectable({ providedIn: 'root' })
], BloodIssueService);
export { BloodIssueService };
