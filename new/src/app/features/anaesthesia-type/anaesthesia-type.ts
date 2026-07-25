import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { AnaesthesiaTypeService } from './anaesthesia-type.service';
import { AnaesthesiaTypeForm, AnaesthesiaTypeRow, emptyAnaesthesiaTypeForm } from './anaesthesia-type.models';

const PAGE_SIZES = [10, 25, 50, 100];

/**
 * Parity rebuild of `views/master/GlobalMaster/OTIMS/anaesthesia_type.html` +
 * `resources/assets/js/controller/otims/global/anaesthesiaTypeController.js`
 * (`anaesthesiaTypeController`, classic `$scope.*`, zero `vm.*` in the view --
 * confirmed exhaustively). Full Step 1 audit:
 * `docs/ANGULAR_PENDING_FINDINGS.md`, Batch 19.
 *
 * Module gate: id 524, confirmed via the live `.state('anaesthesiaType', ...)`
 * resolve block (`app.js:12435-12451`) -- enforced at the route level via
 * `moduleAccessGuard`, not in this component.
 *
 * ================================================================
 * Server-side pagination -- genuinely functional both endpoints (unlike
 * ARmaster's sibling finding)
 * ================================================================
 * `getListAnesthesiaTypeMaster`/`getAnesthesiaTypeMasterCount` both work
 * correctly and are org+unit scoped. Real offset/limit paging is implemented
 * here, computing `offset` fresh from the target page on every navigation
 * call (not a stale scope variable -- see ARmaster's logged pagination bug
 * for the failure mode being deliberately avoided).
 *
 * ================================================================
 * Duplicate-check: name-only (Desc never compared), org+unit scoped
 * ================================================================
 * `saveAnesthesiaTypeMaster`/`updateAnesthesiaTypeMaster` both reject on a
 * case-insensitive `anesthesiaTypeName` match within the same org+unit --
 * `anesthesiaTypeDesc` is never checked for uniqueness. No client-side
 * duplicate check is added on top; faithfully reproduced as the real,
 * working server behavior.
 *
 * ================================================================
 * Edit -- real GET-by-id call (unlike transactionTypeMaster's sibling,
 * which copies primitives straight off the row)
 * ================================================================
 * `showUpdateBtn(id)` (ctrl:197-223) fires a real
 * `GET /api/otims/getAnesthesiaTypeMasterById/{id}/{orgId}/{unitId}` and
 * populates the form from the response, not from the already-rendered row.
 * Reproduced via `edit()` below.
 *
 * ================================================================
 * Confirm-modal gating -- Save only, matching legacy's own asymmetry
 * ================================================================
 * `PopupOkBtn()` (ctrl) is the only path that actually POSTs on Create --
 * gated behind the confirm modal every time (no `popUpFlag`-style re-arm
 * logic on this screen at all, unlike `transactionTypeMaster`/
 * `nurseAllocationMaster`'s siblings -- confirmed by grep, no `popUpFlag`
 * anywhere in this controller). Update posts directly, no modal.
 *
 * ================================================================
 * Known, disclosed, non-blocking issue -- NOT fixed here
 * ================================================================
 * The shared `<modal>` directive's close (×) button hardcodes a call to
 * `cancel()`, which this controller never defines (confirmed, zero
 * occurrences) -- a live crash on unmodified legacy. Per the standing
 * `docs/ANGULAR_PENDING_FINDINGS.md` entry-55 policy, this rebuild's confirm
 * modal simply has ordinary working close behavior instead -- not a fresh
 * per-screen decision.
 */
@Component({
  selector: 'app-anaesthesia-type',
  imports: [FormsModule, StatusToggle],
  templateUrl: './anaesthesia-type.html',
  styleUrl: './anaesthesia-type.scss',
})
export class AnaesthesiaType {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(AnaesthesiaTypeService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common) ----
  form = signal<AnaesthesiaTypeForm>(emptyAnaesthesiaTypeForm());
  saveMode = signal(true);
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- server-side pagination ----
  rows = signal<AnaesthesiaTypeRow[]>([]);
  totalRecords = signal(0);
  currentPage = signal(1);
  pageSize = signal(10);
  pageSizes = PAGE_SIZES;
  totalPages = computed(() => Math.max(1, Math.ceil(this.totalRecords() / this.pageSize())));

