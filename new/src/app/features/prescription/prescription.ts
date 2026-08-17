import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../core/notification.service';
import { SessionService } from '../../core/session.service';
import { PrescriptionService } from './prescription.service';
import type { ItemRow, LineForm, PrescriptionLine, PrescriptionPayload } from './prescription.models';

/** OPD Prescription Details — the core scope per
 * docs/SPEC_PRESCRIPTION_DETAILS_2026-08-16.md: patient/encounter header,
 * medication search (item by name), prescription lines (medicine, dose,
 * frequency, route, duration, instruction, quantity), add multiple lines,
 * save (header + lines in one POST /api/opd/saveMedication).
 * DATE CONVENTION: String (dd-M-yyyy HH:mm:ss) via formatLegacyNow() — the
 * prescription DTO family is NOT the encounter's Long-epoch convention. */
@Component({
  selector: 'app-prescription',
  imports: [FormsModule],
  templateUrl: './prescription.html',
  styleUrl: './prescription.scss',
})
export class PrescriptionComponent {
  private api = inject(PrescriptionService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;
  readonly userId = this.session.current?.userId ?? 1;

  // encounter context (read-only header)
  readonly encounterId = signal<number | null>(null);
  readonly patientId = signal<number | null>(null);
  readonly patientName = signal('');
  readonly doctorId = signal<number | null>(null);

  // medication search
  readonly searchKeyword = signal('');
  readonly searchResults = signal<ItemRow[]>([]);
  readonly searchOpen = signal(false);

  // frequency options (the legacy mock's 1-12 values = the frequency master)
  readonly frequencyOptions = [
    { id: 1, name: 'Once a day' },
    { id: 2, name: 'Twice a day' },
    { id: 3, name: 'Thrice a day' },
    { id: 4, name: 'Four Times a day' },
    { id: 5, name: 'Before meals' },
    { id: 6, name: 'After meals' },
    { id: 7, name: 'As required' },
    { id: 8, name: 'Hourly' },
    { id: 9, name: 'Every 2 Hours' },
    { id: 10, name: 'Before Sleep' },
    { id: 11, name: 'Every 4 Hours' },
    { id: 12, name: 'Now' },
  ];

  // the lines being written
  readonly lines = signal<LineForm[]>([]);
  readonly saving = signal(false);

  constructor() {
    const ctx = this.session.current as Record<string, unknown> | null;
    const pid = ctx?.['patientId'];
    const eid = ctx?.['encounterId'];
    this.patientId.set(typeof pid === 'number' ? pid : null);
    this.encounterId.set(typeof eid === 'number' ? eid : null);
  }

  search(): void {
    const kw = this.searchKeyword().trim();
    if (!kw) return;
    this.api.searchItems(kw, this.unitId, this.orgId).subscribe((r) => {
      if (r.status === 'success') this.searchResults.set(r.listObject ?? []);
      this.searchOpen.set(true);
    });
  }

  pickItem(item: ItemRow): void {
    const id = item.itemId ?? item.id ?? null;
    const name = item.itemName ?? item.description ?? item.itemCode ?? '';
    this.lines.update((ls) => [...ls, { itemId: id, itemName: name, dose: '', doseUnitId: null, frequencyId: null, routeId: null, quantity: '', durationDays: '', instruction: '' }]);
    this.searchOpen.set(false);
    this.searchKeyword.set('');
  }

  removeLine(i: number): void {
    this.lines.update((ls) => ls.filter((_, idx) => idx !== i));
  }

  updateLine<K extends keyof LineForm>(i: number, key: K, value: unknown): void {
    this.lines.update((ls) => ls.map((l, idx) => (idx === i ? { ...l, [key]: value as LineForm[K] } : l)));
  }

  save(): void {
    const ls = this.lines();
    if (ls.length === 0) {
      this.notify.warning('Add at least one medicine');
      return;
    }
    this.saving.set(true);
    const now = formatLegacyNow();
    const details: PrescriptionLine[] = ls
      .filter((l) => l.itemId != null)
      .map((l) => ({
        itemId: l.itemId,
        dose: l.dose ? Number(l.dose) : null,
        doseUnitId: l.doseUnitId,
        frequencyId: l.frequencyId,
        routeId: l.routeId,
        orderQty: l.quantity ? Number(l.quantity) : null,
        remark: l.instruction || null,
        startDate: now,
        stopDate: addDays(now, l.durationDays),
        organizationId: this.orgId,
        unitId: this.unitId,
        addedBy: this.userId,
        addedDate: now,
        isActive: true,
        statusId: 1,
      }));
    const payload: PrescriptionPayload = {
      organizationId: this.orgId,
      unitId: this.unitId,
      encounterId: this.encounterId(),
      patientId: this.patientId(),
      doctorId: this.doctorId(),
      prescriptionDate: now,
      addedBy: this.userId,
      addedDate: now,
      updatedBy: this.userId,
      updatedDate: now,
      isActive: true,
      statusId: 1,
      isDischarge: false,
      statflag: false,
      prescriptionDetailsDtosList: details,
      normalPrescriptionDetailsDtosList: details,
    };
    this.api.save(payload).subscribe({
      next: (r) => {
        this.saving.set(false);
        if (r.status === 'success') {
          this.notify.warning('Prescription saved');
        } else {
          this.notify.warning(r.message ?? 'Prescription save failed');
        }
      },
      error: () => {
        this.saving.set(false);
        this.notify.error('Prescription save failed');
      },
    });
  }
}

/** The prescription DTO family's String date format (dd-M-yyyy HH:mm:ss) —
 * the registration-style convention, NOT the encounter's Long. */
function formatLegacyNow(): string {
  const d = new Date();
  const pad = (n: number): string => String(n).padStart(2, '0');
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  return `${pad(d.getDate())}-${months[d.getMonth()]}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}

function addDays(from: string, days: string): string | null {
  const n = Number(days);
  if (!days || Number.isNaN(n) || n <= 0) return null;
  const base = new Date(from);
  base.setDate(base.getDate() + n);
  const pad = (x: number): string => String(x).padStart(2, '0');
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  return `${pad(base.getDate())}-${months[base.getMonth()]}-${base.getFullYear()} ${pad(base.getHours())}:${pad(base.getMinutes())}:${pad(base.getSeconds())}`;
}
