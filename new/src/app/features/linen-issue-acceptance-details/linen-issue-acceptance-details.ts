import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { CommonDetailHandoffService } from '../../core/common-detail-handoff.service';
import { LinenIssueAcceptanceDetailsService } from './linen-issue-acceptance-details.service';
import { LiadDetail, LiadItem } from './linen-issue-acceptance-details.models';

/**
 * Rebuild of the legacy `linenIssueAcceptanceDetails` state -- see
 * linenIssueAcceptanceDetailsController.js (314 lines) +
 * linen-issue-acceptance-details.html (124 lines). No data.moduleId (no
 * resolve block), real cookieObject guard only. Item id comes from
 * CommonDetailHandoffService -- receiving side of the pair built
 * alongside `linenIssueacceptance` (screen 21, this same batch).
 *
 * TWO SEVERITY 1 findings, logged (docs/ANGULAR_PENDING_FINDINGS.md),
 * NOT fixed, faithfully reproduced as this screen's parity target:
 *
 * 1. `RejectionPopup(item)` (linenIssueAcceptanceDetailsController.js:101)
 *    assigns to a bare `itemId` that is never declared anywhere in the
 *    file, under this file's own "use strict" -- throws
 *    `ReferenceError: itemId is not defined` the instant a user toggles
 *    any row's reject switch, before the popup even opens. TypeScript
 *    can't express an undeclared-variable assignment, so the identical
 *    OBSERVABLE crash (throws immediately on toggle, popup never opens)
 *    is reproduced via a direct throw instead -- see `rejectionPopup()`.
 *
 * 2. This screen has NO reachable save path at all. `SaveApproval` (the
 *    real, validated save function, self.model.SaveApproval) is never
 *    wired to any element anywhere in the view -- confirmed by reading
 *    the full view plus every `ng-include`d shared partial
 *    (asset-no-popup.html, qty-accept-reject.html, note-save.html). The
 *    two buttons that look like "Save" both call BARE, lowercase
 *    `$scope` functions (`saveAcceptReject()`, `saveAcceptance()`) that
 *    this controllerAs:'vm' controller never defines on `$scope` at all
 *    -- confirmed these are copy-paste leftovers from a different,
 *    classic-$scope screen's shared partials, included here unchanged.
 *    In practice the reject-popup path is doubly unreachable (finding 1
 *    prevents it from ever opening), and the note-save modal is never
 *    triggered by anything on this screen either. Net effect: a user
 *    can select rows and edit quantities, but there is no way to
 *    actually complete an acceptance on this screen. Not reproduced as
 *    dead modal markup (confirmed unreachable, same treatment as other
 *    confirmed-dead content this project doesn't rebuild) -- the
 *    checkbox/quantity interactions are built faithfully; no working
 *    Save control is rendered, matching legacy's real, if surely
 *    unintended, gap.
 */
@Component({
  selector: 'app-linen-issue-acceptance-details',
  imports: [FormsModule, RouterLink, DatePipe],
  templateUrl: './linen-issue-acceptance-details.html',
  styleUrl: './linen-issue-acceptance-details.scss',
})
export class LinenIssueAcceptanceDetails {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly handoff = inject(CommonDetailHandoffService);
  private readonly api = inject(LinenIssueAcceptanceDetailsService);

  detail = signal<LiadDetail>({ items: [] });
  selectAllRows = signal(false);
  private selectedItems: LiadItem[] = [];

  constructor() {
    const cookieObject = this.session.current;
    if (cookieObject == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }

    const id = this.handoff.getDataId();
    this.api.getDetail(id).subscribe({
      next: (res) => {
        res.items.forEach((item) => (item.isDisabled = true));
        this.detail.set(res);
      },
    });
  }

  selectAllItems(): void {
    const items = this.detail().items;
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

  selectItem(item: LiadItem): void {
    if (item.isSelected) {
      this.selectedItems.push(item);
      item.isDisabled = false;
    } else {
      item.isDisabled = true;
      item.isReject = false;
      const index = this.selectedItems.indexOf(item);
      if (index > -1) this.selectedItems.splice(index, 1);
    }
    this.selectAllRows.set(this.detail().items.every((i) => i.isSelected));
    this.detail.set({ ...this.detail() });
  }

  valueOnChange(item: LiadItem | undefined, isSelectAllToggle: boolean): void {
    if (isSelectAllToggle) {
      this.detail().items.forEach((i) => {
        i.acceptedQuantity = this.selectAllRows() ? i.quantity : 0;
      });
    } else if (item) {
      item.acceptedQuantity = item.isSelected ? item.quantity : 0;
    }
    this.detail.set({ ...this.detail() });
  }

  approvedQuantity(item: LiadItem): void {
    const accepted = item.acceptedQuantity ?? 0;
    const issue = item.quantity ?? 0;
    if (accepted > issue) {
      item.rejectQuantity = 0;
      item.isReject = false;
    } else if (accepted === 0) {
      item.rejectQuantity = issue;
      item.isReject = true;
    } else if (accepted < issue) {
      item.rejectQuantity = issue - accepted;
      item.isReject = true;
    } else {
      item.rejectQuantity = issue - accepted;
      item.isReject = false;
    }
    this.detail.set({ ...this.detail() });
  }

  isDisable(item: LiadItem): boolean {
    return false;
  }

  /** Faithful crash replication -- see class doc comment, finding 1. */
  rejectionPopup(item: LiadItem): void {
    if (item.isReject) {
      throw new ReferenceError('itemId is not defined');
    }
  }
}
