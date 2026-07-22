import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { CommonDetailHandoffService } from '../../core/common-detail-handoff.service';
import { LinenIssuePendingDetailsService } from './linen-issue-pending-details.service';
import {
  LinenPendingBatch,
  LinenPendingDetailResponse,
  LinenPendingItem,
  LinenPendingStatus,
  emptyLinenPendingDetail,
} from './linen-issue-pending-details.models';

/**
 * Rebuild of legacy `linenIssuePendingDetails` -- a genuinely real,
 * distinct "pending issue detail" screen reached from `linenIssue`'s Tab 1
 * "Details" link (`linen-issue.html:247`, `materialPendingListDetails(issue)`
 * -> `commonDetailService.setDataId(id)` -> `$state.go('linenIssuePendingDetails')`;
 * mirrored on the rebuild side by `linen-issue.ts`'s existing
 * `openPendingDetails()`, which already targets this exact route/handoff
 * pair). NOT the same code lineage as `linenIssue`'s own dead "Issuance
 * List" tab (entry 97) -- that's a separate, self-contained dead tab
 * inside `linenIssue` itself; this screen shares none of its markup or
 * controller code and has its own full, real, backend-confirmed
 * GET/PATCH pair.
 *
 * Self-registered `.config($stateProvider)` in the controller's own file
 * (linenIssuePendingDetailsController.js:183-190) -- NOT in the central
 * app.js, and NOT duplicated anywhere else (confirmed via full-tree grep).
 * `$state` IS in this controller's own DI list (ctrl:4) and the
 * missing-cookie guard returns before anything else runs -- no instance
 * of the recurring missing-`$state`-DI crash bug here. The state has NO
 * resolve block at all (no `data.moduleId` on this rebuild's route either,
 * matching `linenIssueAcceptanceDetails`' own precedent) -- legacy itself
 * only reaches this screen via the already-gated `linenIssue` (moduleId
 * 344), but a user who knew the URL could reach it directly with zero
 * module check; not a rebuild gap, a faithful match to legacy's real (lack
 * of) restriction on this specific state.
 *
 * Backend confirmed real end-to-end for both endpoints this screen calls:
 * GET `/api/linenLaundry/linen/issue/{unitId}/{orgId}/{id}` and PATCH
 * `/api/linenLaundry/linen/issue`, both in `LinenIssueController.java` ->
 * `LinenIssueService.java` -> `LinenIssueDao.java`.
 *
 * **Findings, all reproduced faithfully per this project's standing
 * "parity over correctness" policy, none fixed:**
 *
 * 1. **Dead copy-pasted "Get Indent" modal + "Smart Issue" icon, dropped
 *    from this rebuild.** View lines 49-51 (`<i class="fa fa-life-ring"
 *    title="Smart Issue">`, no href/ng-click) and lines 291-638 (a ~350
 *    line `#getIndentModal` Bootstrap modal with hardcoded mock rows)
 *    are copy-paste residue from an unrelated "material pending issue"
 *    template reused verbatim across several other, unrelated screens
 *    (`views/PharmacyInventory/InPatient/details-material-pending.html`,
 *    `add-new-material-issue.html`, several asset/procurement "pending"
 *    screens). Confirmed via full-file grep: this view has NO
 *    `data-toggle="modal"`/`data-target="#getIndentModal"` anywhere, so
 *    nothing ever opens this modal -- zero live bindings, same class as
 *    `advisedVisits`' pre-approved dead-markup drop (entry 98). Dropped
 *    here on the same basis (verified zero live bindings, zero effect on
 *    the golden trace); flagged here for the owner's after-the-fact
 *    confirmation, same as `advisedVisits`' own precedent.
 *
 * 2. **`vm.model.statusId == 17` disable-guards are dead -- always
 *    false.** The Carrier `<select>` and the Issue Qty `<input>`
 *    (view:255,221) are both meant to lock once the record reaches some
 *    "completed" status (id 17), but the real field lives at
 *    `vm.model.materialIssuePendingDetail.statusId` (confirmed against
 *    `LinenResponse.java`'s real `statusId` field) -- `vm.model.statusId`
 *    itself is never assigned anywhere, so the guard is always
 *    `undefined == 17` -> false. Both controls are therefore ALWAYS
 *    enabled in legacy today, regardless of the record's real status.
 *    Reproduced by simply never wiring a disable condition on either
 *    control here -- the faithful, observable outcome.
 *
 * 3. **`RemoveRowData` always deletes the LAST batch row, not the one
 *    clicked -- confirmed parameter-passing bug, live-reachable.** The
 *    "-" button (view:228, inside `ng-repeat="batches in item.batch"`)
 *    is wired `ng-click="vm.model.RemoveRowData(item)"` -- passing the
 *    parent `item` (the linen item wrapper), NOT `batches` (the specific
 *    row being removed). Inside `RemoveRowData`, `item.batch.indexOf(item)`
 *    always returns -1 (the wrapper `item` is never itself an element of
 *    its own `.batch` array), so `item.batch.splice(-1, 1)` always
 *    removes the last batch row in the list, regardless of which row's
 *    "-" was actually clicked. Same observable shape as entry 59's
 *    dual-list-picker "wrong entry removed" bug class, but a distinct
 *    code cause (direct wrong-argument passing, not the copy-pasted
 *    `angular.forEach`/`indexOf(value)` template). Reproduced verbatim
 *    in `removeRowData()` below.
 *
 * 4. **`batchOnChange` is a confirmed no-op -- switching a row's Batch
 *    No. dropdown never refreshes that row's Expiry/Available Stock
 *    columns.** `batchOnChange(batch, item)` (ctrl:28-35, called on the
 *    per-row batch `<select>`'s `ng-change`) re-finds the row's own
 *    (already-mutated) object inside `item.batch` by its new `batchId`,
 *    then does `batch.expiry = batch.expiry` -- a pure self-assignment.
 *    Nothing ever copies the newly-selected batch's real expiry/stock
 *    data into the row being edited; only the `<select>`'s own displayed
 *    value changes, the Expiry Date/Available Stock Qty columns keep
 *    showing whichever batch the row started as. A stray `debugger`
 *    statement (ctrl:29) and a shadowed `batch` parameter both confirm
 *    this was never finished/cleaned up. Reproduced as a true no-op in
 *    `batchOnChange()` below.
 *
 * 5. **`setStatus(x)` is an unguarded `.find()` + `.id` access -- same
 *    crash-risk class as `details-asset-issue-acceptance`'s own
 *    `setStatus` (that screen's class doc comment, finding 1).**
 *    `self.model.statuses` is populated asynchronously by
 *    `StatusService.GetStatus()` (ctrl:154-156), fired at controller
 *    construction alongside (not awaited before) `fromMaterialPending()`;
 *    nothing gates the footer Save button on that load finishing.
 *    `ApprovePending()` calls `setStatus('Completed')` unconditionally
 *    once validation passes -- if `statuses` hasn't loaded yet, or has no
 *    entry whose `.status` is literally `'Completed'`, this throws.
 *    Reproduced as a direct, unguarded 1:1 port in `setStatus()` below.
 *
 * 6. **Save's response handling reads `response.data.message`
 *    unconditionally on any 2xx -- `saveLinenIssue`'s real response
 *    (`CommonResponse`) never sets its own `status` field server-side**
 *    (confirmed: `LinenIssueService.saveLinenIssue` only calls
 *    `response.setId(...)`/`response.setMessage(...)`). This rebuild's
 *    `save()` therefore does NOT gate success on a `status` field (there
 *    isn't one), matching legacy exactly.
 *
 * 7. **Decorative-only sort carets, genuinely inert in legacy too.**
 *    Every column header's `fa-caret-up`/`fa-caret-down` pair (view,
 *    throughout) has no `ng-click` in legacy, and the row list's
 *    `orderBy:sortType:sortReverse` (view:131) references two scope
 *    variables never assigned anywhere in this `controllerAs:'vm'`
 *    controller -- an always-no-op filter. Not a rebuild gap to "fix";
 *    rendered here as the same static, non-interactive icons with no
 *    sort behaviour, matching legacy's own (non-)functionality exactly.
 */
