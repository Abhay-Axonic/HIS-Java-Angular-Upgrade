/**
 * Field set taken directly from views/master/GlobalMaster/ICPC-mapper.html +
 * resources/assets/js/controller/master/global/iCPCMapperController.js
 * (`iCPCMapperController`, classic `$scope`, no `controllerAs`). This screen
 * is pure org/unit-scoped REFERENCE/CONFIGURATION metadata -- it maps an
 * "ICPC System" (a coding-standard variant, e.g. ICPC-1/ICPC-2) to one or
 * more Gender categories. Confirmed via full controller + DTO + entity read:
 * there is no patientId/encounterId/visitId/diagnosisId anywhere in this
 * screen's real request/response payloads -- see the class doc comment in
 * icpc-mapper.ts for the full clinical-content verdict.
 */

/** Dropdown option for the "ICPC System" select (GETICPCSYSTEMLIST). */
export interface IcpcSystemOption {
  icpcSystemId: number;
  icpcSystemDesc: string;
}

/** Master gender list for the multi-select checkbox panel
 * (GET /adt/getActiveGenderList) -- mapped id/label the same way the
 * legacy controller itself maps it (ctrl:71-77: `data.id = value.id;
 * data.label = value.desc;`). */
export interface GenderOption {
  id: number;
  label: string;
}

/** One row of the main table (GETICPCMAPPERLIST). */
export interface IcpcMapperRow {
  icpcSystemMapperId: number;
  icpcSystemId: number;
  icpcSystemDesc: string;
  status: 'A' | 'I';
}

/** One entry of the "Gender Details" read-only popup list
 * (GETGENDERLISTBYICPCSYSTEMID) and of `getICPCMapperById`'s nested
 * `genderList`. Real column aliases confirmed against
 * `GET_GENDER_LIST_BY_ICPC_SYSTEM_ID` in Entity/ICPCMapper.java:
 * `genderId`/`genderDesc` (NOT `id`/`desc` -- that shorter alias is only
 * used by the separate `/adt/getActiveGenderList` master-list endpoint). */
export interface IcpcMapperGenderDetail {
  genderId: number;
  genderDesc: string;
}

export interface IcpcMapperSavePayload {
  icpcSystemId: number;
  updatedBy: number;
  updatedDate: string;
  createdDate: string;
  createdBy: number;
  organizationId: number;
  unitId: number;
  status: 'A';
  genderList: { id: number }[];
}

export interface IcpcMapperUpdatePayload {
  icpcSystemMapperId: number;
  icpcSystemId: number;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
  unitId: number;
  status: 'A';
  genderList: { id: number }[];
}

export interface IcpcMapperStatusPayload {
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
  unitId: number;
  status: 'A' | 'I';
  icpcSystemMapperId: number;
}
