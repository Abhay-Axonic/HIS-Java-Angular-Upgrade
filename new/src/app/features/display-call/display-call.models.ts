/**
 * Real record shape for displayCall, read from
 * resources/assets/js/controller/qms/QMSTokenDisplayMasterCallController.js
 * (`$scope.activeQMSList`, populated by `getQMSMasterList()`; rendered at
 * views/master/QMSMaster/QMS-display.html:160-161 as
 * `{{qms.qmsKioskMasterId}}` / `{{qms.qmsDesc}}`).
 */
export interface QmsKioskOption {
  qmsKioskMasterId: number;
  qmsDesc: string;
}
