import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { StorageUnitRequest, StorageUnitRow } from './storage-unit-master.models';

/**
 * Endpoint paths taken directly from `StorageUnitController.java`, all
 * under `/api/globals/storage/units/*`. Modern REST-style controller
 * (`ResponseEntity`), NOT the old `{status,message,listObject,object}`
 * envelope used by every other batch 22/23 screen -- `GetAll` returns a
 * raw JSON array; Create/Update return a bare `{id, message}` body with
 * HTTP 201, no `status` field at all. Success/failure is read from the
 * HTTP status code, not a body field -- see class doc.
 */
@Injectable({ providedIn: 'root' })
export class StorageUnitMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // controller:41-46 (GetAll) -- scoped by organizationId ONLY, no unitId
  // anywhere in this screen's API (unlike every other screen this batch).
  getAll(orgId: number): Observable<StorageUnitRow[]> {
    return this.http.get<StorageUnitRow[]>(`${this.base}/api/globals/storage/units/${orgId}`);
  }

  // controller:48-59 (GetById)
  getById(orgId: number, id: number): Observable<StorageUnitRow> {
    return this.http.get<StorageUnitRow>(`${this.base}/api/globals/storage/units/${orgId}/${id}`);
  }

  // controller:61-72 (Create) -- HTTP 201 on success, body {id, message}.
  create(orgId: number, payload: StorageUnitRequest): Observable<{ id: number; message: string }> {
    return this.http.post<{ id: number; message: string }>(`${this.base}/api/globals/storage/units/${orgId}`, payload);
  }

  // controller:74-93 (Update) -- also used for status-toggle (partial
  // body, service only overwrites fields that are present). HTTP 201 on
  // success. If deactivating a unit still assigned to at least one Item,
  // the service reverts the status and the call fails (non-2xx) with
  // message "Storage Unit is assigned to Items" -- see class doc,
  // faithfully reproduced, not worked around.
  update(orgId: number, id: number, payload: StorageUnitRequest): Observable<{ id: number; message: string }> {
    return this.http.patch<{ id: number; message: string }>(`${this.base}/api/globals/storage/units/${orgId}/${id}`, payload);
  }
}
