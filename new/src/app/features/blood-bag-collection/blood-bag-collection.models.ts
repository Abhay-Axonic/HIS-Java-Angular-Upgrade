/** Blood Bag Collection form — the legacy's donorDetailsObj fields
 * (blood-bag-collection-donor.html): camp, bank, collection date/times,
 * collected-from, blood unit/segment/barcode/bag-type/capacity/batch,
 * termination reason, accept/discard status + the examination child rows.
 * Dates: dd-M-yyyy HH:mm:ss strings (numeric month); '' for absent dates —
 * GlobalCommonDateUtils.getDate NPEs on null. */
export interface BagCollectionDetail {
  donorId?: number | null;
  donorNumber?: string;
  donorName?: string;
  bloodGroup?: string;
  gender?: string;
  campId?: number | null;
  bloodBankId?: number | null;
  collectionDate?: string | null;
  fromTime?: string | null;
  toTime?: string | null;
  collectedFrom?: number | null;
  bloodUnitNumber?: number | null;
  segmentNumber?: string | null;
  barCode?: string | null;
  bloodBagTypeId?: number | null;
  bagCapacityId?: number | null;
  batchNo?: string | null;
  terminationReason?: string | null;
  terminationReasonId?: number | null;
  bloodBagCollectionStatusId?: number | null;
  bloodBagCollectionId?: number | null;
  examinationDetailsList?: Examination[];
  unitId?: number | null;
  organizationId?: number | null;
  createdBy?: number | null;
  createdDate?: string | null;
  updatedBy?: number | null;
  updatedDate?: string | null;
  [key: string]: unknown;
}

/** The examination child rows (the legacy's exam block: examinationDate,
 * pulse, bpSystolic, bpDiastolic — separate child writes). */
export interface Examination {
  examinationDate?: string | null;
  pulse?: number | null;
  bpSystolic?: number | null;
  bpDiastolic?: number | null;
  [key: string]: unknown;
}

/** The dropdown rows (id + display-name masters). */
export interface IdNameRow {
  id?: number;
  name?: string;
  [key: string]: unknown;
}
