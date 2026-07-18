import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { InputOutputChartsService } from './input-output-charts.service';
import {
  ChartHistoryRow,
  FluidOption,
  IntakeRow,
  OutputRow,
  PatientSearchResult,
  emptyIntakeRow,
  emptyOutputRow,
} from './input-output-charts.models';

/**
 * Parity rebuild of views/Nursing/input-output-charts.html +
 * resources/assets/js/controller/master/unit/inputOutputChartsController.js.
 * Audited and golden-traced in docs/PHASE2_PILOT_AUDIT.md section 3 + 8.
 *
 * Two deliberate scope decisions, not silent gaps:
 * 1. The legacy view includes two shared partials for patient search/
 *    demography (`common-patient-search-opd-without-counter.html`,
 *    `patient-demography-opd.html`) that the audit explicitly scoped OUT
 *    of per-screen characterization -- they're reused across many
 *    clinical screens and should be characterized once, separately. This
 *    rebuild uses a minimal inline patient search instead of a full
 *    rebuild of those shared partials, so the screen is usable standalone;
 *    a real shared "patient search" Angular component should replace this
 *    once those partials get their own characterization pass.
 * 2. `reportService.generate(...)` (the Print button's PDF generation) is
 *    an external report-rendering integration, not rebuilt here -- the
 *    button is wired but calls a stub that logs rather than a real
 *    report call, since there's no PDF-generation backend to integrate
 *    against in this pilot.
 *
 * The `sessionStorage.getItem('nursingStationsWardsList')` cross-screen
 * coupling flagged in the audit (written by the legacy sidebar, read by
 * 30+ nursing/pharmacy controllers) IS preserved here, read the same way,
 * since ngUpgrade hybrid mode shares the same browser session/storage.
 */
