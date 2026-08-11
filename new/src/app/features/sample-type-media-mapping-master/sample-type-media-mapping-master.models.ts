/**
 * Row shape AS RETURNED by GET /LIS/global/listSampleTypeMedia/{org}/{offset}/{size}
 * (legacy sampleTypeMediaMappingController.js:134/153, lis.url.config.js:259).
 *
 * NOTE (DQ-2026-08-12-023): the endpoint currently returns the generic error
 * envelope (HTTP 200, status=error, code 500, "Internal Server Error") on
 * every call — NOT dev-empty. LIS fall-through class: the DAO's size()>0
 * guard is a dead branch; the HQL projection (Transformers.aliasToBean into
 * Long-typed createdDate/updatedDate DTO fields with no @Convert converter)
 * throws first and the exception is swallowed at DAO/service/controller
 * (SampleTypeMediaMappingMaster.java:172-181 entity vs DTO, 2026-08-12).
 * The projection below is the intended row shape from the legacy view
 * ($scope.commonList); it is never observed while the endpoint is masked.
 */
export interface SampleTypeMediaMappingRow {
  sampleTypeName: string;
  mediaName: string;
  status: string;
}
