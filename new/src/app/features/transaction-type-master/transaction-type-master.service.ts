import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  TransactionTypeRow,
  TransactionTypeSavePayload,
  TransactionTypeStatusPayload,
  TransactionTypeUpdatePayload,
} from './transaction-type-master.models';

/**
 * Endpoint paths per this batch's Step 0/1 audit
 * (`docs/ANGULAR_PENDING_FINDINGS.md`,
 * `PENDING-batch18-step1-transactiontypemaster`) -- all 4 confirmed real,
 * live, authenticated Controller -> Service -> DAO chains under
 * `TransactionTypeController`'s class-level `@RequestMapping("/api/billing")`
 * (`legacy/Global/.../controller/TransactionTypeController.java`). Taken
 * directly from `resources/assets/js/controller/master/
 * transactionTypeMasterController.js` -- legacy's own
 * `GenericService.serviceAction` calls every one of these as POST, including
 * update (the Java controller method itself is
 * `@RequestMapping(method = RequestMethod.POST)`, not PUT, unlike
 * `billing-group-master`'s sibling convention -- confirmed directly from the
 * Java source, not guessed).
 *
 * `getList` deliberately takes only `orgId` -- no `unitId`, no
 * `offset`/`recordPerPage`. Golden trace (live, this session) showed exactly
 * 2 calls on load (a template fetch + this one, both 200) and NO separate
 * count-endpoint call: legacy's own `init()` POSTs `{ orgId }` once and gets
 * back the FULL org-scoped list unconditionally, then paginates it entirely
 * client-side via `PaginationService.searchData()`. There is no
 * `getTransactionTypeMasterCount`-shaped endpoint for this screen to call.
 */
@Injectable({ providedIn: 'root' })
export class TransactionTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(orgId: number): Observable<ApiResponse<never, TransactionTypeRow>> {
    return this.http.post<ApiResponse<never, TransactionTypeRow>>(
      `${this.base}/api/billing/getTransactionTypeMasterList`,
      { orgId },
    );
  }

  save(payload: TransactionTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/saveTransactionTypeMaster`, payload);
  }

  update(payload: TransactionTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updateTransactionTypeMaster`, payload);
  }

  updateStatus(payload: TransactionTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/billing/updateStatusForTransactionType`, payload);
  }
}
