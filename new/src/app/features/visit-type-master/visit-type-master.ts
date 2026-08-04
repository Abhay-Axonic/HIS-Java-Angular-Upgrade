import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { VisitTypeMasterService } from './visit-type-master.service';
import { VisitTypeForm, VisitTypeRow, emptyVisitTypeForm } from './visit-type-master.models';

/**
 * Parity rebuild of the `visitTypeMaster` screen (batch 3, 2026-08-04).
 *
 * Reachable via the shared `.state('commonMaster', {url:'/commonMaster/:url',
 * resolve:{getController:...}})` dispatcher, entry `visitType` ->
 * `visitTypeMasterController`, template `views/master/common-master-screen.html`.
 * The dispatcher is frontend-only (no shared backend — fan-in finding
 * batch29-3); this screen's backend is `VisitTypeMasterController`
 * (`/api/global`) -> `VisitTypeMasterServiceImpl` -> `VisitTypeMasterDaoImpl`.
 *
 * Backend secured 2026-08-04 (BR-161-class): VerifiedIdentityService rebind
 * on all reads/writes, named queries org-scoped
 * (`GET_ACTIVE_VISIT_TYPE_LIST`/`GET_VISIT_TYPE_LIST` now
 * `WHERE organizationId=:orgId`), DAO params bound, cache keyed org+unit.
 * Live-verified: forged org-999 reads return verified org-1 rows only, 0
 * victims leaked (seeded VT-999-X), legit counts intact (6 rows).
 *
 * Wire contract (golden-traced live, same common-master id/code/desc
 * pattern): list POST /api/global/visitTypeMasterList
 * {organizationId,offset,noOfRecordsPerPage}; count POST
 * .../visitTypeMasterListCount {organizationId}; save POST /saveVisitType
 * {visitTypeCode,visitTypeName,status,createdBy,updatedBy,organizationId,
 * createdDate,updatedDate}; edit GET /getVisitTypeById/visitTypeId/{id}/
 * orgId/{org}; update POST /updateVisitType {visitTypeCode,visitTypeName,
 * visitTypeId,updatedBy,updatedDate}; toggle POST /updateVisitTypeStatus
 * {visitTypeId,status,updatedDate,updatedBy}.
 *
 * Faithful-to-legacy quirks reproduced: save is confirm-modal-gated with the
 * popup rearmed on BOTH outcomes (entry-55-class convention); update has no
 * modal; toggle has no revert-on-failure and does not reload the list.
 */
@Component({
  selector: 'app-visit-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './visit-type-master.html',
  styleUrl: './visit-type-master.scss',
})
export class VisitTypeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(VisitTypeMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  form = signal<VisitTypeForm>(emptyVisitTypeForm());
  saveMode = signal(true);
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  private popupArmed = true;

  rows = signal<VisitTypeRow[]>([]);

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.api.getVisitTypeList(this.organizationId, 0, 10).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
    this.api.getVisitTypeCount(this.organizationId).subscribe();
  }

  updateField<K extends keyof VisitTypeForm>(field: K, value: VisitTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ctrl:20-22 -- required code (max 10) + required desc (max 256).
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.code.trim() && f.code.length <= 10 && f.desc.trim() && f.desc.length <= 256);
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !(this.form().code.trim() && this.form().code.length <= 10);
  }
  isDescInvalid(): boolean {
    return this.formSubmitted() && !(this.form().desc.trim() && this.form().desc.length <= 256);
  }

  // mirrors $scope.save -- confirm-modal-gated, rearmed on every outcome.
  onSaveClick(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    if (this.popupArmed) {
      this.showConfirmModal.set(true);
      return;
    }
    this.confirmSave();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmFromModal(): void {
    this.popupArmed = false;
    this.showConfirmModal.set(false);
    this.confirmSave();
  }

  private confirmSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .saveVisitType({
        visitTypeCode: f.code,
        visitTypeName: f.desc,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Visit Type Added Successfully...!!');
            this.form.set(emptyVisitTypeForm());
            this.formSubmitted.set(false);
          } else {
            this.notify.error('Visit Type already exists...!!');
          }
          this.loadList();
          this.popupArmed = true;
        },
      });
  }

  // mirrors $scope.update -- no confirm-modal gate.
  onUpdateClick(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    this.api
      .updateVisitType({
        visitTypeCode: f.code,
        visitTypeName: f.desc,
        visitTypeId: f.id as number,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Visit Type Updated Successfully...!!');
            this.form.set(emptyVisitTypeForm());
            this.formSubmitted.set(false);
            this.saveMode.set(true);
          } else {
            this.notify.error('Visit Type already exists...!!');
          }
          this.loadList();
        },
      });
  }

  // mirrors showUpdateBtn.
  edit(row: VisitTypeRow): void {
    this.saveMode.set(false);
    this.api.getVisitTypeById(row.id, this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          window.alert('Error!!');
          return;
        }
        const d = res.listObject[0];
        this.form.set({ id: d.id, code: d.code, desc: d.desc });
      },
    });
  }

  // mirrors updateStatus -- no revert-on-failure, no list reload.
  toggleStatus(row: VisitTypeRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.rows.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    this.api
      .updateVisitTypeStatus({
        visitTypeId: row.id,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Visit Type Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl dt. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
