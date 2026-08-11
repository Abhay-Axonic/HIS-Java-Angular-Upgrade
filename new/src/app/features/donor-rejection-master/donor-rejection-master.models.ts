export type DonorRejectionStatus = 'A' | 'I';

/**
 * Row shape returned by POST /bloodBank/getDonorRejectionMasterList
 * (legacy donorRejectionMasterController.js:87-107, bloodBank.url.config.js:80).
 * Live-verified 2026-08-12: SUCCESS with an empty listObject — honest
 * dev-empty, the grid renders the @empty "No records." state.
 */
export interface DonorRejectionRow {
  donorRejectionId: number;
  donorRejectionCode: string;
  donorRejectionDesc: string;
  status: DonorRejectionStatus;
}
