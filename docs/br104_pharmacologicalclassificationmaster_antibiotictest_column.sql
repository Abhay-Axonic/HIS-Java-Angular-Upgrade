-- BR-104: pharmacologicalClassificationMaster's list (and, transitively,
-- Save/Update, which re-read it) is completely broken on real, unmodified
-- legacy. PharmacologicalClassificationDao.getAll
-- (legacy/Pharmacy/src/main/java/com/param/pharmacy/master/unit/dao/pharmacologicalClassification/PharmacologicalClassificationDao.java)
-- runs a Criteria query against the PharmacologicalClassification entity,
-- which maps @Column(name = "antibiotic_test")
-- (legacy/Entity/.../PharmacologicalClassification.java:32-33), but that
-- column does not exist on public.m_pharmacological_classification --
-- confirmed via psql \d AND independently confirmed absent from the tracked
-- DDL source of truth (legacy/Database/HIS-SME-DDL_20210430_150255584.sql:
-- 67628-67640), same class as BR-101/BR-102/BR-103 -- not local-dev-only
-- drift.
--
-- Additive only, per Expand-Migrate-Contract. Nullable boolean, matching the
-- entity's Boolean (not primitive boolean) field type -- NULL is a valid,
-- distinct "not yet classified" state, not forced to false.

ALTER TABLE public.m_pharmacological_classification
  ADD COLUMN IF NOT EXISTS antibiotic_test boolean;
