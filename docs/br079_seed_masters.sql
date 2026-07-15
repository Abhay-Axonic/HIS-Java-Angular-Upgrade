-- BR-079: Phase 3 proactive master/lookup-table seeding for modules NOT yet E2E-tested tonight.
-- Scope: Assets, Inventory, Procurement, Linen & Laundry, Kitchen/Dietary, Blood Bank, RIS, CSSD.
-- Discipline (same as BR-046/049/053/054/067/070): only real, standard, structurally-necessary
-- values (a genuine department/category/status/UOM name) are inserted. No specific clinical or
-- business-judgment value (reference range, price, approval rule, screening-test cutoff, blood
-- compatibility rule, sterilization cycle parameter) is fabricated -- those fields are left NULL.
-- All rows use organization_id=1, unit_id=1, created_by/added_by/updated_by=1 (existing seed
-- convention from BR-070/BR-076 application). Local dev DB only (HIS-SME @ localhost:5434).

-- ============================================================
-- ASSETS (schema "assets" has zero dedicated masters of its own --
-- its masters live in public + inventory + equipment schemas)
-- ============================================================

-- New asset_category linking to the existing "Fixed" asset_type (id=2); the only pre-existing
-- category (id=1, "Pharmaceutical") is linked to "Consumable" (id=1) and unsuitable for
-- physical/fixed assets like equipment.
INSERT INTO public.m_asset_category (organization_id, unit_id, asset_type_id, code, category, is_active, updated_by, added_by)
VALUES (1, 1, 2, 'EQP', 'Equipment', true, 1, 1);

INSERT INTO public.m_product_category (organization_id, unit_id, asset_category_id, code, category, is_active, updated_by, added_by)
VALUES (1, 1, (SELECT id FROM public.m_asset_category WHERE code = 'EQP'), 'EQP', 'Equipment', true, 1, 1);

INSERT INTO public.m_fixed_asset_type (organization_id, unit_id, code, type, is_active, updated_by, added_by)
VALUES
  (1, 1, 'MEQ', 'Medical Equipment', true, 1, 1),
  (1, 1, 'ITE', 'IT Equipment', true, 1, 1);

-- one sample catalog entry so an indent/issue/return/corrective-maintenance workflow has an
-- asset to select; name is an explicitly-flagged placeholder, not a real asset
INSERT INTO public.m_fixed_asset_master (organization_id, unit_id, product_category_id, fixed_asset_type_id, item_code, product_code, name, manufacturer_id, is_active, updated_by, added_by)
VALUES (1, 1, (SELECT id FROM public.m_product_category WHERE code = 'EQP'),
        (SELECT id FROM public.m_fixed_asset_type WHERE code = 'MEQ'),
        'FA0001', 'FA0001', 'Sample Fixed Asset (placeholder -- needs owner review before real use)', 1, true, 1, 1);

-- ============================================================
-- INVENTORY (schema "inventory" -- PMS/indent config masters were empty)
-- ============================================================

INSERT INTO inventory.m_pms_frequency (organization_id, unit_id, code, description, is_active, updated_by, added_by)
VALUES
  (1, 1, 'MON', 'Monthly', true, 1, 1),
  (1, 1, 'QTR', 'Quarterly', true, 1, 1),
  (1, 1, 'YR', 'Yearly', true, 1, 1);

INSERT INTO inventory.m_pms_type (code, type, is_active, updated_by, added_by)
VALUES
  ('PM', 'Preventive Maintenance', true, 1, 1),
  ('BD', 'Breakdown Maintenance', true, 1, 1);

INSERT INTO inventory.m_working_condition (code, description, is_active, updated_by, added_by)
VALUES
  ('WRK', 'Working', true, 1, 1),
  ('NWRK', 'Not Working', true, 1, 1),
  ('REP', 'Under Repair', true, 1, 1);

-- public.m_store_item_mapper intentionally NOT seeded here: max_qty/min_qty (reorder-level
-- stock thresholds) are a business/inventory-manager judgment call, not a structural fact --
-- left empty and flagged for owner/inventory-manager input instead.

