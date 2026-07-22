/**
 * Real field shape read directly from `ButtonMasterDto`
 * (legacy/Global/src/main/java/com/param/global/unit/dto/ButtonMasterDto.java)
 * and `AIButtonMaster` controller
 * (resources/assets/js/controller/master/unit/aiButtonMasterController.js) --
 * not guessed. See docs/ANGULAR_PENDING_FINDINGS.md entry N (this batch)
 * for the audited quirks this shape needs to preserve.
 */
export interface AiButtonMasterRecord {
  buttonId: number;
  label: string;
  systemPrompt: string;
  userPrompt: string;
  tabId: number;
  /** Raw JSON string combining { urlObject: { URL }, aiPayload: {...} } -- parsed on demand, never restructured. */
  jsonObject: string;
  /** Legacy DTO field is `isStatus` (Jackson property name from `getIsStatus()`/`setIsStatus()`), not `status`. */
  isStatus: boolean;
}

/** Parsed shape of `jsonObject`/the AI-configuration modal's payload. */
export interface AiButtonConfigPayload {
  urlObject: { URL: string };
  aiPayload: Record<string, string>;
}
