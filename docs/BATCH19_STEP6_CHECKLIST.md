# Batch 19 — Step 6 Domain-Expert Review Checklist

*Clinical/billing-tier batch. All 5 screens are through Steps 0-5 on `rebuild/phase4-batch19` (held, not merged, not pushed). This doc consolidates each screen's own Step 6 material into one place for a methodical walkthrough — see `docs/ANGULAR_PENDING_FINDINGS.md` for the full audit writeups behind each item below, and the two `DECISION_NEEDED_*.md` docs for the two escalated severity-1 findings.*

**Setup, once, before starting any of the 5:**
- `git checkout rebuild/phase4-batch19`, confirm `ng serve` (port 4200, `proxy.conf.json` pointed at the real backend) and the Java backend (port 8080) are both running.
- Legacy screens are reached at `http://localhost:8080/#/<state>`; rebuilt screens at `http://localhost:4200/<state>`.
- otherCharges was swapped out of this batch (its frontend route is disabled and its own wiring is broken — logged separately as its own future task, not part of this batch). `bloodBagCapacity` replaces it.

---

## 1. `bloodBagCapacity` — recommended first, given severity

**Paths:** `bloodBagCapacity` (legacy / rebuild)

**Test data already in the dev DB** (disclosed, not cleaned up): `BBC1`/"Bag Capacity One CONFIRMED" (active), `BBC2`/"Bag Capacity Two" (inactive).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | List shows the 2 rows above; Show/Search/pagination all present |
| 2 | Create `BBC3`/"Bag Capacity Three" | Confirm modal appears → Ok → "Blood Bag Capacity Added Successfully...!!" → row appears |
| 3 | Create a second new row immediately after | **No confirm modal this time** — saves directly. This is a faithful reproduction of a real legacy quirk (see below), not a bug |
| 4 | Leave both fields blank, click Save | Toast "Please Enter Blood Bag Capacity Code" (not an inline message) — matches legacy's own validation style |
| 5 | **The critical test:** click Edit on one row (form populates), then — *without saving* — click the status toggle on a *different* row | The edited row's form fields must stay exactly as they were; only the toggled row's status should change |
| 6 | Now click Update on the still-open edit form from step 5 | Must update the *originally edited* row, not the one you toggled in step 5 |
| 7 | Toggle any row's status | Switch flips, "...Status Updated Successfully..." toast |

**Needs your explicit sign-off on:**
- **Severity-1, rebuild-only fix, confirmed working live** (steps 5-6): legacy reuses one shared form object for both the Edit form and the status-toggle action — editing Row A, then toggling Row B before saving, corrupts the pending save into writing Row B's ID with Row A's data. This was empirically reproduced and confirmed fixed in the rebuild during Step 5 testing (edit-form state is now structurally isolated from toggle actions). **This is a disclosed, structural improvement, not faithful reproduction** — flagging per the identificationMaster/currencyMaster precedent for confirmed wrong-record-risk fixes.
- **Legacy bug, NOT fixed, backend-only**: Update's duplicate-check query has a field mix-up (the "code already exists" check actually compares description text against the code column), and Create's duplicate-check has no organization/unit scope. Both are server-side; a separate DECISION_NEEDED proposal exists for the org/unit-scope class of bug (shared with `bloodDonortype`, item 5 below) but the field-mix-up bug is newly logged, not yet proposed as its own fix.
- Confirm-modal fires only once per page session (step 3) — faithful to legacy's own `popUpFlag` behavior, confirmed by source and live test, not a bug.

---

## 2. `ARmaster`

**Paths:** `ARmaster` (legacy / rebuild)

**Test data already in the dev DB:** `TESTAR2`/"Test AR Description Two" (active), `TESTAR1`/"Updated AR Description One" (inactive).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | 2 rows show; note both AR Code and AR Description have a red `*` but neither is actually enforced (see below) |
| 2 | Leave both fields blank, click Save | **Saves successfully with blank values** — faithful to a real, confirmed legacy gap (no validation exists despite the `*`), not a rebuild defect. (Feel free to clean up any blank rows created during this check.) |
| 3 | Create `TESTAR3`/"Test Three" | Confirm modal → Ok → success toast → row appears |
| 4 | Create a second new row | No confirm modal this time — same once-per-session quirk as `bloodBagCapacity` |
| 5 | Edit a row, change the description, click Update | No confirm modal (matches legacy), success toast, row updates |
| 6 | With more than 10 rows total (create a few more if needed), use Next/Previous/page numbers | **Must genuinely show different rows per page** |
| 7 | Toggle a row's status | Flips, success toast |

**Needs your explicit sign-off on:**
- **Rebuild-only fix, owner-approved**: legacy's pagination is completely non-functional — a variable that should track the current page's offset is initialized once and never reassigned, so clicking Next/Previous always re-fetches page 1 regardless of what the pager UI shows. Step 6 above exercises the corrected, working version. This is a disclosed improvement (approved 2026-07-25), not silent parity.
- **Legacy bug, NOT fixed, faithful reproduction** (steps 1-2): zero validation on either field despite both being marked required in the UI. The only real gate is the server-side duplicate-description check.
- Confirm-modal fires only once per page session (step 4) — same faithful-reproduction quirk as `bloodBagCapacity`.

---

## 3. `anaesthesiaType`

**Paths:** `anaesthesiaType` (legacy / rebuild)

