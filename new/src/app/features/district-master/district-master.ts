import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { DistrictMasterService } from './district-master.service';
import {
  CountryOption,
  DistrictForm,
  DistrictRow,
  StateOption,
  emptyDistrictForm,
} from './district-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/district-master.html +
 * resources/assets/js/controller/master/districtMasterController.js
 * (`districtMasterController`, no `controllerAs`). Legacy state
 * `districtMaster` (app.js:3455-3454, resolve block confirmed live directly,
 * not guessed), moduleId 385.
 *
 * **Confirmed real, working CRUD, Country->State cascade, and search this
 * session** -- both statically (full controller/view read) and live
 * (browser-executed `GenericService.serviceAction` calls against the real
 * backend returned genuine HTTP 200 `status:"success"` responses for
 * `getDistrictMasterList`/`getDistrictCount`, with 1 real row present in
 * this dev DB). No `$state`-DI issue here -- `$state` IS in this
 * controller's own DI list (ctrl:13-25) and the `$cookies`-missing guard
 * (ctrl:37-41) correctly redirects to login, unlike the entries 8/10 crash
 * class.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - The confirm modal ("Are you sure you want to Save?", the shared
 *   `<modal>` directive via `PopupOkBtn`/`popUpFlag`, ctrl:236-305) only
 *   gates CREATE; `updateDistrict()` (ctrl:385-438) posts immediately with
 *   no confirm step -- same asymmetry as every other rebuilt sibling in
 *   this codebase (amenity-master, ambulance-service-provider-master).
 * - On a successful save/update, legacy calls `$scope.init()` again
 *   (ctrl:286, 420) -- which resets `$scope.district` back to `{}` AND
 *   re-fetches the Country list (State list is NOT re-fetched; its own
 *   fetch is dead/commented-out code inside `init()`, ctrl:90-99) -- before
 *   `initDistrictMasterList` (list+count reload) ALSO always runs,
 *   unconditionally, in BOTH the success and error branches of save/update
 *   (ctrl:295-299/429-433). Reproduced via `resetFormAndCountry()`
 *   (success-only) + an unconditional `loadListAndCount()` call after both.
 * - **A real, district-specific divergence from this codebase's other
 *   status-toggle sibling screens:** `updateStatus` (ctrl:345-382) calls
 *   `initDistrictMasterList(...)` (a full list+count reload, which also
 *   forces the pager back to page 1) unconditionally in BOTH its success
 *   AND failure branches -- unlike amenity-master/qms-department-master's
 *   `updateStatus`, which only growls and never reloads anything. Net
 *   effect: the checkbox's optimistic client-side flip (from its two-way
 *   `ng-model`) is immediately overwritten by the server's true state a
 *   moment later, which makes a FAILED toggle self-correct back to its
 *   original value instead of staying stuck flipped (contrast with this
 *   codebase's amenity-master, where a failed toggle stays visually
 *   flipped forever). Reproduced via `toggleStatus()` applying the
 *   optimistic flip, then always calling `loadListAndCount()` after the
 *   response resolves, regardless of outcome.
 * - The failure branch of `updateStatus` shows `growl.warning(response.data.message, ...)`
 *   -- the server's own message, not a hardcoded string -- unlike
 *   save/update's hardcoded "already exits...!!" error text. Reproduced via
 *   `notify.warning(res.message ?? 'Warning!')`.
 * - `showUpdateBtn` (ctrl:308-342) correctly RE-CASCADES the State dropdown
 *   (`getStateByCountry(...)`, ctrl:333-334) before applying the fetched
 *   record's `stateId` -- unlike this codebase's
 *   `ambulance-service-provider-master`, editing a row here does NOT leave
 *   a stale/empty State list. Reproduced via `edit()` calling
 *   `onCountryChange`'s cascade fetch (without resetting `countryId` itself)
 *   before setting the form.
 * - `showUpdateBtn`'s failure branch uses a raw `alert("Error!!")`
 *   (ctrl:338), not `growl` -- reproduced verbatim via `window.alert`.
 * - The search box (view:156, `ng-model="searchKeyword"`) IS genuinely
 *   wired to a real Angular `filter:searchKeyword` over the ALREADY
 *   FETCHED page (view:187) -- a working, generic all-fields substring
 *   filter, not a dead search box (contrast with this codebase's
 *   `amenity-master`'s confirmed-dead `serachTestmaster` search). Reproduced
 *   the same way `qms-department-master.ts`/`ambulance-service-provider-master.ts`
 *   model the identical bare-filter pattern.
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), the
 *   confirm modal's close button is wired to an ordinary working
 *   `closeConfirmModal()` here, NOT a reproduction of legacy's shared
 *   `<modal>` directive's `cancel()` crash -- a working close is an
 *   accepted standing improvement, not a parity gap.
 * - Both `saveDistrict()` (ctrl:260) and `updateDistrict()` (ctrl:401) only
 *   ever check `$scope.districtform.$valid` with no `else` branch -- an
 *   invalid submit is a silent no-op in both directions. Reproduced via an
 *   early `return` in both `save()` and `update()`.
 */
