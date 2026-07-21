import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { UserCounterMapperService } from './user-counter-mapper.service';
import {
  CounterOption,
  DetailedCounterItem,
  SelectedCounter,
  UserCounterMapperRow,
  UserOption,
} from './user-counter-mapper.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/userManagement/user-counter-mapper.html +
 * resources/assets/js/controller/master/global/userCounterMapperController.js.
 * moduleId 715, gated at the ROUTE level via `moduleAccessGuard` -- same
 * convention as every other rebuilt screen so far, not in this component.
 *
 * Closest structural sibling: `doctor-cabin-mapper` (same dual-list-picker
 * shape, same `showUpdateBtn`/`indexOf`-on-a-boolean bug family). Used as the
 * primary structural template; every place this screen's own legacy source
 * diverges from that sibling's is called out explicitly below rather than
 * silently harmonized.
 *
 * **Flagship bug, reproduced faithfully, NOT fixed (ctrl:363-367):** before
 * repopulating the "Mapped Counter" list for the user being edited, legacy
 * loops over every counterId ever toggled this session
 * (`$scope.userCounterMapper`) and does `$scope.selectCounter.indexOf(value)`
 * where `value` is the BOOLEAN checked-state, never one of the
 * `{id, name, code}` objects actually stored in `selectCounter` -- `indexOf`
 * on a primitive against an array of objects is always `-1`, so the
 * resulting `splice(-1, 1)` silently removes whatever is currently the LAST
 * entry of `selectCounter` instead of the entry that actually belongs to
 * that counterId. Reproduced below in `edit()`: one wrong-entry removal per
 * distinct counterId ever tracked in `checkedCounters`, keyed the same way
 * legacy's own loop is keyed -- the exact convention `doctor-cabin-mapper.ts`
 * established for this identical bug (judgment call there, carried over
 * here unchanged: legacy's array-indexed-by-id is modeled as a
 * `Record<counterId, boolean>` instead of a sparse array with holes, so this
 * reproduces "wrong-entry removal, once per id ever toggled" -- the
 * qualitative bug -- without inventing a fake "array holes" concept for a
 * Record).
 *
 * **Other real, deliberately-preserved behaviors/asymmetries (not bugs to
 * silently fix or harmonize with the sibling):**
 * - `$scope.common.counterDesc`/`counterCode`/`employeeName`/`employeeCode`/
 *   `empName` (ctrl:55-67 initMaster) are set to `""` on init and NEVER
 *   reassigned anywhere else in the controller -- not in `showUpdateBtn`,
 *   not from any `<select>` binding. `save()`/`update()` read these same
 *   dead-empty fields straight into the POST/PUT payload (ctrl:263-266/
 *   405-408). Reproduced: this rebuild's `save()`/`update()` send these five
 *   fields as literal `''` too, not wired to any UI control -- this is a
 *   genuine legacy characteristic, not an oversight introduced here.
 * - `save()` (ctrl:285-304) validates userId-empty and counter-list-empty
 *   before posting (`growl.warning`, no HTTP call). `update()` (ctrl:389-458)
 *   has NEITHER check -- it always posts, even with an empty user or an
 *   empty counter list. Same asymmetry class as the sibling.
 * - `save()`'s success handler resets `counterList` to `[]` AND re-calls
 *   `searchGroupList()` to refetch it (ctrl:319-320) -- a DIFFERENCE from
 *   the sibling (`doctor-cabin-mapper`'s `save()` does not refetch its
 *   cabin list on success). Reproduced: this rebuild's `save()` calls
 *   `loadCounterList()` again on success; `update()`'s success handler does
 *   the same (ctrl:439-440) -- unlike the sibling, BOTH save and update
 *   refetch the counter list here, not just save.
 * - `save()`'s success handler also resets `selectCounter` to `[]`
 *   (ctrl:322); `update()`'s success handler does NOT (ctrl:436-442 has no
 *   equivalent line) -- a real asymmetry, not harmonized here. Same
 *   asymmetry class as the sibling's `docSelectCabin`.
 * - `updateStatus()` (ctrl:460-496) resets the ENTIRE form on success
 *   (`initMaster()` + clearing `selectedCounterList`/`userCounterMapper`)
 *   AND ALSO resets `counterList` + refetches it via `searchGroupList()`
 *   (ctrl:479-480) -- again a difference from the sibling's `updateStatus`
 *   equivalent, which does neither. Reproduced in `toggleStatus()`. Never
 *   reverts the toggle on failure (optimistic).
 * - `getCounterListMasterByUserId()` (ctrl:499-518, the info-icon "Details"
 *   modal) assigns `$scope.getListObject` with NO `status === 'success'`
 *   check at all -- reproduced in `openDetails()` with the same
 *   unconditional assignment.
 * - `listOfCounters()` (ctrl:138-146, the "<< >>" commit button) does an
 *   EXTRA de-duplication pass on `selectCounter` (by `.id`) before copying
 *   it into `selectedCounterList` -- the sibling's `selectCabin()` has no
 *   such dedupe. Reproduced in `selectCounter()` below: dedupes
 *   `selectCounterWorking` by `id` before committing.
 * - The user `<select>` (view:13) has NO `ng-change` at all in legacy --
 *   selecting a user never auto-populates anything else on this screen.
 * - The bottom table's `orderBy:column:reverse` (view:132) is permanently
 *   inert -- `column`/`reverse` are never assigned anywhere in the
 *   controller, and no header in the view has a click handler to set them.
 *   Not modeled (dead) -- only the `filter:searchKeyword` half is
 *   reproduced, via `displayedRows`.
 * - The search box (view:92, `searchKeyword`) filters only the
 *   already-fetched page (`commonList`); simplified here to the one
 *   rendered text column (`employeeName`), same simplification class used
 *   throughout this rebuild for this bug family.
 */
