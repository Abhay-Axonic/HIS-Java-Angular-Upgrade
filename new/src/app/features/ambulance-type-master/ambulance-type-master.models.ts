/**
 * `ambulanceTypeId` is deliberately typed `string`, not `number`: legacy's
 * view binds it via `<input type="text" ... is-number ...>` (view line 13)
 * -- `is-number` only filters keystrokes to digits, it does not coerce the
 * AngularJS model to a JS number the way `<input type="number">` would.
 * Whatever the user types is sent over the wire as a string. Typing it
 * `number` here would silently assume a JSON-shape the legacy client never
 * actually produced.
 */
export interface AmbulanceTypeRow {
  ambulanceTypeMasterId: number;
  ambulanceTypeId: string;
  ambulanceType: string;
  status: 'A' | 'I';
}

export interface AmbulanceTypeDetail {
  ambulanceTypeMasterId: number;
  ambulanceTypeId: string;
  ambulanceType: string;
}

export interface AmbulanceTypeSavePayload {
  ambulanceType: string;
  ambulanceTypeId: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

export interface AmbulanceTypeUpdatePayload {
  ambulanceTypeMasterId: number;
  ambulanceType: string;
  ambulanceTypeId: string;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

export interface AmbulanceTypeStatusPayload {
  ambulanceTypeMasterId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}
