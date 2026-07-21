import { ShellNavSection } from './shell.models';

/**
 * Sidebar content for the 26 "primary" (staff-navigated, non-parameterized,
 * non-detail) screens rebuilt so far, grouped by the same module taxonomy
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
    items: [{ path: '/organizationMaster', title: 'Organization Master' }],
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
