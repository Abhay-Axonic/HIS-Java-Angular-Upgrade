import { QuickLinkTile } from './quick-links.models';

/**
 * The level-1 tile grid, transcribed tile-by-tile (order, label, icon
 * path, `checkAccess` module id, click index -> category mapping,
 * clickable/dead status) from
 * `legacy/HIS-WAR/src/main/webapp/views/common/dashboard-quick-links.html`
 * lines 66-651, cross-checked against the `click(info)` switch in
 * `dashboardInfoMasterController.js` lines 39-143. See
 * `quick-links.models.ts`'s doc comment for the scope decision covering
 * why only this level-1 grid (not the ~600+ item level-3 sub-menus) is
 * ported.
 */
export const QUICK_LINK_TILES: QuickLinkTile[] = [
  { label: 'Emergency Room', icon: 'home_icon/Emergency Room.png', moduleId: 328, clickable: true, category: 'emergencyRoom' },
  { label: 'OPD', icon: 'home_icon/OPD.png', moduleId: 1, clickable: true, category: 'opd' },
  { label: 'IPD', icon: 'home_icon/IPD.png', moduleId: 26, clickable: true, category: 'ipd' },
  { label: 'Billing', icon: 'home_icon/Billing.png', moduleId: 886, clickable: true, category: 'billing' },
  { label: 'Day Care', icon: 'dashboard/temp/Daycare.png', moduleId: 779, clickable: true, category: 'dayCare' },
  { label: 'Nursing', icon: 'home_icon/Nursing.png', moduleId: 44, clickable: true, category: 'nursing' },
  { label: 'EHR', icon: 'home_icon/EHR.png', moduleId: 243, clickable: true, category: 'ehr' },
  { label: 'LIS', icon: 'home_icon/LIS.png', moduleId: 272, clickable: true, category: 'lis' },
  { label: 'RIS', icon: 'home_icon/RIS.png', moduleId: 828, clickable: true, category: 'radiology' },
  // click(33) has no matching `case` in the controller's click(info) --
  // a genuine legacy dead-end (see QuickLinkTile.category's doc).
  { label: 'Procedure', icon: 'home_icon/Procedure.png', moduleId: 868, clickable: true, category: null },
  { label: 'Physiotherapy', icon: 'home_icon/Physiotherapy.png', moduleId: 943, clickable: true, category: 'physiotherapy' },
  // Legacy has this exact tile twice, under two different module ids and
  // click indices (34 and 25), both mapping to the SAME $scope.physiotherapy
  // flag -- a real duplicate, faithfully kept as two entries.
  { label: 'Physiotherapy', icon: 'home_icon/Physiotherapy.png', moduleId: 271, clickable: true, category: 'physiotherapy' },
  // ng-click="click(26);checkCssdStore()" in legacy -- checkCssdStore()
  // targets `#cssdStoreSelectionPopup`, which is absent from all 3 of
  // this batch's views (confirmed by grep), so it's a silent no-op here
  // exactly as it is a silent jQuery no-op in legacy for these 3 screens.
  // Not reproduced; the category toggle (click(26)) is.
  { label: 'CSSD', icon: 'home_icon/CSSD.png', moduleId: 88, clickable: true, category: 'CSSD' },
  { label: 'OTIMS', icon: 'home_icon/OTIMS.png', moduleId: 321, clickable: true, category: 'OTIMS' },
  // Dead tiles: `cursor-pointer` styling present, but NO ng-click
  // anywhere on the tile in legacy. Faithfully non-interactive.
  { label: 'Diet & Kitchen', icon: 'home_icon/Diet & Kitchen.png', moduleId: 337, clickable: false, category: null },
  { label: 'House Keeping', icon: 'home_icon/House Keeping.png', moduleId: 359, clickable: false, category: null },
  { label: 'Mortuary', icon: 'home_icon/Mortuary.png', moduleId: 362, clickable: true, category: 'mortuary' },
  { label: 'Procurement', icon: 'home_icon/Procurement.png', moduleId: 161, clickable: true, category: 'procurement' },
  { label: 'Store / Inventory', icon: 'home_icon/Store_ Inventory.png', moduleId: 121, clickable: false, category: null },
  { label: 'Pharmacy', icon: 'home_icon/Pharmacy.png', moduleId: 101, clickable: false, category: null },
  { label: 'Assets', icon: 'home_icon/Assets.png', moduleId: 203, clickable: true, category: 'assets' },
  { label: 'Equipments', icon: 'home_icon/Equipments.png', moduleId: 221, clickable: true, category: 'equipments' },
  // Dead: only a commented-out ng-click in legacy (:466).
  { label: 'Back Office', icon: 'home_icon/Back Office.png', moduleId: 678, clickable: false, category: null },
  // Confirmed legacy bug, faithfully reproduced: this tile's live
  // ng-click="click(17)" resolves to $scope.backOffice = true (same flag
  // the dead "Back Office" tile above would have set) -- there is no
  // separate "misReport" flag in the controller. See quick-links.models.ts.
  { label: 'MIS Report', icon: 'home_icon/MIS Report.png', moduleId: 768, clickable: true, category: 'backOffice' },
  { label: 'Linen-Laundry', icon: 'home_icon/Linen-Laundry.png', moduleId: 343, clickable: true, category: 'linenLaundry' },
  // The one tile with NO checkAccess gate at all in legacy (:516-533) --
  // always visible regardless of permissions. Faithfully kept ungated.
  { label: 'Blood Bank', icon: 'home_icon/Blood Bank.png', clickable: true, category: 'bloodBank' },
  { label: 'IVF', icon: 'home_icon/IVF.png', moduleId: 706, clickable: true, category: 'IVF' },
  // Real anchor target in legacy (href="#/videoConferenceNew"), not yet an
  // Angular route -- see QuickLinkTile.externalHref's doc.
  { label: 'Tele Consulting', icon: 'home_icon/Tele Consulting.png', moduleId: 769, clickable: true, category: 'teleConsulting', externalHref: '#/videoConferenceNew' },
  { label: 'QMS', icon: 'home_icon/QMS.png', moduleId: 790, clickable: true, category: 'QMS' },
  { label: 'Surgery Safety', icon: 'home_icon/Surgery Safety.png', moduleId: 766, clickable: true, category: 'surgerySafety' },
  { label: 'Global Master', icon: 'home_icon/Global Master.png', moduleId: 369, clickable: true, category: 'global' },
  { label: 'Unit Master', icon: 'home_icon/Unit Master.png', moduleId: 531, clickable: true, category: 'unit' },
];
