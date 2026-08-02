import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { AmbulanceTarrifMasterService } from './ambulance-tarrif-master.service';
import { AmbulanceTarrifRow } from './ambulance-tarrif-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/ambulanceService/ambulance-Tarrif-Master.html +
 * resources/assets/js/controller/lis/global/ambulanceTarrifController.js.
 * Legacy state `ambulanceTarrifMaster` (app.js:19781-19807), gated by a
 * resolve-block check for moduleId 816 -- wired at the ROUTE level via
 * `moduleAccessGuard` (`data: { moduleId: 816 }`), not in this component.
 *
 * **Deliberate deviations/preserved quirks, not bugs in this rebuild:**
 * - "Ambulance Provider" is a hardcoded, backend-independent `<select>`
 *   with exactly two static options, "In House" / "Out Sourced" (view:30-33)
 *   -- NOT a dropdown fed by any master-data list, so there is no seed-data
 *   gap to check or fix here (unlike some other batches' dropdowns).
 * - "Ambulance Tariff Name" is a plain free-text input bound to
 *   `ambulanceTarrif.ambulanceType` (view:13), despite the field's `name`
 *   attribute being the copy-pasted `ambulanceTarrifMasterId` (a real
 *   legacy markup bug, harmless since AngularJS resolves `ng-model`, not
 *   `name`, for binding). A commented-out `<select>` driven by
 *   `ambulanceTypeList` sits right above it (view:14-15) -- genuinely dead
 *   markup, never rendered.
 * - "Additional Mileage" is a required field that is also permanently
 *   `disabled` in the markup (view:90), pre-seeded to `1` by
 *   `$scope.ambulanceTarrif.ambulanceAddMileage = 1` in `init()`
 *   (ctrl:42) and never user-editable in either create or edit mode --
 *   editing an existing row still shows whatever value the record already
 *   has, read-only. Reproduced as a disabled input defaulting to `'1'`.
 * - The Save button opens a real confirm modal ("Are you sure you want to
 *   Save?") before the create POST fires (`saveAmbulanceTarrif`'s
 *   `popUpFlag`/`PopupOkBtn` dance, ctrl:130-202, using the shared `modal`
 *   directive in commonPopUpDirectives.js). The Update button does NOT --
 *   `updateAmbulanceTarrif` (ctrl:327-379) posts immediately with no
 *   confirmation step at all. This asymmetry is real, not an oversight:
 *   only the create path is wrapped in `showConfirmModal` below. Per the
 *   project's confirm-modal re-audit (docs/ANGULAR_PENDING_FINDINGS.md
 *   entry 152 + the shared-directive re-audit entry), the shared `<modal>`
 *   directive always closes `showModal` and always re-arms on every Ok
 *   click, synchronously, regardless of the eventual save outcome --
 *   `confirmSave()` below closes the modal unconditionally at the top,
 *   before the POST even fires, matching that real behavior.
 * - Save's duplicate-name error text reads "Ambulance Tariff Already
 *   Exist...!!" (ctrl:187) while Update's reads "Ambulance Tariff already
 *   exits...!!" (ctrl:364, note the "exits" typo and different casing) --
 *   two genuinely different strings in real legacy, both preserved
 *   verbatim rather than unified.
 * - After a successful save OR update, legacy calls `$scope.init()`
 *   (resetting `offset` to 0 and `noOfRecordsPerPage` to 10, clearing the
 *   form back to create mode) and THEN unconditionally reloads the list via
 *   `initAmbulanceTarrifMasterList(..., $scope.offset,
 *   $scope.noOfRecordsPerPage)` (ctrl:193-196, 370-374) -- silently
 *   snapping the table back to page 1 @ page size 10, even if the user was
 *   on page 3 @ page size 50. On a FAILED save/update, `init()` is skipped
 *   (so the page-size choice and the in-progress form both survive), but
 *   the reload still always uses `$scope.offset`, which is never
 *   reassigned anywhere except inside `init()` -- so the reload always
 *   effectively targets page 1 regardless of success/failure. Reproduced
 *   via `afterSaveOrUpdate()`: always resets `currentPage` to 1, and
 *   additionally resets `pageSize` to the default and clears the form only
 *   on success.
 * - The search box filters only the CURRENT PAGE's already-fetched rows
 *   (bare Angular `filter:searchKeyword` over `$scope.ambulanceTarrifList`,
 *   not a server-side search across the full dataset) -- same quirk class
 *   already documented in `ambulance-type-master.ts` and
 *   `ambulance-facilities-master.ts`. Reproduced the same way via
 *   `displayedRows`, matching against the visible text columns.
 * - `getList`'s single call returns both the page of rows AND the total
 *   count in one response; the separate count endpoint/call
 *   (`COUNTAMBULANCETARRIFMASTER`) is commented-out dead code in the
 *   controller (ctrl:112-123), never actually invoked. One call, not two.
 * - Deliberately NOT reproduced: `init()` also fires a
 *   `GETAMBULANCETARRIFACTIVELIST` call on every load/reset into
 *   `$scope.ambulanceTypeList`, which nothing in the view ever reads (see
 *   the service file's header comment) -- inert background traffic with no
 *   observable effect, omitted rather than ported.
 */
@Component({
  selector: 'app-ambulance-tarrif-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './ambulance-tarrif-master.html',
  styleUrl: './ambulance-tarrif-master.scss',
})
export class AmbulanceTarrifMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(AmbulanceTarrifMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  private static readonly DEFAULT_ADD_MILEAGE = '1';

  // ---- form state (mirrors $scope.ambulanceTarrif) ----
  ambulanceType = signal('');
  ambulanceProvider = signal('');
  ambulanceOutSrcVender = signal('');
  ambulanceFirstDist = signal('');
  ambulanceFirstDistRate = signal('');
  ambulanceAddMileage = signal(AmbulanceTarrifMaster.DEFAULT_ADD_MILEAGE);
  ambulanceAddMileageRate = signal('');
  private activeMasterId: number | undefined;
  saveMode = signal(true); // true = saveBtnFlag, false = updateBtnFlag
  submitted = signal(false); // drives *required* error display, like show-errors-check-validity

  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors PagerService.GetPager:
  // startIndex = (currentPage-1)*pageSize, confirmed in pagerService.js) ----
  list = signal<AmbulanceTarrifRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters the CURRENT PAGE only (see class comment).
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.ambulanceType?.toLowerCase().includes(keyword) ||
        r.ambulanceProvider?.toLowerCase().includes(keyword) ||
        r.ambulanceOutSrcVender?.toLowerCase().includes(keyword) ||
        r.ambulanceFirstDist?.toLowerCase().includes(keyword) ||
        r.ambulanceFirstDistRate?.toLowerCase().includes(keyword) ||
        r.ambulanceAddMileage?.toLowerCase().includes(keyword) ||
        r.ambulanceAddMileageRate?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
          if (typeof res.object === 'number') this.totalCount.set(res.object);
        }
      },
    error: (err) => console.error('ambulance-tarrif-master error', err),
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

  // ---- validation (mirrors ambulanceTarrifForm.$valid over the required fields) ----
  isTariffNameInvalid(): boolean {
    return this.submitted() && !this.ambulanceType().trim();
  }

  isProviderInvalid(): boolean {
    return this.submitted() && !this.ambulanceProvider().trim();
  }

  isVendorInvalid(): boolean {
    return this.submitted() && !this.ambulanceOutSrcVender().trim();
  }

  isFirstDistInvalid(): boolean {
    return this.submitted() && !this.ambulanceFirstDist().trim();
  }

  isFirstDistRateInvalid(): boolean {
    return this.submitted() && !this.ambulanceFirstDistRate().trim();
  }

  isAddMileageRateInvalid(): boolean {
    return this.submitted() && !this.ambulanceAddMileageRate().trim();
  }

  private validate(): boolean {
    this.submitted.set(true);
    return (
      !!this.ambulanceType().trim() &&
      !!this.ambulanceProvider().trim() &&
      !!this.ambulanceOutSrcVender().trim() &&
      !!this.ambulanceFirstDist().trim() &&
      !!this.ambulanceFirstDistRate().trim() &&
      !!this.ambulanceAddMileageRate().trim()
    );
  }

  // ---- save (mirrors saveAmbulanceTarrif's popUpFlag confirm dance) ----
  openSaveConfirm(): void {
    if (!this.validate()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const now = formatDateTime(new Date());
    this.api
      .save({
        ambulanceType: this.ambulanceType(),
        ambulanceProvider: this.ambulanceProvider(),
        ambulanceOutSrcVender: this.ambulanceOutSrcVender(),
        ambulanceFirstDist: this.ambulanceFirstDist(),
        ambulanceFirstDistRate: this.ambulanceFirstDistRate(),
        ambulanceAddMileage: this.ambulanceAddMileage(),
        ambulanceAddMileageRate: this.ambulanceAddMileageRate(),
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
            this.notify.success('Ambulance Tariff Added Successfully...!!');
            this.clearFields();
            this.afterSaveOrUpdate(true);
          } else {
            this.notify.error('Ambulance Tariff Already Exist...!!');
            this.afterSaveOrUpdate(false);
          }
        },
      error: (err) => console.error('ambulance-tarrif-master error', err),
      });
  }

  // ---- update (mirrors updateAmbulanceTarrif -- no confirm modal) ----
  update(): void {
    if (!this.validate()) return;
    const now = formatDateTime(new Date());
    this.api
      .update({
        ambulanceTarrifMasterId: this.activeMasterId as number,
        ambulanceType: this.ambulanceType(),
        ambulanceProvider: this.ambulanceProvider(),
        ambulanceOutSrcVender: this.ambulanceOutSrcVender(),
        ambulanceFirstDist: this.ambulanceFirstDist(),
        ambulanceFirstDistRate: this.ambulanceFirstDistRate(),
        ambulanceAddMileage: this.ambulanceAddMileage(),
        ambulanceAddMileageRate: this.ambulanceAddMileageRate(),
        updatedBy: this.userId,
        updatedDate: now,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Ambulance Tariff Updated Successfully...!!');
            this.clearFields();
            this.afterSaveOrUpdate(true);
          } else {
            this.notify.error('Ambulance Tariff already exits...!!');
            this.afterSaveOrUpdate(false);
          }
        },
      error: (err) => console.error('ambulance-tarrif-master error', err),
      });
  }

  // Legacy quirk preserved -- see class doc comment.
  private afterSaveOrUpdate(success: boolean): void {
    this.currentPage.set(1);
    if (success) this.pageSize.set(PAGE_SIZE_OPTIONS[0]);
    this.loadList();
  }

  private clearFields(): void {
    this.ambulanceType.set('');
    this.ambulanceProvider.set('');
    this.ambulanceOutSrcVender.set('');
    this.ambulanceFirstDist.set('');
    this.ambulanceFirstDistRate.set('');
    this.ambulanceAddMileage.set(AmbulanceTarrifMaster.DEFAULT_ADD_MILEAGE);
    this.ambulanceAddMileageRate.set('');
    this.activeMasterId = undefined;
    this.saveMode.set(true);
    this.submitted.set(false);
  }

  // ---- edit (mirrors showUpdateBtn) ----
  edit(ambulanceTarrifMasterId: number): void {
    this.api.getSingle(ambulanceTarrifMasterId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) return;
        const detail = res.listObject[0];
        this.ambulanceType.set(detail.ambulanceType);
        this.ambulanceProvider.set(detail.ambulanceProvider);
        this.ambulanceOutSrcVender.set(detail.ambulanceOutSrcVender);
        this.ambulanceFirstDist.set(detail.ambulanceFirstDist);
        this.ambulanceFirstDistRate.set(detail.ambulanceFirstDistRate);
        this.ambulanceAddMileage.set(detail.ambulanceAddMileage);
        this.ambulanceAddMileageRate.set(detail.ambulanceAddMileageRate);
        this.activeMasterId = detail.ambulanceTarrifMasterId;
        this.saveMode.set(false);
      },
    error: (err) => console.error('ambulance-tarrif-master error', err),
    });
  }

  // ---- status toggle (mirrors updateStatus) ----
  toggleStatus(row: AmbulanceTarrifRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api
      .changeStatus({
        ambulanceTarrifMasterId: row.ambulanceTarrifMasterId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.list.update((rows) =>
              rows.map((r) => (r.ambulanceTarrifMasterId === row.ambulanceTarrifMasterId ? { ...r, status: nextStatus } : r)),
            );
            this.notify.success('Ambulance Tariff Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }
}

/** `moment(currentDate).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:138-139/288-289/328-329. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
