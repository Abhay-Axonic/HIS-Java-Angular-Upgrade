import { Routes } from '@angular/router';

/**
 * One route per Phase 2 pilot screen, path matching the legacy ui-router
 * state's `url` exactly (see docs/PHASE2_PILOT_AUDIT.md) so this is a
 * drop-in replacement path for whichever ngUpgrade routing strategy Phase 7
 * ends up using -- not decided in this pilot, kept out of scope here.
 */
export const routes: Routes = [
  {
    path: 'billingGroupMaster',
    loadComponent: () =>
      import('./features/billing-group-master/billing-group-master').then(
        (m) => m.BillingGroupMaster,
      ),
  },
  {
    path: 'insuranceMaster',
    loadComponent: () =>
      import('./features/insurance-master/insurance-master').then((m) => m.InsuranceMaster),
  },
  {
    path: 'inputOutputCharts',
    loadComponent: () =>
      import('./features/input-output-charts/input-output-charts').then(
        (m) => m.InputOutputCharts,
      ),
  },
  {
    path: 'organizationMaster',
    loadComponent: () =>
      import('./features/organization-master/organization-master').then(
        (m) => m.OrganizationMaster,
      ),
  },
  {
    path: 'MISReports',
    loadComponent: () => import('./features/mis-reports/mis-reports').then((m) => m.MisReports),
  },
  { path: '', redirectTo: 'billingGroupMaster', pathMatch: 'full' },
];
