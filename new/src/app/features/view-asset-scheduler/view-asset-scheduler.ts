import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { SessionService } from '../../core/session.service';
import { LoaderService } from '../../core/loader.service';
import { StoreSelectionService } from '../../core/store-selection.service';
import { AssetDataHandoffService } from '../../core/asset-data-handoff.service';
import { ViewAssetSchedulerService } from './view-asset-scheduler.service';
import { AssetScheduleItem, AssetScheduleChecklistItem } from './view-asset-scheduler.models';

const MONTH_NAMES = [
  'January',
  'February',
  'March',
  'April',
  'May',
  'June',
  'July',
  'August',
  'September',
  'October',
  'November',
  'December',
];

/**
 * Rebuild of the legacy `viewAssetScheduler` state -- see
 * viewAssetSchedulerController.js (123 lines). Full audit:
 * docs/ANGULAR_PENDING_FINDINGS.md entry 16 (RESOLVED -- rebuilt against
 * the CURRENT, corrected controller, which already has the
 * selectedTopStore else-branch fix, not the pre-fix crash).
 *
 * No data.moduleId on this route -- legacy's state has no resolve block,
 * but the controller has a real cookieObject session guard, same
 * moduleAccessGuard-with-no-moduleId shape as pesapalProcessPage.
 *
 * assetInfo comes from AssetDataHandoffService (in-memory ui-router param
 * `assetDataObj`, sent by assetPMSScheduler -- not itself one of this
 * batch's 14 screens). Store selection uses the shared
 * StoreSelectionService/root-shell modal (see entry 37 for the known
 * orphaned-modal bug, faithfully replicated, not fixed here).
 *
 * `$scope.fetchAssetSchedule`/`createAssetScheduleList`
 * (viewAssetSchedulerController.js:73-92) are confirmed dead -- never
 * called from the view, `createAssetScheduleList` never bound anywhere --
 * not modeled here, matching the established "don't rebuild dead code"
 * precedent. The commented-out search box (view-asset-scheduler.html's
 * `filter:searchKeyword` with no live input ever setting it) is
 * similarly inert and not rebuilt.
 */
@Component({
  selector: 'app-view-asset-scheduler',
  imports: [DatePipe],
  templateUrl: './view-asset-scheduler.html',
  styleUrl: './view-asset-scheduler.scss',
})
export class ViewAssetScheduler {
  private readonly session = inject(SessionService);
  private readonly loader = inject(LoaderService);
  private readonly storeSelection = inject(StoreSelectionService);
  private readonly handoff = inject(AssetDataHandoffService);
  private readonly api = inject(ViewAssetSchedulerService);

  assetInfo = signal(this.handoff.takeAssetData());
  assetScheduleList = signal<AssetScheduleItem[]>([]);
  assetScheduleCheckList = signal<AssetScheduleChecklistItem[]>([]);
  showChecklistModal = signal(false);

  constructor() {
    if (this.storeSelection.getSelectedStoreId() == null) {
      this.storeSelection.checkAndFetchStores();
      this.storeSelection.show();
    }

    this.loader.start();
    const unitId = this.session.unitId ?? 0;
    const organizationId = this.session.organizationId ?? 0;
    const info = this.assetInfo();

    this.api.fetchAssetSchedule(info.itemNameId, info.serialNo, unitId, organizationId).subscribe({
      next: (res) => {
        this.loader.stop();
        this.assetScheduleList.set(res.listObject ?? []);
      },
      error: () => this.loader.stop(),
    });
  }

  getMonth(dateStr: string | undefined): string {
    if (!dateStr) return '';
    const date = new Date(dateStr);
    const monthIndex = date.getMonth() + 1;
    return MONTH_NAMES[monthIndex - 1] ?? '';
  }

  getCheckListByAssetScheduleId(assetScheduleId: number): void {
    if (assetScheduleId === 0) return;
    const unitId = this.session.unitId ?? 0;
    const organizationId = this.session.organizationId ?? 0;

    this.loader.start();
    this.api.fetchCheckListByAssetScheduleId(assetScheduleId, unitId, organizationId).subscribe({
      next: (res) => {
        this.loader.stop();
        this.assetScheduleCheckList.set(res.listObject ?? []);
        this.showChecklistModal.set(true);
      },
      error: () => this.loader.stop(),
    });
  }
}
