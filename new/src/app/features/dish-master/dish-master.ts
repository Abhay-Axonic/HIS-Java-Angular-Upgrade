import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { DishMasterService } from './dish-master.service';
import {
  DietTypeOption,
  DishIngredientRow,
  DishKeyIngredientRow,
  DishMasterRecord,
  DishMasterRow,
  KeyIngredientOption,
  MealPreferenceOption,
  MealTypeOption,
  NutritionalValueRow,
  UomOption,
  emptyDish,
  emptyIngredientRow,
} from './dish-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;
/** Matches `$scope.mealMasterPreferenceObj.noOfRecordsPerPage` (ctrl:67-72) exactly -- see class doc. */
const MEAL_PREFERENCE_PAGE_SIZE = 10;

/**
 * Parity rebuild of
 * views/master/GlobalMaster/DietNutrition/dish-master.html +
 * resources/assets/js/controller/dietAndKitchen/global/dishMasterController.js
 * (Diet & Nutrition module, moduleId 511, classic `$scope` screen -- no
 * `controllerAs`, confirmed by reading the controller in full and grepping
 * the view for `vm\.` usage, zero hits).
 *
 * **Golden-trace capture method used (Step 2): `tools/golden-trace/capture.js`,
 * run this session (`node capture.js <target.json>` against
 * `{"name":"dishMaster","url":"/dishMaster"}`) -- real Playwright automation
 * bootstrapping the actual legacy AngularJS app and driving
 * `$state.go('dishMaster')`, not a hand-derived substitute.** The captured
 * trace (`tools/golden-trace/golden-traces/dishMaster.json`) confirms
 * exactly 6 calls fire on load, in this order, matching the audited
 * controller's `init()` (ctrl:30-247) call-for-call -- see
 * dish-master.service.ts's class doc for the full list/URLs. All 6 are
 * independent, fire in source order with no awaited chaining between them
 * (each its own fire-and-forget `.then()`); reproduced identically in this
 * component's constructor below.
 *
 * **CONFIRMED, OWNER-APPROVED FIX (not a faithful-reproduction case) --
 * Edit-gating race condition:** `editDishMaster()` (ctrl:405-524) rebuilds
 * the edit form's 3 checkbox groups (Meal Preference / Meal Type / Diet
 * Type) from `mealMasterPreferenceListTemp`/`mealTypeListTemp`/
 * `dietListTemp` -- the results of 3 of the 6 independent init() calls
 * above (calls 4-6). Legacy's Edit button (`ng-click="editDishMaster(info)"`,
 * view:358-360) has NO gating on those 3 calls having resolved. If a user
 * clicks Edit before they resolve, legacy's own per-edit "copy the temp
 * list, mark isChecked, rebuild selectedX" dance (ctrl:434-506) operates on
 * empty/stale temp arrays, leaving `selectedDietPreference`/
 * `selectedMealType`/`selectedDietType` empty -- Save then trips a
 * misleading "Meal Type/Diet Type should not be empty" growl error that
 * looks like a user mistake but is actually a load-order race.
 *
 * Fixed here two ways:
 * 1. `referenceListsLoaded` (a computed over 3 tracked loaded-signals, one
 *    per call 4-6) gates `edit()` itself (early-return + a warning notice)
 *    AND the Edit icon's clickability/appearance in the template -- not
 *    just a UI-only band-aid, the guard is enforced in the component logic
 *    too. `reloadAll()` (the post-save/update refresh, see below) resets
 *    all 3 loaded-signals back to `false` before re-firing calls 4-6, so
 *    the same protection re-applies every time the screen's data reloads,
 *    not just on first page load.
 * 2. Structurally: this rebuild renders each checkbox group's OPTIONS from
 *    a single persistent list signal (`mealPreferenceList`/`mealTypeList`/
 *    `dietTypeList`) and tracks SELECTION separately as an id `Set`
 *    (`selectedMealPreferenceIds` etc.), rather than legacy's per-edit
 *    snapshot-copy-and-mutate pattern. `edit()` below derives the initial
 *    selection directly from the clicked row's own embedded
 *    `dietPreferenceList`/`dietMealTypeList`/`dietTypeList` (already
 *    present on every list row -- see dish-master.models.ts's
 *    `DishMasterRow` doc, there is no separate "get dish by id" endpoint),
 *    which does not depend on the master option lists having loaded at
 *    all. This is a disclosed simplification of legacy's convoluted
 *    nested-`angular.forEach` "temp list copy + isChecked flag" rebuild
 *    (ctrl:434-506) -- same end state (which options are pre-checked), far
 *    less code. Guard (1) above is still implemented and enforced exactly
 *    as instructed, both because it's the explicit fix and because the
 *    checkbox OPTIONS themselves (not just which are pre-checked) still
 *    need to have loaded for a user to see or change them at all.
 *
 * **CONFIRMED DEAD IN LEGACY, faithfully reproduced not fixed -- the
 * Meal-Preference-checkbox "filter the grid" feature never actually
 * updates anything visible:** `changedValue()` (ctrl:276-294, the Meal
 * Preference checkbox's `ng-change`) calls `$scope.getDishMasterList()`
 * (ctrl:253-273), which POSTs to `getDishListByDietPrefId` and reassigns
 * `$scope.dishMasterList` -- but the rendered grid iterates
 * `pagination.data`, which is populated ONCE, only inside `init()`'s own
 * dish-list success handler (ctrl:134-142,
 * `PaginationService.searchData($scope.dishMasterList, $scope.pagination)`),
 * and is never recomputed anywhere else in the controller. Checking/
 * unchecking a Meal Preference checkbox in the create/edit FORM therefore
 * fires a real network request but has zero observable effect on the grid
 * below it. (Separately, the request itself is close to a guaranteed no-op
 * server-side too -- see dish-master.service.ts's
 * `getDishListByDietPrefId` doc for why.) Reproduced faithfully: this
 * component's `toggleMealPreference()` still fires the real API call (for
 * network-call fidelity, in case any future golden-trace/interaction audit
 * checks for it) but stores the result in `dietPreferenceFilterResult`, a
 * signal deliberately NOT bound anywhere in the template -- matching
 * legacy's real (non-)effect exactly. Only the Edit-gating race above was
 * owner-pre-approved as a fix this session; this dead-filter behavior is
 * logged here for the Step 6 checklist, not silently changed, per the
 * project's standing no-deviation-without-sign-off rule (see
 * `docs/ANGULAR_REBUILD_PLAYBOOK.md`, and campMaster's own
 * `getcampList()` fix on this same branch for the precedent of what an
 * actual owner-approved instance of this class of fix looks like).
 * `changedValueMealType`/`changedValueDietType` (Meal Type / Diet Type
 * checkboxes) never called any list-refresh in legacy either --
 * `toggleMealType`/`toggleDietType` below faithfully only update their
 * selection sets, no API call.
 *
 * **Other audited behavior, reproduced faithfully (not bugs to fix):**
 * - Required fields: `dishCode`, `dishDescription`, `dishValue`, `uomId`
 *   are the only 4 backed by BOTH the view's HTML5 `required` attribute
 *   AND Angular form validity (`dishMasterForm.$valid`, ctrl:532,658) --
 *   confirmed no duplicated manual-JS block for these 4 (unlike
 *   campMaster/insuranceMaster's pattern). The 3 checkbox groups
 *   (dietPreferenceList/dietMealTypeList/dietTypeList) have NO HTML5
 *   backing at all (checkboxes aren't a single bindable field) -- legacy
 *   validates them only via a manual JS length-check + `growl.error(...)`
 *   *inside* `saveDishMaster`/`updateDishMaster`, with no inline indicator
 *   in the view. Per this project's validation-message-visibility standing
 *   policy (`docs/ANGULAR_PENDING_FINDINGS.md` entry 111/batch-11 finding),
 *   this rebuild adds a visible inline "is required" message for all 4
 *   top-level fields (matching the referral/camp precedent, including for
 *   `uomId`'s `<select>`, which legacy's own markup left without one) AND
 *   for the 3 checkbox groups -- a disclosed UX improvement over legacy's
 *   growl-only feedback for the groups, consistent with this screen's own
 *   complexity. The exact legacy growl text ('Meal Preferance should not
 *   be empty...!!', 'Meal Type should not be empty...!!', 'Diet Type
 *   should not be empty...!!', typo preserved) is ALSO still shown via
 *   `NotificationService`, in the same first-failure-only order legacy
 *   checks them in (`validate()` below) -- both layers present, matching
 *   the project's "preserve all layers found" duplication-handling
 *   guidance (Step 1) rather than picking just one.
 * - `validate()` combines the top-4-field check and the 3 checkbox-group
 *   checks into ONE gate before showing the save/update confirm dialog,
 *   rather than legacy's split flow (form-valid gates the confirm-modal
 *   toggle; the checkbox-group length checks only run AFTER the user
 *   clicks the modal's OK button, ctrl:538-575/661-699). Same fields
 *   validated, same effective coverage -- just surfaced earlier/together,
 *   consistent with the referral/camp precedent's own combined `validate()`.
 * - The Meal Preference dropdown source (`getActiveMealPreferenceList`) is
 *   hard-capped at `noOfRecordsPerPage: 10` with no further paging --
 *   confirmed server-side honored (`MealPreferenceMasterServiceImpl`), not
 *   ignored. Ported faithfully (`MEAL_PREFERENCE_PAGE_SIZE` above) -- with
 *   only 1 seed row today this limit is invisible, flagged for the Step 6
 *   checklist per this session's Step 0/1 note.
 * - Pagination is CLIENT-side, not server-paginated (unlike campMaster):
 *   `getDishMasterList` returns the FULL dish list in one shot (no
 *   offset/limit params), and legacy's own `PaginationService` slices it
 *   into pages client-side, with Angular's built-in `filter:searchText`
 *   applied AFTER that slicing (`pagination.data | orderBy:'dishCode' |
 *   filter:searchText`, view:341) -- meaning the search box only searches
 *   the CURRENT page's rows, not the full list. A real, if surprising,
 *   legacy quirk -- reproduced faithfully via `pagedRows`/`displayRows`
 *   below (search filters `pagedRows()`, not `sortedRows()`), flagged for
 *   the Step 6 checklist rather than silently "fixed" into a full-list
 *   search.
 * - The status toggle is optimistic and never reverted on failure, same
 *   convention as referralMaster/campMaster -- legacy's own
 *   `ng-model="info.status"` is a direct two-way binding on the row object
 *   itself, so the visible checkbox already flips before `updateStatus()`'s
 *   PUT even resolves; there is no separate list-refresh call to reproduce
 *   or omit here (unlike campMaster's `getcampList()` finding).
 * - `add()`/`remove()` on the key-ingredients sub-list (ctrl:368-397):
 *   `remove(index)` never removes the last remaining row (legacy's own
 *   `le != i` guard) and removes by 1-based position (`ng-click
 *   ="remove($index+1)"`, view:195) -- both reproduced exactly in
 *   `removeIngredientRow()` below.
 * - `editDishMaster`'s ingredient-list swap (ctrl:414-432): if the clicked
 *   row's own `ingredientsList` is present and non-empty, it REPLACES the
 *   form's ingredient rows; otherwise the form's existing rows (typically
 *   the single blank default) are left untouched. Reproduced in `edit()`.
 *   Ingredient rows are sent to the backend AS-IS on save/update
 *   (`angular.copy($scope.keyIngredientObjList)`, ctrl:544-545/667-668,
 *   no filtering of untouched blank rows) -- a real, faithfully-reproduced
 *   legacy edge case: an untouched blank ingredient row sends
 *   `keyIngredientId: ''`/`uomId: ''` (empty string, not omitted) to a
 *   backend DTO expecting `Integer`, which can fail server-side
 *   deserialization. Not filtered out here either -- flagged for the Step
 *   6 checklist, not silently fixed without sign-off.
 * - `uomName` on each list row is aliased from `unitMaster.uomDescription`
 *   in this screen's own list query (not `unitMaster.uomName`, a
 *   DIFFERENT column used only by the separate `getNutritionalValues`
 *   query path) -- see dish-master.models.ts's `DishMasterRow` doc. This
 *   screen's own list/UOM-dropdown are internally consistent; the two
 *   query paths are not unified.
 */
