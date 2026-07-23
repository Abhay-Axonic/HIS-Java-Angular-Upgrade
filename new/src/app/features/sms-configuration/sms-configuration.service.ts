import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  CategoryOption,
  MobileListEntry,
  RegistrationFieldConfig,
  RequiredDataOption,
  SmsUtilityConfiguration,
  TriggerOption,
  WhatsAppTemplateOption,
} from './sms-configuration.models';

/**
 * Endpoint paths verified directly against `SmsUtilityConfigController.java`
 * (found live, Step 5): the controller has NO class-level `@RequestMapping`
 * at all (confirmed by reading the file top-to-bottom), so every method's
 * path is bare -- no `/api/globals` prefix, matching the legacy frontend's
 * own `BASE_URL + ROOT_URL + "/sms-utility-config/..."` construction where
 * `ROOT_URL` is the empty string (`url.config.js:19`). The original spec for
 * this screen incorrectly assumed a shared `/api/globals` base (true for
 * sibling controllers like `DepartmentStoreMapperController`, not this one)
 * -- fixed directly (rebuild bug, not a parity question), causing a live
 * 404 on initial load before this fix. Only the registration-field-config
 * lookup lives under a real prefixed path, `/api/unit/...`.
 */
@Injectable({ providedIn: 'root' })
export class SmsConfigurationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:153-176
  getTriggerList(): Observable<ApiResponse<never, TriggerOption>> {
    return this.http.get<ApiResponse<never, TriggerOption>>(`${this.base}/trigger-master`);
  }

  // ctrl:181-201
  getCategoryList(): Observable<ApiResponse<never, CategoryOption>> {
    return this.http.get<ApiResponse<never, CategoryOption>>(`${this.base}/category-master`);
  }

  // ctrl:305-371 (save())
  save(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/sms-utility-config`, payload);
  }

  // ctrl:384-411 (getSmsConfigurationList())
  getList(orgId: number, unitId: number): Observable<ApiResponse<never, SmsUtilityConfiguration>> {
    return this.http.get<ApiResponse<never, SmsUtilityConfiguration>>(`${this.base}/sms-utility-config/${orgId}/${unitId}`);
  }

  // ctrl:473-484 (getReqiredData())
  getRequiredData(triggerId: number | string): Observable<ApiResponse<never, RequiredDataOption>> {
    return this.http.get<ApiResponse<never, RequiredDataOption>>(`${this.base}/requiredData/trigger/${triggerId}`);
  }

  // ctrl:591-611 -- returns a BARE array, not the ApiResponse envelope. See
  // MobileListEntry doc comment in sms-configuration.models.ts.
  getMobileList(triggerId: number | string, orgId: number, unitId: number): Observable<MobileListEntry[]> {
    return this.http.get<MobileListEntry[]>(`${this.base}/getMobileList/trigger/${triggerId}/${orgId}/${unitId}`);
  }

  // ctrl:554-589 (updateStatus())
  updateStatus(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/update/status`, payload);
  }

  // ctrl:686-703 (getWhatsAppTemplateList())
  getWhatsAppTemplateList(orgId: number, unitId: number): Observable<ApiResponse<never, WhatsAppTemplateOption>> {
    return this.http.get<ApiResponse<never, WhatsAppTemplateOption>>(`${this.base}/getWhatsAppTemplateList/${orgId}/${unitId}`);
  }

  // ctrl:96-140 (getMobileNoConfigurationList()) -- note different base path.
  getMobileDigitConfig(orgId: number, unitId: number): Observable<ApiResponse<never, RegistrationFieldConfig>> {
    return this.http.get<ApiResponse<never, RegistrationFieldConfig>>(
      `${this.base}/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/${orgId}/${unitId}`,
    );
  }
}
