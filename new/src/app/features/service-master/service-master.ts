import { Component, computed, inject, signal } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { SessionService } from '../../core/session.service';
import { NotificationService } from '../../core/notification.service';
import { StatusToggle } from '../../shared/status-toggle/status-toggle';
import { ServiceMasterService } from './service-master.service';
import {
  ChangeServiceMasterStatusPayload,
  GeneralLedgerMaster,
  InventoryItemLine,
  ItemMaster,
  SaveServiceMasterPayload,
  ServiceItemMapperPayload,
  ServiceMasterForm,
  ServiceMasterRecord,
  SpecialityMaster,
  SubSpecialityMaster,
  UpdateServiceMasterPayload,
  emptyServiceMaster,
} from './service-master.models';

const PAGE_SIZE_OPTIONS = [10, 25, 50, 100] as const;

/**
 * Parity rebuild of
 *   views/master/GlobalMaster/service-master.html +
 *   resources/assets/js/controller/master/serviceMasterController.js
 * (state `serviceMaster`, module id 376, "Admin - Global Master (general)").
 *
 * Legacy behaviors reproduced faithfully (flagged, not silently fixed, per
 * the standing "flag, don't silently fix" policy for write paths):
 *
 * 1. The legacy controller maps the general-ledger field through a
 *    typeahead that always resets `masterObj.generalLedgerId=null` on every
 *    keystroke (ledgerNameSearch, controller.js:189-191) and only re-sets it
 *    on explicit selection — typing a partial ledger name then saving
 *    persists generalLedgerId=null. Reproduced (the select resets the id on
 *    input), flagged as a parity quirk.
 *
 * 2. The status-toggle endpoint returns a success envelope even on a 0-row
 *    update (changeOrganizationServiceMasterStatus, ServiceMasterDaoImpl
 *    :107-121 — no row-count check). Same class as the campaign's
 *    "200+0 rows" false-success. The rebuild surfaces the response message
 *    as-is; a toggle that matched nothing will still say "Updated
 *    Successfully." (the org-trust side of this endpoint is COVERED — the
 *    BR-16x service rebind overrides the client org with the verified
 *    session org before the DAO call; live-proven during this batch).
 *
 * 3. The legacy save flow is TWO steps: the first click opens a custom
 *    confirm popup (`showModal`/`popUpFlag`, controller.js:535-538) and only
 *    the popup's OK re-invokes saveServiceMaster. Rebuilt as a direct save
 *    (one click) — parity divergence, flagged, not silently hidden.
 *
 * 4. The edit flow calls `.toString()` on the returned ids, including
 *    generalLedgerId after only clearing the name when null
 *    (controller.js:630-633) — a crash on a ledger-less record. Rebuilt
 *    null-safe (Number(...) with a guard) — divergence flagged.
 */
@Component({
  selector: 'app-service-master',
  imports: [FormsModule, StatusToggle],
  templateUrl: './service-master.html',
  styleUrl: './service-master.scss',
})
export class ServiceMaster {
  private readonly session = inject(SessionService);
  private readonly notify = inject(NotificationService);
  private readonly api = inject(ServiceMasterService);

  private readonly userId = this.session.userId ?? 0;
  private readonly organizationId = this.session.organizationId ?? 0;
  private readonly unitId = this.session.unitId ?? 0;

  // ---- dropdown data (mirrors $scope.specialityMasterList / subSpecialityMasterList / ledgerList) ----
  specialityMasterList = signal<SpecialityMaster[]>([]);
  subSpecialityMasterList = signal<SubSpecialityMaster[]>([]);
  ledgerList = signal<GeneralLedgerMaster[]>([]);
  itemList = signal<ItemMaster[]>([]);

  // ---- load-failure state (Track A: a failed load must never render as an
  // empty dropdown — a clinician cannot distinguish "no departments configured"
  // from "the call failed"). Each on-load call sets this on error; the template
  // shows the banner instead of a blank select. ---- 
  loadError = signal<string | null>(null);

  private setLoadError(what: string, err: unknown): void {
    const status = (err as { status?: number } | null)?.status;
    const reason = status ? `HTTP ${status}` : 'network error';
    this.loadError.set(`${what} failed to load (${reason}).`);
  }

