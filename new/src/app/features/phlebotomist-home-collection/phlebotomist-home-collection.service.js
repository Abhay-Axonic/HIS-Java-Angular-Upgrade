import { __decorate } from "tslib";
import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { API_BASE_URL } from '../../core/api-config';
/**
 * Endpoint + payload from the legacy
 * `userAllocationForHomecollectionController.js` (:649-650): POST
 * /LIS/PhlebotomistHomeCollection/save with the collection entries list
 * (PhlebotomistHomeCollectionController:28). Live-verified 2026-08-13
 * (BUILDABLE, DQ-061). The search feed (getUserAllocationHomeCollectionList)
 * is a read — session-required like the other reads; the form's search is
 * honest-state until the browser session verifies it.
 */
let PhlebotomistHomeCollectionService = class PhlebotomistHomeCollectionService {
    http = inject(HttpClient);
    base = inject(API_BASE_URL);
    save(entries) {
        return this.http.post(`${this.base}/LIS/PhlebotomistHomeCollection/save`, entries);
    }
};
PhlebotomistHomeCollectionService = __decorate([
    Injectable({ providedIn: 'root' })
], PhlebotomistHomeCollectionService);
export { PhlebotomistHomeCollectionService };
