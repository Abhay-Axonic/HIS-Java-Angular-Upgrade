export interface BaPrefix {
  prefixId: number;
  prefixDesc: string;
}

export interface BaGender {
  id: number;
  desc: string;
}

export interface BaSpeciality {
  specialityId: number;
  specialityName: string;
}

export interface BaUnitDetail {
  unitId: number;
  unitName: string;
  unitAddress: string;
}

export interface BaWebAppDetail {
  unitDetList: BaUnitDetail[];
  doctorDto: Record<string, unknown>;
  doctorDegrees: string;
  orderMasterDto: { listOrderDetailsMasterDto: { netAmount: number }[] };
}

export interface BaSlot {
  slotId: number;
  fromTimeString: string;
  fromTimeString2: string;
  toTimeString: string;
  appointmentDate2: string;
  isBlockedUnblocked: string;
  slotPatientDetailsListsSize: number;
  allowedOverBookingSlots: number;
}

export interface BaPatientSearchResult {
  patientId: number;
  prefixId: number;
  firstName: string;
  middleName?: string;
  lastName: string;
  genderId: number;
  birthDate: string;
  ageString: string;
  mobileNumber: string;
  email?: string;
  patientCategoryId?: number;
}

export interface BaApplicationConfig {
  defaultPatientCategoryId?: number;
  razorPayKeyId?: string;
  [key: string]: unknown;
}
