export type MachineParameterStatus = 'A' | 'I';

/** Row shape returned by `listMachineParaMaster` (dao query `GET_PAGINATED_MACHINE_PARAMETER_MASTER_LIST`). */
export interface MachineParameterRow {
  machinParameterId: number;
  code: string;
  machineParameterDesc: string;
  machine: string; // joined machine description text, read-only
  status: MachineParameterStatus;
}

/** Option shape returned by `getActiveMachineList` (ctrl:89-114). */
export interface MachineOption {
  machineId: number;
  machineDesc: string;
}

/** Editable form state -- mirrors `$scope.common` (ctrl:51-64). */
export interface MachineParameterForm {
  machinParameterId?: number;
  code: string;
  machineParameterDesc: string;
  machineId: string; // bound to the <select>, empty string = unselected
}

export function emptyMachineParameterForm(): MachineParameterForm {
  return { machinParameterId: undefined, code: '', machineParameterDesc: '', machineId: '' };
}

/** POST body for `addMachineParameter` (ctrl:259-300). */
export interface MachineParameterSavePayload {
  machineId: number;
  code: string;
  machineParameterDesc: string;
  status: 'A';
  organisationId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
}

/** PUT body for `updateMachineParaMaster` (ctrl:302-341). */
export interface MachineParameterUpdatePayload {
  machinParameterId: number;
  machineId: number;
  code: string;
  machineParameterDesc: string;
  organisationId: number;
  unitId: number;
  updatedBy: number;
}
