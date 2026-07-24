import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { StateMasterService } from './state-master.service';
import { CountryOption, StateForm, StateRow, emptyStateForm } from './state-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/state-master.html +
 * resources/assets/js/controller/master/stateMasterController.js
 * (`stateMasterController`, plain `$scope`, no `controllerAs`). State
 * `stateMaster` (app.js:3428-3452 resolve block, confirmed live), moduleId
 * 384. Organization-scoped, same as raceMaster -- no `unitId` anywhere.
 *
 * Backend confirmed live 2026-07-21 via
 * tools/angular-audit/check_backend_endpoints.py against a real dev-Tomcat
 * JWT: all 7 endpoints returned HTTP 200 (getActiveCountryList's bare-GET
 * check returned 405, meaning the route IS mapped, just not to a
 * segment-less GET -- expected, not a missing-backend signal).
 *
 * **This screen shares raceMaster's confirm-modal/reload divergences from
 * the amenity-master/qms-department-master reference screens (same two
 * findings, flagged once there, not re-litigated field-by-field here):**
 * 1. The list/count reload after save/update is UNCONDITIONAL (ctrl:233-266,
 *    ctrl:365-398) -- runs after both success and failure.
 * 2. FIXED, diverges from legacy (codebase-wide sweep + owner decision,
 *    2026-07-24, see docs/ANGULAR_PENDING_FINDINGS.md entry 152): a failed
 *    CREATE does NOT actually leave the confirm modal permanently disabled
 *    in real legacy, despite `init()` -- which resets `popUpFlag`/`showModal`
 *    -- only running in the success branch (ctrl:247-250) when read in
 *    isolation. Legacy's shared `<modal>` directive (`commonPopUpDirectives.js`)
 *    has its own hardcoded Ok-button reset logic that re-arms `popUpFlag`
 *    on every click regardless of outcome -- confirmed live against real,
 *    unmodified legacy (`cardTypeMaster`/`raceMaster`, duplicate-then-retry).
 *    `popUpFlag` below is now re-armed on both success AND failure to match.
 *
 * **Screen-specific findings, flagged for docs/ANGULAR_PENDING_FINDINGS.md:**
 * - `showUpdateBtn` (ctrl:271-298) uses a native `alert("Error!!")` on a
 *   failed by-id fetch, same as raceMaster's equivalent (a real trait
 *   shared by both siblings, not a divergence between them). Reproduced
 *   verbatim via `window.alert(...)`.
 * - `updateStatus` (ctrl:302-339), UNLIKE raceMaster's own `updateStatus`:
 *   reloads the list/count on BOTH branches (`initStateMasterList(...)`,
 *   which also always forces the pager back to page 1), and growls the
 *   server's own `response.data.message` as a `warning` on failure --
 *   not a hardcoded string at `error` severity like raceMaster. A genuine
 *   status-vocabulary/severity divergence between two near-identical
 *   sibling screens (see raceMaster's doc comment), reproduced distinctly
 *   here rather than shared.
 * - `init()` (ctrl:35-92) re-fetches the Country dropdown EVERY time it
 *   runs, including the resets after a successful save/update (ctrl:250,
 *   ctrl:382 both call `$scope.init()` in full) -- not just on first load.
 *   Reproduced by having `resetForm()` re-invoke `loadCountryOptions()`,
 *   not just the form/pagination reset.
 * - **The State Name field's character allowlist is a real, working,
 *   non-Angular constraint** (ctrl:342-348): legacy binds a raw jQuery
 *   `keypress` handler directly to `#statename-field` (bypassing Angular
 *   entirely) that blocks any keystroke not matching `/[a-zåäö ]/i` --
 *   letters, the å/ä/ö diacritics, and space only; digits/punctuation are
 *   silently rejected at the keystroke level. Reproduced below via a
 *   `(keypress)` binding on the same input calling the identical regex
 *   test, which is the Angular-idiomatic equivalent of the same behavior
 *   (the jQuery `$('#id').bind(...)` approach itself isn't meaningfully
 *   portable/reproducible-as-a-bug since it's a one-time global DOM bind,
 *   not app logic with an observable divergence from a template-level
 *   binding).
 * - **The second State Name validation message is genuinely DEAD, unlike
 *   most "always show required message" patterns** -- view:55-56 gates on
 *   `!stateform.stateName.$error.required && stateform.stateName.$dirty &&
 *   stateform.stateName.$invalid`, but no `ng-pattern`/`ng-minlength`/other
 *   validator is attached to this input anywhere in the view, so
 *   `$invalid` can only ever be `true` because of `$error.required` -- the
 *   exact case this condition excludes. This branch can never render.
 *   Omitted here entirely, matching the established convention of omitting
 *   confirmed-dead UI rather than rendering inert markup (see
 *   amenity-master's dead sort-caret omission). Flagged for
 *   docs/ANGULAR_PENDING_FINDINGS.md as a residual validation-duplication
 *   leftover (inverse of the `insuranceMaster` pattern, entry 1 -- here a
 *   *second* validator was apparently removed but its message wasn't).
 * - Per docs/ANGULAR_PENDING_FINDINGS.md entry 55 (standing policy), the
 *   confirm modal's close button is wired to an ordinary working
 *   `closeConfirmModal()`, NOT a reproduction of the shared `<modal>`
 *   directive's `cancel()` crash (stateMasterController.js never defines
 *   `$scope.cancel`) -- accepted standing improvement, not a parity gap.
 * - No missing-`$state`-DI crash risk here -- `$state` is correctly present
 *   in this controller's own DI list (ctrl:21/25), and this rebuild never
 *   ports that legacy-DI crash class regardless, per standing instruction.
 * - No dead search-box typo bug -- the search input is a plain, working
 *   client-side `filter:searchKeyword` (view:168/140), reproduced as a
 *   real filter below, same as raceMaster.
 * - **Minor, deliberately NOT reproduced DOM quirk (judgment call, low
 *   severity):** the Country `<select>`'s first `<option>` (view:19,
 *   `<option>Select Country</option>`) has no `value` attribute, so a
 *   browser's native default assigns it the value `"Select Country"` (its
 *   own text) rather than an empty string -- meaning if a user explicitly
 *   re-selects that placeholder after having picked a real country, legacy
 *   would post the literal string `"Select Country"` as `countryId` and
 *   `required` validation would NOT catch it (a non-empty string passes).
 *   This requires a deliberate, unusual user action to trigger and has no
 *   realistic impact; reproducing the exact DOM value-fallback semantics
 *   would add meaningfully more complexity for effectively no real risk
 *   reduction, so this rebuild uses a standard `value=""` placeholder
 *   instead (properly empty, properly caught by `required`). Flagged for
 *   docs/ANGULAR_PENDING_FINDINGS.md as a characterized-not-reproduced
 *   edge case, not silently dropped.
 */
