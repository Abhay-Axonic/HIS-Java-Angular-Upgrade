/**
 * Field set taken directly from
 * views/master/GlobalMaster/qualification-master.html +
 * resources/assets/js/controller/master/global/common/qualificationMasterController.js
 * (`qualificationMasterController`, no `controllerAs`). Only two fields on
 * this screen: qualification code + description.
 */
export interface QualificationRow {
  qualificationId: number;
  qualificationCode: string;
  qualificationDesc: string;
  status: 'A' | 'I';
}

/** Shape of GETQUALIFICATIONMASTERBYID's `listObject[0]` -- only the fields
 * `showUpdateBtn` (ctrl:255-280) actually reads back into the form. */
export interface QualificationDetail {
  qualificationId: number;
  qualificationCode: string;
  qualificationDesc: string;
}

/** Mirrors `saveQualificationMaster`'s `data` object (ctrl:201-210). */
export interface QualificationSavePayload {
  qualificationCode: string;
  qualificationDesc: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** Mirrors `updateQualificationMaster`'s `data` object (ctrl:328-335) -- note
 * no `status` field is sent on update; status only changes via the separate
 * `updateStatus` endpoint. */
export interface QualificationUpdatePayload {
  qualificationCode: string;
  qualificationDesc: string;
  qualificationId: number;
  organizationId: number;
  updatedBy: number;
  updatedDate: string;
}

/** Mirrors `updateStatus`'s `data` object (ctrl:290-295). */
export interface QualificationStatusPayload {
  qualificationId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}

/** Form/edit-buffer shape (mirrors `$scope.qualificationMasterObj`).
 * `qualificationId` is only present while editing an existing row. */
export interface QualificationForm {
  qualificationId?: number;
  qualificationCode: string;
  qualificationDesc: string;
}

export function emptyQualificationForm(): QualificationForm {
  return {
    qualificationCode: '',
    qualificationDesc: '',
  };
}
