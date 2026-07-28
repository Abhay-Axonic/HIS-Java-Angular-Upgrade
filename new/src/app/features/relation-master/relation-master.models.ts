export type RelationStatus = 'A' | 'I';

/** Row shape the REAL, reachable `getRelationMasterList` response actually
 * uses. **Correction, 2026-07-28:** the real, live screen (reached via the
 * sidebar's `/commonMaster/relationmaster` dispatcher route, NOT the
 * unlinked dedicated `/relationMaster` state this file previously modeled)
 * renders its table from `common-master-screen.html`'s
 * `ng-repeat="info in commonList"`, which reads generic `info.id`/`info.code`/
 * `info.desc`/`info.status` -- exactly what `$scope.commonList` (the real
 * list response) actually contains. See relation-master.ts's class doc
 * comment for the full correction writeup. */
export interface RelationRow {
  id: number;
  code: string;
  desc: string;
  status: RelationStatus;
}

/** Form model. **Correction, 2026-07-28:** matches `$scope.common` (the
 * object the controller's real `save()`/`update()`/`showUpdateBtn()`
 * actually read from/write to, ctrl:207-217/340-347/275-277) -- not the
 * unreachable dedicated view's separate `relation.*` binding this file
 * previously (wrongly) modeled. */
export interface RelationForm {
  id?: number;
  code: string;
  desc: string;
}

export function emptyRelationForm(): RelationForm {
  return {
    id: undefined,
    code: '',
    desc: '',
  };
}

/** POST body for updateStatusForRelation (ctrl:291-304, `updateStatus` --
 * wired to the real table's status toggle, `ng-change="updateStatus(info.id,info.status)"`).
 * No `organizationId` sent -- confirmed via ctrl:299-304. */
export interface RelationStatusPayload {
  id: number;
  status: RelationStatus;
  updatedBy: number;
  updatedDate: string;
}

/** Response shape of getRelationById (ctrl:264-281, `GETSINGLERELATION`,
 * `showUpdateBtn` -- wired to the real table's edit pencil,
 * `ng-click="showUpdateBtn(info.id)"`). Generic `id`/`code`/`desc` field
 * names, confirmed live 2026-07-28 (real edit-fetch round trip against a
 * real created row). */
export interface RelationDetail {
  id: number;
  code: string;
  desc: string;
}
