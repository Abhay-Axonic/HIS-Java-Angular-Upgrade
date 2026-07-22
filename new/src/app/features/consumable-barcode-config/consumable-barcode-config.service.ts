import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ConsumableBarcodeConfigPayload, ConsumableBarcodeConfigRow } from './consumable-barcode-config.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/unit/consumableBarcodeConfigController.js
 * and CONSTANTS.MASTER.UNIT.CONSUMABLE_BARCODE_CONFIG.* in
 * resources/assets/js/constants/constants.js:269-271 (globalsApi =
 * '/api/globals'). Confirmed real via
 * com.param.pharmacy.master.global.controller.ConsumableBarcodeConfigController
 * (`@RequestMapping("/api/globals/consumableBarcodeConfig/")`) ->
 * ConsumableBarcodeConfigService -> ConsumableBarcodeConfigDao.
 */
@Injectable({ providedIn: 'root' })
export class ConsumableBarcodeConfigService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getByUnit(unitId: number): Observable<ConsumableBarcodeConfigRow[]> {
    return this.http.get<ConsumableBarcodeConfigRow[]>(`${this.base}/api/globals/consumableBarcodeConfig/${unitId}`);
  }

  create(payload: ConsumableBarcodeConfigPayload): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/globals/consumableBarcodeConfig/save`, payload);
  }

  update(id: number, payload: ConsumableBarcodeConfigPayload): Observable<ApiResponse> {
    return this.http.patch<ApiResponse>(`${this.base}/api/globals/consumableBarcodeConfig/${id}`, payload);
  }
}
