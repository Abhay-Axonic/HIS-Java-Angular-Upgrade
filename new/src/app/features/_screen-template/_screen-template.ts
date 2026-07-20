import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { buildOptionalParams } from '../../shared/search/optional-params';
import { ScreenTemplateService } from './_screen-template.service';
import { ScreenTemplateRecord, emptyScreenTemplateRecord } from './_screen-template.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * TEMPLATE -- starting point for a new Phase 4 screen rebuild. Copy this
 * whole folder, rename `_screen-template` -> the real screen's name (drop
 * the leading underscore -- it exists only so this folder is never
 * accidentally treated as a real, routed screen), and replace every
 * TEMPLATE-tagged section with the real screen's audited behavior.
 *
 * See ./README.md for the full checklist. See
 * docs/ANGULAR_REBUILD_PLAYBOOK.md for the process this scaffold fits into
 * (audit -> golden-trace -> rebuild from this scaffold -> re-audit ->
 * interaction testing -> domain review -> branch/hold-for-review).
 *
 * What this scaffold bakes in, by default, so a new screen doesn't have to
 * rediscover any of Phase 2's 4 fixed bugs from scratch:
 *  - moduleAccessGuard: wired at the ROUTE level, not in this component --
 *    see the route-registration example in ./README.md. Nothing to add here.
 *  - authInterceptor: already global (app.config.ts) -- every HttpClient
 *    call through ScreenTemplateService gets the right headers automatically.
 *  - <app-status-toggle>: correct CSS ships with the component itself.
 *  - buildOptionalParams(): correct "send only filled-in fields" search pattern.
 */
@Component({
  selector: 'app-screen-template',
  imports: [FormsModule, StatusToggle],
  templateUrl: './_screen-template.html',
  styleUrl: './_screen-template.scss',
})
export class ScreenTemplate {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ScreenTemplateService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;

  // ---- list + pagination state (standard shape -- see
  // billing-group-master.ts for a real screen using the identical pattern) ----
  list = signal<ScreenTemplateRecord[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // ---- TEMPLATE: multi-field-optional search example (see
  // new/src/app/shared/search/optional-params.ts and
  // docs/ANGULAR_PENDING_FINDINGS.md entry 15). Replace field names with
  // the real screen's search fields; confirm AND/OR combination against
  // the actual legacy DAO query, don't assume. ----
  codeSearchTerm = signal('');
  descriptionSearchTerm = signal('');

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
        }
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') {
          this.totalCount.set(res.object);
        }
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadList();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadList();
  }

  search(): void {
    const params = buildOptionalParams({
      code: this.codeSearchTerm(),
      description: this.descriptionSearchTerm(),
    });
    if (!params.code && !params.description) return;
    this.api.search(params).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
        }
      },
    });
  }

  // ---- status toggle: the correct pattern, matching docs/
  // ANGULAR_PENDING_FINDINGS.md entry 12's fix. Update via the list
  // signal's `.update()` with a NEW row object -- never mutate `row`
  // directly, even though it looks like it would "just work". ----
  onToggleStatus(row: ScreenTemplateRecord): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api.changeStatus(row.id, nextStatus, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
          this.notify.success(res.message ?? 'Status updated');
        } else {
          this.notify.error(res.message ?? 'Update failed');
        }
      },
      error: () => this.notify.error('Update failed'),
    });
  }

  // ---- TEMPLATE: replace with the real screen's create/update flow,
  // matching the audited legacy behavior exactly (validation order,
  // required fields, error messages -- see the playbook's Step 1). ----
  record = signal<ScreenTemplateRecord>(emptyScreenTemplateRecord());
}
