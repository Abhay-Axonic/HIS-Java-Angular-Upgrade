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
    heading: 'Billing',
    items: [
      { path: '/billingGroupMaster', title: 'Billing Group Master' },
      { path: '/insuranceMaster', title: 'Insurance Master' },
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
      { path: '/nationalityMaster', title: 'Nationality Master' },
      { path: '/countryMaster', title: 'Country Master' },
      { path: '/cityMaster', title: 'City Master' },
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
    ],
  },
  {
    heading: 'ADT - Registration',
    items: [
      { path: '/bedCategoryMaster', title: 'Bed Category Master' },
      { path: '/bedTypeMaster', title: 'Ward Bed Type Master' },
      { path: '/shortLeaveReasonMaster', title: 'Short Leave Reason Master' },
    ],
  },
  {
    heading: 'ADT - Ambulance',
    items: [
      { path: '/ambulanceTypeMaster', title: 'Ambulance Type Master' },
      { path: '/ambulanceMaster', title: 'Ambulance Master' },
      { path: '/ambulanceFacilitiesMaster', title: 'Ambulance Facilities Master' },
      { path: '/resourceAllocationMaster', title: 'Resource Allocation Master' },
    ],
  },
  {
    heading: 'HR/Staff Master',
    items: [
      { path: '/doctorCategory', title: 'Doctor Category' },
      { path: '/qualificationMaster', title: 'Qualification Master' },
      { path: '/degreeMaster', title: 'Degree Master' },
      { path: '/employeeCategoryMaster', title: 'Employee Category Master' },
      { path: '/designationMaster', title: 'Designation Master' },
    ],
  },
  {
    heading: 'Queue Management',
    items: [
      { path: '/QMSkioskMaster', title: 'QMS Kiosk Master' },
      { path: '/QMSdepartmentMaster', title: 'QMS Department Master' },
    ],
  },
  {
    heading: 'User Management',
    items: [
      { path: '/userStoreMapper', title: 'User Store Mapper' },
      { path: '/doctorCabinMapper', title: 'Doctor Cabin Mapper' },
      { path: '/userCounterMapper', title: 'User Counter Mapper' },
    ],
  },
  {
    heading: 'Configuration',
    items: [{ path: '/prefixConfiguration', title: 'Prefix Configuration' }],
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
    ],
  },
  {
    heading: 'Pharmacy',
    items: [{ path: '/assigningPickerDispenser', title: 'Pending List for Assigning Picker' }],
  },
  {
    heading: 'Appointment Scheduling',
    items: [{ path: '/pharman', title: 'Pharman Page' }],
  },
  {
    heading: 'Asset Management',
    items: [
      { path: '/assetCategoryMaster', title: 'Asset Category Master' },
      { path: '/fixedAssetType', title: 'Fixed Asset Type' },
      { path: '/viewAssetScheduler', title: 'Asset PMS Scheduler' },
      { path: '/scheduleWorklist', title: 'Schedule Worklist' },
      { path: '/PMSchedule', title: 'PM Schedule' },
      { path: '/assetScheduler', title: 'Asset PMS Scheduler' },
      { path: '/correctiveMaintenance', title: 'Corrective Maintenance Request' },
      { path: '/addNewCorrectiveMaintenance', title: 'Add Corrective Maintenance Request' },
      { path: '/correctiveMaintenanceWorklist', title: 'Corrective Maintenance Worklist' },
      { path: '/correctiveMaintenanceScheduleWorklist', title: 'Corrective Maintenance Schedule Worklist' },
      { path: '/correctiveMaintenanceReturnWorklist', title: 'Corrective Maintenance Return Worklist' },
      { path: '/assetIssueAcceptance', title: 'Asset Issuance Acceptance' },
      { path: '/assetReturnAcceptance', title: 'Asset Return Acceptance' },
      { path: '/rejectedAssetAcceptance', title: 'Rejected Asset Acceptance' },
    ],
  },
  {
    heading: 'Linen & Laundry',
    items: [{ path: '/linenIssueacceptance', title: 'Linen Issuance Acceptance' }],
  },
  {
    heading: 'Templates',
    items: [{ path: '/createTabularDataTemplates', title: 'Tabular Data Template' }],
  },
];
