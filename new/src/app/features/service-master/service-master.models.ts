/**
 * serviceMaster — legacy state `serviceMaster` (app.js:3366), module id 376
 * ("Admin - Global Master (general)"). Rebuilt from
 *   views/master/GlobalMaster/service-master.html +
 *   resources/assets/js/controller/master/serviceMasterController.js
 * Endpoint paths and payload shapes taken from the legacy controller's
 * GenericService/BillingGenericService calls and the values of
 * billing.url.config.js / global.url.config.js / adt.url.config.js
 * constants — not guessed.
 */

/** A service-master row as returned by the list / get-by-id endpoints. */
export interface ServiceMasterRecord {
  serviceMasterId: number;
  specialityId: number | null;
  specialityName?: string;
  subSpecialityId: number | null;
  subSpecialityName?: string;
  serviceStandardCode: string;
  serviceStandardName: string;
  codificationServiceName?: string | null;
  generalLedgerId: number | null;
  generalLedgerCode?: string;
  generalLedgerName?: string;
  status: 'A' | 'I';
  organizationId?: number;
  unitId?: number;
  [key: string]: unknown;
}

/** Department dropdown row (POST /adt/getSpecialityList). */
export interface SpecialityMaster {
  specialityId: number;
  specialityName: string;
  status: 'A' | 'I';
  [key: string]: unknown;
}

/** Sub-department dropdown row (GET /adt/subspeciality/orgId/.../specialityId/...). */
export interface SubSpecialityMaster {
  subSpecialityMasterId: number;
  subSpecialityMasterName: string;
  [key: string]: unknown;
}

/** General-ledger dropdown row (POST /org/getGeneralLedgerMasterActiveList/{orgId}). */
export interface GeneralLedgerMaster {
  generalLedgerId: number;
  generalLedgerName: string;
  generalLedgerCode?: string;
  [key: string]: unknown;
}

/** Item typeahead row (POST /api/globals/items/typeahead/search). */
export interface ItemMaster {
  itemId: number;
  itemName: string;
  [key: string]: unknown;
}

/**
 * One inventory-item line in the modal grid. Mirrors the legacy
 * `inventoryItems` array: new lines have serviceItemMapperId=null (spliced
 * on remove), persisted lines keep their id and are flagged 'I' on remove
 * (controller.js:302-310) so the mapper update deactivates them.
 */
export interface InventoryItemLine {
  itemId: number | null;
  itemName: string;
  assignQty: number;
  status: 'A' | 'I';
  serviceItemMapperId: number | null;
}

/**
 * One service-item mapper entry sent in the save/update payload. Built from
 * the inventory lines exactly as the legacy addServiceItemMapperList does
 * (controller.js:312-343): serviceId null, dates 'DD-MM-YYYY HH:mm:ss'.
 */
export interface ServiceItemMapperPayload {
  serviceId: number | null;
  itemId: number;
  serviceItemMapperId: number | null;
  assignQty: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
  createdBy: number;
  updatedBy: number;
  status: 'A' | 'I';
}

/** Save payload (POST /billing/saveOrganizationServiceMaster). */
export interface SaveServiceMasterPayload {
  specialityId: number | null;
  subSpecialityId: number | null;
  serviceStandardName: string;
  codificationServiceName?: string | null;
  serviceStandardCode: string;
  generalLedgerId: number | null;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
  serviceItemMapperList: ServiceItemMapperPayload[];
}

/** Update payload (PUT /billing/updateGlobalServiceMaster). */
export interface UpdateServiceMasterPayload {
  specialityId: number | null;
  subSpecialityId: number | null;
  serviceStandardName: string;
  codificationServiceName?: string | null;
  serviceStandardCode: string;
  generalLedgerId: number | null;
  status: 'A';
  updatedBy: number;
  organizationId: number;
  updatedDate: string;
  serviceMasterId: number;
  serviceItemMapperList: ServiceItemMapperPayload[];
}

/** Status-toggle payload (PUT /billing/changeOrganizationServiceMasterStatus). */
export interface ChangeServiceMasterStatusPayload {
  serviceMasterId: number;
  organizationId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}

/** Form state — mirrors legacy $scope.masterObj. */
export interface ServiceMasterForm {
  serviceMasterId: number | null;
  specialityId: number | null;
  subSpecialityId: number | null;
  serviceName: string;
  codificationServiceName: string;
  serviceCode: string;
  generalLedgerId: number | null;
  generalLedgerName: string;
}

export function emptyServiceMaster(userId: number, organizationId: number): ServiceMasterForm {
  return {
    serviceMasterId: null,
    specialityId: null,
    subSpecialityId: null,
    serviceName: '',
    codificationServiceName: '',
    serviceCode: '',
    generalLedgerId: null,
    generalLedgerName: '',
  };
}
