export type AmenityStatus = 'A' | 'I';

/** Row shape returned by getAmenityMasterList (ctrl:79-96) -- the only
 * fields the legacy view renders (amenity-master.html:118-126). */
export interface AmenityRow {
  amenityId: number;
  amenityCode: string;
  amenityDesc: string;
  status: AmenityStatus;
}

/** Shape returned by getAmenityMasterListById (ctrl:198-226) -- assigned
 * wholesale to `$scope.common` off `listObject[0]`, matching legacy. */
export interface AmenityDetail {
  amenityId: number;
  amenityCode: string;
  amenityDesc: string;
}

/** Editable form state -- mirrors legacy's `$scope.common`
 * (ctrl:41-47, `initMaster()`). */
export interface AmenityForm {
  amenityId?: number;
  amenityCode: string;
  amenityDesc: string;
}

/** Matches ctrl:41-45's `initMaster()` reset shape exactly
 * (`amenityId:"", amenityCode:"", amenityDesc:""`). */
export function emptyAmenityForm(): AmenityForm {
  return {
    amenityId: undefined,
    amenityCode: '',
    amenityDesc: '',
  };
}

/** POST body for saveAmenityMaster (ctrl:149-159). */
export interface AmenityMasterSavePayload {
  amenityCode: string;
  amenityDesc: string;
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  status: 'A';
  organizationId: number;
  unitId: number;
  updatedDate: string;
}

/** POST body for updateAmenityMaster (ctrl:236-244) -- no `createdBy`/
 * `status`, matching legacy exactly (only present on the create payload). */
export interface AmenityMasterUpdatePayload {
  amenityCode: string;
  amenityDesc: string;
  amenityId: number;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for updateAmenityMasterStatus (ctrl:285-290, `wingObj` --
 * legacy's own variable name, kept in code comments only, not reproduced as
 * a field name here). */
export interface AmenityMasterStatusPayload {
  amenityId: number;
  status: AmenityStatus;
  updatedDate: string;
  updatedBy: number;
}
