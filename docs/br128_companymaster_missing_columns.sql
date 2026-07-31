-- BR-128: CompanyMaster (public.t_company_master) is missing 3 columns the
-- JPA entity declares -- confirmed absent from both the local dev DB AND the
-- tracked DDL source of truth (legacy/Database/HIS-SME-DDL_20210430_150255584.sql:20807-20843),
-- same class as BR-101/BR-103/BR-104 -- not local-dev-only drift.
--
-- Found while live-verifying the CompanyMaster cross-tenant IDOR fix (see
-- fix/companymaster-crosstenant-idor): every read/write path that touches
-- these 3 fields (GET_COMPANY_MASTER_LIST_BY_ID, updateCompanyMaster) threw
-- a live PSQLException ("column ... does not exist") before this fix could
-- even be tested, not a hypothetical gap.
--
-- CompanyMaster.java's declared mappings:
--   blockReportPrint -> block_report_print, Character   (line 269-270)
--   generalLedgerId  -> general_ledger_id, Integer       (line 302-303)
--   creditDays       -> credit_days, String               (line 194-195)
--
-- Additive only, per Expand-Migrate-Contract. All nullable, no existing row
-- affected -- NULL simply means "not set yet," matching every other optional
-- column on this table.

ALTER TABLE public.t_company_master
  ADD COLUMN IF NOT EXISTS block_report_print character(1);

ALTER TABLE public.t_company_master
  ADD COLUMN IF NOT EXISTS general_ledger_id integer;

ALTER TABLE public.t_company_master
  ADD COLUMN IF NOT EXISTS credit_days character varying(20);
