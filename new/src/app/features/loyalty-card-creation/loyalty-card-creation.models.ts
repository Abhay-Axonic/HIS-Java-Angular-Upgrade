export type LoyaltyCardStatus = 'A' | 'I';

/**
 * Row shape returned by POST /api/unit/getLoyaltiCardMasterList
 * (legacy loyaltyCardCreationMasterController.js:68-79, global.url.config.js:236).
 * Projection is what the legacy view reads
 * (views/master/GlobalMaster/loyalty-card-creation.html:95-103 —
 * cardNo cell, loyaltyCardId passed to updateStatus/remove).
 * Live-verified 2026-08-12: SUCCESS with an empty listObject — honest
 * dev-empty, the grid renders the @empty "No records." state.
 */
export interface LoyaltyCardRow {
  loyaltyCardId: number;
  cardNo: string;
  status: LoyaltyCardStatus;
}
