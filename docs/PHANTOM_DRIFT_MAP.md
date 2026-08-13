# PHANTOM DRIFT MAP — schema-code divergence at system scale (DQ-054, owner decision OPTION C)

Owner framing (2026-08-12, DQ-054 decision): *"371 missing columns across 177
tables is not a defect list, it is a SCHEMA-CODE DIVERGENCE AT SYSTEM SCALE.
Six phantoms found one at a time looked like sloppiness; 371 is a different
fact about this system. It means most remaining transaction writes are dead on
arrival, and it means the two flows that worked did so by luck of which tables
they touched."* Option C adopted: park-and-skip per flow autonomously; this
map is the sizing artifact for the BACKEND OWNER (the DDL/entity reconciliation
is a backend decision with its own maker-checker — NOT agent-applied).

## Method

`tools/phantom_drift_sweep.py` (2026-08-12): every entity under
`legacy/Entity/src/main/java` (@Table + @Column declarations) diffed
case-insensitively against his_local `information_schema`. A "phantom column"
= the entity maps a column the DDL lacks; a "phantom table" = the entity maps
a relation absent from every schema. Regenerate with
`python3 tools/phantom_drift_sweep.py [--admission-only]`.

ACCURACY / CONFIDENCE SPLIT (2026-08-12, re-run with comment-stripping): the
sweep is a SCREENING tool. The earlier 371/177 included ~15 commented-out
@Column artifacts (e.g., m_blood_bank_status_master.name — the column EXISTS
in DDL; the flag came from a commented-out annotation in the entity source).
**WHAT IS ACTUALLY WRONG — READ FIRST (2026-08-13)**: THE 3:0 RATIO — the
seed (tools/seed_number_config.py) has unblocked THREE flows (donor
registration, walk-in sale, deposit — all blocked by the empty number
configuration) and code changes have unblocked NONE. Part of what looks like
a broken system is an environment nobody finished setting up, and the
seeding is the instrument that tells it apart from the real drift. Then: 11
CONFIRMED phantoms (live endpoint errors — the real drift), and the bands
below (the screening set).

Comment-stripped totals and bands (all case-insensitive):

- TOTAL flagged: 356 columns across 177 tables (354 after 2026-08-13
  corrections: +1 promoted, -1 artifact removed, -1 comment artifact).
- CONFIRMED (reproduced by a live endpoint error): 11 — 10 columns of the 356
  (technique_id, is_bed_charges_service, form_template_id, icpc_id,
  package_applicable_date, family_id, appointment_unique_id, cash_in_out_id,
  cancel_reason_id, medication_type)
  + the phantom TABLE t_message_queue (the 11th instance, outside the column
  count). cash_in_out_id promoted PROBABLE->CONFIRMED 2026-08-13 (cash
  handover smoke: live SQL error, honest 500, full rollback).
  cancel_reason_id (billing.m_deposit_master) + medication_type
  (pharmacy.t_prescription) promoted PROBABLE->CONFIRMED 2026-08-13 — the
  DQ-067 number-config seed unblocked the deposit + walk-in writes and they
  hit the real columns (live SQLGrammarExceptions).
- PROBABLE (entity declares it; ZERO case-insensitive matches anywhere in the
  DB): 177 (was 179; cash_in_out_id + cancel_reason_id + medication_type
  promoted out).
- SUSPECT (name exists on ANOTHER table — plausible typo/mapping artifact,
  e.g. orgnisation_id vs organisation_id; unverified): 175 (was 176;
  cross_match_register_id removed — DDL-present artifact: the column EXISTS
  on the flagged table, 3 matches anywhere).

The backend owner should size CONFIRMED + PROBABLE as near-certain work
(188) and treat SUSPECT (176) as the verification queue: each SUSPECT is
name-exists-somewhere (a probable typo/mapping artifact) and the band count
above is now 175 after the cross_match_register_id removal.

CALIBRATION HONESTY (2026-08-13): the PROBABLE band is LARGELY UNTESTED BY
THE SMOKE METHOD. The park-sweep smokes die BEFORE touching the flagged
columns in most cases (contract-400s on nested DTOs — deposit/walk-in; the
inverted admission gate; the I/O-chart LocalTime NPE; the stock-gated blood
issue) — "zero disconfirmations" is STRUCTURAL (the smokes rarely reach the
SQL), NOT calibration evidence. No claim of confirmation follows from it.
PROBABLE-REACH EVIDENCE (2026-08-13): the FIRST two PROBABLE entries reached
by a live write BOTH confirmed — cancel_reason_id (m_deposit_master) and
medication_type (t_prescription) promoted the moment the DQ-067 seed let the
deposit and walk-in writes reach SQL. That establishes the band is REAL, not
regex noise — but two of 179 is a SAMPLE, not a proof. The band remains
largely untested, and the per-table DDL diff is still the cheap instrument:
a column the entity declares and the DDL lacks case-insensitively anywhere
is PROBABLE by construction — each row in the band is the diff result, no
working write path needed. Live confirmation (a real endpoint error) is
reserved for the CONFIRMED band.

