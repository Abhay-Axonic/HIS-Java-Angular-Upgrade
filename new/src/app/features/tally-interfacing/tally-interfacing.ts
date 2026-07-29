import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { TallyInterfacingService } from './tally-interfacing.service';
import { HisLedgerName, TallyCommResult, TallyLedgerRow, TallyTabId, TallyUnitOption } from './tally-interfacing.models';

/** 33-item hardcoded "Under Group" list, copied verbatim (same order) from
 * `tallyInterfacingController.js`'s `$scope.underGroupList.push(...)`
 * chain -- not a real Tally-fetched list, an unconditional constant in
 * legacy, faithfully reproduced including its exact wording/casing/order
 * (e.g. "Cash-in-hand", "Misc. Expenses (ASSET)"). Live-confirmed against
 * the real, unmodified legacy screen this session. */
const UNDER_GROUP_LIST: readonly string[] = [
  'Bank Accounts',
  'Bank OCC A/c',
  'Bank OD A/c',
  'Branch / Divisions',
  'Capital Account',
  'Cash-in-hand',
  'Current Assets',
  'Current Liabilities',
  'Deposits (Asset)',
  'Direct Expenses',
  'Direct Incomes',
  'Duties & Taxes',
  'Expenses (Direct)',
  'Expenses (Indirect)',
  'Fixed Assets',
  'Income (Direct)',
  'Income (Indirect)',
  'Indirect Expenses',
  'Indirect Incomes',
  'Investments',
  'Loan & Advances (Asset)',
  'Loans (Liability)',
  'Misc. Expenses (ASSET)',
  'Provisions',
  'Purchase Accounts',
  'Reserves & Surplus',
  'Retained Earnings',
  'Sales Accounts',
  'Secured Loans',
  'Sundry Creditors',
  'Sundry Debtors',
  'Suspense A/c',
  'Unsecured Loans',
];

function todayIso(): string {
  const d = new Date();
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const dd = String(d.getDate()).padStart(2, '0');
  return `${d.getFullYear()}-${mm}-${dd}`;
}

/**
 * `tallyInterfacing` -- accounting-ledger export/transfer screen to the
 * external Tally application. Legacy: `tallyInterfacingController.js` +
 * `views/master/UnitMaster/tally-interfacing.html`. No `data.moduleId`
 * resolve block on legacy's own `.state("tallyInterfacing", ...)` (confirmed
 * directly, `resources/assets/js/config/app.js`) -- no route guard needed.
 *
 * This screen's external Tally-software integration is never reachable in
 * this dev environment by design (nothing listens on the configured
 * IP:port in any real deployment sense) -- every Tally-communication call
 * site below is built to gracefully surface "Tally Software is not
 * Running!" on failure, matching legacy exactly, not to make the
 * integration actually succeed.
 *
 * See docs/ANGULAR_PENDING_FINDINGS.md for every audited discrepancy this
 * screen carries forward faithfully (search
 * "PENDING-tallyinterfacing-").
 */
@Component({
  selector: 'app-tally-interfacing',
  imports: [FormsModule],
  templateUrl: './tally-interfacing.html',
  styleUrl: './tally-interfacing.scss',
})
export class TallyInterfacing {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(TallyInterfacingService);

  private readonly organizationId = this.session.organizationId ?? 0;

  readonly underGroupList = UNDER_GROUP_LIST;

  // ---- top filter bar state ----
  unitList = signal<TallyUnitOption[]>([]);
  selectedUnitId = signal<string>('');
  searchDate = signal<string>(todayIso());

  // ---- tabs + per-tab data (legacy: opTallyDataList=OPD, tallyDataList=IPD,
  // invTallyDataList=Inventory, docShareTallyDataList=Doctor's Share) ----
  activeTab = signal<TallyTabId>(1);
  opList = signal<TallyLedgerRow[]>([]);
  ipList = signal<TallyLedgerRow[]>([]);
  invList = signal<TallyLedgerRow[]>([]);
  docShareList = signal<TallyLedgerRow[]>([]);

  activeList = computed<TallyLedgerRow[]>(() => {
    switch (this.activeTab()) {
      case 1:
        return this.opList();
      case 2:
        return this.ipList();
      case 3:
        return this.invList();
      case 4:
        return this.docShareList();
      default:
        return [];
    }
  });

