import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { StoreSelectionService } from '../../core/store-selection.service';
import { DetailScheduleWorklistHandoffService, AssetScheduleDetailObject } from '../../core/detail-schedule-worklist-handoff.service';
import { DetailScheduleWorklistService } from './detail-schedule-worklist.service';
import {
  DswChecklistEquipment,
  DswStore,
  DswAssetChecklistItem,
  DswSpareConsumptionItem,
  DswItemSearchResult,
} from './detail-schedule-worklist.models';

/**
 * Rebuild of the legacy `detailScheduleWorklist` state -- see
 * detailScheduleWorklistController.js (457 lines). Full audit:
 * docs/ANGULAR_PENDING_FINDINGS.md entries 24 (checklist modal's
 * two-selects-one-model bug, replicated), 25 (PMS-details modal's 4 tabs
 * confirmed always empty here, rendered as an inert shell), 37 (recurs
 * via shared StoreSelectionService -- this controller has an active
 * storePopup call, unlike PMSchedule), 40 (SEVERITY 1, the
 * addSpareConsumption crash -- fixed 2026-07-20, matching legacy's own
 * fix: the store dropdown is now bound to a real `selectedStoreId`
 * signal instead of the undefined `scheduleWorklistObj.storeId`).
 *
 * No data.moduleId -- no resolve block, real cookieObject guard only.
 * `assetObject.assetScheduleId == null` redirects to `scheduleWorklist`
 * -- this one IS one of this batch's 14 screens (screen 7) and already
 * built, so this redirect target is real and used as-is (not an
 * out-of-batch placeholder like other redirects in this batch).
 */
