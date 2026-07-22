export type ClinicMasterStatus = 'A' | 'I';

/** Floor dropdown option (ctrl:57-73, `getFloorMasterList`). Legacy sends
 * NO offset/noOfRecordsPerPage in this specific call (ctrl:60-63), so the
 * shared `/adt/getFloorMasterList` endpoint's own default pagination
 * (`FloorMasterDaoImpl.java:87-88`, offset 0 / 10 rows when unset) silently
 * caps this dropdown at the first 10 floors, ordered by floorId DESC --
 * NOT filtered to active-only floors either (`GET_FLOOR_LIST` has no status
 * predicate, unlike the Wing list below). Reproduced verbatim, not fixed --
 * this is a real, pre-existing legacy limitation, not a rebuild bug. */
export interface FloorOption {
  floorId: number;
  floorName: string;
}

/** Wing dropdown option (ctrl:77-91, `getWingMasterListByOrgAndUnit`) --
 * unlike Floor above, this list IS active-only (`GET_ACTIVE_WING_MASTER_LIST`,
 * `WingMaster.java`, `AND wing.status='A'`) and is NOT paginated/capped. */
export interface WingOption {
  wingId: number;
  wingDesc: string;
}

/** Row shape returned by getClinicMasterList (ctrl:99-116,
 * `GET_CLINIC_MASTER_LIST_BY_ORG_AND_UNIT`) -- an INNER JOIN to Floor/Wing,
 * so floorName/wingDesc always come back populated together with the row. */
export interface ClinicMasterRow {
  clinicMasterId: number;
  floorName: string;
  wingDesc: string;
  clinicCode: string;
  clinicDescription: string;
  status: ClinicMasterStatus;
}

/** Response shape of getClinicMasterById (ctrl:185-215,
 * `GET_CLINIC_MASTER_BY_ID`, `response.data.object`). Note this endpoint
 * returns the record in `object`, NOT `listObject[0]` -- confirmed via
 * `ClinicMasterDaoImpl.getClinicMasterById` returning `new Response(...,
 * null, masterDto)` (5th ctor arg = object), unlike most sibling by-id
 * endpoints in this codebase. */
export interface ClinicMasterDetail {
  clinicMasterId: number;
  clinicCode: string;
  clinicDescription: string;
  floorId: number;
  wingId: number;
  status: ClinicMasterStatus;
}

/** Mirrors `$scope.clinic` (ctrl:34-49). `status` is carried through
 * unmodified from `editClinic`'s fetch (ctrl:203, `response.data.object`)
 * to `updateClinicMaster` (ctrl:220-247) -- legacy's update payload sends
 * whatever status the record already had (it's not editable from this
 * form; the checkbox toggle is the only thing that changes it), so a
 * hardcoded `'A'` here would be a real parity bug: editing and updating an
 * already-INACTIVE clinic would silently reactivate it. `'A'` is only
 * correct as the literal default for a brand-new record (ctrl:48). */
export interface ClinicMasterForm {
  clinicMasterId?: number;
  floorId: string;
  wingId: string;
  clinicCode: string;
  clinicDescription: string;
  status: ClinicMasterStatus;
}

export function emptyClinicMasterForm(): ClinicMasterForm {
  return {
    clinicMasterId: undefined,
    floorId: '',
    wingId: '',
    clinicCode: '',
    clinicDescription: '',
    status: 'A',
  };
}

/** POST body for saveClinicMaster (ctrl:153-182). Field names/typo
 * (`created_by`/`cerated_date`, not `createdBy`/`createdDate`) are taken
 * verbatim from `ClinicMasterDto.java`'s own getter-derived JSON property
 * names, matching what the legacy controller actually sends -- not a
 * rebuild-introduced typo. */
export interface ClinicMasterSavePayload {
  clinicCode: string;
  clinicDescription: string;
  floorId: number;
  wingId: number;
  created_by: number;
  updated_by: number;
  cerated_date: string;
  updated_date: string;
  organizationId: number;
  unitId: number;
  status: 'A';
}

/** POST body for updateClinicMaster (ctrl:218-248). Legacy's
 * `updateClinicMaster()` overwrites BOTH `cerated_date` and `updated_date`
 * to "now" before sending (ctrl:221-222) -- confirmed the backend DAO
 * (`ClinicMasterDaoImpl.updateClinicMaster`) genuinely persists whatever
 * `cerated_date` it's given, so every update silently overwrites the
 * original creation timestamp. Reproduced verbatim, not fixed -- a real,
 * pre-existing legacy data-quality quirk (not visible anywhere in this
 * screen's own UI, which never displays created/updated dates), not
 * something this rebuild introduces or corrects on its own initiative. */
export interface ClinicMasterUpdatePayload {
  clinicMasterId: number;
  clinicCode: string;
  clinicDescription: string;
  floorId: number;
  wingId: number;
  created_by: number;
  updated_by: number;
  cerated_date: string;
  updated_date: string;
  organizationId: number;
  unitId: number;
  status: ClinicMasterStatus;
}
