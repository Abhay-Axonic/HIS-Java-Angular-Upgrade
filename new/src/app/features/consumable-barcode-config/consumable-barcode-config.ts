import { Component, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ConsumableBarcodeConfigService } from './consumable-barcode-config.service';

/**
 * Parity rebuild of
 * views/master/UnitMaster/assetManagement/consumable-barcode-configuration.html +
 * resources/assets/js/controller/master/unit/consumableBarcodeConfigController.js
 * (self-registered state `consumableBarcodeConfig`, moduleId 636, no
 * requireSuperUser). `app.js:10885` has a DEAD, commented-out duplicate of
 * this same state name pointing at the generic `MasterController` -- the
 * LIVE state is the self-registered one in this controller's own file,
 * confirmed via `grep` across the whole webapp tree (only one live
 * `$stateProvider.state("consumableBarcodeConfig", ...)` registration
 * exists). This is the mirror image of the `addItemPOCreation` pattern
 * (entry 2): here the app.js duplicate is the dead one, not the self-
 * registered one -- verified, not assumed.
 *
 * **Findings preserved deliberately (see
 * docs/ANGULAR_PENDING_FINDINGS.md batch 11 entry, placeholder "Entry N"):**
 * - `GetConsumableBarcode`'s success handler iterates every row the
 *   `GET .../consumableBarcodeConfig/{unitId}` call returns (ordered by id
 *   DESC, no `LIMIT` in `ConsumableBarcodeConfigDao.getAll`) and
 *   unconditionally overwrites `$scope.id` on every iteration -- so if more
 *   than one row ever exists for a unit, the LAST-iterated (lowest id,
 *   i.e. OLDEST) row's id wins, while the 11 boolean flags get OR-merged
 *   across every row (a flag true in ANY row stays true, since the
 *   `if (barcode.x == true)` checks never explicitly reset a flag to
 *   false). Reproduced exactly via the same forEach-overwrite loop below.
 * - `SaveConsumableBarcodeConfig` only ever calls the update path when
 *   `$scope.id` is already set from that initial load -- `CreateConsumableConfig`'s
 *   own success handler never sets `$scope.id` from the newly-created row's
 *   id (only `self.model.consumableBarcodeConfigId`, a field nothing else
 *   reads). This means: on a unit with NO existing config row, clicking
 *   Save repeatedly (without a page reload) creates a NEW duplicate row on
 *   the backend every single time, rather than updating the first one --
 *   a real, confirmed data-integrity bug (the backend has no unique
 *   constraint per unit either). Reproduced exactly: `recordId` is only
 *   ever populated by `loadConfig()`, never by `create()`'s own response.
 * - Every checkbox's `ngModel` was initialized in legacy to the STRING
 *   `"false"` (not the boolean `false`) until first toggled or until a
 *   real row loads -- a harmless quirk (a truthy JS string that Jackson's
 *   Boolean deserializer still coerces correctly if ever POSTed unchanged)
 *   not reproduced literally here, since a strongly-typed `boolean` signal
 *   is otherwise equivalent in every observable way; noted for the record.
 * - The **Vendor Name** checkbox's `ng-change` in legacy's view calls
 *   `vm.model.isVendorNameFlag(vm.model.isVendorName)` -- but the
 *   controller never defines any `isVendorNameFlag` function (it defines
 *   `isItemNameFlag`, `isItemCodeFlag`, ... `isVendorCodeFlag`,
 *   `isHopspitalNameFlag`, but for Vendor Name only a same-named `isVendorName`
 *   function that gets assigned to `self.model.isVendorName` and then
 *   IMMEDIATELY overwritten by the literal string `"false"` a few lines
 *   later -- so it's unreachable via `vm.model` either way). Clicking the
 *   Vendor Name toggle in legacy throws
 *   `TypeError: vm.model.isVendorNameFlag is not a function`, every time,
 *   confirmed by reading every function definition in the file. Per this
 *   project's standing practice for a straightforward broken-reference
 *   crash (entries 40/44/54 -- "FIX IMPLEMENTED, HELD FOR REVIEW"), this
 *   rebuild wires Vendor Name identically to its 10 working siblings
 *   rather than porting a guaranteed TypeError into TypeScript. Flagged
 *   here and in the findings doc for fast-track owner consideration.
 * - The missing-`$state`-DI crash bug (same class as entries 8/10/104/106)
 *   isn't reproducible in this rebuild's architecture -- `SessionService`
 *   never throws; noted for the record only.
 */
