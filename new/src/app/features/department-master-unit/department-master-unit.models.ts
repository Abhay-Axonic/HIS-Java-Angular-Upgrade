/**
 * `departmentMasterUnit` -- module ID **533**, no `requireSuperUser`
 * (confirmed live from the resolve block itself,
 * legacy/HIS-WAR/.../resources/assets/js/config/app.js:8443-8468 --
 * `permissions.modules` filtered for `id == 533`).
 *
 * **Risk tag CORRECTED this session** (see
 * docs/ANGULAR_PENDING_FINDINGS.md `PENDING-departmentmasterunit-billing-
 * mistag`): entry 118 originally called this screen "billing-only" purely
 * because its controller lives under a `.../billing/unit/...` folder --
 * folder-path-only reasoning this project's own playbook already warns is
 * unreliable in both directions. Content-verified against the real entity
 * (`UnitSpecialityMapper` / table `t_unit_speciality_mapper`) instead: its
 * columns are `is_surgical_code`/`is_clinical_speciality`/
 * `is_rendering_speciality`/`is_nursing_assessment_required` (clinical
 * workflow configuration flags) plus `dept_wise_appointment`/
 * `allowed_overbooking_slots`/`pdd_days` (scheduling/admin fields) -- no
 * rate, charge, amount, tax, or any other financial field anywhere on this
 * entity. Corrected classification: **CLINICAL-ONLY**, not billing. See
 * `docs/ANGULAR_PENDING_FINDINGS.md`
 * `PENDING-batch18-step1-departmentmasterunit` for the full Step 1 audit
 * this rebuild is based on.
 *
 * Field names/types read directly from `UnitSpecialtyMapperDto.java`
 * (legacy/ADT/src/main/java/com/param/adt/master/unit/dto/
 * UnitSpecialtyMapperDto.java), the entity `UnitSpecialityMapper.java`'s
 * own `@NamedQuery`-selected columns, and
 * `departmentMasterUnitController.js` -- not guessed.
 */

/** One candidate from `getOrgActiveSpecialityList` -- specialities NOT YET
 * mapped (active OR inactive) to this unit, filtered server-side by the
 * real, working `GET_ORG_ACTIVE_SPECIALITY_LIST_NOT_MAPPED_TO_UNIT` query
 * (`SpecialityMaster.java:80-94`) -- the strongest duplicate-prevention
 * mechanism of any screen in this batch, per the Step 1 audit (though not
 * airtight -- see this screen's `.ts` class doc comment for the remaining
 * save-time gap). */
export interface SpecialityCandidate {
  specialityId: number;
  specialityName: string;
  specialityCode: string;
}

/** A speciality staged for mapping -- legacy's own `tempSelectSpecialty`/
 * `selectedSpecialityList` item shape (ctrl:19-20, 84-97). */
export interface StagedSpeciality {
  specialityId: number;
  specialityName: string;
}

/** One row of the mapped-department list
 * (`GET_UNIT_SPECIALITY_LIST`/`GET_PAGINATED_UNIT_DEPARTMENT_MASTER_
 * DETAILS_LIST_SEARCH`, `UnitSpecialityMapper.java:26-44/89-110`). All 6
 * boolean-ish flags are legacy 'Y'/'N' CHAR columns
 * (`ng-true-value="'Y'"`/`ng-false-value="'N'"`, view:184-230), not real
 * booleans. `allowedOverbookingSlots`/`pddDays` are boxed `Integer`
 * columns server-side -- `null` when never set. */
export interface DepartmentMasterUnitRow {
  unitSpecialityMapperId: number;
  specialityId: number;
  specialityName: string;
  specialityCode: string;
  isSurgicalCode: 'Y' | 'N';
  isClinicalSpeciality: 'Y' | 'N';
  isRenderingSpeciality: 'Y' | 'N';
  deptWiseAppointment: 'Y' | 'N';
  isKioskDept: 'Y' | 'N';
  isNursingAssessmentRequired: 'Y' | 'N';
  allowedOverbookingSlots: number | null;
  pddDays: number | null;
  status: 'A' | 'I';
}