-- ============================================================
-- EQUIPMENT (schema "equipment" -- PMS checklist masters; note m_asset_schedule,
-- m_asset_schedule_checklist, m_pms_details, m_spare_consumption are NOT master/lookup
-- tables despite the "m_" prefix -- they hold per-asset scheduling/execution instance data
-- (ticket numbers, completion dates, remarks) and are correctly out of scope here)
-- ============================================================

INSERT INTO equipment.m_checklist (organization_id, unit_id, code, description, is_active, updated_by, added_by)
VALUES (1, 1, 'GEN', 'General Equipment Inspection Checklist', true, 1, 1);

INSERT INTO equipment.m_checklist_detail (organization_id, unit_id, checklist_id, code, description, is_active, updated_by, added_by)
VALUES
  (1, 1, (SELECT id FROM equipment.m_checklist WHERE code = 'GEN'), 'VIS', 'Visual Inspection', true, 1, 1),
  (1, 1, (SELECT id FROM equipment.m_checklist WHERE code = 'GEN'), 'FUNC', 'Functional Test', true, 1, 1);

-- ============================================================
-- PROCUREMENT (schema "procurement" -- only m_contract_type existed and was empty;
-- public.m_vendor, referenced throughout Procurement/Inventory, was also empty)
-- ============================================================

INSERT INTO procurement.m_contract_type (code, description, is_active, updated_by, added_by)
VALUES
  ('AMC', 'Annual Maintenance Contract', true, 1, 1),
  ('CMC', 'Comprehensive Maintenance Contract', true, 1, 1),
  ('RC', 'Rate Contract', true, 1, 1);

-- generic placeholder vendor -- contact/GST/pricing/payment-term fields left NULL/placeholder,
-- flagged for owner review before any real procurement transaction
INSERT INTO public.m_vendor (organization_id, unit_id, code, name, contact_person_name, address, country_id, is_local_purchase, is_active, updated_by, added_by)
VALUES (1, 1, 'VEND01', 'Sample Vendor (placeholder -- needs owner review before real use)', 'Not Set', 'Not Set', 1, true, true, 1, 1);

-- ============================================================
-- LINEN & LAUNDRY (schema "linen_and_laundry" has zero dedicated masters -- entirely
-- dependent on the shared public.m_store; only a Pharmacy-flagged store existed)
-- ============================================================

INSERT INTO public.m_store (unit_id, organization_id, store_type_id, store_name, store_code, is_active, updated_by, added_by, is_linen, is_laundry)
VALUES (1, 1, 5, 'Linen Store', 'LS01', true, 1, 1, true, true);

-- ============================================================
-- CSSD (schema "cssd" -- all 5 masters were empty; also needed its own store,
-- since only a Pharmacy-flagged store existed)
-- ============================================================

INSERT INTO public.m_store (unit_id, organization_id, store_type_id, store_name, store_code, is_active, updated_by, added_by, is_cssd)
VALUES (1, 1, 4, 'CSSD Store', 'CSSDS01', true, 1, 1, true);

INSERT INTO cssd.m_sterilization_type_master (code, name, is_deleted, created_by, updated_by, organization_id, unit_id, store_id)
VALUES
  ('STM', 'Steam (Autoclave)', false, 1, 1, 1, 1, (SELECT store_id FROM public.m_store WHERE store_code = 'CSSDS01')),
  ('ETO', 'Ethylene Oxide', false, 1, 1, 1, 1, (SELECT store_id FROM public.m_store WHERE store_code = 'CSSDS01'));

-- sterilization_temperature/pressure/biological_indicator_day intentionally left NULL --
-- specific cycle parameters are an operational/safety judgment call, flagged for owner review
INSERT INTO cssd.m_autoclave_master (machine_name, machine_description, sterilization_type, is_active, is_deleted, created_by, updated_by, status, organization_id, unit_id, store_id)
VALUES ('Autoclave 1', 'Sample autoclave (placeholder -- cycle parameters need owner review before real use)',
        (SELECT id FROM cssd.m_sterilization_type_master WHERE code = 'STM'), true, false, 1, 1, 'A', 1, 1,
        (SELECT store_id FROM public.m_store WHERE store_code = 'CSSDS01'));

