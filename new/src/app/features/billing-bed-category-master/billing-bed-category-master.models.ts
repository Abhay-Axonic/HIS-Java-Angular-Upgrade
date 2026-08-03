export interface HierarchyOption {
  hierarchyId: number;
  hierarchyDesc: string;
}

export interface VisitTypeOption {
  id: number;
  label: string;
}

// Hardcoded static list, verbatim from billingBedCategoryMasterUnitController.js
// ctrl:67-84 ($scope.visitTypeList) -- not fetched from any endpoint.
export const VISIT_TYPE_OPTIONS: VisitTypeOption[] = [
  { id: 3, label: 'Day Care' },
  { id: 4, label: 'ER' },
  { id: 2, label: 'IP' },
  { id: 1, label: 'OP' },
];

export interface VisitTypeRef {
  id: number;
}

// GET_BILLING_BED_CATEGORY_LIST (BillingBedCategoryMaster.java:54-68) +
// BillingBedCategoryMasterServiceImpl.getBillingBedCategoryList (service:154-
// 177), which fans out a 2nd query per row (getBillingBedCategoryVistList,
// status='A' mappings only) to populate visitTypeMasterDtosList.
export interface BillingBedCategoryRow {
  billingBedCategoryId: number;
  billingBedCode: string;
  billingBedDesc: string;
  hierarchyId: number;
  hierarchyDesc: string;
  status: 'A' | 'I';
  isPeriodicBilling: 'Y' | 'N' | null;
  isGstApplicable: boolean;
  gstAmountRange: number | null;
  gstPercentage: number | null;
  visitTypeMasterDtosList: VisitTypeRef[];
}

// GET_BILLING_BED_CATEGORY_BY_ID (ctrl:362-393, showUpdateBtn).
export interface BillingBedCategoryDetail {
  billingBedCategoryId: number;
  billingBedCode: string;
  billingBedDesc: string;
  hierarchyId: number;
  isPeriodicBilling: 'Y' | 'N' | null;
}

export interface BillingBedCategoryFormState {
  billingBedCategoryId?: number;
  billingBedCode: string;
  billingBedDesc: string;
  hierarchyId: number | '';
  isGstApplicable: boolean;
  gstAmountRange: number | null;
  gstPercentage: number | null;
  isPeriodicBilling: 'Y' | 'N';
  selectedVisitTypeIds: number[];
}

export function emptyBillingBedCategoryForm(): BillingBedCategoryFormState {
  return {
    billingBedCode: '',
    billingBedDesc: '',
    hierarchyId: '',
    isGstApplicable: false,
    gstAmountRange: null,
    gstPercentage: null,
    isPeriodicBilling: 'N',
    selectedVisitTypeIds: [],
  };
}

// saveBillingBed's payload (ctrl:249-265).
export interface BillingBedCategorySavePayload {
  billingBedCode: string;
  billingBedDesc: string;
  hierarchyId: number;
  isGstApplicable: boolean;
  gstAmountRange: number | null;
  gstPercentage: number | null;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  unitId: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
  isPeriodicBilling: 'Y' | 'N';
  visitTypeMasterDtosList: VisitTypeRef[];
}

// updateBillingBed's payload (ctrl:449-462, +newlyAdded/removed lists).
export interface BillingBedCategoryUpdatePayload {
  billingBedCode: string;
  billingBedDesc: string;
  hierarchyId: number;
  billingBedCategoryId: number;
  isGstApplicable: boolean;
  gstAmountRange: number | null;
  gstPercentage: number | null;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
  unitId: number;
  isPeriodicBilling: 'Y' | 'N';
  newlyAddedVisitTypeList: VisitTypeRef[];
  removedVisitTypeList: VisitTypeRef[];
}

export interface BillingBedCategoryStatusPayload {
  billingBedCategoryId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}
