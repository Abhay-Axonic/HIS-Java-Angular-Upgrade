# Batch 17 — Step 6 Domain-Expert Review Checklist

*Clinical/billing-tier batch, continuing the standing policy from batch 15. All 5 screens are through Steps 0-5 on `rebuild/phase4-batch17` (held, not merged, not pushed). This doc consolidates each screen's own Step 6 material into one place for a methodical walkthrough — see `docs/ANGULAR_PENDING_FINDINGS.md` entries 159-163 for the full audit writeups behind each item below.*

**Setup, once, before starting any of the 5:**
- `git checkout rebuild/phase4-batch17`, confirm `ng serve` (port 4200) and the Java backend (port 8080) are both running.
- Legacy screens are all reached at `http://localhost:8080/#/<state>`; rebuilt screens at `http://localhost:4200/<state>`.
- Confirm the `Admin` seed account isn't locked out before starting: `psql -h localhost -d his_local -c "SELECT login_failed_count, login_block_time FROM m_user_master WHERE user_id=1;"` should show `0`/`null`. If a prior session left it locked, reset both to `0`/`null`.

---

## 1. `sterilizationTypeMaster` — recommended first, given severity

**Paths:** `sterilizationTypeMaster` (legacy / rebuild)

**Test data already in the dev DB** (disclosed, not cleaned up): `ST1`/"Steam Sterilization" ×2 (one updated/inactive), `ETO`/"Ethylene Oxide Sterilization" ×2 (one updated/inactive), `PLASMA`/"Plasma Sterilization" (active).

| # | Step | Expected result |
|---|---|---|
| 1 | Load the screen | List shows 5 rows; **no pagination controls visible** — this is expected, matches a real, confirmed live legacy bug (see below), not a rebuild defect |
| 2 | Create a new row (any code/name) | Confirm modal appears → Ok → success toast → list refreshes with the new row |
| 3 | Save a **duplicate** code+name | **Accepted on both apps** — legacy has no duplicate-check at all here; this is a known, disclosed bug, not a rebuild defect |
| 4 | Edit a row, change a field, but don't click Save yet | The table's displayed value must **not** change until Save is clicked (confirms a rebuild-only fix — legacy has a live bug here, see below) |
| 5 | Click Save while editing | No confirm modal (direct update, matches legacy) |
| 6 | Toggle a row's status | Switch animates and persists after reload |
| 7 | Search "updated" | Only matching rows show |
| 8 | Leave Code blank, click Save | Red "Code is required" message appears, no network request fires |

**Needs your explicit sign-off on:**
- **Severity-1, legacy bug, NOT fixed in the rebuild (faithful reproduction)**: the count endpoint (`getSterilizationTypesListCount`) unconditionally throws `HibernateException: No Session found for current thread` — missing `@Transactional`, confirmed the only method in its service class lacking it. **This means pagination has never worked in real, unmodified legacy**, independent of row count. A legacy fix is proposed and held on `fix/sterilizationtypemaster-missing-transactional` (one-line, adds the missing annotation) — separate approval decision from this rebuild.
- **No duplicate-check exists in the backend at all** (step 3) — logged, not fixed, faithful to legacy.
- **Two rebuild-only fixes** you should confirm you're comfortable with as "disclosed improvements," not deviations requiring a hold: (a) step 4's edit-form-mutates-live-table-cell bug, fixed by cloning the row before editing; (b) legacy's Update payload never sends a real `unitId`, silently defaulting a server-side timezone lookup to zone 0 — fixed by sending the real session values.
- **One validation fix**: legacy's Code and Name inputs share `name="code"` in the HTML, breaking the inline "Code is required" message via a form-control-name collision (overall submission was still correctly blocked, just no visible message) — corrected per this project's standing validation-visibility policy.
- Legacy's own "Update" button doesn't functionally exist — a single "Save"-titled button always does both — reproduced faithfully (not a bug, just a UI label quirk).

---

## 2. `discountCategoryMaster`

**Paths:** `discountCategoryMaster` (legacy / rebuild)

| # | Step | Expected result |
|---|---|---|
| 1 | Create Code=`DC-OWNER1`, Description=`Owner Review Test` | Confirm modal → Ok → success toast, row appears, toggle green |
| 2 | Save the **exact same** Code+Description again | **Silently accepted, not rejected** — a second identical row appears. Looks wrong, but is faithful to a real, confirmed legacy bug (see below) |
| 3 | Edit any row's Description, click Update | No confirm modal (matches legacy), success toast, row updates in place |
| 4 | Toggle a row's status | Immediate visual flip; check Network tab for a real `updateDiscountCategoryStatus` 200 |
| 5 | Search by partial code/description | Visible rows filter (current page only) |
| 6 | Visit with a session lacking module 973 | "Access Denied" on both apps identically |

