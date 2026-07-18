export interface GenderOption {
  id: number;
  gender: string;
}

export interface PatientSearchResult {
  patientId: number;
  patientName: string;
  uhIdNumber: string;
  admissionId?: number;
}

export interface AdmissionDetail {
  admissionId: number;
  isFinalBill: 'Y' | 'N';
  bedNumber: string;
  wardName: string;
  admissionNumber: string;
  [key: string]: unknown;
}

export interface PeriodicityServiceSearchResult {
  serviceId: number;
  displayName: string;
  periodicityServiceConfigId: number;
  equipmentId?: number;
  equipmentName?: string;
  productCategoryId?: number;
  assetCategoryId?: number;
}

export interface PeriodicityServOrderEntry {
  serviceName: string;
  serviceId?: number;
  periodicityServiceConfigId?: number;
  equipmentId?: number;
  equipmentName?: string;
  productCategoryId?: number;
  assetCategoryId?: number;
  perServConfig?: unknown;
  fromDate: Date | null;
  fromTime: string;
  toDate: Date | null;
  toTime: string;
  visitTypeId: number | null;
  patientId?: number;
  uhidNumber?: string;
  patientName?: string;
  wardName?: string;
  bedNumber?: string;
  admissionNumber?: string;
  admissionId?: number;
  status?: 'A' | 'I';
}

export interface PeriodicityServOrderListRow {
  orderNo: number;
  orderDetailId: number;
  periodicityServiceOrderId: number | null;
  serviceId: number;
  serviceName: string;
  equipmentName: string;
  fromDate: Date | null;
  fromDateString: string;
  fromTime: string;
  toDate: Date | null;
  toDateString: string;
  toTime: string | null;
  tatTimeName: string;
  serviceRendered: number;
  status: string;
  equipmentId?: number;
  productCategoryId?: number;
  assetCategoryId?: number;
  wardId?: number;
  bedId?: number;
  admissionId?: number;
}
