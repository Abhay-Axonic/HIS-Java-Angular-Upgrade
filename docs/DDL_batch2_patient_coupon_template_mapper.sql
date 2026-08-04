-- BR-batch2 additive DDL (2026-08-03): billing.t_patient_coupon_template_mapper
-- + sequence were missing from the dev DB (entity-mapped, table absent — BR-155 class).
-- Mirrors PatientCouponTemplateMapper entity; percentage widened to numeric(5,2)
-- to match billing.m_coupon_template_details.percentage (batch-1 fix) so a
-- decimal coupon percentage survives the mapper boundary (old Integer truncated).
CREATE SEQUENCE IF NOT EXISTS billing.patient_coupon_template_mapper_seq INCREMENT BY 1 MINVALUE 1 MAXVALUE 9223372036854775807 START 1 CACHE 1 NO CYCLE;

CREATE TABLE IF NOT EXISTS billing.t_patient_coupon_template_mapper (
  patient_coupon_template_mapper_id integer DEFAULT nextval('billing.patient_coupon_template_mapper_seq'::regclass) NOT NULL,
  coupon_template_details_id integer,
  coupon_template_master_id integer,
  patient_id integer,
  consume boolean,
  speciality_id integer,
  sub_speciality_id integer,
  service_id integer,
  percentage numeric(5,2),
  coupon_template_description character varying,
  created_date timestamp without time zone,
  updated_date timestamp without time zone,
  updated_by integer,
  created_by integer,
  status character(1),
  organization_id integer,
  unit_id integer,
  coupon_code character varying,
  coupon_qr_url character varying,
  expiry_days integer,
  amount double precision,
  consumption character(1),
  order_details_id integer,
  CONSTRAINT t_patient_coupon_template_mapper_pkey PRIMARY KEY (patient_coupon_template_mapper_id)
);
