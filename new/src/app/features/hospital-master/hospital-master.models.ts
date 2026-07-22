/**
 * Field set taken directly from views/master/GlobalMaster/hospital-master.html
 * + resources/assets/js/controller/master/hospitalMasterController.js
 * (`hospitalMasterController`, no `controllerAs`, wired via app.js's central
 * `$stateProvider` at app.js:7852 -- NOT self-registered). moduleId 371,
 * requireSuperUser: true (the resolve block checks BOTH module 371 AND
 * `cookieObject.isSuperUser == 'Y'`, app.js:7857-7873).
 */
export interface OrganizationOption {
  organizationId: number;
  organizationName: string;
  /** Used by `setTimeZoneOrganizationWise` (ctrl:568-576) to auto-fill the
   * Timezone select when the Organization dropdown changes. */
  unitTimeZone?: string;
}

/** Real DTO field names differ per level (CountryMasterDto/StateMasterDto/
 * DistrictMasterDto/CityMasterDto all confirmed via source read) -- NOT a
 * generic `{id, name}` shape. Matches this codebase's sibling
 * `organization-master`'s identically-shaped `GeoOption`. */
export interface GeoOption {
  countryId?: number;
  countryName?: string;
  stateId?: number;
  stateName?: string;
  districtId?: number;
  districtName?: string;
  cityId?: number;
  cityName?: string;
}

export interface AreaSearchResult {
  areaId: number;
  areaName: string;
  postCode?: string;
}

export interface TimezoneOption {
  code: string;
  value: string;
}

/** Row shape returned by GET /unit/{offset}/{noOfRecordsPerPage}/{orgId}
 * (ctrl:583-601, via promiseFactory). */
export interface HospitalMasterRow {
  unitId: number;
  organizationName: string;
  unitCode: string;
  unitName: string;
  registrationNumber: string;
  unitAddress: string;
  countryName?: string;
  stateName?: string;
  cityName?: string;
  postCode?: string;
  unitContact: string;
  unitEmailId: string;
  processingUnit: boolean;
  status: 'A' | 'I';
}

/** Editable form state -- mirrors legacy's `$scope.unit`
 * (ctrl:125-172/782-808). */
export interface HospitalMasterRecord {
  unitId?: number;
  organizationId: number | '';
  unitCode: string;
  unitName: string;
  status: 'A' | 'I';
  unitDesc: string;
  unitAddress: string;
  unitCountryId: number | '';
  unitDistrictId: number | '';
  unitAreaId: number | '';
  unitStateId: number | '';
  unitCityId: number | '';
  postCode: string;
  unitContact: string;
  registrationNumber: string;
  unitEmailId: string;
  unitLogo: string;
  areaName: string;
  unitTimeZone: string;
  processingUnit: boolean;
  /** Only ever populated on a fresh/new record, from the static
   * adminPriviledges.json asset -- see hospital-master.service.ts's
   * `getAdminPrivileges()` doc comment. Never set on an edited/fetched
   * record (legacy discards it the moment editUnit() overwrites `$scope.unit`
   * with the real fetched record). */
  previlageString?: string;
}

export function emptyHospitalMasterRecord(): HospitalMasterRecord {
  return {
    unitId: undefined,
    organizationId: '',
    unitCode: '',
    unitName: '',
    status: 'A',
    unitDesc: '',
    unitAddress: '',
    unitCountryId: '',
    unitDistrictId: '',
    unitAreaId: '',
    unitStateId: '',
    unitCityId: '',
    postCode: '',
    unitContact: '',
    registrationNumber: '',
    unitEmailId: '',
    unitLogo: '',
    areaName: '',
    unitTimeZone: '',
    processingUnit: false,
  };
}

// ---- Subscription sub-feature (ctrl:1097-1395, view:647-991) -- only
// reachable when editing an existing hospital AND the logged-in user is a
// super-admin for this org/unit (`getSuperAdminUser`, ctrl:199-234; the
// Subscription button is `ng-if="editMode && isSuperAdmin==='Y'"`). ----

export interface SubscriptionTypeOption {
  code: string;
  name: string;
}

/** Mirrors `$scope.subscriptionConfig` (ctrl:1125-1134). */
export interface SubscriptionConfig {
  subscriptionConfigId: number | null;
  subscriptionType: string;
  subscriptionAmount: number | null;
  subscriptionDiscount: number | null;
  subscriptionTax: number | null;
  subscriptionNetAmount: number | null;
  unitId?: number;
  organizationId?: number;
}

export function emptySubscriptionConfig(): SubscriptionConfig {
  return {
    subscriptionConfigId: null,
    subscriptionType: '',
    subscriptionAmount: null,
    subscriptionDiscount: null,
    subscriptionTax: null,
    subscriptionNetAmount: null,
  };
}

/** Row shape of `$scope.subscriptionList` (ctrl:1197-1223). */
export interface SubscriptionHistoryRow {
  subscriptionId?: number;
  subscriptionStartDateString?: string;
  subscriptionEndDateString?: string;
  subscriptionType?: string;
  subscriptionStatus?: string;
  subscriptionAmount?: number;
  subscriptionDiscount?: number;
  subscriptionTax?: number;
  subscriptionNetAmount?: number;
  paidAmount?: number;
  amountPaidStatus?: string;
  createdDate?: string;
  remark?: string;
}

/** Mirrors `$scope.subscription` (ctrl:1283-1314/1348-1378), the manual
 * "Create Subscription" popup's model. */
export interface ManualSubscriptionForm {
  subscriptionId?: number;
  subscriptionConfigId: number | null;
  subscriptionType: string;
  subscriptionAmount: number | null;
  subscriptionDiscount: number | null;
  subscriptionNetAmount: number | null;
  subscriptionTax: number | null;
  paidAmount: number | null;
  amountPaidStatus: 'PAID' | 'UNPAID';
  subscriptionStartDate: Date | null;
  subscriptionEndDate: Date | null;
  subscriptionStatus: string;
  paymentType: string;
  finalReminderCount: number;
  reminderCount: number;
  remark?: string;
}
