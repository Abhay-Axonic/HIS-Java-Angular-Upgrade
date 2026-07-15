-- BR-085: Diet Planning E2E test -- master-data gap closure
-- Local dev DB only (his-local-postgres / HIS-SME). Run in a single transaction.

BEGIN;

-- 1. adt.m_ward_master.is_ip_ward is 'N' for every ward in the dev DB, so the Diet Order
--    Patient List screen's "In Patient" visit-type filter (WardMasterDaoImpl.getWardByVisitType,
--    AND wd.is_ip_ward=:getIsIpWard when isIpWard='Y') returns zero wards and the screen can
--    never show an admitted IPD patient. All 5 existing wards are legitimate inpatient wards by
--    name (General/Maternity/Paediatric/Surgical/ICU, none ER- or day-care-only) -- flipping the
--    flag is a structural correction, not a business-judgment call.
UPDATE adt.m_ward_master SET is_ip_ward = 'Y' WHERE unit_id = 1 AND organization_id = 1;

-- 2. adt.m_meal_preference_master was completely empty -- blocks
--    dietPatientRequestTransactionController.js's getDietPreferenceList() (GET
--    /api/adt/unit/getMealPreferenceMasterList), which populates the "Diet Preference" dropdown
--    that the save flow requires before it will let the user pick a diet type or dish. One
--    generic, standard preference type (no clinical meaning).
INSERT INTO adt.m_meal_preference_master
  (organization_id, unit_id, meal_preference_code, meal_preference_desc, created_by, status)
VALUES
  (1, 1, 'GEN', 'General', 1, 'A')
RETURNING meal_preference_id;

-- 3. kitchen.m_diet_template was empty (BR-079 deliberately left it for dietitian input).
--    getDietTemplateByDietPrefIdsAndDietTypeId returns ERROR "No matching Diet Template is
--    Available" with zero rows, which leaves $scope.dietTemplate.mealDetailsList empty and the
--    UI renders no meal/day grid at all -- the save flow can never be reached. This is just the
--    structural container (diet_type_id + org/unit); it carries no menu content of its own --
--    the day/meal cells start empty and are filled in live by the person using the screen.
INSERT INTO kitchen.m_diet_template
  (diet_type_id, organization_id, unit_id, created_by, updated_by, created_date, updated_date)
SELECT diet_type_id, 1, 1, 1, 1, CURRENT_DATE, CURRENT_DATE
FROM kitchen.m_diet_type_master WHERE diet_type_code = 'VEG' AND organization_id = 1 AND unit_id = 1
RETURNING diet_template_id;

-- 4. Link the new diet template to the new meal preference (both from steps above).
INSERT INTO kitchen.m_diet_template_diet_preference_mapper (diet_template_id, diet_preference_id)
SELECT dt.diet_template_id, mp.meal_preference_id
FROM kitchen.m_diet_template dt, adt.m_meal_preference_master mp
WHERE dt.organization_id = 1 AND dt.unit_id = 1 AND mp.organization_id = 1 AND mp.unit_id = 1
  AND mp.meal_preference_code = 'GEN';

-- 5. kitchen.m_dish_master / kitchen.dish_master_diet_pref_mapper were empty (BR-079 flagged
--    actual dish content as a dietitian decision, deliberately not seeded). Two genuinely
--    generic, structurally-necessary food items -- no dish_value/nutrition numbers invented,
--    same discipline as BR-079's own seeding (names only, no reference values).
INSERT INTO kitchen.m_dish_master
  (dish_code, dish_description, uom_id, diet_preference_id, diet_type_id, organization_id, unit_id, status, created_by)
SELECT 'RICE', 'Rice', 1, mp.meal_preference_id, dtm.diet_type_id, 1, 1, 'A', 1
FROM adt.m_meal_preference_master mp, kitchen.m_diet_type_master dtm
WHERE mp.meal_preference_code = 'GEN' AND mp.organization_id = 1 AND mp.unit_id = 1
  AND dtm.diet_type_code = 'VEG' AND dtm.organization_id = 1 AND dtm.unit_id = 1
RETURNING dish_id;

INSERT INTO kitchen.m_dish_master
  (dish_code, dish_description, uom_id, diet_preference_id, diet_type_id, organization_id, unit_id, status, created_by)
SELECT 'DAL', 'Dal', 1, mp.meal_preference_id, dtm.diet_type_id, 1, 1, 'A', 1
FROM adt.m_meal_preference_master mp, kitchen.m_diet_type_master dtm
WHERE mp.meal_preference_code = 'GEN' AND mp.organization_id = 1 AND mp.unit_id = 1
  AND dtm.diet_type_code = 'VEG' AND dtm.organization_id = 1 AND dtm.unit_id = 1
RETURNING dish_id;

-- 6. Map both dishes to all 3 seeded meals (Breakfast/Lunch/Dinner) under the new diet
--    preference + Veg diet type -- this is what DishMasterDaoImpl.getDishListByDietPrefId
--    (GET_DISH_LIST_BY_DIET_PREF_ID) actually filters on to populate the dish-selection dropdown.
INSERT INTO kitchen.dish_master_diet_pref_mapper (dish_id, diet_preference_id, meal_type_id, diet_type_id)
SELECT dm.dish_id, mp.meal_preference_id, mm.id, dm.diet_type_id
FROM kitchen.m_dish_master dm
CROSS JOIN kitchen.m_meal_master mm
JOIN adt.m_meal_preference_master mp ON mp.meal_preference_code = 'GEN' AND mp.organization_id = 1 AND mp.unit_id = 1
WHERE dm.dish_code IN ('RICE', 'DAL') AND dm.organization_id = 1 AND dm.unit_id = 1
  AND mm.organization_id = 1 AND mm.unit_id = 1;

COMMIT;