INSERT INTO cssd.m_instrument_master (instrument_code, instrument_name, organization_id, unit_id, store_id)
VALUES
  ('INS001', 'Scissors - Surgical', 1, 1, (SELECT store_id FROM public.m_store WHERE store_code = 'CSSDS01')),
  ('INS002', 'Forceps - Artery', 1, 1, (SELECT store_id FROM public.m_store WHERE store_code = 'CSSDS01'));

INSERT INTO cssd.m_kit_master (code, name, is_active, is_deleted, created_by, updated_by, status, organization_id, unit_id, store_id)
VALUES ('KIT001', 'General Surgery Kit', true, false, 1, 1, 'A', 1, 1, (SELECT store_id FROM public.m_store WHERE store_code = 'CSSDS01'));

-- duration (sterile shelf-life) intentionally left NULL -- a specific numeric shelf-life is a
-- sterile-processing judgment call, flagged for owner review
INSERT INTO cssd.m_wrapper_expiry_master (code, name, is_active, is_deleted, created_by, updated_by, status, organization_id, unit_id, store_id)
VALUES ('WRP001', 'Non-Woven Wrapper (placeholder -- shelf-life duration needs owner review)', true, false, 1, 1, 'A', 1, 1,
        (SELECT store_id FROM public.m_store WHERE store_code = 'CSSDS01'));

-- ============================================================
-- KITCHEN / DIETARY (schema "kitchen" -- 11 masters, all empty. Only the purely structural/
-- calendar/generic-unit ones are seeded here. Dish/nutrition/ingredient/tube-feeding-product/
-- diet-template content is a dietitian's menu-and-nutrition-program decision, not a standard
-- universal fact, so those tables (m_dish_master, key_ingredients_master, m_nutrition_master's
-- specific values, m_tube_feeding_products, m_diet_template) are intentionally left empty and
-- flagged for dietitian/owner input instead of seeded here.)
-- ============================================================

INSERT INTO kitchen.m_diet_type_master (organization_id, diet_type_code, diet_type_description, created_by, status, unit_id)
VALUES
  (1, 'VEG', 'Vegetarian', 1, 'A', 1),
  (1, 'NVEG', 'Non-Vegetarian', 1, 'A', 1);

INSERT INTO kitchen.m_diet_timing_master (timing_type)
VALUES ('Breakfast'), ('Lunch'), ('Dinner');

INSERT INTO kitchen.m_meal_master (code, name, status, created_by, organization_id, unit_id)
VALUES
  ('BF', 'Breakfast', 'A', 1, 1, 1),
  ('LN', 'Lunch', 'A', 1, 1, 1),
  ('DN', 'Dinner', 'A', 1, 1, 1);

INSERT INTO kitchen.m_unit_of_measurment_master (uom_id, uom_name, uom_description, created_by, status, organization_id, unit_id)
VALUES
  (1, 'gm', 'Grams', 1, 'A', 1, 1),
  (2, 'ml', 'Milliliters', 1, 'A', 1, 1),
  (3, 'unit', 'Unit', 1, 'A', 1, 1);

INSERT INTO kitchen.m_week_days_master (day)
VALUES ('Monday'), ('Tuesday'), ('Wednesday'), ('Thursday'), ('Friday'), ('Saturday'), ('Sunday');

-- standard universal nutrient category names only, no numeric reference values (clinical judgment)
INSERT INTO kitchen.m_nutrition_master (nutrition_id, organization_id, nutrition_code, nutrition_name, created_by, status, unit_id, is_calories)
VALUES
  (1, 1, 'PROT', 'Protein', 1, 'A', 1, 'N'),
  (2, 1, 'CARB', 'Carbohydrate', 1, 'A', 1, 'N'),
  (3, 1, 'FAT', 'Fat', 1, 'A', 1, 'N'),
  (4, 1, 'CAL', 'Calories', 1, 'A', 1, 'Y'),
  (5, 1, 'FIB', 'Fiber', 1, 'A', 1, 'N');

