import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { PrefixConfigurationService } from './prefix-configuration.service';
import { FormatOption, PrefixRow, ResetOption, ScreenOption } from './prefix-configuration.models';

const SELECT_SCREEN: ScreenOption = { screen: 'Select Screen' };
const SELECT_RESET: ResetOption = { resetName: 'Select Reset' };
const SELECT_FORMAT: FormatOption = { formatName: 'Select Format' };

// format.id -> hardcoded preview token, straight off `examplePreview` (ctrl:283-317).
const EXAMPLE_PREVIEW: Record<number, string> = {
  1: '2018',
  2: '18',
  3: 'MARCH',
  4: 'MAR',
  5: '03',
  6: '15',
  7: 'PUN',
  8: 'PH',
  9: 'ABC',
  10: '/',
  11: '-',
};

/**
 * Parity rebuild of
 * views/master/UnitMaster/configuration/prefix-master.html +
 * resources/assets/js/controller/master/unit/prefixConfigurationMasterController.js.
 * State self-registers moduleId 699 (ctrl:321-350, confirmed live via its own
 * resolve block, not app.js) -- wired centrally by the routing task, not
 * here. `controllerAs:"vm"` in legacy, view binds `vm.model.*`.
 *
 * The legacy view duplicates the ENTIRE form + table markup once per tab
 * (Global lines 33-256, Unit lines 259-482 of the view) even though both
 * tabs bind the SAME underlying `vm.model.*` fields -- there is no per-tab
 * *form* state, only per-tab *data* (the screens dropdown is filtered by
 * `isGlobal`, ctrl:69-78, and the search-list endpoint takes an
 * `orgUnitWiseList/.../{flag}` unit-vs-global flag, ctrl:91-99). This
 * rebuild renders the form and table ONCE, gated by `activeTab`, rather
 * than duplicating identical bindings twice in the DOM -- the calling
 * task's own audit already flagged this exact duplication as inflating the
 * "18 form fields" count, not 18 distinct live inputs.
 *
 * **Deliberate deviations/preserved quirks, not bugs in this rebuild:**
 *
 * - REAL bug, preserved verbatim: `self.model.prefixId` (ctrl:123, set only
 *   by `GetConfiguration`/`edit()`) is never reset anywhere, including
 *   inside `clearFields()` (ctrl:270-277) or after a successful
 *   create/update (ctrl:190-206, 226-237). Once any row has been edited in
 *   a session, `saveRequest` (ctrl:162-171) permanently routes every
 *   subsequent save through `updateRequest`/PATCH instead of
 *   `saveNewRequest`/POST -- even after clicking the eraser "Clear" button
 *   -- until a *different* row is edited. Reproduced as-is: `prefixId` is
 *   never cleared by `clearFields()` here either.
 * - REAL quirk, preserved verbatim: `clearFields()` (ctrl:270-277) resets
 *   `generatedCode`/`example`/`format`/`reset`/`length`/`startFrom` but NOT
 *   `screen` -- the previously selected "Prefix For" screen survives a
 *   Clear click. Reproduced as-is.
 * - REAL quirk, preserved verbatim: `GetConfiguration` (ctrl:116-143)
 *   deliberately leaves `self.model.format` untouched on edit (the matching
 *   `.find(...)` lookup is commented out, ctrl:132-134) -- editing an
 *   existing row never restores which format token was last appended, only
 *   the already-generated code string. Reproduced by NOT setting `format`
 *   in `edit()`.
 * - REAL quirk, preserved verbatim: `self.model.isPrefixFormatValid`
 *   (ctrl:148, drives the format `<select>`'s red-bottom-border class via
 *   `ng-class="{bottomRed: ...}"`, view:109/335) is only ever set `true` on
 *   a failed save validation and is never reset to `false` anywhere in the
 *   controller -- once the "[SF]"-missing check fails once, the red border
 *   is permanent for the rest of the session. Reproduced via
 *   `isPrefixFormatValid` never being cleared.
 * - REAL finding, not extra validation added: `saveRequest` (ctrl:162-171)
 *   only calls `ValidateGenerateCode` (ctrl:145-160), which checks that the
 *   generated-code string contains the literal substring `"[SF]"` and is
 *   <=90 chars -- there is NO required-field check anywhere for
 *   screen/reset/startFrom/length despite the view's `*` required markers
 *   (view:45,59,73,87,271,285,299,313) being purely cosmetic. Reproduced by
 *   NOT adding any such validation in `save()`.
 * - NOT reproduced (AngularJS `ngOptions`-rendering artifact, not a
 *   business rule): `PopulateDropdown`'s success handler (ctrl:62-88)
 *   resets `screens`/`resets`/`formats` to `[]` on EVERY iteration of its
 *   `angular.forEach` before concatenating the real options once after the
 *   loop -- the net effect is that all three "Select X" placeholder options
 *   are silently dropped from every dropdown after the FIRST successful
 *   load (the bound model value stays pointed at the now-list-absent
 *   placeholder object, an AngularJS "unknown option" rendering edge case,
 *   not a functional difference -- the placeholder was already
 *   unselectable/inert either way, and `GetConfiguration`'s own `.find()`
 *   calls, ctrl:124-130, would produce `undefined` rather than the
 *   placeholder on a lookup miss for the same reason). This rebuild keeps
 *   the placeholder in `screens()`/`resets()`/`formats()` permanently and
 *   falls back to it on a lookup miss, which is simpler and behaviorally
 *   equivalent (nothing is functionally reachable via the placeholder
 *   either way).
 * - NOT reproduced (same class of AngularJS reference-equality rendering
 *   artifact): `addToGeneratedCode`'s non-custom-format branch (ctrl:240-259)
 *   resets `self.model.format` to `formats[0]` and then IMMEDIATELY
 *   overwrites it again with a bare `{code:"",formatName:"",id:""}` object
 *   that isn't reference-equal to any real dropdown option -- functionally
 *   identical to the placeholder (both have a falsy `id`, so the ">>"
 *   button stays disabled either way; the custom-format branch, ctrl:260-267,
 *   resets to `formats[0]` cleanly with no such double-overwrite, which
 *   confirms this was leftover/redundant code rather than intentional).
 *   This rebuild always resets to `formats()[0]` for both branches.
 * - `LoaderService` is not wired here, matching the established convention
 *   of this codebase's other "simple master" screens (`ward-master`,
 *   `city-master`, `billing-group-master`, `organization-master`,
 *   `asset-category-master`), none of which wire it despite legacy calling
 *   `$rootScope.startLoader()/stopLoader()` around every call here too.
 * - The search box's client-side filter (view:183/225, 409/451, bare
 *   `filter: vm.model.search`) is narrowed to the actual displayed columns
 *   (screen/generated code/running number/reset/length) rather than
 *   literally reimplementing AngularJS's generic all-properties `filter`
 *   -- same simplification already established in this codebase's
 *   `asset-category-master`/`ward-master`/`city-master`.
 * - Legacy's missing `$state` DI (ctrl:8: `$state.go('login')` on a missing
 *   `cookieObject`, but `$state` is never in this controller's own DI list
 *   `$scope, $rootScope, $http, $cookies, CONSTANTS, GenericService, growl`
 *   -- would throw `ReferenceError: $state is not defined` on session
 *   expiry) is handled by this app's `moduleAccessGuard` + `SessionService`
 *   at the routing layer (wired centrally, not in this component) -- same
 *   non-reproduction already established for this exact bug class in
 *   `organization-master`.
 */
