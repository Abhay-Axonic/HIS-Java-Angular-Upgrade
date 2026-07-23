import { Component, inject, signal } from '@angular/core';
import { DatePipe } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { SessionService } from '../../core/session.service';
import { CommonDetailHandoffService } from '../../core/common-detail-handoff.service';
import { DetailsAssetReturnService } from './details-asset-return.service';
import { DetailsAssetReturn as DetailsAssetReturnModel } from './details-asset-return.models';

/**
 * Rebuild of the legacy `detailsAssetReturn` state -- see
 * detailAssetReturnController.js (57 lines) + details-asset-return.html
 * (174 lines). Self-registered state, NO resolve/moduleId guard in legacy
 * (unlike the list state `assetReturn`, moduleId 216) -- only the real
 * cookieObject session guard applies here, matching legacy's own lack of
 * a permission gate on this detail screen (`config($stateProvider)` in
 * detailAssetReturnController.js has no `resolve` block at all). Item id
 * comes from the shared `CommonDetailHandoffService` slot, set by the
 * list screen's own `AssetReturnDetails(areturn)` / this rebuild's
 * `AssetReturn.viewDetails(row)`.
 *
 * Pure read-only view -- no approve/reject/edit, just a Back link to
 * `/assetReturn` (details-asset-return.html:113, `href="#/assetReturn"`).
 *
 * NOT rebuilt: the `viewDetailsAccessoriesModal` markup
 * (details-asset-return.html:122-163) and its would-be trigger,
 * `setaccessory(item)` (ctrl:35-39). The modal and the function both
 * exist as real code, but the ONLY element that could ever call
 * `setaccessory` (details-asset-return.html:88-90, an "Accessories"
 * column's View button) is HTML-commented-out, along with the column
 * header that would have gone with it (view:49-59, also commented). Real
 * code, zero reachable callers -- confirmed unreachable, omitted here,
 * same "confirmed-dead -> don't rebuild" treatment already established
 * by details-fixed-asset-master.ts's similarly-dead popup functions.
 */
@Component({
  selector: 'app-details-asset-return',
  imports: [RouterLink, DatePipe],
  templateUrl: './details-asset-return.html',
  styleUrl: './details-asset-return.scss',
})
export class DetailsAssetReturn {
  private readonly router = inject(Router);
  private readonly session = inject(SessionService);
  private readonly handoff = inject(CommonDetailHandoffService);
  private readonly api = inject(DetailsAssetReturnService);

  detail = signal<DetailsAssetReturnModel>({ item: [] });

  constructor() {
    const cookieObject = this.session.current;
    if (cookieObject == null) {
      this.router.navigate(['/accessDenied'], { queryParams: { reason: 'notLoggedIn' } });
      return;
    }
    const unitId = this.session.unitId ?? 0;
    const organizationId = this.session.organizationId ?? 0;
    const id = this.handoff.getDataId();

    this.api.getDetail(unitId, organizationId, id).subscribe({
      next: (res) => this.detail.set(res),
    });
  }
}
