import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { PaymentEntitlementMasterService } from './payment-entitlement-master.service';
import {
  PaymentEntitlementForm,
  PaymentEntitlementRow,
  emptyPaymentEntitlementForm,
} from './payment-entitlement-master.models';

/**
 * Parity rebuild of the `paymentEntitmentMaster` screen (batch 3,
 * 2026-08-04).
 *
 * Reachable via the shared `.state('commonMaster', {url:'/commonMaster/:url',
 * resolve:{getController:...}})` dispatcher, entry `paymentEntitment` ->
 * `paymentEntitmentMasterController`, template
 * `views/master/common-master-screen.html`. Backend:
 * `PaymentEntitlementController` (`/global`) -> service -> DAO.
 *
 * Backend secured 2026-08-04 (BR-161-class): VerifiedIdentityService rebind
 * on all reads/writes; `GET_PAYMENT_ENTITLEMENT_LIST` named query had NO
 * WHERE clause at all and the DAO's `setParameter("orgId")` was COMMENTED
 * OUT -- now org-scoped (`WHERE organizationId=:orgId`) with the param
 * re-enabled. Live-verified: forged org-999 reads return verified org-1
 * rows only, 0 victims leaked (seeded PE-999-X), legit counts intact (3).
 *
 * Wire contract (golden-traced live): list POST /global/
 * getPaymententitlementList {organizationId,offset,noOfRecordsPerPage};
 * count POST /global/getPaymententitlementCount {organizationId}; save POST
 * /global/savePaymententitlement {paymentEntitlementCode,
 * paymentEntitlementDesc,status,createdBy,updatedBy,organizationId,
 * createdDate,updatedDate}; edit GET /global/getPaymententitlementById/
 * orgId/{org}/paymentEntId/{id}; update POST /global/updatePaymententitlement
 * {paymentEntitlementCode,paymentEntitlementDesc,paymentEntitlementId,
 * organizationId,updatedBy,updateDate}; toggle POST /global/
 * updatePaymententitlementStatus {paymentEntitlementId,status,updateDate,
 * updatedBy}.
 *
 * Faithful-to-legacy quirks reproduced: save is confirm-modal-gated with the
 * popup rearmed on BOTH outcomes (entry-55-class convention); update has no
 * modal; toggle has no revert-on-failure and does not reload the list.
 */
@Component({
  selector: 'app-payment-entitlement-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './payment-entitlement-master.html',
  styleUrl: './payment-entitlement-master.scss',
})
export class PaymentEntitlementMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(PaymentEntitlementMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  form = signal<PaymentEntitlementForm>(emptyPaymentEntitlementForm());
  saveMode = signal(true);
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  private popupArmed = true;

  rows = signal<PaymentEntitlementRow[]>([]);

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.api.getPaymentEntitlementList(this.organizationId, 0, 10).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
    this.api.getPaymentEntitlementCount(this.organizationId).subscribe();
  }

  updateField<K extends keyof PaymentEntitlementForm>(field: K, value: PaymentEntitlementForm[K]): void {
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
      .savePaymentEntitlement({
        paymentEntitlementCode: f.code,
        paymentEntitlementDesc: f.desc,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        createdDate: now,
        // Legacy-exact wire field: the backend DAO reads getUpdateDate()
        // (Global PaymentEntitlementMasterDto), NOT updatedDate -- sending
        // updatedDate left updateDate null -> 400 "Invalid parameters".
        updateDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Payment Entitlement Added Successfully...!!');
            this.form.set(emptyPaymentEntitlementForm());
            this.formSubmitted.set(false);
          } else {
            this.notify.error('Payment Entitlement already exists...!!');
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
      .updatePaymentEntitlement({
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
            this.notify.success('Payment Entitlement Updated Successfully...!!');
            this.form.set(emptyPaymentEntitlementForm());
            this.formSubmitted.set(false);
            this.saveMode.set(true);
          } else {
            this.notify.error('Payment Entitlement already exists...!!');
          }
          this.loadList();
        },
      });
  }

  edit(row: PaymentEntitlementRow): void {
    this.saveMode.set(false);
    this.api.getPaymentEntitlementById(row.id, this.organizationId).subscribe({
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

  toggleStatus(row: PaymentEntitlementRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.rows.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    this.api
      .updatePaymentEntitlementStatus({
        paymentEntitlementId: row.id,
        status: nextStatus,
        updatedBy: this.userId,
        updateDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Payment Entitlement Updated Successfully...!!');
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
