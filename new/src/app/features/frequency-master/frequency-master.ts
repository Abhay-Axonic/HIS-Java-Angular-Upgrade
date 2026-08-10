import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { FrequencyMasterService } from './frequency-master.service';
import { FrequencyRow } from './frequency-master.models';

@Component({
  selector: 'app-frequency-master',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './frequency-master.html',
  styleUrl: './frequency-master.scss',
})
export class FrequencyMaster {
  private session = inject(SessionService);
  private notify = inject(NotificationService);
  private service = inject(FrequencyMasterService);

  rows = signal<FrequencyRow[]>([]);
  code = signal('');
  description = signal('');
  /** Administration times, one per frequency item — the legacy contract
   * derives noOfAdministrationTime from this list's length and sends the
   * list as `items`; the backend REQUIRES items (NPE otherwise). */
  times = signal<string[]>(['']);
  editingId = signal<number | null>(null);
  saving = signal(false);

  private orgId = this.session.organizationId ?? 1;
  private unitId = this.session.unitId ?? 1;

  searchTerm = signal('');
  filteredRows = computed(() => {
    const q = this.searchTerm().toLowerCase();
    if (!q) return this.rows();
    return this.rows().filter(
      (r) =>
        (r.code || '').toLowerCase().includes(q) ||
        (r.description || '').toLowerCase().includes(q)
    );
  });

  constructor() {
    this.loadList();
  }

  loadList() {
    this.service.list(this.orgId).subscribe({
      next: (res) => {
        if (Array.isArray(res)) this.rows.set(res);
        else this.notify.error(`Frequency list load failed: unexpected envelope`);
      },
      error: (e) => this.notify.error(`Frequency list load failed: ${e.message ?? e}`),
    });
  }

  addTime() {
    this.times.set([...this.times(), '']);
  }

  removeTime(i: number) {
    const t = this.times().filter((_, idx) => idx !== i);
    this.times.set(t.length ? t : ['']);
  }

  setTime(i: number, v: string) {
    const t = [...this.times()];
    t[i] = v;
    this.times.set(t);
  }

  edit(row: FrequencyRow) {
    // Honesty obligation (backend-defect merge rule): the byId endpoint
    // 500s in dev (phantom public.t_translated_frequency — see the service
    // JSDoc). We CALL it so the failure surfaces to the user, populate the
    // form from the loaded list row so the screen stays usable, and report
    // the backend defect in the toast.
    this.service.byId(this.orgId, row.id).subscribe({
      next: (res) => {
        if (res && res.code !== undefined) {
          this.code.set(res.code);
          this.description.set(res.description);
          const items = res.items ?? [];
          this.times.set(items.length ? items.map((it) => it.administrationTime) : ['']);
        }
        this.editingId.set(row.id);
      },
      error: (e) => {
        // HTTP 500 HIS-1001 — backend defect: the byId sub-query hits the
        // missing public.t_translated_frequency table. Named, not silent.
        const detail = e?.error?.message ?? e?.message ?? `${e}`;
        this.notify.error(`Edit failed to load record: ${detail}. Backend defect: byId queries the missing table public.t_translated_frequency. Editing from the list data instead.`);
        this.code.set(row.code);
        this.description.set(row.description);
        this.times.set(
          (row.items ?? []).length ? (row.items ?? []).map((it) => it.administrationTime) : ['']
        );
        this.editingId.set(row.id);
      },
    });
  }

  clearForm() {
    this.code.set('');
    this.description.set('');
    this.times.set(['']);
    this.editingId.set(null);
  }

  /**
   * Save/Update — HONEST. The save endpoint is LIVE-VERIFIED (201 with the
   * legacy-contract payload: code/description/noOfAdministrationTime/items/
   * addedLanguageList). The backend REQUIRES the `items` array — a save
   * without it 500s with an NPE (FrequencyService.saveFrequency:193,
   * request.getItems() null); that was the original maker defect (missing
   * items in the first payload), now fixed by sending the full contract.
   * The phantom t_translated_frequency is NOT on the plain save path (the
   * translation insert only fires when addedLanguageList is non-empty).
   * Per WRITE-VERIFICATION the outcome is reported from the observed
   * response; a success is "accepted the payload" — never "working" beyond
   * the observed response + follow-up read.
   */
  save() {
    const code = this.code().trim();
    const desc = this.description().trim();
    const timeItems = this.times()
      .map((t) => t.trim())
      .filter((t) => t.length > 0);
    if (!code || !desc || timeItems.length === 0) {
      this.notify.error('Code, Description and at least one administration time are required');
      return;
    }
    const userId = this.session.userId ?? 0;
    const base = {
      code,
      description: desc,
      noOfAdministrationTime: timeItems.length,
      items: timeItems.map((t) => ({
        administrationTime: t,
        // The backend's FrequencyRequestItem setters map an EMPTY string to
        // the midnight default (00:00) — matching the legacy contract, which
        // always sends these keys. ABSENT keys leave the fields null and the
        // m_frequency_detail insert violates NOT NULL (the 500 the probe
        // first hit). Empty strings are the legacy shape.
        preBufferAdministrationTime: '',
        postBufferAdministrationTime: '',
      })),
      addedLanguageList: [],
      unitId: this.unitId,
      organizationId: this.orgId,
      userId,
    };
    this.saving.set(true);
    if (this.editingId()) {
      const id = this.editingId()!;
      this.service.update(this.orgId, id, { code, description: desc }).subscribe({
        next: (res) => {
          this.saving.set(false);
          this.notify.success(res?.message ?? 'Frequency updated');
          this.clearForm();
          this.loadList();
        },
        error: (e) => {
          this.saving.set(false);
          this.notify.error(`Update failed: ${e?.error?.message ?? e?.message ?? e}`);
        },
      });
    } else {
      this.service.save(base).subscribe({
        next: (res) => {
          this.saving.set(false);
          this.notify.success(res?.message ?? 'Frequency saved');
          this.clearForm();
          this.loadList();
        },
        error: (e) => {
          this.saving.set(false);
          // Name the failure in plain language — never echo the raw HTTP error.
          this.notify.error(`Save failed: ${e?.error?.message ?? 'the server rejected the request'}. Check the server log for the correlation reference.`);
        },
      });
    }
  }
}
