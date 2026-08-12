export type CardStatus = 'A' | 'I';

/**
 * Row shapes for the assign-loyalty-card flow (module 752). Every payload
 * below is READ FROM THE LEGACY CONTROLLER'S LITERAL SIGNATURE (T0
 * payload-literalism amendment): patientLoyaltiCardMapperController.js.
 *
 * NOTE the legacy typo `orgnisationId` (missing 'i') on the cardTypeMaster
 * feed — the rebuild sends it byte-for-byte or the feed 500s (T0 finding).
 */

/** Picker search row (POST /adt/patientSearchByMultipleCriteria). */
export interface PatientSearchRow {
  patientId: number;
  patientName?: string;
  uhIdNumber?: string;
  genderId?: number;
  mobileNo?: string;
  birthDate?: string;
  patientType?: string;
  organisationId?: number;
  unitId?: number;
}

/** cardTypeMaster feed row (POST /api/billing/cardTypeMaster, typo payload). */
export interface CardTypeRow {
  cardTypeId: number;
  cardTypeName: string;
  unitId?: number;
  orgnisationId?: number;
  status?: string;
}

/** Existing-card lookup row (POST /api/loyaltyCard/getLoyaltiCardDetailsByPatientId). */
export interface LoyaltyCardRow {
  loyaltyCardId?: number;
  cardNo?: string;
  cardTypeId?: number;
  patientId?: number;
  status?: string;
}

/** The assign write (POST /api/loyaltyCard/patientLoyaltyCard). */
export interface AssignPayload {
  patientLoyaltyCardMpprId: string;
  loyaltyCardId: string;
  patientId: string;
  patientCategoryId?: string;
  cardConfigurationId: string;
  organisationId: number;
  unitId: number;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  isDeleted: 'N';
  isUserDefined: 'Y';
  cardNo: string;
}
