import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { WardMasterService } from './ward-master.service';
import {
  FloorOption,
  GenderOption,
  IcuTypeOption,
  WardGenderDetail,
  WardMasterForm,
  WardMasterRow,
  emptyWardMasterForm,
} from './ward-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/ward-master.html +
 * resources/assets/js/controller/adt/unit/wardMasterController.js.
 * moduleId 545 (app.js:8683-8709 resolve block, confirmed live). No
 * `controllerAs` in legacy.
 *
 * Audited as real, full CRUD -- see the task brief for the already-confirmed
 * endpoints (SAVEWARDMASTERMASTER/UPDATEWARDMASTER/GETWARDMASTERBYID/
 * UPDATEWARDMASTERSTATUS/GETWARDMASTERLIST/GETWARDMASTERCOUNT), plus the
 * floor dropdown (GETACTIVEFLOORLIST), ICU-type dropdown (inline
 * `/adt/getActiveICUTypeList`), gender dropdown (GETACTIVEGENDERLIST) and
 * the "gender details" popup (`genderDetailsPopup`, ctrl:542-545).
 *
 * **Deliberate deviations/preserved quirks, not bugs in this rebuild:**
 * - The Save button's confirm-modal step (`saveWardMaster`'s `popUpFlag`
 *   dance, ctrl:270-372, re-entered via `PopupOkBtn`) only exists for
 *   CREATE. `updateWardMaster` (ctrl:455-541) runs the identical validation
 *   but saves immediately with no confirm step -- same asymmetry already
 *   established in this codebase's `city-master` (`saveCity`/`updateCity`).
 *   Reproduced via `save()` (opens the modal) vs. `update()` (saves
 *   directly).
 * - Floor-not-selected is checked *before*, and independently of, the rest
 *   of the form's required-field validation, with its own growl
 *   ("Please Select Floor") -- ctrl:285-292/460-467. The remaining required
 *   fields (ward code, ward name, and ICU type when "ICU Ward" is checked)
 *   are enforced silently via `wardForm.$valid` with no growl, only inline
 *   help-block text. Gender is checked *last*, after those pass, with its
 *   own growl ("Please Select Gender") -- ctrl:321-329/495-503. All three
 *   checks, in that exact order, are reproduced in `validateForm()`.
 * - The Gender field has no `*`/required-styled label in the legacy view
 *   (view:63, plain `<label>Gender</label>`) even though it IS functionally
 *   required (the growl above) -- a real legacy UI inconsistency, preserved
 *   verbatim rather than "fixed" by adding an asterisk that was never there.
 * - `icuTypeId` is force-set to `null` on save/update whenever "ICU Ward" is
 *   unchecked (ctrl:313-320/486-493), regardless of whatever stale value is
 *   still sitting in the (hidden) ICU-type field -- reproduced the same way
 *   rather than also clearing the form field's own state when unchecked,
 *   which legacy never does either.
 * - `showUpdateBtn` (ctrl:377-410) sets `wardMasterObj.floorId` via an
 *   unguarded `.toString()` on the fetched id (ctrl:394) -- same bug class
 *   already preserved in this codebase's `city-master.edit()`. Reproduced
 *   as `Number(detail.floorId.toString())`, not defensively guarded.
 * - Legacy's `genderIdList` per-ward entries come back from the backend
 *   with the display text under a field literally named `lable` (a real
 *   typo baked into `WardMaster.java`'s `GET_GENDER_LIST_BY_WARD_ID` HQL
 *   alias, `gen.desc as lable`) -- kept verbatim in `WardGenderDetail`
 *   rather than silently renamed to `label`.
 * - The gender multiselect (`ng-dropdown-multiselect` in legacy, a
 *   third-party widget) is rebuilt here as a plain checkbox dropdown panel
 *   (`genderDropdownOpen` + a list of checkboxes) -- same immediate-select
 *   behavior (no "move to selected" step, unlike this codebase's
 *   `billing-group-master` speciality picker, which legacy itself
 *   implements differently), just without the exact pixel-for-pixel
 *   third-party widget.
 * - The search box filters only the CURRENT PAGE's already-fetched rows
 *   (server-paginated `wardMasterList|filter:searchKeyword` in the legacy
 *   view), narrowed here to the actual displayed text columns (floor, ward
 *   code, ward name, ICU type name) rather than literally reimplementing
 *   Angular's generic all-properties `filter` -- same simplification
 *   already established in this codebase's `asset-category-master`/
 *   `city-master`.
 * - Status toggle updates the row via the list signal's `.update()` with a
 *   new object (this codebase's standard fix for the visual-toggle bug,
 *   entry 12) rather than literally reproducing legacy's `updateStatus`,
 *   which calls `$scope.init()` on success -- that call only re-fetches the
 *   floor/ICU-type/gender dropdown lists, not the ward table itself, so it
 *   has no observable effect on the toggled row (which already reflects
 *   its new state via two-way-bound `ng-model="info.status"`). Reproducing
 *   `.update()` here achieves the identical observable result more
 *   robustly.
 * - `LoaderService` (start/stopLoader) is not wired here, matching the
 *   established convention of this codebase's other "simple master" screens
 *   (`city-master`, `billing-group-master`, `organization-master`,
 *   `asset-category-master`), none of which wire it either despite legacy
 *   calling `$rootScope.startLoader()/stopLoader()` around their own
 *   save/update calls.
 */
