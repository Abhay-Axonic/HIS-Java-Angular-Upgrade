export interface PatientSearchResult {
  patientId: number;
  admissionId: number;
  patientName: string;
  genderCode: string;
  uhIdNumber: string;
  birthDate: string;
  bedNumber: string;
  wardName: string;
  doctorName: string;
  createdDate: string;
  createdTime: string;
}

export interface FluidOption {
  intakeFluidId?: number;
  intakeFluiddesc?: string;
  outputTypeId?: number;
  outputTypeDesc?: string;
}

export interface FluidTypeOption {
  intakeTypeFluidId: number;
  intakeTypeFluidDesc: string;
}

export interface ShiftOption {
  shiftId: number;
  shiftName: string;
}

export interface IntakeRow {
  intakeStartDate: string;
  intakeStartTime: string;
  intakeShiftId: number | '';
  intakeFluidId: number | '';
  intakeTypeFluidId: number | '';
  intakeQuantity: number | '';
  fluidTypeOptions: FluidTypeOption[];
  shiftOptions: ShiftOption[];
}

export interface OutputRow {
  outputStartDate: string;
  outputStartTime: string;
  outputShiftId: number | '';
  outputTypeId: number | '';
  outputQuantity: number | '';
  shiftOptions: ShiftOption[];
}

export function emptyIntakeRow(): IntakeRow {
  return {
    intakeStartDate: '',
    intakeStartTime: '',
    intakeShiftId: '',
    intakeFluidId: '',
    intakeTypeFluidId: '',
    intakeQuantity: '',
    fluidTypeOptions: [],
    shiftOptions: [],
  };
}

export function emptyOutputRow(): OutputRow {
  return {
    outputStartDate: '',
    outputStartTime: '',
    outputShiftId: '',
    outputTypeId: '',
    outputQuantity: '',
    shiftOptions: [],
  };
}

export interface ChartHistoryRow {
  date?: string;
  shift?: string;
  time?: string;
  intakeTypeFluidDesc?: string;
  itemName?: string;
  startIntakeQuantity?: number;
  totalIntake?: number;
  outputTypeDesc?: string;
  startOutputQuantity?: number;
  totalOutput?: number;
  sum?: number;
  isIoStart?: boolean;
}
