export type YesNo = 'Y' | 'N';

export interface FloorOption {
  floorId: number;
  floorName: string;
}

/** ctrl:94-106 -- `{{info.icutypeMasterId}}`/`{{info.icutype}}` per the view's ICU-type select. */
export interface IcuTypeOption {
  icutypeMasterId: number;
  icutype: string;
}

/** Raw shape of GETACTIVEGENDERLIST's `listObject` (ctrl:108-130), before the
 * controller re-maps it into `{id, label}` pairs for the multiselect. */
export interface GenderListItem {
  id: number;
  desc: string;
}

/** `{id, label}` shape the controller builds from `GenderListItem` (ctrl:122-128)
 * and feeds into the gender multiselect / re-sends as `genderIdList` on save. */
export interface GenderOption {
  id: number;
  label: string;
}

/**
 * Shape of each entry inside a ward row's `genderIdList` (attached
 * server-side by WardMasterServiceImpl for every row of GETWARDMASTERLIST/
 * GETWARDMASTERBYID via `GET_GENDER_LIST_BY_WARD_ID`). That HQL query
 * aliases the display text as `lable` (`gen.desc as lable` --
 * WardMaster.java), a real typo in the backend's MultiSelectDto/HQL
 * contract -- kept verbatim here rather than silently corrected, since it's
 * an existing API field name, not something this rebuild invented.
 */
export interface WardGenderDetail {
  id: number;
  lable: string;
}

/** Row shape returned by GETWARDMASTERLIST / GETWARDMASTERBYID (both backed
 * by the same WardMasterDto via aliasToBean; the "store" fields the DTO
 * also carries are dead in this screen -- both the store dropdown and the
 * store table column are commented out in the legacy view/controller -- so
 * they're intentionally not modeled here). */
export interface WardMasterRow {
  wardId: number;
  floorId: number;
  floorName: string;
  wardCode: string;
  wardName: string;
  isIpWard: YesNo;
  isEr: YesNo;
  isDayCare: YesNo;
  islaborWard: YesNo;
  ishemodialysisward: YesNo;
  isIcu: YesNo;
  icuTypeId: number | null;
  icuTypeName: string | null;
  status: 'A' | 'I';
  genderIdList: WardGenderDetail[];
}

/** Editable form state -- mirrors legacy's `$scope.wardMasterObj` (ctrl:50-57). */
export interface WardMasterForm {
  wardId?: number;
  floorId: number | '';
  wardCode: string;
  wardName: string;
  isIcu: YesNo;
  isEr: YesNo;
  isDayCare: YesNo;
  islaborWard: YesNo;
  isIpWard: YesNo;
  ishemodialysisward: YesNo;
  icuTypeId: number | '';
}

export function emptyWardMasterForm(): WardMasterForm {
  return {
    wardId: undefined,
    floorId: '',
    wardCode: '',
    wardName: '',
    isIcu: 'N',
    isEr: 'N',
    isDayCare: 'N',
    islaborWard: 'N',
    isIpWard: 'N',
    ishemodialysisward: 'N',
    icuTypeId: '',
  };
}

/** POST body for SAVEWARDMASTERMASTER (ctrl:293-320). `genderIdList` only
 * needs `id` server-side (WardMasterServiceImpl.saveWardMasterMaster reads
 * `multiSelectDto.getId()` only) but `label` is sent too, matching what
 * legacy actually puts on the wire (the full selected option objects). */
export interface WardMasterSavePayload {
  floorId: number;
  wardCode: string;
  wardName: string;
  isDayCare: YesNo;
  isEr: YesNo;
  isIcu: YesNo;
  genderIdList: { id: number; label: string }[];
  islaborWard: YesNo;
  ishemodialysisward: YesNo;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  unitId: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
  isIpWard: YesNo;
  icuTypeId: number | null;
}

/** POST body for UPDATEWARDMASTER (ctrl:468-485) -- no `createdBy`/`status`,
 * matching legacy exactly (only present on the create payload). */
export interface WardMasterUpdatePayload {
  floorId: number;
  wardCode: string;
  wardName: string;
  isDayCare: YesNo;
  isEr: YesNo;
  isIcu: YesNo;
  wardId: number;
  genderIdList: { id: number; label: string }[];
  islaborWard: YesNo;
  ishemodialysisward: YesNo;
  updatedBy: number;
  updatedDate: string;
  unitId: number;
  organizationId: number;
  isIpWard: YesNo;
  icuTypeId: number | null;
}
