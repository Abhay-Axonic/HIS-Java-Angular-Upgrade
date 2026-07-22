/**
 * Field names/types read directly from
 * RejectionMasterDto.java (legacy/Global/src/main/java/com/param/global/org/common/dto/RejectionMasterDto.java)
 * and rejectionMasterController.js -- not guessed.
 *
 * Note the legacy asymmetry, preserved here: the frontend form field is
 * named `rejectDesc` (ctrl:203/330 -- `$scope.rejectionMasterObj.rejectDesc`),
 * but the wire/DTO field it maps to is `reasonDesc` (RejectionMasterDto,
 * SAVEREJECTIONMASTER/UPDATEREJECTIONMASTER payload). The list response rows
 * only ever expose `reasonDesc` (ctrl:132/274, `response.data.listObject[0].reasonDesc`)
 * -- `rejectDesc` never appears on the wire in either direction, only inside
 * the in-memory form object.
 */
export interface RejectionMasterFormRecord {
  rejectId: number | null;
  code: string;
  rejectDesc: string;
}

export function emptyRejectionMaster(): RejectionMasterFormRecord {
  return {
    rejectId: null,
    code: '',
    rejectDesc: '',
  };
}

/** Shape of one row in `getRejectionMasterList`'s `listObject` (ctrl:108-116). */
export interface RejectionMasterListRow {
  id: number;
  code: string;
  reasonDesc: string;
  isActive: boolean;
}

/** Shape of `getRejectionMasterById`'s `listObject[0]` (ctrl:271-275). */
export interface RejectionMasterDetail {
  id: number;
  code: string;
  reasonDesc: string;
}
