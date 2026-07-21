export type RaceStatus = 'A' | 'I';

/** Row shape returned by getRaceMasterList (ctrl:87-140, view:151-162). */
export interface RaceRow {
  raceId: number;
  raceCode: string;
  /** Legacy's own list field name -- the form label is "Race Description"
   * (`race.raceDesc`) but the list/API field is `raceName`, a genuine
   * naming split preserved verbatim (see RaceForm below). */
  raceName: string;
  isDefault: RaceStatus;
  status: RaceStatus;
}

/** Shape returned by getRaceById (ctrl:265-293), assigned field-by-field
 * (not wholesale) onto `$scope.race`. */
export interface RaceDetail {
  raceId: number;
  raceCode: string;
  raceName: string;
  isDefault: RaceStatus;
}

/** Editable form state -- mirrors `$scope.race` (ctrl:74-76, `init()`). */
export interface RaceForm {
  raceId?: number;
  raceCode: string;
  /** Bound to the "Race Description" input; mapped to the API's `raceName`
   * field on save/update (ctrl:216-217/346-347). */
  raceDesc: string;
  isDefault: RaceStatus;
}

/** Matches ctrl:74-76's reset shape exactly (`race = { isDefault: 'I' }`,
 * code/desc implicitly undefined until typed). */
export function emptyRaceForm(): RaceForm {
  return {
    raceId: undefined,
    raceCode: '',
    raceDesc: '',
    isDefault: 'I',
  };
}

/** POST body for saveRaceMaster (ctrl:215-225). */
export interface RaceSavePayload {
  raceCode: string;
  raceName: string;
  status: 'A';
  updatedBy: number;
  createdBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
  isDefault: RaceStatus;
}

/** POST body for updateRaceMaster (ctrl:345-353) -- no `createdBy`/`status`,
 * matching legacy exactly (only present on the create payload). */
export interface RaceUpdatePayload {
  raceCode: string;
  raceName: string;
  raceId: number;
  updatedBy: number;
  updatedDate: string;
  isDefault: RaceStatus;
  organizationId: number;
}

/** POST body for updateStatusForRace (ctrl:302-308). */
export interface RaceStatusPayload {
  raceId: number;
  status: RaceStatus;
  updatedBy: number;
  updatedDate: string;
}
