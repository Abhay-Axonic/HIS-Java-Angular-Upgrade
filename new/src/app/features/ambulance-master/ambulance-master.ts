import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { AmbulanceMasterService } from './ambulance-master.service';
import {
  AmbulanceFacilityOption,
  AmbulanceMasterRow,
  AmbulanceTypeOption,
  ServiceProviderOption,
} from './ambulance-master.models';

const OFFSET = 0;
const PAGE_SIZE = 10;

/**
 * Parity rebuild of
 * views/master/UnitMaster/ambulanceService/ambulance-Master.html +
 * resources/assets/js/controller/lis/global/ambulanceMasterController.js.
 * Legacy state `ambulanceMaster` (app.js:19671-19696), gated by a
 * resolve-block check for moduleId 814 -- confirmed live, wired at the
 * ROUTE level via `moduleAccessGuard` (`data: { moduleId: 814 }`), not in
 * this component. `$state` is properly injected in this controller (unlike
 * some sibling screens' missing-DI crash), so its `cookieObject == undefined
 * -> $state.go('login')` guard is a real, working redirect --
 * `moduleAccessGuard` already reproduces the same effect at the route level
 * (redirect on missing session), so no separate constructor-level check is
 * added here, matching the convention already used by
 * pesapal-process-page.ts/ambulance-type-master.ts for this exact bug class.
 *
 * **SEVERITY 1 crash bug, logged, NOT fixed -- faithfully reproduced as
 * this screen's parity target (see `onAmbulanceTypeChange`):**
 * `getNameByAmbType(id)` (ctrl:106-124) is wired from the view as the
 * Ambulance Type dropdown's `ng-change`
 * (ambulance-Master.html:15, `getNameByAmbType(ambulanceMaster.ambulanceType)`)
 * -- but the view passes the ambulance-TYPE-NAME STRING, while the function
 * sends it as `resourceAllocationMasterId: id` to GETSINGLERESOURCEALLOCATION
 * (`/adt/getSingleResourceAllocation`), an endpoint whose backing DAO query
 * filters on a numeric primary key
 * (legacy/Global/.../dao/ResourceAllocationDaoImpl.java:171-189,
 * `ResourceAllocationDto.resourceAllocationMasterId` is a Java `int`). A
 * string-for-numeric-ID mismatch means the backend returns an empty
 * `listObject`, and ctrl:117's unguarded
 * `response.data.listObject[0].vehicalNo` throws
 * `TypeError: Cannot read properties of undefined (reading 'vehicalNo')` on
 * every single selection of the Ambulance Type dropdown. Reproduced
 * verbatim below via the same unguarded index access, not fixed.
 *
 * Caveat worth flagging for whoever reconciles this with the audit: because
 * `resourceAllocationMasterId` is declared as a primitive Java `int` (not
 * `Integer`/`String`), sending a non-numeric JSON string for it may fail
 * Jackson deserialization before the DAO is ever reached, which
 * `GlobalExceptionHandler` (HIS-WAR/.../exception/GlobalExceptionHandler.java,
 * `HttpMessageNotReadableException`/`InvalidFormatException` handlers) turns
 * into an HTTP 400 with `status: "error"` -- in which case AngularJS's
 * `$http(...).then(successFn)` (no failure handler registered) would never
 * even enter the success branch that does the unguarded access, making the
 * real-world failure mode an unhandled promise rejection rather than a
 * thrown `TypeError`. Either way the dropdown's selection never functionally
 * populates the vehicle number, so the same unguarded-access shape is
 * reproduced here per the assigned parity target; flagging the DTO-typing
 * detail in case it changes how this should be modeled later.
 *
 * Related dead scope var (not a crash, just inert, and unreachable besides
 * since it sits after the line above that throws): ctrl:119/121 sets
 * `$scope.inhouseVehicle`, but the view binds the checkbox to
 * `ambulanceMaster.inhouseVehicle` instead (view line 55) -- never read.
 * Not modeled here at all; there is nothing to wire up.
 *
 * **Other deliberate deviations/preserved quirks, not bugs in this rebuild:**
 * - This screen's view has NO pagination UI whatsoever -- confirmed by
 *   grepping ambulance-Master.html for "pager"/"pagination"/"First"/
 *   "Previous"/"Next"/"noOfRecordsPerPage"/"setPage": zero matches, unlike
 *   its sibling `ambulance-Type-Master.html` which has a full page-size
 *   selector + First/Previous/page-numbers/Next/Last footer. The controller
 *   still fetches a count (`COUNTAMBULANCETYPEMASTER`) and builds a
 *   `$scope.pager` via `PagerService.GetPager`, but nothing in the view ever
 *   reads `pager`. Reproduced as-is: `loadList()` always fetches offset 0 /
 *   10 records and fires the count call for network-level parity, but
 *   nothing is rendered from either beyond the plain list -- there is no
 *   page picker in this rebuild's template either, and records beyond the
 *   first 10 are genuinely unreachable through this screen, matching legacy.
 * - The search box filters only the already-fetched list
 *   (`ambulanceMasterDataList|filter:searchKeyword`, view line 128) -- since
 *   that list is always just the first 10 records (see above), this is
 *   moot in practice, but reproduced the same way via `displayedRows`.
 * - `getAmbulanceMasterDataList` (ctrl:129-152) is a dead function -- never
 *   called anywhere in the controller or the view (confirmed by search);
 *   `initAmbulanceMasterList` is what's actually wired up. Not rebuilt.
 * - `$scope.ambulanceTypeList` is reset to `[]` twice (ctrl:79, ctrl:95) but
 *   never populated with anything and never read by the view. Genuinely
 *   dead; not modeled.
 * - Save vs. Update asymmetry, both real: the Save button
 *   (`saveAmbulanceMaster`, ctrl:213-276) goes through a real confirm modal
 *   ("Are you sure you want to Save?", the shared `modal`/`PopupOkBtn` dance
 *   in commonPopUpDirectives.js) before the create POST fires. The Update
 *   button (`updateAmbulanceMaster`, ctrl:373-423) does NOT -- it posts
 *   immediately with no confirmation step. Reproduced: only `openSaveConfirm`
 *   opens `showConfirmModal`; `update()` posts directly.
 * - After a successful (or failed) save/update, the form fields are NEVER
 *   cleared -- there is no `$scope.ambulanceMaster = {}` anywhere in either
 *   function. `showUpdateBtn` (edit) is the ONLY place that ever sets
 *   `saveBtnFlag`/`updateBtnFlag`, and it only ever flips them one way
 *   (`saveBtnFlag = false; updateBtnFlag = true`) -- nothing resets them
 *   back afterward, so once a user edits any single record, this screen
 *   shows "Update" for the rest of the session (a page reload is the only
 *   way back to "Save" mode). Reproduced verbatim: `saveMode` is only ever
 *   set to `false` by `edit()`, never back to `true`; `confirmSave()`/
 *   `update()` never clear the form fields.
 * - On success, both `saveAmbulanceMaster` and `updateAmbulanceMaster` call
 *   `$scope.init()` again (re-fetching all three active dropdown lists,
 *   redundant network calls) before the list is refreshed; on failure that
 *   re-fetch is skipped, but the list refresh (`initAmbulanceMasterList`)
 *   always runs regardless of success/failure. Reproduced via
 *   `refreshDropdowns()` (success-only) + `loadList()` (unconditional).
 * - `updateStatus` (ctrl:332-370) never reverts the checkbox on a failed
 *   POST -- legacy's `ng-model="info.status"` binds directly to the row
 *   object, so the toggle is applied optimistically and stays applied no
 *   matter what the server responds with (unlike some sibling screens' 406
 *   revert handling). Reproduced by always applying the new status to the
 *   `rows` signal, never reverting it in the subscribe callback.
 * - `showUpdateBtn`'s failure branch uses a raw `window.alert("Error!!")`,
 *   not `growl` -- same convention already established in
 *   `country-master.ts`'s `edit()`. Reproduced verbatim.
 */
