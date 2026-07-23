import { Component, inject, signal } from '@angular/core';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { CorrectiveMaintenanceScheduleWorklistHandoffService } from '../../core/corrective-maintenance-schedule-worklist-handoff.service';
import { DetailsCorrectiveMaintenanceScheduleWorklistService } from './details-corrective-maintenance-schedule-worklist.service';
import {
  DcmswReplaceAssetObj,
  DcmswResolutionObj,
  DcmswRow,
  DcmswSerialOption,
  DcmswSpareConsumption,
  DcmswSpareSearchResult,
  DcmswUser,
  DcmswVendorObj,
  DcmswVendorOption,
} from './details-corrective-maintenance-schedule-worklist.models';

/**
 * Rebuild of the legacy `detailsCorrectiveMaintenanceScheduleWorklist`
 * state -- see detailsCorrectiveMaintenanceScheduleWorklistController.js
 * (855 lines) and details-corrective-maintenance-schedule-worklist.html
 * (826 lines). No data.moduleId (no resolve block), real cookieObject
 * guard only. Row data arrives via
 * `CorrectiveMaintenanceScheduleWorklistHandoffService.takeObject()`, the
 * rebuild's equivalent of legacy's `$stateParams.detailsObj` (ctrl:32).
 *
 * CRASH FIX (the main reason this screen was flagged): legacy's ctrl:20-26
 * reads `cookieObjectAppConfig.assetMaintenanceSpecialityId` (ctrl:22)
 * BEFORE its own `cookieObject == undefined -> $state.go('login')` guard
 * even runs (ctrl:23-26) -- so on an expired/missing session this throws
 * instead of cleanly redirecting. Fixed here by running the standard
 * login guard FIRST, then reading `session.appConfig?.assetMaintenanceSpecialityId`
 * defensively via optional chaining -- the exact established pattern
 * `CookieObjectAppConfig` already documents in session.service.ts for this
 * same bug class (see its doc comment citing `roomModality`/
 * `radiologySpecialityId`, entry 34). If absent, `undefined` is sent as
 * `specialityId` to getEmployeeListBySpecialityAndSubSpeciality rather
 * than crashing.
 *
 * Two separate status id-spaces, kept as distinct typed fields (do NOT
 * conflate, they overlap numerically but mean different things):
 * - `maintenanceWorklistStatusId` (on the received row): `4` == "Work In
 *   Progress" for that field.
 * - `maintenanceScheduleStatusId` (set by THIS screen's own Save):
 *   `2` == "Completed", `4` == "Work In Progress" too, but it's a
 *   different column entirely.
 *
 * Bugs faithfully reproduced (NOT fixed), confirmed via source read:
 * - Replace ("saveReplace", ctrl:682-706): the modal's own Save button
 *   never issues an HTTP call -- it only stages `replaceAssetObj` in
 *   memory. That staged object is bundled into the *next* Resolution
 *   Details "Save" call as `correctiveMaintenanceReplacementDto`, but the
 *   Replace modal's Save/Cancel buttons themselves are a dead end with no
 *   direct persistence. Reproduced below: `saveReplace()` only closes the
 *   modal and updates `replaceAssetObj`, never calls the API.
 * - Return: the entire Return button/modal is HTML-commented-out in
 *   legacy (view:399-405, 716-802 unreachable from any visible control).
 *   Omitted entirely here -- no Return button is rendered, matching
 *   legacy's own zero visible trace of it.
 * - `saveDetails()`'s two allocation-type validations (ctrl:789-798) are
 *   dead code: one compares `ticketNo` to the literal `" "` (a single
 *   space, never equal to `""`), the other reads `saveObj.VendorId`
 *   (capital V, a key that is never set -- the real key is lowercase
 *   `vendorId`), so `undefined == " "` is always false. Neither check can
 *   ever fire. Omitted below rather than ported as pointless dead code;
 *   only the one validation that actually works (`resolutionDetails`
 *   non-empty) is kept.
 * - "Is Completed" / "Under Observation" toggles (ctrl:340-375, view:285,323):
 *   BOTH checkboxes' `ng-click` calls the same `isComplete()`, which
 *   branches only on `resolutionObj.isCompleted`'s current value -- so
 *   clicking "Under Observation" doesn't branch on itself, it just
 *   re-evaluates based on whatever `isCompleted` already is. Reproduced
 *   as a single `onCompletionToggle()` handler used by both checkboxes.
 * - "Is Start" (ctrl:377-408, view:196-199): `ng-disabled="resolutionObj.isStart"`
 *   makes this a one-way switch (off -> on only, never back off) once
 *   checked and saved; the HTTP call only fires on the on-transition.
 *   Reproduced identically.
 *
 * Simplified/deferred sub-flows (see final report): the spares-consumption
 * "Item Code"/"Item Name" fields are real `uib-typeahead` autocompletes in
 * legacy (view:459-488); rebuilt here as a manual search-then-pick list
 * (still calls the real `getCorrectiveMainSparesComsumptionByitemId`
 * endpoint) rather than a live-typing autocomplete.
 */
