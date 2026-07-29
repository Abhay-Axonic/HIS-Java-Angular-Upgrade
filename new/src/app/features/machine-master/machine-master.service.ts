import { Injectable, inject } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { API_BASE_URL } from '../../core/api-config';
import { ApiResponse } from '../../core/api-response';
import { MachineMasterRecord } from './machine-master.models';

/**
 * Endpoint paths taken directly from
 * resources/assets/js/config/lis.url.config.js (LIS_GLOBAL = "/LIS/global")
 * and confirmed live against
 * legacy/LIS/src/main/java/com/param/lis/unit/controller/MachineMasterController.java
 * (@RequestMapping("/LIS/global")):
 *  - listMachineMaster/{orgId}/{unitId}/{offset}/{recordPerPage}  (GET)
 *  - getTotalMachineMasterRecord/{orgId}/{unitId}                 (GET)
 *  - addMachine                                                   (POST)
 *  - getMachinebyId/{orgId}/{unitId}/{machineId}                  (GET)
 *  - updateMachineMaster                                          (PUT)
 *  - activeInactiveMachine/{orgId}/{machineId}/{status}           (GET)
 *
 * **Deliberately NOT used for pagination past page 1: `/LIS/global/listHeaderMaster`.**
 * See the class-level comment in machine-master.ts for the full writeup --
 * legacy's own `getMachineMasterList()` (ctrl:116-135), called from every
 * `setPage(page, true)` pagination click, calls this endpoint instead of
 * `listMachineMaster`. `listHeaderMaster` is `HeaderMasterController`'s own
 * endpoint (legacy/LIS/.../controller/HeaderMasterController.java:93-109),
 * backed by the completely unrelated `lab.m_header_master` table via the
 * `GET_PAGINATED_HEADER_MASTER_LIST` named query
 * (legacy/Entity/.../HeaderMaster.java:97-109) -- a real, confirmed-live,
 * cross-entity data bug in legacy today, corrected here (this service only
 * ever calls the real `listMachineMaster` endpoint) per this project's
 * standing silent-bug-correction policy for bugs that cascade into the
 * write path (see class comment for why edit/toggle break on the
 * mis-fetched rows).
 */
@Injectable({ providedIn: 'root' })
export class MachineMasterService {
  private readonly http = inject(HttpClient);
  private readonly base = inject(API_BASE_URL);

  getList(
    organisationId: number,
    unitId: number,
    offset: number,
    recordPerPage: number,
  ): Observable<ApiResponse<never, MachineMasterRecord>> {
    return this.http.get<ApiResponse<never, MachineMasterRecord>>(
      `${this.base}/LIS/global/listMachineMaster/${organisationId}/${unitId}/${offset}/${recordPerPage}`,
    );
  }

  getCount(organisationId: number, unitId: number): Observable<ApiResponse<number>> {
    return this.http.get<ApiResponse<number>>(
      `${this.base}/LIS/global/getTotalMachineMasterRecord/${organisationId}/${unitId}`,
    );
  }

  getById(organisationId: number, unitId: number, machineId: number): Observable<ApiResponse<MachineMasterRecord>> {
    return this.http.get<ApiResponse<MachineMasterRecord>>(
      `${this.base}/LIS/global/getMachinebyId/${organisationId}/${unitId}/${machineId}`,
    );
  }

  create(payload: MachineMasterRecord): Observable<ApiResponse<MachineMasterRecord>> {
    return this.http.post<ApiResponse<MachineMasterRecord>>(`${this.base}/LIS/global/addMachine`, payload);
  }

  update(payload: MachineMasterRecord): Observable<ApiResponse<MachineMasterRecord>> {
    return this.http.put<ApiResponse<MachineMasterRecord>>(`${this.base}/LIS/global/updateMachineMaster`, payload);
  }

  changeStatus(organisationId: number, machineId: number, status: 'A' | 'I'): Observable<ApiResponse> {
    return this.http.get<ApiResponse>(
      `${this.base}/LIS/global/activeInactiveMachine/${organisationId}/${machineId}/${status}`,
    );
  }
}
