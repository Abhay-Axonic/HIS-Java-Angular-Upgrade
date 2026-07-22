/**
 * QMS Logo (legacy state `Qmslogo`, url `/qmslogo`, self-registered --
 * legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller/master/global/qmslogo.js).
 *
 * NOT a master-data list/search/pagination screen. It's a pure
 * settings/upload form with 8 independent fields, each saved by its own
 * button, all posting `multipart/form-data` to the SAME single backend
 * endpoint (`POST /api/unit/applicationConfiguration/screen-images`) with a
 * different `imageTypeId` (1-8) per field. See
 * legacy/Global/src/main/java/com/param/global/controller/
 * UnitApplicationBillingConfigurationController.java:152-190 for the real
 * backend contract (`saveScreenImage`) confirming these exact param names.
 */

/**
 * The 8 legacy `imageTypeId` values, in the exact order qmslogo.js assigns
 * them (`$scope.imageTypeId = 1` through `= 8`, one per save function).
 * `createdBy`/`updatedBy` in legacy are hardcoded to the SAME number as
 * `imageTypeId` (not the logged-in user id) for every one of the 8 fields --
 * a real legacy quirk, faithfully ported rather than "fixed" to use the
 * actual session user id, since that would be an unlogged behavioral change.
 */
export type QmsLogoImageTypeId = 1 | 2 | 3 | 4 | 5 | 6 | 7 | 8;

/** File-field definitions -- imageTypeId 1 through 6. */
export interface QmsLogoFileFieldConfig {
  imageTypeId: QmsLogoImageTypeId;
  key: 'mainScreen' | 'afterLogin' | 'nursingTokenCabin' | 'kioskHeader' | 'kioskFooter' | 'loginPageImg';
  label: string;
  dimensionHint: string;
  missingFileMessage: string;
}

/** Text-field definitions -- imageTypeId 7 and 8, submitted as `logoName` (no `file` part). */
export interface QmsLogoTextFieldConfig {
  imageTypeId: QmsLogoImageTypeId;
  key: 'applicationName' | 'copyWrite';
  label: string;
  placeholder: string;
  missingValueMessage: string;
}

export const QMS_LOGO_FILE_FIELDS: readonly QmsLogoFileFieldConfig[] = [
  {
    imageTypeId: 1,
    key: 'mainScreen',
    label: 'QMS Main Screen Background',
    dimensionHint: '1280 × 800 px',
    missingFileMessage: 'Please Select QMS Main Screen Background Image',
  },
  {
    imageTypeId: 2,
    key: 'afterLogin',
    label: 'After loggin Main logo',
    dimensionHint: '2416 × 511 px',
    missingFileMessage: 'Please Select After loggin Main logo',
  },
  {
    imageTypeId: 3,
    key: 'nursingTokenCabin',
    label: 'Nursing Display / Cabin display / Token display / Triage display / Consultant display',
    dimensionHint: '14984 × 1178 px',
    missingFileMessage: 'Please Select Nursing,Cabin,Token,Triage,Consultant logo',
  },
  {
    imageTypeId: 4,
    key: 'kioskHeader',
    label: 'kiosk Header',
    dimensionHint: '1537 × 486 px',
    missingFileMessage: 'Please Select kiosk Header Image',
  },
  {
    imageTypeId: 5,
    key: 'kioskFooter',
    label: 'kiosk Footer',
    dimensionHint: '2416 × 511 px',
    missingFileMessage: 'Please Select kiosk Footer Image',
  },
  {
    imageTypeId: 6,
    key: 'loginPageImg',
    label: 'Powered by login page',
    dimensionHint: '223 × 79 px',
    missingFileMessage: 'Please Select Powered by login page Image',
  },
];

export const QMS_LOGO_TEXT_FIELDS: readonly QmsLogoTextFieldConfig[] = [
  {
    imageTypeId: 7,
    key: 'applicationName',
    label: 'Application Name',
    placeholder: 'Enter Application Name',
    missingValueMessage: 'Please Enter Application Name',
  },
  {
    imageTypeId: 8,
    key: 'copyWrite',
    label: 'Copy Write Name',
    placeholder: 'Enter Copy Write Name',
    missingValueMessage: 'Please Enter Copy Write Name',
  },
];

/**
 * Client-side validation config for the 6 file inputs, read directly from
 * the shared `validfile` directive
 * (resources/assets/js/service/fileUploadDirective.js:17-48) that decorates
 * every `<input type="file">` on this screen (`fileinput`/`filepreview`
 * attrs + bare `validfile`).
 *
 * `validfile` is registered TWICE under the same directive name in that
 * file -- once with a 5MB limit, once with a 25MB limit. AngularJS merges
 * same-named directives into one array and runs every matching link
 * function's own `change` handler on the same `<input>`. The 5MB handler
 * runs first: on an oversize file it calls `elem.val('')`, clearing the
 * input's FileList, THEN sets `scope.fileError`. The 25MB handler's own
 * `change` handler then also fires on the same event, but by that point
 * `elem[0].files` is already empty (cleared by the first handler), so its
 * `angular.forEach` loop iterates zero files and never overwrites
 * `fileError` or re-validates anything. Net effect: the 25MB limit is
 * dead code in practice -- 5MB is the real, effective limit. This rebuild
 * enforces 5MB directly rather than porting the two-directive race.
 */
export const QMS_LOGO_MAX_FILE_SIZE_BYTES = 5 * 1024 * 1024;

/**
 * Accepted extensions, read verbatim from `validFormats` in
 * fileUploadDirective.js (both `validfile` registrations use the identical
 * list). Deliberately not image-only -- `txt`/`pdf`/`docx`/`doc`/`csv` are
 * accepted by legacy's real directive config even though every field on
 * this screen is conceptually an "image" upload; the view's own
 * `accept="image/*"` attribute is a separate, weaker, non-authoritative
 * hint (the native file picker's filter, easily bypassed by picking "All
 * Files") -- `validfile`'s extension check is the actual enforcement layer.
 */
export const QMS_LOGO_ACCEPTED_EXTENSIONS = ['jpg', 'txt', 'pdf', 'docx', 'doc', 'jpeg', 'png', 'gif', 'csv'] as const;

export const QMS_LOGO_INVALID_FORMAT_MESSAGE =
  `Invalid file format. Allowed file formats are : ${QMS_LOGO_ACCEPTED_EXTENSIONS.join(', ')}.`;

export const QMS_LOGO_TOO_LARGE_MESSAGE = 'File is too large. Upload a file less than 5MB.';
