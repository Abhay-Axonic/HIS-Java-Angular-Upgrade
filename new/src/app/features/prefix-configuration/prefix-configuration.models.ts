/**
 * Shapes taken directly from
 * resources/assets/js/controller/master/unit/prefixConfigurationMasterController.js.
 * None of these are wrapped in the app's usual `ApiResponse` envelope --
 * the dropdown endpoint returns a bare `{screen, reset, format}` object
 * (ctrl:62-84), the search-list endpoint returns a bare array (ctrl:104-113),
 * the detail endpoint returns a bare object (ctrl:121-142), and the
 * save/update endpoints return a bare `{code, message}` object (ctrl:190-206,
 * 226-237) -- not guessed, read straight off the controller's own
 * `response.data.*` accesses.
 */

export interface ScreenOption {
  id?: number;
  screen: string;
  isGlobal?: boolean;
}

export interface ResetOption {
  id?: number;
  resetName: string;
}

export interface FormatOption {
  id?: number;
  formatName: string;
  code?: string;
}

export interface PrefixDropdownResponse {
  screen: ScreenOption[];
  reset: ResetOption[];
  format: FormatOption[];
}

export interface PrefixRow {
  id: number;
  screenName: string;
  generatedCode: string;
  runningNumber: number | string;
  resetName: string;
  length: number | string;
}

export interface PrefixDetail {
  id: number;
  screenId: number;
  resetId: number;
  runningNumber: number | string;
  length: number | string;
  generatedCode: string;
}

export interface PrefixSavePayload {
  screenId?: number;
  resetId?: number;
  runningNumber: string | number;
  length: string | number;
  generatedCode: string;
  flag?: boolean;
  unitId: number;
  organizationId: number;
  userId: number;
}

export interface PrefixUpdatePayload {
  screenId?: number;
  resetId?: number;
  runningNumber: string | number;
  length: string | number;
  generatedCode: string;
  unitId: number;
  organizationId: number;
  userId: number;
}

export interface PrefixSaveResponse {
  code?: string;
  message?: string;
}
