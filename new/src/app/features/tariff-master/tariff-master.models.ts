export type TariffStatus = 'A' | 'I';

/**
 * `GET_TARIFF_MASTER_LIST`'s real column aliases
 * (`TariffMaster.java:31-39` named query). `validFrom`/`validTo` here are
 * `to_char(..., 'dd/MM/yyyy')` -- DISPLAY-ONLY strings, never parsed back
 * into a `Date` (the table just renders `{{info.validFrom}}` verbatim in
 * legacy). See `TariffDetail` below for the edit-load format, which is
 * genuinely different.
 */
export interface TariffRow {
  tariffId: number;
  tariffCode: string;
  tariffDesc: string;
  validFrom: string;
  validTo: string;
  status: TariffStatus;
}

/** Editable form state -- mirrors `$scope.tariffMasterObj` (ctrl:86-94). */
export interface TariffForm {
  tariffId?: number;
  tariffCode: string;
  tariffDesc: string;
  validFrom: Date | null;
  validTo: Date | null;
}

export function emptyTariffForm(): TariffForm {
  return { tariffId: undefined, tariffCode: '', tariffDesc: '', validFrom: null, validTo: null };
}

/** One selectable Visit Type / Patient Type option for the "Tariff
 * Properties" multiselects -- mapped from the raw master-data shape the
 * same way the legacy controller does (ctrl:118-136/140-158): `{id, label}`. */
export interface PickerOption {
  id: number;
  label: string;
}

/** Raw `GET /api/global/visitTypeMaster/unit/{unitId}/org/{orgId}` row shape. */
export interface RawVisitTypeOption {
  visitTypeId: number;
  visitTypeName: string;
}

/** Raw `GET /global/patientcategory/{orgId}` row shape -- already `id`/`name`,
 * confirmed live (not `patientTypeId`/`patientCategory` like the mapper-detail
 * queries use). */
export interface RawPatientTypeOption {
  id: number;
  name: string;
}

/** POST body shape for `saveTariffMaster`/`updateTariffMaster`'s
 * `visitTypeList`/`patientTypeList`/`paymentEntitlementIdList` --
 * `TariffMasterDto.getId()` is all the DAO ever reads off each entry
 * (`TariffMasterDaoImpl.saveVisitTypeMapper`/`saveTariffPatientTypeMapper`). */
export interface IdRef {
  id: number;
}

/** POST body for `saveTariffMaster` (ctrl:315-349). `paymentEntitlementIdList`
 * is always `[]` on create -- `$scope.selectedPaymentEnt` is only ever
 * populated by `showUpdateBtn` (edit-load), never by this screen's own
 * (fully commented-out) Payment Entitlement multiselect -- see
 * tariff-master.ts's class doc comment. `billingBedList` is always `null`,
 * verbatim (ctrl:348, that feature is also fully commented out in the view). */
export interface TariffSavePayload {
  tariffCode: string;
  tariffDesc: string;
  validFrom: string;
  validTo: string;
  createdBy: number;
  updatedBy: number;
  createdDate: string;
  updatedDate: string;
  unitId: number;
  organizationId: number;
  status: 'A';
  paymentEntitlementIdList: IdRef[];
  visitTypeList: IdRef[];
  patientTypeList: IdRef[];
  billingBedList: null;
}

/** POST body for `updateTariffMaster` (ctrl:519-559) -- no `status`,
 * no `createdBy`/`createdDate`, no `billingBedList` (legacy's own
 * `dataUpdate` object omits all four, verbatim). */
export interface TariffUpdatePayload {
  tariffId: number;
  tariffCode: string;
  tariffDesc: string;
  validFrom: string;
  validTo: string;
  unitId: number;
  organizationId: number;
  updatedBy: number;
  updatedDate: string;
  paymentEntitlementIdList: IdRef[];
  visitTypeList: IdRef[];
  patientTypeList: IdRef[];
}

/** POST body for `updateTariffMasterStatus` (ctrl:594-609). */
export interface TariffStatusPayload {
  tariffId: number;
  status: TariffStatus;
  updatedBy: number;
  updatedDate: string;
}

/** `getTariffListById`'s real response shape (ctrl:454-514,
 * `TariffMasterDaoImpl.getTariffListById`). **`validFrom`/`validTo` here are
 * `to_char(..., 'MM/dd/yyyy')` -- a DIFFERENT format than the list query's
 * `dd/MM/yyyy` (confirmed real, both read directly from the two distinct
 * named queries in `TariffMaster.java`), and legacy relies on this exact
 * format so that `new Date(response.data.object.validFrom)` parses
 * correctly via JS's native `M/d/yyyy` interpretation (ctrl:479-482).** This
 * rebuild's `parseMonthDayYear()` helper reproduces that same parse. */
export interface TariffDetail {
  tariffId: number;
  tariffCode: string;
  tariffDesc: string;
  validFrom: string;
  validTo: string;
  status: TariffStatus;
  applicablePaymentEntitlementIdList2?: Array<{
    applicablePaymentEntitlementId: number;
    paymentEntitlementDesc: string;
  }>;
  visitTypeList?: Array<{ visitTypeId: number; visitTypeName: string }>;
  patientTypeList?: Array<{ patientTypeId: number; patientCategory: string }>;
}

/** One row of the "Tariff Properties" details popup (`showMapperDetails`,
 * ctrl:392-452) -- Visit Type half. Only the Visit Type/Patient Type
 * halves are modeled; the Payment Entitlement/Billing Bed Category halves
 * are fully commented out in the legacy view (dead, unrendered) -- see
 * tariff-master.ts's class doc comment for why those two calls are not
 * reproduced here. */
export interface VisitTypeMapperDetail {
  tariffVisitTypeMapperId: number;
  visitTypeName: string;
  status: TariffStatus;
}

export interface PatientTypeMapperDetail {
  tariffPatientTypeMapperId: number;
  patientCategory: string;
  status: TariffStatus;
}
