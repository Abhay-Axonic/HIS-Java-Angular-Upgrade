import { Component, inject, signal } from '@angular/core';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { DatePipe } from '@angular/common';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { StoreSelectionService } from '../../core/store-selection.service';
import { AssetSchedulerHandoffService } from '../../core/asset-scheduler-handoff.service';
import { AssetSchedulerService } from './asset-scheduler.service';
import {
  PmsFrequency,
  ChecklistEquipment,
  AssetScheduleChecklistItem,
  SchedulerObj,
  CreateAssetScheduleRow,
} from './asset-scheduler.models';

const FREQUENCY_MAP: Record<string, { amount: number; unit: 'days' | 'weeks' | 'months' | 'years'; label: string }> = {
  D: { amount: 1, unit: 'days', label: 'Daily' },
  W: { amount: 1, unit: 'weeks', label: 'Weekly' },
  M: { amount: 1, unit: 'months', label: 'Monthly' },
  Q: { amount: 3, unit: 'months', label: 'Quarterly' },
  HY: { amount: 6, unit: 'months', label: 'Half Yearly' },
  Y: { amount: 1, unit: 'years', label: 'Yearly' },
};

/**
 * Rebuild of the legacy `assetScheduler` state -- see
 * createAssetSchedulerController.js (484 lines). Full audit:
 * docs/ANGULAR_PENDING_FINDINGS.md entry 37 (recurs here via shared
 * StoreSelectionService -- this controller IS in entry 37's originally
 * named list, unlike PMSchedule).
 *
 * No data.moduleId on this route -- no resolve block, real cookieObject
 * guard only (no cookieObjectAppConfig check here, unlike
 * scheduleWorklist/PMSchedule). `assetInfoObj` (in-memory ui-router
 * param) missing `itemNameId` redirects to `assetPMSScheduler` -- not
 * one of this batch's 14 screens, not yet built. Redirects to
 * `/accessDenied?reason=assetInfoMissing` instead, same reasoning as
 * every other out-of-batch redirect target in this batch.
 *
 * Parity target: `calcDate`'s frequency-based schedule generation
 * (D/W/M/Q/HY/Y) ported exactly, including the loop's
 * isSameOrBefore(endDate) boundary (inclusive of the end date).
 */
@Component({
  selector: 'app-asset-scheduler',
  imports: [FormsModule, DatePipe],
  templateUrl: './asset-scheduler.html',
  styleUrl: './asset-scheduler.scss',
})
export class AssetScheduler {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly notify = inject(NotificationService);
  private readonly storeSelection = inject(StoreSelectionService);
  private readonly handoff = inject(AssetSchedulerHandoffService);
  private readonly api = inject(AssetSchedulerService);

  private unitId = 0;
  private organizationId = 0;
  private userId = 0;
  private storeId = 0;

  assetInfo = this.handoff.takeInfo();
  pmsFrequencyList = signal<PmsFrequency[]>([]);
  checklistEquipmentList = signal<ChecklistEquipment[]>([]);
  createAssetScheduleList = signal<CreateAssetScheduleRow[]>([]);
  searchKeyword = signal('');
  saveFFF = signal(true);

  schedulerObj: SchedulerObj;

  // Checklist modal state
  showAdd = signal(true);
  showView = signal(true);
  addChecklist = signal<AssetScheduleChecklistItem[]>([]);
  showChecklistModal = signal(false);
  selectedAssetScheduleId = signal(0);
  checklistMapperObj = signal<{ checklistEquipmentMasterId?: number }>({});
  private editIndex = 0;

