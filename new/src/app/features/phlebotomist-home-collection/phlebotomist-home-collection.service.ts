import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  PhlebotomistCollectionEntry,
  PhlebotomistCollectionRow,
} from './phlebotomist-home-collection.models';

/**
 * Endpoint + payload from the legacy
 * `userAllocationForHomecollectionController.js` (:649-650): POST
 * /LIS/PhlebotomistHomeCollection/save with the collection entries list
 * (PhlebotomistHomeCollectionController:28). Live-verified 2026-08-13
 * (BUILDABLE, DQ-061). The search feed (getUserAllocationHomeCollectionList)
 * is a read — session-required like the other reads; the form's search is
 * honest-state until the browser session verifies it.
 */
@Injectable({ providedIn: 'root' })
export class PhlebotomistHomeCollectionService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  save(entries: PhlebotomistCollectionEntry[]): Observable<ApiResponse<PhlebotomistCollectionRow[], never>> {
    return this.http.post<ApiResponse<PhlebotomistCollectionRow[], never>>(
      `${this.base}/LIS/PhlebotomistHomeCollection/save`,
      entries,
    );
  }
}
