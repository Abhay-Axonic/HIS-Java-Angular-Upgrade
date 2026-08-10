import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { UnitServiceMasterService } from './unit-service-master.service';

@Component({
  selector: 'app-unit-service-master',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './unit-service-master.html',
  styleUrl: './unit-service-master.scss',
})
export class UnitServiceMaster {
  private svc = inject(UnitServiceMasterService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected loading = signal(false);
  protected backendError = signal('');
  protected backendErrorRef = signal('');
  protected count = signal<number | null>(null);

  private orgId(): number {
    return Number(this.session.current?.organizationId ?? 1);
  }
  private unitId(): number {
    return Number(this.session.current?.unitId ?? 1);
  }

  constructor() {
    this.load();
  }

  protected load(): void {
    this.loading.set(true);
    this.backendError.set('');
    this.backendErrorRef.set('');
    // the count works (object: 1 — a row exists) — fetch it to prove the
    // data is real, then the list (which 501s every call)
    this.svc.count(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        if (r?.status === 'success') {
          this.count.set(typeof r.object === 'number' ? r.object : null);
        }
      },
      error: () => { /* count is auxiliary; the list is the finding */ },
    });
    this.svc.list(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.notify.success(`List loaded: ${(r.listObject ?? []).length} row(s)`);
        } else {
          // the masked-error shape: HTTP 200 + body status error (code 501).
          // The list is UNCONDITIONALLY broken by the char-field DTO defect.
          this.setError(r?.message);
        }
      },
      error: (e) => this.setError(this.errorMessage(e)),
    });
  }

  private setError(message: string | null | undefined): void {
    this.loading.set(false);
    const msg = message || 'List failed';
    this.backendError.set(msg);
    const ref = msg.match(/ERR-[\w-]+/)?.[0] ?? '';
    this.backendErrorRef.set(ref);
  }

  private errorMessage(e: unknown): string {
    try {
      const raw = typeof (e as any)?.error === 'string' ? (e as any).error : JSON.stringify((e as any)?.error ?? {});
      const body = raw ? JSON.parse(raw) : {};
      return body?.message || 'List failed';
    } catch {
      return 'List failed';
    }
  }

  protected retry(): void {
    this.load();
  }
}
