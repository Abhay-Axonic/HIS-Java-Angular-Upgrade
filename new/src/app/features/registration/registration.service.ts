import { HttpClient } from '@angular/common/http';
import { inject, Injectable } from '@angular/core';
import { map, Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import type { ApiResponse } from '../../core/api-response';
import type { FieldConfig, GeoRow, IdNameRow, RegistrationPayload } from './registration.models';

/** The legacy registration's endpoint paths — resolved from the url.config
 * constants (including the legacy's `getAvtive...` typos, ported faithfully). */
@Injectable({ providedIn: 'root' })
export class RegistrationService {
  private http = inject(HttpClient);
  private base = inject(API_BASE_URL);

  private post<T>(url: string, body: unknown): Observable<ApiResponse<never, T>> {
    return this.http.post<ApiResponse<never, T>>(`${this.base}${url}`, body);
  }

  getPrefixList(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    // the legacy endpoint is a GET with the orgId in the path (WebAppointmentController).
    // The named query GET_ACTIVE_PREFIX_LIST joins the gender master and puts the
    // GENDER's desc in `desc` (that's why 'Male' showed); the real prefix text is
    // in `prefixDesc` — map it into `name` so the dropdown shows Mr./Mrs./Ms./Dr.
    return this.http
      .get<ApiResponse<never, IdNameRow>>(`${this.base}/api/webappointment/getActivePrefixList/${orgId}`, {
        params: { unitId },
      })
      .pipe(
        map((r) => ({
          ...r,
          listObject: (r.listObject ?? []).map((row) => ({
            id: row.id,
            name: row.prefixDesc ?? row.desc ?? row.name,
          })),
        })),
      );
  }

  getGenderList(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    // the corrected GenderMasterDto carries id + desc — mapped for the uniform
    // IdNameRow shape (the template's desc fallback already rendered it).
    return this.http
      .get<ApiResponse<never, IdNameRow>>(`${this.base}/adt/getActiveGenderList`)
      .pipe(
        map((r) => ({
          ...r,
          listObject: (r.listObject ?? []).map((row) => ({
            id: Number(row.id ?? row['genderId']),
            name: String(row.desc ?? row.name ?? row.code),
          })),
        })),
      );
  }

  getIdentificationList(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    // the corrected IdentificationMasterDto carries identificationId +
    // identificationName — map them into the IdNameRow shape (the same class
    // as the prefix's prefixDesc map).
    return this.http
      .get<ApiResponse<never, IdNameRow>>(`${this.base}/adt/getActiveIdentificationList`)
      .pipe(
        map((r) => ({
          ...r,
          listObject: (r.listObject ?? []).map((row) => ({
            id: Number(row['identificationId'] ?? row.id),
            name: String(row['identificationName'] ?? row.name ?? row.desc ?? row.code),
          })),
        })),
      );
  }

  getRelationList(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    // the corrected RelationMasterDto carries id + desc — the template's
    // name ?? code ?? desc chain already covers desc; mapped for symmetry.
    // NOTE: the corrected relation master is currently EMPTY (200 + empty
    // listObject) — a data gap, not a code issue.
    return this.http
      .get<ApiResponse<never, IdNameRow>>(`${this.base}/adt/getActiveRelationList`)
      .pipe(
        map((r) => ({
          ...r,
          listObject: (r.listObject ?? []).map((row) => ({
            id: Number(row.id ?? row['relationId']),
            name: String(row.desc ?? row['relationName'] ?? row.name ?? row.code),
          })),
        })),
      );
  }

  getPaymentEntitlementList(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    // the legacy constant's typo is the real endpoint — ported faithfully.
    // The corrected PaymentEntitlementMasterDto carries paymentEntitlementId +
    // paymentEntitlementDesc — map into the IdNameRow shape.
    return this.http
      .get<ApiResponse<never, IdNameRow>>(`${this.base}/adt/getAvtivePaymentEntitlementList`)
      .pipe(
        map((r) => ({
          ...r,
          listObject: (r.listObject ?? []).map((row) => ({
            id: Number(row['paymentEntitlementId'] ?? row.id),
            name: String(row['paymentEntitlementDesc'] ?? row['paymentEntitlementName'] ?? row.name ?? row.desc ?? row.code),
          })),
        })),
      );
  }

  getCountry(orgId: number, unitId: number): Observable<ApiResponse<never, IdNameRow>> {
    // the cascade root is the LIST endpoint — GET /adt/getActiveCountryList/{orgId}
    // (the corrected CountryMasterController:92); getCountryById is the by-id
    // detail, not the list. The CountryMasterDto has NO unitId field — the
    // orgId rides the path, unitId is not sent.
    return this.http
      .get<ApiResponse<never, IdNameRow>>(`${this.base}/adt/getActiveCountryList/${orgId}`)
      .pipe(
        map((r) => ({
          ...r,
          listObject: (r.listObject ?? []).map((row) => ({
            id: Number(row['countryId'] ?? row.id),
            name: String(row['countryName'] ?? row.name ?? row.desc),
          })),
        })),
      );
  }

  getStatesByCountry(parentId: number, orgId: number, unitId: number): Observable<ApiResponse<never, GeoRow>> {
    return this.post('/adt/getActiveStateListByCountryId', { countryId: parentId }).pipe(
      map((r) => ({
        ...r,
        listObject: (r.listObject ?? []).map((row) => {
          const rr = row as Record<string, unknown>;
          return {
            id: Number(rr['countryId'] ?? rr['id']),
            name: String(rr['stateName'] ?? rr['name'] ?? rr['desc'] ?? rr['code']),
          };
        }),
      })),
    );
  }
  getDistrictsByState(parentId: number, orgId: number, unitId: number): Observable<ApiResponse<never, GeoRow>> {
    return this.post('/adt/getActiveDistrictListByStateId', { stateId: parentId }).pipe(
      map((r) => ({
        ...r,
        listObject: (r.listObject ?? []).map((row) => {
          const rr = row as Record<string, unknown>;
          return {
            id: Number(rr['stateId'] ?? rr['id']),
            name: String(rr['districtName'] ?? rr['name'] ?? rr['desc'] ?? rr['code']),
          };
        }),
      })),
    );
  }
  getCitiesByDistrict(parentId: number, orgId: number, unitId: number): Observable<ApiResponse<never, GeoRow>> {
    return this.post('/adt/getActiveCityListByDistrictId', { districtId: parentId }).pipe(
      map((r) => ({
        ...r,
        listObject: (r.listObject ?? []).map((row) => {
          const rr = row as Record<string, unknown>;
          return {
            id: Number(rr['districtId'] ?? rr['id']),
            name: String(rr['cityName'] ?? rr['name'] ?? rr['desc'] ?? rr['code']),
          };
        }),
      })),
    );
  }
  getAreasByCity(parentId: number, orgId: number, unitId: number): Observable<ApiResponse<never, GeoRow>> {
    return this.post('/adt/getAreaByCityId', { cityId: parentId }).pipe(
      map((r) => ({
        ...r,
        listObject: (r.listObject ?? []).map((row) => {
          const rr = row as Record<string, unknown>;
          return {
            id: Number(rr['cityId'] ?? rr['id']),
            name: String(rr['areaName'] ?? rr['name'] ?? rr['desc'] ?? rr['code']),
          };
        }),
      })),
    );
  }
  getFieldConfig(orgId: number, unitId: number): Observable<ApiResponse<never, FieldConfig>> {
    // the legacy endpoint is a GET with orgId/unitId in the path
    return this.http.get<ApiResponse<never, FieldConfig>>(
      `${this.base}/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/${orgId}/${unitId}`,
    );
  }

  save(payload: RegistrationPayload): Observable<ApiResponse<never, unknown>> {
    return this.post('/api/global/patientRegistration', payload);
  }
}
