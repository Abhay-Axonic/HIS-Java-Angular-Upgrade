/**
 * Field set taken directly from `SystemObservationMasterDto.java`
 * (legacy/Global/src/main/java/com/param/global/dto/SystemObservationMasterDto.java)
 * and resources/assets/js/controller/opd/global/systemObservationMasterController.js.
 *
 * The backing named query (`GET_LIST_SYSTEM_OBSERVATION_PROPERTY`,
 * Entity/.../model/SystemObervationMaster.java:25-38) LEFT OUTER JOINs the
 * property-master child table, so the flat list returned by
 * `getListOfSystemObservationProperty` has ONE ROW PER (observation,
 * property) PAIR -- an observation with 3 properties yields 3 rows sharing
 * the same `observationId`/`observationName`/etc. but different
 * `propertyId`/`propertyName`/`propertyStatus`. Legacy's own controller
 * (ctrl:149-157) deduplicates this by `observationId` for the main table
 * and keeps the full flat list around to filter by `observationId` for the
 * "Property Details" info popup (`displayNote`, ctrl:234-257) -- this
 * rebuild mirrors that exact two-shapes-from-one-response pattern rather
 * than assuming the backend returns one row per observation.
 *
 * Also preserved: the named query filters ONLY by `organizationId` -- the
 * `unitId` filter is commented out server-side (model:36) -- so this list
 * is genuinely ORG-WIDE, not unit-scoped, matching legacy exactly (not a
 * rebuild simplification).
 */
export interface SystemObservationFlatRow {
  observationId: number;
  observationName: string;
  systemId: number;
  systemName: string;
  observationCode: string;
  isPropertyRequired: 'Y' | 'N';
  specialityIds: string;
  status: 'A' | 'I';
  unitId: number;
  organizationId: number;
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  updatedDate: string;
  propertyId: number | null;
  propertyName: string | null;
  propertyStatus: 'A' | 'I' | null;
}

/** One deduplicated row of the main table (one per `observationId`). */
export interface SystemObservationRow {
  observationId: number;
  observationName: string;
  systemId: number;
  systemName: string;
  observationCode: string;
  isPropertyRequired: 'Y' | 'N';
  specialityIds: string;
  status: 'A' | 'I';
}

/** Static, hardcoded "Type" dropdown -- NOT fetched from any backend
 * endpoint (ctrl:93-107, `$scope.examinationList`). The commented-out
 * `GETLISTOFSYSTEMMASTER` call (ctrl:121-133) confirms this was probably a
 * later simplification, but it's what legacy actually ships today. */
export const EXAMINATION_TYPES = [
  { typeId: 3, typeName: 'Both' },
  { typeId: 1, typeName: 'General Examination' },
  { typeId: 2, typeName: 'Systemic Examination' },
] as const;

/** Options for the "System" dropdown, populated by `getListOfSystemMasterByType`
 * once a Type is selected (ctrl:203-232). */
export interface SystemOption {
  systemId: number;
  systemName: string;
}

/** Speciality multiselect option -- mapped from `getActiveSpecialityList`'s
 * `specialityId`/`specialityName` the same way the legacy controller does
 * (ctrl:180-187), and only for `isClinicalSpeciality=='Y'` rows. */
export interface SpecialityOption {
  id: number;
  label: string;
}

/** Raw `getActiveSpecialityList` row, kept in FULL (unfiltered by
 * `isClinicalSpeciality`) because `displaySpecialities()` (ctrl:259-277)
 * looks up the "Specialities" info popup's names against
 * `$scope.specialityMasterList` -- the full, unfiltered response -- not
 * against the clinical-only-filtered `$scope.specialityList` used for the
 * multiselect dropdown's own options. Two different derived views of the
 * same one API response, exactly as legacy keeps them. */
export interface RawSpecialityOption {
  specialityId: number;
  specialityName: string;
  isClinicalSpeciality: 'Y' | 'N';
}

/** One property-name row in the create-mode "Property Required" list
 * (`$scope.oservationPropertyObjList`, ctrl:89-91/280-306). Legacy's own
 * update-mode equivalent (`addUpdate`/`removeUpdate`, ctrl:309-336) is
 * DEAD CODE -- see class comment in system-observation-master.ts --  so
 * this rebuild only implements the create-mode add/remove behavior. */
export interface PropertyDraft {
  propertyName: string;
}

/** One row of the "Property Details" info popup (`displayNote`, ctrl:234-257). */
export interface PropertyDetail {
  propertyId: number | null;
  observationId: number;
  propertyName: string | null;
  status: 'A' | 'I' | null;
}

/** One row of the "Specialities" info popup (`displaySpecialities`, ctrl:259-277). */
export interface SpecialityDetail {
  id: number;
  specialityName: string;
}

/** Mirrors `$scope.observationObj` on save (ctrl:59-71/335-373). */
export interface SaveSystemObservationPayload {
  systemId: number | '';
  observationCode: string;
  observationName: string;
  isPropertyRequired: 'Y' | 'N';
  specialityIds: string;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  createdBy: number;
  status: 'A';
  createdDate: string;
  updatedDate: string;
  listSystemPropertyDto: PropertyDraft[];
}
