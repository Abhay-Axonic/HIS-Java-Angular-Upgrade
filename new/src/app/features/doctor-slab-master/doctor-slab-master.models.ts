/**
 * Parity types for `doctorSlabMasterController.js` +
 * `views/master/UnitMaster/billingMaster/doctor-slab-master.html`.
 *
 * Legacy binds every slab field (`slabFrom`/`slabTo`/`percentage`) to a
 * plain `<input type="text">` via `ng-model` -- never `type="number"` --
 * and does all comparisons with `parseInt(...)`/loose `>` on whatever
 * value happens to be sitting in the field (a string while a user is
 * typing, or a `Double` echoed straight out of a JSON response after an
 * edit-load). Kept as `string` here to match that exact input/validation
 * shape field-for-field, rather than introducing a stricter numeric type
 * legacy's own form never had.
 */
export interface DoctorOption {
  doctorId: number;
  doctorName: string;
}

/** One row of the editable slab-tier list (`$scope.doctorSlabList`). */
export interface DoctorSlabRow {
  /** Present only for a row loaded via the edit (pencil) flow -- ctrl:349,
   * `doctorSlabMasterId` on the raw row pushed straight from the server
   * response. Its presence, not any local flag, is what the backend uses
   * to decide `update` vs. `save` (DoctorSlabMasterDaoImpl.doctorSlabSave,
   * `if (dto.getDoctorSlabMasterId() != null) { ...update... } else { ...save... }`). */
  doctorSlabMasterId?: number | null;
  slabFrom: string;
  slabTo: string;
  percentage: string;
  status: 'A' | 'I';
  /** Only ever present on a row loaded via the edit flow (ctrl:349's raw
   * `value` push includes it); always overwritten to `null` again right
   * before every save (ctrl:197, `value.amount = null;`) regardless of
   * what was loaded, so it never actually round-trips a non-null value. */
  amount?: number | null;
}

/** One row of the summary table (`$scope.doctorSlabMasterList`) -- one row
 * per doctor, not per slab tier (`getdoctorSlabList`'s SQL is `select
 * distinct doctor_id, status, doctorName ...`). */
export interface DoctorSlabMasterListRow {
  doctorId: number;
  doctorName: string;
  status: 'A' | 'I';
}

/** One row of the read-only "Slab Info" modal (`getdoctorSlabListByDoctorId`,
 * `#doctorSlabInfo`). */
export interface DoctorSlabDetail {
  slabFrom: number;
  slabTo: number;
  percentage: number;
}

/** The real shape `getdoctorSlabListByDoctorId` actually returns
 * (`DoctorSlabMasterDaoImpl.getdoctorSlabListByDoctorId`'s SQL aliases:
 * doctorId, doctorSlabMasterId, percentage, slabFrom, slabTo, status,
 * amount -- all numeric fields as `Double`/JSON numbers, NOT the strings
 * `DoctorSlabRow` uses for its own editable form state). Used for both
 * call sites this one endpoint serves: the read-only modal (`viewSlabInfo`,
 * consumed as-is) and the edit-load (`editDoctorSlab`, converted to
 * `DoctorSlabRow`'s string fields via `String(...)`). */
export interface DoctorSlabApiRow extends DoctorSlabDetail {
  doctorId: number;
  doctorSlabMasterId: number | null;
  status: 'A' | 'I';
  amount: number | null;
}

export function emptyDoctorSlabRow(): DoctorSlabRow {
  return { slabFrom: '0', slabTo: '', percentage: '', status: 'A' };
}
