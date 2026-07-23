import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { LoaderService } from '../../core/loader.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { PharmacyFormulationTypeMasterService } from './pharmacy-formulation-type-master.service';
import { FormulationTypeForm, FormulationTypeRow, emptyFormulationTypeForm } from './pharmacy-formulation-type-master.models';
import {
  PaginationState,
  createPaginationState,
  searchData,
  setFirstPage,
  setLastPage,
  setNextPage,
  setPage,
  setPreviousPage,
  setTotalPages,
  getPagesList,
} from '../../core/client-pagination.util';

const CODE_PATTERN = /^[a-z0-9]*[-]?[a-z0-9]*$/i;

/**
 * Parity rebuild of
 * views/master/GlobalMaster/pharmacy-formulation-type-master.html +
 * resources/assets/js/controller/master/pharmacy/formulationTypeMasterController.js
 * (`FormulationTypeMaster.Controller`, `controllerAs: "vm"`). State
 * `pharmacyformulationTypeMaster` self-registered via the controller file's
 * own `.config($stateProvider)` block (not app.js's central router),
 * moduleId 459 -- confirmed real/active, gated entirely by the route guard,
 * no code needed in this component for that.
 *
 * **Disambiguation from two similarly-named sibling screens (confirmed
 * distinct, NOT referenced or touched here):**
 * - `formulationTypeROAMaster` -- maps a Formulation Type to Route of
 *   Administration. A different screen entirely.
 * - `formulationTypeToDosageTypeMapperController` -- maps a Formulation
 *   Type to a Dosage Form, and uses a DIFFERENT status vocabulary
 *   (`'A'`/`'I'` strings) than this screen's real `Boolean`.
 * This component represents the Formulation Type MASTER entity itself
 * (e.g. Tablet, Syrup, Injection) -- just code + name + status, nothing
 * else.
 *
 * **Confirmed real, working CRUD end-to-end** against
 * `/api/globals/formulationTypes` (base `globalsApi + '/formulationTypes/'`,
 * `constants.js:130`): list (ctrl:38-54), get-by-id for edit (ctrl:57-71),
 * status toggle (ctrl:74-98), create (ctrl:147-181), update (ctrl:184-210).
 * List/by-id responses are raw JSON (`response.data` IS the array/object),
 * not the `{status, listObject}` envelope -- see .models.ts header comment.
 *
 * **"Clinical Unit" is dead, decorative-only UI -- dropped, not wired to
 * anything real.** Legacy's own form input (view:37-50) and table column
 * (view:130-139, always an empty `<td>`) have NO `ng-model` anywhere in the
 * source -- confirmed via direct read, not assumed. This rebuild renders a
 * disabled placeholder input for visual parity with the live screenshot and
 * an always-blank table cell, but neither is a real, saved, editable field.
 *
 * **Save icon has no real `disabled` gate (cosmetic `ng-class` only,
 * view:89-91)** -- `data-toggle="modal"` fires unconditionally, so clicking
 * it always opens the confirm-save modal even with an empty form. Only the
 * modal's own "Ok" button (`SaveFormulationType()`) runs real validation
 * (`validateFormulationMaster()`, required-fields, then `ValidateCode()`/
 * `ValidateCodeForUpdate()` inside create/update -- duplicate-code check
 * against the already-loaded list, plus the `/^[a-z0-9]*[-]?[a-z0-9]*$/i`
 * regex). Reproduced identically: `openSaveConfirm()` always opens the
 * modal; `confirmSave()` is the only place validation actually runs.
 *
 * **Search is a genuine full-list client-side filter** (`searchFormulation`,
 * ctrl:229-246, called with the full `formulationTypeDataList` every time,
 * not just the current page) -- unlike some sibling screens' "current-page-
 * only search" quirk, this one really does search every row. Reproduced via
 * `client-pagination.util.ts`'s `searchData()` against the full list, an
 * intentional, disclosed structural improvement (shared pagination helper
 * instead of legacy's locally-reimplemented copy) without changing
 * observable behavior -- legacy reimplements `PaginationService` verbatim
 * inline in this controller (ctrl:229-259) rather than actually injecting
 * the service, but the logic is byte-for-byte the same algorithm.
 *
 * **Column-sort carets are real, working functionality** (`sortBy`,
 * paginationService.js:76-79, `$filter('orderBy')` + reset to page 1) --
 * reproduced via `sortBy()` below. Faithful copy-paste quirk preserved: the
 * third header's caret (view:130-139, labelled "Clinical Unit") is wired to
 * sort by `'type'`, the SAME field as the second (Formulation Type) header
 * -- there is no real per-unit sort, clicking either caret produces an
 * identical reorder. Also faithful: `sortReverse` is one shared flag toggled
 * on every click regardless of which column was clicked last (ctrl/view use
 * a single bare `sortReverse` variable, not a per-column one).
 *
 * **BUG 1, faithfully reproduced (edit-save always claims `status: true`):**
 * `updateFormulationType()`'s PATCH body (ctrl:184-210) hardcodes
 * `"status": true` unconditionally -- it never reads the record's actual
 * current status. Harmless in practice only because Edit is disabled for
 * inactive rows (see below), so every editable record is already active.
 * Reproduced verbatim in `updateFormulationType()` below: the update payload
 * always sends `status: true`, never the real current value.
 *
 * **BUG 2, faithfully reproduced (toggle-status error swallows the real
 * "in use" reason and leaves the switch visually wrong):** the backend has a
 * real server-side guard that refuses to deactivate a Formulation Type still
 * referenced by Item Master or Formulation-ROA records -- but it answers
 * with a generic error, not the specific "assigned to Items/ROA"-style
 * message the frontend's `err.status == 406` branch is written to expect
 * (ctrl:88-95). In practice the user only ever sees a generic error toast
 * (the `else` branch, `growl.error('Something Went Wrong')`), and because
 * the checkbox's `ng-model` already flipped optimistically before the PATCH
 * even resolved, the switch is left showing the NEW (wrong) state -- out of
 * sync with the DB, which never actually changed -- until the next list
 * reload. Reproduced below: `onToggleStatus()` optimistically flips the row
 * first, and on any error shows a generic toast and does NOT revert the
 * visual state -- no special "in use" detection, no snap-back.
 */
