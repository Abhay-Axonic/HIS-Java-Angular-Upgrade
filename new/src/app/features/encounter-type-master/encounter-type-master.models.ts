export type EncounterTypeStatus = 'A' | 'I';

/** Row shape returned by `GETENCOUNTERTYPELIST` (`getEncounterTypeMasterList`,
 * ctrl:97-131) -- the only fields the legacy view actually renders
 * (encounter-type-master.html table: code, description, status, action). */
export interface EncounterTypeRow {
  encounterTypeId: number;
  encounterTypeCode: string;
  encounterTypeName: string;
  status: EncounterTypeStatus;
}

/** `response.data.listObject[0]` shape returned by `GETSINGLEENCOUNTERTYPE`
 * (`showUpdateBtn`, ctrl:246-264) -- NOTE this is `listObject[0]`, not
 * `object` like several sibling GlobalMaster screens' single-record fetch
 * (e.g. city-master's `getCityById`); read directly off the controller,
 * not assumed. */
export interface EncounterTypeDetail {
  encounterTypeId: number;
  encounterTypeCode: string;
  encounterTypeName: string;
}

/** Editable form state -- mirrors legacy's `$scope.encountertype`. Field
 * names kept as `encounterTypeCode`/`encounterTypeName` (the payload's own
 * names) rather than legacy's internal `encounterCode`/`encounterName`
 * scope-object aliases, since this rebuild has no separate "scope object"
 * vs "payload" distinction to preserve. */
export interface EncounterTypeForm {
  encounterTypeId?: number;
  encounterTypeCode: string;
  encounterTypeName: string;
}

export function emptyEncounterTypeForm(): EncounterTypeForm {
  return { encounterTypeId: undefined, encounterTypeCode: '', encounterTypeName: '' };
}

/** POST body for `ADDENCOUNTERTYPE` (`saveEncounterType`, ctrl:170-208).
 * `status` is hardcoded to `'A'` by legacy on every create -- there is no
 * create-as-inactive path in the legacy UI at all (no status control on
 * the create form), reproduced verbatim, not a rebuild simplification. */
export interface EncounterTypeSavePayload {
  encounterTypeCode: string;
  encounterTypeName: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** POST body for `UPDATEENCOUNTERTYPE` (`updateEncounterType`, ctrl:308-337).
 * Deliberately narrower than the save payload -- no `status`, `createdBy`,
 * or `organizationId` fields, matching legacy's own `data` object exactly. */
export interface EncounterTypeUpdatePayload {
  encounterTypeCode: string;
  encounterTypeName: string;
  encounterTypeId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `UPDATEENCOUNTERTYPESTATUS` (`updateStatus`, ctrl:266-296). */
export interface EncounterTypeStatusPayload {
  encounterTypeId: number;
  status: EncounterTypeStatus;
  updatedBy: number;
  updatedDate: string;
}
