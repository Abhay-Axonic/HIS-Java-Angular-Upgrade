/**
 * Row shape AS RETURNED by POST /api/global/clinical/getSiteMasterList
 * (legacy siteOfAdministartionController.js:107-109 [legacy filename typo],
 * lis.url.config.js:542). Confirmed live 2026-08-12: SUCCESS with 4 real
 * rows. Live row fields: siteMasterId/siteName/status — exposed here under
 * the generic id/name/status names.
 */
export interface SiteOfAdministrationRow {
  /** siteMasterId in the live response. */
  id: number;
  /** siteName in the live response. */
  name: string;
  status: string;
}
