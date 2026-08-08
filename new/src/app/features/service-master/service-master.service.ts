import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  ChangeServiceMasterStatusPayload,
  GeneralLedgerMaster,
  InventoryItemLine,
  ItemMaster,
  SaveServiceMasterPayload,
  ServiceMasterRecord,
  SpecialityMaster,
  SubSpecialityMaster,
  UpdateServiceMasterPayload,
} from './service-master.models';

/**
 * Endpoint paths taken directly from the legacy controller's
 * serviceAction calls and the billing.url.config.js / global.url.config.js /
 * adt.url.config.js constant values:
 *   GETSPECIALITY                       "/adt/getSpecialityList"
 *   GETGENERALLEDGERMASTERACTIVELIST    "/org/getGeneralLedgerMasterActiveList"
 *   GET_ORGANIZATION_SERVICE_MASTER_LIST    BILLING + "/listOrganizationServiceMaster"
 *   GET_ORGANIZATION_SERVICE_MASTER_COUNT   BILLING + "/getOrganizationServiceMasterCount"
 *   GET_ORGANIZATION_SERVICE_MASTER_BY_ID   "/getOrganizationServiceMasterById"
 *   SAVE_ORGANIZATION_SERVICE_MASTER        "/saveOrganizationServiceMaster"
 *   UPDATEGLOBALSERVICEMASTER               BILLING + "/updateGlobalServiceMaster"
 *   CHANGE_ORGANIZATION_SERVICE_MASTER_STATUS "/changeOrganizationServiceMasterStatus"
 *   (hardcoded) "/billing/getServiceItemMapperDetails",
 *               "/billing/serviceMasterUniversalSearch",
 *               "/api/globals/items/typeahead/search",
 *               "/api/globals/items/item/search/{string}"
 * BILLING = "/billing".
 *
 * Methods and URL shapes are reproduced as-is (the status toggle is a real
 * PUT here, the sub-speciality fetch a real GET) — do not "normalize" them.
 */
@Injectable({ providedIn: 'root' })
export class ServiceMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  /** POST /adt/getSpecialityList {organizationId, unitId} -> specialityMasterList */
  getSpecialityList(organizationId: number, unitId: number): Observable<ApiResponse<never, SpecialityMaster>> {
    return this.http.post<ApiResponse<never, SpecialityMaster>>(`${this.base}/adt/getSpecialityList`, {
      organizationId,
      unitId,
    });
  }

  /** POST /org/getGeneralLedgerMasterActiveList/{orgId} {} -> ledgerList */
  getGeneralLedgerList(organizationId: number): Observable<ApiResponse<never, GeneralLedgerMaster>> {
    return this.http.post<ApiResponse<never, GeneralLedgerMaster>>(
      `${this.base}/org/getGeneralLedgerMasterActiveList/${organizationId}`,
      {},
    );
  }

  /** GET /adt/subspeciality/orgId/{orgId}/specialityId/{specialityId} -> subSpecialityMasterList */
  getSubSpeciality(organizationId: number, specialityId: number): Observable<ApiResponse<never, SubSpecialityMaster>> {
    return this.http.get<ApiResponse<never, SubSpecialityMaster>>(
      `${this.base}/adt/subspeciality/orgId/${organizationId}/specialityId/${specialityId}`,
    );
  }

  /** GET /billing/listOrganizationServiceMaster/{orgId}/{offset}/{noOfRecordsPerPage} -> serviceList */
  getList(organizationId: number, offset: number, noOfRecordsPerPage: number): Observable<ApiResponse<never, ServiceMasterRecord>> {
    return this.http.get<ApiResponse<never, ServiceMasterRecord>>(
      `${this.base}/billing/listOrganizationServiceMaster/${organizationId}/${offset}/${noOfRecordsPerPage}`,
    );
  }

  /** GET /billing/getOrganizationServiceMasterCount/{orgId} -> object (number) */
  getCount(organizationId: number): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(`${this.base}/billing/getOrganizationServiceMasterCount/${organizationId}`);
  }

  /** POST /billing/getOrganizationServiceMasterById {serviceMasterId, unitId, organizationId} -> object */
  getById(serviceMasterId: number, unitId: number, organizationId: number): Observable<ApiResponse<ServiceMasterRecord>> {
    return this.http.post<ApiResponse<ServiceMasterRecord>>(`${this.base}/billing/getOrganizationServiceMasterById`, {
      serviceMasterId,
      unitId,
      organizationId,
    });
  }

  /** POST /billing/saveOrganizationServiceMaster */
  save(payload: SaveServiceMasterPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/billing/saveOrganizationServiceMaster`, payload);
  }

  /** PUT /billing/updateGlobalServiceMaster */
  update(payload: UpdateServiceMasterPayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/billing/updateGlobalServiceMaster`, payload);
  }

  /** PUT /billing/changeOrganizationServiceMasterStatus */
  changeStatus(payload: ChangeServiceMasterStatusPayload): Observable<ApiResponse> {
    return this.http.put<ApiResponse>(`${this.base}/billing/changeOrganizationServiceMasterStatus`, payload);
  }

  /** POST /billing/serviceMasterUniversalSearch {organizationId, serviceStandardName} -> listObject */
  universalSearch(organizationId: number, serviceStandardName: string): Observable<ApiResponse<never, ServiceMasterRecord>> {
    return this.http.post<ApiResponse<never, ServiceMasterRecord>>(`${this.base}/billing/serviceMasterUniversalSearch`, {
      organizationId,
      serviceStandardName,
    });
  }

  /** POST /billing/getServiceItemMapperDetails {serviceId, organizationId} -> inventory lines */
  getServiceItemMapperDetails(serviceId: number, organizationId: number): Observable<ApiResponse<never, InventoryItemLine>> {
    return this.http.post<ApiResponse<never, InventoryItemLine>>(`${this.base}/billing/getServiceItemMapperDetails`, {
      serviceId,
      organizationId,
    });
  }

  /** GET /api/globals/items/item/search/{string} — the ACTIVE item typeahead
   *  (legacy `complete()`, controller.js:218-229; the POST
   *  /api/globals/items/typeahead/search is dead code — `searchItems()` is
   *  defined but never wired to the HTML). Returns a raw array.
   */
  searchItems(itemName: string): Observable<ItemMaster[]> {
    return this.http.get<ItemMaster[]>(`${this.base}/api/globals/items/item/search/${itemName}`);
  }
}
