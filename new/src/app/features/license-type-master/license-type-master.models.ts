/**
 * Row shape AS RETURNED by GET /org/getLiceneceTypeMasterList/{org} (legacy
 * licenseTypeMasterController.js:69-73). Confirmed live 2026-08-12: SUCCESS
 * with an empty listObject — honest dev-empty. Exact column aliases were not
 * captured during verification, so this interface uses the generic
 * id/name/status projection the legacy view reads (entity:
 * licenseTypeId/licenseTypeName).
 */
export interface LicenseTypeRow {
  id: number;
  name: string;
  status: string;
}
