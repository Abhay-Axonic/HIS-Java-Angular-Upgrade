# Batch 34 — Step 6 Consolidated Test Checklist

**Screens (5):** `InsuranceTPAMapping`, `operationTable`, `bloodBankTypeComponent`, `pharmacyStrengthUnitMaster`, `tubeFeedingProducts`
**Branch:** `rebuild/phase4-batch34`
**Status:** Steps 0-5 complete, live-verified by the orchestrating session -- including real interaction-level browser testing (Playwright) against the actual Angular dev server, proxy-configured to the live backend, with a genuine authenticated login. Every create round-trip below was independently confirmed via `psql`, then cleaned up. Step 6 (this checklist) is your own manual click-through -- mandatory, blocking, cannot be marked complete by any agent or tool.

**Batch size:** 5 of the original 6 candidates. `mpesaApplicationConfiguration` is excluded -- its controller script is never referenced by `index.html` and so never loads in the browser at all (confirmed via real golden-trace capture, not static analysis), logged separately in `docs/ANGULAR_PENDING_FINDINGS.md` as `PENDING-mpesaapplicationconfiguration-missing-script-include`. Not fixed tonight per your instruction.

**Access:** `http://localhost:4200/<route>` for each screen below. Log in via the real legacy app first (`http://localhost:8080`, same host, shared `cookieObject` cookie -- cookies are host-scoped, not port-scoped, so the session carries over automatically). If any screen redirects to Access Denied, your session's `localStorage.permissions` on the `4200` origin is likely missing that screen's moduleId -- this dev environment's `permissions` blob on `:4200` is separate from `:8080`'s (localStorage IS port-scoped, unlike cookies), same documented quirk as an earlier batch's testing. The 4 module ids this batch needs: 573 (InsuranceTPAMapping), 522 (operationTable), 739 (bloodBankTypeComponent), 468 (pharmacyStrengthUnitMaster). `tubeFeedingProducts` has no module gate (legacy's own state has no resolve block).

**Environment note, found and fixed along the way:** the local `proxy.conf.json` (gitignored, machine-local) was missing a `/bloodBank/**` proxy rule -- without it, `bloodBankTypeComponent`'s API calls hit `ng serve`'s own fallback instead of the real backend, producing confusing `HttpErrorResponse` console errors despite the underlying request actually being fine. The tracked `proxy.conf.example.json` already had this entry from a prior session; only the local file needed to catch up. If you see similar errors on a fresh machine, check `proxy.conf.json` against `proxy.conf.example.json`.

**Seed data note:** this dev DB (`his_local`) has real pre-existing rows for each screen's dropdowns as of this session (e.g. 3 operation table types, 2 blood bag types, some existing operation tables). All test rows created during this session's own verification (test tube-feeding products, strength units, operation tables, a blood-bank component mapping, an insurance-TPA mapping, plus their supporting seed rows) were deleted afterward -- confirmed via `psql` row counts before finishing.

---

## InsuranceTPAMapping -- one disclosed, deliberate gap, verify it specifically

**Edit/Update is intentionally disabled, not a missing feature.** Confirmed at three independent levels that legacy's Edit/Update was already completely broken: the view's Update button calls a function that doesn't exist in the controller at all (only dead leftover code from an unrelated screen); the Edit/pencil icon's handler calls a completely unrelated endpoint (`/adt/getUnitSpecialityById`, belonging to Unit Speciality Master, not this screen); and the backend controller has no update endpoint for this feature at all. Rather than silently porting a dead button or inventing a new capability the backend has never had, the Edit icon here is visibly disabled with an honest tooltip.

- [ ] Select a TPA from the dropdown. Confirm the "Insurance Companies Not Mapped" list populates.
- [ ] Check one or more insurance companies, confirm they move into the "Selected" list, then click Save. Confirm a success message appears and the new mapping appears in the table below.
- [ ] Hover/click the Edit icon on any row. Confirm it's visibly greyed out and shows the tooltip "Editing an existing TPA mapping is not currently supported" -- confirm clicking it does nothing destructive (no crash, no navigation, no silent wrong-data fetch).
- [ ] Toggle a row's status. Confirm it flips and a success message appears.
- [ ] Try clicking Save with no TPA selected. Confirm a "Please Select TPA" warning appears, nothing is submitted.
- [ ] Try clicking Save with a TPA selected but no insurance companies picked. Confirm a "Please Add Mapped Insurance" warning appears.

