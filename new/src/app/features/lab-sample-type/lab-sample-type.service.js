import { __decorate } from "tslib";
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE_URL } from '../../core/api-config';
let LabSampleTypeService = class LabSampleTypeService {
    http = inject(HttpClient);
    base = inject(API_BASE_URL);
    addSample(payload) {
        return this.http.post(`${this.base}/LIS/global/addSample`, payload);
    }
};
LabSampleTypeService = __decorate([
    Injectable({ providedIn: 'root' })
], LabSampleTypeService);
export { LabSampleTypeService };
