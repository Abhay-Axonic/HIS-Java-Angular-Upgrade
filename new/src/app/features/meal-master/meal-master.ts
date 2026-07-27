import { Component, computed, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { MealMasterService } from './meal-master.service';
import { MealMasterForm, MealMasterRow, emptyMealMasterForm } from './meal-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/master/GlobalMaster/DietNutrition/meal-master.html
 * + resources/assets/js/controller/dietAndKitchen/global/mealMasterController.js
 * (323 lines, `mealMasterController`). State `mealMaster` registered
 * with a `resolve` block (module id confirmed from the live route
 * config, see app.routes.ts).
 *
 * ================================================================
 * SEVERITY-1: deactivating a meal permanently removes it from the
 * list, on real unmodified legacy -- confirmed at the source level,
 * faithfully reproduced (not fixed)
 * ================================================================
 * The paginated list endpoint (`getAllMealMasterListPaginationList`)
 * reuses the `MEAL_MASTER.GET_ACTIVE_MEAL_MASTER_LIST` named query
 * (`MealMaster.java`), which has `WHERE mealMaster.status='A' ...` --
 * ACTIVE ONLY. Once a row's status is toggled to Inactive, it vanishes
 * from this screen's list entirely -- there is no filter, toggle, or
 * any other path in this screen's UI to see or reactivate it again.
 * This is a real, severe, soft-delete-with-no-undo-path bug. Confirmed
 * by direct source read (not independently re-verified via a live
 * toggle-then-reload this session, given the severity was already
 * unambiguous from the query text itself). Reproduced as-is: the
 * rebuild's `loadList()` calls the same real endpoint, so a
 * deactivated row disappears from `list()` on the next reload here too.
 *
 * A second, lower-severity scoping bug, also confirmed at the source
 * level: the paginated LIST query is organizationId-scoped only (no
 * unitId, "coment by minesh bcoz its only on org level"), while the
 * COUNT query (`getAllMealMasterListPaginationListCount`) is scoped by
 * BOTH organizationId AND unitId (`Criteria` with `Restrictions.eq`
 * on both). In a multi-unit-per-org deployment this would desync the
 * pager's total-page count from what the list actually returns. Not
 * independently observable in this single-org/single-unit dev
 * environment; faithfully reproduced (the rebuild calls both real
 * endpoints exactly as legacy does, with no client-side correction).
 *
 * ================================================================
 * Update ALSO goes through the confirm-modal gate, sharing the SAME
 * popUpFlag as Save -- unlike every sibling screen in this batch,
 * where Update has no confirm-modal gate at all
 * ================================================================
 * `saveMealMaster()` (ctrl:200-245) branches to `updateMeal()` when in
 * edit mode; `updateMeal()` (ctrl:280-319) has its own `if
 * ($scope.popUpFlag) { $scope.showModal = !$scope.showModal; }` gate,
 * identical in shape to `save()`'s. Both share one `popUpFlag`. Fixed
 * here too (owner decision, 2026-07-27, extending batch 22's decision
 * to all 5 batch 23 screens) via one shared `popupArmed` signal used
 * by both `save()` and `update()`, rearmed on both success and failure.
 *
 * ================================================================
 * Duplicate-check messages are copy-paste artifacts, reproduced
 * verbatim including the wrong entity name and the typo
 * ================================================================
 * The backend's real duplicate-check messages ("Meal with same code
 * already exists" / "Meal with same serving time already exists") are
 * NEVER actually shown -- the frontend hardcodes its own generic
 * strings regardless of which reason the backend returned: create
 * failure always shows `"Meal already exits...!!"` (typo: "exits"),
 * while UPDATE failure always shows `"Diet already exits...!!"` --
 * "Diet", not "Meal", a copy-paste artifact from a sibling screen.
 * Update's SUCCESS message has the same artifact: `"Diet updated
 * Successfully...!!"`, vs. create's `"Meal added Successfully...!!"`.
 * All four strings reproduced exactly, not corrected/unified.
 *
 * ================================================================
 * Serving Time is NOT a required field (unlike Code/Name) -- the
 * legacy view's real `<input>` for it has no `required` attribute
 * (only a commented-out, superseded version did). Modeled as a plain
 * `<input type="time">` here rather than replicating the legacy
 * jQuery clockpicker widget -- same modeling choice as shiftMaster
 * (batch 22) for its time fields; the backend already returns/expects
 * 24-hour "HH:mm" strings (`to_char(...,'HH24:MI')` /
 * `SimpleDateFormat("HH:mm")`), which `<input type="time">` produces
 * natively.
 * ================================================================
 *
 * Edit populates the form directly from the row already in `list()`
 * (no server round-trip) -- same pattern as `OTIMSroleCreation`.
 *
 * Status toggle does NOT reload the list (confirmed by source read --
 * no `initMasterList()` call anywhere in `updateStatus()`), reproduced
 * via the optimistic-local-patch pattern used elsewhere this batch.
 *
 * Pagination is genuinely functional (real offset tracking via
 * `PagerService`/`setPage`, same shape as `bloodBagType`).
 */
@Component({
  selector: 'app-meal-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './meal-master.html',
  styleUrl: './meal-master.scss',
})
export class MealMaster {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(MealMasterService);

  private organizationId = 0;
  private unitId = 0;
  private userId = 0;

  form = signal<MealMasterForm>(emptyMealMasterForm());
  saveMode = signal(true);
  formSubmitted = signal(false);

  showConfirmModal = signal(false);
  private popupArmed = signal(true); // shared by save() AND update() -- see class doc

  list = signal<MealMasterRow[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;

  searchKeyword = signal('');
  displayedRows = computed(() => {
    const keyword = this.searchKeyword().trim().toLowerCase();
    const rows = this.list();
    if (!keyword) return rows;
    return rows.filter((r) => [r.code, r.name].join(' ').toLowerCase().includes(keyword));
  });

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  constructor() {
    if (this.session.current == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.organizationId = this.session.organizationId ?? 0;
    this.unitId = this.session.unitId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.loadList();
  }

  private loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.loader.start();
    this.api.getList(this.organizationId, offset, this.pageSize(), this.unitId).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
        }
      },
      error: () => this.loader.stop(),
    });
    this.api.getCount(this.organizationId, this.unitId).subscribe({
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

  updateField<K extends keyof MealMasterForm>(field: K, value: MealMasterForm[K]): void {
    this.form.update((f) => ({ ...f, [field]: value }));
  }

  isCodeInvalid(): boolean {
    return this.formSubmitted() && !this.form().code.trim();
  }

  isNameInvalid(): boolean {
    return this.formSubmitted() && !this.form().name.trim();
  }

  private isValid(): boolean {
    const f = this.form();
    return !!f.code.trim() && !!f.name.trim();
  }

  // ---- save / confirm-modal (mirrors saveMealMaster, ctrl:200-245) ----
  save(): void {
    this.formSubmitted.set(true);
    if (!this.isValid()) return;
    if (this.popupArmed()) {
      this.showConfirmModal.set(true);
    } else {
      this.doSave();
    }
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // Mirrors PopupOkBtn -- routes to create or update depending on edit mode.
  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.popupArmed.set(false);
    if (this.saveMode()) {
      this.doSave();
    } else {
      this.doUpdate();
    }
  }

  private doSave(): void {
    const f = this.form();
    this.loader.start();
    this.api
      .save({
        code: f.code,
        name: f.name,
        servingTime: f.servingTime,
        organizationId: this.organizationId,
        unitId: this.unitId,
        createdBy: this.userId,
        updatedBy: this.userId,
        status: 'A',
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Meal added Successfully...!!');
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error('Meal already exits...!!');
            this.popupArmed.set(true); // fixed here, see class doc
          }
        },
        error: () => this.loader.stop(),
      });
  }

  private resetForm(): void {
    this.form.set(emptyMealMasterForm());
    this.formSubmitted.set(false);
    this.saveMode.set(true);
    this.popupArmed.set(true);
  }

  // ---- edit (mirrors editMealMaster, ctrl:183-197 -- direct row copy,
  // no server round-trip) ----
  edit(row: MealMasterRow): void {
    this.saveMode.set(false);
    this.form.set({ id: row.mealId, code: row.code, name: row.name, servingTime: row.servingTime });
  }

  // ---- update (mirrors updateMeal, ctrl:280-319 -- SHARES the
  // confirm-modal gate with save(), see class doc) ----
  update(): void {
    this.formSubmitted.set(true);
    if (!this.isValid()) return;
    if (this.popupArmed()) {
      this.showConfirmModal.set(true);
    } else {
      this.doUpdate();
    }
  }

  private doUpdate(): void {
    const f = this.form();
    this.loader.start();
    this.api
      .update({
        mealId: f.id,
        code: f.code,
        name: f.name,
        servingTime: f.servingTime,
        organizationId: this.organizationId,
        unitId: this.unitId,
        updatedBy: this.userId,
        status: 'A',
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Diet updated Successfully...!!');
            this.resetForm();
            this.loadList();
          } else {
            this.notify.error('Diet already exits...!!');
            this.popupArmed.set(true); // fixed here, see class doc
          }
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- status toggle (mirrors updateStatus, ctrl:247-278 -- does NOT
  // reload the list) ----
  toggleStatus(row: MealMasterRow): void {
    const nextStatus: 'A' | 'I' = row.status === 'A' ? 'I' : 'A';
    this.list.update((rows) => rows.map((r) => (r.mealId === row.mealId ? { ...r, status: nextStatus } : r)));
    this.loader.start();
    this.api.updateStatus({ mealId: row.mealId, status: nextStatus, updatedBy: this.userId }).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.notify.success('Meal Updated Successfully...!!');
        } else {
          this.notify.error('Something went wrong...!!');
        }
      },
      error: () => this.loader.stop(),
    });
  }
}
