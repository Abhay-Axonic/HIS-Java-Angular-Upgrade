export type ConfigStatus = 'A' | 'I';

/**
 * Row shape returned by POST /api/unit/getLoyaltyCardConfigurationList
 * (legacy loyaltyCardConfigurationMasterController.js initMasterList;
 * GET_LOYALTY_CARD_CONFIGURATION_LIST = "/api/unit/getLoyaltyCardConfigurationList"
 * from global.url.config.js:230). Confirmed live 2026-08-12: SUCCESS with 1
 * real row — cardConfigurationId 1, minStartAmount 1000.0, thresholdAmount
 * 5000.0, minRechargeAmount 1000.0, status 'A'.
 */
export interface LoyaltyCardConfigRow {
  cardConfigurationId: number;
  minStartAmount: number;
  thresholdAmount: number;
  minRechargeAmount: number;
  status: ConfigStatus;
}
