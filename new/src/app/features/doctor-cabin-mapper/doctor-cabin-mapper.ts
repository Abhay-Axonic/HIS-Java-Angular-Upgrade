import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { DoctorCabinMapperService } from './doctor-cabin-mapper.service';
import {
  CabinOption,
  DetailedCabinItem,
  DoctorCabinMapperRow,
  DoctorOption,
  SelectedCabin,
} from './doctor-cabin-mapper.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/userManagement/doctor-cabin-mapper.html +
 * resources/assets/js/controller/master/global/doctorCabinMapperController.js.
 * Legacy state `doctorCabinMapper` (app.js:17841-17865), gated by a
 * resolve-block check for moduleId 714 -- confirmed live, wired at the
 * ROUTE level via `moduleAccessGuard` (`data: { moduleId: 714 }`), not in
 * this component -- same convention as every other rebuilt screen so far.
 *
 * Built in parallel with its sibling `userStoreMapper` this batch; the two
 * screens share the exact same dual-list-picker shape and the exact same
 * `showUpdateBtn`/`indexOf`-on-a-boolean bug (see below) -- a real shared
 * legacy pattern, not a coincidence, and not something to fix differently
 * here than there.
 *
 * **2 silent logic bugs, reproduced faithfully, NOT fixed:**
 *
 * 1. `showUpdateBtn` (ctrl:364-368): before repopulating the "Mapped Cabin"
 *    list for the doctor being edited, legacy loops over every cabinId ever
 *    toggled this session (`$scope.DocCabinMapper`) and does
 *    `$scope.docSelectCabin.indexOf(value)` where `value` is the BOOLEAN
 *    checked-state, never one of the `{cabinId, cabinDesc}` objects actually
 *    stored in `docSelectCabin` -- `indexOf` on a primitive against an array
 *    of objects is always `-1`, so the resulting `splice(-1, 1)` silently
 *    removes whatever is currently the LAST entry of `docSelectCabin`
 *    instead of the entry that actually belongs to that cabinId. Reproduced
 *    below in `edit()`: one wrong-entry removal per distinct cabinId ever
 *    tracked in `checkedCabins`, keyed the same way legacy's own loop is
 *    keyed. (Judgment call: legacy's `DocCabinMapper` is a genuine sparse
 *    ARRAY indexed by cabinId, so AngularJS's `forEach` -- which walks
 *    `0..array.length` including untouched holes, each yielding `value =
 *    undefined` -- actually runs this destructive splice `highestCabinId + 1`
 *    times, not once per real entry, which in practice tends to empty
 *    `docSelectCabin` out entirely rather than leaving stray survivors. This
 *    rebuild models the same state as a `Record<cabinId, boolean>` instead
 *    (no holes), so it reproduces "wrong-entry removal, once per cabinId
 *    ever toggled" -- the qualitative bug the task calls out -- without
 *    inventing a fake "array holes" concept for a Record. Flagging the
 *    difference in degree here rather than silently picking one.)
 *
 * 2. `showUpdateBtn` (ctrl:371) also reads `value.diagnosisDesc` when
 *    building the repopulated `docSelectCabin`/`selectDoctorCabinMapperList`
 *    entries -- but GET_DOCTOR_CABIN_MAPPER_BY_ID's real DTO/query field is
 *    `cabinDesc`, confirmed against both
 *    `DoctorCabinMapperDto.java` (`getCabinDesc()`/`setCabinDesc()`, no
 *    `diagnosisDesc` anywhere) and the `GET_DOCTOR_CABIN_MAPPER_BY_ID`
 *    `@NamedQuery` itself (`cabinMaster.cabinDesc AS cabinDesc`,
 *    `DoctorCabinMapper.java:39`). `diagnosisDesc` is always `undefined` --
 *    a copy-paste leftover from some other (diagnosis-flavored) controller.
 *    Every "Mapped Cabin" label goes blank after clicking Edit. Reproduced
 *    verbatim in `edit()` via an explicit read of a field that does not
 *    exist on `DoctorCabinMapperByIdItem`.
 *
 * **Other real, deliberately-preserved asymmetries (not bugs to fix):**
 * - `save()` (ctrl:290-309) validates doctorId-empty and
 *   cabin-list-empty before posting (`growl.warning`, no HTTP call).
 *   `update()` (ctrl:390-449) has NEITHER check -- it always posts, even
 *   with an empty doctor or an empty cabin list. Reproduced: `save()`
 *   guards, `update()` doesn't.
 * - `save()`'s success handler resets `docSelectCabin` to `[]`
 *   (ctrl:325); `update()`'s success handler does NOT (ctrl:430-432 has no
 *   equivalent line) -- a real asymmetry, not harmonized here.
 * - `getDoctorCabinMapperByDoctorId` (ctrl:153-172, the info-icon "Details"
 *   modal) assigns `$scope.getListObject` with NO `status === 'success'`
 *   check at all -- reproduced in `openDetails()` with the same
 *   unconditional assignment (a failed/errored fetch silently renders an
 *   empty modal table, no error toast either).
 * - `updateStatus` (ctrl:451-485) always resets the ENTIRE form on success
 *   (`initMaster()` + clearing `selectDoctorCabinMapperList`/
 *   `DocCabinMapper`) regardless of which row was toggled or which
 *   direction -- reproduced in `toggleStatus()`. It never reverts the
 *   toggle on failure (optimistic, matches the `ambulance-master.ts`
 *   convention for this exact bug class).
 * - The doctor `<select>` (view:13) has NO `ng-change` at all in legacy --
 *   selecting a doctor never auto-populates anything else on this screen
 *   (unlike its sibling `userStoreMapper`'s doctor-equivalent field). Left
 *   with no change-triggered side effect here either.
 * - The bottom table's `orderBy:column:reverse` (view:104) is permanently
 *   inert -- `column`/`reverse` are never assigned anywhere in the
 *   controller, and no header in the view has a click handler to set them.
 *   Not modeled (dead, same class as `ambulance-master.ts`'s dead-var
 *   callouts) -- only the `filter:searchKeyword` half is reproduced, via
 *   `displayedRows`.
 * - The search box (view:88, `searchKeyword`) filters only the
 *   already-fetched page (`commonList`), matching Angular's default
 *   `filter:` on the whole row object; simplified here to the one rendered
 *   text column (`doctorName`), the same simplification already used by
 *   `asset-category-master.ts`/`ambulance-master.ts` for this bug class.
 */
@Component({
  selector: 'app-doctor-cabin-mapper',
  imports: [FormsModule, StatusToggle],
  templateUrl: './doctor-cabin-mapper.html',
  styleUrl: './doctor-cabin-mapper.scss',
})
export class DoctorCabinMapper {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(DoctorCabinMapperService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organisationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.common / saveBtnFlag / updateBtnFlag) ----
  // Always a string -- see doctor-cabin-mapper.models.ts's doc comment on
  // `DoctorCabinMapperSavePayload.doctorId`.
  doctorId = signal('');
  saveMode = signal(true); // true = saveBtnFlag, false = updateBtnFlag

  doctorList = signal<DoctorOption[]>([]);
  cabinList = signal<CabinOption[]>([]);

  // ---- dual-list picker state (mirrors DocCabinMapper / docSelectCabin /
  // selectDoctorCabinMapperList, ctrl:38-40/109-128) ----
  private checkedCabins = signal<Record<number, boolean>>({});
  docSelectCabin = signal<SelectedCabin[]>([]); // working left-side list (post-checkbox, pre-commit)
  selectedCabins = signal<SelectedCabin[]>([]); // committed right-side "Mapped Cabin" list -- what's actually posted

  // ---- bottom table + pagination (mirrors commonList / pager) ----
  rows = signal<DoctorCabinMapperRow[]>([]);
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
    return list.filter((r) => r.doctorName?.toLowerCase().includes(keyword));
  });

  // ---- details modal (mirrors getDoctorCabinMapperByDoctorId / userCabinDetailsModal) ----
  showDetailsModal = signal(false);
  detailCabins = signal<DetailedCabinItem[]>([]);

  constructor() {
    this.loadCabinList(); // searchGroupList() ctrl:79-106
    this.loadDoctorList(); // getDoctorList() ctrl:132-149
    this.loadRows(); // initMasterList() ctrl:181-217/256
  }

  private loadCabinList(): void {
    // legacy: no error/failure handler on this call either (ctrl:95-103).
    this.api.getCabinList(this.organisationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.cabinList.set(res.listObject ?? []);
      },
    });
  }

  private loadDoctorList(): void {
    this.api.getDoctorList(this.organisationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          // ctrl:140-144: excludes RMO doctors client-side.
          this.doctorList.set((res.listObject ?? []).filter((d) => d.isRMO !== 'Y'));
        }
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

  // ---- dual-list picker (mirrors addCabin()/selectCabin(), ctrl:110-128) ----
  isCabinChecked(cabinId: number): boolean {
    return !!this.checkedCabins()[cabinId];
  }

  addCabin(cabinId: number, cabinDesc: string): void {
    const list = this.docSelectCabin();
    const idx = list.findIndex((c) => c.cabinId === cabinId);
    if (idx !== -1) {
      this.docSelectCabin.set(list.filter((_, i) => i !== idx));
      this.checkedCabins.update((map) => ({ ...map, [cabinId]: false }));
    } else {
      this.docSelectCabin.set([...list, { cabinId, cabinDesc }]);
      this.checkedCabins.update((map) => ({ ...map, [cabinId]: true }));
    }
  }

  /** The ">> <<" button -- commits the working list into the list that's
   * actually submitted on save/update (ctrl:125-128). */
  selectCabin(): void {
    this.selectedCabins.set([...this.docSelectCabin()]);
  }

  // ---- save (mirrors save(), ctrl:261-342 -- validates, unlike update()) ----
  save(): void {
    const doctorId = this.doctorId();
    if (!doctorId) {
      this.notify.warning('Please select user.');
      return;
    }
    const cabinIds = this.selectedCabins().map((c) => c.cabinId);
    if (cabinIds.length <= 0) {
      this.notify.warning('Please select cabin.');
      return;
    }

    const now = formatDateTime(new Date());
    this.api
      .save({
        doctorId,
        organisationId: this.organisationId,
        unitId: this.unitId,
        status: 'A',
        createdBy: this.userId,
        createdDate: now,
        updatedBy: this.userId,
        updatedDate: now,
        selectDoctorCabinMapperList: cabinIds,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Saved successfully');
            this.resetForm();
            this.selectedCabins.set([]);
            this.checkedCabins.set({});
            this.docSelectCabin.set([]); // save()'s success handler resets docSelectCabin (ctrl:325) -- see class doc
            this.loadRows();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.notify.error('Something went wrong'),
      });
  }

  // ---- update (mirrors update(), ctrl:390-449 -- no validation at all) ----
  update(): void {
    const cabinIds = this.selectedCabins().map((c) => c.cabinId);
    const now = formatDateTime(new Date());
    this.api
      .update({
        doctorId: this.doctorId(),
        organisationId: this.organisationId,
        unitId: this.unitId,
        status: 'A',
        createdBy: this.userId,
        createdDate: now,
        updatedBy: this.userId,
        updatedDate: now,
        selectDoctorCabinMapperList: cabinIds,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Saved successfully');
            this.resetForm();
            this.selectedCabins.set([]);
            this.checkedCabins.set({});
            // NOTE: update()'s success handler does NOT reset docSelectCabin
            // (ctrl:430-432 has no equivalent of save()'s ctrl:325) -- real
            // asymmetry, preserved faithfully, not fixed. See class doc.
            this.loadRows();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
        },
        error: () => this.notify.error('Something went wrong'),
      });
  }

  private resetForm(): void {
    this.doctorId.set('');
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn(), ctrl:345-388 -- see class doc for
  // the 2 silent bugs reproduced verbatim below) ----
  edit(doctorId: number): void {
    this.saveMode.set(false);
    this.api.getById(doctorId, this.organisationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success') {
          window.alert('Error In Fetching Data');
          return;
        }
        const list = res.listObject ?? [];
        this.doctorId.set(String(list[0]?.doctorId ?? '')); // ctrl:360's `+ ""` coercion
        this.selectedCabins.set([]);

        // BUG 1 (ctrl:364-368) -- see class doc "silent logic bugs" #1.
        for (const cabinIdKey of Object.keys(this.checkedCabins())) {
          this.checkedCabins.update((map) => ({ ...map, [Number(cabinIdKey)]: false }));
          this.docSelectCabin.update((current) => {
            const next = [...current];
            next.splice(-1, 1); // indexOf(booleanValue) on an object array is always -1
            return next;
          });
        }

        // BUG 2 (ctrl:371) -- see class doc "silent logic bugs" #2. Reads a
        // field (`diagnosisDesc`) that does not exist on this DTO.
        for (const item of list) {
          const temp: SelectedCabin = {
            cabinId: item.cabinId,
            cabinDesc: (item as unknown as { diagnosisDesc?: string }).diagnosisDesc,
          };
          this.docSelectCabin.update((l) => [...l, temp]);
          this.checkedCabins.update((map) => ({ ...map, [item.cabinId]: true }));
          this.selectedCabins.update((l) => [...l, temp]);
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus(), ctrl:451-485 -- always
  // resets the whole form on success, never reverts on failure) ----
  toggleStatus(row: DoctorCabinMapperRow, nextStatus: 'A' | 'I'): void {
    this.rows.update((list) => list.map((r) => (r.doctorId === row.doctorId ? { ...r, status: nextStatus } : r)));
    this.api.updateStatus(row.doctorId, this.organisationId, this.unitId, nextStatus).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Status updated');
          this.resetForm();
          this.selectedCabins.set([]);
          this.checkedCabins.set({});
          this.loadRows();
        } else {
          this.notify.error(res.message ?? 'Something went wrong');
        }
      },
      error: () => this.notify.error('Something went wrong'),
    });
  }

  // ---- details modal (mirrors getDoctorCabinMapperByDoctorId(), ctrl:153-172
  // -- no status check at all, see class doc) ----
  openDetails(doctorId: number): void {
    this.showDetailsModal.set(true);
    this.api.getDetailedCabinList(doctorId, this.organisationId, this.unitId).subscribe({
      next: (res) => this.detailCabins.set(res.listObject ?? []),
    });
  }

  closeDetailsModal(): void {
    this.showDetailsModal.set(false);
  }
}

/** `moment(currentDate).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:267-268/396-397. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
