/**
 * Field names/types read directly from the Java DTOs, not guessed:
 * `GetFrequencyResponse`/`GetFrequencyListResponse`
 * (legacy/Pharmacy/src/main/java/com/param/pharmacy/master/global/dto/frequency/)
 * for the list (GetAll) shape, `FrequencyDetailsResponse`/`FrequencyRequestItem`
 * for the by-id (edit) shape -- these two endpoints return the SAME
 * underlying per-item time fields under genuinely DIFFERENT names
 * (`administrationTime` on the list response vs. `administrationTimeString`
 * on the by-id response) -- confirmed via direct source read of both DTOs,
 * not a typo to "fix"; modeled as two separate interfaces below.
 *
 * `status` is a real `Boolean` on the wire (`GetFrequencyResponse.status`),
 * NOT the `'A'|'I'` string vocabulary this codebase's other master screens
 * (e.g. `role-creation`, built in this same batch) use -- a genuine
 * status-vocabulary drift between two screens built in the same batch,
 * confirmed from each screen's own real DTO, not assumed to match.
 */

/** Per-item admin-time shape as returned inside `GetFrequencyResponse.items`
 * (the LIST/GetAll response, and therefore what the "View Times" modal
 * displays) -- ctrl:76-82, view:346-350. */
export interface FrequencyListTimeItem {
  id?: number;
  administrationTime: string;
  preBufferAdministrationTime: string;
  postBufferAdministrationTime: string;
}

export interface FrequencyRow {
  id: number;
  code: string;
  description: string;
  noOfAdministrationTime: number;
  status: boolean;
  items?: FrequencyListTimeItem[];
}

/** Per-item admin-time shape as returned inside
 * `FrequencyDetailsResponse.items` (the GET-BY-ID/edit response) --
 * ctrl:286-303. Note the `...String` suffix, absent from the list shape
 * above -- both are real, both are used, per their own endpoint. */
export interface FrequencyEditTimeItem {
  id?: number;
  administrationTimeString: string;
  preBufferAdministrationTimeString: string;
  postBufferAdministrationTimeString: string;
}

/** A single manually-entered per-language translation of the Frequency
 * Description -- built by the shared "Customize Language" partial
 * (views/OPD/CoverSheet/customise-language.html, reused verbatim by this
 * screen) and round-tripped as `addedLanguageList` on save/update/edit.
 * Field names match `TranslatedFrequencyMasterDto` or, on edit, whatever
 * `FrequencyDetailsResponse.addedLanguageList` returns (same shape). */
export interface TranslatedLanguageEntry {
  languageId: number | string;
  desc: string;
  convertedText: string;
}

/** `LanguageMasterDto` shape (legacy/LIS), as returned by
 * `/LIS/global/listLanguageMaster/{orgId}/{unitId}` -- ctrl `getLanguageMasterList()`. */
export interface LanguageMasterRow {
  languageId: number;
  desc: string;
  code?: string;
}

/** GET-by-id response shape (`FrequencyDetailsResponse`, extends
 * `FrequencyResponse`) -- ctrl:262-312 (`populateFrequencyMaster`). Unlike
 * most other rebuilt screens' by-id endpoints, this one is NOT wrapped in
 * `{status, object}` -- `response.data` IS the detail object directly
 * (confirmed: `FrequencyController.GetById` returns
 * `ResponseEntity<FrequencyDetailsResponse>`, no envelope). Modeled via a
 * plain `Observable<FrequencyDetail>` in the service, not `ApiResponse`. */
export interface FrequencyDetail {
  id: number;
  code: string;
  description: string;
  noOfAdministrationTime: number;
  status: boolean;
  items?: FrequencyEditTimeItem[];
  addedLanguageList?: TranslatedLanguageEntry[];
}

/** A single clockpicker row in the "Add Administration Time" modal
 * (`$scope.clockList`, ctrl:85-149) -- `adminTime`/`preAdminTime`/`postAdminTime`
 * strings in `HH:mm` form, plus the two never-actually-used validation flag
 * names (`isAdminTime`/`isPreAdminTime`/`isPostAdminTime`) legacy uses to
 * drive the `borderRed` CSS class on a failed row. */
export interface ClockRow {
  id?: number;
  adminTime: string;
  preAdminTime: string;
  postAdminTime: string;
  isAdminTime?: boolean;
  isPreAdminTime?: boolean;
  isPostAdminTime?: boolean;
}

/** The editable top-of-page form (`self.model.*` scalar fields, NOT
 * `self.model.pagination`/list state). */
export interface FrequencyForm {
  id?: number;
  frequencyCode: string;
  frequencyDescription: string;
}

export function emptyFrequencyForm(): FrequencyForm {
  return { id: undefined, frequencyCode: '', frequencyDescription: '' };
}

/** Default single clock row, matching `ViewTimeDetails()`'s always-midnight
 * seed (`moment(36000000).utc().format('HH:mm')` == `"10:00"`, ctrl:84-96). */
export function defaultClockRow(): ClockRow {
  return { adminTime: '10:00', preAdminTime: '10:00', postAdminTime: '10:00' };
}

/** POST body for /api/globals/frequency/save and PATCH body for
 * /api/globals/frequency/{orgId}/{id} (ctrl:387-451) -- same shape for both
 * (update simply omits nothing; legacy sends the identical field set to
 * both endpoints). */
export interface FrequencySavePayload {
  code: string;
  description: string;
  noOfAdministrationTime: number;
  items: Array<{ id?: number; administrationTime: string; preBufferAdministrationTime: string; postBufferAdministrationTime: string }>;
  unitId: number;
  organizationId: number;
  userId?: number;
  addedLanguageList: TranslatedLanguageEntry[];
}

/** PATCH body for the status-only partial update (ctrl:315-335) -- just the
 * one field; the Java side falls back to the existing entity's own values
 * for anything else omitted (`FrequencyService.updateFrequency`, `request.getX() != null ? ... : frequency.getX()`). */
export interface FrequencyStatusPayload {
  status: boolean;
}
