export type MaritalStatus = 'A' | 'I';

/**
 * Row shape the REAL, reachable `getMaritalStatusMasterList` response
 * actually uses. **Correction, 2026-07-28:** the real, live screen (reached
 * via the sidebar's `/commonMaster/maritalstatus` dispatcher route, NOT the
 * unlinked dedicated `/maritalstatusMaster` state this file previously
 * modeled) renders its table from `common-master-screen.html`'s
 * `ng-repeat="info in commonList"`, which reads generic `info.id`/`info.code`/
 * `info.desc`/`info.status` -- exactly what `$scope.commonList` (the real
 * list response) actually contains. This screen has 2 genuine pre-existing
 * rows in this dev DB ("MRD"/Married, "SNGL"/Single, both Active) -- direct
 * evidence the real screen has worked before. See marital-status-master.ts's
 * class doc comment for the full correction writeup.
 */
export interface MaritalStatusRow {
  id: number;
  code: string;
  desc: string;
  status: MaritalStatus;
}

/**
 * Shape returned by getMaritalById (ctrl:270-283, `GETSINGLEMARITALSTATUS`).
 */
export interface MaritalStatusDetail {
  id: number;
  code: string;
  desc: string;
}

/** Form model. **Correction, 2026-07-28:** matches `$scope.common` (the
 * object the controller's real `save()`/`update()`/`showUpdateBtn()`
 * actually read from/write to, ctrl:210-219/343-351/275-277) -- not the
 * unreachable dedicated view's separate `marital.*` binding this file
 * previously (wrongly) modeled. */
export interface MaritalStatusForm {
  id?: number;
  code: string;
  desc: string;
}

export function emptyMaritalStatusForm(): MaritalStatusForm {
  return {
    id: undefined,
    code: '',
    desc: '',
  };
}

/** POST body for saveMaritalStatusMaster (ctrl:210-219, `ADDMARITALSTATUS`). */
export interface MaritalStatusSavePayload {
  code: string;
  desc: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** POST body for updateMaritalStatusMaster (ctrl:343-351, `UPDATEMARITAL`).
 * Unlike Occupation's equivalent payload, this one DOES include `status:'A'`
 * -- a confirmed divergence between the two otherwise near-identical sibling
 * controllers (occupationMasterController.js's `update()` omits `status`
 * entirely), preserved here. */
export interface MaritalStatusUpdatePayload {
  id: number;
  code: string;
  desc: string;
  status: 'A';
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

/** POST body for updateStatusForMaritalStatus (ctrl:302-307,
 * `UPDATEMARITALSTATUS`). */
export interface MaritalStatusStatusPayload {
  id: number;
  status: MaritalStatus;
  updatedBy: number;
  updatedDate: string;
}
