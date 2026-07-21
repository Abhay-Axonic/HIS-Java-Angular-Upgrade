/**
 * Field set taken directly from
 * views/master/QMSMaster/QMS-kiosk-Master.html +
 * resources/assets/js/controller/qms/QMSkioskMasterController.js
 * (`QMSkioskMasterController`, no `controllerAs`).
 */
export interface QmsKioskRow {
  qmsKioskMasterId: number;
  qmsCode: string;
  qmsDesc: string;
  status: 'A' | 'I';
}

/** Shape of `getQMSMasterById`'s `listObject[0]` -- only the fields
 * `showUpdateBtn` (ctrl:198-229) actually reads back into the form. */
export interface QmsKioskDetail {
  qmsKioskMasterId: number;
  qmsCode: string;
  qmsDesc: string;
}

/**
 * Mirrors `$scope.common` at save time (ctrl:48-56, 153-196). Note
 * `qmsKioskMasterId` is always the literal empty string on create -- there
 * is no ng-model bound to it in the view, so it's never cleared/typed --
 * sent to the server as-is rather than omitted.
 *
 * `createdBy`/`updatedBy` are DELIBERATELY not modeled here: legacy sets
 * them from `$scope.createdBy`/`$scope.updatedBy` (ctrl:52-53), which are
 * never assigned anywhere in this controller and are therefore always
 * `undefined` in the real save payload (dropped entirely by JSON
 * serialization). Reproduced by omission, not "fixed" to send the real
 * userId (contrast: `QMSdepartmentMasterController` does this correctly
 * -- not copied here on purpose).
 */
export interface QmsKioskSavePayload {
  qmsKioskMasterId: '';
  qmsCode: string;
  qmsDesc: string;
  organisationId: number;
  unitId: number;
}

/** Mirrors `$scope.common` at update time (ctrl:232-269) -- same object
 * the getById response populated, with organisationId/unitId reasserted
 * (ctrl:239-240) immediately before POSTing. */
export interface QmsKioskUpdatePayload {
  qmsKioskMasterId: number;
  qmsCode: string;
  qmsDesc: string;
  organisationId: number;
  unitId: number;
}

/** Mirrors `updateStatus`'s `obj` (ctrl:276-280) -- note there genuinely is
 * no organisationId/unitId sent here (unlike every other payload on this
 * screen), and `updatedBy` is the same always-undefined `$scope.updatedBy`
 * as the save payload -- omitted for the same reason (ctrl:279). */
export interface QmsKioskStatusPayload {
  qmsKioskMasterId: number;
  status: 'A' | 'I';
}
