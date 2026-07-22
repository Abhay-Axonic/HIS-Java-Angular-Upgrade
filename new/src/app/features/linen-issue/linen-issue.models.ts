/**
 * Rebuild of legacy `linenIssue` state -- see linenIssueController.js
 * (194 lines, self-registered `.config($stateProvider)`, moduleId 344).
 * Response shapes taken directly from the real backend DTOs:
 * LinenLaundryDropdown.java (fromStore/toStore/status/priority -- note:
 * NO indentType field exists on the real dropdown response, even though
 * legacy's controller reads `response.data.indentType` -- always
 * `undefined` there; harmless since the one UI control that would show it
 * (Tab 1's Type select) is HTML-commented-out and Tab 2's Type select is
 * a hardcoded static <select>, never bound to this array at all) and
 * LinenResponse.java (id/indentDate/indentNo/fromStoreId/fromStoreName/
 * toStoreId/priorityId/priorityName/toStoreName/requestedBy/
 * requestedById/remark/statusId/status -- the exact constructor
 * LinenIssueService.getLinenIssueList/getLinenIssue actually populate).
 */
export interface LinenIssueOption {
  id?: number;
  store?: string;
  status?: string;
  type?: string;
  [key: string]: unknown;
}

export interface LinenIssueDropdowns {
  fromStore?: LinenIssueOption[];
  toStore?: LinenIssueOption[];
  status?: LinenIssueOption[];
}

export interface LinenIssueRow {
  id: number;
  indentDate?: string;
  indentNo?: string;
  fromStoreId?: number;
  fromStoreName?: string;
  toStoreId?: number;
  priorityId?: number;
  priorityName?: string;
  toStoreName?: string;
  requestedBy?: string;
  requestedById?: number;
  remark?: string;
  statusId?: number;
  status?: string;
  isChecked?: boolean;
}
