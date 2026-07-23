/**
 * Rebuild of the legacy `detailsCorrectiveMaintenanceWorklist` state -- see
 * detailsCorrectiveMaintenanceWorklistController.js (194 lines) +
 * details-corrective-maintenance-worklist.html (383 lines). Row data
 * arrives via `CorrectiveMaintenanceWorklistHandoffService.takeObject()`
 * (the rebuild's equivalent of legacy's `$stateParams.detailsObj`,
 * ctrl:58) -- see `CorrectiveMaintenanceWorklistDetailObject` in
 * `core/corrective-maintenance-worklist-handoff.service.ts` for the shape
 * of the received row.
 */

/** `usersList` rows -- getEmployeeListBySpecialityAndSubSpeciality (ctrl:93-119). */
export interface DcmwEmployee {
  userId?: number;
  name?: string;
  [key: string]: unknown;
}

/**
 * `saveObj` payload built by `saveDetailsWorklist()` (ctrl:123-139), sent to
 * `saveCorrectiveMaintenance/worklist`. `maintenanceWorklistStatusId` is
 * ALWAYS hardcoded to 3 ("Assigned") here regardless of the row's real
 * prior status -- see the class doc comment on
 * `DetailsCorrectiveMaintenanceWorklist` for the faithfully reproduced bug
 * this represents.
 */
export interface DcmwSaveWorklistPayload {
  correctiveMaintenanceWorklistId: number | null;
  correctiveMaintenanceId?: number;
  scheduleDate: string;
  scheduleTime: string;
  maintenanceWorklistStatusId: number;
  assignTo: number | string;
  allocationRemark: string;
  createdBy: number;
  createdDate: string;
  status: string;
  updatedBy: number;
  updatedDate: string;
  unitId: number;
  organizationId: number;
}
