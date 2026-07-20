export interface DfamAccessory {
  itemCode: string;
  itemName: string;
  serialNumber: string;
  warrantyFrom: string;
  warrantyTo: string;
  quantity: number;
  specification: string;
}

export interface DfamItem {
  itemCode: string;
  itemName: string;
  serialNo: string;
  yearOfManufacture: string;
  vendor: string;
  serviceProvider: string;
  warrantyFrom: string;
  warrantyTo: string;
  pmsfrequency: string;
  quantity: number;
  cop: number;
  mrp: number;
  specification: string;
  accesories?: DfamAccessory[];
  spares?: DfamAccessory[];
  insurance?: {
    insuranceCompanyId?: number;
    insuranceCompany?: string;
    policyNumber?: string;
    policyFromDate?: string;
    policyToDate?: string;
    amountInsured?: number;
    remark?: string;
  };
}

export interface DfamDetail {
  opbNo?: string;
  opbDate?: string;
  store?: string;
  requestedBy?: string;
  status?: string;
  items: DfamItem[];
}
