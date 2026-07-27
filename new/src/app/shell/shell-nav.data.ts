import { ShellNavSection } from './shell.models';

/**
 * Sidebar content for the 45 "primary" (staff-navigated, non-parameterized,
 * non-detail) screens rebuilt so far (26 through Phase 4 batch 3, +9 in
 * batch 4, +10 in batch 5 -- Queue Management, User Management,
 * Configuration newly added; Admin/HR-Staff Master, ADT-Registration,
 * ADT-Ambulance expanded), grouped by the same module taxonomy
 * `tools/angular-audit/`'s `states_enriched.json` already assigns each
 * screen (not an invented grouping). Deliberately excludes: `accessDenied`
 * (system route), `display`/`webAppointmentBooking`/
 * `webAppointmentBookingConfirmation`/`pesapalProcessPage` (public,
 * parameterized, or callback-only routes nobody navigates to via a menu),
 * `bookAppointment`/`outPatientFeedback`/`inPatientFeedback` (reached only
 * via another screen's flow, not a direct destination), and every
 * `detailsX`/`XDetails` screen (reached only via a row action on its own
 * list screen, matching legacy's sidebar convention of listing top-level
 * states only). See docs/ANGULAR_HYBRID_DEPLOYMENT_PROPOSAL.md Part 2 for
 * why this starts small and grows per batch rather than attempting
 * legacy's full ~784-link sidebar upfront.
 */
