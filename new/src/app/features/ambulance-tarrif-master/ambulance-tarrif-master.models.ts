/**
 * `ambulanceFirstDist`, `ambulanceFirstDistRate`, `ambulanceAddMileage`, and
 * `ambulanceAddMileageRate` are deliberately typed `string`, not `number`:
 * legacy's view (ambulance-Tarrif-Master.html:62,76,90,104) binds every one
 * of them via plain `<input type="text" ...>` -- no `is-number` directive,
 * no `type="number"`. Whatever the user types (or whatever the disabled
 * default carries) is sent over the wire as a string, matching the backend
 * model/DTO (`AmbulanceTarrif.java`, `AmbulanceTarrifDto.java`), which store
 * all four as `String` columns too.
 */
export interface AmbulanceTarrifRow {
  ambulanceTarrifMasterId: number;
  ambulanceType: string;
  ambulanceProvider: string;
  ambulanceOutSrcVender: string;
  ambulanceFirstDist: string;
  ambulanceFirstDistRate: string;
  ambulanceAddMileage: string;
  ambulanceAddMileageRate: string;
  status: 'A' | 'I';
}

export interface AmbulanceTarrifDetail {
  ambulanceTarrifMasterId: number;
  ambulanceType: string;
  ambulanceProvider: string;
  ambulanceOutSrcVender: string;
  ambulanceFirstDist: string;
  ambulanceFirstDistRate: string;
  ambulanceAddMileage: string;
  ambulanceAddMileageRate: string;
  status?: 'A' | 'I';
}

export interface AmbulanceTarrifSavePayload {
  ambulanceType: string;
  ambulanceProvider: string;
  ambulanceOutSrcVender: string;
  ambulanceFirstDist: string;
  ambulanceFirstDistRate: string;
  ambulanceAddMileage: string;
  ambulanceAddMileageRate: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

export interface AmbulanceTarrifUpdatePayload {
  ambulanceTarrifMasterId: number;
  ambulanceType: string;
  ambulanceProvider: string;
  ambulanceOutSrcVender: string;
  ambulanceFirstDist: string;
  ambulanceFirstDistRate: string;
  ambulanceAddMileage: string;
  ambulanceAddMileageRate: string;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

export interface AmbulanceTarrifStatusPayload {
  ambulanceTarrifMasterId: number;
  status: 'A' | 'I';
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}
