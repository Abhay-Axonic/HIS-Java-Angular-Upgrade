import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { CommonDetailHandoffService } from '../../core/common-detail-handoff.service';
import { AssetReturnAcceptanceDetailsService } from './asset-return-acceptance-details.service';
import { AradDetail, AradItem, AradRejectReason, AradStatus } from './asset-return-acceptance-details.models';

/**
 * Rebuild of the legacy `assetReturnAcceptanceDetails` state -- see
 * assetReturnAcceptanceDetailsController.js (261 lines) +
 * details-asset-return-acceptance.html (277 lines). Self-registered state,
 * no resolve/moduleId guard (unlike the list state `assetReturnAcceptance`,
 * moduleId 217 -- already rebuilt) -- only the real cookieObject session
 * guard applies here. Item id comes from CommonDetailHandoffService, set
 * by the list screen's own `assetReturnAcceptanceDetails(item)`.
 *
 * TWO real, reproducible crash bugs, NOT fixed, faithfully reproduced as
 * this screen's parity target (same convention as
 * linen-issue-acceptance-details.ts -- TypeScript can't express
 * AngularJS's exact undefined-variable/undefined-method bugs, so the
 * identical OBSERVABLE crash is reproduced via a direct `throw` at the
 * equivalent trigger point instead):
 *
 * 1. `SelectItem` (ctrl:87-100): the else-branch (row checkbox unchecked,
 *    i.e. a user deselects an already-selected row) calls
 *    `areturn.indexOf(areturn)` -- `.indexOf` on the row object itself,
 *    not on the `SelectedItems` array (`mreturns`) it clearly meant to
 *    search. Throws `TypeError: areturn.indexOf is not a function` the
 *    instant a row is deselected, after `isDisabled`/`isReject` are reset
 *    on the object but before the `SelectedItems` splice or the
 *    select-all recompute run -- see `selectItem()`.
 *
 * 2. `CancelRejection` (ctrl:142-154), reached from the else-branch of
 *    `RejectionPopup` (ctrl:130-140) when a user toggles a row's reject
 *    switch back OFF: reads `self.model.requestItems.areturn`, but
 *    `self.model.requestItems` is never assigned anywhere in the
 *    controller. Throws `TypeError: Cannot read properties of undefined
 *    (reading 'areturn')` -- see `rejectionPopup()`. The switch-ON path
 *    (opening the reject modal) is real and unaffected.
 *
 * Two more inert bindings rendered but left unwired, matching legacy's own
 * dead state (`valueOnChange`/`ApprovedQuantity` are never assigned on
 * `self.model` anywhere in the controller -- no-ops in legacy): the
 * select-all/row checkboxes' change handlers and the accepted-quantity
 * input's blur handler are deliberately NOT given behavior here either.
 *
 * Also NOT rebuilt: the `viewDetailsAccessoriesModal` markup (view:217-267)
 * -- confirmed unreachable, nothing in the view ever opens it (no
 * ng-click/id reference anywhere), same treatment as other confirmed-dead
 * content this project doesn't rebuild. `isDisable()` (ctrl:109-114) is
 * likewise dead: its only call site (inside `SelectAllItems`) is
 * commented out in legacy, and the view never calls it either -- omitted
 * here rather than ported unused.
 */
@Component({
  selector: 'app-asset-return-acceptance-details',
  imports: [FormsModule, RouterLink, DatePipe],
  templateUrl: './asset-return-acceptance-details.html',
  styleUrl: './asset-return-acceptance-details.scss',
})
export class AssetReturnAcceptanceDetails {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly handoff = inject(CommonDetailHandoffService);
  private readonly api = inject(AssetReturnAcceptanceDetailsService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;

  detail = signal<AradDetail>({ item: [] });
  selectAllRows = signal(false);
  statuses = signal<AradStatus[]>([]);
  rejectReasons = signal<AradRejectReason[]>([]);
  private selectedItems: AradItem[] = [];

  showRejectModal = signal(false);
  private rejectItemId: number | undefined;
  rejectQty: number | null = null;
  rejectReason = '';
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

    this.api.getStatuses().subscribe({ next: (res) => this.statuses.set(res) });
    this.api.getRejectReasons(this.unitId).subscribe({ next: (res) => this.rejectReasons.set(res) });

    const id = this.handoff.getDataId();
    this.api.getDetail(id, this.unitId).subscribe({
      next: (res) => {
        res.item.forEach((item) => {
          item.isDisabled = true;
          // Faithful port of ctrl:57-62's brace-less `if` -- only
          // `isReject` is conditional; the `rejectedQuantity` assignment
          // below runs unconditionally for every item, not just rejected
          // ones (a real legacy indentation bug, not fixed here).
          if (item.rejectQuantity) item.isReject = true;
          item.rejectedQuantity = item.rejectQuantity;
        });
        this.detail.set(res);
      },
    });
  }

