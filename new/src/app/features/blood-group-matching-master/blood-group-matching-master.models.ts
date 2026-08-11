export type BloodGroupStatus = 'A' | 'I';

/**
 * Row shape returned by GET /global/bloodBank/getActiveBloodGroupMaster/{org}/
 * {unit} (legacy bloodGroupMatchingMasterController.js:70). Confirmed live
 * 2026-08-12: SUCCESS with 3 real rows — bloodGroupId 1, bloodGroupCode "A+",
 * bloodGroupDesc "A Positive (Updated)", status 'A'.
 */
export interface BloodGroupRow {
  bloodGroupId: number;
  bloodGroupCode: string;
  bloodGroupDesc: string;
  status: BloodGroupStatus;
}

/** POST body for /api/bloodBank/saveBloodGroupMatchingMaster (ctrl:187-189).
 * The legacy screen posts the whole matching list. */
export interface BloodGroupMatchingSavePayload {
  bloodGroupMatchingList: Array<{
    bloodGroupId: number;
    bloodComponentId: number;
    compatibleGroupIds: number[];
    organizationId: number;
    unitId: number;
    createdBy: number;
    updatedBy: number;
  }>;
}
