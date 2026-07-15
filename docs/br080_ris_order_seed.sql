-- BR-080: RIS Chest X-Ray order E2E test -- master-data gap closure
-- Closes the structural gap that blocked ordering the pre-existing RAD-CXR service
-- (service.m_service_master id=13, "Chest X-Ray") from the OPD Cover Sheet's
-- Diagnostics/Procedure order tab -- same class of gap as BR-046/053/054/067/070/079
-- (a service row existed, but the surrounding master-data chain needed to price/search
-- it did not). Local dev DB only (his-local-postgres / HIS-SME). Run in one transaction.

BEGIN;

-- 1. New "Radiology" sub-speciality under the existing "General Medicine" speciality (id=1),
--    same placement pattern BR-070 used for "Hematology" (sub_speciality_id=2) under lab.
--    Required because SEARCH_SERVICES_BY_NAME_AND_CODE (the Cover Sheet's service typeahead
--    query) inner-joins doctor.m_sub_speciality_master on service.sub_speciality_id, which was
--    NULL for RAD-CXR/RAD-USG -- an inner join on NULL never matches, so neither service could
--    ever appear in the search results.
INSERT INTO doctor.m_sub_speciality_master
  (sub_speciality_code, sub_speciality_name, created_by, status, speciality_id, updated_by, organization_id, is_modality, refdocper, general_ledger_id_ip, is_mri_modality, is_ct_modality)
VALUES
  ('RAD', 'Radiology', 1, 'A', 1, 1, 1, 'N', 0, 0, 'N', 'N')
RETURNING sub_speciality_id;
-- -> sub_speciality_id 3 (verified via psql; substitute if re-run against a different DB state)

-- 2. Link Chest X-Ray (service_master_id=13) to that new sub-speciality (and the same
--    General Medicine speciality CBC/service_master_id=8 already uses).
UPDATE service.m_service_master
SET speciality_id = 1, sub_speciality_id = 3
WHERE service_master_id = 13;

-- 3. Unit-level service detail row -- required by the same search query's inner join to
--    service.t_unit_service_master_details. Mirrors CBC's existing row (service_master_id=8)
--    field-for-field, except sample_collection='N' (imaging, not a blood draw) and
--    is_pacs_req left 'N' (PACS integration is a clinical/ops decision, not seeded here).
INSERT INTO service.t_unit_service_master_details
  (service_master_id, is_outsource, is_package, is_rate_editable, is_tax_applicable, is_ref_doctor_share, is_discount,
   unit_id, organization_id, status, is_panel, is_allow_multiple_quantity, is_doc_req, gender, sample_collection, is_pacs_req)
VALUES
  (13, 'N', 'I', 'N', 'N', 'N', 'Y', 1, 1, 'A', 'N', 'Y', 'N', 0, 'N', 'N');

-- 4. Base price row -- required by the same search query's inner join to
--    service.m_service_base_price_master. Value (400) matches the rate already present on
--    the pre-existing tariff row for this service (service.m_unit_service_tariff_master,
--    unit_service_tariff_id=13, seeded before this session) -- not a newly-invented price.
INSERT INTO service.m_service_base_price_master
  (service_master_id, visit_type_id, base_price, status, unit_id, organisation_id)
VALUES
  (13, 1, 400, 'A', 1, 1);

-- 5. Patient-category/bed-category-specific tariff row. The pre-existing tariff row
--    (unit_service_tariff_id=13) has NULL patient_type_id/billing_bed_category_id, which does
--    NOT satisfy EncounterMasterDaoImpl.getBasePriceByServiceTariffMaster's exact-match WHERE
--    clause (patient_type_id=:patientTypeId AND billing_bed_category_id=:billingBedCategoryId)
--    -- same gap class BR-070 already found and fixed for CBC (its unit_service_tariff_id=18).
--    patient_type_id=3 ("Staff") / billing_bed_category_id=1 match this project's standard E2E
--    test patient category.
INSERT INTO service.m_unit_service_tariff_master
  (organization_id, unit_id, service_id, tariff_id, payment_entitlement_id, visit_type_id, patient_type_id, billing_bed_category_id, rate, status, effective_date)
VALUES
  (1, 1, 13, 1, 1, 1, 3, 1, 400, 'A', now());
-- NOTE: this session's first attempt at this INSERT omitted effective_date (defaults to NULL,
-- there is no column default). getBasePriceByServiceTariffMaster's WHERE clause filters
-- "to_char(unitservic0_.effective_date,'YYYY-MM-DD') <= :effectiveDate" -- NULL <= anything is
-- never true in SQL, so the row silently never matched until a live E2E run surfaced a
-- growl "Service is not mapped with the tariff..!!" warning and the gap was traced and
-- corrected within the same session (UPDATE ... SET effective_date = now()). Included here so
-- a fresh run of this script produces a working row in one pass.

COMMIT;
