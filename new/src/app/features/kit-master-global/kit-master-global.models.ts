/**
 * `kitMaster` -- the legacy screen is driven by a MasterController shell (common-controller.js) that performs zero endpoint calls. The noData class (DQ-2026-08-12-022:
 * no endpoint at all is its own class). No row model exists; the screen has
 * nothing to list.
 */
export interface KitMasterRow {
  // no data path in the legacy backend -- nothing to list
}
