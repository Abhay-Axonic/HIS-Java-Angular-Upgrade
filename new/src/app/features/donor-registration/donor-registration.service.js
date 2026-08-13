import { __decorate } from "tslib";
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE_URL } from '../../core/api-config';
/**
 * Screen 719 feeds (all verified live 2026-08-12; DQ-056 / drift map):
 *  - prefix / gender / nationality / identification: adt master lists (LIVE)
 *  - blood group / donor type: bloodbank master lists, org/unit path (LIVE)
 *  - save: POST /api/bloodbank/donorRegistration — HONEST chain (all catches
 *    log-and-throw ServiceException; HTTP 500 carries the real message; the
 *    T0 write smoke passed BUILDABLE, exit 0).
 *  - patientAutofillSearch is a TODO STUB (empty 200) — deliberately NOT
 *    wired; the autofill control is omitted (honest state).
 */
let DonorRegistrationService = class DonorRegistrationService {
    http = inject(HttpClient);
    base = inject(API_BASE_URL);
    getPrefixes() {
        return this.http.get(`${this.base}/adt/getActivePrefixList`);
    }
    getGenders() {
        return this.http.get(`${this.base}/adt/getActiveGenderList`);
    }
    getNationalities() {
        return this.http.get(`${this.base}/adt/getActiveNationalityList`);
    }
    getBloodGroups() {
        return this.http.get(`${this.base}/global/bloodBank/getActiveBloodGroupMaster/1/1`);
    }
    getDonorTypes() {
        return this.http.get(`${this.base}/global/bloodBank/getActiveBloodDonorTypeMaster/1/1`);
    }
    getIdentifications() {
        return this.http.get(`${this.base}/adt/getActiveIdentificationList`);
    }
    saveDonor(payload) {
        return this.http.post(`${this.base}/api/bloodbank/donorRegistration`, payload);
    }
};
DonorRegistrationService = __decorate([
    Injectable({ providedIn: 'root' })
], DonorRegistrationService);
export { DonorRegistrationService };
