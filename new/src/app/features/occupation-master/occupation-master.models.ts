export type OccupationStatus = 'A' | 'I';

/**
 * Row shape the REAL, reachable `getOccupationMasterList` response actually
 * uses. **Correction, 2026-07-28:** the real, live screen (reached via the
 * sidebar's `/commonMaster/occupation` dispatcher route, NOT the unlinked
 * dedicated `/occupationMaster` state this file previously modeled) renders
 * its table from `common-master-screen.html`'s `ng-repeat="info in commonList"`,
 * which reads generic `info.id`/`info.code`/`info.desc`/`info.status` --
 * exactly what `$scope.commonList` (the real list response) actually
 * contains. See occupation-master.ts's class doc comment for the full
 * correction writeup.
 */
export interface OccupationRow {
  id: number;
  code: string;
  desc: string;
  status: OccupationStatus;
}

/**
 * Shape returned by getOccupationMasterById (ctrl:264-281,
 * `GETSINGLEOCCUPATION`). Confirmed live 2026-07-28 (real edit-fetch round
 * trip against a real created row).
 */
export interface OccupationDetail {
  id: number;
  code: string;
  desc: string;
}

/** Form model. **Correction, 2026-07-28:** matches `$scope.common` (the
 * object the controller's real `save()`/`update()`/`showUpdateBtn()`
 * actually read from/write to, ctrl:209-217/343-347/275-277) -- not the
 * unreachable dedicated view's separate `occupation.*` binding this file
 * previously (wrongly) modeled. */
export interface OccupationForm {
  id?: number;
  code: string;
  desc: string;
}

export function emptyOccupationForm(): OccupationForm {
  return {
    id: undefined,
    code: '',
    desc: '',
  };
}

/** POST body for saveOccupationMaster (ctrl:208-217, `ADDOCCUPATION`).
 * Confirmed live 2026-07-28: real save, real persistence, via the actual
 * sidebar-linked route. */
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
 * otherwise near-identical sibling controllers, preserved here. Confirmed
 * live 2026-07-28: real update, real persistence. */
export interface OccupationUpdatePayload {
  id: number;
  code: string;
  desc: string;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

/** POST body for updateStatusForOccupation (ctrl:300-305,
 * `UPDATEOCCUPATIONSTATUS`). Confirmed live 2026-07-28: real status toggle. */
export interface OccupationStatusPayload {
  id: number;
  status: OccupationStatus;
  updatedBy: number;
  updatedDate: string;
}