  /** Narration is only bound for OPD/IPD tabs, faithfully reproducing
   * legacy's own field-name-mismatch bug on Inventory/Doctor's Share (see
   * docs/ANGULAR_PENDING_FINDINGS.md,
   * PENDING-tallyinterfacing-narration-blank-inventory-doctorshare). */
  activeTabShowsNarration = computed(() => this.activeTab() === 1 || this.activeTab() === 2);

  // ---- ledger-name / Tally-transfer chain state ----
  private hisLedgerNames: HisLedgerName[] = [];
  tallyLedgerNames = signal<string[]>([]);
  createThisLedger = signal('');
  selectedUnderGroup = signal('');
  selectedTallyLedgerName = signal('');

  // ---- Tally communication keys (never rendered, only used to decide
  // direct-post vs. backend-relay -- legacy: $scope.tallyIp/tallyPortNo/isTallyLocalHost) ----
  private tallyIp = 'http://127.0.0.1';
  private tallyPortNo = '9000';
  private isTallyLocalHost: string | null = 'Y';

  // ---- modal visibility ----
  showTallyLedgerModal = signal(false);
  showConfirmCreateModal = signal(false);
  showCreateLedgerModal = signal(false);

  constructor() {
    this.loadUnits();
    // Legacy calls `$scope.getAllTallyData()` unconditionally right after
    // defining it (tallyInterfacingController.js:303), not gated behind
    // the Search button -- this fires the "Please Select Unit" warning
    // immediately on every navigation to this screen, live-confirmed this
    // session. Faithfully reproduced, not corrected (see
    // docs/ANGULAR_PENDING_FINDINGS.md,
    // PENDING-tallyinterfacing-onload-autosearch-warning).
    this.search();
  }

