/**
 * Field shapes match the legacy backend contract exactly (including the
 * `qmsCallMassage` typo) -- see QMSTokenDisplayMasterController.js and
 * docs/ANGULAR_PENDING_FINDINGS.md entry 33 for the audited behavior these
 * types support.
 */

export interface QmsActiveDept {
  qmsDeptMasterId: number;
  qmsDeptName: string;
  qmsDesc?: string;
}

export interface QmsTokenDisplayItem {
  tokenNumberWithStartLetter: string;
  assignedDeptId: number;
  isCall: 'A' | 'N' | null;
  qmsCounterDesc?: string;
  callLogStatus?: number;
}

export interface QmsTokenMessage {
  qmsCallMassage: string;
}

export interface UnitBillingAppConfig {
  tableHeaderColor?: string;
}
