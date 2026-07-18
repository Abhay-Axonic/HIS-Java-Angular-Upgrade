export interface DropdownOption {
  id: number;
  type?: string;
  status?: string;
}

export interface CancelReason {
  id: number;
  reason: string;
}

export interface PatientTypeaheadResult {
  patientName?: string;
  uhid?: string;
  details: string;
}

export interface DoctorTypeaheadResult {
  doctorName: string;
  details: string;
}

export interface AssigningPickerRow {
  id: number;
  prescriptionId: number;
  assignPickerId: number;
  patientName: string;
  ageString: string;
  genderCode: string;
  genderId: number;
  uhid: string;
  imagePath: string | null;
  visitType: string;
  visitDate: string;
  prescriptionDate: string;
  doctorDetail: string;
  wardName: string;
  bedNo: string;
  priority: string;
  status: string;
}

export interface AssigningPickerSearchRequest {
  fromDate: string;
  toDate: string;
  visitType?: number;
  patientId?: number;
  patientName?: string;
  doctorName?: string;
  wardName?: string;
  statusId?: number;
  unitId: number;
}