@Component({
  selector: 'app-district-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './district-master.html',
  styleUrl: './district-master.scss',
})
export class DistrictMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(DistrictMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- dropdown option lists (mirrors $scope.countryList/stateList) ----
  countryOptions = signal<CountryOption[]>([]);
  stateOptions = signal<StateOption[]>([]);

  // ---- form state (mirrors $scope.district) ----
  form = signal<DistrictForm>(emptyDistrictForm());
  saveMode = signal(true); // true = saveBtnFlag hidden-state inverse (see html) -- Save visible while adding
  formSubmitted = signal(false);
  showConfirmModal = signal(false);

  // ---- list + server-side pagination + client-side search (mirrors
  // initDistrictMasterList/getDistrictMasterList/PagerService) ----
  list = signal<DistrictRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Real, working search (view:187, `districtList|filter:searchKeyword`) --
  // a generic substring filter over the currently-fetched page only.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter((r) => Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)));
  });

  constructor() {
    this.loadCountryList();
    this.loadListAndCount();
  }

  // ctrl:75-89.
  private loadCountryList(): void {
    this.api.getActiveCountryList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.countryOptions.set(res.listObject ?? []);
      },
    });
  }

  // mirrors initDistrictMasterList (ctrl:110-158): fetches list + count
  // together, always from offset 0 (`$scope.offset` is never reassigned
  // anywhere), and forces the pager back to page 1 once the count responds.
  // Used for initial load, page-size changes, and unconditionally after
  // every save/update/status-toggle response (see class doc comment).
  private loadListAndCount(): void {
    this.api.getDistrictMasterList(this.organizationId, 0, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
    this.api.getDistrictCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
        this.currentPage.set(1);
      },
    });
  }

  // mirrors getDistrictMasterList via setPage(page,true): re-fetches just
  // the list at the new offset, no count refetch, no forced page reset.
  private loadPage(page: number): void {
    const offset = (page - 1) * this.pageSize();
    this.api.getDistrictMasterList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  onPageSizeChange(): void {
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  updateField<K extends keyof DistrictForm>(field: K, value: DistrictForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- Country -> State cascade (mirrors getStateByCountry, ctrl:214-234) ----
  onCountryChange(countryId: string): void {
    this.updateField('countryId', countryId);
    this.api.getStateListByCountryId(countryId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.stateOptions.set(res.listObject ?? []);
      },
    });
  }

  // ---- required-field validation (mirrors districtform.$valid -- all 4
  // fields carry `required`, view:18/35/52/70) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.countryId.trim() && f.stateId.trim() && f.districtCode.trim() && f.districtName.trim());
  }

  isCountryInvalid(): boolean {
    return this.formSubmitted() && !this.form().countryId.trim();
  }
  isStateInvalid(): boolean {
    return this.formSubmitted() && !this.form().stateId.trim();
  }
  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().districtCode.trim();
  }
  isNameInvalid(): boolean {
    return this.formSubmitted() && !this.form().districtName.trim();
  }

  // ---- save / confirm-modal (mirrors saveDistrict/PopupOkBtn, ctrl:243-305) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:260 guard (no else branch)
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .saveDistrictMaster({
        districtCode: f.districtCode,
        districtName: f.districtName,
        countryId: f.countryId,
        stateId: f.stateId,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        updatedDate: now,
        createdDate: now,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('District added successfully...!!');
            this.formSubmitted.set(false);
            this.resetFormAndCountry();
          } else {
            this.notify.error('District already exits...!!');
          }
          // ctrl:295-299 -- unconditional, runs on both success and failure.
          this.loadListAndCount();
        },
      });
  }

  // ---- update (mirrors updateDistrict, ctrl:385-438 -- no confirm modal) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl:401 guard (no else branch)
    const f = this.form();
    this.api
      .updateDistrictMaster({
        districtCode: f.districtCode,
        districtName: f.districtName,
        countryId: f.countryId,
        stateId: f.stateId,
        districtId: f.districtId as number,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('District updated successfully...!!');
            this.formSubmitted.set(false);
            this.resetFormAndCountry();
          } else {
            this.notify.error('District already exits...!!');
          }
          // ctrl:429-433 -- unconditional, runs on both success and failure.
          this.loadListAndCount();
        },
      });
  }

  // mirrors $scope.init() being re-invoked on a successful save/update
  // (ctrl:286, 420) -- resets the form to create-mode defaults AND
  // re-fetches the Country list only (State list fetch is dead/commented
  // out inside init(), ctrl:90-99). Only called from the SUCCESS branch.
  private resetFormAndCountry(): void {
    this.form.set(emptyDistrictForm());
    this.saveMode.set(true);
    this.loadCountryList();
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:308-342 -- DOES correctly
  // re-cascade the State list, unlike this codebase's
  // ambulance-service-provider-master sibling) ----
  edit(districtId: number): void {
    this.saveMode.set(false);
    this.api.getDistrictById(districtId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          window.alert('Error!!'); // ctrl:338 -- a real native alert, not growl
          return;
        }
        const d = res.listObject[0];
        const countryId = String(d.countryId);
        this.form.set({
          districtId: d.districtId,
          districtCode: d.districtCode,
          districtName: d.districtName,
          countryId,
          stateId: String(d.stateId),
        });
        // ctrl:333-334 -- re-cascades the State dropdown before the
        // stateId above is meant to be selectable.
        this.api.getStateListByCountryId(countryId).subscribe({
          next: (stateRes) => {
            if (stateRes.status === 'success') this.stateOptions.set(stateRes.listObject ?? []);
          },
        });
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:345-382 -- see class doc
  // comment for the district-specific "always reload list+count after"
  // divergence from this codebase's other status-toggle siblings) ----
  toggleStatus(row: DistrictRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.districtId === row.districtId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatusForDistrict({
        districtId: row.districtId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'District Updated Successfully...!!');
          } else {
            this.notify.warning(res.message ?? 'Warning!'); // ctrl:375-378 -- the server's own message
          }
          // ctrl:373/379 -- unconditional, runs on both success and
          // failure; this is what makes a failed toggle self-correct.
          this.loadListAndCount();
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:244-246/346-348/386-388. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
