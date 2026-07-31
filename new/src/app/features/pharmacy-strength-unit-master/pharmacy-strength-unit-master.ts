import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { HttpErrorResponse } from '@angular/common/http';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { PharmacyStrengthUnitMasterService } from './pharmacy-strength-unit-master.service';
import { StrengthUnitRecord, StrengthUnitRow, emptyStrengthUnit } from './pharmacy-strength-unit-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of views/PharmacyInventory/pharmacy-strength-unit-master.html
 * + resources/assets/js/controller/master/pharmacy/strengthUnitMasterController.js
 * (`controllerAs: 'vm'`). moduleId 468 (the controller's own self-registered
 * `.state()` resolve block, confirmed live).
 *
 * **This screen's backend contract is genuinely different from every other
 * legacy screen in this app, confirmed live (2026-07-31), not guessed:**
 * `GET /api/globals/strength/units/{orgId}` returns a raw JSON array (not
 * the usual `{status, listObject}` envelope); `POST`/`PATCH` return
 * `{id, message}`/`{message}` directly. Pagination and search are entirely
 * CLIENT-SIDE over the one full list fetched on load (`PaginationService`
 * in legacy) -- there is no server-side offset/count call on this screen
 * at all, unlike every other rebuilt screen in this codebase.
 *
 * **Live-verified, real create round trip (2026-07-31):** `POST
 * .../strength/units/1` with a real payload returned `201` and a real
 * `{id, message}`; confirmed the row existed via `psql`
 * (`m_strength_unit`), then deleted it to restore state before this
 * rebuild was written.
 *
 * **Deliberate quirks preserved, not bugs in this rebuild:**
 * - The Save button (view line 64) opens a Bootstrap `data-toggle="modal"`
 *   confirm dialog UNCONDITIONALLY -- its `ng-class="{disabled: ...}"` is a
 *   CSS-only visual state (AngularJS `ng-class` never sets the real
 *   `disabled` HTML attribute), so the modal opens even with empty fields.
 *   Validation (`ValidateStrengthUnit()`, ctrl:237-248) only happens INSIDE
 *   the modal's "Ok" handler (`SaveStrengthUnits()`, ctrl:37-45) -- if
 *   invalid, nothing is submitted, the modal still closes
 *   (`data-dismiss="modal"` fires unconditionally), and the user is left
 *   looking at inline "required" errors on the now-hidden form behind it.
 *   This is the OPPOSITE order from `nationality-master`/`tube-feeding-
 *   products`, where validation gates whether the modal even opens.
 *   Reproduced faithfully: `openConfirmModal()` opens unconditionally;
 *   `confirmSave()` validates and either proceeds or just sets the
 *   required-field flags, closing the modal either way.
 * - `SaveStrengthUnits()` (ctrl:37-45) branches Create vs. Update purely on
 *   `!self.model.id` -- there is no separate "edit mode" flag driving which
 *   button shows (unlike `nationality-master`'s `saveMode`). The rebuild
 *   keeps a `saveMode` signal anyway (set by `edit()`/`resetForm()`) purely
 *   to decide the SAME thing legacy's presence/absence of `self.model.id`
 *   decides -- functionally identical, not a behavioral change.
 * - The Edit icon is visually disabled or an INACTIVE row
 *   (`ng-class="{disabled: !unit.status}"`, view line 127) -- legacy does
 *   not prevent the click at the HTML/JS level (no real `disabled`
 *   attribute, `PopulateStrengthUnits` has no own guard), so this is a
 *   purely cosmetic-looking restriction that a determined click still
 *   bypasses in legacy. Reproduced as CSS-only here too, not a real guard.
 * - `ToggleStatus` (ctrl:96-121): the checkbox's own `ng-model="unit.status"`
 *   already flips the value optimistically before the handler even fires
 *   (confirmed via the view's binding, not assumed) -- so by the time the
 *   PATCH fires, the row already shows the NEW state. On a real error
 *   response with HTTP status 406 specifically, legacy force-sets
 *   `unit.status = true` REGARDLESS of which direction was being toggled
 *   (ctrl:112, not toggle-aware) -- reproduced verbatim via
 *   `StatusToggle`'s `toggled` event plus an explicit revert-to-true on a
 *   406 error, not a generic revert-to-previous.
 * - `ClearFields()` (ctrl:192-198) also resets `pagination.search` -- the
 *   Clear button clears BOTH the form and any active search filter in one
 *   click (view line 60: `ClearFields(); clearSearch();` -- belt-and-braces
 *   double-clear legacy itself does). Reproduced: `clearAll()` resets both.
 */
@Component({
  selector: 'app-pharmacy-strength-unit-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './pharmacy-strength-unit-master.html',
  styleUrl: './pharmacy-strength-unit-master.scss',
})
export class PharmacyStrengthUnitMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(PharmacyStrengthUnitMasterService);

  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  record = signal<StrengthUnitRecord>(emptyStrengthUnit());
  formSubmitted = signal(false);
  isCodeMissing = signal(false);
  isDescriptionMissing = signal(false);
  showConfirmModal = signal(false);
  saveMode = signal(true);

  allUnits = signal<StrengthUnitRow[]>([]);
  search = signal('');
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  sortColumn = signal<'code' | 'unit' | null>(null);
  sortReverse = signal(false);

  // Client-side filter, matching legacy's `searchStrength()` -- code OR
  // unit description substring match, case-insensitive.
  private readonly filtered = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    const rows = this.allUnits();
    if (!keyword) return rows;
    return rows.filter(
      (r) => r.code?.toLowerCase().includes(keyword) || r.unit?.toLowerCase().includes(keyword),
    );
  });

  private readonly sorted = computed(() => {
    const col = this.sortColumn();
    const rows = [...this.filtered()];
    if (!col) return rows;
    const reverse = this.sortReverse();
    rows.sort((a, b) => {
      const cmp = (a[col] ?? '').localeCompare(b[col] ?? '');
      return reverse ? -cmp : cmp;
    });
    return rows;
  });

  totalCount = computed(() => this.filtered().length);
  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  pagedRows = computed(() => {
    const offset = (this.currentPage() - 1) * this.pageSize();
    return this.sorted().slice(offset, offset + this.pageSize());
  });

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.api.getList(this.organizationId).subscribe({
      next: (rows) => {
        this.allUnits.set(rows ?? []);
        this.currentPage.set(1);
      },
    });
  }

  sortBy(column: 'code' | 'unit'): void {
    this.sortReverse.set(this.sortColumn() === column ? !this.sortReverse() : false);
    this.sortColumn.set(column);
  }

  onPageSizeChange(): void {
    this.currentPage.set(1);
  }

  goToPage(page: number): void {
    if (page < 1 || page > this.totalPages()) return;
    this.currentPage.set(page);
  }

  updateField<K extends keyof StrengthUnitRecord>(field: K, value: StrengthUnitRecord[K]): void {
    this.record.update((r) => ({ ...r, [field]: value }));
    if (field === 'code') this.isCodeMissing.set(false);
    if (field === 'units') this.isDescriptionMissing.set(false);
  }

  // Opens unconditionally -- see class comment. Validation happens in confirmSave().
  openConfirmModal(): void {
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  confirmSave(): void {
    this.showConfirmModal.set(false);
    const r = this.record();
    let valid = true;
    if (!r.code.trim()) {
      this.isCodeMissing.set(true);
      valid = false;
    }
    if (!r.units.trim()) {
      this.isDescriptionMissing.set(true);
      valid = false;
    }
    if (!valid) return;

    if (!r.id) {
      this.api
        .create({
          id: undefined,
          code: r.code,
          unit: r.units,
          organizationId: this.organizationId,
          unitId: this.unitId,
          userId: this.userId,
          status: true,
        })
        .subscribe({
          next: (res) => {
            this.notify.success(res.message);
            this.resetForm();
            this.loadList();
          },
          error: () => this.notify.error('Something went wrong'),
        });
    } else {
      this.api.update(this.organizationId, r.id, r.code, r.units).subscribe({
        next: (res) => {
          this.notify.success(res.message);
          this.resetForm();
          this.loadList();
        },
        error: () => this.notify.error('Something went wrong'),
      });
    }
  }

  private resetForm(): void {
    this.record.set(emptyStrengthUnit());
    this.formSubmitted.set(false);
    this.isCodeMissing.set(false);
    this.isDescriptionMissing.set(false);
    this.saveMode.set(true);
    this.search.set('');
  }

  // Mirrors ClearFields()+clearSearch() both firing on one click -- see class comment.
  clearAll(): void {
    this.resetForm();
  }

  edit(id: number): void {
    this.saveMode.set(false);
    this.api.getById(this.organizationId, id).subscribe({
      next: (row) => {
        this.record.set({ id: row.id, code: row.code, units: row.unit });
      },
    });
  }

  toggleStatus(row: StrengthUnitRow): void {
    const nextStatus = !row.status;
    this.allUnits.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    this.api.updateStatus(this.organizationId, row.id, nextStatus).subscribe({
      next: () => this.notify.success('Status changed successfully'),
      error: (err: HttpErrorResponse) => {
        if (err.status === 406) {
          this.notify.error(err.error?.message ?? 'Conflict');
          // ctrl:112 -- always reverts to true on a 406, not toggle-aware. See class comment.
          this.allUnits.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: true } : r)));
        } else {
          this.notify.error('Something Went Wrong');
        }
      },
    });
  }
}
