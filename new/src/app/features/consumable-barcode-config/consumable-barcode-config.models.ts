/**
 * Field shape taken directly from the real Java backend (confirmed real --
 * see class comment in consumable-barcode-config.ts):
 * com.param.entity.model.master.ConsumableBarcodeConfig via
 * ConsumableBarcodeConfigResponse/-Request.
 */
export interface ConsumableBarcodeConfigRow {
  id: number;
  status?: boolean;
  itemName: boolean;
  itemCode: boolean;
  batchNo: boolean;
  expiryDate: boolean;
  lotNo: boolean;
  mrp: boolean;
  salePrice: boolean;
  grnNo: boolean;
  vendorName: boolean;
  vendorCode: boolean;
  hospitalName: boolean;
}

export interface ConsumableBarcodeConfigPayload {
  itemName: boolean;
  itemCode: boolean;
  batchNo: boolean;
  expiryDate: boolean;
  lotNo: boolean;
  mrp: boolean;
  salePrice: boolean;
  grnNo: boolean;
  vendorName: boolean;
  vendorCode: boolean;
  hospitalName: boolean;
  unitId: number;
  organizationId: number;
  userId: number;
}
