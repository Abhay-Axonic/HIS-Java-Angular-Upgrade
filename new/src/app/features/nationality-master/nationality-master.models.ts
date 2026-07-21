export interface CountryOption {
  countryId: number;
  countryName: string;
}

/**
 * Row shape returned by GETNATIONALITYLIST / GETSINGLENATIONALITY --
 * confirmed identical column set for both (`GET_NATIONALITY_LIST` and
 * `GET_NATIONALITY_LIST_BY_ID` named queries both select
 * nationalityId/nationalityName/nationalityCode/status/countryId/countryName,
 * see Entity's NationalityMaster.java). Reused for both the list and the
 * single-record fetch rather than declaring a separate "detail" shape.
 */
export interface NationalityRow {
  nationalityId: number;
  nationalityCode: string;
  nationalityName: string;
  countryId: number;
  countryName: string;
  status: 'A' | 'I';
}

/** Editable form state -- mirrors legacy's `$scope.nationality`. */
export interface NationalityRecord {
  nationalityId?: number;
  countryId: number | '';
  nationalityCode: string;
  nationalityDesc: string;
}

export function emptyNationality(): NationalityRecord {
  return {
    nationalityId: undefined,
    countryId: '',
    nationalityCode: '',
    nationalityDesc: '',
  };
}
