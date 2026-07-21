import { TestBed } from '@angular/core/testing';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { DetailScheduleWorklist } from './detail-schedule-worklist';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { NotificationService } from '../../core/notification.service';
import { StoreSelectionService } from '../../core/store-selection.service';
import { DetailScheduleWorklistHandoffService } from '../../core/detail-schedule-worklist-handoff.service';
import { DetailScheduleWorklistService } from './detail-schedule-worklist.service';

/**
 * Regression test for ANGULAR_PENDING_FINDINGS.md entry 40: clicking
 * "Add" in the Spare Consumption section used to throw unconditionally
 * (dereferencing the never-defined `scheduleWorklistObj`), every time,
 * regardless of whether a store was selected. Fixed 2026-07-20 by
 * binding the dropdown to a real `selectedStoreId` signal instead.
 */
describe('DetailScheduleWorklist -- entry 40 spare-consumption crash fix', () => {
  let component: DetailScheduleWorklist;
  let notify: { success: ReturnType<typeof vi.fn>; error: ReturnType<typeof vi.fn>; warning: ReturnType<typeof vi.fn> };

  beforeEach(async () => {
    notify = { success: vi.fn(), error: vi.fn(), warning: vi.fn() };

    await TestBed.configureTestingModule({
      imports: [DetailScheduleWorklist],
      providers: [
        { provide: Router, useValue: { navigate: vi.fn() } },
        {
          provide: SessionService,
          useValue: { current: { unitId: 1, organizationId: 1, userId: 1 }, unitId: 1, organizationId: 1, userId: 1 },
        },
        { provide: LoaderService, useValue: { start: vi.fn(), stop: vi.fn() } },
        { provide: NotificationService, useValue: notify },
        {
          provide: StoreSelectionService,
          useValue: { getSelectedStoreId: () => 1, checkAndFetchStores: vi.fn(), show: vi.fn() },
        },
        { provide: DetailScheduleWorklistHandoffService, useValue: { takeObject: () => ({ assetScheduleId: 5 }) } },
        {
          provide: DetailScheduleWorklistService,
          useValue: {
            getAllEquipmentChecklistMasterList: () => of({ status: 'success', listObject: [] }),
            getDetailedStoreList: () => of({ status: 'success', listObject: [{ storeId: 1, storeName: 'Main Store' }] }),
            fetchPMSByPMSId: () => of({ status: 'success', object: {} }),
          },
        },
      ],
    }).compileComponents();

    const fixture = TestBed.createComponent(DetailScheduleWorklist);
    component = fixture.componentInstance;
  });

  it('does not throw when Add is clicked with no store selected, and shows the intended error instead', () => {
    component.itemResponseList.set([{ itemId: 1, itemCode: 'X1', itemName: 'Widget' }]);
    component.spareConsumptionObj.itemCode = 'X1';

    // Before the fix, this always threw a TypeError regardless of item
    // state -- scheduleWorklistObj was undefined, full stop.
    expect(() => component.addSpareConsumption()).not.toThrow();

    expect(notify.error).toHaveBeenCalledWith('Please Select a Store...!!');
    expect(component.spareConsumptionList()).toEqual([]);
  });

  it('adds a spare-consumption row when a store is selected and the typed code matches a searched item', () => {
    component.selectedStoreId.set(7);
    component.itemResponseList.set([{ itemId: 1, itemCode: 'X1', itemName: 'Widget' }]);
    component.spareConsumptionObj.itemCode = 'X1';

    component.addSpareConsumption();

    expect(component.spareConsumptionList()).toEqual([
      { spareConsumptionId: 0, assetId: 1, assetName: 'Widget', assetCode: 'X1', storeId: 7, serialNo: '', assetQty: 1, remark: '' },
    ]);
  });

  it('shows the "select an item" error and adds nothing when no item has been searched, even with a store selected', () => {
    component.selectedStoreId.set(7);

    expect(() => component.addSpareConsumption()).not.toThrow();

    expect(notify.error).toHaveBeenCalledWith('Please input an Item...!!');
    expect(component.spareConsumptionList()).toEqual([]);
  });
});
