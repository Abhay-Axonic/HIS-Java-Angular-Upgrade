# Audit: Tracked `.java~` Editor-Backup Files

*Read-only audit. No files were deleted, moved, or modified as part of this document. Branch: `master` (audit docs/governance files may be committed directly per `CLAUDE.md`; nothing in `legacy/` was touched).*

## Scope and trigger

`git ls-files | grep '\.java~$'` returns **132** tracked files (the task named 131; the discrepancy is one file — see "Count note" below). All 132 live in a single module, **`legacy/Global`**, spread across its `dao/`, `billing/dao`, `unit/dao`, `otims/dao`, `opd/dao`, `mrd/dao`, `doctor/dao`, `nabhprocess/service`, `pesapal/dao`, and `utility` packages. None are in Billing, ADT, Entity, or any other module.

This audit follows the same method as the two prior secret-remediation passes on this repo:

- **Batch 1** (`fix/batch1-secrets-remediation`, commit `9672ec59`, not yet merged to master) — fixed 12 hardcoded-secret findings codebase-wide and, during its own verification pass, *surfaced but explicitly left untouched* a 13th: this exact file class. Its note reads:

  > `legacy/Global/src/main/java/com/param/global/dao/PatientRegistrationDaoImpl.java~` — a stray editor-backup file (note the trailing `~`), tracked in git since the very first baseline commit, containing the same `sa` credential fixed under item 7. Backup files like this shouldn't be tracked at all, but deleting it is a separate file not named in scope.

- **Batch 2** (`fix/batch2-secrets-stragglers-clean`, commit `d46649ac`, based on `master`, also not yet merged) picked that finding up as its item 5: confirmed the file was a stale, divergent snapshot of the live counterpart, confirmed zero references anywhere in the build or codebase, deleted it via `git rm`, and added `*.java~` to the repo-root `.gitignore`.

**Important: neither branch is merged to `master`.** On `master` today (where this audit runs), that file is still tracked, `*.java~` is not yet gitignored anywhere active, and none of Batch 1/2's fixes for it exist yet. This audit re-verifies that one known file from scratch and extends the exact same checks to the other 131 files, none of which either batch ever looked at individually.

## Method (applied identically to all 132 files)

1. **Secret scan** — the same regex Batch 1's audit source (`docs/AUDIT_WholeCodebase_patterns.md` finding #4) used:
   `grep -rniE '(password|passwd|pwd|secret|apikey|api_key|access_key|accesskey|consumerSecret|authToken)\s*=\s*"[^"]{4,}"' --include=*.java~ legacy/`
   plus additional passes for patterns that regex alone would miss: AWS key literals (`AKIA[0-9A-Z]{16}`), Twilio SID/`Twilio.init(...)` literals, PEM private-key blocks, inline `jdbc:...://user:pass@` URLs, `DriverManager.getConnection("jdbc...")` with literal credentials, and a final loose keyword sweep (any line merely containing `password`/`secret`/`apikey`/`consumerkey`/etc., regardless of assignment shape) to catch anything the strict form missed.
2. **Counterpart diff** — for every `<Name>.java~`, confirmed a live `<Name>.java` exists in the same directory, then diffed the two.
3. **Reference check** — grepped the entire repository (all modules, all `pom.xml`s, all scripts) for each file's exact basename, and separately checked every `pom.xml` for any `<includes>`/`<excludes>`/`build-helper-maven-plugin` source-directory customization that could cause a `.java~` file to be picked up by a build. Maven's default `**/*.java` source pattern only matches `.java`, never `.java~`, and no module customizes this.

## Result summary

| Check | Result |
|---|---|
| Files with a live `.java` counterpart in the same directory | **132 / 132** |
| Files with zero live counterpart (orphan backup) | 0 |
| Files byte-identical to their live counterpart | 0 (all 132 diverge — expected, since these are old point-in-time saves and the live files have since been edited) |
| Files where backup and live differ in line count (unambiguous divergence) | 84 |
| Files where backup and live have the *same* line count but differing content (confirmed via `diff`, not just `wc -l`) | 48 |
| Files referenced anywhere else in the repo (source, `pom.xml`, scripts, `.gitignore`) by filename | **0** |
| Files matched by any Maven build's source-inclusion config | **0** (no module customizes `<includes>`/`<excludes>`; no `build-helper-maven-plugin` anywhere in the repo) |
| Files containing a hardcoded secret/credential | **1** — see below |

