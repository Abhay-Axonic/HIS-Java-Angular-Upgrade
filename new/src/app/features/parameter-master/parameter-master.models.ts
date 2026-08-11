export type ParameterStatus = 'A' | 'I';

/**
 * Row shape as RETURNED BY the backend for GET /LIS/unit/listParameterMaster
 * {orgId}/{unitId}/{offset}/{size}. NOTE (DQ-2026-08-12-009): the endpoint
 * currently 500s with the correlation envelope "Something went wrong.
 * Reference: ERR-<uuid>" because the GET_PAGINATED_PARAMETER_MASTER_LIST
 * named query selects parameterMaster.techniqueId, a column that does NOT
 * exist in lab.m_parameter_master (phantom column, verified information_schema
 * 2026-08-12). The row shape below is the intended projection from
 * ParameterMaster.java:107-140 (the query's SELECT list).
 */
export interface ParameterRow {
  parameterId: number;
  parameterName: string;
  parameterCode: string;
  unitId: number;
  unitName: string;
  status: ParameterStatus;
  isMultyparameter: string;
  isFormula: string;
}
