export interface FixedAssetTypeRow {
  id: number;
  fixedAssetCode: string;
  fixedAssetType: string;
  status: boolean;
}

export interface FixedAssetTypeDetail {
  id: number;
  fixedAssetCode: string;
  fixedAssetType: string;
}

export interface FixedAssetTypeSavePayload {
  fixedAssetCode: string;
  fixedAssetType: string;
  status: boolean;
  unitId: number;
  organizationId: number;
  userId: number;
}
