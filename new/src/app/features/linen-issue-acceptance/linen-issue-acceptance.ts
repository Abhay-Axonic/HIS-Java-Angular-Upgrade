import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { CommonDetailHandoffService } from '../../core/common-detail-handoff.service';
import { LinenIssueAcceptanceService } from './linen-issue-acceptance.service';
import { LiaOption, LiaRow } from './linen-issue-acceptance.models';

const ALL_OPTION: LiaOption = { id: 0 };

/**
 * Rebuild of the legacy `linenIssueacceptance` state -- see
 * linenIssueAcceptanceController.js (153 lines). Real module-access
 * resolve block (moduleId 96). Sending side of the
 * CommonDetailHandoffService pair with `linenIssueAcceptanceDetails`
 * (screen 20, this same batch, built alongside this one).
 *
 * NOT reproduced: the search form's "Type" dropdown
 * (linen-issue-acceptance.html:100-104, static Auto/Manual options) has
 * no `ng-model` at all -- confirmed dead, never affects the search
 * payload. Same treatment as other confirmed-dead controls this project
 * has already chosen not to wire (e.g. MISReports' disabled params).
 * The row-level "Print" link (line 229) has no `ng-click` either -- also
 * not wired, same reasoning.
 */
@Component({
  selector: 'app-linen-issue-acceptance',
  imports: [FormsModule, DatePipe],
  templateUrl: './linen-issue-acceptance.html',
  styleUrl: './linen-issue-acceptance.scss',
})
export class LinenIssueAcceptance {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly handoff = inject(CommonDetailHandoffService);
  private readonly api = inject(LinenIssueAcceptanceService);

  private unitId = 0;
  private userId = 0;

  fromDate = signal(formatYmd(new Date()));
  toDate = signal(formatYmd(new Date()));
  fromStores = signal<LiaOption[]>([ALL_OPTION]);
  toStores = signal<LiaOption[]>([ALL_OPTION]);
  statuses = signal<LiaOption[]>([ALL_OPTION]);

  fromStore: LiaOption = ALL_OPTION;
  toStore: LiaOption = ALL_OPTION;
  status: LiaOption = ALL_OPTION;
  indentNo = '';
  searchText = '';

  results = signal<LiaRow[]>([]);

  constructor() {
    const cookieObject = this.session.current;
    if (cookieObject == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.unitId = this.session.unitId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.getDropdowns();
    this.search();
  }

  private getDropdowns(): void {
    this.loader.start();
    this.api.getDropdowns(this.unitId, this.userId).subscribe({
      next: (res) => {
        this.fromStores.set([ALL_OPTION, ...(res.fromStore ?? [])]);
        this.toStores.set([ALL_OPTION, ...(res.toStore ?? [])]);
        this.statuses.set([ALL_OPTION, ...(res.status ?? [])]);
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
  }

  search(): void {
    this.loader.start();
    this.api
      .search({
        fromDate: this.fromDate(),
        toDate: this.toDate(),
        fromStoreId: this.fromStore?.id || undefined,
        toStoreId: this.toStore?.id || undefined,
        indentNo: this.indentNo || undefined,
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

  openDetails(item: LiaRow): void {
    this.handoff.setDataId(item.id);
    this.router.navigate(['/linenIssueAcceptanceDetails']);
  }
}

function formatYmd(d: Date): string {
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
}
