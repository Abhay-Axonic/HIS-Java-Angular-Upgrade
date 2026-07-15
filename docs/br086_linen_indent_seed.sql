-- BR-085: Linen & Laundry Linen Indent E2E test -- master-data gap closure
-- Local dev DB only (his-local-postgres / "HIS-SME"). Expand-only, additive, nothing dropped or altered.
-- Flow: addNewLinenController.js (state addNewLinenIndent) -- Linen Indent, screen_id=95 (m_screen.code='LI'),
-- indents item_id=3 ("Sample Medical Equipment Item", assetTypeId=2/Fixed -- the only item reachable via
-- this controller's hardcoded assetTypeId=2 search, see BUSINESS_RULE_MANIFEST.md BR-085 finding) from
-- "Linen Store" (store_id=3, seeded by BR-079) to a second Linen-type store.

BEGIN;

-- Gap 1: public.m_store had exactly ONE row of store_type_id=5 ("Linen Store" type), from BR-079's
-- structural-only seed. LinenIndentService.getDropdowns/GlobalDao.getLinenUserStoreList (fromStore) and
-- GlobalDao.getLinenStoreList (toStore) BOTH filter store.storeType.id = 5 -- i.e. both the requesting and
-- issuing store must be of Linen-Store type. With only one such store, fromStore and toStore would always
-- resolve to the same store, which addNewLinenController.js's validateSave() explicitly blocks ("Please
-- Select Different Stores"). Seeded a second Linen-type store to act as the issuing/laundry-facing side,
-- same class of gap as BR-082's "zero Assets stores" finding.
INSERT INTO public.m_store (unit_id, organization_id, store_type_id, store_name, store_code, is_active, is_deleted, is_linen, is_laundry, updated_by, added_by)
VALUES (1, 1, 5, 'Central Laundry Store', 'CLS', true, false, true, true, 1, 1);

-- Gap 2: public.m_user_store_mapper had zero rows mapping admin (user_id=1) to any store_type_id=5 store.
-- GlobalDao.getLinenUserStoreList inner-joins m_user_store_mapper for the logged-in user, so without this
-- row admin's "Requesting Store" (fromStore) dropdown would be empty -- same class of gap as BR-053's
-- Pharmacy finding and BR-081/082's per-module store-mapper gaps.
INSERT INTO public.m_user_store_mapper (organization_id, unit_id, user_id, store_id, is_active, is_deleted, updated_by, added_by)
VALUES (1, 1, 1, 3, true, false, 1, 1);

-- Note: item_id=3 already has public.m_store_item_mapper rows for store_id 2 and 5 (from BR-082), which is
-- sufficient for addNewLinenController.js's searchNewItem() (ItemsDaoImpl.itemsList, assetTypeId=2 branch)
-- since that query's store_item_mapper join is filtered by unit_id only -- storeId is never sent in the
-- Linen Indent search request (commented out in the client code), so no additional store_item_mapper rows
-- are needed here. Similarly item_id=3's itemsDetails() (AddItems POST) has no store_item_mapper
-- dependency at all (LEFT JOIN stock lookup only) -- no further seeding needed for the item side.

COMMIT;
