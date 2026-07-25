import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ArMasterService } from './ar-master.service';
import { ArMasterForm, ArMasterRow, emptyArMasterForm } from './ar-master.models';

const PAGE_SIZES = [10, 25, 50, 100];

/**
 * Parity rebuild of `views/master/UnitMaster/billingMaster/AR-master.html` +
 * `resources/assets/js/controller/billing/unit/aRmasterMasterController.js`
 * (`aRmasterMasterController`, classic `$scope.*`). Full Step 1 audit:
 * `docs/ANGULAR_PENDING_FINDINGS.md`, `PENDING-armaster-step1-findings`.
 *
 * Module gate: id 616, confirmed via the live `.state('ARmaster', ...)`
 * resolve block (`app.js:14835-14862`) -- enforced at the route level.
 *
 * ================================================================
 * Pagination -- CORRECTED here (owner-approved), not faithfully reproduced
 * ================================================================
 * Legacy's own pagination is functionally dead: `initMasterList` is declared
 * with 4 params but called with only 2 (ctrl:116); the list-fetch URL build
 * (ctrl:92) reads `$scope.offset` directly instead of its own parameter, and
 * `$scope.offset` is initialized to 0 and never reassigned anywhere in the
 * controller -- clicking Next/Previous/a page number always re-fetches
 * offset 0 while the pager UI shows a different position. A user has no way
 * to know which page is actually loaded, matching the same
 * invisible-to-the-user reasoning already applied to `nutritionalMaster`'s
 * count crash. `loadPage()` below computes `offset` fresh from the target
 * page on every call -- deliberately not reproducing the bug.
 *
 * ================================================================
 * Validation -- FAITHFULLY reproduced: genuinely zero validation exists
 * ================================================================
 * Both fields show a red `*` in legacy's own markup, but neither has any
 * HTML5 attribute (`required`/`ng-required`/etc.) nor any manual JS check in
 * `save()`/`update()` before the POST fires (grepped, confirmed absent). A
 * blank form saves successfully in legacy today. This is real, observable
 * (non-silent) current behavior, not a hidden bug -- reproduced as-is below
 * (no client-side required check is added). The only real gate anywhere is
 * the server-side duplicate-description check (see below).
 *
 * ================================================================
 * Duplicate-check: description only (Code never compared)
 * ================================================================
 * `saveARCodeMaster`/`updateARCodeMaster` both reject on a case-insensitive
 * `aRCodeDesc` match within the same org+unit -- `aRCode` is never checked
 * for uniqueness. Faithfully reproduced as the real, working server
 * behavior; no client-side duplicate check added.
 *
 * ================================================================
 * Confirm-modal: fires ONLY on this page-load's first-ever Save, faithfully
 * reproduced
 * ================================================================
 * `$scope.popUpFlag` is set to `true` exactly once, at controller init
 * (ctrl:13), and to `false` in `PopupOkBtn` (ctrl:122) -- it is NEVER reset
 * back to `true` anywhere else in the controller (confirmed by exhaustive
 * grep: only these 2 assignments + the 1 read exist). This is a distinct
 * shape from the already-covered `docs/ANGULAR_PENDING_FINDINGS.md` entry-152
 * pattern (which is about an `init()` that resets the flag on success but not
 * failure) -- ARmaster's `init()`/`resetForm` never touches `popUpFlag` at
 * all. Practical effect: the confirm dialog appears before the very first
 * Save of the page session, and never again after that, for any subsequent
 * Save in the same session (a page reload is the only way to see it again).
 * Reproduced verbatim via `confirmArmed`, which is set once at construction
 * and never re-armed.
 *
 * ================================================================
 * Edit -- real GET-by-id call
 * ================================================================
 * `showUpdateBtn(id)` fires a real `GET /global/billing/getARCodeMasterById/
 * {id}/{orgId}/{unitId}` and populates the form from the response.
 *
 * ================================================================
 * Known, disclosed, non-blocking issue -- NOT fixed here
 * ================================================================
 * The shared `<modal>` directive's close (×) button hardcodes a call to
 * `cancel()`, which this controller never defines -- a live crash on
 * unmodified legacy. Per the standing entry-55 policy, this rebuild's
 * confirm modal has ordinary working close behavior instead.
 */
