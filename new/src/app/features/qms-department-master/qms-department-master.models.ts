export interface KioskOption {
  qmsKioskMasterId: number;
  qmsDesc: string;
}

/** Mirrors a `listObject` entry from getQMSDeptMasterList (ctrl:105-119,
 * view:174-191) -- the joined kiosk description reuses the field name
 * `qmsDesc` here too (kiosk's own desc, not the dept's), exactly as legacy
 * returns it. */
export interface QMSDeptRow {
  qmsDeptMasterId: number;
  qmsKioskMasterId: number;
  qmsDeptName: string;
  startLetter: string;
  startNumber: string;
  qmsDesc: string;
  isReception: 'A' | 'I';
  isVoice: 'A' | 'I';
  status: 'A' | 'I';
}

/** Mirrors `$scope.common` (ctrl:47-60), the single object used for both
 * the add/edit form AND the save/update request body verbatim -- legacy
 * never builds a separate DTO, it POSTs this object as-is. */
export interface QMSDeptCommon {
  qmsDeptMasterId: number | '';
  qmsKioskMasterId: number | '';
  qmsDeptName: string;
  startLetter: string;
  startNumber: string;
  isReception: 'A' | 'I' | '';
  isVoice: 'A' | 'I' | '';
  qmsDesc: string;
  createdBy: number | '';
  updatedBy: number | '';
  organisationId: number;
  unitId: number;
}

/** Mirrors initQMSMaster's object literal (ctrl:47-60) -- createdBy/updatedBy
 * default to the session user for a fresh create; isReception/isVoice start
 * as `''` (neither 'A' nor 'I'), matching legacy's checkbox defaults exactly
 * (an untouched checkbox posts `''`, not `'I'`). */
export function emptyQMSDeptCommon(userId: number, organisationId: number, unitId: number): QMSDeptCommon {
  return {
    qmsDeptMasterId: '',
    qmsKioskMasterId: '',
    qmsDeptName: '',
    startLetter: '',
    startNumber: '',
    isReception: '',
    isVoice: '',
    qmsDesc: '',
    createdBy: userId,
    updatedBy: userId,
    organisationId,
    unitId,
  };
}
