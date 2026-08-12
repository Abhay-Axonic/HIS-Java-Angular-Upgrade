/**
 * Intended row shape of POST /api/orderset/orderSetsList — the projection
 * of LIST_ORDER_MASTER / LIST_ORDER_MASTER_FOR_ICPC (OrdersetMaster.java
 * 149-207, alias-to-bean OrdersetMasterDto).
 *
 * MASKED-ERROR (DQ-2026-08-12-031): the endpoint returns the masked envelope
 * {"status":"error","code":"500","message":"Something went wrong!!"} — NOT
 * dev-empty. Root cause: LIST_ORDER_MASTER_FOR_ICPC (OrdersetMaster.java:199)
 * joins `osm.icpc_id = icpc.icpc_id`, but doctor.m_orderset_master has NO
 * icpc_id column (information_schema: 14 columns). psql proof: Q1
 * (LIST_ORDER_MASTER) runs clean; Q2 fails `column osm.icpc_id does not
 * exist`. DAO rethrows, service rethrows, OrdersetController:93-96 catches
 * and masks. FOURTH phantom column (technique_id, is_bed_charges_service,
 * form_template, icpc_id). DDL fix (add icpc_id) is a BR candidate, NOT
 * applied (Rule 10).
 */
export interface OrdersetRow {
  ordersetMasterId: number;
  ordersetName: string;
  ordersetTypeId?: number;
  ordersetTypeName?: string;
  diagnosisId?: number;
  diagnosisName?: string;
  icpcId?: number;
  icpcDesc?: string;
  specialityId?: number;
  specialityName?: string;
  doctorId?: number;
  doctorName?: string;
  status?: string;
}
