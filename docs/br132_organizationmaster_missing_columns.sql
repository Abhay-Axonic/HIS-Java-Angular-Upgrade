-- BR-132: public.m_organization_master is missing 4 columns the JPA entity
-- (OrganizationMaster.java) declares -- confirmed absent from the local dev
-- DB, same class as BR-076/101/103/104/128/131 -- not local-dev-only drift.
--
-- Found while live-verifying the GlobalOrganisationMaster cross-tenant IDOR
-- fix (see fix/globalorganisationmaster-crosstenant-idor): getOrganizationById
-- threw a live PSQLException on each missing column in turn before this fix
-- could even be tested.
--
-- OrganizationMaster.java's declared mappings:
--   unitTimeZone     -> time_zone, String                    (line 238-239)
--   isSyncScheduling -> is_sync_scheduling, boolean            (line 244-245)
--   orgPrivileges    -> org_privileges, String                 (line 247-248)
--   channelId        -> channel_id, Integer                    (line 250-251)
--
-- Additive only, per Expand-Migrate-Contract. All nullable (is_sync_scheduling
-- defaults false, matching the entity's primitive boolean default), no
-- existing row affected.

ALTER TABLE public.m_organization_master
  ADD COLUMN IF NOT EXISTS time_zone character varying(100);

ALTER TABLE public.m_organization_master
  ADD COLUMN IF NOT EXISTS is_sync_scheduling boolean NOT NULL DEFAULT false;

ALTER TABLE public.m_organization_master
  ADD COLUMN IF NOT EXISTS org_privileges text;

ALTER TABLE public.m_organization_master
  ADD COLUMN IF NOT EXISTS channel_id integer;
