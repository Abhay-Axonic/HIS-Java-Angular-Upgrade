/**
 * Parity rebuild of views/master/UnitMaster/organizationMaster/referral-master.html
 * + resources/assets/js/controller/adt/global/referralMasterController.js.
 * Field names/shapes read directly from that controller and from
 * legacy/Global/src/main/java/com/param/global/dto/ReferralMasterDto.java
 * (not guessed) -- see referral-master.ts's class doc for the full audit
 * trail and the disclosed geo-cascade fix.
 */

export interface ReferralTypeOption {
  id: number;
  desc: string;
}

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

export interface AreaOption {
  areaId: number;
  areaName: string;
}

/** Mirrors legacy's `$scope.referral` form-state object. */
export interface ReferralMasterRecord {
  referralId: number | '';
  referralTypeId: number | '';
  referralName: string;
  address: string;
  email: string;
  contact: string;
  contactPerson: string;
  countryId: number | '';
  stateId: number | '';
  districtId: number | '';
  cityId: number | '';
  areaId: number | '';
}

export function emptyReferral(): ReferralMasterRecord {
  return {
    referralId: '',
    referralTypeId: '',
    referralName: '',
    address: '',
    email: '',
    contact: '',
    contactPerson: '',
    countryId: '',
    stateId: '',
    districtId: '',
    cityId: '',
    areaId: '',
  };
}

/**
 * List row shape returned by `getReferralMasterList` -- includes the
 * country/state/district/city/area NAME fields and `referralAddress`/
 * `postCode` even though the table itself only renders a subset of these;
 * the rest are read straight off the same row object by `PopUpDetails(info)`
 * (view line 400, the info-circle "Details" popup) with no separate API
 * call, so the row DTO must carry them.
 */
export interface ReferralMasterRow {
  referralId: number;
  referralTypeDesc: string;
  referralName: string;
  referralAddress: string;
  referralEmail: string;
  referralContact: string;
  referralContactPerson: string;
  status: 'A' | 'I';
  countryName?: string;
  stateName?: string;
  districtName?: string;
  cityName?: string;
  areaName?: string;
  postCode?: string;
}

/** `getReferralById` response shape (ctrl:386-413, `showUpdateBtn`). */
export interface ReferralDetail {
  referralId: number;
  /** Referral-TYPE id -- legacy reads this off `.id`, not `.referralTypeId` (ctrl:389, the same "need to verify" ambiguity `saveReferral` sends both under). */
  id: number;
  referralName: string;
  referralAddress: string;
  referralEmail: string;
  referralContact: string;
  referralContactPerson: string;
  countryId: number;
  stateId: number;
  districtId: number;
  cityId: number;
  areaId: number;
}
