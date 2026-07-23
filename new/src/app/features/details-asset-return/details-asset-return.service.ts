import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { DetailsAssetReturn } from './details-asset-return.models';

/**
 * `CONSTANTS.ASSET.ASSET_RETURN.DETAIL_API` (constants.js:1300) + id --
 * same endpoint family as `../asset-return/asset-return.service.ts` (this
 * screen's list-view counterpart): `GET
 * /api/asset/management/asset/return/{unitId}/{orgId}/{id}`, confirmed
 * against `AssetReturnController.java`'s `getAssetReturnDetails` mapping
 * (`@GetMapping("/{unitId}/{orgId}/{id}")`).
 */
@Injectable({ providedIn: 'root' })
export class DetailsAssetReturnService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getDetail(unitId: number, organizationId: number, id: number | string): Observable<DetailsAssetReturn> {
    return this.http.get<DetailsAssetReturn>(
      `${this.base}/api/asset/management/asset/return/${unitId}/${organizationId}/${id}`,
    );
  }
}