@Component({
  selector: 'app-detail-schedule-worklist',
  imports: [FormsModule],
  templateUrl: './detail-schedule-worklist.html',
  styleUrl: './detail-schedule-worklist.scss',
})
export class DetailScheduleWorklist {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly storeSelection = inject(StoreSelectionService);
  private readonly handoff = inject(DetailScheduleWorklistHandoffService);
  private readonly api = inject(DetailScheduleWorklistService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;

  assetObject: AssetScheduleDetailObject;
  pmsTypeSelected = signal('OutSource');

  checklistEquipmentList = signal<DswChecklistEquipment[]>([]);
  storeList = signal<DswStore[]>([]);
  itemResponseList = signal<DswItemSearchResult[]>([]);
  spareConsumptionList = signal<DswSpareConsumptionItem[]>([]);
  assetChecklist = signal<DswAssetChecklistItem[]>([]);

  spareConsumptionObj = { itemCode: '', itemName: '', qty: '', itemId: undefined as number | undefined };
  selectedStoreId = signal<number | undefined>(undefined);

  showAdd = signal(true);
  showSave = signal(true);
  showView = signal(true);
  showChecklistModal = signal(false);
  showPmsDetailsModal = signal(false);
  pmsDetailsTab = signal(1);
  checklistMapperObj = signal<{ checklistEquipmentMasterId?: number }>({});

  ticketNo = signal('');
  pmsDetails = signal('');
  pmsRemarks = signal('');
  workOrderStatus = signal('N');
  completionDate = signal('');
  completionTime = signal('');
  underObservationDate = signal('');
  underObservationTime = signal('');
  private clickCountC = 0;
  private clickCountO = 0;
  private clickCountS = 0;

  constructor() {
    this.assetObject = this.handoff.takeObject();

    if (this.assetObject.assetScheduleId == null) {
      this.router.navigate(['/scheduleWorklist']);
    }

    this.pmsTypeSelected.set(this.assetObject.pmsType === 'InHouse' ? 'InHouse' : 'OutSource');

    const cookieObject = this.session.current;
    if (cookieObject == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }

    if (this.storeSelection.getSelectedStoreId() == null) {
      this.storeSelection.checkAndFetchStores();
      this.storeSelection.show();
    }

    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.loader.start();
    this.api.getAllEquipmentChecklistMasterList(this.unitId, this.organizationId).subscribe({
      next: (res) => this.checklistEquipmentList.set(res.status === 'success' ? (res.listObject ?? []) : []),
    });

    this.api.getDetailedStoreList(this.userId, this.organizationId, this.unitId).subscribe({
      next: (res) => this.storeList.set(res.listObject ?? []),
    });

    const pmsDetailsId = (this.assetObject as { pmsDetailsId?: number }).pmsDetailsId;
    if (pmsDetailsId != null && pmsDetailsId !== 0) {
      this.fetchPMSDetailsById(pmsDetailsId);
    } else {
      (this.assetObject as { pmsDetailsId?: number }).pmsDetailsId = 0;
    }
    this.loader.stop();
  }

  private applyPmsDetails(obj: {
    assetScheduleId?: number;
    ticketNo?: string;
    pmsDetails?: string;
    pmsRemarks?: string;
    workOrderStatus?: string;
    completionDate?: string;
    completionTime?: string;
    underObservationDate?: string;
    underObservationTime?: string;
    getSpareConsumptionList?: DswSpareConsumptionItem[];
  }): void {
    this.assetObject.assetScheduleId = obj.assetScheduleId;
    this.ticketNo.set(obj.ticketNo ?? '');
    this.pmsDetails.set(obj.pmsDetails ?? '');
    this.pmsRemarks.set(obj.pmsRemarks ?? '');
    this.workOrderStatus.set(obj.workOrderStatus || 'N');
    this.completionDate.set(obj.completionDate ?? '');
    this.completionTime.set(obj.completionTime ?? '');
    this.underObservationDate.set(obj.underObservationDate ?? '');
    this.underObservationTime.set(obj.underObservationTime ?? '');
    this.spareConsumptionList.set(obj.getSpareConsumptionList ?? []);
  }

  fetchPMSDetailsById(pmsDetailsId?: number): void {
    const id = pmsDetailsId ?? (this.assetObject as { pmsDetailsId?: number }).pmsDetailsId ?? 0;
    this.api.fetchPMSByPMSId(id, this.unitId, this.organizationId).subscribe({
      next: (res) => {
        if (res.status === 'success' && res.object) {
          this.applyPmsDetails(res.object);
        }
      },
    });
  }

  getCheckListByAssetScheduleId(assetScheduleId: number | undefined, action: 'view' | 'add'): void {
    if (action === 'view') {
      this.showAdd.set(false);
      this.showView.set(false);
      this.showSave.set(false);
    } else {
      this.showAdd.set(true);
      this.showView.set(true);
      this.showSave.set(true);
    }
    if (assetScheduleId != null && assetScheduleId !== 0) {
      this.loader.start();
      this.api.getCheckListByAssetScheduleId(assetScheduleId, this.unitId, this.organizationId).subscribe({
        next: (res) => {
          this.loader.stop();
          this.assetChecklist.set(res.listObject ?? []);
        },
      });
    }
    this.showChecklistModal.set(true);
  }

  searchItemByCodeOrName(): void {
    this.loader.start();
    this.api
      .searchItemByCodeOrName(this.spareConsumptionObj.itemCode, this.spareConsumptionObj.itemName, this.assetObject['itemNameId'] as number | undefined, this.unitId, this.organizationId)
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.itemResponseList.set(res.listObject ?? []);
          }
        },
      });
  }

  selectItem(item: DswItemSearchResult): void {
    this.spareConsumptionObj.itemCode = item.itemCode;
    this.spareConsumptionObj.itemName = item.itemName;
  }

  addChecklistToAsset(): void {
    const mapperId = this.checklistMapperObj().checklistEquipmentMasterId;
    const match = this.checklistEquipmentList().find((c) => c.checklistEquipmentMasterId === mapperId);
    if (match) {
      this.assetChecklist.update((list) => [
        ...list,
        {
          assetScheduleChecklistId: 0,
          assetScheduleId: this.assetObject.assetScheduleId ?? 0,
          checklistNameId: match.checklistEquipmentMasterId,
          checklistName: match.checklistName,
          checklistCode: match.checklistCode,
        },
      ]);
    }
  }

  addAndUpdateChecklistData(): void {
    this.loader.start();
    this.api
      .addAndUpdateChecklist({
        getChecklistData: this.assetChecklist(),
        createdBy: this.userId,
        createdDate: formatDateTime(new Date()),
        unitId: this.unitId,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Asset Schedule Check List Updated sucessfully!!!.');
          } else {
            this.notify.error('something wrong!!!!.');
          }
        },
      });
  }

  removeChecklistToAsset(index: number): void {
    const item = this.assetChecklist()[index];
    if (item?.assetScheduleChecklistId !== 0) {
      this.loader.start();
      this.api.deleteChecklist(item.assetScheduleChecklistId, this.userId, formatDateTime(new Date())).subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success('Asset Schedule Check List deleted sucessfully!!!.');
          } else {
            this.notify.error('something wrong!!!!.');
          }
        },
      });
    }
    this.assetChecklist.update((list) => list.filter((_, i) => i !== index));
  }

  /** Entry 40 fix (2026-07-20): now reads the real `selectedStoreId`
   * signal instead of the undefined `scheduleWorklistObj.storeId`. */
  addSpareConsumption(): void {
    if (this.selectedStoreId() !== undefined) {
      const storeId = this.selectedStoreId()!;
      if (this.itemResponseList().length === 0) {
        this.notify.error('Please input an Item...!!');
        return;
      }
      for (const value of this.itemResponseList()) {
        if (value.itemCode === this.spareConsumptionObj.itemName || value.itemCode === this.spareConsumptionObj.itemCode) {
          this.spareConsumptionList.update((list) => [
            ...list,
            {
              spareConsumptionId: 0,
              assetId: value.itemId,
              assetName: value.itemName,
              assetCode: value.itemCode,
              storeId: Number(storeId),
              serialNo: '',
              assetQty: 1,
              remark: '',
            },
          ]);
        }
      }
    } else {
      this.notify.error('Please Select a Store...!!');
    }
  }

  setPMSSrNo(index: number, value: string): void {
    this.spareConsumptionList.update((list) => {
      const copy = [...list];
      copy[index] = { ...copy[index], serialNo: value };
      return copy;
    });
  }

  setPMSRemark(index: number, value: string): void {
    this.spareConsumptionList.update((list) => {
      const copy = [...list];
      copy[index] = { ...copy[index], remark: value };
      return copy;
    });
  }

  removeSpareFromList(index: number): void {
    const item = this.spareConsumptionList()[index];
    if (item?.spareConsumptionId !== 0) {
      this.deleteSpareFromList(item.spareConsumptionId);
    }
    this.spareConsumptionList.update((list) => list.filter((_, i) => i !== index));
  }

  deleteSpareFromList(spareConsumptionId: number): void {
    this.loader.start();
    this.api.deleteSpareById(spareConsumptionId, this.userId, formatDateTime(new Date())).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.notify.success('Spare deleted sucessfully!!!.');
        } else {
          this.notify.error('something wrong!!!!.');
        }
      },
    });
  }

  getWorkOrderStatus(): void {
    const status = this.workOrderStatus();
    if (status === 'C') {
      if (this.clickCountC === 0) {
        this.clickCountC = 1;
        this.clickCountO = 0;
        this.clickCountS = 0;
      } else {
        this.workOrderStatus.set('N');
        this.clickCountC = 0;
      }
    } else if (status === 'O') {
      if (this.clickCountO === 0) {
        this.clickCountO = 1;
        this.clickCountC = 0;
        this.clickCountS = 0;
      } else {
        this.workOrderStatus.set('N');
        this.clickCountO = 0;
      }
    } else if (status === 'S') {
      if (this.clickCountS === 0) {
        this.clickCountS = 1;
        this.clickCountO = 0;
        this.clickCountC = 0;
      } else {
        this.workOrderStatus.set('N');
        this.clickCountS = 0;
      }
    } else {
      this.workOrderStatus.set('N');
      this.clickCountC = 0;
      this.clickCountO = 0;
      this.clickCountS = 0;
    }
  }

  onWorkOrderStatusChange(value: string): void {
    this.workOrderStatus.set(value);
    this.getWorkOrderStatus();
  }

  savePMSDetails(): void {
    if (this.workOrderStatus() === 'C' && (!this.completionDate() || !this.completionTime())) {
      this.notify.warning('Please Select a Completion Date and Time...!!');
      return;
    }
    if (this.workOrderStatus() === 'O' && (!this.underObservationDate() || !this.underObservationTime())) {
      this.notify.warning('Please Select Date and Time for Under Observation...!!');
      return;
    }
    if (!this.ticketNo()) {
      this.notify.warning('Please enter ticket no...!!');
      return;
    }

    if (this.workOrderStatus() === 'C') {
      this.underObservationDate.set('');
      this.underObservationTime.set('');
    }
    if (this.workOrderStatus() === 'O') {
      this.completionDate.set('');
      this.completionTime.set('');
    }
    if (this.workOrderStatus() === 'S') {
      this.completionDate.set('');
      this.completionTime.set('');
      this.underObservationDate.set('');
      this.underObservationTime.set('');
    }

    const pmsDetailsId = (this.assetObject as { pmsDetailsId?: number }).pmsDetailsId ?? 0;
    const now = formatDateTime(new Date());
    this.loader.start();
    this.api
      .saveOrUpdatePMSDetails({
        pmsDetailsId,
        assetScheduleId: this.assetObject.assetScheduleId,
        pmsType: this.pmsTypeSelected(),
        ticketNo: this.ticketNo(),
        pmsDetails: this.pmsDetails(),
        pmsRemarks: this.pmsRemarks(),
        workOrderStatus: this.workOrderStatus() || 'N',
        completionDate: this.completionDate(),
        completionTime: this.completionTime(),
        underObservationDate: this.underObservationDate(),
        underObservationTime: this.underObservationTime(),
        getChecklistData: this.assetChecklist(),
        getSpareConsumptionList: this.spareConsumptionList(),
        createdBy: this.userId,
        createdDate: now,
        updatedBy: this.userId,
        updatedDate: now,
        unitId: this.unitId,
        organizationId: this.organizationId,
      })
      .subscribe({
        next: (res) => {
          this.loader.stop();
          if (res.status === 'success') {
            this.notify.success(pmsDetailsId === 0 ? 'PMS Details Saved sucessfully...!!' : 'PMS Details Updated sucessfully...!!');
            (this.assetObject as { pmsDetailsId?: number }).pmsDetailsId = res.object?.pmsDetailsId;
            this.fetchPMSDetailsById();
          } else {
            this.notify.error('Something went wrong...!!');
          }
        },
      });
  }
}

function formatDateTime(d: Date): string {
  const dd = String(d.getDate()).padStart(2, '0');
  const mm = String(d.getMonth() + 1).padStart(2, '0');
  const yyyy = d.getFullYear();
  const hh = String(d.getHours()).padStart(2, '0');
  const min = String(d.getMinutes()).padStart(2, '0');
  const ss = String(d.getSeconds()).padStart(2, '0');
  return `${dd}-${mm}-${yyyy} ${hh}:${min}:${ss}`;
}
