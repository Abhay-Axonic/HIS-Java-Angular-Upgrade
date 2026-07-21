/** Row shape returned by GET /api/globals/manufacturers/{organizationId}
 * (ctrl:53-70) -- legacy assigns `response.data` directly to the list, no
 * `{status,listObject}` envelope (this `/api/globals/...` REST family
 * returns raw arrays/objects, matching asset-category-master's identical
 * convention). */
export interface ManufacturerRow {
  id: number;
  code: string;
  name: string;
  address: string;
  status: boolean;
}

/** Shape returned by GET /api/globals/manufacturers/{organizationId}/{id}
 * (ctrl:104-133) -- same raw-object convention. */
export interface ManufacturerDetail {
  id: number;
  code: string;
  name: string;
  address: string;
  status: boolean;
}

/** Editable form state -- mirrors legacy's `self.model.{code,name,address,id}`
 * (ctrl:9-36). */
export interface ManufacturerForm {
  id?: number;
  code: string;
  name: string;
  address: string;
}

export function emptyManufacturerForm(): ManufacturerForm {
  return { id: undefined, code: '', name: '', address: '' };
}

/** POST body for create (ctrl:204-209). */
export interface ManufacturerCreatePayload {
  name: string;
  code: string;
  address: string;
  status: true;
}

/** Response shape from create (ctrl:212-233) -- `response.data.id` and
 * `response.data.message`, not an envelope. */
export interface ManufacturerCreateResponse {
  id: number;
  message?: string;
}

/** PATCH body for update (ctrl:242-246) -- no `status` field, matching
 * legacy exactly (status is only ever changed via the separate toggle
 * endpoint). */
export interface ManufacturerUpdatePayload {
  name: string;
  code: string;
  address: string;
}

/** PATCH body for the status-toggle endpoint (ctrl:144-146). */
export interface ManufacturerStatusPayload {
  status: boolean;
}

/** Response from GET /api/globals/manufacturers/prefix/{organizationId}/177
 * (ctrl:72-93) -- `177` is a hardcoded manufacturer-type id in legacy,
 * reproduced verbatim, not looked up from anywhere else. Only `.code` is
 * ever read (into `previousCode`, ctrl:83) -- legacy's own
 * `self.model.code = response.data.code` assignment (ctrl:82) is commented
 * out, so the prefix response never actually populates the code field
 * directly; see the class doc comment. */
export interface ManufacturerPrefixResponse {
  code?: string;
}
