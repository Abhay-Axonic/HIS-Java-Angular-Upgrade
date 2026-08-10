import { inject, Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import {
  NursingOrderMasterRow,
  NursingOrderSavePayload,
  ServiceMasterRow,
  SpecialityRow,
  SubSpecialityRow,
} from './nursing-order-master.models';

@Injectable({ providedIn: 'root' })
export class NursingOrderMasterService {
  private http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  list(organizationId: number, unitId: number): Observable<{ status: string; listObject: NursingOrderMasterRow[] }> {
    return this.http.post<{ status: string; listObject: NursingOrderMasterRow[] }>(
      `${this.base}/api/global/unit/getNursingOrderMasterList`,
      { organizationId, unitId }
    );
  }

  save(payload: NursingOrderSavePayload): Observable<{ status: string; code: number; message: string }> {
    return this.http.post<{ status: string; code: number; message: string }>(
      `${this.base}/api/global/unit/saveNursingOrderMaster`,
      payload
    );
  }

  specialities(organizationId: number, unitId: number): Observable<{ status: string; listObject: SpecialityRow[] }> {
    return this.http.post<{ status: string; listObject: SpecialityRow[] }>(
      `${this.base}/adt/getActiveSpecialityList`,
      { organizationId, unitId }
    );
  }

  subSpecialities(organizationId: number, unitId: number, specialityId: number): Observable<{ status: string; listObject: SubSpecialityRow[] }> {
    return this.http.post<{ status: string; listObject: SubSpecialityRow[] }>(
      `${this.base}/adt/getSubSpecialityBySpecialityArrayForTariff`,
      { organizationId, unitId, specialityIdList: [specialityId] }
    );
  }

  searchServices(organizationId: number, unitId: number, searchKeyword: string): Observable<{ status: string; listObject: ServiceMasterRow[] }> {
    return this.http.post<{ status: string; listObject: ServiceMasterRow[] }>(
      `${this.base}/global/serviceMaster`,
      { organizationId, unitId, searchKeyword }
    );
  }
}