@Component({
  selector: 'app-consumable-barcode-config',
  imports: [FormsModule, StatusToggle],
  templateUrl: './consumable-barcode-config.html',
  styleUrl: './consumable-barcode-config.scss',
})
export class ConsumableBarcodeConfig {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ConsumableBarcodeConfigService);

  private readonly unitId = this.session.unitId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly userId = this.session.userId ?? 0;

  // Only ever set from a loaded row's id -- see class comment (create's
  // own response never sets this, faithfully reproducing legacy's
  // duplicate-row-on-repeated-save bug).
  private recordId: number | undefined;

  itemName = signal(false);
  itemCode = signal(false);
  batchNo = signal(false);
  expiryDate = signal(false);
  lotNo = signal(false);
  mrp = signal(false);
  salePrice = signal(false);
  grnNo = signal(false);
  vendorName = signal(false);
  vendorCode = signal(false);
  hospitalName = signal(false);

  showConfirmModal = signal(false);

  constructor() {
    this.loadConfig();
  }

  private loadConfig(): void {
    this.api.getByUnit(this.unitId).subscribe({
      next: (rows) => {
        // Legacy quirk preserved: overwrite id on every iteration, OR-merge
        // flags across every returned row (see class comment).
        for (const row of rows ?? []) {
          this.recordId = row.id;
          if (row.itemName) this.itemName.set(true);
          if (row.itemCode) this.itemCode.set(true);
          if (row.batchNo) this.batchNo.set(true);
          if (row.expiryDate) this.expiryDate.set(true);
          if (row.lotNo) this.lotNo.set(true);
          if (row.mrp) this.mrp.set(true);
          if (row.salePrice) this.salePrice.set(true);
          if (row.grnNo) this.grnNo.set(true);
          if (row.vendorName) this.vendorName.set(true);
          if (row.vendorCode) this.vendorCode.set(true);
          if (row.hospitalName) this.hospitalName.set(true);
        }
      },
      error: () => {
        // legacy: empty error handler, silently swallowed
      },
    });
  }

  openConfirmModal(): void {
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  // ---- Ok inside confirm modal (mirrors SaveConsumableBarcodeConfig) ----
  save(): void {
    this.showConfirmModal.set(false);
    if (this.recordId == null) {
      this.create();
    } else {
      this.update(this.recordId);
    }
  }

  private payload() {
    return {
      itemName: this.itemName(),
      itemCode: this.itemCode(),
      batchNo: this.batchNo(),
      expiryDate: this.expiryDate(),
      lotNo: this.lotNo(),
      mrp: this.mrp(),
      salePrice: this.salePrice(),
      grnNo: this.grnNo(),
      vendorName: this.vendorName(),
      vendorCode: this.vendorCode(),
      hospitalName: this.hospitalName(),
      unitId: this.unitId,
      organizationId: this.organizationId,
      userId: this.userId,
    };
  }

  private create(): void {
    this.api.create(this.payload()).subscribe({
      next: () => this.notify.success('Record saved successfully'),
      error: () => this.notify.error('Something went wrong'),
    });
  }

  private update(id: number): void {
    this.api.update(id, this.payload()).subscribe({
      next: (res) => this.notify.success(res.message ?? 'ConsumableBarcodeConfig updated successfully'),
      error: () => this.notify.error('Something went wrong'),
    });
  }
}
