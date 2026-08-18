/** Donor Screening form — the legacy form's fields (views/master/bloodBankDonor/
 * donor-screening.html): the vitals block (age, temperature + unit, pulse, blood
 * pressure SBP/DBP, height ft/inches, weight + unit, HB + unit), the screening
 * result (screeningResultStatusId radio + deferral days + deferred-till date),
 * the HB-done-by, and the remarks. Dates: dd-M-yyyy HH:mm:ss via formatLegacyNow
 * (numeric month — the dozer contract; month names fail). */
export interface DonorScreeningDetail {
  donorId?: number | null;
  donorNumber?: string;
  donorName?: string;
  age?: number | null;
  donorTemperature?: number | null;
  temperatureType?: string | null;
  donorPulse?: number | null;
  donorBPSystolic?: number | null;
  donorBpDiastolic?: number | null;
  heightInFt?: number | null;
  heightInInches?: number | null;
  donorWeight?: number | null;
  weightType?: string | null;
  donorHB?: number | null;
  hBType?: string | null;
  hbDoneById?: number | null;
  screeningResultStatusId?: number | null;
  donorDeferralTillDate?: string | null;
  deferralDays?: number | null;
  screeningRemarks?: string | null;
  /** The questionnaire answers — REQUIRED by the backend's save (the NPE at
   * DonorScreeningDetailController.java:36 when null). Never send empty. */
  donorScreeningMapperList?: DonorScreeningMapper[];
  unitId?: number | null;
  organizationId?: number | null;
  createdBy?: number | null;
  createdDate?: string | null;
  updatedBy?: number | null;
  updatedDate?: string | null;
  [key: string]: unknown;
}

/** One questionnaire answer (DonorScreeningMapperDto — the DAO's consumption at
 * DonorScreeningDetailDaoImpl.java:76-93): the question id, the answer status
 * ('Y'/'N' Character), and the per-question deferred-till date. */
export interface DonorScreeningMapper {
  donorScreeningDetailMapperId?: number;
  donorScreeningDetailId?: number;
  donorId?: number;
  donorScreeningParameterId?: number | null;
  parameterResultStatus?: string | null;
  donorDeferralTillDate?: string | null;
  status?: string | null;
  createdBy?: number | null;
  createdDate?: string | null;
  updatedBy?: number | null;
  updatedDate?: string | null;
  organizationId?: number | null;
  unitId?: number | null;
  [key: string]: unknown;
}

/** The screening-question master rows (DonorScreeningMasterDto):
 * id / parameter / deferredTypeId / deferredType / genderId / gender. */
export interface ScreeningQuestion {
  id?: number;
  parameter?: string;
  deferredTypeId?: number;
  deferredType?: string;
  genderId?: number;
  gender?: string;
  [key: string]: unknown;
}
