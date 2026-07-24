export type CurrencyDenominationStatus = 'A' | 'I';
export type YesNo = 'Y' | 'N';

/** Dropdown option -- populated from a DIFFERENT controller/base path
 * (`billing/getCurrencyMasterList`, `CurrencyMasterController`, no `/api`
 * prefix) than every other call on this screen (see service file header
 * comment). Only `currencyId`/`currencyName` are read (ctrl:72-83,
 * view:19-20). */
export interface CurrencyOption {
  currencyId: number;
  currencyName: string;
}

/** Row shape returned by `getCurrencyDenominationMasterList`
 * (`GET_CURRENCY_DENOMINATIONMASTER_LIST` named query, joins
 * `t_currency_denomination_master` to `t_currency_master` for
 * `currencyName`) -- exactly the fields the legacy table renders
 * (view:206-222). */
export interface CurrencyDenominationRow {
  currencyDenominationId: number;
  currencyId: number;
  currencyName: string;
  denominationCode: string;
  denominationDesc: string;
  denominatonUnit: number;
  lowestDenomination: YesNo;
  status: CurrencyDenominationStatus;
}

/** Shape of `getCurrencyDenominationMasterById`'s `listObject[0]`
 * (`GET_CURRENCY_DENOMINATIONMASTER_BY_ID` named query) -- only the 6
 * fields `showUpdateBtn` actually reads back into the form (ctrl:290-298).
 * No `status`, no `currencyName` -- neither is read here even though the
 * named query for the LIST endpoint returns extra columns; this one
 * genuinely doesn't select them. */
export interface CurrencyDenominationDetail {
  currencyDenominationId: number;
  currencyId: number;
  denominationCode: string;
  denominationDesc: string;
  denominatonUnit: number;
  lowestDenomination: YesNo;
}

/** Editable form buffer (mirrors `$scope.currencyDenominationMasterObj`).
 * `currencyId` is `number | ''` (not a string) -- this rebuild uses a typed
 * `[value]` binding on the `<select>` (same established pattern as
 * city-master/state-master's cascade dropdowns), so there is no need to
 * reproduce legacy's own `.toString()` conversion in `showUpdateBtn`
 * (ctrl:297-298), which existed only because AngularJS's plain
 * `value="{{}}"` interpolated `<option>` forces every model value to a
 * string. `denominatonUnit` stays a plain `string` -- the legacy input is
 * `type="text"` with zero numeric parsing/validation (view:65-66) despite
 * the DTO/entity column being a `Float`; kept as the literal typed string,
 * not coerced to a JS number, to match the exact (already-working) wire
 * behaviour. */
export interface CurrencyDenominationForm {
  currencyDenominationId?: number;
  currencyId: number | '';
  denominationCode: string;
  denominationDesc: string;
  denominatonUnit: string;
  lowestDenomination: YesNo;
}

export function emptyCurrencyDenominationForm(): CurrencyDenominationForm {
  return {
    currencyId: '',
    denominationCode: '',
    denominationDesc: '',
    denominatonUnit: '',
    lowestDenomination: 'N',
  };
}

/** POST body for `saveCurrencyDenominationMaster` (ctrl:216-228). Field
 * names -- including `orgnisationId` -- are legacy's own literal spelling,
 * kept verbatim as the real wire contract (same spelling this codebase's
 * bank-master/card-type-master already use verbatim for the identical
 * reason). `status` is always `'A'` on create, matching legacy exactly (no
 * way to create a row as inactive from this screen). Note there is NO
 * `unitId` field sent here even though the DTO/entity both carry one --
 * legacy's own `data` object never includes it (ctrl:216-228), so the
 * server always persists `unit_id` as whatever Hibernate defaults an
 * unset `Integer` field to (null) on every save from this screen. */
export interface CurrencyDenominationSavePayload {
  currencyId: number;
  denominationCode: string;
  denominationDesc: string;
  denominatonUnit: string;
  lowestDenomination: YesNo;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  orgnisationId: number;
  createdDate: string;
  updatedDate: string;
}

/** POST body for `updateCurrencyDenominationMaster` (ctrl:353-363) -- no
 * `status` field, matching legacy exactly (update never touches status;
 * only the separate status-toggle endpoint does). */
export interface CurrencyDenominationUpdatePayload {
  currencyDenominationId: number;
  denominationDesc: string;
  denominatonUnit: string;
  lowestDenomination: YesNo;
  denominationCode: string;
  currencyId: number;
  updatedBy: number;
  updatedDate: string;
  orgnisationId: number;
}

/** POST body for `updateCurrencyDenominationMasterStatus` (ctrl:314-319).
 * Deliberately has NO `orgnisationId` field -- legacy's own `data` object
 * for this call really is just these 4 fields; the DAO's
 * `updateCurrencyDenominationMasterStatus` looks the row up by
 * `currencyDenominationId` alone (`findById`, no org scoping) so this
 * omission doesn't actually change which row gets updated, but it's
 * reproduced verbatim rather than "completed" with an org id nobody asked
 * for. */
export interface CurrencyDenominationStatusPayload {
  currencyDenominationId: number;
  status: CurrencyDenominationStatus;
  updatedBy: number;
  updatedDate: string;
}