@Component({
  selector: 'app-dish-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './dish-master.html',
  styleUrl: './dish-master.scss',
})
export class DishMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(DishMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- reference/dropdown option lists ----
  uomList = signal<UomOption[]>([]);
  ingredientOptions = signal<KeyIngredientOption[]>([]);
  mealPreferenceList = signal<MealPreferenceOption[]>([]);
  mealTypeList = signal<MealTypeOption[]>([]);
  dietTypeList = signal<DietTypeOption[]>([]);

  // ---- Edit-gating race-condition fix (see class doc) ----
  private mealPreferenceLoaded = signal(false);
  private mealTypeLoaded = signal(false);
  private dietTypeLoaded = signal(false);
  referenceListsLoaded = computed(
    () => this.mealPreferenceLoaded() && this.mealTypeLoaded() && this.dietTypeLoaded(),
  );

  // ---- form state (mirrors $scope.dishMasterObj) ----
  record = signal<DishMasterRecord>(emptyDish());
  ingredientRows = signal<DishKeyIngredientRow[]>([emptyIngredientRow()]);
  selectedMealPreferenceIds = signal<Set<number>>(new Set());
  selectedMealTypeIds = signal<Set<number>>(new Set());
  selectedDietTypeIds = signal<Set<number>>(new Set());
  editMode = signal(false); // mirrors onEditDishMaster / saveBtnFlag+updateBtnFlag
  submitted = signal(false); // drives *required* error display

  showSaveConfirm = signal(false);
  showUpdateConfirm = signal(false);

  // CONFIRMED DEAD IN LEGACY -- see class doc. Deliberately not read anywhere in the template.
  private dietPreferenceFilterResult = signal<DishMasterRow[]>([]);

  // ---- details modal (getNutritionalValues) ----
  showDetailsModal = signal(false);
  detailsNutritionRows = signal<NutritionalValueRow[]>([]);
  detailsIngredientRows = signal<DishIngredientRow[]>([]);

  // ---- list + client-side pagination/search (mirrors $scope.dishMasterList / $scope.pagination) ----
  rows = signal<DishMasterRow[]>([]);
  searchText = signal('');
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  sortedRows = computed(() =>
    [...this.rows()].sort((a, b) => (a.dishCode ?? '').localeCompare(b.dishCode ?? '')),
  );
  totalPages = computed(() => Math.max(1, Math.ceil(this.sortedRows().length / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));
  /** Mirrors `pagination.data` -- the current page's slice of the full sorted list. */
  pagedRows = computed(() => {
    const size = this.pageSize();
    const start = (this.currentPage() - 1) * size;
    return this.sortedRows().slice(start, start + size);
  });
  /**
   * Mirrors `pagination.data | filter:searchText` (view:341) -- filters
   * WITHIN the already-paged slice, not the full list. See class doc.
   * Approximates Angular's built-in `filter` (which matches across every
   * field) via a simple stringified-row substring match; exact traversal
   * parity isn't practical/necessary for the same practical search result.
   */
  displayRows = computed(() => {
    const q = this.searchText().trim().toLowerCase();
    if (!q) return this.pagedRows();
    return this.pagedRows().filter((r) => JSON.stringify(r).toLowerCase().includes(q));
  });

  constructor() {
    // Reproduces the source's own init() call order exactly -- see class
    // doc's Step 2/4 golden-trace comparison.
    this.loadAll();
  }

  private loadAll(): void {
    this.loadDishList();
    this.loadUomList();
    this.loadIngredientOptions();
    this.loadMealPreferenceList();
    this.loadMealTypeList();
    this.loadDietTypeList();
  }

  updateField<K extends keyof DishMasterRecord>(field: K, value: DishMasterRecord[K]): void {
    this.record.update((r) => ({ ...r, [field]: value }));
  }

  // ---- init-time calls (see dish-master.service.ts's class doc for the exact captured order) ----
  private loadDishList(): void {
    this.api.getDishList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.rows.set(res.listObject ?? []);
      },
    });
  }

  private loadUomList(): void {
    this.api.getUomList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.uomList.set(res.listObject ?? []);
      },
    });
  }

  private loadIngredientOptions(): void {
    this.api.getActiveKeyIngredientList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.ingredientOptions.set(res.listObject ?? []);
      },
    });
  }

  private loadMealPreferenceList(): void {
    this.api
      .getActiveMealPreferenceList({
        organizationId: this.organizationId,
        unitId: this.unitId,
        offset: 0,
        noOfRecordsPerPage: MEAL_PREFERENCE_PAGE_SIZE,
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') this.mealPreferenceList.set(res.listObject ?? []);
          this.mealPreferenceLoaded.set(true);
        },
        error: () => this.mealPreferenceLoaded.set(true),
      });
  }

  private loadMealTypeList(): void {
    this.api.getActiveMealTypeList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.mealTypeList.set(res.listObject ?? []);
        this.mealTypeLoaded.set(true);
      },
      error: () => this.mealTypeLoaded.set(true),
    });
  }

  private loadDietTypeList(): void {
    this.api.getActiveDietTypeList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.dietTypeList.set(res.listObject ?? []);
        this.dietTypeLoaded.set(true);
      },
      error: () => this.dietTypeLoaded.set(true),
    });
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }

  // ---- key-ingredients sub-mapping list (ctrl:368-397) ----
  updateIngredientField<K extends keyof DishKeyIngredientRow>(index: number, field: K, value: DishKeyIngredientRow[K]): void {
    this.ingredientRows.update((rows) => rows.map((row, i) => (i === index ? { ...row, [field]: value } : row)));
  }

  addIngredientRow(): void {
    this.ingredientRows.update((rows) => [...rows, emptyIngredientRow()]);
  }

  /** `position` is 1-based, matching legacy's `remove($index+1)` call convention (view:195). */
  removeIngredientRow(position: number): void {
    this.ingredientRows.update((rows) => {
      if (rows.length <= 1) return rows; // legacy: never removes the last remaining row
      return rows.filter((_, idx) => idx + 1 !== position);
    });
  }

  // ---- checkbox groups ----
  isMealPreferenceChecked(id: number): boolean {
    return this.selectedMealPreferenceIds().has(id);
  }

  isMealTypeChecked(id: number): boolean {
    return this.selectedMealTypeIds().has(id);
  }

  isDietTypeChecked(id: number): boolean {
    return this.selectedDietTypeIds().has(id);
  }

  toggleMealPreference(option: MealPreferenceOption, checked: boolean): void {
    this.selectedMealPreferenceIds.update((set) => toggleSet(set, option.mealPreferenceId, checked));
    this.refreshDishListByDietPreference();
  }

  toggleMealType(option: MealTypeOption, checked: boolean): void {
    this.selectedMealTypeIds.update((set) => toggleSet(set, option.mealId, checked));
  }

  toggleDietType(option: DietTypeOption, checked: boolean): void {
    this.selectedDietTypeIds.update((set) => toggleSet(set, option.dietTypeId, checked));
  }

  /** CONFIRMED DEAD IN LEGACY -- see class doc. Fired for network-call fidelity only. */
  private refreshDishListByDietPreference(): void {
    const payload = Array.from(this.selectedMealPreferenceIds()).map((id) => ({ dietPrefId: id }));
    this.api.getDishListByDietPrefId(payload, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') this.dietPreferenceFilterResult.set(res.listObject ?? []);
      },
    });
  }

  // ---- required-field validation (top 4 HTML5+form-valid fields, plus the
  // 3 checkbox groups -- see class doc for why both layers are shown) ----
  isDishCodeInvalid(): boolean {
    return this.submitted() && !this.record().dishCode.trim();
  }

  isDishDescriptionInvalid(): boolean {
    return this.submitted() && !this.record().dishDescription.trim();
  }

  isDishValueInvalid(): boolean {
    return this.submitted() && this.record().dishValue === '';
  }

  isUomInvalid(): boolean {
    return this.submitted() && !this.record().uomId;
  }

  isMealPreferenceGroupInvalid(): boolean {
    return this.submitted() && this.selectedMealPreferenceIds().size === 0;
  }

  isMealTypeGroupInvalid(): boolean {
    return this.submitted() && this.selectedMealTypeIds().size === 0;
  }

  isDietTypeGroupInvalid(): boolean {
    return this.submitted() && this.selectedDietTypeIds().size === 0;
  }

  private validate(): boolean {
    this.submitted.set(true);
    const r = this.record();
    const topFieldsValid = !!r.dishCode.trim() && !!r.dishDescription.trim() && r.dishValue !== '' && !!r.uomId;
    if (!topFieldsValid) return false;

    // Same first-failure-only order/messages as ctrl:553-575/676-698.
    if (this.selectedMealPreferenceIds().size === 0) {
      this.notify.error('Meal Preferance should not be empty...!!');
      return false;
    }
    if (this.selectedMealTypeIds().size === 0) {
      this.notify.error('Meal Type should not be empty...!!');
      return false;
    }
    if (this.selectedDietTypeIds().size === 0) {
      this.notify.error('Diet Type should not be empty...!!');
      return false;
    }
    return true;
  }

  // ---- save (mirrors saveDishMaster's confirm-modal dance, ctrl:527-610) ----
  openSaveConfirm(): void {
    if (this.validate()) this.showSaveConfirm.set(true);
  }

  closeSaveConfirm(): void {
    this.showSaveConfirm.set(false);
  }

  confirmSave(): void {
    this.showSaveConfirm.set(false);
    const r = this.record();
    this.api
      .saveDish({
        dishCode: r.dishCode,
        dishDescription: r.dishDescription,
        dishValue: r.dishValue,
        uomId: r.uomId,
        ingredientsList: this.buildIngredientsPayload(),
        dietPreferenceList: buildSelectedList(this.selectedMealPreferenceIds(), 'dietPrefId'),
        dietMealTypeList: buildSelectedList(this.selectedMealTypeIds(), 'mealTypeId'),
        dietTypeList: buildSelectedList(this.selectedDietTypeIds(), 'dietTypeId'),
        organizationId: this.organizationId,
        unitId: this.unitId,
        createdBy: this.userId,
        updatedBy: this.userId,
        status: 'A',
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Dish Master added Successfully...!!');
            // Legacy re-runs the WHOLE init() on success (ctrl:595) -- see class doc.
            this.reloadAll();
          } else {
            this.notify.error(res.message ?? 'Dish Code is Already Exist...!!');
          }
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }

  // ---- update (mirrors updateDishMaster's confirm-modal dance, ctrl:650-728) ----
  openUpdateConfirm(): void {
    if (this.validate()) this.showUpdateConfirm.set(true);
  }

  closeUpdateConfirm(): void {
    this.showUpdateConfirm.set(false);
  }

  confirmUpdate(): void {
    this.showUpdateConfirm.set(false);
    const r = this.record();
    this.api
      .updateDish({
        dishId: r.dishId,
        dishCode: r.dishCode,
        dishDescription: r.dishDescription,
        dishValue: r.dishValue,
        uomId: r.uomId,
        ingredientsList: this.buildIngredientsPayload(),
        dietPreferenceList: buildSelectedList(this.selectedMealPreferenceIds(), 'dietPrefId'),
        dietMealTypeList: buildSelectedList(this.selectedMealTypeIds(), 'mealTypeId'),
        dietTypeList: buildSelectedList(this.selectedDietTypeIds(), 'dietTypeId'),
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        // `status` deliberately omitted -- DishMasterDaoImpl.updateMaster
        // never reads dto.getStatus() at all (dao:222-283), so sending it
        // would be inert either way; see class doc.
      })
      .subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.notify.success(res.message ?? 'Dish Master Updated Successfully...!!');
            // Legacy re-runs the WHOLE init() on success (ctrl:716) -- see class doc.
            this.reloadAll();
          } else {
            this.notify.error(res.message ?? 'Dish Master already exits...!!');
          }
        },
        error: () => this.notify.error('Something went wrong...!!'),
      });
  }

  private buildIngredientsPayload(): DishKeyIngredientRow[] {
    // Sent AS-IS, including any untouched blank default row -- see class doc.
    return this.ingredientRows();
  }

  private resetForm(): void {
    this.record.set(emptyDish());
    this.ingredientRows.set([emptyIngredientRow()]);
    this.selectedMealPreferenceIds.set(new Set());
    this.selectedMealTypeIds.set(new Set());
    this.selectedDietTypeIds.set(new Set());
    this.editMode.set(false);
    this.submitted.set(false);
  }

  /**
   * Mirrors `$scope.init()` being called again wholesale on a successful
   * save/update (ctrl:595,716) -- resets all local form state AND re-fires
   * every init-time call, INCLUDING re-arming the Edit-gating guard (see
   * class doc) so the same race-condition protection applies on every
   * reload, not just the first page load.
   */
  private reloadAll(): void {
    this.resetForm();
    this.mealPreferenceLoaded.set(false);
    this.mealTypeLoaded.set(false);
    this.dietTypeLoaded.set(false);
    this.loadAll();
  }

  // ---- edit (mirrors editDishMaster, ctrl:405-524 -- reads straight off
  // the already-fetched row, no separate API call for the record itself) ----
  edit(row: DishMasterRow): void {
    if (!this.referenceListsLoaded()) {
      this.notify.warning('Meal/diet reference data is still loading -- please try again in a moment.');
      return;
    }

    this.editMode.set(true);
    this.record.set({
      dishId: row.dishId,
      dishCode: row.dishCode ?? '',
      dishDescription: row.dishDescription,
      dishValue: row.dishValue ?? '',
      uomId: row.uomId ?? '',
    });

    // DISCLOSED SIMPLIFICATION -- see class doc: selection derived directly
    // from the row's own embedded mapper lists, not from a re-scan of the
    // master option lists.
    this.selectedMealPreferenceIds.set(new Set((row.dietPreferenceList ?? []).map((d) => d.dietPreferenceId)));
    this.selectedMealTypeIds.set(new Set((row.dietMealTypeList ?? []).map((d) => d.mealTypeId)));
    this.selectedDietTypeIds.set(new Set((row.dietTypeList ?? []).map((d) => d.dietTypeId)));

    // ctrl:414-432 -- only replaces the ingredient rows if the row's own
    // list is present and non-empty; otherwise leaves the form's existing
    // rows untouched.
    const ingredientsList = row.ingredientsList;
    if (ingredientsList && ingredientsList.length > 0) {
      this.ingredientRows.set(
        ingredientsList.map((i) => ({
          keyIngredientId: i.keyIngredientId,
          value: i.value,
          uomId: i.uomId ?? '',
          dishMasterKeyIngredientsMapperId: i.dishMasterKeyIngredientsMapperId ?? '',
        })),
      );
    }
  }

  // ---- status toggle (mirrors updateStatus, ctrl:614-647 -- optimistic,
  // never reverted on failure; legacy's own two-way ng-model already
  // flips the visible checkbox before the PUT resolves, so there is no
  // separate list-refresh to reproduce here) ----
  toggleStatus(row: DishMasterRow, nextStatus: 'A' | 'I'): void {
    if (row.dishId == null) return;
    this.rows.update((list) => list.map((r) => (r.dishId === row.dishId ? { ...r, status: nextStatus } : r)));
    this.api.updateStatus({ dishId: row.dishId, status: nextStatus, updatedBy: this.userId }).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.notify.success(res.message ?? 'Dish Master Updated Successfully...!!');
        } else {
          this.notify.error(res.message ?? 'Something went wrong...!!');
        }
      },
      error: () => this.notify.error('Something went wrong...!!'),
    });
  }

  // ---- details modal (mirrors getNutritionalValues + showDishMasterDetails, ctrl:336-365) ----
  showDetails(row: DishMasterRow): void {
    if (row.dishId == null) return;
    this.api.getNutritionalValues(row.dishId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.detailsNutritionRows.set(res.listObject ?? []);
          this.detailsIngredientRows.set(res.listObject2 ?? []);
          this.showDetailsModal.set(true);
        }
      },
    });
  }

  closeDetailsModal(): void {
    this.showDetailsModal.set(false);
  }
}

function toggleSet(set: Set<number>, id: number, checked: boolean): Set<number> {
  const next = new Set(set);
  if (checked) next.add(id);
  else next.delete(id);
  return next;
}

function buildSelectedList<K extends string>(ids: Set<number>, key: K): Array<Record<K, number>> {
  return Array.from(ids).map((id) => ({ [key]: id }) as Record<K, number>);
}
