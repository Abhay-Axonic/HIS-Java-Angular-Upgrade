import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { ProcedureTemplateService } from './procedure-template.service';

@Component({
  selector: 'app-procedure-template',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './procedure-template.html',
  styleUrl: './procedure-template.scss',
})
export class ProcedureTemplate {
  private svc = inject(ProcedureTemplateService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  protected loading = signal(false);
  protected backendError = signal('');
  protected backendErrorRef = signal('');

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
    this.svc.configList(this.orgId(), this.unitId()).subscribe({
      next: (r) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          const n = (r.listObject ?? []).length;
          if (n === 0) {
            this.backendError.set(
              'The procedure configuration list is empty in this environment (no procedure configuration rows exist).'
            );
          } else {
            this.notify.success(`Procedure configurations loaded: ${n}`);
          }
        } else {
          this.setError(r?.message);
        }
      },
      error: (e) => this.setError(this.errorMessage(e)),
    });
  }

  protected loadTemplate(procedureConfigId: number): void {
    this.loading.set(true);
    this.svc.template(this.orgId(), this.unitId(), procedureConfigId).subscribe({
      next: (r) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.notify.success(`Template loaded for configuration ${procedureConfigId}`);
        } else {
          this.setError(r?.message);
        }
      },
      error: (e) => this.setError(this.errorMessage(e)),
    });
  }

  private setError(message: string | null | undefined): void {
    this.loading.set(false);
    const msg = message || 'Request failed';
    this.backendError.set(msg);
    const ref = msg.match(/ERR-[\w-]+/)?.[0] ?? '';
    this.backendErrorRef.set(ref);
  }

  private errorMessage(e: unknown): string {
    try {
      const raw = typeof (e as any)?.error === 'string' ? (e as any).error : JSON.stringify((e as any)?.error ?? {});
      const body = raw ? JSON.parse(raw) : {};
      return body?.message || 'Request failed';
    } catch {
      return 'Request failed';
    }
  }

  protected retry(): void {
    this.load();
  }
}
