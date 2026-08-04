import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { PaymentModeMasterService } from './payment-mode-master.service';
import { PaymentModeForm, PaymentModeRow, emptyPaymentModeForm } from './payment-mode-master.models';

/**
 * Parity rebuild of the `paymentModeMaster` screen (batch 3, 2026-08-04).
 *
 * Reachable via the shared `.state('commonMaster', {url:'/commonMaster/:url',
 * resolve:{getController:...}})` dispatcher, entry `paymentMode` ->
 * `paymentModeMasterController`, template `views/master/common-master-screen.html`.
 * Backend: `PaymentModeMasterController` (`/api/billing`) -> service -> DAO.
 *
 * Backend secured 2026-08-04 (BR-161-class): VerifiedIdentityService rebind
 * on all reads/writes (the list was scoped by the CLIENT-SUPPLIED DTO
 * organizationId -- treated vulnerable-until-proven, same class as the two
 * confirmed leaks). Live-verified: forged org-999 reads return verified
 * org-1 rows only, 0 victims leaked (seeded PM-999-X), legit counts intact
 * (8). Sibling endpoints verified: count=8, active-list=8 (no victim),
 * getById 200.
 *
 * Wire contract (golden-traced live, DTO field names): list POST
 * /api/billing/getPaymentModeMasterList {organizationId,offset,
 * noOfRecordsPerPage}; count POST /api/billing/getPaymentModeMasterCount
 * {organizationId}; save POST /api/billing/savePaymentModeMaster
 * {paymentEntitlementCode,paymentEntitlementDesc,status,createdBy,
 * updatedBy,organizationId,createdDate,updateDate}; edit GET
 * /api/billing/getPaymentModeMasterById/{id}/{orgId}; update POST
 * /api/billing/updatePaymentModeMaster {paymentEntitlementCode,
 * paymentEntitlementDesc,paymentEntitlementId,organizationId,updatedBy,
 * updateDate}; toggle POST /api/billing/updatePaymentModeMasterStatus
 * {paymentEntitlementId,status,updateDate,updatedBy}.
 *
 * Faithful-to-legacy quirks reproduced: save is confirm-modal-gated with the
 * popup rearmed on BOTH outcomes (entry-55-class convention); update has no
 * modal; toggle has no revert-on-failure and does not reload the list.
 */
@Component({
  selector: 'app-payment-mode-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './payment-mode-master.html',
  styleUrl: './payment-mode-master.scss',
})
export class PaymentModeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(PaymentModeMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  form = signal<PaymentModeForm>(emptyPaymentModeForm());
  saveMode = signal(true);
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  private popupArmed = true;

  rows = signal<PaymentModeRow[]>([]);

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.api.getPaymentModeList(this.organizationId, 0, 10).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
    this.api.getPaymentModeCount(this.organizationId).subscribe();
  }

  updateField<K extends keyof PaymentModeForm>(field: K, value: PaymentModeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // required code (max 10) + required desc (max 256).
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
      .savePaymentMode({
        paymentEntitlementCode: f.code,
        paymentEntitlementDesc: f.desc,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        createdDate: now,
        updateDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Payment Mode Added Successfully...!!');
            this.form.set(emptyPaymentModeForm());
            this.formSubmitted.set(false);
          } else {
            this.notify.error('Payment Mode already exists...!!');
          }
          this.loadList();
          this.popupArmed = true;
        },
      });
  }

  onUpdateClick(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    this.api
      .updatePaymentMode({
        paymentEntitlementCode: f.code,
        paymentEntitlementDesc: f.desc,
        paymentEntitlementId: f.id as number,
        organizationId: this.organizationId,
        updatedBy: this.userId,
        updateDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Payment Mode Updated Successfully...!!');
            this.form.set(emptyPaymentModeForm());
            this.formSubmitted.set(false);
            this.saveMode.set(true);
          } else {
            this.notify.error('Payment Mode already exists...!!');
          }
          this.loadList();
        },
      });
  }

  edit(row: PaymentModeRow): void {
    this.saveMode.set(false);
    this.api.getPaymentModeById(row.id, this.organizationId).subscribe({
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

  toggleStatus(row: PaymentModeRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.rows.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    this.api
      .updatePaymentModeStatus({
        paymentEntitlementId: row.id,
        status: nextStatus,
        updateDate: formatDateTime(new Date()),
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Payment Mode Updated Successfully...!!');
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
