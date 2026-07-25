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

## 5. `nutritionalMaster` — expect this one to look broken; that's the confirmed, correct behavior

**Paths:** `nutritionalMaster` (legacy / rebuild)

**Test data:** dev DB is currently **empty** for this screen (a SQL-seeded test row used during Step 5 verification was deleted afterward — Create doesn't work, so seeding was the only way to test Edit/Update/toggle at all).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | Empty list, "No Records Found" — no error shown, matches legacy |
| 2 | Fill in a Nutrition Name (Code is optional — not required in legacy), click Save | Confirm modal appears → Ok → **nothing visibly happens**. No success toast, no error toast, no new row. This is the confirmed, correct, faithful match to a real, live legacy bug (see below) — not a rebuild defect |
| 3 | (Optional, for your own confirmation) Seed one row directly via SQL, then try Edit/Update/status-toggle against it | Edit populates the form (client-side copy, no server call). Update and status-toggle will **also** silently do nothing, for the same underlying reason as step 2 |

**Needs your explicit sign-off on:**
- **Severity-1, CONFIRMED root cause, legacy bug, NOT fixed (backend fix requires your separate approval)**: `nutritionalMaster`'s Create, Update, AND status-toggle are all completely non-functional in current legacy — verified independently of the rebuild via direct `curl` testing against the live backend. Root cause confirmed via direct database schema inspection: the Java entity expects a column (`is_calories`) that does not exist in the real table. Every entity-level write references this nonexistent column and fails; only the read-only list queries (which don't touch that column) work. The failure is completely silent to the user in legacy today — no error appears, ever, on any of the three actions. Full detail and a proposed one-line additive fix (add the missing column) are in `docs/DECISION_NEEDED_NutritionalMasterCreateAlwaysFails.md`, awaiting your approval on a dedicated fix branch.
- **Separately, already logged**: the count endpoint also 500s (a different, already-identified bug — a stray `session.close()` call). The rebuild works around this with corrected, working client-side pagination (owner-approved), independent of whether the write-path fix above is applied.
- **Net effect for this Step 6 review**: there is currently no way to see a populated, working version of this screen's Create/Update/toggle flow on *either* app. This isn't something Step 6 can "pass" in the usual sense — it's flagging that the screen is faithfully, correctly broken in the same way on both, pending the backend fix.

---

## Summary for your review

| Screen | Write operations functional? | Rebuild-only fixes needing sign-off | Legacy bugs faithfully reproduced |
|---|---|---|---|
| bloodBagCapacity | Yes | Edit/toggle state isolation (severity-1 safety fix) | Duplicate-check field mix-up + missing scope (backend, separate) |
| ARmaster | Yes | Corrected pagination | Zero field validation |
| anaesthesiaType | Yes | None | None |
| bloodDonortype | Yes | None | Missing org/unit scope on duplicate-check (backend, separate) |
| nutritionalMaster | **No — all 3 broken in legacy today** | None (nothing to fix client-side) | Entire write path (backend, separate, root cause confirmed) |