@Component({
  selector: 'app-linen-issue-pending-details',
  imports: [FormsModule, RouterLink, DatePipe],
  templateUrl: './linen-issue-pending-details.html',
  styleUrl: './linen-issue-pending-details.scss',
})
export class LinenIssuePendingDetails {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly handoff = inject(CommonDetailHandoffService);
  private readonly api = inject(LinenIssuePendingDetailsService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;
  private readonly storeId = this.session.current?.['storeId'] as number | undefined;

  detail = signal<LinenPendingDetailResponse>(emptyLinenPendingDetail());
  statuses = signal<LinenPendingStatus[]>([]);
  activeItemId = signal<number | ''>('');
  /** Mirrors legacy's overloaded `vm.model.id` (the Carrier select's own
   * `ng-model`, also gating the Save button's `ng-disabled="!vm.model.id"`
   * and the value sent as `carrierId`) -- given a saner name here since
   * the field's dual purpose is an internal legacy naming quirk, not
   * user-observable behaviour. */
  selectedCarrierId = signal<number | null>(null);
  remark = signal('');

  constructor() {
    if (!this.session.current) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }

    const pendingId = this.handoff.getDataId();
    this.api.getDetail(this.unitId, this.organizationId, pendingId).subscribe((res) => {
      for (const item of res.items) {
        for (const batch of item.batch) {
          batch.batchId = String(batch.batchId);
        }
      }
      this.detail.set(res);
    });

    this.api.getStatuses().subscribe((status) => this.statuses.set(status));
  }

