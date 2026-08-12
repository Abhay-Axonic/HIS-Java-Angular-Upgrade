/**
 * Intended row shape of POST /api/global/getListOfDenominations — the
 * DenominationMasterDto projection (billing/cashDesk/dto/
 * DenominationMasterDto.java) fed by named query GET_DENOMINATION_MASTER_LIST
 * via alias-to-bean (CashHandoverMasterDaoImpl.java:46-48). Legacy controller
 * maps each row to {denominationId, denomination, qty: 0, amount: 0} for its
 * qty-entry table (cashOpeningBalanceController.js:99-108).
 *
 * LIVE-VERIFIED 2026-08-12: the endpoint returns the SAVE envelope
 * {"status":"success","listObject":[],"message":"Added Successfully."} — 0
 * rows; it is a POST save, NOT a list. Screen 597 is classified write-path
 * cash-entry form; the denomination feed is deferred to the transaction
 * methodology. Interface kept as the true DTO projection so a real feed
 * renders if the backend ever returns one.
 */
export interface DenominationRow {
  denominationId: number;
  countryId?: number;
  countryName?: string;
  denomination?: number;
  status?: string;
  currencyCode?: string;
  currencySymbol?: string;
  organizationId?: number;
  unitId?: number;
  createdDate?: string;
  updatedDate?: string;
}
