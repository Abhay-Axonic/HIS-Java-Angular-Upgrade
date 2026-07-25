export type SurgeryGradeStatus = 'A' | 'I';

/** Row shape returned by `getSurgeryGradeMasterList`'s `listObject`
 * (`GET_SURGERY_GRADE_LIST` named query, `SurgeryGradeMaster.java:25-30`).
 * The query deliberately selects only these 4 fields -- no
 * `orgnisationId`/`unitId` at all -- see this screen's `.ts` class doc
 * comment for why that matters to the Update flow specifically. */
export interface SurgeryGradeRow {
  surgeryGradeId: number;
  surgeryGradeCode: string;
  surgeryGrade: string;
  status: SurgeryGradeStatus;
}

/** Editable form state -- mirrors `$scope.surgeryGradeObj`
 * (ctrl:107-112, `{ surgeryGrade, surgeryGradeCode, surgeryGradeId }`). */
export interface SurgeryGradeForm {
  surgeryGradeId?: number;
  surgeryGradeCode: string;
  surgeryGrade: string;
}

export function emptySurgeryGradeForm(): SurgeryGradeForm {
  return { surgeryGradeId: undefined, surgeryGradeCode: '', surgeryGrade: '' };
}

/** POST body for `saveSurgeryGradeMaster` (ctrl:125-174). Legacy's own
 * literal field spelling, `orgnisationId` (ctrl:135), kept verbatim as the
 * real wire contract. `status` is always `'A'` -- there is no way to create
 * a surgery grade as inactive (ctrl:136, hardcoded literal). */
export interface SurgeryGradeCreatePayload {
  surgeryGradeCode: string;
  surgeryGrade: string;
  unitId: number;
  orgnisationId: number;
  status: 'A';
  createdDate: string;
  createdBy: number;
  updatedDate: string;
  updatedBy: number;
}

/**
 * PUT/POST body for `updateSurgeryGradeMaster` (ctrl:247-286).
 *
 * ================================================================
 * DELIBERATE OMISSION -- do not add `orgnisationId`/`organizationId` here
 * ================================================================
 * Legacy's own `updateSurgeryGrade()` builds its payload as EXACTLY
 * `{ surgeryGradeCode, surgeryGrade, surgeryGradeId, updatedBy, updatedDate }`
 * (ctrl:252-258) -- no `orgnisationId`, no `unitId`, confirmed by reading the
 * controller directly. Server-side, `GET_SURGERY_GRADE_LIST_BY_NAME_NOT_ID`
 * (`SurgeryGradeMaster.java:49-55`) is itself correctly written
 * (`(LOWER(x)=:p OR x=:p) AND surgeryGradeId!=:id AND orgnisationId=:orgId`)
 * -- the query logic is fine -- but because the frontend never sends
 * `orgnisationId`, the bound parameter is `null`, and `orgnisationId=:orgId`
 * with a null parameter matches ZERO rows under SQL three-valued logic. The
 * Update duplicate-check is therefore a **permanent no-op**: it can never
 * reject any update, ever, regardless of any real duplicate -- functionally
 * identical in effect to `sterilizationTypeMaster` having no duplicate-check
 * at all (see `docs/ANGULAR_PENDING_FINDINGS.md`,
 * `PENDING-batch18-step1-surgerygrademaster`). This is a genuine,
 * disclosed, data-integrity gap, logged for the owner's future decision --
 * NOT something to silently "fix" here. Sending a real `organizationId` on
 * this call would silently change server-side behavior (the check would
 * start actually rejecting duplicates) -- an undisclosed behavioral
 * deviation this project's standing policy prohibits during a parity
 * rebuild. This interface therefore has NO organization/unit field, on
 * purpose, and `surgery-grade-master.ts`'s `doUpdate()` must never add one.
 */
export interface SurgeryGradeUpdatePayload {
  surgeryGradeCode: string;
  surgeryGrade: string;
  surgeryGradeId: number;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `updateStatusForSurgeryGrade` (ctrl:209-244) -- exactly
 * the 4 fields legacy's own `data` object literal sends (ctrl:216-221), no
 * `orgnisationId`/`unitId` here either. */
export interface SurgeryGradeStatusPayload {
  surgeryGradeId: number;
  status: SurgeryGradeStatus;
  updatedBy: number;
  updatedDate: string;
}

/** POST body for `getSurgeryGradeById` (ctrl:182-186) -- used by the edit
 * flow only. */
export interface SurgeryGradeByIdPayload {
  surgeryGradeId: number;
  orgnisationId: number;
}
