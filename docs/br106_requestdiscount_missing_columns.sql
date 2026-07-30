-- BR-106: billing.t_request_discount is missing 6 columns already mapped by
-- legacy/Entity/src/main/java/com/param/billing/global/transaction/model/RequestDiscount.java
-- (and referenced in its raw native queries GETR_EQUESTED_DISCOUNT,
-- GET_DISC_REQ_DTLS_FOR_OP_BILLING, GET_DISC_REQ_DTLS_FOR_BAD_DEBIT).
-- Already known, already scripted in docs/br076_missing_columns.sql (lines 51-56);
-- confirmed known_local_dev_drift=True in docs/schema-drift-sweep/missing_column_mismatches.tsv.
-- Purely additive, nullable, no existing data touched.
ALTER TABLE billing."t_request_discount" ADD COLUMN IF NOT EXISTS "from_patient_id" int4;
ALTER TABLE billing."t_request_discount" ADD COLUMN IF NOT EXISTS "to_patient_id" int4;
ALTER TABLE billing."t_request_discount" ADD COLUMN IF NOT EXISTS "request_from" int4;
ALTER TABLE billing."t_request_discount" ADD COLUMN IF NOT EXISTS "request_to" int4;
ALTER TABLE billing."t_request_discount" ADD COLUMN IF NOT EXISTS "username" bpchar(1);
ALTER TABLE billing."t_request_discount" ADD COLUMN IF NOT EXISTS "remark" varchar;
