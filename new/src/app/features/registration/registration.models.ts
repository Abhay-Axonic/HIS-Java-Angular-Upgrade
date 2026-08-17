/** Registration payloads — field names EXACTLY as PatientRegistrationDto /
 * KinDetailsDto (the @RequestBody of POST /api/global/patientRegistration). */
export interface KinDetailsPayload {
  kinDetailsId?: number | null;
  organizationId?: number | null;
  unitId?: number | null;
  prefixId?: number | null;
  kinName?: string | null;
  relationId?: number | null;
  mobileNo?: string | null;
  phoneNo?: string | null;
  identificationId?: number | null;
  identificationNo?: string | null;
  expiry?: string | null;
  isGuarantor?: string | null;
  countryId?: number | null;
  stateId?: number | null;
  districtId?: number | null;
  cityId?: number | null;
  areaId?: number | null;
  address?: string | null;
  postCode?: string | null;
}

export interface RegistrationPayload {
  patientId?: number | null;
  organizationId: number;
  unitId: number;
  uhidNumber?: string | null;
  prefixId?: number | null;
  firstName: string;
  middleName?: string | null;
  lastName: string;
  genderId?: number | null;
  dob?: string | null;
  mobileNumber?: string | null;
  countryCallingCode?: string | null;
  email?: string | null;
  identificationTypeId?: number | null;
  identificationNumber?: string | null;
  identificationExpiryDate?: string | null;
  patientCategoryId?: number | null;
  address?: string | null;
  countryId?: number | null;
  stateId?: number | null;
  districtId?: number | null;
  cityId?: number | null;
  areaId?: number | null;
  zipCode?: string | null;
  kinDetailsDto?: KinDetailsPayload | null;
  status: string;
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  updatedDate: string;
}

export interface IdNameRow {
  id: number;
  name?: string;
  code?: string;
  desc?: string;
  /** the prefix master's own description — the named query also joins the
   * gender's desc into `desc`, so the prefix text lives here (see the service) */
  prefixDesc?: string;
}

export interface GeoRow {
  id: number;
  name?: string;
}

export interface FieldConfig {
  /** Field names the unit config marks mandatory (e.g. mobileNumber). */
  required?: string[];
  [key: string]: unknown;
}
