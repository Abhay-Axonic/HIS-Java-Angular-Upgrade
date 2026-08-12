/**
 * Screen 722 — blood-bag search result row. The legacy controller
 * (transfusionTransmittedDiseasesController.js) has NO list endpoint — only
 * SEARCH_BAG_DETAILS = "/api/bloodbank/searchBloodBag"
 * (bloodBank.url.config.js:141), a blood-bag search whose result rows land in
 * response.data.listObject (controller:90). The result shape was NOT
 * live-verified (search/write-path, DQ-2026-08-12-034); bloodBagNo is the one
 * field the search form itself keys on. Fields are optional with an index
 * signature so unverified search columns do not break reading.
 */
export interface BloodBagRow {
  bloodBagId?: number;
  bloodBagNo?: string;
  status?: string;
  [key: string]: unknown;
}
