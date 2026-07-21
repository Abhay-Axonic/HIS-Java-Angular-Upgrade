import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { RaceMasterService } from './race-master.service';
import { RaceForm, RaceRow, emptyRaceForm } from './race-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/race-master.html +
 * resources/assets/js/controller/master/raceMasterController.js
 * (`raceMasterController`, plain `$scope`, no `controllerAs`). State
 * `raceMaster` (app.js:3339-3365 resolve block, confirmed live), moduleId
 * 382. Organization-scoped (unlike unit-scoped masters such as
 * amenity-master) -- every call sends `organizationId` only, no `unitId`.
 *
 * Backend confirmed live 2026-07-21 via
 * tools/angular-audit/check_backend_endpoints.py against a real dev-Tomcat
 * JWT: all 6 endpoints this screen calls returned HTTP 200 (not the
 * `modalityTypeMaster`-class all-404 gap).
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - The confirm modal ("Are you sure you want to Save?", the shared
 *   `<modal>` directive wired via legacy's `PopupOkBtn`/`popUpFlag`) only
 *   gates CREATE (ctrl:201-262); `updateRace()` (ctrl:338-388) saves
 *   immediately with no confirm step -- same asymmetry as amenity-master/
 *   qms-department-master.
 * - Both `saveRace()`/`updateRace()` only check `raceform.$valid` with no
 *   `else` -- an invalid submit is a silent no-op (no growl, no modal).
 *
 * **2 genuine behavioral divergences from the amenity-master/
 * qms-department-master reference screens, confirmed by reading this
 * controller's own code, not assumed from the siblings -- flagged for
 * docs/ANGULAR_PENDING_FINDINGS.md:**
 *
 * 1. **The list/count reload after save/update is UNCONDITIONAL** (ctrl:227-258,
 *    ctrl:355-386): `initRaceMasterList(...)` runs after BOTH the success
 *    and the error branch of the response handler, sitting outside the
 *    `if(status=='success')/else` block entirely -- unlike amenity-master
 *    (reload only on success). Reproduced by calling `loadList()`/
 *    `loadCount()` unconditionally in both `confirmSave()` and `update()`.
 * 2. **A failed CREATE permanently disables the confirm modal for the rest
 *    of the session** (a real, subtle sequencing bug, not present in the
 *    amenity-master reference): `init()` -- which resets `popUpFlag` back to
 *    `true` and `showModal` back to `false` -- is called ONLY in the
 *    success branch (ctrl:241-243). On a duplicate-code failure, `growl.error`
 *    fires but `popUpFlag` stays `false` (it was flipped by `PopupOkBtn`,
 *    ctrl:195-198, to make the first confirmed attempt actually POST) and
 *    `showModal` is never reset either. Every subsequent `saveRace()` call
 *    in that session then skips the `if(popUpFlag)` branch entirely and
 *    posts directly, with no confirm dialog, until a save eventually
 *    succeeds (or the page is reloaded). Reproduced verbatim below via the
 *    `popUpFlag` signal, which `resetForm()` (called only on success) is
 *    the only thing that ever sets back to `true`.
 *
 * - `showUpdateBtn` (ctrl:265-293) uses a native `alert("Error!!")` on a
 *   failed by-id fetch -- NOT `growl.error` like amenity-master's
 *   equivalent path. Reproduced verbatim via `window.alert(...)` (same
 *   choice stateMaster's own `showUpdateBtn` makes, ctrl:271-298 there --
 *   this is a real trait shared by both siblings, not a divergence between
 *   them).
 * - `updateStatus` (ctrl:296-333) growls a hardcoded string on failure
 *   ("Something went wrong...!!", not `response.data.message`) and never
 *   calls `initRaceMasterList`/reloads anything on EITHER branch -- unlike
 *   stateMaster's own `updateStatus` (ctrl:302-339), which reloads the list
 *   on both branches and growls the server's own message as a `warning` on
 *   failure. A genuine status-vocabulary/severity divergence between two
 *   otherwise near-identical sibling screens -- flagged for
 *   docs/ANGULAR_PENDING_FINDINGS.md, reproduced distinctly per screen
 *   rather than sharing one implementation. The view's `ng-model="info.status"`
 *   is two-way bound directly to the row, so the toggle still visibly
 *   flips immediately regardless of the response, with no revert on
 *   failure -- reproduced the same way as every other master's toggle
 *   (optimistic list-signal patch before the call resolves).
 * - The "Is Default" checkbox's `disableIsDefault` guard is DEAD CODE: the
 *   only logic that would ever set it `true` (looping the fetched list for
 *   an existing `isDefault=='A'` row, ctrl:116-121) is commented out.
 *   `showUpdateBtn` only ever sets it back to `false` (ctrl:286-287), a
 *   no-op given the default. Net effect: this checkbox is never actually
 *   disabled by anything, in legacy, today -- multiple races could be
 *   marked default with no client-side prevention. Reproduced by keeping
 *   the `disableIsDefault` signal permanently `false` (flagged for
 *   docs/ANGULAR_PENDING_FINDINGS.md as a live-vs-commented-out guard, per
 *   the playbook's Step 1 checklist).
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), the
 *   confirm modal's close button is wired to an ordinary working
 *   `closeConfirmModal()`, NOT a reproduction of the shared `<modal>`
 *   directive's `cancel()` crash (raceMasterController.js never defines
 *   `$scope.cancel`) -- accepted standing improvement, not a parity gap.
 * - No missing-`$state`-DI crash risk here (per entries 8/10) -- `$state`
 *   IS correctly present in this controller's own DI list (ctrl:21/25).
 *   Session-guard handling in this rebuild goes through `SessionService`/
 *   `moduleAccessGuard` defensively either way, per standing instruction to
 *   never port that legacy-DI crash class regardless.
 * - No dead "serach..." search-box typo bug here -- unlike amenity-master,
 *   this screen's search input has no `ng-keyup` binding at all; it's a
 *   plain client-side `filter:searchKeyword` over the already-fetched page
 *   (view:151/117), reproduced as a real, working filter below.
 */
