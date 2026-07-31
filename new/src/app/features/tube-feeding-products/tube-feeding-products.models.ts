/**
 * Row shape returned by GET_TUBE_FEEDING_PRODUCTS_LIST -- confirmed against
 * TubeFeedingProductsController.java / TubeFeedingProductsDto (Global module).
 */
export interface TubeFeedingProductRow {
  tubeFeedingProductsId: number;
  productsCode: string;
  productsDesc: string;
  status: 'A' | 'I';
}

/** Editable form state -- mirrors legacy's `$scope.common`. */
export interface TubeFeedingProductRecord {
  tubeFeedingProductsId?: number;
  productsCode: string;
  productsDesc: string;
}

export function emptyTubeFeedingProduct(): TubeFeedingProductRecord {
  return {
    tubeFeedingProductsId: undefined,
    productsCode: '',
    productsDesc: '',
  };
}
