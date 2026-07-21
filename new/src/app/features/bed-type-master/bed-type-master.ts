import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { BedTypeMasterService } from './bed-type-master.service';
import { BedTypeFormState, BedTypeRow, emptyBedTypeForm } from './bed-type-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/Adt/bed-type-master.html +
 * resources/assets/js/controller/adt/global/bedTypeMasterController.js.
 * State `bedTypeMaster` (app.js:4059-4084), moduleId 402, no `controllerAs`
 * (plain `$scope`).
 *
 * **Deliberate deviations/preserved quirks, not bugs in this rebuild:**
 * - Legacy's confirm-popup mechanism is a scope-flag toggle: `saveBedType()`
 *   is the SAME function bound to both the Save button and (indirectly) the
 *   modal's Ok button. The first call (`popUpFlag` starts `true` from
 *   `init()`) only flips `showModal`; `PopupOkBtn()` then sets
 *   `popUpFlag = false` and calls `saveBedType()` again, which this time
 *   takes the real-POST branch (ctrl:188-257). A real quirk falls out of
 *   this: only a SUCCESSFUL save calls `$scope.init()`, which is the only
 *   place `popUpFlag` is reset back to `true` -- after a FAILED (duplicate)
 *   save, `popUpFlag` stays `false`, so the next Save click skips the
 *   confirm modal entirely and posts directly. This mechanism also depends
 *   on an unaudited custom `<modal>` directive (not part of this screen's
 *   controller/view, out of scope here). Reproduced instead with the same
 *   explicit open/close confirm-modal convention used by every other
 *   rebuilt master screen (asset-category-master, bed-category-master) --
 *   every Save click opens the confirm modal, including right after a
 *   failed save. The failed-save-skips-confirmation quirk is NOT
 *   reproduced.
 * - No client-side duplicate-code check exists anywhere in legacy for this
 *   screen (unlike e.g. bed-category-master's `checkDuplicateForSave`) --
 *   confirmed absent from the controller. Duplicate detection is entirely
 *   server-side: a non-`"success"` response on save/update surfaces
 *   "Bed Type already exits...!!". Reproduced as-is -- no client dup check
 *   added here either.
 * - `updateStatus()`/`toggleStatus` has no revert-on-failure (ctrl:289-329)
 *   -- the switch stays flipped even if the backend call fails. Reproduced
 *   as-is, matching bed-category-master's identical quirk.
 * - Legacy's edit-fetch failure uses a native `alert("Error!!")` (ctrl:283)
 *   instead of `growl` -- reproduced here as `notify.error(...)` for
 *   consistency with every other error path on this screen and every
 *   sibling rebuilt screen, not as a native browser `alert()` (same
 *   established convention as bed-category-master).
 * - The view's `ng-repeat="info in bedTypeList|filter:searchKeyword"`
 *   (view:140) filters only the CURRENT server-fetched page
 *   (`bedTypeList`), not the full dataset -- pagination is server-side
 *   (offset/noOfRecordsPerPage via `GETBEDTYPELIST`/`COUNTBEDTYPEMASTER`).
 *   Reproduced via `displayedRows()` over just `bedTypeList()`, same as
 *   bed-category-master.
 * - `$scope.unitId` is captured from the session cookie in `init()`
 *   (ctrl:35) but is never read anywhere else in the controller -- not in
 *   the list/count/save/update/status payloads (confirmed against
 *   `BedTypeMasterDto.java`, which has no `unitId` field at all). A true
 *   dead binding; not carried into this rebuild.
 * - A real HTTP-level failure (as opposed to an app-level
 *   `status !== 'success'` response) has no handler at all in legacy's
 *   `GenericService.serviceAction` call sites here -- this rebuild instead
 *   shows a generic "Something Went Wrong" notification, matching the
 *   established convention already used by every other rebuilt master
 *   screen rather than reproducing total silence on network failure.
 */
@Component({
  selector: 'app-bed-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './bed-type-master.html',
  styleUrl: './bed-type-master.scss',
})
export class BedTypeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(BedTypeMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.bedtype) ----
  form = signal<BedTypeFormState>(emptyBedTypeForm());
  formSubmitted = signal(false);
  saveBtnFlag = signal(false); // true while editing -> hides Save
  updateBtnFlag = signal(true); // true while adding -> hides Update

  showConfirmModal = signal(false);

  // ---- list + pagination (server-driven, mirrors PagerService usage) ----
  bedTypeList = signal<BedTypeRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters only the CURRENT server-fetched
  // page (`bedTypeList`), not the full dataset -- see class doc comment.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.bedTypeList();
    return this.bedTypeList().filter((r) =>
      Object.values(r).some((v) => v != null && typeof v !== 'object' && String(v).toLowerCase().includes(keyword)),
    );
  });

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.loader.start();
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getBedTypeList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') this.bedTypeList.set(res.listObject ?? []);
      },
      error: () => this.loader.stop(),
    });
    this.api.getBedTypeCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  updateField<K extends keyof BedTypeFormState>(field: K, value: BedTypeFormState[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
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

  clearSearch(): void {
    this.searchKeyword.set('');
  }

  // ---- required-field validation (mirrors bedtypeform.$valid) ----
  private validateRequired(): boolean {
    const f = this.form();
    return !!f.bedTypeCode.trim() && !!f.bedTypeName.trim();
  }

  isRequiredMissing(value: string): boolean {
    return this.formSubmitted() && !value.trim();
  }

  // ---- save (create) -- mirrors saveBedType()'s confirm flow (ctrl:193-257) ----
  openConfirmModal(): void {
    this.formSubmitted.set(true);
    if (!this.validateRequired()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  save(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .saveBedType({
        bedTypeCode: f.bedTypeCode,
        bedTypeDesc: f.bedTypeName,
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
            this.notify.success('Bed Type Added Successfully...!!');
            this.resetAfterSuccess();
          } else {
            this.notify.error('Bed Type already exits...!!');
          }
          this.loadList();
        },
        error: () => this.notify.error('Something Went Wrong'),
      });
  }

  // Full $scope.init() equivalent -- resets the form and pagination (back
  // to page 1 / default page size), only called on a SUCCESSFUL
  // save/update, exactly like legacy.
  private resetAfterSuccess(): void {
    this.form.set(emptyBedTypeForm());
    this.formSubmitted.set(false);
    this.saveBtnFlag.set(false);
    this.updateBtnFlag.set(true);
    this.currentPage.set(1);
    this.pageSize.set(PAGE_SIZE_OPTIONS[0]);
  }

  // ---- edit (mirrors showUpdateBtn(id), ctrl:259-287) ----
  edit(id: number): void {
    this.saveBtnFlag.set(true);
    this.updateBtnFlag.set(false);
    this.formSubmitted.set(false);
    this.loader.start();
    this.api.getBedTypeById(id).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success' && res.listObject?.length) {
          const d = res.listObject[0];
          this.form.set({
            bedTypeId: d.bedTypeId,
            bedTypeCode: d.bedTypeCode,
            bedTypeName: d.bedTypeDesc,
          });
        } else {
          this.notify.error('Error!!');
        }
      },
      error: () => {
        this.loader.stop();
        this.notify.error('Error!!');
      },
    });
  }

  // ---- update (mirrors updateBedType(), ctrl:331-384 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.validateRequired()) return;

    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .updateBedType({
        bedTypeId: f.bedTypeId as number,
        bedTypeCode: f.bedTypeCode,
        bedTypeDesc: f.bedTypeName,
        updatedBy: this.userId,
        updatedDate: now,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Bed Type Updated Successfully...!!');
            this.resetAfterSuccess();
          } else {
            this.notify.error('Bed Type already exits...!!');
          }
          this.loadList();
        },
        error: () => this.notify.error('Something Went Wrong'),
      });
  }

  // ---- status toggle (mirrors updateStatus(id, type), ctrl:289-329) ----
  // Update via the list signal, not a direct mutation of `row` -- see
  // docs/ANGULAR_PENDING_FINDINGS.md entry 12. No revert-on-failure --
  // legacy's updateStatus() has none either (see class doc comment).
  toggleStatus(row: BedTypeRow): void {
    const status: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.bedTypeList.update((rows) =>
      rows.map((r) => (r.bedTypeId === row.bedTypeId ? { ...r, status } : r)),
    );
    this.api
      .updateStatus({
        bedTypeId: row.bedTypeId,
        status,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') this.notify.success('Bed Type Updated Successfully...!!');
          else this.notify.error('Something went wrong...!!');
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }
}

function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
