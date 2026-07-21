/** Mirrors a `listObject` entry from GETSHORTLEAVEREASONLIST (ctrl:106-117). */
export interface ShortLeaveReasonRow {
  shortLeaveReasonId: number;
  shortLeaveReasonCode: string;
  shortLeaveReasonName: string;
  status: 'A' | 'I';
}

/** Mirrors `listObject[0]` from GETSINGLESHORTLEAVEREASON (ctrl:279-285). */
export interface ShortLeaveReasonDetail {
  shortLeaveReasonId: number;
  shortLeaveReasonCode: string;
  shortLeaveReasonName: string;
}

/** Mirrors $scope.shortleavereason, the add/edit form model (ctrl:47, 283-285). */
export interface ShortLeaveReasonFormState {
  shortLeaveReasonId?: number;
  shortLeaveReasonCode: string;
  shortLeaveReasonName: string;
}

export function emptyShortLeaveReasonForm(): ShortLeaveReasonFormState {
  return {
    shortLeaveReasonId: undefined,
    shortLeaveReasonCode: '',
    shortLeaveReasonName: '',
  };
}

/** Save payload for ADDSHORTLEAVEREASON (ctrl:206-215). */
export interface ShortLeaveReasonSavePayload {
  shortLeaveReasonCode: string;
  shortLeaveReasonName: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** Update payload for UPDATESHORTLEAVEREASON (ctrl:342-349). */
export interface ShortLeaveReasonUpdatePayload {
  shortLeaveReasonId: number;
  shortLeaveReasonCode: string;
  shortLeaveReasonName: string;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

/** Payload for UPDATESHORTLEAVEREASONSTATUS (ctrl:301-306). */
export interface ShortLeaveReasonStatusPayload {
  shortLeaveReasonId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}
