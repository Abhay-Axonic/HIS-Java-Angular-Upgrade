export type FormulaStatus = 'A' | 'I';

/**
 * Row shape returned by GET /LIS/unit/formula/{orgId}/{unitId}/{offset}/{size}
 * (legacy parameterFormulaController.js:176-177 list URI1). Projection of
 * FormulaMasterDto via the GET_FORMULA_MASTER_LIST query
 * (FormulaMasterDaoImpl.java:231-235).
 *
 * Dev-empty: the DAO answers status 'error' with the plain message
 * "Formula Records Not Found." when the dev DB has no rows
 * (FORMULA_RECORDS_NOT_FOUND, ICommonConstants.java:856, returned at
 * FormulaMasterDaoImpl.java:237-240) -- the component surfaces that message
 * verbatim instead of masking it behind a generic banner.
 */
export interface FormulaRow {
  formulaId: number;
  parameterId: number;
  paramDesc: string;
  formula: string;
  formulaLatex: string;
  status: FormulaStatus;
}
