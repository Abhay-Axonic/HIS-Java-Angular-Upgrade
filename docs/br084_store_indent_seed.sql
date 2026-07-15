-- BR-084: Inventory Store Indent E2E test -- master-data gap closure
-- Local dev DB only (his-local-postgres / "HIS-SME"). Expand-only, additive, nothing dropped or altered.
-- Flow: addNewStoreIndenting.js (state addNewStoreIndenting) -- Store Indent, screen_id=2 (m_screen.code='SI'),
-- indents item_id=1 ("E2E Test Paracetamol 500mg", assetTypeId=1/Consumable) from Pharmacy Main Store (store_id=2)
-- to CSSD Store (store_id=4) -- the dev DB has no second Pharmacy/Sub Store, so CSSD Store was reused as the
-- issuing store purely for structural test-data purposes, same precedent as BR-082's Pharmacy Main -> Assets Store
-- pairing.

BEGIN;

-- Gap 1: m_store_item_mapper had zero rows for item_id=1 in any store. The item-search query used by
-- ItemsDaoImpl.itemsListForStoreIndent (assetTypeId=1 branch) inner-joins m_store_item_mapper for BOTH
-- the fromStore and toStore -- with no rows the item never appears in the "Add Item" search results
-- regardless of which store pair is picked, the same class of gap BR-082 found for Assets.
INSERT INTO public.m_store_item_mapper (organization_id, unit_id, store_id, item_id, max_qty, min_qty, is_active, updated_by, added_by, is_sellable)
VALUES
  (1, 1, 2, 1, 500, 10, true, 1, 1, false),
  (1, 1, 4, 1, 500, 10, true, 1, 1, false);

-- Gap 2: m_unit_of_measurement had zero rows for item_id=1. addNewStoreIndenting.js's AddItems() client
-- code branches on item.uom.length == 3 (Purchase/Stock/Dispense) before it will populate item.uomListdata;
-- with zero UOM rows the item.uom array comes back empty and a later, unguarded
-- `item.uomListdata[2].uomUnit` read throws a client-side TypeError instead of silently degrading -- see
-- BUSINESS_RULE_MANIFEST.md BR-084 for the full finding, reported but not patched (no .js file touched).
-- Seeded the minimal structurally-correct 3-row set: all three UOM types (Purchase/Stock/Dispense) mapped
-- to the only UOM unit that exists in this dev DB ("Tablet", id=1), 1:1 conversions (no fabricated
-- multi-tier packaging ratio -- that would be a business/procurement judgment call, out of scope here,
-- same caution as BR-079/BR-082's placeholder conventions). store_uom=true on the Stock-type row only,
-- matching the single-flag convention the client's `sUom.storeUom == true` lookup expects.
INSERT INTO public.m_unit_of_measurement (organization_id, unit_id, item_id, uom_type_id, uom_unit_id, conversions, ip_uom, op_uom, store_uom, is_active, updated_by, added_by)
VALUES
  (1, 1, 1, 1, 1, 1, false, false, false, true, 1, 1),
  (1, 1, 1, 2, 1, 1, false, false, true,  true, 1, 1),
  (1, 1, 1, 3, 1, 1, false, false, false, true, 1, 1);

-- Gap 3: m_prefix_configuration had zero rows for screen_id=2 (m_screen.code='SI', "Store Indent"). Save
-- calls GlobalService.getBillNoByGeneratedCode -> IGlobalUnitMasterDao.getGeneratedNo, which returns NULL
-- when no active, non-deleted prefix_configuration row exists for the (screen_id, unit_id) pair; the NULL
-- indentNo then hits StoreIndent's not-null column and Hibernate throws PropertyValueException at save
-- time -- same discovery class as BR-081's donor_number gap (m_prefix_configuration empty except for
-- whichever screens a prior session had already seeded).
INSERT INTO public.m_prefix_configuration (organization_id, unit_id, screen_id, running_number, reset_id, length, format_id, code, is_active, updated_by, added_by, generated_code)
VALUES (1, 1, 2, 1, 1, 6, 1, 'SI', true, 1, 1, 'SI-[SF]');

COMMIT;
