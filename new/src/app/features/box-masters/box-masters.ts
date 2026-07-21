import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { BoxMastersService } from './box-masters.service';
import { BoxForm, BoxRow, emptyBoxForm } from './box-masters.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/box-master.html +
 * resources/assets/js/controller/billing/unit/boxMastersController.js
 * (`BoxMasters.Controller`; `controllerAs:'vm'` is declared in the state
 * config but never used -- the view/controller are 100% `$scope.*`, zero
 * `vm.*` bindings, the same declared-but-unused pattern as entry 11). The
 * state self-registers its OWN `.config($stateProvider)` in this same
 * controller file (ctrl:258-285), NOT in app.js -- url `/boxMasters`,
 * moduleId 1069, confirmed live via the sidebar's own `checkAccess(1069)`
 * entry (views/common/new-side-bar.html:3952).
 *
 * Confirmed real, working CRUD: save (ctrl:31-79), update (ctrl:197-224),
 * edit-populate `viewBtn` (ctrl:174-195), status toggle `updateStatus`
 * (ctrl:226-250) -- all wired to real endpoints, see box-masters.service.ts.
 *
 * **Explicitly dropped per audit + rebuild instruction, NOT ported:**
 * - `serachDepartmentMaster` (box-master.html:80, `ng-keyup`) is never
 *   defined anywhere in boxMastersController.js -- confirmed via full-file
 *   grep. It's a copy-paste leftover: the real function of that name lives
 *   in a completely different screen's controller
 *   (specialityMasterController.js:516), where it POSTs to
 *   `/adt/departmentMasterUniversalSearch`, an unrelated department-name
 *   lookup with nothing to do with box code/description data. It rides dead
 *   alongside the real, WORKING client-side `filter:searchKeyword` already
 *   applied to the same `ng-repeat` (box-master.html:98) -- dropped
 *   entirely here; the working filter is kept, reimplemented below as
 *   `displayedRows`.
 * - The "Department Master" copy-paste debris throughout the legacy view
 *   (commented-out `<legend>Department Master</legend>`, the
 *   `Specialityform`/`specialityCode`/`specialityName` form/field naming,
 *   and the page-header-text/sidebar-nav-highlight jQuery block that both
 *   target "Department Master"/`specialityMaster` instead of this screen)
 *   -- all N/A here: real `boxCode`/`boxDescription` naming throughout, and
 *   the `Shell` component sets page title/active-nav-highlight centrally
 *   from route data, so there is deliberately no per-page title/highlight
 *   code to port.
 * - Legacy's redundant triple-fetch of `getMrdBoxList` on every load/save --
 *   `getBox()` (ctrl:82-101) and `initBoxGroupMasterList()` (ctrl:109-131)
 *   both fire back-to-back at controller init, and `getBox()`'s own success
 *   handler re-triggers `initBoxGroupMasterList()` a 3rd time via
 *   `setNoOfRecords()`; `saveBox()`'s completion handler repeats the same
 *   double-call again on top of that. Normalized to exactly ONE
 *   `loadListAndCount()`/`loadPage()` call per trigger below -- same
 *   end-user-observable result, no wasted round-trips/race.
 *
 * **Faithfully preserved quirks, NOT bugs in this rebuild:**
 * - No confirm modal anywhere on this screen -- box-master.html never uses
 *   the shared `<modal>` directive (confirmed absent from the view);
 *   `saveBox()`/`showUpdateBtn()` both POST/PUT directly on click. Entry
 *   55's standing modal-`cancel()` exception does not apply here since
 *   there is no modal to begin with.
 * - `saveBox()` (ctrl:31-79) has explicit, sequential, field-specific growl
 *   validation ("Please Enter Box Code" / "Please Enter Box Description")
 *   -- NOT the silent-no-op-on-invalid-submit pattern this codebase's
 *   amenity-master/qms-department-master use. Reproduced verbatim via two
 *   early-return checks in `save()`, each with its own `notify.error(...)`.
 *   The HTML5 `required` + inline help-block message is a SEPARATE,
 *   independent validation layer on top of that (same dual-validation-layer
 *   shape as `insuranceMaster`, entry 1) -- both layers are reproduced.
 * - `save()`'s completion handler reloads the list and resets the form
 *   UNCONDITIONALLY -- on BOTH success and error (ctrl:72-77 run outside
 *   any `if`), unlike amenity-master's success-only reload. Reproduced by
 *   calling `loadListAndCount()`/`resetForm()` outside the
 *   `res.status === 'success'` branch.
 * - `showUpdateBtn()` (ctrl:197-224) growls on success ONLY -- there is no
 *   `else` branch for an app-level error status at all (unlike `save()`,
 *   which does growl on error). Reproduced by never calling `notify.error`
 *   in `update()`'s response handler.
 * - `viewBtn()` (ctrl:174-195) blocks editing an Inactive row with
 *   `growl.error("Please update status then you can edit.")` and returns --
 *   but unlike this codebase's amenity-master, the pencil icon itself is
 *   NOT visually greyed out in legacy (no `ng-class="{disabled:...}"`
 *   anywhere in box-master.html) -- it only refuses at click-time.
 *   Reproduced by omitting any `[class.disabled]` on the edit icon here.
 * - `updateStatus()` (ctrl:226-250) growls on success only (same asymmetry
 *   as `showUpdateBtn`), optimistically flips the row via the view's
 *   two-way-bound checkbox with no revert-on-failure -- reproduced via a
 *   signal `.update()` per docs/ANGULAR_PENDING_FINDINGS.md entry 12, not an
 *   in-place mutation -- and afterward calls ONLY `clearFunction()` (blanks
 *   the two text fields) plus a single list reload; it does NOT reset
 *   `saveMode`/the edit-target id. A user mid-edit who toggles some row's
 *   status has their in-progress Code/Description fields silently blanked
 *   while remaining stuck in Update mode against a now-empty form -- a real,
 *   faithfully-reproduced legacy quirk, not fixed here.
 * - Changing the "Show N entries" page size (`setNoOfRecords`, ctrl:105-107)
 *   does NOT reset the current page back to 1 in legacy (`$scope.offset` is
 *   read as-is, and is in fact never reassigned anywhere after its initial
 *   `0`) -- differs from amenity-master/qms-department-master's convention
 *   of resetting to page 1 on a page-size change. Reproduced by NOT calling
 *   `currentPage.set(1)` in `onPageSizeChange()` here.
 * - `updateBox()` always sends `status:'A'` regardless of the record's
 *   actual status (ctrl:202) -- harmless in practice since `edit()`'s guard
 *   above means Update is only ever reachable via a row that was Active to
 *   begin with.
 */
