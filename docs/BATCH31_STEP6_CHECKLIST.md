# Batch 31 — Step 6 Consolidated Test Checklist

**Screen:** `subDepartmentMasterUnit` (rebuilt as the `unit-sub-speciality-mapping` feature — see naming note below)
**Branch:** `rebuild/phase4-batch31-subdepartmentmasterunit`
**Status:** Steps 0-5 complete, live-verified by the orchestrating session, including a real bug found and fixed during Step 5 testing (not just Step 0/1 audit findings). Step 6 (this checklist) is the owner's own manual click-through — mandatory, blocking, cannot be marked complete by any agent or tool.

**Batch size:** 1 screen. All 4 other original batch 31 candidates plus 3 replacement candidates were excluded or rejected during Step 0/1 audits this session (6b clinical-safety gates or live-confirmed security/functional defects) — see `docs/ANGULAR_PENDING_FINDINGS.md` for the full trail. This is the only screen from the whole round that survived full verification.

**Naming disclosure — read this first.** Despite the legacy screen and its own UI being titled "Sub Department Master," it does not manage a department/sub-department hierarchy at all — it manages **Speciality → Sub-Speciality mapping** at the unit level (confirmed via its real backend, `UnitSubSpecialityController`/`UnitSubSpecialityMapper`/`t_unit_sub_speciality_mapper`). The rebuild is intentionally renamed throughout (nav label "Unit Sub-Speciality Mapping", field label "Speciality" not "Department", table headers "Speciality/Sub-Speciality Code/Description") rather than perpetuating the legacy naming confusion. This was a deliberate choice, partly forced by a real naming collision: legacy's *actual* Speciality/Sub-Speciality global master (a different screen entirely) is already rebuilt at `/subSpecialityMaster` and already owns the "Sub Department Master" nav label — reusing it here would have created a duplicate label in the same sidebar. If you feel the renaming is wrong or confusing, flag it — it's a judgment call, not a settled decision.

**Access:** `http://localhost:4200/subDepartmentMasterUnit` (route path unchanged for continuity, only the on-screen labels changed). Log in via the real legacy app first (same host, shared `cookieObject` cookie). If the screen redirects to Access Denied, your session's `localStorage.permissions` may be missing moduleId 534 — ask for the shim used this session if needed.

**Seed data note:** this dev DB (`his_local`) has **zero rows** in `doctor.m_sub_speciality_master` (no sub-specialities defined at all) as of this session. You will need at least one sub-speciality seeded under a real speciality to exercise the picker/save/toggle flow — ask for two or three to be added if none exist when you start (the orchestrating session seeded and cleaned up test rows itself for verification; nothing persists from that testing).

---

## Real bug found and fixed during Step 5 (not just disclosed from Step 0/1) — verify this specifically

**The duplicate-mapping prevention did not actually work as built**, despite being one of the disclosed Step 3 fixes. Root cause: the Speciality `<select>` used `[value]` binding instead of `[ngValue]`, so the browser's native select semantics silently coerced the selected speciality id to a string on every change, breaking a strict-equality comparison deeper in the duplicate-check logic — the dedup guard silently matched nothing, regardless of real data. Found by live-reproducing the exact scenario: map a sub-speciality, deactivate it, then re-select the same speciality — the deactivated sub-speciality reappeared as pickable, and re-adding it created a genuine second database row for the same speciality/sub-speciality pair. Fixed by correcting the binding; re-verified live via direct inspection of the running component's internal state, confirming the fix. Full detail in the branch's commit history and `docs/BUSINESS_RULE_MANIFEST.md`.

- [ ] Map a sub-speciality to a speciality (see steps below). Deactivate it via the status toggle. Re-select the same speciality in the dropdown. **Confirm the deactivated sub-speciality does NOT reappear in the Global Master (available) list.**
- [ ] Confirm only ONE row exists for that speciality/sub-speciality pair in the table throughout (no duplicate rows).

## Standard flow

- [ ] Load the screen. Confirm the Speciality dropdown populates with real data.
- [ ] Select a speciality with at least one available (unmapped) sub-speciality. Confirm the "Global Master" (left) picker populates.
- [ ] Check one or more sub-specialities in Global Master, click the `<<>>` move button, confirm they move to "Clinical Unit Master" (right).
- [ ] Click Save. Confirm a success message appears AND the table below refreshes automatically to show the new mapping(s) — no manual page reload needed.
- [ ] Confirm the Global Master picker also refreshes after save, correctly excluding the sub-speciality/ies you just mapped.
- [ ] Try Save with no speciality selected — confirm a visible warning ("Please select speciality...") and Save is blocked.
- [ ] Try Save with a speciality selected but nothing moved to Clinical Unit Master — confirm a visible warning ("Please select at least one sub-speciality...") and Save is blocked.
- [ ] Toggle a mapping's status switch (active → inactive). Confirm the toggle visually updates and the change persists after a page refresh. Toggle it back (inactive → active), confirm the same.
- [ ] Use the Search box to filter the table by a real value (e.g. part of a speciality or sub-speciality code/name). Confirm matching rows show. Search for a value that matches nothing, confirm "No record found..." displays.
- [ ] Change the "Show N entries" page-size dropdown. Confirm the table re-renders with the new page size (if you have enough rows to see a difference — may need several seeded mappings to test meaningfully).
- [ ] **Error-disclosure check:** if you can trigger a real backend failure (e.g., ask for a temporary invalid FK or similar), confirm the screen shows a clear, generic "Something went wrong, please try again" message rather than doing nothing or showing a blank/confusing state. Legacy silently did nothing on many failure paths; the rebuild should always show *something* to the user.

## Sign-off

Once you've clicked through everything above and are satisfied, let me know and I'll record your sign-off as a permanent artifact in `docs/ANGULAR_PENDING_FINDINGS.md`, same as every prior batch. This branch stays held, unmerged, until you separately approve the merge.
