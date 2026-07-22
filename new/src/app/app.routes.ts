import { Routes } from '@angular/router';
import { moduleAccessGuard } from './core/module-access.guard';
import { Shell } from './shell/shell';

/**
 * One route per Phase 2 pilot screen, path matching the legacy ui-router
 * state's `url` exactly (see docs/PHASE2_PILOT_AUDIT.md) so this is a
 * drop-in replacement path for whichever ngUpgrade routing strategy Phase 7
 * ends up using -- not decided in this pilot, kept out of scope here.
 *
 * Second pilot batch (controllerAs:'vm' self-registered screens, see
 * docs/PHASE2_PILOT_BATCH2_AUDIT.md) added below the first 5.
 *
 * Each route's `data.moduleId` matches the module id legacy's own live
 * `.state(...)` resolve block checks for that exact state name (confirmed
 * by reading each state definition directly, including the 5 batch-2
 * screens' self-registered `.config($stateProvider)` blocks in their own
 * controller files, not assumed) -- `moduleAccessGuard` enforces the same
 * check legacy enforces, since nothing on the backend does (see
 * docs/ANGULAR_PENDING_FINDINGS.md entry 13). Routes with no `moduleId`
 * (billingGroupMaster, periodicityService) match legacy states that
 * likewise have no resolve block at all -- not a gap this fix introduces,
 * a faithful match to legacy's actual (lack of) restriction.
 *
 * `data.title` added on every route below (per
 * docs/ANGULAR_HYBRID_DEPLOYMENT_PROPOSAL.md Part 3's retrofit plan) --
 * sourced from each state's own `title` in
 * `tools/angular-audit/data/states_enriched.json`, not invented, matching
 * exactly what legacy's own `$state.current.title` binding would have
 * shown in its top bar. A handful of self-registered detail screens
 * never set a legacy `title` at all (`fixedAssetType`,
 * `linenIssueAcceptanceDetails`) -- those get a plain, sensible label
 * instead, noted inline. `Shell` (./shell/shell.ts) reads this field to
 * drive its top-bar page title; nothing else about any screen's own
 * files changed to add this.
 */
