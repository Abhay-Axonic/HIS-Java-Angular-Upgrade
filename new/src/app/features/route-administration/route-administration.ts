import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { RouteAdministrationService } from './route-administration.service';
import { RouteOfAdministrationRow } from './route-administration.models';

@Component({
  selector: 'app-route-administration',
  standalone: true,
  imports: [FormsModule],
  templateUrl: './route-administration.html',
  styleUrl: './route-administration.scss',
})
export class RouteAdministration {
  private session = inject(SessionService);
  private notify = inject(NotificationService);
  private service = inject(RouteAdministrationService);

  rows = signal<RouteOfAdministrationRow[]>([]);
  code = signal('');
  description = signal('');
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
        else this.notify.error(`Route of administration list load failed: unexpected envelope`);
      },
      error: (e) => this.notify.error(`Route of administration list load failed: ${e.message ?? e}`),
    });
  }

  edit(row: RouteOfAdministrationRow) {
    this.code.set(row.code);
    this.description.set(row.description);
    this.editingId.set(row.id);
  }

  clearForm() {
    this.code.set('');
    this.description.set('');
    this.editingId.set(null);
  }

  /**
   * Save/Update — HONEST. The endpoints exist and are live-mapped
   * (POST .../{orgId}/{userId} create, PATCH .../{orgId}/{id} update/status;
   * org/unit set server-side from VerifiedIdentityService — the org-safe
   * pattern, batch-35 fixed the prior write IDOR). The legacy's duplicate-
   * code check is CLIENT-SIDE ONLY (ValidateCode/ValidateCodeForUpdate in
   * the legacy controller) — there is NO server-side dup-check; the rebuild
   * mirrors the client-side check on the loaded list. Per WRITE-VERIFICATION
   * the outcome is reported from the response; a success is "not erroring" —
   * never "working" beyond the observed response.
   */
  save() {
    const code = this.code().trim();
    const desc = this.description().trim();
    if (!code || !desc) {
      this.notify.error('Code and Description are required');
      return;
    }
    const dup = this.rows().find(
      (r) => r.code.toLowerCase() === code.toLowerCase() && r.id !== this.editingId()
    );
    if (dup) {
      this.notify.error('Duplicate Route Of Administration code');
      return;
    }
    const userId = this.session.userId ?? 0;
    this.saving.set(true);
    if (this.editingId()) {
      const id = this.editingId()!;
      this.service.update(this.orgId, id, { code, description: desc }).subscribe({
        next: (res) => {
          this.saving.set(false);
          this.notify.success(res?.message ?? 'Route of administration updated');
          this.clearForm();
          this.loadList();
        },
        error: (e) => {
          this.saving.set(false);
          this.notify.error(`Update failed: ${e.message ?? e}`);
        },
      });
    } else {
      this.service.save(this.orgId, userId, { code, description: desc, status: true }).subscribe({
        next: (res) => {
          this.saving.set(false);
          this.notify.success(res?.message ?? 'Route of administration added');
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