**Needs your explicit sign-off on:**
- **Legacy bug, not fixed**: the duplicate-check queries the wrong table entirely (`m_discount_type` instead of `m_discount_category` — an incomplete rename left behind, confirmed via a source comment referencing the original developers). Legacy can never actually catch a real duplicate. Step 2 above demonstrates this exactly.
- **Legacy bug, not fixed**: `getDiscountCategoryCount` filters active-only while `getDiscountCategoryList` doesn't — deactivating any row makes the pager wrong.
- One rebuild-only fix (confirm-modal gate not re-arming on a genuine HTTP 500, only on 200-body rejections) — narrow, no sign-off needed, matches established precedent.

---

## 3. `GLcode`

**Paths:** `GLcode` (legacy / rebuild)

| # | Step | Expected result |
|---|---|---|
| 1 | Create `GL010`/"Test Account" on both apps | Confirm modal → Ok → success toast → row appears |
| 2 | Save the same code/name again | "Already Exists." rejection on both, no second row |
| 3 | Retry immediately with fresh valid values | Confirm modal must reappear (not skip straight to save) |
| 4 | Edit a row, change its description, click Update | **On legacy**: the on-screen list does NOT show the new value until a full page reload (confirmed bug — the underlying save is still correct, just not reflected). **On the rebuild**: the list updates immediately (the fix) |
| 5 | Toggle a row's status | Flips and persists (reload to confirm if the switch itself is hard to click) |
| 6 | Search a keyword matching an existing code/description | Filters the currently-loaded page |

**Needs your explicit sign-off on:**
- **Legacy bug, not fixed**: the save-path duplicate-check has a real HQL operator-precedence bug — `LOWER(name)=:x OR name=:x AND orgId=:y` parses (AND binds tighter than OR) as `LOWER(name)=:x OR (name=:x AND orgId=:y)`, so a case-insensitive match from a *different organization* would falsely reject as a duplicate. Not reproducible live in this single-org dev environment, but the source gap is real.
- **Legacy bug, not fixed**: the update-path duplicate-check has zero organization scoping at all.
- **Rebuild-only fix, needs your sign-off on classification** (step 4): legacy's stale-list-after-update bug is fixed in the rebuild — a disclosed improvement, not silent parity, per the identificationMaster/currencyMaster precedent.

---

## 4. `storeTypeMaster`

**Paths:** `storeTypeMaster` (legacy / rebuild)

**Test data already in the dev DB** (real, pre-existing seed rows, not created for this batch): `MS`/Main Store, `SS`/Sub Store, `CS`/Consignment Store, `CSSDS`/CSSD Store, `LS`/Linen Store, `NS`/Narcotics Store — all org 1/unit 1.

| # | Step | Expected result |
|---|---|---|
| 1 | Load the list | 6 seed rows shown, matching legacy exactly, pagination correct |
| 2 | Create a new row | Success |
| 3 | Attempt an **exact** duplicate | Rejected with "Already Exists." |
| 4 | Edit a row | Form populates correctly |
| 5 | Update it | Resets to create mode |
| 6 | Toggle status | Switch flips; independently check `is_active` in the DB if you want to confirm persistence directly |
| 7 | Click Cancel on the confirm modal | Nothing saves |
| 8 | Search | Filters current page only |

**Needs your explicit sign-off on:**
- **Legacy bug, not fixed**: the by-id lookup and both exists-check queries have their unit-scope clause literally commented out in source (`// AND mstr.unitId=:unitId`), while the count query IS unit-scoped — a real mismatch between sibling queries. Not reproducible live in this single-unit dev environment.
- A local-dev-DB sequence drift was found and fixed during testing (harmless, environment-only) — all 6 original seed rows confirmed unchanged afterward.

---

## 5. `bloodGroupMaster`

**Paths:** `bloodGroupMaster` (legacy / rebuild)

| # | Step | Expected result |
|---|---|---|
| 1 | Create code `A+`, description "A Positive" | Confirm modal → Ok → "Added Successfully." toast, row appears |
| 2 | Create a second row with the **same description but a different code** (e.g. `A+DUP`) | "Already Exists." — confirms the real, mis-scoped duplicate check keys on description only, not code |
| 3 | Open the confirm modal, then Cancel/close it | Form stays populated, nothing saved; click Save again — modal must reopen |
| 4 | Edit a row, change description, Update | "Updated Successfully.", form resets to Create mode |
| 5 | Toggle status | Visible flip, persists, no revert |
| 6 | Search by partial code/description | Filters current page only |
| 7 | Leave Code or Description blank, click Save | Toast only ("Please Enter Code "/"Please Enter Description "), no network call, no inline red text (legacy has none for this screen) |

**Needs your explicit sign-off on:**
- **Legacy bug, not fixed**: the duplicate-check is scoped globally across every organization and unit, **and never checks the code field at all** — only description. The most permissive gap found across this whole batch.
- No module/permission gate exists on this screen at all (confirmed genuinely gate-less in source, same class as the already-documented `billingGroupMaster`/`periodicityService` gap) — correctly reproduced as gate-less, not a rebuild oversight.

---

## Cross-cutting pattern worth your attention

All 5 screens had some form of broken or mis-scoped duplicate-detection in the legacy backend — see my separate note on whether this warrants its own dedicated future sweep.
