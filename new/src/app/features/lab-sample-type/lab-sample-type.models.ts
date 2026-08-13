/**
 * Lab Sample Type (LIS) model shapes.
 *
 * LIVE-VERIFIED 2026-08-13: POST /LIS/global/addSample — orgId, unitId,
 * the code/description + epoch dates; landed sample_id=1 in lab.m_sample_master.
 */
export interface SampleTypeSave {
  orgId: number;
  code: string;
  desc: string;
  status: string;
  createdBy: number;
  createdDate: number;
  updatedBy: number;
  updatedDate: number;
}
