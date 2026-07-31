export interface BloodBagOption {
  bloodBagTypeId: number;
  bloodBagTypeDesc: string;
}

export interface BloodComponentOption {
  bloodComponentId: number;
  bloodComponentDesc: string;
}

/** Row shape returned by getBloodBankTypeComponentMasterList -- one row per bag type. */
export interface BloodBankTypeComponentRow {
  bloodBagTypeId: number;
  bloodBagTypeDesc: string;
  status: 'A' | 'I';
}

/** Row shape returned by getDetailedComponentList (view-details modal). */
export interface DetailedComponentRow {
  bloodComponentId: number;
  bloodComponentDesc: string;
}
