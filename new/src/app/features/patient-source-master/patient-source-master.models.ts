export type PatientSourceStatus = 'A' | 'I';

/**
 * Row shape the REAL, reachable `getPatientSourceMasterList` response
 * actually uses. `patientSourceMasterController.js` is registered on TWO
 * legacy ui-router states -- see patient-source-master.ts's class doc
 * comment for the full writeup -- and the ONLY one with a live sidebar link
 * (`new-side-bar.html:3398`, `href="#/commonMaster/patientsource"`) renders
 * its table from the shared `common-master-screen.html`'s
 * `ng-repeat="info in commonList"`, which reads generic
 * `info.id`/`info.code`/`info.desc`/`info.status` -- exactly what
 * `$scope.commonList` (the real list response) actually contains.
 */
export interface PatientSourceRow {
  id: number;
  code: string;
  desc: string;
  status: PatientSourceStatus;
}

/**
 * Shape returned by getPatientSourceById (ctrl:241-265, `showUpdateBtn`,
 * wired to the shared table's edit pencil `ng-click="showUpdateBtn(info.id)"`).
 */
export interface PatientSourceDetail {
  id: number;
  code: string;
  desc: string;
}

/** Form model. Matches `$scope.common` (ctrl:190-199/323-330/257-259) --
 * the object the controller's real `save()`/`update()`/`showUpdateBtn()`
 * actually read from/write to. */
export interface PatientSourceForm {
  id?: number;
  code: string;
  desc: string;
}

export function emptyPatientSourceForm(): PatientSourceForm {
  return {
    id: undefined,
    code: '',
    desc: '',
  };
}

/** POST body for savePatientSourceMaster (ctrl:190-199, `ADDPATIENTSOURCE`). */
export interface PatientSourceSavePayload {
  code: string;
  desc: string;
  status: 'A';
  createdBy: number;
  updatedBy: number;
  organizationId: number;
  createdDate: string;
  updatedDate: string;
}

/** POST body for updatePatientSourceMaster (ctrl:323-330, `UPDATEPATIENTSOURCE`).
 * No `status` field -- confirmed via direct read of ctrl:323-330, same
 * shape as `occupation-master`'s equivalent payload (unlike
 * `marital-status-master`'s, which does include one). */
export interface PatientSourceUpdatePayload {
  id: number;
  code: string;
  desc: string;
  updatedBy: number;
  updatedDate: string;
  organizationId: number;
}

/** POST body for updateStatusForPatientSource (ctrl:283-288). No
 * `organizationId` sent -- confirmed via ctrl:283-288. */
export interface PatientSourceStatusPayload {
  id: number;
  status: PatientSourceStatus;
  updatedBy: number;
  updatedDate: string;
}
