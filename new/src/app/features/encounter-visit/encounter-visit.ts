import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../core/notification.service';
import { SessionService } from '../../core/session.service';
import { EncounterVisitService } from './encounter-visit.service';
import type { ComplaintRow, EncounterPayload, IdNameRow, VitalRow } from './encounter-visit.models';

/** OPD Patient Visit / Encounter Details — the core scope per
 * docs/SPEC_ENCOUNTER_DETAILS_2026-08-16.md: patient header, vitals entry,
 * chief complaints, ICD diagnosis search+add, clinical notes, and the
 * SEQUENCED save (encounter first -> vitals with the returned encounterId). */
@Component({
  selector: 'app-encounter-visit',
  imports: [FormsModule],
  templateUrl: './encounter-visit.html',
  styleUrl: './encounter-visit.scss',
})
export class EncounterVisitComponent {
  private api = inject(EncounterVisitService);
  private session = inject(SessionService);
  private notify = inject(NotificationService);

  readonly orgId = this.session.current?.organizationId ?? 1;
  readonly unitId = this.session.current?.unitId ?? 1;
  readonly userId = this.session.current?.userId ?? 1;

  // patient header (from the loaded encounter context)
  readonly header = signal<{ uhid?: string; name?: string; age?: string; gender?: string; encounterNumber?: string }>({});
  readonly encounterId = signal<number | null>(null);
  readonly patientId = signal<number | null>(null);

  // vitals: the six core rows keyed by the vital master id
  readonly vitalMasters = signal<IdNameRow[]>([]);
  readonly vitals = signal<Record<string, string>>({});

  // complaints + diagnosis rows + notes
  readonly complaints = signal<ComplaintRow[]>([]);
  readonly diagnosisRows = signal<IdNameRow[]>([]);
  readonly clinicalNote = signal('');
  readonly icdKeyword = signal('');
  readonly icdResults = signal<IdNameRow[]>([]);
  readonly icdOpen = signal(false);

  readonly saving = signal(false);

  constructor() {
    this.api.getVitalMasterList().subscribe((r) => {
      if (r.status === 'success') this.vitalMasters.set(r.listObject ?? []);
    });
  }

  updateVital(key: string, value: string): void {
    this.vitals.update((v) => ({ ...v, [key]: value }));
  }

  updateNote(value: string): void {
    this.clinicalNote.set(value);
  }

  addComplaint(): void {
    this.complaints.update((list) => [...list, { complaint: '' }]);
  }

  removeComplaint(i: number): void {
    this.complaints.update((list) => list.filter((_, idx) => idx !== i));
  }

  updateComplaint(i: number, value: string): void {
    this.complaints.update((list) => list.map((c, idx) => (idx === i ? { ...c, complaint: value } : c)));
  }

  searchIcd(): void {
    const kw = this.icdKeyword().trim();
    if (!kw) return;
    this.api.searchDiagnosis(kw).subscribe((r) => {
      if (r.status === 'success') this.icdResults.set(r.listObject ?? []);
      this.icdOpen.set(true);
    });
  }

  addDiagnosis(row: IdNameRow): void {
    this.diagnosisRows.update((list) => [...list, row]);
    this.icdOpen.set(false);
    this.icdKeyword.set('');
  }

  removeDiagnosis(i: number): void {
    this.diagnosisRows.update((list) => list.filter((_, idx) => idx !== i));
  }

  /** The sequenced save: encounter first, then each vitals row with the
   * encounterId. Dates: encounter = Long epoch-millis (Date.now() — the
   * backend converts via getStringDateFromLongDate); vitals = String
   * (dd-M-yyyy HH:mm:ss — the AppointmentVitalMapperDto's own format). */
  save(): void {
    // validation guard — the encounter cannot be saved without its patient
    // anchor (the legacy's manualValidate pattern; the owner's polish pass).
    if (this.patientId() == null) {
      this.notify.warning('Patient is required — open the encounter from a patient/visit context first');
      return;
    }
    this.saving.set(true);
    const nowLong = Date.now();
    const nowStr = formatLegacyNow();
    const payload: EncounterPayload = {
      organizationId: this.orgId,
      unitId: this.unitId,
      patientId: this.patientId(),
      encounterId: this.encounterId(),
      encounterDate: nowLong,
      status: 'A',
      createdBy: this.userId,
      updatedBy: this.userId,
      createdDate: nowLong,
      updatedDate: nowLong,
      listComplaintDataSave: this.complaints().filter((c) => c.complaint?.trim()),
    };
    this.api.saveEncounter(payload).subscribe({
      next: (r) => {
        if (r.status === 'success') {
          const id = (r.listObject?.[0] ?? r.object)?.encounterId ?? null;
          this.encounterId.set(id);
          this.saveVitals(id, nowStr);
        } else {
          this.saving.set(false);
          this.notify.warning(r.message ?? 'Encounter save failed');
        }
      },
      error: () => {
        this.saving.set(false);
        this.notify.error('Encounter save failed');
      },
    });
  }

  private saveVitals(encounterId: number | null, nowStr: string): void {
    const rows = this.vitalMasters().filter((m) => this.vitals()[m.id]?.trim()).map((m) => ({
      vitalId: m.id,
      value: this.vitals()[m.id],
      patientId: this.patientId(),
      encounterId,
      unitId: this.unitId,
      organizationId: this.orgId,
      status: 'A',
      createdBy: this.userId,
      updatedBy: this.userId,
      createdDate: nowStr,
      updatedDate: nowStr,
    }));
    if (rows.length === 0) {
      this.saving.set(false);
      this.notify.warning('Encounter saved (no vitals entered)');
      return;
    }
    let done = 0;
    rows.forEach((v) => {
      this.api.saveVital(v as VitalRow, this.unitId, this.orgId).subscribe({
        next: (res) => {
          done += 1;
          if (res.status !== 'success') this.notify.warning(`Vital ${v.vitalId} not saved`);
          if (done === rows.length) {
            this.saving.set(false);
            this.notify.warning('Encounter and vitals saved');
          }
        },
        error: () => {
          done += 1;
          if (done === rows.length) {
            this.saving.set(false);
            this.notify.warning('Encounter saved; some vitals failed');
          }
        },
      });
    });
  }
}

/** The AppointmentVitalMapperDto's String-date format (dd-M-yyyy HH:mm:ss). */
function formatLegacyNow(): string {
  const d = new Date();
  const pad = (n: number): string => String(n).padStart(2, '0');
  const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'];
  return `${pad(d.getDate())}-${months[d.getMonth()]}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}
