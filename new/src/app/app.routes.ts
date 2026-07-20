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
  { path: '', redirectTo: 'billingGroupMaster', pathMatch: 'full' },
];
