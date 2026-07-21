export interface ReportGroupHeader {
  reportGroupHeaderId: number;
  reportGroupHeaderName: string;
}

export interface ReportSummary {
  reportId: number;
  reportName: string;
  reportGroupHeaderId: number;
  action: string;
}

export interface ReportParameter {
  parameterName: string;
  paramType: string;
  controlName: string;
  lable: string;
  action: string;
  reportName: string;
  metaDataDetailsDtosList?: { id: number; name: string }[];
  // rebuild-side working state, mirrors the legacy controller mutating
  // these directly onto each parameter object
  paramValue?: string | number | boolean;
  fromDate?: Date | null;
  toDate?: Date | null;
  fromTime?: string;
  toTime?: string;
  expiryDate?: Date | null;
  multiSelectList?: { id: number; name: string }[];
}

export interface ReportFormatOption {
  id: number;
  description: string;
  format: string;
  fileExtension: string;
}
