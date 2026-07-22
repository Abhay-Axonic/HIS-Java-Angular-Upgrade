export type ActiveStatus = 'A' | 'I';

/** Row shape returned by GETREASONTYPEMASTERLIST (`/api/common/getReasonTypeMasterList`),
 * aliased from `GET_REASON_TYPE_MASTER_LIST`
 * (legacy/Entity/.../org/common/model/ReasonTypeMaster.java:23-29). */
export interface ReasonTypeRow {
  reasonTypeId: number;
  reasonTypeCode: string;
  reasonTypeDesc: string;
  status: ActiveStatus;
}

/** Shape returned by GETREASONTYPEMASTERBYID (ctrl:253-278) --
 * `response.data.listObject[0]`, assigned field-by-field into
 * `$scope.reasonMasterObj`, same field names as the list row. */
export type ReasonTypeDetail = ReasonTypeRow;

/** Editable form state -- mirrors `$scope.reasonMasterObj` (an empty object
 * literal on init, ctrl:57 -- every field starts genuinely empty, not
 * defaulted). */
export interface ReasonTypeForm {
  reasonTypeId?: number;
  reasonTypeCode: string;
  reasonTypeDesc: string;
}

export function emptyReasonTypeForm(): ReasonTypeForm {
  return { reasonTypeId: undefined, reasonTypeCode: '', reasonTypeDesc: '' };
}

/** POST body for SAVEREASONTYPEMASTER (ctrl:193-250). */
export interface ReasonTypeSavePayload {
  reasonTypeCode: string;
  reasonTypeDesc: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** POST body for UPDATEREASONTYPEMASTER (ctrl:321-368) -- no `status`
 * field, matching legacy exactly (create-only). */
export interface ReasonTypeUpdatePayload {
  reasonTypeId: number;
  reasonTypeCode: string;
  reasonTypeDesc: string;
  organizationId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for UPDATEREASONTYPEMASTERSTATUS (ctrl:280-318). */
export interface ReasonTypeStatusPayload {
  reasonTypeId: number;
  status: ActiveStatus;
  updatedBy: number;
  updatedDate: string;
}
