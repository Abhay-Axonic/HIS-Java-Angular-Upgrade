/**
 * Field set taken directly from `InstructionMasterDto.java`/
 * `TranslatedInstructionMasterDto.java`
 * (legacy/Global/src/main/java/com/param/global/dto/global/) and
 * resources/assets/js/controller/master/global/common/inspectionMasterController.js.
 *
 * **Naming quirk, confirmed deliberate, not a shadow-state trap (re-verified
 * this batch, see class comment in instruction-master.ts):** the legacy
 * state is `instructionMaster` (url `/instructionMaster`, the ONLY
 * registration of this state name in app.js), but its `controller:` field
 * is literally `"inspectionMasterController"` -- the real JS file is
 * `inspectionMasterController.js`, not anything named "instruction". Real,
 * deliberate (if oddly-named) wiring.
 */

/** One row of the main table -- `GET_INSPECTION_MASTER_LIST`'s real column
 * aliases (Entity/.../InstructionMaster.java:15-23). List query filters
 * ONLY by `organizationId` (no `unitId` filter) -- org-wide, not
 * unit-scoped, same preserved quirk as `systemObservationMaster`'s list. */
export interface InstructionMasterRow {
  instructionId: number;
  instructionCode: string;
  instructionDesc: string;
  isMedication: 'Y' | 'N';
  status: 'A' | 'I';
}

/** Department/Speciality multiselect + "Department Details" popup option.
 * `getDeptSpecilityById`'s real column aliases
 * (InstructionMasterDaoImpl.java:279-289): `id`/`specialityName`/
 * `instuctionDeptSpecialityMapperId`. */
export interface DeptSpecialityDetail {
  id: number;
  specialityName: string;
  instuctionDeptSpecialityMapperId?: number;
}

/** Department dropdown master-data option -- mapped from
 * `getSpecialityList`'s `specialityId`/`specialityName` the same way the
 * legacy controller does (ctrl:508-521, `getSpecialityMasterList()`).
 * UNLIKE `systemObservationMaster`'s sibling multiselect, this screen does
 * NOT filter by `isClinicalSpeciality` at all -- every active speciality
 * shows, confirmed by reading ctrl:496-528 in full (no such filter exists
 * here). A real, confirmed status/filter-vocabulary drift between the two
 * "Admin - Global Master (general)" siblings audited this batch -- not
 * normalized, preserved as-is per screen. */
export interface SpecialityOption {
  id: number;
  label: string;
}

/** Raw `getSpecialityList` row shape, before mapping to `SpecialityOption`. */
export interface RawSpecialityOption {
  specialityId: number;
  specialityName: string;
}

/** One row of the read-only "Customise Language" details popup
 * (`getConvertedLanguageListById`'s real aliases,
 * InstructionMasterDaoImpl.java:394-400: `desc`/`convertedText`). */
export interface TranslatedInstructionRow {
  desc: string;
  convertedText: string;
  languageId?: number;
}

/** Language dropdown option (`/LIS/global/listLanguageMaster/{orgId}/{unitId}`,
 * ctrl:629-641, `getLanguageMasterList()`). **Known risk, carried over from
 * docs/ANGULAR_PENDING_FINDINGS.md entry 114** (found on `frequencyMaster`,
 * Phase 4 batch 10, which calls this exact same shared endpoint): the
 * response returns HTTP 200 but modern Angular `HttpClient` has been
 * observed throwing a parse error on it where legacy's more lenient
 * AngularJS `$http` does not. Background-only (doesn't block list/save on
 * this screen either), not fixed here -- see the service's own doc
 * comment. */
export interface LanguageOption {
  languageId: number;
  desc: string;
}

/** One entry of `$scope.addedLanguageList` -- the in-progress "Customise
 * Language" draft list attached to save/update as `addedLanguageList`
 * (ctrl:198-267/347/460). */
export interface AddedLanguageEntry {
  desc: string;
  languageId: string;
  convertedText: string;
}

/** Mirrors `$scope.common` + the save/update payload shape (ctrl:335-348/445-460). */
export interface SaveInstructionPayload {
  instructionCode: string;
  instructionDesc: string;
  isMedication: 'Y' | 'N';
  instructionId?: number;
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  updatedDate: string;
  unitId: number;
  organizationId: number;
  status?: 'A';
  deptSpecilityId: Array<{ id: number }>;
  addedLanguageList: AddedLanguageEntry[];
}

export function emptyInstructionForm() {
  return {
    instructionId: undefined as number | undefined,
    instructionCode: '',
    instructionDesc: '',
    isMedication: 'N' as 'Y' | 'N',
  };
}
