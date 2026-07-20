export interface AssetCategoryOption {
  id: number;
  category: string;
}

export interface ProductCategoryOption {
  id: number;
  category: string;
  productCategoryId?: number;
}

export interface TaxOption {
  id: number;
  type: string;
}

export interface StorageUnitOption {
  id: number;
  unit: string;
}

export interface SpecialityOption {
  specialityId: number;
  specialityName: string;
}

export interface SpecialityRow {
  specialityName: SpecialityOption | null;
  id?: number;
}

export interface ManufacturerResult {
  id: number;
  manufacturerName: string;
  detail: string;
}

export interface ImplantMasterRow {
  id: number;
  assetCategory: string;
  productCategory: string;
  itemCode: string;
  productCode: string;
  name: string;
  manufactureName: string;
  pTaxName: string;
  sTaxName: string;
  storageUnit: string;
  isConsignment: boolean;
  isBatchRequired: boolean;
  isSterilized: boolean;
  status: boolean;
  speciality?: { specialityName: string }[];
}

export interface ImplantMasterDetail {
  id: number;
  assetTypeId: number;
  productCategoryId: number;
  itemCode: string;
  productCode: string;
  itemName: string;
  manufactureId: number;
  manufacturer: string;
  sTaxId: number;
  pTaxId: number;
  storageUnitId: number;
  isConsignment: boolean;
  isSterilize: boolean;
  isBatchRequired: boolean;
  status: boolean;
  speciality: { specialityId: number; specialityName: string }[];
}

export interface ImplantMasterSavePayload {
  productCategoryId: number;
  manufactureId: number | undefined;
  itemCode: string;
  sTaxId: number;
  pTaxId: number;
  productCode: string;
  itemName: string;
  storageUnitId: number;
  status: boolean;
  isConsignment: boolean;
  isSterilize: boolean;
  specialeety: { id: number | undefined }[];
  isBatchRequired: boolean;
  unitId?: number;
  organizationId?: number;
  userId?: number;
}
