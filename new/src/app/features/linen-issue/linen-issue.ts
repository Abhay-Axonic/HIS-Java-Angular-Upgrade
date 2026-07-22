import { Component, computed, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { CommonDetailHandoffService } from '../../core/common-detail-handoff.service';
import { LinenIssueService } from './linen-issue.service';
import { LinenIssueOption, LinenIssueRow } from './linen-issue.models';

const ALL_STORE: LinenIssueOption = { store: 'All Stores' };
const ALL_STATUS: LinenIssueOption = { status: 'All' };

/**
 * Rebuild of legacy `linenIssue` (Linen & Laundry) -- see
 * linenIssueController.js (194 lines) / linen-issue.html (577 lines).
 * Real module-access resolve block, moduleId 344, no requireSuperUser,
 * self-registered state (not in app.js). controllerAs:'vm' IS load-bearing
 * here (every real binding uses vm.model.*, confirmed by grep).
 *
 * Two legacy tabs, reproduced faithfully including their real divergence
 * in how "real" each one is (see docs/ANGULAR_PENDING_FINDINGS.md, this
 * batch's entries):
 *
 * TAB 1 "Pending Issuance" -- fully real: live search against
 * /api/linenLaundry/linen/issue/search, live ng-repeat results, working
 * "Details" row action. Reproduces one confirmed legacy bug faithfully:
 * the Indent No. filter input is bound to a scope-typo (`self.model.*`
 * instead of `vm.model.*` under controllerAs:'vm') in legacy, so typing
 * into it never reaches the search payload -- NOT wired into
 * `search()`'s payload here either, on purpose, matching that bug.
 *
 * TAB 2 "Issuance List" -- NOT a real, working tab in legacy today, but
 * NOT the owner-approved dead-markup-drop case either (that's
 * advisedVisits only) -- reproduced faithfully rather than dropped:
 *  - Its own dropdown-fetch code
 *    (CONSTANTS.LINEN_LAUNDRY.LINEN_ISSUE.LINEN_ISSUE_LIST.DROPDOWN_API)
 *    is real and backend-confirmed, but is NEVER actually reachable in
 *    legacy: the tab-switch calls a bare `setScreen()` that only exists
 *    as a private controller closure, never assigned to `$scope`/`vm` --
 *    so clicking this tab throws against the view's own scope and the
 *    fetch never fires. In practice, Tab 2's From/To Store + Status
 *    selects only ever show data because they happen to share the exact
 *    same `vm.model.fromStores`/`toStores`/`statuses` arrays Tab 1's real
 *    load already populated -- reproduced here as one shared set of
 *    dropdown signals, not a separate (dead) fetch.
 *  - Its results grid was ALWAYS a hardcoded static single row in legacy
 *    (no `ng-repeat` at all) -- reproduced verbatim below, not live data.
 *  - Its search button has no `ng-click`, its "Type" select and bottom
 *    search box have no `ng-model` at all -- all left genuinely inert,
 *    matching legacy exactly (same treatment as linenIssueAcceptance's
 *    already-accepted dead Type dropdown).
 *  - "Direct Issue" IS a real, working navigation link in legacy
 *    (`href="#/linenIssueDirect"`, a separate self-registered state) --
 *    wired here too, even though `linenIssueDirect` itself isn't built in
 *    this batch (out of scope; see report).
 */
@Component({
  selector: 'app-linen-issue',
  imports: [FormsModule, DatePipe],
  templateUrl: './linen-issue.html',
  styleUrl: './linen-issue.scss',
})
export class LinenIssue {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly handoff = inject(CommonDetailHandoffService);
  private readonly api = inject(LinenIssueService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  activeTab = signal<1 | 2>(1);

  // Shared across both tabs -- see class-doc note on why this is one set,
  // not two, matching legacy's actual (buggy) observed behavior.
  fromStores = signal<LinenIssueOption[]>([ALL_STORE]);
  toStores = signal<LinenIssueOption[]>([ALL_STORE]);
  statuses = signal<LinenIssueOption[]>([ALL_STATUS]);

  fromDate = signal(formatYmd(new Date()));
  toDate = signal(formatYmd(new Date()));
  fromStore: LinenIssueOption = ALL_STORE;
  toStore: LinenIssueOption = ALL_STORE;
  status: LinenIssueOption = ALL_STATUS;

  /** Tab 1's Indent No. filter -- deliberately NOT read by search(), see class doc. */
  tab1IndentNo = '';
  /** Tab 2's Indent No./Issued Number fields -- rendered, never wired to anything (dead in legacy too). */
  tab2IndentNo = '';
  issueNo = '';

  quickFilter = signal('');

  results = signal<LinenIssueRow[]>([]);
  filteredResults = computed(() => {
    const term = this.quickFilter().trim().toLowerCase();
    if (!term) return this.results();
    return this.results().filter((row) => JSON.stringify(row).toLowerCase().includes(term));
  });

  constructor() {
    this.loadDropdowns();
  }

  selectTab(tab: 1 | 2): void {
    this.activeTab.set(tab);
    // NOTE: legacy's own tab-click handler (`setScreen()`) is a dead
    // binding here (see class doc) -- no re-fetch happens on tab switch
    // in legacy either, so none happens here.
  }

  private loadDropdowns(): void {
    this.loader.start();
    this.api.getPendingDropdowns(this.unitId, this.userId).subscribe({
      next: (res) => {
        this.fromStores.set([ALL_STORE, ...(res.fromStore ?? [])]);
        this.toStores.set([ALL_STORE, ...(res.toStore ?? [])]);
        this.statuses.set([ALL_STATUS, ...(res.status ?? [])]);
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
  }

  search(): void {
    this.loader.start();
    this.api
      .searchPending({
        fromDate: this.fromDate(),
        toDate: this.toDate(),
        fromStoreId: this.fromStore?.id || undefined,
        toStoreId: this.toStore?.id || undefined,
        // indentNo intentionally omitted -- see class doc (Tab 1 phantom binding bug)
        statusId: this.status?.id || undefined,
        unitId: this.unitId,
        userId: this.userId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          this.results.set((res ?? []).map((r) => ({ ...r, isChecked: false })));
        },
        error: () => {
          this.loader.stop();
          this.notify.error('Error');
        },
      });
  }

  openPendingDetails(row: LinenIssueRow): void {
    this.handoff.setDataId(row.id);
    this.router.navigate(['/linenIssuePendingDetails']);
  }

  openDirectIssue(): void {
    this.router.navigate(['/linenIssueDirect']);
  }
}

function formatYmd(d: Date): string {
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
}
