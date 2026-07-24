/**
 * Field names/types read directly from `GeneralLedgerMasterDto.java`
 * (legacy/Global/src/main/java/com/param/global/org/dto/GeneralLedgerMasterDto.java)
 * and `gLcodeMasterController.js` -- not guessed.
 *
 * Legacy's own form object is `$scope.common` (ctrl:40-46):
 * `{ generalLedgerId:"", generalLedgerCode:"", generalLedgerName:"" }` --
 * every field starts as an empty string, including the id. `unitId` is read
 * from the login cookie (ctrl:29) but never sent on the wire anywhere in
 * this controller (every `unitId:` reference in every payload is commented
 * out, ctrl:69/74/119/172) -- this screen operates at the ORGANIZATION
 * level only, confirmed by `GET_GENERAL_LEDGER_MASTER_LIST`/`_COUNT` both
 * filtering by `organizationId` alone (GeneralLedgerMaster.java:41-47,
 * GeneralLedgerMasterServiceDaoImpl.java:206-216).
 */
export interface GlCodeForm {
  generalLedgerId?: number;
  generalLedgerCode: string;
  generalLedgerName: string;
}

export function emptyGlCodeForm(): GlCodeForm {
  return { generalLedgerId: undefined, generalLedgerCode: '', generalLedgerName: '' };
}

/** Shape of one row in `getGeneralLedgerMasterList`'s `listObject`
 * (GeneralLedgerMaster.java:41-47, named query aliases exactly these 4
 * fields) -- `status` is a real single-char 'A'/'I' column (`Character` on
 * the entity/DTO), not a boolean like several sibling screens. */
export interface GlCodeRow {
  generalLedgerId: number;
  generalLedgerCode: string;
  generalLedgerName: string;
  status: 'A' | 'I';
}

/** Shape of `getGeneralLedgerMasterById`'s `listObject[0]`
 * (GeneralLedgerMaster.java:32-39) -- same 4 fields as the list row. */
export type GlCodeDetail = GlCodeRow;

/** POST body for `saveGeneralLedgerMaster` (ctrl:156-215, payload at
 * ctrl:165-176). No `generalLedgerId` -- DB-generated
 * (`general_ledger_master_seq`). Legacy hardcodes `status:'A'` on every
 * create (ctrl:170), never reads it from the form (there is no status
 * input on create). */
export interface GlCodeSavePayload {
  generalLedgerCode: string;
  generalLedgerName: string;
  status: 'A';
  organizationId: number;
  createdBy: number;
  createdDate: string;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `updateGeneralLedgerMaster` (ctrl:249-305, payload at
 * ctrl:255-262) -- no `organizationId`/`status`/`createdBy`/`createdDate`,
 * matching legacy exactly (the backend's own `updateGeneralLedgerMaster`
 * DAO method never touches `status` or `organisation_id` on update either,
 * GeneralLedgerMasterServiceDaoImpl.java:137-153). */
export interface GlCodeUpdatePayload {
  generalLedgerId: number;
  generalLedgerCode: string;
  generalLedgerName: string;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `updateGeneralLedgerMasterStatus` (ctrl:307-343, payload
 * at ctrl:314-319) -- legacy sends exactly these 4 fields here, no other
 * form fields replayed. */
export interface GlCodeStatusPayload {
  generalLedgerId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}
