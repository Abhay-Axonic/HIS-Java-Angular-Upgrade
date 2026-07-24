export type NursingStationStatus = 'A' | 'I';

/**
 * Row shape returned by `getNursingStationMasterList` (ctrl:89-144,
 * `GETNURSINGSTATIONMASTERLIST`) -- the fields the legacy view actually
 * renders (nursing-station-master.html:172-183): code, description, the
 * FK's resolved display name (`storeName`, NOT `storeId`), and status.
 */
export interface NursingStationRow {
  nursingStationId: number;
  nursingStationCode: string;
  nursingStationDesc: string;
  storeName: string;
  status: NursingStationStatus;
}

/**
 * Shape of `getNursingStationMasterById`'s `listObject[0]` (ctrl:279-291,
 * `GETNURSINGSTATIONMASTERBYID/{id}/{orgId}/{unitId}`) -- only the 4 fields
 * `showUpdateBtn` actually reads back into the form. Legacy casts `storeId`
 * to a string on read (`response.data.listObject[0].storeId.toString()`,
 * ctrl:287) purely so it matches the `<select>`'s stringly-typed `ng-model`;
 * the wire value itself is a plain Integer (`NursingStationMasterDto
 * .storeId`, Global/.../dto/NursingStationMasterDto.java:21).
 */
export interface NursingStationDetail {
  nursingStationId: number;
  nursingStationCode: string;
  nursingStationDesc: string;
  storeId: number;
}

/**
 * Store dropdown option -- `GET /api/unit/main/stores/{unitId}/{orgId}`
 * (ctrl:66-77, hardcoded URL, NOT a url-config constant), backed by
 * `MainStoreController.GetAll` (legacy/Pharmacy/.../controller/
 * MainStoreController.java:33-43), which returns a RAW JSON ARRAY
 * (`ResponseEntity<List<MainStoreListResponse>>`) -- NOT the usual
 * `{status, listObject}` envelope every other endpoint on this screen uses.
 * Confirmed directly against `MainStoreResponse.java` (the response DTO):
 * the FK/option-value field is `id` (Integer), NOT `storeId` -- the view's
 * `<option value="{{info.id}}">` (nursing-station-master.html:46) and
 * `ng-if="info.status == true"` (status is a real Boolean here, unlike this
 * screen's own `char`-typed `NursingStationMasterDto.status`) both confirm
 * this directly. Only active stores are shown, filtered client-side.
 */
export interface StoreOption {
  id: number;
  storeName: string;
  status: boolean;
}

/** Editable form state -- mirrors `$scope.nursingMasterMasterObj` (ctrl:56,
 * 213-215, 284-287, 342-345). `storeId` kept as `number | ''` (not a
 * string) -- Angular's `[ngModel]`/`[value]` binding on a `<select>`
 * coerces for display; legacy's own `.toString()` cast (ctrl:287) was only
 * ever a plain-AngularJS `ng-model` string-matching workaround, not a real
 * wire-type requirement, same simplification already used by this
 * codebase's currency-master (`countryId: number | ''`). */
export interface NursingStationForm {
  nursingStationId?: number;
  nursingStationCode: string;
  nursingStationDesc: string;
  storeId: number | '';
}

export function emptyNursingStationForm(): NursingStationForm {
  return { nursingStationId: undefined, nursingStationCode: '', nursingStationDesc: '', storeId: '' };
}

/** POST body for `saveNursingStationMaster` (ctrl:206-265, data built at
 * ctrl:212-223). `status` is ALWAYS `'A'` on create -- there is no way to
 * create a nursing station as inactive, same shape as this codebase's
 * mortuary-bed-master/currency-master siblings. */
export interface NursingStationSavePayload {
  nursingStationCode: string;
  nursingStationDesc: string;
  storeId: number;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  unitId: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/**
 * POST body for `updateNursingStationMaster` (ctrl:336-383, data built at
 * ctrl:341-349) -- deliberately has NO `createdBy`/`status`/`unitId` keys,
 * matching legacy's real request shape exactly (only present on the create
 * payload above).
 *
 * *** See docs/DECISION_NEEDED_NursingStationMasterUpdatedByCorruption.md ***:
 * `NursingStationDaoImpl.updateNursingStationMaster` (legacy/Global/.../dao/
 * NursingStationDaoImpl.java:143-159) reads `nursingStationMasterDto
 * .getCreatedBy()` (line 150) instead of `.getUpdatedBy()` when setting the
 * persisted `updated_by` column -- a copy-paste bug from the sibling
 * `save()` method, where using `getCreatedBy()` for that same setter is
 * correct (create sends `createdBy == updatedBy`, so the bug is invisible
 * there). Since this UPDATE payload never sends a `createdBy` key at all,
 * the server-side DTO deserializes that field to its Java primitive `int`
 * default, `0` -- so every edit via this screen silently persists
 * `updated_by = 0`, discarding the real acting user's id. This is a
 * purely server-side DTO-field misread with ZERO client-observable effect
 * and NO client-side workaround (there is nothing wrong with what this
 * payload sends -- `updatedBy` below IS the real, correct user id, exactly
 * as legacy's own frontend already sends it). Per the owner's recorded
 * decision (see the doc above), Step 2+ proceeds wiring to the endpoint
 * exactly as it exists today; a one-line legacy fix (`.getUpdatedBy()`
 * instead of `.getCreatedBy()`) is proposed separately on
 * `fix/nursingstationmaster-updatedby-corruption`, held for owner approval,
 * NOT part of this branch.
 */
export interface NursingStationUpdatePayload {
  nursingStationCode: string;
  nursingStationDesc: string;
  nursingStationId: number;
  storeId: number;
  updatedBy: number;
  organizationId: number;
  updatedDate: string;
}

/** POST body for `updateNursingStationMasterStatus` (ctrl:296-333, data
 * built at ctrl:303-308). This is the ONE place on this screen where
 * `updatedBy` IS read correctly server-side (`NursingStationDaoImpl
 * .updateNursingStationMasterStatus`, dao:168, `.getUpdatedBy()` --
 * confirmed correct, unlike the update-master bug above). */
export interface NursingStationStatusPayload {
  nursingStationId: number;
  status: NursingStationStatus;
  updatedBy: number;
  updatedDate: string;
}
