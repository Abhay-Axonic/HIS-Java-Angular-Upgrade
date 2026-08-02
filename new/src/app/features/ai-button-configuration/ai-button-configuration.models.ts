/**
 * Real field shapes read directly from `ButtonMasterDto`/`DoctorMasterDto`
 * (legacy/Global/src/main/java/com/param/global/unit/dto/ButtonMasterDto.java,
 * legacy/Global/src/main/java/com/param/global/dto/DoctorMasterDto.java) and
 * `aiButtonConfugurationController.js` (note the legacy filename typo --
 * "Confuguration", missing the second "r" -- the state name and title are
 * spelled correctly, only the controller/JS filename has the typo). See
 * docs/ANGULAR_PENDING_FINDINGS.md (this batch) for the full audited quirk
 * list this shape needs to preserve.
 */

/** `/api/global/getDoctor` list item. */
export interface DoctorOption {
  doctorId: number;
  doctorName: string;
}

/**
 * `/api/global/getSpeciality` list item. Backend reuses `DoctorMasterDto`
 * for specialities too (`UserRoleMasterDaoImpl.getDoctorMasterByUnitIdForSpeciality`:
 * `msm.speciality_id as "doctorId", msm.speciality_name as "firstName"`) --
 * `doctorId` here actually holds the speciality id, `firstName` the
 * speciality name. Not a bug, just a confusing existing wire shape kept
 * verbatim for backend compatibility.
 */
export interface SpecialityOption {
  /** Actually the speciality id. */
  doctorId: number;
  /** Actually the speciality name. */
  firstName: string;
}

/**
 * `/api/global/getButtonMaster` list item -- the same endpoint (and same
 * DTO) the already-rebuilt `aIButtonMaster` screen uses for its own list.
 * Populates this screen's "Select Button" dropdown (unfiltered by status,
 * matching legacy -- `getButtonMaster`, not `getActiveButtonMaster`).
 */
export interface ButtonMasterOption {
  buttonId: number;
  label: string;
  systemPrompt: string;
  userPrompt: string;
  tabId: number;
  /** Raw JSON string: `{ urlObject: { URL }, aiPayload: {...} }`. */
  jsonObject: string;
  /** Legacy DTO field is `isStatus` (Jackson property name from `getIsStatus()`/`setIsStatus()`), not `status`. */
  isStatus: boolean;
}

/** `/api/global/getSavedConfig` row -- backs this screen's own main table. */
export interface SavedConfigRow {
  configId: number;
  doctorId: number | null;
  specialityId: number | null;
  doctorName: string | null;
  specialityName: string | null;
  /** The underlying button's label -- legacy's view renders this SAME value in
   * both the "Configuration Name" and "Button" columns (aiButtonConfiguration.html
   * lines 264 and 268 both interpolate `{{button.label}}`) -- a real, visible,
   * redundant-column bug, faithfully reproduced, not invented differently here. */
  label: string;
  buttonId: number;
  /** Raw JSON string: `{ urlObject: { URL }, aiPayload: {...} }`. */
  jsonObject: string;
  isStatus: boolean;
}

/** Parsed shape of `jsonObject`/the AI-configuration modal's payload. */
export interface AiConfigPayload {
  urlObject: { URL: string };
  aiPayload: Record<string, string>;
}
