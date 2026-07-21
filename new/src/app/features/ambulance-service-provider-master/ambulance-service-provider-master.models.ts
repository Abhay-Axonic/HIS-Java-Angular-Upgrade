export type ServiceProviderStatus = 'A' | 'I';

/** Row shape returned by getServiceProviderList (ctrl:260-288) -- the
 * fields the legacy view actually renders
 * (ambulance-Service-Provider-Master.html:387-407). */
export interface ServiceProviderRow {
  serviceProviderMasterId: number;
  serviceProviderName: string;
  serviceProviderAddress: string;
  serviceProviderAmbulanceType: string;
  serviceProviderPersonName: string;
  serviceProviderPhoneNo: string;
  serviceProviderCellNo: string;
  serviceProviderEmailAddress: string;
  serviceProviderContractFromDate: string;
  serviceProviderContractToDate: string;
  serviceProviderFirstDistance: string;
  serviceProviderFirstDistanceRate: string;
  serviceProviderAdditionalMileage: string;
  serviceProviderAdditionalMileageRate: string;
  status: ServiceProviderStatus;
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

/** Ambulance Tariff dropdown option (ctrl:84-98, "Ambulance Tariff Name" in
 * the view, view:134) -- a DIFFERENT list from this codebase's own
 * `ambulance-type-master` rows; it's `GETAMBULANCETARRIFACTIVELIST`
 * (`/adt/getAmbulanceTarrifActiveList`), the ambulance-tariff master, not
 * the ambulance-type master. */
export interface AmbulanceTariffOption {
  ambulanceTarrifMasterId: number;
  ambulanceType: string;
}

/** Response shape of getSingleAmbulanceTarrif (ctrl:229-249), used to
 * auto-fill 5 form fields when a tariff is selected -- see
 * `onAmbulanceTariffChange` in the component. All numeric-looking fields
 * are string here: they populate plain `<input type="text">` fields with
 * no `is-number`/`convert-to-number` anywhere on this form. */
export interface AmbulanceTariffDetail {
  ambulanceOutSrcVender: string;
  ambulanceFirstDist: string;
  ambulanceFirstDistRate: string;
  ambulanceAddMileage: string;
  ambulanceAddMileageRate: string;
}

/** Response shape of getSingleServiceProvider (ctrl:428-471,
 * `response.data.listObject[0]`) -- the full record, assigned field-by-field
 * onto `$scope.serviceProvider` with no `showUpdateBtn`-side cascade
 * re-fetch of state/district/city/area (see the component's class doc
 * comment for the real "Area dropdown can show empty on edit" quirk this
 * causes). Dates come back in legacy's stored `DD-MM-YYYY HH:mm:ss` format,
 * not ISO. */
export interface ServiceProviderDetail {
  serviceProviderMasterId: number;
  serviceProviderName: string;
  serviceProviderAddress: string;
  serviceProviderCountry: string;
  serviceProviderState: number;
  serviceProviderDistrict: number;
  serviceProviderCity: number;
  serviceProviderArea: number;
  serviceProviderZipCode: string;
  serviceProviderAmbulanceType: string;
  serviceProviderPersonName: string;
  serviceProviderPhoneNo: string;
  serviceProviderCellNo: string;
  serviceProviderEmailAddress: string;
  serviceProviderContractFromDate: string;
  serviceProviderContractToDate: string;
  serviceProviderFirstDistance: string;
  serviceProviderFirstDistanceRate: string;
  serviceProviderAdditionalMileage: string;
  serviceProviderAdditionalMileageRate: string;
}

/**
 * Mirrors `$scope.serviceProvider` (FORM state only). Unlike this
 * codebase's qms-department-master/modality-type-master (which reuse ONE
 * shared object as both form model and POST body), legacy builds a
 * SEPARATE literal for the actual save/update request (ctrl:306-332,
 * ctrl:519-543) -- same style as amenity-master. `serviceProviderCountry`/
 * `serviceProviderAmbulanceType` are typed `string`: those 2 selects have
 * NO `convert-to-number` (view:42/133). `serviceProviderState`/`District`/
 * `City`/`Area` ARE typed numeric: `convert-to-number` IS present on all 4
 * of those selects (view:57/72/89/104). Contract dates use the
 * established ISO (`yyyy-MM-dd`) `<input type="date">` convention for
 * legacy's `uib-datepicker-popup` fields (see `asset-issue-acceptance.ts`),
 * not a raw JS `Date`.
 */
export interface ServiceProviderForm {
  serviceProviderMasterId?: number;
  serviceProviderName: string;
  serviceProviderAddress: string;
  serviceProviderCountry: string;
  serviceProviderState: number | '';
  serviceProviderDistrict: number | '';
  serviceProviderCity: number | '';
  serviceProviderArea: number | '';
  serviceProviderZipCode: string;
  serviceProviderAmbulanceType: string;
  serviceProviderPersonName: string;
  serviceProviderPhoneNo: string;
  serviceProviderCellNo: string;
  serviceProviderEmailAddress: string;
  serviceProviderContractFromDate: string; // ISO yyyy-MM-dd
  serviceProviderContractToDate: string; // ISO yyyy-MM-dd
  serviceProviderFirstDistance: string;
  serviceProviderFirstDistanceRate: string;
  serviceProviderAdditionalMileage: string;
  serviceProviderAdditionalMileageRate: string;
}

/** Matches `$scope.serviceProvider = {}` (ctrl:41) -- every field starts
 * genuinely empty/unset, not defaulted, matching legacy exactly. */
export function emptyServiceProviderForm(): ServiceProviderForm {
  return {
    serviceProviderMasterId: undefined,
    serviceProviderName: '',
    serviceProviderAddress: '',
    serviceProviderCountry: '',
    serviceProviderState: '',
    serviceProviderDistrict: '',
    serviceProviderCity: '',
    serviceProviderArea: '',
    serviceProviderZipCode: '',
    serviceProviderAmbulanceType: '',
    serviceProviderPersonName: '',
    serviceProviderPhoneNo: '',
    serviceProviderCellNo: '',
    serviceProviderEmailAddress: '',
    serviceProviderContractFromDate: '',
    serviceProviderContractToDate: '',
    serviceProviderFirstDistance: '',
    serviceProviderFirstDistanceRate: '',
    serviceProviderAdditionalMileage: '',
    serviceProviderAdditionalMileageRate: '',
  };
}

/** POST body for saveServiceProvider (ctrl:306-332) -- a separate literal
 * from `$scope.serviceProvider`, no `serviceProviderMasterId`.
 *
 * Note for the record: legacy NEVER sends `unitId` anywhere in this
 * controller -- confirmed via full-file read, every list/count/save/
 * update/status/lookup call is organizationId-only. This is a real,
 * faithfully-reproduced divergence from this codebase's unit-scoped
 * siblings (modality-type-master, amenity-master), not a rebuild
 * simplification -- legacy's own `ambulanceServiceProviderController.js`
 * truly has no `unitId` anywhere in its source. */
export interface ServiceProviderSavePayload {
  serviceProviderName: string;
  serviceProviderAddress: string;
  serviceProviderCountry: string;
  serviceProviderState: number | '';
  serviceProviderDistrict: number | '';
  serviceProviderCity: number | '';
  serviceProviderArea: number | '';
  serviceProviderZipCode: string;
  serviceProviderAmbulanceType: string;
  serviceProviderPersonName: string;
  serviceProviderPhoneNo: string;
  serviceProviderCellNo: string;
  serviceProviderEmailAddress: string;
  serviceProviderContractFromDate: string; // DD-MM-YYYY HH:mm:ss
  serviceProviderContractToDate: string; // DD-MM-YYYY HH:mm:ss
  serviceProviderFirstDistance: string;
  serviceProviderFirstDistanceRate: string;
  serviceProviderAdditionalMileage: string;
  serviceProviderAdditionalMileageRate: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** POST body for updateServiceProvider (ctrl:519-543) -- no `createdBy`/
 * `status`, matching legacy exactly (only present on the create payload). */
export interface ServiceProviderUpdatePayload {
  serviceProviderMasterId: number;
  serviceProviderName: string;
  serviceProviderAddress: string;
  serviceProviderCountry: string;
  serviceProviderState: number | '';
  serviceProviderDistrict: number | '';
  serviceProviderCity: number | '';
  serviceProviderArea: number | '';
  serviceProviderZipCode: string;
  serviceProviderAmbulanceType: string;
  serviceProviderPersonName: string;
  serviceProviderPhoneNo: string;
  serviceProviderCellNo: string;
  serviceProviderEmailAddress: string;
  serviceProviderContractFromDate: string;
  serviceProviderContractToDate: string;
  serviceProviderFirstDistance: string;
  serviceProviderFirstDistanceRate: string;
  serviceProviderAdditionalMileage: string;
  serviceProviderAdditionalMileageRate: string;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

/** POST body for changeServiceProviderStatus (ctrl:474-490) -- no
 * `unitId`, same note as ServiceProviderSavePayload. */
export interface ServiceProviderStatusPayload {
  serviceProviderMasterId: number;
  status: ServiceProviderStatus;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}
