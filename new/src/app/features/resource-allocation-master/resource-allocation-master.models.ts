/**
 * Field set taken directly from
 * views/master/UnitMaster/ambulanceService/resource-Allocation-Master.html +
 * resources/assets/js/controller/lis/global/resourceAllocationController.js
 * (`resourceAllocationController`, no `controllerAs`).
 */

/**
 * Legacy quirk, faithfully reproduced (NOT a bug -- see the component's
 * class doc comment): the "Ambulance Type" dropdown is populated from
 * `GETAMBULANCETARRIFACTIVELIST` (`/adt/getAmbulanceTarrifActiveList`, an
 * organization-scoped active-TARIFF list), not from a dedicated
 * ambulance-type-master list (ctrl:51-66). Only `ambulanceType` is ever read
 * off each entry (view:14, `info.ambulanceType`); every other
 * `AmbulanceTarrifDto` field the backend returns is ignored here, matching
 * legacy.
 */
export interface AmbulanceTypeTariffOption {
  ambulanceType: string;
}

export interface ResourceAllocationRow {
  resourceAllocationMasterId: number;
  ambulanceType: string;
  ambulanceProvider: string;
  vehicalNo: string;
  resourceName: string;
  mobileNo: string;
  status: 'A' | 'I';
}

/** Shape of GETSINGLERESOURCEALLOCATION's `listObject[0]` -- only the fields
 * `showUpdateBtn` (ctrl:325-354) actually reads back into the form. */
export interface ResourceAllocationDetail {
  resourceAllocationMasterId: number;
  ambulanceType: string;
  ambulanceProvider: string;
  vehicalNo: string;
  resourceName: string;
  mobileNo: string;
}

/** Mirrors `saveResourceAllocation`'s `data` object (ctrl:217-229). */
export interface ResourceAllocationSavePayload {
  ambulanceType: string;
  ambulanceProvider: string;
  vehicalNo: string;
  resourceName: string;
  mobileNo: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** Mirrors `updateResourceAllocation`'s `data` object (ctrl:402-412) -- note
 * no `status` field is sent on update; status only changes via the separate
 * `changeResourceAllocationStatus` endpoint. */
export interface ResourceAllocationUpdatePayload {
  resourceAllocationMasterId: number;
  ambulanceType: string;
  ambulanceProvider: string;
  vehicalNo: string;
  resourceName: string;
  mobileNo: string;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

/** Mirrors `updateStatus`'s `data` object (ctrl:364-370). */
export interface ResourceAllocationStatusPayload {
  resourceAllocationMasterId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

/** Form/edit-buffer shape (mirrors `$scope.resourceAllocation`).
 * `resourceAllocationMasterId` is only present while editing an existing row
 * (mirrors the view's hidden input, view:17). */
export interface ResourceAllocationForm {
  resourceAllocationMasterId?: number;
  ambulanceType: string;
  ambulanceProvider: string;
  vehicalNo: string;
  resourceName: string;
  mobileNo: string;
}

export function emptyResourceAllocationForm(): ResourceAllocationForm {
  return {
    ambulanceType: '',
    ambulanceProvider: '',
    vehicalNo: '',
    resourceName: '',
    mobileNo: '',
  };
}
