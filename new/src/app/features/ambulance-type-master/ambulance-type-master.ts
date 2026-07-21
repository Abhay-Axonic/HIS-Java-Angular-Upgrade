import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { AmbulanceTypeMasterService } from './ambulance-type-master.service';
import { AmbulanceTypeRow } from './ambulance-type-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/ambulanceService/ambulance-Type-Master.html +
 * resources/assets/js/controller/lis/global/ambulanceTypeController.js.
 * Legacy state `ambulanceTypeMaster` (app.js:19643-19670), gated by a
 * resolve-block check for moduleId 813 -- wired at the ROUTE level via
 * `moduleAccessGuard` (`data: { moduleId: 813 }`), not in this component.
 *
 * **Deliberate deviations/preserved quirks, not bugs in this rebuild:**
 * - Both required-field error messages read literally "Ambulance Type is
 *   required" in the legacy view (view lines 15 and 29) -- a copy-paste bug
 *   where the Ambulance Type ID field's own error text was never updated
 *   to say "Ambulance Type ID is required". Preserved verbatim on both
 *   fields; "corrected" text would be a silent behavior change.
 * - The Save button opens a real confirm modal ("Are you sure you want to
 *   Save?") before the create POST fires (`saveAmbulanceType`'s
 *   `popUpFlag`/`PopupOkBtn` dance, ctrl:114-181, using the shared `modal`
 *   directive in commonPopUpDirectives.js). The Update button does NOT --
 *   `updateAmbulanceType` (ctrl:300-347) posts immediately with no
 *   confirmation step at all. This asymmetry is real, not an oversight:
 *   only the create path is wrapped in `showConfirmModal` below.
 * - After a successful save OR update, legacy calls `$scope.init()`
 *   (resetting `offset` to 0 and `noOfRecordsPerPage` to 10) and THEN
 *   unconditionally reloads the list via `initAmbulanceMasterList(...,
 *   $scope.offset, $scope.noOfRecordsPerPage)` (ctrl:172-176, 338-343) --
 *   silently snapping the table back to page 1 @ page size 10, even if the
 *   user was on page 3 @ page size 50. On a FAILED save/update, `init()` is
 *   skipped, so the page-size choice survives, but the reload still always
 *   uses `$scope.offset`, which is never reassigned anywhere except inside
 *   `init()` -- so the reload always effectively targets page 1 regardless
 *   of success/failure. Reproduced via `afterSaveOrUpdate()`: always resets
 *   `currentPage` to 1, and additionally resets `pageSize` to the default
 *   only on success.
 * - The search box filters only the CURRENT PAGE's already-fetched rows
 *   (bare Angular `filter:searchKeyword` over `$scope.ambulanceTypeList`,
 *   not a server-side search across the full dataset) -- same quirk class
 *   already documented in `asset-category-master.ts`. Reproduced the same
 *   way via `displayedRows`.
 * - `getList`'s single call returns both the page of rows AND the total
 *   count in one response; the separate count endpoint/call
 *   (`COUNTAMBULANCETYPEMASTER`) is commented-out dead code in the
 *   controller (ctrl:96-107), never actually invoked. One call, not two.
 * - Legacy's missing-`$state`-DI crash-on-expired-session bug (ctrl:24-27)
 *   isn't reproducible in this rebuild's architecture -- `SessionService`
 *   never throws, and `moduleAccessGuard` handles the redirect at the
 *   route level. Noted for the record only, nothing to preserve.
 */
@Component({
  selector: 'app-ambulance-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './ambulance-type-master.html',
  styleUrl: './ambulance-type-master.scss',
})
export class AmbulanceTypeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(AmbulanceTypeMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.ambulance) ----
  ambulanceTypeId = signal('');
  ambulanceType = signal('');
  private activeMasterId: number | undefined;
  saveMode = signal(true); // true = saveBtnFlag, false = updateBtnFlag
  submitted = signal(false); // drives *required* error display, like show-errors-check-validity

  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors PagerService.GetPager:
  // startIndex = (currentPage-1)*pageSize, confirmed in pagerService.js) ----
  list = signal<AmbulanceTypeRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters the CURRENT PAGE only (see class comment).
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) => r.ambulanceTypeId?.toLowerCase().includes(keyword) || r.ambulanceType?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
          if (typeof res.object === 'number') this.totalCount.set(res.object);
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

  // ---- validation (mirrors ambulanceform.$valid over the two required fields) ----
  isTypeIdInvalid(): boolean {
    return this.submitted() && !this.ambulanceTypeId().trim();
  }

  isTypeInvalid(): boolean {
    return this.submitted() && !this.ambulanceType().trim();
  }

  private validate(): boolean {
    this.submitted.set(true);
    return !!this.ambulanceTypeId().trim() && !!this.ambulanceType().trim();
  }

  // ---- save (mirrors saveAmbulanceType's popUpFlag confirm dance) ----
  openSaveConfirm(): void {
    if (!this.validate()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const now = formatDateTime(new Date());
    this.api
      .save({
        ambulanceType: this.ambulanceType(),
        ambulanceTypeId: this.ambulanceTypeId(),
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        createdDate: now,
        updatedDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Ambulance Type Added Successfully...!!');
            this.clearFields();
            this.afterSaveOrUpdate(true);
          } else {
            this.notify.error('Ambulance Type already exits...!!');
            this.afterSaveOrUpdate(false);
          }
        },
      });
  }

  // ---- update (mirrors updateAmbulanceType -- no confirm modal) ----
  update(): void {
    if (!this.validate()) return;
    const now = formatDateTime(new Date());
    this.api
      .update({
        ambulanceTypeMasterId: this.activeMasterId as number,
        ambulanceType: this.ambulanceType(),
        ambulanceTypeId: this.ambulanceTypeId(),
        updatedBy: this.userId,
        updatedDate: now,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Ambulance Type Updated Successfully...!!');
            this.clearFields();
            this.afterSaveOrUpdate(true);
          } else {
            this.notify.error('Ambulance Type already exits...!!');
            this.afterSaveOrUpdate(false);
          }
        },
      });
  }

  // Legacy quirk preserved -- see class doc comment.
  private afterSaveOrUpdate(success: boolean): void {
    this.currentPage.set(1);
    if (success) this.pageSize.set(PAGE_SIZE_OPTIONS[0]);
    this.loadList();
  }

  private clearFields(): void {
    this.ambulanceTypeId.set('');
    this.ambulanceType.set('');
    this.activeMasterId = undefined;
    this.saveMode.set(true);
    this.submitted.set(false);
  }

  // ---- edit (mirrors showUpdateBtn) ----
  edit(ambulanceTypeMasterId: number): void {
    this.api.getSingle(ambulanceTypeMasterId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) return;
        const detail = res.listObject[0];
        this.ambulanceTypeId.set(detail.ambulanceTypeId);
        this.ambulanceType.set(detail.ambulanceType);
        this.activeMasterId = detail.ambulanceTypeMasterId;
        this.saveMode.set(false);
      },
    });
  }

  // ---- status toggle (mirrors updateStatus) ----
  toggleStatus(row: AmbulanceTypeRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api
      .changeStatus({
        ambulanceTypeMasterId: row.ambulanceTypeMasterId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.list.update((rows) =>
              rows.map((r) => (r.ambulanceTypeMasterId === row.ambulanceTypeMasterId ? { ...r, status: nextStatus } : r)),
            );
            this.notify.success('Ambulance Type Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }
}

/** `moment(currentDate).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:121-123/260-262/301-302. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
