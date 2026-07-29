import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  GeneralInsuranceMasterRecord,
  GeoOption,
  SaveGeneralInsurancePayload,
  SaveResponse,
  ToggleStatusPayload,
  VendorDropdownResponse,
} from './general-insurance-master.models';

/**
 * Endpoint paths taken directly from
 * generalInsuranceMasterController.js + constants.js's
 * `MASTER.UNIT.GENERAL_INSURANCE_MASTER` / `GLOBAL.STATE_API` /
 * `GLOBAL.CITY_API` / `GLOBAL.AREA_API` / `MASTER.UNIT.PHARAMACY.
 * VENDOR_MASTER.DROPDOWN_API` blocks, confirmed against the real Java
 * controllers (GeneralInsuranceMasterController.java, VendorController.java).
 *
 * Real oddity, confirmed not a mistake to "fix": the Country dropdown and
 * the State/City/Area cascade all hit the *Vendor* master's own endpoints
 * (`/api/unit/vendors/...`), not anything general-insurance-specific --
 * legacy genuinely reuses VendorController's generic address-lookup
 * endpoints here. Reproduced as-is.
 *
 * None of these endpoints return the shared `{status, object, listObject}`
 * envelope other screens use -- see general-insurance-master.models.ts's
 * top comment. Every method below returns the RAW body shape.
 */
@Injectable({ providedIn: 'root' })
export class GeneralInsuranceMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getDropdown(unitId: number, organizationId: number): Observable<VendorDropdownResponse> {
    return this.http.get<VendorDropdownResponse>(
      `${this.base}/api/unit/vendors/dropdown/${unitId}/${organizationId}`,
    );
  }

  getStates(organizationId: number, countryId: number): Observable<GeoOption[]> {
    return this.http.get<GeoOption[]>(`${this.base}/api/unit/vendors/states/${organizationId}/${countryId}`);
  }

  getCities(organizationId: number, stateId: number): Observable<GeoOption[]> {
    return this.http.get<GeoOption[]>(`${this.base}/api/unit/vendors/cities/${organizationId}/${stateId}`);
  }

  getAreas(organizationId: number, cityId: number): Observable<GeoOption[]> {
    return this.http.get<GeoOption[]>(`${this.base}/api/unit/vendors/areas/${organizationId}/${cityId}`);
  }

  getList(): Observable<GeneralInsuranceMasterRecord[]> {
    return this.http.get<GeneralInsuranceMasterRecord[]>(`${this.base}/api/unit/generalinsurance`);
  }

  getById(id: number): Observable<GeneralInsuranceMasterRecord> {
    return this.http.get<GeneralInsuranceMasterRecord>(`${this.base}/api/unit/generalinsurance/${id}`);
  }

  save(payload: SaveGeneralInsurancePayload): Observable<SaveResponse> {
    return this.http.post<SaveResponse>(`${this.base}/api/unit/generalinsurance/save`, payload);
  }

  update(id: number, payload: SaveGeneralInsurancePayload): Observable<SaveResponse> {
    return this.http.patch<SaveResponse>(`${this.base}/api/unit/generalinsurance/${id}`, payload);
  }

  /** Same PATCH endpoint/id as update() -- narrower body, see
   * ToggleStatusPayload's doc comment. */
  toggleStatus(id: number, payload: ToggleStatusPayload): Observable<SaveResponse> {
    return this.http.patch<SaveResponse>(`${this.base}/api/unit/generalinsurance/${id}`, payload);
  }
}
