/**
 * Real record shapes for the `QMS` kiosk token-issue screen, read from
 * QMSMasterController.js and views/master/QMSMaster/QMS.html.
 */

/** `$scope.activeQMSList`, populated by `getActiveQMSMasterList`. */
export interface QmsKioskOption {
  qmsKioskMasterId: number;
  qmsDesc: string;
}

/**
 * `$scope.activeQMSDeptList`, populated by `getActiveDeptListByKioskId`.
 * `discripton` (legacy's own misspelling, kept verbatim -- it is never
 * corrected anywhere in the codebase) is filled in client-side for the
 * two hardcoded department names, matching QMSMasterController.js's own
 * `angular.forEach` post-processing (ctrl:~129-136), not returned by the
 * backend itself.
 */
export interface QmsActiveDept {
  qmsDeptMasterId: number;
  qmsDeptName: string;
  qmsKioskMasterId: number;
  qmsDesc?: string;
  isReception?: string;
  startLetter?: string;
  startNumber?: number;
  discripton?: string;
}

/** `getQMSPrnFormat`'s `listObject[0]`. */
export interface QmsPrnFormatEntry {
  qmsPrnPrintFormat: string;
}

/** Raw shape of the `labConfig` blob legacy stores in `localStorage`. */
export interface QmsLabConfig {
  prnUrl?: string;
  [key: string]: unknown;
}

/** Raw shape of the `cookieObjectAppConfigSecond` blob in `localStorage`. */
export interface QmsAppConfigSecond {
  qmsPrnPrint?: string;
  [key: string]: unknown;
}