## operationTable

- [ ] Confirm the Table Type and Theatre dropdowns populate with real options on load.
- [ ] Fill in Table Code, Table Name, Table Description, pick a Table Type and Theatre, click Save, confirm the modal, confirm the new row appears in the list.
- [ ] Try Save with an empty Table Type -- confirm "Please Select Table Type" appears before anything else. Try again with a Theatre missing instead -- confirm "Please Select Theatre".
- [ ] Click the Edit icon on a row, confirm the form populates with that row's data, change the description, click Update (no confirm modal expected here), confirm the row updates in place.
- [ ] Toggle a row's status, confirm it flips with a success message and the list is not reloaded (this matches legacy's own behavior -- the checkbox's own state is the only visual feedback).

## bloodBankTypeComponent

- [ ] Confirm the Blood Bag Type dropdown and the "Available Components" checklist both populate with real options.
- [ ] Select a bag type, check one or more components, click Save, confirm the modal, confirm the new row appears in the list (labeled by bag type description).
- [ ] Click the view-details icon on a row, confirm a modal lists the components actually mapped to that bag type, close it.
- [ ] Click the Edit icon, confirm the bag type and its already-mapped components pre-populate correctly (both the picker checkboxes and the "Selected" list), change the selection, click Update.
- [ ] Try Save with no bag type selected -- confirm "Please select Blood Bag". Try again with a bag type but no components picked -- confirm "Please Select Component Type".
- [ ] Toggle a row's status, confirm it flips with a success message.

## pharmacyStrengthUnitMaster

- [ ] Confirm the list loads with any existing units, pagination controls work correctly (this screen paginates entirely client-side over one full fetched list, unlike the other 4 screens in this batch -- worth specifically confirming Show-N and Next/Previous/First/Last all behave sensibly).
- [ ] Click Save with the form completely empty. **Confirm the confirm modal still opens** (this is a faithful reproduction of a real legacy quirk -- the Save button's disabled look is CSS-only, it doesn't actually block the modal). Click "Ok" inside the modal, confirm nothing is submitted and inline "required" errors appear on the form instead.
- [ ] Fill in a real Code and Description, Save, confirm through the modal, confirm the new row appears.
- [ ] Click the Edit icon on an ACTIVE row, confirm the form populates, change the description, Save again (this screen branches Create vs. Update on whether an id is present, confirmed via the same Save button, not a separate Update button).
- [ ] Confirm the Edit icon looks visually disabled on an INACTIVE row (a cosmetic-only restriction, faithfully matching legacy -- clicking it isn't blocked at the code level either way).
- [ ] Toggle a row's status. Confirm the switch flips and a success message appears.
- [ ] Click a sortable column header (Code / Description) twice, confirm the sort direction visibly reverses.
- [ ] Type into Search, confirm the list filters; click Clear, confirm both the form and the search filter reset together (a real legacy quirk -- Clear does both at once).

## tubeFeedingProducts

- [ ] Fill in Product Code and Product Description, click Save, confirm through the modal, confirm the new row appears.
- [ ] Click Edit on a row, change the description, click Update, confirm the row updates in the list.
- [ ] Toggle a row's status, confirm it flips with a success message and the list DOES reload afterward (unlike some of this batch's other screens -- confirmed as this screen's own real legacy behavior).
- [ ] Confirm this screen is reachable without any specific module grant (no access-control gate exists for it in legacy either).

## Sign-off

Once you've clicked through everything above and are satisfied, let me know and I'll record your sign-off as a permanent artifact in `docs/ANGULAR_PENDING_FINDINGS.md`, same as every prior batch. This branch stays held, unmerged, until you separately approve the merge.
