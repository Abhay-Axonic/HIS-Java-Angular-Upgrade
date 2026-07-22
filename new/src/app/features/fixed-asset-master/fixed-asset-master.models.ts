/**
 * Field shapes taken directly from the real Java backend (confirmed real
 * -- see class comment in fixed-asset-master.ts):
 * com.param.pharmacy.master.unit.dto.fixedAsset.FixedAssetResponse (list +
 * detail rows) and the shared `/api/global` dropdown DTOs
 * (com.param.global.dto.global.AssetType/AssetCategory/ProductCategory,
 * ManufacturerResponseDto).
 */
export interface AssetTypeOption {
  id: number;
  type: string;
}

export interface AssetCategoryOption {
  id: number;
  category: string;
}

export interface ProductCategoryOption {
  id: number;
  category: string;
}

export interface ManufacturerOption {
  id: number;
  detail: string;
  manufacturerName?: string;
}

export interface FixedAssetRow {
  id: number;
  assetTypeId?: number;
  assetType?: string;
  assetCategoryId?: number;
  assetCategory?: string;
  productCategoryId?: number;
  productCategory?: string;
  manufactureId?: number;
  manufactureName?: string;
  itemCode: string;
  productCode: string;
  name: string;
  status?: boolean;
  isCssd?: boolean;
  isModality?: boolean;
  isChargeableEqupiment?: boolean;
  isAccessories?: boolean;
  isSpares?: boolean;
  isDialyser?: boolean;
  isCritical?: boolean;
}

export interface FixedAssetSavePayload {
  productCategoryId?: number;
  manufactureId?: number;
  itemCode: string;
  productCode: string;
  name: string;
  status?: boolean;
  isCssd: boolean;
  isModality: boolean;
  isChargeableEqupiment: boolean;
  isAccessories: boolean;
  isSpares: boolean;
  isDialyser: boolean;
  isCritical: boolean;
  unitId: number;
  organizationId: number;
  userId: number;
}
