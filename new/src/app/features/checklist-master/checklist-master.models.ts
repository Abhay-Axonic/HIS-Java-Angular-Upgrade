export type ActiveStatus = 'A' | 'I';

/**
 * Field names read directly from `checklistMasterController.js` (legacy/
 * HIS-WAR/.../controller/otims/global/checklistMasterController.js) and the
 * backend DTOs (`legacy/Global/.../otims/dto/ChecklistMasterDto.java`,
 * `ChecklistMasterDetailsDto.java`) -- not guessed.
 */

/** Stage dropdown -- fixed, hardcoded options in legacy's own view (view:71-77),
 * NOT fetched from any endpoint. Same numbering reused for the read-only
 * table's stage-name display (view:167-174) -- confirmed identical in both
 * places, no vocabulary drift. */
export const STAGE_OPTIONS: ReadonlyArray<{ id: number; label: string }> = [
  { id: 1, label: 'Consent' },
  { id: 2, label: 'Pre-Operative' },
  { id: 3, label: 'Intra-Operative' },
  { id: 4, label: 'Post-Operative' },
  { id: 5, label: 'Discharge' },
  { id: 6, label: 'Ward' },
];

/** Row shape returned by `getAllChecklistMasterList`
 * (ChecklistMasterController.java:48-56 -> ChecklistMasterServiceImpl ->
 * IChecklistMasterDao). `isWho` is rendered as a raw `{{info.isWho}}`
 * literal in legacy's own table cell (view:178, a boolean straight from
 * the DB) -- reproduced the same way, not converted to a Yes/No label
 * legacy itself doesn't show. */
export interface ChecklistMasterRow {
  checklistMasterId: number;
  code: string;
  description: string;
  roleId: number;
  roleName: string;
  stageId: number;
  isWho: boolean;
  status: ActiveStatus;
}

/** Role dropdown option -- `getActiveOTRoleList`
 * (`UserRoleMapperContrtoller.java:209-217`). */
export interface OtimsRole {
  roleId: number;
  roleName: string;
}

/** Autocomplete suggestion shape returned by `searchChecklistItemName`
 * (ChecklistMasterController.java, backed by the SEPARATE `m_checklist_item_master`
 * table -- items are created/maintained on the dedicated `checklistItemMaster`
 * screen, not here). */
export interface ChecklistItemSuggestion {
  checklistItemMasterId: number;
  description: string;
}

/**
 * One row of `checkListItemNameList` -- the in-progress, client-side-only
 * list built via `addChecklistItemName()`/`remove()` (ctrl:143-170) and
 * submitted wholesale as `checklistMasterDetails` on Save. Also reused,
 * unchanged, as the shape returned by `getChecklistMasterDetailsByMasterId`
 * when editing (ctrl:274-294, label==2 branch) -- legacy assigns the raw
 * fetched list directly into `checkListItemNameList`, so both paths share
 * one shape.
 *
 * **`description` here is DISPLAY-ONLY and is never persisted itself.**
 * Confirmed by reading `ChecklistMasterDetailsDaoImpl.saveChecklistMasterDetails`
 * (legacy/OTIMS/.../dao/ChecklistMasterDetailsDaoImpl.java:33-52): the
 * `ChecklistMasterDetails` entity being saved has NO `description` field or
 * setter at all (confirmed: `ChecklistMasterDetails.java` model has no
 * `description` column) -- only `checklistItemMasterId` (a real FK to
 * `otims.m_checklist_item_master`) is written. On read, the description
 * shown is JOINed live from `checklistItemMaster.description` via that FK
 * (`GET_CHECKLIST_MASTER_DETAILS_LIST_BY_MASTER_ID`,
 * `ChecklistMasterDetails.java:22-33`). See the component's class doc
 * comment for why this is central to a severity-1 finding in this screen's
 * Add-Checklist-Item flow.
 */
export interface ChecklistItemDetail {
  checklistMasterDetailsId: number | null;
  checklistItemMasterId: number;
  description: string;
  status: ActiveStatus;
}

/** Editable form state -- mirrors `$scope.checklistObj` (ctrl:42-57), minus
 * the audit/org/unit fields the component adds back in at save time. */
export interface ChecklistMasterForm {
  checklistMasterId: number | null;
  code: string;
  description: string;
  roleId: number | null;
  stageId: number | null;
  isWho: boolean;
  status: ActiveStatus;
}

export function emptyChecklistMasterForm(): ChecklistMasterForm {
  return {
    checklistMasterId: null,
    code: '',
    description: '',
    roleId: null,
    stageId: null,
    isWho: false,
    status: 'A',
  };
}

/** POST body for `saveChecklistMaster` (ctrl:172-233). */
export interface ChecklistMasterSavePayload {
  checklistMasterId: number | null;
  code: string;
  description: string;
  roleId: number;
  stageId: number | null;
  isWho: boolean;
  checklistMasterDetails: ChecklistMasterDetailSavePayload[];
  createdBy: number;
  createdDate: string;
  status: ActiveStatus;
  updatedBy: number;
  updatedDate: string;
  unitId: number;
  organizationId: number;
}

/** One entry of `checklistMasterDetails` on the save payload -- mirrors
 * `newdata` built in `addChecklistItemName()` (ctrl:148-160). `description`
 * is included for wire-parity with legacy's real payload shape (legacy
 * sends it too) even though the backend silently ignores it (see
 * `ChecklistItemDetail`'s doc comment) -- kept here for byte-for-byte
 * request parity, not because it does anything server-side. */
export interface ChecklistMasterDetailSavePayload {
  checklistItemMasterId: number;
  description: string;
  checklistMasterDetailsId: number | null;
  createdBy: number;
  createdDate: string;
  status: ActiveStatus;
  updatedBy: number;
  updatedDate: string;
  unitId: number;
  organizationId: number;
}

/** POST body for `updateChecklistMasterStatus` (ctrl:235-270). */
export interface ChecklistMasterStatusPayload {
  updatedBy: number;
  updatedDate: string;
  status: ActiveStatus;
  checklistMasterId: number;
}

/**
 * `saveChecklistMaster`'s response has a THIRD real status value beyond the
 * shared `ApiResponse<'success'|'error'>` contract: `ChecklistMasterDaoImpl
 * .saveChecklistMaster` (:32-90) returns the literal
 * `ICommonConstants.ALREADY_EXIST` ("Already Exists.") on a real,
 * org+unit-scoped Code/Description uniqueness violation -- confirmed the
 * frontend's own literal string check (`response.data.status == 'Already
 * Exists.'`, ctrl:222) matches this constant byte-for-byte, so this is a
 * genuinely live branch, not dead code. Modeled as its own response type
 * here rather than widening the shared `ApiResponse` status union (which
 * every other screen in this codebase also uses) for one endpoint's extra
 * value.
 */
export interface ChecklistMasterSaveResponse {
  status: 'success' | 'error' | 'Already Exists.';
  message?: string;
}
