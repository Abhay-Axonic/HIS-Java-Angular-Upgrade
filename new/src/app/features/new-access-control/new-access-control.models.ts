/**
 * Rebuild of legacy `newAccessControl` -- see newAccessControlController.js
 * (405 lines, classic `$scope`, no `controllerAs`) +
 * new-access-control.html (161 lines). Field shapes taken directly from
 * the real backend DTO `ActionMasterDto.java`, cross-checked against the
 * real Hibernate named queries in `ActionMasterGlobal.java` (entity +
 * `@NamedQuery`/`@NamedNativeQuery` annotations) since several fields are
 * only populated by SOME queries and not others (see the component's
 * class doc comment).
 */

/** Row shape from `GET_ACTION_MASTER_LIST` (list) --
 * actionName/parentId/parentActioName(camelCase)/status/actionId. */
export interface ActionMasterRow {
  actionId: number;
  actionName?: string;
  parentId?: number;
  parentActioName?: string;
  status?: 'A' | 'I';
}

/** Row shape from `GET_ACTION_MASTER_BY_ID` (edit) -- note
 * `parentactioname` here is all-lowercase (a DIFFERENT DTO field to the
 * list's camelCase `parentActioName`), confirmed via the real
 * `@NamedNativeQuery` column aliases. */
export interface ActionMasterDetail {
  actionId: number;
  actionName?: string;
  actionLink?: string;
  actionIcon?: string;
  parentId?: number;
  status?: 'A' | 'I';
  parentactioname?: string;
}

/** Typeahead search result shape from
 * `SEARCH_ACTION_MASTER_BY_PARENT_ACTION_NAME` -- a recursive query that
 * builds a pipe-joined hierarchical path (`parent|child|grandchild`) as
 * `name`, plus `id`/`actionId` (same value, both aliased). Confirmed this
 * query does NOT alias an `actionName` column at all -- see the
 * component's class doc comment for why that makes `selectParentId`'s
 * `parentActioName` assignment always-undefined (and why that's harmless). */
export interface ActionMasterSearchOption {
  id: number;
  name: string;
  actionId: number;
}

/** Mirrors `$scope.common`, the shared add/edit form model. */
export interface ActionMasterForm {
  actionId?: number;
  actionName: string;
  isChild: 'Y' | 'N';
  parentActioName?: string;
  parentId?: number;
  /** The parent-search typeahead's own bound text (`ng-model="common.name"`). */
  name: string;
  grandParentActionName?: string;
}

export function emptyActionMasterForm(): ActionMasterForm {
  return {
    actionName: '',
    isChild: 'N',
    parentActioName: '',
    parentId: 0,
    name: '',
    grandParentActionName: '',
  };
}
