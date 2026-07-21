/** Mirrors a `listObject` entry from GETBEDTYPELIST (ctrl:103-114, view:140-150). */
export interface BedTypeRow {
  bedTypeId: number;
  bedTypeCode: string;
  bedTypeDesc: string;
  status: 'A' | 'I';
}

/** Mirrors `listObject[0]` from GETSINGLEBEDTYPE (ctrl:271-286). */
export interface BedTypeDetail {
  bedTypeId: number;
  bedTypeCode: string;
  bedTypeDesc: string;
}

/** Mirrors `$scope.bedtype`, the add/edit form model (ctrl:40, 201-210, 259-287). */
export interface BedTypeFormState {
  bedTypeId?: number;
  bedTypeCode: string;
  bedTypeName: string;
}

export function emptyBedTypeForm(): BedTypeFormState {
  return {
    bedTypeId: undefined,
    bedTypeCode: '',
    bedTypeName: '',
  };
}

/** Payload for ADDBEDTYPE (ctrl:201-210). No `unitId` -- legacy's `BedTypeMasterDto`
 * has no such field and the controller never sends one. */
export interface BedTypeSavePayload {
  bedTypeCode: string;
  bedTypeDesc: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** Payload for UPDATEBEDTYPE (ctrl:338-345) -- deliberately no `status`/`createdBy`/
 * `createdDate`, matching the legacy payload's exact shape (asymmetric with save). */
export interface BedTypeUpdatePayload {
  bedTypeId: number;
  bedTypeCode: string;
  bedTypeDesc: string;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

/** Payload for UPDATEBEDTYPESTATUS (ctrl:297-302). */
export interface BedTypeStatusPayload {
  bedTypeId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
}
