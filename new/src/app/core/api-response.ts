/**
 * Shared response envelope every legacy endpoint returns (via
 * GenericService/BillingGenericService.serviceAction in the AngularJS app).
 * Rebuilt screens must keep reading these exact fields for parity --
 * this is not a new API contract, it's the existing one.
 */
export interface ApiResponse<TObject = unknown, TList = unknown> {
  status: 'success' | 'error';
  message?: string;
  object?: TObject;
  listObject?: TList[];
}
