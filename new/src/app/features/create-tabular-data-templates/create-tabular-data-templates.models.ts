export interface CtdtComponent {
  tabularTemplateId: number;
  tabularTemplateDesc: string;
}

export interface CtdtRowCol {
  rowId: number;
  coulmnId: number;
  rowName: string;
  columnName: string;
  inputbox: boolean;
  tabularTemplateId?: number;
}

export interface CtdtSavedEntry {
  inputValue: string;
  coulmnId: number;
  tableName?: string;
  columnName: string;
  rowId: number;
  rowName: string;
  tabularTemplateId?: number;
  numberOfRow?: number;
  numberOfCoulmn?: number;
  organizationId?: number;
  unitId?: number;
  createdBy?: number;
  updatedBy?: number;
  status?: string;
  createdDate?: string;
  updatedDate?: string;
  tabularTemplateDataMapperId?: number;
}