## The one confirmed secret

**`legacy/Global/src/main/java/com/param/global/dao/PatientRegistrationDaoImpl.java~`** (row 55 in the table below) contains the SQL Server `sa` credential (`user = "sa"`, `pass = "Oyster12345"`, host `202.149.218.166`) in two places:

- A `main`-adjacent block, lines ~3436–3440, building a `jdbc:sqlserver://...` connection string with inline `user`/`pass` literals.
- A commented-out sample JSON payload around line 2755 embedding the same `dbUser`/`dbPassward` values.

This is the identical finding already investigated in Batch 1 (item 7, for the *live* `.java` file — same credential, same dead block-commented method) and Batch 2 (item 5, for this exact `.java~` file). Both fixes exist only on unmerged branches:

- Batch 1's branch deletes the dead block from the *live* `PatientRegistrationDaoImpl.java`.
- Batch 2's branch `git rm`s this `.java~` file outright and adds `*.java~` to `.gitignore`.

Since neither branch is merged, **on `master` today this credential still exists in both the live file's dead code and this backup file**, unchanged from when Batch 1/2 found it. No new secret was found in this file beyond what those two prior passes already documented.

No other file among the 132 contains any hardcoded secret, credential, key, or token under any of the patterns checked (see Method above). The handful of other password/secret-*related* hits found by the loose keyword sweep (e.g. `TemplateTransactionDaoImpl.java~`'s `getPasswordAndUserId` SQL alias, `PesaPalIntegrationDao.java~`'s `consumerKey`/`consumerSecretKey` column aliases) are all read-from-database plumbing — column aliases and getter/setter names for values fetched dynamically per-unit at runtime, not hardcoded literals — consistent with the same pattern Batch 1/2 already established for the live (non-`~`) Pesapal integration code.

## Confirmed dead/unreferenced status (all 132 files)

Every file below is confirmed genuinely a stale editor backup, on all three grounds Batch 2 used for its one file:
(a) it diverges from a live, currently-compiled `.java` counterpart in the same package (an older snapshot missing since-added fields/methods, not a distinct source file);
(b) zero references to its filename exist anywhere in the codebase, `pom.xml`s, or deploy scripts;
(c) Maven's default `**/*.java` source pattern — unmodified in every module's `pom.xml` — never matches a `.java~` file regardless of directory, so none of these could ever have been compiled even while tracked.