  // ---- form state (mirrors $scope.masterObj / saveBtnFlag / updateBtnFlag) ----
  record = signal<ServiceMasterForm>(emptyServiceMaster(this.userId, this.organizationId));
  saveMode = signal(true); // true = saveBtnFlag, false = updateBtnFlag
  submitted = signal(false);

  // ---- grid + pagination (mirrors serviceList / commonListCount / PagerService) ----
  list = signal<ServiceMasterRecord[]>([]);
  totalCount = signal(0);
  pageSize = signal<number>(PAGE_SIZE_OPTIONS[0]);
  currentPage = signal(1);
  pageSizeOptions = PAGE_SIZE_OPTIONS;
  searchKeyword = signal('');
  loadingMsg = signal('');

  totalPages = computed(() => Math.max(1, Math.ceil(this.totalCount() / this.pageSize())));
  pageNumbers = computed(() => Array.from({ length: this.totalPages() }, (_, i) => i + 1));

  // ---- inventory-item modal state (mirrors inventoryItems / savedInventoryItems / itemObj / qty) ----
  inventoryModalOpen = signal(false);
  inventoryReadOnly = signal(false); // view mode (grid's Inventory Details icon) vs edit mode
  inventoryItems = signal<InventoryItemLine[]>([]);
  savedInventoryItems = signal<InventoryItemLine[]>([]);
  itemSearchTerm = signal('');
  itemObj = signal<{ itemId: number | null; itemName: string }>({ itemId: null, itemName: '' });
  qty = signal(0);

  constructor() {
    this.init();
    this.loadList();
  }

