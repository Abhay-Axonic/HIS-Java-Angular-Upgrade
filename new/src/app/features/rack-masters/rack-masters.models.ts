/**
 * Field set taken directly from
 * views/master/UnitMaster/organizationMaster/rack-master.html +
 * resources/assets/js/controller/billing/unit/rackMastersController.js
 * (self-registered controller `RackMasters.Controller`, state `rackMasters`,
 * moduleId 1068). Structurally near-identical to this codebase's
 * `row-masters`/`box-masters` (same "MRD <X> Master" family, same
 * copy-paste lineage -- see rack-masters.ts's class comment) -- only two
 * editable fields, rack code + a free-text description.
 */
export interface RackMasterRow {
  rackId: number;
  rackCode: string;
  rackDescription: string;
  status: 'A' | 'I';
}

/** Shape of GET /mrdrack/getMrdRackById/{rackId}/{orgId}/{unitId}'s
 * `response.data.object` (ctrl:177-187) -- a single record, NOT a
 * `listObject` array (unlike row-masters' by-id lookup, which reads
 * `response.data.object` too, so this matches that sibling exactly; some
 * other "Master" siblings in this codebase use `listObject[0]` instead --
 * confirmed from source per-screen, not assumed). */
export interface RackMasterDetail {
  rackId: number;
  rackCode: string;
  rackDescription: string;
}

/** Editable form state -- mirrors legacy's loose `$scope.rackCode`/
 * `$scope.rackDescription`/`$scope.rackID` globals (ctrl:26-29/182-184). */
export interface RackMasterForm {
  rackId?: number;
  rackCode: string;
  rackDescription: string;
}

export function emptyRackMasterForm(): RackMasterForm {
  return { rackId: undefined, rackCode: '', rackDescription: '' };
}

/** POST body for /mrdrack/saveMrdRack (ctrl:31-57). */
export interface RackMasterSavePayload {
  rackCode: string;
  rackDescription: string;
  unitId: number;
  organizationId: number;
  updatedBy: number;
  createdBy: number;
  status: 'A';
}

/** PUT body for /mrdrack/updateMrdRack (ctrl:190-217). `status` is
 * hardcoded `'A'` in legacy's `showUpdateBtn` regardless of the row's
 * actual current status -- harmless in practice since `viewBtn`
 * (ctrl:168-188) refuses to populate the edit form for any row whose
 * status is already `'I'`, so an edit can only ever be submitted for an
 * already-active row. Reproduced verbatim. */
export interface RackMasterUpdatePayload {
  rackId: number;
  rackCode: string;
  rackDescription: string;
  status: 'A';
  organizationId: number;
  unitId: number;
  updatedBy: number;
  createdBy: number;
}

/** PUT body for /mrdrack/updateMrdRackStatus (ctrl:219-243). */
export interface RackMasterStatusPayload {
  rackId: number;
  rackCode: string;
  rackDescription: string;
  status: 'A' | 'I';
  organizationId: number;
  unitId: number;
  updatedBy: number;
  createdBy: number;
}
