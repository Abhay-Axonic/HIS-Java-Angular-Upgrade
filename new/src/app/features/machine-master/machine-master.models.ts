/**
 * Field shape taken directly from the real Java backend DTO
 * (legacy/LIS/src/main/java/com/param/lis/unit/dto/MachineMasterDto.java) --
 * not guessed. `organisationId` (not `orgId`) is the wire field name the
 * backend actually expects/returns; legacy's own controller juggles a
 * differently-named local `common.orgId`/`common.unitId` form-state object
 * and only renames to `organisationId` when building the outgoing payload
 * (`initMachineMaster()`,
 * resources/assets/js/controller/lis/unit/machineMasterController.js:66-79).
 * This rebuild skips that local renaming indirection and uses the backend's
 * own field names throughout -- same wire behavior, simpler code.
 */
export interface MachineMasterRecord {
  machineId?: number;
  code: string;
  machineDesc: string;
  status: 'A' | 'I';
  createdBy?: number;
  createdDate?: number;
  updatedBy?: number;
  updatedDate?: number;
  organisationId?: number;
  unitId?: number;
}

/**
 * Mirrors legacy's `initCommon()` (ctrl:48-63): a fresh record for create
 * mode, `status` defaulted to `'A'`, `createdBy`/`updatedBy` defaulted to
 * the current session user.
 */
export function emptyMachineMasterRecord(unitId: number, organisationId: number, userId: number): MachineMasterRecord {
  return {
    machineId: undefined,
    code: '',
    machineDesc: '',
    status: 'A',
    createdBy: userId,
    createdDate: undefined,
    updatedBy: userId,
    updatedDate: undefined,
    organisationId,
    unitId,
  };
}
