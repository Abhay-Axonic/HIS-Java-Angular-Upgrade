export interface AradItem {
  id: number;
  itemCode?: string;
  itemName?: string;
  availableQuantity?: number;
  returnQuantity?: number;
  batchNo?: string;
  acceptedQuantity?: number;
  rejectedQuantity?: number;
  rejectQuantity?: number;
  rejectNote?: string;
  rejectReasonId?: number;
  statusId?: number;
  isSelected?: boolean;
  isDisabled?: boolean;
  isReject?: boolean;
}

export interface AradDetail {
  id?: number;
  returnDate?: string;
  returnNo?: string;
  toStore?: string;
  fromStore?: string;
  approvalStatusId?: number;
  status?: string;
  item: AradItem[];
}

export interface AradStatus {
  id: number;
  status: string;
}

export interface AradRejectReason {
  id: number;
  reason: string;
}