@Component({
  selector: 'app-ar-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './ar-master.html',
  styleUrl: './ar-master.scss',
})
export class ArMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ArMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common) ----
  form = signal<ArMasterForm>(emptyArMasterForm());
  saveMode = signal(true);
  showConfirmModal = signal(false);
  /** Mirrors `$scope.popUpFlag` -- see class doc comment. Set once, never
   * re-armed: only this page session's first-ever Save shows the modal. */
  private confirmArmed = true;

  // ---- server-side pagination (corrected -- see class doc) ----
  rows = signal<ArMasterRow[]>([]);
  totalRecords = signal(0);
  currentPage = signal(1);
  pageSize = signal(10);
  pageSizes = PAGE_SIZES;
  totalPages = computed(() => Math.max(1, Math.ceil(this.totalRecords() / this.pageSize())));

  // ---- search -- client-side over the currently-loaded page only, matching
  // legacy's dead `serachTestmaster` ng-keyup call (function doesn't exist in
  // the controller) still working via Angular's own `filter` reacting to the
  // same ng-model. ----
  searchKeyword = signal('');
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.rows();
    if (!keyword) return rows;
    return rows.filter(
      (r) => r.aRCode?.toLowerCase().includes(keyword) || r.aRCodeDesc?.toLowerCase().includes(keyword),
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
    return Array.from({ length: this.totalPages() }, (_, i) => i + 1);
  }

  updateField<K extends keyof ArMasterForm>(field: K, value: ArMasterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- save (mirrors save() -- confirm-modal gated only once per page
  // session, see class doc; no field validation anywhere -- faithful) ----
  save(): void {
    if (this.confirmArmed) {
      this.showConfirmModal.set(true);
      return;
    }
    this.doSave();
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.confirmArmed = false;
    this.showConfirmModal.set(false);
    this.doSave();
  }

  private doSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .save({
        aRCode: f.aRCode,
        aRCodeDesc: f.aRCodeDesc,
        status: 'A',
        createdDate: now,
        createdBy: this.userId,
        unitId: this.unitId,
        orgId: this.organizationId,
        updatedBy: this.userId,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('AR Code Added Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('AR Code Description already exist...!!');
          }
        },
      });
  }

  // ---- edit (mirrors showUpdateBtn -- real GET-by-id) ----
  edit(row: ArMasterRow): void {
    this.saveMode.set(false);
    this.api.getById(row.aRCodeId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({ aRCodeId: detail.aRCodeId, aRCode: detail.aRCode, aRCodeDesc: detail.aRCodeDesc });
        } else {
          this.notify.error(res.message ?? 'Something went wrong...!!');
        }
      },
    });
  }

  // ---- update (mirrors update() -- no confirm modal, no validation) ----
  update(): void {
    const f = this.form();
    this.api
      .update({
        aRCodeId: f.aRCodeId as number,
        aRCode: f.aRCode,
        aRCodeDesc: f.aRCodeDesc,
        unitId: this.unitId,
        orgId: this.organizationId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('AR Code Updated Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('AR Code Description already exist...!!');
          }
        },
      });
  }

  // ---- status toggle ----
  toggleStatus(row: ArMasterRow): void {
    const nextStatus = row.status === 'A' ? 'I' : 'A';
    this.rows.update((list) => list.map((r) => (r.aRCodeId === row.aRCodeId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatus({
        status: nextStatus,
        aRCodeId: row.aRCodeId,
        unitId: this.unitId,
        orgId: this.organizationId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('AR Code Status Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyArMasterForm());
    this.saveMode.set(true);
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
