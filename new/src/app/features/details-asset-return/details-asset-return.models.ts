/**
 * Rebuild of the legacy `detailsAssetReturn` state's data shape -- see
 * detailAssetReturnController.js (57 lines, note the singular filename
 * vs. the plural real state name `detailsAssetReturn` -- a filename-only
 * inconsistency, zero functional impact) + details-asset-return.html
 * (174 lines). Same DTO as the list screen
 * (../asset-return/asset-return.models.ts's `AssetReturnRow`) --
 * `com.param.assetManagement.indent.assetReturn.dto.AssetReturnResponse`
 * -- here with its `item` list (`ItemResponse`) actually populated.
 */
export interface DetailsAssetReturnItem {
  id?: number;
  itemCode?: string;
  itemName?: string;
  returnQuantity?: number;
  availableQuantity?: number;
}

export interface DetailsAssetReturn {
  id?: number;
  returnNo?: string;
  returnDate?: string;
  fromStoreId?: number;
  /** Rendered under "Accepting Store:" -- details-asset-return.html:13. */
  fromStore?: string;
  toStoreId?: number;
  /** Rendered under "Returning Store:" -- details-asset-return.html:16.
   * NOT cross-wired like the list screen's table columns -- this detail
   * header binds fromStore/toStore straight to their matching labels. */
  toStore?: string;
  carrierId?: number;
  carrierName?: string;
  remark?: string;
  item: DetailsAssetReturnItem[];
}
