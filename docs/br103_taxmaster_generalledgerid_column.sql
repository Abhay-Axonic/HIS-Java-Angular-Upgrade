-- BR-103: taxMaster's list (and, transitively, Save/Update, which re-read the
-- list) is completely broken on real, unmodified legacy. TaxDao.getAll
-- (legacy/Pharmacy/src/main/java/com/param/pharmacy/master/global/dao/tax/TaxDao.java:23-29)
-- runs an HQL query ("SELECT tax FROM Tax AS tax ...") against the Tax entity,
-- which maps @Column(name = "general_ledger_id") (legacy/Entity/.../Tax.java:43-44),
-- but that column does not exist on public.m_tax -- confirmed via psql \d AND
-- independently confirmed absent from the tracked DDL source of truth
-- (legacy/Database/HIS-SME-DDL_20210430_150255584.sql:67242-67255), same class
-- as BR-101 (tariffMaster) and BR-102 (bedMaster) -- not local-dev-only drift.
--
-- Additive only, per Expand-Migrate-Contract. Nullable so no existing row is
-- affected; a NULL general_ledger_id simply means "no ledger mapped yet," which
-- is already how every other optional FK-shaped column on this table's
-- neighbors is treated project-wide.

ALTER TABLE public.m_tax
  ADD COLUMN IF NOT EXISTS general_ledger_id integer;
