export type MaritalStatus = 'A' | 'I';

/**
 * Row shape returned by getMaritalStatusMasterList (ctrl:107-118,
 * `GETMARITALSTATUSLIST`). NOTE: this is the shape the *backend actually
 * returns and the controller actually stores* (`$scope.commonList`) -- it is
 * NOT the shape the legacy view's table ever reads. See the component's
 * class doc comment for why the fetched rows are never rendered.
 */
export interface MaritalStatusRow {
  maritalStatusId: number;
  maritalStatusCode: string;
  maritalStatusName: string;
  status: MaritalStatus;
}

/**
 * Shape returned by getMaritalById (ctrl:270-283, `GETSINGLEMARITALSTATUS`)
 * -- a different field vocabulary (`id`/`code`/`desc`) than the list
 * endpoint's DTO above. Legacy assigns this wholesale into `$scope.common`.
 * Reachability caveat: `showUpdateBtn` (ctrl:261-285) that calls this
 * endpoint is only invoked from the row-level edit pencil
 * (`ng-click="showUpdateBtn(info.maritalStatusId)"`, view:88), which lives
 * inside `ng-repeat="info in maritalList"` (view:79) -- a variable never
 * assigned anywhere in this controller. No row ever renders, so this
 * endpoint is never actually called by any real user action today. Kept
 * here and in the service purely for documentation completeness /
 * in case the view-controller wiring is ever fixed.
 */
export interface MaritalStatusDetail {
  id: number;
  code: string;
  desc: string;
}

/** Mirrors legacy's `$scope.common` (ctrl:59, `initMaster()`-equivalent
 * inline in `init()`). NOTE: the legacy VIEW never actually binds its form
 * inputs to `common.*` -- it binds to `marital.maritalCode`/
 * `marital.maritalName` (view:16/30), a completely different scope object.
 * `$scope.common.code`/`.desc` are therefore always `undefined` in every
 * real save/update payload legacy ever sends -- see the component's class
 * doc comment. */
export interface MaritalStatusForm {
  maritalId?: number;
  maritalCode: string;
  maritalName: string;
}

export function emptyMaritalStatusForm(): MaritalStatusForm {
  return {
    maritalId: undefined,
    maritalCode: '',
    maritalName: '',
  };
}

/** POST body for saveMaritalStatusMaster (ctrl:210-219, `ADDMARITALSTATUS`).
 * Reachability caveat: `$scope.save` is real, working code (ctrl:197-258),
 * but the view's Save button calls `saveMarital()` (view:48) -- a function
 * never defined anywhere in this controller -- so `$scope.save` itself is
 * unreachable from any button click in the real, deployed screen. See class
 * doc comment. */
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
 * entirely). Reachability caveat: same as the save payload above --
 * `$scope.update` is real but the view's Update button calls
 * `updateMarital()` (view:49), never defined. */
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
 * `UPDATEMARITALSTATUS`). This one IS reachable in principle
 * (`ng-change="updateStatus(...)"`, view:84) -- but only from inside the
 * same dead `ng-repeat="info in maritalList"` row template, so it never
 * actually fires either, for the same reason. */
export interface MaritalStatusStatusPayload {
  id: number;
  status: MaritalStatus;
  updatedBy: number;
  updatedDate: string;
}
