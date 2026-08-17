/** Pending orders payloads — field names EXACTLY as the corrected backend's
 * NursingOrderMapperDto (the @RequestBody of POST /api/opd/pendingOrders/...).
 * DATE CONVENTION: the order's dates (startDate/stopDate/createdDate/
 * updatedDate) are STRING — dd-M-yyyy HH:mm:ss via formatLegacyNow, the
 * registration/prescription convention, NOT the encounter's Long. */
export interface OrderCriteria {
  fromDate?: string | null;
  toDate?: string | null;
  specialityId?: number | null;
  patientId?: number | null;
  admissionId?: number | null;
  unitId?: number | null;
  organizationId?: number | null;
  status?: string | null;
}

/** The order row (the DTO's display fields — the grid's columns). */
export interface OrderRow {
  nursingOrderMapperId?: number | null;
  nursingOrderId?: number | null;
  patientId?: number | null;
  admissionId?: number | null;
  doctorId?: number | null;
  specialityId?: number | null;
  subSpecialityId?: number | null;
  serviceId?: number | null;
  frequencyId?: number | null;
  patientName?: string | null;
  serviceStandardName?: string | null;
  specialityName?: string | null;
  subSpecialityName?: string | null;
  frequency?: string | null;
  administrationTimefreq?: string | null;
  instruction?: string | null;
  startDate?: string | null;
  stopDate?: string | null;
  status?: string | null;
  color?: string | null;
  checkStatus?: boolean | null;
}

export interface IdNameRow {
  id: number;
  name?: string;
  desc?: string;
}
