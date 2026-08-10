import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { AccountPayableMasterService, AccountPayableRow } from './account-payable-master.service';

@Component({
  selector: 'app-account-payable-master',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './account-payable-master.html',
  styleUrl: './account-payable-master.scss',
})
export class AccountPayableMaster {
  private svc = inject(AccountPayableMasterService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected rows = signal<AccountPayableRow[]>([]);
  protected loading = signal(false);
  protected searchTerm = '';
  protected formValid = signal(true);
  protected formError = signal('');

  protected form = {
    accCode: '',
    accPayableCode: '',
    accDesc: '',
    status: 'A',
  };

  private orgId(): number {
    return Number(this.session.current?.organizationId ?? 1);
  }
  private unitId(): number {
    return Number(this.session.current?.unitId ?? 1);
  }

  constructor() {
    this.loadList();
  }

  protected loadList(): void {
    this.loading.set(true);
    this.svc.list(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.rows.set((r.listObject as AccountPayableRow[]) ?? []);
        } else {
          this.formValid.set(false);
          this.formError.set(r?.message || 'List failed');
        }
      },
      error: () => {
        this.loading.set(false);
        this.formValid.set(false);
        this.formError.set('List request failed');
      },
    });
  }

  protected filteredRows = computed(() => {
    const q = this.searchTerm.toLowerCase();
    return this.rows().filter((r) =>
      !q || r.accCode.toLowerCase().includes(q) || (r.accDesc || '').toLowerCase().includes(q)
    );
  });

  protected onSearch(): void {
    // live-filter via computed; nothing else needed
  }

  protected clearSearch(): void {
    this.searchTerm = '';
  }

  protected save(): void {
    if (!this.form.accCode || !this.form.accPayableCode) {
      this.formValid.set(false);
      this.formError.set('Account code and payable code are required.');
      return;
    }
    this.formValid.set(true);
    this.formError.set('');
    // the legacy date contract: DD-MM-YYYY HH:mm:ss (legacy JS:308 —
    // GlobalCommonDateUtils "dd-M-yyyy HH:ss:mm")
    const d = new Date();
    const pad = (n: number) => String(n).padStart(2, '0');
    const now = `${pad(d.getDate())}-${pad(d.getMonth() + 1)}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
    const payload = {
      accCode: this.form.accCode,
      accPayableCode: this.form.accPayableCode,
      accDesc: this.form.accDesc,
      status: this.form.status || 'A',
      orgId: this.orgId(),
      orgUnitId: this.unitId(),
      accPayableMstId: 0,
      createdBy: this.userId(),
      updatedBy: this.userId(),
      createdDate: now,
      updatedDate: now,
      // REQUIRED by the DAO's unconditional getDate calls (DAO:90-91):
      // omitting them NPEs at GlobalCommonDateUtils.getDate(null) — the
      // BR-187-class NPE on a billing write path, verified live (ERR-0a7cc3d9)
      accValidityFromDate: now,
      accValidityToDate: now,
    };
    this.svc.save(payload).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.notify.success('Added Successfully.');
          this.form.accCode = '';
          this.form.accPayableCode = '';
          this.form.accDesc = '';
          this.loadList();
        } else {
          this.formValid.set(false);
          this.formError.set(r?.message || 'Save failed');
        }
      },
      error: (e) => {
        let msg = 'Save failed';
        try {
          const raw = typeof e?.error === 'string' ? e.error : JSON.stringify(e?.error ?? {});
          const body = raw ? JSON.parse(raw) : {};
          msg = body?.message || msg;
        } catch { /* non-JSON */ }
        this.formValid.set(false);
        this.formError.set(msg);
      },
    });
  }

  private userId(): number {
    return Number(this.session.current?.userId ?? 1);
  }
}
