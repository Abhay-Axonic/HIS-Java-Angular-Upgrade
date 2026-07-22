/**
 * Field set taken directly from
 * views/master/UnitMaster/organizationMaster/icpc-codes-configuration.html +
 * resources/assets/js/controller/master/unit/icpcCodesConfigurationController.js
 * (`icpcCodesConfigurationController`, classic `$scope`, no `controllerAs`).
 * This screen is pure org/unit-scoped REFERENCE/CONFIGURATION metadata --
 * it maps an "ICPC System" to one or more hospital Specialties. Confirmed
 * via full controller + DTO + entity read (Java side:
 * `ICPCCodesConfigurationMapperDto`/`ICPCCodeConfiguration`/
 * `ICPCCodesConfigurationMapper`): no `patientId`/`encounterId`/`visitId`
 * anywhere in this screen's real request/response payloads -- see the
 * class doc comment in icpc-codes-configuration.ts for the full
 * clinical-content verdict.
 */

/** Dropdown option for the "System" select (shared GETICPCSYSTEMLIST
 * endpoint, same one ICPCMapper uses). */
export interface IcpcSystemOption {
  icpcSystemId: number;
  icpcSystemDesc: string;
}

/** Master + selected-list shape for the Specialty multi-select (mapped
 * id/label the same way the legacy controller maps
 * `getActiveSpecialityList`'s `specialityId`/`specialityName`, ctrl:80-86). */
export interface SpecialityOption {
  id: number;
  label: string;
}

/** One row of the main table -- GET_ICPC_CODES_CONFIGURATION_MAPPER_LIST's
 * actual column aliases (Entity/ICPCCodeConfiguration.java): `icpcSystemId`,
 * `icpcCodeConfigurationId`, `status`, `systemName`. The ENTIRE active-status
 * list is fetched in one call (no offset/page params exist on this
 * endpoint at all) and paginated client-side, matching legacy's own
 * `PaginationService` (fetch-all + `Array.slice`), not a rebuild
 * simplification. */
export interface IcpcCodesConfigurationRow {
  icpcSystemId: number;
  icpcCodeConfigurationId: number;
  status: 'A' | 'I';
  systemName: string;
}

/** One entry of the "Specialty Details" read-only popup list
 * (GET_SPECIALITY_LIST_BY_SYSTEM_ID's real aliases:
 * `icpcCodeConfigurationMpprId`/`specialityId`/`specialityName`). */
export interface IcpcConfigurationSpecialityDetail {
  icpcCodeConfigurationMpprId: number;
  specialityId: number;
  specialityName: string;
}

/** Mirrors `saveICPCCodesConfigurationMapper()`'s `data` object
 * (ctrl:134-145). `icpcCodeConfigurationId` is `null` on create, and the
 * row's real id on edit (ctrl:378). */
export interface IcpcCodesConfigurationSavePayload {
  organizationId: number;
  unitId: number;
  specialityIdList: number[];
  icpcSystemId: number;
  createdBy: number;
  updatedBy: number;
  status: 'A';
  createdDate: string;
  updatedDate: string;
  icpcCodeConfigurationId: number | null;
}

/** Mirrors `updateStatus()`'s `data` object (ctrl:340-345). Field is named
 * `icpcConfigurationId` here even though the value passed in is the row's
 * `icpcCodeConfigurationId` (ctrl:335, view:91) -- the real parent-table
 * primary key, correctly matched on the Java side
 * (`ICPCCodesConfigurationDaoImpl.updateICPCCodesConfigurationStatus`
 * does `findById(...getIcpcConfigurationId())` against the
 * `ICPCCodeConfiguration` entity). This is a DIFFERENT, and correct, key
 * usage from the save path's FK bug described in the class comment. */
export interface IcpcCodesConfigurationStatusPayload {
  updatedDate: string;
  updatedBy: number;
  status: 'A' | 'I';
  icpcConfigurationId: number;
}

/** Mirrors `getSpecialityListByID()`'s request payload (ctrl:294-298) --
 * the field named `icpcConfigurationId` here is actually populated from the
 * row's `icpcSystemId` (ctrl:297, view:88/98), not its
 * `icpcCodeConfigurationId`. See class comment: this is intentional-looking
 * but almost certainly a real legacy bug, faithfully reproduced because it
 * is entirely a server-side/Java-side quirk this rebuild cannot alter
 * without changing behavior. */
export interface SpecialityListByIcpcSystemRequest {
  organizationId: number;
  unitId: number;
  icpcConfigurationId: number;
}
