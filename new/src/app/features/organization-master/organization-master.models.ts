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

export interface OrganizationRecord {
  organizationId: number | '';
  organizationCode: string;
  organizationName: string;
  status: 'A' | 'I';
  organizationAddress: string;
  countryId: number | '';
  stateId: number | '';
  cityId: number | '';
  districtId: number | '';
  channelId: string;
  areaId: number | '';
  organizationContact: string;
  faxNumber: string;
  organizationEmailId: string;
  organizationLogo: string;
  registrationNumber: string;
  isDuoAuthenticationRequired: boolean;
  isSyncScheduling: boolean;
  unitTimeZone?: string;
  appUrl?: string;
  postCode?: string;
}

export function emptyOrganization(): OrganizationRecord {
  return {
    organizationId: '',
    organizationCode: '',
    organizationName: '',
    status: 'A',
    organizationAddress: '',
    countryId: '',
    stateId: '',
    cityId: '',
    districtId: '',
    channelId: '',
    areaId: '',
    organizationContact: '',
    faxNumber: '',
    organizationEmailId: '',
    organizationLogo: '',
    registrationNumber: '',
    isDuoAuthenticationRequired: false,
    isSyncScheduling: true,
  };
}

export interface TimezoneOption {
  value: string;
  code: string;
}

export interface ModuleNode {
  moduleId: number;
  moduleName: string;
  parentId: number;
  actionLink?: string;
  children: ModuleNode[];
  selected?: boolean;
}
