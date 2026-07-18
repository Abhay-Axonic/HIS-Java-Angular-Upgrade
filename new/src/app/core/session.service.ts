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
