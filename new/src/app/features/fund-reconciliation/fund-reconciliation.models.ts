export interface FundInflowRow {
  fundInFlowId: number;
  trustId: number;
  trustName: string;
  totalCollectedAmount: number;
  fundtypeId: number;
  availableAmount: number;
  billConsumeAmount: number;
}

/**
 * POST body for /api/billing/getAllfundInflowDetailsByTrust
 * (legacy fundInflowController.js:416-420 — the exact object built for the
 * call: { trustId, orgId, unitId }). trustId 0 = no trust selected yet
 * (legacy passes the selected trust's id from its dropdown).
 */
export interface FundInflowListPayload {
  orgId: number;
  unitId: number;
  trustId: number;
}
