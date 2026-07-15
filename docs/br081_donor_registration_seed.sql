-- BR-081: Blood Bank Donor Registration E2E test -- master-data gap closure
--
-- public.m_prefix_configuration had ZERO rows system-wide (verified: `select count(*) from
-- public.m_prefix_configuration` = 0, across ALL 93 m_screen rows with is_prefix_required=true,
-- not just this one). Donor Registration's save flow (DonorRegistrationServiceImpl.saveDonorRegistration
-- -> GlobalUnitMasterDaoImpl.getGeneratedNo(0, unitId, DONORNUMBER=146)) silently returns null when
-- no matching row exists for a screen_id (no exception -- just `return null;`), so every donor saved
-- with donor_number=NULL and the success growl literally read "Donor null Saved Successfully".
-- Confirmed live before this fix (blood_bank.t_donor_registration donor_id=2, donor_number='').
--
-- Seeds the ONE minimal row needed to unblock this screen (screen_id=146, "Donor Number", code='DN',
-- is_prefix_required=true in m_screen -- pre-existing, not touched here). reset_id/format_id/length
-- are NOT referenced anywhere in getGeneratedNo()'s actual code path (traced live) and have no FK
-- constraints in the DB, so safe placeholder values are used; only running_number/generated_code
-- matter functionally. generated_code follows the documented placeholder-substitution convention
-- ([SF] = running number) and the screen's own 'DN' code, matching how other screens' running-number
-- templates are named elsewhere in the app.
--
-- The other 92 screens using this same mechanism remain unconfigured -- flagged for a future
-- dedicated session, out of scope here (this task only needed screen_id=146 to make Donor
-- Registration work at all).

BEGIN;

INSERT INTO public.m_prefix_configuration
    (organization_id, unit_id, screen_id, running_number, reset_id, length, format_id, code,
     is_active, updated_by, updated_date, added_by, added_date, is_deleted, generated_code)
VALUES
    (1, 1, 146, 1, 1, 6, 1, 'DN',
     true, 1, now(), 1, now(), false, 'DN-[SF]');

COMMIT;