**Test data already in the dev DB:** `AT1`/"General"/"General Anaesthesia" (inactive).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | 1 row shows |
| 2 | Create `AT2`/"Regional"/"Regional Anaesthesia" | Confirm modal appears → Ok → "Anesthesia Type Added Successfully...!!" |
| 3 | Create a second new row | **Confirm modal appears again** — unlike ARmaster/bloodBagCapacity, this screen has no once-only quirk; every Create is confirm-gated |
| 4 | Leave any field blank, click Save | Inline red "... is required" message under the empty field(s), no network request fires |
| 5 | Edit `AT1` | Form populates via a real server round-trip (check Network tab for a `getAnesthesiaTypeMasterById` call) |
| 6 | Change a field, click Update | No confirm modal, success toast, row updates |
| 7 | Toggle a row's status | Flips, success toast |

**Needs your explicit sign-off on:**
- Nothing outstanding — this screen's audit came back clean. The only note is the already-covered, standing shared-`<modal>`-directive close-button policy (entry 55), not a fresh decision.

---

## 4. `bloodDonortype`

**Paths:** `bloodDonortype` (legacy / rebuild)

**Test data already in the dev DB:** `BDT1`/"Voluntary Donor" (inactive).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | 1 row shows |
| 2 | Leave both fields blank, click Save | Toast "Please Enter Blood Donor Type Code" — matches legacy's own toast-style (not inline) validation |
| 3 | Create `BDT2`/"Replacement Donor" | Confirm modal → Ok → "Added Successfully." |
| 4 | Create a second new row | No confirm modal — same once-per-session quirk as `ARmaster`/`bloodBagCapacity` |
| 5 | Edit `BDT1` | Form populates via a real `getBloodDonorTypeMasterById` call |
| 6 | Update, then toggle status | Both succeed with matching toasts |

**Needs your explicit sign-off on:**
- **Legacy bug, NOT fixed, backend-only**: the duplicate-check has no organization/unit scope (checks description only, globally) — same class as the already-fixed `BedsideImagingMaster`/`BedsideProcedureMaster` finding from earlier this batch cycle, but not yet applied here. A DECISION_NEEDED proposal exists for this fix, awaiting your separate approval.

---

## 5. `nutritionalMaster` — write path fixed 2026-07-25; Create/Update/status-toggle now genuinely work

**Paths:** `nutritionalMaster` (legacy / rebuild)

**Fix applied since this checklist was first drafted:** the entire write path was previously broken (Create/Update/status-toggle all 500'd on real legacy, root cause an entity/schema mismatch — the Java entity expected a column, `is_calories`, that didn't exist in the real table). Owner approved the additive fix (`ALTER TABLE kitchen.m_nutrition_master ADD COLUMN IF NOT EXISTS is_calories varchar(1);`) on dedicated branch `fix/nutritionalmaster-is-calories-column`; applied to the local dev DB and live-verified on both apps — see `docs/DECISION_NEEDED_NutritionalMasterCreateAlwaysFails.md` §7 for the full before/after record. **The DB fix is in place regardless of that branch's merge status** (it's a database change, not application code), so both apps below should now show fully working behavior.

**Test data:** dev DB is currently **empty** for this screen (all test rows created during verification were deleted afterward).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Empty list, "No Records Found" |
| 2 | Fill in a Nutrition Code and Nutrition Name (Code is optional — not required in legacy, but fill it in for a cleaner test), click Save | Confirm modal appears → Ok → **real success toast** ("Nutrition Added Successfully...!!"), new row appears in the list |
| 3 | Click Edit on that row, change the Nutrition Name, click Save | Form populates from the row (client-side copy, no server call for Edit itself); Update call fires on Save → real success toast ("Nutrition Updated Successfully...!!"), row reflects the new name |
| 4 | Click the status toggle on that row | Switch flips and persists → real HTTP 200 on `updateStatus` (no explicit toast on this action, matching legacy) |
| 5 | Reload the page | All changes from steps 2-4 persisted — confirms real database writes, not just optimistic UI updates |

**Needs your explicit sign-off on:**
- **Confirms resolution of a severity-1 legacy bug**: this screen's entire write path was non-functional in real, unmodified legacy until this session's fix — the above steps should now all succeed genuinely, where they previously silently did nothing. This was a real backend bug (missing database column), not a rebuild-side issue, and not something the Angular rebuild could have worked around on its own.
- **Fix is held, not yet merged**: `fix/nutritionalmaster-is-calories-column` (the DB-schema fix) awaits your separate, explicit approval to merge into `main`, per standing practice — this Step 6 pass is verifying the fix works, not authorizing the merge.
- **Separately, already logged, unaffected by this fix**: the count endpoint (`getNutritionMasterCount`) still 500s (a different, already-identified bug — a stray `session.close()` call). The rebuild works around this with corrected, working client-side pagination (owner-approved previously), independent of the write-path fix above.

---

## Summary for your review

| Screen | Write operations functional? | Rebuild-only fixes needing sign-off | Legacy bugs faithfully reproduced |
|---|---|---|---|
| bloodBagCapacity | Yes | Edit/toggle state isolation (severity-1 safety fix) | Duplicate-check field mix-up + missing scope (backend, separate) |
| ARmaster | Yes | Corrected pagination | Zero field validation |
| anaesthesiaType | Yes | None | None |
| bloodDonortype | Yes | None | Missing org/unit scope on duplicate-check (backend, separate) |
| nutritionalMaster | **Yes — fixed and verified 2026-07-25** | None (fix was DB-schema-only, held on `fix/nutritionalmaster-is-calories-column` pending merge approval) | count endpoint still 500s (separate, already-logged, unaffected by this fix) |
