/**
 * Screen 650 — relation feed row. Shape returned by GET /adt/getActiveRelationList
 * (GETACTIVERELATIONLIST, adt.url.config.js:343). Verified live 2026-08-12:
 * SUCCESS, 2 rows {id, desc, code, createdBy, createdDate, status} — REAL FEED.
 * The screen itself (staffRegistration) is a registration FORM (transaction
 * class, DQ-2026-08-12-034); this row type only models the feed the form's
 * path actually exposes.
 */
export interface RelationRow {
  id: number;
  desc: string;
  code: string;
  createdBy?: number;
  createdDate?: string;
  status: string;
}
