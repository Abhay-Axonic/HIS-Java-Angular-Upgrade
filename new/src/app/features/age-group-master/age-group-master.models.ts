/**
 * Parity rebuild of
 * legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller/lis/global/ageGroupController.js
 * (state `ageGroupMaster`) + legacy/LIS/.../dto/AgeGroupMasterDto.java.
 *
 * `ageTypeGrpfromId`/`ageTypeGrptoId`/`ageGroupFrom`/`ageGroupTo` are kept as
 * strings here even though the backend DTO types them as `Integer` -- legacy
 * binds all four to plain HTML controls (`<select>` option values and
 * `numbers-only`-restricted text inputs) that are always strings client-side
 * (ageGroupController.js:33-38, 61-77, 228-231), only coerced to numbers at
 * submit time. Kept as strings here too so the from/to-in-days validation
 * gate below can be reproduced with the exact same semantics legacy uses.
 */
export interface AgeTypeGroup {
  ageTypeGrpId: number;
  ageTypeGrpName: string;
}

export interface AgeGroupMasterRecord {
  ageGroupId: number | '';
  ageGroupCode: string;
  ageGroupFrom: string;
  ageGroupTo: string;
  ageTypeGrpfromId: string;
  ageTypeGrptoId: string;
  ageTypeGrpName: string;
  ageGroupStatus: 'A' | 'I';
  createdBy: number | '';
  createdDate: number | '';
  updatedBy: number | '';
  updatedDate: number | '';
  orgId: number;
}

/**
 * The wire payload sent to add/update -- built from `AgeGroupMasterRecord`
 * the same way legacy's `$scope.initAgeGroupMaster()` builds
 * `$scope.ageGroupMaster` from `$scope.common`
 * (ageGroupController.js:63-82). `ageFromDay`/`ageToday` are deliberately
 * omitted: the backend always recomputes both itself before persisting
 * (`AgeGroupMasterServiceImpl.addAgeGroupMaster` service.java:35-57 and
 * `.updateAgeGroupMaster` service.java:180-202), so whatever value legacy
 * sends for them (always `""` from `initCommon()`) is discarded either way
 * -- omitting them is behavior-neutral, not a simplification that changes
 * anything the backend does with them.
 */
export interface AgeGroupMasterPayload {
  ageGroupId?: number;
  ageGroupCode: string;
  ageGroupFrom: number;
  ageGroupTo: number;
  ageTypeGrpfromId: number;
  ageTypeGrptoId: number;
  ageTypeGrpName: string;
  ageGroupStatus: 'A' | 'I';
  createdBy: number | '';
  createdDate: number | '';
  updatedBy: number | '';
  updatedDate: number | '';
  orgId: number;
}

export function emptyAgeGroupMaster(userId: number, orgId: number): AgeGroupMasterRecord {
  return {
    ageGroupId: '',
    ageGroupCode: '',
    ageGroupFrom: '',
    ageGroupTo: '',
    ageTypeGrpfromId: '',
    ageTypeGrptoId: '',
    ageTypeGrpName: '',
    ageGroupStatus: 'A',
    createdBy: userId,
    createdDate: '',
    updatedBy: userId,
    updatedDate: '',
    orgId,
  };
}
