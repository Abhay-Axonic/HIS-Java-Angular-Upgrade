/**
 * Field names/types read directly from `SterilizationTypeMasterDto.java`
 * (legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dto/
 * SterilizationTypeMasterDto.java) and `sterilizationTypeMasterController.js`
 * -- not guessed.
 *
 * Legacy's own form object is `$scope.sterilizationMasterObj`
 * (ctrl:72-85, `$scope.dummySterilizationMasterObj`): `{ code, name,
 * created_by, updated_by, organizationId, unitId, storeId }`. This screen
 * is the CSSD sterilization-METHOD classification lookup itself (steam,
 * ETO, plasma, etc.) -- a 2-field PURE-LOOKUP master consumed only as an
 * active/not-deleted FK target by `m_autoclave_master.sterilization_type`
 * (confirmed via the live schema's own FK constraint,
 * `m_autoclave_master_sterilization_type_fkey`). No rate/amount/formula
 * field anywhere on the entity -- confirmed PURE-LOOKUP, no Step 6b
 * required.
 */
export interface SterilizationTypeForm {
  id?: number;
  code: string;
  name: string;
}

export function emptySterilizationTypeForm(): SterilizationTypeForm {
  return { id: undefined, code: '', name: '' };
}

/** Shape of one row in `getSterilizationTypesList`'s `listObject`
 * (`GET_STERILIZATION_TYPE_LIST` named query,
 * `SterilizationTypeMaster.java:21-30`) -- `status` is a legacy 'A'/'I'
 * character column (NOT a real boolean, per the view's own
 * `ng-true-value="'A'"`/`ng-false-value="'I'"`, view:117). Note the named
 * query deliberately does NOT select `organizationId`/`unitId`/`storeId`
 * at all -- see this screen's `.ts` class doc comment for why that matters
 * to the Update flow specifically. */
export interface SterilizationTypeRow {
  id: number;
  code: string;
  name: string;
  status: 'A' | 'I';
  created_by?: number;
  created_date?: string;
  updated_by?: number;
  updated_date?: string;
}

/** POST body for `createSterilizationType` (ctrl:184-220, the "create"
 * branch of `saveSterilization()`, payload built from
 * `$scope.dummySterilizationMasterObj`, ctrl:72-85). Legacy's own payload
 * also includes `storeId` (from the dead `selectedCssdStore` session-var
 * read, ctrl:41-46) -- DELIBERATELY OMITTED here, see this screen's `.ts`
 * class doc comment: `SterilizationTypeMaster.java`'s entity has no active
 * `storeId` field at all (only a `store` `@ManyToOne` relation mapped
 * `insertable = false, updatable = false`, confirmed by reading the entity
 * directly), so a `storeId` sent on the wire is silently dropped
 * server-side no matter what value the client sends -- confirmed live,
 * `store_id` stayed NULL after a real create with a session that had no
 * `selectedCssdStore` set. `status` is NOT sent by the frontend (legacy's
 * own object literal has it commented out, ctrl:76) -- the backend sets
 * `status='A'` unconditionally on every create
 * (`SterilizationTypeMasterDaoImpl.createSterilizationType:150`),
 * confirmed by reading the DAO directly. */
export interface SterilizationTypeCreatePayload {
  code: string;
  name: string;
  organizationId: number;
  unitId: number;
  created_by: number;
  updated_by: number;
}

/** PUT body for `updateSterilizationType` (ctrl:257-278). See this
 * screen's `.ts` class doc comment for a CONFIRMED, FIXED frontend-only bug:
 * legacy's own `updateSterilization()` sends whatever
 * `$scope.sterilizationMasterObj` currently holds, which -- because
 * `editSterilization()` (ctrl:159-165) assigns the EDITED ROW OBJECT
 * directly (itself sourced from `GET_STERILIZATION_TYPE_LIST`, which never
 * selects `organizationId`/`unitId`) -- means legacy's own update PUT body
 * NEVER carries a real `unitId`, every single time, for every update, on
 * this screen. `SterilizationTypeMasterDaoImpl.updateSterilizationType`
 * (:92-108) uses `unitId` ONLY to pick a timezone for the
 * `updated_date` timestamp (`TimeZoneComponent.getDateByzone(... != null ?
 * ... : 0)`), defaulting to timezone-id 0 when absent -- so real legacy
 * silently records every update's timestamp using timezone 0 instead of
 * the unit's actual configured timezone (unlike `createSterilizationType`,
 * whose payload DOES carry a real `unitId` from `dummySterilizationMasterObj`).
 * This is a genuine, silent, pure-frontend-controller bug (nothing
 * server-side is wrong -- the DAO correctly uses whatever `unitId` it's
 * given) -- classified the same way `GLcode`'s stale-list bug was (entry
 * 160): fixed directly in this rebuild by sending the CURRENT session's
 * real `organizationId`/`unitId` on every update (available via
 * `SessionService`, not sourced from the stale row object), restoring the
 * same per-unit-timezone behavior `create` already has. Flagged
 * prominently for owner sign-off on this classification, same as
 * `GLcode`'s precedent. `code`/`name` are the only fields the DAO actually
 * reads besides `id`/`updated_by`/`unitId` -- `status`/`created_by`/
 * `created_date` sent by legacy's own payload (leftover row-object fields)
 * have zero effect and are correctly omitted here too. */
export interface SterilizationTypeUpdatePayload {
  id: number;
  code: string;
  name: string;
  organizationId: number;
  unitId: number;
  updated_by: number;
}

/** PUT body for `updateSterilizationTypeStatus` (ctrl:225-255). The DAO
 * (`updateSterilizationTypeStatus`, `SterilizationTypeMasterDaoImpl.java:
 * 111-137`) loads the row by `id` and only ever writes
 * `status`/`updated_by`/`updated_date` back onto it (`unitId` again only
 * picks the timezone for `updated_date`, same 0-default caveat as update
 * above) -- so this rebuild sends exactly the 4 fields the backend
 * actually reads. */
export interface SterilizationTypeStatusPayload {
  id: number;
  status: 'A' | 'I';
  unitId: number;
  updated_by: number;
}
