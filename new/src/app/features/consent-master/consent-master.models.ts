export type ConsentStatus = 'A' | 'I';

/**
 * Row shape AS RETURNED by POST /adt/getConsentMasterList. NOTE
 * (DQ-2026-08-12-023): the endpoint currently returns the BLANK generic
 * envelope (HTTP 200, status=error, code/message null) — NOT dev-empty. The
 * GET_CONSENT_LIST named query (ConsentMaster.java:29) selects
 * c.formTemplateId but adt.m_consent_master has NO form_template column
 * (THIRD phantom column; verified information_schema 2026-08-12). The
 * exception is TRIPLE-swallowed: DAO (ConsentMasterDaoImpl:205-210),
 * service (ConsentMasterServiceImpl:224-227) and controller
 * (ConsentMasterController:60-63) each catch and return a blank ERROR. The
 * shape below is the intended projection from the named query.
 */
export interface ConsentRow {
  consentMasterId: number;
  consentTypeMasterId: number;
  consentMasterName: string;
  consentMasterCode: string;
  filePath: string;
  templateType: string;
  consentTypeMasterName: string;
  status: ConsentStatus;
}
