/**
 * resource-configuration — parity rebuild of the legacy
 *   views/master/TheatreManagement/resource-configuration.html
 *   (menu node 517 'Resource Configuration', parent 514 OTIMS ->
 *   369 Global Master, actionLink #/resourceConfiguration).
 *
 * LIVE CLASS (per the amended reachability rule): the state IS registered
 * (`state('resourceConfiguration')` in app.js), the menu node exists, the
 * controller IS loaded (index.html:1733 resourceConfigurationMasterController.js),
 * and a REAL backend exists (Global SurgeryResourceConfigController,
 * /api/otims/*, table otims.t_surgery_resource_config). This is the first
 * screen in the OTIMS subtree after the 476 subtree was exhausted.
 */
export interface ResourceConfigurationRow {
  surgeryResourceConfigId: number;
  specialityId?: number | null;
  serviceId?: number | null;
  specialityName?: string | null;
  procedureName?: string | null;
  visitTypeId?: number | null;
  visitTypeName?: string | null;
  tariffId?: number | null;
  tariffDesc?: string | null;
  surgeryGradeId?: number | null;
  surgeryGrade?: string | null;
  doctorGradeId?: number | null;
  docGradeName?: string | null;
  patientCategoryId?: number | null;
  patientCategory?: string | null;
  paymentEntitlementId?: number | null;
  paymentEntitlementDesc?: string | null;
  bedCategoryId?: number | null;
  billingBedDesc?: string | null;
  surgeonPercentage?: number | null;
  asstSurgeonPercentage?: number | null;
  anesthesiologistPercentage?: number | null;
  otPercentage?: number | null;
  status?: string | null;
  surgeonDependentOn?: string | null;
  asstSurgeonDependentOn?: string | null;
  anesthesiologistDependentOn?: string | null;
  otDependentOn?: string | null;
}

export interface ResourceConfigListPayload {
  orgId: number;
  unitId: number;
  offset: number;
  noOfRecordsPerPage: number;
}