THE 3:0 RATIO (2026-08-13) — the sharpest statement of what is actually
wrong: the seed has unblocked THREE flows (donor registration, walk-in
sale, deposit — all three blocked by the empty number configuration) and
code changes have unblocked NONE. Some of what looked like a broken backend
was an unseeded environment; some of it is real drift (the two CONFIRMED
promotions appeared the moment the seeding let the writes reach SQL); the
seeding is what separates them.
resolves it. Use the sweep to SIZE and PRIORITIZE; use live calls to CONFIRM.

## Confirmed live instances (8) — the class as observed, not inferred

| # | Kind | Table | Column/Relation | Blocks (live-verified 2026-08-12) |
|---|------|-------|-----------------|-----------------------------------|
| 1 | column | billing.t_package_consumption_master | package_applicable_date | saveBill (OPD Bill, parked BR-035) |
| 2 | column | (technique_id — lab m_parameter_master) | technique_id | lab parameter flows (prior sessions) |
| 3 | column | (is_bed_charges_service — public.t_auto_posting_services_cofiguration) | is_bed_charges_service | auto-posting config (prior) |
| 4 | column | (form_template_id — adt.m_consent_master) | form_template_id | consent/template flows (prior) |
| 5 | column | (icpc_id — doctor.m_orderset_master) | icpc_id | orderset list feed (DQ-031) |
| 6 | column | patient.t_patient_details | family_id | **patientRegistration save — masked 501, DQ-053** |
| 7 | column | opd.m_appointment_master | appointment_unique_id | **scheduleAppointment save — masked 501, DQ-054** |
| 8 | table | public.t_message_queue | (whole relation) | **scheduleAppointmentList feed — masked 501, DQ-055** |
| 9 | column | public.t_denomination_details | cash_in_out_id | **cash handover save — honest 500 + full rollback (atomic), DQ-062** |

The live failures all share the same shape: Hibernate flush → SQLGrammarException
→ an empty/generic catch → HTTP 200 `{"status":"error","code":"501",
"message":"Something went wrong!!"}` with nothing landed.

## Scale (case-insensitive, 2026-08-12)

- **371 entity-declared columns missing from DDL across 177 existing tables**.
- 1 phantom TABLE (t_message_queue). Phantom-COLUMN vs phantom-TABLE are the
  same class (a save/read dies at flush/execution); only the granularity differs.
- Per-family distribution (columns / tables / known-write tables):

| Family (schema) | Missing cols | Tables | Known write-path tables | Sample write tables hit |
|---|---|---|---|---|
| lab | 49 | 21 | 2 | t_lab_sample_dtls (7), t_lab_sample_master, t_lab_test_result (5) |
| public | 49 | 24 | 2 | t_order_master (2), t_denomination_details, t_template_transaction (8) |
| billing | 46 | 28 | 6 | t_bill_payee_details (4), m_deposit_master (4), t_doctor_share (2), t_doc_share_voucher, t_payment_reciept_details |
| pharmacy | 40 | 12 | 7 | **t_sale (12)**, t_sale_detail (6), t_prescription_detail (8), t_prescription, t_issue, t_sale_payment_mode, t_patient_issue_return |
| opd | 29 | 12 | 3 | m_appointment_master, t_encounter_master (3), t_vaccine_immunization_mapper (5) |
| otims | 29 | 10 | 0 | t_bill_of_material (6), t_surgery_resource_config (4), t_ot_completion_* |
| inventory | 21 | 12 | 0 | t_stock_transaction (7), t_material_issue_detail, t_opening_balance_detail |
| procurement | 21 | 13 | 0 | t_new_work_order_creation (5), t_purchase_order_detail (3) |
| service | 20 | 9 | 0 | t_package_services_details (7), m_package_master (5) |
| doctor | 18 | 7 | 0 | t_doctor_favorites (10), m_speciality_master |
| patient | 18 | 12 | 1 | **t_patient_details (family_id)** |
| emr | 14 | 5 | 0 | m_vital_master (3), m_diagnosis_master (5) |
| adt | 11 | 8 | 2 | t_admission (ot_status), t_admission_details (3) |
| cssd/abdm/assets/equipment | 6 | 4 | 0 | m_sterilization_type_master (3), m_abdm_credential |

## Write-path ranking (what a rebuild touches — the backend owner's sizing input)

