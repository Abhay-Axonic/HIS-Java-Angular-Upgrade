export type DistrictStatus = 'A' | 'I';

export interface CountryOption {
  countryId: number;
  countryName: string;
}

export interface StateOption {
  stateId: number;
  stateName: string;
}

/** Row shape returned by getDistrictMasterList (ctrl:110-158) -- the fields
 * the legacy view actually renders (district-master.html:187-199). */
export interface DistrictRow {
  districtId: number;
  countryName: string;
  stateName: string;
  districtCode: string;
  districtName: string;
  status: DistrictStatus;
}

/** Response shape of getDistrictById (ctrl:308-342, `GETSINGLEDISTRICT`,
 * `response.data.listObject[0]`) -- countryId/stateId come back as numbers
 * but are immediately `.toString()`-ed onto the form (ctrl:331-336) since
 * the Country/State `<select>` values are string-typed. */
export interface DistrictDetail {
  districtId: number;
  districtCode: string;
  districtName: string;
  countryId: number;
  stateId: number;
}

/** Mirrors `$scope.district` (form state only, ctrl:51). */
export interface DistrictForm {
  districtId?: number;
  countryId: string;
  stateId: string;
  districtCode: string;
  districtName: string;
}

/** Matches `$scope.district = {}` (ctrl:51) -- every field starts genuinely
 * empty, not defaulted. */
export function emptyDistrictForm(): DistrictForm {
  return {
    districtId: undefined,
    countryId: '',
    stateId: '',
    districtCode: '',
    districtName: '',
  };
}

/** POST body for saveDistrictMaster (ctrl:248-259). */
export interface DistrictSavePayload {
  districtCode: string;
  districtName: string;
  countryId: string;
  stateId: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  updatedDate: string;
  createdDate: string;
}

/** POST body for updateDistrictMaster (ctrl:391-400) -- no `createdBy`/
 * `status`, matching legacy exactly (only present on the create payload). */
export interface DistrictUpdatePayload {
  districtCode: string;
  districtName: string;
  countryId: string;
  stateId: string;
  districtId: number;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

/** POST body for updateStatusForDistrict (ctrl:352-358). */
export interface DistrictStatusPayload {
  districtId: number;
  status: DistrictStatus;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}
