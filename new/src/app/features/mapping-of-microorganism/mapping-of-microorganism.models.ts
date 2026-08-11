/**
 * Row shape AS RETURNED by
 * GET /LIS/global/listAntibioticOrganismMpprMaster/{org}/{offset}/{size}
 * (legacy antibioticOrganismMpprController.js:184/205, lis.url.config.js:387).
 *
 * NOTE (DQ-2026-08-12-023): the endpoint currently returns the generic error
 * envelope (HTTP 200, status=error, code 500, "Internal Server Error") on
 * every call — NOT dev-empty. LIS fall-through class: the DAO's size()>0
 * guard is a dead branch; the HQL projection (Transformers.aliasToBean into
 * Long-typed createdDate/updatedDate DTO fields with no @Convert converter)
 * throws first and the exception is swallowed at DAO/service/controller
 * (2026-08-12, same sibling-DTO shape as SampleTypeMediaMappingMasterDto /
 * MediaColonyMapperMasterDto). The projection below is the intended row shape
 * from the legacy view ($scope.commonList); it is never observed while the
 * endpoint is masked.
 */
export interface MappingofMicroorganismRow {
  antibioticName: string;
  organismName: string;
  status: string;
}
