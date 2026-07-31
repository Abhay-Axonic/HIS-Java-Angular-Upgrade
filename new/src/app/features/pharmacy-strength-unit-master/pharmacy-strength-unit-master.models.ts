/**
 * Row shape returned by `GET /api/globals/strength/units/{organizationId}` --
 * confirmed live (2026-07-31, real authenticated request) that this
 * endpoint returns a RAW JSON ARRAY, not the standard
 * `{status, listObject}` envelope every other legacy screen in this app
 * uses. `status` is a genuine BOOLEAN here too, not the usual 'A'/'I'
 * string -- confirmed against the real `m_strength_unit` table
 * (`is_active boolean`).
 */
export interface StrengthUnitRow {
  id: number;
  code: string;
  unit: string;
  status: boolean;
}

/** POST create response shape -- confirmed live: `{id, message}`, not an ApiResponse envelope. */
export interface StrengthUnitCreateResponse {
  id: number;
  message: string;
}

/** PATCH update/status response shape -- confirmed live: `{message}`. */
export interface StrengthUnitMutateResponse {
  message: string;
}

/** Editable form state -- mirrors legacy's `self.model.{code,units,id}`. */
export interface StrengthUnitRecord {
  id?: number;
  code: string;
  units: string;
}

export function emptyStrengthUnit(): StrengthUnitRecord {
  return { id: undefined, code: '', units: '' };
}
