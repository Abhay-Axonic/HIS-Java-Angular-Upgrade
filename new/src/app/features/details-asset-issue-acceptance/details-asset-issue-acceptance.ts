import { Component, computed, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { CommonDetailHandoffService } from '../../core/common-detail-handoff.service';
import { DetailsAssetIssueAcceptanceService } from './details-asset-issue-acceptance.service';
import { DaiaDetail, DaiaItem, DaiaRejectReason, DaiaStatus, emptyDaiaDetail } from './details-asset-issue-acceptance.models';

/**
 * Rebuild of the legacy `detailsAssetIssueAcceptance` state -- see
 * detailsAssetIssueAcceptanceController.js (360 lines) + the matching
 * details-asset-issue-acceptance.html view. Self-registered `config`
 * block, `controllerAs: "vm"`, no `data.moduleId`/resolve guard -- real
 * cookieObject session guard only, matching legacy exactly (unlike the
 * list state `assetIssueAcceptance`, which does gate on moduleId 215).
 * Item id arrives via CommonDetailHandoffService, set by the list
 * screen's own `navigateToDetail()`.
 *
 * Confirmed genuinely wired (unlike some sibling detail screens in this
 * batch): the modal Save (`SaveAcceptReject`, ctrl:220-249) and the
 * footer Save (`SaveApproval`, ctrl:305-342 -> real PATCH to
 * ASEET_ISSUE_ACCEPTANCE.SAVE_API) both have real, reachable
 * implementations wired to real view bindings. This screen's own inline
 * rejection modal (view:291-352) is used -- it does NOT include the
 * broken qty-accept-reject.html/note-save.html shared partials some
 * other screens in this batch fell back to.
 *
 * Two things confirmed DEAD in the legacy view (commented out /
 * genuinely unreferenced) and deliberately NOT rebuilt, same treatment
 * as Organization Master's commented-out licence-details sub-table:
 * - The "View" accessories button + its column header (view:71-81,
 *   159-162) are HTML-commented out; `openAccessory()`
 *   (ctrl:299-304) and the `#viewDetailsAccessoriesModal` markup
 *   (view:213-289) are unreachable from any live control.
 * - `quantityChange()` (ctrl:146-149), `valueOnChange()` (ctrl:194-218),
 *   `isRejectReasonValid`/`rejectionFlag` (view:348, set inline but
 *   never read anywhere) are all defined/assigned but never actually
 *   invoked/read from the view -- confirmed by grepping the view for
 *   every `vm.model.*` call. Omitted; zero observable effect.
 * - `self.model.rejectQty` (ctrl:136,169,173,177,181) and
 *   `self.model.reasons` (ctrl:141) are written but never bound to any
 *   template expression (the modal's reason `<select>` reads
 *   `vm.model.rejectReasons` directly, not `vm.model.reasons`) --
 *   likewise omitted as non-observable dead state. One side effect of
 *   this is worth flagging: `RejectionPopup` (ctrl:134-145) resets
 *   `self.model.rejectQty` on open, NOT the actually-bound
 *   `self.model.qty` -- so the Qty input is never cleared between
 *   rejection popups and can show a stale value from a previous item.
 *   Reproduced faithfully below (`rejectionPopup()` does not reset `qty`).
 * - `SelectedRequestIds` (ctrl:86,91,96) is built in `SelectAllItems`
 *   but never read anywhere else -- omitted, non-observable.
 * - `data.statusId = setStatus('Partially Completed')` (ctrl:328-329)
 *   is guarded by `self.model.rejectQuantity > 0`, but
 *   `self.model.rejectQuantity` (not `item.rejectQuantity`) is never
 *   assigned anywhere in this controller -- always `undefined`, so this
 *   branch can never execute. Confirmed dead; omitted from `saveApproval()`.
 *
 * TWO items intentionally reproduced AS-IS per audit (NOT fixed):
 *
 * 1. CRASH RISK -- `setStatus(x)` (ctrl:47-52) does
 *    `self.model.statuses.find(o => o.status == x)` and immediately
 *    reads `.id` off the result with no guard. `self.model.statuses` is
 *    populated asynchronously by `StatusService.GetStatus()`
 *    (ctrl:19-21), fired in `initializeController()` alongside (not
 *    awaited before) `GetAssetIssueAcceptanceDetails()` -- nothing gates
 *    the footer Save button on that load finishing. `SaveApproval`
 *    (ctrl:305-342) unconditionally calls `setStatus('Closed')` at
 *    ctrl:309 as the first thing it does after `ValidateApproval()`
 *    passes. Exact trigger: `.find()` returns `undefined` -- either
 *    because `self.model.statuses` hasn't finished loading yet (empty),
 *    or because the loaded list has no entry whose `.status` field is
 *    the literal string `'Closed'` -- and the following `.id` access
 *    throws `TypeError: Cannot read properties of undefined (reading 'id')`.
 *    Reproduced as a direct, unguarded 1:1 port in `setStatus()` below --
 *    same non-null property access, no added guard.
 *
 * 2. SILENT DATA BUG -- ctrl:323: `self.model.rejectReasons.id` where
 *    `rejectReasons` is an ARRAY (set at ctrl:23-25 from
 *    `rejectReasonService.GetReason()`), so `.id` is always `undefined`
 *    and legacy silently falls back to the hardcoded literal reason id
 *    `1` for every saved item, regardless of which reason was actually
 *    selected in the modal. Reproduced identically in `saveApproval()`.
 */
@Component({
  selector: 'app-details-asset-issue-acceptance',
  imports: [FormsModule, RouterLink, DatePipe],
  templateUrl: './details-asset-issue-acceptance.html',
  styleUrl: './details-asset-issue-acceptance.scss',
})
export class DetailsAssetIssueAcceptance {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly handoff = inject(CommonDetailHandoffService);
  private readonly api = inject(DetailsAssetIssueAcceptanceService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;
  private storeId: number | undefined;
  private selectedItems: DaiaItem[] = [];
  private rejectItemId: number | undefined;

  detail = signal<DaiaDetail>(emptyDaiaDetail());
  selectAllRows = signal(false);
  statuses = signal<DaiaStatus[]>([]);
  rejectReasons = signal<DaiaRejectReason[]>([]);

  // Sort-arrow UI sugar (view:51-125) -- purely decorative `orderBy`
  // toggle, kept as component state rather than $scope-only like legacy.
  sortType = signal('');
  sortReverse = signal(false);
  sortedItems = computed(() => {
    const items = this.detail().itemResponse;
    const field = this.sortType();
    if (!field) return items;
    const sorted = [...items].sort((a, b) => {
      const av = (a as unknown as Record<string, unknown>)[field];
      const bv = (b as unknown as Record<string, unknown>)[field];
      if (av == null && bv == null) return 0;
      if (av == null) return -1;
      if (bv == null) return 1;
      if (typeof av === 'number' && typeof bv === 'number') return av - bv;
      return String(av).localeCompare(String(bv));
    });
    return this.sortReverse() ? sorted.reverse() : sorted;
  });

  // Rejection modal (view:291-352)
  showRejectModal = signal(false);
  qty = '';
  rejectReason: DaiaRejectReason | '' = '';
  rejectNote = '';

  constructor() {
    const cookieObject = this.session.current;
    if (cookieObject == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;
    this.storeId = cookieObject['storeId'] as number | undefined;

    this.api.getStatuses().subscribe((res) => this.statuses.set(res));
    this.api.getRejectReasons(this.unitId).subscribe((res) => this.rejectReasons.set(res));

    this.loadDetail();
  }

  private loadDetail(): void {
    const id = this.handoff.getDataId();
    this.loader.start();
    // Faithful port of ctrl:58-76: legacy calls $rootScope.stopLoader()
    // synchronously right after firing the GET, not inside the .then
    // callback -- the loader overlay doesn't actually cover the wait
    // for the response. Reproduced as-is (not "fixed"): stop() below
    // runs immediately after subscribe() fires the request, same order.
    this.api.getDetail(this.unitId, this.organizationId, id).subscribe({
      next: (res) => {
        res.itemResponse.forEach((item) => {
          item.isDisabled = true;
          item.approvedQuantity = item.issueQuantity;
          if (item.rejectQuantity) item.isReject = true;
        });
        this.detail.set(res);
      },
    });
    this.loader.stop();
  }

  private isSelected(item: DaiaItem): boolean {
    return !!item.isSelected;
  }

  private isDisable(item: DaiaItem): boolean {
    return item.statusId === 10;
  }

  selectAllItems(): void {
    const items = this.detail().itemResponse;
    if (this.selectAllRows()) {
      this.selectedItems = [];
      items.forEach((item) => {
        if (!this.isDisable(item)) {
          item.isSelected = true;
          item.isDisabled = false;
          this.selectedItems.push(item);
        }
      });
    } else {
      this.selectedItems = [];
      items.forEach((item) => {
        item.isSelected = false;
        item.isDisabled = true;
      });
    }
    this.detail.set({ ...this.detail() });
  }

  selectItem(item: DaiaItem): void {
    if (item.isSelected) {
      this.selectedItems.push(item);
      item.isDisabled = false;
    } else {
      item.isDisabled = true;
      item.isReject = false;
      const index = this.selectedItems.indexOf(item);
      if (index > -1) this.selectedItems.splice(index, 1);
    }
    this.selectAllRows.set(this.detail().itemResponse.every((i) => this.isSelected(i)));
    this.detail.set({ ...this.detail() });
  }

  /** ApprovedQuantity, ctrl:166-184. */
  approvedQuantity(item: DaiaItem): void {
    const approved = item.approvedQuantity;
    const issue = item.issueQuantity ?? 0;
    if ((approved ?? 0) > issue) {
      item.rejectQuantity = 0;
      item.isReject = false;
    } else if (approved === 0 || !approved) {
      item.rejectQuantity = issue;
      item.isReject = true;
    } else if (approved < issue) {
      item.rejectQuantity = issue - approved;
      item.isReject = true;
    } else if (approved === issue) {
      item.rejectQuantity = issue - approved;
      item.isReject = false;
    }
    this.detail.set({ ...this.detail() });
  }

  /** ValidateQuantity, ctrl:283-297 -- also called directly from the view's ng-blur. */
  validateQuantity(item: DaiaItem): boolean {
    let isValid = true;
    if (item.approvedQuantity === 0 || item.approvedQuantity == null) {
      item.isQuantityValid = true;
      isValid = false;
      this.notify.error('Please Enter Approved Quantity');
    } else if (item.approvedQuantity > issueQty(item)) {
      this.notify.error('Please Enter Accepted Quantity less than or equal to issue quantity');
      item.isQuantityValid = true;
      isValid = false;
    }
    this.detail.set({ ...this.detail() });
    return isValid;
  }

  private validateApproval(): boolean {
    let isValid = true;
    if (!this.selectedItems || this.selectedItems.length === 0) {
      this.notify.error('Select Items to Update and Save');
      isValid = false;
    }
    for (const issueDetail of this.detail().itemResponse) {
      if (!this.validateQuantity(issueDetail)) {
        isValid = false;
      }
    }
    return isValid;
  }

  /** RejectionPopup, ctrl:134-145. */
  rejectionPopup(item: DaiaItem): void {
    this.rejectReason = '';
    if (item.isReject) {
      this.rejectItemId = item.id;
      this.showRejectModal.set(true);
    } else {
      this.cancelRejection(item);
    }
  }

  private cancelRejection(item: DaiaItem): void {
    const prevRequest = this.detail().itemResponse.find((p) => p.id === item.id);
    if (prevRequest) {
      item.rejectQuantity = 0;
      item.approvedQuantity = prevRequest.issueQuantity;
      item.rejectNote = prevRequest.rejectNote;
      item.rejectReasonId = prevRequest.rejectReasonId;
      item.statusId = prevRequest.statusId;
    }
    this.detail.set({ ...this.detail() });
  }

  modalDismiss(): void {
    this.showRejectModal.set(false);
  }

  private validateRejection(): boolean {
    let isValid = true;
    if (!this.rejectReason) {
      isValid = false;
      this.notify.error('Please Select Reason to Reject');
    }
    if (!this.qty) {
      isValid = false;
      this.notify.error('Please enter reject quantity');
    }
    return isValid;
  }

  /** SaveAcceptReject, ctrl:220-249 (modal Save). */
  saveAcceptReject(): void {
    if (!this.validateRejection()) return;
    const rejectItemId = this.rejectItemId;
    const qty = Number(this.qty);
    const reasons = this.rejectReasons();
    this.selectedItems.forEach((item) => {
      if (item.id !== rejectItemId) return;
      const issue = item.issueQuantity ?? 0;
      if (qty > issue) {
        this.notify.error('Please Enter Reject Quantity Less Than Issue Quantity');
        return;
      } else if (qty === issue) {
        item.rejectQuantity = item.issueQuantity;
        item.approvedQuantity = issue - (item.rejectQuantity ?? 0);
        item.rejectReasonId = reasons[0]?.id;
      } else if (qty === 0 || !this.qty) {
        item.approvedQuantity = item.issueQuantity;
        item.rejectQuantity = 0;
        item.rejectReasonId = reasons[0]?.id;
      } else {
        item.rejectQuantity = qty;
        item.rejectNote = this.rejectNote;
        item.approvedQuantity = issue - qty;
        item.rejectReasonId = reasons[0]?.id;
      }
      this.modalDismiss();
      this.qty = '';
      this.rejectReason = '';
      this.rejectNote = '';
    });
    this.detail.set({ ...this.detail() });
  }

  /**
   * setStatus, ctrl:47-52 -- faithful, unguarded 1:1 port. See class doc
   * comment (finding 1) for the exact crash condition this reproduces.
   */
  private setStatus(label: string): number {
    const status = this.statuses().find((s) => s.status === label);
    return status!.id;
  }

  /** SaveApproval, ctrl:305-342 (footer Save). */
  saveApproval(): void {
    if (!this.validateApproval()) return;
    const id = this.handoff.getDataId();

    // See class doc comment (finding 2): `rejectReasons` is an array,
    // so this is always `undefined` and every item silently gets the
    // hardcoded fallback reason id `1` below -- ctrl:323, not fixed.
    const rejectReasonsDotId = (this.rejectReasons() as unknown as { id?: number }).id;

    const itemRequest: Record<string, unknown>[] = [];
    const data: Record<string, unknown> = {
      statusId: this.setStatus('Closed'),
      userId: this.userId,
      unitId: this.unitId,
      organizationId: this.organizationId,
      storeId: this.storeId,
      itemRequest,
    };

    this.loader.start();
    this.selectedItems.forEach((item) => {
      itemRequest.push({
        batchMapperId: Number(item.batchMapperId),
        acceptedQuantity: Number(item.approvedQuantity),
        rejectQuantity: item.rejectQuantity ? item.rejectQuantity : 0,
        rejectReasonId: rejectReasonsDotId ? rejectReasonsDotId : 1,
        rejectNote: this.rejectNote,
      });
    });

    this.api.saveApproval(id, data).subscribe({
      next: (res) => {
        this.loader.stop();
        this.notify.success(res.message ?? '');
        this.router.navigate(['/assetIssueAcceptance']);
      },
      error: () => {
        this.loader.stop();
        this.notify.error('Somthing Went Wrong');
      },
    });
  }
}

function issueQty(item: DaiaItem): number {
  return item.issueQuantity ?? 0;
}
