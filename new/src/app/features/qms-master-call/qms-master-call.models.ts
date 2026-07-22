/**
 * Real record shapes for `QMSMasterCall`, read from
 * QMSMasterTokenCallController.js and views/master/QMSMaster/QMS-master-call.html.
 */

/** `$scope.activeQMSList`, populated by `getActiveQMSMasterList`. */
export interface QmsKioskOption {
  qmsKioskMasterId: number;
  qmsDesc: string;
}

/**
 * `$scope.activeQMSDeptList`, populated by `getActiveDeptListByKioskId`.
 * Received and stored (matching legacy) but never rendered by this
 * screen's own view -- QMS-master-call.html has no binding to it at all;
 * the department picker lives on the separate `QMS` screen this one opens
 * in a new tab.
 */
export interface QmsActiveDept {
  qmsDeptMasterId: number;
  qmsDeptName: string;
  qmsDesc?: string;
}
