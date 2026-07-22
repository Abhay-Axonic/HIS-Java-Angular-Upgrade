/**
 * Field shape taken directly from the real Java backend (confirmed real,
 * not guessed -- see class comment in asset-type-master.ts):
 * com.param.pharmacy.master.unit.dto.assetType.AssetTypeResponse (id, code,
 * type, status) and AssetTypeRequest (same shape, used for create/update).
 */
export interface AssetTypeRow {
  id: number;
  code: string;
  type: string;
  status?: boolean;
}

export interface AssetTypeDetail {
  id: number;
  code: string;
  type: string;
  status?: boolean;
}

export interface AssetTypeSavePayload {
  code: string;
  type: string;
  status?: boolean;
  unitId: number;
  organizationId: number;
  userId: number;
}
