import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SessionService } from '../../core/session.service';
import { CommonDetailHandoffService } from '../../core/common-detail-handoff.service';
import { DetailsFixedAssetMasterService } from './details-fixed-asset-master.service';
import { DfamAccessory, DfamDetail, DfamItem } from './details-fixed-asset-master.models';

/**
 * Rebuild of the legacy `detailsFixedAssetMaster` state -- see
 * detailsFixedAssetMasterController.js (95 lines). No data.moduleId (no
 * resolve block), real cookieObject guard only. Item id comes from the
 * shared, app-wide `commonDetailService.getDataId()` slot -- receiving
 * side only, sender (`addfixedAssetMaster`) is out of this batch, same
 * out-of-batch-placeholder treatment as other screens this project has
 * already built.
 *
 * Sharpened Step 1 checklist applied: no live-vs-commented guard
 * divergence found (this screen has no sibling in this batch); not a
 * mockup (real GET call, real ng-repeat-bound table, confirmed via the
 * pre-proposal real-bindings check); the 3 "view details" popup
 * functions' commented-out `.find()` bodies are genuinely dead but
 * harmless (the simplified live code they surround does the same job
 * correctly) -- not reproduced as commented-out dead code, since there's
 * no observable behavioral difference to preserve.
 *
 * Bug found and faithfully reproduced: `GetDetailsFixedAssetMaster`
 * calls `$rootScope.stopLoader()` synchronously, immediately after
 * firing the async GET, not inside its `.then()` callback
 * (detailsFixedAssetMasterController.js:31-38) -- the loading spinner
 * clears before the table actually has data, every time. Cosmetic, not
 * a crash; replicated via calling `loader.stop()` right after
 * `loader.start()` rather than in the subscribe callback.
 */
@Component({
  selector: 'app-details-fixed-asset-master',
  imports: [RouterLink, DatePipe],
  templateUrl: './details-fixed-asset-master.html',
  styleUrl: './details-fixed-asset-master.scss',
})
export class DetailsFixedAssetMaster {
  private readonly session = inject(SessionService);
  private readonly handoff = inject(CommonDetailHandoffService);
  private readonly api = inject(DetailsFixedAssetMasterService);

  detail = signal<DfamDetail>({ items: [] });
  itemForAccessoryDetail = signal<DfamAccessory[]>([]);
  itemForSpareDetail = signal<DfamAccessory[]>([]);
  insuranceDetail = signal<DfamItem['insurance'] | undefined>(undefined);

  showAccessoryModal = signal(false);
  showSparesModal = signal(false);
  showInsuranceModal = signal(false);

  constructor() {
    const unitId = this.session.unitId ?? 0;
    const organizationId = this.session.organizationId ?? 0;
    const itemId = this.handoff.getDataId();

    // Faithful replication: stopLoader() fires synchronously, not on
    // response -- see class doc comment.
    this.api.getDetails(unitId, organizationId, itemId).subscribe({
      next: (res) => this.detail.set(res),
    });
  }

  viewAccessoryDetails(item: DfamItem): void {
    this.itemForAccessoryDetail.set(item.accesories ?? []);
    this.showAccessoryModal.set(true);
  }

  viewSparesDetails(item: DfamItem): void {
    this.itemForSpareDetail.set(item.spares ?? []);
    this.showSparesModal.set(true);
  }

  viewInsuranceDetails(item: DfamItem): void {
    this.insuranceDetail.set(item.insurance);
    this.showInsuranceModal.set(true);
  }
}
