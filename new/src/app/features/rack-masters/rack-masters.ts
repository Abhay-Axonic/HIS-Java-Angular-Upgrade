import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { RackMastersService } from './rack-masters.service';
import { RackMasterForm, RackMasterRow, emptyRackMasterForm } from './rack-masters.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/rack-master.html +
 * resources/assets/js/controller/billing/unit/rackMastersController.js
 * (self-registered controller `RackMasters.Controller`, state `rackMasters`
 * -- self-registered inside the controller file's own `.config()` block,
 * NOT app.js's central `$stateProvider`, same as `row-masters`/`box-masters`
 * -- architecturally orphaned from app.js's inventory but confirmed live
 * and reachable at `/rackMasters` via index.html's script tag + the
 * sidebar's real `href="#/rackMasters"` link, `checkAccess(1068)`). moduleId
 * 1068, confirmed from the state's own resolve block; no requireSuperUser
 * check.
 *
 * **This screen is a near-byte-identical structural sibling of `row-masters`
 * (MRD Row Master, moduleId 1070) and `box-masters` (MRD Box Master,
 * moduleId 1069) -- same "MRD <X> Master" family, evidently built by
 * copy-pasting one file into the other two and renaming `row`/`box` -->
 * `rack`. All 5 of the "recurring sibling-divergence" patterns from the
 * playbook were checked against `row-masters` specifically: no
 * status-vocabulary drift (`status: 'A'/'I'` throughout, identical), no
 * live-vs-commented-out guard differences, same workflow category (a plain
 * CRUD+status-toggle master, not a read-only report or worklist), no
 * mockup markers (every binding resolves to a real, defined `$scope.*`
 * function except the one dead search handler below, which row-masters also
 * has, under the SAME function name), and no new bug unique to this
 * "reference" copy that row-masters lacks -- rack-master.html/
 * rackMastersController.js are, line-for-line, the identical pattern with
 * `rack*` substituted for `row*`.
 *
 * `controllerAs: "vm"` is declared in the self-registered state config, but
 * the entire controller body and view use plain `$scope.*` throughout --
 * zero `vm.` references anywhere (confirmed via grep of both files) --
 * vestigial scaffolding, built here exactly like every other `$scope`-based
 * screen in this codebase.
 *
 * Confirmed real, working CRUD against `/mrdrack/*` (all 5 endpoints
 * confirmed to exist end-to-end, Controller -> Service -> DAO, in
 * `legacy/Global/src/main/java/com/param/global/mrd/`): save (ctrl:31-80),
 * list (ctrl:83-101, also independently confirmed via a golden-trace
 * capture showing 2 real `POST /mrdrack/getMrdRackList` calls on initial
 * load -- see the "collapsed" note below), get-by-id/edit-populate
 * (ctrl:168-188), update (ctrl:190-217), status toggle (ctrl:219-243). No
 * `<modal>` directive anywhere in this screen (0 occurrences, confirmed via
 * grep) -- the Save/Update anchors carry a dead `data-toggle="modal"`
 * Bootstrap attribute with no matching modal element, so entry 55's shared
 * `<modal>`-crash standing policy does NOT apply here (unlike this batch's
 * `weekend-master`). `save()` posts directly, no confirm step.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - `saveRack` (ctrl:31-80) validates `rackCode`/`rackDescription` with two
 *   separate growls ("Please Enter Rack Code"/"Please Enter Rack
 *   Description") before building the payload. `showUpdateBtn`
 *   (ctrl:190-217) -- the *update* path -- has **no validation of any
 *   kind**, client or growl: it builds `objUpdate` straight from whatever is
 *   currently in `$scope.rackCode`/`$scope.rackDescription` and PUTs it
 *   immediately. Reproduced verbatim: `save()` growls and early-returns on
 *   either empty field; `update()` has no early-return guard at all.
 * - `viewBtn` (ctrl:168-188) reads `response.data.object.rackId/rackCode/
 *   rackDescription` and switches to edit mode **unconditionally**, with no
 *   `response.data.status === 'success'` check at all. Reproduced as-is in
 *   `edit()`: no status check, only the pre-existing `status === 'I'` guard
 *   (ctrl:170-176, "Please update status then you can edit.") blocks entry
 *   into edit mode.
 * - `updateStatus` (ctrl:219-243) shows a growl **only on success**
 *   (`response.data.status == 'success'`) -- no `else` branch, so a
 *   failed/errored status toggle produces no user-visible feedback
 *   whatsoever. It then unconditionally calls `$scope.clearFunction()`
 *   (wiping only the `rackCode`/`rackDescription` text fields) and
 *   `$scope.getRack()` (a full server reload). Reproduced via
 *   `toggleStatus()`: no error notification on failure, form fields cleared
 *   and list reloaded after every response, success or not.
 * - The list's search box (`ng-model="searchKeyword"`, view:80) feeds a real
 *   working `getMrd |filter:searchKeyword` pipe on the table (view:98) --
 *   *and*, on the very same input, an
 *   `ng-keyup="serachDepartmentMaster(searchKeyword)"` that calls a function
 *   never defined anywhere in rackMastersController.js (confirmed via
 *   full-file grep) -- the IDENTICAL dead function name row-masters' own
 *   copy-pasted view uses, plus leftover `Specialityform`/`specialityCode`/
 *   `specialityName` field names (view:7/19/37) confirming both screens were
 *   copy-pasted from the same Speciality/Department Master source. Both
 *   halves reproduced here exactly like row-masters: `displayedRows` is a
 *   working client-side filter over `rackCode`/`rackDescription`, and
 *   `onSearchKeyup()` throws.
 * - Legacy's `getRack()`/`initRackGroupMasterList()`/`masterList()` are
 *   three near-duplicate functions that all hit `/mrdrack/getMrdRackList`
 *   and all get invoked redundantly on page load (`getRack()` then
 *   `initRackGroupMasterList()` fire back-to-back at controller init,
 *   independently confirmed via a golden-trace capture showing 2 identical
 *   `POST /mrdrack/getMrdRackList` calls with identical params). Collapsed
 *   here into a single `loadList()`, called once per page-load/save/
 *   update/status change -- not a behavior change (both calls hit the
 *   identical endpoint with identical params and both just re-set the same
 *   list/count signals), purely dropping wasted duplicate network calls,
 *   matching the exact precedent row-masters already established for this
 *   same bug shape.
 */
@Component({
  selector: 'app-rack-masters',
  imports: [FormsModule, StatusToggle],
  templateUrl: './rack-masters.html',
  styleUrl: './rack-masters.scss',
})
export class RackMasters {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(RackMastersService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors loose $scope.rackCode/rackDescription/rackID) ----
  form = signal<RackMasterForm>(emptyRackMasterForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false); // only ever set by save() -- see class comment on update()'s asymmetry

  // ---- list + server-side pagination (mirrors getRack/PagerService) ----
  list = signal<RackMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  search = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: a real working filter, live alongside the
  // crashing ng-keyup handler on the same input -- see class comment.
  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter(
      (r) => r.rackCode?.toLowerCase().includes(keyword) || r.rackDescription?.toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getMrdRackList(this.unitId, this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
          if (typeof res.object === 'number') this.totalCount.set(res.object);
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

  updateFormField<K extends keyof RackMasterForm>(field: K, value: RackMasterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  isRequiredMissing(value: string): boolean {
    return this.formSubmitted() && !value.trim();
  }

  // ---- save (mirrors saveRack, ctrl:31-80) -- growl-validated, no modal ----
  save(): void {
    this.formSubmitted.set(true);
    const f = this.form();
    if (!f.rackCode.trim()) {
      this.notify.error('Please Enter Rack Code');
      return;
    }
    if (!f.rackDescription.trim()) {
      this.notify.error('Please Enter Rack Description');
      return;
    }
    this.api
      .saveMrdRack({
        rackCode: f.rackCode,
        rackDescription: f.rackDescription,
        unitId: this.unitId,
        organizationId: this.organizationId,
        updatedBy: this.userId,
        createdBy: this.userId,
        status: 'A',
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
          } else {
            this.notify.error(res.message ?? 'Error!');
          }
          this.resetForm();
          this.loadList();
        },
      });
  }

  // ---- update (mirrors showUpdateBtn, ctrl:190-217) -- NO validation at
  // all, unlike save() -- see class comment ----
  update(): void {
    const f = this.form();
    this.api
      .updateMrdRack({
        rackId: f.rackId as number,
        rackCode: f.rackCode,
        rackDescription: f.rackDescription,
        status: 'A',
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        createdBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
          }
          this.resetForm();
          this.loadList();
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyRackMasterForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  private clearFunction(): void {
    this.updateFormField('rackCode', '');
    this.updateFormField('rackDescription', '');
  }

  // ---- edit (mirrors viewBtn, ctrl:168-188) -- no response.status check ----
  edit(row: RackMasterRow): void {
    if (row.status === 'I') {
      this.notify.error('Please update status then you can edit.');
      return;
    }
    this.api.getMrdRackById(row.rackId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.form.set({
          rackId: res.object?.rackId as number,
          rackCode: res.object?.rackCode as string,
          rackDescription: res.object?.rackDescription as string,
        });
        this.saveMode.set(false);
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:219-243) ----
  toggleStatus(row: RackMasterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.api
      .updateMrdRackStatus({
        rackId: row.rackId,
        rackCode: row.rackCode,
        rackDescription: row.rackDescription,
        status: nextStatus,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        createdBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          // ctrl:233-238 -- growl only on success, no error branch at all;
          // see class comment.
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Success!');
          }
          // ctrl:239-240 -- clearFunction() + getRack() run unconditionally,
          // regardless of success/failure.
          this.clearFunction();
          this.loadList();
        },
        error: () => {
          this.clearFunction();
          this.loadList();
        },
      });
  }

  /** Faithful crash replication -- see class doc comment. Legacy's
   * rack-master.html:80 wires `ng-keyup="serachDepartmentMaster(searchKeyword)"`
   * to a function never defined anywhere in rackMastersController.js --
   * the identical dead-function name row-masters' own copy-pasted view
   * uses. Per docs/ANGULAR_PENDING_FINDINGS.md's standing convention for
   * this class of copy-paste crash bug, the crash is reproduced directly
   * rather than silently fixed; the `filter:searchKeyword` pipe (see
   * `displayedRows`) genuinely keeps working alongside it. */
  onSearchKeyup(): void {
    throw new TypeError('serachDepartmentMaster is not a function');
  }
}
