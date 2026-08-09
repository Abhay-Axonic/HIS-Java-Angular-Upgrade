/**
 * combination-medication-master — parity rebuild of the legacy
 *   views/PharmacyInventory/combination-medication-master.html +
 *   the generic MasterController (common-controller.js).
 *
 * The legacy screen is a STATIC SHELL (228-line template, ZERO ng-bindings,
 * zero API calls, empty selects) — the rebuild reproduces the form + grid
 * chrome and wires the endpoints that demonstrably exist (the Generic
 * master's list/combination/uom endpoints — all verified by calling with the
 * cached token). The save has NO backend path (finding logged) — the rebuild's
 * save button reports that plainly instead of pretending.
 */

export interface GenericOption {
  id: number;
  code?: string;
  name?: string;
  status?: boolean;
  [key: string]: unknown;
}

export interface UomOption {
  id: number;
  name?: string;
  [key: string]: unknown;
}

/** One grid row — the combination-generic list (GET /api/globals/generics/
 * IsCombination/1 rows: id/code/name/status). The generic/strength/unit/
 * display-name cells come from the detail fetch on edit. */
export interface CombinationMedicationRow {
  id: number;
  code?: string;
  name?: string;
  status?: boolean;
  genericName?: string;
  strength?: number | null;
  strengthUnitName?: string;
  displayGenericName?: string;
  [key: string]: unknown;
}

export interface CombinationMedicationForm {
  combinationMedicineId: number | null;
  genericId: number | null;
  strength: number | null;
  strengthUnitId: number | null;
  displayGenericName: string;
}

export const emptyCombinationMedicationForm: CombinationMedicationForm = {
  combinationMedicineId: null,
  genericId: null,
  strength: null,
  strengthUnitId: null,
  displayGenericName: '',
};

export interface ChangeCombinationStatusPayload {
  status: boolean;
}
