-- BR-134: public.m_user_master is missing 1 column the JPA entity
-- (UserMaster.java) declares -- confirmed absent from the local dev DB,
-- same class as BR-076/101/103/104/128/131/132 -- not local-dev-only drift.
--
-- Found while live-verifying the UserMasterDaoImpl.updatePassword
-- cross-tenant IDOR fix (see fix/usermaster-updatepassword-crosstenant-idor):
-- findById(userId) threw a live PSQLException on this missing column before
-- this fix could even be tested.
--
-- UserMaster.java's declared mapping:
--   subscriptionDate -> subscription_date, String   (line 448-449)
--
-- Additive only, per Expand-Migrate-Contract. Nullable, no existing row
-- affected.

ALTER TABLE public.m_user_master
  ADD COLUMN IF NOT EXISTS subscription_date character varying(50);
