import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { finalize } from 'rxjs';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { QualityParameterMasterService } from './quality-parameter-master.service';
import {
  ModuleOption,
  QualityParameterMasterRecord,
  UomOption,
  emptyQualityParameterMaster,
} from './quality-parameter-master.models';

/**
 * Parity rebuild of
 * legacy/HIS-WAR/.../views/master/UnitMaster/organizationMaster/quality-parameter-master.html
 * + resources/assets/js/controller/master/unit/qualityParameterMasterController.js
 * (self-registered state `qualityParameterMaster`, no `controllerAs` usage,
 * no resolve block -- no `data.moduleId`/`requireSuperUser` gate in legacy,
 * same class as billingGroupMaster/periodicityService).
 *
 * No pagination in legacy: `getQualityParamList()` (ctrl:283-300) takes no
 * offset/recordPerPage and always returns the full list for the unit,
 * rendered in a fixed-height scrolling table (`fix-height-scroll430`) --
 * this rebuild matches that rather than inventing a paging UI the real
 * backend doesn't support.
 *
 * Defensive fix (not a literal port): legacy's `editQualityParam()`
 * (ctrl:255-274) can fire before `getUOMList()`/`getModules()` resolve,
 * silently leaving `selectedUOMId`/`selectModuleId` undefined with no error
 * shown. The edit action here is disabled until both lookups have resolved
 * (`dropdownsReady`), per the playbook's Step 3 guidance -- not a
 * business-logic deviation, just avoiding porting a race condition literally.
 */
