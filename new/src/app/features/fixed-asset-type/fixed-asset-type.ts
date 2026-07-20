import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { FixedAssetTypeService } from './fixed-asset-type.service';
import { FixedAssetTypeRow } from './fixed-asset-type.models';

type SortField = 'fixedAssetCode' | 'fixedAssetType';

/**
 * Parity rebuild of
 * views/master/UnitMaster/assetManagement/fixed-asset-type.html +
 * resources/assets/js/controller/master/unit/fixedAssetTypeController.js.
 * Self-registered state (controllerAs: 'vm', url `/fixedAssetType`), no
 * resolve/moduleId guard -- only the base session guard applies here (this
 * rebuild uses `SessionService`/routing-level guard, see below).
 *
 * Unlike `asset-category-master`, this legacy screen has NO pagination at
 * all (no `PaginationService`, no page-size control) -- just a bare
 * `ng-repeat ... | filter : search | orderBy:sortType:sortReverse` over the
 * FULL list. Reproduced as a single unpaginated table with client-side
 * search + sort over `fixedAssetTypes()`.
 *
 * **Deliberate deviations/preserved quirks, not bugs in this rebuild:**
 * - `PopulateFixedAssetType` (ctrl, uses the differently-named
 *   `FIXED_ASSET_TYPE_DU_API` constant) is defined but never wired to the
 *   view -- confirmed dead code, not ported. Only `getFixedAssetTypeForEdit`
 *   (the function the edit-pencil icon actually calls) is ported.
 * - `SaveFixedAssetType`/`ValidateFixedAssetType` are each declared twice
 *   in the legacy controller (var hoisting -- the second silently wins).
 *   No behavioral difference; nothing to reproduce.
 * - `updateFixedAssetType`'s PATCH body always sends `status: true`, even
 *   when editing an already-inactive row -- and the local list update on
 *   success only patches `fixedAssetCode`/`fixedAssetType`, never `status`.
 *   So editing+saving an inactive row silently reactivates it server-side
 *   while the row still LOOKS inactive in the table until the next full
 *   reload. Reproduced exactly: `update()` below sends `status: true` and
 *   patches only code/type locally, matching `updateFixedAssetType`
 *   verbatim (fixedAssetTypeController.js:173-202).
 * - `getFixedAssetTypeForEdit` does NOT reset the `isfixedAssetCodeValid`/
 *   `isfixedAssetTypeValid` error flags (unlike the dead `PopulateFixedAssetType`,
 *   which would have). Reproduced: `edit()` below leaves the invalid signals
 *   untouched.
 * - `ClearFields()` does not reset the invalid flags either -- reproduced
 *   verbatim in `clearFields()`.
 * - Create pushes an optimistic row into the in-memory list using
 *   `response.data.id` in legacy; this rebuild instead re-fetches the full
 *   list after a successful create (matching this project's own
 *   `asset-category-master.ts` convention for `create()`/`loadCategories()`,
 *   since the exact response envelope shape for the new id is not
 *   guessable from the controller alone) -- same end-visible result (the
 *   new row appears), no behavioral change a user could observe. Update
 *   still patches the row in place locally, matching legacy exactly (see
 *   above).
 * - The Save button (`<a data-toggle="modal" data-target="#confirmSave">`)
 *   has no disabled styling of any kind and no `ng-click` guard -- it always
 *   opens the confirm modal regardless of field state; validation only runs
 *   after "Ok". Reproduced: `openConfirmModal()` has no guard.
 * - Recurring missing-`$state`-DI bug (this controller's cookieObject guard
 *   calls `$state.go('login')` but `$state` is never injected -- would
 *   throw in legacy on session expiry): NOT reproduced, per this project's
 *   playbook. `SessionService`/`moduleAccessGuard` at the routing layer
 *   handle this safely instead.
 *
 * **1 real crash bug, reproduced faithfully (not fixed):**
 * - `ToggleStatus`'s error callback (fixedAssetTypeController.js:109-113)
 *   references `othercharge`, a variable never declared anywhere in this
 *   controller (should have been the function's own `fixedassettype`
 *   parameter) -- throws `ReferenceError: othercharge is not defined` on
 *   ANY failed PATCH toggle request, BEFORE the `growl.error(...)` line
 *   after it ever runs (so that error toast is unreachable dead code in
 *   legacy too). Reproduced via a direct `throw` in `toggleStatus()`'s
 *   error handler, same pattern as
 *   `linen-issue-acceptance-details.ts`'s `rejectionPopup()`.
 */
