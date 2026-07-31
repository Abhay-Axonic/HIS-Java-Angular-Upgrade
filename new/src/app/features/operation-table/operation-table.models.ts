export interface TableTypeOption {
  operationTableTypeId: number;
  tableName: string;
}

export interface TheatreOption {
  operationTheatreId: number;
  theatreName: string;
}

/** Row shape returned by getOperationTableList -- confirmed live (2026-07-31). */
export interface OperationTableRow {
  operationTableId: number;
  tableCode: string;
  tableName: string;
  tableDesc: string;
  tableTypeId: number;
  tableTypeName?: string;
  operationTheatreId: number;
  theatreName?: string;
  status: 'A' | 'I';
}

/** Editable form state -- mirrors legacy's `$scope.common`. */
export interface OperationTableRecord {
  operationTableId?: number;
  tableCode: string;
  tableName: string;
  tableDesc: string;
  tableTypeId: number | '';
  operationTheatreId: number | '';
}

export function emptyOperationTable(): OperationTableRecord {
  return {
    operationTableId: undefined,
    tableCode: '',
    tableName: '',
    tableDesc: '',
    tableTypeId: '',
    operationTheatreId: '',
  };
}
