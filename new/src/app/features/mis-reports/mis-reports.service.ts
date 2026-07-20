import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { ReportFormatOption, ReportGroupHeader, ReportParameter, ReportSummary } from './mis-reports.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/controller/master/MISReportsController.js.
 * Golden-trace-confirmed in docs/PHASE2_PILOT_AUDIT.md section 1 + 8.
 */
@Injectable({ providedIn: 'root' })
export class MisReportsService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getReportFormats(): Observable<ApiResponse<never, ReportFormatOption>> {
    return this.http.post<ApiResponse<never, ReportFormatOption>>(
      `${this.base}/api/global/unit/getMISReportFormattesMasterforShowReports`,
      {},
    );
  }

  getReportGroupHeaders(organizationId: number, userId: number, unitId: number): Observable<ApiResponse<never, ReportGroupHeader>> {
    return this.http.post<ApiResponse<never, ReportGroupHeader>>(`${this.base}/api/global/unit/getReportGroupHeaderMaster`, {
      organizationId,
      userId,
      unitId,
    });
  }

  getReportsByGroup(organizationId: number, reportGroupHeaderId: number): Observable<ApiResponse<never, ReportSummary>> {
    return this.http.post<ApiResponse<never, ReportSummary>>(`${this.base}/api/global/unit/getReportDetailsList`, {
      organizationId,
      reportGroupHeaderId,
    });
  }

  getReportParameters(
    organizationId: number,
    unitId: number,
    reportId: number,
    userId: number,
  ): Observable<ApiResponse<never, ReportParameter>> {
    return this.http.post<ApiResponse<never, ReportParameter>>(`${this.base}/api/global/unit/getReportDetailsByReportId`, {
      organizationId,
      unitId,
      reportId,
      userId,
    });
  }

  searchReports(organizationId: number, searchString: string): Observable<ApiResponse<never, ReportSummary>> {
    return this.http.post<ApiResponse<never, ReportSummary>>(`${this.base}/api/global/unit/searchMISReport`, {
      organizationId,
      searchString,
    });
  }

  searchPatients(firstName: string, unitId: number, organizationId: number): Observable<ApiResponse<never, { patientId: number; patientName: string }>> {
    return this.http.post<ApiResponse<never, { patientId: number; patientName: string }>>(`${this.base}/adt/getPatientListLike`, {
      firstName,
      unitId,
      organizationId,
    });
  }

  generatePdf(reportDesignFilePath: string, reportParams: unknown[]): Observable<Blob> {
    return this.http.post(
      `${this.base}/api/billing/unit/bill/Report/MIS`,
      {
        appName: 'HIS',
        appSecretKey: 'f306b6eccd9d5069d2dc71afe139271a',
        reportDesignFilePath,
        reportFormat: 'PDF',
        exportFileName: 'admission',
        reportParams,
      },
      { responseType: 'blob' },
    );
  }

  downloadFile(organizationId: number, unitId: number, reportDesignFilePath: string, format: string, reportParams: unknown[]): Observable<Blob> {
    return this.http.post(
      `${this.base}/api/billing/unit/bill/Report/file/${organizationId}/${unitId}`,
      {
        appName: 'HIS',
        appSecretKey: 'f306b6eccd9d5069d2dc71afe139271a',
        reportDesignFilePath,
        reportFormat: format,
        exportFileName: 'admission',
        reportParams,
      },
      { responseType: 'blob' },
    );
  }
}
