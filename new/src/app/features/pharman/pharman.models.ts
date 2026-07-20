export interface PharmanDrugSuggestion {
  medicine: string;
}

export interface PharmanDrugForm {
  formulation: string;
}

/**
 * Fields match the transform in pharmanController.js:109-126 exactly
 * (field names are the legacy backend's own, all-lowercase-run-together).
 */
export interface PharmanPopupInfo {
  allergyandcrosssensitivity?: string;
  cautions?: string;
  hepaticimpairment?: string;
  indicationsanddoses?: string;
  medicinalforms?: string;
  medicine?: string;
  monitoringrequirements?: string;
  patientandcareradvice?: string;
  prescribinganddispensinginformations?: string;
  pretreatmentscreeninginformation?: string;
  sideeffects?: string;
  renalimpairment?: string;
  /** Rendered in the view (`{{info.interactions}}`, medicineInfo.html:269)
   * but never actually copied into this shape by the controller's own
   * transform (pharmanController.js:109-124) -- confirmed always blank
   * in legacy, kept here only so the template compiles; not a rebuild bug. */
  interactions?: string;
}
