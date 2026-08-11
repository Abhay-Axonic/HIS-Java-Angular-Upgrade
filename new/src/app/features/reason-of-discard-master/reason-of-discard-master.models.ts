export type ReasonOfDiscardStatus = 'A' | 'I';

/**
 * Row shape returned by POST /bloodBank/getReasonOfDiscardMasterList
 * (legacy reasonOfDiscardMasterController.js:87-107, bloodBank.url.config.js:60).
 * Live-verified 2026-08-12: SUCCESS with an empty listObject — honest
 * dev-empty, the grid renders the @empty "No records." state.
 */
export interface ReasonOfDiscardRow {
  reasonOfDiscardId: number;
  reasonOfDiscardCode: string;
  reasonOfDiscardDesc: string;
  status: ReasonOfDiscardStatus;
}
