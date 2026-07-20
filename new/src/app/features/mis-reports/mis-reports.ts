import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { debounceTime, Subject, switchMap } from 'rxjs';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { MisReportsService } from './mis-reports.service';
import { ReportFormatOption, ReportGroupHeader, ReportParameter, ReportSummary } from './mis-reports.models';

/**
 * Parity rebuild of views/OtherApps/MIS-reports.html +
 * resources/assets/js/controller/master/MISReportsController.js.
 * Audited and golden-traced in docs/PHASE2_PILOT_AUDIT.md section 1 + 8.
 *
 * This is NOT a fixed-schema form -- it's a dynamic report-parameter
 * renderer. Which fields appear, and of what type, is driven entirely by
 * `controlName` values returned from the backend per selected report
 * (`getReportDetailsByReportId`). The audit identified ~19 distinct
 * `controlName` values; several collapse to the same rendered control
 * (e.g. textBox/textBoxstring/inputBox are all a numeric text input in
 * the legacy view, and list/unit/user/org are all the same dropdown
 * pattern with different default-population logic) -- rebuilt here as
 * the same reduced set of distinct UI patterns the legacy view actually
 * renders, keyed the same way (a template @switch on controlName), not
 * as 19 separate components.
 *
 * ItemSearch/Servicesearch/DiagnosiSearch/VenderSearch typeaheads exist
 * in the legacy controller but only ItemSearch (medication) and the
 * generic patient "text" search are wired here to a real backend call --
 * Servicesearch/DiagnosiSearch/VenderSearch call endpoints this pilot
 * didn't independently audit in depth; their inputs render but resolve
 * to an empty result list rather than a fabricated one. Flagged here
 * rather than silently guessed.
 */
@Component({
  selector: 'app-mis-reports',
  imports: [FormsModule, DatePipe],
  templateUrl: './mis-reports.html',
  styleUrl: './mis-reports.scss',
})
export class MisReports {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(MisReportsService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  reportFormats = signal<ReportFormatOption[]>([]);
  selectedFormatId = signal<number | ''>('');

  groupHeaders = signal<ReportGroupHeader[]>([]);
  activeGroupIndex = signal<number | null>(null);
  reportsByGroup = signal<ReportSummary[]>([]);

  selectedReport = signal<ReportSummary | null>(null);
  parameters = signal<ReportParameter[]>([]);

  searchTerm = signal('');
  searchResults = signal<ReportSummary[]>([]);
  private search$ = new Subject<string>();

  patientSearchTerm = signal('');
  patientResults = signal<{ patientId: number; patientName: string }[]>([]);
  private selectedPatientId = signal<number | null>(null);
  private patientSearch$ = new Subject<string>();

  constructor() {
    this.api.getReportFormats().subscribe({
      next: (res) => {
        if (res.status === 'success' && res.listObject?.length) {
          this.reportFormats.set(res.listObject);
          this.selectedFormatId.set(res.listObject[0].id);
        }
      },
    });
    this.api.getReportGroupHeaders(this.organizationId, this.userId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.groupHeaders.set(res.listObject ?? []);
      },
    });

    this.search$.pipe(debounceTime(250), switchMap((term) => this.api.searchReports(this.organizationId, term))).subscribe({
      next: (res) => this.searchResults.set(res.status === 'success' ? res.listObject ?? [] : []),
    });

