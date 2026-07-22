/**
 * Field names/types read directly from `UserRoleMasterDto`
 * (legacy/Global/src/main/java/com/param/global/unit/dto/UserRoleMasterDto.java)
 * and `roleCreationMasterController.js`'s own `$scope.common` usage, not
 * guessed. `organisationId` (British spelling) is the REAL wire field name
 * both the controller and the DTO use -- preserved verbatim, matching this
 * batch's confirmed `role-management` precedent (same DTO family).
 *
 * `status` is the `'A'|'I'` string vocabulary here -- a genuine
 * status-vocabulary drift against this batch's OTHER screen,
 * `frequency-master`, whose own `GetFrequencyResponse.status` is a real
 * boolean. Confirmed from each screen's own DTO, not assumed to match.
 */

/** Static role-type list -- HARDCODED in the legacy controller itself
 * (ctrl:64-147, `$scope.role`), not fetched from any backend endpoint.
 * Note the real gap: `roleTypeId` 16 is skipped entirely (15 -> 17) --
 * reproduced verbatim, not renumbered. */
export const ROLE_TYPES: ReadonlyArray<{ roleTypeId: number; roleType: string }> = [
  { roleTypeId: 1, roleType: 'Doctor' },
  { roleTypeId: 2, roleType: 'Nurse' },
  { roleTypeId: 3, roleType: 'Not Applicable' },
  { roleTypeId: 4, roleType: 'Staff' },
  { roleTypeId: 5, roleType: 'Picker' },
  { roleTypeId: 6, roleType: 'Cash Desk Management' },
  { roleTypeId: 7, roleType: 'Pharmacy' },
  { roleTypeId: 8, roleType: 'OT Surgeon' },
  { roleTypeId: 9, roleType: 'OT Anaesthesia' },
  { roleTypeId: 10, roleType: 'OT Manager' },
  { roleTypeId: 11, roleType: 'OT Nurse' },
  { roleTypeId: 12, roleType: 'OT Admin' },
  { roleTypeId: 13, roleType: 'Dialysis Technician' },
  { roleTypeId: 14, roleType: 'Pathologist' },
  { roleTypeId: 15, roleType: 'Phlebotomist' },
  { roleTypeId: 17, roleType: 'MRD' },
  { roleTypeId: 18, roleType: 'Housekeeping' },
];

export interface RoleRow {
  roleId: number;
  roleName: string;
  roleTypeId?: number;
  /** Populated CLIENT-SIDE from `ROLE_TYPES` by id -- ctrl:195-247
   * (`initMasterList`'s `angular.forEach` block), not returned by the
   * backend itself. */
  roleType?: string;
  dashboardId?: number;
  dashboardDesc?: string;
  isCounterApplicable: 'Y' | 'N';
  prophylacticVaccinationApplication: 'Y' | 'N';
  status: 'A' | 'I';
}

export interface VaccinationRoleMapperRow {
  vaccinationRoleMapperId?: number;
  vaccinationId: number;
  vaccinationName?: string;
}

/** `getUserRoleMasterById` response shape -- `response.data.listObject[0]`. */
export interface RoleDetail extends RoleRow {
  vaccinations?: VaccinationRoleMapperRow[];
}

export interface VaccinationOption {
  vaccinationId: number;
  vaccinationName: string;
}

export interface DashboardOption {
  dashboardId: number;
  dashboardDesc: string;
}

/** Editable top-of-page form (`$scope.common`, ctrl:41-57 `initMaster()`). */
export interface RoleForm {
  roleId?: number;
  roleName: string;
  roleTypeId: number | '';
  dashboardId: number | '';
  isCounterApplicable: 'Y' | 'N';
  prophylacticVaccinationApplication: 'Y' | 'N';
}

export function emptyRoleForm(): RoleForm {
  return {
    roleId: undefined,
    roleName: '',
    roleTypeId: '',
    dashboardId: '',
    isCounterApplicable: 'N',
    prophylacticVaccinationApplication: 'N',
  };
}

/** POST body for /api/global/saveUserRoleMaster (ctrl:391-463, `save()`). */
export interface SaveRolePayload {
  roleName: string;
  roleTypeId: number;
  dashboardId: number | '';
  isCounterApplicable: 'Y' | 'N';
  prophylacticVaccinationApplication: 'Y' | 'N';
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  updatedDate: string;
  status: 'A';
  unitId: number;
  organisationId: number;
  vaccination: number[];
}

/** POST body for /api/global/updateUserRoleMaster (ctrl:543-596, `update()`). */
export interface UpdateRolePayload {
  roleId: number;
  roleName: string;
  roleTypeId: number | '';
  dashboardId: number | '';
  isCounterApplicable: 'Y' | 'N';
  prophylacticVaccinationApplication: 'Y' | 'N';
  updatedBy: number;
  updatedDate: string;
  unitId: number;
  organisationId: number;
  vaccination: number[];
  vaccinations: VaccinationRoleMapperRow[];
}

/** POST body for /api/global/updateUserRoleMasterStatus (ctrl:598-632,
 * `updateStatus()`) -- NOTE this reuses the shared `$scope.common` object in
 * legacy (see class doc comment's cross-contamination finding); modeled
 * here as its own explicit payload so the rebuild's `toggleStatus()` can
 * choose to send only these 4 fields without actually mutating the
 * in-progress create/edit form. */
export interface UpdateRoleStatusPayload {
  roleId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}
