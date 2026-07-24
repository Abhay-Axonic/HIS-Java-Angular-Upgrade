export type CurrencyStatus = 'A' | 'I';

/**
 * Row shape returned by `getAllCurrencyMasterList` (GET_ALL_CURRENCY_MASTER_LIST
 * named query, CurrencyMaster.java:55-64, joined to CountryMaster) -- the
 * fields the legacy view actually renders (currency-master.html:211-226).
 * NOTE: this list query does NOT select `countryId` at all -- only
 * `getCurrencyMasterById` (GET_CURRENCY_MASTER_BY_ID, CurrencyMaster.java:28-39)
 * does. That's why `edit()` below always does a fresh GET instead of reusing
 * the already-loaded row, matching legacy's `showUpdateBtn` (ctrl:296-324)
 * exactly.
 */
export interface CurrencyRow {
  currencyId: number;
  currencyName: string;
  currencyCode: string;
  countryName: string;
  countryCode: string;
  status: CurrencyStatus;
}

/** Row shape returned by `getCurrencyMasterById` -- includes `countryId`,
 * unlike CurrencyRow above (see its doc comment). */
export interface CurrencyByIdRow extends CurrencyRow {
  countryId: number;
}

export interface CountryOption {
  countryId: number;
  countryName: string;
}

/** Editable form state -- mirrors `$scope.currencyMaster` (ctrl:50, 241-251,
 * 313-318, 373-381). */
export interface CurrencyForm {
  currencyId?: number;
  countryId: number | '';
  countryCode: string;
  currencyCode: string;
  currencyName: string;
}

export function emptyCurrencyForm(): CurrencyForm {
  return { currencyId: undefined, countryId: '', countryCode: '', currencyCode: '', currencyName: '' };
}

/**
 * POST body for `saveCurrencyMaster` (ctrl:234-291, data built at ctrl:240-251).
 *
 * `currencySymbol` is always sent as a literal empty string by legacy
 * (ctrl:243, `currencySymbol : ""`) -- and, further, `CurrencyMasterDaoImpl
 * .saveCurrencyMaster` (legacy/Global/.../dao/CurrencyMasterDaoImpl.java:37-62)
 * never calls `setCurrencySymbol` on the entity at all, so this field is
 * never actually persisted either way. The view's "Symbol" file-upload
 * input (currency-master.html:86-91) is likewise never read by the
 * controller and never included in this payload. Kept here only because
 * it's part of the real wire contract, not because it does anything --
 * this whole Symbol concept is dead end-to-end on this screen.
 *
 * `unitId` is NOT part of legacy's save payload at all (ctrl:240-251 has no
 * `unitId` key), even though both `CurrencyMasterDto` and the `CurrencyMaster`
 * entity have a `unitId` column -- so `unit_id` is always persisted as NULL
 * for every currency created via this screen. Not modeled here; matches
 * legacy's real, dead field.
 */
export interface CurrencySavePayload {
  currencyName: string;
  countryId: number;
  currencySymbol: '';
  currencyCode: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  orgnisationId: number;
  createdDate: string;
  updatedDate: string;
}

/**
 * POST body for `updateCurrencyMaster` (ctrl:368-418, data built at
 * ctrl:373-381).
 *
 * *** See docs/DECISION_NEEDED_CurrencyMasterUpdateCountryIdDropped.md ***:
 * `countryId` IS part of this real wire payload (ctrl:377) and the Country
 * dropdown IS editable while updating (same `<select>` as create,
 * currency-master.html:17-23) -- but `CurrencyMasterDaoImpl
 * .updateCurrencyMaster` (legacy/Global/.../dao/CurrencyMasterDaoImpl.java:
 * 120-145) never calls `setCountryId` on the fetched entity before saving,
 * so changing the Country while updating has NO effect on the persisted
 * row -- silently. `countryId` is sent here anyway because that's the real,
 * confirmed wire contract (what a byte-for-byte capture of the real legacy
 * request shows), not a rebuild invention -- and this is disclosed here and
 * in the linked decision doc rather than silently worked around.
 */
export interface CurrencyUpdatePayload {
  currencyName: string;
  currencyCode: string;
  currencyId: number;
  countryId: number;
  updatedBy: number;
  updatedDate: string;
  orgnisationId: number;
}

/** POST body for `updateCurrencyMasterStatus` (ctrl:327-339). Deliberately
 * has NO `orgnisationId` field -- legacy's own `data` object here
 * (ctrl:334-339) never includes one, unlike every other call on this
 * screen. */
export interface CurrencyStatusPayload {
  currencyId: number;
  status: CurrencyStatus;
  updatedBy: number;
  updatedDate: string;
}