@Component({
  selector: 'app-fixed-asset-type',
  imports: [FormsModule, StatusToggle],
  templateUrl: './fixed-asset-type.html',
  styleUrl: './fixed-asset-type.scss',
})
export class FixedAssetType {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(FixedAssetTypeService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // ---- form state (mirrors self.model.*) ----
  fixedAssetTypeId = signal<number | undefined>(undefined);
  fixedAssetCode = signal('');
  fixedAssetType = signal('');
  isCodeInvalid = signal(false);
  isTypeInvalid = signal(false);

  showConfirmModal = signal(false);

  // ---- list + client-side search/sort (no pagination in legacy) ----
  fixedAssetTypes = signal<FixedAssetTypeRow[]>([]);
  search = signal('');
  sortField = signal<SortField | null>(null);
  sortReverse = signal(false);

  displayedRows = computed(() => {
    const keyword = this.search().trim().toLowerCase();
    let rows = this.fixedAssetTypes();
    if (keyword) {
      rows = rows.filter(
        (r) => r.fixedAssetCode?.toLowerCase().includes(keyword) || r.fixedAssetType?.toLowerCase().includes(keyword),
      );
    }
    const field = this.sortField();
    if (field) {
      const dir = this.sortReverse() ? -1 : 1;
      rows = [...rows].sort((a, b) => (a[field] > b[field] ? 1 : a[field] < b[field] ? -1 : 0) * dir);
    }
    return rows;
  });

  constructor() {
    this.loadFixedAssetTypes();
  }

  private loadFixedAssetTypes(): void {
    this.api.getFixedAssetTypes().subscribe({
      next: (rows) => this.fixedAssetTypes.set(rows ?? []),
      error: () => {
        // legacy: empty error handler, silently swallowed
      },
    });
  }

  sortBy(field: SortField): void {
    this.sortField.set(field);
    this.sortReverse.set(!this.sortReverse());
  }

  // ---- required-field validation (mirrors ValidateFixedAssetType) ----
  private validate(): boolean {
    let valid = true;
    if (!this.fixedAssetCode()) {
      this.isCodeInvalid.set(true);
      valid = false;
    }
    if (!this.fixedAssetType()) {
      this.isTypeInvalid.set(true);
      valid = false;
    }
    return valid;
  }

  openConfirmModal(): void {
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // ---- save (mirrors SaveFixedAssetType -> CreateFixedAssetType/updateFixedAssetType) ----
  save(): void {
    this.showConfirmModal.set(false);
    if (!this.validate()) return;

    if (this.fixedAssetTypeId() == null) {
      this.create();
    } else {
      this.update();
    }
  }

  private create(): void {
    this.api
      .create({
        fixedAssetCode: this.fixedAssetCode(),
        fixedAssetType: this.fixedAssetType(),
        status: true,
        unitId: this.unitId,
        organizationId: this.organizationId,
        userId: this.userId,
      })
      .subscribe({
        next: () => {
          this.loadFixedAssetTypes();
          this.clearFields();
          this.notify.success('Record saved successfully');
        },
        error: () => this.notify.error('Something went wrong'),
      });
  }

  private update(): void {
    const id = this.fixedAssetTypeId() as number;
    this.api
      .update(id, {
        fixedAssetCode: this.fixedAssetCode(),
        fixedAssetType: this.fixedAssetType(),
        status: true,
        unitId: this.unitId,
        organizationId: this.organizationId,
        userId: this.userId,
      })
      .subscribe({
        next: () => {
          // Legacy quirk preserved: only code/type are patched locally,
          // `status` is left untouched even though the request just sent
          // `status: true` to the server -- see class doc comment.
          this.fixedAssetTypes.update((rows) =>
            rows.map((r) => (r.id === id ? { ...r, fixedAssetCode: this.fixedAssetCode(), fixedAssetType: this.fixedAssetType() } : r)),
          );
          this.clearFields();
          this.notify.success('Record Updated successfully');
        },
        error: () => this.notify.error('Something went wrong'),
      });
  }

  clearFields(): void {
    this.fixedAssetCode.set('');
    this.fixedAssetType.set('');
    this.fixedAssetTypeId.set(undefined);
  }

  // ---- edit (mirrors getFixedAssetTypeForEdit) ----
  edit(id: number): void {
    this.api.getFixedAssetTypeForEdit(id).subscribe({
      next: (detail) => {
        this.fixedAssetTypeId.set(detail.id);
        this.fixedAssetCode.set(detail.fixedAssetCode);
        this.fixedAssetType.set(detail.fixedAssetType);
      },
    });
  }

  // ---- status toggle (mirrors ToggleStatus) ----
  toggleStatus(row: FixedAssetTypeRow): void {
    const nextStatus = !row.status;
    this.fixedAssetTypes.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    this.api.toggleStatus(row.id, nextStatus, this.unitId, this.organizationId, this.userId).subscribe({
      next: () => this.notify.success('Status changed successfully'),
      error: () => {
        // Legacy bug reproduced verbatim (fixedAssetTypeController.js:109-113):
        // the error callback references `othercharge`, a variable never
        // declared anywhere in this controller (should have been this
        // function's own `fixedassettype` parameter) -- throws
        // ReferenceError on ANY failed toggle PATCH, before the
        // `growl.error(...)` line after it is ever reached. Not fixed;
        // see class doc comment and linen-issue-acceptance-details.ts's
        // rejectionPopup() for the same reproduction pattern.
        throw new ReferenceError('othercharge is not defined');
      },
    });
  }
}
