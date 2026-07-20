import { Routes } from '@angular/router';
import { moduleAccessGuard } from './core/module-access.guard';

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
 */
export const routes: Routes = [
  {
    path: 'billingGroupMaster',
    canActivate: [moduleAccessGuard],
    loadComponent: () =>
      import('./features/billing-group-master/billing-group-master').then(
        (m) => m.BillingGroupMaster,
      ),
  },
  {
    path: 'insuranceMaster',
    canActivate: [moduleAccessGuard],
    data: { moduleId: 571 },
    loadComponent: () =>
      import('./features/insurance-master/insurance-master').then((m) => m.InsuranceMaster),
  },
  {
    path: 'inputOutputCharts',
    canActivate: [moduleAccessGuard],
    data: { moduleId: 99 },
    loadComponent: () =>
      import('./features/input-output-charts/input-output-charts').then(
        (m) => m.InputOutputCharts,
      ),
  },
  {
    path: 'organizationMaster',
    canActivate: [moduleAccessGuard],
    data: { moduleId: 377, requireSuperUser: true },
    loadComponent: () =>
      import('./features/organization-master/organization-master').then(
        (m) => m.OrganizationMaster,
      ),
  },
  {
    path: 'MISReports',
    canActivate: [moduleAccessGuard],
    data: { moduleId: 768 },
    loadComponent: () => import('./features/mis-reports/mis-reports').then((m) => m.MisReports),
  },
  {
    path: 'GRN',
    canActivate: [moduleAccessGuard],
    data: { moduleId: 181 },
    loadComponent: () => import('./features/grn/grn').then((m) => m.Grn),
  },
  {
    path: 'assetCategoryMaster',
    canActivate: [moduleAccessGuard],
    data: { moduleId: 642 },
    loadComponent: () =>
      import('./features/asset-category-master/asset-category-master').then((m) => m.AssetCategoryMaster),
  },
  {
    path: 'periodicityService',
    canActivate: [moduleAccessGuard],
    loadComponent: () =>
      import('./features/periodicity-service/periodicity-service').then((m) => m.PeriodicityService),
  },
  {
    path: 'assigningPickerDispenser',
    canActivate: [moduleAccessGuard],
    data: { moduleId: 104 },
    loadComponent: () =>
      import('./features/assigning-picker-dispenser/assigning-picker-dispenser').then(
        (m) => m.AssigningPickerDispenser,
      ),
  },
  {
    path: 'implantMaster',
    canActivate: [moduleAccessGuard],
    data: { moduleId: 806 },
    loadComponent: () => import('./features/implant-master/implant-master').then((m) => m.ImplantMaster),
  },
  {
    path: 'accessDenied',
    loadComponent: () =>
      import('./features/access-denied/access-denied').then((m) => m.AccessDenied),
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
    loadComponent: () =>
      import('./features/pesapal-process-page/pesapal-process-page').then((m) => m.PesapalProcessPage),
  },
  // viewAssetScheduler: no data.moduleId (no resolve block), but a real
  // cookieObject session guard in the controller -- same shape as
  // pesapalProcessPage.
  {
    path: 'viewAssetScheduler',
    canActivate: [moduleAccessGuard],
    loadComponent: () =>
      import('./features/view-asset-scheduler/view-asset-scheduler').then((m) => m.ViewAssetScheduler),
  },
  // pharman: no data.moduleId (no resolve block), real cookieObject
  // session guard in the controller -- same shape as pesapalProcessPage.
  {
    path: 'pharman',
    canActivate: [moduleAccessGuard],
    loadComponent: () => import('./features/pharman/pharman').then((m) => m.Pharman),
  },
  // webAppointmentBooking: no moduleAccessGuard. Confirmed at
  // app.js:20989-20994 -- no resolve block, and the controller has zero
  // session guard anywhere ($scope.userId hardcoded to 1) -- a genuinely
  // public, patient-facing doctor-browsing landing page.
  {
    path: 'webAppointmentBooking/:orgId/:unitId',
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
    loadComponent: () => import('./features/pm-schedule/pm-schedule').then((m) => m.PmSchedule),
  },
  // assetScheduler: no data.moduleId (no resolve block), real cookieObject
  // guard only (no cookieObjectAppConfig check, unlike scheduleWorklist/PMSchedule).
  {
    path: 'assetScheduler',
    canActivate: [moduleAccessGuard],
    loadComponent: () => import('./features/asset-scheduler/asset-scheduler').then((m) => m.AssetScheduler),
  },
  // detailScheduleWorklist: no data.moduleId (no resolve block), real
  // cookieObject guard, active storePopup wiring (entry 37 recurs here).
  {
    path: 'detailScheduleWorklist',
    canActivate: [moduleAccessGuard],
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
    loadComponent: () => import('./features/book-view/book-view').then((m) => m.BookView),
  },
  // bookAppointment: no session guard in legacy (confirmed by full read) --
  // reached only via webAppointmentBooking's public flow. Receives
  // webAppDetailObj via BookAppointmentHandoffService (sending side
  // already built in webAppointmentBooking, batch 1b).
  {
    path: 'bookAppointment',
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
    loadComponent: () => import('./features/out-patient-feedback/out-patient-feedback').then((m) => m.OutPatientFeedback),
  },
  {
    path: 'inPatientFeedback/:organizationId/:unitId/:patientId/:visitType/:visitTypeId/:admissionId',
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
    loadComponent: () => import('./features/details-fixed-asset-master/details-fixed-asset-master').then((m) => m.DetailsFixedAssetMaster),
  },
  {
    path: 'correctiveMaintenanceWorklist',
    canActivate: [moduleAccessGuard],
    loadComponent: () => import('./features/corrective-maintenance-worklist/corrective-maintenance-worklist').then((m) => m.CorrectiveMaintenanceWorklist),
  },
  {
    path: 'assetIssueAcceptance',
    canActivate: [moduleAccessGuard],
    data: { moduleId: 215 },
    loadComponent: () => import('./features/asset-issue-acceptance/asset-issue-acceptance').then((m) => m.AssetIssueAcceptance),
  },
  {
    path: 'assetReturnAcceptance',
    canActivate: [moduleAccessGuard],
    data: { moduleId: 217 },
    loadComponent: () => import('./features/asset-return-acceptance/asset-return-acceptance').then((m) => m.AssetReturnAcceptance),
  },
  {
    path: 'correctiveMaintenanceScheduleWorklist',
    canActivate: [moduleAccessGuard],
    loadComponent: () =>
      import('./features/corrective-maintenance-schedule-worklist/corrective-maintenance-schedule-worklist').then((m) => m.CorrectiveMaintenanceScheduleWorklist),
  },
  {
    path: 'linenIssueacceptance',
    canActivate: [moduleAccessGuard],
    data: { moduleId: 96 },
    loadComponent: () => import('./features/linen-issue-acceptance/linen-issue-acceptance').then((m) => m.LinenIssueAcceptance),
  },
  {
    path: 'linenIssueAcceptanceDetails',
    canActivate: [moduleAccessGuard],
    loadComponent: () => import('./features/linen-issue-acceptance-details/linen-issue-acceptance-details').then((m) => m.LinenIssueAcceptanceDetails),
  },
  {
    path: 'createTabularDataTemplates',
    canActivate: [moduleAccessGuard],
    loadComponent: () => import('./features/create-tabular-data-templates/create-tabular-data-templates').then((m) => m.CreateTabularDataTemplates),
  },
  { path: '', redirectTo: 'billingGroupMaster', pathMatch: 'full' },
];
