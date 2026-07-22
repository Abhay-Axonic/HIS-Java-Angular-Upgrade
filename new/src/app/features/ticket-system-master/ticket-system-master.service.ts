import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { TicketAttachment, TicketSystemMasterRecord } from './ticket-system-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/unit/ticketSystemMasterController.js,
 * backed by `TicketSystemMasterController.java` (`@RequestMapping("/api/ticketsystemmaster")`)
 * -> `TicketSystemMasterServiceImpl` -> `TicketSystemMasterDaoImpl`. Full
 * chain confirmed real for all 9 endpoints below.
 *
 * IMPORTANT (see docs/ANGULAR_PENDING_FINDINGS.md entry N, this batch):
 * `public.m_ticket_system_master` and `public.ticket_system_master_seq` --
 * confirmed via live Tomcat logs and a direct `psql` check against this
 * environment's `his_local` database -- do not exist in this dev DB. Every
 * one of these calls fails identically in legacy today (same DB, same
 * missing table/sequence). Local-environment gap, same class as
 * BR-038/039/043/046 and entries 62/100/101, not a rebuild defect.
 */
@Injectable({ providedIn: 'root' })
export class TicketSystemMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /**
   * Faithful to legacy: this endpoint takes no query/body params at all --
   * the backend's named query hardcodes `LIMIT 10`, so this ALWAYS returns
   * (at most) the latest 10 tickets regardless of the page-size control.
   * Real, page-size-aware pagination only happens via `search()` below.
   */
  getAll(unitId: number, organizationId: number): Observable<ApiResponse<number, TicketSystemMasterRecord>> {
    return this.http.get<ApiResponse<number, TicketSystemMasterRecord>>(
      `${this.base}/api/ticketsystemmaster/getall/${unitId}/${organizationId}`,
    );
  }

  save(payload: Record<string, unknown>): Observable<ApiResponse<{ ticketMasterId: number }>> {
    return this.http.post<ApiResponse<{ ticketMasterId: number }>>(`${this.base}/api/ticketsystemmaster/save`, payload);
  }

  update(payload: Record<string, unknown>): Observable<ApiResponse<TicketSystemMasterRecord>> {
    return this.http.patch<ApiResponse<TicketSystemMasterRecord>>(`${this.base}/api/ticketsystemmaster/update`, payload);
  }

  /** `ticketStatus` is the NEW status being set (matches legacy: the checkbox's two-way binding already flips it before this fires). */
  changeStatus(ticketMasterId: number, unitId: number, organizationId: number, ticketStatus: 'A' | 'I'): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(
      `${this.base}/api/ticketsystemmaster/remove/${ticketMasterId}/${unitId}/${organizationId}/${ticketStatus}`,
      {},
    );
  }

  /**
   * Multipart upload -- matches legacy's GenericServiceFileUpload call
   * exactly, including sending the request with zero `files` parts when
   * the user attached none (legacy calls this unconditionally after every
   * successful save/update, see ticket-system-master.ts's class doc
   * comment for the possible backend implication).
   */
  uploadFiles(
    files: File[],
    ticketMasterId: number,
    updateFileFlag: boolean,
    userId: number,
    unitId: number,
    organizationId: number,
    organizationName: string,
  ): Observable<ApiResponse<TicketSystemMasterRecord>> {
    const form = new FormData();
    for (const file of files) {
      form.append('files', file);
    }
    form.append('updateFileFlage', String(updateFileFlag));
    form.append('ticketMasterId', String(ticketMasterId));
    form.append('userId', String(userId));
    form.append('unitId', String(unitId));
    form.append('organizationId', String(organizationId));
    form.append('organizationName', organizationName);
    return this.http.post<ApiResponse<TicketSystemMasterRecord>>(`${this.base}/api/ticketsystemmaster/savefile`, form);
  }

  getAttachments(ticketMasterId: number, unitId: number, organizationId: number): Observable<ApiResponse<never, TicketAttachment>> {
    return this.http.get<ApiResponse<never, TicketAttachment>>(
      `${this.base}/api/ticketsystemmaster/getfile/byticketid/${ticketMasterId}/${unitId}/${organizationId}`,
    );
  }

  removeFile(fileId: number, unitId: number, organizationId: number): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/ticketsystemmaster/removefile/${fileId}/${unitId}/${organizationId}`, {});
  }

  removeAllFiles(ticketMasterId: number, unitId: number, organizationId: number): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(
      `${this.base}/api/ticketsystemmaster/removeallfile/${ticketMasterId}/${unitId}/${organizationId}`,
      {},
    );
  }

  /**
   * Real server-side pagination path (offset/limit honored) -- unlike
   * getAll() above. `searchKeyWord` (when non-empty) matches ONLY
   * ticket_issue_description and ticket_contact_number (case-insensitive
   * substring) -- NOT reportedBy, email, or ticketNumber, confirmed from
   * TicketSystemMasterDaoImpl.searchTicketSystem's actual SQL.
   */
  search(payload: {
    unitId: number;
    organizationId: number;
    offSet: number;
    noOfRecordsPerPage: number;
    searchKeyWord?: string | null;
  }): Observable<ApiResponse<number, TicketSystemMasterRecord>> {
    return this.http.post<ApiResponse<number, TicketSystemMasterRecord>>(`${this.base}/api/ticketsystemmaster/search`, payload);
  }
}
