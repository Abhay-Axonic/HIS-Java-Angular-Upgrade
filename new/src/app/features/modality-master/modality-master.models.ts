/**
 * Real record shapes for modalityMaster, read from
 * resources/assets/js/controller/opd/unit/modalityMasterController.js
 * (`$scope.modalityObj`, `$scope.modalityList`, `$scope.specialityList`,
 * `$scope.subSpecialityList`, `$scope.equpmentList`) -- not guessed.
 */

/** One row of the loaded/listed modality table (`$scope.modalityList`). */
export interface ModalityListRow {
  modalityId: number;
  modalityCode: string;
  modalityDesc: string;
  specialityName: string;
  subSpecialityName: string;
  equipmentDesc: string;
  status: 'A' | 'I';
}

/** `GET /api/opd/getModalityById` single-record shape (ctrl:337-345). */
export interface ModalityByIdRecord {
  modalityId: number;
  modalityCode: string;
  modalityDesc: string;
  specialityId: number;
  subSpecialityId: number;
  equipmentId: number;
}

/** `$scope.specialityList` option shape (view:47). */
export interface SpecialityOption {
  specialityId: number;
  specialityName: string;
}

/**
 * `$scope.subSpecialityList` option shape (view:63) -- `isModality` gates
 * which rows the legacy view actually renders (`ng-if="info.isModality == 'Y'"`),
 * not the backend response itself; kept as a field so the rebuild can
 * reproduce that same client-side filter.
 */
export interface SubSpecialityOption {
  subSpecialityId: number;
  subSpecialityName: string;
  isModality: 'Y' | 'N';
}

/** `$scope.equpmentList` option shape (view:88 -- `info.id`/`info.name`). */
export interface EquipmentOption {
  id: number;
  name: string;
}

/** Editable form state, mirrors `$scope.modalityObj` (ctrl:61-74). */
export interface ModalityFormRecord {
  modalityId: number;
  modalityCode: string;
  modalityDesc: string;
  equipmentId: string;
  specialityId: string;
  subSpecialityId: string;
}

export function emptyModality(): ModalityFormRecord {
  return {
    modalityId: 0,
    modalityCode: '',
    modalityDesc: '',
    equipmentId: '',
    specialityId: '',
    subSpecialityId: '',
  };
}
