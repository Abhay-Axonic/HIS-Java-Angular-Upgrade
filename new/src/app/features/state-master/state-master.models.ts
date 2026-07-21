export type StateStatus = 'A' | 'I';

/** Country dropdown option -- getActiveCountryList (ctrl:63-78). */
export interface CountryOption {
  countryId: number;
  countryName: string;
}

/** Row shape returned by getStateMasterList (ctrl:108-156, view:168-179). */
export interface StateRow {
  stateId: number;
  countryName: string;
  stateCode: string;
  stateName: string;
  status: StateStatus;
}

/** Shape returned by getStateById (ctrl:271-298), assigned field-by-field
 * onto `$scope.state`. `countryId` comes back numeric from the backend but
 * is converted to a string (ctrl:293, `.toString()`) since the `<select>`'s
 * `ng-model` compares against the string-interpolated `value="{{...}}"` on
 * each `<option>`. */
export interface StateDetail {
  stateId: number;
  stateCode: string;
  stateName: string;
  countryId: number;
}

/** Editable form state -- mirrors `$scope.state` (ctrl:52, `init()`). */
export interface StateForm {
  stateId?: number;
  countryId: string;
  stateCode: string;
  stateName: string;
}

/** Matches ctrl:52's reset shape (`state = {}` -- all fields implicitly
 * undefined/blank until the user picks a country or types). */
export function emptyStateForm(): StateForm {
  return {
    stateId: undefined,
    countryId: '',
    stateCode: '',
    stateName: '',
  };
}

/** POST body for saveStateMaster (ctrl:216-226). */
export interface StateSavePayload {
  stateCode: string;
  stateName: string;
  countryId: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** POST body for updateStateMaster (ctrl:356-364) -- no `createdBy`/`status`,
 * matching legacy exactly (only present on the create payload). */
export interface StateUpdatePayload {
  stateCode: string;
  stateName: string;
  countryId: string;
  stateId: number;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

/** POST body for updateStatusForState (ctrl:308-314) -- includes
 * `organizationId`, unlike raceMaster's equivalent status payload. */
export interface StateStatusPayload {
  stateId: number;
  status: StateStatus;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}
