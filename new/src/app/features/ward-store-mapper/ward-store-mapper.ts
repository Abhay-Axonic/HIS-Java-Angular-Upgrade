import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { WardStoreMapperService } from './ward-store-mapper.service';
import { WardOption, WardStoreMapperRow, WardStoreOption } from './ward-store-mapper.models';
import {
  createPaginationState,
  PaginationState,
  searchData,
  setPage,
  setFirstPage,
  setPreviousPage,
  setNextPage,
  setLastPage,
  getPagesList,
} from '../../core/client-pagination.util';

/**
 * Parity rebuild of
 * views/master/UnitMaster/assetManagement/ward-store-mapper.html (151 lines) +
 * resources/assets/js/controller/pharmacy/wardStoreMapperController.js
 * (372 lines, `wardStoreMapperController`, no `controllerAs`). Legacy state
 * `wardStoreMapper` (app.js:20441-20446) has NO `resolve` block at all --
 * confirmed by reading the state block directly -- so there is no
 * `moduleId` to gate this route on, same class as its sibling
 * `departmentStoreMapper` (app.js:20435-20439) immediately above it.
 *
 * This is the Ward-flavored twin of department-store-mapper, sharing the
 * same Java controller family, but its actual FRONTEND shape is simpler
 * and meaningfully different in one respect: it uses two plain
 * single-select `<select>` dropdowns (Store, Ward), not the
 * `ng-dropdown-multiselect` dual-checklist department-store-mapper uses --
 * so `edit()` below just sets two scalar ids directly, with none of that
 * screen's `findIndex`-into-an-options-array dance.
 *
 * ================================================================
 * BUG #1 FAITHFULLY REPRODUCED -- list never refreshes, PLUS a confirmed
 * form-reset asymmetry that falls out of the exact same root cause
 * ================================================================
 * `saveWardStoreMapper()`'s, `updateWardStoreMapper()`'s, and
 * `updateStatus()`'s SUCCESS handlers all call `$scope.wardStoremappedList()`
 * (ctrl:221, ctrl:351, ctrl:261) -- but that function is never defined
 * anywhere live: its only definition (ctrl:169-189) sits inside a JS block
 * comment (`/* ... *­/`), so the identifier resolves to `undefined` and
 * calling it throws a `TypeError` inside the `.then()` callback. Net
 * effect, reproduced below: none of Save/Update/Status-toggle ever
 * refreshes the list -- only the success toast fires.
 *
 * Reading the statement ORDER around that crash line by line surfaces a
 * second, genuinely surprising nuance -- worth being precise about because
 * it's backwards in each direction:
 * - In `saveWardStoreMapper()`/`updateWardStoreMapper()`, the broken call
 *   comes BEFORE `$scope.initCommon()` in the SUCCESS branch (ctrl:220-223,
 *   ctrl:350-353) -- so the thrown exception ALSO stops `initCommon()`
 *   from ever running: a successful Save/Update does NOT reset the form
 *   (dropdowns stay populated with what was just submitted; Update mode
 *   stays Update). Their ERROR branches call `initCommon()` with no
 *   broken call in front of it (ctrl:232-233, ctrl:362-363) -- so an
 *   ERROR response DOES reset the form. Net effect: success leaves the
 *   form alone, error resets it -- backwards from what you'd expect.
 * - In `updateStatus()`, it's the opposite: `initCommon()` runs BEFORE the
 *   broken `wardStoremappedList()` call in the SUCCESS branch
 *   (ctrl:259-261) -- so a successful status toggle DOES reset the form.
 *   Its ERROR branch has no `initCommon()` call at all (ctrl:263-270) --
 *   so an errored toggle leaves the form untouched.
 *
 * Reproduced exactly below: `save()`/`update()` call `resetForm()` only in
 * their error branch; `toggleStatus()` calls `resetForm()` only in its
 * success branch. None of the three ever reload the list.
 *
 * ================================================================
 * BUG #2 -- duplicate-mapping validation (not reproducible, nothing to do)
 * ================================================================
 * The backend's own duplicate-mapping validation is confirmed broken on
 * both save and update (checks the wrong table/column) -- nothing for the
 * frontend to do differently; this rebuild validates only that both Store
 * and Ward are selected client-side (`save()`/`update()` below), matching
 * legacy's own `wardObj.id`/`wardObj.wardId` empty-check (ctrl:197-208,
 * ctrl:327-338) and `growl.warning` wording verbatim.
 *
 * ================================================================
 * Other findings (not reproduced as behavior, recorded for completeness)
 * ================================================================
 * - **`wardObj.storeId` is a dead field.** Set once in `initCommon()`
 *   (ctrl:36) and again in `initWardStoreMapped()` (ctrl:305), but the
 *   Store `<select>`'s `ng-model` is bound to `wardObj.id` (view:11), NOT
 *   `wardObj.storeId` -- so `storeId` is never actually read from or
 *   populated by the UI. Not modeled here; `selectedStoreId` below maps
 *   directly to legacy's real `wardObj.id`.
 * - **`initWardStoreMapped()`/`$scope.wardStoreMapped` are dead state.**
 *   Called once, at the very top of `updateStatus()` (ctrl:249-250), but
 *   the object it builds is never read by anything else in the file --
 *   not sent to any endpoint, not bound in the view. Not modeled, same
 *   precedent as department-store-mapper's `deptStoreMapped` callout.
 * - **List/pagination is genuinely server-offset-paginated in legacy.**
 *   `masterList(organizationId, pager.startIndex, pager.pageSize, unitId)`
 *   re-POSTs to `getWardStoremappedListPagination` with a fresh
 *   offset/pageSize on every `setPage(page, true)` click (ctrl:97-132) --
 *   a real divergence from department-store-mapper/corrective-maintenance-
 *   worklist's "fetch everything once" `PaginationService`/
 *   `client-pagination.util` pattern. Per instruction, this rebuild still
 *   models pagination via `client-pagination.util` (this codebase's
 *   shared, already-established convention): `loadList()` below fetches
 *   the total count first (`getWardStoremappedListPaginationCount`), then
 *   issues a SINGLE `getWardStoremappedListPagination` call sized to that
 *   count (so the full result set arrives in one response), and hands it
 *   to `searchData`/`setPage`/etc. for all subsequent client-side paging.
 *   Both real endpoints are still genuinely called on load, just not
 *   re-called per page.
 * - **The search box filters the CURRENT PAGE only, matching legacy
 *   exactly** (view:95, `getWardStoremappedList|filter:searchKeyword`,
 *   applied directly to whatever page legacy's server call already
 *   returned). Because this rebuild holds the FULL list client-side (see
 *   bullet above) rather than one server page at a time, wiring the
 *   search box to `client-pagination.util`'s own `p.search`/`searchData`
 *   would incorrectly search the ENTIRE dataset, not just the visible
 *   page. To keep the observable behavior faithful, `searchKeyword`
 *   below is deliberately kept OUTSIDE `PaginationState.search` (which is
 *   never set) and instead filters `pagination().data` -- the
 *   already-paginated current-page slice -- via the `displayedRows`
 *   computed, the same "second, independent filter layered on top of an
 *   already-paginated slice" shape already established by this codebase's
 *   department-store-mapper.
 * - **No `<modal>`/confirm-dialog anywhere in this screen**, matching
 *   department-store-mapper -- Save/Update fire directly, no confirm step.
 */
