-- BR-131: abdm.m_abdm_credential did not exist at all in the local dev DB
-- (the whole `abdm` schema was missing), and even the table definition
-- already tracked in docs/br076_missing_tables.sql (line 1, a prior, broader
-- schema-drift fix) is itself missing 2 columns that AbdmCredentialDaoImpl's
-- live SQL queries select -- confirmed absent from both the local dev DB and
-- docs/br076_missing_tables.sql, same class as BR-076/101/103/104/128 --
-- not local-dev-only drift.
--
-- Found while live-verifying the getCredentialDetails/addGetServiceAbdm
-- cross-tenant IDOR fix (see fix/abdm-credentialdetails-addgetservice-crosstenant-idor):
-- this fix's own live verification couldn't run until both the base table
-- (docs/br076_missing_tables.sql:1) and these 2 extra columns existed.
--
-- AbdmCredentialDaoImpl.getCredentialDetails' native SELECT references:
--   ddcds.xcm_id        as "xcmId"       -- not in br076's table definition
--   ddcds.abhasbx_url   as "abhasbxUrl"  -- not in br076's table definition
--
-- Run docs/br076_missing_tables.sql (line 1) first if abdm.m_abdm_credential
-- does not exist yet -- this file is additive on top of that one, not a
-- replacement. Additive only, per Expand-Migrate-Contract. Both nullable, no
-- existing row affected.

ALTER TABLE abdm.m_abdm_credential
  ADD COLUMN IF NOT EXISTS xcm_id text;

ALTER TABLE abdm.m_abdm_credential
  ADD COLUMN IF NOT EXISTS abhasbx_url text;
