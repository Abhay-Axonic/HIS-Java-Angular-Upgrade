import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { AgeGroupMasterService } from './age-group-master.service';
import {
  AgeGroupMasterPayload,
  AgeGroupMasterRecord,
  AgeTypeGroup,
  emptyAgeGroupMaster,
} from './age-group-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

type SortableColumn = 'ageGroupCode' | 'ageTypeGrpName';

/**
 * Parity rebuild of
 * views/master/LIS/age-group-master.html +
 * resources/assets/js/controller/lis/global/ageGroupController.js
 * (state `ageGroupMaster`, module id 416).
 *
 * Two legacy write-path behaviors are DELIBERATELY faithfully reproduced
 * here, not silently fixed -- see this batch's rebuild report for full
 * citations. Flagged as fix-candidates for the orchestrating session /
 * owner, per this project's standing "flag, don't silently fix" policy for
 * write-path bugs found during a rebuild:
 *
 * 1. `updatedBy` on save/update is always set from the record's
 *    `createdBy`, never the current session user
 *    (ageGroupController.js:78, `"updatedBy":$scope.common.createdBy`).
 *    Harmless on create (both happen to equal the current user right after
 *    `initCommon()`), but on every UPDATE of a pre-existing record,
 *    `$scope.common` has just been overwritten wholesale with the stored
 *    record's own fields (`showUpdateBtn`, ageGroupController.js:227), so
 *    `updatedBy` ends up permanently re-attributed to the record's
 *    ORIGINAL creator, not whoever is actually editing it. Confirmed
 *    persisted verbatim, not recomputed server-side:
 *    `AgeGroupDaoImpl.updateAgeGroupMaster` (dao:102-112) Dozer-maps the
 *    whole DTO onto the entity and saves it as-is.
 * 2. The from/to age-range sanity check
 *    (`validateAgeGroupInDays`, ageGroupController.js:248-256) only runs
 *    when `ageTypeGrpfromId >= ageTypeGrptoId`
 *    (ageGroupController.js:267, 318) -- i.e. only when the "From" unit is
 *    the same or a coarser granularity than "To". Whenever "From" uses a
 *    strictly finer unit than "To" (e.g. From=Days, To=Years), the
 *    day-equivalent comparison is skipped entirely regardless of the
 *    actual numeric values, and no backend check exists either
 *    (`AgeGroupMasterServiceImpl.addAgeGroupMaster`/`updateAgeGroupMaster`,
 *    service.java:29-79/172-221, compute `ageFromDay`/`ageToday` for
 *    storage but never compare them). A numerically-inverted range (e.g.
 *    From = 1000 Days, To = 1 Years) can be saved with zero warning,
 *    anywhere.
 */
