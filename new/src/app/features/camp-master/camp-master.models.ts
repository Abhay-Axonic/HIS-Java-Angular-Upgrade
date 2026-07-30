/**
 * Parity rebuild of
 * views/master/UnitMaster/bloodBank/camp-master.html +
 * resources/assets/js/controller/bloodBank/campMasterController.js.
 * Field names/shapes read directly from that controller and from
 * legacy/Global/src/main/java/com/param/bloodBank/dto/CampMasterDto.java
 * plus the `GET_CAMP_MASTER_LIST` named query on
 * legacy/Entity/src/main/java/com/param/bloodBank/model/CampMaster.java
 * (not guessed) -- see camp-master.ts's class doc for the full audit trail
 * and the two disclosed fixes (list-refresh-after-save, geo-cascade).
 */

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

/** `/api/billing/getPatientType/{orgId}/{unitId}` response row (optional field, ctrl:166-184). */
export interface PatientTypeOption {
  patientTypeId: number;
  description: string;
}

/** Mirrors legacy's `$scope.camp` form-state object (ctrl:34-55). */
export interface CampMasterRecord {
  campId: number | '';
  campCode: string;
  campName: string;
  address: string;
  countryId: number | '';
  stateId: number | '';
  districtId: number | '';
  cityId: number | '';
  areaId: number | '';
  pincode: string;
  patientTypeId: number | '';
}

export function emptyCamp(): CampMasterRecord {
  return {
    campId: '',
    campCode: '',
    campName: '',
    address: '',
    countryId: '',
    stateId: '',
    districtId: '',
    cityId: '',
    areaId: '',
    pincode: '',
    patientTypeId: '',
  };
}

/**
 * List row shape returned by `getCampMasterListPaginationList`/
 * `getCampMasterListCount` -- the `GET_CAMP_MASTER_LIST` named query
 * (CampMaster.java:27-49) selects every id/name pair below in one INNER-
 * joined row, including `patientTypeName` via a LEFT JOIN (patient type is
 * optional). This is also the ONLY source used to populate the edit form
 * (`showUpdateBtn(info)`, ctrl:421-446 reads straight off the already-
 * fetched row) -- there is no separate "get camp by id" endpoint.
 */
export interface CampMasterRow {
  campId: number;
  campCode: string;
  campName: string;
  address: string;
  pincode: string;
  status: 'A' | 'I';
  countryId: number;
  countryName: string;
  stateId: number;
  stateName: string;
  districtId: number;
  districtName: string;
  cityId: number;
  cityName: string;
  areaId: number;
  areaName: string;
  patientTypeId?: number;
  patientTypeName?: string;
}
