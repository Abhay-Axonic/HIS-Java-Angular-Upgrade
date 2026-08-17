import {Component, inject, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NotificationService} from '../../core/notification.service';
import {SessionService} from '../../core/session.service';
import {NursingService} from './procedure-scheduling.service';
import type {NursingRow, SavePayload} from './procedure-scheduling.models';

/** Procedure Scheduling — core scope per docs/SPEC_NURSING_OT_MODULES_2026-08-16.md: the
 * read-only worklist / status-display; the clinical entry forms carry save.
 * DATE CONVENTION: String dates via formatLegacyNow — the registration/
 * prescription convention, NOT the encounter's Long. */
@Component({
  selector: 'app-procedure-scheduling',
  imports: [FormsModule],
  templateUrl: './procedure-scheduling.html',
  styleUrl: './procedure-scheduling.scss',
})
export class NursingComponent {
  private api = inject(NursingService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;
  readonly userId = this.session.current?.userId ?? 1;

  readonly rows = signal<NursingRow[]>([]);
  readonly selected = signal<NursingRow | null>(null);
  readonly loading = signal(false);
  readonly loaded = signal(false);
  readonly saving = signal(false);
  readonly remark = signal('');

  constructor() {
    this.search();
  }

  search(): void {
    this.loading.set(true);
    this.api.search({organizationId: this.orgId, unitId: this.unitId}).subscribe({
      next: (r) => { this.loading.set(false); this.loaded.set(true);
        if (r.status === 'success') this.rows.set(r.listObject ?? []); },
      error: () => { this.loading.set(false); this.loaded.set(true); },
    });
  }

  pick(row: NursingRow): void {
    this.selected.set(row);
  }

  save(): void {
    if (!this.selected()) { this.notify.warning('Select a row first'); return; }
    this.saving.set(true);
    const now = formatLegacyNow();
    const payload: SavePayload = {
      patientId: this.selected()?.patientId ?? null,
      admissionId: this.selected()?.admissionId ?? null,
      remark: this.remark() || null,
      unitId: this.unitId,
      organizationId: this.orgId,
      createdBy: this.userId,
      createdDate: now,
      updatedBy: this.userId,
      updatedDate: now,
    };
    this.api.save(payload).subscribe({
      next: (r) => { this.saving.set(false);
        if (r.status === 'success') { this.notify.warning('Saved'); this.search(); }
        else this.notify.warning(r.message ?? 'Save failed'); },
      error: () => { this.saving.set(false); this.notify.error('Save failed'); },
    });
  }
}

/** The nursing/OT DTOs' String-date format (dd-M-yyyy HH:mm:ss) — the
 * registration/prescription convention, NOT the encounter's Long. */
function formatLegacyNow(): string {
  const d = new Date();
  const pad = (n: number): string => String(n).padStart(2, '0');
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  return `${pad(d.getDate())}-${months[d.getMonth()]}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}
