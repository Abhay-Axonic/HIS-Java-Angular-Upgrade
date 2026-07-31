-- BR-137: t_unit_application_billing_configuration had a UNIQUE constraint on
-- unit_id ALONE, which made UnitApplicationBillingConfigurationDaoImpl.
-- updateUnitApplicationBillingConfig's "deactivate old row (status='I'), then
-- INSERT a new one with the same unit_id" design structurally impossible to
-- complete for any unit that already has a config row -- confirmed to fail
-- identically for every caller, legitimate or forged, independent of BR-136's
-- tenant-scoping fix.
--
-- Owner-approved fix (2026-07-31): every existing read path against this table
-- (GET_DEFAULT_SELF_TARIFF_FROM_APP_BILLING_CONF -- used live by
-- RazorPayServiceImpl for real payment-gateway order creation --,
-- GET_URL_FOR_PDF_GENERATION[_MIS][_FOR_BI], GET_LIST_UNIT_APPLICATION_BILLING_CONFIGURATION)
-- already explicitly filters "AND status='A'", confirming the original design
-- intent was one ACTIVE row per unit with a retained history of prior
-- (inactive) configs, not a hard one-row-per-unit rule. The constraint itself
-- was the bug.
--
-- Expand-Migrate-Contract:
--   1. EXPAND: add a new constraint enforcing "at most one ACTIVE row per
--      unit" alongside the old one-row-per-unit-period constraint. Always
--      safe to add -- unit_id is currently unique on its own, so this is
--      trivially satisfied by 100% of existing rows.
--   2. CONTRACT: drop the old unit_id-alone constraint, once the new one is
--      confirmed present. This is the step that actually unblocks
--      updateUnitApplicationBillingConfig's deactivate-then-insert design.
--
-- Self-caught during live verification: a first attempt used a plain
-- composite UNIQUE(unit_id, status) constraint. That is WRONG -- it also
-- limits each unit to at most one INACTIVE row total, which breaks history
-- across a 3rd update (2nd historical/inactive row for the same unit
-- collides with the 1st). Caught live by testing a second consecutive
-- update() call and confirming a real duplicate-key failure. The correct
-- shape is a PARTIAL unique index scoped to status='A' only, which allows
-- unlimited historical (inactive) rows per unit while still guaranteeing at
-- most one active row -- verified live via 2 consecutive updates producing 3
-- generations of the same unit's config (2 inactive, 1 active), and a direct
-- attempt to insert a 2nd active row for the same unit correctly rejected.
--
-- No data migration needed -- this is a pure constraint swap, not a data
-- transformation; every existing row already satisfies the new index.

ALTER TABLE public.t_unit_application_billing_configuration
  DROP CONSTRAINT IF EXISTS t_unit_application_billing_configuration_un;

CREATE UNIQUE INDEX IF NOT EXISTS t_unit_application_billing_configuration_active_un
  ON public.t_unit_application_billing_configuration (unit_id)
  WHERE status = 'A';
