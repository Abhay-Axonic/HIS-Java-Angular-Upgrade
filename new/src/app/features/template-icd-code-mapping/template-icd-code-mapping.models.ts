export type TemplateIcdCodeMappingStatus = 'A' | 'I';

/**
 * Row shape returned by GET /api/global/getTemplateDiagnosisMapperlist/{offset}/{noOfRecordsPerPage}
 * (legacy templateICDCodeMappingMasterController.js:281-285, lis.url.config.js:287;
 * backend maps via aliasToBean(TemplateDiagnosisMapperDto) —
 * TemplateDiagnosisMapperDaoImpl.java:144-154). Projection is what the
 * legacy view reads (views/template/template-ICD-code-mapping.html:190-200 —
 * title cell, templateId passed to the details call).
 * Live-verified 2026-08-12: SUCCESS with an empty listObject — honest
 * dev-empty, the grid renders the @empty "No records." state.
 */
export interface TemplateIcdCodeMappingRow {
  templateDiagnosisMapperId: number;
  templateId: number;
  title: string;
  status: TemplateIcdCodeMappingStatus;
}
