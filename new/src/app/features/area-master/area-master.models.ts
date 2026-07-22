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

export interface CityOption {
  cityId: number;
  cityName: string;
}

/** Row shape returned by GETAREALIST -- already joined with country/state/district/city names. */
export interface AreaRow {
  areaId: number;
  areaCode: string;
  areaName: string;
  postCode: string;
  countryName?: string;
  stateName?: string;
  districtName?: string;
  cityName?: string;
  status: 'A' | 'I';
}

/** `response.data.listObject[0]` shape returned by GETSINGLEAREA (ctrl:344-386). */
export interface AreaDetail {
  areaId: number;
  areaCode: string;
  areaName: string;
  postCode: string;
  countryId: number;
  stateId: number;
  districtId: number;
  cityId: number;
}

/** Editable form state -- mirrors legacy's `$scope.area`. */
export interface AreaRecord {
  areaId?: number;
  countryId: number | '';
  stateId: number | '';
  districtId: number | '';
  cityId: number | '';
  areaCode: string;
  areaName: string;
  postCode: string;
}

export function emptyArea(): AreaRecord {
  return {
    areaId: undefined,
    countryId: '',
    stateId: '',
    districtId: '',
    cityId: '',
    areaCode: '',
    areaName: '',
    postCode: '',
  };
}