export const routes: Routes = [
  // Deliberately OUTSIDE the shell -- an access-denied page showing a full
  // nav shell around it would be confusing (a user without access
  // shouldn't see a sidebar promising screens they can't reach), matching
  // legacy's own `ng-if="loginpage"` chrome-gating precedent
  // (legacy/HIS-WAR/.../index.html:187, top-bar/sidebar only render when
  // `loginpage` is true -- the login screen itself has none either).
  {
    path: 'accessDenied',
    loadComponent: () =>
      import('./features/access-denied/access-denied').then((m) => m.AccessDenied),
  },
  {
    path: '',
    component: Shell,
    children: [
      {
        path: 'billingGroupMaster',
        canActivate: [moduleAccessGuard],
        data: { title: 'Billing Group Master' },
        loadComponent: () =>
          import('./features/billing-group-master/billing-group-master').then(
            (m) => m.BillingGroupMaster,
          ),
      },
      {
        path: 'insuranceMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 571, title: 'Insurance Master' },
        loadComponent: () =>
          import('./features/insurance-master/insurance-master').then((m) => m.InsuranceMaster),
      },
      {
        path: 'inputOutputCharts',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 99, title: 'Intake Fluid Output(ml) Charts' },
        loadComponent: () =>
          import('./features/input-output-charts/input-output-charts').then(
            (m) => m.InputOutputCharts,
          ),
      },
      {
        path: 'organizationMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 377, requireSuperUser: true, title: 'Organization Master' },
        loadComponent: () =>
          import('./features/organization-master/organization-master').then(
            (m) => m.OrganizationMaster,
          ),
      },
      {
        path: 'MISReports',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 768, title: 'MIS Report' },
        loadComponent: () => import('./features/mis-reports/mis-reports').then((m) => m.MisReports),
      },
      {
        path: 'GRN',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 181, title: 'Good Receipt Note' },
        loadComponent: () => import('./features/grn/grn').then((m) => m.Grn),
      },
      {
        path: 'assetCategoryMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 642, title: 'Asset Category Master' },
        loadComponent: () =>
          import('./features/asset-category-master/asset-category-master').then((m) => m.AssetCategoryMaster),
      },
      {
        path: 'periodicityService',
        canActivate: [moduleAccessGuard],
        data: { title: 'Periodicity Service' },
        loadComponent: () =>
          import('./features/periodicity-service/periodicity-service').then((m) => m.PeriodicityService),
      },
      {
        path: 'assigningPickerDispenser',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 104, title: 'Pending List for Assigning Picker' },
        loadComponent: () =>
          import('./features/assigning-picker-dispenser/assigning-picker-dispenser').then(
            (m) => m.AssigningPickerDispenser,
          ),
      },
      {
        path: 'implantMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 806, title: 'Implant Master' },
        loadComponent: () => import('./features/implant-master/implant-master').then((m) => m.ImplantMaster),
      },
      // Phase 4, first batch (docs/ANGULAR_PHASE4_SEQUENCING_RECOMMENDATION.md).
      // display: deliberately NO moduleAccessGuard. Confirmed at app.js:20149 --
      // legacy's `display` state has no resolve block at all, and its
      // controller uses GenericServiceForWithoutLogin, not the usual
      // session-authenticated GenericService -- it's a public, unauthenticated
      // kiosk display. Adding the guard here would invent a login requirement
      // legacy never had (see docs/ANGULAR_PENDING_FINDINGS.md entry 33).
      {
        path: 'display/:orgId/:unitId/:cookieskioskId/:userId',
        data: { title: 'QMS Token Display' },
        loadComponent: () => import('./features/display/display').then((m) => m.Display),
      },
      // webAppointmentBookingConfirmation: no moduleAccessGuard. Confirmed at
      // app.js:21006-21013 -- no resolve block, and the controller (65 lines,
      // read in full) makes zero backend calls at all, so there's no session
      // requirement to even discover -- a public, patient-facing appointment
      // confirmation screen. `webAppConfirmationObj` is an in-memory-only
      // ui-router param, not part of the URL -- see
      // WebAppointmentHandoffService for how this is replicated.
      {
        path: 'webAppointmentBookingConfirmation',
        data: { title: 'Web Appointment Confirmation' },
        loadComponent: () =>
          import('./features/web-appointment-booking-confirmation/web-appointment-booking-confirmation').then(
            (m) => m.WebAppointmentBookingConfirmation,
          ),
      },
      // pesapalProcessPage: no data.moduleId (no resolve block in legacy's
      // state), but moduleAccessGuard's baseline "must be logged in" check
      // still applies -- the controller has a real cookieObject session guard
      // (pesapalSuccessPageController.js:22-27), same shape as
      // billingGroupMaster/periodicityService.
      {
        path: 'pesapalProcessPage',
        canActivate: [moduleAccessGuard],
        data: { title: 'Pesapal' },
        loadComponent: () =>
          import('./features/pesapal-process-page/pesapal-process-page').then((m) => m.PesapalProcessPage),
      },
      // viewAssetScheduler: no data.moduleId (no resolve block), but a real
      // cookieObject session guard in the controller -- same shape as
      // pesapalProcessPage.
      {
        path: 'viewAssetScheduler',
        canActivate: [moduleAccessGuard],
        data: { title: 'Asset PMS Scheduler' },
        loadComponent: () =>
          import('./features/view-asset-scheduler/view-asset-scheduler').then((m) => m.ViewAssetScheduler),
      },
      // pharman: no data.moduleId (no resolve block), real cookieObject
      // session guard in the controller -- same shape as pesapalProcessPage.
      {
        path: 'pharman',
        canActivate: [moduleAccessGuard],
        data: { title: 'Pharman Page' },
        loadComponent: () => import('./features/pharman/pharman').then((m) => m.Pharman),
      },
      // webAppointmentBooking: no moduleAccessGuard. Confirmed at
      // app.js:20989-20994 -- no resolve block, and the controller has zero
      // session guard anywhere ($scope.userId hardcoded to 1) -- a genuinely
      // public, patient-facing doctor-browsing landing page.
      {
        path: 'webAppointmentBooking/:orgId/:unitId',
        data: { title: 'Web Appointment' },
        loadComponent: () =>
          import('./features/web-appointment-booking/web-appointment-booking').then((m) => m.WebAppointmentBooking),
      },
      // scheduleWorklist: no data.moduleId (no resolve block), real
      // cookieObject + cookieObjectAppConfig guards in the controller --
      // moduleAccessGuard covers the first, the second is checked in the
      // component itself (see schedule-worklist.ts doc comment).
      {
        path: 'scheduleWorklist',
        canActivate: [moduleAccessGuard],
        data: { title: 'Schedule Worklist' },
        loadComponent: () => import('./features/schedule-worklist/schedule-worklist').then((m) => m.ScheduleWorklist),
      },
      // PMSchedule: no data.moduleId (no resolve block), real cookieObject +
      // cookieObjectAppConfig guards, same shape as scheduleWorklist. NO
      // store-selection modal wiring -- legacy's own selectedTopStore check
      // is entirely commented out for this screen (see pm-schedule.ts doc
      // comment).
      {
        path: 'PMSchedule',
        canActivate: [moduleAccessGuard],
        data: { title: 'PM Schedule' },
        loadComponent: () => import('./features/pm-schedule/pm-schedule').then((m) => m.PmSchedule),
      },
      // assetScheduler: no data.moduleId (no resolve block), real cookieObject
      // guard only (no cookieObjectAppConfig check, unlike scheduleWorklist/PMSchedule).
      {
        path: 'assetScheduler',
        canActivate: [moduleAccessGuard],
        data: { title: 'Asset PMS Scheduler' },
        loadComponent: () => import('./features/asset-scheduler/asset-scheduler').then((m) => m.AssetScheduler),
      },
      // detailScheduleWorklist: no data.moduleId (no resolve block), real
      // cookieObject guard, active storePopup wiring (entry 37 recurs here).
      {
        path: 'detailScheduleWorklist',
        canActivate: [moduleAccessGuard],
        data: { title: 'Schedule Worklist' },
        loadComponent: () =>
          import('./features/detail-schedule-worklist/detail-schedule-worklist').then((m) => m.DetailScheduleWorklist),
      },
      // bookView: no data.moduleId (no resolve block), real cookieObject +
      // cookieObjectAppConfig guards in the controller, same shape as
      // pesapalProcessPage. SCOPED REBUILD -- see book-view.ts's class doc
      // comment: covers the guards and the corrected patient-context safety
      // check (entry 28) plus the header it gates, not the full 81-function
      // tab surface of the 4,027-line legacy controller.
      {
        path: 'bookView',
        canActivate: [moduleAccessGuard],
        data: { title: 'Book View' },
        loadComponent: () => import('./features/book-view/book-view').then((m) => m.BookView),
      },
      // bookAppointment: no session guard in legacy (confirmed by full read) --
      // reached only via webAppointmentBooking's public flow. Receives
      // webAppDetailObj via BookAppointmentHandoffService (sending side
      // already built in webAppointmentBooking, batch 1b).
      {
        path: 'bookAppointment',
        data: { title: 'Book Appointment' },
        loadComponent: () => import('./features/book-appointment/book-appointment').then((m) => m.BookAppointment),
      },
      // outPatientFeedback / inPatientFeedback: no session guard anywhere in
      // either legacy controller (confirmed by full read) -- genuinely public,
      // patient-facing feedback forms. Route path drops legacy's literal
      // `/api` prefix -- see out-patient-feedback.ts's doc comment for why
      // (this app's Router is path-based, not hash-based like ui-router, so
      // keeping `/api` in a frontend route would collide with this dev
      // server's own `/api/**` proxy rule on every direct navigation).
      {
        path: 'outPatientFeedback/:organizationId/:unitId/:patientId/:visitType/:visitTypeId/:encounterId',
        data: { title: 'OutPatient Feedback' },
        loadComponent: () => import('./features/out-patient-feedback/out-patient-feedback').then((m) => m.OutPatientFeedback),
      },
      {
        path: 'inPatientFeedback/:organizationId/:unitId/:patientId/:visitType/:visitTypeId/:admissionId',
        data: { title: 'InPatient Feedback' },
        loadComponent: () => import('./features/in-patient-feedback/in-patient-feedback').then((m) => m.InPatientFeedback),
      },
      // Phase 4 batch 2 (docs/ANGULAR_PHASE4_SEQUENCING_RECOMMENDATION.md
      // follow-on). All 8 have a real cookieObject session guard in their
      // legacy controller; 3 also have a real module-access resolve block
      // (moduleId noted per route below), the rest have none (faithful match
      // to legacy's own lack of a resolve block, same as several batch 1
      // screens).
      {
        path: 'detailsFixedAssetMaster',
        canActivate: [moduleAccessGuard],
        data: { title: 'Add Fixed Asset Opening Balance' },
        loadComponent: () => import('./features/details-fixed-asset-master/details-fixed-asset-master').then((m) => m.DetailsFixedAssetMaster),
      },
      {
        path: 'correctiveMaintenanceWorklist',
        canActivate: [moduleAccessGuard],
        data: { title: 'Corrective Maintenance Worklist' },
        loadComponent: () => import('./features/corrective-maintenance-worklist/corrective-maintenance-worklist').then((m) => m.CorrectiveMaintenanceWorklist),
      },
      {
        path: 'assetIssueAcceptance',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 215, title: 'Asset Issuance Acceptance' },
        loadComponent: () => import('./features/asset-issue-acceptance/asset-issue-acceptance').then((m) => m.AssetIssueAcceptance),
      },
      {
        path: 'assetReturnAcceptance',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 217, title: 'Asset Return Acceptance' },
        loadComponent: () => import('./features/asset-return-acceptance/asset-return-acceptance').then((m) => m.AssetReturnAcceptance),
      },
      {
        path: 'correctiveMaintenanceScheduleWorklist',
        canActivate: [moduleAccessGuard],
        data: { title: 'Corrective Maintenance Schedule Worklist' },
        loadComponent: () =>
          import('./features/corrective-maintenance-schedule-worklist/corrective-maintenance-schedule-worklist').then((m) => m.CorrectiveMaintenanceScheduleWorklist),
      },
      {
        path: 'linenIssueacceptance',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 96, title: 'Linen Issuance Acceptance' },
        loadComponent: () => import('./features/linen-issue-acceptance/linen-issue-acceptance').then((m) => m.LinenIssueAcceptance),
      },
      {
        path: 'linenIssueAcceptanceDetails',
        canActivate: [moduleAccessGuard],
        // No legacy title (self-registered state, never set one) -- plain,
        // sensible label instead of leaving the top bar blank.
        data: { title: 'Linen Issuance Acceptance Details' },
        loadComponent: () => import('./features/linen-issue-acceptance-details/linen-issue-acceptance-details').then((m) => m.LinenIssueAcceptanceDetails),
      },
      {
        path: 'createTabularDataTemplates',
        canActivate: [moduleAccessGuard],
        data: { title: 'Tabular Data Template' },
        loadComponent: () => import('./features/create-tabular-data-templates/create-tabular-data-templates').then((m) => m.CreateTabularDataTemplates),
      },
      // Phase 4 batch 3 (docs/ANGULAR_PHASE4_SEQUENCING_RECOMMENDATION.md
      // follow-on). All 8 are Asset Management screens; only rejectedAssetAcceptance
      // has a real module-access resolve block in legacy (moduleId 218, confirmed
      // in rejectedAssetAcceptanceController.js's own self-registered state) --
      // the rest have none, faithfully matching legacy's own lack of a resolve
      // block, same convention as several batch 1/2 screens.
      {
        path: 'correctiveMaintenance',
        canActivate: [moduleAccessGuard],
        data: { title: 'Corrective Maintenance Request' },
        loadComponent: () => import('./features/corrective-maintenance/corrective-maintenance').then((m) => m.CorrectiveMaintenance),
      },
      {
        path: 'detailsCorrectiveMaintenance',
        canActivate: [moduleAccessGuard],
        data: { title: 'Corrective Maintenance Request' },
        loadComponent: () => import('./features/details-corrective-maintenance/details-corrective-maintenance').then((m) => m.DetailsCorrectiveMaintenance),
      },
      {
        path: 'addNewCorrectiveMaintenance',
        canActivate: [moduleAccessGuard],
        data: { title: 'Corrective Maintenance Request' },
        loadComponent: () => import('./features/add-new-corrective-maintenance/add-new-corrective-maintenance').then((m) => m.AddNewCorrectiveMaintenance),
      },
      {
        path: 'correctiveMaintenanceReturnWorklist',
        canActivate: [moduleAccessGuard],
        data: { title: 'Corrective Maintenance Return Worklist' },
        loadComponent: () =>
          import('./features/corrective-maintenance-return-worklist/corrective-maintenance-return-worklist').then((m) => m.CorrectiveMaintenanceReturnWorklist),
      },
      {
        path: 'rejectedAssetAcceptance',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 218, title: 'Rejected Asset Acceptance' },
        loadComponent: () => import('./features/rejected-asset-acceptance/rejected-asset-acceptance').then((m) => m.RejectedAssetAcceptance),
      },
      {
        path: 'assetReturnAcceptanceDetails',
        canActivate: [moduleAccessGuard],
        data: { title: 'Asset Return Acceptance' },
        loadComponent: () => import('./features/asset-return-acceptance-details/asset-return-acceptance-details').then((m) => m.AssetReturnAcceptanceDetails),
      },
      {
        path: 'detailsAssetIssueAcceptance',
        canActivate: [moduleAccessGuard],
        data: { title: 'Asset Issue Acceptance' },
        loadComponent: () => import('./features/details-asset-issue-acceptance/details-asset-issue-acceptance').then((m) => m.DetailsAssetIssueAcceptance),
      },
      {
        path: 'fixedAssetType',
        canActivate: [moduleAccessGuard],
        // No legacy title either (same reason as linenIssueAcceptanceDetails
        // above) -- matches the screen's own H1 heading instead.
        data: { title: 'Fixed Asset Type' },
        loadComponent: () => import('./features/fixed-asset-type/fixed-asset-type').then((m) => m.FixedAssetType),
      },
      // Phase 4 batch 4 (docs/ANGULAR_PHASE4_SEQUENCING_RECOMMENDATION.md
      // follow-on). Diversified across 6 modules (per owner instruction),
      // all simple master-data CRUD screens. bedBook was pre-screened out
      // as a mockup (entry 53) and is not built. Shell integration (this
      // nesting + `data.title`) is now standard practice from this batch
      // onward -- applied at Step 3, not retrofitted afterward.
      {
        path: 'specialityMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 372, title: 'Department Master' },
        loadComponent: () => import('./features/speciality-master/speciality-master').then((m) => m.SpecialityMaster),
      },
      {
        path: 'nationalityMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 381, title: 'Nationality Master' },
        loadComponent: () => import('./features/nationality-master/nationality-master').then((m) => m.NationalityMaster),
      },
      {
        path: 'countryMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 383, title: 'Country Master' },
        loadComponent: () => import('./features/country-master/country-master').then((m) => m.CountryMaster),
      },
      {
        path: 'cityMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 386, title: 'City Master' },
        loadComponent: () => import('./features/city-master/city-master').then((m) => m.CityMaster),
      },
      {
        path: 'wardMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 545, title: 'Ward Master' },
        loadComponent: () => import('./features/ward-master/ward-master').then((m) => m.WardMaster),
      },
      {
        path: 'bedCategoryMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 543, title: 'Bed Category Master' },
        loadComponent: () => import('./features/bed-category-master/bed-category-master').then((m) => m.BedCategoryMaster),
      },
      {
        path: 'ambulanceTypeMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 813, title: 'Ambulance Type Master' },
        loadComponent: () => import('./features/ambulance-type-master/ambulance-type-master').then((m) => m.AmbulanceTypeMaster),
      },
      {
        path: 'ambulanceMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 814, title: 'Ambulance Master' },
        loadComponent: () => import('./features/ambulance-master/ambulance-master').then((m) => m.AmbulanceMaster),
      },
      {
        path: 'doctorCategory',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 453, title: 'Doctor Category' },
        loadComponent: () => import('./features/doctor-category/doctor-category').then((m) => m.DoctorCategory),
      },
      // Phase 4 batch 5 (docs/ANGULAR_PHASE4_SEQUENCING_RECOMMENDATION.md
      // follow-on). Diversified across 6 modules, 3 new to the rebuild
      // (Admin - Queue Management, Admin - User Management,
      // Admin - Configuration). All 10 pre-screened real; entry 55's
      // standing policy applies to any of these using the shared <modal>
      // directive (see docs/ANGULAR_PENDING_FINDINGS.md).
      {
        path: 'QMSkioskMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 795, title: 'QMS Kiosk Master' },
        loadComponent: () => import('./features/qms-kiosk-master/qms-kiosk-master').then((m) => m.QmsKioskMaster),
      },
      {
        path: 'QMSdepartmentMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 796, title: 'QMS Department Master' },
        loadComponent: () => import('./features/qms-department-master/qms-department-master').then((m) => m.QmsDepartmentMaster),
      },
      {
        path: 'bedTypeMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 402, title: 'Ward Bed Type Master' },
        loadComponent: () => import('./features/bed-type-master/bed-type-master').then((m) => m.BedTypeMaster),
      },
      {
        path: 'shortLeaveReasonMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 406, title: 'Short Leave Reason Master' },
        loadComponent: () => import('./features/short-leave-reason-master/short-leave-reason-master').then((m) => m.ShortLeaveReasonMaster),
      },
      {
        path: 'ambulanceFacilitiesMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 818, title: 'Ambulance Facilities Master' },
        loadComponent: () => import('./features/ambulance-facilities-master/ambulance-facilities-master').then((m) => m.AmbulanceFacilitiesMaster),
      },
      {
        path: 'qualificationMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 496, title: 'Qualification Master' },
        loadComponent: () => import('./features/qualification-master/qualification-master').then((m) => m.QualificationMaster),
      },
      {
        path: 'degreeMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 497, title: 'Degree Master' },
        loadComponent: () => import('./features/degree-master/degree-master').then((m) => m.DegreeMaster),
      },
      {
        path: 'userStoreMapper',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 690, title: 'User Store Mapper' },
        loadComponent: () => import('./features/user-store-mapper/user-store-mapper').then((m) => m.UserStoreMapper),
      },
      {
        path: 'doctorCabinMapper',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 714, title: 'Doctor Cabin Mapper' },
        loadComponent: () => import('./features/doctor-cabin-mapper/doctor-cabin-mapper').then((m) => m.DoctorCabinMapper),
      },
      {
        path: 'prefixConfiguration',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 699, title: 'Prefix Configuration' },
        loadComponent: () => import('./features/prefix-configuration/prefix-configuration').then((m) => m.PrefixConfiguration),
      },
      // Phase 4 batch 6 (docs/ANGULAR_PHASE4_SEQUENCING_RECOMMENDATION.md
      // follow-on). Diversified across Ward/Unit Master, HR/Staff Master,
      // User Management, and ADT-Ambulance. Entry 55's standing policy
      // (working closeConfirmModal() instead of legacy's crashing shared
      // <modal> cancel()) applies to 8 of these 10 screens -- confirmed via
      // full pre-build audit, a wider blast radius than the 5 originally
      // assumed going into this batch (see docs/ANGULAR_PENDING_FINDINGS.md
      // entry 55). userCounterMapper faithfully reproduces entry 59's
      // dual-list-picker indexOf/splice bug (3rd on-branch instance).
      {
        path: 'floorMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 537, title: 'Floor Master' },
        loadComponent: () => import('./features/floor-master/floor-master').then((m) => m.FloorMaster),
      },
      {
        path: 'amenityMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 539, title: 'Amenity Master' },
        loadComponent: () => import('./features/amenity-master/amenity-master').then((m) => m.AmenityMaster),
      },
      {
        path: 'wingMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 540, title: 'Wing Master' },
        loadComponent: () => import('./features/wing-master/wing-master').then((m) => m.WingMaster),
      },
      {
        path: 'counterMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 704, title: 'Counter Master' },
        loadComponent: () => import('./features/counter-master/counter-master').then((m) => m.CounterMaster),
      },
      {
        path: 'cabinMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 705, title: 'Cabin Master' },
        loadComponent: () => import('./features/cabin-master/cabin-master').then((m) => m.CabinMaster),
      },
      {
        path: 'employeeCategoryMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 498, title: 'Employee Category Master' },
        loadComponent: () => import('./features/employee-category-master/employee-category-master').then((m) => m.EmployeeCategoryMaster),
      },
      {
        path: 'designationMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 500, title: 'Designation Master' },
        loadComponent: () => import('./features/designation-master/designation-master').then((m) => m.DesignationMaster),
      },
      {
        path: 'userCounterMapper',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 715, title: 'User Counter Mapper' },
        loadComponent: () => import('./features/user-counter-mapper/user-counter-mapper').then((m) => m.UserCounterMapper),
      },
      {
        path: 'resourceAllocationMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 817, title: 'Resource Allocation Master' },
        loadComponent: () => import('./features/resource-allocation-master/resource-allocation-master').then((m) => m.ResourceAllocationMaster),
      },
      {
        path: 'rowMasters',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 1070, title: 'MRD Row Master' },
        loadComponent: () => import('./features/row-masters/row-masters').then((m) => m.RowMasters),
      },
      {
        path: 'subSpecialityMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 373, title: 'Sub Speciality Master' },
        loadComponent: () => import('./features/sub-speciality-master/sub-speciality-master').then((m) => m.SubSpecialityMaster),
      },
      {
        path: 'bankMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 456, title: 'Bank Master' },
        loadComponent: () => import('./features/bank-master/bank-master').then((m) => m.BankMaster),
      },
      {
        path: 'manufacturerMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 461, title: 'Manufacturer Master' },
        loadComponent: () => import('./features/manufacturer-master/manufacturer-master').then((m) => m.ManufacturerMaster),
      },
      {
        path: 'mealPreferenceMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 549, title: 'Meal Preference Master' },
        loadComponent: () => import('./features/meal-preference-master/meal-preference-master').then((m) => m.MealPreferenceMaster),
      },
      {
        path: 'boxMasters',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 1069, title: 'MRD Box Master' },
        loadComponent: () => import('./features/box-masters/box-masters').then((m) => m.BoxMasters),
      },
      {
        path: 'QMScounterMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 797, title: 'QMS Counter Master' },
        loadComponent: () => import('./features/qms-counter-master/qms-counter-master').then((m) => m.QmsCounterMaster),
      },
      {
        path: 'modalityTypeMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 654, title: 'Modality Type Master' },
        loadComponent: () => import('./features/modality-type-master/modality-type-master').then((m) => m.ModalityTypeMaster),
      },
      {
        path: 'ambulanceServiceProviderMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 815, title: 'Ambulance Service Provider Master' },
        loadComponent: () => import('./features/ambulance-service-provider-master/ambulance-service-provider-master').then((m) => m.AmbulanceServiceProviderMaster),
      },
      {
        path: 'passTypeMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 821, title: 'Pass Type Master' },
        loadComponent: () => import('./features/pass-type-master/pass-type-master').then((m) => m.PassTypeMaster),
      },
      {
        // No `data.moduleId` -- legacy's state genuinely has no resolve
        // block (confirmed reading app.js:20435 directly), same class as
        // billingGroupMaster/periodicityService above.
        path: 'departmentStoreMapper',
        canActivate: [moduleAccessGuard],
        data: { title: 'Department Store Mapper' },
        loadComponent: () => import('./features/department-store-mapper/department-store-mapper').then((m) => m.DepartmentStoreMapper),
      },
      {
        path: 'raceMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 382, title: 'Race Master' },
        loadComponent: () => import('./features/race-master/race-master').then((m) => m.RaceMaster),
      },
      {
        path: 'stateMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 384, title: 'State Master' },
        loadComponent: () => import('./features/state-master/state-master').then((m) => m.StateMaster),
      },
      {
        path: 'districtMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 385, title: 'District Master' },
        loadComponent: () => import('./features/district-master/district-master').then((m) => m.DistrictMaster),
      },
      {
        path: 'relationMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 390, title: 'Relation Master' },
        loadComponent: () => import('./features/relation-master/relation-master').then((m) => m.RelationMaster),
      },
      {
        path: 'maritalstatusMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 391, title: 'Marital Status Master' },
        loadComponent: () => import('./features/marital-status-master/marital-status-master').then((m) => m.MaritalStatusMaster),
      },
      {
        path: 'occupationMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 393, title: 'Occupation Master' },
        loadComponent: () => import('./features/occupation-master/occupation-master').then((m) => m.OccupationMaster),
      },
      {
        path: 'prefixMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 398, title: 'Prefix Master' },
        loadComponent: () => import('./features/prefix-master/prefix-master').then((m) => m.PrefixMaster),
      },
      {
        path: 'gradeCreationMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 575, title: 'Grade Creation Master' },
        loadComponent: () => import('./features/grade-creation-master/grade-creation-master').then((m) => m.GradeCreationMaster),
      },
      {
        path: 'employeeCategory',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 454, title: 'Employee Category' },
        loadComponent: () => import('./features/employee-category/employee-category').then((m) => m.EmployeeCategory),
      },
      {
        path: 'bankBranch',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 455, title: 'Bank Branch' },
        loadComponent: () => import('./features/bank-branch/bank-branch').then((m) => m.BankBranch),
      },
      // Phase 4 batch 9
      {
        path: 'doctorCategoryMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 499, title: 'Doctor Category Master' },
        loadComponent: () =>
          import('./features/doctor-category-master/doctor-category-master').then((m) => m.DoctorCategoryMaster),
      },
      {
        path: 'roleManagement',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 675, title: 'Role Management' },
        loadComponent: () => import('./features/role-management/role-management').then((m) => m.RoleManagement),
      },
      {
        path: 'rackMasters',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 1068, title: 'MRD Rack Master' },
        loadComponent: () => import('./features/rack-masters/rack-masters').then((m) => m.RackMasters),
      },
      {
        path: 'weekendMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 697, title: 'Weekend Master' },
        loadComponent: () => import('./features/weekend-master/weekend-master').then((m) => m.WeekendMaster),
      },
      {
        path: 'genericMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 463, title: 'Generic Master' },
        loadComponent: () => import('./features/generic-master/generic-master').then((m) => m.GenericMaster),
      },
      {
        path: 'createTabularTemplates',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 736, title: 'Tabular Template' },
        loadComponent: () =>
          import('./features/create-tabular-templates/create-tabular-templates').then(
            (m) => m.CreateTabularTemplates,
          ),
      },
      {
        path: 'modalityMaster',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 655, title: 'Modality Master' },
        loadComponent: () => import('./features/modality-master/modality-master').then((m) => m.ModalityMaster),
      },
      {
        path: 'displayCall',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 794, title: 'QMS Token Display' },
        loadComponent: () => import('./features/display-call/display-call').then((m) => m.DisplayCall),
      },
      {
        path: 'linenIssue',
        canActivate: [moduleAccessGuard],
        data: { moduleId: 344, title: 'Linen Issue' },
        loadComponent: () => import('./features/linen-issue/linen-issue').then((m) => m.LinenIssue),
      },
      {
        path: 'advisedVisits',
        loadComponent: () => import('./features/advised-visits/advised-visits').then((m) => m.AdvisedVisits),
        data: { title: 'Advised Visits' },
      },
      { path: '', redirectTo: 'billingGroupMaster', pathMatch: 'full' },
    ],
  },
];
