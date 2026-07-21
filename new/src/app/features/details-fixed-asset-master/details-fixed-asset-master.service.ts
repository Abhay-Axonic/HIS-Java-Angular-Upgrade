import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { DfamDetail } from './details-fixed-asset-master.models';

@Injectable({ providedIn: 'root' })
export class DetailsFixedAssetMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getDetails(unitId: number, organizationId: number, itemId: number | string): Observable<DfamDetail> {
    return this.http.get<DfamDetail>(`${this.base}/api/asset/fixedAsset/addFixedAsset/${unitId}/${organizationId}/${itemId}`);
  }
}
