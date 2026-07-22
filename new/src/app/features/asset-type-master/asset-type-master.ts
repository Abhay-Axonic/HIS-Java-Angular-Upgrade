import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { AssetTypeMasterService } from './asset-type-master.service';
import { AssetTypeRow } from './asset-type-master.models';

/**
 * Parity rebuild of
 * views/master/GlobalMaster/AssetManagenent/Master/asset-type-master.html +
 * resources/assets/js/controller/master/AssetManagenent/assetTypeMasterController.js
 * (self-registered state `assetTypeMaster`, moduleId 641, no requireSuperUser
 * -- confirmed from that file's own `.config($stateProvider)` block, there is
 * no entry for this state anywhere in app.js).
 *
 * **SEVERITY-1 FINDING, not silently normalized -- see
 * docs/ANGULAR_PENDING_FINDINGS.md (batch 11 entry, placeholder "Entry N"):**
 * legacy's controller computes its API base as
 * `var apiUrl = constants.apiurl + constants.unitsApi;` using a bare, lowercase
 * global `constants` that is never defined ANYWHERE in the codebase (the
 * Angular constant is `CONSTANTS`, injected but never used in this file --
 * confirmed by grepping the entire `resources/assets/js` tree and
 * `index.html` for any `var constants =`/`window.constants =` assignment;
 * none exists outside an unrelated bundled math library). This throws
 * `ReferenceError: constants is not defined` synchronously, at controller
 * construction time, before `initializeController()` ever runs -- so
 * `GetAssetTypeList()` never fires. Confirmed independently via this
 * batch's golden-trace capture: `assetTypeMaster.json` shows ZERO
 * `/api/units/assets/types` calls, only the template fetch -- the exact
 * "anomalous call-count" signature entry 9 used to catch a similar crash.
 * The screen is a real, correctly-wired, real-backend screen (see
 * asset-type-master.service.ts) that is 100% non-functional in legacy
 * TODAY: blank list forever, and any click on Save/Popup throws
 * `Cannot read properties of undefined (reading 'Popup')` since
 * `self.model` itself was never assigned either.
 *
 * Per this project's standing practice for a straightforward broken-
 * reference crash (see entries 40/44/54 -- "FIX IMPLEMENTED, HELD FOR
 * REVIEW"), this rebuild calls the real, confirmed backend
 * (`AssetTypeController` -> `AssetTypeService` -> `AssetTypeDao`) rather
 * than porting a ReferenceError into TypeScript, which isn't meaningfully
 * possible anyway. This is a functional fix relative to legacy's current
 * behavior, not a silent one -- flagged here and in the findings doc for
 * fast-track owner consideration, the same as the other confirmed-live
 * crash bugs in this project's history.
 *
 * **Other legacy quirks preserved deliberately (not bugs in this rebuild):**
 * - The Save button is plain `ng-click="vm.model.Popup()"` with no
 *   CSS-disabled styling at all (unlike sibling `assetCategoryMaster`) --
 *   the confirm modal opens unconditionally regardless of field validity;
 *   `Validate()` only runs after "Ok" is clicked. Reproduced via `save()`
 *   validating only when actually invoked, `openConfirmModal()` never
 *   checking validity first.
 * - `ToggleStatus`'s error handler shows an error toast but performs NO
 *   revert of the optimistically-flipped checkbox at all (not even a
 *   partial revert like sibling `assetCategoryMaster`'s always-revert-to-
 *   true). Reproduced as-is: the toggle stays in the failed state.
 * - `CreateAssetType`'s success handler pushes the new row into the local
 *   list WITHOUT a `status` field (only `id`/`code`/`type`), even though
 *   the backend always creates it active (`status: true`) -- so a newly
 *   created row's status switch renders unchecked/off until the list is
 *   reloaded, even though the record is actually active server-side.
 *   Reproduced by leaving `status` unset on the locally-pushed row.
 * - The missing-`$state`-DI crash bug (same class as entries 8/10/104/106)
 *   isn't reproducible in this rebuild's architecture -- there is no
 *   `SessionService` call here at all, since this screen needs no cookie
 *   values beyond what create/update send; noted for the record only.
 */
@Component({
  selector: 'app-asset-type-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './asset-type-master.html',
  styleUrl: './asset-type-master.scss',
})
export class AssetTypeMaster {
  private readonly notify = inject(NotificationService);
  private readonly api = inject(AssetTypeMasterService);

