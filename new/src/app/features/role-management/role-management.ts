import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { RoleManagementService } from './role-management.service';
import { RoleRow, RoleUserRow } from './role-management.models';
import {
  createPaginationState,
  PaginationState,
  searchData,
  setFirstPage,
  setLastPage,
  setNextPage,
  setPage,
  setPreviousPage,
  getPagesList,
} from '../../core/client-pagination.util';

/**
 * Parity rebuild of
 * views/master/UnitMaster/userManagement/role-management.html +
 * resources/assets/accessControl/controller/roleManagementMasterController.js
 * (`roleManagementMasterController`, plain `$scope`, no `controllerAs` --
 * confirmed 0 `vm.*` bindings in the view). Legacy state `roleManagement`
 * (app.js:16169-16196), gated by a resolve-block check for moduleId 675 --
 * wired at the ROUTE level via `moduleAccessGuard` (`data: { moduleId: 675 }`),
 * not in this component. No `requireSuperUser` check in the resolve block.
 *
 * **Step 0 confirmed this is a real, singly-wired screen.** Confirmed via
 * grep: exactly one `.state('roleManagement', ...)` registration exists
 * anywhere in the codebase. Two adjacent, genuinely DIFFERENT sibling
 * states exist in the same Unit-User-Management family --
 * `userRoleManagement` (moduleId 674, `userRoleManagementMasterController`,
 * a much larger per-user privilege-configuration screen) and `roleCreation`
 * (moduleId 672, `roleCreationMasterController`, where new roles are
 * actually created) -- `roleManagement` itself has NO create-role
 * capability at all; it only lists existing roles, toggles their status,
 * and views which users hold a role. Not a duplicate/dead-route pair.
 *
 * **Backend confirmed real end to end** for all 4 endpoints this screen
 * calls, in legacy/Global/src/main/java/com/param/global/unit/{controller,
 * service,dao}/UserRoleMaster*, UserRoleMapper* -- see service file's doc
 * comment for the one investigated-and-ruled-out anomaly
 * (`UserRoleMasterController`'s trailing-space `@RequestMapping`, confirmed
 * harmless via the live Tomcat access log, not a live routing bug).
 *
 * **Judgment calls / preserved quirks, not bugs introduced by this rebuild:**
 *
 * - **Client-side "fetch everything once, paginate/search in the browser"
 *   pattern** (`PaginationService`, ctrl:36-50/62-86), NOT the server-side
 *   offset/limit pagination `doctorCategoryMaster`/`ambulanceTypeMaster` use
 *   -- `getActiveUserRoleMasterList` returns the FULL active-role list in
 *   one call, no separate count endpoint. Reproduced via
 *   `client-pagination.util.ts` (the same shared helper
 *   `scheduleWorklist`/`PMSchedule` already use for this exact pattern),
 *   not the Phase-3-scaffold's server-side pagination.
 *
 * - **The search box only filters the CURRENT PAGE's already-paginated
 *   slice, and does so WITHOUT ever calling `PaginationService.searchData`
 *   at all** -- confirmed by reading the view directly
 *   (role-management.html:72): `ng-repeat="info in pagination.data |
 *   filter:searchKeyword track by $index"` is a bare Angular `filter`
 *   layered on top of `pagination.data` (the already-sliced current page),
 *   with `searchKeyword` never wired to `pagination.search`/`searchData()`
 *   anywhere. This is narrower than even the already-documented
 *   "current-page-only search" quirk on other screens (`ambulanceTypeMaster`/
 *   `doctorCategoryMaster`): here it isn't a deliberate design choice
 *   reachable via `searchData`, the plumbing for a real full-dataset search
 *   exists (`pagination.searchData`) and is simply never invoked by
 *   anything in the view. Reproduced faithfully via `displayedRows` below
 *   (filters `pagination().data` only), not "fixed" into a real search.
 *
 * - **Status toggle** (`updateRoleStatus`, ctrl:147-180) reuses
 *   `$scope.common` the same way `doctorCategoryMaster.updateStatus` does --
 *   confirmed harmless the same way (the DAO loads the row fresh via
 *   `findById(roleId)` and only sets `status`/`updatedBy`/`updatedDate`).
 *   4 fields sent, no more.
 *
 * - **"View User" modal is fully real and live** (`getUserDetails`,
 *   ctrl:90-108): fetches the real user list for that role. Its own
 *   trash-icon "delete" (really: sets that one user-role mapping to
 *   Inactive, `updateUserStatus`, ctrl:110-146) has NO confirm dialog and,
 *   on success, does NOT refresh or remove the row from the still-open
 *   modal's user list (ctrl:125-142's success handler only growls and
 *   stops the loader -- no re-fetch, no splice). Reproduced faithfully:
 *   this rebuild's `removeUserFromRole` also leaves the modal's list
 *   untouched after a successful removal, matching legacy's own stale-list
 *   behavior rather than "fixing" it with an optimistic update.
 *
 * - **"View Role" (info-circle icon) opens a genuinely non-functional
 *   mockup in legacy** -- confirmed by reading `views/common/add-view-role.html`
 *   directly: the `#viewRoleModal` it contains is a raw Bootstrap modal (no
 *   `ng-click`/`ng-model` anywhere), a hardcoded Kendo checkbox tree with
 *   static OPD/Billing placeholder items completely unrelated to whichever
 *   role's info-circle was actually clicked. Not wired to any controller
 *   function at all -- confirmed not reachable via any `ng-click` in
 *   `role-management.html` either (`data-toggle="modal"` fires it via raw
 *   jQuery/Bootstrap, bypassing Angular entirely). Reproduced here as an
 *   equally inert placeholder dialog (see `viewRoleModalOpen`) rather than
 *   inventing a real permission-tree feature legacy itself never built.
 *   The separate `#addRoleModal` markup in the same partial is unreachable
 *   from this screen at all (no `data-target="#addRoleModal"` anywhere in
 *   role-management.html) and is not reproduced here.
 */
