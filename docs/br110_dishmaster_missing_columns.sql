-- BR-110: kitchen.m_dish_master is missing 4 columns already mapped by
-- legacy/DietAndKitchen/src/main/java/com/param/global/master/model/DishMaster.java.
-- Already known, already scripted in docs/br076_missing_columns.sql (lines 126-129);
-- confirmed known_local_dev_drift=True in docs/schema-drift-sweep/missing_column_mismatches.tsv.
-- Purely additive, no existing data touched.
ALTER TABLE kitchen."m_dish_master" ADD COLUMN IF NOT EXISTS "diet_type_description" varchar(255);
ALTER TABLE kitchen."m_dish_master" ADD COLUMN IF NOT EXISTS "diet_type_id" int4;
ALTER TABLE kitchen."m_dish_master" ADD COLUMN IF NOT EXISTS "meal_preference" varchar(255);
ALTER TABLE kitchen."m_dish_master" ADD COLUMN IF NOT EXISTS "meal_type_id" int4;
-- Same save transaction also touches kitchen.dish_master_diet_pref_mapper, missing 2 more
-- already-known-drift columns (br076_missing_columns.sql lines 124-125).
ALTER TABLE kitchen."dish_master_diet_pref_mapper" ADD COLUMN IF NOT EXISTS "meal_type_id" int4;
ALTER TABLE kitchen."dish_master_diet_pref_mapper" ADD COLUMN IF NOT EXISTS "diet_type_id" int4;
