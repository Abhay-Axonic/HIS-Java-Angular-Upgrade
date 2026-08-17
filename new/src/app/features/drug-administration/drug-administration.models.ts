/** Pharmacy worklist row — the corrected backend's list rows (generic
 * key/value; the display fields per screen are patient/order/item/status).
 * DATE CONVENTION: String dates (dd-M-yyyy HH:mm:ss via formatLegacyNow) —
 * the registration/prescription convention, NOT the encounter's Long. */
export interface PharmacyRow {
  patientId?: number;
  patientName?: string;
  orderId?: number;
  orderDetailId?: number;
  itemId?: number;
  itemName?: string;
  dose?: string;
  route?: string;
  frequency?: string;
  reason?: string;
  status?: string;
  createdDate?: string;
  [key: string]: unknown;
}

export interface IdNameRow { id: number; name?: string; desc?: string; }

export interface SavePayload {
  patientId?: number | null;
  orderDetailId?: number | null;
  itemId?: number | null;
  dose?: string | null;
  routeId?: number | null;
  frequencyId?: number | null;
  reason?: string | null;
  status?: string | null;
  unitId?: number | null;
  organizationId?: number | null;
  createdBy?: number | null;
  createdDate?: string | null;
  updatedBy?: number | null;
  updatedDate?: string | null;
  [key: string]: unknown;
}
