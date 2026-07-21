/**
 * Field names/types read directly from `UserRoleMasterDto`/`UserRoleMapperDto`
 * (legacy/Global/src/main/java/com/param/global/unit/dto/) and the legacy
 * controller's own `$scope` usage (roleManagementMasterController.js), not
 * guessed. `organisationId` (British spelling) is the REAL wire field name
 * this screen's own controller and DTO both use -- unlike
 * `doctorCategoryMaster`'s `organizationId`. Preserved verbatim; "fixing"
 * the spelling would silently change the wire payload's field name.
 */
export interface RoleRow {
  roleId: number;
  roleName: string;
  status: 'A' | 'I';
}

export interface RoleUserRow {
  userId: number;
  userName: string;
  userRoleMpprId: number;
}

export interface ActiveRoleListRequest {
  organisationId: number;
  unitId: number;
}

export interface UserListByRoleRequest {
  organisationId: number;
  unitId: number;
  roleId: number;
}

export interface UpdateRoleStatusPayload {
  roleId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}

export interface UpdateUserRoleMapperStatusPayload {
  userRoleMpprId: number;
  status: 'I';
  updatedBy: number;
  updatedDate: string;
}
