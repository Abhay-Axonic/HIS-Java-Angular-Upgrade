/**
 * Field shapes taken directly from
 * legacy/Global/src/main/java/com/param/global/unit/dto/DoctorCabinMapperDto.java
 * and the `@NamedQuery`/`@NamedNativeQuery` aliases in
 * legacy/Entity/src/main/java/com/param/global/model/DoctorCabinMapper.java --
 * not guessed. See doctor-cabin-mapper.ts's class doc for the two silent
 * bugs reproduced against these exact fields.
 */

/** Left-hand "User" <select> options -- GET_LIST_DOCTOR (native query), filtered
 * client-side to isRMO !== 'Y' (ctrl:132-148, doctorCabinMapperController.js). */
export interface DoctorOption {
  doctorId: number;
  doctorName: string;
  isRMO: 'Y' | 'N';
}

/** Left-hand cabin checkbox list -- `/api/global/unit/getActiveCabinMaster`
 * (searchGroupList, ctrl:79-104). Not served by DoctorCabinMapperController --
 * a separate CabinMaster endpoint, path taken verbatim from the controller. */
export interface CabinOption {
  cabinId: number;
  cabinDesc: string;
}

/** An entry in the working list (`docSelectCabin`) or the committed list
 * (`selectDoctorCabinMapperList`) -- see addCabin()/selectCabin() (ctrl:110-128). */
export interface SelectedCabin {
  cabinId: number;
  cabinDesc: string | undefined;
}

/** GET_DOCTOR_CABIN_MAPPER_LIST row (the bottom "commonList" table, distinct
 * per doctor). */
export interface DoctorCabinMapperRow {
  doctorId: number;
  doctorName: string;
  status: 'A' | 'I';
}

/** GET_DOCTOR_CABIN_MAPPER_BY_ID's listObject item -- real DTO/query fields
 * are `doctorId`/`cabinId`/`cabinDesc` (DoctorCabinMapperDto.java +
 * GET_DOCTOR_CABIN_MAPPER_BY_ID's `cabinMaster.cabinDesc AS cabinDesc`).
 * There is no `diagnosisDesc` field anywhere in this DTO/query -- see
 * doctor-cabin-mapper.ts's `edit()` for why that matters. */
export interface DoctorCabinMapperByIdItem {
  doctorId: number;
  cabinId: number;
  cabinDesc: string;
}

/** GET_DETAILED_CABIN_LIST's listObject item -- the info-icon "Details" modal
 * (view:150-191). Correctly named `cabinDesc` in both the query and the view
 * binding (`{{info.cabinDesc}}`, view:177) -- no bug in this one. */
export interface DetailedCabinItem {
  cabinId: number;
  cabinDesc: string;
}

/** Body for ADD_DOCTOR_CABIN_MAPPER / UPDATE_DOCTOR_CABIN_MAPPER (ctrl:272-284/401-413).
 * Field names/casing (`organisationId`, not `organizationId`) match
 * DoctorCabinMapperDto.java verbatim -- required for correct JSON
 * deserialization server-side. */
export interface DoctorCabinMapperSavePayload {
  /** Legacy sends whatever the plain `<select>` gave it -- always a string
   * (ctrl:360 explicitly coerces with `+ ""` on edit-populate too). */
  doctorId: string;
  organisationId: number;
  unitId: number;
  status: 'A';
  createdBy: number;
  createdDate: string;
  updatedBy: number;
  updatedDate: string;
  selectDoctorCabinMapperList: number[];
}
