export type AutoclaveStatus = 'A' | 'I';

/** Sterilization Type dropdown option (ctrl:91-103,
 * GET /cssd/getActiveSterilizationTypesList). */
export interface SterilizationTypeOption {
  id: number;
  code: string;
  name: string;
}

/** Sterilization Color dropdown option (ctrl:105-116,
 * POST /cssd/getActiveSterilizationColorList). This dev DB's
 * m_sterilization_color_indicator table (org/unit-unscoped, a genuinely
 * global reference table) was completely empty -- see
 * docs/ANGULAR_PENDING_FINDINGS.md, PENDING-batch24-step2-autoclavemaster-goldentrace. */
export interface SterilizationColorOption {
  colorId: number;
  colorName: string;
}

/** Row shape returned by getAllAutoclavesList (ctrl:122-158). Confirmed
 * live 2026-07-28 -- `sterilization_type` is a joined display name,
 * `sterilization_color_id` is present but never rendered in the table
 * (view has no Sterilization Color column at all, confirmed by direct
 * read). */
export interface AutoclaveRow {
  id: number;
  machine_name: string;
  machine_description: string;
  sterilization_type: string;
  sterilization_type_id: number;
  sterilization_temperature: number;
  sterilization_pressure: number;
  biological_indicator_day: number;
  sterilization_color_id: number;
  status: AutoclaveStatus;
}

/** Mirrors $scope.autoClaveMasterObj (ctrl:70-87). */
export interface AutoclaveForm {
  id?: number;
  machine_name: string;
  machine_description: string;
  sterilization_type_id: number | '';
  sterilization_color_id: number | '';
  sterilization_temperature: string;
  sterilization_pressure: string;
  biological_indicator_day: string;
}

export function emptyAutoclaveForm(): AutoclaveForm {
  return {
    id: undefined,
    machine_name: '',
    machine_description: '',
    sterilization_type_id: '',
    sterilization_color_id: '',
    sterilization_temperature: '',
    sterilization_pressure: '',
    biological_indicator_day: '',
  };
}
