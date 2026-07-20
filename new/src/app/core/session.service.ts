import { Injectable } from '@angular/core';

/**
 * Reads the SAME `cookieObject` cookie the legacy AngularJS app's
 * loginController writes (resources/assets/js/controller/login/loginController.js,
 * `$cookies.putObject('cookieObject', ...)`). Deliberately reads the raw
 * cookie rather than assuming a fresh modern-Angular login flow -- during
 * the ngUpgrade hybrid rollout (Phase 7 of the rebuild plan) both apps run
 * in the same browser session and must agree on who's logged in without
 * either one re-implementing auth.
 *
 * See docs/PHASE2_PILOT_AUDIT.md for the shape of this object as observed
 * across the 5 pilot screens.
 */
export interface CookieObject {
  unitId: number;
  organizationId: number;
  userId: number;
  doctorId?: number;
  isSuperUser?: string;
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class SessionService {
  private readonly cached: CookieObject | null;

  constructor() {
    this.cached = this.readCookieObject();
  }

  get current(): CookieObject | null {
    return this.cached;
  }

  get unitId(): number | undefined {
    return this.cached?.unitId;
  }

  get organizationId(): number | undefined {
    return this.cached?.organizationId;
  }

  get userId(): number | undefined {
    return this.cached?.userId;
  }

  get authToken(): string | undefined {
    return this.cached?.['authToken'] as string | undefined;
  }

  get isSuperUser(): boolean {
    return this.cached?.isSuperUser === 'Y';
  }

  /**
   * Mirrors legacy's per-state resolve-block check: `app.js` reads
   * `localStorage.getItem('permissions').modules` and filters for the
   * route's required module id (e.g. `organizationMaster`'s state at
   * app.js:8279 requires module 377). Same key, same shape, same check --
   * see docs/ANGULAR_PENDING_FINDINGS.md entry 13 for why this needs to
   * exist on the rebuild side at all (legacy enforces this only in its
   * frontend router; there is no backend-level check to fall back on).
   */
  hasModuleAccess(moduleId: number): boolean {
    try {
      const raw = localStorage.getItem('permissions');
      if (!raw) return false;
      const parsed = JSON.parse(raw) as { modules?: Array<{ id: number }> };
      return (parsed.modules ?? []).some((m) => m.id === moduleId);
    } catch {
      return false;
    }
  }

  /**
   * Mirrors legacy's `cookieObject.storeId = ...; $cookies.putObject('cookieObject', cookieObject)`
   * pattern used by `$rootScope.storePopup()`/`setStore()` (app.js:2607-2653)
   * after a store is selected -- see StoreSelectionService.
   */
  updateStoreId(storeId: number): void {
    if (!this.cached) return;
    this.cached['storeId'] = storeId;
    document.cookie = `cookieObject=${encodeURIComponent(JSON.stringify(this.cached))}; path=/`;
  }

  private readCookieObject(): CookieObject | null {
    const match = document.cookie
      .split('; ')
      .find((row) => row.startsWith('cookieObject='));
    if (!match) {
      return null;
    }
    try {
      const raw = decodeURIComponent(match.substring('cookieObject='.length));
      return JSON.parse(raw) as CookieObject;
    } catch {
      return null;
    }
  }
}
