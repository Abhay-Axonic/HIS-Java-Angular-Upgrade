import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { MulticounterPackageService } from './multicounter-package.service';
import { PackageSearchRow } from './multicounter-package.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of the legacy `multicounterPackage` screen
 * (multicounterPackageController.js + multicounter-package-main.html):
 * search/list of multi-counter visit packages (packageTypeId 5) + status
 * toggle, via POST /api/packages/filter.
 *
 * Module id 627 (legacy resolve). Tenant scoping enforced server-side
 * (BR-159).
 *
 * SME-RELEVANT (Step 6): the list only surfaces package rows; the actual
 * package inclusion/exclusion + price logic lives in the add/edit builder
 * (legacy addNewMultiEncounterController.js, ~70KB) which is NOT part of
 * this rebuild yet -- see the batch report's SME points for what to review
 * there (addItemWiseExclusion/selectAllServices/calculatePackageAmounts).
 */
@Component({
  selector: 'app-multicounter-package',
  imports: [FormsModule, StatusToggle],
  templateUrl: './multicounter-package.html',
  styleUrl: './multicounter-package.scss',
})
export class MulticounterPackage {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(MulticounterPackageService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;

  list = signal<PackageSearchRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  searchTerm = signal('');

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.search(this.organizationId, this.unitId, offset, this.pageSize(), this.searchTerm()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
          if (typeof res.object === 'number') this.totalCount.set(res.object);
        }
      },
      error: () => this.notify.error('Failed to load packages'),
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
    this.currentPage.set(1);
    this.loadList();
  }

  // Status toggle via GET /api/packages/inActivePackage/parentPkgId/{id}/
  // PackageTypeId/5/activeInactive/{bool} -- the legacy endpoint, now
  // org-scoped post-BR-160 (live-verified: forged toggle on org-999 victim
  // blocked, legit org-1 toggle works).
  onToggleStatus(row: PackageSearchRow): void {
    const nextStatus = row.status === 'A' ? false : true;
    this.api
      .toggleStatus(row.parentPackageId, nextStatus)
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            const newStatus = nextStatus ? 'A' : 'I';
            this.list.update((rows) =>
              rows.map((r) => (r.parentPackageId === row.parentPackageId ? { ...r, status: newStatus } : r)),
            );
            this.notify.success(nextStatus ? 'Package activated' : 'Package inactivated');
          } else {
            this.notify.error(res.message ?? 'Update failed');
          }
        },
        error: () => this.notify.error('Update failed'),
      });
  }
}
