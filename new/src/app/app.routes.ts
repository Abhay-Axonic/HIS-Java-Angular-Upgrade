import { Routes } from '@angular/router';

/**
 * One route per Phase 2 pilot screen, path matching the legacy ui-router
 * state's `url` exactly (see docs/PHASE2_PILOT_AUDIT.md) so this is a
 * drop-in replacement path for whichever ngUpgrade routing strategy Phase 7
 * ends up using -- not decided in this pilot, kept out of scope here.
 *
 * Second pilot batch (controllerAs:'vm' self-registered screens, see
 * docs/PHASE2_PILOT_BATCH2_AUDIT.md) added below the first 5.
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
  {
    path: 'GRN',
    loadComponent: () => import('./features/grn/grn').then((m) => m.Grn),
  },
  {
    path: 'assetCategoryMaster',
    loadComponent: () =>
      import('./features/asset-category-master/asset-category-master').then((m) => m.AssetCategoryMaster),
  },
  {
    path: 'periodicityService',
    loadComponent: () =>
      import('./features/periodicity-service/periodicity-service').then((m) => m.PeriodicityService),
  },
  {
    path: 'assigningPickerDispenser',
    loadComponent: () =>
      import('./features/assigning-picker-dispenser/assigning-picker-dispenser').then(
        (m) => m.AssigningPickerDispenser,
      ),
  },
  {
    path: 'implantMaster',
    loadComponent: () => import('./features/implant-master/implant-master').then((m) => m.ImplantMaster),
  },
  { path: '', redirectTo: 'billingGroupMaster', pathMatch: 'full' },
];
