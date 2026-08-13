/**
 * Screen 719 — donor registration (blood bank / Donor Registration).
 *
 * Feed row shapes are literal to each live endpoint (verified live
 * 2026-08-12):
 *  - GET /adt/getActivePrefixList           -> {prefixId, prefixDesc, prefixCode, status}
 *  - GET /adt/getActiveGenderList           -> {id, desc, code}
 *  - GET /adt/getActiveNationalityList      -> honest-empty dev list
 *  - GET /global/bloodBank/getActiveBloodGroupMaster/{org}/{unit}
 *                                           -> {bloodGroupId, bloodGroupCode, bloodGroupDesc}
 *  - GET /global/bloodBank/getActiveBloodDonorTypeMaster/{org}/{unit}
 *                                           -> {bloodDonorTypeId, bloodDonorTypeCode, bloodDonorTypeDesc}
 *  - GET /adt/getActiveIdentificationList   -> {identificationId, identificationName, identificationCode}
 *
 * DonorPayload is LITERAL to com.param.bloodBank.dto.DonorRegistrationDto
 * (verified 2026-08-12): bloodGroupId / donoTypeId / document-expiry are NOT
 * DTO fields — the legacy form validates them but the backend drops them at
 * registration (they are consumed by the later screening flow). Do not add
 * invented fields (payload literalism rule).
 */
export interface PrefixRow {
  prefixId: number;
  prefixDesc: string;
  prefixCode: string;
  status: string;
}

export interface GenderRow {
  id: number;
  desc: string;
  code: string | null;
}

export interface NationalityRow {
  id?: number;
  desc?: string;
}

export interface BloodGroupRow {
  bloodGroupId: number;
  bloodGroupCode: string;
  bloodGroupDesc: string;
}

export interface DonorTypeRow {
  bloodDonorTypeId: number;
  bloodDonorTypeCode: string;
  bloodDonorTypeDesc: string;
}

export interface IdentificationRow {
  identificationId: number;
  identificationName: string;
  identificationCode: string | null;
}

export interface DonorPayload {
  organizationId: number;
  unitId: number;
  prefixId?: number | null;
  firstName: string;
  middleName?: string | null;
  lastName: string;
  genderId?: number | null;
  /** "DD-MM-YYYY HH:mm:ss" — the Dozer mapping date-format (DQ-056). */
  dob?: string | null;
  mobileNumber: string;
  countryCallingCode?: string | null;
  email?: string | null;
  nationalityId?: number | null;
  raceId?: number | null;
  maritalStatusId?: number | null;
  identificationTypeId?: number | null;
  identificationNumber?: string | null;
  address?: string | null;
  createdBy: number;
  updatedBy: number;
  createdDate?: string | null;
  updatedDate?: string | null;
}

export interface DonorSaveResult {
  donorId?: number | null;
  donorNumber?: string | null;
  patientId?: number | null;
  firstName?: string | null;
  lastName?: string | null;
  mobileNumber?: string | null;
}
