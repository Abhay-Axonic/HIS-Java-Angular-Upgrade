-- BR-081: master-data gap closure for the Assets Indent E2E write-operation test.
-- Same discipline as BR-079/BR-080: real, structural, standard-shape values only.
-- Genuine pre-existing gap: Assets has no dedicated store (public.m_store had only
-- Pharmacy Main Store/Linen Store/CSSD Store -- same "no dedicated store" gap BR-079
-- already found and fixed for Linen & Laundry and CSSD), and public.m_item had zero
-- rows tagged as Fixed/Equipment (asset_category_id=3 -> asset_type_id=2), so the
-- Asset Indent item search (ItemsDaoImpl.itemsListForStoreIndent) returned zero
-- results no matter what store was picked -- the flow was completely untestable.
-- Run as a single transaction, all-or-nothing.

BEGIN;

-- 1. New store to receive/issue fixed assets. No dedicated "Assets" store_type exists
--    (public.m_store_type has only Main Store/Sub Store/Consignment Store/CSSD
--    Store/Linen Store/Narcotics Store) -- reusing the generic "Sub Store" (id=2)
--    type rather than inventing a new type, matching this session's own discipline
--    of not fabricating new master rows beyond what's structurally necessary.
INSERT INTO public.m_store (
    unit_id, organization_id, store_type_id, store_name, store_code,
    is_active, updated_by, added_by, is_deleted,
    is_cssd, is_linen, is_pharmacy, is_narcotic, is_laundry, is_maintenance,
    plant_code, gst_no, general_ledger_id_stock, general_ledger_id_cogs, is_tax_appllicable
) VALUES (
    1, 1, 2, 'Assets Store', 'AST01',
    true, 1, 1, false,
    false, false, false, false, false, false,
    '0', '0', 0, 0, 'Y'
);

-- 2. One Equipment item, flagged as a placeholder needing owner/procurement review
--    (same convention as BR-079's m_fixed_asset_master placeholder row and BR-080's
--    reused-real-value discipline). Uses product_category_id directly (the
--    ItemsServiceImpl.itemsDetails() "Fixed Asset" branch, productCategoryCount() > 0),
--    not the generic_id path -- this is the code path the app actually takes for
--    Equipment-category items (see ItemsDaoImpl.itemsDetailsFixedAsset).
INSERT INTO public.m_item (
    organization_id, unit_id, manufacturer_id, code, name,
    product_category_id, p_tax_id,
    is_reusable, is_vaccin, is_otc, is_combination, is_batch_required,
    is_high_risk_product, is_psychotropic, is_kit, is_infusion, is_narcotic,
    is_active, updated_by, added_by, is_deleted, is_consignment, is_consumable,
    is_injection, is_dose_calculation_applicable, is_three_uom_applicable
) VALUES (
    1, 1, 1, 'AST0001', 'Sample Medical Equipment Item (placeholder -- needs owner/procurement review)',
    3, 1,
    false, false, false, false, false,
    false, false, false, false, false,
    true, 1, 1, false, false, false,
    false, false, false
);

-- 3. Map the new item into both the requesting store (Pharmacy Main Store, id=2 --
--    the only store admin/user_id=1 is mapped to via m_user_store_mapper) and the
--    new issuing store (Assets Store, id=4 -- next store_id after CSSD Store).
--    max_qty/min_qty are generic inventory reorder-level placeholders (not a
--    clinical or financial judgment call like BR-079's deliberately-skipped
--    m_store_item_mapper reasoning), flagged here for inventory-manager review
--    before any real (non-test) indent is raised against this item.
INSERT INTO public.m_store_item_mapper (
    organization_id, unit_id, store_id, item_id, max_qty, min_qty,
    is_active, updated_by, added_by, is_deleted, is_sellable
)
SELECT 1, 1, s.store_id, i.id, 100, 1, true, 1, 1, false, false
FROM public.m_store s, public.m_item i
WHERE s.store_code IN ('PHM-MS', 'AST01') AND i.code = 'AST0001';

-- 4. The Fixed-Asset item search path (ItemsServiceImpl.getItemsForStoreIndentList,
--    assetTypeId=2 branch -> ItemsDaoImpl.assetItemsListForAssetIndent) additionally
--    requires the item's product category to be mapped to BOTH the from- and to-store
--    via public.m_store_pc_mapper -- a separate table from m_store_item_mapper, and one
--    that was completely empty (0 rows) across the whole DB before this fix, for every
--    module, not just Assets. Without it the item search returns zero rows no matter
--    what item/store data exists, so this table was a genuine, previously-undiscovered
--    structural gap, not an Assets-specific one.
INSERT INTO public.m_store_pc_mapper (
    organization_id, unit_id, store_id, product_category_id,
    is_active, updated_by, added_by, is_deleted
)
SELECT 1, 1, s.store_id, 3, true, 1, 1, false
FROM public.m_store s
WHERE s.store_code IN ('PHM-MS', 'AST01');

COMMIT;
