import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { NationalityMasterService } from './nationality-master.service';
import { CountryOption, NationalityRecord, NationalityRow, emptyNationality } from './nationality-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/nationality-master.html +
 * resources/assets/js/controller/master/nationalityMasterController.js.
 * moduleId 381 (app.js:3312-3338 resolve block, confirmed live).
 *
 * Audited as real, full CRUD -- see per-method comments for exact
 * endpoint/behavior citations back to the controller.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - `saveNationality`'s confirm-modal step (`popUpFlag` dance, ctrl:210-276)
 *   only exists for CREATE, and -- unlike this codebase's
 *   `asset-category-master`/`fixed-asset-type` siblings -- required-field
 *   validation runs BEFORE the modal opens, not after "Ok" is clicked
 *   (ctrl:220-236: `$broadcast('show-errors-check-validity')` then
 *   `if ($valid) { if (popUpFlag) open-modal else POST }`). `updateNationality`
 *   (ctrl:351-399) has no confirm step at all -- it validates and POSTs
 *   immediately. Reproduced: `openConfirmModal()` validates first and only
 *   opens the modal when valid; `update()` validates and POSTs directly,
 *   no modal, matching `city-master`'s same save/update asymmetry.
 * - `showUpdateBtn` (ctrl:279-308) calls `.toString()` directly on the
 *   fetched `countryId` with no null-check (ctrl:301) -- if the backend
 *   ever returned a null countryId here, legacy would throw. Reproduced
 *   verbatim in `edit()` below, not guarded.
 * - `showUpdateBtn`'s failure branch is a bare native `alert("Error!!")`
 *   (ctrl:303-305), not a `growl` toast like everywhere else in this
 *   controller. Routed through `NotificationService.error(...)` instead --
 *   consistent with every other rebuilt screen in this codebase (none use
 *   a real browser `alert()`), not a deliberate downgrade of this specific
 *   screen's behavior.
 * - On a SUCCESSFUL save or update, the legacy controller calls
 *   `$scope.init()` (ctrl:257/382), which resets `offset`/`noOfRecordsPerPage`
 *   back to `0`/`10` -- and the list-reload that unconditionally follows
 *   right after (ctrl:266-270/391-395) then runs against those just-reset
 *   values. So a successful save/update silently snaps the list back to
 *   page 1 / 10-per-page even if the user was on a later page or a larger
 *   page size; a FAILED save/update reloads at the CURRENT page/size
 *   instead (no visual change, since `init()` isn't called on failure).
 *   Reproduced: `resetForm()` resets `pageSize`/`currentPage` to defaults
 *   and is only called on success; `loadList()` always runs afterward,
 *   using whichever page/size is current at that point.
 * - `updateStatus` (ctrl:311-348) never reloads the list after the POST --
 *   the checkbox's own two-way-bound `ng-model="info.status"` already
 *   flips the row's status client-side the instant it's toggled, with no
 *   revert on failure (just a growl message either way, no revert logic at
 *   all). Reproduced: `toggleStatus` optimistically flips the row locally
 *   and never reverts it, even on a failed request.
 * - The country dropdown (`getActiveCountryList`, ctrl:80-95) is fetched
 *   exactly once at controller load, not re-fetched by `init()` on every
 *   save/update reset. Reproduced: fetched once in the constructor only.
 * - No client-side duplicate-code check exists in this controller (unlike
 *   `asset-category-master`'s `ValidateCode`) -- the only "already exists"
 *   signal is the backend's non-success response status. Not added here.
 */
@Component({
  selector: 'app-nationality-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './nationality-master.html',
  styleUrl: './nationality-master.scss',
})
export class NationalityMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(NationalityMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.nationality) ----
  nationality = signal<NationalityRecord>(emptyNationality());
  saveMode = signal(true);
  formSubmitted = signal(false);

  countryList = signal<CountryOption[]>([]);

  showConfirmModal = signal(false);

  // ---- list + server-side pagination (mirrors initNationalityMasterList/PagerService) ----
  nationalityList = signal<NationalityRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters the CURRENT PAGE only (view
  // line 156: `nationalityList|filter:searchKeyword` runs over the
  // already server-paginated list) -- same pattern noted in this
  // codebase's `asset-category-master`/`city-master`.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.nationalityList();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.countryName?.toLowerCase().includes(keyword) ||
        r.nationalityCode?.toLowerCase().includes(keyword) ||
        r.nationalityName?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    // ctrl:80-95 -- fetched once, not re-fetched on form reset (see class comment).
    this.api.getActiveCountryList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.countryList.set(res.listObject ?? []);
      },
    });
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getNationalityList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.nationalityList.set(res.listObject ?? []);
      },
    });
    this.api.getNationalityCount(this.organizationId).subscribe({
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

  updateField<K extends keyof NationalityRecord>(field: K, value: NationalityRecord[K]): void {
    this.nationality.update((n) => ({ ...n, [field]: value }));
  }

  isRequiredMissing(value: string | number | ''): boolean {
    return this.formSubmitted() && (value === '' || value == null);
  }

  private requiredFieldsValid(): boolean {
    const n = this.nationality();
    return !!(n.countryId !== '' && n.nationalityCode.trim() && n.nationalityDesc.trim());
  }

  // ---- save (mirrors saveNationality's popUpFlag confirm-modal dance,
  // ctrl:216-276 -- validation runs BEFORE the modal opens, see class
  // comment) ----
  openConfirmModal(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const n = this.nationality();
    const now = formatDateTime(new Date());
    this.api
      .saveNationality({
        nationalityCode: n.nationalityCode,
        nationalityName: n.nationalityDesc,
        status: 'A',
        createdBy: this.userId,
        updatedBy: this.userId,
        organizationId: this.organizationId,
        createdDate: now,
        updatedDate: now,
        countryId: n.countryId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Nationality added successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Nationality already exits...!!');
          }
          this.loadList();
        },
      });
  }

  // ---- update (mirrors updateNationality, ctrl:351-399) -- no confirm modal ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return;
    const n = this.nationality();
    this.api
      .updateNationality({
        nationalityCode: n.nationalityCode,
        nationalityName: n.nationalityDesc,
        nationalityId: n.nationalityId as number,
        countryId: n.countryId,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success('Nationality updated successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Nationality already exits...!!');
          }
          this.loadList();
        },
      });
  }

  // Only called on success (ctrl:257/382 -> $scope.init() resets
  // offset=0/noOfRecordsPerPage=10 -- see class comment).
  private resetForm(): void {
    this.nationality.set(emptyNationality());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.pageSize.set(PAGE_SIZE_OPTIONS[0]);
    this.currentPage.set(1);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:279-308) ----
  edit(id: number): void {
    this.saveMode.set(false);
    this.api.getNationalityById(id).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          // ctrl:303-305: native `alert("Error!!")` -- see class comment.
          this.notify.error('Error!!');
          return;
        }
        const detail = res.listObject[0];
        // ctrl:301 -- unguarded `.toString()` on the fetched countryId, no
        // null-check. Preserved as-is; throws the same way legacy does if
        // the backend ever returns a null countryId here.
        const countryId = Number(detail.countryId.toString());
        this.nationality.set({
          nationalityId: detail.nationalityId,
          nationalityCode: detail.nationalityCode,
          nationalityDesc: detail.nationalityName,
          countryId,
        });
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:311-348) -- optimistic
  // local flip, never reverted even on failure (see class comment) ----
  toggleStatus(row: NationalityRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.nationalityList.update((rows) =>
      rows.map((r) => (r.nationalityId === row.nationalityId ? { ...r, status: nextStatus } : r)),
    );
    this.api.updateStatus(row.nationalityId, nextStatus, this.userId, formatDateTime(new Date())).subscribe({
      next: (res) => {
        if (res.status === 'success') this.notify.success('Nationality Updated successfully...!!');
        else this.notify.error('Something went wrong...!!');
      },
    });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:217-219/312-314/352-354. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