  constructor() {
    if (this.assetInfo.itemNameId == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'assetInfoMissing' } });
      this.schedulerObj = this.emptySchedulerObj();
      return;
    }

    if (this.storeSelection.getSelectedStoreId() == null) {
      this.storeSelection.checkAndFetchStores();
      this.storeSelection.show();
    } else {
      this.storeId = this.storeSelection.getSelectedStoreId()!;
    }

    this.unitId = this.session.unitId ?? 0;
    this.organizationId = this.session.organizationId ?? 0;
    this.userId = this.session.userId ?? 0;

    this.loader.start();
    this.api.getFrequencyList(this.organizationId).subscribe({
      next: (list) => this.pmsFrequencyList.set(list ?? []),
    });

    this.api.getAllEquipmentChecklistMasterList(this.unitId, this.organizationId).subscribe({
      next: (res) => {
        this.checklistEquipmentList.set(res.status === 'success' ? (res.listObject ?? []) : []);
      },
    });

    this.schedulerObj = {
      assetScheduleId: 0,
      itemNameId: this.assetInfo.itemNameId,
      serialNo: this.assetInfo.serialNo,
      assetCategory: this.assetInfo.assetCategory,
      itemCode: this.assetInfo.itemCode,
      itemName: this.assetInfo.itemName,
      vendorName: this.assetInfo.vendor,
      vendorId: this.assetInfo.vendorId,
      manufacturerId: this.assetInfo.manufacturerId,
      pmsFrequency: '',
      pmsType: '',
      startDate: '',
      endDate: '',
      createdBy: this.userId,
      createdDate: formatDateTime(new Date()),
      status: 'A',
      processStatus: 0,
      updatedBy: this.userId,
      updatedDate: formatDateTime(new Date()),
      unitId: this.unitId,
      organizationId: this.organizationId,
    };

    this.fetchAssetSchedule();
  }

  private emptySchedulerObj(): SchedulerObj {
    return {
      assetScheduleId: 0,
      pmsFrequency: '',
      pmsType: '',
      startDate: '',
      endDate: '',
      createdBy: 0,
      createdDate: '',
      status: 'A',
      processStatus: 0,
      updatedBy: 0,
      updatedDate: '',
      unitId: 0,
      organizationId: 0,
    };
  }

  fetchAssetSchedule(): void {
    this.loader.start();
    this.api.fetchAssetSchedule(this.assetInfo.itemNameId, this.assetInfo.serialNo, this.unitId, this.organizationId).subscribe({
      next: (res) => {
        this.loader.stop();
        const list = res.listObject ?? [];
        this.createAssetScheduleList.set(list);
        if (list.length > 0) {
          this.saveFFF.set(false);
        }
      },
    });
  }

  createAssetSchedule(): void {
    if (!this.schedulerObj.pmsFrequency) {
      this.notify.warning('Select PMS frequency');
      return;
    }
    if (!this.schedulerObj.pmsType) {
      this.notify.warning('Select PMS type');
      return;
    }
    if (!this.schedulerObj.startDate) {
      this.notify.warning('Please select from date');
      return;
    }
    if (!this.schedulerObj.endDate) {
      this.notify.warning('Please select end date');
      return;
    }

    const dt = formatDateTime(new Date());
    const frequency = FREQUENCY_MAP[this.schedulerObj.pmsFrequency];
    const rows: CreateAssetScheduleRow[] = [];

    let current = new Date(this.schedulerObj.startDate);
    const end = new Date(this.schedulerObj.endDate);
    while (isSameOrBefore(current, end)) {
      rows.push({
        assetScheduleId: 0,
        itemNameId: this.schedulerObj.itemNameId,
        itemCode: this.schedulerObj.itemCode,
        itemName: this.schedulerObj.itemName,
        serialNo: this.schedulerObj.serialNo,
        vendor: this.schedulerObj.vendorName,
        vendorId: this.schedulerObj.vendorId,
        pmsFrequency: frequency.label,
        nextDate: new Date(current),
        pmsType: this.schedulerObj.pmsType,
        remark: '',
        storeId: this.storeId,
        manufacturerId: this.schedulerObj.manufacturerId,
        processStatus: 1,
        status: 'A',
        unitId: this.unitId,
        organizationId: this.organizationId,
        createdBy: this.userId,
        createdDate: dt,
        updatedBy: this.userId,
        updatedDate: dt,
        getChecklistData: [],
      });
      current = addToDate(current, frequency.amount, frequency.unit);
    }

    this.createAssetScheduleList.set(rows);
  }

  setIndex(index: number): void {
    this.editIndex = index;
    this.showAdd.set(true);
    this.showView.set(true);
    if (this.selectedAssetScheduleId() === 0) {
      this.addChecklist.set([...(this.createAssetScheduleList()[index]?.getChecklistData ?? [])]);
    }
    this.showChecklistModal.set(true);
  }

  getIndex(index: number): void {
    this.showAdd.set(false);
    this.editIndex = index;
    this.showView.set(false);
    if (this.selectedAssetScheduleId() === 0) {
      this.addChecklist.set([...(this.createAssetScheduleList()[index]?.getChecklistData ?? [])]);
    }
    this.showChecklistModal.set(true);
  }

  setCheckList(index: number, assetScheduleId: number): void {
    this.editIndex = index;
    this.addChecklist.set([]);
    this.selectedAssetScheduleId.set(assetScheduleId);
    this.getCheckListByAssetScheduleId(assetScheduleId);
    this.showChecklistModal.set(true);
  }

  addToCheckList(index: number, assetScheduleId: number): void {
    this.selectedAssetScheduleId.set(assetScheduleId);
    this.getCheckListByAssetScheduleId(assetScheduleId);
  }

  getCheckListByAssetScheduleId(assetScheduleId: number): void {
    if (assetScheduleId !== 0) {
      this.loader.start();
      this.api.getCheckListByAssetScheduleId(assetScheduleId, this.unitId, this.organizationId).subscribe({
        next: (res) => {
          this.loader.stop();
          this.addChecklist.set(res.listObject ?? []);
        },
      });
    }
  }

  addChecklistToAsset(): void {
    const mapperId = this.checklistMapperObj().checklistEquipmentMasterId;
    const match = this.checklistEquipmentList().find((c) => c.checklistEquipmentMasterId === mapperId);
    if (match) {
      this.addChecklist.update((list) => [
        ...list,
        {
          assetScheduleChecklistId: 0,
          assetScheduleId: this.selectedAssetScheduleId(),
          checklistNameId: match.checklistEquipmentMasterId,
          checklistName: match.checklistName,
          checklistCode: match.checklistCode,
        },
      ]);
    }
  }

  removeChecklistToAsset(index: number): void {
    this.addChecklist.update((list) => list.filter((_, i) => i !== index));
    this.createAssetScheduleList.update((rows) => {
      const copy = [...rows];
      if (copy[this.editIndex]) {
        copy[this.editIndex] = { ...copy[this.editIndex], getChecklistData: copy[this.editIndex].getChecklistData.filter((_, i) => i !== index) };
      }
      return copy;
    });
  }

  addChecklistData(): void {
    if (this.selectedAssetScheduleId() !== 0) {
      this.addAndUpdateChecklist();
    } else if (this.addChecklist().length > 0) {
      this.createAssetScheduleList.update((rows) => {
        const copy = [...rows];
        if (copy[this.editIndex]) {
          copy[this.editIndex] = { ...copy[this.editIndex], getChecklistData: this.addChecklist() };
        }
        return copy;
      });
    } else {
      this.notify.warning('Please add atleast one checkList!!!!.');
    }
    this.showChecklistModal.set(false);
  }

  addAndUpdateChecklist(): void {
    if (this.addChecklist().length > 0) {
      this.loader.start();
      this.api
        .addAndUpdateChecklist({
          getChecklistData: this.addChecklist(),
          createdBy: this.userId,
          createdDate: formatDateTime(new Date()),
          unitId: this.unitId,
          organizationId: this.organizationId,
        })
        .subscribe({
          next: (res) => {
            this.loader.stop();
            if (res.status === 'success') {
              this.notify.success('Asset Schedule Check List Updated Successfully!!!.');
            } else {
              this.notify.error('something wrong!!!!.');
            }
            this.selectedAssetScheduleId.set(0);
          },
        });
    } else {
      this.notify.error('No Data to Save !!!!.');
    }
  }

  deleteChecklist(assetScheduleChecklistId: number): void {
    this.loader.start();
    this.api.deleteChecklist(assetScheduleChecklistId, this.userId, formatDateTime(new Date())).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.notify.success('Asset Schedule Check List deleted Successfully!!!.');
          this.getCheckListByAssetScheduleId(this.selectedAssetScheduleId());
        } else {
          this.notify.error('something wrong!!!!.');
        }
      },
    });
  }

  saveAssetSchedule(): void {
    const rows = this.createAssetScheduleList();
    if (rows.length === 0) {
      this.notify.error('No Data to Save...!!');
      return;
    }
    this.loader.start();
    this.api.saveAssetSchedule(rows).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.notify.success('Asset Schedule Saved sucessfully...!!');
          this.fetchAssetSchedule();
        } else {
          this.notify.error('Something went wrong...!!');
        }
      },
    });
  }

  updateAssetSchedule(row: CreateAssetScheduleRow): void {
    if (row.assetScheduleId === 0) {
      this.notify.error('Asset Not Scheduled yet!!!!.');
      return;
    }
    this.loader.start();
    const updated = { ...row, updatedBy: this.userId, updatedDate: formatDateTime(new Date()) };
    this.api.updateAssetSchedule(updated).subscribe({
      next: (res) => {
        this.loader.stop();
        if (res.status === 'success') {
          this.notify.success('Asset Schedule Updated Successfully!!!.');
          this.fetchAssetSchedule();
        } else {
          this.notify.error('something wrong!!!!.');
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

function isSameOrBefore(a: Date, b: Date): boolean {
  return a.getTime() <= b.getTime();
}

function addToDate(d: Date, amount: number, unit: 'days' | 'weeks' | 'months' | 'years'): Date {
  const next = new Date(d);
  if (unit === 'days') next.setDate(next.getDate() + amount);
  else if (unit === 'weeks') next.setDate(next.getDate() + amount * 7);
  else if (unit === 'months') next.setMonth(next.getMonth() + amount);
  else if (unit === 'years') next.setFullYear(next.getFullYear() + amount);
  return next;
}
