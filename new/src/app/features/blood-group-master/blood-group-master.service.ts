import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  BloodGroupDetail,
  BloodGroupRow,
  BloodGroupSavePayload,
  BloodGroupStatusPayload,
  BloodGroupUpdatePayload,
} from './blood-group-master.models';

/**
 * Endpoint paths taken directly from
 * `resources/assets/js/config/bloodBank.url.config.js` (all 6 build
 * `BASE_URL + ROOT_URL + "/global/bloodBank/..."` literals -- both
 * `BASE_URL`/`ROOT_URL` are empty strings in this deployment,
 * `url.config.js:13,19`). Confirmed real end to end by reading source
 * directly: `BloodGroupMasterController.java`
 * (`@RequestMapping("global/bloodBank")`, NOT `/api/global/...` like
 * `storeTypeMaster`'s sibling controller) -> `BloodGroupMasterServiceImpl`
 * -> `BloodGroupMasterDaoImpl` -> table `blood_bank.m_blood_group_master`.
 * `getBloodGroupMasterList` independently confirmed live 200 in the prior
 * batch-17 research pass (entry 158) against a real authenticated session
 * -- re-confirmed here via a fresh golden-trace capture against
 * `localhost:8080` (see this screen's `.ts` class doc comment).
 *
 * ================================================================
 * CONFIRMED BACKEND DEFECT, NOT FIXED HERE (legacy bug, logged, not a
 * rebuild bug) -- the duplicate-description check is GLOBAL, across every
 * organization AND unit, not scoped to the current org/unit at all
 * ================================================================
 * `GET_BLOOD_GROUP_MASTER_BY_NAME`/`GET_BLOOD_GROUP_MASTER_BY_NAME_NOT_ID`
 * (`BloodGroupMaster.java:47-58`, used by `saveBloodGroupMaster`/
 * `updateBloodGroupMaster`'s duplicate guard,
 * `BloodGroupMasterServiceImpl.java:27-63`) read:
 * `WHERE LOWER(bloodGroupDesc)=:bloodGroupDesc` (save) /
 * `WHERE bloodGroupId!=:bloodGroupId AND LOWER(bloodGroupDesc)=:bloodGroupDesc`
 * (update) -- neither clause filters on `organizationId` or `unitId` AT
 * ALL, despite both being real columns on the entity and sent on every
 * payload. Net effect: creating "Rh Null" under Organization B's own Blood
 * Bank would be incorrectly rejected as "Already Exists." if Organization
 * A already has an (entirely unrelated) blood-group row with that same
 * description -- the same defect *shape* as `GLcode`'s confirmed
 * globally-scoped duplicate check (entry 160), and worse than
 * `storeTypeMaster`'s org-scoped-only gap (entry 161) in that there is no
 * scoping clause here at all, not even organization-level. The check also
 * only ever compares `bloodGroupDesc`, never `bloodGroupCode` -- two rows
 * with the same code but different descriptions are NOT caught as
 * duplicates by this guard, matching how `save()`/`update()`'s own client
 * validation never cross-checks code against an existing row either
 * (see this screen's `.ts` class doc comment). Not exercised further live
 * here since the local dev DB only has one seeded organization/unit --
 * confirmed via source reading, not fabricated multi-org data, per this
 * project's no-unrequested-data-fabrication discipline. Reproduced as-is;
 * the frontend has no way to compensate for a server-side scope decision.
 * Flagged in this batch's findings entry.
 */
@Injectable({ providedIn: 'root' })
export class BloodGroupMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:58-97 (initMasterList) -- POST {organizationId, offset,
  // noOfRecordsPerPage, unitId}.
  getList(
    organizationId: number,
    unitId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, BloodGroupRow>> {
    return this.http.post<ApiResponse<never, BloodGroupRow>>(`${this.base}/global/bloodBank/getBloodGroupMasterList`, {
      organizationId,
      unitId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // ctrl:71-74/88-95 (same function, the parallel count call) -- POST
  // {organizationId, unitId}; count returned in `object`. Confirmed
  // org+unit scoped both here (Hibernate Criteria,
  // `BloodGroupMasterDaoImpl.java:174-194`) AND in getList/getById, unlike
  // `storeTypeMaster`'s scope mismatch -- this screen has no list/count
  // scope divergence.
  getCount(organizationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/global/bloodBank/getBloodGroupMasterCount`, {
      organizationId,
      unitId,
    });
  }

  // ctrl:146-219 (save). See class doc comment: the backend's own
  // duplicate check (getBloodGroupMasterByName) is scoped GLOBALLY by
  // `bloodGroupDesc` only -- NOT by organizationId/unitId/bloodGroupCode.
  save(payload: BloodGroupSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/bloodBank/saveBloodGroupMaster`, payload);
  }

  // ctrl:226-254 (showUpdateBtn) -- a real GET with path params
  // (`/{bloodGroupId}/{organizationId}/{unitId}`), unlike
  // `storeTypeMaster`'s POST-with-body equivalent.
  getById(bloodGroupId: number, organizationId: number, unitId: number): Observable<ApiResponse<never, BloodGroupDetail>> {
    return this.http.get<ApiResponse<never, BloodGroupDetail>>(
      `${this.base}/global/bloodBank/getBloodGroupMasterById/${bloodGroupId}/${organizationId}/${unitId}`,
    );
  }

  // ctrl:257-320 (update). Same globally-scoped duplicate check applies
  // here too (getBloodGroupMasterByNameNotId) -- see class doc comment.
  update(payload: BloodGroupUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/bloodBank/updateBloodGroupMaster`, payload);
  }

  // ctrl:322-358 (updateStatus) -- real status toggle. The DAO
  // (`updateBloodGroupMasterStatus`, `BloodGroupMasterDaoImpl.java:92-111`)
  // loads the row by `bloodGroupId` and only ever writes
  // `status`/`updatedBy`/`updatedDate` back onto it -- `bloodGroupCode`/
  // `bloodGroupDesc` are never touched by this call, confirmed by reading
  // the DAO directly, so this rebuild sends only the 4 fields legacy's own
  // payload actually needs (see `BloodGroupStatusPayload`'s doc comment).
  updateStatus(payload: BloodGroupStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/global/bloodBank/updateBloodGroupMasterStatus`, payload);
  }
}
