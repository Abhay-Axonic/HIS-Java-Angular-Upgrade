/**
 * Field set taken directly from
 * views/master/UnitMaster/organizationMaster/counter-master.html +
 * resources/assets/js/controller/master/unit/counterMasterController.js
 * (`counterMasterController`, no `controllerAs`). Only two editable fields
 * on this screen: counter code + description.
 */
export interface CounterRow {
  counterId: number;
  counterCode: string;
  counterDesc: string;
  status: 'A' | 'I';
}

/** Shape of GETCOUNTERMASTERBYID's `listObject[0]` (ctrl:219-235) -- only the
 * fields `showUpdateBtn` actually reads back into the form. */
export interface CounterDetail {
  counterId: number;
  counterCode: string;
  counterDesc: string;
}

/** Mirrors `save()`'s `data` object (ctrl:157-169). */
export interface CounterSavePayload {
  counterDesc: string;
  counterCode: string;
  organizationId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
  status: 'A';
  createdDate: string;
  updatedDate: string;
}

/** Mirrors `update()`'s `data` object (ctrl:251-260) -- no `createdBy`/`status`,
 * matching legacy exactly (only present on the create payload). */
export interface CounterUpdatePayload {
  counterId: number;
  counterDesc: string;
  counterCode: string;
  organizationId: number;
  unitId: number;
  updatedBy: number;
  updatedDate: string;
}

/**
 * Minimal status-toggle payload used by this rebuild -- NOT a literal
 * reproduction of `updateStatus` (ctrl:294-330), which POSTs the ENTIRE
 * `$scope.common` edit-form object (whatever stray counterCode/counterDesc
 * happen to be sitting in the add/edit form at the time) as the status-toggle
 * body. That's a cross-cutting sloppy-payload anti-pattern shared with this
 * codebase's cabinMaster/employeeCategoryMaster, being cleaned up as a
 * standing improvement across all three screens in this batch -- it has no
 * observable behavior difference for the user, so it is fixed here rather
 * than faithfully reproduced as a "bug".
 */
export interface CounterStatusPayload {
  counterId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}

/** Form/edit-buffer shape (mirrors `$scope.common`, ctrl:41-46).
 * `counterId` is only present while editing an existing row. */
export interface CounterForm {
  counterId?: number;
  counterCode: string;
  counterDesc: string;
}

export function emptyCounterForm(): CounterForm {
  return {
    counterCode: '',
    counterDesc: '',
  };
}
