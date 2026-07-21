export interface DaiaStatus {
  id: number;
  status: string;
}

export interface DaiaRejectReason {
  id: number;
  reason: string;
}

export interface DaiaItem {
  id: number;
  itemCode?: string;
  itemName?: string;
  issueQuantity?: number;
  pendingQuantity?: number;
  maxOrderQuantity?: number;
  specification?: string;
  approvedQuantity?: number;
  rejectQuantity?: number;
  rejectNote?: string;
  rejectReasonId?: number;
  batchMapperId?: number;
  statusId?: number;
  isSelected?: boolean;
  isDisabled?: boolean;
  isReject?: boolean;
  isQuantityValid?: boolean;
}

export interface DaiaDetail {
  fromStore?: string;
  toStore?: string;
  issueDate?: string;
  issueNo?: string;
  status?: string;
  itemResponse: DaiaItem[];
}

export function emptyDaiaDetail(): DaiaDetail {
  return { itemResponse: [] };
}
