/**
 * Field names/types read directly from
 * `formulationTypeMasterController.js` (getFormulationTypeData:38-54,
 * populateFormulationType:57-71) -- not guessed. Both the list (`GET
 * /api/globals/formulationTypes/{orgId}`) and by-id (`GET
 * /api/globals/formulationTypes/{orgId}/{id}`) endpoints return RAW JSON
 * (an array, or a plain object) -- `response.data` IS the payload directly
 * in legacy, never a `{status, listObject}` envelope. Modeled below as
 * plain interfaces, not `ApiResponse`.
 *
 * `status` is a real `Boolean` on the wire (`response.data.status`), not the
 * `'A'|'I'` string vocabulary some sibling master screens use.
 *
 * The "Clinical Unit" field/column is deliberately NOT modeled here: legacy's
 * own `pharmacy-formulation-type-master.html` renders a Clinical Unit input
 * (view:37-50) and table column (view:130-139, always an empty `<td>`)
 * with no `ng-model` anywhere -- confirmed dead, decorative-only UI, not a
 * real, saved field. See this screen's component class-doc comment for the
 * full writeup.
 */
export interface FormulationTypeRow {
  id: number;
  code: string;
  /** Display name -- legacy's own field name (`type`), not "name". */
  type: string;
  status: boolean;
}

/** Shape returned by `GET /api/globals/formulationTypes/{orgId}/{id}`
 * (`populateFormulationType`, ctrl:57-71) -- same fields as the list row. */
export type FormulationTypeDetail = FormulationTypeRow;

/** Editable top-of-page create/edit form (mirrors the handful of scalar
 * `self.model.*` fields legacy keeps outside `self.model.pagination`). */
export interface FormulationTypeForm {
  id: number | null;
  code: string;
  type: string;
}

export function emptyFormulationTypeForm(): FormulationTypeForm {
  return { id: null, code: '', type: '' };
}

/** POST body for create (`createFormulationType`, ctrl:147-181). */
export interface FormulationTypeCreatePayload {
  code: string;
  type: string;
  status: true;
  unitId: number;
  organizationId: number;
  userId: number;
}

/** PATCH body for edit-save (`updateFormulationType`, ctrl:184-210).
 * Deliberately just these 4 fields, matching legacy's own literal payload --
 * no `unitId`/`userId` here (unlike create). `status` is ALWAYS `true`
 * regardless of the record's real current status -- see this screen's
 * component class-doc comment, bug faithfully reproduced verbatim. */
export interface FormulationTypeUpdatePayload {
  type: string;
  code: string;
  status: true;
  organizationId: number;
}

/** PATCH body for the status-toggle-only call (`ToggleStatus`, ctrl:74-98) --
 * a genuinely different, smaller payload shape than the edit-save PATCH
 * above, even though both hit the same `PATCH /{id}` endpoint. */
export interface FormulationTypeStatusPayload {
  status: boolean;
  organizationId: number;
}
