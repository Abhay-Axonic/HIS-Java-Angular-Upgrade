/**
 * Screen 660 — service-master feed row. Shape returned by
 * GET /billing/getListOfServiceMaster/{orgId}/{unitId}
 * (applicationConfigurationController.js, ctrl:955). Verified live 2026-08-12:
 * SUCCESS, 1 row {serviceMasterId, serviceStandardName, ...} — REAL FEED. The
 * full DTO is the ~100-field service master record (DQ-2026-08-12-034); only
 * the live-verified fields are declared here, with an index signature so the
 * remaining service columns do not break reading.
 */
export interface ServiceMasterRow {
  serviceMasterId: number;
  serviceStandardName: string;
  serviceCode?: string;
  status?: string;
  [key: string]: unknown;
}
