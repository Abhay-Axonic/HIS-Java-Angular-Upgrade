/**
 * Screen 625 — gender feed row. Shape returned by GET /adt/getActiveGenderList
 * (GETACTIVEGENDERLIST, adt.url.config.js:170). Verified live 2026-08-12:
 * SUCCESS, 3 rows {id, desc, code, createdBy, createdDate, status} — REAL FEED.
 * The screen itself (allInclusivePackage) is a package-creation FORM
 * (transaction class, DQ-2026-08-12-034); this row type only models the feed
 * the form's path actually exposes.
 */
export interface GenderRow {
  id: number;
  desc: string;
  code: string;
  createdBy?: number;
  createdDate?: string;
  status: string;
}
