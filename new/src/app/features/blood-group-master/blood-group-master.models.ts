/**
 * Field names/types read directly from `BloodGroupMasterDto.java`
 * (legacy/Global/src/main/java/com/param/bloodBank/dto/BloodGroupMasterDto.java)
 * and `bloodGroupMasterController.js` -- not guessed.
 *
 * Legacy's own form object is `$scope.common` (ctrl:40-45):
 * `{ bloodGroupId:"", bloodGroupCode:"", bloodGroupDesc:"" }`. This screen
 * is the blood-TYPE classification lookup itself (A+/B-/O+ etc.), not a
 * transfusion/donor-safety workflow -- confirmed PURE-LOOKUP, no Step 6b
 * required (see this screen's `.ts` class doc comment for the full Step 0
 * writeup).
 */
export interface BloodGroupForm {
  bloodGroupId?: number;
  bloodGroupCode: string;
  bloodGroupDesc: string;
}

export function emptyBloodGroupForm(): BloodGroupForm {
  return { bloodGroupId: undefined, bloodGroupCode: '', bloodGroupDesc: '' };
}

/** Shape of one row in `getBloodGroupMasterList`'s `listObject`
 * (`GET_BLOOD_GROUP_MASTER_LIST` named query, `BloodGroupMaster.java:16-24`)
 * -- `status` is a legacy 'A'/'I' character column (NOT a real boolean, per
 * the view's own `ng-true-value="'A'"`/`ng-false-value="'I'"`, view:123),
 * unlike `storeTypeMaster`'s real `is_active` boolean column. */
export interface BloodGroupRow {
  bloodGroupId: number;
  bloodGroupCode: string;
  bloodGroupDesc: string;
  organizationId?: number;
  unitId?: number;
  status: 'A' | 'I';
}

/** Shape of `getBloodGroupMasterById`'s `listObject[0]`
 * (`GET_BLOOD_GROUP_MASTER_BY_ID` named query, same 6 fields as the list
 * row). */
export type BloodGroupDetail = BloodGroupRow;

/** POST body for `saveBloodGroupMaster` (ctrl:146-219, payload at
 * ctrl:171-183). Legacy sends `status:'A'` unconditionally on every create
 * (ctrl:179) -- not something the user chooses, reproduced as a literal
 * here too. `createdDate`/`updatedDate` are both the SAME client-computed
 * timestamp (`moment(date).format('DD-MM-YYYY HH:mm:ss')`, ctrl:168-169),
 * not two independent calls. */
export interface BloodGroupSavePayload {
  bloodGroupCode: string;
  bloodGroupDesc: string;
  organizationId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
  status: 'A';
  createdDate: string;
  updatedDate: string;
}

/** POST body for `updateBloodGroupMaster` (ctrl:257-320, payload at
 * ctrl:280-289). No `createdBy`/`createdDate`/`status` on the wire --
 * `BloodGroupMasterDaoImpl.updateBloodGroupMaster` never reads or writes
 * those 3 columns on update (only code/desc/org/unit/updatedBy/
 * updatedDate), matching the DTO fields legacy actually sends here. */
export interface BloodGroupUpdatePayload {
  bloodGroupId: number;
  bloodGroupCode: string;
  bloodGroupDesc: string;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `updateBloodGroupMasterStatus` (ctrl:322-358, payload
 * built directly on top of whatever `$scope.common` last held --
 * see this screen's `.ts` class doc comment for why this is harmless in
 * practice: the DAO's `updateBloodGroupMasterStatus` only ever reads
 * `bloodGroupId`/`status`/`updatedBy`/`updatedDate` off the DTO, so the
 * rebuild sends exactly those 4 fields and nothing else. */
export interface BloodGroupStatusPayload {
  bloodGroupId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}
