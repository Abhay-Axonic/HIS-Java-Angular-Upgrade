import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { AmbulanceFacilitiesMasterService } from './ambulance-facilities-master.service';
import { AmbulanceFacilityRow } from './ambulance-facilities-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/ambulanceService/ambulance-Facilities-Master.html +
 * resources/assets/js/controller/lis/global/ambulanceFacilitiesController.js.
 * Legacy state `ambulanceFacilitiesMaster` (app.js:19790-19817), gated by a
 * resolve-block check for moduleId 818 -- confirmed live, wired at the
 * ROUTE level via `moduleAccessGuard` (`data: { moduleId: 818 }`), not in
 * this component.
 *
 * Audited clean: full CRUD wired, no dead bindings, no crash bug of the
 * ambulance-master.ts string/numeric-ID class (`GETSINGLERESOURCEALLOCATION`
 * doesn't exist on this screen at all -- there is no cascading dropdown
 * here, just a single free-text field). Real endpoints used: `getList`
 * (GETAMBULANCEFACILITIESLIST), `save` (ADDAMBULANCEFACILITIES), `getSingle`
 * (GETSINGLEAMBULANCEFACILITIES), `update` (UPDATEAMBULANCEFACILITIES),
 * `updateStatus` (UPDATEAMBULANCEFACILITIESSTATUS).
 *
 * **Deliberate deviations/preserved quirks, not bugs in this rebuild:**
 * - Unlike ambulance-type-master (a single unified `loadList`/`goToPage`
 *   pair computing offset from `currentPage` every time), THIS screen's
 *   legacy controller genuinely has TWO separate offset-fetching functions
 *   over the identical endpoint: `initAmbulanceFacilitiesMasterList`
 *   (ctrl:63-93 -- used by init, `setNoOfRecords`, and both post-save/update
 *   reloads) always fetches at offset 0 and then calls `setPage(1,false)`
 *   -- i.e. it ALWAYS snaps back to page 1, regardless of what page the
 *   user was on -- versus `initAmbulanceFacilitiesMasterList1` (ctrl:96-124,
 *   functionally identical response handling, no `setPage` call) which is
 *   the only function ever invoked with a nonzero offset, exclusively from
 *   `setPage(page,'true')` when the user clicks First/Previous/a page
 *   number/Next/Last (view lines 100/102/105/107/110). Reproduced with two
 *   distinct methods here too: `loadList()` (offset always 0, resets
 *   `currentPage` to 1) for init/page-size-change/post-save/post-update,
 *   and `goToPage(page)` (offset computed from the target page) for the
 *   pagination footer only.
 * - Save vs. Update asymmetry, both real: `saveAmbulanceFacilities`
 *   (ctrl:155-215) is the SAME function invoked both by the Save button
 *   click and by the confirm modal's "Ok" button (`PopupOkBtn`,
 *   ctrl:149-152) -- the first call only flips `popUpFlag`/opens the modal
 *   (never posts), the second call (with `popUpFlag` now false) performs
 *   the actual POST. `updateAmbulanceFacilities` (ctrl:334-380) has no
 *   confirm step at all -- it posts immediately. Reproduced via
 *   `openSaveConfirm()` (validates + opens modal only) / `confirmSave()`
 *   (posts) vs. `update()` (posts directly, no modal), matching the
 *   convention already established by ambulance-type-master.ts /
 *   ambulance-master.ts for this exact module.
 * - On a SUCCESSFUL save or update, legacy calls `$scope.init()`
 *   (ctrl:196/361) BEFORE the unconditional list reload -- this resets
 *   `offset` to 0, `noOfRecordsPerPage` to 10, clears
 *   `$scope.ambulanceFacilities` entirely (the form), and resets
 *   `saveBtnFlag = true; updateBtnFlag = false`. That last part is a real
 *   difference from this module's `ambulance-master.ts` sibling (whose
 *   `saveMode` never resets back to `true` once flipped) -- THIS screen
 *   genuinely does flip back to "Save" mode after a successful update, not
 *   just after a successful save. On FAILURE, `init()` is skipped (growl
 *   error only), but the list reload still always runs, using whatever
 *   `offset`/`noOfRecordsPerPage` were current (offset is always 0 in
 *   practice since nothing else ever reassigns it). Reproduced via
 *   `clearFields()` + `afterSaveOrUpdate(true)` on success (which also
 *   resets `pageSize` to the default) vs. `afterSaveOrUpdate(false)` alone
 *   on failure (page still resets to 1, `pageSize` untouched) -- exact same
 *   shape as `ambulance-type-master.ts`'s `afterSaveOrUpdate`.
 * - `showUpdateBtn(id)` (ctrl:265-290) flips `saveBtnFlag`/`updateBtnFlag`
 *   to "Update" mode SYNCHRONOUSLY, as the very first two statements,
 *   BEFORE the `GETSINGLEAMBULANCEFACILITIES` call even fires -- not inside
 *   the `.then()` success branch the way `ambulance-type-master.ts`'s
 *   `edit()` does it. So the UI switches to "Update" mode even if the
 *   fetch subsequently fails (only a raw `alert("Error!!")` fires in that
 *   case, same convention as `ambulance-master.ts`'s `edit()`/`country-
 *   master.ts`). Reproduced verbatim: `saveMode.set(false)` runs before the
 *   `subscribe()` call, unconditionally.
 * - The search box filters only the already-fetched CURRENT PAGE
 *   (`ambulanceFacilitiesList|filter:searchKeyword`, view line 83), same
 *   quirk class already documented on every sibling screen in this module.
 *   Reproduced via `displayedRows`.
 * - The only required field is the single free-text "Ambulance Facilities"
 *   name (view line 13, `required="required"` on `ambFacilities`) -- there
 *   is no separate ID field on this screen (unlike ambulance-type-master's
 *   Type ID + Type name pair).
 * - `updateStatus` (ctrl:293-331) never reverts the checkbox on a failed
 *   POST -- optimistic update applied unconditionally, same as this
 *   module's `ambulance-master.ts`/`ambulance-type-master.ts` siblings.
 *   Reproduced the same way.
 */
@Component({
  selector: 'app-ambulance-facilities-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './ambulance-facilities-master.html',
  styleUrl: './ambulance-facilities-master.scss',
})
export class AmbulanceFacilitiesMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(AmbulanceFacilitiesMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.ambulanceFacilities) ----
  ambFacilities = signal('');
  private activeMasterId: number | undefined;
  saveMode = signal(true); // true = saveBtnFlag, false = updateBtnFlag
  submitted = signal(false); // drives *required* error display, like show-errors-check-validity

  showConfirmModal = signal(false);

  // ---- list + server-side pagination (see class doc for the two distinct
  // legacy fetch functions this models) ----
  list = signal<AmbulanceFacilityRow[]>([]);
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
    return rows.filter((r) => r.ambFacilities?.toLowerCase().includes(keyword));
  });

  constructor() {
    this.loadList();
  }

  // Mirrors `initAmbulanceFacilitiesMasterList` (ctrl:63-93): always fetches
  // offset 0, always resets the pager back to page 1 -- used for init,
  // page-size changes, and both post-save/post-update reloads.
  private loadList(): void {
    this.api.getList(this.organizationId, 0, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
          if (typeof res.object === 'number') this.totalCount.set(res.object);
          this.currentPage.set(1);
        }
      },
    });
  }

  onPageSizeChange(): void {
    this.loadList();
  }

  // Mirrors `initAmbulanceFacilitiesMasterList1` (ctrl:96-124): fetches at
  // an arbitrary offset for the target page, WITHOUT resetting the pager
  // afterward -- exclusively how the pagination footer navigates.
  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    const offset = (page - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
          if (typeof res.object === 'number') this.totalCount.set(res.object);
        }
      },
    });
  }

  // ---- validation (mirrors ambulanceFacilitiesForm.$valid over the single required field) ----
  isAmbFacilitiesInvalid(): boolean {
    return this.submitted() && !this.ambFacilities().trim();
  }

  private validate(): boolean {
    this.submitted.set(true);
    return !!this.ambFacilities().trim();
  }

  // ---- save (mirrors saveAmbulanceFacilities's popUpFlag confirm dance) ----
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
        ambFacilities: this.ambFacilities(),
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
            this.notify.success('Ambulance Facilities Added Successfully...!!');
            this.clearFields();
            this.afterSaveOrUpdate(true);
          } else {
            this.notify.error('Ambulance Facilities already exits...!!');
            this.afterSaveOrUpdate(false);
          }
        },
      });
  }

  // ---- update (mirrors updateAmbulanceFacilities -- no confirm modal) ----
  update(): void {
    if (!this.validate()) return;
    const now = formatDateTime(new Date());
    this.api
      .update({
        ambulanceFacilitiesMasterId: this.activeMasterId as number,
        ambFacilities: this.ambFacilities(),
        updatedBy: this.userId,
        updatedDate: now,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Ambulance Facilities Updated Successfully...!!');
            this.clearFields();
            this.afterSaveOrUpdate(true);
          } else {
            this.notify.error('Ambulance Facilities already exits...!!');
            this.afterSaveOrUpdate(false);
          }
        },
      });
  }

  // Legacy quirk preserved -- see class doc comment.
  private afterSaveOrUpdate(success: boolean): void {
    this.currentPage.set(1);
    if (success) this.pageSize.set(PAGE_SIZE_OPTIONS[0]);
    this.loadList();
  }

  private clearFields(): void {
    this.ambFacilities.set('');
    this.activeMasterId = undefined;
    this.saveMode.set(true);
    this.submitted.set(false);
  }

  // ---- edit (mirrors showUpdateBtn -- mode flips synchronously, see class doc) ----
  edit(ambulanceFacilitiesMasterId: number): void {
    this.saveMode.set(false);
    this.activeMasterId = ambulanceFacilitiesMasterId;
    this.api.getSingle(ambulanceFacilitiesMasterId, this.organizationId).subscribe({
      next: (res) => {
        const detail = res.listObject?.[0];
        if (res.status === 'success' && detail) {
          this.ambFacilities.set(detail.ambFacilities);
          this.activeMasterId = detail.ambulanceFacilitiesMasterId;
        } else {
          window.alert('Error!!');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus -- optimistic, never reverted on failure) ----
  toggleStatus(row: AmbulanceFacilityRow, nextStatus: 'A' | 'I'): void {
    this.list.update((rows) =>
      rows.map((r) => (r.ambulanceFacilitiesMasterId === row.ambulanceFacilitiesMasterId ? { ...r, status: nextStatus } : r)),
    );
    this.api
      .updateStatus({
        ambulanceFacilitiesMasterId: row.ambulanceFacilitiesMasterId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Ambulance Facilities Updated Successfully...!!');
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }
}

/** `moment(currentDate).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:156-158/294-296/335-336. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
