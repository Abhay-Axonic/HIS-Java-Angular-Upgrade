export interface HierarchyOption {
  hierarchyId: number;
  hierarchyDesc: string;
}

export interface OccupancyUnitOption {
  occupancyUnitId: number;
  occupancyUnitDesc: string;
  status?: string;
}

export interface BillingBedCategoryOption {
  billingBedCategoryId: number;
  billingBedDesc: string;
}

export interface ConsultationServiceOption {
  serviceMasterId: number;
  serviceStandardName: string;
}

/** Mirrors a `listObject` entry from GETBEDCATEGORYLIST -- IDs plus the
 * joined description columns the list endpoint already returns. */
export interface BedCategoryRow {
  bedCategoryId: number;
  bedCategoryCode: string;
  bedCategoryDesc: string;
  billingBedCategoryId: number;
  billingBedDesc?: string;
  hierarchyId: number;
  hierarchyDesc?: string;
  occupancyUnitId: number;
  occupancyUnitDesc?: string;
  serviceId?: number | null;
  serviceStandardName?: string | null;
  isBedRetention: 'Y' | 'N';
  isIcu: boolean;
  status: 'A' | 'I';
}

/** Mirrors `listObject[0]` from GETSINGLEBEDCATEGORY (ctrl:365-388). */
export interface BedCategoryDetail {
  bedCategoryId: number;
  bedCategoryCode: string;
  bedCategoryDesc: string;
  hierarchyId: number;
  occupancyUnitId: number;
  isBedRetention: 'Y' | 'N';
  isIcu: boolean;
  billingBedCategoryId: number;
  serviceId: number | null;
}

/** Mirrors $scope.bedCategory, the add/edit form model (ctrl:55-58). */
export interface BedCategoryFormState {
  bedCategoryId?: number;
  bedCategoryCode: string;
  bedCategoryName: string;
  isBedRetention: 'Y' | 'N';
  isIcu: boolean;
  hierarchyId: number | '';
  occupancyUnitId: number | '';
  billingBedCategoryId: number | '';
  serviceId: number | null;
}

export function emptyBedCategoryForm(): BedCategoryFormState {
  return {
    bedCategoryId: undefined,
    bedCategoryCode: '',
    bedCategoryName: '',
    isBedRetention: 'N',
    isIcu: false,
    hierarchyId: '',
    occupancyUnitId: '',
    billingBedCategoryId: '',
    serviceId: null,
  };
}

/** Save payload for ADDBEDCATEGORY / UPDATEBEDCATEGORY (ctrl:293-309, 556-570). */
export interface BedCategorySavePayload {
  bedCategoryId?: number;
  bedCategoryCode: string;
  bedCategoryDesc: string;
  isBedRetention: 'Y' | 'N';
  isIcu: boolean;
  hierarchyId: number | '';
  occupancyUnitId: number | '';
  billingBedCategoryId: number | '';
  status?: 'A';
  createdBy?: number;
  updatedBy: number;
  unitId: number;
  organizationId: number;
  updatedDate: string;
  createdDate?: string;
  serviceId: number | null;
}

/** Payload for UPDATEBEDCATEGORYSTATUS (ctrl:402-407). */
export interface BedCategoryStatusPayload {
  bedCategoryId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}

/** One row of `dayCareIpBedCategoryList` in the Set Hierarchy payload (ctrl:478-492). */
export interface HierarchyRowPayload {
  bedCategoryCode: string;
  bedCategoryDesc: string;
  isBedRetention: 'Y' | 'N';
  isIcu: boolean;
  hierarchyId: number | '';
  occupancyUnitId: number | '';
  billingBedCategoryId: number | '';
  bedCategoryId: number;
  updatedBy: number;
  updatedDate: string;
  serviceId: number | null;
  unitId: number;
  organizationId: number;
}

/** Full payload for the `/adt/updateBedCategoryMaster/updateHierarchy` call
 * (ctrl:461-497). The top-level fields are sourced from whatever the
 * add/edit form (`$scope.bedCategory`) currently holds -- not from any one
 * row in `dayCareIpBedCategoryList` -- a real legacy quirk, preserved as-is. */
export interface SetHierarchyPayload {
  bedCategoryCode: string;
  bedCategoryDesc: string;
  isBedRetention: 'Y' | 'N';
  isIcu: boolean;
  hierarchyId: number | '';
  occupancyUnitId: number | '';
  billingBedCategoryId: number | '';
  bedCategoryId?: number;
  updatedBy: number;
  updatedDate: string;
  serviceId: number | null;
  unitId: number;
  organizationId: number;
  dayCareIpBedCategoryList: HierarchyRowPayload[];
}
