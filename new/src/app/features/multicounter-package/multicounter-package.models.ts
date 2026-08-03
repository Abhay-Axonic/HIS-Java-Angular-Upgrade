export interface PackageSearchRow {
  parentPackageId: number;
  packageName?: string | null;
  packageCode?: string | null;
  packageTypeId?: number | null;
  status?: string;
  orgId?: number | null;
  unitId?: number | null;
  packagePrice?: number | null;
  packageCost?: number | null;
  [key: string]: unknown;
}