@Component({
  selector: 'app-input-output-charts',
  imports: [FormsModule],
  templateUrl: './input-output-charts.html',
  styleUrl: './input-output-charts.scss',
})
export class InputOutputCharts {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(InputOutputChartsService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- patient search (simplified, see class doc comment) ----
  private readonly nursingWardList: unknown[] = this.readNursingWardList();
  uhidSearchTerm = signal('');
  patientNameSearchTerm = signal('');
  patientResults = signal<PatientSearchResult[]>([]);
  selectedPatient = signal<PatientSearchResult | null>(null);

  private readNursingWardList(): unknown[] {
    try {
      const raw = sessionStorage.getItem('nursingStationsWardsList');
      return raw ? JSON.parse(raw) : [];
    } catch {
      return [];
    }
  }

  /**
   * Two separate fields, matching legacy's real UI (`patSearchObj.uhIdNumber`
   * and `patSearchObj.patientName` are always distinct inputs there -- see
   * views/Nursing/input-output-charts.html). Legacy's backend AND-combines
   * whichever fields are non-empty (UnknownPatientRegistrationDaoImpl.
   * searchPatientsByWardId), so sending both with the same value (this
   * screen's original single-search-box shortcut) made a combined search
   * return zero results for virtually any real patient -- confirmed and
   * logged as docs/ANGULAR_PENDING_FINDINGS.md entry 15. Only send the
   * field(s) the user actually filled in, same as legacy's own conditional
   * request-building (`if (patSearchObj.uhIdNumber) ...`).
   */
  searchPatients(): void {
    const uhIdNumber = this.uhidSearchTerm().trim();
    const patientName = this.patientNameSearchTerm().trim();
    if (!uhIdNumber && !patientName) return;
    this.api
      .searchPatients({
        ...(uhIdNumber ? { uhIdNumber } : {}),
        ...(patientName ? { patientName } : {}),
        unitId: this.unitId,
        organizationId: this.organizationId,
        wardList: this.nursingWardList,
      })
      .subscribe({
        next: (res) => this.patientResults.set(res.status === 'success' ? res.listObject ?? [] : []),
      });
  }

  selectPatient(patient: PatientSearchResult): void {
    this.selectedPatient.set(patient);
    this.patientResults.set([]);
    this.startFlag.set(true);
    this.loadChartHistory();
  }

  // ---- intake/output fluid + shift options ----
  intakeFluidOptions = signal<FluidOption[]>([]);
  outputFluidOptions = signal<FluidOption[]>([]);

  constructor() {
    this.api.getIntakeFluidList(this.organizationId, this.unitId).subscribe({
      next: (res) => this.intakeFluidOptions.set(res.status === 'success' ? res.listObject ?? [] : []),
    });
    this.api.getOutputFluidList(this.organizationId, this.unitId).subscribe({
      next: (res) => this.outputFluidOptions.set(res.status === 'success' ? res.listObject ?? [] : []),
    });
  }

  // ---- intake/output header date-time ----
  intakeDate = signal('');
  intakeTime = signal('');
  outputDate = signal('');
  outputTime = signal('');

  startIOFlag = signal(true);
  stopIOFlag = signal(true);
  startFlag = signal(true);
  stopAndSaveDisabled = signal(true);
  showStartConfirm = signal(false);
  showStopConfirm = signal(false);

  // ---- repeatable rows ----
  intakeRows = signal<IntakeRow[]>([emptyIntakeRow()]);
  outputRows = signal<OutputRow[]>([emptyOutputRow()]);
  totalIntakeQuantity = signal(0);
  totalOutputQuantity = signal(0);

  chartHistory = signal<ChartHistoryRow[]>([]);

  addIntakeRow(): void {
    this.intakeRows.update((rows) => [...rows, emptyIntakeRow()]);
  }

  removeIntakeRow(index: number): void {
    const removed = this.intakeRows()[index];
    this.intakeRows.update((rows) => rows.filter((_, i) => i !== index));
    if (typeof removed.intakeQuantity === 'number') {
      this.totalIntakeQuantity.update((t) => t - (removed.intakeQuantity as number));
    }
  }

  addOutputRow(): void {
    this.outputRows.update((rows) => [...rows, emptyOutputRow()]);
  }

  removeOutputRow(index: number): void {
    const removed = this.outputRows()[index];
    this.outputRows.update((rows) => rows.filter((_, i) => i !== index));
    if (typeof removed.outputQuantity === 'number') {
      this.totalOutputQuantity.update((t) => t - (removed.outputQuantity as number));
    }
  }

  onIntakeQuantityChange(index: number, oldValue: number | '', newValue: string): void {
    const parsed = Number(newValue) || 0;
    if (typeof oldValue === 'number') this.totalIntakeQuantity.update((t) => t - oldValue);
    this.totalIntakeQuantity.update((t) => t + parsed);
    this.updateIntakeRow(index, 'intakeQuantity', parsed);
  }

  onOutputQuantityChange(index: number, oldValue: number | '', newValue: string): void {
    const parsed = Number(newValue) || 0;
    if (typeof oldValue === 'number') this.totalOutputQuantity.update((t) => t - oldValue);
    this.totalOutputQuantity.update((t) => t + parsed);
    this.updateOutputRow(index, 'outputQuantity', parsed);
  }

  updateIntakeRow<K extends keyof IntakeRow>(index: number, field: K, value: IntakeRow[K]): void {
    this.intakeRows.update((rows) => rows.map((r, i) => (i === index ? { ...r, [field]: value } : r)));
  }

  updateOutputRow<K extends keyof OutputRow>(index: number, field: K, value: OutputRow[K]): void {
    this.outputRows.update((rows) => rows.map((r, i) => (i === index ? { ...r, [field]: value } : r)));
  }

  onIntakeFluidChange(index: number, intakeFluidId: number): void {
    this.updateIntakeRow(index, 'intakeFluidId', intakeFluidId);
    this.api.getIntakeFluidTypeList(this.organizationId, this.unitId, intakeFluidId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.updateIntakeRow(index, 'fluidTypeOptions', res.listObject ?? []);
      },
    });
  }

  onIntakeTimeChange(index: number): void {
    const row = this.intakeRows()[index];
    const fromTime = row.intakeStartTime ? `${row.intakeStartTime}:00` : '';
    if (!fromTime) return;
    this.api.getShiftsByTime(this.organizationId, this.unitId, fromTime).subscribe({
      next: (res) => {
        if (res.status === 'success') this.updateIntakeRow(index, 'shiftOptions', res.listObject ?? []);
      },
    });
  }

  onOutputTimeChange(index: number): void {
    const row = this.outputRows()[index];
    const fromTime = row.outputStartTime ? `${row.outputStartTime}:00` : '';
    if (!fromTime) return;
    this.api.getShiftsByTime(this.organizationId, this.unitId, fromTime).subscribe({
      next: (res) => {
        if (res.status === 'success') this.updateOutputRow(index, 'shiftOptions', res.listObject ?? []);
      },
    });
  }

  // ---- time validation (warn-and-auto-correct, matching legacy) ----
  validateIntakeTime(): void {
    const now = new Date();
    const currentTime = now.toTimeString().slice(0, 5);
    const currentDate = now.toISOString().slice(0, 10);
    if (this.intakeDate() === currentDate && this.intakeTime() > currentTime) {
      this.notify.warning('Time Should be equal to or less than Current time.');
      this.intakeTime.set(currentTime);
    }
  }

  validateOutputTime(): void {
    if (this.outputDate() === this.intakeDate() && this.outputTime() < this.intakeTime()) {
      this.notify.warning('Time Should be equal to or greater than intake time.');
      this.outputTime.set(this.intakeTime());
    }
  }

  // ---- chart history ----
  private loadChartHistory(): void {
    const patient = this.selectedPatient();
    if (!patient) return;
    const today = new Date().toISOString().slice(0, 10);
    this.api.getChartHistory(patient.patientId, this.organizationId, this.unitId, today).subscribe({
      next: (res) => {
        const rows = res.status === 'success' ? res.listObject ?? [] : [];
        this.chartHistory.set(rows);
        const hasRows = rows.length > 0;
        this.startFlag.set(!hasRows);
        this.stopAndSaveDisabled.set(!hasRows);
      },
    });
  }

  // ---- start / stop I/O ----
  confirmStart(): void {
    this.showStartConfirm.set(true);
  }

  confirmStop(): void {
    if (!this.outputDate() || !this.outputTime()) {
      this.notify.warning('Please enter date and time to stop I/O.');
      return;
    }
    this.showStopConfirm.set(true);
  }

  startChart(): void {
    this.showStartConfirm.set(false);
    const patient = this.selectedPatient();
    if (!patient) return;
    this.api
      .startChart({
        updatedBy: this.userId,
        updatedDate: new Date().toISOString(),
        isIoStart: false,
        patientId: patient.patientId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Started');
            this.startIOFlag.set(false);
            this.stopIOFlag.set(true);
          }
        },
      });
  }

  stopChart(): void {
    this.showStopConfirm.set(false);
    const patient = this.selectedPatient();
    if (!patient) return;
    this.api
      .stopChart({
        updatedBy: this.userId,
        updatedDate: new Date().toISOString(),
        isIoStart: false,
        outputDate: this.outputDate(),
        outputTime: this.outputTime() ? `${this.outputTime()}:00` : null,
        patientId: patient.patientId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Stopped');
            this.startIOFlag.set(false);
            this.stopIOFlag.set(true);
          }
        },
      });
  }

  // ---- save ----
  save(): void {
    const patient = this.selectedPatient();
    if (!patient) return;
    if (!this.intakeDate() || !this.intakeTime()) {
      this.notify.warning('Please enter Intake date and time');
      return;
    }

    const intakeList = this.intakeRows()
      .filter((r) => r.intakeFluidId !== '')
      .map((r) => ({
        startIntakeDate: r.intakeStartDate,
        startIntakeTime: r.intakeStartTime ? `${r.intakeStartTime}:00` : '',
        intakeFluidId: r.intakeFluidId,
        intakeTypeFluidId: r.intakeTypeFluidId,
        startIntakeQuantity: r.intakeQuantity,
        totalIntake: this.totalIntakeQuantity(),
        shiftId: r.intakeShiftId,
        status: 'A',
        unitId: this.unitId,
        organizationId: this.organizationId,
        createdBy: this.userId,
      }));

    const outputList = this.outputRows()
      .filter((r) => r.outputTypeId !== '')
      .map((r) => ({
        startOutputDate: r.outputStartDate,
        startOutputTime: r.outputStartTime ? `${r.outputStartTime}:00` : '',
        outputTypeId: r.outputTypeId,
        startOutputQuantity: r.outputQuantity,
        totalOutput: this.totalOutputQuantity(),
        shiftId: r.outputShiftId,
        status: 'A',
        unitId: this.unitId,
        organizationId: this.organizationId,
        createdBy: this.userId,
      }));

    if (intakeList.length === 0 && outputList.length === 0) {
      this.notify.error('Please enter Intake or Output');
      return;
    }

    this.api
      .saveChart({
        intakeDate: this.intakeDate(),
        intakeTime: `${this.intakeTime()}:00`,
        outputDate: this.outputDate() || null,
        outputTime: this.outputTime() ? `${this.outputTime()}:00` : null,
        admissionId: patient.admissionId,
        patientId: patient.patientId,
        unitId: this.unitId,
        organizationId: this.organizationId,
        userId: this.userId,
        totalIntake: this.totalIntakeQuantity(),
        totalOutput: this.totalOutputQuantity(),
        status: 'A',
        createdBy: this.userId,
        intakeOutputChartMasterDtoList: intakeList,
        outputChartMasterDtoList: outputList,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Saved successfully');
            this.intakeRows.set([emptyIntakeRow()]);
            this.outputRows.set([emptyOutputRow()]);
            this.totalIntakeQuantity.set(0);
            this.totalOutputQuantity.set(0);
            this.loadChartHistory();
          } else {
            this.notify.error(res.message ?? 'Save failed');
          }
        },
      });
  }

  print(): void {
    // reportService.generate(...) is an external PDF-report integration,
    // not rebuilt in this pilot -- see class doc comment.
    this.notify.warning('Print/report generation is not part of this pilot rebuild.');
  }
}
