# Click-Through Campaign — Human QA Across the 378 Rebuilt Screens

Prepared 2026-08-13 (session close). The owner runs the clicks; this is the
brief. Why this exists: every defect found by a person this run was one the
automated gates had passed — the dead filter across four screens, the empty
Service column, the row that saved but could not be found, the
fixture-dependent golden trace, the shell class 11-for-11 wrong. ~30 of the
378 have ever been opened by a person; the rest carry a check's signature,
not a human's. This document is the plan to fix that, one click at a time.

---

## 1. THE LIST, grouped by build batch, ordered by verification weakness

The actual risk ranking is the GATES THAT EXISTED when the batch was built.
The gate timeline (oldest → newest): the T0–T6 loop and its amendments
(including C7 verify-failures, C9 expected-findings) → the golden traces →
the both-directions probe rule → the freshness gate → the route-survival
check → the collision precheck → the render-trace gate (this run's end).

| Batch | Screens | Gates that existed when built | Risk |
|---|---|---|---|
| **census/early** (353) | everything before the transaction phase | predates C7, C9, the freshness gate, the both-directions rule, the route-survival check, the collision precheck | **HIGHEST — click everything** |
| **signed flows** (8) | transferScreen, donorRegistration, shiftClosing, phlebotomistHomeCollection, crossMatchRegister, bloodIssue, nursingOrder, labSampleType | T0–T6 + amendments + golden traces; predates the freshness gate's later form, both-directions, route-survival, collision precheck | HIGH — the writes are traced, the reads are not |
| **dashboard 1** (2) | fundStatus, storeStock | render-trace gate + freshness + route-survival + collision precheck | MEDIUM |
| **dashboard 2** (6) | erPatient, nurse, diagnostics, nursingAllocation, nursingShift, otManager | the above | MEDIUM |
| **reports** (2) | MLC worklist, incident-category | the above | MEDIUM |
| **seven** (7) | radiology pair, dueDeposit, consignment detail, MPShah trio | the above + the app's-own-login render method | LOWEST — but still click |

## 2. PER SCREEN: the route + THE ONE interaction

The single step where the automated gate is thinnest. Not a checklist —
one click per screen. Route prefix: the SPA routes under `/#/` (legacy
states keep their legacy routes; the rebuilt SPA mirrors them).

**KNOWN-DEFECT column (added 2026-08-13, DQ-091):** every screen whose
save/dropdown is ALREADY explained by the ledger carries a `KNOWN:` marker
in its row — the 18 confirmed phantoms (the drift screens), the 4
legacy-defect screens, and the bare-toast class. Do not re-derive by
clicking what the ledger already names; click to CONFIRM and log once.
Example rows: `allergyMaster — KNOWN: phantom #18 (cims_code/cims_type on
emr.m_allergy_master) + the bare-toast class`; `ambulanceMaster — KNOWN:
broken-feed proxy gap (the feeds 404 on the :4201 dev-server origin — an
env-class dev-server wiring issue, not dev-empty tables)`.

### census/early (353) — route + interaction

| Route | The one interaction |
|---|---|
| `accessControl` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `accountPayable` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `accountPayableMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `accountPayableStatus` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `addfixedAssetMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `addNewCorrectiveMaintenance` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `addNewDrugRecall` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `advisedVisits` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `ageGroupMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `aiButtonConfiguration` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `aIButtonMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `allInclusivePackage` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `allergyMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `allergyTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `ambulanceFacilitiesMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `ambulanceMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `ambulanceServiceProviderMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `ambulanceTarrifMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `ambulanceTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `amenityMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `anaesthesiaType` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `antibiotic` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `antibioticsClass` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `antibioticsGroup` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `APmaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `applicationConfiguration` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `appointmentList` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `appointmentScheduling` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `appointmentSchedulingUnitMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `ARmaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `areaMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `assetCategoryMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `assetIssueAcceptance` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `assetIssueIndent` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `assetReturn` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `assetReturnAcceptance` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `assetReturnAcceptanceDetails` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `assetScheduler` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `assetTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `assignLoyaltyCard` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `assigningPickerDispenser` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `autoServicePostingConfiguration` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `autoclaveMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `bacteriaClassification` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `bankBranch` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `bankMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `bedCategoryMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `bedMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `bedTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `billingBedCategoryMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `billingGroupMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `bloodBagCapacity` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `bloodBagType` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `bloodBankMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `bloodBankTypeComponent` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `bloodComponent` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `bloodDonortype` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `bloodGroupMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `bloodGroupMatchingMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `bloodTest` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `bookAppointment` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `bookView` | the render itself |
| `boxMasters` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `cabinDisplay` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `cabinMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `campMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `cancellationMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `cardTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `cashOpeningBalance` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `chargeConversion` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `checklistEquipmentMapper` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `checklistEquipmentMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `checklistItem` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `checklistMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `cityMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `clinicMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `colonyTestWithMediaMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `combinationMedicationMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `commonMaster/:url` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `companyContractMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `consentMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `consentType` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `consentTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `consumableBarcodeConfig` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `corporateMasterGlobal` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `correctiveMaintenance` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `correctiveMaintenanceReturnWorklist` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `correctiveMaintenanceScheduleWorklist` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `correctiveMaintenanceWorklist` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `counterMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `counterWorklist` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `countryMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `couponTemplateMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `createTabularDataTemplates` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `createTabularTemplates` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `currencyMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `currencydenomination` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `dashboard` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `dashboardInfo` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `daywiseDietTemplate` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `degreeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `deliveryTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `departmentMasterUnit` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `departmentNursingStationMapper` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `departmentStoreMapper` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `depositMatrixConfig` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `designationMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `detailScheduleWorklist` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `detailsAssetIssueAcceptance` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `detailsAssetReturn` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `detailsCorrectiveMaintenance` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `detailsCorrectiveMaintenanceScheduleWorklist` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `detailsCorrectiveMaintenanceWorklist` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `detailsFixedAssetMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `dietTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `dietUnitMater` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `discountCategoryMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `discountRights` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `dishMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `dispensingMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `display/:orgId/:unitId/:cookieskioskId/:userId` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `displayCall` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `districtMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `doctorCabinMapper` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `doctorCategory` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `doctorCategoryMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `doctorConsultation` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `doctorDashboard` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `doctorDashboardInfo` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `doctorReferralShare` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `doctorRegistration` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `doctorSlabMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `doctorWise` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `doctorwiseShare` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `donorRejectionMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `doseMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `EHCpackage` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `eitherOrPackage` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `employeeCategory` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `employeeCategoryMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `employeeSurvey` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `encounter` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `encounterQueueEHC` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `encounterQueueManagement` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `encounterTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `fixedAssetMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `fixedAssetType` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `floorMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `formulationTypeROAMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `frequencyMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `fundApproval` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `fundApprovedList` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `fundReconciliation` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `fundRequisition` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `generalInsuranceMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `genericMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `genericScheduleMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `GLcode` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `globalDoctorShare` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `gradeCreationMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `GRN` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `growthChart` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `growthChartMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `histopathologyTest` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `holidayCalendarMasterUnit` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `holidayCalenderMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `home` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `hospitalAccountMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `hospitalMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `icpcCodesConfiguration` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `ICPCMapper` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `icutypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `identificationMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `immunizationSchedule` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `implantMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `inPatientFeedback/:organizationId/:unitId/:patientId/:visitType/:visitTypeId/:admissionId` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `incidentCategoryMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `inputOutputCharts` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `instructionMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `insuranceMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `InsuranceTPAMapping` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `interimHandover` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `IPConsultationConfiguration` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `IPdefaultServiceConfig` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `keyIngredientsMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `kitCreation` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `kitMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `licenseTypeMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `linenIssue` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `linenIssueacceptance` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `linenIssueAcceptanceDetails` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `linenIssuePendingDetails` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `loyaltyCardConfiguration` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `loyaltyCardCreation` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `machineMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `machineParameterMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `manufacturerMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `mappingofMicroorganism` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `maritalstatusMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `mealMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `mealPreferenceMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `MISReports` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `modalityMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `modalityTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `mortuaryBedMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `multicounterPackage` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `multiparameterTestMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `nationalityMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `newAccessControl` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `notesMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `nurseDashboardInfo` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `nursingOrderMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `nursingStationMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `nursingStationWardMapping` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `nutritionalMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `occupationMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `OPConsulationConfigurationMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `OPDCoverSheet` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `operationTable` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `operationTableType` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `operation_Theatre` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `operationTheatreType` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `orderSet` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `orderSetList` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `organizationMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `OTIMSroleCreation` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `outPatientFeedback/:organizationId/:unitId/:patientId/:visitType/:visitTypeId/:encounterId` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `outSideHospitalMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `packageMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `packageWithCAP` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `parameterFormula` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `parameterMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `passBedCategoryMapper` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `passTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `patientCategory` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `patientList` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `patientRegistration` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `patientSourceMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `patientTypeDetails` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `patientType` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `paymentEntitmentMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `paymentModeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `periodicityService` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `pesapalProcessPage` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `pharmacyformulationTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `pharmacyStrengthUnitMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `pharmacyTariffMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `pharman` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `PMRequest` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `PMSchedule` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `prefixConfiguration` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `prefixMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `procedureConfiguration` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `procedureConfigurations` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `procedureTemplate` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `procedureType` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `productRestriction/productCategoryMapping` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `profilepanelTestMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `QMS` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `QMScounterMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `QMSdepartmentMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `QMSkioskMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `QMSMasterCall` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `qmslogo` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `qualificationMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `qualityParameterMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `raceMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `rackMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `rackMasters` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `reasonMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `reasonOfDiscard` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `reasonTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `referralMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `referralTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `rejectedAssetAcceptance` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `rejectionMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `relationMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `resetPwdconfirmPwd` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `resourceAllocationMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `resourceChargesConfiguration` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `resourceConfiguration` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `RMOAllocationMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `roleCreation` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `roleManagement` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `roomMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `roomModality` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `roundOffConfiguration` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `routeAdministration` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `rowMasters` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `sampleTypeMediaMappingMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `schedulePriceUnitService` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `scheduleWise` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `scheduleWorklist` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `screeningTest` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `selfRegistration` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `sensitivityTestResult` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `serviceMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `serviceTariffMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `shiftMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `shortLeaveReasonMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `siteOfAdministration` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `smsConfiguration` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `specialityMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `specimanMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `staffRegistration` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `stateMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `sterilizationTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `storageLocationMasterForSlideBlocks` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `storageunitMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `storeTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `storewiseGenericMapper` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `subdepartmentMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `subSpecialityMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `subsequentSurgeryConfiguration` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `surgeryGradeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `surgeryMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `surgicalCalendar` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `systemMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `systemObservationMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `tallyInterfacing` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `tariffMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `taxComponentMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `taxMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `templateICDCodeMapping` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `templateMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `testMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `ticketSystemMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `tokenDisplay` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `TPAmaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `transactionTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `transfusionTransmittedDiseases` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `tubeFeedingProducts` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `unitServiceBasePrice` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `unitServiceMapping` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `unitServiceMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `subDepartmentMasterUnit` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `uomMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `userAuthentication` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `userCounterMapper` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `userNursingStationMapper` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `userStoreMapper` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `vaccinationMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `vaccinationSchedule` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `viewAssetScheduler` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `visitTypeMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `vitalQualifierMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `wardMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `wardStoreMapper` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `webAppointmentBooking/:orgId/:unitId` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `webAppointmentBookingConfirmation` | the render itself |
| `weekendMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `wingMaster` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `wrapperExpiryMaster` | the data: REAL or EMPTY (a live feed vs a zero-row table) |

### signed flows (8)

| Route | The one interaction |
|---|---|
| `bloodIssue` | the render itself |
| `crossMatchRegister` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `donorRegistration` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `labSampleType` | the render itself |
| `nursingOrder` | the render itself |
| `phlebotomistHomeCollection` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `shiftClosing` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `transferScreen` | the render itself |

### dashboards + reports + seven (17)

| Route | The one interaction |
|---|---|
| `fundStatusDashboard` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `storeStockDashboard` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `diagnosticsDashboard` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `erPatientDashboard` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `nurseDashboard` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `nursingAllocationDashboard` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `nursingShiftDashboard` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `otManagerDashboard` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `incidentCategory` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `mlcReportingWorklist` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `detailsConsignmentManagerDashboard` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `dueDepositReport` | the data: REAL or EMPTY (a live feed vs a zero-row table) |
| `mpShahEncounterList` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `mpShahEncounterSummary` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `mpShahPatientList` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `radiologyReportReleaseWorkList` | the PRIMARY WRITE (save) — the gate is thinnest where the write lands |
| `radiologyReportValidation` | the data: REAL or EMPTY (a live feed vs a zero-row table) |

## 3. LEGITIMATELY EMPTY IN DEV (do not report correct behaviour as a defect)

- **ambulanceMaster's three dropdowns** (Ambulance Type / Service Provider /
  Available Facilities) are CONFIRMED dev-empty: `adt.m_ambulance_type`,
  `adt.m_service_provider`, `adt.m_ambulance_facilities` all count 0 in
  psql (checked 2026-08-13, DQ-093). The feeds 200 with empty lists; the
  placeholder-only dropdowns are CORRECT. Do not log as a defect.

- **Zero-row worklists/reports** are correct empties: the radiology worklists,
  the MPShah encounter list/summary (no encounters in dev), the deposit-side
  reports without seeded patients, the nursing/incident lists.
- **m_nationality_master has 0 rows** — the donor screen's nationality dropdown
  being empty is a dev-data gap, not a defect (DQ-074).
- **The LIS screens** show only what the seed created: SAMPLE-1 sample type,
  one sample, one bag/donor pair. Anything beyond that is a correct empty.
- **The transaction screens** (transfer, crossMatch, bloodIssue, nursingOrder)
  show the SEEDED fixtures only — SMOKE-BAG1, DON-5, the CM fixture, the
  sample-type SAMPLE-1. A real user's flow needs the seeded state; absent it,
  the list is honestly empty.
- **The number-config screens** carry the 4 seeded prefix configs (123/146/149/164).
- **The seven read-only buildables** — the tables are seeded or empty; an
  empty table here is a correct empty, a REAL row is the seeded patient
  (Mr. Anuprit K / PUHID-2339).

## 4. DEFECT-LOGGING SHAPE (one line each; copy, fill, go)

    ROUTE:       <the /#/ route>
    WHAT I DID:  <the one click, exactly>
    EXPECTED:    <what a correct screen does>
    HAPPENED:    <what actually happened, incl. any error text>

Log them in a running note; the pattern (route → did → expected → happened)
is the evidence. If the screen 500s, add the ERR- reference from the toast.

## 5. SAMPLING PLAN

**ENVIRONMENT PREREQUISITE (added 2026-08-13, DQ-092 — read before any run):**
a campaign run against a misconfigured dev server produces noise, not
findings. Before clicking: (1) THE SERVED BRANCH must be stated and pinned —
the dev server compiles whatever tree the repo root is checked out on, and
"the fix is merged" says nothing about what the browser executes. Record
`git branch --show-current` at the repo root before the run and confirm the
code under test is an ancestor of it; re-record after every branch switch
(switching mid-investigation silently changes what the owner tests — the
2026-08-14 served-tree lesson, ledger #17, cost an hour and a false "the fix
did not work"). A claim about what a click exercised names the served
branch; (2) the dev server must run WITH the proxy (`ng serve` reads
angular.json's `proxyConfig: proxy.conf.json` — the file maps `/api/**`,
`/adt/**`, etc. to :8080; a server started without it makes every relative
API call 404 on the :4201 origin); (3) the SPA session must come from the
app's own login flow (the :8080 login + the token in the SPA session — the
`setup_spa_session.py` flow); (4) auxiliary calls (counts) must not toast —
the per-service path convention (DQ-092): every service call must hit an
endpoint the backend actually maps, and aux calls degrade. A screen whose
ONLY failure is a count-404 under a misconfigured server is an environment
artifact, not a screen defect.

378 screens at ~1 minute each is ~6 hours. Propose:

1. **FIRST PASS — the census/early batch (353 screens)
   + every write-path screen across all batches (308 of 378 have a write path).**
   The census batch is the weakest (predates every gate); the write-path
   screens are where a broken save hides — and this run proved saves are
   where the gates go thin (dead filter, saved-but-not-found, the unwired
   trio saves).
2. **The defect rate in that subset tells you about the rest.** If the
   earliest + write-heavy subset shows <5% defects, the later read-only
   batches (gated by render-trace + freshness + collision precheck) can be
   sampled at 10–20% rather than fully clicked. If it shows ≥10%, plan the
   full pass — the gates have been capable of passing while incapable, and
   the early batch is the measure of how often.
3. **The read-only remainder** (the 64 feed screens + the static 6) is the
   cheapest: a render-and-look, ~20 seconds each, sample at 20%.

Total first-pass estimate: the census/early 353 + the
write-path screen count overlaps heavily (most of the 378 are writes) — the
honest first pass is the census batch fully plus every write screen's one
interaction, ~2.5–3 hours in two sittings.

---

*Prepared by Hermes (2026-08-13) — preparation only, nothing built. The gates
that pass are listed so the risk ranking is the actual history, not a guess.*