  private loadUnits(): void {
    this.api.getUnitsByOrganization(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.unitList.set(res.listObject ?? []);
        }
      },
    });
  }

  onUnitChange(): void {
    const unitId = this.selectedUnitId();
    if (!unitId) return;
    this.api.getTallyInterfaceCommunicationKeys(parseInt(unitId, 10), this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          const obj = res.object;
          if (obj?.isTallyLocalHost !== undefined && obj?.isTallyLocalHost !== null && obj.isTallyLocalHost === 'N') {
            this.tallyIp = obj.tallyIp ?? 'http://127.0.0.1';
            this.tallyPortNo = obj.tallyPortNo ?? '9000';
            this.isTallyLocalHost = obj.isTallyLocalHost ?? 'N';
          } else {
            this.tallyIp = 'http://127.0.0.1';
            this.tallyPortNo = '9000';
            this.isTallyLocalHost = 'Y';
          }
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Something went wrong!!');
        }
      },
    });
  }

  setTab(tab: TallyTabId): void {
    this.activeTab.set(tab);
  }

  /** Legacy: `getAllTallyData()`, bound to the Search button AND fired
   * unconditionally at controller init (see constructor comment above). */
  search(): void {
    const unitId = this.selectedUnitId();
    const searchDate = this.searchDate();
    if (!unitId) {
      this.notify.warning('Please Select Unit');
      return;
    }
    this.api.getAllLedgersNameFromHis(unitId, searchDate).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.hisLedgerNames = res.listObject ?? [];
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Something went wrong!!');
        }
      },
    });
    this.api.getIpLedger(unitId, searchDate).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          const list = res.listObject ?? [];
          this.ipList.set(list);
          if (list.length === 0) this.notify.warning('Data not found for IP');
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Something went wrong!!');
        }
      },
    });
    this.api.getOpLedger(unitId, searchDate).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          const list = res.listObject ?? [];
          this.opList.set(list);
          if (list.length === 0) this.notify.warning('Data not found for OP');
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Something went wrong!!');
        }
      },
    });
    this.api.getInventoryLedger(unitId, searchDate).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          const list = res.listObject ?? [];
          this.invList.set(list);
          if (list.length === 0) this.notify.warning('Data not found for Inventory');
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Something went wrong!!');
        }
      },
    });
    this.api.getDoctorShareLedger(unitId, searchDate).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          const list = res.listObject ?? [];
          this.docShareList.set(list);
          if (list.length === 0) this.notify.warning('Data not found for Doctor Share');
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Something went wrong!!');
        }
      },
    });
  }

  // ---------------------------------------------------------------------
  // Export .xml to local disk (legacy: exportTallyToXml -> exportTallyData
  // -> download). Not gated on the active tab's row-count being non-zero --
  // legacy's own `($scope.getTallyIntegrationData)==null||(...)==[]` guard
  // is dead code (`arr == []` is never true in JS), so an empty active tab
  // still posts an empty array to the backend, which is what actually
  // decides "no data" (returns status:'error', message:"No Data Found").
  // Faithfully reproduced rather than adding a client-side short-circuit
  // that legacy doesn't really have.
  // ---------------------------------------------------------------------
  exportTallyToXml(): void {
    const rows = this.activeList();
    this.api.exportTallyData(rows).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.object) {
          this.downloadXml(res.object);
          this.notify.success('Data Exported Successfully!');
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Something went wrong!!');
        }
      },
    });
  }

  private downloadXml(xml: string): void {
    const name = `Tally - ${this.searchDate()}.xml`;
    const blob = new Blob([xml], { type: 'application/xml' });
    const url = URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    a.download = name;
    a.click();
    URL.revokeObjectURL(url);
  }

  // ---------------------------------------------------------------------
  // Transfer to Tally (legacy: transferTallyToXml -> transferTallyData ->
  // sendToHost). Reached directly when called from `verifyLedgerNames`'s
  // success branch (the real Transfer-button flow, below); also callable
  // on its own exactly like legacy exposes both entry points.
  // ---------------------------------------------------------------------
  private transferTallyToXml(): void {
    const rows = this.activeList();
    this.api.sendTallyData(rows).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.object) {
          this.sendToHost(res.object, 'Data Sent to Tally!!');
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Something went wrong!!');
        }
      },
    });
  }

  /** Shared isTallyLocalHost branch used by every Tally-communication call
   * site (legacy repeats this exact 3-outcome shape 4 times: `sendToHost`,
   * `sendRequstToTally`, `sendCreateLedgerRequest`,
   * `sendRefreshTallyLedgerNameList`). */
  private sendToTally(xml: string): { subscribe: (cb: (r: TallyCommResult) => void) => void } {
    const isTallyLocalHost = this.isTallyLocalHost;
    const tallyIp = this.tallyIp;
    const tallyPortNo = this.tallyPortNo;
    const api = this.api;
    return {
      subscribe(cb: (r: TallyCommResult) => void) {
        if (isTallyLocalHost !== undefined && isTallyLocalHost !== null && isTallyLocalHost === 'Y') {
          const url = `${tallyIp}:${tallyPortNo}`;
          api.sendXmlDirectToTally(url, xml).subscribe({
            next: (data) => cb({ kind: 'success', data: data ?? '' }),
            error: () => cb({ kind: 'notRunning' }),
          });
        } else {
          api.sendRequestToTally(tallyIp, tallyPortNo, xml).subscribe({
            next: (res) => {
              const code = parseInt(res.code ?? '', 10);
              if (code === 200) {
                cb({ kind: 'success', data: (res.object as string) ?? '' });
              } else if (code === 404) {
                cb({ kind: 'notRunning' });
              } else {
                cb({ kind: 'serverError', message: res.message ?? 'Something went wrong!!' });
              }
            },
          });
        }
      },
    };
  }

  private handleTallyCommResult(result: TallyCommResult, successMessage: string | null, onSuccess: (data: string) => void): void {
    if (result.kind === 'success') {
      if (successMessage) this.notify.success(successMessage);
      onSuccess(result.data);
    } else if (result.kind === 'notRunning') {
      this.notify.error('Tally Software is not Running!');
    } else {
      this.notify.error(result.message);
    }
  }

  private sendToHost(xml: string, successMessage: string): void {
    this.sendToTally(xml).subscribe((result) => this.handleTallyCommResult(result, successMessage, () => {}));
  }

  // ---------------------------------------------------------------------
  // Transfer button flow (legacy: getAllLedgerNamesFromHisForTallyTxn ->
  // sendLedgerNameRequest -> sendRequstToTally -> verifyLedgerNames ->
  // transferTallyToXml). This is the primary user-facing "send this tab's
  // ledger rows to Tally" action; it always tries to first reconcile HIS
  // ledger names against Tally's own ledger list before actually sending
  // the accounting entries.
  // ---------------------------------------------------------------------
  transfer(): void {
    const unitId = this.selectedUnitId();
    const searchDate = this.searchDate();
    if (!unitId) {
      this.notify.warning('Please Select Unit');
      return;
    }
    this.api.getAllLedgersNameFromHis(unitId, searchDate).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.hisLedgerNames = res.listObject ?? [];
          this.sendLedgerNameRequest();
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Something went wrong!!');
        }
      },
    });
  }

  private sendLedgerNameRequest(): void {
    const unitId = this.selectedUnitId();
    const searchDate = this.searchDate();
    if (!unitId) {
      this.notify.warning('Please Select Unit');
      return;
    }
    this.api.sendLedgerNameRequest(unitId, searchDate).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.object) {
          this.sendToTally(res.object).subscribe((result) =>
            this.handleTallyCommResult(result, 'Request Sent to Tally!!', (data) => this.verifyLedgerNames(data)),
          );
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Something went wrong!!');
        }
      },
    });
  }

  private verifyLedgerNames(ledgerNameFromTally: string): void {
    this.api.verifyLedgerNames(ledgerNameFromTally, this.hisLedgerNames).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.tallyLedgerNames.set(res.listObject ?? []);
          this.transferTallyToXml();
        } else if (res.message === 'Ledger Not Found In Tally') {
          // Legacy: opens the confirm-create modal with the missing
          // ledger's name pre-filled. Any OTHER error/status is a silent
          // no-op in legacy (no `else` branch at all) -- faithfully
          // reproduced, see docs/ANGULAR_PENDING_FINDINGS.md,
          // PENDING-tallyinterfacing-verifyledgernames-silent-noop-unexpected-error.
          this.tallyLedgerNames.set(res.listObject ?? []);
          this.createThisLedger.set((res.object as string) ?? '');
          this.showConfirmCreateModal.set(true);
        }
      },
    });
  }

  // ---------------------------------------------------------------------
  // Confirm-create-ledger modal chain
  // ---------------------------------------------------------------------
  confirmCreateYes(): void {
    this.showConfirmCreateModal.set(false);
    this.showCreateLedgerModal.set(true);
  }

  confirmCreateNo(): void {
    this.showConfirmCreateModal.set(false);
  }

  createLedgerInTally(): void {
    this.api.sendCreateLedgerRequest(this.createThisLedger(), this.selectedUnderGroup()).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.object) {
          this.sendToTally(res.object).subscribe((result) =>
            this.handleTallyCommResult(result, 'Ledger Created Successfully!!', () => this.sendRefreshLedgerNameRequest()),
          );
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Something went wrong!!');
        }
      },
    });
  }

  // ---------------------------------------------------------------------
  // Tally Ledger picker modal + Refresh
  // ---------------------------------------------------------------------
  openTallyLedgerModal(): void {
    this.showTallyLedgerModal.set(true);
  }

  closeTallyLedgerModal(): void {
    this.showTallyLedgerModal.set(false);
  }

  openCreateLedgerModal(): void {
    this.showCreateLedgerModal.set(true);
  }

  closeCreateLedgerModal(): void {
    this.showCreateLedgerModal.set(false);
  }

  /** Legacy's own `sendRefreshLedgerNameRequest()` posts only `searchDate`
   * (no `unitId`) -- faithfully reproduced via
   * `sendLedgerNameRequestForRefresh`; harmless in practice since the
   * backend implementation of this endpoint ignores both fields. */
  sendRefreshLedgerNameRequest(): void {
    this.api.sendLedgerNameRequestForRefresh(this.searchDate()).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.object) {
          this.sendToTally(res.object).subscribe((result) =>
            this.handleTallyCommResult(result, null, (data) => this.getRefreshTallyLedgerNameList(data)),
          );
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Something went wrong!!');
        }
      },
    });
  }

  private getRefreshTallyLedgerNameList(ledgerNameFromTally: string): void {
    this.api.refreshTallyLedgerNameList(ledgerNameFromTally).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.tallyLedgerNames.set(res.listObject ?? []);
          this.notify.success('Ledger List Refresh Succeed!!');
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Something went wrong!!');
        }
      },
    });
  }
}
