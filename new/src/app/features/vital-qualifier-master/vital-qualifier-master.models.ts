export type VitalQualifierStatus = 'A' | 'I';

/** Options for the Category dropdown (ctrl:335-349, `getVitalCategoryList`,
 * `/api/global/getVitalCategoryList`) -- no organizationId filter server-side
 * (commented out in the named query), status='A' only. */
export interface VitalCategoryOption {
  vitalCategoryId: number;
  vitalCategoryDesc: string;
}

/** Row shape returned by getVitalQualifireMasterList (ctrl:62-100). Matches
 * VitalQualifireMasterDto's real field names -- confirmed live 2026-07-28. */
export interface VitalQualifierRow {
  vitalQualifireId: number;
  vitalCategoryId: number;
  vitalCategoryDesc: string;
  vitalQualifireCode: string;
  vitalQualifireDesc: string;
  status: VitalQualifierStatus;
}

/** Mirrors $scope.common (ctrl:35-42/149-207/245-295). */
export interface VitalQualifierForm {
  vitalQualifireId?: number;
  vitalCategoryId: string;
  vitalQualifireCode: string;
  vitalQualifireDesc: string;
}

export function emptyVitalQualifierForm(): VitalQualifierForm {
  return {
    vitalQualifireId: undefined,
    vitalCategoryId: '',
    vitalQualifireCode: '',
    vitalQualifireDesc: '',
  };
}
