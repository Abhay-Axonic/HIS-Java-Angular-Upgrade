import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { Subject, debounceTime, switchMap } from 'rxjs';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { UserAuthenticationService } from './user-authentication.service';
import { EmployeeTypeaheadResult, RoleTypeaheadResult, UserAuthenticationRow } from './user-authentication.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/userManagement/user-authentication.html +
 * userAuthenticationMasterController.js. Module id 676 (confirmed live
 * from the real `.state(...)` resolve block).
 *
 * **Deliberate, flagged scope reduction -- not a settled decision:**
 * per docs/DECISION_NEEDED_UserAuthenticationPlaintextPasswordDisclosure.md
 * (open, awaiting owner sign-off), this rebuild calls the real
 * `getUserAuthenticationList` endpoint to populate the table -- there is no
 * alternative endpoint, the list and the plaintext-password data are the
 * same API response -- but deliberately omits every password-viewing
 * feature: the "view password" icon/link, the `#viewPassword` modal, and
 * `getPassword()` are not built at all, and the raw response's `password`
 * field is never read, bound, stored, or rendered anywhere in this
 * component (see user-authentication.models.ts). `sendPasswordToUser`
 * (orphaned in legacy's own view -- no `ng-click` reaches it) and
 * `resendUserNameAndPassword`/`resendPassword` (live in legacy, but part of
 * the same plaintext-password-transmission endpoint family) are also not
 * built. This interpretation is provisional pending the owner's explicit
 * answer to that doc's open question 3.
 *
 * Also out of scope for this pass, independent of the security question
 * above: the "Change Password & Expiry" modal (`updateUserPassword`,
 * `/adt/updatePassword` -- a write-only reset, not a disclosure) and
 * `resendUserName` (`/adt/resendUserName` -- doesn't transmit a password)
 * were not requested for this build and are not implemented; only the
 * list/search/pagination surface was in scope here. Both are legitimate,
 * separate candidates for a follow-up pass since neither touches the
 * plaintext-password issue.
 *
 * No status toggle, no role-assignment UI: the Status and Role columns are
 * both fully commented out in the live legacy view
 * (user-authentication.html:90-119) -- there is no live toggle or
 * role-assignment UI on this screen today, so building either would be
 * over-delivering relative to legacy parity.
 *
 * Pagination: legacy loads the full unit-scoped list on every search (no
 * offset/page params sent to the backend) and paginates client-side via
 * `PaginationService.searchData` -- mirrored here with a computed
 * client-side slice, the same pattern already used by
 * assigning-picker-dispenser.ts, rather than billing-group-master.ts's
 * server-side offset/count pair.
 */
@Component({
  selector: 'app-user-authentication',
  imports: [FormsModule],
  templateUrl: './user-authentication.html',
  styleUrl: './user-authentication.scss',
})
export class UserAuthentication {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(UserAuthenticationService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;

  // ---- search fields (mirrors $scope.common.roleName / common.empName) ----
  roleName = signal('');
  employeeName = signal('');

  private readonly roleSearch$ = new Subject<string>();
  private readonly employeeSearch$ = new Subject<string>();
  roleResults = signal<RoleTypeaheadResult[]>([]);
  employeeResults = signal<EmployeeTypeaheadResult[]>([]);

  // ---- list + client-side pagination (mirrors PaginationService.searchData) ----
  rows = signal<UserAuthenticationRow[]>([]);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.rows().length / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));
  pagedRows = computed(() => {
    const limit = this.pageSize();
    const offset = (this.currentPage() - 1) * limit;
    return this.rows().slice(offset, offset + limit);
  });

  constructor() {
    // mirrors userAuthenticationMasterController.js:159 --
    // `$scope.getUserAuthenticatedList()` fires immediately on load.
    this.search();

    this.roleSearch$
      .pipe(
        debounceTime(0),
        switchMap((term) => this.api.searchRoleByName(this.organizationId, this.unitId, term)),
      )
      .subscribe({
        next: (res) => this.roleResults.set(res.status === 'success' ? (res.listObject ?? []) : []),
      });

    this.employeeSearch$
      .pipe(
        debounceTime(0),
        switchMap((term) => this.api.searchEmployeeByName(this.organizationId, this.unitId, term)),
      )
      .subscribe({
        next: (res) => this.employeeResults.set(res.status === 'success' ? (res.listObject ?? []) : []),
      });
  }

  // ---- typeaheads (mirrors getListByRoleName / getListByUserName) ----
  onRoleNameChange(term: string): void {
    this.roleName.set(term);
    if (term.length >= 1) {
      this.roleSearch$.next(term);
    } else {
      this.roleResults.set([]);
    }
  }

  selectRole(result: RoleTypeaheadResult): void {
    // mirrors selectRoleId: only common.roleName ends up sent to the
    // search call -- the selected roleId is captured, same as legacy, but
    // never actually transmitted by getUserAuthenticatedList().
    this.roleName.set(result.roleName);
    this.roleResults.set([]);
  }

  onEmployeeNameChange(term: string): void {
    this.employeeName.set(term);
    if (term.length >= 1) {
      this.employeeSearch$.next(term);
    } else {
      this.employeeResults.set([]);
    }
  }

  selectEmployee(result: EmployeeTypeaheadResult): void {
    // mirrors selectUserId: common.empName is set from the selected result;
    // the selected userId is captured, same as legacy, but never actually
    // transmitted by getUserAuthenticatedList().
    this.employeeName.set(result.empName);
    this.employeeResults.set([]);
  }

  // ---- search (mirrors getUserAuthenticatedList) ----
  search(): void {
    this.api.getList(this.organizationId, this.unitId, this.roleName(), this.employeeName()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.rows.set(res.listObject ?? []);
          this.currentPage.set(1);
        }
      },
      error: () => this.notify.error('Failed to load list'),
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }
}
