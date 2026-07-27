/** Row shape returned by `GetAll` (controller:41-46) -- status is a real BOOLEAN, not 'A'/'I' like every other screen in this batch. */
export interface StorageUnitRow {
  id: number;
  code: string;
  unitName: string;
  status: boolean;
}

/** Editable form state -- mirrors `vm.model.{code,unitName,id}` (ctrl:8-30). */
export interface StorageUnitForm {
  id?: number;
  code: string;
  unitName: string;
}

export function emptyStorageUnitForm(): StorageUnitForm {
  return { id: undefined, code: '', unitName: '' };
}

/** Request body for Create (POST) / Update (PATCH) -- both use the same
 * partial-update-friendly shape server-side (service:83-130 only
 * overwrites fields that are non-null/non-empty). */
export interface StorageUnitRequest {
  id?: number;
  code?: string;
  unitName?: string;
  status?: boolean;
  userId?: number;
}

/** Code format constraint, ctrl:130,148 -- alphanumeric with at most one hyphen. */
export const STORAGE_UNIT_CODE_PATTERN = /^[a-z0-9]*[-]?[a-z0-9]*$/i;