@Component({
  selector: 'app-details-corrective-maintenance-schedule-worklist',
  imports: [FormsModule, RouterLink],
  templateUrl: './details-corrective-maintenance-schedule-worklist.html',
  styleUrl: './details-corrective-maintenance-schedule-worklist.scss',
})
export class DetailsCorrectiveMaintenanceScheduleWorklist {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notification = inject(NotificationService);
  private readonly handoff = inject(CorrectiveMaintenanceScheduleWorklistHandoffService);
  private readonly api = inject(DetailsCorrectiveMaintenanceScheduleWorklistService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;
  private storeId = 0;
  private assetMaintenanceSpecialityId: number | undefined;

  detail = signal<DcmswRow>({});

  // Allocation Details (top-level fields in legacy, not nested in resolutionObj)
  assignTo = signal<number | ''>('');
  scheduleDateIso = signal('');
  scheduleTime = signal('');
  allocationRemark = signal('');
  isAllocationDisabled = signal(false);
  usersList = signal<DcmswUser[]>([]);

  // Is Start
  isResolutionDisabled = signal(true);

  // Resolution Details
  resolution = signal<DcmswResolutionObj>({
    requestAllocationTypeId: 1,
    ticketNo: '',
    resolutionDetails: '',
    vendorId: '',
    isCompleted: false,
    assetUpDateIso: '',
    assetUpTime: '',
    underObservation: false,
    fromDateIso: '',
    toDateIso: '',
    isStart: false,
    remark: '',
  });
  vendorObj = signal<DcmswVendorObj>({});
  vendorList = signal<DcmswVendorOption[]>([]);

  // Spares consumption
  showSparesModal = signal(false);
  spareItemCode = signal('');
  spareItemName = signal('');
  spareQty = signal<number | ''>('');
  spareItemId = signal<number | ''>('');
  spareStock = signal(0);
  spareSearchResults = signal<DcmswSpareSearchResult[]>([]);
  sparesComsumptionList = signal<DcmswSpareConsumption[]>([]);
  private tempSpareList: DcmswSpareConsumption[] = [];

  // Replace
  showReplaceModal = signal(false);
  serialNumberList = signal<DcmswSerialOption[]>([]);
  serialNoObj = signal<DcmswSerialOption>({});
  replaceBtnDisabled = signal(false);
  private replaceAssetObj: DcmswReplaceAssetObj = {};

  constructor() {
    // Standard login guard FIRST -- see class doc comment's crash-fix note.
    const cookieObject = this.session.current;
    if (cookieObject == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;
    this.storeId = (cookieObject['storeId'] as number | undefined) ?? 0;
    // Read defensively -- CookieObjectAppConfig documents this exact field
    // as optional for this exact reason (see class doc comment).
    this.assetMaintenanceSpecialityId = this.session.appConfig?.assetMaintenanceSpecialityId;

    const obj = this.handoff.takeObject();
    this.detail.set(obj as DcmswRow);

    this.scheduleTime.set(obj.scheduleTime ?? '');
    this.scheduleDateIso.set(parseLegacyDateToIso(obj.scheduleDate));
    this.assignTo.set(obj.assignTo ?? '');
    this.allocationRemark.set(obj.allocationRemark ?? '');

    if (obj.maintenanceWorklistStatusId === 4) {
      this.resolution.update((r) => ({ ...r, isStart: true }));
      this.isResolutionDisabled.set(false);
      this.isAllocationDisabled.set(true);
    } else {
      this.resolution.update((r) => ({ ...r, isStart: false }));
      this.isResolutionDisabled.set(true);
      this.isAllocationDisabled.set(false);
    }

    this.getEmployeeList();

    // Existing schedule record -- back-fill Resolution Details + fetch related lists.
    if (obj.correctiveMaintenanceScheduleId !== undefined) {
      this.backfillFromExistingRecord(obj);
      this.getConsumptionDetails();
      this.getReplaceDetails();
    }
  }

  private backfillFromExistingRecord(obj: DcmswRow): void {
    this.resolution.update((r) => {
      const next = { ...r };
      if (obj.assetUpDate == null) {
        next.fromDateIso = parseLegacyDateToIso(obj.fromDate);
        next.toDateIso = parseLegacyDateToIso(obj.toDate);
      } else {
        next.assetUpDateIso = parseLegacyDateToIso(obj.assetUpDate);
        next.assetUpTime = obj.assetUpTime ?? '';
      }
      next.isCompleted = obj.isCompleted ?? false;
      next.isStart = obj.isStart ?? next.isStart;
      next.underObservation = obj.isUnderObservation ?? false;
      next.remark = obj.reSolutionRemark ?? '';
      next.requestAllocationTypeId = obj.requestAllocationTypeId ?? next.requestAllocationTypeId;
      next.resolutionDetails = obj.resolutionDetails ?? '';
      next.ticketNo = obj.ticketNo ?? '';
      if (obj.vendorId != null) {
        next.vendorId = obj.vendorId;
      }
      return next;
    });

    if (obj.vendorId != null) {
      if (obj.requestAllocationTypeId === 0) {
        this.getVendorListByBatch(obj.batchId);
      } else if (obj.requestAllocationTypeId === 2) {
        this.getVendorList();
        this.vendorObj.update((v) => ({ ...v, vendorId: obj.vendorId }));
      } else {
        this.vendorObj.set({});
      }
    }
  }

  private getEmployeeList(): void {
    this.loader.start();
    this.api
      .getEmployeeList({
        organizationId: this.organizationId,
        unitId: this.unitId,
        specialityId: this.assetMaintenanceSpecialityId,
      })
      .subscribe({
        next: (res) => {
          this.usersList.set(res.status === 'success' ? (res.listObject ?? []) : []);
          this.loader.stop();
        },
        error: () => this.loader.stop(),
      });
  }

  /** Reallocate -- ctrl:209-272. */
  updateAllocationDetails(): void {
    const d = this.detail();
    if (this.assignTo() === '') {
      this.notification.warning('Please Select Assign To');
      return;
    }
    if (!this.scheduleDateIso()) {
      this.notification.warning('Please select scheduleDate');
      return;
    }
    if (!this.scheduleTime()) {
      this.notification.warning('Please select scheduleTime');
      return;
    }

    const payload = {
      correctiveMaintenanceWorklistId: d.correctiveMaintenanceWorklistId,
      correctiveMaintenanceId: d.correctiveMaintenanceId,
      scheduleDate: isoToLegacyDateTime(this.scheduleDateIso()),
      scheduleTime: this.scheduleTime(),
      maintenanceWorklistStatusId: 3, // assigned
      assignTo: this.assignTo(),
      allocationRemark: this.allocationRemark(),
      createdBy: this.userId,
      createdDate: formatLegacyNow(),
      status: 'A',
      updatedBy: this.userId,
      updatedDate: formatLegacyNow(),
      unitId: this.unitId,
      organizationId: this.organizationId,
    };

    this.loader.start();
    this.api.saveAllocation(payload).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.notification.success(res.message ?? '');
          this.router.navigate(['/correctiveMaintenanceScheduleWorklist']);
        } else if (res.status === 'error') {
          this.notification.error(res.message ?? '');
        }
      },
      error: () => this.loader.stop(),
    });
  }

  reallocateDisabled(): boolean {
    return this.detail().maintenanceWorklistStatusId !== 3 || this.isAllocationDisabled();
  }

  allocationFieldsDisabled(): boolean {
    return this.isAllocationDisabled() || this.detail().maintenanceScheduleStatusId === 2;
  }

  /** Is Start toggle -- ctrl:377-408. One-way switch: disabled once checked (see class doc comment). */
  updateStartedTask(checked: boolean): void {
    this.resolution.update((r) => ({ ...r, isStart: checked }));
    if (checked) {
      const d = this.detail();
      const payload = {
        updatedBy: this.userId,
        updatedDate: formatLegacyNow(),
        unitId: this.unitId,
        organizationId: this.organizationId,
        correctiveMaintenanceId: d.correctiveMaintenanceId,
        maintenanceStatusId: 4, // work in progress
      };
      this.loader.start();
      this.api.updateStartedTask(payload).subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.isAllocationDisabled.set(true);
            this.isResolutionDisabled.set(false);
          }
        },
        error: () => this.loader.stop(),
      });
    } else {
      this.isAllocationDisabled.set(false);
      this.isResolutionDisabled.set(true);
    }
  }

  /** Allocation-type radio click handler -- sets the value then runs the same side effects as legacy's `ng-click="changeStatus()"`. */
  setAllocationType(typeId: number): void {
    this.resolution.update((r) => ({ ...r, requestAllocationTypeId: typeId }));
    this.changeStatus();
  }

  /** Allocation-type radio -- ctrl:318-338. 1 = In house, 0 = OEM, 2 = Out Source. */
  private changeStatus(): void {
    const typeId = this.resolution().requestAllocationTypeId;
    if (typeId === 0) {
      this.getVendorListByBatch(this.detail().batchId);
    } else if (typeId === 2) {
      this.getVendorList();
    } else {
      this.resolution.update((r) => ({ ...r, ticketNo: '' }));
      this.vendorObj.set({});
    }
  }

  private getVendorListByBatch(batchId: number | undefined): void {
    this.loader.start();
    this.api
      .getVendorListByBatch({ batchId, unitId: this.unitId, organizationId: this.organizationId })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success' && res.object != null) {
            this.vendorObj.set(res.object);
          }
        },
        error: () => this.loader.stop(),
      });
  }

  private getVendorList(): void {
    this.loader.start();
    this.api.getVendorList(this.organizationId, this.unitId).subscribe({
      next: (list) => {
        this.vendorList.set(list ?? []);
        this.loader.stop();
      },
      error: () => this.loader.stop(),
    });
  }

  /** Both "Is Completed" and "Under Observation" checkboxes call this -- see class doc comment. */
  onCompletionToggle(): void {
    const r = this.resolution();
    if (r.isCompleted) {
      const now = new Date();
      // Legacy's off-by-one zero-pad bug (ctrl:346-356): pads only when
      // `< 9` (not `< 10`), so hour/minute 9 renders unpadded ("9") while
      // 0-8 are zero-padded. Reproduced exactly, not "fixed" to padStart.
      const legacyPad = (n: number) => (n < 9 ? `0${n}` : `${n}`);
      this.resolution.update((cur) => ({
        ...cur,
        underObservation: false,
        fromDateIso: '',
        toDateIso: '',
        assetUpTime: `${legacyPad(now.getHours())}:${legacyPad(now.getMinutes())}:00`,
        assetUpDateIso: formatYmd(now),
      }));
    } else {
      const today = formatYmd(new Date());
      this.resolution.update((cur) => ({
        ...cur,
        isCompleted: false,
        underObservation: true,
        assetUpDateIso: '',
        assetUpTime: '',
        fromDateIso: today,
        toDateIso: today,
      }));
    }
  }

  itemStock(): void {
    const d = this.detail();
    const tomorrow = new Date();
    tomorrow.setDate(tomorrow.getDate() + 1);
    this.loader.start();
    this.api
      .checkItemStock({
        itemId: d.itemId,
        fromDate: formatYmd(tomorrow),
        storeId: this.storeId,
      })
      .subscribe({
        next: (rows) => {
          this.loader.stop();
          if (rows && rows.length > 0) {
            this.spareStock.set(rows[0].currentStock ?? 0);
          }
        },
        error: () => this.loader.stop(),
      });
  }

  searchSpareItem(): void {
    const d = this.detail();
    this.loader.start();
    this.api
      .searchSpareItem({
        itemCode: this.spareItemCode(),
        itemName: this.spareItemName(),
        itemId: d.itemId,
        unitId: this.unitId,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          this.spareSearchResults.set(res.status === 'success' ? (res.listObject ?? []) : []);
        },
        error: () => this.loader.stop(),
      });
  }

  selectSpareItem(info: DcmswSpareSearchResult): void {
    this.itemStock();
    this.spareItemName.set(info.itemName ?? '');
    this.spareItemCode.set(info.itemCode ?? '');
    this.spareItemId.set(info.itemId ?? '');
    this.spareSearchResults.set([]);
  }

  addSpareDetails(): void {
    const itemId = this.spareItemId();
    const already = this.sparesComsumptionList().some((v) => v.itemId === itemId && v.status === 'A');
    if (already) {
      this.notification.warning('Selected Item Already Exist');
      this.resetSpareForm();
      return;
    }
    if (this.spareItemName() === '') {
      this.notification.warning('Please Selected Item');
      return;
    }
    if (this.spareQty() === '') {
      this.notification.warning('Please Selected Item Qty');
      return;
    }
    if (this.spareStock() < Number(this.spareQty())) {
      this.notification.warning('Spare stock should be greater than enter Qty');
      return;
    }

    const entry: DcmswSpareConsumption = {
      correctiveMaintenanceSparesComsumptionId: null,
      itemName: this.spareItemName(),
      itemCode: this.spareItemCode(),
      itemId: itemId === '' ? undefined : itemId,
      qty: this.spareQty(),
      sparesComsumptionRemark: '',
      createdBy: this.userId,
      stock: this.spareStock(),
      createdDate: formatLegacyNow(),
      status: 'A',
      updatedBy: this.userId,
      updatedDate: formatLegacyNow(),
      unitId: this.unitId,
      organizationId: this.organizationId,
    };
    this.sparesComsumptionList.update((list) => [...list, entry]);
    this.resetSpareForm();
  }

  private resetSpareForm(): void {
    this.spareItemCode.set('');
    this.spareItemName.set('');
    this.spareQty.set('');
    this.spareItemId.set('');
    this.spareStock.set(0);
  }

  removeSpareItems(index: number, info: DcmswSpareConsumption): void {
    if (info.correctiveMaintenanceSparesComsumptionId != null) {
      const removed = { ...info, status: 'I' as const };
      this.tempSpareList.push(removed);
    }
    this.sparesComsumptionList.update((list) => list.filter((_, i) => i !== index));
  }

  private getConsumptionDetails(): void {
    const d = this.detail();
    this.loader.start();
    this.api
      .getSparesConsumptionList({
        correctiveMaintenanceScheduleId: d.correctiveMaintenanceScheduleId,
        unitId: this.unitId,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.sparesComsumptionList.set(res.listObject ?? []);
          }
        },
        error: () => this.loader.stop(),
      });
  }

  private getReplaceDetails(): void {
    const d = this.detail();
    this.loader.start();
    this.api
      .getReplaceDetails({
        correctiveMaintenanceScheduleId: d.correctiveMaintenanceScheduleId,
        unitId: this.unitId,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            const list = res.listObject ?? [];
            if (list.length > 0) {
              this.replaceBtnDisabled.set(true);
            }
          }
        },
        error: () => this.loader.stop(),
      });
  }

  /** Opens the Replace modal -- ctrl:835-838. */
  replaceAsset(): void {
    this.getSerialNumbers();
    this.showReplaceModal.set(true);
  }

  private getSerialNumbers(): void {
    const d = this.detail();
    this.loader.start();
    this.api.getSerialNoByItemId(d.itemId ?? '', this.unitId, this.organizationId).subscribe({
      next: (res) => {
        this.loader.stop();
        const list = (res.listObject ?? []).filter((v) => v.batchId !== d.batchId);
        this.serialNumberList.set(list);
        if (list.length === 0) {
          this.notification.warning('Stock Not Available');
        }
      },
      error: () => this.loader.stop(),
    });
  }

  /**
   * Replace modal's Save -- ctrl:682-706. FAITHFULLY NON-FUNCTIONAL: only
   * stages `replaceAssetObj` in memory and closes the modal. There is no
   * HTTP call here in legacy, and none here either -- see class doc
   * comment. The staged object is later included in `saveDetails()`'s
   * payload as `correctiveMaintenanceReplacementDto`, but this button
   * itself persists nothing.
   */
  saveReplace(): void {
    const serial = this.serialNoObj();
    if (serial.batchId === undefined || (serial.batchId as unknown) === '') {
      this.notification.warning('Please Select Serial Number');
      return;
    }
    const d = this.detail();
    this.replaceAssetObj = {
      correctiveMaintenanceId: d.correctiveMaintenanceId,
      itemId: d.itemId,
      requestedStoreId: d.storeId,
      requestedSerialNoId: d.batchId,
      newSerialNoId: serial.batchId,
      newStoreId: this.storeId,
      createdBy: this.userId,
      createdDate: formatLegacyNow(),
      updatedBy: this.userId,
      updatedDate: formatLegacyNow(),
      unitId: this.unitId,
      organizationId: this.organizationId,
    };
    // No save call by design -- matches legacy's dead-end Replace save.
    this.showReplaceModal.set(false);
  }

  /** Resolution Details Save -- ctrl:709-833. */
  saveDetails(): void {
    const r = this.resolution();
    const d = this.detail();
    let assetUpDateLegacy = '';
    let fromDateLegacy = '';
    let toDateLegacy = '';
    let completionDate = '';
    let maintenanceScheduleStatusId: number;
    let assetUpTime = r.assetUpTime;

    if (r.isCompleted) {
      if (!r.assetUpDateIso) {
        this.notification.warning('Please Select Asset Up Date');
        return;
      }
      if (!r.assetUpTime) {
        this.notification.warning('Please Select Asset Up Time');
        return;
      }
      assetUpDateLegacy = isoToLegacyDateTime(r.assetUpDateIso);
      maintenanceScheduleStatusId = 2; // Completed
      completionDate = formatLegacyNow();
    } else {
      if (!r.fromDateIso) {
        this.notification.warning('Please Select From Date');
        return;
      }
      if (!r.toDateIso) {
        this.notification.warning('Please Select ToDate');
        return;
      }
      assetUpTime = '';
      fromDateLegacy = isoToLegacyDateTime(r.fromDateIso);
      toDateLegacy = isoToLegacyDateTime(r.toDateIso);
      maintenanceScheduleStatusId = 4; // Work In Progress
    }

    if (r.resolutionDetails === '') {
      this.notification.warning('Please Enter Resolution Details');
      return;
    }
    // NOTE: legacy also has ticketNo/VendorId checks here (ctrl:789-798)
    // that can never fire (typo'd comparisons) -- omitted, see class doc comment.

    let spares = this.sparesComsumptionList();
    if (this.tempSpareList.length > 0) {
      spares = spares.concat(this.tempSpareList);
    }

    const vendor = this.vendorObj();
    const payload: Record<string, unknown> = {
      correctiveMaintenanceScheduleId: d.correctiveMaintenanceScheduleId ?? null,
      correctiveMaintenanceWorklistId: d.correctiveMaintenanceWorklistId,
      correctiveMaintenanceId: d.correctiveMaintenanceId,
      assetUpTime,
      assetUpDate: assetUpDateLegacy,
      completionDate,
      fromDate: fromDateLegacy,
      toDate: toDateLegacy,
      isCompleted: r.isCompleted,
      isStart: r.isStart,
      isUnderObservation: r.underObservation,
      maintenanceScheduleStatusId,
      requestAllocationTypeId: r.requestAllocationTypeId,
      resolutionDetails: r.resolutionDetails,
      ticketNo: r.ticketNo,
      vendorId: vendor != null && vendor.vendorId != null ? vendor.vendorId : null,
      remark: r.remark,
      createdBy: this.userId,
      createdDate: formatLegacyNow(),
      status: 'A',
      updatedBy: this.userId,
      updatedDate: formatLegacyNow(),
      unitId: this.unitId,
      organizationId: this.organizationId,
      correctiveMaintenanceSparesComsumptionDto: spares,
      correctiveMaintenanceReplacementDto: this.replaceAssetObj,
      storeId: this.storeId,
    };

    this.loader.start();
    this.api.saveResolution(payload).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.notification.success(res.message ?? '');
          this.router.navigate(['/correctiveMaintenanceScheduleWorklist']);
        } else if (res.status === 'error') {
          this.notification.error(res.message ?? '');
        }
      },
      error: () => this.loader.stop(),
    });
  }
}

