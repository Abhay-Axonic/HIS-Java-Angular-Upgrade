export type ChecklistItemStatus = 'A' | 'I';

/** Row shape returned by getChecklistItemMasterList (ctrl:39-62). Confirmed
 * live 2026-07-28. */
export interface ChecklistItemRow {
  checklistItemMasterId: number;
  code: string;
  description: string;
  status: ChecklistItemStatus;
}

/** Mirrors $scope.checklistItemObj (ctrl:67-78). One object serves BOTH
 * create and edit -- same real upsert shape as this batch's
 * checklistEquipmentMaster sibling (one save endpoint, no separate update
 * endpoint exists at all -- confirmed via the backend controller having
 * only saveChecklistItemMaster/updateChecklistItemMasterStatus, no
 * updateChecklistItemMaster). */
export interface ChecklistItemForm {
  checklistItemMasterId: number | null;
  code: string;
  description: string;
}

export function emptyChecklistItemForm(): ChecklistItemForm {
  return {
    checklistItemMasterId: null,
    code: '',
    description: '',
  };
}
