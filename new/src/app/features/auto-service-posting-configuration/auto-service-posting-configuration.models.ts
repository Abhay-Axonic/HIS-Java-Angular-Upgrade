export type PostingStatus = 'A' | 'I';

/**
 * Row shape AS RETURNED by GET config/billing/list/{orgId}/{unitId}
 * (AutoPostingConfigController.java:38-47). NOTE (DQ-2026-08-12-013): the
 * endpoint currently 500s with the correlation envelope "Something went
 * wrong. Reference: ERR-<uuid>" because the raw SQL in
 * AutoPostingConfigDaoImpl.java:92,134 selects autopostin0_.
 * is_bed_charges_service, a column that does NOT exist in
 * public.t_auto_posting_services_cofiguration (phantom column, verified
 * information_schema 2026-08-12). The row shape below is the intended
 * projection from the query's select list.
 */
export interface AutoPostingConfigRow {
  id: number;
  orgId: number;
  unitId: number;
  serviceId: number;
  specialityId: number;
  postingTime: string;
  isHalfDay: string;
  halfDayHrs: number;
  isBufferTimeApplicable: string;
  bufferTimeForAdmission: number;
  bufferTimeForDischarge: number;
  isPeriodic: string;
  status: PostingStatus;
}
