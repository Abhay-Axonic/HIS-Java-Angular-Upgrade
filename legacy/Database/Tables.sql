--hf-3.08.1.2 started

CREATE SEQUENCE adt.speciality_triage_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 1
	CACHE 1
	NO CYCLE;


    CREATE TABLE IF NOT EXISTS adt.speciality_triage (
        speciality_triage_id INT4 DEFAULT nextval('adt.speciality_triage_seq') NOT NULL,

        admission_details_id INT4 NOT NULL,
        admission_id INT4 NULL,

        speciality_triage VARCHAR(20) NOT NULL,   -- PAEDIATRIC / OBSTETRIC

        triage_details text NULL,                -- full form data

        created_by INT4 NULL,
        created_date TIMESTAMP DEFAULT now(),
        updated_by INT4 NULL,
        updated_date TIMESTAMP DEFAULT now(),

        CONSTRAINT speciality_triage_pkey PRIMARY KEY (speciality_triage_id),

        CONSTRAINT fk_admission_details
            FOREIGN KEY (admission_details_id)
            REFERENCES adt.t_admission_details(admission_details_id)
    );


CREATE SEQUENCE public.t_aws_credentials_seq
    INCREMENT BY 1
    MINVALUE 1
    MAXVALUE 9223372036854775807
    START 1
	CACHE 1
	NO CYCLE;

CREATE TABLE IF NOT EXISTS public.t_aws_credentials (
    aws_key_id INT4 DEFAULT nextval('public.t_aws_credentials_seq') NOT NULL,
    access_key text Default NULL,
    secret_key text Default NULL,
    status character(1) DEFAULT 'A'::bpchar,
    created_by INT4 NULL,
    created_date TIMESTAMP DEFAULT now(),
    updated_by INT4 NULL,
    updated_date TIMESTAMP DEFAULT now()
);


CREATE SEQUENCE IF NOT EXISTS opd.ai_appointment_seq
	INCREMENT BY 1
	MINVALUE 1
	MAXVALUE 9223372036854775807
	START 7
	CACHE 1
	NO CYCLE;

CREATE TABLE IF NOT EXISTS opd.ai_appointment_queue (
	ai_appointment_id int4 DEFAULT nextval('opd.ai_appointment_seq'::regclass) NOT NULL,
	appointment_id int4 NULL,
	doctor_id int4 NULL,
	consulted_by int4 NULL,
	created_by int4 NULL,
	created_date timestamp DEFAULT now() NULL,
	updated_by int4 NULL,
	updated_date timestamp DEFAULT now() NULL,
	channel_id int4 DEFAULT 1 NULL,
	status varchar(100) NULL,
	ai_payload_json text NULL,
	is_payload_finalize bpchar(1) DEFAULT 'N'::bpchar NULL,
	CONSTRAINT pk_ai_appointment_id PRIMARY KEY (ai_appointment_id)
);

--HIS-42088 ABDM Security Issues- Billing Validations
alter table t_unit_application_billing_configuration add column if not exists max_input_amount NUMERIC(15, 0) default null;
--alter table t_unit_application_billing_configuration drop column if exists max_input_amount;

--
alter table t_unit_application_billing_configuration add column if not exists is_ai_doctor_enabled character(1) DEFAULT 'N'::bpchar;
--alter table t_unit_application_billing_configuration drop column if exists is_ai_doctor_enabled;


ALTER TABLE doctor.m_doctor_master
ADD COLUMN IF NOT EXISTS ai_doctor CHAR(1) DEFAULT 'N';

-- ALTER TABLE doctor.m_doctor_master
-- DROP COLUMN IF EXISTS ai_doctor;

-- HIS-42119 Query Optimization
CREATE INDEX IF NOT EXISTS CONCURRENTLY idx_encounter_status_patient_date
    ON opd.t_encounter_master
    (status_id, encounter_date, patient_id, encounter_id);

CREATE INDEX IF NOT EXISTS idx_admission_patient_status
    ON adt.t_admission
    (patient_id, status, admission_id DESC);

--	HIS-42107 Query Optimization
CREATE INDEX concurrently if not exists idx_tprm_bill_latest
    ON billing.t_payment_reciept_master
    (
        billing_master_id,
        date_of_reciept DESC
    )
    WHERE status='A'
    AND reciept_cancelled='N';

CREATE INDEX CONCURRENTLY if not exists idx_doctor_share_pending
    ON billing.t_doctor_share
    (
        org_id,
        org_unit_id,
        doc_id
    )
    WHERE status='A'
    AND referral_id IS NULL
    AND due_share_amt > 0;

CREATE INDEX CONCURRENTLY if not exists idx_dslogs_docid
    ON billing.t_doctor_share_split_logs
    (
        doc_id,
        doctor_share_id
    );

--HIS-42140 Query Optimization
CREATE INDEX if not exists idx_order_details_doc_share
    ON public.t_order_details(order_sch_date, order_details_id)
    WHERE doc_share_per > 0
    AND ord_doc_id IS NOT NULL
    AND ord_doc_spl_id IS NOT NULL
    AND ord_doc_id > 0;

CREATE INDEX CONCURRENTLY if not exists idx_order_details_ref_doc_share
    ON public.t_order_details(order_sch_date, order_details_id)
    WHERE ref_doc_share_per > 0;

CREATE INDEX CONCURRENTLY if not exists idx_bdtls_order_share
    ON billing.t_billing_details
    (
    order_details_id,
    unit_id,
    is_doctor_share_posted
    );