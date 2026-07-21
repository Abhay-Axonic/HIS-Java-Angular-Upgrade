export interface AmbulanceFacilityRow {
  ambulanceFacilitiesMasterId: number;
  ambFacilities: string;
  status: 'A' | 'I';
}

export interface AmbulanceFacilityDetail {
  ambulanceFacilitiesMasterId: number;
  ambFacilities: string;
}

export interface AmbulanceFacilitySavePayload {
  ambFacilities: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

export interface AmbulanceFacilityUpdatePayload {
  ambulanceFacilitiesMasterId: number;
  ambFacilities: string;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

export interface AmbulanceFacilityStatusPayload {
  ambulanceFacilitiesMasterId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}
