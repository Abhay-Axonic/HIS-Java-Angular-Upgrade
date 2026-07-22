import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { IcpcCodesConfigurationService } from './icpc-codes-configuration.service';
import {
  IcpcCodesConfigurationRow,
  IcpcConfigurationSpecialityDetail,
  IcpcSystemOption,
  SpecialityOption,
} from './icpc-codes-configuration.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/icpc-codes-configuration.html +
 * resources/assets/js/controller/master/unit/icpcCodesConfigurationController.js
 * (state `icpcCodesConfiguration`, registered centrally in
 * `resources/assets/js/config/app.js:19003` -- NOT self-registered, unlike
 * `ICPCMapper`). No `resolve` block at all on the state (confirmed by
 * reading app.js directly, the two states immediately above and below it
 * in the file DO have `resolve` blocks with a real module-id check, this
 * one does not), so this route has NO module gate in legacy, same as
 * `billingGroupMaster`/`periodicityService` (see docs/
 * ANGULAR_REBUILD_PLAYBOOK.md "Known caveats" #1) -- `data.moduleId` is
 * intentionally omitted at the route level, not an oversight.
 *
 * **CLINICAL-CONTENT VERDICT (owner-requested deep dive, batch 11): this
 * screen is pure org/unit-scoped REFERENCE/CONFIGURATION metadata, NOT
 * per-patient clinical data.** It maps an "ICPC System" to one or more
 * hospital Specialties (e.g. which specialties a given ICPC coding system
 * applies to for this org/unit). Every field on every request/response
 * confirmed by full-chain source read (controller -> DTO ->
 * Controller.java -> ServiceImpl -> DaoImpl -> entity): `icpcSystemId`,
 * `specialityId`/`specialityIdList`, `icpcCodeConfigurationId`,
 * `organizationId`, `unitId`, `status`, audit fields. No `patientId`, no
 * `encounterId`/`visitId`, no diagnosis entry tied to any real patient
 * anywhere in this screen's own code path. This confirms Phase 0's "no
 * clinical, no billing" tag for this screen is CORRECT (unlike
 * `doorToNeedle`/entry 66 or the entry 118 cluster).
 *
 * **A real, confirmed Java-side referential-integrity bug, faithfully
 * reproduced because it lives entirely server-side (nothing this
 * TypeScript service could change without altering behavior):**
 * `ICPCCodesConfigurationMapperServiceImpl.saveICPCCodesConfigurationMapper`
 * creates/updates the parent `t_icpc_code_configuration` row, then does
 * `icpcCodeConfigurationId = mstr.getIcpcSystemId()` (the ICPC SYSTEM id,
 * e.g. 1/2) where it clearly means `mstr.getIcpcCodeConfigurationId()`
 * (the row's own auto-generated primary key) -- then uses that wrong value
 * as the mapper rows' `icpc_configuration_id` foreign key. This is
 * self-consistent end-to-end (the "deactivate old mappings" SQL in
 * `ICPCCodesConfigurationMapperDaoImpl.saveICPCCodesConfigurationMapper`
 * filters on the same wrong value, and the front-end's own
 * `getSpecialityListByID` sends `info.icpcSystemId` as
 * `icpcConfigurationId`, matching what was actually written) and is
 * independently corroborated by a THIRD code path using the identical
 * substitution (`GET_SYSTEM_LIST_FOR_COMPLAINTS_ON_COVERSHEET`'s native
 * SQL literally joins `icpc_configuration_id = icpc_system_id`). Net
 * effect: the parent row's real generated primary key
 * (`icpc_code_configuration_id`) is functionally dead weight everywhere
 * this feature is actually exercised at runtime; the ICPC System id
 * (a small, per-org/unit-scoped natural key, since there are only a
 * handful of ICPC systems) does the real work instead. Low practical risk
 * today (every write path agrees, and every read path this screen uses
 * agrees), but the JPA `@ManyToOne` relationship's own semantics don't
 * match what's actually stored, which would matter to any FUTURE code that
 * navigates that relationship directly rather than going through these
 * DTOs/native queries. This rebuild's frontend sends exactly what legacy's
 * frontend sends (`info.icpcSystemId` as the lookup key, ctrl:297) -- the
 * bug itself is 100% server-side and out of scope for this Angular-only
 * session to fix.
 *
 * **Judgment calls / preserved quirks, not bugs introduced by this rebuild:**
 *
 * - **No confirm-save modal at all.** The Save button has a vestigial
 *   `data-toggle="modal"` attribute (view:57) with no `data-target` and no
 *   `<modal>` element anywhere in this view -- `ng-click="saveICPCCodesConfigurationMapper()"`
 *   runs directly. Confirmed: unlike `ICPCMapper`, this screen does not use
 *   the shared `<modal>` directive at all, so entry 55 does not apply here.
 *   Reproduced as a direct save with no confirm step.
 *
 * - **The entire active-status list is fetched in one call and paginated
 *   CLIENT-SIDE** (`PaginationService`'s fetch-all + `Array.slice`
 *   pattern, ctrl:30-44/218-242) -- not a rebuild simplification, this is
 *   what legacy's own `getICPCCodesConfigurationMapperList` endpoint does
 *   (no offset/page params exist on it at all).
 *
 * - **The header search box filters only the CURRENT PAGE's already-sliced
 *   rows** (`ng-repeat="info2 in pagination.data |orderBy:column:reverse|filter:searchKeyword"`,
 *   view:83) -- the same practical "current-page-only search" quirk
 *   already documented for several sibling screens in this project.
 *   `pagination.search` (a separate field `PaginationService` supports) is
 *   never wired to any input in this view -- dead, unused.
 *
 * - **Create vs. update success message asymmetry**, same shape as
 *   `ICPCMapper`: create uses the server's own `message`; update always
 *   shows the hardcoded string `"Updated successfully"` regardless of what
 *   the server actually returned (ctrl:158-168).
 *
 * - **An "Already Exists" response also reloads the list**, unlike a plain
 *   error response which does not (ctrl:184-192) -- reproduced exactly.
 *
 * - **Status toggle: only updates the list signal on a confirmed success**
 *   (standard `<app-status-toggle>` convention, entry 12) -- legacy's raw
 *   `ng-model` checkbox flips optimistically and never reverts on error
 *   (`updateStatus`, ctrl:335-369, has no list-reload or row-mutation of
 *   its own either way); this rebuild's equivalent simply never shows the
 *   flip until the server confirms it. Same disclosed, cross-cutting
 *   scaffold convention used by every other rebuilt screen.
 */
@Component({
  selector: 'app-icpc-codes-configuration',
  imports: [FormsModule, StatusToggle],
  templateUrl: './icpc-codes-configuration.html',
  styleUrl: './icpc-codes-configuration.scss',
})
export class IcpcCodesConfiguration {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(IcpcCodesConfigurationService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- dropdown/master data ----
  icpcSystemList = signal<IcpcSystemOption[]>([]);
  specialityMasterList = signal<SpecialityOption[]>([]);

  // ---- full list + CLIENT-SIDE pagination (mirrors PaginationService --
  // legacy fetches the whole active list in one call, no offset param
  // exists on this endpoint; see class comment) ----
  private fullList = signal<IcpcCodesConfigurationRow[]>([]);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.fullList().length / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  pagedRows = computed(() => {
    const size = this.pageSize();
    const offset = (this.currentPage() - 1) * size;
    return this.fullList().slice(offset, offset + size);
  });

  // Legacy quirk preserved: the search box filters only the CURRENT PAGE's
  // already-sliced rows (view:83) -- see class comment.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.pagedRows();
    if (!keyword) return rows;
    return rows.filter((r) => r.systemName?.toLowerCase().includes(keyword));
  });

  // ---- form state (mirrors $scope.icpcSystemObj + selectedSpeacilityList) ----
  updateFlag = signal(false);
  submitted = signal(false);
  icpcCodeConfigurationId = signal<number | null>(null);
  selectedIcpcSystemId = signal<number | ''>('');
  private selectedSpecialityIds = signal<Set<number>>(new Set());

  selectedSpecialityLabels = computed(() => {
    const ids = this.selectedSpecialityIds();
    return this.specialityMasterList()
      .filter((s) => ids.has(s.id))
      .map((s) => s.label);
  });

  isSpecialityChecked = (id: number) => this.selectedSpecialityIds().has(id);

  showSpecialityDropdown = signal(false);

  // ---- read-only "Specialty Details" info popup (hand-rolled bootstrap
  // modal in legacy, `#SpecialityInfo`) ----
  showSpecialityDetailsModal = signal(false);
  specialityDetailsForModal = signal<IcpcConfigurationSpecialityDetail[]>([]);

  constructor() {
    this.loadIcpcSystemList();
    this.loadSpecialityMasterList();
    this.loadList();
  }

  private loadIcpcSystemList(): void {
    this.api.getIcpcSystemList().subscribe({
      next: (res) => {
        if (res.status === 'success') this.icpcSystemList.set(res.listObject ?? []);
      },
    });
  }

  private loadSpecialityMasterList(): void {
    this.api.getActiveSpecialityList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.specialityMasterList.set(
            (res.listObject ?? []).map((v) => ({ id: v.specialityId, label: v.specialityName })),
          );
        }
      },
    });
  }

  private loadList(): void {
    this.api.getList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.fullList.set(res.listObject ?? []);
          this.currentPage.set(1);
        }
      },
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }

  toggleSpecialityDropdown(): void {
    this.showSpecialityDropdown.update((v) => !v);
  }

  toggleSpeciality(id: number): void {
    const next = new Set(this.selectedSpecialityIds());
    if (next.has(id)) next.delete(id);
    else next.add(id);
    this.selectedSpecialityIds.set(next);
  }

  isFieldInvalid(): boolean {
    return this.submitted() && !this.selectedIcpcSystemId();
  }

  isSpecialitySelectionInvalid(): boolean {
    return this.submitted() && this.selectedSpecialityIds().size === 0;
  }

  save(): void {
    this.submitted.set(true);
    const icpcSystemId = this.selectedIcpcSystemId();
    if (!icpcSystemId) {
      this.notify.error('Please Select System');
      return;
    }
    if (this.selectedSpecialityIds().size === 0) {
      this.notify.error('Please Select Speciality');
      return;
    }

    const now = formatDmyHms(new Date());
    this.api
      .save({
        organizationId: this.organizationId,
        unitId: this.unitId,
        specialityIdList: [...this.selectedSpecialityIds()],
        icpcSystemId: icpcSystemId as number,
        createdBy: this.userId,
        updatedBy: this.userId,
        status: 'A',
        createdDate: now,
        updatedDate: now,
        icpcCodeConfigurationId: this.icpcCodeConfigurationId(),
      })
      .subscribe({
        next: (res) => {
          // Real backend responses observed for this endpoint include a
          // third status literal beyond 'success'/'error' -- 'Already
          // Exists.' (ctrl:184-192) -- so this is read as a plain string,
          // not narrowed to ApiResponse['status'].
          const status: string = res.status;
          if (status === 'success') {
            // Create uses the server's own message; update always shows
            // this hardcoded string regardless of what the server
            // returned (ctrl:158-168).
            this.notify.success(this.updateFlag() ? 'Updated successfully' : res.message ?? 'Success!');
            this.resetForm();
            this.loadSpecialityMasterList();
            this.loadList();
          } else {
            this.notify.error(res.message ?? 'Error!');
            // An "Already Exists" response also reloads the list,
            // unlike a plain error response (ctrl:184-192).
            if (status === 'Already Exists.') {
              this.loadList();
            }
          }
        },
        error: () => this.notify.error('Error!'),
      });
  }

  private resetForm(): void {
    this.updateFlag.set(false);
    this.submitted.set(false);
    this.icpcCodeConfigurationId.set(null);
    this.selectedIcpcSystemId.set('');
    this.selectedSpecialityIds.set(new Set());
    this.showSpecialityDropdown.set(false);
  }

  // ---- edit (mirrors updateDetails, ctrl:374-404) ----
  edit(row: IcpcCodesConfigurationRow): void {
    this.updateFlag.set(true);
    this.submitted.set(false);
    this.showSpecialityDropdown.set(false);
    this.icpcCodeConfigurationId.set(row.icpcCodeConfigurationId);
    this.selectedIcpcSystemId.set(row.icpcSystemId);
    this.selectedSpecialityIds.set(new Set());

    // Passes the row's icpcSystemId as `icpcConfigurationId` -- matches
    // legacy exactly, see class comment on the FK/lookup-key bug.
    this.api
      .getSpecialityListByIcpcSystemId({
        organizationId: this.organizationId,
        unitId: this.unitId,
        icpcConfigurationId: row.icpcSystemId,
      })
      .subscribe({
        next: (res) => {
          if (res.status !== 'success') {
            this.notify.error('Something went wrong..');
            return;
          }
          const ids = new Set((res.listObject ?? []).map((s) => s.specialityId));
          this.selectedSpecialityIds.set(ids);
        },
        error: () => this.notify.error('Something went wrong..'),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:335-369) ----
  toggleStatus(row: IcpcCodesConfigurationRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api
      .updateStatus({
        updatedDate: formatDmyHms(new Date()),
        updatedBy: this.userId,
        status: nextStatus,
        icpcConfigurationId: row.icpcCodeConfigurationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.fullList.update((rows) =>
              rows.map((r) =>
                r.icpcCodeConfigurationId === row.icpcCodeConfigurationId ? { ...r, status: nextStatus } : r,
              ),
            );
            this.notify.success(res.message ?? 'Success!');
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
        },
        error: () => this.notify.error('Error!'),
      });
  }

  // ---- "Specialty Details" read-only info popup (mirrors
  // getSpecialityListByID, ctrl:290-332) ----
  viewSpecialityDetails(row: IcpcCodesConfigurationRow): void {
    this.api
      .getSpecialityListByIcpcSystemId({
        organizationId: this.organizationId,
        unitId: this.unitId,
        icpcConfigurationId: row.icpcSystemId,
      })
      .subscribe({
        next: (res) => {
          this.specialityDetailsForModal.set(res.status === 'success' ? res.listObject ?? [] : []);
          this.showSpecialityDetailsModal.set(true);
        },
      });
  }

  closeSpecialityDetailsModal(): void {
    this.showSpecialityDetailsModal.set(false);
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching ctrl:142-143/339. */
function formatDmyHms(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
