export type ChecklistEquipmentStatus = 'A' | 'I';

/** Row shape returned by getAllEquipmentChecklistMasterListPaginationList
 * (ctrl:71-109). Confirmed live 2026-07-28. */
export interface ChecklistEquipmentRow {
  checklistEquipmentMasterId: number;
  checklistCode: string;
  checklistName: string;
  status: ChecklistEquipmentStatus;
}

/** Mirrors $scope.checklistObj (ctrl:35-46). One object serves BOTH create
 * and edit -- `checklistEquipmentMasterId` is null for create, populated for
 * edit; the single save endpoint upserts based on its presence (confirmed
 * via direct DAO read: `EquipmentChecklistDaoImpl.saveEquipmentChecklist`
 * calls `update()` if the id is non-null, `save()` otherwise). */
export interface ChecklistEquipmentForm {
  checklistEquipmentMasterId: number | null;
  checklistCode: string;
  checklistName: string;
}

export function emptyChecklistEquipmentForm(): ChecklistEquipmentForm {
  return {
    checklistEquipmentMasterId: null,
    checklistCode: '',
    checklistName: '',
  };
}
