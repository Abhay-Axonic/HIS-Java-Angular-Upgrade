import { Routes } from '@angular/router';
import { moduleAccessGuard } from './core/module-access.guard';

/**
 * One route per Phase 2 pilot screen, path matching the legacy ui-router
 * state's `url` exactly (see docs/PHASE2_PILOT_AUDIT.md) so this is a
 * drop-in replacement path for whichever ngUpgrade routing strategy Phase 7
 * ends up using -- not decided in this pilot, kept out of scope here.
 *
 * Each route's `data.moduleId` matches the module id legacy's own
 * `.state(...)` resolve block checks for that exact state name (confirmed
 * by reading each live state definition directly, not assumed) --
 * `moduleAccessGuard` enforces the same check legacy enforces, since
 * nothing on the backend does (see docs/ANGULAR_PENDING_FINDINGS.md entry
 * 13). Routes with no `moduleId` (billingGroupMaster) match legacy states
 * that likewise have no resolve block at all -- not a gap this fix
 * introduces, a faithful match to legacy's actual (lack of) restriction.
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
    path: 'accessDenied',
    loadComponent: () =>
      import('./features/access-denied/access-denied').then((m) => m.AccessDenied),
  },
  { path: '', redirectTo: 'billingGroupMaster', pathMatch: 'full' },
];