@Component({
  selector: 'app-race-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './race-master.html',
  styleUrl: './race-master.scss',
})
export class RaceMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(RaceMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors $scope.race) ----
  form = signal<RaceForm>(emptyRaceForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false);
  showConfirmModal = signal(false);
  // mirrors $scope.popUpFlag -- see class doc comment, divergence #2.
  private popUpFlag = signal(true);
  // Dead guard -- see class doc comment. Never set `true` anywhere, matching
  // legacy's own commented-out enabling logic (ctrl:116-121).
  disableIsDefault = signal(false);

  // ---- list + server-side pagination (mirrors initRaceMasterList/PagerService) ----
  list = signal<RaceRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // mirrors `raceList | filter:searchKeyword` (view:151) -- a real, working
  // client-side filter over the current server-fetched page only, unlike
  // amenity-master's dead search box. See class doc comment.
  filteredList = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter((r) => Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)));
  });

  constructor() {
    this.loadList();
    this.loadCount();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getRaceMasterList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  private loadCount(): void {
    this.api.getRaceMasterCount(this.organizationId).subscribe({
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

  updateField<K extends keyof RaceForm>(field: K, value: RaceForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- required-field validation (mirrors raceform.$valid via the 2
  // `required` inputs -- raceCode/raceDesc, view:18/32; the "Is Default"
  // checkbox has no `required`) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.raceCode.trim() && f.raceDesc.trim());
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().raceCode.trim();
  }

  isDescInvalid(): boolean {
    return this.formSubmitted() && !this.form().raceDesc.trim();
  }

  // ---- save / confirm-modal (mirrors saveRace, ctrl:201-262) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl guard (no else branch)
    if (this.popUpFlag()) {
      this.showConfirmModal.update((v) => !v); // matches ctrl:209-211 `showModal = !showModal`
    } else {
      // See class doc comment, divergence #2: only reachable after a prior
      // failed save this session -- confirm modal is permanently skipped.
      this.doSave();
    }
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors PopupOkBtn (ctrl:195-198): flips popUpFlag then re-invokes save().
  confirmSave(): void {
    this.popUpFlag.set(false);
    this.doSave();
  }

  private doSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .saveRace({
        raceCode: f.raceCode,
        raceName: f.raceDesc,
        status: 'A',
        updatedBy: this.userId,
        createdBy: this.userId,
        organizationId: this.organizationId,
        createdDate: now,
        updatedDate: now,
        isDefault: f.isDefault,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Race added successfully...!!');
            // ctrl:241-243 -- init() (form + popUpFlag + showModal reset)
            // runs ONLY on success. See class doc comment, divergence #2.
            this.resetForm();
          } else {
            this.notify.error('Race already exits...!!');
          }
          // ctrl:252-257 -- unconditional, outside the if/else. See class
          // doc comment, divergence #1.
          this.loadList();
          this.loadCount();
        },
      });
  }

  // ---- update (mirrors updateRace, ctrl:338-388) -- no confirm modal ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl guard (no else branch)
    const f = this.form();
    this.api
      .updateRace({
        raceCode: f.raceCode,
        raceName: f.raceDesc,
        raceId: f.raceId as number,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        isDefault: f.isDefault,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Race updated successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('Race already exits...!!');
          }
          // ctrl:379-384 -- unconditional, same as saveRace(). See class
          // doc comment, divergence #1.
          this.loadList();
          this.loadCount();
        },
      });
  }

  // mirrors init()'s form-reset side effects (ctrl:29-79): race object,
  // save/update flags, popUpFlag, showModal. Called only from the success
  // branches of doSave()/update() -- see class doc comment.
  private resetForm(): void {
    this.form.set(emptyRaceForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popUpFlag.set(true);
    this.showConfirmModal.set(false);
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:265-293) ----
  edit(raceId: number): void {
    this.saveMode.set(false);
    this.api.getRaceById(raceId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          // eslint-disable-next-line no-alert -- ctrl:289 uses a native
          // `alert("Error!!")` here, not growl. Faithfully reproduced.
          alert('Error!!');
          return;
        }
        const d = res.listObject[0];
        this.form.set({ raceId: d.raceId, raceCode: d.raceCode, raceDesc: d.raceName, isDefault: d.isDefault });
        // ctrl:286-287 -- no-op given disableIsDefault is already always
        // false. See class doc comment.
        if (d.isDefault === 'A') this.disableIsDefault.set(false);
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:296-333) ----
  toggleStatus(row: RaceRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    // No revert on failure -- the view's checkbox is two-way bound, so it
    // has already visually flipped regardless of the response. See class
    // doc comment.
    this.list.update((rows) => rows.map((r) => (r.raceId === row.raceId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatusForRace({
        raceId: row.raceId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Race Updated Successfully...!!');
          } else {
            // ctrl:324-330 -- hardcoded message, `error` severity, no
            // reload. Unlike stateMaster's equivalent. See class doc comment.
            this.notify.error('Something went wrong...!!');
          }
          // Deliberately no loadList()/loadCount() call here on either
          // branch -- see class doc comment.
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:202-204/297-299/339-341. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
