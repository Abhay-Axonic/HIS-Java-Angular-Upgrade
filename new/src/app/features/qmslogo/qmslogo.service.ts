import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { QmsLogoImageTypeId } from './qmslogo.models';

/**
 * Single real backend endpoint for this whole screen, called once per
 * field (8 total) with a different `imageTypeId`:
 * `POST /api/unit/applicationConfiguration/screen-images` (multipart).
 * Controller confirmed at
 * legacy/Global/src/main/java/com/param/global/controller/
 * UnitApplicationBillingConfigurationController.java:152-201
 * (`saveScreenImage` / `getScreenImageById`).
 *
 * legacy's `qmslogo.js` hardcodes `createdBy`/`updatedBy` to the SAME
 * number as `imageTypeId` for every field (not the real session user id) --
 * a genuine legacy quirk, ported faithfully below rather than "fixed", per
 * the no-deviation-without-sign-off rule. `status` is always `'A'`.
 * `createdDate`/`updatedDate` are sent as `DD-MM-YYYY HH:mm:ss` strings,
 * matching legacy's `moment(...).format('DD-MM-YYYY HH:mm:ss')`.
 */
export interface QmsLogoSaveResult {
  screenImageId?: number;
  imageURL?: string;
  logoName?: string;
}

/**
 * Row shape returned by `GET .../screen-images/{orgId}/{imageTypeId}`
 * (field names read verbatim from the DAO's hand-built SQL alias list --
 * DifferentTypesScreenImageDaoImpl.java:173-199). This endpoint is real and
 * working, but NOT called anywhere in legacy's own `qmslogo.js` -- that
 * controller has zero GET/load calls; it only ever POSTs on button click,
 * with nothing pre-populating existing images/values on page load. Exposed
 * here for completeness/because the endpoint is real, but the component
 * deliberately does not auto-call it on init, to match legacy's actual
 * (load-nothing) behavior rather than an assumed one -- see the qmslogo.ts
 * header comment.
 */
export interface QmsLogoScreenImageRow {
  screenImageId?: number;
  imageURL?: string;
  logoName?: string;
  imageTypeId?: number;
  createdBy?: number;
  createdDate?: string;
  status?: string;
  updatedBy?: number;
  updatedDate?: string;
  organizationId?: number;
}

@Injectable({ providedIn: 'root' })
export class QmslogoService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /**
   * File-field save (imageTypeId 1-6): multipart with a `file` part.
   * Angular's `HttpClient` sets the multipart boundary/Content-Type
   * automatically from the `FormData` body -- no explicit header needed
   * (legacy's own `{ 'Content-Type': undefined }` override exists only to
   * stop AngularJS's `$http` defaulting to `application/json`, which
   * `HttpClient` never does for a `FormData` body in the first place).
   */
  saveScreenImageFile(params: {
    imageTypeId: QmsLogoImageTypeId;
    organisationId: number;
    unitId: number;
    file: File;
  }): Observable<ApiResponse<QmsLogoSaveResult>> {
    const form = this.buildBaseForm(params.imageTypeId, params.organisationId, params.unitId);
    form.append('file', params.file);
    return this.http.post<ApiResponse<QmsLogoSaveResult>>(
      `${this.base}/api/unit/applicationConfiguration/screen-images`,
      form,
    );
  }

  /**
   * Text-field save (imageTypeId 7-8, Application Name / Copy Write Name):
   * same endpoint, `logoName` part instead of `file`. The backend branches
   * on `inputFile == null` (controller:180-184) to route to
   * `saveScreenApplicationName` instead of `saveScreenImage` -- so simply
   * omitting the `file` part (never appending it) is the correct, and only,
   * way to hit that branch; appending an empty string would NOT be null on
   * the Spring side for a required MultipartFile param behavior here since
   * it's `required = false`, but omitting it entirely matches legacy's own
   * `fd` construction, which also never appends `file` in these two
   * branches (qmslogo.js:271-280, :312-321).
   */
  saveScreenImageName(params: {
    imageTypeId: QmsLogoImageTypeId;
    organisationId: number;
    unitId: number;
    logoName: string;
  }): Observable<ApiResponse<QmsLogoSaveResult>> {
    const form = this.buildBaseForm(params.imageTypeId, params.organisationId, params.unitId);
    form.append('logoName', params.logoName);
    return this.http.post<ApiResponse<QmsLogoSaveResult>>(
      `${this.base}/api/unit/applicationConfiguration/screen-images`,
      form,
    );
  }

  /**
   * `GET .../screen-images/{orgId}/{imageTypeId}` -- real, working endpoint,
   * but not called by legacy's own qmslogo.js on load (see the interface
   * doc comment above). Provided so a future, explicitly-approved
   * "show current value" enhancement doesn't need to rediscover the
   * endpoint shape; not wired into this component's init flow, to avoid
   * inventing load-time behavior legacy doesn't have.
   */
  getScreenImage(organisationId: number, imageTypeId: QmsLogoImageTypeId): Observable<ApiResponse<QmsLogoScreenImageRow>> {
    return this.http.get<ApiResponse<QmsLogoScreenImageRow>>(
      `${this.base}/api/unit/applicationConfiguration/screen-images/${organisationId}/${imageTypeId}`,
    );
  }

  private buildBaseForm(imageTypeId: QmsLogoImageTypeId, organisationId: number, unitId: number): FormData {
    const now = formatLegacyDateTime(new Date());
    const form = new FormData();
    form.append('imageTypeId', String(imageTypeId));
    form.append('status', 'A');
    form.append('createdBy', String(imageTypeId));
    form.append('updatedBy', String(imageTypeId));
    form.append('createdDate', now);
    form.append('updatedDate', now);
    form.append('unitId', String(unitId));
    form.append('organisationId', String(organisationId));
    return form;
  }
}

/** Mirrors legacy's `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`. */
function formatLegacyDateTime(date: Date): string {
  const pad = (n: number) => String(n).padStart(2, '0');
  const day = pad(date.getDate());
  const month = pad(date.getMonth() + 1);
  const year = date.getFullYear();
  const hours = pad(date.getHours());
  const minutes = pad(date.getMinutes());
  const seconds = pad(date.getSeconds());
  return `${day}-${month}-${year} ${hours}:${minutes}:${seconds}`;
}