/** POST body for `saveUnitSpecialtiy` (ctrl:100-131, `saveDeptAtUnitLevel`
 * -- the Save action for the checkbox picker). The backend
 * (`UnitSpecialityServiceImpl.saveUnitSpecialtiy:49-133`) only ever reads
 * `organizationId`/`unitId`/`createdBy`/`createdDate`/`updatedBy`/
 * `updatedDate` off the OUTER object plus the `unitSpecialtyMapperDtosList`
 * array (iterated, one INSERT per entry -- all 6 boolean flags hardcoded
 * `'N'` and `allowedOverbookingSlots` hardcoded `0` server-side
 * UNCONDITIONALLY regardless of what's sent, confirmed by reading
 * `UnitSpecialityServiceImpl.java:86-93` directly; `pddDays` is likewise
 * server-computed from the unit's own app-config, never client-supplied).
 * Legacy's own `billingGroupMasterObj` literal (ctrl:35-46) also carries a
 * top-level `unitSpecialityMapperId`/`specialityId`/`status` -- dead
 * weight never read server-side for THIS call -- deliberately omitted
 * here. */
export interface SaveDepartmentMasterUnitPayload {
  organizationId: number;
  unitId: number;
  createdBy: number;
  createdDate: string; // 'DD-MM-YYYY HH:mm:ss', ctrl:109-113
  updatedBy: number;
  updatedDate: string;
  unitSpecialtyMapperDtosList: StagedSpeciality[];
}

/** POST body for `updateStatusForUnitSpecialityList` (ctrl:134-152, the
 * status-toggle action). */
export interface UpdateStatusPayload {
  unitSpecialityMapperId: number;
  status: 'A' | 'I';
  organizationId: number;
  unitId: number;
  updatedDate: string;
  updatedBy: number;
}

/** POST body for `updateUnitSpecialtyMapperDetails` (ctrl:166-231, the
 * Details-modal Update action).
 *
 * **RETRACTED typo-bug severity claim, 2026-07-25** (see
 * docs/ANGULAR_PENDING_FINDINGS.md `PENDING-batch18-step1-departmentmasterunit`):
 * legacy's own empty-string-to-zero fallback (ctrl:181-183) does read a
 * MISSPELLED, never-assigned scope var (`$scope.allowedOverbookingSlot`,
 * missing the trailing "s") -- that dead-code observation is real. But
 * Step 5 live testing (direct request replay against unmodified legacy)
 * confirmed sending `""` for this field returns 200, not a rejection --
 * Spring/Jackson coerces empty string to `0` for this boxed `Integer`
 * field at the framework level, independent of the app's own dead
 * fallback. **No observable defect exists; nothing to reproduce.**
 * `allowedOverbookingSlots` is still typed `string` here (not `number`) to
 * mirror legacy's own `ng-model` binding to a plain `type="text"` input
 * (ctrl/view:241) -- sent verbatim, which is fine, since the server
 * produces the correct value (`0`) either way. */
export interface UpdateDetailsPayload {
  unitSpecialityMapperId: number;
  isSurgicalCode: 'Y' | 'N';
  isClinicalSpeciality: 'Y' | 'N';
  isRenderingSpeciality: 'Y' | 'N';
  deptWiseAppointment: 'Y' | 'N';
  isKioskDept: 'Y' | 'N';
  isNursingAssessmentRequired: 'Y' | 'N';
  allowedOverbookingSlots: string;
  pddDays: string;
  unitId: number;
}

/** POST body for `unitDepartmentMasterUniversalSearch` (ctrl:365-388, the
 * search box). Sent RAW/untrimmed, exactly as legacy's own
 * `searchDepartmentMasterUnit(searchString)` does (the DAO itself trims +
 * lowercases server-side, `UnitSpecialityDaoImpl.java:248`). */
export interface UniversalSearchPayload {
  organizationId: number;
  specialityName: string;
  unitId: number;
}

/** Details-modal form state -- mirrors the scope vars `getFlagByMapperId`
 * populates (ctrl:154-165), including its own `''`/`null` -> `'N'`
 * fallback for the 6 flags (the 2 numeric fields get NO such fallback in
 * legacy -- direct passthrough, faithfully mirrored here too). */
export interface DetailsForm {
  unitSpecialityMapperId: number;
  isSurgicalCode: 'Y' | 'N';
  isClinicalSpeciality: 'Y' | 'N';
  isRenderingSpeciality: 'Y' | 'N';
  isKioskDept: 'Y' | 'N';
  isNursingAssessmentRequired: 'Y' | 'N';
  deptWiseAppointment: 'Y' | 'N';
  allowedOverbookingSlots: string;
  pddDays: string;
}

export function emptyDetailsForm(): DetailsForm {
  return {
    unitSpecialityMapperId: 0,
    isSurgicalCode: 'N',
    isClinicalSpeciality: 'N',
    isRenderingSpeciality: 'N',
    isKioskDept: 'N',
    isNursingAssessmentRequired: 'N',
    deptWiseAppointment: 'N',
    allowedOverbookingSlots: '',
    pddDays: '',
  };
}
