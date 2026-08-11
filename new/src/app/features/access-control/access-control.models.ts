/**
 * The accessControl / accessControl1 states share the accessControlController
 * STUB (60 lines, zero serviceAction/api/URI refs — pure angular-ui-tree on
 * an empty $scope.data; DQ-2026-08-12-019). The legacy screens render an
 * empty tree editor with NO data path — the noData class (DQ-2026-08-12-005:
 * no endpoint at all is its own class). No row model exists; the screen has
 * nothing to list.
 */
export interface AccessControlRow {
  // no data path in the legacy backend — nothing to list
}