@Component({
  selector: 'app-ward-store-mapper',
  imports: [FormsModule, StatusToggle],
  templateUrl: './ward-store-mapper.html',
  styleUrl: './ward-store-mapper.scss',
})
export class WardStoreMapper {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(WardStoreMapperService);

  private unitId = 0;
  private orgId = 0;
  private userId = 0;

  // ---- dropdown option lists (mirrors getStoreList/getWardList) ----
  storeOptions = signal<WardStoreOption[]>([]);
  wardOptions = signal<WardOption[]>([]);

  // ---- form/edit-mode state (mirrors wardObj.id/wardObj.wardId + saveBtnFlag/updateBtnFlag) ----
  selectedStoreId = signal<number | ''>('');
  selectedWardId = signal<number | ''>('');
  updateMode = signal(false);
  private currentMapperId = signal<number | ''>('');

  // ---- full mapping list + entirely client-side pagination (see class doc) ----
  private fullList: WardStoreMapperRow[] = [];
  pagination = signal<PaginationState<WardStoreMapperRow>>(createPaginationState<WardStoreMapperRow>());

  // ---- search box -- filters the CURRENT PAGE only, see class doc ----
  searchKeyword = signal('');
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.pagination().data;
    if (!keyword) return rows;
    return rows.filter((r) => r.storeName?.toLowerCase().includes(keyword) || r.wardName?.toLowerCase().includes(keyword));
  });

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.unitId = this.session.unitId ?? 0;
    this.orgId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.loadStoreOptions();
    this.loadWardOptions();
    this.loadList();
  }

  // ctrl:138-150 (storeList) -- no startLoader() call in legacy, only
  // stopLoader() on success; raw-array response, no envelope.
  private loadStoreOptions(): void {
    this.api.getStoreOptions(this.orgId, this.unitId, this.userId).subscribe({
      next: (list) => {
        this.loader.stop();
        this.storeOptions.set(list ?? []);
      },
    });
  }

  // ctrl:152-167 (wardList) -- same "no startLoader, stopLoader-on-success-only" shape.
  private loadWardOptions(): void {
    this.api.getWardOptions(this.orgId, this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        this.wardOptions.set(res.listObject ?? []);
      },
    });
  }

  // ctrl:57-95 (initMasterList) -- see class doc: fetches the full result
  // set once (sized by the real count endpoint) rather than re-issuing a
  // fresh server call per page like legacy's own setPage(page,true) does.
  loadList(): void {
    this.loader.start();
    this.api.getListCount(this.orgId, this.unitId).subscribe({
      next: (countRes) => {
        const total = countRes.object ?? 0;
        this.api
          .getPagedList({
            orgId: this.orgId,
            unitId: this.unitId,
            offset: 0,
            noOfRecordsPerPage: total > 0 ? total : 10,
            storeId: null,
            wardId: null,
          })
          .subscribe({
            next: (listRes) => {
              this.loader.stop();
              this.fullList = listRes.listObject ?? [];
              const p = this.pagination();
              searchData(this.fullList, p);
              this.pagination.set({ ...p });
            },
            error: () => this.loader.stop(),
          });
      },
      error: () => this.loader.stop(),
    });
  }

  setPageSize(): void {
    const p = this.pagination();
    searchData(this.fullList, p);
    this.pagination.set({ ...p });
  }

  page(action: 'first' | 'prev' | 'next' | 'last' | number): void {
    const p = this.pagination();
    if (action === 'first') setFirstPage(p);
    else if (action === 'prev') setPreviousPage(p);
    else if (action === 'next') setNextPage(p);
    else if (action === 'last') setLastPage(p);
    else setPage(action, p);
    this.pagination.set({ ...p });
  }

  pagesList(): number[] {
    return getPagesList(this.pagination());
  }

  // ---- save (mirrors saveWardStoreMapper, ctrl:192-242) ----
  save(): void {
    if (this.selectedStoreId() === '' || this.selectedStoreId() == null) {
      this.notify.warning('Please select store..!');
      return;
    }
    if (this.selectedWardId() === '' || this.selectedWardId() == null) {
      this.notify.warning('Please select ward..!');
      return;
    }
    this.loader.start();
    this.api
      .save({
        id: this.selectedStoreId(),
        wardId: this.selectedWardId(),
        orgId: this.orgId,
        unitId: this.unitId,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Department Store Mapped Save Successfully..!'); // ctrl:216-219, verbatim (legacy's own copy-paste from department-store-mapper)
            // ctrl:220-223 -- wardStoremappedList() throws before
            // initCommon() runs; form deliberately left populated, no
            // reload. See class doc's bug #1.
          } else {
            this.notify.error(res.message ?? 'Error!');
            // ctrl:232-233 -- error branch's initCommon() has no broken
            // call in front of it, so it runs fine -- form DOES reset on
            // error (opposite of the success case). See class doc.
            this.resetForm();
          }
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:279-300) ----
  edit(wardStoreMapperId: number): void {
    // ctrl:282-283 -- legacy flips saveBtnFlag/updateBtnFlag to "Update"
    // mode IMMEDIATELY on click, before the GET even resolves.
    this.updateMode.set(true);
    this.loader.start();
    this.api.getById(this.orgId, this.unitId, wardStoreMapperId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status !== 'success' || !res.object) {
          window.alert('Error In Fetching Data'); // ctrl:293-294
          return;
        }
        const detail = res.object;
        this.currentMapperId.set(detail.wardStoreMapperId);
        this.selectedStoreId.set(detail.id);
        this.selectedWardId.set(detail.wardId);
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- update (mirrors updateWardStoreMapper, ctrl:320-370) ----
  update(): void {
    if (this.selectedStoreId() === '' || this.selectedStoreId() == null) {
      this.notify.warning('Please select store..!');
      return;
    }
    if (this.selectedWardId() === '' || this.selectedWardId() == null) {
      this.notify.warning('Please select ward..!');
      return;
    }
    this.loader.start();
    this.api
      .update({
        wardStoreMapperId: this.currentMapperId(),
        id: this.selectedStoreId(),
        wardId: this.selectedWardId(),
        orgId: this.orgId,
        unitId: this.unitId,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Department Strore Mapped Update successfully'); // ctrl:346-349, verbatim (legacy's own typo)
            // ctrl:350-353 -- same unreachable-reset nuance as save(); see class doc.
          } else {
            this.notify.error(res.message ?? 'Error!');
            this.resetForm(); // ctrl:362-363 -- runs fine on error, see class doc.
          }
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:245-276) ----
  toggleStatus(row: WardStoreMapperRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // Optimistic flip -- the view's checkbox is two-way bound in legacy,
    // so the visual change happens immediately regardless of the response
    // either way (same precedent as department-store-mapper's toggleStatus).
    this.fullList = this.fullList.map((r) => (r.wardStoreMapperId === row.wardStoreMapperId ? { ...r, status: nextStatus } : r));
    const p = this.pagination();
    searchData(this.fullList, p);
    this.pagination.set({ ...p });

    this.loader.start();
    this.api.updateStatus(this.orgId, this.unitId, row.wardStoreMapperId, nextStatus).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.notify.success('Status Update Successfully..!');
          // ctrl:259-261 -- initCommon() runs BEFORE the broken
          // wardStoremappedList() call here (unlike save()/update()
          // above), so the form DOES reset on a successful toggle. See
          // class doc's bug #1 asymmetry callout.
          this.resetForm();
        } else {
          this.notify.error(res.message ?? 'Something went wrong...!!');
          // ctrl:263-270 -- error branch has NO initCommon() call at all;
          // form is left untouched on error (opposite of save()/update()'s
          // asymmetry). See class doc.
        }
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- reset (mirrors initCommon, ctrl:32-50) ----
  private resetForm(): void {
    this.updateMode.set(false);
    this.currentMapperId.set('');
    this.selectedStoreId.set('');
    this.selectedWardId.set('');
  }
}
