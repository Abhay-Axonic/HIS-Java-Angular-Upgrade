import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { StoreSelectionService } from '../../core/store-selection.service';
import { AddNewCorrectiveMaintenanceService } from './add-new-corrective-maintenance.service';
import {
  AncmAssetCategoryOption,
  AncmProductCategoryOption,
  AncmSearchItemRow,
  AncmSerialNoOption,
  AncmStoreOption,
  CorrectiveMaintenanceObj,
  SerialNoObj,
} from './add-new-corrective-maintenance.models';

/**
 * Rebuild of the legacy `addNewCorrectiveMaintenance` state -- see
 * addNewCorrectiveMaintenanceController.js (391 lines) +
 * add-new-corrective-maintenance-worklist.html (613 lines). No
 * data.moduleId/resolve block (app.js:10701-10706) -- real cookieObject
 * guard only, same as its sibling create-flow screens in this batch.
 *
 * Verdict from the pre-build audit: PARTIALLY-DEAD view, but the create/save
 * flow itself (store + asset-category driven item search -> serial-number
 * lookup -> full validation chain -> POST saveCorrectiveMaintenance) is
 * REAL and fully wired -- that's what's rebuilt here.
 *
 * Known dead binding in legacy itself, faithfully NOT reproduced as working
 * logic (view:491): the "select all items" checkbox in the "Add Item List"
 * modal references `ng-model="vm.model.selectAllRows"` /
 * `ng-click="vm.model.SelectAllItems()"` -- `vm` doesn't exist anywhere in
 * this controller (plain `$scope`, no `controllerAs`), so the checkbox is
 * inert in legacy. Rendered below for visual parity only, with no
 * (change)/(click) handler and no backing signal -- wiring it to a working
 * select-all would be new functionality legacy never had.
 *
 * view:117's `ng-disabled="editflag"` on the Serial No. select references
 * an undefined scope var in legacy -- harmless, the field simply never
 * disables. Not modeled (the field is just always enabled here too).
 *
 * Date fields use ISO string locals (`yyyy-MM-dd`) for the two
 * `<input type="date">` pickers, not raw JS `Date` objects -- see
 * asset-issue-acceptance.ts for the established fix for this exact class
 * of bug in this codebase. `downDateTime`/`resolutionExpectedDateTime` are
 * derived from those ISO strings at save time via `toLegacyDateTime()`
 * below, reproducing `moment(dateObj).format('DD-MM-YYYY HH:mm:ss')`
 * applied to a date-only JS Date (i.e. always midnight -- ctrl:276/331).
 */
