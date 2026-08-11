/**
 * The `OPConsulationConfigurationMaster` state (module 611) has NO backend list endpoint
 * (DQ-2026-08-12-020): its controller is the generic MasterController shell
 * (common-controller.js — zero api refs) or does not exist in the webapp.
 * No row model exists; the screen has nothing to list (noData class, DQ-005).
 */
export interface OPConsulationConfigurationMasterRow {
  // no data path in the legacy backend — nothing to list
}
