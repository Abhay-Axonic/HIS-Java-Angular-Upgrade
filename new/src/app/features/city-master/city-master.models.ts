export interface CountryOption {
  countryId: number;
  countryName: string;
}

export interface StateOption {
  stateId: number;
  stateName: string;
}

export interface DistrictOption {
  districtId: number;
  districtName: string;
}

/** Row shape returned by GETCITYLIST -- already joined with country/state/district names. */
export interface CityRow {
  cityId: number;
  cityCode: string;
  cityName: string;
  countryName?: string;
  stateName?: string;
  districtName?: string;
  status: 'A' | 'I';
}

/** `response.data.listObject[0]` shape returned by GETSINGLECITY (ctrl:336-350). */
export interface CityDetail {
  cityId: number;
  cityCode: string;
  cityName: string;
  countryId: number;
  stateId: number;
  districtId: number;
}

/** Editable form state -- mirrors legacy's `$scope.city`. */
export interface CityRecord {
  cityId?: number;
  countryId: number | '';
  stateId: number | '';
  districtId: number | '';
  cityCode: string;
  cityName: string;
}

export function emptyCity(): CityRecord {
  return {
    cityId: undefined,
    countryId: '',
    stateId: '',
    districtId: '',
    cityCode: '',
    cityName: '',
  };
}
