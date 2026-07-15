-- BR-083: master-data gap closure for the Procurement
-- Local Purchase Request E2E write-operation test.
-- Genuine pre-existing gap, same class as BR-081's m_prefix_configuration finding:
-- public.m_prefix_configuration had exactly 1 row total (screen_id=146/Donor Number,
-- seeded by BR-081) and zero rows for screen_id=5 (Local Purchase Request). Without a
-- config row, GlobalUnitMasterDaoImpl.getGeneratedNo() returns null for the purchase
-- number, and procurement.t_purchase_request.purchase_no is NOT NULL -- so every save
-- would fail with a DataIntegrityViolationException regardless of any other data being
-- correct. Confirmed via psql before writing the test, not discovered by trial and error.
-- Run as a single transaction, all-or-nothing.

BEGIN;

INSERT INTO public.m_prefix_configuration (
    organization_id, unit_id, screen_id, running_number, reset_id, length,
    format_id, code, is_active, updated_by, added_by, is_deleted, generated_code
) VALUES (
    1, 1, 5, 1, 1, 6,
    1, 'LPR', true, 1, 1, false, 'LPR-[SF]'
);

COMMIT;