@Component({
  selector: 'app-ambulance-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './ambulance-master.html',
  styleUrl: './ambulance-master.scss',
})
export class AmbulanceMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(AmbulanceMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- dropdown option lists (mirrors $scope.ambulanceMasterList /
  // serviceProviderList / ambulanceFacilitiesList) ----
  ambulanceTypeOptions = signal<AmbulanceTypeOption[]>([]);
  serviceProviderOptions = signal<ServiceProviderOption[]>([]);
  facilityOptions = signal<AmbulanceFacilityOption[]>([]);

  // ---- form state (mirrors $scope.ambulanceMaster) ----
  ambulanceType = signal('');
  serviceProvider = signal('');
  vehicalNo = signal('');
  availableFacilities = signal('');
  inhouseVehicle = signal<'Y' | 'N' | undefined>(undefined);
  private activeMasterId: number | undefined;

  saveMode = signal(true); // true = saveBtnFlag, false = updateBtnFlag -- one-way, see class doc
  submitted = signal(false); // drives *required* error display, like show-errors-check-validity

  showConfirmModal = signal(false);

  // ---- list + search (no pagination UI on this screen -- see class doc) ----
  rows = signal<AmbulanceMasterRow[]>([]);
  search = signal('');

  // Legacy quirk preserved: search filters the already-fetched list only
  // (see class comment) -- moot here since that list is always just the
  // first 10 records anyway.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const list = this.rows();
    if (!keyword) return list;
    return list.filter(
      (r) =>
        r.ambulanceType?.toLowerCase().includes(keyword) ||
        r.serviceProvider?.toLowerCase().includes(keyword) ||
        r.ambulanceVehicalNo?.toLowerCase().includes(keyword) ||
        r.availableFacilities?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.refreshDropdowns();
    this.loadList();
  }

  private refreshDropdowns(): void {
    this.api.getAmbulanceTypeActiveList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.ambulanceTypeOptions.set(res.listObject ?? []);
      },
    });
    this.api.getServiceProviderActiveList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.serviceProviderOptions.set(res.listObject ?? []);
      },
    });
    this.api.getAmbulanceFacilitiesActiveList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.facilityOptions.set(res.listObject ?? []);
      },
    });
  }

  private loadList(): void {
    this.api.getAmbulanceMasterDataList(this.organizationId, OFFSET, PAGE_SIZE).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
    // Fired unconditionally alongside the list, matching
    // `initAmbulanceMasterList` -- see class doc comment for why the result
    // is never rendered anywhere on this screen.
    this.api.getCount(this.organizationId).subscribe();
  }

  /**
   * Faithful crash reproduction -- see class doc comment for the full
   * trace. Legacy's `getNameByAmbType(id)` (ctrl:106-124) is wired to this
   * exact dropdown's `ng-change` (ambulance-Master.html:15), passing the
   * ambulance-TYPE-NAME STRING as `resourceAllocationMasterId`. The backend
   * returns an empty `listObject` for every such value, so ctrl:117's
   * unguarded `listObject[0].vehicalNo` access throws
   * `TypeError: Cannot read properties of undefined (reading 'vehicalNo')`
   * on every single selection. TypeScript's `ApiResponse.listObject` is
   * typed optional (`listObject?: TList[]`), so the compiler won't allow the
   * literal unguarded index expression to build -- same class of limitation
   * as `linen-issue-acceptance-details.ts`'s `rejectionPopup()`. Reproduced
   * via a direct `throw` at the equivalent trigger point instead, matching
   * that screen's convention, rather than fixed or guarded.
   */
  onAmbulanceTypeChange(type: string): void {
    this.ambulanceType.set(type);
    this.api.getSingleResourceAllocation(type, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          if (!res.listObject?.length) {
            throw new TypeError("Cannot read properties of undefined (reading 'vehicalNo')");
          }
          this.vehicalNo.set(res.listObject[0].vehicalNo);
        }
      },
    });
  }

  // ---- required-field validation (mirrors ambulanceform.$valid over
  // ambulanceType/serviceProvider/availableFacilities -- vehicalNo and
  // inhouseVehicle are not required in the legacy view) ----
  isAmbulanceTypeInvalid(): boolean {
    return this.submitted() && !this.ambulanceType();
  }

  isServiceProviderInvalid(): boolean {
    return this.submitted() && !this.serviceProvider();
  }

  isAvailableFacilitiesInvalid(): boolean {
    return this.submitted() && !this.availableFacilities();
  }

  private validate(): boolean {
    this.submitted.set(true);
    return !!this.ambulanceType() && !!this.serviceProvider() && !!this.availableFacilities();
  }

  // ---- save (mirrors saveAmbulanceMaster's popUpFlag confirm dance --
  // Save-only, see class doc) ----
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
        serviceProvider: this.serviceProvider(),
        ambulanceType: this.ambulanceType(),
        ambulanceVehicalNo: this.vehicalNo(),
        availableFacilities: this.availableFacilities(),
        inhouse: this.inhouseVehicle() ?? 'N',
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
            this.notify.success('Ambulance Data Added Successfully...!!');
            this.refreshDropdowns();
          } else {
            this.notify.error('Ambulance Vehicle No. is already exist.!!');
          }
          // Unconditional, regardless of success/failure -- see class doc.
          this.loadList();
        },
      });
  }

  // ---- update (mirrors updateAmbulanceMaster -- no confirm modal) ----
  update(): void {
    if (!this.validate()) return;
    const now = formatDateTime(new Date());
    this.api
      .update({
        ambulanceMasterId: this.activeMasterId as number,
        ambulanceType: this.ambulanceType(),
        serviceProvider: this.serviceProvider(),
        ambulanceVehicalNo: this.vehicalNo(),
        availableFacilities: this.availableFacilities(),
        inhouse: this.inhouseVehicle(),
        updatedBy: this.userId,
        updatedDate: now,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Ambulance Type Updated Successfully...!!');
            this.refreshDropdowns();
          } else {
            this.notify.error('Ambulance Type already exist...!!');
          }
          // Unconditional, regardless of success/failure -- see class doc.
          this.loadList();
        },
      });
  }

  // ---- edit (mirrors showUpdateBtn) ----
  edit(ambulanceMasterId: number): void {
    this.saveMode.set(false);
    this.activeMasterId = ambulanceMasterId;
    this.api.getSingle(ambulanceMasterId, this.organizationId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.ambulanceType.set(detail.ambulanceType);
          this.serviceProvider.set(detail.serviceProvider);
          this.vehicalNo.set(detail.ambulanceVehicalNo);
          this.availableFacilities.set(detail.availableFacilities);
          this.inhouseVehicle.set(detail.inhouse);
        } else {
          window.alert('Error!!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus -- optimistic, never reverted
  // on failure, see class doc) ----
  toggleStatus(row: AmbulanceMasterRow, nextStatus: 'A' | 'I'): void {
    this.rows.update((list) =>
      list.map((r) => (r.ambulanceMasterId === row.ambulanceMasterId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        ambulanceMasterId: row.ambulanceMasterId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Ambulance Type Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }
}

/** `moment(currentDate).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:214-216/333-335/374-375. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
