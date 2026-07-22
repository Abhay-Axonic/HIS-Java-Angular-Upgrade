/**
 * Field names/types read directly from
 * MasterOfSytemDto.java (legacy/Global/src/main/java/com/param/global/dto/MasterOfSytemDto.java),
 * SystemMasterDaoImpl.java (GET_LIST_OF_SYSTEM_MASTER_BY_ID +
 * `m_system_master_gender_mapper` join query), and
 * systemMasterController.js -- not guessed.
 */

/** Legacy's own hardcoded list ($scope.examinationList, ctrl:132-146). */
export const EXAMINATION_TYPES = [
  { typeId: 3, typeName: 'Both' },
  { typeId: 1, typeName: 'General Examination' },
  { typeId: 2, typeName: 'Systemic Examination' },
] as const;

export interface SystemMasterFormRecord {
  systemId: number | null;
  systemCode: string;
  systemName: string;
  typeId: number | null;
}

export function emptySystemMaster(): SystemMasterFormRecord {
  return {
    systemId: null,
    systemCode: '',
    systemName: '',
    typeId: null,
  };
}

/** Shape of one row in `getListOfSystemMaster`'s `listObject` (ctrl:126). */
export interface SystemMasterListRow {
  systemId: number;
  systemCode: string;
  systemName: string;
  typeId: number;
  status: 'A' | 'I';
}

/**
 * Shape of `getListOfSystemMasterById`'s `object`
 * (SystemMasterDaoImpl.getListOfSystemMasterById -> `new Response(SUCCESS,
 * null, null, genderList, listSystemMaster)`, i.e. `object` = the
 * `MasterOfSytemDto` record itself, `listObject` = the gender mappings --
 * see SystemGenderMapping below).
 */
export interface SystemMasterDetail {
  systemId: number;
  systemCode: string;
  systemName: string;
  typeId: number;
  status: 'A' | 'I';
}

/**
 * Shape of `getListOfSystemMasterById`'s `listObject` items -- the named
 * SQL query aliases columns as `systemId`/`id` (the gender_id)/`genderDesc`
 * (DaoImpl.java ~194-202). Sent back to `SAVESYSTEMMASTER`/`UPDATESYSTEMMASTER`
 * as `genderIdList`; only `.id` is ever read server-side
 * (`genderListObj.getId()`, DaoImpl.java ~118/232) -- `genderDesc`/`label`
 * are round-tripped but never consumed on save.
 */
export interface SystemGenderMapping {
  systemId?: number;
  id: number;
  genderDesc?: string;
  label?: string;
}

/** Raw shape of `/adt/getActiveGenderList`'s `listObject` items (GenderMasterDto: `id`/`desc`). */
export interface ActiveGenderOption {
  id: number;
  desc: string;
}

/** Shape of `/api/global/getSystemGenderListById`'s `listObject` items (ctrl:306, `info.genderDesc`). */
export interface SystemGenderDetailRow {
  genderDesc: string;
}
