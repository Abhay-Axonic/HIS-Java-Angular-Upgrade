export interface LiadItem {
  id: number;
  itemCode?: string;
  itemName?: string;
  quantity?: number;
  pendingQuantity?: number;
  batchNo?: string;
  acceptedQuantity?: number;
  rejectQuantity?: number;
  batchMapperId?: number;
  isSelected?: boolean;
  isDisabled?: boolean;
  isReject?: boolean;
}

export interface LiadDetail {
  fromStoreName?: string;
  toStoreName?: string;
  issueNo?: string;
  issueDate?: string;
  indentNo?: string;
  items: LiadItem[];
}
