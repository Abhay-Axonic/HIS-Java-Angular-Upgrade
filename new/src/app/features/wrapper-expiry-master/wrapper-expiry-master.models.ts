export type WrapperStatus = 'A' | 'I';

/**
 * Row shape returned by GET /cssd/getWrapperExpiryList?organizationId&unitId&
 * offset&noOfRecordsPerPage (legacy wrapperExpiryMasterController.js URI;
 * GETWRAPPEREXPIRYMASTERLIST = CSSD+"/getWrapperExpiryList" from
 * cssd.url.config.js:29). Confirmed live 2026-08-12: SUCCESS with 1 real row
 * — code WRPTEST01, name "Test Wrapper", duration 30, status 'I'.
 * NOTE (DQ-2026-08-12-018): the offset/noOfRecordsPerPage query params are
 * REQUIRED — without them the endpoint 500s with the correlation envelope
 * (wrong-payload artifact, not masked).
 */
export interface WrapperExpiryRow {
  id: number;
  code: string;
  name: string;
  duration: number;
  status: WrapperStatus;
}
