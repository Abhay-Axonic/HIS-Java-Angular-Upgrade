import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { UserStoreMapperService } from './user-store-mapper.service';
import { StoreOption, UserOption, UserStoreMapperRow } from './user-store-mapper.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/userManagement/user-store-mapper.html +
 * resources/assets/js/controller/master/global/userStoreMapperController.js.
 * State `userStoreMapper` (app.js:17609-17633), moduleId 690, no
 * `controllerAs` (plain `$scope`).
 *
 * **Deliberate deviations/preserved quirks, not bugs in this rebuild:**
 * - See user-store-mapper.service.ts's header comment: the "Show N
 *   entries" dropdown reuses the same real, offset-honoring list endpoint
 *   for every load rather than reproducing legacy's separate
 *   always-page-1-of-10 `initMasterList` call. A judgment call, not the
 *   flagged bug below.
 * - The search box (`searchKeyword`) filters only the CURRENTLY-FETCHED
 *   page's rows by employee name, matching the bare `filter:searchKeyword`
 *   AngularJS expression in the legacy view -- it was never wired to a
 *   real server-side search. Same precedent as asset-category-master's
 *   `displayedRows`.
 * - `update()` (ctrl:440-500) performs NO required-field validation at
 *   all, unlike `save()` (ctrl:344-436) which checks user/employeeName/
 *   empCode/selected-store-list in that order before posting. This
 *   asymmetry is real legacy behavior (same class of asymmetry as
 *   organization-master's save-vs-update mobile-number check) and is
 *   reproduced as-is: `update()` below skips `validateForSave()` entirely.
 * - `updateStatus()` (ctrl:260-291) resets only the top form's user/
 *   empCode/employeeName fields and Save/Update button flags on success
 *   (`$scope.initCommon()`), and does NOT touch the dual-list picker
 *   (available/mapped store state) or reload the list. `save()`/`update()`
 *   additionally clear the picker afterward (ctrl:416-419/481-483). Two
 *   different reset scopes, reproduced as two separate private methods
 *   (`resetForm()` vs `clearPicker()`) rather than one shared "clear
 *   everything" helper.
 * - **The one bug this batch is told to reproduce, not fix**
 *   (`showUpdateBtn`, ctrl:314-319): on edit-load, legacy iterates the
 *   sparse `storeList` checkbox map and, for each previously-touched
 *   entry, does `tempSelectStore.indexOf(value)` where `value` is the
 *   BOOLEAN checkbox state -- never a match against the `{storeId,
 *   storeName}` objects `tempSelectStore` actually holds, so `indexOf`
 *   always returns -1 and `splice(-1, 1)` silently removes the LAST
 *   element of `tempSelectStore` instead of the one actually being
 *   cleared. The visual checkbox state (`storeChecked`) still resets
 *   correctly; only the parallel `tempSelectStore` array used by the
 *   "<< >>" `selectStore()` re-sync button silently corrupts. Reproduced
 *   verbatim in `edit()` below -- do NOT fix.
 */
@Component({
  selector: 'app-user-store-mapper',
  imports: [FormsModule, StatusToggle],
  templateUrl: './user-store-mapper.html',
  styleUrl: './user-store-mapper.scss',
})
export class UserStoreMapper {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(UserStoreMapperService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly loggedInUserId = this.session.userId ?? 0;

  // ---- top form (mirrors $scope.common's userId/empCode/employeeName + button flags) ----
  saveBtnFlag = signal(true);
  updateBtnFlag = signal(false);
  selectedUserId = signal<number | null>(null);
  empCode = signal('');
  employeeName = signal('');

  usersList = signal<UserOption[]>([]);
  sortedUsers = computed(() => [...this.usersList()].sort((a, b) => a.employeeName.localeCompare(b.employeeName)));

  // ---- dual-list picker (mirrors storeList/tempSelectStore/selectedStoreList1) ----
  availableStores = signal<StoreOption[]>([]);
  storeChecked = signal<Record<number, boolean>>({});
  private tempSelectStore = signal<StoreOption[]>([]);
  selectedStoreList = signal<StoreOption[]>([]);

  // ---- list + pagination (real, offset-honoring endpoint -- see service header) ----
  commonList = signal<UserStoreMapperRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters the CURRENTLY-FETCHED page only (see class comment).
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.commonList();
    return this.commonList().filter((r) => r.employeeName?.toLowerCase().includes(keyword));
  });

  // ---- mapped-store detail modal (mirrors #userNursingStationDetailsModal) ----
  showDetailModal = signal(false);
  detailStoreList = signal<StoreOption[]>([]);

  constructor() {
    this.api.getStoreList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.availableStores.set(res.listObject ?? []);
      },
    });
    this.api.getUsersList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.usersList.set(res.listObject ?? []);
      },
    });
    this.loadList();
  }

  /**
   * Matches legacy's `initMasterList` -- fired on init and after every
   * save/update, always via the `userNameWise` endpoint, always resetting
   * to page 1 / size 10 regardless of what the user had selected (a real
   * legacy quirk, confirmed via golden-trace re-audit, not a rebuild bug).
   */
  private loadList(): void {
    this.currentPage.set(1);
    this.pageSize.set(PAGE_SIZE_OPTIONS[0]);
    this.api.getList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.commonList.set(res.listObject ?? []);
      },
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  /** Matches legacy's `masterList` -- real page/size navigation, honors the requested offset/size. */
  private loadPage(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getPage(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.commonList.set(res.listObject ?? []);
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadPage();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage();
  }

  // ---- user select (mirrors usrDetails()) ----
  onUserSelect(id: number | null): void {
    this.selectedUserId.set(id);
    if (id != null) this.applyUserDetails(id);
  }

  private applyUserDetails(id: number): void {
    const match = this.usersList().find((u) => u.userId === id);
    if (match) {
      this.empCode.set(match.empCode);
      this.employeeName.set(match.employeeName);
    }
  }

  // ---- dual-list picker (mirrors addTempStore()/selectStore()) ----
  onStoreCheckboxChange(store: StoreOption, checked: boolean): void {
    this.storeChecked.update((s) => ({ ...s, [store.storeId]: checked }));
    this.addTempStore(store.storeId, store.storeName);
  }

  private addTempStore(storeId: number, storeName: string): void {
    let flag = false;
    this.tempSelectStore.update((arr) => {
      const idx = arr.findIndex((v) => v.storeId === storeId);
      if (idx === -1) return arr;
      flag = true;
      const copy = [...arr];
      copy.splice(idx, 1);
      return copy;
    });
    if (flag) {
      this.storeChecked.update((s) => ({ ...s, [storeId]: false }));
    } else {
      this.tempSelectStore.update((arr) => [...arr, { storeId, storeName }]);
    }
  }

  selectStore(): void {
    this.selectedStoreList.set(this.tempSelectStore().map((s) => ({ ...s })));
  }

  // ---- mapped-store detail modal (mirrors getStoreByUserId()) ----
  openDetailModal(userId: number): void {
    this.showDetailModal.set(true);
    this.detailStoreList.set([]);
    this.api.getDetailedStoreList(userId, this.organizationId, this.unitId).subscribe({
      next: (res) => this.detailStoreList.set(res.listObject ?? []),
    });
  }

  closeDetailModal(): void {
    this.showDetailModal.set(false);
  }

  // ---- status toggle (mirrors updateStatus()) ----
  toggleStatus(row: UserStoreMapperRow, nextStatus: boolean): void {
    this.commonList.update((rows) => rows.map((r) => (r.userId === row.userId ? { ...r, isActive: nextStatus } : r)));
    this.api.updateStatus(row.userId, this.organizationId, this.unitId, nextStatus).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Success');
          // Legacy's updateStatus() only calls initCommon() on success --
          // it resets the top form, not the dual-list picker, and does
          // not reload commonList. See class header comment.
          this.resetForm();
        } else {
          this.notify.error(res.message ?? 'Error');
        }
      },
    });
  }

  // ---- edit (mirrors showUpdateBtn()) ----
  edit(userId: number): void {
    this.saveBtnFlag.set(false);
    this.updateBtnFlag.set(true);
    this.api.getById(userId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success') {
          this.notify.error('Error In Fetching Data');
          return;
        }
        const rows = res.listObject ?? [];
        const uid = rows[0]?.userId;
        if (uid == null) return;

        this.selectedUserId.set(uid);
        this.applyUserDetails(uid);

        // ---- reset-on-edit: legacy bug reproduced verbatim, do NOT fix
        // (see class header comment for the full explanation) ----
        this.selectedStoreList.set([]);
        const touched = this.storeChecked();
        for (const key of Object.keys(touched)) {
          const storeId = Number(key);
          this.storeChecked.update((s) => ({ ...s, [storeId]: false }));
          this.tempSelectStore.update((arr) => {
            const copy = [...arr];
            copy.splice(-1, 1); // wrong-element removal -- reproduced verbatim
            return copy;
          });
        }

        // ---- populate with the user's actually-mapped stores ----
        for (const r of rows) {
          const store: StoreOption = { storeId: r.storeId, storeName: r.storeName };
          this.tempSelectStore.update((arr) => [...arr, store]);
          this.storeChecked.update((s) => ({ ...s, [r.storeId]: true }));
          this.selectedStoreList.update((arr) => [...arr, store]);
        }
      },
    });
  }

  // ---- save (mirrors save()) ----
  save(): void {
    if (this.selectedUserId() == null) {
      this.notify.warning('Please select user.');
      return;
    }
    if (!this.employeeName()) {
      this.notify.warning('Please enter employee name');
      return;
    }
    if (!this.empCode()) {
      this.notify.warning('Please enter employee code');
      return;
    }
    if (this.selectedStoreList().length === 0) {
      this.notify.warning('Please select store');
      return;
    }

    const now = formatDateTime(new Date());
    this.api
      .save({
        userId: this.selectedUserId() as number,
        organizationId: this.organizationId,
        unitId: this.unitId,
        isActive: true,
        isDeleted: false,
        addedBy: this.loggedInUserId,
        addedDate: now,
        updatedBy: this.loggedInUserId,
        updatedDate: now,
        selectUserStoreMapperList: this.selectedStoreList().map((s) => s.storeId),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success');
            this.resetForm();
            this.clearPicker();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Error');
          }
        },
      });
  }

  // ---- update (mirrors update() -- NO validation, see class header comment) ----
  update(): void {
    const now = formatDateTime(new Date());
    this.api
      .update({
        userId: this.selectedUserId() as number,
        organizationId: this.organizationId,
        unitId: this.unitId,
        isActive: true,
        isDeleted: false,
        addedBy: this.loggedInUserId,
        addedDate: now,
        updatedBy: this.loggedInUserId,
        updatedDate: now,
        selectUserStoreMapperList: this.selectedStoreList().map((s) => s.storeId),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success');
            this.resetForm();
            this.clearPicker();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Error');
          }
        },
      });
  }

  private resetForm(): void {
    this.saveBtnFlag.set(true);
    this.updateBtnFlag.set(false);
    this.selectedUserId.set(null);
    this.empCode.set('');
    this.employeeName.set('');
  }

  private clearPicker(): void {
    this.selectedStoreList.set([]);
    this.storeChecked.set({});
    this.tempSelectStore.set([]);
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
