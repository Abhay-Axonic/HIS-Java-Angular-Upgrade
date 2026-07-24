import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  MortuaryBedDetail,
  MortuaryBedRow,
  MortuaryBedSavePayload,
  MortuaryBedStatusPayload,
  MortuaryBedUpdatePayload,
} from './mortuary-bed-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/unit/mortuaryBedMasterController.js
 * and their literal string values in
 * resources/assets/js/config/adt.url.config.js:550-556:
 *   GETMORTUARYBEDLIST      = /adt/getMortuaryBedList      (ctrl:97-98)
 *   GETMORTUARYBEDCOUNT     = /adt/getMortuaryBedCount     (ctrl:98)
 *   SAVEMORTUARYBEDS        = /adt/saveMortuaryBeds        (ctrl:224-225)
 *   GETMOURTUARYBEDBYID     = /adt/getMortuaryBedListById  (ctrl:269-270 --
 *                              constant NAME has a "Mourtuary" typo, but its
 *                              STRING VALUE does not; the real URL is spelled
 *                              correctly)
 *   UPDATEMORTUARYBEDMASTER = /adt/updateMortuaryBedMaster (ctrl:355-356)
 *   UPDATESTATUSFORMORTUARYBED = /adt/updateStatusForMortuaryBed (ctrl:311-312)
 *
 * `BASE_URL`/`ROOT_URL` (config/url.config.js) are both `''` in this
 * deployment, so these are the complete paths -- bare `/adt`, NO `/api`
 * prefix (confirmed directly against `MortuaryBedMasterController.java`'s
 * `@RequestMapping("/adt")`). ALL 6 calls are POST in legacy
 * (`GenericService.serviceAction("POST", ...)`), including the by-id lookup.
 *
 * `getActiveMortuaryBeds` (`GETACTIVEMORTUARYBEDS` = `/adt/getActiveMortuaryBeds`)
 * exists in both the URL config and the Java controller but is never called
 * anywhere in this 389-line frontend controller (confirmed by full-file
 * read) -- not modeled here, dead from this screen's perspective.
 * `getMorturyBedListByStatusId`/`allotMortuaryBed`/`releaseMorturyBed`/
 * `getDataForMorturyMapOfBed` all belong to the separate, OUT-OF-SCOPE
 * allotment/management workflow (`mortuaryBedManagementController.js`) --
 * not touched here.
 */
@Injectable({ providedIn: 'root' })
export class MortuaryBedMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:91-138 -- POST { organizationId, offset, noOfRecordsPerPage, unitId }.
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, MortuaryBedRow>> {
    return this.http.post<ApiResponse<never, MortuaryBedRow>>(`${this.base}/adt/getMortuaryBedList`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
      unitId,
    });
  }

  // ctrl:107-110 -- POST { organizationId, unitId }, count returned in `object`.
  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getMortuaryBedCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:195-257 (saveMortuaryBed) -- POST, real create.
  save(payload: MortuaryBedSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveMortuaryBeds`, payload);
  }

  // ctrl:264-270/259-288 (showUpdateBtn) -- POST { mortuaryBedId },
  // response.data.listObject[0] is the record.
  getById(mortuaryBedId: number): Observable<ApiResponse<never, MortuaryBedDetail>> {
    return this.http.post<ApiResponse<never, MortuaryBedDetail>>(`${this.base}/adt/getMortuaryBedListById`, {
      mortuaryBedId,
    });
  }

  // ctrl:338-386 (updateMortuaryBed) -- POST, real update (no confirm-modal
  // step, unlike create).
  update(payload: MortuaryBedUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateMortuaryBedMaster`, payload);
  }

  // ctrl:296-336 (updateStatus) -- POST, real status toggle.
  updateStatus(payload: MortuaryBedStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForMortuaryBed`, payload);
  }
}
