export interface TpaOption {
  companyId: number;
  companyDesc: string;
}

export interface InsuranceOption {
  insuranceId: number;
  insuranceDesc: string;
}

/** Row shape returned by getInsuranceTPAMapperList -- one row per TPA mapping. */
export interface InsuranceTpaMapperRow {
  insuranceTpaMapperId: number;
  companyId: number;
  companyDesc?: string;
  status: 'A' | 'I';
}