  // ---- form state (mirrors self.model.*) ----
  assetTypeId = signal<number | undefined>(undefined);
  assetTypeCode = signal('');
  assetTypeName = signal('');
  isAssetTypeCodeValid = signal(false);
  isAssetTypeNameValid = signal(false);

  showConfirmModal = signal(false);

  // ---- list + client-side search/sort (legacy: bare `filter`/`orderBy`
  // over the full in-memory list -- no pagination on this screen at all) ----
  assetTypes = signal<AssetTypeRow[]>([]);
  search = signal('');
  predicate = signal<'code' | 'type' | null>(null);
  reverse = signal(false);

  constructor() {
    this.loadList();
  }

  private loadList(): void {
    this.api.getAll().subscribe({
      next: (rows) => this.assetTypes.set(rows ?? []),
      error: () => {
        // legacy: empty error handler, silently swallowed
      },
    });
  }

  sortBy(field: 'code' | 'type'): void {
    this.predicate.set(field);
    this.reverse.set(!this.reverse());
  }

  displayedRows(): AssetTypeRow[] {
    const keyword = this.search().trim().toLowerCase();
    let rows = this.assetTypes();
    if (keyword) {
      rows = rows.filter(
        (r) => r.code?.toLowerCase().includes(keyword) || r.type?.toLowerCase().includes(keyword),
      );
    }
    const field = this.predicate();
    if (field) {
      const dir = this.reverse() ? -1 : 1;
      rows = [...rows].sort((a, b) => (a[field] > b[field] ? 1 : a[field] < b[field] ? -1 : 0) * dir);
    }
    return rows;
  }

  // ---- edit (mirrors GetAssetType) ----
  edit(id: number): void {
    this.api.getById(id).subscribe({
      next: (detail) => {
        this.assetTypeId.set(detail.id);
        this.assetTypeCode.set(detail.code);
        this.assetTypeName.set(detail.type);
      },
    });
  }

  // ---- Save button -> confirm modal (mirrors Popup(), no upfront validation) ----
  openConfirmModal(): void {
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // ---- Ok inside confirm modal (mirrors SaveAssetType -> Validate) ----
  save(): void {
    this.showConfirmModal.set(false);
    if (!this.validate()) return;

    const id = this.assetTypeId();
    if (id == null) {
      this.create();
    } else {
      this.update(id);
    }
  }

  private validate(): boolean {
    let valid = true;
    if (!this.assetTypeCode()) {
      this.isAssetTypeCodeValid.set(true);
      valid = false;
    }
    if (!this.assetTypeName()) {
      this.isAssetTypeNameValid.set(true);
      valid = false;
    }
    return valid;
  }

  private create(): void {
    this.api
      .create({
        code: this.assetTypeCode(),
        type: this.assetTypeName(),
        status: true,
        unitId: 0,
        organizationId: 0,
        userId: 0,
      })
      .subscribe({
        next: (res) => {
          const created = res.object as { id: number } | undefined;
          // Legacy quirk preserved: no `status` on the locally-pushed row
          // (see class comment) -- reload would show it as active.
          this.assetTypes.update((rows) => [
            ...rows,
            { id: created?.id ?? 0, code: this.assetTypeCode(), type: this.assetTypeName() },
          ]);
          this.clearFields();
          this.notify.success(res.message ?? 'Asset Type Inserted Successfully');
        },
        error: () => this.notify.error('Something went wrong'),
      });
  }

  private update(id: number): void {
    const code = this.assetTypeCode();
    const type = this.assetTypeName();
    this.api.update(id, { code, type }).subscribe({
      next: (res) => {
        this.assetTypes.update((rows) => rows.map((r) => (r.id === id ? { ...r, code, type } : r)));
        this.assetTypeId.set(undefined);
        this.clearFields();
        this.notify.success(res.message ?? 'Asset Type Updated Successfully');
      },
      error: () => this.notify.error('Something went wrong'),
    });
  }

  clearFields(): void {
    this.assetTypeCode.set('');
    this.assetTypeName.set('');
    this.isAssetTypeCodeValid.set(false);
    this.isAssetTypeNameValid.set(false);
  }

  // ---- status toggle (mirrors ToggleStatus -- no revert on failure at all,
  // see class comment) ----
  toggleStatus(row: AssetTypeRow): void {
    const nextStatus = !row.status;
    this.assetTypes.update((rows) => rows.map((r) => (r.id === row.id ? { ...r, status: nextStatus } : r)));
    this.api.toggleStatus(row.id, nextStatus).subscribe({
      next: () => this.notify.success('Status changed successfully'),
      error: () => this.notify.error('Something went wrong'),
    });
  }
}