Ranked by drift size × known write role (confirmed write tables from this
session's controller/DAO reads and the candidate flows):

1. **pharmacy.t_sale — 12 columns** — blocks the pharmacy SALES flow
   (walk-in sale, bill, refunds): cancelled_by_id, cancelled_reason_id,
   cancelled_remark, cop, is_from_narcotics, is_from_patient_indent,
   is_from_pending_issue, is_virtual_store, issue_id, received_from,
   screen_id, search_patient.
2. **pharmacy.t_prescription_detail — 8** — blocks prescription entry:
   cov_template_id, duration, duration_unit, infusion_total_volume,
   is_additive, is_manually_added_item, is_solution, priority_id.
3. **pharmacy.t_sale_detail — 6** — sales lines: at_cop, grn_id, grn_number,
   is_dummy_service, itemNetAmount, old_discount_percentage.
4. **lab.t_lab_sample_dtls — 7** — lab sample collection/result entry:
   icmr_id, internal_notes, is_on_hold_parameter, is_report_recall,
   manual_sample_collection_date_time, provisional, srf_id.
5. **lab.t_lab_test_result — 5** — result entry/release: createdDate,
   manual_release_time, manual_result_enter_time, manual_validation_time, status.
6. **billing.t_bill_payee_details — 4** — bill payee/payment splits:
   cr_net_amt, paid_amount, payment_entitlement_id, priority_id.
7. **billing.m_deposit_master — 4** — deposit/cash flows: cancel_reason_id,
   cancel_reason_note, cancelled_by, pharmacy_sale_mode_id.
8. **opd.t_encounter_master — 3** — encounter create (OPD/IPD):
   advised_by_id, advised_by_name, nursing_station_id.
9. **adt.t_admission_details — 3** — inpatient admission: admission_date_time,
   diet_type, remark_for_diet.  **adt.t_admission — 1** (ot_status).
10. **billing.t_doctor_share / t_doc_share_voucher / t_payment_reciept_details —
    2/1/2** — the doctor-share billing family.
11. **opd.t_vaccine_immunization_mapper — 5** — immunization administration.
12. **public.t_denomination_details — 1** (cash_in_out_id) — cash-desk
    shift-closing chain. **public.t_order_master — 2** (visit_adm_id,
    ward_id) — the registration auto-order.

## Clean surfaces (zero drift on write tables) — buildable now

From the sweep (tables with NO missing entity columns) plus the existence
check, the transaction flows whose PRIMARY WRITE TABLES are clean. VERIFIED
BUILDABLE by live T0 smoke (2026-08-12/13) — smoke-passing surfaces, NOT map
assumptions:

- **Blood bank — donor registration**: t_donor_registration, t_donor_details,
  t_donor_screening_details — ALL clean. T0 smoke PASS (exit 0). THIRD
  SIGNED FLOW MAP (T2–T6 + browser golden trace, merge-ready).
- **Cash desk — shift-closing (600)**: the saveShiftCloseBalance write lands
  billing.m_shift_close_balance (entity inversion: ShiftClosingMaster maps
  the BALANCE table; DQ-059). T0 smoke PASS (exit 0, id-verified, teardown
  clean) — the SECOND verified buildable flow. The cash-handover write
  (t_denomination_details, cash_in_out_id flag) is a separate smoke pending
  (batch 2) — the screen's full buildability is decided by that smoke.
- **Transfer (65)** — done (t_transfer/t_transfer_request/t_bed_log_status/
  m_bed_master clean).
- **Loyalty card (752)** — done.
- **Nursing order master** — m_nursing_order_master clean (13/13, README-verified).
- **Master close-out screens** — clean (why the master phase succeeded).
- Partial: patient registration's patient row (t_patient_registration clean)
  but the flow is blocked by t_patient_details.family_id (DQ-053).
- Read-side clean where the write is blocked: patient search feed works
  (patient-registration README), encounter search works; the appointment list
  is blocked by the phantom TABLE t_message_queue (DQ-055 — not even read-only).

## Read-only candidates (write drift-blocked, reads clean) — deliverable 4

Per Option C rule "a blocked write does not block a list": screens whose reads
work but whose writes wait on the backend — patient registration (search
works; save parks DQ-053), encounter (search works; save patient-gated +
drift), appointment scheduling (list blocked by t_message_queue — NOT
read-only until that table lands). The read-only classification is per-screen
at T0, logged in the parks batch.

## How this document is maintained

- Regenerate the table with `tools/phantom_drift_sweep.py` after any DDL or
  entity change.
- Each autonomous park appends its table+column to the confirmed-instances
  table (or a new row) and the flow's DQ entry.
- The backend owner sizes the reconciliation from the write-path ranking; the
  agent never applies DDL (Rule 10, Expand-Migrate-Contract).
