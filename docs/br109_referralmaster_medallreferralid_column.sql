-- BR-109: adt.m_referral_master is missing medall_referral_id, already mapped by
-- legacy/Entity/src/main/java/com/param/adt/master/global/model/ReferralMaster.java.
-- Already known, already scripted in docs/br076_missing_columns.sql (line 11);
-- confirmed known_local_dev_drift=True in docs/schema-drift-sweep/missing_column_mismatches.tsv.
-- Purely additive, nullable, no existing data touched.
ALTER TABLE adt."m_referral_master" ADD COLUMN IF NOT EXISTS "medall_referral_id" int4;
