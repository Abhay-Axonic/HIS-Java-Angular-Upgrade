import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { NotesMasterService } from './notes-master.service';

@Component({
  selector: 'app-notes-master',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './notes-master.html',
  styleUrl: './notes-master.scss',
})
export class NotesMaster {
  private svc = inject(NotesMasterService);
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
    this.loadList();
  }

  protected loadList(): void {
    this.loading.set(true);
    this.backendError.set('');
    this.backendErrorRef.set('');
    this.svc.list(this.orgId(), this.unitId(), 0, 100).subscribe({
      next: (r) => {
        this.loading.set(false);
        if (r?.status === 'success') {
          this.notify.success(`List loaded: ${(r.listObject ?? []).length} row(s)`);
        } else {
          // the phantom-broken list: HTTP 200 with an error envelope, or a
          // correlation-id 500 — surface the defect honestly (screen-17
          // pattern), never fake a green grid.
          this.backendError.set(r?.message || 'List failed');
          const ref = r?.message?.match(/ERR-[\w-]+/)?.[0] ?? '';
          this.backendErrorRef.set(ref);
        }
      },
      error: (e) => {
        this.loading.set(false);
        let msg = 'List request failed';
        let ref = '';
        try {
          // Angular parses JSON error bodies into e.error (an object);
          // a raw string body needs JSON.parse. Handle both.
          const raw = typeof e?.error === 'string' ? e.error : (e?.error ? JSON.stringify(e.error) : '');
          const body = raw ? JSON.parse(raw) : {};
          if (body?.message) {
            msg = body.message;
            ref = body.message.match(/ERR-[\w-]+/)?.[0] ?? '';
          }
        } catch {
          /* non-JSON error body */
        }
        this.backendError.set(msg);
        this.backendErrorRef.set(ref);
      },
    });
  }

  protected retry(): void {
    this.loadList();
  }
}