  toggleItem(item: LinenPendingItem): void {
    this.activeItemId.set(this.activeItemId() === item.itemId ? '' : item.itemId);
  }

  /** Live sum of a item's batch issue quantities -- see the class doc
   * comment and `LinenPendingItem.issueQuantity`'s own comment for why
   * this replaces legacy's template-assignment `sumByColumn` filter
   * (`item.issueQuantity = (item.batch | sumByColumn:'issueQuantity')`,
   * view:143) with an equivalent computed read, not a stored field. */
  itemIssueTotal(item: LinenPendingItem): number {
    return item.batch.reduce((sum, b) => {
      const n = typeof b.issueQuantity === 'number' ? b.issueQuantity : parseFloat(String(b.issueQuantity));
      return sum + (isNaN(n) ? 0 : n);
    }, 0);
  }

  /** Faithful no-op -- see class doc comment, finding 4. Selecting a
   * different Batch No. from a row's dropdown never refreshes that row's
   * Expiry/Available Stock columns in legacy; this handler exists only so
   * the `(ngModelChange)` wiring matches legacy's `ng-change`, not because
   * it does anything. */
  batchOnChange(_batch: LinenPendingBatch, _item: LinenPendingItem): void {
    // Intentionally empty -- see finding 4 above.
  }

  addDuplicateRow(item: LinenPendingItem): void {
    const source = item.batch[0];
    if (!source) return;
    const clone: LinenPendingBatch = {
      ...source,
      expiry: '',
      batchId: '',
      issueQuantity: undefined,
      batchNo: '',
    };
    item.batch = [...item.batch, clone];
    this.detail.set({ ...this.detail() });
  }

  /** Faithful bug reproduction -- see class doc comment, finding 3. Always
   * removes the LAST batch row in `item.batch`, regardless of which row's
   * "-" button was actually clicked, matching legacy's
   * `item.batch.indexOf(item)` (-1) -> `splice(-1, 1)` bug exactly. */
  removeRowData(item: LinenPendingItem): void {
    item.batch = [...item.batch];
    item.batch.splice(item.batch.length - 1, 1);
    this.detail.set({ ...this.detail() });
  }

  /** Faithful, unguarded 1:1 port -- see class doc comment, finding 5.
   * Throws if `statuses` hasn't loaded yet or has no matching entry. */
  private setStatus(label: string): number {
    const found = this.statuses().find((s) => s.status === label);
    return found!.id;
  }

  approvePending(): void {
    if (!this.validatePending()) return;

    const items: Record<string, unknown>[] = [];
    for (const item of this.detail().items) {
      const issueQuantity = this.itemIssueTotal(item);
      if (issueQuantity > 0) {
        items.push({
          id: item.id,
          issueQuantity,
          batches: item.batch.map((b) => ({
            id: b.id,
            batchId: parseInt(b.batchId, 10),
            issueQuantity: parseInt(String(b.issueQuantity ?? ''), 10),
          })),
        });
      }
    }

    const payload = {
      id: this.handoff.getDataId(),
      statusId: this.setStatus('Completed'),
      carrierId: this.selectedCarrierId() ?? undefined,
      remark: this.remark(),
      items,
      unitId: this.unitId,
      organizationId: this.organizationId,
      userId: this.userId,
      storeId: this.storeId,
    };

    if (items.length > 0) {
      this.api.save(payload).subscribe({
        next: (res) => {
          this.notify.success(res.message ?? 'Saved');
          this.router.navigate(['/linenIssue']);
        },
        error: () => this.notify.error('Something Went Wrong'),
      });
    } else {
      this.router.navigate(['/linenIssue']);
    }
  }

  private validatePending(): boolean {
    const items = this.detail().items;
    let isValid = true;

    if (items.length < 1) {
      this.notify.error('Please validate all required fields');
      isValid = false;
    }
    for (const item of items) {
      if (this.itemIssueTotal(item) < 0) {
        this.notify.error('Please Enter Valid Issue Quantity');
        isValid = false;
      }
    }
    for (const item of items) {
      if (this.itemIssueTotal(item) > (item.pendingQuantity ?? 0)) {
        this.notify.error('Issue Quantity Should be less than Pending Quantity');
        isValid = false;
      }
    }

    return isValid;
  }
}
