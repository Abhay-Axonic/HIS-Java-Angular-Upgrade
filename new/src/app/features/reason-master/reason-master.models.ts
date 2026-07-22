export type ActiveStatus = 'A' | 'I';

/** "Reason Type" dropdown option (ctrl:70-87, POST
 * `/api/common/getReasonTypeMasterList` -- same endpoint reasonTypeMaster's
 * own list uses; only `reasonTypeId`/`reasonTypeDesc` are read here). */
export interface ReasonTypeOption {
  reasonTypeId: number;
  reasonTypeDesc: string;
}

/** Row shape returned by GETREASONMASTERLIST (`/api/common/getReasonMasterList`),
 * aliased from `GET_REASONMASTER_LIST`
 * (legacy/Entity/.../adt/master/global/model/ReasonMaster.java:37-46) --
 * joined against ReasonTypeMaster so `reasonTypeDesc` comes along with each
 * row. */
export interface ReasonRow {
  reasonMasterId: number;
  reasonTypeId: number;
  reasonTypeDesc: string;
  reasonCode: string;
  reasonDesc: string;
  status: ActiveStatus;
}

/** Shape returned by GETREASONMASTERBYID (ctrl:276-303) --
 * `response.data.listObject[0]`, assigned field-by-field into
 * `$scope.reasonMasterObj`. */
export interface ReasonDetail {
  reasonMasterId: number;
  reasonCode: string;
  reasonDesc: string;
  reasonTypeId: number;
}

/** Editable form state -- mirrors `$scope.reasonMasterObj` (an empty
 * object literal on init, ctrl:46 -- every field starts genuinely empty).
 * `reasonTypeId` is kept as a string, matching legacy's own `.toString()`
 * on edit (ctrl:296) and the view's raw `value="{{info.reasonTypeId}}"`
 * option binding. */
export interface ReasonForm {
  reasonMasterId?: number;
  reasonTypeId: string;
  reasonCode: string;
  reasonDescription: string;
}

export function emptyReasonForm(): ReasonForm {
  return { reasonMasterId: undefined, reasonTypeId: '', reasonCode: '', reasonDescription: '' };
}

/** POST body for SAVEREASONMASTER (ctrl:213-273). */
export interface ReasonSavePayload {
  reasonCode: string;
  reasonDesc: string;
  reasonTypeId: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** POST body for UPDATEREASONMASTER (ctrl:349-400) -- no `status` field,
 * matching legacy exactly (create-only). */
export interface ReasonUpdatePayload {
  reasonMasterId: number;
  reasonCode: string;
  reasonDesc: string;
  reasonTypeId: string;
  organizationId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for UPDATEREASONMASTERSTATUS (ctrl:306-346). */
export interface ReasonStatusPayload {
  reasonMasterId: number;
  status: ActiveStatus;
  updatedBy: number;
  updatedDate: string;
}
