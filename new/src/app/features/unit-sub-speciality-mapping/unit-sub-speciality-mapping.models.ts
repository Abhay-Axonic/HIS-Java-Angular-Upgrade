/**
 * Parity rebuild of
 * views/master/UnitMaster/organizationMaster/sub-department-master-unit.html
 * + resources/assets/js/controller/billing/unit/subDepartmentMasterUnitController.js
 * (legacy state `subDepartmentMasterUnit`).
 *
 * Field names/shapes read directly from the real backend, NOT guessed:
 * `legacy/ADT/.../dto/UnitSubSpecialityMapperDto.java`,
 * `legacy/Entity/.../model/UnitSubSpecialityMapper.java` (named-query
 * aliases), and `legacy/Global/.../dto/SubSpecialityMasterDto.java` (for the
 * `getSubspecialityNotInUnit` picker endpoint, a different Maven module's
 * DTO than the rest of this screen -- see unit-sub-speciality-mapping.ts's
 * class doc for the full backend map).
 */

/** Top "Department" dropdown option, from `getActiveUnitSpecialityList`. */
export interface UnitSpecialityOption {
  specialityId: number;
  specialityName: string;
}

/**
 * Left-pane "available" sub-speciality picker row, from
 * `GET getSubspecialityNotInUnit/orgId/{orgId}/unitId/{unitId}/specialityId/{specialityId}`.
 * NOTE (disclosed bug #3, see class doc): the backend query behind this
 * endpoint only excludes sub-specialities already mapped with status='A' --
 * an inactive ('I') mapping for the same speciality/sub-speciality pair is
 * NOT excluded here, so this list alone is not a reliable duplicate guard.
 */
export interface AvailableSubSpeciality {
  subSpecialityMasterId: number;
  subSpecialityMasterName: string;
  subSpecialityMasterCode?: string;
  specialityId: number;
}

/** Right-pane picker selection shape -- exactly what gets sent in `saveUnitSubSpecialtiy`'s per-item list. */
export interface PickedSubSpeciality {
  subSpecialityMasterId: number;
  subSpecialityName: string;
}

/**
 * Mapped-row shape shared by `getUnitSubSpecialityList` and
 * `unitSubDepartmentMasterUniversalSearch` (both backed by the same
 * `UnitSubSpecialityMapperDto` aliasToBean named queries -- confirmed
 * identical column aliases in UnitSubSpecialityMapper.java).
 */
export interface UnitSubSpecialityRow {
  unitSubSpecialityId: number;
  specialityId: number;
  subSpecialityId: number;
  status: 'A' | 'I';
  specialityCode: string;
  specialityName: string;
  subSpecialityMasterCode: string;
  subSpecialityName: string;
}
