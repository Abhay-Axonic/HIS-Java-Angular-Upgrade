export type TariffStatus = 'A' | 'I';

/**
 * Row shape returned by GET /api/units/pharmacy/tariff/getAll/{org}/{unit}
 * (legacy pharmacyTariffMasterController.js:55). Confirmed live 2026-08-12:
 * SUCCESS with 3 real rows — id 1, assetCategoryName "Pharmaceutical",
 * productCategoryName "Test PMS Product Category", markUpDown 0,
 * markUpDownPer 0.0, status 'A'.
 */
export interface PharmacyTariffRow {
  id: number;
  assetCategoryId: number;
  assetCategoryName: string;
  productCategoryId: number;
  productCategoryName: string;
  markUpDown: number;
  markUpDownPer: number;
  status: TariffStatus;
  paymentEntitlementId: number;
}

/** POST body for /api/units/pharmacy/tariff/save (ctrl:295). */
export interface PharmacyTariffSavePayload {
  assetCategoryId: number;
  productCategoryId: number;
  markUpDown: number;
  markUpDownPer: number;
  paymentEntitlementId: number;
  status: 'A';
  organizationId: number;
  unitId: number;
  createdBy: number;
  updatedBy: number;
}

/** POST body for /api/units/pharmacy/tariff/update (ctrl:141). */
export interface PharmacyTariffUpdatePayload {
  id: number;
  assetCategoryId: number;
  productCategoryId: number;
  markUpDown: number;
  markUpDownPer: number;
  paymentEntitlementId: number;
  organizationId: number;
  unitId: number;
  updatedBy: number;
}
