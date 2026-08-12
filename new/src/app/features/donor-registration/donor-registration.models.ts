/**
 * Screen 719 — prefix feed row. Shape returned by GET /adt/getActivePrefixList
 * (GETACTIVEPREFIXLIST, adt.url.config.js:239). Verified live 2026-08-12:
 * SUCCESS, 1 row {prefixId, prefixDesc, prefixCode, status} — REAL FEED.
 * The screen itself (donorRegistration) is a registration FORM (transaction
 * class, DQ-2026-08-12-034); this row type only models the feed the form's
 * path actually exposes.
 */
export interface PrefixRow {
  prefixId: number;
  prefixDesc: string;
  prefixCode: string;
  status: string;
}
