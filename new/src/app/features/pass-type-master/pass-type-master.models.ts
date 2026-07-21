export type PassTypeStatus = 'A' | 'I';

/** Row shape returned by getPassTypeMasterList (ctrl:94-101) -- the only
 * fields the legacy view renders (pass-Type-Master.html:114-127). */
export interface PassTypeRow {
  visitorPassTypeId: number;
  visitorPassTypeCode: string;
  visitorPassTypeDesc: string;
  status: PassTypeStatus;
}

/** Shape returned by getPassTypeMasterById (ctrl:239-252) -- assigned
 * wholesale to `$scope.visitorType` off `listObject[0]`, matching legacy. */
export interface PassTypeDetail {
  visitorPassTypeId: number;
  visitorPassTypeCode: string;
  visitorPassTypeDesc: string;
}

/** Editable form state -- mirrors legacy's `$scope.visitorType`
 * (ctrl:54-60, `init()`). */
export interface PassTypeForm {
  visitorPassTypeId?: number;
  visitorPassTypeCode: string;
  visitorPassTypeDesc: string;
}

/** Matches ctrl:54-60's `init()` reset shape exactly
 * (`visitorPassTypeCode:"", visitorPassTypeDesc:"", visitorPassTypeId:""`). */
export function emptyPassTypeForm(): PassTypeForm {
  return {
    visitorPassTypeId: undefined,
    visitorPassTypeCode: '',
    visitorPassTypeDesc: '',
  };
}

/** POST body for savePassTypeMaster (ctrl:182-198). Note: legacy computes a
 * `moment(...)`-formatted `$scope.currentDate` (ctrl:176-178) but never
 * assigns it onto `visitorType` before posting -- a harmless dead local, not
 * reproduced as a payload field here (see class doc comment). */
export interface PassTypeSavePayload {
  visitorPassTypeCode: string;
  visitorPassTypeDesc: string;
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  unitId: number;
}

/** POST body for updatePassTypeMaster (ctrl:300-307) -- whatever is
 * currently in `visitorType` (id/code/desc, from form or the by-id fetch)
 * plus these three fields patched on top. */
export interface PassTypeUpdatePayload {
  visitorPassTypeId: number;
  visitorPassTypeCode: string;
  visitorPassTypeDesc: string;
  organizationId: number;
  unitId: number;
  updatedBy: number;
}

/** POST body for updatePassTypeMasterStatus (ctrl:263-268) -- the one
 * payload that DOES carry a real `moment(...)`-formatted date. */
export interface PassTypeStatusPayload {
  visitorPassTypeId: number;
  status: PassTypeStatus;
  updatedBy: number;
  updatedDate: string;
}