  selectAllItems(): void {
    const items = this.detail().item;
    if (this.selectAllRows()) {
      // Legacy never clears `SelectedItems` on this branch (only
      // `SelectedRequestIds`, which is itself write-only and read
      // nowhere) -- repeated re-checks accumulate duplicate entries.
      // Reproduced as-is rather than "fixed" into a fresh array.
      items.forEach((areturn) => {
        areturn.isSelected = true;
        areturn.isDisabled = false;
        this.selectedItems.push(areturn);
      });
    } else {
      this.selectedItems = [];
      items.forEach((areturn) => {
        areturn.isSelected = false;
        areturn.isDisabled = true;
      });
    }
    this.detail.set({ ...this.detail() });
  }

  /** Faithful crash replication -- see class doc comment, finding 1. */
  selectItem(areturn: AradItem): void {
    if (areturn.isSelected) {
      this.selectedItems.push(areturn);
      areturn.isDisabled = false;
      this.selectAllRows.set(this.detail().item.every((i) => !!i.isSelected));
      this.detail.set({ ...this.detail() });
    } else {
      areturn.isDisabled = true;
      areturn.isReject = false;
      throw new TypeError('areturn.indexOf is not a function');
    }
  }

  validateRejection(): boolean {
    if (!this.rejectReason) {
      this.notify.error('Please Select Reason to Reject');
      return false;
    }
    return true;
  }

  /** Faithful crash replication -- see class doc comment, finding 2. */
  rejectionPopup(areturn: AradItem): void {
    if (areturn.isReject) {
      this.rejectItemId = areturn.id;
      this.rejectQty = null;
      this.rejectReason = '';
      this.rejectNote = '';
      this.showRejectModal.set(true);
    } else {
      throw new TypeError("Cannot read properties of undefined (reading 'areturn')");
    }
  }

  closeRejectModal(): void {
    this.showRejectModal.set(false);
  }

  saveAcceptReject(): void {
    if (!this.validateRejection()) return;
    const rejectItemId = this.rejectItemId;
    // Legacy uses `angular.forEach`/`.forEach` here too -- the early
    // `return` on the invalid-quantity branch only skips that one
    // iteration's callback, it does not break the loop.
    this.selectedItems.forEach((areturn) => {
      if (areturn.id !== rejectItemId) return;

      if ((this.rejectQty ?? 0) > (areturn.returnQuantity ?? 0)) {
        this.notify.error('Please Enter Recject less than or  equal to return quantity');
        return;
      } else if (this.rejectQty === areturn.returnQuantity) {
        areturn.rejectedQuantity = areturn.returnQuantity;
        areturn.acceptedQuantity = (areturn.returnQuantity ?? 0) - (areturn.rejectedQuantity ?? 0);
        areturn.rejectReasonId = this.rejectReasons()[0]?.id;
        areturn.statusId = this.setStatus('Rejected');
      } else if (this.rejectQty === 0 || !this.rejectQty) {
        areturn.acceptedQuantity = areturn.returnQuantity;
        areturn.rejectedQuantity = 0;
        areturn.rejectReasonId = this.rejectReasons()[0]?.id;
        areturn.statusId = this.setStatus('Pending');
      } else {
        areturn.rejectedQuantity = this.rejectQty;
        areturn.rejectNote = this.rejectNote;
        areturn.acceptedQuantity = (areturn.returnQuantity ?? 0) - (areturn.rejectedQuantity ?? 0);
        areturn.rejectReasonId = this.rejectReasons()[0]?.id;
        areturn.statusId = this.setStatus('Partially Approved');
      }
      this.closeRejectModal();
    });
    this.detail.set({ ...this.detail() });
  }

  /**
   * Note: whatever reason a user actually picks in the modal's dropdown
   * (`rejectReason`) is used only to gate `validateRejection()` -- the
   * value actually saved is always `rejectReasons[0].id`, the first
   * reason in the list, matching ctrl:173/178/184 exactly. Not fixed.
   */
  private setStatus(status: string): number {
    const found = this.statuses().find((s) => s.status === status);
    return found!.id;
  }

  navigateToAssetIssueReturn(): void {
    this.router.navigate(['/assetReturnAcceptance']);
  }

  validateQty(): boolean {
    let isValid = true;
    this.detail().item.forEach((asset) => {
      if (!asset.acceptedQuantity) {
        this.notify.error('Please enter accepted quantity');
        isValid = false;
      }
    });
    return isValid;
  }

  saveAssetReturnAcceptance(): void {
    if (!this.validateQty()) return;
    const id = this.detail().id as number;
    // Legacy fires one PATCH per item, all to the same URL, independently
    // (not sequenced/awaited) -- reproduced as-is, not batched into one
    // call, so this is faithfully N racing requests per Save click.
    this.detail().item.forEach((asset) => {
      const payload = {
        id,
        organizationId: this.organizationId,
        unitId: this.unitId,
        userId: this.userId,
        items: [
          {
            id: asset.id,
            acceptedQuantity: asset.acceptedQuantity,
            rejectQuantity: asset.rejectedQuantity,
            rejectNote: asset.rejectNote,
            rejectReasonId: asset.rejectReasonId,
            statusId: 4,
            accessories: [],
          },
        ],
      };
      this.loader.start();
      this.api.save(id, payload).subscribe({
        next: (res) => {
          this.loader.stop();
          this.navigateToAssetIssueReturn();
          this.notify.success(res.message ?? '');
        },
        error: () => {
          this.loader.stop();
          this.notify.error('Error');
        },
      });
    });
  }
}
