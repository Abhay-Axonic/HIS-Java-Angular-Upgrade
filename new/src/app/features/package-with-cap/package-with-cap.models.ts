/**
 * Row shape returned by POST /api/packages/filter listObject
 * (legacy services/unit/packageWithCAPController.js:56-68
 * initPackageMasterList; view package-with-CAP-main.html binds
 * packageType/packageCode/packageName/versionName/status). Same envelope
 * as the sibling multicounter-package screen (BR-159 golden trace). For
 * THIS screen the call is live-verified 2026-08-12: SUCCESS with an EMPTY
 * listObject — honest dev-empty, no rows to render yet.
 */
export interface PackageRow {
  parentPackageId: number;
  packageCode?: string | null;
  packageName?: string | null;
  packageType?: string | null;
  versionName?: string | null;
  packageTypeId?: number | null;
  status?: string;
  orgId?: number | null;
  unitId?: number | null;
  packagePrice?: number | null;
  packageCost?: number | null;
  [key: string]: unknown;
}
