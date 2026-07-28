export type ReferralTypeStatus = 'A' | 'I';

/**
 * Row shape the REAL, reachable `getReferralTypeMasterList` response
 * actually uses. `referralTypeMasterController.js`
 * (`resources/assets/js/controller/adt/global/referralTypeMasterController.js`)
 * is registered on TWO legacy ui-router states -- see
 * referral-type-master.ts's class doc comment for the full writeup -- and
 * the ONLY one with a live sidebar link (`side-bar.html:150`,
 * `new-side-bar.html:3401`, `href="#/commonMaster/referraltype"`) renders
 * its table from the shared `common-master-screen.html`'s
 * `ng-repeat="info in commonList"`, which reads generic
 * `info.id`/`info.code`/`info.desc`/`info.status` -- exactly what
 * `$scope.commonList` (the real list response) actually contains.
 */
export interface ReferralTypeRow {
  id: number;
  code: string;
  desc: string;
  status: ReferralTypeStatus;
}

/**
 * Shape returned by getReferralTypeById (ctrl:240-264, `showUpdateBtn`,
 * wired to the shared table's edit pencil `ng-click="showUpdateBtn(info.id)"`).
 */
export interface ReferralTypeDetail {
  id: number;
  code: string;
  desc: string;
}

/** Form model. Matches `$scope.common` (ctrl:189-198/323-330/256-258) --
 * the object the controller's real `save()`/`update()`/`showUpdateBtn()`
 * actually read from/write to. */
export interface ReferralTypeForm {
  id?: number;
  code: string;
  desc: string;
}

export function emptyReferralTypeForm(): ReferralTypeForm {
  return {
    id: undefined,
    code: '',
    desc: '',
  };
}

/** POST body for saveReferralTypeMaster (ctrl:189-198, `ADDREFERRALTYPE`). */
export interface ReferralTypeSavePayload {
  code: string;
  desc: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** POST body for updateReferralTypeMaster (ctrl:323-330, `UPDATEREFERRALTYPE`).
 * No `status` field -- confirmed via direct read of ctrl:323-330, same
 * shape as `patient-source-master`/`occupation-master`'s equivalent payload. */
export interface ReferralTypeUpdatePayload {
  id: number;
  code: string;
  desc: string;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

/** POST body for updateStatusForReferralType (ctrl:282-287). No
 * `organizationId` sent -- confirmed via ctrl:282-287. */
export interface ReferralTypeStatusPayload {
  id: number;
  status: ReferralTypeStatus;
  updatedBy: number;
  updatedDate: string;
}
