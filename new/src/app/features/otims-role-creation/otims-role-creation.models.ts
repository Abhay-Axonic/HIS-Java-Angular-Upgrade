export type OtimsRoleStatus = 'A' | 'I';

/**
 * The 4 hardcoded role-name options from the legacy view
 * (`OTIMS-role-creation.html:12-15`) -- a static `<select>`, NOT
 * fetched from any API. Reproduced verbatim, including the exact
 * casing ("admin" is lowercase in legacy, the others are not).
 */
export const OTIMS_ROLE_NAME_OPTIONS = ['OT Manager', 'Surgeon', 'admin', 'Anaesthetist'] as const;

/**
 * Legacy's placeholder `<option>` has no `value` attribute, so its
 * bound value defaults to its own text content -- if the user never
 * touches the dropdown, `common.roleName` is this literal string, not
 * `undefined`/empty. Reproduced verbatim -- see class doc for the
 * "no validation" finding this enables.
 */
export const OTIMS_ROLE_PLACEHOLDER = 'Select OTIMS User Role';

/** Row shape returned by `getAllOtimsRoleMasterList` (ctrl:38-51). */
export interface OtimsRoleRow {
  roleId: number;
  roleName: string;
  orgId: number;
  unitId: number;
  status: OtimsRoleStatus;
}

/** Editable form state -- mirrors `$scope.common` (ctrl:31-34). */
export interface OtimsRoleForm {
  roleId?: number;
  roleName: string;
}

export function emptyOtimsRoleForm(): OtimsRoleForm {
  return { roleId: undefined, roleName: OTIMS_ROLE_PLACEHOLDER };
}

/** POST body for `saveOtimsRoleMaster` (ctrl:69-125). */
export interface OtimsRoleSavePayload {
  roleName: string;
  orgId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
  status: 'A';
  createdDate: string;
  updatedDate: string;
}

/** POST body for `updateOtimsRoleMaster` (ctrl:159-197). */
export interface OtimsRoleUpdatePayload {
  roleId: number;
  roleName: string;
  orgId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `updateOtimsRoleMasterStatus` (ctrl:209-230). */
export interface OtimsRoleStatusPayload {
  roleId: number;
  status: OtimsRoleStatus;
  updatedBy: number;
  updatedDate: string;
}
