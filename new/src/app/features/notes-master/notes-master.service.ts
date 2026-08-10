/**
 * notes-master — parity rebuild of the legacy
 *   views/master/GlobalMaster/OTIMS/notes-master.html
 *   (menu node 529 'Anaesthesia Note', parent 514 OTIMS -> 369 Global Master,
 *   controller notesMasterController, loaded index.html:1741).
 *
 * SCREEN 24 — DANGLE class WITH a routing mismatch (the logged one-character
 * legacy defect QF-2026-08-10-AG): node 529's actionLink is '#/noteMaster'
 * (a state that does NOT exist) while the state's url is '/notesMaster'.
 * The rebuild uses the STATE's url (per the owner's instruction) — the node
 * link stays broken in legacy until the legacy fix lands.
 *
 * PHANTOM-BROKEN LIST (the walk's FOURTH phantom finding): every list read
 * 500s with a correlation-id envelope (ERR-e81a54b3 / ERR-7f29a04a —
 * "could not extract ResultSet"). Root cause: GET_NOTELIST_MASTER_LIST
 * selects 8 columns that DO NOT EXIST in the DDL — otims.m_note_master has
 * only 12 columns (note_master_id, code, description, note_type, template,
 * created_date, created_by, status, updated_by, updated_date, unit_id,
 * organization_id) but the entity maps template_format_id, form_template_id,
 * form_template_keywords, template_type_id, template_id, title
 * (NotesMaster.java:129-147) and the query selects them. The rebuild CALLS
 * the list, detects the error envelope, and surfaces the phantom honestly
 * (the screen-17 pattern) — the screen cannot be green while the backend
 * 500s.
 */
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { inject } from '@angular/core';

export interface NotesMasterRow {
  noteMasterId: number;
  code: string;
  description: string;
  noteType: string;
  template: string;
  status: string;
}

export interface ApiEnvelope {
  status: string;
  code?: string;
  message?: string | null;
  listObject?: unknown[] | null;
  object?: unknown;
}

@Injectable({ providedIn: 'root' })
export class NotesMasterService {
  private base = inject(API_BASE_URL);
  private http = inject(HttpClient);

  list(orgId: number, unitId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/global/otims/getNotesMasterListPagination`, {
      organizationId: orgId, unitId, offset, noOfRecordsPerPage,
    });
  }

  count(orgId: number, unitId: number): Observable<ApiEnvelope> {
    return this.http.post<ApiEnvelope>(`${this.base}/global/otims/getNotesMasterListCount`, {
      organizationId: orgId, unitId,
    });
  }
}
