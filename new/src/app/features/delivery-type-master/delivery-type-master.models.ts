export type DeliveryTypeStatus = 'A' | 'I';

/** Row shape returned by getDeliveryTypeMaster (ctrl:52-64) -- the fields
 * legacy's view actually renders (delivery-type-master.html:120-130). */
export interface DeliveryTypeRow {
  deliveryTypeId: number;
  deliveryTypeCode: string;
  deliveryTypeName: string;
  status: DeliveryTypeStatus;
}

/** Response shape of getDeliveryTypeById (ctrl:127-155,
 * `response.data.listObject[0]`). */
export interface DeliveryTypeDetail {
  deliveryTypeId: number;
  deliveryTypeCode: string;
  deliveryTypeName: string;
}

/** Mirrors `$scope.deliverytype` (form state only, ctrl:38). Field names
 * kept aligned to the real wire-format field names (`deliveryTypeCode`/
 * `deliveryTypeName`), not legacy's own shorthand scope fields
 * (`deliverytype.deliveryCode`/`deliveryName`). */
export interface DeliveryTypeForm {
  deliveryTypeId?: number;
  deliveryTypeCode: string;
  deliveryTypeName: string;
}

/** Matches `$scope.deliverytype = {}` (ctrl:38) -- every field starts genuinely empty. */
export function emptyDeliveryTypeForm(): DeliveryTypeForm {
  return {
    deliveryTypeId: undefined,
    deliveryTypeCode: '',
    deliveryTypeName: '',
  };
}

/** POST body for saveDeliveryTypeMaster (ctrl:81-88). */
export interface DeliveryTypeSavePayload {
  deliveryTypeCode: string;
  deliveryTypeName: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
}

/** POST body for updateDeliveryTypeMaster (ctrl:201-206) -- no `status`/
 * `createdBy`/`organizationId`, matching legacy exactly (only present on the
 * create payload). */
export interface DeliveryTypeUpdatePayload {
  deliveryTypeCode: string;
  deliveryTypeName: string;
  deliveryTypeId: number;
  updatedBy: number;
}

/** POST body for updateStatusForDeliveryType (ctrl:168-172). */
export interface DeliveryTypeStatusPayload {
  deliveryTypeId: number;
  status: DeliveryTypeStatus;
  updatedBy: number;
}
