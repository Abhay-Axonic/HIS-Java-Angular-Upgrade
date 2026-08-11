export type BloodTestStatus = 'A' | 'I';

/**
 * Row shape returned by POST /global/bloodBank/getBloodTestMasterList
 * (legacy bloodTestMasterController.js:88-102, bloodBank.url.config.js:40).
 * Live-verified 2026-08-12: SUCCESS with an empty listObject — honest
 * dev-empty, the grid renders the @empty "No records." state.
 */
export interface BloodTestRow {
  bloodTestId: number;
  bloodTestCode: string;
  bloodTestDesc: string;
  status: BloodTestStatus;
}
