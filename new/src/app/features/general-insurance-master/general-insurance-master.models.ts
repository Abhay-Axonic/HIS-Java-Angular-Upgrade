/**
 * Parity rebuild of
 * resources/assets/js/controller/master/AssetManagenent/generalInsuranceMasterController.js
 * + views/master/GlobalMaster/AssetManagenent/Master/general-insurance-master.html.
 *
 * IMPORTANT, real backend-response shape note: unlike most already-built
 * screens (which read the shared `{status, message, object, listObject}`
 * envelope -- see core/api-response.ts), every endpoint this screen calls
 * returns its body RAW -- a plain array or a plain object, with no
 * envelope at all. Confirmed directly from the Java controllers:
 *  - GeneralInsuranceMasterController.GetAll()/GetById() return
 *    `ResponseEntity<List<...>>` / `ResponseEntity<...>` directly (no
 *    Response/CommonResponse wrapper).
 *  - VendorController.GetDropdowns()/getStates()/getCities()/getAreas()
 *    likewise return their DTOs/lists directly.
 *  - Only Save/Update return a `CommonResponse` (id + message), still not
 *    the `{status, object, listObject}` shape other screens use.
 * Do NOT reuse `ApiResponse<T>` here -- it would silently look correct
 * at compile time (all fields optional) while reading the wrong shape at
 * runtime.
 */

export interface GeoOption {
  countryId?: number;
  countryName?: string;
  stateId?: number;
  stateName?: string;
  cityId?: number;
  cityName?: string;
  areaId?: number;
  areaName?: string;
}

/** Legacy seeds each cascade list with one non-selectable-looking, but
 * actually real and selectable, placeholder entry (id-less) as index 0 --
 * see `self.model.Countries = [{countryName: "Select Country"}]` etc. in
 * the controller. Selecting it back sends id=0 to the next cascade level
 * exactly like selecting a real entry would (no guard skips the call) --
 * reproduced faithfully via `PLACEHOLDER_*` below rather than a native
 * empty-value `<option>`, since the two are NOT behaviorally identical
 * (see general-insurance-master.ts `onCountryChange` etc.). */
export const PLACEHOLDER_COUNTRY: GeoOption = { countryName: 'Select Country' };
export const PLACEHOLDER_STATE: GeoOption = { stateName: 'Select State' };
export const PLACEHOLDER_CITY: GeoOption = { cityName: 'Select City' };
export const PLACEHOLDER_AREA: GeoOption = { areaName: 'Select Area' };

/** Raw body of GET /api/unit/vendors/dropdown/{unitId}/{orgId}
 * (VendorDropdownRespose.java) -- only `country` is consumed here. */
export interface VendorDropdownResponse {
  country: GeoOption[];
  manufacturer?: unknown[];
  paymentMode?: unknown[];
  mobileDigit?: number;
}

/** Raw body of GET /api/unit/generalinsurance (list) and
 * GET /api/unit/generalinsurance/{id} (details) --
 * GenralInsuranceListResponse / GeneralInsuranceDetailsResponse, both
 * just `GenralInsuranceResponse` + `id`. */
export interface GeneralInsuranceMasterRecord {
  id: number;
  insuranceCompanyName: string;
  contactNo: string;
  email: string;
  contactPersonName: string;
  contactPersonNumber: string;
  address: string;
  zipCode: string;
  status: boolean;
  countryId?: number;
  countryName?: string;
  stateId?: number;
  stateName?: string;
  cityId?: number;
  cityName?: string;
  areaId?: number;
  areaName?: string;
}

/** Body of the create/edit form -- a subset of the record plus the
 * selected geo option objects (legacy's `self.model.countryName` etc.
 * are the whole selected list-item object, not just an id). */
export interface GeneralInsuranceFormState {
  id?: number;
  insuranceCompany: string;
  contactNo: string;
  emailAddress: string;
  contactPersonName: string;
  contactPersonNo: string;
  address: string;
  zipCode: string;
  status?: boolean;
  countryName: GeoOption;
  stateName: GeoOption;
  cityName: GeoOption;
  areaName: GeoOption;
}

export function emptyGeneralInsuranceForm(): GeneralInsuranceFormState {
  return {
    insuranceCompany: '',
    contactNo: '',
    emailAddress: '',
    contactPersonName: '',
    contactPersonNo: '',
    address: '',
    zipCode: '',
    countryName: PLACEHOLDER_COUNTRY,
    stateName: PLACEHOLDER_STATE,
    cityName: PLACEHOLDER_CITY,
    areaName: PLACEHOLDER_AREA,
  };
}

/** Per-field "is required" flags -- legacy's `isXValid` booleans, shown
 * as visible validation messages per this project's standing
 * validation-visibility policy (docs/ANGULAR_REBUILD_PLAYBOOK.md, added
 * 2026-07-22) rather than legacy's CSS-hidden `.help-block`. */
export interface GeneralInsuranceValidity {
  isInsuranceCompanyValid: boolean;
  isContactNoValid: boolean;
  isemailValid: boolean;
  iscontactPersonNameValid: boolean;
  isContactPersonNoValid: boolean;
  isaddressValid: boolean;
  iscountryNameValid: boolean;
  isstateNameValid: boolean;
  iscityNameValid: boolean;
  isareaNameValid: boolean;
  iszipCodeValid: boolean;
}

export function emptyValidity(): GeneralInsuranceValidity {
  return {
    isInsuranceCompanyValid: false,
    isContactNoValid: false,
    isemailValid: false,
    iscontactPersonNameValid: false,
    isContactPersonNoValid: false,
    isaddressValid: false,
    iscountryNameValid: false,
    isstateNameValid: false,
    iscityNameValid: false,
    isareaNameValid: false,
    iszipCodeValid: false,
  };
}

/** "Address Details" view-only modal -- `getAddressDetails(data)`. */
export interface AddressDetails {
  address: string;
  country: string;
  state: string;
  city: string;
  area: string;
  zipCode: string;
}

/** Body of POST .../save and PATCH .../{id} (create/update) --
 * `createGenInsMaster()`/`updateGenInsMaster()`'s `data` object. */
export interface SaveGeneralInsurancePayload {
  id?: number;
  insuranceCompanyName: string;
  contactNo: string;
  email: string;
  contactPersonName: string;
  contactPersonNumber: string;
  address: string;
  areaId?: number;
  zipCode: string;
  status?: boolean;
  unitId: number;
  organizationId: number;
  userId: number;
}

/** Body of the status-toggle PATCH to the SAME .../{id} endpoint as
 * update, deliberately a different, narrower shape (no id in the body --
 * `ToggleStatus(insMaster)`'s `data` object). */
export interface ToggleStatusPayload {
  status: boolean;
  unitId: number;
  organizationId: number;
  userId: number;
}

/** Raw body of POST save / PATCH update -- `CommonResponse` (id +
 * message only, no `status` field on this response -- do not confuse
 * with the record's own boolean `status`). */
export interface SaveResponse {
  id?: number;
  message?: string;
}
