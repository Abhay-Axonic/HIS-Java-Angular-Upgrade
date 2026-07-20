import { Injectable } from '@angular/core';

/**
 * In-memory-only ui-router param, matching `bookViewObj` -- app.js's
 * `redirectBookViewUrl(encounterObj)` (app.js:2532-2573) both writes
 * `sessionStorage.cookieEncounterObject` AND passes the *same* object as
 * this state param in one call. Deliberately a plain service, not
 * Angular Router's `extras.state` -- ui-router's own param does not
 * survive a real page refresh either, and that fact is exactly what
 * entry 28's fix (docs/ANGULAR_PENDING_FINDINGS.md) depends on: a stale
 * `cookieEncounterObject` from an unrelated screen with no matching
 * `bookViewObj` is precisely the failure case the fix now catches.
 */
export interface BookViewEncounterObject {
  patientName?: string;
  uhIdNumber?: string;
  genderCode?: string;
  age?: string | number;
  birthDate?: string;
  encounterId?: number | string;
  patientId?: number | string;
  genderId?: number;
  visitTypeId?: number;
  admissionId?: number;
  patientCategoryId?: number;
  roleId?: number;
  isCritical?: boolean;
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class BookViewHandoffService {
  private pendingObject: BookViewEncounterObject | null = null;

  setObject(obj: BookViewEncounterObject): void {
    this.pendingObject = obj;
  }

  takeObject(): BookViewEncounterObject {
    return this.pendingObject ?? {};
  }
}
