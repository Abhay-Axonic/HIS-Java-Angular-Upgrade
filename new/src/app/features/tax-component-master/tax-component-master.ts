import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { TaxComponentMasterService } from './tax-component-master.service';
import { TaxComponentRecord, emptyTaxComponentRecord } from './tax-component-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of the legacy `taxComponentMaster` screen
 * (controller/master/pharmacy/taxComponentMasterController.js +
 * views/master/GlobalMaster/tax-component-master.html).
 *
 * Backend: Pharmacy TaxComponentController `/api/globals/tax/components/`
 * (GET list by org, GET by id, POST create, PATCH update). The orgId is a
 * PATH variable; the rebuild sends the verified session org. Tenant
 * scoping is enforced server-side (BR-158).
 *
 * Step 0 note (2026-08-03): the legacy app.js state is commented out
 * (no live legacy route); backend is real and live-verified. Module id for
 * the guard: taxMaster's intended module was 408 (commented); no active
 * legacy resolve exists -- the route is registered WITHOUT moduleId
 * (matching legacy's effectively-unreachable state, per the playbook rule
 * "don't invent a stricter check than legacy actually has").
 */
@Component({
  selector: 'app-tax-component-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './tax-component-master.html',
  styleUrl: './tax-component-master.scss',
})
export class TaxComponentMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(TaxComponentMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;

  list = signal<TaxComponentRecord[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Client-side search over the loaded list (the legacy screen filters the
  // fetched list client-side; the backend list endpoint returns all rows for
  // the org).
  searchTerm = signal('');

  // ---- form state ----
  record = signal<TaxComponentRecord>(emptyTaxComponentRecord());
  isEdit = signal(false);
  /** Signal-aware field setter — rerouted from the in-place `record().x = v`
   * bindings (the signal never notified; the save read a stale object). */
  setField(key: string, value: unknown): void {
    this.record.update((f) => ({...f, [key]: value}));
  }


  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.api.getList(this.organizationId).subscribe({
      next: (rows) => {
        this.list.set(rows ?? []);
        this.totalCount.set(rows?.length ?? 0);
      },
      error: () => this.notify.error('Failed to load tax components'),
    });
  }

  filteredList = computed(() => {
    const term = this.searchTerm().toLowerCase();
    if (!term) return this.list();
    return this.list().filter(
      (r) => r.code.toLowerCase().includes(term) || r.component.toLowerCase().includes(term),
    );
  });

  onPageSizeChange(): void {
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }

  // ---- create/update ----
  startCreate(): void {
    this.record.set(emptyTaxComponentRecord());
    this.isEdit.set(false);
  }

  startEdit(row: TaxComponentRecord): void {
    this.record.set({ ...row });
    this.isEdit.set(true);
  }

  private requiredFieldsValid(): boolean {
    const r = this.record();
    return !!r.code?.trim() && !!r.component?.trim();
  }

  save(): void {
    if (!this.requiredFieldsValid()) {
      this.notify.error('Code and Component are required');
      return;
    }
    const r = this.record();
    const payload = { code: r.code.trim(), component: r.component.trim(), status: r.status };
    if (this.isEdit()) {
      this.api.update(this.organizationId, r.id, { id: r.id, ...payload }).subscribe({
        next: (res) => {
          if (res.status === 'success' || (res as any)?.message) {
            this.notify.success('Tax component updated');
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Update failed');
          }
        },
        error: (err) => this.notify.error('Update failed'),
      });
    } else {
      this.api.create(this.organizationId, payload).subscribe({
        next: (res) => {
          if (res.status === 'success' || (res as any)?.id) {
            this.notify.success('Tax component created');
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Create failed');
          }
        },
        error: (err) => this.notify.error('Create failed'),
      });
    }
  }

  // ---- status toggle (legacy: PATCH with status:false = deactivate) ----
  onToggleStatus(row: TaxComponentRecord): void {
    const nextStatus = !row.status;
    this.api.update(this.organizationId, row.id, {
      id: row.id,
      code: row.code,
      component: row.component,
      status: nextStatus,
    }).subscribe({
      next: (res) => {
        if (res.status === 'success' || (res as any)?.message) {
          this.list.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
          this.notify.success('Status updated');
        } else {
          this.notify.error(res.message ?? 'Update failed');
        }
      },
      error: (err) => this.notify.error('Update failed'),
    });
  }
}
