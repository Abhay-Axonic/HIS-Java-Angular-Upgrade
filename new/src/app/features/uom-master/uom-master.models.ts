/**
 * uom-master — parity rebuild of the legacy
 *   views/master/GlobalMaster/OTMS/uom-master.html
 *   (state `uomMaster`, module id 466, parent 457 Pharmacy -> 369 Global
 *   Master; controller: the generic MasterController — no screen logic).
 *
 * The legacy is a STATIC SHELL (the approved precedent): a 119-line template
 * with two decorative ng-models (`common.code`, `common.uomCode` — the
 * MasterController defines no `common` object, so they bind to nothing), an
 * inert Save anchor, an inert search box, and one hardcoded grid row. Zero
 * API calls. The rebuild IMPLEMENTS the shell (per the owner's standing
 * decision): the form + grid are wired to the endpoints that demonstrably
 * exist, and every missing endpoint is reported honestly in the UI.
 */

export interface UomUnitRow {
  id: number;
  code?: string;
  units?: string;
  status?: boolean;
  [key: string]: unknown;
}

export interface UomMasterForm {
  code: string;
  units: string;
}

export const emptyUomMasterForm: UomMasterForm = {
  code: '',
  units: '',
};