  // ---- search -- client-side over the currently-loaded page only, matching
  // legacy's own `filter:searchKeyword` over `$scope.commonList` (no keyword
  // param exists anywhere in the DTO/DAO). ----
  searchKeyword = signal('');
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.rows();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.anesthesiaTypeCode?.toLowerCase().includes(keyword) ||
        r.anesthesiaTypeName?.toLowerCase().includes(keyword) ||
        r.anesthesiaTypeDesc?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadPage(1);
  }

  loadPage(page: number): void {
    this.currentPage.set(page);
    const offset = (page - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalRecords.set(res.object);
      },
    });
  }

  onPageSizeChange(): void {
    this.loadPage(1);
  }

  pagesList(): number[] {
    const total = this.totalPages();
    return Array.from({ length: total }, (_, i) => i + 1);
  }

  updateField<K extends keyof AnaesthesiaTypeForm>(field: K, value: AnaesthesiaTypeForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (all 3 fields required in legacy's own
  // markup, gated by $broadcast('show-errors-check-validity')) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.anesthesiaTypeCode.trim() && f.anesthesiaTypeName.trim() && f.anesthesiaTypeDesc.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().anesthesiaTypeCode.trim();
  }

  isNameInvalid(): boolean {
    return this.formSubmitted() && !this.form().anesthesiaTypeName.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().anesthesiaTypeDesc.trim();
  }

  // ---- save (mirrors saveAnesthesiaTypeMaster -- always confirm-modal
  // gated, no re-arm logic to model) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        anesthesiaTypeCode: f.anesthesiaTypeCode,
        anesthesiaTypeName: f.anesthesiaTypeName,
        anesthesiaTypeDesc: f.anesthesiaTypeDesc,
        status: 'A',
        orgId: this.organizationId,
        unitId: this.unitId,
        createdBy: this.userId,
        updatedBy: this.userId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Anesthesia Type Added Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Anesthesia Type already exist...!!');
          }
        },
      });
  }

  // ---- edit (mirrors showUpdateBtn -- real GET-by-id, see class doc) ----
  edit(row: AnaesthesiaTypeRow): void {
    this.saveMode.set(false);
    this.formSubmitted.set(false);
    this.api.getById(row.anesthesiaTypeId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            anesthesiaTypeId: detail.anesthesiaTypeId,
            anesthesiaTypeCode: detail.anesthesiaTypeCode,
            anesthesiaTypeName: detail.anesthesiaTypeName,
            anesthesiaTypeDesc: detail.anesthesiaTypeDesc,
          });
        } else {
          this.notify.error('Something went wrong...!!');
        }
      },
    });
  }

  // ---- update (mirrors updateAnesthesiaTypeMaster -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const f = this.form();
    this.api
      .update({
        anesthesiaTypeId: f.anesthesiaTypeId as number,
        anesthesiaTypeCode: f.anesthesiaTypeCode,
        anesthesiaTypeName: f.anesthesiaTypeName,
        anesthesiaTypeDesc: f.anesthesiaTypeDesc,
        orgId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Anesthesia Type Updated Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Anesthesia Type already exist...!!');
          }
        },
      });
  }

  // ---- status toggle ----
  toggleStatus(row: AnaesthesiaTypeRow): void {
    const nextStatus = row.status === 'A' ? 'I' : 'A';
    this.rows.update((list) =>
      list.map((r) => (r.anesthesiaTypeId === row.anesthesiaTypeId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        anesthesiaTypeId: row.anesthesiaTypeId,
        status: nextStatus,
        orgId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Anesthesia Type Status Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyAnaesthesiaTypeForm());
    this.saveMode.set(true);
    this.formSubmitted.set(false);
    this.loadPage(this.currentPage());
  }
}

function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
