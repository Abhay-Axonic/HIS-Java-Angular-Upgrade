export type ModalityTypeStatus = 'A' | 'I';

/** Row shape returned by getModalityTypeMasterList (ctrl:138-166) -- the
 * only fields the legacy view renders (modality-type-master.html:154-165). */
export interface ModalityTypeRow {
  modalityTypeId: number;
  modalityTypeCode: string;
  modalityTypeDesc: string;
  specialityName: string;
  status: ModalityTypeStatus;
}

/** Active-specialty dropdown option, from GETACTIVESPECIALITY (ctrl:85-98,
 * called via BillingGenericService in legacy -- same POST contract as
 * GenericService, no functional difference for this rebuild). */
export interface SpecialityOption {
  specialityId: number;
  specialityName: string;
}

/** Response shape of getModalityTypeById (ctrl:293-307) -- `specialityId`
 * is a raw number here; ctrl:303 explicitly `.toString()`s it before
 * storing it on `$scope.modalityTypeObj`, because the Specialty select's
 * own option values are unconverted strings (`value="{{info.specialityId}}"`,
 * no `convert-to-number`, view:48) -- see `ModalityTypeObj`. */
export interface ModalityTypeDetail {
  modalityTypeId: number;
  modalityTypeCode: string;
  modalityTypeDesc: string;
  specialityId: number;
}

/**
 * Mirrors `$scope.modalityTypeObj` (ctrl:60-73) -- the SAME object literal
 * legacy builds once in `init()` and reuses verbatim as both the add/edit
 * form model AND the exact POST body for save/update (ctrl:241-245,
 * ctrl:367-369), same pattern as this codebase's qms-department-master's
 * `QMSDeptCommon`.
 *
 * Real, faithfully-reproduced quirk this carries: `showUpdateBtn`
 * (ctrl:280-309) only ever overwrites `modalityTypeId`/`modalityTypeCode`/
 * `modalityTypeDesc`/`specialityId` on this object -- `organizationId`/
 * `unitId`/`createdBy`/`updatedBy`/`status` are left exactly as `init()`
 * set them. In practice `organizationId`/`unitId` are still correct (same
 * session), but `updateModalityType`'s POST body therefore always resends
 * the CURRENT session's `userId` as both `createdBy` and `updatedBy` -- not
 * the row's real original creator -- on every single update, regardless of
 * who actually created that row. Reproduced verbatim, not fixed.
 */
export interface ModalityTypeObj {
  modalityTypeId: number;
  modalityTypeCode: string;
  modalityTypeDesc: string;
  specialityId: string;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  createdBy: number;
  status: ModalityTypeStatus;
  createdDate?: string;
  updatedDate?: string;
}

/** Matches `init()`'s object literal exactly (ctrl:60-73). */
export function emptyModalityTypeObj(userId: number, organizationId: number, unitId: number): ModalityTypeObj {
  return {
    modalityTypeId: 0,
    modalityTypeCode: '',
    modalityTypeDesc: '',
    specialityId: '',
    organizationId,
    unitId,
    updatedBy: userId,
    createdBy: userId,
    status: 'A',
  };
}

/** POST body for updateStatusForModalityType (ctrl:318-325). */
export interface ModalityTypeStatusPayload {
  modalityTypeId: number;
  status: ModalityTypeStatus;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
  unitId: number;
}