@Component({
  selector: 'app-age-group-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './age-group-master.html',
  styleUrl: './age-group-master.scss',
})
export class AgeGroupMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(AgeGroupMasterService);

  private readonly userId = this.session.userId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;

  // ---- dropdown data (mirrors $scope.ageTypeGroupList) ----
  ageTypeGroupList = signal<AgeTypeGroup[]>([]);

  // ---- form state (mirrors $scope.common / saveBtnFlag / updateBtnFlag) ----
  record = signal<AgeGroupMasterRecord>(emptyAgeGroupMaster(this.userId, this.organizationId));
  saveMode = signal(true); // true = saveBtnFlag, false = updateBtnFlag
  submitted = signal(false); // drives *required* error display, like show-errors-check-validity

  // ---- list + pagination state ----
  list = signal<AgeGroupMasterRecord[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  searchKeyword = signal('');
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  // ---- sort state (mirrors $rootScope.sortColumn/column/reverse, app.js:456-468 --
  // a real, working, globally-shared sort handler; the view's
  // `ng-click='sortColumn(...)'`/`orderBy:column:reverse` bindings are live,
  // not dead markup, confirmed by reading app.js directly rather than
  // assuming an undefined-function click is a broken feature) ----
  sortColumnName = signal<SortableColumn | null>(null);
  sortReverse = signal(false);

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // ---- mirrors `commonList|orderBy:column:reverse|filter:searchKeyword` ----
  filteredList = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    let rows = this.list();
    if (keyword) {
      rows = rows.filter(
        (r) => r.ageGroupCode.toLowerCase().includes(keyword) || r.ageTypeGrpName.toLowerCase().includes(keyword),
      );
    }
    const col = this.sortColumnName();
    if (col) {
      rows = [...rows].sort((a, b) => {
        const av = (a[col] ?? '').toLowerCase();
        const bv = (b[col] ?? '').toLowerCase();
        return av < bv ? -1 : av > bv ? 1 : 0;
      });
      if (this.sortReverse()) {
        rows.reverse();
      }
    }
    return rows;
  });

  constructor() {
    this.loadAgeTypeGroupList();
    this.loadList();
  }

  private loadAgeTypeGroupList(): void {
    this.api.getAgeTypeGroupList().subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.ageTypeGroupList.set(res.listObject ?? []);
        }
      },
    });
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
        }
      },
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') {
          this.totalCount.set(res.object);
        }
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

  sortBy(col: SortableColumn): void {
    this.sortColumnName.set(col);
    this.sortReverse.update((r) => !r);
  }

  // ---- validation (mirrors ageGroupform's HTML5 `required`/`ng-maxlength`
  // plus the manual code-max-length message) ----
  isCodeInvalid(): boolean {
    const code = this.record().ageGroupCode;
    return this.submitted() && (code.trim().length === 0 || code.length > 10);
  }

  isFromUnitInvalid(): boolean {
    return this.submitted() && this.record().ageTypeGrpfromId === '';
  }

  isFromValueInvalid(): boolean {
    return this.submitted() && this.record().ageGroupFrom.trim().length === 0;
  }

  isToUnitInvalid(): boolean {
    return this.submitted() && this.record().ageTypeGrptoId === '';
  }

  isToValueInvalid(): boolean {
    return this.submitted() && this.record().ageGroupTo.trim().length === 0;
  }

  private isFormValid(r: AgeGroupMasterRecord): boolean {
    return (
      r.ageGroupCode.trim().length > 0 &&
      r.ageGroupCode.length <= 10 &&
      r.ageTypeGrpfromId !== '' &&
      r.ageGroupFrom.trim().length > 0 &&
      r.ageTypeGrptoId !== '' &&
      r.ageGroupTo.trim().length > 0
    );
  }

  /**
   * Converts an age value to a day-equivalent based on the unit-type id --
   * 1 = days, 2 = months (x30), anything else = years (x365). Verbatim port
   * of `$scope.validateAgeGroupInDays` (ageGroupController.js:248-256);
   * multipliers confirmed to match the backend's own
   * `ITransactionConstants` (DAY=1, MONTHS=2/x30, YEAR=3/x365).
   */
  private validateAgeGroupInDays(unitId: string, value: string): number {
    const id = Number(unitId);
    if (id === 1) {
      return Number(value);
    } else if (id === 2) {
      return 30 * Number(value);
    } else {
      return 365 * Number(value);
    }
  }

  /**
   * Mirrors the `if ($scope.common.ageTypeGrpfromId >=
   * $scope.common.ageTypeGrptoId)` gate in `save()`/`update()`
   * (ageGroupController.js:267-279, 318-330). Returns `false` (blocks save)
   * only when the gate fires AND the day-equivalent from-value exceeds the
   * day-equivalent to-value -- faithfully reproduced including the gate's
   * asymmetry (see the class-level doc comment, finding 2).
   */
  private isRangeValid(r: AgeGroupMasterRecord): boolean {
    if (r.ageTypeGrpfromId >= r.ageTypeGrptoId) {
      const fromVal = this.validateAgeGroupInDays(r.ageTypeGrpfromId, r.ageGroupFrom);
      const toVal = this.validateAgeGroupInDays(r.ageTypeGrptoId, r.ageGroupTo);
      if (fromVal > toVal) {
        return false;
      }
    }
    return true;
  }

  private buildPayload(r: AgeGroupMasterRecord): AgeGroupMasterPayload {
    return {
      ageGroupId: r.ageGroupId === '' ? undefined : r.ageGroupId,
      ageGroupCode: r.ageGroupCode,
      ageGroupFrom: Number(r.ageGroupFrom),
      ageGroupTo: Number(r.ageGroupTo),
      ageTypeGrpfromId: Number(r.ageTypeGrpfromId),
      ageTypeGrptoId: Number(r.ageTypeGrptoId),
      ageTypeGrpName: r.ageTypeGrpName,
      ageGroupStatus: r.ageGroupStatus,
      createdBy: r.createdBy,
      createdDate: r.createdDate,
      // Faithful reproduction of ageGroupController.js:78 -- see class doc
      // comment finding 1. NOT `r.updatedBy`.
      updatedBy: r.createdBy,
      updatedDate: r.updatedDate,
      orgId: r.orgId,
    };
  }

  save(): void {
    this.submitted.set(true);
    const r = this.record();
    if (!this.isFormValid(r)) return;
    if (!this.isRangeValid(r)) {
      this.notify.warning('Please Enter Valid Age Group');
      return;
    }

    this.api.add(this.buildPayload(r)).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Saved successfully');
          this.resetForm();
          this.currentPage.set(1);
          this.loadList();
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Save failed');
        }
      },
    });
  }

  update(): void {
    this.submitted.set(true);
    const r = this.record();
    if (!this.isFormValid(r)) return;
    if (!this.isRangeValid(r)) {
      this.notify.warning('Please Enter Valid Age Group');
      return;
    }

    this.api.update(this.buildPayload(r)).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Updated successfully');
          this.resetForm();
          this.currentPage.set(1);
          this.loadList();
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Update failed');
        }
      },
    });
  }

  private resetForm(): void {
    this.record.set(emptyAgeGroupMaster(this.userId, this.organizationId));
    this.submitted.set(false);
    this.saveMode.set(true);
  }

  // ---- edit (mirrors showUpdateBtn, ageGroupController.js:206-245) ----
  edit(ageGroupId: number): void {
    this.api.getById(this.organizationId, ageGroupId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.object) {
          const obj = res.object;
          if (obj.ageGroupStatus === 'I') {
            this.notify.warning('This age group entry is deactivated please active first than update.');
            return;
          }
          this.record.set({
            ageGroupId: obj.ageGroupId,
            ageGroupCode: obj.ageGroupCode,
            ageGroupFrom: String(obj.ageGroupFrom),
            ageGroupTo: String(obj.ageGroupTo),
            ageTypeGrpfromId: String(obj.ageTypeGrpfromId),
            ageTypeGrptoId: String(obj.ageTypeGrptoId),
            ageTypeGrpName: obj.ageTypeGrpName,
            ageGroupStatus: obj.ageGroupStatus,
            createdBy: obj.createdBy,
            createdDate: obj.createdDate,
            updatedBy: obj.updatedBy,
            updatedDate: obj.updatedDate,
            orgId: obj.orgId,
          });
          this.saveMode.set(false);
          this.submitted.set(false);
        } else if (res.status === 'error') {
          // Legacy uses a raw `alert("Error In Fetching Data")` here
          // (ageGroupController.js:238) instead of `growl.error` like every
          // other error path in this same controller -- normalized to the
          // app's standard toast notification for consistency with the rest
          // of this screen and every other rebuilt screen, not a behavioral
          // change (same message, same trigger, non-blocking presentation
          // instead of a blocking native dialog).
          this.notify.error('Error In Fetching Data');
        }
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ageGroupController.js:172-203) ----
  toggleStatus(row: AgeGroupMasterRecord): void {
    const nextStatus: 'A' | 'I' = row.ageGroupStatus === 'A' ? 'I' : 'A';
    this.api.changeStatus(this.organizationId, row.ageGroupId as number, nextStatus).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.update((rows) =>
            rows.map((r) => (r.ageGroupId === row.ageGroupId ? { ...r, ageGroupStatus: nextStatus } : r)),
          );
          this.notify.success(res.message ?? 'Status updated');
          // Legacy unconditionally resets the add/edit form on a successful
          // toggle (`$scope.initCommon()`, ageGroupController.js:189) --
          // toggling ANY row's status blanks whatever add/edit form the user
          // currently has open, even if it's for a different row. Faithfully
          // reproduced, not an accident of this rebuild's own wiring.
          this.resetForm();
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Update failed');
        }
      },
    });
  }

  updateField<K extends keyof AgeGroupMasterRecord>(field: K, value: AgeGroupMasterRecord[K]): void {
    this.record.update((r) => ({ ...r, [field]: value }));
  }
}
