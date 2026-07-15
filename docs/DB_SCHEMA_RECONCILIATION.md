# DB Schema Reconciliation — @Entity vs Local Dev Postgres

**Date:** 2026-07-08
**Scope:** Every JPA `@Entity`/`@Table` class in `legacy/Entity/src/main/java` and `legacy/Global/src/main/java`, reconciled against every table in the local dev Postgres DB (`localhost:5434`, DB `HIS-SME`, all 26 schemas).

**Why this exists:** three separate missing-table surprises during login-flow testing alone (BR-038, BR-039, and one resolved inline mid-BR-039) made it clear the gap between the entity layer and the local dev DB was systemic, not isolated. This is a one-time comprehensive reconciliation replacing the ad hoc one-at-a-time discovery pattern. It does **not** touch any tracked DDL script, any `.java` file, or any application code — it is local-dev-database-only work, the same class of action already owner-approved in BR-038/BR-039.

## Method

1. Parsed every `.java` file under the two module roots containing `@Entity`, extracting its `@Table(name=..., schema=...)` value via a script (not manual reading — 1,380 files).
2. Queried `information_schema.tables` across all 26 non-system schemas in the local DB (1,866 tables/views existed before this session).
3. Diffed the two sets. **182 @Entity-declared tables were missing from the DB entirely.**
4. Triaged all 182 into HIGH vs LOW priority (see criteria below) using two objective signals, not guesswork:
   - **Module relevance**: is the table's schema one of the five near-term test flows (hospital setup, user/role management, OPD/IPD consultation, pharmacy, billing)?
   - **Live wiring**: is the entity class actually referenced by a `Dao`/`Service`/`Controller`/`Repository` class anywhere else in the codebase (not just its own file)? An entity with zero such references is very likely dead/speculative scaffolding regardless of which schema it claims.
5. Created all 88 HIGH-priority tables in the local dev DB — DDL derived from each entity's real `@Column`/`@JoinColumn`/`@SequenceGenerator` mappings, cross-checked against the DAO/Service code that actually queries the table (catching cases where raw SQL needs a type the entity doesn't obviously imply — the same class of check BR-039 used to find `bytea`/`pgp_sym_encrypt` columns), and matched to each schema's real sibling-table conventions. One sensible seed row per table, sourced from `ICommonConstants.java` where a value should be a known application constant rather than an invented string.
6. Left all 94 LOW-priority tables uncreated — listed in full (Appendix B) with the reason each was deferred.
7. Checked the reverse direction: DB tables with no matching `@Entity` anywhere in the two scanned modules (755 orphans) — listed only, per scope, not investigated or acted on.

**This work was parallelized across 4 independent agents** (one per schema cluster), each independently reading entity files, checking sibling conventions, verifying against live DAO/Service usage, and executing DDL. Their findings are consolidated below; independently verified afterward via a full re-run of the same reconciliation diff (see Verification section).

## Priority criteria (exact rule applied)

- **LOW automatically**, regardless of wiring: `abdm`, `procurement`, `assets`, `blood_bank`, `otims`, `inventory`, `lab`, `pharman` schemas — all outside the five named near-term flows. (`abdm` = government ABDM/Ayushman Bharat integration, the exact example the task brief called out as deferrable; `pharman` is a real but distinct third-party drug-reference schema, not a typo of `pharmacy` — see note in Appendix B.)
- **LOW**: not referenced by any `Dao`/`Service`/`Controller`/`Repository` class outside the entity's own file — treated as dead/speculative scaffolding.
- **LOW**: wired and in an otherwise-relevant schema, but the table itself is peripheral/administrative (MRD physical-file-location tracking, NABH accreditation, 2FA/LDAP/WhatsApp integration, HR surveys, support ticketing, chat, UI button config, etc.) — doesn't serve any of the five named flows.
- **HIGH**: everything else — wired, in a relevant schema (`adt`, `opd`, `patient`, `doctor`, `emr`, `pharmacy`, `billing`, `service`, or the directly-relevant subset of `public`), and directly supports one of the five named flows.

## Summary

| | Count |
|---|---|
| Total `@Entity` classes scanned | 1,380 |
| Present in DB already | 1,198 (entity records) / 1,111 distinct tables |
| **Missing from DB entirely** | **182** |
| — HIGH priority (created this session) | **88** |
| — LOW priority (deferred, listed only) | **94** |
| Orphan tables (in DB, no matching `@Entity`) | **755** |

### HIGH-priority tables, by schema (all 88 now exist in the local DB — verified)

| Schema | HIGH tables | Outcome |
|---|---|---|
| `adt` | 27 | 27 created |
| `public` | 22 | 20 created, 2 already existed (`m_nurse_shift_allocation`, `m_nurse_shift_allocation_details` — see whitespace-bug note) |
| `billing` | 12 | 12 created |
| `opd` | 7 | 7 created |
| `patient` | 6 | 6 created |
| `service` | 4 | 3 created, 1 already existed (`t_package_version`, — see whitespace-bug note) |
| `pharmacy` | 4 | 4 created |
| `emr` | 3 | 2 created, 1 already existed (`m_system_master_gender_mapper` — see whitespace-bug note) |
| `doctor` | 3 | 3 created |
| **Total** | **88** | **84 net-new tables + 4 confirmed pre-existing** |

**Post-creation verification**: re-ran the full entity-vs-DB diff after all 4 agent batches completed. **0 of the 88 HIGH-priority tables are still missing.** Total DB table count went from 1,866 to 1,950 (+84 net-new tables), consistent with 88 HIGH-priority tables minus the 4 that turned out to already exist under a whitespace-trimmed name.

## A pre-existing source bug this reconciliation surfaced: `@Table` name whitespace

Seven `@Entity` classes across the whole codebase (not just the missing set) have a **literal leading or trailing space** baked into their `@Table(name = "...")` string — a real bug in the Java source, not a formatting artifact of this report:

| Entity class | File | Literal (buggy) name |
|---|---|---|
| `SystemMasterGenderMapper` | `legacy/Entity/src/main/java/com/param/global/model/SystemMasterGenderMapper.java:17` | `" m_system_master_gender_mapper"` (leading space) |
| `NurseShiftAllocation` | `legacy/Entity/src/main/java/com/param/global/model/NurseShiftAllocation.java:204` | `"m_nurse_shift_allocation "` (trailing space) |
| `NurseShiftAllocationDetails` | `legacy/Entity/src/main/java/com/param/global/model/NurseShiftAllocationDetails.java` | `"m_nurse_shift_allocation_details "` (trailing space) |
| `VisitorPassMapper` | `legacy/Entity/src/main/java/com/param/adt/admission/model/VisitorPassMapper.java:75` | `"t_visitor_pass_mapper "` (trailing space) |
| `PackageVersionMaster` | `legacy/Entity/src/main/java/com/param/service/global/model/PackageVersionMaster.java:14` | `"t_package_version "` (trailing space) |
| `LanguageMaster` | `legacy/Entity/src/main/java/com/param/entity/lis/unit/LanguageMaster.java:97` | `"m_language_master "` (trailing space) |
| `DischargeSummaryTabularTemplateData` | `legacy/Entity/src/main/java/com/param/adt/discharge/model/DischargeSummaryTabularTemplateData.java` | `"m_discharge_summary_tabular_template_data "` (trailing space) |

This bug caused the initial reconciliation scan to flag all 7 of these tables as "missing" (the literal annotated name, with its space, never matches a real Postgres identifier). On investigation, **6 of the 7 already existed in the DB under the correctly-trimmed name** (`m_system_master_gender_mapper`, `m_nurse_shift_allocation`, `m_nurse_shift_allocation_details`, `t_visitor_pass_mapper`, `t_package_version`, `m_language_master`) — these were false positives, left untouched, and seeded a row only where they were part of the HIGH-priority creation batch and genuinely empty. Only **1 of the 7** (`m_discharge_summary_tabular_template_data`) was genuinely missing and has now been created (without the space, matching every raw-SQL call site that queries it).

