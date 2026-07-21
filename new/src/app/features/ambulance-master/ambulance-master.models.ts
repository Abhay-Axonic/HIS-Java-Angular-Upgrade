export interface AmbulanceTypeOption {
  ambulanceType: string;
}

export interface ServiceProviderOption {
  serviceProviderName: string;
}

export interface AmbulanceFacilityOption {
  ambFacilities: string;
}

export interface AmbulanceMasterRow {
  ambulanceMasterId: number;
  ambulanceType: string;
  serviceProvider: string;
  ambulanceVehicalNo: string;
  availableFacilities: string;
  status: 'A' | 'I';
}

export interface AmbulanceMasterDetail {
  ambulanceMasterId: number;
  ambulanceType: string;
  serviceProvider: string;
  ambulanceVehicalNo: string;
  availableFacilities: string;
  inhouse: 'Y' | 'N';
}

/**
 * Response shape of GETSINGLERESOURCEALLOCATION -- see
 * ambulance-master.ts's class doc comment for why reading this response's
 * `listObject[0]` is a guaranteed crash in the screen's real, unfixed
 * behavior (ambulanceMasterController.js:106-124).
 */
export interface AmbulanceResourceAllocation {
  vehicalNo: string;
  serviceProvider: string;
}

export interface AmbulanceMasterSavePayload {
  serviceProvider: string;
  ambulanceType: string;
  ambulanceVehicalNo: string;
  availableFacilities: string;
  inhouse: 'Y' | 'N';
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

export interface AmbulanceMasterUpdatePayload {
  ambulanceMasterId: number;
  ambulanceType: string;
  serviceProvider: string;
  ambulanceVehicalNo: string;
  availableFacilities: string;
  // Deliberately not defaulted to 'N' here -- legacy's updateAmbulanceMaster
  // (ctrl:373-423) sends `$scope.ambulanceMaster.inhouseVehicle` as-is with
  // no fallback, unlike saveAmbulanceMaster's `!= undefined ? ... : 'N'`
  // guard. See the component for the same asymmetry.
  inhouse: 'Y' | 'N' | undefined;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

export interface AmbulanceStatusPayload {
  ambulanceMasterId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}
