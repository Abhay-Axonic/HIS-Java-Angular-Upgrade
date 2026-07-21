export interface GeneralLedgerOption {
  generalLedgerId: number;
  generalLedgerName: string;
}

/** Row shape returned by getSpecialityListOfActiveInactive / departmentMasterUniversalSearch
 * (ctrl:102-160, 516-537) -- only the fields the view actually renders
 * (view:266-288; the rest of that row's columns are commented out). */
export interface SpecialityListRow {
  specialityId: number;
  specialityCode: string;
  specialityName: string;
  generalLedgerName: string;
  specialitySAC: string;
  status: 'A' | 'I';
}

/** Shape returned by getSpecialityById (ctrl:300-339). */
export interface SpecialityDetail {
  specialityId: number;
  specialityCode: string;
  specialityName: string;
  generalLedgerId: number | string;
  generalLedgerName: string;
  specialitySAC: string;
  isSurgicalCode: 'Y' | 'N';
  isClinicalSpeciality: 'Y' | 'N';
  isRenderingSpeciality: 'Y' | 'N';
  deptWiseAppointment: 'Y' | 'N';
  isKioskDept?: 'Y' | 'N';
  isNursingAssessmentRequired: 'Y' | 'N';
  allowedOverbookingSlots?: number | string;
}

/**
 * Form/edit-state shape -- mirrors $scope.speciality (ctrl:48-55, 318-330).
 *
 * `isSurgicalCode`/`isClinicalSpeciality`/`isRenderingSpeciality`/
 * `deptWiseAppointment`/`isKioskDept`/`isNursingAssessmentRequired`/
 * `allowedOverbookingSlots` have NO corresponding UI control anywhere in
 * the legacy view -- department-master-global.html:91-158 (the whole
 * checkbox block) is entirely commented out. They're carried here purely
 * because the save/update payload still includes them (edit-populate sets
 * them from the server response, create always sends the ctrl:48-54
 * defaults) -- not rendered as inputs, and not a missing feature: legacy
 * itself has no way to edit these on this screen.
 */
export interface SpecialityFormRecord {
  specialityId: number;
  specialityCode: string;
  specialityName: string;
  generalLedgerId: number | string | null;
  generalLedgerName: string;
  specialitySAC: string;
  isSurgicalCode: 'Y' | 'N';
  isClinicalSpeciality: 'Y' | 'N';
  isRenderingSpeciality: 'Y' | 'N';
  deptWiseAppointment: 'Y' | 'N';
  isNursingAssessmentRequired: 'Y' | 'N';
  isKioskDept?: 'Y' | 'N';
  allowedOverbookingSlots?: number | string;
}

/** Matches ctrl:48-55's defaults exactly (isKioskDept/allowedOverbookingSlots
 * are left undefined there too -- never defaulted). */
export function emptySpeciality(): SpecialityFormRecord {
  return {
    specialityId: 0,
    specialityCode: '',
    specialityName: '',
    generalLedgerId: null,
    generalLedgerName: '',
    specialitySAC: '',
    isSurgicalCode: 'N',
    isClinicalSpeciality: 'N',
    isRenderingSpeciality: 'N',
    deptWiseAppointment: 'N',
    isNursingAssessmentRequired: 'N',
  };
}
