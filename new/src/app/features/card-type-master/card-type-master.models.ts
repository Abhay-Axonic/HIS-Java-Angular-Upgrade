export type CardTypeStatus = 'A' | 'I';

/** Row shape returned by `getCardTypeMasterList` (ctrl:70-86) -- the only
 * fields the legacy view actually renders (card-type-master.html:76-94). */
export interface CardTypeRow {
  cardTypeId: number;
  cardTypeCode: string;
  cardTypeName: string;
  status: CardTypeStatus;
}

/** Editable form state -- mirrors `$scope.cardType` (ctrl:60, 168, 227).
 * A `pinpadScheme` DTO field exists on the backend but is confirmed
 * dead/unused by THIS screen (belongs to an unrelated endpoint) -- not
 * modeled here. */
export interface CardTypeForm {
  cardTypeId?: number;
  cardTypeCode: string;
  cardTypeName: string;
}

export function emptyCardTypeForm(): CardTypeForm {
  return { cardTypeId: undefined, cardTypeCode: '', cardTypeName: '' };
}

/** POST body for `saveCardTypeMaster` (ctrl:100-160). Field names --
 * including `orgnisationId` -- are legacy's own literal spelling (ctrl:112),
 * kept verbatim since this is the real backend contract, not a local typo
 * to "fix" (same spelling this codebase's bank-master already uses
 * verbatim for the identical reason). `status` is ALWAYS `'A'` here -- see
 * card-type-master.ts's class doc comment, there is no way to create a
 * card type as inactive. */
export interface CardTypeSavePayload {
  cardTypeCode: string;
  cardTypeName: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  orgnisationId: number;
  createdDate: string;
  updatedDate: string;
}

/** POST body for `updateCardTypeMaster` (ctrl:219-269) -- no `status`,
 * matching legacy exactly (update never touches status). */
export interface CardTypeUpdatePayload {
  cardTypeCode: string;
  cardTypeName: string;
  cardTypeId: number;
  updatedBy: number;
  updatedDate: string;
  orgnisationId: number;
}

/** POST body for `updateStatusForCardType` (ctrl:175-216). */
export interface CardTypeStatusPayload {
  cardTypeId: number;
  status: CardTypeStatus;
  updatedBy: number;
  updatedDate: string;
  orgnisationId: number;
}
