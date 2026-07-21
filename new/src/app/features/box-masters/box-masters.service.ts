import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { BoxDetail, BoxRow, BoxSavePayload, BoxStatusPayload, BoxUpdatePayload } from './box-masters.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/billing/unit/boxMastersController.js. The
 * `boxMasters` state self-registers its OWN `.config($stateProvider)` block
 * in that same file (ctrl:258-285) -- NOT in app.js like most other
 * screens -- url `/boxMasters`, moduleId 1069, confirmed live via the
 * sidebar's own `checkAccess(1069)` entry (views/common/new-side-bar.html:3952).
 *
 * NOTE: unlike amenity-master/qms-department-master, legacy has only ONE
 * list endpoint here -- `getMrdBoxList` returns BOTH the current page
 * (`listObject`) and the total row count (`object`) in a single response
 * (ctrl:90-100, 121-131, 146-156); there is no separate
 * `.../getMrdBoxListCount`. Modeled as one method below rather than
 * splitting an endpoint legacy doesn't actually have.
 *
 * Flag for Step 5 live verification, not assumed either way: the backing
 * `m_mrd_box_master` table is listed in `docs/br076_missing_tables.sql` as
 * absent from this dev environment -- the same gap class that produced a
 * live HTTP 500 on the sibling `rowMasters` screen
 * (`docs/ANGULAR_PENDING_FINDINGS.md` entry 62). The rebuild calls the real,
 * field-for-field-correct endpoints regardless of whether that table exists
 * here today.
 */
@Injectable({ providedIn: 'root' })
export class BoxMastersService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // Unifies ctrl:82-101 (getBox), ctrl:109-131 (initBoxGroupMasterList), and
  // ctrl:134-156 (masterList) -- all three are the identical POST with
  // different offset values. See the component's normalized single-reload
  // doc comment for why this is one method here instead of three.
  getBoxList(organizationId: number, unitId: number, page: number, size: number): Observable<ApiResponse<number, BoxRow>> {
    return this.http.post<ApiResponse<number, BoxRow>>(`${this.base}/mrdbox/getMrdBoxList`, {
      organizationId,
      unitId,
      page,
      size,
    });
  }

  // ctrl:31-79, real create.
  saveBox(payload: BoxSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/mrdbox/saveMrdBox`, payload);
  }

  // ctrl:174-195 -- GET, 3 path segments (boxId/organizationId/unitId);
  // response.data.object is the record.
  getBoxById(boxId: number, organizationId: number, unitId: number): Observable<ApiResponse<BoxDetail>> {
    return this.http.get<ApiResponse<BoxDetail>>(`${this.base}/mrdbox/getMrdBoxById/${boxId}/${organizationId}/${unitId}`);
  }

  // ctrl:197-224, real update (no confirm-modal step in legacy -- this
  // screen doesn't use the shared `<modal>` directive at all, see component
  // doc comment).
  updateBox(payload: BoxUpdatePayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/mrdbox/updateMrdBox`, payload);
  }

  // ctrl:226-250, real status toggle.
  updateBoxStatus(payload: BoxStatusPayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/mrdbox/updateMrdBoxStatus`, payload);
  }
}