  // ---- init: speciality + ledger dropdowns (controller.js:104-157) ----
  private init(): void {
    this.api.getSpecialityList(this.organizationId, this.unitId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.specialityMasterList.set(res.listObject ?? []);
          this.loadError.set(null);
        }
      },
      error: (err) => this.setLoadError('Department list', err),
    });
    this.api.getGeneralLedgerList(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.ledgerList.set(res.listObject ?? []);
          this.loadError.set(null);
        }
      },
      error: (err) => this.setLoadError('General ledger list', err),
    });
  }

  // ---- grid load (controller.js:374-429) ----
  loadList(): void {
    const offset = (this.currentPage() - 1) * this.pageSize();
    this.api.getList(this.organizationId, offset, this.pageSize()).subscribe({
      next: (res) => {
        if (res.status === 'success') {
          this.list.set(res.listObject ?? []);
          this.loadError.set(null);
        }
      },
      error: (err) => this.setLoadError('Service list', err),
    });
    this.api.getCount(this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && typeof res.object === 'number') {
          this.totalCount.set(res.object);
        }
      },
      error: (err) => this.setLoadError('Service count', err),
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

  // ---- search (controller.js:864-888, serachServiceMaster) ----
  search(): void {
    const keyword = this.searchKeyword().trim();
    if (keyword === '') {
      this.loadList();
      return;
    }
    this.api.universalSearch(this.organizationId, keyword).subscribe((res) => {
      if (res.status === 'success') {
        this.list.set(res.listObject ?? []);
        this.totalCount.set((res.listObject ?? []).length);
        if ((res.listObject ?? []).length === 0) {
          this.loadingMsg.set('No record found...');
        }
      }
    });
  }

  // ---- department change -> sub-department dropdown (controller.js:514-523) ----
  onSpecialityChange(): void {
    const sid = this.record().specialityId;
    this.subSpecialityMasterList.set([]);
    if (sid != null) {
      this.api.getSubSpeciality(this.organizationId, sid).subscribe({
        next: (res) => {
          if (res.status === 'success') {
            this.subSpecialityMasterList.set(res.listObject ?? []);
            this.loadError.set(null);
          }
        },
        error: (err) => this.setLoadError('Sub-department list', err),
      });
    }
  }

  // ---- general-ledger typeahead (controller.js:189-210, ledgerNameSearch/SelectLedgerName) ----
  onLedgerSearch(keyword: string): void {
    // legacy parity: every keystroke resets the id (controller.js:191)
    this.record.update((r) => ({ ...r, generalLedgerId: null }));
    this.api.getGeneralLedgerList(this.organizationId).subscribe((res) => {
      if (res.status === 'success') {
        this.ledgerList.set((res.listObject ?? []).filter((l) => l.generalLedgerName.toLowerCase().includes(keyword.toLowerCase())));
      }
    });
  }

  selectLedger(ledger: GeneralLedgerMaster): void {
    this.record.update((r) => ({ ...r, generalLedgerId: ledger.generalLedgerId, generalLedgerName: ledger.generalLedgerName }));
  }

  // ---- reset form (mirrors the post-save/update reset) ----
  resetForm(): void {
    this.record.set(emptyServiceMaster(this.userId, this.organizationId));
    this.saveMode.set(true);
    this.submitted.set(false);
    this.inventoryItems.set([]);
    this.savedInventoryItems.set([]);
    this.itemObj.set({ itemId: null, itemName: '' });
    this.qty.set(0);
    this.itemSearchTerm.set('');
  }

  private buildMapperList(): ServiceItemMapperPayload[] {
    const now = new DatePipe('en-US').transform(new Date(), 'dd-MM-yyyy HH:mm:ss') ?? '';
    return this.inventoryItems().map((v) => ({
      serviceId: null,
      itemId: v.itemId as number,
      serviceItemMapperId: v.serviceItemMapperId,
      assignQty: Number(v.assignQty),
      organizationId: this.organizationId,
      createdDate: now,
      updatedDate: now,
      createdBy: this.userId,
      updatedBy: this.userId,
      status: v.status,
    }));
  }

  // ---- save (controller.js:531-603, saveServiceMaster) ----
  save(): void {
    this.submitted.set(true);
    const r = this.record();
    if (!r.specialityId || !r.subSpecialityId || !r.serviceCode || !r.serviceName) return;
    const now = new DatePipe('en-US').transform(new Date(), 'dd-MM-yyyy HH:mm:ss') ?? '';
    const payload: SaveServiceMasterPayload = {
      specialityId: r.specialityId,
      subSpecialityId: r.subSpecialityId,
      serviceStandardName: r.serviceName,
      codificationServiceName: r.codificationServiceName || null,
      serviceStandardCode: r.serviceCode,
      generalLedgerId: r.generalLedgerId,
      status: 'A',
      createdBy: this.userId,
      updatedBy: this.userId,
      organizationId: this.organizationId,
      createdDate: now,
      updatedDate: now,
      serviceItemMapperList: this.buildMapperList(),
    };
    this.api.save(payload).subscribe((res) => {
      if (res.status === 'success') { this.notify.success(res.message ?? ''); } else { this.notify.error(res.message ?? 'Save failed'); }
      this.resetForm();
      this.loadList();
    });
  }

  // ---- edit (controller.js:605-646, showUpdateBtn) ----
  edit(row: ServiceMasterRecord): void {
    this.api.getById(row.serviceMasterId, this.unitId, this.organizationId).subscribe((res) => {
      if (res.status === 'success' && res.object) {
        const o = res.object;
        this.record.set({
          serviceMasterId: o.serviceMasterId,
          specialityId: o.specialityId != null ? Number(o.specialityId) : null,
          subSpecialityId: o.subSpecialityId != null ? Number(o.subSpecialityId) : null,
          serviceName: o.serviceStandardName ?? '',
          codificationServiceName: o.codificationServiceName ?? '',
          serviceCode: o.serviceStandardCode ?? '',
          generalLedgerId: o.generalLedgerId != null ? Number(o.generalLedgerId) : null,
          generalLedgerName: o.generalLedgerName ?? '',
        });
        this.saveMode.set(false);
        if (o.specialityId != null) this.onSpecialityChange();
        this.api.getServiceItemMapperDetails(o.serviceMasterId, this.organizationId).subscribe((mres) => {
          if (mres.status === 'success') {
            this.inventoryItems.set(mres.listObject ?? []);
            this.savedInventoryItems.set(mres.listObject ?? []);
          }
        });
      }
    });
  }

  // ---- update (controller.js:692-767, updateServiceMaster) ----
  update(): void {
    this.submitted.set(true);
    const r = this.record();
    if (!r.specialityId || !r.subSpecialityId || !r.serviceCode || !r.serviceName || !r.serviceMasterId) return;
    const now = new DatePipe('en-US').transform(new Date(), 'dd-MM-yyyy HH:mm:ss') ?? '';
    const payload: UpdateServiceMasterPayload = {
      specialityId: r.specialityId,
      subSpecialityId: r.subSpecialityId,
      serviceStandardName: r.serviceName,
      codificationServiceName: r.codificationServiceName || null,
      serviceStandardCode: r.serviceCode,
      generalLedgerId: r.generalLedgerId,
      status: 'A',
      updatedBy: this.userId,
      organizationId: this.organizationId,
      updatedDate: now,
      serviceMasterId: r.serviceMasterId,
      serviceItemMapperList: this.buildMapperList(),
    };
    this.api.update(payload).subscribe((res) => {
      if (res.status === 'success') { this.notify.success(res.message ?? ''); } else { this.notify.error(res.message ?? 'Save failed'); }
      this.resetForm();
      this.loadList();
    });
  }

  // ---- status toggle (controller.js:649-689, updateStatus) ----
  onToggleStatus(row: ServiceMasterRecord): void {
    const next = row.status === 'A' ? 'I' : 'A';
    const now = new DatePipe('en-US').transform(new Date(), 'dd-MM-yyyy HH:mm:ss') ?? '';
    const payload: ChangeServiceMasterStatusPayload = {
      serviceMasterId: row.serviceMasterId,
      organizationId: this.organizationId,
      status: next,
      updatedBy: this.userId,
      updatedDate: now,
    };
    this.api.changeStatus(payload).subscribe((res) => {
      if (res.status === 'success') { this.notify.success(res.message ?? ''); } else { this.notify.error(res.message ?? 'Save failed'); }
      this.loadList();
    });
  }

  // ---- inventory modal (controller.js:245-366) ----
  openInventoryModal(row: ServiceMasterRecord, readOnly: boolean): void {
    this.inventoryReadOnly.set(readOnly);
    this.itemObj.set({ itemId: null, itemName: '' });
    this.qty.set(0);
    this.inventoryModalOpen.set(true);
    if (readOnly) {
      this.api.getServiceItemMapperDetails(row.serviceMasterId, this.organizationId).subscribe((res) => {
        if (res.status === 'success') {
          this.savedInventoryItems.set(res.listObject ?? []);
          this.inventoryItems.set([]);
        }
      });
    }
  }

  searchItems(keyword: string): void {
    this.itemSearchTerm.set(keyword);
    if (keyword.trim() === '') {
      this.itemList.set([]);
      return;
    }
    this.api.searchItems(keyword.trim()).subscribe((res) => {
      // The item search returns a RAW ARRAY (confirmed live: [{itemId,itemName,...}]
      // / []), same as legacy `complete()` hands the typeahead. The legacy
      // controller's `response.data.status == "success"` check never matches an
      // array — its `searchItems()` (POST /typeahead/search) is dead code, and
      // the active path is this GET. Accept the raw array here.
      this.itemList.set(Array.isArray(res) ? res : []);
    });
  }

  selectItem(item: ItemMaster): void {
    this.itemObj.set({ itemId: item.itemId, itemName: item.itemName });
    this.itemList.set([]);
  }

  addInventoryItem(): void {
    const item = this.itemObj();
    if (item.itemId == null) {
      this.notify.warning('Please Select Item');
      return;
    }
    if (this.qty() === 0) {
      this.notify.warning('Please Enter Quantity');
      return;
    }
    if (this.inventoryItems().some((v) => v.itemId === item.itemId)) {
      this.notify.warning('Item Already Exist');
      return;
    }
    this.inventoryItems.update((rows) => [...rows, { itemId: item.itemId, itemName: item.itemName, assignQty: this.qty(), status: 'A', serviceItemMapperId: null }]);
    this.itemObj.set({ itemId: null, itemName: '' });
    this.qty.set(0);
    this.itemSearchTerm.set('');
  }

  removeInventoryItem(line: InventoryItemLine): void {
    // Legacy parity (controller.js:302-310): persisted lines keep their id
    // and are flagged 'I' so the mapper update deactivates them; new lines
    // are spliced. The row list hides 'I' lines (legacy's `ng-if="item.status!='I'"`).
    if (line.serviceItemMapperId != null) {
      this.inventoryItems.update((rows) => rows.map((r) => (r.itemId === line.itemId ? { ...r, status: 'I' } : r)));
    } else {
      this.inventoryItems.update((rows) => rows.filter((r) => r !== line));
    }
  }

  closeInventoryModal(): void {
    this.inventoryModalOpen.set(false);
  }
}
