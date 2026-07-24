/**
 * Field set taken directly from
 * views/master/UnitMaster/Mortuary/Mortuary-Bed-Master.html +
 * resources/assets/js/controller/master/unit/mortuaryBedMasterController.js
 * (`mortuaryBedMasterController`, no `controllerAs`, 389 lines). This is the
 * MASTER/admin screen only -- MortuaryBedMaster.java's `mortuary_bed_id` /
 * `mortuary_bed_code` / `mortuary_bed_desc` / `mortuary_bed_number` columns
 * back it, but only 2 of those 3 are real EDITABLE form fields.
 *
 * `mortuaryBedNumber` is NEVER sent by the frontend on create -- it's
 * server-generated (`MortuaryBedMasterServiceImpl.generateBedNumber()`, a
 * random `MBED00XXXX` string) and only ever comes BACK from the list/count
 * endpoints for display (view:109, `{{info.mortuaryBedNumber}}`). It is not
 * part of `MortuaryBedForm` or either payload below, only `MortuaryBedRow`.
 *
 * Legacy's local scope var names for the 2 editable fields are `mortuaryCode`/
 * `mortuaryName` (`$scope.mortuaryObj.mortuaryCode`/`.mortuaryName`,
 * ctrl:204-205/346-347) -- NOT the wire field names. The real wire contract
 * (SAVEMORTUARYBEDS/UPDATEMORTUARYBEDMASTER request bodies,
 * GETMORTUARYBEDLIST/GETMOURTUARYBEDBYID response rows) uses
 * `mortuaryBedCode`/`mortuaryBedDesc` throughout -- kept as the form field
 * names here too, same simplification this codebase's floor-master already
 * made for the identical reason (dropping `floorDesc` for `floorName`).
 */
export type MortuaryBedStatus = 'A' | 'I';

/** Row shape returned by `getMortuaryBedList` (ctrl:91-138). */
export interface MortuaryBedRow {
  mortuaryBedId: number;
  mortuaryBedCode: string;
  mortuaryBedDesc: string;
  mortuaryBedNumber: string;
  status: MortuaryBedStatus;
}

/** Shape of `getMortuaryBedListById`'s `listObject[0]` (ctrl:271-286) --
 * only the 3 fields `showUpdateBtn` actually reads back into the form.
 * `mortuaryBedNumber` is NOT read back (not part of the form at all). */
export interface MortuaryBedDetail {
  mortuaryBedId: number;
  mortuaryBedCode: string;
  mortuaryBedDesc: string;
}

/** Mirrors `saveMortuaryBed`'s `data` object (ctrl:203-213). `status` is
 * ALWAYS `'A'` on create -- there is no way to create a bed as inactive. */
export interface MortuaryBedSavePayload {
  mortuaryBedCode: string;
  mortuaryBedDesc: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
  unitId: number;
}

/** Mirrors `updateMortuaryBed`'s `data` object (ctrl:345-351) -- no
 * `organizationId`/`unitId`/`status` sent on update, matching legacy exactly
 * (only present on the create payload). */
export interface MortuaryBedUpdatePayload {
  mortuaryBedCode: string;
  mortuaryBedDesc: string;
  mortuaryBedId: number;
  updatedBy: number;
  updatedDate: string;
}

/** Mirrors `updateStatus`'s `data` object (ctrl:304-309). */
export interface MortuaryBedStatusPayload {
  mortuaryBedId: number;
  status: MortuaryBedStatus;
  updatedBy: number;
  updatedDate: string;
}

/** Form/edit-buffer shape (mirrors `$scope.mortuaryObj`, ctrl:56). */
export interface MortuaryBedForm {
  mortuaryBedId?: number;
  mortuaryBedCode: string;
  mortuaryBedDesc: string;
}

export function emptyMortuaryBedForm(): MortuaryBedForm {
  return {
    mortuaryBedCode: '',
    mortuaryBedDesc: '',
  };
}
