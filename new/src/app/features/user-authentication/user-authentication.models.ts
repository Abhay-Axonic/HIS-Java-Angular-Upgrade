/**
 * Parity rebuild of the row shape returned by
 * `POST /api/global/getUserAuthenticationList`
 * (legacy/Global/src/main/java/com/param/global/unit/dao/UserRoleMapperDaoImpl.java:306-375,
 * UserRoleMapperDto.java). The real backend response also includes
 * `password` (decrypted server-side, in plaintext, unconditionally --
 * see docs/DECISION_NEEDED_UserAuthenticationPlaintextPasswordDisclosure.md)
 * plus `contactNo`, `userRoleMpprId`, `roleId`, `roleName`, `status`,
 * `emailId`, `firstName`.
 *
 * **`password` is deliberately NOT declared on this interface.**
 * TypeScript's structural typing means the raw HTTP JSON can (and does)
 * still contain it on the wire -- omitting it here only means this
 * rebuild's code never types, reads, binds, or stores it anywhere. This is
 * a provisional, flagged interpretation of an explicitly still-open
 * decision (see the doc above, question 3) -- not a resolution of the
 * underlying backend disclosure, which is unchanged and tracked
 * separately. `roleName`/`status` are also omitted: the Role and Status
 * columns are both fully commented out in the live legacy view
 * (user-authentication.html:90-119) -- there is no live role-assignment or
 * status-toggle UI on this screen today, so building either here would be
 * over-delivering relative to legacy parity.
 */
export interface UserAuthenticationRow {
  userId: number;
  empCode: string;
  empName: string;
  userName: string;
}

/**
 * Typeahead result shape for `POST /api/global/searchEmployeeByName`
 * (userAuthenticationMasterController.js:84-98, `getListByUserName`).
 * Legacy's typeahead label is `group.empName+'-'+group.empCode`; selecting
 * a result stores `info.userId`/`info.empName` onto `$scope.common`
 * (`selectUserId`, controller lines 101-105).
 */
export interface EmployeeTypeaheadResult {
  userId: number;
  empCode: string;
  empName: string;
}

/**
 * Typeahead result shape for `POST /api/global/searchRoleByName`
 * (userAuthenticationMasterController.js:111-125, `getListByRoleName`).
 * Legacy's typeahead label is `group.roleName`; selecting a result stores
 * `info.roleId`/`info.roleName` onto `$scope.common` (`selectRoleId`,
 * controller lines 128-132).
 */
export interface RoleTypeaheadResult {
  roleId: number;
  roleName: string;
}
