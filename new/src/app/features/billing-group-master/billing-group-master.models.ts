export interface SpecialityMaster {
  specialityId: number;
  specialityName: string;
}

export interface BillingGroupSpeciality {
  specialityId: number;
  specialityName: string;
}

export interface BillingGroupMasterRecord {
  billingGroupId: number | '';
  billingGroup: string;
  billingCode: string;
  billingGroupDesc: string;
  status: 'A' | 'I';
  createdDate: string;
  createdBy: string;
  updatedDate: string;
  updatedBy: string;
  unitId: number;
  orgnisationId: number;
  listSpecialityMasterDto: BillingGroupSpeciality[];
}

export function emptyBillingGroupMaster(unitId: number, organizationId: number): BillingGroupMasterRecord {
  return {
    billingGroupId: '',
    billingGroup: '',
    billingCode: '',
    billingGroupDesc: '',
    status: 'A',
    createdDate: '',
    createdBy: '',
    updatedDate: '',
    updatedBy: '',
    unitId,
    orgnisationId: organizationId,
    listSpecialityMasterDto: [],
  };
}
