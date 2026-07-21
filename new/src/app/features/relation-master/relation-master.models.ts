export type RelationStatus = 'A' | 'I';

/** Row shape the REAL `getRelationMasterList` backend response actually
 * returns (list POST fires for real -- confirmed live this session, HTTP
 * 200, `status:"success"`, `object:0` since this dev DB currently holds
 * zero relation rows). Legacy assigns this response to `$scope.commonList`
 * (ctrl:105-116) -- NOT to `$scope.relationList`, the name the view's own
 * `ng-repeat="info in relationList"` (relation-master.html:79) actually
 * reads. `$scope.relationList` is never assigned anywhere in
 * relationMasterController.js (confirmed via full-file grep) -- so this
 * table renders zero rows forever in legacy, regardless of what the
 * backend returns. See relation-master.ts's class doc comment for the
 * full severity-1 writeup; this interface exists to type the real (but
 * never-displayed) fetch response faithfully. */
export interface RelationRow {
  relationId: number;
  relationCode: string;
  relationName: string;
  status: RelationStatus;
}

/** Mirrors the view's OWN form model, `relation.*`
 * (relation-master.html:15-17/30, `ng-model="relation.relationCode"` /
 * `"relation.relationName"` / hidden `"relation.relationId"`). Note this is
 * a SEPARATE object from `$scope.common` -- the object the controller's
 * orphaned `save()`/`update()`/`showUpdateBtn()` actually read from/write
 * to (ctrl:207-283, `common.code`/`common.desc`/`common.incremetedId`).
 * Nothing in legacy ever copies between `relation.*` and `common.*` -- see
 * class doc comment. */
export interface RelationForm {
  relationId?: number;
  relationCode: string;
  relationName: string;
}

export function emptyRelationForm(): RelationForm {
  return {
    relationId: undefined,
    relationCode: '',
    relationName: '',
  };
}

/** POST body for updateStatusForRelation (ctrl:298-304, `updateStatus` --
 * the ONE piece of CRUD in this controller that IS correctly named/wired
 * to the view, `ng-change="updateStatus(info.relationId,info.status)"`,
 * relation-master.html:84). Note: unlike district-master's equivalent, no
 * `organizationId` is sent here at all -- confirmed via ctrl:299-304. */
export interface RelationStatusPayload {
  id: number;
  status: RelationStatus;
  updatedBy: number;
  updatedDate: string;
}

/** Response shape of getRelationById (ctrl:264-281, `GETSINGLERELATION`,
 * `showUpdateBtn` -- also correctly named/wired to the view's edit pencil,
 * `ng-click="showUpdateBtn(info.relationId)"`). Legacy reads
 * `response.data.listObject[0].id`/`.code`/`.desc` -- generic field names,
 * NOT `relationId`/`relationCode`/`relationName` -- yet more of the same
 * un-renamed copy-paste residue documented in the class doc comment. Never
 * exercised live (no relation rows exist in this dev DB, and the
 * `relationList` display bug means the edit pencil never renders in the
 * DOM to click), so this shape is transcribed from the controller's own
 * assumptions, not confirmed against a real response body. */
export interface RelationDetail {
  id: number;
  code: string;
  desc: string;
}
