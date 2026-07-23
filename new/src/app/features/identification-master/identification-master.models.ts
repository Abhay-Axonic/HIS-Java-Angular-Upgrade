export type IdentificationStatus = 'A' | 'I';
export type YN = 'Y' | 'N';

/**
 * Row shape returned by `GET_IDENTIFICATION_LIST`
 * (`getIdentificationMasterList`, ctrl:89-138) -- exactly the columns the
 * legacy table renders (view:139-160: Code, Identification Card, Expiry
 * required, Numerical, Alpha Numerical, Defined Length, Status, Action).
 * Confirmed directly against the named query
 * (`legacy/Entity/.../IdentificationMaster.java` `GET_IDENTIFICATION_LIST`),
 * not guessed.
 */
export interface IdentificationRow {
  identificationId: number;
  identificationCode: string;
  identificationName: string;
  isExpiryRequired: YN;
  isNumerical: YN;
  isAlphanumeric: YN;
  definedLength: number | null;
  status: IdentificationStatus;
}

/**
 * `response.data.listObject[0]` shape returned by
 * `GET_IDENTIFICATION_LIST_BY_ID` (`showUpdateBtn`, ctrl:296-332) -- same
 * columns as `IdentificationRow`, confirmed against the named query (it
 * selects the identical field set plus the id). No separate DTO shape
 * needed.
 */
export type IdentificationDetail = IdentificationRow;

/**
 * Editable form state -- mirrors legacy's `$scope.identification`
 * (ctrl:54-60). Field named `isExpiryRequired` directly here (the wire
 * payload's own name) rather than legacy's internal `isExpire` scope alias
 * (ctrl:241,325,403) -- same reasoning as this codebase's
 * encounter-type-master: this rebuild has no separate "scope object" vs
 * "payload" distinction worth preserving.
 *
 * `isLength` is a PURE UI-ONLY toggle -- there is no `isLength` column
 * anywhere in the DTO/entity/named queries (confirmed by reading
 * `IdentificationMasterDto.java` and every `@NamedQuery` in
 * `IdentificationMaster.java` directly). Legacy sets it two different ways
 * depending on flow, never from a server field:
 *  - On create/reset it's just initialized `'N'` (ctrl:54-60).
 *  - On edit (`showUpdateBtn`, ctrl:321-324) it's INFERRED after the fetch:
 *    `if ($scope.identification.definedLength != null) { $scope.identification.isLength = 'Y'; }`
 *    -- i.e. "was a defined length ever saved", not a flag round-tripped
 *    from the server. Reproduced identically below (see component's
 *    `edit()`); `isLength` itself is never part of any request payload.
 */
export interface IdentificationForm {
  identificationId?: number;
  identificationCode: string;
  identificationName: string;
  isExpiryRequired: YN;
  isNumerical: YN;
  isAlphanumeric: YN;
  isLength: YN;
  definedLength: number | null;
}

export function emptyIdentificationForm(): IdentificationForm {
  return {
    identificationId: undefined,
    identificationCode: '',
    identificationName: '',
    isExpiryRequired: 'N',
    isNumerical: 'N',
    isAlphanumeric: 'N',
    isLength: 'N',
    definedLength: null,
  };
}

/**
 * POST body for `ADDIDENTIFICATION` (`saveIdentification`, ctrl:201-293).
 *
 * ================================================================
 * Disclosed, approved improvement over legacy's CURRENT (already-fixed)
 * behavior -- see docs/DECISION_NEEDED_IdentificationMasterCreateFlagReset.md
 * ================================================================
 * `IdentificationMasterDaoImpl.addIdentificationMaster` used to
 * unconditionally overwrite `isExpiryRequired`/`isAlphanumeric`/`isNumerical`
 * back to `'N'` on every create, discarding whatever the Dozer mapper had
 * just correctly mapped from this exact payload -- a real, silent
 * data-loss bug (decision doc section 1). Per the owner's decision 1 in
 * that doc, this has since been FIXED on `fix/identificationmaster-create-flag-reset`
 * (commit da85e25) -- the three unconditional overrides were removed, and
 * the fix was verified live end-to-end (create with Expiry Required +
 * Numerical checked correctly persisted `Y`/`Y`/`N`, not `N`/`N`/`N`).
 * This rebuild therefore does a normal, straightforward create: it submits
 * exactly what the user checked and trusts the (corrected) backend to
 * persist it correctly, with NO client-side workaround to discard or
 * re-derive these three flags. Building such a workaround here would
 * silently reintroduce the very bug that was just fixed -- explicitly not
 * wanted, per the owner's direction for this screen.
 *
 * NOTE for whoever reconciles branches: as of this rebuild, `da85e25` is
 * NOT yet an ancestor of the branch this file was written on (`git
 * merge-base --is-ancestor da85e25 HEAD` returns false) -- i.e. the fix
 * lives on its own branch/the locally-deployed Tomcat, not yet merged to
 * `main`. This payload shape assumes the fix lands before/alongside this
 * screen going live; it does not itself carry the fix (that's legacy-side,
 * out of scope for this Angular-only change).
 */
export interface IdentificationSavePayload {
  identificationCode: string;
  identificationName: string;
  isAlphanumeric: YN;
  isNumerical: YN;
  definedLength: number | null;
  isExpiryRequired: YN;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  updatedDate: string;
  createdDate: string;
}

/**
 * POST body for `UPDATEIDENTIFICATION` (`updateIdentification`, ctrl:377-452).
 * `status: 'A'` is hardcoded here too (ctrl:404), exactly like the save
 * payload -- kept for wire fidelity even though
 * `IdentificationMasterDaoImpl.updateIdentificationMaster` never reads/sets
 * `status` at all (confirmed directly: that method touches name, code,
 * definedLength, isAlphanumeric, isNumerical, updatedBy, isExpiryRequired,
 * updatedDate -- never status). A real status change only ever happens via
 * the separate `updateStatusForIdentification` endpoint/toggle.
 */
export interface IdentificationUpdatePayload {
  identificationId: number;
  identificationCode: string;
  identificationName: string;
  isAlphanumeric: YN;
  isNumerical: YN;
  definedLength: number | null;
  isExpiryRequired: YN;
  status: 'A';
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

/** POST body for `UPDATEIDENTIFICATIONSTATUS` (`updateStatus`, ctrl:335-374). */
export interface IdentificationStatusPayload {
  identificationId: number;
  status: IdentificationStatus;
  updatedBy: number;
  updatedDate: string;
}
