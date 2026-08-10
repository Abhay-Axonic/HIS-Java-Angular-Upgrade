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
  noOfTimes = signal<number | null>(null);
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
          this.noOfTimes.set(res.noOfAdministrationTime);
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
        this.noOfTimes.set(row.noOfAdministrationTime);
        this.editingId.set(row.id);
      },
    });
  }

  clearForm() {
    this.code.set('');
    this.description.set('');
    this.noOfTimes.set(null);
    this.editingId.set(null);
  }

  /**
   * Save/Update — HONEST. The endpoints exist and are mapped
   * (POST /api/globals/frequency/save, PATCH .../{orgId}/{id}); org/unit on
   * the WRITE path are payload-carried (the legacy ternary — FINDING, NOT
   * the VerifiedIdentityService pattern; the reads are org-scoped). No
   * server-side dup-check exists (the legacy check is client-side only).
   * Per WRITE-VERIFICATION the outcome is reported from the response; a
   * success is "not erroring" — never "working" beyond the observed response.
   */
  save() {
    const code = this.code().trim();
    const desc = this.description().trim();
    const times = this.noOfTimes();
    if (!code || !desc || times === null) {
      this.notify.error('Code, Description and Number of administration times are required');
      return;
    }
    const userId = this.session.userId ?? 0;
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
          this.notify.error(`Update failed: ${e.message ?? e}`);
        },
      });
    } else {
      const payload = {
        code,
        description: desc,
        noOfAdministrationTime: times,
        status: true,
        unitId: this.unitId,
        organizationId: this.orgId,
        userId,
      };
      this.service.save(payload).subscribe({
        next: (res) => {
          this.saving.set(false);
          this.notify.success(res?.message ?? 'Frequency saved');
          this.clearForm();
          this.loadList();
        },
        error: (e) => {
          this.saving.set(false);
          this.notify.error(`Save failed: ${e.message ?? e}`);
        },
      });
    }
  }
}
