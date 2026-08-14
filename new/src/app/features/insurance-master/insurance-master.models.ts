export interface GeoOption {
  countryId?: number;
  stateId?: number;
  districtId?: number;
  cityId?: number;
  areaId?: number;
  countryName?: string;
  stateName?: string;
  districtName?: string;
  cityName?: string;
  areaName?: string;
  postCode?: string;
}

export interface InsuranceMasterRecord {
  insuranceId: number | '';
  insuranceCode: string;
  insuranceDesc: string;
  startDate: string;
  endDate: string;
  address: string;
  countryId: number | '';
  stateId: number | '';
  districtId: number | '';
  cityId: number | '';
  areaId: number | '';
  zipCode: string;
  contactNo: string;
  faxNo: string;
  email: string;
  contactPerson: string;
  blockReportPrint: 'Y' | 'N' | '';
  generalLedgerId?: number;
  generalLedgerName?: string;
  reportPath?: string;
  status?: 'A' | 'I';
  orgId?: number;
  unitId?: number;
  createdBy?: number;
  updatedBy?: number;
  createdDate?: string;
  updatedDate?: string;
}

export function emptyInsuranceMaster(): InsuranceMasterRecord {
  return {
    insuranceId: '',
    insuranceCode: '',
    insuranceDesc: '',
    startDate: '',
    endDate: '',
    address: '',
    countryId: '',
    stateId: '',
    districtId: '',
    cityId: '',
    areaId: '',
    zipCode: '',
    contactNo: '',
    faxNo: '',
    email: '',
    contactPerson: '',
    blockReportPrint: '',
  };
}

export interface AddressDetails {
  address?: string;
  countryName?: string;
  stateName?: string;
  districtName?: string;
  cityName?: string;
  areaName?: string;
  zipCode?: string;
}