-- ============================================================
-- BLOOD BANK (schema "blood_bank" -- 21 masters, all empty. Standard/universal identity,
-- category, and workflow-status masters are seeded. Clinical-judgment tables (compatibility
-- matrix, precheck criteria, donor rejection/screening criteria) are intentionally left empty
-- and flagged for clinical/owner review instead of seeded here.)
-- ============================================================

INSERT INTO blood_bank.m_blood_group_master (blood_group_code, blood_group_desc, created_by, status, unit_id, organization_id)
VALUES
  ('A+', 'A Positive', 1, 'A', 1, 1),
  ('A-', 'A Negative', 1, 'A', 1, 1),
  ('B+', 'B Positive', 1, 'A', 1, 1),
  ('B-', 'B Negative', 1, 'A', 1, 1),
  ('O+', 'O Positive', 1, 'A', 1, 1),
  ('O-', 'O Negative', 1, 'A', 1, 1),
  ('AB+', 'AB Positive', 1, 'A', 1, 1),
  ('AB-', 'AB Negative', 1, 'A', 1, 1);

-- capacity_max/min intentionally left NULL on the type master (a specific numeric fill volume
-- policy is an operational decision) -- capacity master below only records the standard label
INSERT INTO blood_bank.m_blood_bag_type_master (blood_bag_type_code, blood_bag_type_desc, item_name, created_by, status, unit_id, organization_id)
VALUES
  ('SGL', 'Single Bag', 'Single Bag', 1, 'A', 1, 1),
  ('DBL', 'Double Bag', 'Double Bag', 1, 'A', 1, 1),
  ('TPL', 'Triple Bag', 'Triple Bag', 1, 'A', 1, 1),
  ('QAD', 'Quadruple Bag', 'Quadruple Bag', 1, 'A', 1, 1);

INSERT INTO blood_bank.m_blood_bag_capacity_master (blood_bag_capacity_code, blood_bag_capacity_desc, created_by, status, unit_id, organization_id)
VALUES
  ('350', '350 ml', 1, 'A', 1, 1),
  ('450', '450 ml', 1, 'A', 1, 1);

-- expiry/quantity intentionally left NULL -- specific shelf-life/volume policy is an
-- operational/clinical decision, flagged for owner review
INSERT INTO blood_bank.m_blood_component_master (blood_component_code, blood_component_desc, blood_component_short_desc, created_by, status, unit_id, organization_id)
VALUES
  ('WB', 'Whole Blood', 'WB', 1, 'A', 1, 1),
  ('PRBC', 'Packed Red Blood Cells', 'PRBC', 1, 'A', 1, 1),
  ('FFP', 'Fresh Frozen Plasma', 'FFP', 1, 'A', 1, 1),
  ('PLT', 'Platelet Concentrate', 'PLT', 1, 'A', 1, 1),
  ('CRYO', 'Cryoprecipitate', 'CRYO', 1, 'A', 1, 1);

INSERT INTO blood_bank.m_blood_donor_type_master (blood_donor_type_code, blood_donor_type_desc, created_by, status, unit_id, organization_id)
VALUES
  ('VOL', 'Voluntary', 1, 'A', 1, 1),
  ('REP', 'Replacement', 1, 'A', 1, 1),
  ('AUTO', 'Autologous', 1, 'A', 1, 1);

INSERT INTO blood_bank.m_blood_bank_status_master (name, created_date, created_by, status, unit_id, organization_id)
VALUES
  ('Available', now(), 1, 'A', 1, 1),
  ('Reserved', now(), 1, 'A', 1, 1),
  ('Issued', now(), 1, 'A', 1, 1),
  ('Discarded', now(), 1, 'A', 1, 1),
  ('Expired', now(), 1, 'A', 1, 1);

INSERT INTO blood_bank.m_deffered_type_master (deffered_type_id, deffered_type_name)
VALUES (1, 'Temporary'), (2, 'Permanent');