export const SHELL_NAV_SECTIONS: ShellNavSection[] = [
  {
    heading: 'Dashboard',
    items: [
      { path: '/home', title: 'Home' },
      { path: '/doctorDashboard', title: 'Doctor Dashboard' },
      { path: '/dashboard', title: 'Dashboard' },
    ],
  },
  {
    heading: 'Billing',
    items: [
      { path: '/billingGroupMaster', title: 'Billing Group Master' },
      { path: '/insuranceMaster', title: 'Insurance Master' },
      { path: '/bankMaster', title: 'Bank Master' },
      { path: '/bankBranch', title: 'Bank Branch' },
      { path: '/cardTypeMaster', title: 'Card Type Master' },
      { path: '/currencyMaster', title: 'Currency Master' },
      { path: '/currencydenomination', title: 'Currency Denomination Master' },
      { path: '/discountCategoryMaster', title: 'Discount Category Master' },
      { path: '/GLcode', title: 'GL Code' },
      { path: '/gradeCreationMaster', title: 'Grade Creation Master' },
      { path: '/qmslogo', title: 'QMS Logo' },
      { path: '/qualityParameterMaster', title: 'Quality Parameter Master' },
      { path: '/hospitalAccountMaster', title: 'Hospital Account Master' },
      { path: '/transactionTypeMaster', title: 'Transaction Type Master' },
      { path: '/ARmaster', title: 'AR Master' },
    ],
  },
  {
    heading: 'Nursing',
    items: [
      { path: '/inputOutputCharts', title: 'Intake Fluid Output(ml) Charts' },
      { path: '/periodicityService', title: 'Periodicity Service' },
    ],
  },
  {
    heading: 'Admin',
    items: [
      { path: '/organizationMaster', title: 'Organization Master' },
      { path: '/specialityMaster', title: 'Department Master' },
      { path: '/subSpecialityMaster', title: 'Sub Department Master' },
      { path: '/raceMaster', title: 'Race Master' },
      { path: '/stateMaster', title: 'State Master' },
      { path: '/districtMaster', title: 'District Master' },
      { path: '/relationMaster', title: 'Relation Master' },
      { path: '/maritalstatusMaster', title: 'Marital Status Master' },
      { path: '/occupationMaster', title: 'Occupation Master' },
      { path: '/prefixMaster', title: 'Prefix Master' },
      { path: '/nationalityMaster', title: 'Nationality Master' },
      { path: '/countryMaster', title: 'Country Master' },
      { path: '/cityMaster', title: 'City Master' },
      { path: '/encounterTypeMaster', title: 'Encounter Type Master' },
      { path: '/consentTypeMaster', title: 'Consent Type Master' },
      { path: '/identificationMaster', title: 'Identification Card Master' },
      { path: '/hospitalMaster', title: 'Hospital Master' },
      { path: '/areaMaster', title: 'Area Master' },
      { path: '/holidayCalenderMaster', title: 'Holiday Calendar Master' },
      { path: '/clinicMaster', title: 'Clinic Master' },
      { path: '/outSideHospitalMaster', title: 'Out Side Hospital Master' },
      { path: '/reasonMaster', title: 'Reason Master' },
      { path: '/reasonTypeMaster', title: 'Reason Type Master' },
      { path: '/rejectionMaster', title: 'Rejection Master' },
      { path: '/systemMaster', title: 'System Master' },
      { path: '/systemObservationMaster', title: 'System Observations Master' },
      { path: '/ICPCMapper', title: 'ICPC Mapper' },
      { path: '/icpcCodesConfiguration', title: 'ICPC Codes Configuration' },
      { path: '/instructionMaster', title: 'Instruction Master' },
    ],
  },
  {
    heading: 'Ward/Unit Master',
    items: [
      { path: '/wardMaster', title: 'Ward Master' },
      { path: '/floorMaster', title: 'Floor Master' },
      { path: '/amenityMaster', title: 'Amenity Master' },
      { path: '/wingMaster', title: 'Wing Master' },
      { path: '/counterMaster', title: 'Counter Master' },
      { path: '/cabinMaster', title: 'Cabin Master' },
      { path: '/rowMasters', title: 'MRD Row Master' },
      { path: '/boxMasters', title: 'MRD Box Master' },
      { path: '/rackMasters', title: 'MRD Rack Master' },
      { path: '/mealPreferenceMaster', title: 'Meal Preference Master' },
      { path: '/weekendMaster', title: 'Weekend Master' },
      { path: '/roomMaster', title: 'Room Master' },
      { path: '/roomModality', title: 'Room Modality' },
      { path: '/ticketSystemMaster', title: 'Ticket Master' },
      { path: '/aIButtonMaster', title: 'AI Button Master' },
      { path: '/employeeSurvey', title: 'Employee Satisfaction Survey' },
      { path: '/holidayCalendarMasterUnit', title: 'Holiday Calendar Master' },
      { path: '/mortuaryBedMaster', title: 'Mortuary Bed Master' },
      { path: '/nursingStationMaster', title: 'Nursing Station Master' },
      { path: '/departmentMasterUnit', title: 'Department Master (Unit)' },
    ],
  },
  {
    heading: 'ADT - Registration',
    items: [
      { path: '/bedCategoryMaster', title: 'Bed Category Master' },
      { path: '/bedTypeMaster', title: 'Ward Bed Type Master' },
      { path: '/shortLeaveReasonMaster', title: 'Short Leave Reason Master' },
      { path: '/passTypeMaster', title: 'Pass Type Master' },
    ],
  },
  {
    heading: 'ADT - Ambulance',
    items: [
      { path: '/ambulanceTypeMaster', title: 'Ambulance Type Master' },
      { path: '/ambulanceMaster', title: 'Ambulance Master' },
      { path: '/ambulanceFacilitiesMaster', title: 'Ambulance Facilities Master' },
      { path: '/resourceAllocationMaster', title: 'Resource Allocation Master' },
      { path: '/ambulanceServiceProviderMaster', title: 'Ambulance Service Provider Master' },
    ],
  },
  {
    heading: 'HR/Staff Master',
    items: [
      { path: '/doctorCategory', title: 'Doctor Category' },
      { path: '/doctorCategoryMaster', title: 'Doctor Category Master' },
      { path: '/qualificationMaster', title: 'Qualification Master' },
      { path: '/degreeMaster', title: 'Degree Master' },
      { path: '/employeeCategoryMaster', title: 'Employee Category Master' },
      { path: '/employeeCategory', title: 'Employee Category' },
      { path: '/designationMaster', title: 'Designation Master' },
    ],
  },
  {
    heading: 'Queue Management',
    items: [
      { path: '/QMSkioskMaster', title: 'QMS Kiosk Master' },
      { path: '/QMSdepartmentMaster', title: 'QMS Department Master' },
      { path: '/QMScounterMaster', title: 'QMS Counter Master' },
      { path: '/displayCall', title: 'QMS Token Display' },
      { path: '/QMSMasterCall', title: 'QMS Token' },
    ],
  },
  {
    heading: 'User Management',
    items: [
      { path: '/userStoreMapper', title: 'User Store Mapper' },
      { path: '/doctorCabinMapper', title: 'Doctor Cabin Mapper' },
      { path: '/userCounterMapper', title: 'User Counter Mapper' },
      { path: '/departmentStoreMapper', title: 'Department Store Mapper' },
      { path: '/roleManagement', title: 'Role Management' },
      { path: '/userAuthentication', title: 'User Authentication' },
      { path: '/roleCreation', title: 'Role Creation' },
      { path: '/newAccessControl', title: 'Access Control' },
      { path: '/OTIMSroleCreation', title: 'OTIMS Role Creation' },
    ],
  },
  {
    heading: 'Configuration',
    items: [
      { path: '/prefixConfiguration', title: 'Prefix Configuration' },
      { path: '/smsConfiguration', title: 'SMS Configuration' },
    ],
  },
  {
    heading: 'Reports',
    items: [{ path: '/MISReports', title: 'MIS Report' }],
  },
  {
    heading: 'Procurement',
    items: [
      { path: '/GRN', title: 'Good Receipt Note' },
      { path: '/implantMaster', title: 'Implant Master' },
      { path: '/storeTypeMaster', title: 'Store Type Master' },
    ],
  },
  {
    heading: 'Pharmacy',
    items: [
      { path: '/assigningPickerDispenser', title: 'Pending List for Assigning Picker' },
      { path: '/manufacturerMaster', title: 'Manufacturer Master' },
      { path: '/genericMaster', title: 'Generic Master' },
      { path: '/frequencyMaster', title: 'Frequency Master' },
      { path: '/pharmacyformulationTypeMaster', title: 'Formulation Type Master' },
    ],
  },
  {
    heading: 'Appointment Scheduling',
    items: [
      { path: '/pharman', title: 'Pharman Page' },
      { path: '/modalityTypeMaster', title: 'Modality Type Master' },
      { path: '/modalityMaster', title: 'Modality Master' },
    ],
  },
  {
    heading: 'Asset Management',
    items: [
      { path: '/assetCategoryMaster', title: 'Asset Category Master' },
      { path: '/fixedAssetType', title: 'Fixed Asset Type' },
      { path: '/viewAssetScheduler', title: 'Asset PMS Scheduler' },
      { path: '/scheduleWorklist', title: 'Schedule Worklist' },
      { path: '/PMSchedule', title: 'PM Schedule' },
      { path: '/PMRequest', title: 'PM Request' },
      { path: '/assetScheduler', title: 'Asset PMS Scheduler' },
      { path: '/correctiveMaintenance', title: 'Corrective Maintenance Request' },
      { path: '/addNewCorrectiveMaintenance', title: 'Add Corrective Maintenance Request' },
      { path: '/correctiveMaintenanceWorklist', title: 'Corrective Maintenance Worklist' },
      { path: '/correctiveMaintenanceScheduleWorklist', title: 'Corrective Maintenance Schedule Worklist' },
      { path: '/correctiveMaintenanceReturnWorklist', title: 'Corrective Maintenance Return Worklist' },
      { path: '/assetIssueAcceptance', title: 'Asset Issuance Acceptance' },
      { path: '/assetReturnAcceptance', title: 'Asset Return Acceptance' },
      { path: '/rejectedAssetAcceptance', title: 'Rejected Asset Acceptance' },
      { path: '/assetTypeMaster', title: 'Asset Type Master' },
      { path: '/consumableBarcodeConfig', title: 'Consumable Barcode Configuration' },
      { path: '/fixedAssetMaster', title: 'Fixed Asset Master' },
      { path: '/wardStoreMapper', title: 'Ward Store Mapper' },
      { path: '/addfixedAssetMaster', title: 'Add Fixed Asset Opening Balance' },
      { path: '/assetReturn', title: 'Asset Return' },
    ],
  },
  {
    heading: 'Linen & Laundry',
    items: [
      { path: '/linenIssueacceptance', title: 'Linen Issuance Acceptance' },
      { path: '/linenIssue', title: 'Linen Issue' },
    ],
  },
  {
    heading: 'Templates',
    items: [
      { path: '/createTabularDataTemplates', title: 'Tabular Data Template' },
      { path: '/createTabularTemplates', title: 'Tabular Template' },
    ],
  },
  {
    heading: 'OPD',
    items: [{ path: '/advisedVisits', title: 'Advised Visits' }],
  },
  {
    heading: 'Incident Reporting',
    items: [{ path: '/incidentCategoryMaster', title: 'Category Master' }],
  },
  {
    heading: 'Blood Bank',
    items: [
      { path: '/bloodGroupMaster', title: 'Blood Group Master' },
      { path: '/bloodDonortype', title: 'Blood Donor Type Master' },
      { path: '/bloodBagCapacity', title: 'Blood Bag Capacity Master' },
      { path: '/bloodBagType', title: 'Blood Bag Type Master' },
    ],
  },
  {
    heading: 'CSSD',
    items: [{ path: '/sterilizationTypeMaster', title: 'Sterilization Type Master' }],
  },
  {
    heading: 'OT/Theatre',
    items: [
      { path: '/surgeryGradeMaster', title: 'Surgery Grade Master' },
      { path: '/operationTableType', title: 'Operation Table Type' },
      { path: '/anaesthesiaType', title: 'Anaesthesia Type' },
    ],
  },
  {
    heading: 'Diet & Nutrition',
    items: [{ path: '/nutritionalMaster', title: 'Nutritional Master' }],
  },
];
