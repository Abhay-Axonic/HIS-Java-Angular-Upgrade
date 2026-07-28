export type ChecklistEquipmentMapperStatus = 'A' | 'I';

/** Options for the Checklist Name dropdown (ctrl:64-77,
 * getAllEquipmentChecklistMasterList) -- confirmed live 2026-07-28. */
export interface ChecklistOption {
  checklistEquipmentMasterId: number;
  checklistCode: string;
  checklistName: string;
  status: ChecklistEquipmentMapperStatus;
}

/** Options for the Asset Category dropdown (ctrl:79-88,
 * /api/global/assets/categories/2/{unitId}/{organizationId} -- the `2` path
 * segment is a fixed asset-type-code constant, confirmed live, not guessed). */
export interface AssetCategoryOption {
  id: number;
  category: string;
}

/** Options for the Product Category dropdown, cascaded from Asset Category
 * (ctrl:118-130, /api/global/products/categories/{assetCategoryId}/{organizationId}).
 * Confirmed live 2026-07-28 -- this endpoint 500'd in this dev environment
 * until a real backend schema-drift fix (see
 * docs/BUSINESS_RULE_MANIFEST.md, BR-local-dev-db-drift-productcategory-generalledgerid). */
export interface ProductCategoryOption {
  id: number;
  category: string;
}

/** Row shape returned by getAllEquipmentChecklistMapperMasterList (ctrl:91-110).
 * Confirmed live 2026-07-28 -- joined display names (checklistEquipmentName/
 * assetCategoryName/productCategoryName) alongside the raw ids. */
export interface ChecklistEquipmentMapperRow {
  checklistEquipmentMapperMasterId: number;
  checklistEquipmentMasterId: number;
  checklistEquipmentName: string;
  assetCategoryId: number;
  assetCategoryName: string;
  productCategoryId: number;
  productCategoryName: string;
  itemCode: string;
  itemName: string;
  status: ChecklistEquipmentMapperStatus;
}

/** Mirrors $scope.checklistMapperObj (ctrl:48-62). One object serves BOTH
 * create and edit -- same real upsert shape as this batch's
 * checklistEquipmentMaster sibling, confirmed via direct DAO read
 * (EquipmentChecklistMapperDaoImpl chooses save()/update() based on whether
 * checklistEquipmentMapperMasterId is present). */
export interface ChecklistEquipmentMapperForm {
  checklistEquipmentMapperMasterId: number | null;
  checklistEquipmentMasterId: number | '';
  assetCategoryId: number | '';
  productCategoryId: number | '';
  itemCode: string;
  itemName: string;
}

export function emptyChecklistEquipmentMapperForm(): ChecklistEquipmentMapperForm {
  return {
    checklistEquipmentMapperMasterId: null,
    checklistEquipmentMasterId: '',
    assetCategoryId: '',
    productCategoryId: '',
    itemCode: '',
    itemName: '',
  };
}
