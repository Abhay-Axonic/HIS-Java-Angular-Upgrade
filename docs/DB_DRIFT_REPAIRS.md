# DB Drift Repairs — reproducible DDL

Local-DB alignment repairs applied to `his_local` on **2026-08-05** while verifying the
clinical-leak fixes on `fix/drug-history-and-clinical-leaks`. These are **local-DB alignment only —
not code, not schema changes to any other environment**. They restore the local database to what the
authoritative deployment snapshot (`legacy/Deployment/HIS-DDL-20211019_124148463PM`) already defines.

Anyone rebuilding this environment from the repo must apply both repairs before the consult-doctors
endpoint (`POST /api/opd/consultDoctorListByAdmission`) will execute — without them it returns 501
(`relation "public.t_outside_hospital_doctor_mapper" does not exist`, then
`column consultdoc0_.outside_hos_doc_id does not exist`).

## Repair 1 — missing table `public.t_outside_hospital_doctor_mapper`

Expected by `OutsideHospitalDoctorMapper` (`@Table(name = "t_outside_hospital_doctor_mapper", schema = "public")`)
and joined by the ConsultDoctor queries. Source: `HIS-DDL-20211019_124148463PM` (CREATE TABLE verbatim).

```sql
CREATE SEQUENCE IF NOT EXISTS public.t_outside_hospital_doctor_mapper_seq;
CREATE TABLE IF NOT EXISTS public.t_outside_hospital_doctor_mapper (
    outside_hos_doc_id integer DEFAULT nextval('public.t_outside_hospital_doctor_mapper_seq'::regclass) NOT NULL,
    outside_hospital_id integer NOT NULL,
    hosp_doctor_name character varying,
    organization_id integer,
    unit_id integer,
    created_date timestamp without time zone DEFAULT now(),
    created_by integer,
    updated_date timestamp without time zone DEFAULT now(),
    updated_by integer,
    status character(1) DEFAULT 'A'::bpchar
);
```

(Order matters: the sequence must exist before the table, since the column default references it.)

## Repair 2 — missing column `outside_hos_doc_id` on `opd.t_consult_doctor`

Expected by `ConsultDoctor` (`@Column(name = "outside_hos_doc_id")` + the
`@JoinColumn(name = "outside_hos_doc_id")` many-to-one to `OutsideHospitalDoctorMapper`).
Source: `HIS-DDL-20211019_124148463PM` (the `opd.t_consult_doctor` CREATE TABLE includes the column).

```sql
ALTER TABLE opd.t_consult_doctor ADD COLUMN IF NOT EXISTS outside_hos_doc_id integer;
```

## Verification

After applying both: `POST /api/opd/consultDoctorListByAdmission` returns 200 (empty list for a
non-existent admission) instead of 501.

## Known test data (left in place per owner instruction, 2026-08-05)

Seeded on `his_local` for pre-fix leak capture + re-runnable verification:

- `opd.t_consult_doctor` id 1: org 999 / unit 999, admission_id 999999, patient_id 999999, status 'A' (victim).
- `patient.t_patient_registration` id 999999: org 999 / unit 999, 'Victim'/'Consult999' (join prerequisite).
- `patient.t_death_registration` id 1: org 999 / unit 999, 'Victim'/'DeathReg999', age 45, gender 1 (victim).
- `patient.t_death_registration` id 3: org 1 / unit 1, 'Victim'/'DeathReg999' — **test artifact** from the
  post-fix verification (the check passed with no same-tenant match, the save inserted, the downstream
  errored). It is what makes a same-name dup-check call return 409 — correct same-tenant duplicate detection.
- `patient.t_death_registration` id 4, 5, …: org 1 — test artifacts from subsequent verification runs

- `opd.t_medication_history` ids 800001-800003: org 1 / unit 1, **patient_id 1** ("Test Patient"),
  Paracetamol 500mg / Amoxicillin 250mg / Pantoprazole 40mg (+ generic names, frequency 1, status 'A'),
  seq `opd.t_medication_history_seq` setval to 800000 — **Step-6 seeding (2026-08-05)** so the coversheet
  Drug History section is visually verifiable (the coversheet loads them via POST
  /api/opd/getMedicationHistoryList). Idempotent (explicit ids + ON CONFLICT DO NOTHING). To see the
  empty state again: `DELETE FROM opd.t_medication_history WHERE medication_history_id BETWEEN 800001 AND 800003;`
  (re-run the INSERT to restore).

  (unique-name checks; each run's save inserts before the downstream error).