INSERT INTO blood_bank.m_reason_of_discard_master (reason_of_discard_code, reason_of_discard_desc, created_by, status, unit_id, organization_id)
VALUES
  ('EXP', 'Expired', 1, 'A', 1, 1),
  ('DMG', 'Damaged Bag', 1, 'A', 1, 1),
  ('TTI', 'TTI Reactive', 1, 'A', 1, 1),
  ('HEM', 'Hemolyzed', 1, 'A', 1, 1);

INSERT INTO blood_bank.m_cross_matching_report_status (cross_matching_report_name, created_by, status, unit_id, organization_id)
VALUES
  ('Compatible', 1, 'A', 1, 1),
  ('Incompatible', 1, 'A', 1, 1),
  ('Pending', 1, 'A', 1, 1);

INSERT INTO blood_bank.m_blood_bank_master (blood_bank_code, blood_bank_desc, created_by, status, unit_id, organization_id)
VALUES ('MAIN', 'Main Blood Bank', 1, 'A', 1, 1);

-- standard TTI (transfusion-transmitted infection) screening test names only -- no cutoff/
-- reference values (clinical judgment), flagged for owner/clinical review before real use
INSERT INTO blood_bank.m_blood_test_master (blood_test_code, blood_test_desc, created_by, status, unit_id, organization_id)
VALUES
  ('HIV', 'HIV', 1, 'A', 1, 1),
  ('HBSAG', 'Hepatitis B (HBsAg)', 1, 'A', 1, 1),
  ('HCV', 'Hepatitis C (HCV)', 1, 'A', 1, 1),
  ('VDRL', 'Syphilis (VDRL)', 1, 'A', 1, 1),
  ('MAL', 'Malaria', 1, 'A', 1, 1);

INSERT INTO blood_bank.m_screening_test_master (screening_test_code, screening_test_desc, created_by, status, unit_id, organization_id, test_id)
VALUES
  ('HIV', 'HIV', 1, 'A', 1, 1, (SELECT blood_test_id FROM blood_bank.m_blood_test_master WHERE blood_test_code = 'HIV')),
  ('HBSAG', 'Hepatitis B (HBsAg)', 1, 'A', 1, 1, (SELECT blood_test_id FROM blood_bank.m_blood_test_master WHERE blood_test_code = 'HBSAG')),
  ('HCV', 'Hepatitis C (HCV)', 1, 'A', 1, 1, (SELECT blood_test_id FROM blood_bank.m_blood_test_master WHERE blood_test_code = 'HCV')),
  ('VDRL', 'Syphilis (VDRL)', 1, 'A', 1, 1, (SELECT blood_test_id FROM blood_bank.m_blood_test_master WHERE blood_test_code = 'VDRL')),
  ('MAL', 'Malaria', 1, 'A', 1, 1, (SELECT blood_test_id FROM blood_bank.m_blood_test_master WHERE blood_test_code = 'MAL'));

INSERT INTO blood_bank.m_blood_test_result_master (blood_test_result_code, blood_test_result_desc, created_by, status, unit_id, organization_id)
VALUES
  ('REACT', 'Reactive', 1, 'A', 1, 1),
  ('NREACT', 'Non-Reactive', 1, 'A', 1, 1);

-- ============================================================
-- RIS / RADIOLOGY (public.m_modality_master -- the modality lookup used by
-- opd.m_slot_master and radiology order booking; equipment_id left NULL since
-- public.m_equipment is itself empty and not in this session's scope to populate)
-- ============================================================

INSERT INTO public.m_modality_master (organization_id, unit_id, modality_code, modality_desc, status, speciality_id, sub_speciality_id)
VALUES
  (1, 1, 'XRAY', 'X-Ray', 'A', 1, 1),
  (1, 1, 'CT', 'CT Scan', 'A', 1, 1),
  (1, 1, 'MRI', 'MRI', 'A', 1, 1),
  (1, 1, 'USG', 'Ultrasound', 'A', 1, 1);
