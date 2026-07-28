export type ICUTypeStatus = 'A' | 'I';

/** Tri-state mirror of `$scope.icuTypeObj.isOpen` (icuTypeController.js:48-51,
 * 216-226, 385-395) -- the two radio buttons (view:53-62) share one
 * `ng-model` bound to `'open'`/`'close'` string values via `ng-value`.
 * `'N'` is the neutral/unselected default (neither radio checked) -- legacy
 * initializes `icuTypeObj = { isOpen: 'N', isClose: 'N' }` and there is no
 * `required` on either radio input, so a record CAN be saved with neither
 * Open nor Closed picked (both `isOpenICU`/`isCloseICU` end up `'N'`). */
export type ICUOpenSelection = 'N' | 'open' | 'close';

/** Row shape returned by `getICUTypeList`/`getICUById` (`GET_ICU_LIST`/
 * `GET_ICU_LIST_BY_ID` named queries, ICUTypeMaster.java:26-51). Field
 * names confirmed against the legacy controller's own reads
 * (icuTypeController.js:307-309/193-196: `response.data.listObject[0]
 * .icutypeMasterId/.icutypeCode/.icutype`, `info.isOpenICU`/`info.isCloseICU`)
 * -- Jackson's default (non-standard-bean) getter-name mangling on this
 * DTO's `getICUType()`/`getICUtypeCode()`/`getICUTypeMasterId()` produces
 * these exact lowercase-led names, not the DTO's own field casing. Live-
 * verified against real, unmodified legacy at localhost:8080 during this
 * screen's build (create/duplicate/update/status-toggle round trip). */
export interface ICUTypeRow {
  icutypeMasterId: number;
  icutypeCode: string;
  icutype: string;
  isOpenICU: 'Y' | 'N';
  isCloseICU: 'Y' | 'N';
  status: ICUTypeStatus;
}

/** Editable form state -- mirrors `$scope.icuTypeObj`. */
export interface ICUTypeForm {
  icuTypeId?: number;
  icuCode: string;
  icuType: string;
  isOpen: ICUOpenSelection;
}

export function emptyICUTypeForm(): ICUTypeForm {
  return { icuTypeId: undefined, icuCode: '', icuType: '', isOpen: 'N' };
}

/** POST body for `saveICUTypeMaster` (ctrl:201-281, `ADDICUTYPE`). */
export interface ICUTypeSavePayload {
  icutypeCode: string;
  icutype: string;
  isCloseICU: 'Y' | 'N';
  isOpenICU: 'Y' | 'N';
  status: 'A';
  organizationId: number;
  unitId: number;
  updatedBy: number;
  createdBy: number;
  createdDate: string;
  updatedDate: string;
}

/** POST body for `updateICUtypeMaster` (ctrl:375-447, `UPDATEICUTYPE`) --
 * no `status`/`createdBy`/`createdDate`, matching the legacy payload and
 * `ICUTypeMasterDaoImpl.updateICUTypeMaster` (which never touches status). */
export interface ICUTypeUpdatePayload {
  icutypeCode: string;
  icutype: string;
  isCloseICU: 'Y' | 'N';
  isOpenICU: 'Y' | 'N';
  icutypeMasterId: number;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `updateStatusForICU` (ctrl:332-372, `UPDATEICUTYPESTATUS`). */
export interface ICUTypeStatusPayload {
  icutypeMasterId: number;
  status: ICUTypeStatus;
  updatedBy: number;
  updatedDate: string;
}
