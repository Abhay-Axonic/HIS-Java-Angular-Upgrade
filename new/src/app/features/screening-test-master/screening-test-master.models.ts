export type ScreeningTestStatus = 'A' | 'I';

/**
 * Row shape returned by POST /bloodBank/getScreeningTestMasterList
 * (legacy screeningTestMasterController.js:100-115, bloodBank.url.config.js:70).
 * Live-verified 2026-08-12: SUCCESS with an empty listObject — honest
 * dev-empty, the grid renders the @empty "No records." state.
 */
export interface ScreeningTestRow {
  screeningTestId: number;
  screeningTestCode: string;
  screeningTestDesc: string;
  status: ScreeningTestStatus;
}
