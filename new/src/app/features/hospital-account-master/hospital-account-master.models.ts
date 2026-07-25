export type ActiveStatus = 'A' | 'I';

/**
 * Field names/types read directly from `HospitalAccountMasterDto.java`
 * (legacy/Billing/.../global/dto/HospitalAccountMasterDto.java) and the
 * entity (legacy/Entity/.../global/model/HospitalAccountMaster.java) -- not
 * guessed. `orgnizationId` is legacy's own literal spelling (missing the
 * "a" in "organization"), kept verbatim since it's the real wire field, not
 * a local typo to "fix".
 */

/** Row shape returned by `GET_HOSPITAL_ACCOUNT_MASTER_LIST`
 * (HospitalAccountMaster.java:63-74, the `getHospitalAccountMasterList`
 * endpoint). `isDefault` is a legacy 'Y'/'N' character column (NOT a real
 * boolean) -- rendered as a raw `{{info.isDefault}}` literal in legacy's own
 * table cell (view:159), not a checkbox/icon; reproduced the same way here. */
export interface HospitalAccountRow {
  hospitalAccountId: number;
  hospitalAccountCode: string;
  hospitalAccountDesc: string;
  isDefault: 'Y' | 'N';
  status: ActiveStatus;
  orgnizationId?: number;
  unitId?: number;
}

/** Shape returned by `GET_HOSPITAL_ACCOUNT_MASTER_LIST_BY_ID`
 * (`getHospitalAccountMasterById`, `showUpdateBtn`'s GET, ctrl:214-242) --
 * assigned wholesale onto `$scope.common` in legacy, reproduced the same
 * way via `form.set(...)` in the component. */
export type HospitalAccountDetail = HospitalAccountRow;

/** Editable form state -- mirrors `$scope.common`
 * (`initHospitalAccountMaster`, ctrl:42-49). */
export interface HospitalAccountForm {
  hospitalAccountId?: number;
  hospitalAccountCode: string;
  hospitalAccountDesc: string;
  isDefault: 'Y' | 'N';
}

export function emptyHospitalAccountForm(): HospitalAccountForm {
  return {
    hospitalAccountId: undefined,
    hospitalAccountCode: '',
    hospitalAccountDesc: '',
    isDefault: 'N',
  };
}

/** POST body for `saveHospitalAccountMaster` (ctrl:152-212, the CREATE
 * branch of `save()`). Legacy's own payload also includes
 * `hospitalAccountId: $scope.common.hospitalAccountId` (always `""` on
 * create, from `initHospitalAccountMaster()`) -- deliberately OMITTED here:
 * `HospitalAccountMasterDaoImpl.saveHospitalAccountMaster` (:67-90) builds a
 * brand-new `HospitalAccountMaster` entity field-by-field and never calls
 * `.setHospitalAccountId(...)` on it at all, so whatever the client sends
 * for this field is inert -- Hibernate always assigns a fresh id from
 * `billing.hospital_accounts_seq` regardless. `status:'A'` IS read by the
 * create path (`HospitalAccountMasterDaoImpl.java:76`), so every created
 * row really is Active by construction -- sent as a hardcoded literal here
 * too, matching legacy, not exposed as a user-editable field (legacy's view
 * has no status control on the create form either). */
export interface HospitalAccountSavePayload {
  hospitalAccountCode: string;
  hospitalAccountDesc: string;
  isDefault: 'Y' | 'N';
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  status: 'A';
  orgnizationId: number;
  unitId: number;
  updatedDate: string;
}

/** POST body for `updateHospitalAccountMaster` (ctrl:245-294).
 *
 * **Faithfully-reproduced dead field:** legacy's own `update()` ALSO
 * hardcodes `status:'A'` into this payload (ctrl:260) -- but
 * `HospitalAccountMasterDaoImpl.updateHospitalAccountMaster` (:152-174)
 * loads the existing row and sets Code/Description/isDefault/org/unit/
 * updatedBy/updatedDate onto it, and never once calls `.setStatus(...)` --
 * confirmed by reading the DAO method directly, line by line. This field is
 * therefore silently inert on the update path: editing an Inactive row's
 * Code/Description can NEVER silently reactivate it through this endpoint,
 * despite the client always sending `status:'A'` on every update. Kept in
 * the payload verbatim (not omitted) to match legacy's real wire traffic
 * byte-for-byte -- this comment is the record of why it's safe to leave in.
 * No `createdBy`/`createdDate` -- update never sends them, matching legacy. */
export interface HospitalAccountUpdatePayload {
  hospitalAccountId: number;
  hospitalAccountCode: string;
  hospitalAccountDesc: string;
  isDefault: 'Y' | 'N';
  updatedBy: number;
  status: 'A';
  orgnizationId: number;
  unitId: number;
  updatedDate: string;
}

/** POST body for `updateHospitalAccountMasterStatus` (ctrl:296-331). Unlike
 * several sibling screens in this batch, this one DOES carry both
 * `orgnizationId` and `unitId` on the status payload (ctrl:301-308) -- kept
 * verbatim, even though `HospitalAccountMasterDaoImpl.
 * updateHospitalAccountMasterStatus` (:176-193) looks the row up by
 * `hospitalAccountId` alone with no org/unit predicate (the same
 * already-disclosed, project-wide zero-backend-authorization gap noted on
 * other master screens in this batch, not a new finding specific to this
 * one). */
export interface HospitalAccountStatusPayload {
  hospitalAccountId: number;
  status: ActiveStatus;
  updatedDate: string;
  updatedBy: number;
  orgnizationId: number;
  unitId: number;
}