@Component({
  selector: 'app-prefix-configuration',
  imports: [FormsModule],
  templateUrl: './prefix-configuration.html',
  styleUrl: './prefix-configuration.scss',
})
export class PrefixConfiguration {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(PrefixConfigurationService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // 1 = Global tab, 2 = Unit tab (mirrors `self.model.globalLevel`/`unitLevel`).
  activeTab = signal<1 | 2>(1);

  screens = signal<ScreenOption[]>([SELECT_SCREEN]);
  resets = signal<ResetOption[]>([SELECT_RESET]);
  formats = signal<FormatOption[]>([SELECT_FORMAT]);

  screen = signal<ScreenOption>(SELECT_SCREEN);
  reset = signal<ResetOption>(SELECT_RESET);
  format = signal<FormatOption>(SELECT_FORMAT);
  formatCode = signal('');
  startFrom = signal('');
  length = signal('');
  generatedCode = signal('');
  example = signal('');
  isPrefixFormatValid = signal(false);

  // Never reset by clearFields() -- see class doc comment (REAL bug, preserved).
  prefixId = signal<number | undefined>(undefined);

  rows = signal<PrefixRow[]>([]);
  search = signal('');

  showConfirmModal = signal(false);

  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.rows();
    if (!keyword) return rows;
    return rows.filter(
      (r) =>
        r.screenName?.toLowerCase().includes(keyword) ||
        r.generatedCode?.toLowerCase().includes(keyword) ||
        String(r.runningNumber ?? '').toLowerCase().includes(keyword) ||
        r.resetName?.toLowerCase().includes(keyword) ||
        String(r.length ?? '').toLowerCase().includes(keyword),
    );
  });

  constructor() {
    this.populateDropdown();
    this.loadList();
  }

  // ---- tabs (mirrors setTabAccordingToLevel) ----
  setTab(tab: 1 | 2): void {
    this.activeTab.set(tab);
    this.loadList();
    this.populateDropdown();
  }

  // ---- dropdowns (mirrors PopulateDropdown, ctrl:54-89) ----
  private populateDropdown(): void {
    this.screen.set(this.screens()[0]);
    this.reset.set(this.resets()[0]);
    this.format.set(this.formats()[0]);

    this.api.getDropdowns().subscribe({
      next: (res) => {
        const global = this.activeTab() === 1;
        const filteredScreens = (res.screen ?? []).filter((s) => Boolean(s.isGlobal) === global);
        this.screens.set([SELECT_SCREEN, ...filteredScreens]);
        this.resets.set([SELECT_RESET, ...(res.reset ?? [])]);
        this.formats.set([SELECT_FORMAT, ...(res.format ?? [])]);
      },
    });
  }

  // ---- list (mirrors PrefixConfigurationSearch, ctrl:91-114) ----
  private loadList(): void {
    const unitLevel = this.activeTab() === 2;
    this.api.search(this.unitId, this.organizationId, unitLevel).subscribe({
      next: (rows) => this.rows.set(rows ?? []),
      error: () => this.notify.error('Error'),
    });
  }

  // ---- edit (mirrors GetConfiguration, ctrl:116-143) ----
  edit(row: PrefixRow): void {
    this.api.getById(row.id).subscribe({
      next: (detail) => {
        this.prefixId.set(detail.id);
        this.screen.set(this.screens().find((s) => s.id === detail.screenId) ?? this.screens()[0]);
        this.reset.set(this.resets().find((r) => r.id === detail.resetId) ?? this.resets()[0]);
        // `format` intentionally NOT set here -- see class doc comment.
        this.startFrom.set(String(detail.runningNumber ?? ''));
        this.length.set(String(detail.length ?? ''));
        this.generatedCode.set(detail.generatedCode ?? '');
      },
    });
  }

  // ---- format token append (mirrors addToGeneratedCode, ctrl:240-268) ----
  addToGeneratedCode(): void {
    const fmt = this.format();
    if (fmt.id !== 9) {
      const token = fmt.code ?? '';
      this.appendExamplePreview(fmt.id);
      this.generatedCode.set(this.generatedCode() + '[' + token + ']' + '|');
      if (this.generatedCode().length > 90) {
        this.notify.warning('Entered generated code is too large!!');
      }
      this.format.set(this.formats()[0]);
    } else {
      const token = this.formatCode();
      this.generatedCode.set(this.generatedCode() + '{' + token + '}' + '|');
      this.appendExamplePreview(fmt.id);
      this.format.set(this.formats()[0]);
      this.formatCode.set('');
    }
  }

  private appendExamplePreview(formatId?: number): void {
    if (formatId != null && EXAMPLE_PREVIEW[formatId] !== undefined) {
      this.example.set(this.example() + EXAMPLE_PREVIEW[formatId]);
    }
  }

  // ---- clear (mirrors clearFields, ctrl:270-277 -- `screen`/`prefixId` NOT reset) ----
  clearFields(): void {
    this.generatedCode.set('');
    this.example.set('');
    this.format.set(this.formats()[0]);
    this.reset.set(this.resets()[0]);
    this.length.set('');
    this.startFrom.set('');
  }

  openConfirmModal(): void {
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // ---- validation (mirrors ValidateGenerateCode, ctrl:145-160) ----
  private validateGeneratedCode(): boolean {
    let valid = true;
    const code = this.generatedCode();
    if (!code.includes('[SF]')) {
      this.isPrefixFormatValid.set(true);
      valid = false;
      this.notify.error('Please Select Format as start from');
    }
    if (code.length > 90) {
      valid = false;
      this.notify.warning('Entered generated code is too large!!');
    }
    return valid;
  }

  // ---- save (mirrors saveRequest, ctrl:162-171) ----
  save(): void {
    this.showConfirmModal.set(false);
    if (!this.validateGeneratedCode()) return;
    if (this.prefixId() == null) {
      this.createNew();
    } else {
      this.updateExisting();
    }
  }

  // ---- create (mirrors saveNewRequest, ctrl:173-207) ----
  private createNew(): void {
    this.api
      .save({
        screenId: this.screen().id,
        resetId: this.reset().id,
        runningNumber: this.startFrom(),
        length: this.length(),
        generatedCode: this.generatedCode(),
        flag: this.screen().isGlobal,
        unitId: this.unitId,
        organizationId: this.organizationId,
        userId: this.userId,
      })
      .subscribe({
        next: (res) => {
          this.loadList();
          this.clearFields();
          this.populateDropdown();
          if (res.code === '409') {
            this.notify.warning(res.message ?? 'Duplicate entry');
          } else {
            this.notify.success(res.message ?? 'Saved successfully');
          }
        },
        error: () => this.notify.error('Something Went Wrong'),
      });
  }

  // ---- update (mirrors updateRequest, ctrl:210-238 -- no populateDropdown() afterward) ----
  private updateExisting(): void {
    const id = this.prefixId() as number;
    this.api
      .update(id, {
        screenId: this.screen().id,
        resetId: this.reset().id,
        runningNumber: this.startFrom(),
        length: this.length(),
        generatedCode: this.generatedCode(),
        unitId: this.unitId,
        organizationId: this.organizationId,
        userId: this.userId,
      })
      .subscribe({
        next: (res) => {
          this.loadList();
          this.clearFields();
          this.notify.success(res.message ?? 'Saved successfully');
        },
        error: () => this.notify.error('Something Went Wrong'),
      });
  }
}