@Component({
  selector: 'app-quality-parameter-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './quality-parameter-master.html',
  styleUrl: './quality-parameter-master.scss',
})
export class QualityParameterMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(QualityParameterMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? '';

  // ---- list state (no pagination -- see class doc comment) ----
  list = signal<QualityParameterMasterRecord[]>([]);

  // ---- dropdown state ----
  uomList = signal<UomOption[]>([]);
  moduleList = signal<ModuleOption[]>([]);
  private uomLoaded = signal(false);
  private moduleLoaded = signal(false);
  /** Gate for the edit action -- see class doc comment re: ctrl:255-274's race. */
  dropdownsReady = computed(() => this.uomLoaded() && this.moduleLoaded());

  // ---- form state (mirrors the $scope.* fields ctrl:164-226 reads) ----
  record = signal<QualityParameterMasterRecord>(emptyQualityParameterMaster(this.unitId, this.organizationId));
  saveMode = signal(true); // true = create, false = update (mirrors $scope.update === 'N' / 'Y')

  constructor() {
    this.loadUomList();
    this.loadModuleList();
    this.loadList();
  }

  private loadUomList(): void {
    this.api
      .getUomList(this.organizationId)
      .pipe(finalize(() => this.uomLoaded.set(true)))
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.uomList.set(res.listObject ?? []);
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  private loadModuleList(): void {
    this.api
      .getModuleList()
      .pipe(finalize(() => this.moduleLoaded.set(true)))
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.moduleList.set(res.listObject ?? []);
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  private loadList(): void {
    this.loader.start();
    this.api
      .getList(this.unitId, this.organizationId)
      .pipe(finalize(() => this.loader.stop()))
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.list.set(res.listObject ?? []);
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  updateField<K extends keyof QualityParameterMasterRecord>(field: K, value: QualityParameterMasterRecord[K]): void {
    this.record.update((r) => ({ ...r, [field]: value }));
  }

  // ---- live field checks, ctrl:129-157 `checkTargetPercentage`/`checkThresholdPercentage` ----
  onTargetPercentageChange(value: number | ''): void {
    this.updateField('targetPercentage', value);
    if (value === '' || value == null) return;
    if (value > 100) {
      this.notify.warning('Target Percentage should be less than 100');
      this.updateField('targetPercentage', '');
    } else if (value < 0) {
      this.notify.warning('Target Percentage should be greater than 0');
      this.updateField('targetPercentage', '');
    }
  }

  onThresholdPercentageChange(value: number | ''): void {
    this.updateField('thresholdPercentage', value);
    if (value === '' || value == null) return;
    const target = this.record().targetPercentage;
    if (target === '' || target == null) {
      this.notify.warning('Please fill-up Target Percentage first...');
      this.updateField('thresholdPercentage', '');
      return;
    }
    if (value > target) {
      this.notify.warning('Threshold Percentage should be less than Target Percentage');
      this.updateField('thresholdPercentage', '');
    } else if (value < 0) {
      this.notify.warning('Threshold Percentage should be greater than 0');
      this.updateField('thresholdPercentage', '');
    }
  }

  // ---- create / update, ctrl:164-226 `saveQualityParam()`. Validation order
  // and messages ported faithfully, sequentially, exactly as legacy checks
  // them (UOM, Module, Activity Id, Activity, Parameter Id, Standard,
  // Threshold %, Target %). ----
  save(): void {
    const r = this.record();

    if (r.uomId === '' || r.uomId == null) {
      this.loader.stop(); // ctrl:167-172 -- stopLoader() called even though start() hasn't fired yet for this click
      this.notify.error('Please select UOM');
      return;
    }
    if (r.moduleId === '' || r.moduleId == null) {
      this.loader.stop();
      this.notify.error('Please select Module');
      return;
    }
    if (r.activityId === '' || r.activityId == null) {
      this.loader.stop();
      this.notify.error('Please fill up Activity Id');
      return;
    }
    if (!r.activityName) {
      this.loader.stop();
      this.notify.error('Please fill up Activity');
      return;
    }
    if (r.parameterId === '' || r.parameterId == null) {
      this.loader.stop();
      this.notify.error('Please fill up Parameter Id');
      return;
    }
    if (!r.standard) {
      this.loader.stop();
      this.notify.error('Please fill up Standard');
      return;
    }
    if (r.thresholdPercentage === '' || r.thresholdPercentage == null) {
      this.loader.stop();
      this.notify.error('Please fill up Threshold Percentage');
      return;
    }
    if (r.targetPercentage === '' || r.targetPercentage == null) {
      this.loader.stop();
      this.notify.error('Please fill up Target Percentage');
      return;
    }

    const selectedUom = this.uomList().find((u) => u.id === r.uomId);
    const selectedModule = this.moduleList().find((m) => m.moduleId === r.moduleId);
    const now = formatDateTime(new Date());

    const payload: Partial<QualityParameterMasterRecord> = {
      unitId: this.unitId,
      organizationId: this.organizationId,
      moduleId: r.moduleId,
      moduleName: selectedModule?.moduleName ?? '',
      activityId: r.activityId,
      activityName: r.activityName,
      parameterId: r.parameterId,
      standard: r.standard,
      uomId: r.uomId,
      uomDesc: selectedUom?.desc ?? '',
      thresholdPercentage: r.thresholdPercentage,
      targetPercentage: r.targetPercentage,
      update: this.saveMode() ? 'N' : 'Y',
      createdBy: this.userId,
      updatedBy: this.userId,
      createdDate: now,
      updatedDate: now,
    };

    if (!this.saveMode()) {
      payload.qualityParameterMasterId = r.qualityParameterMasterId;
      payload.status = r.status;
    }

    this.loader.start();
    this.api
      .save(payload)
      .pipe(finalize(() => this.loader.stop()))
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
            this.resetForm();
            this.loadList();
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }

  private resetForm(): void {
    this.record.set(emptyQualityParameterMaster(this.unitId, this.organizationId));
    this.saveMode.set(true);
  }

  // ---- edit, ctrl:255-274 `editQualityParam()`. The row from
  // getQualityParamList already carries `uomId`/`moduleId` directly (the
  // DTO), so no re-matching against the dropdown lists is needed here --
  // this sidesteps ctrl:262-273's fragile forEach-matching entirely, not
  // just the race around it. ----
  edit(row: QualityParameterMasterRecord): void {
    if (!this.dropdownsReady()) return;
    this.record.set({ ...row });
    this.saveMode.set(false);
  }

  // ---- status toggle, ctrl:305-325 `updateStatus()`. Update via the list
  // signal's `.update()` with a NEW row object, per docs/
  // ANGULAR_PENDING_FINDINGS.md entry 12 -- never mutate `row` in place. ----
  toggleStatus(row: QualityParameterMasterRecord): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.loader.start();
    this.api
      .updateStatus({
        unitId: this.unitId,
        organizationId: this.organizationId,
        qualityParameterMasterId: row.qualityParameterMasterId as number,
        status: nextStatus,
        update: 'Y',
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .pipe(finalize(() => this.loader.stop()))
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.list.update((rows) =>
              rows.map((r) =>
                r.qualityParameterMasterId === row.qualityParameterMasterId ? { ...r, status: nextStatus } : r,
              ),
            );
            this.notify.success(res.message ?? 'Success!');
          } else if (res.status === 'error') {
            this.notify.error(res.message ?? 'Error!');
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:206-207/223-224/312-313. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
