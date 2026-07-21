export type OccupationStatus = 'A' | 'I';

/**
 * Row shape returned by getOccupationMasterList (ctrl:105-116,
 * `GETOCCUPATIONLIST`). This is the shape the *backend actually returns and
 * the controller actually stores* (`$scope.commonList`) -- it is NOT the
 * shape the legacy view's table ever reads. See the component's class doc
 * comment for why the fetched rows are never rendered.
 */
export interface OccupationRow {
  occupationId: number;
  occupationCode: string;
  occupationName: string;
  status: OccupationStatus;
}

/**
 * Shape returned by getOccupationMasterById (ctrl:268-281,
 * `GETSINGLEOCCUPATION`) -- a different field vocabulary (`id`/`code`/
 * `desc`) than the list endpoint's DTO above. Reachability caveat: identical
 * to marital-status-master's -- `showUpdateBtn` (ctrl:259-283) is only
 * invoked from inside `ng-repeat="info in occupationList"` (view:79), a
 * variable never assigned anywhere in this controller, so no row ever
 * renders and this endpoint is never actually called by any real user
 * action today.
 */
export interface OccupationDetail {
  id: number;
  code: string;
  desc: string;
}

/** Mirrors legacy's `$scope.common` (ctrl:58). NOTE: the legacy VIEW never
 * binds its form inputs to `common.*` -- it binds to
 * `occupation.occupationCode`/`occupation.occupationName` (view:16/30), a
 * completely different scope object. `$scope.common.code`/`.desc` are
 * therefore always `undefined` in every real save/update payload legacy
 * ever sends -- see the component's class doc comment. */
export interface OccupationForm {
  occupationId?: number;
  occupationCode: string;
  occupationName: string;
}

export function emptyOccupationForm(): OccupationForm {
  return {
    occupationId: undefined,
    occupationCode: '',
    occupationName: '',
  };
}

/** POST body for saveOccupationMaster (ctrl:208-217, `ADDOCCUPATION`).
 * Reachability caveat: `$scope.save` is real, working code (ctrl:196-256),
 * but the view's Save button calls `saveOccupation()` (view:48) -- never
 * defined anywhere in this controller -- so `$scope.save` is unreachable
 * from any button click in the real, deployed screen. */
export interface OccupationSavePayload {
  code: string;
  desc: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** POST body for updateOccupationMaster (ctrl:341-347, `UPDATEOCCUPATION`).
 * Confirmed divergence from marital-status-master's equivalent payload:
 * this one does NOT include a `status` field at all (marital's does,
 * hardcoded to `'A'`) -- a real, confirmed inconsistency between these two
 * otherwise near-identical sibling controllers. Reachability caveat: same
 * as the save payload -- `$scope.update` is real but the view's Update
 * button calls `updateOccupation()` (view:49), never defined. */
export interface OccupationUpdatePayload {
  id: number;
  code: string;
  desc: string;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

/** POST body for updateStatusForOccupation (ctrl:300-305,
 * `UPDATEOCCUPATIONSTATUS`). Reachable in principle
 * (`ng-change="updateStatus(...)"`, view:84) but only from inside the same
 * dead `ng-repeat="info in occupationList"` row template, so it never
 * actually fires either. */
export interface OccupationStatusPayload {
  id: number;
  status: OccupationStatus;
  updatedBy: number;
  updatedDate: string;
}
