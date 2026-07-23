import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  DeliveryTypeDetail,
  DeliveryTypeRow,
  DeliveryTypeSavePayload,
  DeliveryTypeStatusPayload,
  DeliveryTypeUpdatePayload,
} from './delivery-type-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/adt.url.config.js:220-225 and
 * deliveryTypeMasterController.js's own call sites, not guessed. BASE_URL/
 * ROOT_URL are both "" (same as this codebase's other `/adt` master
 * rebuilds -- district-master, ward-store-mapper), so these are the literal
 * relative paths legacy calls.
 *
 * **BUG #1 FIXED here (owner-approved, see component class doc comment for
 * the full writeup):** `getDeliveryTypeMasterList()` below issues a real
 * POST to `/adt/getDeliveryTypeMaster`. Legacy's own frontend calls this
 * same URL with `GenericService.serviceAction("GET", ...)` (ctrl:52-53), but
 * the real Java controller (`DeliveryTypeMasterController.java:35`,
 * `@RequestMapping(value="/getDeliveryTypeMaster", method =
 * RequestMethod.POST, consumes = APPLICATION_JSON_VALUE)`) is POST-only --
 * legacy's own list call returns a genuine HTTP 405 today, confirmed
 * directly from the controller source, not assumed. This method also always
 * sends `organizationId`, which legacy's own request body omits (ctrl:48,
 * `var data = {};`) even though the DAO's named query filters on it
 * (`DeliveryTypeMasterDaoImpl.getDeliveryTypeMasterList`,
 * `.setParameter("orgId", deliveryTypeMasterDto.getOrganizationId())`) --
 * with no orgId supplied Hibernate binds `null` and the DAO's own
 * `setFirstResult(0)/setMaxResults(10)` fallback silently returns up to 10
 * rows from ANY organization. Sending `organizationId` is the correct,
 * narrowly-scoped fix, not new behavior.
 *
 * **Real, confirmed backend endpoint NOT referenced anywhere in legacy's own
 * frontend:** `getDeliveryTypeCount()` calls `/adt/getDeliveryTypeCount`
 * (`DeliveryTypeMasterController.java:89-98` -> `DeliveryTypeMasterDaoImpl
 * .getCount`, a real, org-scoped `Restrictions.eq("organizationId", ...)`
 * count query) -- legacy's controller.js never calls it. Used here only to
 * size a single "fetch the whole list in one shot" request (see component
 * class doc comment), matching this codebase's `ward-store-mapper`/
 * `district-master` precedent of a dedicated count call driving one large
 * `noOfRecordsPerPage` list request, rather than re-implementing legacy's
 * own (nonexistent, for this screen) pagination UI.
 *
 * **FIX #2 (owner-approved):** `updateDeliveryTypeMaster()` below wires a
 * real, tested backend endpoint (`DeliveryTypeMasterController.java:46-55`)
 * that legacy's own UI can never reach today, because its live template is
 * missing the Update button entirely -- see component class doc comment.
 */
@Injectable({ providedIn: 'root' })
export class DeliveryTypeMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:48-64 (init/GETDELIVERYTYPELIST) -- POST (fixed), always includes
  // organizationId (fixed). See class doc comment.
  getDeliveryTypeMasterList(
    organizationId: number,
    offset: number,
    noOfRecordsPerPage: number,
  ): Observable<ApiResponse<never, DeliveryTypeRow>> {
    return this.http.post<ApiResponse<never, DeliveryTypeRow>>(`${this.base}/adt/getDeliveryTypeMaster`, {
      organizationId,
      offset,
      noOfRecordsPerPage,
    });
  }

  // Real endpoint, not called by legacy's own frontend -- see class doc comment.
  getDeliveryTypeCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.post<ApiResponse<number>>(`${this.base}/adt/getDeliveryTypeCount`, { organizationId });
  }

  // ctrl:78-125 (saveDeliveryType, ADDDELIVERYTYPE) -- real create, gated
  // behind the confirm modal (see component class doc comment).
  saveDeliveryTypeMaster(payload: DeliveryTypeSavePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/saveDeliveryTypeMaster`, payload);
  }

  // ctrl:127-155 (showUpdateBtn, GETSINGLEDELIVERYTYPE) -- POST
  // {deliveryTypeId}; response.data.listObject[0] is the record.
  getDeliveryTypeById(deliveryTypeId: number): Observable<ApiResponse<never, DeliveryTypeDetail>> {
    return this.http.post<ApiResponse<never, DeliveryTypeDetail>>(`${this.base}/adt/getDeliveryTypeById`, {
      deliveryTypeId,
    });
  }

  // FIX #2 -- see class doc comment: real, tested, previously-unreachable endpoint.
  updateDeliveryTypeMaster(payload: DeliveryTypeUpdatePayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateDeliveryTypeMaster`, payload);
  }

  // ctrl:164-197 (updateStatus, UPDATEDELIVERYTYPESTATUS) -- real status toggle.
  updateStatusForDeliveryType(payload: DeliveryTypeStatusPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/adt/updateStatusForDeliveryType`, payload);
  }
}
