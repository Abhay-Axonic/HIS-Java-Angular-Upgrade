-- BR-111: capture of the ad-hoc master seeds applied during the 2026-08 verification
-- session — patient registration types + the donor screening questionnaire placeholders.
-- Scope: patient.m_patient_registration_type_master, blood_bank.m_donor_screening_master.
-- Discipline (same as BR-079/081/086): only real, standard, structurally-necessary values are
-- inserted. No specific clinical or business-judgment value is fabricated — and crucially,
-- the placeholder rows below are EXPLICITLY FLAGGED as placeholders:
--   * registration types 1 ('TBD-VERIFY-1') and 7 ('TEST-ARTIFACT-DO-NOT-USE') need owner
--     review before any real use — type 7 is referenced ONLY by E2E test rows (148 patient
--     rows, "E2E WalkIn Customer" / UHID-001). Type 4 ('Unknown Patient') is evidenced from
--     real rows (168) and is NOT a placeholder.
--   * ALL THREE donor-screening questions are 'TBD-VERIFY' placeholders — the real donor
--     screening questionnaire is a CLINICAL DECISION and has not been made.
-- organization_id=1, unit_id=1, created_by/updated_by=1 (the BR-070/076 convention).
-- NOT idempotent (plain INSERT, no ON CONFLICT — the BR convention). These rows are ALREADY
-- APPLIED to the local HIS-SME — this script is for REBUILD/REPLAY only, NOT for running now.
--
-- COLUMN-SOURCE NOTE (corrected from the first draft): the column lists below come from the
-- owner's live \d output, NOT from convention or the entity classes. patient
-- .m_patient_registration_type_master is a 2026 project-generated table with NO entity class
-- in the backend tree (the only reference is the DAO's raw SQL, PendingRequestDaoImpl
-- :439 "from patient.m_patient_registration_type_master master") — its \d is exactly
-- (id integer NOT NULL PK, reg_type varchar) with NO audit columns and NO sequence default
-- on id. The first draft's audit/org/unit columns and its "patient.seq family" sequence
-- speculation were wrong; explicit ids are MANDATORY for this table, not a precaution.
-- blood_bank.m_donor_screening_master's \d (owner) matches DonorScreeningMaster.java's
-- @Column set exactly (id, parameter, deferred_type_id, gender_id, created_by, created_date,
-- status, updated_by, updated_date, unit_id, organization_id); its id has the
-- blood_bank.seq_m_donor_screening_master default, so id is omitted from the INSERT.
BEGIN;

-- ============================================================
-- patient.m_patient_registration_type_master
-- Columns (owner's \d): id integer NOT NULL PK, reg_type varchar. NO audit columns,
-- NO org/unit, NO sequence default — explicit ids are mandatory.
-- ============================================================
INSERT INTO patient.m_patient_registration_type_master (id, reg_type)
VALUES
  (1, 'TBD-VERIFY-1'),             -- PLACEHOLDER — the real default type; 2352 patient rows reference it
  (4, 'Unknown Patient'),          -- EVIDENCED from real rows — not a placeholder
  (7, 'TEST-ARTIFACT-DO-NOT-USE'); -- PLACEHOLDER — referenced ONLY by E2E test rows

-- 56 patient rows have NULL registration_type_id (a data-quality item for the owner; not
-- addressable from a seed script).

-- ============================================================
-- blood_bank.m_donor_screening_master
-- Columns (owner's \d = DonorScreeningMaster.java @Column set): id, parameter varchar(50),
-- deferred_type_id, gender_id, created_by, created_date, status, updated_by, updated_date,
-- unit_id, organization_id. id default: blood_bank.seq_m_donor_screening_master (omitted).
-- deferred_type_id: 1 = temporary deferral, 2 = permanent deferral (the legacy radio split,
-- donorScreeningWorklistDetailsController.js:123-155).
-- gender_id resolved by subselect where a natural key exists — the gender master's natural
-- key is gender_code, whose values are NOT verified in this session, so the owner-verified
-- literal 1 is used instead (the deviation note in the original report still applies).
-- ============================================================
INSERT INTO blood_bank.m_donor_screening_master (parameter, deferred_type_id, gender_id, created_by, created_date, status, updated_by, updated_date, unit_id, organization_id)
VALUES
  ('TBD-VERIFY: recent fever or illness?',   1, 1, 1, now(), 'A', 1, now(), 1, 1),  -- PLACEHOLDER — clinical decision pending
  ('TBD-VERIFY: medication in last 72h?',    1, 1, 1, now(), 'A', 1, now(), 1, 1),  -- PLACEHOLDER — clinical decision pending
  ('TBD-VERIFY: hepatitis or HIV history?',  2, 1, 1, now(), 'A', 1, now(), 1, 1);  -- PLACEHOLDER — clinical decision pending

COMMIT;
