export interface AssetTypeOption {
  id: number;
  type: string;
}

export interface AssetCategoryRow {
  id: number;
  assetType: string;
  assetTypeId: number;
  code: string;
  category: string;
  status: boolean;
}

export interface AssetCategoryDetail {
  id: number;
  assetTypeId: number;
  code: string;
  category: string;
  status: boolean;
}

export interface AssetCategorySavePayload {
  assetTypeId: number;
  code: string;
  category: string;
  status: boolean;
  unitId: number;
  organizationId: number;
  userId: number;
}
