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
 * All endpoints confirmed real end-to-end against
 * smsUtilityConfigurationController.js -- base `/api/globals` except the
 * registration-field-config lookup, which lives under `/api/unit`.
 */
@Injectable({ providedIn: 'root' })
export class SmsConfigurationService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  // ctrl:153-176
  getTriggerList(): Observable<ApiResponse<never, TriggerOption>> {
    return this.http.get<ApiResponse<never, TriggerOption>>(`${this.base}/api/globals/trigger-master`);
  }

  // ctrl:181-201
  getCategoryList(): Observable<ApiResponse<never, CategoryOption>> {
    return this.http.get<ApiResponse<never, CategoryOption>>(`${this.base}/api/globals/category-master`);
  }

  // ctrl:305-371 (save())
  save(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/globals/sms-utility-config`, payload);
  }

  // ctrl:384-411 (getSmsConfigurationList())
  getList(orgId: number, unitId: number): Observable<ApiResponse<never, SmsUtilityConfiguration>> {
    return this.http.get<ApiResponse<never, SmsUtilityConfiguration>>(`${this.base}/api/globals/sms-utility-config/${orgId}/${unitId}`);
  }

  // ctrl:473-484 (getReqiredData())
  getRequiredData(triggerId: number | string): Observable<ApiResponse<never, RequiredDataOption>> {
    return this.http.get<ApiResponse<never, RequiredDataOption>>(`${this.base}/api/globals/requiredData/trigger/${triggerId}`);
  }

  // ctrl:591-611 -- returns a BARE array, not the ApiResponse envelope. See
  // MobileListEntry doc comment in sms-configuration.models.ts.
  getMobileList(triggerId: number | string, orgId: number, unitId: number): Observable<MobileListEntry[]> {
    return this.http.get<MobileListEntry[]>(`${this.base}/api/globals/getMobileList/trigger/${triggerId}/${orgId}/${unitId}`);
  }

  // ctrl:554-589 (updateStatus())
  updateStatus(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/globals/update/status`, payload);
  }

  // ctrl:686-703 (getWhatsAppTemplateList())
  getWhatsAppTemplateList(orgId: number, unitId: number): Observable<ApiResponse<never, WhatsAppTemplateOption>> {
    return this.http.get<ApiResponse<never, WhatsAppTemplateOption>>(`${this.base}/api/globals/getWhatsAppTemplateList/${orgId}/${unitId}`);
  }

  // ctrl:96-140 (getMobileNoConfigurationList()) -- note different base path.
  getMobileDigitConfig(orgId: number, unitId: number): Observable<ApiResponse<never, RegistrationFieldConfig>> {
    return this.http.get<ApiResponse<never, RegistrationFieldConfig>>(
      `${this.base}/api/unit/applicationConfiguration/getUnitApplicationRegistrationFieldConfig/${orgId}/${unitId}`,
    );
  }
}
