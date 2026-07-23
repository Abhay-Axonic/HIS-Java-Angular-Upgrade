export type ConsentTypeStatus = 'A' | 'I';
export type YesNo = 'Y' | 'N';

/** Row shape returned by `getConsentTypeMasterList`'s `listObject`
 * (consent-type-master.html:151-161) -- the only fields the legacy table
 * actually renders (code, description, status; `admissionConsent` is not a
 * table column in legacy, only present in the single-record
 * `getConsentTypeById` response used by Edit -- see `ConsentTypeDetail`). */
export interface ConsentTypeRow {
  consentTypeMasterId: number;
  consentTypeMasterCode: string;
  consentTypeMasterName: string;
  status: ConsentTypeStatus;
}

/** Shape returned by `getConsentTypeById`'s `listObject[0]`
 * (ctrl:270-298, `showUpdateBtn`), assigned field-by-field into
 * `$scope.consenttype`. */
export interface ConsentTypeDetail {
  consentTypeMasterId: number;
  consentTypeMasterCode: string;
  consentTypeMasterName: string;
  admissionConsent: YesNo;
}

/** Editable form state -- mirrors `$scope.consenttype` (ctrl:50-51:
 * `$scope.consenttype = {}; $scope.consenttype.admissionConsent = 'N';` --
 * every OTHER field starts genuinely undefined/empty, only
 * `admissionConsent` is explicitly defaulted). `admissionConsent` is a
 * plain independent boolean flag -- it does NOT gate the visibility of
 * `consentCode`/`consentName` or anything else on this screen, just a
 * persisted attribute (confirmed against the Step 0/1 audit). */
export interface ConsentTypeForm {
  consentTypeId?: number;
  consentCode: string;
  consentName: string;
  admissionConsent: YesNo;
}

export function emptyConsentTypeForm(): ConsentTypeForm {
  return { consentTypeId: undefined, consentCode: '', consentName: '', admissionConsent: 'N' };
}

/** POST body for `saveConsentTypeMaster` (ctrl:203-267). Wire field names
 * (`consentTypeMasterCode`/`consentTypeMasterName`) deliberately differ from
 * the form's own `consentCode`/`consentName` -- mapped explicitly in
 * `consent-type-master.ts`'s `doSave()`, not renamed away, since these are
 * the literal fields the real endpoint expects (ctrl:211-223). */
export interface ConsentTypeSavePayload {
  consentTypeMasterCode: string;
  consentTypeMasterName: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  unitId: number;
  updatedDate: string;
  createdDate: string;
  admissionConsent: YesNo;
}

/** POST body for `updateConsentTypeMaster` (ctrl:342-394) -- deliberately
 * has NO `status` field at all, matching legacy exactly: update never
 * touches status (only the separate status-toggle endpoint does). */
export interface ConsentTypeUpdatePayload {
  consentTypeMasterCode: string;
  consentTypeMasterName: string;
  consentTypeMasterId: number;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
  unitId: number;
  admissionConsent: YesNo;
}

/** POST body for `updateStatusForConsentType` (ctrl:301-339). */
export interface ConsentTypeStatusPayload {
  consentTypeMasterId: number;
  status: ConsentTypeStatus;
  updatedBy: number;
  updatedDate: string;
}