/** Legacy parses `scheduleDate`/`fromDate`/`toDate`/`assetUpDate` with `moment(value, "DD-MM-YYYY")`. */
function parseLegacyDateToIso(value: string | null | undefined): string {
  if (!value) return '';
  const match = /^(\d{2})-(\d{2})-(\d{4})/.exec(value);
  if (!match) return '';
  const [, dd, mm, yyyy] = match;
  return `${yyyy}-${mm}-${dd}`;
}

/** Legacy's save-time format, `moment(dateObj).format('DD-MM-YYYY HH:mm:ss')`, always midnight for pure-date fields. */
function isoToLegacyDateTime(iso: string): string {
  if (!iso) return '';
  const [yyyy, mm, dd] = iso.split('-');
  if (!yyyy || !mm || !dd) return '';
  return `${dd}-${mm}-${yyyy} 00:00:00`;
}

/** Legacy: `moment(new Date()).format('DD-MM-YYYY HH:mm:ss')`. */
function formatLegacyNow(): string {
  const d = new Date();
  const pad = (n: number) => String(n).padStart(2, '0');
  return `${pad(d.getDate())}-${pad(d.getMonth() + 1)}-${d.getFullYear()} ${pad(d.getHours())}:${pad(d.getMinutes())}:${pad(d.getSeconds())}`;
}

function formatYmd(d: Date): string {
  return `${d.getFullYear()}-${String(d.getMonth() + 1).padStart(2, '0')}-${String(d.getDate()).padStart(2, '0')}`;
}