@Component({
  selector: 'app-box-masters',
  imports: [FormsModule, StatusToggle],
  templateUrl: './box-masters.html',
  styleUrl: './box-masters.scss',
})
export class BoxMasters {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(BoxMastersService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors the loose $scope.boxCode/boxDescription/boxID) ----
  form = signal<BoxForm>(emptyBoxForm());
  saveMode = signal(true); // true = Save button visible (create); false = Update button visible (edit)
  formSubmitted = signal(false);
  searchKeyword = signal('');

  // ---- list + server-side pagination ----
  list = signal<BoxRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // Legacy quirk preserved: search filters only the CURRENT server-fetched
  // page (`getMrd | filter:searchKeyword`, box-master.html:98), not the full
  // dataset -- same pattern as this codebase's qms-department-master
  // `displayedRows`. This is the WORKING half of legacy's search box; see
  // class doc comment on why the dead `serachDepartmentMaster` handler
  // riding alongside it was dropped instead of reproduced.
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter((r) => Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)));
  });

  constructor() {
    this.loadListAndCount();
  }

  // Unifies getBox()/initBoxGroupMasterList() into ONE call -- see class doc
  // comment on the normalized single-reload pattern. Always page 0.
  private loadListAndCount(): void {
    this.loader.start();
    this.api.getBoxList(this.organizationId, this.unitId, 0, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        this.list.set(res.listObject ?? []);
        if (typeof res.object === 'number') this.totalCount.set(res.object);
        this.currentPage.set(1);
      },
      error: () => this.loader.stop(),
    });
  }

  // mirrors masterList() via setPage(page,true) -- real per-page offset,
  // same single endpoint (legacy has no separate count-only call to skip
  // here, see service doc comment).
  private loadPage(page: number): void {
    this.loader.start();
    const offset = (page - 1) * this.pageSize();
    this.api.getBoxList(this.organizationId, this.unitId, offset, this.pageSize()).subscribe({
      next: (res) => {
        this.loader.stop();
        this.list.set(res.listObject ?? []);
        if (typeof res.object === 'number') this.totalCount.set(res.object);
      },
      error: () => this.loader.stop(),
    });
  }

  // Does NOT reset currentPage -- faithful quirk, see class doc comment.
  onPageSizeChange(): void {
    this.loadListAndCount();
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
    this.loadPage(page);
  }

  updateField<K extends keyof BoxForm>(field: K, value: BoxForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().boxCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().boxDescription.trim();
  }

  // ---- save (mirrors saveBox, ctrl:31-79) ----
  save(): void {
    this.formSubmitted.set(true);
    const f = this.form();
    if (!f.boxCode.trim()) {
      this.notify.error('Please Enter Box Code');
      return;
    }
    if (!f.boxDescription.trim()) {
      this.notify.error('Please Enter Box Description');
      return;
    }
    this.api
      .saveBox({
        boxCode: f.boxCode,
        boxDescription: f.boxDescription,
        unitId: this.unitId,
        organizationId: this.organizationId,
        updatedBy: this.userId,
        createdBy: this.userId,
        status: 'A',
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') this.notify.success(res.message ?? 'Success!');
          else if (res.status === 'error') this.notify.error(res.message ?? 'Error!');
          // ctrl:72-77 -- reload + reset unconditionally, regardless of
          // status; see class doc comment.
          this.loadListAndCount();
          this.resetForm();
        },
      });
  }

  private resetForm(): void {
    this.form.set(emptyBoxForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
  }

  // Clears just the two text fields -- mirrors clearFunction() (ctrl:26-29),
  // used standalone by toggleStatus() below (see class doc comment) rather
  // than the wider resetForm().
  private clearFields(): void {
    this.form.update((f) => ({ ...f, boxCode: '', boxDescription: '' }));
  }

  // ---- edit (mirrors viewBtn, ctrl:174-195) ----
  edit(row: BoxRow): void {
    if (row.status === 'I') {
      this.notify.error('Please update status then you can edit.');
      return;
    }
    this.saveMode.set(false);
    this.api.getBoxById(row.boxId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        const d = res.object;
        if (!d) return;
        this.form.set({ boxId: d.boxId, boxCode: d.boxCode, boxDescription: d.boxDescription });
      },
    });
  }

  // ---- update (mirrors showUpdateBtn, ctrl:197-224) -- NO required-field
  // validation at all in legacy (unlike save()), and success-only growl
  // (see class doc comment) ----
  update(): void {
    const f = this.form();
    if (f.boxId == null) return;
    this.api
      .updateBox({
        boxId: f.boxId,
        boxCode: f.boxCode,
        boxDescription: f.boxDescription,
        status: 'A',
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        createdBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') this.notify.success(res.message ?? 'Success!');
          // ctrl:210-215 -- no error growl at all, see class doc comment.
          this.loadListAndCount();
          this.resetForm();
        },
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:226-250) ----
  toggleStatus(row: BoxRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.boxId === row.boxId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateBoxStatus({
        boxId: row.boxId,
        boxCode: row.boxCode,
        boxDescription: row.boxDescription,
        status: nextStatus,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        createdBy: this.userId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') this.notify.success(res.message ?? 'Success!');
          // ctrl:239-245 -- no error growl branch, see class doc comment.
          this.clearFields();
          this.loadListAndCount();
        },
      });
  }
}