@Component({
  selector: 'app-ward-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './ward-master.html',
  styleUrl: './ward-master.scss',
})
export class WardMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(WardMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.wardMasterObj) ----
  form = signal<WardMasterForm>(emptyWardMasterForm());
  saveMode = signal(true); // true = create (saveBtnFlag), false = update (updateBtnFlag)
  formSubmitted = signal(false); // drives inline required-field display, like show-errors-check-validity

  floorList = signal<FloorOption[]>([]);
  icuTypeList = signal<IcuTypeOption[]>([]);

  // ---- gender multiselect (mirrors genderListOptions/selectedGenderList) ----
  genderOptions = signal<GenderOption[]>([]);
  selectedGenderList = signal<GenderOption[]>([]);
  genderDropdownOpen = signal(false);

  showConfirmModal = signal(false);

  // ---- "gender details" popup (mirrors genderDetailsPopup, ctrl:542-545) ----
  showGenderDetailsModal = signal(false);
  genderPopupList = signal<WardGenderDetail[]>([]);

  // ---- list + server-side pagination (mirrors initWardMasterList/PagerService) ----
  list = signal<WardMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters the CURRENT PAGE only, narrowed
  // to the displayed text columns (see class comment).
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.floorName?.toLowerCase().includes(keyword) ||
        r.wardCode?.toLowerCase().includes(keyword) ||
        r.wardName?.toLowerCase().includes(keyword) ||
        r.icuTypeName?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadFloorList();
    this.loadIcuTypeList();
    this.loadGenderOptions();
    this.loadList();
    this.loadCount();
  }

  private loadFloorList(): void {
    this.api.getActiveFloorList(this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.floorList.set(res.listObject ?? []);
      },
    });
  }

  private loadIcuTypeList(): void {
    this.api.getActiveIcuTypeList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.icuTypeList.set(res.listObject ?? []);
      },
    });
  }

  private loadGenderOptions(): void {
    this.api.getActiveGenderList().subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.genderOptions.set((res.listObject ?? []).map((g) => ({ id: g.id, label: g.desc })));
        }
      },
    });
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getWardMasterList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  private loadCount(): void {
    this.api.getWardMasterCount(this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') this.totalCount.set(res.object);
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

  updateFormField<K extends keyof WardMasterForm>(field: K, value: WardMasterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  isRequiredMissing(value: string | number | ''): boolean {
    return this.formSubmitted() && (value === '' || value == null);
  }

  // ---- gender multiselect (mirrors ng-dropdown-multiselect wiring) ----
  toggleGenderDropdown(): void {
    this.genderDropdownOpen.update((open) => !open);
  }

  isGenderSelected(id: number): boolean {
    return this.selectedGenderList().some((g) => g.id === id);
  }

  toggleGenderOption(option: GenderOption): void {
    if (this.isGenderSelected(option.id)) {
      this.selectedGenderList.update((list) => list.filter((g) => g.id !== option.id));
    } else {
      this.selectedGenderList.update((list) => [...list, option]);
    }
  }

  genderSummary(): string {
    const selected = this.selectedGenderList();
    return selected.length ? selected.map((g) => g.label).join(', ') : 'Select';
  }

  // ---- required-field validation (mirrors wardForm.$valid; floor and
  // gender are checked separately -- see class comment) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    if (!f.wardCode.trim() || !f.wardName.trim()) return false;
    if (f.isIcu === 'Y' && f.icuTypeId === '') return false;
    return true;
  }

  /** Returns true (and shows the appropriate growl) only when validation
   * fails -- shared by both save() and update(), matching the identical
   * checks/order in ctrl:284-330 and ctrl:459-504. */
  private hasValidationError(): boolean {
    if (this.form().floorId === '') {
      this.notify.error('Please Select Floor');
      return true;
    }
    if (!this.requiredFieldsValid()) return true;
    if (this.selectedGenderList().length === 0) {
      this.notify.error('Please Select Gender');
      return true;
    }
    return false;
  }

  // ---- save (mirrors saveWardMaster's popUpFlag confirm-modal dance,
  // ctrl:276-374) ----
  save(): void {
    this.formSubmitted.set(true);
    if (this.hasValidationError()) return;
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
      .saveWardMaster({
        floorId: f.floorId as number,
        wardCode: f.wardCode,
        wardName: f.wardName,
        isDayCare: f.isDayCare,
        isEr: f.isEr,
        isIcu: f.isIcu,
        genderIdList: this.selectedGenderList().map((g) => ({ id: g.id, label: g.label })),
        islaborWard: f.islaborWard,
        ishemodialysisward: f.ishemodialysisward,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        unitId: this.unitId,
        organizationId: this.organizationId,
        createdDate: now,
        updatedDate: now,
        isIpWard: f.isIpWard,
        icuTypeId: f.isIcu === 'Y' ? (f.icuTypeId as number) : null,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Saved successfully');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
          this.loadList();
          this.loadCount();
        },
        error: () => this.notify.error('Something went wrong'),
      });
  }

  // ---- update (mirrors updateWardMaster, ctrl:455-541) -- no confirm modal ----
  update(): void {
    this.formSubmitted.set(true);
    if (this.hasValidationError()) return;
    const f = this.form();
    this.api
      .updateWardMaster({
        floorId: f.floorId as number,
        wardCode: f.wardCode,
        wardName: f.wardName,
        isDayCare: f.isDayCare,
        isEr: f.isEr,
        isIcu: f.isIcu,
        wardId: f.wardId as number,
        genderIdList: this.selectedGenderList().map((g) => ({ id: g.id, label: g.label })),
        islaborWard: f.islaborWard,
        ishemodialysisward: f.ishemodialysisward,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        unitId: this.unitId,
        organizationId: this.organizationId,
        isIpWard: f.isIpWard,
        icuTypeId: f.isIcu === 'Y' ? (f.icuTypeId as number) : null,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Saved successfully');
            this.resetForm();
          } else {
            this.notify.error(res.message ?? 'Something went wrong');
          }
          this.loadList();
          this.loadCount();
        },
        error: () => this.notify.error('Something went wrong'),
      });
  }

  private resetForm(): void {
    this.form.set(emptyWardMasterForm());
    this.selectedGenderList.set([]);
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:377-412) ----
  edit(wardId: number): void {
    this.saveMode.set(false);
    this.api.getWardMasterById(wardId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) return;
        const detail = res.listObject[0];
        // ctrl:394 -- unguarded `.toString()` on the fetched floorId, no
        // null-check. Preserved as-is (see class comment).
        const floorId = Number(detail.floorId.toString());
        this.form.set({
          wardId: detail.wardId,
          floorId,
          wardCode: detail.wardCode,
          wardName: detail.wardName,
          isDayCare: detail.isDayCare,
          isEr: detail.isEr,
          isIcu: detail.isIcu,
          ishemodialysisward: detail.ishemodialysisward,
          icuTypeId: detail.icuTypeId ?? '',
          islaborWard: detail.islaborWard,
          isIpWard: detail.isIpWard,
        });
        this.selectedGenderList.set((detail.genderIdList ?? []).map((g) => ({ id: g.id, label: g.lable })));
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:415-452) ----
  toggleStatus(row: WardMasterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api.updateWardMasterStatus(row.wardId, nextStatus, this.userId, formatDateTime(new Date())).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.update((rows) => rows.map((r) => (r.wardId === row.wardId ? { ...r, status: nextStatus } : r)));
          this.notify.success(res.message ?? 'Status updated');
        } else {
          this.notify.error(res.message ?? 'Update failed');
        }
      },
      error: () => this.notify.error('Update failed'),
    });
  }

  // ---- "gender details" popup (mirrors genderDetailsPopup, ctrl:542-545) ----
  openGenderDetails(row: WardMasterRow): void {
    this.genderPopupList.set(row.genderIdList ?? []);
    this.showGenderDetailsModal.set(true);
  }

  closeGenderDetailsModal(): void {
    this.showGenderDetailsModal.set(false);
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:280-282/416-418/456-458. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
