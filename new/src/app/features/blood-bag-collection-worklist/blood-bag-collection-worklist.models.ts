/** Blood Bag Collection Worklist row — the legacy grid's columns
 * (blood-bag-collection-donor-worklist.html): Donor Id, Donor Name, Blood
 * Group, Gender, Screening Status, Bag Collection Status, Action taken.
 * DATE CONVENTION: String dates (dd-M-yyyy HH:mm:ss via formatLegacyNow). */
export interface CollectionRow {
  donorId?: number;
  donorNumber?: string;
  donorName?: string;
  bloodGroup?: string;
  bloodGroupDesc?: string;
  gender?: string;
  genderDesc?: string;
  screeningStatus?: string;
  screeningStatusDesc?: string;
  bagCollectionStatus?: string;
  bagCollectionStatusDesc?: string;
  status?: string;
  createdDate?: string;
  [key: string]: unknown;
}
