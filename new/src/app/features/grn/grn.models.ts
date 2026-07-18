export interface GrnListRow {
  id: number;
  grnDate: string;
  grnNo: string;
  isConsignment: boolean;
  requestingStoreName: string;
  storeName: string;
  storeId: number;
  invoiceNo: string;
  invoiceDate: string;
  dcNo: string;
  dcDate: string;
  vendorName: string;
  addedBy: string;
  status: string;
  approvalStatus: string;
  isSelected?: boolean;
}

export interface DropdownOption {
  id: number;
  store?: string;
  status?: string;
  type?: string;
}

export interface StatusMasterEntry {
  id: number;
  status: string;
}

export interface CancelReason {
  id: number;
  reason: string;
}

export interface VendorSearchResult {
  id: number;
  vendorName: string;
  detail: string;
}

export interface ItemSearchResult {
  id: number;
  details: string;
}

export interface GrnSearchRequest {
  fromDate: string;
  toDate: string;
  storeId?: number;
  requestingStoreId?: number;
  grnNumber?: string;
  grnTypeId?: number;
  vendorName?: string;
  vendorId?: number;
  statusId?: number;
  unitId: number;
  itemId?: number;
}
