/**
 * Field set taken directly from
 * views/master/GlobalMaster/country-master.html +
 * resources/assets/js/controller/master/countryMasterController.js.
 * This is the root/simplest screen of the country -> state -> district ->
 * city -> area geography cascade family -- no cascading dropdowns live on
 * this screen itself, just the 5 plain fields below.
 */
export interface CountryRow {
  countryId: number;
  countryCode: string;
  countryName: string;
  countryInitial: string;
  countryCallingCode: string;
  contactDigit: string;
  status: 'A' | 'I';
}

/** Shape of GETSINGLECOUNTRY's `listObject[0]` -- only the fields
 * `showUpdateBtn` (ctrl:290-296) actually reads back into the form. */
export interface CountryDetail {
  countryId: number;
  countryCode: string;
  countryName: string;
  countryInitial: string;
  countryCallingCode: string;
  contactDigit: string;
}

/** Mirrors `saveCountry`'s `data` object (ctrl:201-213). */
export interface CountrySavePayload {
  countryCode: string;
  countryName: string;
  countryInitial: string;
  countryCallingCode: string;
  contactDigit: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** Mirrors `updateCountry`'s `data` object (ctrl:352-362) -- note no
 * `status` field is sent on update; status only changes via the separate
 * `updateStatus` endpoint. */
export interface CountryUpdatePayload {
  countryCode: string;
  countryName: string;
  countryInitial: string;
  countryCallingCode: string;
  countryId: number;
  updatedBy: number;
  updatedDate: string;
  contactDigit: string;
  organizationId: number;
}

/** Form/edit-buffer shape (mirrors `$scope.country`). `countryId` is only
 * present while editing an existing row. */
export interface CountryForm {
  countryId?: number;
  countryCode: string;
  countryName: string;
  countryInitial: string;
  countryCallingCode: string;
  contactDigit: string;
}

export function emptyCountryForm(): CountryForm {
  return {
    countryCode: '',
    countryName: '',
    countryInitial: '',
    countryCallingCode: '',
    contactDigit: '',
  };
}
