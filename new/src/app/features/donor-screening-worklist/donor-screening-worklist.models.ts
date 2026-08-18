/** Donor Screening Worklist row — the legacy grid's columns (the view at
 * views/master/bloodBankDonor/donor-screening-worklist.html:108-114): Donor
 * Id, Donor Name, Blood Group, Gender, Deferral Status, Deferred Till, Action
 * taken. The backend returns DonorScreeningDetailDto rows (the legacy binds
 * donorNumber/donorName/bloodGroupDesc/genderDesc/deferralStatusDesc/
 * deferredTillDate/screeningResultStatusId); the `??` chains keep the display
 * safe across the field-naming variants. DATE CONVENTION: String dates
 * (dd-M-yyyy HH:mm:ss via formatLegacyNow). */
export interface DonorRow {
  donorId?: number;
  donorNumber?: string;
  donorName?: string;
  uhidNumber?: string;
  mobileNumber?: string;
  bloodGroup?: string;
  bloodGroupDesc?: string;
  gender?: string;
  genderDesc?: string;
  deferralStatus?: string;
  deferralStatusDesc?: string;
  deferredTill?: string;
  deferredTillDate?: string;
  screeningResultStatusId?: number;
  status?: string;
  createdDate?: string;
  [key: string]: unknown;
}

export interface DonorSearch {
  donorNumber?: string;
  donorName?: string;
  uhidNumber?: string;
  fromDate?: string;
  toDate?: string;
  unitId?: number;
  organizationId?: number;
  [key: string]: unknown;
}