@Component({
  selector: 'app-add-new-corrective-maintenance',
  imports: [FormsModule, RouterLink],
  templateUrl: './add-new-corrective-maintenance.html',
  styleUrl: './add-new-corrective-maintenance.scss',
})
export class AddNewCorrectiveMaintenance {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly storeSelection = inject(StoreSelectionService);
  private readonly api = inject(AddNewCorrectiveMaintenanceService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;
  private defaultStoreId = 0;

  corectiveMaintenanceObj!: CorrectiveMaintenanceObj;
  serialNoObj!: SerialNoObj;

  storeList = signal<AncmStoreOption[]>([]);
  assetCategoriesList = signal<AncmAssetCategoryOption[]>([]);
  productCategoriesList = signal<AncmProductCategoryOption[]>([]);
  searchNewItemList = signal<AncmSearchItemRow[]>([]);
  serialNumberList = signal<AncmSerialNoOption[]>([]);

  assetCategoryObj: AncmAssetCategoryOption | null = null;
  productCategoryObj: AncmProductCategoryOption | null = null;
  itemObj = { itemCode: '', itemName: '' };

  selectedSearchItem = signal<AncmSearchItemRow | null>(null);
  sortType = '';
  sortReverse = false;

  fromDate: string | undefined = undefined;
  toDate: string | undefined = undefined;

  showItemModal = signal(false);
  showConfirmModal = signal(false);

  constructor() {
    const cookieObject = this.session.current;
    if (cookieObject == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;
    // ctrl:27-31 -- reads the plain sessionStorage default, does NOT show
    // the shared store-selection modal (no storePopup()/checkAndFetchStores()
    // call anywhere in this controller).
    this.defaultStoreId = this.storeSelection.getSelectedStoreId() ?? 0;

    this.init();
  }

  // ---- ctrl:110-158 ----
  private init(): void {
    const dt = formatDateTime(new Date());
    this.corectiveMaintenanceObj = {
      correctiveMaintenanceId: null,
      itemId: '',
      storeId: this.defaultStoreId,
      requestTypeId: '',
      requestCriticalityId: '',
      serialNumber: '',
      isDown: false,
      requestDateTime: dt,
      downDateTime: '',
      downTime: '',
      batchId: '',
      resolutionExpectedDateTime: '',
      resolutionExpectedTime: '',
      requestDetails: '',
      maintenanceStatusId: 5,
      remark: '',
      createdBy: this.userId,
      createdDate: dt,
      status: 'A',
      updatedBy: this.userId,
      updatedDate: dt,
      unitId: this.unitId,
      organizationId: this.organizationId,
      assetName: '',
      assetCode: '',
      assetcategory: '',
    };
    this.serialNoObj = { batchId: '', serialNumber: '' };

    this.assetCategoriesList.set([]);
    this.assetCategoryObj = null;
    this.productCategoriesList.set([]);
    this.productCategoryObj = null;
    this.getAssetCategoryList();
    this.itemObj = { itemCode: '', itemName: '' };
    this.getStoreList();
    this.storeList.set([]);
    this.searchNewItemList.set([]);
    this.serialNumberList.set([]);
    this.selectedSearchItem.set(null);
    this.fromDate = undefined;
    this.toDate = undefined;
  }

  // ---- ctrl:80-91 ----
  private getStoreList(): void {
    this.loader.start();
    this.api.getStoreList(this.userId, this.organizationId, this.unitId).subscribe({
      next: (res) => {
        this.storeList.set(res.listObject ?? []);
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- ctrl:95-108 ----
  private getAssetCategoryList(): void {
    this.loader.start();
    this.api.getAssetCategoryList(this.unitId, this.organizationId).subscribe({
      next: (list) => {
        this.assetCategoriesList.set(list ?? []);
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- ctrl:163-175 ----
  getProductCategoryList(): void {
    const id = this.assetCategoryObj?.id;
    if (id == null) return;
    this.loader.start();
    this.api.getProductCategoryList(id, this.organizationId).subscribe({
      next: (list) => {
        this.productCategoriesList.set(list ?? []);
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- "Get Asset" button (view:27, ng-click="clearList()") ----
  openItemModal(): void {
    this.clearList();
    this.showItemModal.set(true);
  }

  closeItemModal(): void {
    this.showItemModal.set(false);
  }

  // ---- ctrl:254-262 ----
  private clearList(): void {
    this.assetCategoryObj = null;
    this.productCategoryObj = null;
    this.itemObj = { itemCode: '', itemName: '' };
    this.searchNewItemList.set([]);
    this.selectedSearchItem.set(null);
  }

  // ---- ctrl:178-195 ----
  searchNewItem(): void {
    const payload = {
      assetTypeId: 2,
      assetCategoryId: this.assetCategoryObj?.id,
      productCategoryId: this.productCategoryObj?.id,
      storeId: this.corectiveMaintenanceObj.storeId,
    };
    this.api.searchNewItem(this.unitId, payload).subscribe({
      next: (list) => {
        this.searchNewItemList.set(list ?? []);
        this.selectedSearchItem.set(null);
      },
    });
  }

  // ---- radio row select (view:538, ng-click="selectItem(item.isSelected)") ----
  selectSearchItem(item: AncmSearchItemRow): void {
    this.selectedSearchItem.set(item);
  }

  // ---- column-header sort carets (view:494-532) ----
  sortBy(field: string): void {
    this.sortType = field;
    this.sortReverse = !this.sortReverse;
  }

  /** ctrl doesn't have this -- reproduces the AngularJS template's
   * `| filter : itemObj.itemCode | orderBy:sortType:sortReverse` pipe
   * chain (view:536), including the AngularJS `filter:someString`
   * semantics of substring-matching against ANY string/number field on
   * the row, not just itemCode -- that's a real legacy quirk (the filter
   * expression happens to be bound to the Item Code input's model), kept
   * as-is rather than "corrected" to filter on itemCode alone. */
  filteredSearchItemList(): AncmSearchItemRow[] {
    const term = this.itemObj.itemCode.trim().toLowerCase();
    let rows = this.searchNewItemList();
    if (term) {
      rows = rows.filter((row) =>
        Object.values(row).some((v) => (typeof v === 'string' || typeof v === 'number') && String(v).toLowerCase().includes(term)),
      );
    }
    if (this.sortType) {
      const field = this.sortType;
      rows = [...rows].sort((a, b) => {
        const av = a[field];
        const bv = b[field];
        if (av == null && bv == null) return 0;
        if (av == null) return -1;
        if (bv == null) return 1;
        const as = String(av);
        const bs = String(bv);
        if (as < bs) return this.sortReverse ? 1 : -1;
        if (as > bs) return this.sortReverse ? -1 : 1;
        return 0;
      });
    }
    return rows;
  }

  // ---- ctrl:204-217 ----
  private getSerialNumbers(itemId: number): void {
    this.loader.start();
    this.api.getSerialNumbers(itemId, this.unitId, this.organizationId).subscribe({
      next: (res) => {
        this.serialNumberList.set(res.listObject ?? []);
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
  }

  // ---- "Add" button in item modal (view:556-558, ng-click="addItems()") ----
  addItems(): void {
    const item = this.selectedSearchItem();
    if (!item) return;
    this.corectiveMaintenanceObj.itemId = item.itemId;
    this.corectiveMaintenanceObj.assetName = item.itemName ?? '';
    this.corectiveMaintenanceObj.assetCode = item.itemCode ?? '';
    this.corectiveMaintenanceObj.assetcategory = item.assetCategory ?? '';
    this.getSerialNumbers(item.itemId);
    this.searchNewItemList.set([]);
    this.corectiveMaintenanceObj.serialNumber = '';
    this.showItemModal.set(false);
  }

  // ---- ctrl:244-252 -- Request Criticality -> Is NonOperative toggle ----
  getcriticality(): void {
    if (this.corectiveMaintenanceObj.requestCriticalityId === 1) {
      this.corectiveMaintenanceObj.isDown = false;
    } else if (this.corectiveMaintenanceObj.requestCriticalityId === 2) {
      this.corectiveMaintenanceObj.isDown = true;
    }
  }

  // ---- ctrl:264-272 -- the reverse: Is NonOperative toggle -> Request Criticality ----
  getIsDownStatus(): void {
    this.corectiveMaintenanceObj.requestCriticalityId = this.corectiveMaintenanceObj.isDown ? 2 : 1;
  }

  // ---- Save button (view:248, ng-click="comformCorectiveMaintenance()") ----
  comformCorectiveMaintenance(): void {
    this.showConfirmModal.set(true);
  }

  closeConfirmModal(): void {
    this.showConfirmModal.set(false);
  }

  /** Confirm modal's Ok button (view:585) -- `data-dismiss="modal"` closes
   * the modal unconditionally on click in legacy, independent of whatever
   * saveCorectiveMaintenance()'s validation chain decides underneath it. */
  confirmSave(): void {
    this.showConfirmModal.set(false);
    this.saveCorectiveMaintenance();
  }

  // ---- ctrl:273-390 -- full validation chain, reproduced in the exact
  // same order and with the exact same messages. ----
  private saveCorectiveMaintenance(): void {
    const obj = this.corectiveMaintenanceObj;
    obj.resolutionExpectedDateTime = toLegacyDateTime(this.toDate);
    obj.serialNumber = this.serialNoObj.serialNumber ?? '';
    obj.batchId = this.serialNoObj.batchId ?? '';

    if (obj.itemId === '') {
      this.notify.warning('Please Select Asset');
      return;
    } else if (obj.requestTypeId === '') {
      this.notify.warning('Please select request Type');
      return;
    } else if (obj.requestCriticalityId === '') {
      this.notify.warning('Please select request Criticality');
      return;
    } else if (this.toDate == null || this.toDate === '') {
      this.notify.warning('Please select resolution Expected Date');
      return;
    } else if (obj.resolutionExpectedTime == null || obj.resolutionExpectedTime === '') {
      this.notify.warning('Please select resolution Expected Time');
      return;
    } else if (obj.serialNumber === '') {
      this.notify.warning('Please select serial Number');
      return;
    } else if (obj.requestDetails === '') {
      this.notify.warning('Please select request Details');
      return;
    }

    obj.downDateTime = toLegacyDateTime(this.fromDate);
    if (obj.isDown) {
      if (obj.downTime === '') {
        this.notify.warning('Please select  down time');
        return;
      } else if (this.fromDate == null) {
        this.notify.warning('Please select  down date');
        return;
      } else if (obj.downDateTime === obj.resolutionExpectedDateTime) {
        if (obj.downTime >= obj.resolutionExpectedTime) {
          this.notify.warning('Resolution Expected Time should be greater than down time');
          return;
        }
      }
    }

    this.loader.start();
    this.api.save(obj).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.init();
          this.notify.success(res.message ?? 'Success!');
          setTimeout(() => this.router.navigate(['/correctiveMaintenance']), 3000);
        } else if (res.status === 'error') {
          this.notify.error(res.message ?? 'Error!');
        }
      },
      error: () => this.loader.stop(),
    });
  }
}

function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const mi = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${mi}:${ss}`;
}

/** `moment(dateObj).format('DD-MM-YYYY HH:mm:ss')` applied to a date built
 * from a date-only `<input type="date">` picker -- always midnight, same
 * as legacy's uib-datepicker-sourced JS Date (ctrl:276/331). */
function toLegacyDateTime(iso: string | undefined): string {
  if (!iso) return '';
  const [y, m, d] = iso.split('-').map(Number);
  if (!y || !m || !d) return '';
  return formatDateTime(new Date(y, m - 1, d, 0, 0, 0));
}
