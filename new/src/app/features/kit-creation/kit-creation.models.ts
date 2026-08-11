export type KitStatus = 'A' | 'I';

/**
 * Row shape returned by GET /cssd/getAllKitsList?organizationId&unitId&
 * offset&noOfRecordsPerPage (legacy kitCreationController.js URI;
 * GETALLKIT = CSSD+"/getAllKitsList" from cssd.url.config.js:35).
 * Confirmed live 2026-08-12: SUCCESS empty (honest dev-empty).
 * NOTE (DQ-2026-08-12-018): the offset/noOfRecordsPerPage query params are
 * REQUIRED — without them the endpoint 500s with the correlation envelope
 * (wrong-payload artifact, not masked).
 */
export interface KitRow {
  kitId: number;
  kitCode: string;
  kitName: string;
  status: KitStatus;
}
