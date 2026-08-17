import {Component, inject, signal} from '@angular/core';
import {FormsModule} from '@angular/forms';
import {NotificationService} from '../../core/notification.service';
import {SessionService} from '../../core/session.service';
import {PharmacyService} from './pharmacology-validaion-worklist.service';
import type {IdNameRow, PharmacyRow, SavePayload} from './pharmacology-validaion-worklist.models';

/** Pharmacology Validation Worklist — core scope per docs/SPEC_PHARMACY_MODULE_2026-08-16.md: the
 * worklist grid + the save form (no stock-level validation in the first
 * build). DATE CONVENTION: String dates via formatLegacyNow — the
 * registration/prescription convention, NOT the encounter's Long. */
@Component({
  selector: 'app-pharmacology-validaion-worklist',
  imports: [FormsModule],
  templateUrl: './pharmacology-validaion-worklist.html',
  styleUrl: './pharmacology-validaion-worklist.scss',
})
export class PharmacyComponent {
  private api = inject(PharmacyService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;
  readonly userId = this.session.current?.userId ?? 1;

  readonly dropdowns = signal<IdNameRow[]>([]);
  readonly rows = signal<PharmacyRow[]>([]);
  readonly selected = signal<PharmacyRow | null>(null);
  readonly loading = signal(false);
  readonly loaded = signal(false);
  readonly saving = signal(false);
  readonly dose = signal('');
  readonly reason = signal('');

  constructor() {
    this.api.dropdowns(this.orgId, this.unitId).subscribe((r) => {
      if (r.status === 'success') this.dropdowns.set(r.listObject ?? []);
    });
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

  pick(row: PharmacyRow): void {
    this.selected.set(row);
    this.dose.set(row.dose ?? '');
  }

  save(): void {
    if (!this.selected()) { this.notify.warning('Select a row first'); return; }
    this.saving.set(true);
    const now = formatLegacyNow();
    const payload: SavePayload = {
      patientId: this.selected()?.patientId ?? null,
      orderDetailId: this.selected()?.orderDetailId ?? null,
      itemId: this.selected()?.itemId ?? null,
      dose: this.dose() || null,
      reason: this.reason() || null,
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

/** The pharmacy DTOs' String-date format (dd-M-yyyy HH:mm:ss) — the
 * registration/prescription convention, NOT the encounter's Long. */
function formatLegacyNow(): string {
  const d = new Date();
  const pad = (n: number): string => String(n).padStart(2, '0');
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  return `${pad(d.getDate())}-${months[d.getMonth()]}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}
