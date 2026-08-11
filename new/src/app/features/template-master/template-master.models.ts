export type TemplateStatus = 'A' | 'I';

/**
 * Row shape returned by GET /LIS/unit/labtemplates/{org}/{unit}/{offset}/{size}
 * (legacy labTemplateMasterController.js:155-159; LIST_LAB_TEMPLATE_MASTER =
 * "/labtemplates" from lis.url.config.js:521). NOTE the RESPONSE SHAPE: the
 * endpoint returns a RAW ARRAY ([] when empty), not the {listObject} envelope
 * — the legacy reads response.data directly (ctrl:157). DQ-2026-08-12-020.
 * Confirmed live 2026-08-12: raw [] (honest empty).
 */
export interface LabTemplateRow {
  labTemplateId: number;
  templateName: string;
  templateCode: string;
  genderId: number;
  status: TemplateStatus;
}