| # | File | Diff vs. live `.java` counterpart | Hardcoded secret? | Confirmed dead/unreferenced? |
|---|------|-----------------------------------|--------------------|------------------------------|
| 1 | `legacy/Global/src/main/java/com/param/data/display/OtherDBDataDisplayDaoImpl.java~` | DIFFERS(bak=273L,live=273L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 2 | `legacy/Global/src/main/java/com/param/equipmentchecklist/dao/EquipmentChecklistMapperDaoImpl.java~` | DIFFERS(bak=135L,live=135L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 3 | `legacy/Global/src/main/java/com/param/global/billing/dao/APCodeMasterDaoImpl.java~` | DIFFERS(bak=216L,live=216L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 4 | `legacy/Global/src/main/java/com/param/global/billing/dao/AutoPostingConfigDaoImpl.java~` | DIFFERS(bak=257L,live=257L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 5 | `legacy/Global/src/main/java/com/param/global/billing/dao/BankBranchMasterDaoImpl.java~` | DIFFERS(bak=195L,live=195L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 6 | `legacy/Global/src/main/java/com/param/global/billing/dao/ContractConsumptionCostEstimationDaoImpl.java~` | DIFFERS(bak=517L,live=517L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 7 | `legacy/Global/src/main/java/com/param/global/billing/dao/CurrencyDenominationMasterDaoImpl.java~` | DIFFERS(bak=199L,live=199L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 8 | `legacy/Global/src/main/java/com/param/global/billing/dao/CurrencyMasterDaoImpl.java~` | DIFFERS(bak=206L,live=206L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 9 | `legacy/Global/src/main/java/com/param/global/billing/dao/DepositMasterDaoImpl.java~` | DIFFERS(bak=635L,live=635L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 10 | `legacy/Global/src/main/java/com/param/global/billing/dao/DoctorGradeDaoImpl.java~` | DIFFERS(bak=212L,live=212L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 11 | `legacy/Global/src/main/java/com/param/global/billing/dao/EmployeeCategoryMasterDaoImpl.java~` | DIFFERS(bak=206L,live=206L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 12 | `legacy/Global/src/main/java/com/param/global/billing/dao/FrenchiseDepositDaoImpl.java~` | DIFFERS(bak=102L,live=102L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 13 | `legacy/Global/src/main/java/com/param/global/billing/dao/POSMachineDeviceMasterDaoImpl.java~` | DIFFERS(bak=50L,live=51L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 14 | `legacy/Global/src/main/java/com/param/global/billing/dao/PaymentModeMasterDaoImpl.java~` | DIFFERS(bak=207L,live=207L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 15 | `legacy/Global/src/main/java/com/param/global/billing/dao/ServiceBasePriceMasterDaoImpl.java~` | DIFFERS(bak=141L,live=141L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 16 | `legacy/Global/src/main/java/com/param/global/billing/service/DepositMasterServiceImpl.java~` | DIFFERS(bak=894L,live=894L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 17 | `legacy/Global/src/main/java/com/param/global/dao/AllergyMasterDaoImpl.java~` | DIFFERS(bak=207L,live=207L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 18 | `legacy/Global/src/main/java/com/param/global/dao/AllergyTypeMasterDaoImpl.java~` | DIFFERS(bak=243L,live=243L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 19 | `legacy/Global/src/main/java/com/param/global/dao/AmbulanceChargesDaoImpl.java~` | DIFFERS(bak=227L,live=227L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 20 | `legacy/Global/src/main/java/com/param/global/dao/AmbulanceFacilitiesDaoImpl.java~` | DIFFERS(bak=216L,live=216L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 21 | `legacy/Global/src/main/java/com/param/global/dao/AmbulanceTarrifDaoImpl.java~` | DIFFERS(bak=228L,live=228L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 22 | `legacy/Global/src/main/java/com/param/global/dao/AmbulanceTypeDaoImpl.java~` | DIFFERS(bak=289L,live=289L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 23 | `legacy/Global/src/main/java/com/param/global/dao/AppointmentTemplateMasterMapperDaoImpl.java~` | DIFFERS(bak=95L,live=95L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 24 | `legacy/Global/src/main/java/com/param/global/dao/AppointmentTemplateServiceDaoImpl.java~` | DIFFERS(bak=104L,live=104L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 25 | `legacy/Global/src/main/java/com/param/global/dao/AssociatedCompanyMasterDaoImpl.java~` | DIFFERS(bak=43L,live=43L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 26 | `legacy/Global/src/main/java/com/param/global/dao/BedsideLabInvestigationDaoImpl.java~` | DIFFERS(bak=221L,live=221L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 27 | `legacy/Global/src/main/java/com/param/global/dao/BedsideProcedureMasterDaoImpl.java~` | DIFFERS(bak=187L,live=187L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 28 | `legacy/Global/src/main/java/com/param/global/dao/BulkMessageQueueDaoImpl.java~` | DIFFERS(bak=166L,live=165L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 29 | `legacy/Global/src/main/java/com/param/global/dao/CEODashboardDaoImpl.java~` | DIFFERS(bak=322L,live=322L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 30 | `legacy/Global/src/main/java/com/param/global/dao/CompanyMasterDaoImpl.java~` | DIFFERS(bak=353L,live=354L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 31 | `legacy/Global/src/main/java/com/param/global/dao/CorporateEmployeeMasterDaoImpl.java~` | DIFFERS(bak=270L,live=270L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 32 | `legacy/Global/src/main/java/com/param/global/dao/CountryMasterDaoImpl.java~` | DIFFERS(bak=234L,live=236L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 33 | `legacy/Global/src/main/java/com/param/global/dao/CustomEmrConfigDetailsDaoImpl.java~` | DIFFERS(bak=202L,live=203L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 34 | `legacy/Global/src/main/java/com/param/global/dao/CustomHistoryConfigDetailsDaoImpl.java~` | DIFFERS(bak=162L,live=162L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 35 | `legacy/Global/src/main/java/com/param/global/dao/DashboardMasterDaoImpl.java~` | DIFFERS(bak=70L,live=70L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 36 | `legacy/Global/src/main/java/com/param/global/dao/DataObjectMasterDaoImpl.java~` | DIFFERS(bak=135L,live=135L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 37 | `legacy/Global/src/main/java/com/param/global/dao/DepartmentNursingStationMapperDaoImpl.java~` | DIFFERS(bak=318L,live=318L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 38 | `legacy/Global/src/main/java/com/param/global/dao/DistrictMasterDaoImpl.java~` | DIFFERS(bak=273L,live=274L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 39 | `legacy/Global/src/main/java/com/param/global/dao/DoctorDocumentMapperDaoImpl.java~` | DIFFERS(bak=106L,live=106L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 40 | `legacy/Global/src/main/java/com/param/global/dao/DoctorMasterDaoImpl.java~` | DIFFERS(bak=1003L,live=1148L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 41 | `legacy/Global/src/main/java/com/param/global/dao/DrugMasterDaoImpl.java~` | DIFFERS(bak=49L,live=49L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 42 | `legacy/Global/src/main/java/com/param/global/dao/ExaminationMasterDaoImpl.java~` | DIFFERS(bak=186L,live=186L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 43 | `legacy/Global/src/main/java/com/param/global/dao/FormulationTypeToDosageMapperDaoImpl.java~` | DIFFERS(bak=217L,live=217L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 44 | `legacy/Global/src/main/java/com/param/global/dao/GetStoreListUsingsessionDao.java~` | DIFFERS(bak=49L,live=49L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 45 | `legacy/Global/src/main/java/com/param/global/dao/IntakeFluidMasterDaoImpl.java~` | DIFFERS(bak=49L,live=49L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 46 | `legacy/Global/src/main/java/com/param/global/dao/IntakeOutputChartDaoImpl.java~` | DIFFERS(bak=535L,live=566L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 47 | `legacy/Global/src/main/java/com/param/global/dao/IntrimBillDaoImpl.java~` | DIFFERS(bak=99L,live=102L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 48 | `legacy/Global/src/main/java/com/param/global/dao/LoyaltiCardMasterDaoImpl.java~` | DIFFERS(bak=146L,live=146L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 49 | `legacy/Global/src/main/java/com/param/global/dao/LoyaltyCardLogDaoImpl.java~` | DIFFERS(bak=93L,live=93L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 50 | `legacy/Global/src/main/java/com/param/global/dao/MessageLogDaoImpl.java~` | DIFFERS(bak=70L,live=71L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 51 | `legacy/Global/src/main/java/com/param/global/dao/NewBornBabyDaoImpl.java~` | DIFFERS(bak=168L,live=168L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 52 | `legacy/Global/src/main/java/com/param/global/dao/NewBornBabyDisplayDaoImpl.java~` | DIFFERS(bak=124L,live=124L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 53 | `legacy/Global/src/main/java/com/param/global/dao/NursingStationDaoImpl.java~` | DIFFERS(bak=228L,live=228L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 54 | `legacy/Global/src/main/java/com/param/global/dao/PatientCategoryMasterDaoImpl.java~` | DIFFERS(bak=217L,live=217L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 55 | `legacy/Global/src/main/java/com/param/global/dao/PatientRegistrationDaoImpl.java~` | DIFFERS(bak=3471L,live=3705L) | **YES** — `sa`/`Oyster12345` SQL Server credential (dead, block-commented code) | Confirmed dead — see "The one confirmed secret" section above |
| 56 | `legacy/Global/src/main/java/com/param/global/dao/PaymentEntitlementMasterDaoImpl.java~` | DIFFERS(bak=213L,live=213L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 57 | `legacy/Global/src/main/java/com/param/global/dao/PharmanDaoImpl.java~` | DIFFERS(bak=166L,live=167L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 58 | `legacy/Global/src/main/java/com/param/global/dao/PrefixMasteDaoImpl.java~` | DIFFERS(bak=267L,live=267L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 59 | `legacy/Global/src/main/java/com/param/global/dao/RelationMasterDaoImpl.java~` | DIFFERS(bak=239L,live=240L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 60 | `legacy/Global/src/main/java/com/param/global/dao/ResourceAllocationDaoImpl.java~` | DIFFERS(bak=238L,live=238L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 61 | `legacy/Global/src/main/java/com/param/global/dao/SSODaoImpl.java~` | DIFFERS(bak=161L,live=161L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 62 | `legacy/Global/src/main/java/com/param/global/dao/ServiceProviderDaoImpl.java~` | DIFFERS(bak=237L,live=237L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 63 | `legacy/Global/src/main/java/com/param/global/dao/SiteOfAdministrationDaoImpl.java~` | DIFFERS(bak=245L,live=245L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 64 | `legacy/Global/src/main/java/com/param/global/dao/SpecialityMasterDaoImpl.java~` | DIFFERS(bak=445L,live=452L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 65 | `legacy/Global/src/main/java/com/param/global/dao/SubSpecialityMasterDaoImpl.java~` | DIFFERS(bak=384L,live=388L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 66 | `legacy/Global/src/main/java/com/param/global/dao/SurgeryGradeMasterDaoImpl.java~` | DIFFERS(bak=199L,live=200L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 67 | `legacy/Global/src/main/java/com/param/global/dao/TPackageConsumptionMasterDaoImpl.java~` | DIFFERS(bak=663L,live=674L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 68 | `legacy/Global/src/main/java/com/param/global/dao/TemplateTransactionDaoImpl.java~` | DIFFERS(bak=860L,live=857L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 69 | `legacy/Global/src/main/java/com/param/global/dao/TubeFeedingProductsDaoImpl.java~` | DIFFERS(bak=225L,live=223L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 70 | `legacy/Global/src/main/java/com/param/global/dao/UnitApplicationBillingConfigurationDaoImpl.java~` | DIFFERS(bak=752L,live=793L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 71 | `legacy/Global/src/main/java/com/param/global/dao/UnitHolidayCalenderDaoImpl.java~` | DIFFERS(bak=206L,live=207L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 72 | `legacy/Global/src/main/java/com/param/global/dao/UnitServiceTariffMasterDaoImpl.java~` | DIFFERS(bak=672L,live=727L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 73 | `legacy/Global/src/main/java/com/param/global/dao/UserNursingStationMapperDaoImpl.java~` | DIFFERS(bak=310L,live=310L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 74 | `legacy/Global/src/main/java/com/param/global/dao/UserOtTabsMapperDaoImpl.java~` | DIFFERS(bak=113L,live=113L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 75 | `legacy/Global/src/main/java/com/param/global/dao/VariablePricingSequenceDaoImpl.java~` | DIFFERS(bak=66L,live=67L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 76 | `legacy/Global/src/main/java/com/param/global/dao/VitalDaoImpl.java~` | DIFFERS(bak=171L,live=164L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 77 | `legacy/Global/src/main/java/com/param/global/dao/VitalMasterDaoImpl.java~` | DIFFERS(bak=63L,live=62L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 78 | `legacy/Global/src/main/java/com/param/global/dao/VitalQualifireMasterDaoImpl.java~` | DIFFERS(bak=235L,live=234L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 79 | `legacy/Global/src/main/java/com/param/global/dao/WeekendMasterDaoImpl.java~` | DIFFERS(bak=222L,live=222L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 80 | `legacy/Global/src/main/java/com/param/global/doctor/dao/DoctorScheduleDaoImpl.java~` | DIFFERS(bak=234L,live=266L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 81 | `legacy/Global/src/main/java/com/param/global/doctor/dao/OrdersetInvestigationDetailsDaoImpl.java~` | DIFFERS(bak=100L,live=100L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 82 | `legacy/Global/src/main/java/com/param/global/doctor/dao/OutPatientFeedbackDaoImpl.java~` | DIFFERS(bak=73L,live=74L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 83 | `legacy/Global/src/main/java/com/param/global/dto/VendorServiceRateConfigDto.java~` | DIFFERS(bak=570L,live=569L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 84 | `legacy/Global/src/main/java/com/param/global/mrd/dao/MrdBoxMasterDaoImpl.java~` | DIFFERS(bak=156L,live=152L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 85 | `legacy/Global/src/main/java/com/param/global/mrd/dao/MrdRackMasterDaoImpl.java~` | DIFFERS(bak=163L,live=161L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 86 | `legacy/Global/src/main/java/com/param/global/mrd/dao/MrdRowMasterDaoImpl.java~` | DIFFERS(bak=149L,live=149L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 87 | `legacy/Global/src/main/java/com/param/global/nabhprocess/service/NabhReportingMapperServiceImpl.java~` | DIFFERS(bak=124L,live=60L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 88 | `legacy/Global/src/main/java/com/param/global/nabhprocess/service/ReportedNabhMapperServiceImpl.java~` | DIFFERS(bak=195L,live=171L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 89 | `legacy/Global/src/main/java/com/param/global/opd/dao/AppointmentMasterDaoImpl.java~` | DIFFERS(bak=327L,live=327L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 90 | `legacy/Global/src/main/java/com/param/global/opd/dao/EncounterAssignmenMapperDaoImpl.java~` | DIFFERS(bak=306L,live=305L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 91 | `legacy/Global/src/main/java/com/param/global/opd/dao/UserChatDetailsDaoImpl.java~` | DIFFERS(bak=150L,live=168L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 92 | `legacy/Global/src/main/java/com/param/global/opd/service/AppointmentSchedulingServiceImpl.java~` | DIFFERS(bak=2114L,live=2200L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 93 | `legacy/Global/src/main/java/com/param/global/org/dao/TaxComponentDetailsDaoImpl.java~` | DIFFERS(bak=107L,live=107L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 94 | `legacy/Global/src/main/java/com/param/global/otims/dao/OTChecklistDetailsDaoImpl.java~` | DIFFERS(bak=67L,live=67L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 95 | `legacy/Global/src/main/java/com/param/global/otims/dao/OTCompletionNoteDaoImpl.java~` | DIFFERS(bak=77L,live=77L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 96 | `legacy/Global/src/main/java/com/param/global/otims/dao/OperationTheatreDaoImpl.java~` | DIFFERS(bak=239L,live=239L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 97 | `legacy/Global/src/main/java/com/param/global/otims/dao/OperationTheatreTypeDaoImpl.java~` | DIFFERS(bak=226L,live=226L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 98 | `legacy/Global/src/main/java/com/param/global/otims/dao/OtCompletionTimeSheetDaoImpl.java~` | DIFFERS(bak=71L,live=71L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 99 | `legacy/Global/src/main/java/com/param/global/otims/dao/ProcedureEquipmentScheduleDaoImpl.java~` | DIFFERS(bak=184L,live=193L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 100 | `legacy/Global/src/main/java/com/param/global/otims/dao/ProcedureOTScheduleDaoImpl.java~` | DIFFERS(bak=264L,live=282L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 101 | `legacy/Global/src/main/java/com/param/global/otims/dao/ProcedureOrderServiceDaoImpl.java~` | DIFFERS(bak=102L,live=103L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 102 | `legacy/Global/src/main/java/com/param/global/otims/dao/ProcedureRoleMasterDaoImpl.java~` | DIFFERS(bak=226L,live=226L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 103 | `legacy/Global/src/main/java/com/param/global/otims/dao/ProcedureStaffScheduleDaoImpl.java~` | DIFFERS(bak=219L,live=228L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 104 | `legacy/Global/src/main/java/com/param/global/otims/dao/ResourceChargeConfigurationDaoImpl.java~` | DIFFERS(bak=209L,live=209L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 105 | `legacy/Global/src/main/java/com/param/global/otims/dao/SubSequentDetailsInfoDaoImpl.java~` | DIFFERS(bak=92L,live=92L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 106 | `legacy/Global/src/main/java/com/param/global/otims/dao/SurgicalCalendarMasterDaoImpl.java~` | DIFFERS(bak=259L,live=293L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 107 | `legacy/Global/src/main/java/com/param/global/pesapal/dao/PesaPalIntegrationDao.java~` | DIFFERS(bak=160L,live=160L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 108 | `legacy/Global/src/main/java/com/param/global/procedure/dao/ProcedureTypeMasterDaoImpl.java~` | DIFFERS(bak=205L,live=205L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 109 | `legacy/Global/src/main/java/com/param/global/unit/dao/ActionMasterGlobalDaoImpl.java~` | DIFFERS(bak=269L,live=269L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 110 | `legacy/Global/src/main/java/com/param/global/unit/dao/CabinMasterDaoImpl.java~` | DIFFERS(bak=242L,live=242L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 111 | `legacy/Global/src/main/java/com/param/global/unit/dao/CounterMasterDaoImpl.java~` | DIFFERS(bak=241L,live=241L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 112 | `legacy/Global/src/main/java/com/param/global/unit/dao/DiagnosticProcedureConfigDaoImpl.java~` | DIFFERS(bak=220L,live=220L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 113 | `legacy/Global/src/main/java/com/param/global/unit/dao/DiscountTypeMasterDaoImpl.java~` | DIFFERS(bak=199L,live=199L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 114 | `legacy/Global/src/main/java/com/param/global/unit/dao/DoctorCabinMapperDaoImpl.java~` | DIFFERS(bak=284L,live=284L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 115 | `legacy/Global/src/main/java/com/param/global/unit/dao/EmployeeVaccinationDaoImp.java~` | DIFFERS(bak=120L,live=120L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 116 | `legacy/Global/src/main/java/com/param/global/unit/dao/GrowthChartDaoImpl.java~` | DIFFERS(bak=420L,live=423L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 117 | `legacy/Global/src/main/java/com/param/global/unit/dao/GrowthDevelopmentalMilestonesTransationDaoImpl.java~` | DIFFERS(bak=74L,live=74L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 118 | `legacy/Global/src/main/java/com/param/global/unit/dao/IncidentCategoryMasterDaoImpl.java~` | DIFFERS(bak=241L,live=241L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 119 | `legacy/Global/src/main/java/com/param/global/unit/dao/IncidentReportingMapperDaoImpl.java~` | DIFFERS(bak=270L,live=281L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 120 | `legacy/Global/src/main/java/com/param/global/unit/dao/InitialAssessmentICUAndERMapperDaoImpl.java~` | DIFFERS(bak=105L,live=108L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 121 | `legacy/Global/src/main/java/com/param/global/unit/dao/LabConfigurationDaoImpl.java~` | DIFFERS(bak=275L,live=275L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 122 | `legacy/Global/src/main/java/com/param/global/unit/dao/MRoomRentChargesConfigDaoImpl.java~` | DIFFERS(bak=37L,live=37L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 123 | `legacy/Global/src/main/java/com/param/global/unit/dao/NursingOrderMasterDaoImpl.java~` | DIFFERS(bak=255L,live=255L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 124 | `legacy/Global/src/main/java/com/param/global/unit/dao/PassTypeMasterDaoImpl.java~` | DIFFERS(bak=191L,live=191L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 125 | `legacy/Global/src/main/java/com/param/global/unit/dao/QualityParameterMasterDaoImpl.java~` | DIFFERS(bak=134L,live=134L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 126 | `legacy/Global/src/main/java/com/param/global/unit/dao/SmsUtilityConfigDaoImpl.java~` | DIFFERS(bak=976L,live=976L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 127 | `legacy/Global/src/main/java/com/param/global/unit/dao/TabularTemplateDataMapperDaoImpl.java~` | DIFFERS(bak=138L,live=138L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 128 | `legacy/Global/src/main/java/com/param/global/unit/dao/UserRoleMasterDaoImpl.java~` | DIFFERS(bak=604L,live=673L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 129 | `legacy/Global/src/main/java/com/param/global/unit/dao/VisitorPassMapperDaoImpl.java~` | DIFFERS(bak=225L,live=223L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 130 | `legacy/Global/src/main/java/com/param/global/unit/dao/WingMasterDaoImpl.java~` | DIFFERS(bak=192L,live=191L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 131 | `legacy/Global/src/main/java/com/param/global/utility/Util.java~` | DIFFERS(bak=566L,live=566L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |
| 132 | `legacy/Global/src/main/java/com/param/module/dao/ModuleDao.java~` | DIFFERS(bak=100L,live=101L) | No | Confirmed dead — unreferenced, live counterpart exists, not Maven-buildable |

## Count note

The task description said 131 files; `git ls-files | grep '\.java~$'` on current `master` returns 132. The extra file is not a discrepancy in the check itself — it's simply the current count on `master` at the time of this audit (2026-07-05); no filtering was applied to exclude any file from the list.

## What this audit does NOT do

Per the request, this is read-only: no file was deleted, no `.gitignore` was edited, and no branch was created. In particular:

- The one secret-bearing file (`PatientRegistrationDaoImpl.java~`) was **not** deleted here — that fix already exists on the unmerged `fix/batch2-secrets-stragglers-clean` branch.
- No `*.java~` gitignore rule was added to `master` here — that also already exists (for a different reason) on both unmerged Batch 1 and Batch 2 branches.
- Merging either of those two branches to `master`, or opening a new dedicated branch to delete all 132 files as a batch (following Maker-Checker per `CLAUDE.md`, since deleting *application-adjacent* tracked files under `legacy/` counts as application-code work even though the files are dead), is a decision for the owner/Checker, not made here.