**Not fixed** — per this project's `try/catch`/behavioral-change discipline, a `.java` annotation edit is an application-code change requiring its own branch, Maker/Checker review, and (if it changes runtime behavior) a manifest entry; out of scope for a local-DB-only session. **Flagged for a future Maker session**: whether Hibernate's identifier-quoting configuration actually preserves or strips this whitespace at runtime determines whether these 7 entities are silently broken in every environment (not just local dev) or harmlessly cosmetic. Recommend the owner have a future session confirm this before deciding whether/how to fix the 7 annotations.

## Other cross-cutting findings surfaced during table creation

- **Billing-accuracy flag (Mission Priority #1)**: `m_corporate_statement_master`'s (`CorporateStatmentMaster.java`) `cons_amount`/`lab_amount`/`pharmacy_amount` fields are typed as Java primitive `int`, not a decimal type, throughout the entity and its DTOs — structurally incapable of holding fractional currency. Pre-existing defect, not touched (would be a behavioral change requiring its own manifest entry and owner sign-off), flagged here for owner visibility given the project's top mission priority is billing accuracy.
- **Real entity/raw-SQL mismatches found and resolved at the DDL level** (table created to match actual usage, not the sometimes-inconsistent entity annotation):
  - `t_custom_history_configuration_details`: entity declares `@Column(name = "specialityId")` (camelCase) but the DAO's raw SQL and every sibling column use `speciality_id` — created as `speciality_id`.
  - `t_order_status_master`: entity types `order_code`/`order_desc` as single-character Java `char`, which cannot hold values like `"PENDING"` — typed `varchar` to match the real sibling table `m_order_status_master`'s convention and `IConsultOrderStatusMasterConstants.java`'s actual string constants instead.
  - `t_free_text_ordered_services`: entity's `@Id` field has no explicit `@Column(name=...)` — resolved via the app's configured Hibernate `ImprovedNamingStrategy` (declared in `spring-dispatcher-servlet.xml`) to `free_text_ordered_services_id`.
  - `t_cash_in_out`: entity types `status` as `String` (every sibling table uses `Character`) but real usage (`CashInOutDaoImpl.java`) always sets a single character — typed `character(1)` to match actual usage.
  - `t_vendor_service_rate_config`: needed a `package_id` column used by the DAO's raw SQL/named queries with **no corresponding `@Column` in the Java entity at all** — added to the DDL since the live query requires it.
- **Real FK relationships confirmed to exist only via raw SQL, not JPA-declared** — per the strict "FK only where the entity declares `@JoinColumn`/`@ManyToOne`" rule applied throughout, these were left unconstrained at the DB level and flagged individually in the per-table detail below, rather than silently added: `t_admission.admission_type_id`/`bed_category_id`, `letter_attachment_mapper.letter_id`/`appointment_id`, `t_staff_speciality_mapper`'s `class_id`, `t_corporate_statement_checklist_visit_type_mapper.corporate_statement_checklist_id`, `t_discharge_template_department_mapper.department_id`/`discharge_summary_temp_id`, `t_nutritional_assessment_score.nutritional_assessment_score_master_id`, and others noted per-table below.
- **Two task-brief FK assumptions turned out to be wrong** after the agent checked actual code, and were correctly not added: `t_pos_machine_response_mapper` does not FK to `m_pos_machine_device_master` (device lookup is by string match, not integer FK); `t_global_service_mapper` does not FK to `m_global_service_master` (parallel flat structures, not parent/child).
- **A genuine unresolved deeper gap**: `t_growth_developmental_milestones_transaction.growth_developmental_milestones_id` and `t_custom_history_configuration_details.custom_history_configuration_mstr_id` both conceptually FK to master tables (`adt.m_growth_developmental_milestones_master`, `emr.m_custom_history_configuration`) that **do not exist anywhere in the local DB at all** — confirmed via each entity's own `@NamedNativeQuery`. Both are outside this session's 88-table batch; flagged for a future reconciliation pass (they weren't in the original 182-table missing list because no `@Entity` class declares them — they're referenced only from raw SQL inside other entities' native queries, meaning the true gap is larger than what an `@Entity`-based scan alone can find).
- **Two verbatim sequence-name irregularities must be preserved exactly**, not "corrected," or Hibernate's runtime sequence lookup breaks: `adt.t_t_nursing_scale_score_seq` (a genuine doubled `t_` prefix in the entity's own `@SequenceGenerator`) and `adt.nutritional_assessment_score_master_seq` (missing the `t_` prefix its table name has). Both confirmed via direct file read, not transcription errors.
- **Several intended FK/reference targets exist but are currently empty tables locally**, limiting how "real" end-to-end testing through those relations can be even though the new tables themselves are now in place: `m_cabin_master`, `m_frequency`, `m_language_master`, `t_template_master`, `adt.m_discharge_summary_field_master`, `adt.m_discharge_summary_template_master`, `billing.m_hospital_accounts`, `doctor.m_sub_speciality_master`, `emr.m_system_master`, `m_billing_bed_category_master`, and the 7 medication-reference masters under `doctor.m_doctor_favorite_medications_master`'s native query (`m_item`, `m_route_of_administration`, `m_generic`, `m_frequency`, `m_strength_unit`, `m_fomulation_dosage_mapper`, `m_instruction`). None of these were invented or force-seeded with fake data; flagged for a future session if deeper end-to-end testing needs them populated.
- **`billing` schema's real FK convention differs from the `public`/`global`-schema convention** BR-038/BR-039 established: most `billing` tables do not FK `unit_id`/`organization_id` at all. The agent matched the schema's actual existing convention rather than blindly reapplying the BR-038/BR-039 pattern — flagged as a deliberate divergence, not an oversight.
- **Additional pre-existing swallowed-exception patterns and one pre-existing broken query** were incidentally found while researching DAOs for this batch (empty `catch (HibernateException he) {}` in `CorporateStatementDaoImpl`; an unresolved SQL alias `fnr` in `POSMachineResponseMapperDaoImpl.getPOSMachineUniqueId`) — out of scope for this DB-only session, flagged for a future exception-handling remediation pass (same category as this project's ongoing BR-006/BR-007/BR-009/BR-010/BR-011 work).
- **A genuine duplicate table-name collision among the entity classes themselves** (found during LOW-priority research, not created): `CoversheetAccessMaster` and `CustomEMRSubCategoryMaster` — two entirely different, unrelated entity classes — both declare `@Table(name = "m_coversheet_access_master", schema = "patient")`. Both are LOW priority (unwired — neither is referenced by any Dao/Service/Controller), so this wasn't a blocker for HIGH-priority creation, but only one table could ever physically exist under that name; flagged for a future session to determine which (if either) is the real intended entity.
- **One master row created as a seeding prerequisite, not itself part of the 182**: `opd.t_admission.reason_for_admission_id` is `NOT NULL` and FKs to `opd.m_reason_for_admission`, a table that already existed but had zero rows — inserted one supporting row (`'Elective Admission'`) into it so the new `t_admission` seed row could satisfy its own FK. Flagged rather than done silently.
- **No table in this reconciliation batch required `pgcrypto`/`bytea` encryption columns** (unlike BR-039's `t_unit_subscription_details`) — confirmed via targeted grep for `pgp_sym_encrypt`/`pgp_sym_decrypt` against every DAO touching these 88 tables.
- No new business terms were invented anywhere in this batch (all seed values are structural placeholders or sourced from `ICommonConstants.java`/`IConsultOrderStatusMasterConstants.java`), so no `docs/GLOSSARY.json` addition is required by this session's work.

## Full per-table creation detail

The complete, table-by-table DDL/seed/flag detail (all 88 tables, as reported by each of the 4 parallel agents) is preserved in this session's transcript and in `docs/SESSION_LOG.md`'s three batch entries (adt; opd/patient/doctor/emr; and the consolidated entry below covering pharmacy/billing/service and public). Highlights needing owner attention are called out by name above rather than repeated in full here, to keep this report navigable; the raw per-table detail (exact DDL, exact seed values) is authoritative in the session log and available on request.

## Reverse direction: orphan tables (see Appendix C for full list)

755 tables exist in the DB with no matching `@Entity` anywhere in `legacy/Entity` or `legacy/Global`. Per task scope, these are listed only — not investigated or acted on. Notable pattern spotted while compiling the list (not deeply investigated): the `homecare` schema (87 tables) has **zero** `@Entity` references anywhere in the two scanned modules at all — either its JPA layer lives in a module outside this scan's scope, or it's queried entirely via raw SQL/a different persistence mechanism. `cssd`, `kitchen`, and `equipment` schemas do have real entity coverage elsewhere in `legacy/Entity` (confirmed via grep — 27, 20, and 6 references respectively), and those entities' tables are already fully present in the DB; their orphan counts (21, 20, 10 respectively) are just genuinely unmapped tables in otherwise-covered schemas, same as every other schema's orphan tail.

---

## Appendix A: Full list of HIGH-priority missing tables (created this session)


### schema `adt` (27 tables)

| Table | Entity class | Source file |
|---|---|---|
| `m_corporate_statement_checklist_master` | `CorporateStatementChecklistMaster` | `legacy/Entity/src/main/java/com/param/adt/master/unit/model/CorporateStatementChecklistMaster.java` |
| `m_discharge_summary_tabular_template_data` | `DischargeSummaryTabularTemplateData` | `legacy/Entity/src/main/java/com/param/adt/discharge/model/DischargeSummaryTabularTemplateData.java` |
| `t_admission_triage_history` | `AdmissionTriageHistory` | `legacy/Entity/src/main/java/com/param/adt/admission/model/AdmissionTriageHistory.java` |
| `t_admitted_patient_orders_workbench` | `AdmittedPatientOrdersWorkbench` | `legacy/Entity/src/main/java/com/param/adt/admission/model/AdmittedPatientOrdersWorkbench.java` |
| `t_aldrete_score` | `AldreteScaleScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/AldreteScaleScore.java` |
| `t_apache_draft_scale_score` | `ApacheDraftScaleScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/ApacheDraftScaleScore.java` |
| `t_apache_scale_score` | `ApacheScaleScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/ApacheScaleScore.java` |
| `t_apgar_score` | `APGARScaleScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/APGARScaleScore.java` |
| `t_bishop_score` | `BishopScaleScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/BishopScaleScore.java` |
| `t_braden_draft_scale_score` | `BradenDraftScaleScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/BradenDraftScaleScore.java` |
| `t_braden_scale_score` | `BradenScaleScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/BradenScaleScore.java` |
| `t_corporate_statement_checklist_visit_type_mapper` | `CorporateStatementChecklistVisitTypeMapper` | `legacy/Entity/src/main/java/com/param/adt/master/unit/model/CorporateStatementChecklistVisitTypeMapper.java` |
| `t_discharge_template_department_mapper` | `DischargeTemplateDepartmentMapper` | `legacy/Entity/src/main/java/com/param/adt/discharge/model/DischargeTemplateDepartmentMapper.java` |
| `t_fall_risk_score` | `FallRiskScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/FallRiskScore.java` |
| `t_gcs_draft_scale_score` | `GcsDraftScaleScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/GcsDraftScaleScore.java` |
| `t_gcs_scale_score` | `GcsScaleScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/GcsScaleScore.java` |
| `t_growth_developmental_milestones_transaction` | `GrowthDevelopmentalMilestonesTransaction` | `legacy/Entity/src/main/java/com/param/global/model/GrowthDevelopmentalMilestonesTransaction.java` |
| `t_hand_hygiene_score` | `HandHygieneScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/HandHygieneScore.java` |
| `t_laboratory_inspection` | `LaboratoryInspectionScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/LaboratoryInspectionScore.java` |
| `t_mallampatti_score` | `MallamapattiClass` | `legacy/Entity/src/main/java/com/param/adt/admission/model/MallamapattiClass.java` |
| `t_npuap_scale_score` | `NpuapScaleScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/NpuapScaleScore.java` |
| `t_nursing_scale_score` | `NursingScaleScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/NursingScaleScore.java` |
| `t_nutritional_assessment_score` | `NutritionalAssessmentScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/NutritionalAssessmentScore.java` |
| `t_nutritional_assessment_score_master` | `NutritionalAssessmentScoreMaster` | `legacy/Entity/src/main/java/com/param/adt/admission/model/NutritionalAssessmentScoreMaster.java` |
| `t_patient_fall_report` | `PatientFallReport` | `legacy/Entity/src/main/java/com/param/adt/admission/model/PatientFallReport.java` |
| `t_sofa_draft_scale_score` | `SofaDraftScaleScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/SofaDraftScaleScore.java` |
| `t_sofa_scale_score` | `SofaScaleScore` | `legacy/Entity/src/main/java/com/param/adt/admission/model/SofaScaleScore.java` |

### schema `billing` (12 tables)

| Table | Entity class | Source file |
|---|---|---|
| `m_corporate_statement_master` | `CorporateStatmentMaster` | `legacy/Entity/src/main/java/com/param/service/global/model/CorporateStatmentMaster.java` |
| `m_coupon_template_details` | `CouponTemplateDetails` | `legacy/Entity/src/main/java/com/param/billing/global/model/CouponTemplateDetails.java` |
| `m_coupon_template_master` | `CouponTemplateMaster` | `legacy/Entity/src/main/java/com/param/billing/global/model/CouponTemplateMaster.java` |
| `m_franchise_deposit` | `FranchiseDepositMaster` | `legacy/Entity/src/main/java/com/param/billing/global/model/FranchiseDepositMaster.java` |
| `m_franchise_share` | `FranchiseShareMaster` | `legacy/Entity/src/main/java/com/param/billing/global/model/FranchiseShareMaster.java` |
| `m_pos_machine_device_master` | `POSMachineDeviceMaster` | `legacy/Entity/src/main/java/com/param/billing/global/model/POSMachineDeviceMaster.java` |
| `t_cc_avenue_log` | `CCAvenueLog` | `legacy/Entity/src/main/java/com/param/global/common/CCAvenueLog.java` |
| `t_corporate_statement_details_checklist_mapper` | `CorporateStatementDetailsChecklistMapper` | `legacy/Entity/src/main/java/com/param/service/global/model/CorporateStatementDetailsChecklistMapper.java` |
| `t_coupon_template_consume_details` | `CouponTemplateConsumeDetails` | `legacy/Entity/src/main/java/com/param/billing/global/model/CouponTemplateConsumeDetails.java` |
| `t_franchise_deposit_details` | `FranchiseDepositDetails` | `legacy/Entity/src/main/java/com/param/billing/global/model/FranchiseDepositDetails.java` |
| `t_patient_coupon_template_mapper` | `PatientCouponTemplateMapper` | `legacy/Entity/src/main/java/com/param/billing/global/model/PatientCouponTemplateMapper.java` |
| `t_pos_machine_response_mapper` | `POSMachineResponseMapper` | `legacy/Entity/src/main/java/com/param/billing/global/model/POSMachineResponseMapper.java` |

### schema `doctor` (3 tables)

| Table | Entity class | Source file |
|---|---|---|
| `m_doctor_favorite_medications_master` | `DoctorFavouriteMedicationsMaster` | `legacy/Entity/src/main/java/com/param/adt/master/unit/model/DoctorFavouriteMedicationsMaster.java` |
| `t_staff_speciality_mapper` | `StaffSpecialityMapper` | `legacy/Entity/src/main/java/com/param/global/model/StaffSpecialityMapper.java` |
| `t_staff_sub_speciality_mapper` | `StaffSubSplecialityMapper` | `legacy/Entity/src/main/java/com/param/global/model/StaffSubSplecialityMapper.java` |

### schema `emr` (3 tables)

| Table | Entity class | Source file |
|---|---|---|
| `m_examination_master` | `ExaminationMaster` | `legacy/Entity/src/main/java/com/param/global/model/ExaminationMaster.java` |
| `m_system_master_gender_mapper` | `SystemMasterGenderMapper` | `legacy/Entity/src/main/java/com/param/global/model/SystemMasterGenderMapper.java` |
| `t_discharge_sum_tab_columns_print_mapper` | `DischargeSumTabColumnsPrintMapper` | `legacy/Entity/src/main/java/com/param/adt/discharge/model/DischargeSumTabColumnsPrintMapper.java` |

### schema `opd` (7 tables)

| Table | Entity class | Source file |
|---|---|---|
| `t_ABCDE_assessment` | `AbcdeAssessmentModel` | `legacy/Entity/src/main/java/com/param/entity/model/opd/AbcdeAssessmentModel.java` |
| `t_admission` | `AdmissionModel` | `legacy/Entity/src/main/java/com/param/opd/model/AdmissionModel.java` |
| `t_coversheet_template_master` | `CoversheetTemplateMaster` | `legacy/Entity/src/main/java/com/param/global/model/CoversheetTemplateMaster.java` |
| `t_custom_emr_configuration_details` | `CustomEMRConfigurationDetails` | `legacy/Entity/src/main/java/com/param/global/model/CustomEMRConfigurationDetails.java` |
| `t_follow_up_assessment` | `FollowUpAssesmentModel` | `legacy/Entity/src/main/java/com/param/entity/model/opd/FollowUpAssesmentModel.java` |
| `t_instruction_admission_mapper` | `InstructionAdmissionMapper` | `legacy/Entity/src/main/java/com/param/opd/model/InstructionAdmissionMapper.java` |
| `t_quick_notes_mapper` | `QuickNotesMapper` | `legacy/Entity/src/main/java/com/param/opd/model/QuickNotesMapper.java` |

### schema `patient` (6 tables)

| Table | Entity class | Source file |
|---|---|---|
| `letter_attachment_mapper` | `LetterAttachmentMapper` | `legacy/Entity/src/main/java/com/param/global/model/LetterAttachmentMapper.java` |
| `t_examination_mapper` | `ExaminationMapper` | `legacy/Entity/src/main/java/com/param/opd/coversheet/model/ExaminationMapper.java` |
| `t_new_cov_doctor_notes` | `NewCovDoctorNotes` | `legacy/Entity/src/main/java/com/param/opd/model/NewCovDoctorNotes.java` |
| `t_new_cov_nursing_notes` | `NewCovNursingNotes` | `legacy/Entity/src/main/java/com/param/opd/model/NewCovNursingNotes.java` |
| `t_patient_birth_history` | `PatientBirthHistory` | `legacy/Entity/src/main/java/com/param/opd/coversheet/model/PatientBirthHistory.java` |
| `t_patient_registration_doc_details` | `PatientRegistrationDocDetails` | `legacy/Entity/src/main/java/com/param/global/model/PatientRegistrationDocDetails.java` |

### schema `pharmacy` (4 tables)

| Table | Entity class | Source file |
|---|---|---|
| `m_qms_counter` | `QMSCounter` | `legacy/Entity/src/main/java/com/param/qms/model/QMSCounter.java` |
| `t_administration_worklist_time_details` | `AdministrationWorklistTimeDetails` | `legacy/Entity/src/main/java/com/param/entity/model/opd/AdministrationWorklistTimeDetails.java` |
| `t_antibiotic_test_dose` | `AntibioticTestDose` | `legacy/Entity/src/main/java/com/param/entity/model/pharmacy/AntibioticTestDose.java` |
| `t_tax_component_Details` | `TaxComponentDetails` | `legacy/Entity/src/main/java/com/param/entity/model/master/TaxComponentDetails.java` |

### schema `public` (22 tables)

| Table | Entity class | Source file |
|---|---|---|
| `m_appointment_template_master` | `AppointmentTemplateMaster` | `legacy/Entity/src/main/java/com/param/adt/templet/model/AppointmentTemplateMaster.java` |
| `m_appointment_template_master_mapper` | `AppointmentTemplateMasterMapper` | `legacy/Entity/src/main/java/com/param/adt/templet/model/AppointmentTemplateMasterMapper.java` |
| `m_appointment_template_service` | `AppointmentTemplateService` | `legacy/Entity/src/main/java/com/param/adt/templet/model/AppointmentTemplateService.java` |
| `m_discharge_field_unit_wise` | `DischargeFieldUnitWise` | `legacy/Entity/src/main/java/com/param/adt/discharge/model/DischargeFieldUnitWise.java` |
| `m_nurse_shift_allocation` | `NurseShiftAllocation` | `legacy/Entity/src/main/java/com/param/global/model/NurseShiftAllocation.java` |
| `m_nurse_shift_allocation_details` | `NurseShiftAllocationDetails` | `legacy/Entity/src/main/java/com/param/global/model/NurseShiftAllocationDetails.java` |
| `m_patient_poc_master` | `PatientPOCMaster` | `legacy/Global/src/main/java/com/param/global/model/PatientPOCMaster.java` |
| `m_patient_tabular_template_data_mapper` | `PatientTabularTemplateDataMapper` | `legacy/Entity/src/main/java/com/param/global/model/PatientTabularTemplateDataMapper.java` |
| `m_tabular_template_data_mapper` | `TabularTemplateDataMapper` | `legacy/Entity/src/main/java/com/param/global/model/TabularTemplateDataMapper.java` |
| `m_tabular_template_master` | `TabularTemplateMaster` | `legacy/Entity/src/main/java/com/param/global/model/TabularTemplateMaster.java` |
| `t_anesthesia_assessments` | `AnesthesiaAssessment` | `legacy/Entity/src/main/java/com/param/global/model/AnesthesiaAssessment.java` |
| `t_cash_in_out` | `CashInOut` | `legacy/Entity/src/main/java/com/param/billing/global/model/CashInOut.java` |
| `t_custom_history_configuration_details` | `CustomHistoryConfigurationDetails` | `legacy/Global/src/main/java/com/param/global/model/CustomHistoryConfigurationDetails.java` |
| `t_doctor_in_cabin` | `DoctorInCabin` | `legacy/Entity/src/main/java/com/param/global/model/DoctorInCabin.java` |
| `t_free_text_ordered_services` | `FreeTextOrderedServices` | `legacy/Entity/src/main/java/com/param/entity/model/master/FreeTextOrderedServices.java` |
| `t_initial_assessment` | `InitialAssessmentICUAndER` | `legacy/Entity/src/main/java/com/param/global/model/InitialAssessmentICUAndER.java` |
| `t_initial_assessment_mapper` | `InitialAssessmentICUAndERMapper` | `legacy/Entity/src/main/java/com/param/global/model/InitialAssessmentICUAndERMapper.java` |
| `t_order_details_package_mapper_log` | `OrdeDetailsPackageMapperLog` | `legacy/Entity/src/main/java/com/param/service/global/model/OrdeDetailsPackageMapperLog.java` |
| `t_order_status_master` | `OrderStatusMaster` | `legacy/Entity/src/main/java/com/param/global/model/OrderStatusMaster.java` |
| `t_patient_diagnosis_service_mapper` | `PatientDiagnosisServiceMapper` | `legacy/Entity/src/main/java/com/param/opd/coversheet/model/PatientDiagnosisServiceMapper.java` |
| `t_translated_frequency` | `TranslatedFrequencyMaster` | `legacy/Entity/src/main/java/com/param/opd/coversheet/model/TranslatedFrequencyMaster.java` |
| `t_user_indetification_details` | `UserIndetificationDetails` | `legacy/Entity/src/main/java/com/param/global/model/UserIndetificationDetails.java` |

### schema `service` (4 tables)

| Table | Entity class | Source file |
|---|---|---|
| `m_global_service_master` | `GlobalServiceMaster` | `legacy/Entity/src/main/java/com/param/global/model/GlobalServiceMaster.java` |
| `t_global_service_mapper` | `GlobalServiceMapper` | `legacy/Entity/src/main/java/com/param/global/model/GlobalServiceMapper.java` |
| `t_package_version` | `PackageVersionMaster` | `legacy/Entity/src/main/java/com/param/service/global/model/PackageVersionMaster.java` |
| `t_vendor_service_rate_config` | `VendorServiceRateConfig` | `legacy/Entity/src/main/java/com/param/billing/global/transaction/model/VendorServiceRateConfig.java` |


## Appendix B: Full list of LOW-priority missing tables (not created, deferred)


### schema `abdm` (12 tables)

| Table | Entity class | Reason deferred |
|---|---|---|
| `m_abdm_care_context` | `AbdmCareContext` | schema 'abdm' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `m_abdm_credential` | `AbdmCredential` | schema 'abdm' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `m_abdm_discover` | `AbdmDiscoverResponse` | schema 'abdm' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `m_abdm_login_verification` | `AbdmLoginVerification` | schema 'abdm' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `m_abdm_patient_reg_request` | `AbdmPatientRegRequest` | schema 'abdm' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `m_abdm_share` | `AbdmShareResponse` | schema 'abdm' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `m_abdm_transaction` | `AbdmTransaction` | schema 'abdm' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `m_call_back_response` | `AbdmCallBackResponse` | schema 'abdm' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `m_consent_notify_data` | `ConsentNotifyData` | schema 'abdm' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `m_consent_request_artifact_id` | `ConsentRequestArtifactId` | schema 'abdm' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `m_consent_request_data` | `ConsentRequestData` | schema 'abdm' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `m_hiu_fhir_details` | `HiuFhirDetails` | schema 'abdm' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |

### schema `adt` (3 tables)

| Table | Entity class | Reason deferred |
|---|---|---|
| `m_mrd_file_status` | `MrdFileStatus` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `t_mrd_file_management` | `MrdFileAllocation` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `t_mrd_file_management_status` | `MrdFileAllocationStatus` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |

### schema `assets` (1 tables)

| Table | Entity class | Reason deferred |
|---|---|---|
| `t_preventive_maintenance` | `PreventiveMaintenance` | schema 'assets' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |

### schema `billing` (9 tables)

| Table | Entity class | Reason deferred |
|---|---|---|
| `credit_bill_reciept_status_log` | `CreditBillRecieptStatusLog` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `t_active_deposite_credit_note_mapper` | `ActiveDepositeCreditNoteMapper` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `t_concession_manager_bill_payee_details_logs` | `ConcesionManagerBillPayeeDetailsLog` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `t_payment_reciept_mapper` | `PaymentRecieptMapper` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `t_payment_refund_voucher_master` | `PaymentRefundVoucherMaster` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `t_payment_voucher_mode_details` | `PaymentVoucherModeDetails` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `t_refund_voucher` | `RefundVoucher` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `t_refund_voucher_payment_mode_details` | `RefundVoucherPaymentModeDetails` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `t_unit_service_mapper` | `UnitServicePriceMaster` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |

### schema `blood_bank` (2 tables)

| Table | Entity class | Reason deferred |
|---|---|---|
| `m_transfusion_details` | `TransfusionDetailMaster` | schema 'blood_bank' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `t_intra_transfusion_monitoring_mapper` | `IntraTransfusionMonitoring` | schema 'blood_bank' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |

### schema `emr` (1 tables)

| Table | Entity class | Reason deferred |
|---|---|---|
| `m_discharge_sum_tab_columns_master` | `DischargeSumTableColumnsMaster` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |

### schema `inventory` (1 tables)

| Table | Entity class | Reason deferred |
|---|---|---|
| `t_returnable_bonus_batch_expiry` | `returnableBonusBatchExpiry` | schema 'inventory' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |

### schema `lab` (4 tables)

| Table | Entity class | Reason deferred |
|---|---|---|
| `m_roa_master` | `RateOfAdministrationMaster` | schema 'lab' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `m_service_profit_margin` | `ServiceProfitMargin` | schema 'lab' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `t_lab_report_document_details` | `LabReportDocumentDetails` | schema 'lab' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `t_medall_request_response_details` | `MedallRequestResponseDetails` | schema 'lab' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |

### schema `opd` (3 tables)

| Table | Entity class | Reason deferred |
|---|---|---|
| `m_custom_emr_category_master` | `CustomEMRCategoryMaster` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `m_op_visit_rule_master` | `OpVisitRuleMaster` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `t_custom_emr_configuration` | `CustomEmrConfiguration` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |

### schema `otims` (2 tables)

| Table | Entity class | Reason deferred |
|---|---|---|
| `m_diagnostic_nonsurgical_procedure_config` | `DiagnosticProcedureConfig` | schema 'otims' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `m_resource_bill_of_services_mapper` | `ResourceBillOfServicesMapper` | schema 'otims' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |

### schema `patient` (2 tables)

| Table | Entity class | Reason deferred |
|---|---|---|
| `m_coversheet_access_master` | `CoversheetAccessMaster` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `m_coversheet_access_master` | `CustomEMRSubCategoryMaster` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |

### schema `pharman` (1 tables)

| Table | Entity class | Reason deferred |
|---|---|---|
| `m_drug_db` | `PharmanDrugModel` | schema 'pharman' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |

### schema `procurement` (11 tables)

| Table | Entity class | Reason deferred |
|---|---|---|
| `t_consignment_stock_po_accessories_mapper` | `ConsignmentStockPOAccessoriesMapper` | schema 'procurement' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `t_consignment_stock_po_staged` | `ConsignmentStockPOStaged` | schema 'procurement' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `t_consignment_stock_purchase_order` | `ConsignmentStockPO` | schema 'procurement' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `t_consignment_stock_purchase_order_detail` | `ConsignmentStockPODetail` | schema 'procurement' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `t_good_receipt_note_detail_log` | `GoodReceiptNoteDetailLog` | schema 'procurement' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `t_grn_bonus_batch_expiry` | `GrnBonusBatchExpiry` | schema 'procurement' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `t_new_work_order_completion` | `NewWorkOrderCompletion` | schema 'procurement' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `t_new_work_order_completion_details` | `newWorkOrderCompletionDetails` | schema 'procurement' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `t_new_work_order_completion_staged_details` | `NewWorkOrderCompletionStagedDetailsMapper` | schema 'procurement' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `t_new_work_order_creation_staged_details` | `NewWorkOrderCreationStagedDetailsMapper` | schema 'procurement' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |
| `t_other_charges_consignment_stock_po_mapper` | `ConsignmentStockPOOtherChargesMapper` | schema 'procurement' outside near-term testing scope (hospital setup / user-role / OPD-IPD consultation / pharmacy / billing) |

### schema `public` (41 tables)

| Table | Entity class | Reason deferred |
|---|---|---|
| `button_master` | `ButtonMaster` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `button_master_config` | `ButtonMasterConfig` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `in_patient_feedback` | `InPatientFeedback` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `m_department_master` | `DepartmentMasterOPD` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `m_duo_application_properties` | `DuoApplicationProperties` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `m_language_master` | `LanguageMaster` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `m_modality_type_master` | `ModalityType` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `m_mrd_box_master` | `BoxMaster` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `m_mrd_rack_master` | `MrdRackMaster` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `m_mrd_row_master` | `RowMaster` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `m_nabh_process_master` | `NabhProcessMaster` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `m_neuronplus_data_save_phlebotomy` | `NeuronplusDataSavePhlebotomy` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `m_otims_user_master` | `OtimsUserMaster` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `m_razorpay_order_log_details` | `RazorPayOrderLogDetail` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `m_screen_priority_mapper` | `ScreenPriority` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `m_staff_role_master` | `StaffRoleMaster` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `m_ticket_system_master` | `TicketSystemMaster` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `m_vaccination_role_mapper` | `VaccinationRoleMapper` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `m_whatsApp_credential` | `whatsAppCredential` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `out_patient_feedback` | `OutPatientFeedback` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `t_bulk_message_queue` | `BulkMessageQueue` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `t_clinical_intervention_procedure` | `ClinicalInterventionProcedure` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `t_custom_patient_details_external` | `CustomPatientDetails` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `t_daily_transaction_details` | `DailyTransactionDetailsMaster` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `t_employee_survey` | `EmployeeSurvey` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `t_employee_vaccination` | `EmployeeVaccination` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `t_ht7_message_queue` | `Hl7MessageQueue` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `t_ht7_message_queue_details` | `Hl7QueueDetails` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `t_ldap_configuration` | `LdapConfigurationMaster` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `t_message_log` | `MessageLog` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `t_nabh_reporting_mapper` | `NabhReportingMapper` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `t_outside_hospital_doctor_mapper` | `OutsideHospitalDoctorMapper` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `t_patient_pan_card_doc` | `PatientPanCardDoc` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `t_quality_parameter_master` | `QualityParameterMaster` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `t_refund_transaction_details` | `RefundTransactionMaster` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `t_reported_nabh_mapper` | `ReportedNabhMapper` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `t_template_quetionary_print_mapper` | `TemplateQuetionaryPrintMapper` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `t_ticket_system_master_uploaded_files` | `TicketSystemMasterFiles` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `t_unit_user_mapper` | `UnitUserMapper` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |
| `t_visitor_pass_mapper` | `VisitorPassMapper` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |
| `user_chat_details` | `UserChatDetails` | wired but peripheral/administrative/integration/accreditation feature outside the 5 named near-term flows |

### schema `service` (1 tables)

| Table | Entity class | Reason deferred |
|---|---|---|
| `t_contract_group_pharmacy_exclusion_details` | `TContractGroupPharmacyExclusionDetails` | not referenced by any Dao/Service/Controller/Repository outside its own file — likely dead/speculative entity |


## Appendix C: Orphan tables (exist in DB, no matching @Entity anywhere in legacy/Entity or legacy/Global)


Total: 755 across all schemas. Per the task scope, these are listed only — not investigated or acted on.


| Schema | Orphan table count |
|---|---|
| `adt` | 24 |
| `assets` | 1 |
| `billing` | 25 |
| `cssd` | 21 |
| `doctor` | 26 |
| `emr` | 26 |
| `equipment` | 10 |
| `homecare` | 87 |
| `inventory` | 13 |
| `kitchen` | 20 |
| `lab` | 16 |
| `opd` | 9 |
| `otims` | 4 |
| `patient` | 42 |
| `pharmacy` | 5 |
| `procedure` | 30 |
| `procedure_old` | 12 |
| `procurement` | 11 |
| `public` | 335 |
| `reports` | 21 |
| `service` | 17 |

**Total orphan tables: 755**


Full per-table list:


### schema `adt` (24)

`admissionpaper`, `admpaper`, `bed_master`, `get_patient_transfer_details`, `m_discharge_status_master`, `m_discharge_summary_field_master_bkp`, `m_general_ledger_master`, `m_mortuary_status_master`, `m_nursing_station_ward_mapper`, `m_role_master`, `m_short_leave_status`, `m_transfer_type`, `t_admission_inter_consulting`, `t_admisstion_doc_details`, `t_bed_reservation_log`, `t_cost_estimation`, `t_er_transfer_stats`, `t_intensivist_request_close_icu`, `t_mother_delivery_details`, `t_patient_templet`, `t_reserved_bed`, `t_under_maintenance`, `t_under_maintenance_log`, `t_user_role_mapper`


### schema `assets` (1)

`get_asset_opbalance`


### schema `billing` (25)

`claim_interface`, `claim_interface_device_speciality_mppr`, `creditnotes`, `debitnotes`, `deposit`, `depositprintforadmission`, `image_opdpaymentreceipt`, `ipdintrimbiil`, `m_authorization_status_master`, `m_bill_estimation_master`, `m_debit_note_reason_master`, `m_orderset_master`, `m_payment_against_master`, `m_request_type_master`, `opdpaymentreceipt`, `refund_cn`, `t_bill_estimation_detail`, `t_billing_pharmacy_details`, `t_credit_bill_reciept_status_log`, `t_credit_note_outstanding_adjustment`, `t_orderset_detail`, `t_package_consumption_service_detail_log`, `t_payee_master`, `t_refund_payment_mode_details`, `t_unit_service_price_master`


### schema `cssd` (21)

`t_autoclave_log`, `t_cssd_opening_balance`, `t_cssd_opening_balance_detail`, `t_cssd_stock_transaction_log`, `t_issued_kit_transition_mapper`, `t_kit_indent`, `t_kit_indent_details`, `t_kit_issue`, `t_kit_issue_kit_mapper`, `t_kit_movement_record`, `t_kit_return`, `t_kit_return_kit_mapper`, `t_load_batch`, `t_load_batch_kit_mapper`, `t_pre_sterilize_process`, `t_pre_sterilize_process_instrument`, `t_returned_kit_transition_mapper`, `t_send_to_microbiology`, `t_sterilization_request`, `t_sterilization_request_transition_mapper`, `t_sterilized_batch`


### schema `doctor` (26)

`doctor_unit_department_mapper`, `doctor_unit_visittype_mapper`, `doctorinformation`, `doctorinformationipd`, `doctorshare`, `doctorsharedetails`, `ipddoctorinformation`, `m_classification_master`, `m_cpd_log_master`, `m_doctor_consultation_service_mapper_030321`, `m_doctor_schedule_visit_details`, `m_doctor_type_master`, `m_employee_master`, `t_admitting_doctor_master`, `t_doctor_address_details`, `t_doctor_advice_mapper`, `t_doctor_classification_mapper`, `t_doctor_file_details`, `t_doctor_history_mapper`, `t_doctor_instruction_mapper`, `t_doctor_observation_mapper`, `t_doctor_physical_examination_mapper`, `t_doctor_roster`, `t_doctor_schedule`, `t_doctor_voice_recording`, `t_sub_group_master`


### schema `emr` (26)

`m_advice_master`, `m_booking_type_master`, `m_complaint_master`, `m_consult_order_master`, `m_consult_order_status_master`, `m_diagnosis_type_master`, `m_dictation_master`, `m_forum_category_master`, `m_forum_topic_master`, `m_habit_type_master`, `m_history_master`, `m_identification_type_master`, `m_instruction_master`, `m_letter_master`, `m_observation_master`, `m_order_master`, `m_personal_history`, `m_physical_examination_master`, `m_radiology_question_master`, `m_st_allergy_master`, `m_st_family_history_master`, `m_st_genetic_disorder_master`, `m_st_known_conditions_master`, `m_surgical_history_master`, `m_swabs_master`, `m_system_master_gender_mapper`


### schema `equipment` (10)

`m_checklist`, `m_checklist_detail`, `m_checklist_item_mapper`, `t_asset_pms_request`, `t_asset_pms_schedule`, `t_asset_pms_scheduler`, `t_asset_pms_scheduler_checklist`, `t_asset_pms_scheduler_detail`, `t_asset_schedule_worklist`, `t_spare_comsuption`


### schema `homecare` (87)

`activity_m`, `admission_request`, `allergy_patient_physio_mapper`, `ambulation_m`, `ambulation_m_physio_mapper`, `area_m`, `auscultation_m`, `auscultation_physio_mapper`, `bmi`, `braden_score`, `care_plan_m`, `career_m`, `career_physio_mapper`, `chief_complaints_m`, `chief_complaints_physio_mapper`, `city_m`, `cmedetails`, `cognitive_m`, `cognitive_physio_mapper`, `countries_m`, `department_m`, `diagnosis_m`, `education_for_patients_m`, `education_for_patients_physio_mapper`, `education_m`, `education_physio_mapper`, `empcategory_m`, `employmentdetails`, `experiencedetails`, `flowsheet`, `flowsheet_note`, `flowsheet_score_mapper`, `functional_assessment_m`, `functional_assessment_m_physio_mapper`, `identification_docs_m`, `insurancedetails`, `intervention_m`, `kindetails`, `mobility_aids_m`, `mobility_aids_physio_mapper`, `mood_m`, `mood_physio_mapper`, `muscle_power_m`, `muscle_power_physio_mapper`, `muscle_tone_m`, `muscle_tone_m_physio_mapper`, `nationality_m`, `opd_visit_mapper`, `organisation_m`, `pain_area_m`, `pain_area_physio_mapper`, `parameter_m`, `patient_diagnosis`, `patient_diagnosis_activity`, `patient_physiotherapy_details`, `physiotherapy_plans_m`, `physiotherapy_plans_physio_mapper`, `precaution_m`, `precaution_m_physio_mapper`, `qualification_m`, `qualificationdetails`, `race_m`, `reason_for_admission_m`, `role`, `rom_m`, `rom_m_physio_mapper`, `roster`, `score_m`, `sedation_score`, `settlementtype_m`, `shift_m`, `states_m`, `sub_parameter_m`, `subdepartment_m`, `t_score_parameter_transaction`, `t_score_transaction`, `therapy_goals_m`, `therapy_goals_physio_mapper`, `treatment_given_m`, `treatment_given_physio_mapper`, `unit_m`, `user`, `userdocument`, `userimagefile`, `vip_score`, `visit`, `visittype_m`


### schema `inventory` (13)

`get_materialreturndetails`, `get_materialreturnlist`, `get_non_billableconsumption`, `getconsumableacceptance`, `getstoreindent`, `getstoreindentlist`, `inventorystoreid`, `m_pms_type`, `t_batch_insurance`, `t_material_issue_reject`, `t_quotation_enquiry`, `t_quotation_enquiry_detail`, `t_quotation_enquiry_expected_accessories`


### schema `kitchen` (20)

`diet_issue`, `diet_order`, `diet_planning`, `dish_master_diet_pref_mapper`, `dish_master_key_ingredients_mapper`, `dish_type`, `key_ingredients_master`, `key_ingredients_nutrition_mapper`, `m_diet_template`, `m_diet_template_diet_preference_mapper`, `m_diet_template_mapper`, `m_diet_timing_master`, `m_dish_master`, `m_meal_master`, `m_nutrition_master`, `m_unit_of_measurment_master`, `m_week_days_master`, `meal_details`, `meal_details_dish_mapper`, `timedemo`


### schema `lab` (16)

`barcodeforlabsample`, `get_machine_data`, `get_turnaroundtime`, `investigationslip`, `m_hour_master`, `m_incubation_time_master`, `m_lab_sorting_master`, `m_machine_master_interface`, `m_param_textual_range_080421`, `m_patient_status_master`, `m_priority_master`, `m_rejection_master`, `m_sample_status_master`, `m_send_throgh_master`, `m_test_reason_master`, `t_out_source_logger`


### schema `opd` (9)

`appointmentslip`, `casepaper`, `getvital`, `m_instruction_master`, `m_reference_type_master`, `patientcard`, `t_broadcast_msg`, `t_encounter_appointment_mapper`, `t_referal_details`


### schema `otims` (4)

`get_ot_register`, `get_ot_register_completed`, `get_ot_reservation`, `get_otprocedure_details`


### schema `patient` (42)

`diagnosis_master`, `groupwise_receipt_report`, `m_patient_registration_type_master`, `m_special_needs_master`, `mis_issue_to_department_list_report`, `mis_patient_charge_detail_report`, `mis_pending_purchase_order_report`, `mis_ward_wise_monthly_income_report`, `opdcaspaper`, `patientinformation`, `registration`, `t_advice_appo_mapper`, `t_appointment_service_master`, `t_consult_order_appo_mapper`, `t_diagnosis_appo_mapper`, `t_forum_topic_comment_mapper`, `t_habit_master_type_mapper`, `t_history_appo_mapper`, `t_instruction_appo_mapper`, `t_labs_order`, `t_letter_appo_mapper`, `t_message_notification_master`, `t_new_born_details`, `t_observation_appo_mapper`, `t_order_radiology_question_mapper`, `t_order_swab_mapper`, `t_patient_credit_company_details`, `t_patient_document_doctor_log`, `t_patient_document_mapper`, `t_patient_habit_mapper`, `t_patient_identification_details`, `t_patient_insurance_details`, `t_patient_insurance_file_details`, `t_patient_notification`, `t_patient_registration_bkp_211220`, `t_physical_exam_appo_mapper`, `t_special_needs_patient_mapper`, `t_st_pat_genetic_disorder`, `t_st_patient_allergy`, `t_st_patient_family_history`, `t_st_patient_known_conditions`, `t_uk_coversheet_message_master`


### schema `pharmacy` (5)

`get_inpatientissue`, `t_broadcast_qms_msg`, `t_medication_handover`, `t_medication_handover_details`, `t_qms_token_sequence`


### schema `procedure` (30)

`get_computestock`, `get_expired_stock`, `get_grn`, `get_grn_list`, `get_grn_return`, `get_grn_return_list`, `get_grnsettlement`, `get_issueworklist`, `get_pharmacybilloutstanding`, `get_pharmacycollection`, `get_po`, `get_purchase_gst`, `get_purchase_return_gst`, `get_quotation`, `get_reorderlevel`, `get_salesbills`, `get_salesbills_return`, `get_salesbills_return_list`, `get_salesbills_return_new`, `get_salesforipd`, `get_salesforipd_interim`, `get_salesgrossmargin`, `get_stockvalue`, `getgst`, `getgstsalesreturn`, `getgstsalesreturninvoice`, `getsalesgstinvoice`, `getsalesgstwithtaxable`, `getsalesgstwithtaxabledetails`, `getsalesreturngstwithtaxable`


### schema `procedure_old` (12)

`get_grn`, `get_grn_list`, `get_grn_return`, `get_grn_return_list`, `get_pharmacycollection`, `get_po`, `get_quotation`, `get_salesbills`, `get_salesbills_return`, `get_salesbills_return_new`, `get_salesgrossmargin`, `get_stockvalue`


### schema `procurement` (11)

`get_physical_inspection_details`, `get_requestwasteassetdisposal`, `get_workordercreationdetails`, `get_workordercreationdetails_new`, `get_workorderquotationselection`, `get_workorderrequestdetail`, `getgrnsummery`, `getpurchaseordersummery`, `t_pr_po_mapper`, `t_quotation_send_to_vendor`, `t_quotation_send_to_vendor_detail`


### schema `public` (335)

`admbarcode`, `advanceconsumed`, `advanceconsumed_againstbills`, `advancesummary`, `allcollectionsummary`, `anil_allcollectionsummary`, `aws_config`, `bedoccupancy_test`, `bi_er_transfer_stats`, `bi_finaltotal`, `bi_icu_transfer_stats`, `billingreport`, `billingreport_bi`, `billingreportdetails`, `chequereceivedlist`, `cname`, `consignmentbatchcount`, `dailydoctorconsultation`, `depositavilable`, `dischargeslip`, `dischargesummaryreport`, `discountreportcategorywise`, `discountreportdetails`, `discountreportdoctorwise`, `doctorshare`, `doctorwiseservicedetails`, `domain_config`, `get_asset_indent`, `get_batch_exp_change`, `get_batch_exp_mrp_adjustment_list`, `get_bill_estimation`, `get_billableconsumption`, `get_billableconsumption_return`, `get_billcancellation`, `get_birth_register`, `get_cash_handover`, `get_company_details`, `get_companysettlement`, `get_companytds`, `get_confirm_stock`, `get_consignment_inward_details`, `get_crashcartrequest`, `get_death_register`, `get_departmentwise_revenue`, `get_departmentwise_revenue_summary`, `get_doctor_details`, `get_doctor_payment`, `get_doctorslab_nahar`, `get_doctorvoucher`, `get_doctorwisepatientcount`, `get_doctorwisepatientcount_total`, `get_drug_recall`, `get_employee_details`, `get_enquiry_sendto_vendor`, `get_expired_patientstatistics`, `get_filereceived`, `get_haemodailysisdetails`, `get_healthpackage`, `get_implant_opening_balance`, `get_interim_handover`, `get_invoicedetails`, `get_inward`, `get_ipdbill_new`, `get_ipdbill_vijayvallabh`, `get_item_purchase`, `get_item_request`, `get_itemdetails`, `get_loan_in`, `get_loan_inout`, `get_loan_out`, `get_loan_outreceive`, `get_loyaltycard`, `get_material_issue`, `get_material_picker`, `get_misexpenses`, `get_misexpenses_list`, `get_mlc_register`, `get_modalityappointment`, `get_narcoticsitem`, `get_openingbal`, `get_ot_reservation`, `get_patient_consumable_pendingindent`, `get_patient_indent`, `get_patientconsumableissue`, `get_patientcount`, `get_patientcount_transactional_count`, `get_patientcount_transactional_count_totalpatient`, `get_patientcountdetails`, `get_patientgatepass`, `get_pendig_assigning_picker`, `get_pharmacybillreport`, `get_pharmacycollection`, `get_physical_inspection_details`, `get_purchase_request`, `get_quotation_enquiry`, `get_radio_test_countandrevenue`, `get_refer_doctorreport`, `get_refer_doctorreport_gurugopi`, `get_refer_doctorreport_gurugopi_gurugopi`, `get_refer_doctorreport_ssm`, `get_referral_info`, `get_refund_list`, `get_rejected_material_acceptance`, `get_requestforassetdisposal`, `get_revenu`, `get_schedulex`, `get_scrap_request`, `get_service_baseprice`, `get_servicedays`, `get_shiftclosing`, `get_stockledger`, `get_stoptransactionrpt`, `get_tariffwiserate`, `get_technical_approval`, `get_tempdoctor_report`, `get_upload_stock`, `get_variance_approval`, `get_vender_quotation_selection`, `get_vendor_negotitation`, `get_visit_details`, `get_wo_quotation_negotiation`, `get_wo_quotationsendvendor`, `get_workorderquotationentry`, `getadmissiondetails`, `getadmittedpatientlist`, `getbilldetails`, `getbilldetails_new`, `getconcessionwisebilldetails`, `getconsent`, `getcreditbillreport`, `getdischargecancelleddetails`, `getdischargedetails`, `getdoctorwiseservicelist`, `getexpireditemlist`, `getgrnreturnsummery`, `gethistopatholgyreport`, `getipdadvanceamount`, `getipdbilldetails`, `getipdbilldetails_distinct`, `getipdbilldetails_new`, `getipdbilldetailsall`, `getipdbilldetailsathhayu`, `getopdconsultatationcategorywise`, `getopdconsultatationdepartmentwise`, `getopdconsultatationdepartmentwiseonly`, `getopdconsultatationdoctorwise`, `getopdconsultatationdoctorwise_new`, `getopdconsultatationdoctorwiseonly`, `getpackagedetails`, `getpackagedetails_ip`, `getparametercount`, `getpatholgyreport`, `getpatholgyreport_interface`, `getpatholgyreport_sensitivity`, `getpatholgyreport_useforsampleno`, `getpatientregisterlist`, `getpatientregisterlist_withencounter`, `getpatientvisitdetailsreport`, `getpendingissuesdetails`, `getreferralconsultatation`, `getrefundadvandcnreport`, `getserologyreport`, `getservicemaingroupsummery`, `getservicemaingroupsummery_rb`, `getservicemaster`, `getservicewisebilldetails`, `gettestcount`, `gettestcount_new`, `gettestcount_revenue`, `getvital`, `getworkorder`, `image_advancesummary`, `image_allcollectionsummary`, `image_allcollectionsummary_aarogya`, `image_allcollectionsummary_aarogyanew`, `image_allcollectionsummary_all`, `image_allcollectionsummary_all_new`, `image_allcollectionsummary_baner`, `image_allcollectionsummary_banernew`, `image_allcollectionsummary_dhanorinew`, `image_allcollectionsummary_dhole`, `image_allcollectionsummary_dholenew`, `image_allcollectionsummary_kasarwadi`, `image_allcollectionsummary_kasarwadinew`, `image_allcollectionsummary_kharadi`, `image_allcollectionsummary_kharadinew`, `image_allcollectionsummary_koregaon`, `image_allcollectionsummary_koregaonnew`, `image_allcollectionsummary_lonikalbhor`, `image_allcollectionsummary_lonikalbhornew`, `image_allcollectionsummary_monthlyexcelsheet`, `image_allcollectionsummary_pimpari`, `image_allcollectionsummary_pimparinew`, `image_allcollectionsummary_rad`, `image_allcollectionsummary_radnew`, `image_allcollectionsummary_shikrapur`, `image_allcollectionsummary_shikrapurnew`, `image_allcollectionsummary_shirwal`, `image_allcollectionsummary_shirwalnew`, `image_allcollectionsummary_wagholi`, `image_allcollectionsummary_wagholinew`, `image_allcollectionsummarytest`, `image_getbilldetails_new`, `image_getbilldetails_new1`, `image_groupwiserevenucount`, `image_opdipdcollectionsummary`, `image_opipdadvancerefund`, `image_opipdbillsummary`, `image_radiologyreport`, `ipdintrimbiil`, `item_master`, `lock_monitor`, `m_batch_log`, `m_batch_log_room_rent`, `m_bi_action_master`, `m_circufereance_unit`, `m_document_send_through_master`, `m_formulation_type_deolali`, `m_generic_deolali`, `m_height_unit`, `m_insurance_company`, `m_item_deolali`, `m_language_master`, `m_manufacturer_deolali`, `m_my_life_connection_link`, `m_nurse_shift_allocation`, `m_nurse_shift_allocation_details`, `m_pass_type_master`, `m_pharmacological_classification_deolali`, `m_procedure_type_master`, `m_report_header`, `m_role_master_bkp_090420`, `m_sale_type`, `m_sms_category_master`, `m_sms_required_data`, `m_sms_trigger_master`, `m_sterilization_color_indicator`, `m_store_deolali`, `m_store_item_mapper_deolali`, `m_store_pc_mapper_deolali`, `m_strength_unit_deolali`, `m_tally_import_data`, `m_test1`, `m_unit_of_measurement_deolali`, `m_unit_subscription_details`, `m_unit_wise_logo`, `m_uom_unit_deolali`, `m_view_bed_variable_factor_nikhil`, `m_weight_unit`, `m_whatsapp_template_master`, `mis_charges_cancellation_report`, `mis_charges_unbilled_report`, `mis_cheque_list_report`, `mis_creditcard_debitcard_list_report`, `mis_diagnosis_summary_report`, `mis_expired_items_return_list`, `mis_group_wise_summary_report`, `mis_inpatient_issue_list_report`, `mis_inpatient_return_list_report`, `mis_list_of_scheme_discount_and_free_item_report`, `mis_receipt_register_report`, `mis_sales_credit_report`, `mis_sercies_wise_count_report`, `mis_supplier_address_report`, `mis_work_order_list_report`, `narhar_getbilldetails_new`, `newdate`, `non_formulary`, `op_ip_pathology`, `op_ipcash`, `op_ipcash_new`, `op_mis`, `op_revenu`, `op_revenu_nahar`, `opdcovershit`, `opdipdcollectionsummary`, `opipdadvancerefund`, `opipdbillsummary`, `outstandingbills`, `package_healthplan_testcount`, `passed`, `patient_ledger`, `patientadvancereport`, `patientdetails`, `patientdetailsipd`, `patientinformation`, `patientwisewiselabservice`, `pg_stat_activity_history`, `pg_stat_statements`, `pl_admission`, `pl_appointment`, `pl_bills`, `pl_creditnote`, `pl_debitnote`, `pl_deposit`, `pl_encounter`, `pl_payments`, `pl_phama`, `prescriptioncasesheetdetails`, `prescriptioncasesheetipd`, `radiologyreport`, `refferaldoctorshare`, `refferaldoctorsharebills`, `selfsettenmentreciptipd`, `stud`, `t_aws_credentials`, `t_consent_values`, `t_finaccial_tally_interface`, `t_medical_history_details`, `t_order_details_tax_mapper`, `t_sms_schedule_numbers`, `t_synctimestamp`, `t_tally_interface`, `t_temp_tally_interface`, `t_template_transaction_questionary`, `t_trigger_data_mapper`, `t_unit_sub_speciality_mapper_021019`, `t_url_configuration`, `t_visitor_pass_mapper`, `tbl`, `tbl_databasestats`, `temp_nurse_assignment`, `tokennumberprint`, `tracker_er_transfer_stats`, `unitinformation`, `updateexp`, `v_percentage`, `v_ration`, `v_transation_log`, `vaccination_master_chart`, `view_index`, `vw_labinvestprescrition`, `vw_newprescriptioncasesheet`, `vw_patientallergy`, `vw_rediologyinvestpresction`


### schema `reports` (21)

`bi_collection`, `bi_collection_new`, `bi_finaltotal`, `bi_finaltotalnew`, `bi_group_total`, `bi_groupwiserevenucount`, `bi_image_collection`, `bi_image_expenses`, `bi_image_outstandingbills`, `deptcollectionkau`, `doctorinformationipd`, `get_groupwisetotal`, `getopdconsultatationdoctorwise`, `image_groupwiserevenucount`, `investigationslip`, `loyaltycard`, `nabh_nursing`, `prescriptioncasesheet`, `prescriptioncasesheetdetails`, `prescriptioncasesheetipd`, `prescriptioncasesheetnew_ipd`


### schema `service` (17)

`get_servicemaster`, `m_modality_master`, `m_plan_type_master`, `m_service_class_master`, `m_service_time_period_master`, `t_insurance_tpa_mapper_030321`, `t_package_group_mapper`, `t_package_medicine_mapper`, `t_package_version`, `t_plan_class_rate_mapper`, `t_plan_master`, `t_plan_service_mapper`, `t_plan_tariff_mapper`, `t_service_class_mapper`, `t_tariff_bed_category_mapper`, `t_tariff_service_class_mapper`, `t_unit_service_mapper_211218`
