-- BR-102: bedMaster's Save (addBed) is completely broken on real, unmodified legacy
-- for both Normal Bed and Moving Bed tabs. BedMasterDaoImpl.getConfiguredBedLimit
-- (BedMasterDaoImpl.java:1525-1531) runs a raw SQL query selecting allocated_beds
-- from t_unit_application_billing_configuration, but that column does not exist --
-- confirmed via psql \d, and independently confirmed absent from the tracked DDL
-- source of truth (legacy/Database/HIS-SME-DDL_20210430_150255584.sql), same as
-- BR-101 (tariffMaster) -- not local-dev-only drift.
--
-- Additive only, per Expand-Migrate-Contract. Nullable so existing rows are
-- unaffected; a NULL value means "no configured cap," which BedMasterServiceImpl's
-- own null-guard already handles correctly (createBed only enforces the cap when
-- both getBedCount and getConfiguredBedLimit are non-null).

ALTER TABLE public.t_unit_application_billing_configuration
  ADD COLUMN IF NOT EXISTS allocated_beds integer;
