/**
 * Intended row shape of POST /adt/getListOfDenominations — the
 * DenominationMasterDto projection (billing/cashDesk/dto/
 * DenominationMasterDto.java) fed by the interim-handover DAO via
 * alias-to-bean. Legacy controller maps each row to {denominationId,
 * denomination, qty: 0, amount: 0} for its qty-entry table
 * (interimHandoverController.js:91-101).
 *
 * LIVE-VERIFIED 2026-08-12: the endpoint returns the SAVE envelope
 * {"status":"success","listObject":[],"message":"Added Successfully."} — 0
 * rows; it is a POST save, NOT a list. Screen 598 is classified write-path
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