@Component({
  selector: 'app-pharmacy-formulation-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './pharmacy-formulation-type-master.html',
  styleUrl: './pharmacy-formulation-type-master.scss',
})
export class PharmacyFormulationTypeMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly loader = inject(LoaderService);
  private readonly api = inject(PharmacyFormulationTypeMasterService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- list + client-side search/pagination (PaginationService port) ----
  private fullList: FormulationTypeRow[] = [];
  pagination = signal<PaginationState<FormulationTypeRow>>(createPaginationState<FormulationTypeRow>());
  private sortReverse = false;

  // ---- top-of-page create/edit form ----
  form = signal<FormulationTypeForm>(emptyFormulationTypeForm());
  codeInvalid = signal(false);
  typeInvalid = signal(false);
  showConfirmModal = signal(false);

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.loader.start();
    this.api.getAll(this.organizationId).subscribe({
      next: (rows) => {
        this.loader.stop();
        this.fullList = rows ?? [];
        const p = this.pagination();
        if (this.fullList.length > 0) {
          searchData(this.fullList, p);
        } else {
          p.data = [];
          p.filteredData = [];
        }
        this.pagination.set({ ...p });
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- search / pagination (mirrors searchFormulation/setTotalPages, ctrl:229-259) ----
  onSearchChange(term: string): void {
    const p = this.pagination();
    p.search = term;
    searchData(this.fullList, p);
    this.pagination.set({ ...p });
  }

  onPageSizeChange(): void {
    const p = this.pagination();
    setTotalPages(p);
    this.pagination.set({ ...p });
  }

  page(action: 'first' | 'prev' | 'next' | 'last' | number): void {
    const p = this.pagination();
    if (action === 'first') setFirstPage(p);
    else if (action === 'prev') setPreviousPage(p);
    else if (action === 'next') setNextPage(p);
    else if (action === 'last') setLastPage(p);
    else setPage(action, p);
    this.pagination.set({ ...p });
  }

  pagesList(): number[] {
    return getPagesList(this.pagination());
  }

  /** Mirrors `sortBy` (paginationService.js:76-79) via the two real header
   * carets -- see class doc comment for the faithfully-reproduced
   * third-header/"Clinical Unit" copy-paste quirk (also routed here as
   * `'type'`) and the single shared `sortReverse` flag. */
  sortBy(field: 'code' | 'type'): void {
    this.sortReverse = !this.sortReverse;
    const reverse = this.sortReverse;
    const p = this.pagination();
    p.filteredData = [...p.filteredData].sort((a, b) => {
      const av = a[field];
      const bv = b[field];
      const cmp = av < bv ? -1 : av > bv ? 1 : 0;
      return reverse ? -cmp : cmp;
    });
    setTotalPages(p);
    this.pagination.set({ ...p });
  }

  // ---- form field updates ----
  updateCode(code: string): void {
    this.codeInvalid.set(false);
    this.form.update((f) => ({ ...f, code }));
  }

  updateType(type: string): void {
    this.typeInvalid.set(false);
    this.form.update((f) => ({ ...f, type }));
  }

  // ---- Clear button (`ClearFields(); clearSearch();`, ctrl:96/213-226) ----
  clearAll(): void {
    this.form.set(emptyFormulationTypeForm());
    this.codeInvalid.set(false);
    this.typeInvalid.set(false);
    const p = this.pagination();
    p.search = '';
    searchData(this.fullList, p);
    this.pagination.set({ ...p });
  }

  // ---- Edit (populateFormulationType, ctrl:57-71) -- pencil only enabled
  // while the row is active; guarded both in the template (`[class.disabled]`)
  // and here, matching legacy's `ng-class="{disabled: !formulationType.status}"`. ----
  edit(row: FormulationTypeRow): void {
    if (!row.status) return;
    this.codeInvalid.set(false);
    this.typeInvalid.set(false);
    this.api.getById(this.organizationId, row.id).subscribe({
      next: (detail) => {
        this.form.set({ id: detail.id, code: detail.code, type: detail.type });
      },
    });
  }

  // ---- Save icon -> confirm modal (cosmetically "disabled" only, always
  // opens -- see class doc comment). ----
  openSaveConfirm(): void {
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  /** Mirrors `SaveFormulationType()` (ctrl:135-144) -- required-fields check
   * first (`validateFormulationMaster`), then branch on whether an id is
   * set. Only ever called from the confirm-save modal's Ok button. */
  confirmSave(): void {
    this.showConfirmModal.set(false);
    if (!this.validateRequiredFields()) return;
    const f = this.form();
    if (!f.id) {
      this.createFormulationType(f);
    } else {
      this.updateFormulationType(f);
    }
  }

  /** validateFormulationMaster(), ctrl:261-272. */
  private validateRequiredFields(): boolean {
    const f = this.form();
    let valid = true;
    if (!f.code) {
      valid = false;
      this.codeInvalid.set(true);
    }
    if (!f.type) {
      valid = false;
      this.typeInvalid.set(true);
    }
    return valid;
  }

  private duplicateCode(code: string, excludeId: number | null): boolean {
    return this.fullList.some((row) => row.id !== excludeId && row.code === code);
  }

  /** createFormulationType(), ctrl:147-181, gated by ValidateCode()
   * (duplicate-code check across the WHOLE list + the alphanumeric/single-
   * hyphen regex). */
  private createFormulationType(f: FormulationTypeForm): void {
    if (this.duplicateCode(f.code, null)) {
      this.notify.error('Duplicate formulation type code');
      return;
    }
    if (!CODE_PATTERN.test(f.code)) {
      this.notify.error('Code can only be alpha numeric with single hypen.');
      return;
    }
    this.loader.start();
    this.api
      .create({
        code: f.code,
        type: f.type,
        status: true,
        unitId: this.unitId,
        organizationId: this.organizationId,
        userId: this.userId,
      })
      .subscribe({
        next: () => {
          this.loader.stop();
          this.notify.success('Formulation Type Inserted Successfully');
          this.clearAll();
          this.loadList();
        },
        error: () => this.loader.stop(),
      });
  }

  /** updateFormulationType(), ctrl:184-210, gated by ValidateCodeForUpdate()
   * (duplicate-code check EXCLUDING the row being edited + the same regex).
   * BUG 1 faithfully reproduced here: the PATCH body always sends
   * `status: true`, never the record's real current status -- see class
   * doc comment. */
  private updateFormulationType(f: FormulationTypeForm): void {
    if (this.duplicateCode(f.code, f.id)) {
      this.notify.error('Duplicate formulation type code');
      return;
    }
    if (!CODE_PATTERN.test(f.code)) {
      this.notify.error('Code can only be alpha numeric with single hypen.');
      return;
    }
    this.loader.start();
    this.api
      .update(f.id as number, {
        type: f.type,
        code: f.code,
        status: true, // always true -- BUG 1, see class doc comment
        organizationId: this.organizationId,
      })
      .subscribe({
        next: () => {
          this.loader.stop();
          this.notify.success('Formulation Type Updated Successfully');
          this.clearAll();
          this.loadList();
        },
        error: () => this.loader.stop(),
      });
  }

  // ---- status toggle (ToggleStatus, ctrl:74-98) ----
  /** BUG 2 faithfully reproduced here -- see class doc comment: the row is
   * optimistically flipped BEFORE the PATCH resolves (matching legacy's
   * two-way-bound checkbox), deactivating also unconditionally clears the
   * top form (`if (formulationType.status == false) { ClearFields(); }`),
   * and on ANY error this shows a generic toast and deliberately does NOT
   * revert the optimistic flip -- no "in use"-specific detection, no
   * snap-back, matching the real observed behavior (the backend's actual
   * "in use" refusal never carries the specific message legacy's dead
   * `err.status == 406` branch expects, so only this generic path is ever
   * hit in practice). */
  onToggleStatus(row: FormulationTypeRow): void {
    const nextStatus = !row.status;
    this.fullList = this.fullList.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r));
    const p = this.pagination();
    p.data = p.data.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r));
    p.filteredData = p.filteredData.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r));
    this.pagination.set({ ...p });
    if (!nextStatus) {
      this.clearAll();
    }
    this.loader.start();
    this.api.updateStatus(row.id, { status: nextStatus, organizationId: this.organizationId }).subscribe({
      next: () => {
        this.loader.stop();
        this.notify.success('Status changed successfully');
      },
      error: () => {
        this.loader.stop();
        this.notify.error('Something Went Wrong'); // no revert -- see class doc comment, BUG 2
      },
    });
  }
}
