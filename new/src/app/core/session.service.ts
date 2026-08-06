import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { firstValueFrom } from 'rxjs';
import { API_BASE_URL } from './api-config';

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

/**
 * Separate cookie from `cookieObject`, written after unit-level app
 * configuration is loaded -- see `$rootScope.checkCookies()`
 * (app.js:1881-1892) and docs/ANGULAR_PENDING_FINDINGS.md entry 34.
 * `assetMaintenanceSpecialityId` was the first field a rebuilt screen
 * needed (scheduleWorklist, screen 7/14). `radiologySpecialityId` is read
 * by roomModality (`resources/assets/js/controller/adt/unit/
 * roomModalityController.js` ctrl:46, `$scope.radiologySpecialityId =
 * cookieObjectAppConfig.radiologySpecialityId`, no null-check in legacy --
 * read defensively via this optional field instead of porting that crash).
 */
export interface CookieObjectAppConfig {
  assetMaintenanceSpecialityId?: number;
  radiologySpecialityId?: number;
  /**
   * Gates whether `home` (nurseDashboardMasterController.js:41,131) auto-
   * shows the nursing-station popup on init -- legacy compares
   * `$scope.nurseRole == 1` loosely; read defensively here too.
   */
  nurseRole?: number | string;
  [key: string]: unknown;
}

@Injectable({ providedIn: 'root' })
export class SessionService {
  private readonly cached: CookieObject | null;
  private readonly cachedAppConfig: CookieObjectAppConfig | null;
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);
  private permissionsPromise: Promise<void> | null = null;

  constructor() {
    this.cached = this.readCookieObject();
    this.cachedAppConfig = this.readNamedCookie<CookieObjectAppConfig>('cookieObjectAppConfig');
  }

  get appConfig(): CookieObjectAppConfig | null {
    return this.cachedAppConfig;
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
    const fromCookie = this.cached?.['authToken'] as string | undefined;
    if (fromCookie) return fromCookie;
    // Fallback: legacy login writes the token to sessionStorage.token on BOTH
    // login flows (loginController.js:365 `sessionStorage.setItem("token",
    // response.data.object.authToken)` and :1099), but only the fuller
    // post-login cookie write (:424/:1156) includes authToken in the cookie --
    // the sparse cookieObject write at :369 ({organizationId, unitId, userId})
    // does NOT. So after a real legacy login the cookie can be token-less,
    // which left the rebuild's authInterceptor attaching no Authorization
    // header -> backend 401 -> interceptor bounce to
    // /accessDenied?reason=sessionExpired (rendered as the generic "You do
    // not have permission" text). Mirrors legacy's own sessionStorage.token
    // storage. Guarded with try/catch: sessionStorage access can throw in
    // some privacy modes; a failure must not break the session read.
    try {
      return sessionStorage.getItem('token') ?? undefined;
    } catch {
      return undefined;
    }
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
   * Track A #65 (docs/ANGULAR_PENDING_FINDINGS.md entry 65): legacy's login
   * writes localStorage.permissions at ITS origin; this origin never sees it
   * (localStorage is origin-scoped, including port). The legacy login now
   * carries `roleId` in cookieObject (loginController.js — Track A commit),
   * so this origin can fetch the role's modules itself and populate the SAME
   * key/shape `hasModuleAccess` parses ({ status, modules: [{id, ...}] }).
   *
   * Idempotent: at most one fetch per session. Failure modes are NON-silent —
   * a failed/absent fetch leaves the store empty and the module-access guard
   * reports reason=permissionsUnavailable instead of the generic denial, so a
   * permissions-plumbing problem is observable rather than reading as an
   * individual dead screen.
   */
  ensurePermissions(): Promise<void> {
    if (this.permissionsPromise) return this.permissionsPromise;
    this.permissionsPromise = this.fetchPermissions().catch(() => undefined);
    return this.permissionsPromise;
  }

  /** True when the permissions store holds a real (non-empty) module set. */
  permissionsLoaded(): boolean {
    try {
      const parsed = JSON.parse(localStorage.getItem('permissions') ?? 'null') as
        | { modules?: unknown[] }
        | null;
      return Array.isArray(parsed?.modules) && parsed.modules.length > 0;
    } catch {
      return false;
    }
  }

  private async fetchPermissions(): Promise<void> {
    if (this.permissionsLoaded()) return;
    const session = this.cached;
    const roleId = (session as { roleId?: unknown } | null)?.roleId as number | undefined;
    if (!session || typeof roleId !== 'number') {
      // Pre-change session (no roleId in the cookie yet) or no session: the
      // store stays empty -> guard reports permissionsUnavailable, whose
      // message (log out and back in) is the self-healing prompt.
      return;
    }
    const url =
      `${this.base}/privilege/role/${session.organizationId}/${session.unitId}/${roleId}`;
    const res = await firstValueFrom(this.http.get<Record<string, unknown>>(url));
    const rows = Array.isArray(res?.['modules']) ? (res['modules'] as unknown[]) : [];
    const modules: Array<{ id: number }> = [];
    for (const row of rows) {
      const raw = (row as { modules?: unknown }).modules;
      if (typeof raw === 'string') {
        try {
          modules.push(...(JSON.parse(raw) as Array<{ id: number }>));
        } catch {
          /* one malformed row must not fail the whole bootstrap */
        }
      } else if (Array.isArray(raw)) {
        modules.push(...(raw as Array<{ id: number }>));
      }
    }
    const deduped = [
      ...new Map(modules.filter((m) => typeof m?.id === 'number').map((m) => [m.id, m])).values(),
    ];
    localStorage.setItem(
      'permissions',
      JSON.stringify({ status: 'success', modules: deduped }),
    );
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

  /**
   * Mirrors `$rootScope.selectCounter(obj)`'s `cookieObject.counterId = ...;
   * $cookies.putObject('cookieObject', cookieObject)` write (app.js:1833-1835)
   * after a counter is selected -- see CounterSelectionService.
   */
  updateCounterId(counterId: number | string): void {
    if (!this.cached) return;
    this.cached['counterId'] = counterId;
    document.cookie = `cookieObject=${encodeURIComponent(JSON.stringify(this.cached))}; path=/`;
  }

  private readCookieObject(): CookieObject | null {
    return this.readNamedCookie<CookieObject>('cookieObject');
  }

  private readNamedCookie<T>(name: string): T | null {
    const prefix = `${name}=`;
    const match = document.cookie.split('; ').find((row) => row.startsWith(prefix));
    if (!match) {
      return null;
    }
    try {
      const raw = decodeURIComponent(match.substring(prefix.length));
      return JSON.parse(raw) as T;
    } catch {
      return null;
    }
  }
}
