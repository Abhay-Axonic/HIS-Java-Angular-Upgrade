/**
 * Parity rebuild of
 * legacy/HIS-WAR/.../controller/master/unit/qualityParameterMasterController.js
 * + legacy/Global/.../unit/dto/QualityParameterMasterDTO.java. Field names
 * kept verbatim from the DTO -- this is the exact JSON shape
 * `/api/unit/saveQualityParam` / `/api/unit/updateStatus` consume and
 * `/api/unit/getQualityParamList/{unitId}/{organizationId}` returns.
 */
export interface QualityParameterMasterRecord {
  qualityParameterMasterId: number | '';
  unitId: number;
  organizationId: number;
  moduleId: number | '';
  moduleName: string;
  activityId: number | '';
  activityName: string;
  parameterId: number | '';
  standard: string;
  uomId: number | '';
  uomDesc: string;
  thresholdPercentage: number | '';
  targetPercentage: number | '';
  status: 'A' | 'I';
  /** `Character update` on the DTO -- 'Y' on an update save, 'N' on create (ctrl:164/218-224). */
  update: 'Y' | 'N';
  createdBy: number | '';
  updatedBy: number | '';
  createdDate: string;
  updatedDate: string;
}

/**
 * UOM dropdown option -- `GET /LIS/global/listUnitMaster/{organizationId}`.
 * Legacy's `getUOMList()` (ctrl:39-58) copies each raw `{id, desc}` list item
 * into `$scope.UOMListFiltered` verbatim (the copy adds nothing); the
 * rebuild consumes the response shape directly.
 */
export interface UomOption {
  id: number;
  desc: string;
}

/** Module/"Department" dropdown option -- `GET /module/getZeroParentModule` (ctrl:66-80). */
export interface ModuleOption {
  moduleId: number;
  moduleName: string;
}

export function emptyQualityParameterMaster(
  unitId: number,
  organizationId: number,
): QualityParameterMasterRecord {
  return {
    qualityParameterMasterId: '',
    unitId,
    organizationId,
    moduleId: '',
    moduleName: '',
    activityId: '',
    activityName: '',
    parameterId: '',
    standard: '',
    uomId: '',
    uomDesc: '',
    thresholdPercentage: '',
    targetPercentage: '',
    status: 'A',
    update: 'N',
    createdBy: '',
    updatedBy: '',
    createdDate: '',
    updatedDate: '',
  };
}
