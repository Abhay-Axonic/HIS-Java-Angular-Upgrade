export type VisitTypeStatus = 'A' | 'I';

/** Row shape returned by `GET_VISIT_TYPE_LIST` /
 * `GET_ACTIVE_VISIT_TYPE_LIST` (legacy named queries, aliasToBean):
 * generic `id`/`desc`/`code`/`status` field names. */
export interface VisitTypeRow {
  id: number;
  code: string;
  desc: string;
  status: VisitTypeStatus;
}

/** Form model matching `$scope.common` in
 * visitTypeMasterController.js. */
export interface VisitTypeForm {
  id?: number;
  code: string;
  desc: string;
}

export function emptyVisitTypeForm(): VisitTypeForm {
  return {
    id: undefined,
    code: '',
    desc: '',
  };
}

/** POST body for updateVisitTypeStatus (ctrl `visitObj`). */
export interface VisitTypeStatusPayload {
  visitTypeId: number;
  status: VisitTypeStatus;
  updatedBy: number;
  updatedDate: string;
}
