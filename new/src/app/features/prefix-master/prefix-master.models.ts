/**
 * Shapes taken directly from
 * resources/assets/js/controller/master/prefixMasterController.js -- not
 * guessed.
 */

export type PrefixStatus = 'A' | 'I';

/** Returned by GETACTIVEGENDERLIST (ctrl:76-90), rendered as the Gender
 * `<select>` (prefix-master.html:46-50, `info.id`/`info.desc`). */
export interface GenderOption {
  id: number;
  desc: string;
}

/** Row shape returned by GETPREFIXLIST (ctrl:120-130) -- the fields the
 * legacy table actually renders (prefix-master.html:179-190). */
export interface PrefixRow {
  prefixId: number;
  prefixCode: string;
  prefixDesc: string;
  genderName: string;
  status: PrefixStatus;
}

/** Shape returned by GETSINGLEPREFIX (ctrl:262-291), `listObject[0]`. Note
 * legacy's own field name for the gender FK on this DTO is `id` (ctrl:283),
 * not `genderId` -- kept verbatim, it's the real response contract. */
export interface PrefixDetail {
  prefixId: number;
  prefixCode: string;
  prefixDesc: string;
  id: number;
  isShowPatientRegistration: boolean;
}

/**
 * Editable form state -- mirrors legacy's `$scope.prefix` (ctrl:50) plus the
 * page-level `$scope.isShowPatientRegistration` (ctrl:47, NOT nested under
 * `prefix` in legacy -- kept flat here too for the same reason).
 *
 * Legacy naming quirk preserved verbatim: the view's second text field binds
 * `ng-model="prefix.prefixName"` (prefix-master.html:32) and holds the
 * *description* text (labelled "Prefix Master Description") -- `prefixName`
 * is copy-paste residue from some other master's field name, not a second,
 * separate name field. `savePrefix()`/`updatePrefix()` (ctrl:207-218,
 * 342-350) both read `$scope.prefix.prefixName` and send it as `prefixDesc`.
 * Reproduced here as `prefixName` on the form model, mapped to `prefixDesc`
 * only when building the save/update payload.
 */
export interface PrefixForm {
  prefixId?: number;
  prefixCode: string;
  prefixName: string;
  genderId: string;
  isShowPatientRegistration: boolean;
}

/** Matches ctrl:50/47's reset shape (`$scope.prefix = {}`,
 * `$scope.isShowPatientRegistration = false`). */
export function emptyPrefixForm(): PrefixForm {
  return {
    prefixId: undefined,
    prefixCode: '',
    prefixName: '',
    genderId: '',
    isShowPatientRegistration: false,
  };
}

/** POST body for ADDPREFIX / savePrefix (ctrl:207-218). */
export interface PrefixSavePayload {
  prefixCode: string;
  prefixDesc: string;
  id: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
  isShowPatientRegistration: boolean;
}

/** POST body for UPDATEPREFIX / updatePrefix (ctrl:342-350) -- no
 * `createdBy`/`status`/`organizationId`, matching legacy exactly (only
 * present on the create payload). */
export interface PrefixUpdatePayload {
  prefixCode: string;
  prefixDesc: string;
  prefixId: number;
  id: string;
  updatedBy: number;
  updatedDate: string;
  isShowPatientRegistration: boolean;
}

/** POST body for UPDATEPREFIXSTATUS / updateStatus (ctrl:294-307). */
export interface PrefixStatusPayload {
  prefixId: number;
  status: PrefixStatus;
  updatedBy: number;
  updatedDate: string;
}