@Component({
  selector: 'app-state-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './state-master.html',
  styleUrl: './state-master.scss',
})
export class StateMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(StateMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- country dropdown (mirrors $scope.countryList, ctrl:63-78) ----
  countryOptions = signal<CountryOption[]>([]);

  // ---- form state (mirrors $scope.state) ----
  form = signal<StateForm>(emptyStateForm());
  saveMode = signal(true); // true = saveBtnFlag visible (create); false = updateBtnFlag visible
  formSubmitted = signal(false);
  showConfirmModal = signal(false);
  // mirrors $scope.popUpFlag -- see class doc comment, divergence #2 (fixed).
  private popUpFlag = signal(true);

  // ---- list + server-side pagination (mirrors initStateMasterList/PagerService) ----
  list = signal<StateRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // mirrors `stateList | filter:searchKeyword` (view:168) -- a real,
  // working client-side filter over the current server-fetched page only.
  filteredList = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    if (!keyword) return this.list();
    return this.list().filter((r) => Object.values(r).some((v) => v != null && String(v).toLowerCase().includes(keyword)));
  });

  constructor() {
    this.loadCountryOptions();
    this.loadList();
    this.loadCount();
  }

  // ctrl:63-78.
  private loadCountryOptions(): void {
    this.api.getActiveCountryList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.countryOptions.set(res.listObject ?? []);
      },
    });
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getStateMasterList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') this.list.set(res.listObject ?? []);
      },
    });
  }

  private loadCount(): void {
    this.api.getStateMasterCount(this.organizationId).subscribe({
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

  updateField<K extends keyof StateForm>(field: K, value: StateForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  // ---- State Name character allowlist (mirrors the jQuery `testInput`
  // keypress binder, ctrl:342-348) -- letters, å/ä/ö, and space only. ----
  onStateNameKeypress(event: KeyboardEvent): void {
    const char = event.key;
    if (char.length === 1 && !/[a-zåäö ]/i.test(char)) {
      event.preventDefault();
    }
  }

  // ---- required-field validation (mirrors stateform.$valid via the 3
  // `required` inputs -- countryId/stateCode/stateName, view:18/36/52) ----
  private requiredFieldsValid(): boolean {
    const f = this.form();
    return !!(f.countryId && f.stateCode.trim() && f.stateName.trim());
  }

  isCountryInvalid(): boolean {
    return this.formSubmitted() && !this.form().countryId;
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().stateCode.trim();
  }

  isNameInvalid(): boolean {
    return this.formSubmitted() && !this.form().stateName.trim();
  }

  // ---- save / confirm-modal (mirrors saveState, ctrl:211-269) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl guard (no else branch)
    if (this.popUpFlag()) {
      this.showConfirmModal.update((v) => !v); // matches ctrl:229-231 `showModal = !showModal`
    } else {
      // Guards only the narrow window while a confirmed save is still
      // in-flight -- see class doc comment, divergence #2 (fixed).
      this.doSave();
    }
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // mirrors PopupOkBtn (ctrl:97-100): flips popUpFlag then re-invokes save().
  confirmSave(): void {
    this.popUpFlag.set(false);
    this.doSave();
  }

  private doSave(): void {
    const f = this.form();
    const now = formatDateTime(new Date());
    this.api
      .saveState({
        stateCode: f.stateCode,
        stateName: f.stateName,
        countryId: f.countryId,
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
            this.notify.success(res.message ?? 'State added successfully...!!');
            // ctrl:247-250 -- init() (form + popUpFlag + showModal +
            // country-list reset) runs ONLY on success. See class doc comment.
            this.resetForm();
          } else {
            this.notify.error('State already exits...!!');
            // Fixed, diverges from legacy (see class doc comment,
            // divergence #2): re-arm the confirm gate on failure too.
            this.popUpFlag.set(true);
          }
          // ctrl:259-263 -- unconditional, outside the if/else. See class
          // doc comment, divergence #1.
          this.loadList();
          this.loadCount();
        },
      });
  }

  // ---- update (mirrors updateState, ctrl:350-400) -- no confirm modal ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.requiredFieldsValid()) return; // silent no-op, matches ctrl guard (no else branch)
    const f = this.form();
    this.api
      .updateState({
        stateCode: f.stateCode,
        stateName: f.stateName,
        countryId: f.countryId,
        stateId: f.stateId as number,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'State updated successfully...!!');
            this.resetForm();
          } else {
            this.notify.error('State already exits...!!');
          }
          // ctrl:391-396 -- unconditional, same as saveState(). See class
          // doc comment, divergence #1.
          this.loadList();
          this.loadCount();
        },
      });
  }

  // mirrors init()'s reset side effects (ctrl:35-92): country-list refetch,
  // state object, save/update flags, popUpFlag, showModal. Called only from
  // the success branches of doSave()/update() -- see class doc comment.
  private resetForm(): void {
    this.form.set(emptyStateForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popUpFlag.set(true);
    this.showConfirmModal.set(false);
    this.loadCountryOptions();
  }

  // ---- edit (mirrors showUpdateBtn, ctrl:271-298) ----
  edit(stateId: number): void {
    this.saveMode.set(false);
    this.api.getStateById(stateId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status !== 'success' || !res.listObject?.length) {
          // eslint-disable-next-line no-alert -- ctrl:295 uses a native
          // `alert("Error!!")` here, not growl. Faithfully reproduced.
          alert('Error!!');
          return;
        }
        const d = res.listObject[0];
        this.form.set({
          stateId: d.stateId,
          countryId: String(d.countryId), // ctrl:293 `.toString()`
          stateCode: d.stateCode,
          stateName: d.stateName,
        });
      },
    });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:302-339) ----
  toggleStatus(row: StateRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.stateId === row.stateId ? { ...r, status: nextStatus } : r)));
    this.api
      .updateStatusForState({
        stateId: row.stateId,
        status: nextStatus,
        updatedBy: this.userId,
        updatedDate: formatDateTime(new Date()),
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'State Updated successfully...!!');
          } else {
            // ctrl:332-335 -- reads the server's own message, `warning`
            // severity. Unlike raceMaster's equivalent. See class doc comment.
            this.notify.warning(res.message ?? 'Warning!');
          }
          // ctrl:330/336 -- UNLIKE raceMaster, both branches reload the
          // list/count here (and reset the pager to page 1, since
          // initStateMasterList's count callback always calls
          // setPage(1,false)). See class doc comment.
          this.currentPage.set(1);
          this.loadList();
          this.loadCount();
        },
      });
  }
}

/** `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`, matching
 * ctrl:212-214/303-305/351-353. */
function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
