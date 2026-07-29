import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Router } from '@angular/router';
import { CommonDetailHandoffService } from '../../core/common-detail-handoff.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { SessionService } from '../../core/session.service';
import { AssetIssueDropdowns, AssetIssueIndentService } from './asset-issue-indent.service';

function ymd(date = new Date()): string {
  return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`;
}

@Component({
  selector: 'app-asset-issue-indent',
  imports: [FormsModule],
  templateUrl: './asset-issue-indent.html',
  styleUrl: './asset-issue-indent.scss',
})
export class AssetIssueIndent {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly handoff = inject(CommonDetailHandoffService);
  private readonly api = inject(AssetIssueIndentService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  activeTab = signal<'pending' | 'issued'>('pending');
  dropdowns = signal<AssetIssueDropdowns>({});
  rows = signal<Record<string, unknown>[]>([]);
  fromDate = signal(ymd());
  toDate = signal(ymd());
  fromStoreId = '';
  toStoreId = '';
  statusId = '';
  indentTypeId = '';
  indentNo = '';

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.loadDropdowns();
  }

  private loadDropdowns(): void {
    this.api.dropdowns(this.unitId, this.organizationId, this.userId).subscribe({
      next: (res) => {
        this.dropdowns.set(res);
        this.search();
      },
    });
  }

  switchTab(tab: 'pending' | 'issued'): void {
    this.activeTab.set(tab);
    this.search();
  }

  search(): void {
    this.loader.start();
    const commonPayload = {
      fromDate: this.fromDate(),
      toDate: this.toDate(),
      unitId: this.unitId,
      fromStoreId: this.fromStoreId || undefined,
      toStoreId: this.toStoreId || undefined,
      statusId: this.statusId || undefined,
      indentTypeId: this.indentTypeId || undefined,
    };
    const payload =
      this.activeTab() === 'pending'
        ? { ...commonPayload, indentNo: this.indentNo || undefined }
        : { ...commonPayload, indentNumber: this.indentNo || undefined };
    const call = this.activeTab() === 'pending' ? this.api.pendingSearch(payload) : this.api.issuedSearch(payload);
    call.subscribe({
      next: (rows) => {
        this.rows.set(rows ?? []);
        this.loader.stop();
      },
      error: () => {
        this.rows.set([]);
        this.loader.stop();
      },
    });
  }

  details(row: Record<string, unknown>): void {
    this.handoff.setDataId((row['id'] ?? row['indentId']) as number);
    this.notify.warning(this.activeTab() === 'pending' ? 'Details Asset Issue Indent is not rebuilt yet' : 'Details Asset Issue List is not rebuilt yet');
  }

  value(row: Record<string, unknown>, keys: string[]): unknown {
    return keys.map((k) => row[k]).find((v) => v != null && v !== '') ?? '-';
  }
}
