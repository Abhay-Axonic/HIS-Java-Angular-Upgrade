import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ResourceAllocationMasterService } from './resource-allocation-master.service';
import {
  AmbulanceTypeTariffOption,
  ResourceAllocationForm,
  ResourceAllocationRow,
  emptyResourceAllocationForm,
} from './resource-allocation-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Legacy has no dedicated ambulance-provider master -- the dropdown is a
 * hardcoded two-option `<select>` in the view (view:29-32), not fetched from
 * any endpoint. Reproduced verbatim, not "fixed" into a real lookup list.
 */
const AMBULANCE_PROVIDER_OPTIONS = ['In House', 'Out Sourced'] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/ambulanceService/resource-Allocation-Master.html +
 * resources/assets/js/controller/lis/global/resourceAllocationController.js
 * (`resourceAllocationController`, no `controllerAs`). Route guard: moduleId
 * 817, wired at the route level via `moduleAccessGuard`, not here.
 *
 * **Confirmed CLEAN screen -- no crash bug here.** Unlike this same
 * Ambulance module family's `ambulanceMaster` sibling (whose `ng-change`
 * handler sends the ambulance-type-NAME string where a numeric
 * `resourceAllocationMasterId` is expected, guaranteed-crashing every
 * selection -- see ambulance-master.ts's class doc comment), this screen's
 * only caller of `GETSINGLERESOURCEALLOCATION` is `showUpdateBtn(id)`
 * (ctrl:325-354), which is wired from the row-level pencil icon
 * (view:168, `ng-click="showUpdateBtn(info.resourceAllocationMasterId)"`)
 * -- always the real numeric row id. Reproduced with a `number` parameter
 * throughout, no string-typed id path introduced.
 *
 * **Judgment calls / preserved quirks, not bugs introduced by this rebuild:**
 *
 * - The "Ambulance Type" dropdown is populated from
 *   `GETAMBULANCETARRIFACTIVELIST` (`/adt/getAmbulanceTarrifActiveList`,
 *   ctrl:51-66) -- an org-scoped active-TARIFF list, not a dedicated
 *   ambulance-type-master list. A genuine legacy quirk, not a bug;
 *   reproduced exactly via `getAmbulanceTarrifActiveList`, not "fixed" to
 *   call a different, more sensible endpoint.
 * - The "Ambulance Provider" dropdown is a hardcoded two-option list
 *   (`In House` / `Out Sourced`, view:30-31), not fetched from any
 *   endpoint. Reproduced as a local constant, see
 *   `AMBULANCE_PROVIDER_OPTIONS` above.
 * - Save vs. Update is asymmetric, same family as qualification-master /
 *   ambulance-master's own save/update split: clicking Save
 *   (`saveResourceAllocation`, ctrl:211-275) validates, then opens the
 *   shared "Are you sure you want to Save?" confirm modal before actually
 *   POSTing (legacy re-enters the same function via `PopupOkBtn` with
 *   `popUpFlag` now false, which is when the real POST fires, ctrl:205-208 +
 *   233-236). Clicking Update (`updateResourceAllocation`, ctrl:398-447)
 *   validates and POSTs immediately, with NO confirm step at all.
 * - **The shared `<modal>` directive's x close button crashes in legacy**
 *   (`$scope.cancel` is never defined by this controller) -- but per the
 *   standing policy resolved 2026-07-21 (`docs/ANGULAR_PENDING_FINDINGS.md`
 *   entry 55, `docs/ANGULAR_REBUILD_PLAYBOOK.md` Step 3), a shared
 *   component's correct, working default behavior differing from a bug
 *   baked into legacy's own shared directive is an accepted, disclosed
 *   standing improvement, not a parity gap to reproduce. The x button here
 *   is an ordinary, working `closeConfirmModal()`, same pattern as
 *   `qualification-master.ts`/`ambulance-master.ts`.
 * - The header search box (view:115, `ng-model="searchKeyword"`) IS wired
 *   here -- the view's `ng-repeat="info in resourceAllocationList |
 *   filter:searchKeyword"` (view:156) is a bare Angular `filter` over
 *   `resourceAllocationList`, which only ever holds the CURRENT PAGE's
 *   already-fetched rows (list/count refetch on every page change). A user
 *   searching for a record on a different page sees no match even though it
 *   exists. Reproduced via `displayedRows`, matching all 5 visible text
 *   columns (type/provider/vehicle no/resource name/mobile no) -- same
 *   practical interpretation used for this same `filter:keyword` pattern
 *   elsewhere in this rebuild (e.g. asset-category-master,
 *   qualification-master).
 * - List + count fetching: legacy splits this into two nearly-identical
 *   functions -- `initMasterList` (ctrl:78-117, fetches list AND count,
 *   used on init/page-size change, and additionally calls `setPage(1,
 *   false)` once the count resolves) and `masterList` (ctrl:119-139,
 *   fetches list ONLY, used by `setPage`'s flag-gated page-navigation
 *   refetch). Functionally the two always fetch the same list shape off the
 *   same organizationId/offset/pageSize; the split only avoids re-fetching
 *   the count on every page click. Unified here into a single `loadList()`
 *   that fetches both every time (same convention as
 *   `qualification-master.ts`) -- a benign consolidation, not a behavioral
 *   change, since the count value itself doesn't depend on which page is
 *   currently shown.
 * - `showUpdateBtn` (ctrl:325-354) flips into update mode (`saveBtnFlag =
 *   false`) synchronously, BEFORE the `GETSINGLERESOURCEALLOCATION` call
 *   resolves, and uses a raw `alert("Error!!")` on a failed response --
 *   reproduced verbatim (`saveMode.set(false)` up front; `window.alert` on
 *   failure).
 * - `updateStatus` (ctrl:357-395) never reloads the list and never resets
 *   the add/edit form on success -- unlike qualification-master's
 *   `updateStatus`, which resets the form via `$scope.init()`. Reproduced
 *   by patching the `rows` signal locally only, with no form reset and no
 *   list reload on either success or failure; also never reverted on a
 *   failed POST (legacy's `ng-model="info.status"` binds directly to the
 *   row object, so the toggle is applied optimistically and stays applied
 *   regardless of the server's response).
 * - Legacy has large blocks of fully-commented-out dead pagination code
 *   (ctrl:165-202, 277-322) and a stray `console.log("commonList",
 *   $scope.commonList)` referencing a variable that's never assigned
 *   (ctrl:104/137, `$scope.commonList` vs. the actually-used
 *   `$scope.resourceAllocationList`) -- none of this dead/debug code is
 *   reproduced.
 */
@Component({
  selector: 'app-resource-allocation-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './resource-allocation-master.html',
  styleUrl: './resource-allocation-master.scss',
})
export class ResourceAllocationMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ResourceAllocationMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  readonly ambulanceProviderOptions = AMBULANCE_PROVIDER_OPTIONS;

  // ---- dropdown option list (mirrors $scope.ambulanceTypeList) ----
  ambulanceTypeOptions = signal<AmbulanceTypeTariffOption[]>([]);

  // ---- form state (mirrors $scope.resourceAllocation + saveBtnFlag/updateBtnFlag) ----
  form = signal<ResourceAllocationForm>(emptyResourceAllocationForm());
  saveMode = signal(true);
  submitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + pagination state (mirrors initMasterList/masterList/setPage) ----
  rows = signal<ResourceAllocationRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters the CURRENT PAGE's already-fetched
  // rows only (see class comment), matching all 5 visible text columns.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const list = this.rows();
    if (!keyword) return list;
    return list.filter(
      (r) =>
        r.ambulanceType?.toLowerCase().includes(keyword) ||
        r.ambulanceProvider?.toLowerCase().includes(keyword) ||
        r.vehicalNo?.toLowerCase().includes(keyword) ||
        r.resourceName?.toLowerCase().includes(keyword) ||
        r.mobileNo?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.refreshAmbulanceTypeOptions();
    this.loadList();
  }

  private refreshAmbulanceTypeOptions(): void {
    this.api.getAmbulanceTarrifActiveList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.ambulanceTypeOptions.set(res.listObject ?? []);
      },
    });
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  private reloadFirstPage(): void {
    this.currentPage.set(1);
    this.loadList();
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

  updateField<K extends keyof ResourceAllocationForm>(field: K, value: ResourceAllocationForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  isFieldInvalid(value: string): boolean {
    return this.submitted() && !value.trim();
  }

  private validateRequired(): boolean {
    const f = this.form();
    return !!(f.ambulanceType.trim() && f.ambulanceProvider.trim() && f.vehicalNo.trim() && f.resourceName.trim() && f.mobileNo.trim());
  }

  // ---- save flow: Save button validates then opens the confirm modal;
  // only the modal's "Ok" actually POSTs (mirrors saveResourceAllocation's
  // popUpFlag-gated double call via PopupOkBtn, ctrl:205-275). ----
  onSaveClick(): void {
    this.submitted.set(true);
    if (!this.validateRequired()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.create();
  }

  private create(): void {
    const f = this.form();
    const now = formatDmyHms(new Date());
    this.api
      .save({
        ambulanceType: f.ambulanceType,
        ambulanceProvider: f.ambulanceProvider,
        vehicalNo: f.vehicalNo,
        resourceName: f.resourceName,
        mobileNo: f.mobileNo,
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
            this.notify.success('Resource Allocation Added Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Resource Allocation already exits...!!');
          }
          // Unconditional, regardless of success/failure -- see class doc.
          this.reloadFirstPage();
        },
      });
  }

  // ---- update flow: no confirm modal at all, matching legacy's
  // updateResourceAllocation() being wired directly to the Update button. ----
  onUpdateClick(): void {
    this.submitted.set(true);
    if (!this.validateRequired()) return;
    this.update();
  }

  private update(): void {
    const f = this.form();
    this.api
      .update({
        resourceAllocationMasterId: f.resourceAllocationMasterId as number,
        ambulanceType: f.ambulanceType,
        ambulanceProvider: f.ambulanceProvider,
        vehicalNo: f.vehicalNo,
        resourceName: f.resourceName,
        mobileNo: f.mobileNo,
        updatedBy: this.userId,
        updatedDate: formatDmyHms(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Resource Allocation Updated Successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Resource Allocation already exits...!!');
          }
          // Unconditional, regardless of success/failure -- see class doc.
          this.reloadFirstPage();
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyResourceAllocationForm());
    this.submitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn -- flips into update mode BEFORE the
  // GETSINGLERESOURCEALLOCATION response arrives, and uses a raw alert() on
  // failure; both preserved verbatim; see class comment). Always the real
  // numeric row id -- this screen has no string-id crash path. ----
  edit(row: ResourceAllocationRow): void {
    this.saveMode.set(false);
    this.api.getById(row.resourceAllocationMasterId, this.organizationId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.form.set({
            resourceAllocationMasterId: detail.resourceAllocationMasterId,
            ambulanceType: detail.ambulanceType,
            ambulanceProvider: detail.ambulanceProvider,
            vehicalNo: detail.vehicalNo,
            resourceName: detail.resourceName,
            mobileNo: detail.mobileNo,
          });
        } else {
          window.alert('Error!!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus -- patches the list in place,
  // never reloads the list, never resets the form, and never reverts on
  // failure; see class comment). ----
  toggleStatus(row: ResourceAllocationRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.rows.update((list) =>
      list.map((r) => (r.resourceAllocationMasterId === row.resourceAllocationMasterId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        resourceAllocationMasterId: row.resourceAllocationMasterId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDmyHms(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Resource Allocation Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }
}

/** `moment(currentDate).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:212-214/358-360/399-400. */
function formatDmyHms(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
