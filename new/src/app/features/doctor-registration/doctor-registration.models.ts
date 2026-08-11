export type DoctorRegistrationStatus = 'A' | 'I';

/**
 * Row shape returned by POST /api/global/doctorDetailsUniversalSearch
 * (legacy doctorRegistrationController.js:4877 hardcoded URI, body
 * doctorSearchObj ctrl:192-201 + offset/noOfRecordsPerPage ctrl:876-877).
 * Projection is what the legacy view reads
 * (views/AdmissionDischarge/doctor-master.html:188-210 — docCode cell,
 * firstName+' '+lastName, docCategoryDescription, docDesignationDescription,
 * status; doctorId/userId passed to updateStatus).
 * Live-verified 2026-08-12: SUCCESS with an empty listObject — honest
 * dev-empty, the grid renders the @empty "No records." state.
 */
export interface DoctorRegistrationRow {
  doctorId: number;
  docCode: string;
  firstName: string;
  lastName: string;
  docCategoryDescription: string;
  docDesignationDescription: string;
  status: DoctorRegistrationStatus;
}