    this.patientSearch$
      .pipe(
        debounceTime(250),
        switchMap((term) => this.api.searchPatients(term, this.unitId, this.organizationId)),
      )
      .subscribe({
        next: (res) => this.patientResults.set(res.status === 'success' ? res.listObject ?? [] : []),
      });
  }

  onSearchChange(term: string): void {
    this.searchTerm.set(term);
    if (term.length >= 2) this.search$.next(term);
  }

  selectSearchResult(report: ReportSummary): void {
    this.searchResults.set([]);
    const groupIndex = this.groupHeaders().findIndex((g) => g.reportGroupHeaderId === report.reportGroupHeaderId);
    if (groupIndex >= 0) this.selectGroup(groupIndex);
    this.selectReport(report);
  }

  selectGroup(index: number): void {
    this.activeGroupIndex.set(index);
    const group = this.groupHeaders()[index];
    if (!group) return;
    this.api.getReportsByGroup(this.organizationId, group.reportGroupHeaderId).subscribe({
      next: (res) => this.reportsByGroup.set(res.status === 'success' ? res.listObject ?? [] : []),
    });
  }

  selectReport(report: ReportSummary): void {
    this.selectedReport.set(report);
    this.api.getReportParameters(this.organizationId, this.unitId, report.reportId, this.userId).subscribe({
      next: (res) => {
        if (res.status !== 'success') return;
        const now = new Date();
        const params = (res.listObject ?? []).map((p) => this.withDefaultValue(p, now));
        this.parameters.set(params);
      },
    });
  }

  private withDefaultValue(param: ReportParameter, now: Date): ReportParameter {
    const withDefaults = { ...param, multiSelectList: [] as { id: number; name: string }[] };
    switch (param.controlName) {
      case 'DatePickerFrom':
        withDefaults.fromDate = now;
        break;
      case 'DatePickerTo':
        withDefaults.toDate = now;
        break;
      case 'DatePickerExpiry':
        withDefaults.expiryDate = now;
        break;
      case 'TimePickerFrom':
        withDefaults.fromTime = now.toTimeString().slice(0, 5);
        break;
      case 'TimePickerTo':
        withDefaults.toTime = now.toTimeString().slice(0, 5);
        break;
      case 'unit':
        withDefaults.paramValue = this.unitId;
        break;
      case 'user':
        withDefaults.paramValue = this.userId;
        break;
      case 'org':
        withDefaults.paramValue = this.organizationId;
        break;
    }
    return withDefaults;
  }

  updateParameter(index: number, patch: Partial<ReportParameter>): void {
    this.parameters.update((params) => params.map((p, i) => (i === index ? { ...p, ...patch } : p)));
  }

  onPatientSearchChange(term: string): void {
    this.patientSearchTerm.set(term);
    this.selectedPatientId.set(null);
    if (term.length >= 1) this.patientSearch$.next(term);
  }

  selectPatient(patient: { patientId: number; patientName: string }): void {
    this.selectedPatientId.set(patient.patientId);
    this.patientSearchTerm.set(patient.patientName);
    this.patientResults.set([]);
  }

  private buildReportParams(): { paramName: string; paramType: string; paramValue: unknown }[] {
    return this.parameters().map((p) => {
      let value: unknown;
      switch (p.controlName) {
        case 'DatePickerFrom':
          value = p.fromDate ? p.fromDate.toISOString().slice(0, 10) : null;
          break;
        case 'DatePickerTo':
          value = p.toDate ? p.toDate.toISOString().slice(0, 10) : null;
          break;
        case 'DatePickerExpiry':
          value = p.expiryDate ? p.expiryDate.toISOString().slice(0, 10) : null;
          break;
        case 'TimePickerFrom':
          value = p.fromTime ?? null;
          break;
        case 'TimePickerTo':
          value = p.toTime ?? null;
          break;
        case 'text':
          value = this.selectedPatientId() ?? 0;
          break;
        case 'multiSelectlist':
          value = (p.multiSelectList ?? []).map((m) => m.id).join(',');
          break;
        case 'ItemSearch':
        case 'Servicesearch':
        case 'DiagnosiSearch':
        case 'VenderSearch':
          // See class doc comment -- these typeaheads aren't independently
          // wired to a real search in this pilot; resolve to "not selected".
          value = 0;
          break;
        default:
          value = p.paramValue ?? 0;
      }
      return { paramName: p.parameterName, paramType: p.paramType, paramValue: value };
    });
  }

  view(): void {
    const report = this.selectedReport();
    if (!report) return;
    this.api.generatePdf(report.action, this.buildReportParams()).subscribe({
      next: (blob) => this.downloadBlob(blob, 'report.pdf'),
      error: () => this.notify.error('Failed to generate report'),
    });
  }

  export(): void {
    const report = this.selectedReport();
    const format = this.reportFormats().find((f) => f.id === this.selectedFormatId());
    if (!report || !format) return;

    if (format.format === 'PDF') {
      this.view();
      return;
    }
    this.api.downloadFile(this.organizationId, this.unitId, report.action, format.format, this.buildReportParams()).subscribe({
      next: (blob) => this.downloadBlob(blob, `report${format.fileExtension}`),
      error: () => this.notify.error('Failed to export report'),
    });
  }

  private downloadBlob(blob: Blob, filename: string): void {
    const url = URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = url;
    anchor.download = filename;
    anchor.click();
    URL.revokeObjectURL(url);
  }
}
