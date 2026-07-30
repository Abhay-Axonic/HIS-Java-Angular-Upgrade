# Batch 32 — Step 6 Consolidated Test Checklist

**Screen:** `productRestriction.productCategoryMapping` (rebuilt as the `product-category-mapping` feature)
**Branch:** `rebuild/phase4-batch32-productcategorymapping`
**Status:** Steps 0-5 complete, live-verified by the orchestrating session. Step 6 (this checklist) is your own manual click-through — mandatory, blocking, cannot be marked complete by any agent or tool.

**Batch size:** 1 screen. `taxComponentMaster` and `mpesaApplicationConfiguration` (the other 2 original batch 32 candidates) were excluded pending 4 legacy backend fixes — see BR-117 through BR-120, all merged into `main` already, separately from this rebuild. This is the sole batch 32 rebuild.

**Access:** `http://localhost:4200/productRestriction/productCategoryMapping`. Log in via the real legacy app first (same host, shared `cookieObject` cookie), then also log into the new app itself at `http://localhost:4200/login`. If the screen redirects to Access Denied, your session's `localStorage.permissions` on the `4200` origin is likely missing moduleId 633 — this dev environment's `permissions` blob is a small, manually-maintained set (only 3-4 modules), not derived from the real DB privilege table, so this is expected on a fresh login and not itself a bug. Ask for the same shim used this session if needed.

**Seed data note:** this dev DB (`his_local`) currently has exactly 1 product category (`Test PMS Product Category`) and 2 stores (`Test Main Store`, `Linen Store`). `m_store_pc_mapper` (the mapping table itself) is empty as of this session — the orchestrating session seeded and cleaned up its own test rows during verification; nothing persists from that testing.

---

## Two disclosed defects, both owner-reviewed — verify these specifically

**Defect A (NOT fixed, faithfully reproduced, mandatory in-UI disclosure).** Deactivating a mapping here only flips a flag in the backend — it does not actually exclude that product category from 5 real downstream consumers elsewhere in the system (barcode printing, non-billable consumption, shelf allocation, item transfers, stop-transaction item search). This is a known, deliberately-not-fixed backend gap (queued as its own future initiative, out of scope for this screen) — see the standalone cross-cutting finding in `docs/ANGULAR_PENDING_FINDINGS.md` for full detail.
- [ ] Confirm a visible warning banner is shown directly above the table at all times (not hover-only, not collapsed), stating that deactivating a mapping here does not exclude the category from store consumption/barcode/billing processes elsewhere.

**Defect B (fixed with disclosure) — this needed a real correction mid-session, verify it carefully.** Legacy always creates a brand-new row on toggle ON regardless of whether a mapping already exists (just inactive) for that store+category — live-confirmed to produce genuine duplicate rows (the same category rendering twice with conflicting statuses) and, separately, an HTTP 405 on toggle-OFF immediately after toggle-ON. My first attempt at fixing this in the rebuild only fixed the second symptom (the 405) — it still produced a real duplicate row on a toggle-on/off/on-again sequence, because my own instructions to the build agent incorrectly ruled out reusing an existing row's id. I caught this myself via a live, DB-cross-checked test before reporting it as done, and corrected it: the rebuild now reactivates an existing row via the backend's own generic status-update endpoint when the row's id is already known, and only creates a new row when no mapping has ever existed.
- [ ] Toggle a mapping ON. Confirm exactly one row exists for that category (check via the table, not just the toggle's visual state).
- [ ] Toggle it OFF immediately (no page refresh in between). Confirm no error appears and the toggle reflects OFF correctly.
- [ ] Toggle it back ON again (still no page refresh). **Confirm the category still appears exactly ONCE in the table, not twice**, and the toggle shows ON.
- [ ] Repeat the on/off/on sequence a second time to be sure. Confirm the row count never grows beyond 1 for this category.

## Standard flow

- [ ] Load the screen with no store selected. Confirm the table is empty/prompts for a store (no error).
- [ ] Select "Test Main Store". Confirm the table populates with the real product category and its current status.
- [ ] Use the Search Category box to filter by a value that matches nothing. Confirm "No Record Found" displays. Clear the search, confirm the row reappears.
- [ ] Change the "Show N records" page-size dropdown. Confirm no error (only 1 real row exists today, so a visible layout change may not be observable — this is a sanity check, not a meaningful pagination test given current seed data).
- [ ] Click a sortable column header (Asset Type / Asset Category / Product Category). Confirm no error (again, limited seed data means visible reordering won't be obvious with only 1 row).
- [ ] With at least one mapping loaded, switch the Store dropdown to a different store. Confirm a "Are you sure you want to change store?" confirmation modal appears, the dropdown visually stays on the original store until you respond. Click Cancel — confirm the store selection and table are unchanged. Repeat and click Ok — confirm the store actually switches and the table reloads for the new store.
- [ ] **Error-disclosure check:** if you can trigger a real backend failure (e.g., ask for a temporary bad request), confirm the screen shows a clear "Something went wrong" message and the toggle visually reverts, rather than silently doing nothing.

## Sign-off

Once you've clicked through everything above and are satisfied, let me know and I'll record your sign-off as a permanent artifact in `docs/ANGULAR_PENDING_FINDINGS.md`, same as every prior batch. This branch stays held, unmerged, until you separately approve the merge.
