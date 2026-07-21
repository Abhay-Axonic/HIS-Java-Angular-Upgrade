import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import {
  ChartHistoryRow,
  FluidOption,
  FluidTypeOption,
  PatientSearchResult,
  ShiftOption,
} from './input-output-charts.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/unit/inputOutputChartsController.js.
 * Golden-trace-confirmed in docs/PHASE2_PILOT_AUDIT.md section 3 + 8.
 */
@Injectable({ providedIn: 'root' })
export class InputOutputChartsService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  searchPatients(payload: {
    uhIdNumber?: string;
    patientName?: string;
    unitId: number;
    organizationId: number;
    wardList: unknown[];
  }): Observable<ApiResponse<never, PatientSearchResult>> {
    return this.http.post<ApiResponse<never, PatientSearchResult>>(
      `${this.base}/adt/searchListOfPatientsByWardId`,
      payload,
    );
  }

  getIntakeFluidList(organizationId: number, unitId: number): Observable<ApiResponse<never, FluidOption>> {
    return this.http.post<ApiResponse<never, FluidOption>>(`${this.base}/api/global/getIntakeFluidMasterList`, {
      organizationId,
      unitId,
    });
  }

  getIntakeFluidTypeList(organizationId: number, unitId: number, intakeFluidId: number): Observable<ApiResponse<never, FluidTypeOption>> {
    return this.http.post<ApiResponse<never, FluidTypeOption>>(
      `${this.base}/api/global/getIntakeFluidTypeMasterListById`,
      { organizationId, unitId, intakeFluidId },
    );
  }

  getOutputFluidList(organizationId: number, unitId: number): Observable<ApiResponse<never, FluidOption>> {
    return this.http.post<ApiResponse<never, FluidOption>>(`${this.base}/api/global/getOutputFluidMasterList`, {
      organizationId,
      unitId,
    });
  }

  getChartHistory(patientId: number, organizationId: number, unitId: number, createdDate: string): Observable<ApiResponse<never, ChartHistoryRow>> {
    return this.http.post<ApiResponse<never, ChartHistoryRow>>(`${this.base}/api/opd/getIntakeOutputChartListByPatientId`, {
      patientId,
      createdDate,
      organizationId,
      unitId,
    });
  }

  getShiftsByTime(organizationId: number, unitId: number, fromTime: string): Observable<ApiResponse<never, ShiftOption>> {
    return this.http.post<ApiResponse<never, ShiftOption>>(`${this.base}/api/common/getShiftlistByTime`, {
      organizationId,
      unitId,
      fromTime,
    });
  }

  saveChart(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/opd/saveIntakeOutputChart`, payload);
  }

  startChart(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/opd/updateStatusForStartIOChart`, payload);
  }

  stopChart(payload: Record<string, unknown>): Observable<ApiResponse> {
    return this.http.post<ApiResponse>(`${this.base}/api/opd/updateInputOutputStartStatus`, payload);
  }
}