@Component({
  selector: 'app-user-counter-mapper',
  imports: [FormsModule, StatusToggle],
  templateUrl: './user-counter-mapper.html',
  styleUrl: './user-counter-mapper.scss',
})
export class UserCounterMapper {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(UserCounterMapperService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organisationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common / saveBtnFlag / updateBtnFlag) ----
  // Always a string -- see user-counter-mapper.models.ts's doc comment on
  // `UserCounterMapperSavePayload.userId`.
  selectedUserId = signal('');
  saveMode = signal(true); // true = saveBtnFlag, false = updateBtnFlag

  userList = signal<UserOption[]>([]);
  counterList = signal<CounterOption[]>([]);

  // ---- dual-list picker state (mirrors userCounterMapper / selectCounter /
  // selectedCounterList, ctrl:34-36/122-146) ----
  private checkedCounters = signal<Record<number, boolean>>({});
  selectCounterWorking = signal<SelectedCounter[]>([]); // working left-side list (post-checkbox, pre-commit)
  selectedCounterList = signal<SelectedCounter[]>([]); // committed right-side "Mapped Counter" list -- what's actually posted

  // ---- bottom table + pagination (mirrors commonList / pager) ----
  rows = signal<UserCounterMapperRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters the already-fetched page only,
  // simplified to the one rendered text column -- see class doc.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const list = this.rows();
    if (!keyword) return list;
    return list.filter((r) => r.employeeName?.toLowerCase().includes(keyword));
  });

  // ---- details modal (mirrors getCounterListMasterByUserId / userCounterDetailsModal) ----
  showDetailsModal = signal(false);
  detailCounters = signal<DetailedCounterItem[]>([]);

  constructor() {
    this.loadCounterList(); // searchGroupList() ctrl:95-117
    this.loadUserList(); // getUserList() ctrl:150-167
    this.loadRows(); // initMasterList() ctrl:177-208/242
  }

  private loadCounterList(): void {
    // legacy: no error/failure handler on this call either (ctrl:108-115).
    this.api.getCounterList(this.organisationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.counterList.set(res.listObject ?? []);
      },
    });
  }

  private loadUserList(): void {
    this.api.getUserList(this.organisationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.userList.set(res.listObject ?? []);
      },
    });
  }

  private loadRows(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getMapperList(this.organisationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
    this.api.getMapperCount(this.organisationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
    this.loadRows();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadRows();
  }

  // ---- dual-list picker (mirrors addCounter()/listOfCounters(), ctrl:122-146) ----
  isCounterChecked(counterId: number): boolean {
    return !!this.checkedCounters()[counterId];
  }

  addCounter(counterId: number, counterDesc: string, counterCode: string): void {
    const list = this.selectCounterWorking();
    const idx = list.findIndex((c) => c.id === counterId);
    if (idx !== -1) {
      this.selectCounterWorking.set(list.filter((_, i) => i !== idx));
      this.checkedCounters.update((map) => ({ ...map, [counterId]: false }));
    } else {
      this.selectCounterWorking.set([...list, { id: counterId, name: counterDesc, code: counterCode }]);
      this.checkedCounters.update((map) => ({ ...map, [counterId]: true }));
    }
  }

  /** The ">> <<" button -- commits the working list into the list that's
   * actually submitted on save/update (ctrl:138-146). Unlike the sibling's
   * `selectCabin()`, legacy ALSO dedupes `selectCounter` by `id` first --
   * see class doc. */
  selectCounter(): void {
    const deduped = this.selectCounterWorking().filter(
      (value, index, self) => index === self.findIndex((t) => t.id === value.id),
    );
    this.selectCounterWorking.set(deduped);
    this.selectedCounterList.set([...deduped]);
  }

  // ---- save (mirrors save(), ctrl:247-340 -- validates, unlike update()) ----
  save(): void {
    const userId = this.selectedUserId();
    if (!userId) {
      this.notify.warning('Please select user.');
      return;
    }
    const counterIds = this.selectedCounterList().map((c) => c.id);
    if (counterIds.length <= 0) {
      this.notify.warning('Please select counter.');
      return;
    }

    const now = formatDateTime(new Date());
    this.api
      .save({
        userId,
        status: 'A',
        // Always '' -- $scope.common's counterDesc/counterCode/employeeName/
        // employeeCode/empName are never populated anywhere in legacy. See class doc.
        counterDesc: '',
        counterCode: '',
        employeeName: '',
        employeeCode: '',
        isDeleted: 'N',
        organisationId: this.organisationId,
        unitId: this.unitId,
        createdBy: this.userId,
        createdDate: now,
        updatedBy: this.userId,
        updatedDate: now,
        empName: '',
        selectedCounterList: counterIds,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Saved successfully');
            this.resetForm();
            this.selectedCounterList.set([]);
            this.checkedCounters.set({});
            this.selectCounterWorking.set([]); // save()'s success handler resets selectCounter (ctrl:322) -- see class doc
            this.loadCounterList(); // save()'s success handler refetches counterList (ctrl:319-320) -- see class doc
            this.loadRows();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.notify.error('Something went wrong'),
      });
  }

  // ---- update (mirrors update(), ctrl:389-458 -- no validation at all) ----
  update(): void {
    const counterIds = this.selectedCounterList().map((c) => c.id);
    const now = formatDateTime(new Date());
    this.api
      .update({
        userId: this.selectedUserId(),
        status: 'A',
        counterDesc: '',
        counterCode: '',
        employeeName: '',
        employeeCode: '',
        isDeleted: 'N',
        organisationId: this.organisationId,
        unitId: this.unitId,
        createdBy: this.userId,
        createdDate: now,
        updatedBy: this.userId,
        updatedDate: now,
        empName: '',
        selectedCounterList: counterIds,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Saved successfully');
            this.resetForm();
            this.selectedCounterList.set([]);
            this.checkedCounters.set({});
            // NOTE: update()'s success handler does NOT reset selectCounter
            // (ctrl:436-442 has no equivalent of save()'s ctrl:322) -- real
            // asymmetry, preserved faithfully, not fixed. See class doc.
            this.loadCounterList(); // update() ALSO refetches counterList (ctrl:439-440) -- see class doc
            this.loadRows();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.notify.error('Something went wrong'),
      });
  }

  private resetForm(): void {
    this.selectedUserId.set('');
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn(), ctrl:343-387 -- see class doc for the
  // flagship indexOf/splice(-1,1) bug reproduced verbatim below) ----
  edit(userId: number): void {
    this.saveMode.set(false);
    this.api.getById(this.organisationId, this.unitId, userId).subscribe({
      next: (res) => {
        if (res.status !== 'success') {
          window.alert('Error In Fetching Data');
          return;
        }
        const list = res.listObject ?? [];
        this.selectedUserId.set(String(list[0]?.userId ?? '')); // ctrl:358's `+ ""` coercion
        this.selectedCounterList.set([]);

        // FLAGSHIP BUG (ctrl:363-367) -- see class doc. `value` in legacy's
        // forEach is always the boolean checked-state, never one of the
        // `{id,name,code}` objects in `selectCounter` -- `indexOf` is always
        // -1, so `splice(-1, 1)` removes the LAST entry every time, once per
        // distinct counterId ever toggled this session, instead of the
        // entry that actually belongs to that counterId.
        for (const counterIdKey of Object.keys(this.checkedCounters())) {
          this.checkedCounters.update((map) => ({ ...map, [Number(counterIdKey)]: false }));
          this.selectCounterWorking.update((current) => {
            const next = [...current];
            next.splice(-1, 1); // indexOf(booleanValue) on an object array is always -1
            return next;
          });
        }

        for (const item of list) {
          const temp: SelectedCounter = { id: item.counterId, name: item.name, code: item.counterCode };
          this.selectCounterWorking.update((l) => [...l, temp]);
          this.checkedCounters.update((map) => ({ ...map, [item.counterId]: true }));
          this.selectedCounterList.update((l) => [...l, temp]);
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus(), ctrl:460-496 -- always resets
  // the whole form AND refetches counterList on success, never reverts on
  // failure -- a difference from the sibling's toggleStatus, see class doc) ----
  toggleStatus(row: UserCounterMapperRow, nextStatus: 'A' | 'I'): void {
    this.rows.update((list) => list.map((r) => (r.userId === row.userId ? { ...r, status: nextStatus } : r)));
    this.api.updateStatus(row.userId, nextStatus).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Status updated');
          this.resetForm();
          this.selectedCounterList.set([]);
          this.checkedCounters.set({});
          this.loadCounterList(); // ctrl:479-480 -- see class doc
          this.loadRows();
        } else {
          this.notify.error(res.message ?? 'Something went wrong');
        }
      },
      error: () => this.notify.error('Something went wrong'),
    });
  }

  // ---- details modal (mirrors getCounterListMasterByUserId(), ctrl:499-518
  // -- no status check at all, see class doc) ----
  openDetails(userId: number): void {
    this.showDetailsModal.set(true);
    this.api.getDetailedCounterList(this.organisationId, this.unitId, userId).subscribe({
      next: (res) => this.detailCounters.set(res.listObject ?? []),
    });
  }

  closeDetailsModal(): void {
    this.showDetailsModal.set(false);
  }
}

/** `moment(currentDate).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:253-254/395-396. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