@Component({
  selector: 'app-role-management',
  imports: [FormsModule, StatusToggle],
  templateUrl: './role-management.html',
  styleUrl: './role-management.scss',
})
export class RoleManagement {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(RoleManagementService);

  private readonly organisationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  private fullList: RoleRow[] = [];
  pagination = signal<PaginationState<RoleRow>>(createPaginationState<RoleRow>());
  searchKeyword = signal('');

  // Legacy quirk preserved (see class doc comment): searchKeyword filters
  // only the CURRENT PAGE's already-sliced rows, and is never connected to
  // PaginationService.searchData/pagination.search at all.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.pagination().data;
    if (!keyword) return rows;
    return rows.filter((r) => r.roleName?.toLowerCase().includes(keyword));
  });

  showUserModal = signal(false);
  userList = signal<RoleUserRow[]>([]);
  private activeRoleForUsers: RoleRow | undefined;

  showViewRoleModal = signal(false);

  constructor() {
    this.loadRoleList();
  }

  private loadRoleList(): void {
    this.api.getActiveRoleList({ organisationId: this.organisationId, unitId: this.unitId }).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.fullList = res.listObject ?? [];
          if (this.fullList.length > 0) {
            const p = this.pagination();
            searchData(this.fullList, p);
            this.pagination.set({ ...p });
          }
        }
      },
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

  // ---- status toggle (mirrors updateRoleStatus -- see class doc comment
  // on why only 4 fields are sent). Legacy's checkbox is two-way-bound
  // directly to the row object in `pagination.data` and NEVER re-fetches
  // or re-runs searchData() on toggle (ctrl:147-180's success handler is
  // growl-only) -- so this does NOT reset currentPage/search the way a
  // fresh searchData() call would; it only replaces the one row, in both
  // the source list and the currently-displayed page, matching legacy's
  // in-place mutation without disturbing pagination state. ----
  toggleStatus(row: RoleRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api
      .updateRoleStatus({
        roleId: row.roleId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            const replace = (r: RoleRow) => (r.roleId === row.roleId ? { ...r, status: nextStatus } : r);
            this.fullList = this.fullList.map(replace);
            const p = this.pagination();
            p.data = p.data.map(replace);
            p.filteredData = p.filteredData.map(replace);
            this.pagination.set({ ...p });
            this.notify.success(res.message ?? 'Updated Successfully.');
          } else {
            this.notify.error(res.message ?? 'Something went wrong.');
          }
        },
        error: () => this.notify.error('Something went wrong.'),
      });
  }

  // ---- View User modal (mirrors getUserDetails -- fully real/live) ----
  openUserModal(row: RoleRow): void {
    this.activeRoleForUsers = row;
    this.userList.set([]);
    this.api.getUserListByRoleId({ organisationId: this.organisationId, unitId: this.unitId, roleId: row.roleId }).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.userList.set(res.listObject ?? []);
        }
      },
    });
    this.showUserModal.set(true);
  }

  closeUserModal(): void {
    this.showUserModal.set(false);
  }

  // Legacy quirk preserved (see class doc comment): no confirm dialog, and
  // the modal's own list is NOT refreshed/spliced after a successful
  // removal -- matches ctrl:110-146's success handler exactly (growl only).
  removeUserFromRole(userRoleMpprId: number): void {
    this.api
      .updateUserRoleMapperStatus({
        userRoleMpprId,
        status: 'I',
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Updated Successfully.');
          } else {
            this.notify.error(res.message ?? 'Something went wrong.');
          }
        },
        error: () => this.notify.error('Something went wrong.'),
      });
  }

  // ---- View Role modal (mirrors legacy's own non-functional mockup --
  // see class doc comment) ----
  openViewRoleModal(): void {
    this.showViewRoleModal.set(true);
  }

  closeViewRoleModal(): void {
    this.showViewRoleModal.set(false);
  }
}

/** `moment(currentDate).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:112-113/149-150. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
