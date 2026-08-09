# Q1 — the owner click-through (prepared 2026-08-08)

Two checks, five minutes. The app is ALREADY running the merged-main build —
no rebuild, no restart needed to start. Run the rate-limit verifier first
(see step 0), then the two checks.

## Step 0 — before you start (1 minute)

From the repo root, in a terminal:

    python3 tools/verify_rate_limit.py

Expect "RESULT: 10/10 passed". This must run on clean buckets — the
click-through's logins consume them. If it FAILs on the 429-boundary checks,
the instance needs a restart to clear the in-memory rate-limit buckets
(catalina stop/start per docs/DEPLOY_PROCEDURE.md), then re-run the verifier.
If it passes, proceed.

## Check A — the silent-save guard (mailConfiguration)

**URL:** the legacy app at http://localhost:8080 — after login navigate Admin
menu -> Global Master -> Mail Configuration (the route is
http://localhost:8080/#/mailConfiguration if you want the direct link).

**What to do, in order:**
1. Login (your usual dev credentials).
2. Open the Mail Configuration screen.
3. In the TOP-LEFT **Trigger Point** select, pick the CRITICAL-REPORT trigger
   (the screen shows it as one of the trigger options). This matters: the
   "Mark as a important for critical report" checkbox only appears when the
   critical-report trigger is selected.
4. Tick the checkbox whose rendered label reads **"Mark as a important for
   critical report"** (the right side of the form).
5. Fill in the other fields the screen requires (name etc. — whatever the
   validation asks for).
6. Click the **Save** button (top right, the floppy-disk icon, title="Save").
   A two-step confirm applies: the Save click opens a confirmation modal —
   click **OK in the modal**; the save request fires only on that OK.
7. Watch the growl toast (top right).

**PASS:** a red ERROR toast with exactly:
    "Cannot save: critical report has no backing column. Deselect it to save the configuration."
No success toast, no row saved.

**FAIL:** a green success toast (the config saved) — the guard did not reach
the user.

**Caveat (known, not your error):** the dev master tables are EMPTY
(m_sms_trigger_master = 0, m_sms_category_master = 0 rows), so the Trigger
Point select may have no options and the screen's own validation can block
the save before the request fires. If the select is empty and the save will
not proceed, the UI path cannot reach the guard — fall back to the API-level
test of the same guard:
    curl -s -X POST http://localhost:8080/sms-utility-config \
      -H "Authorization: <your session token>" -H "Content-Type: application/json" \
      -d '{"triggerId":30,"criticalReport":"Y"}'
A PASS is the JSON envelope with status ERROR and that same message; a FAIL
is status success.

## Check B — the serviceMaster screen

**URL:** the REBUILT screen runs in the NEW Angular build — start it if it is
not running (ng serve, your command), then open
http://localhost:4200/serviceMaster (the route is registered with the
module-376 guard).

**What should load:** the service master screen — the grid of service
masters, the search, the add/edit modal.

**Dropdowns that should populate:** the department/unit selects and the
item/search dropdowns inside the add/edit modal. NOTE: the dev data has
specialities with a NULL general_ledger_id, and the screen's query INNER-JOINs
on that field — so the dept dropdown can legitimately come up EMPTY on dev
data. That is the known dev-data gap (QF-2026-08-07-L), not a regression.

**Known open divergences (BR-186) — do not mistake these for regressions:**
1. The legacy save's two-step confirm popup is NOT rebuilt — the new screen
   saves directly (a deliberate parity gap, queued).
2. The ledger cell's toString formatting differs from the legacy screen
   (queued).

So the two things to confirm here are only: the screen loads, and the
dropdowns that CAN populate (those not depending on the NULL-general_ledger_id
rows) do.

## After both checks — the six token-blocked harnesses

The six master-screen harnesses need a fresh JWT:
    python3 tools/verify_master_screen.py <screen> --token <jwt>
for sex, relation, marital, religion, occupation, priority (in any order).
The token: from the SAME browser session you logged in with — F12 ->
Application -> Session Storage -> copy the token value -> paste as <jwt>.
(The established remint flow prints TOKEN-OK but does not expose the value,
so the session copy is the unblock.) Run them after the click-through —
the token must be from a login at least as fresh as the checks.

Report the two growl/load outcomes and the six harness results back to the
session; nothing merges or pushes from here.

## Check C — the combinationMedicationMaster screen (screen 4)

**URL:** http://localhost:4201/combinationMedicationMaster (the REBUILT screen in the
NEW Angular build; the route is registered with the module-472 guard).

**What should load:** the combination medication master screen — the form row (Combination
Medicine, Generic, Strength, Clinical Unit of Measurement, Display Generic Name — all marked
required per the legacy labels), the Save button + Search box, and the 7-column grid
(Combination medicine, Generic, Strength, CUM, Display Generic Name, Status, Action).

**Dropdowns that should populate:** the Generic select (GET /api/globals/generics/1 — dev
data has real rows: Paracetamol…). The Combination Medicine select and the grid
(GET /api/globals/generics/IsCombination/1) and the Clinical Unit of Measurement select
(GET /api/global/uom/units/1) come up EMPTY on dev data — no combination generics, no UOM
rows seeded. That is the known dev-data gap (screen-4 README finding 2), not a regression.

**The sequence:**
1. The screen loads; the Generic select populates with the real generics.
2. Type in the Search box — the loaded grid rows filter client-side (an empty grid stays
   empty — the legit dev-data state).
3. A row's status toggle — PATCH /api/globals/generics/{id} — a success growl ("Generic
   Updated Successfully"). NOTE: the grid is empty on dev data, so there is no row to toggle
   in the UI; the endpoint itself is 201-verified.
4. The pencil on a row — the edit fetch (getCombinationDetail/{id}) populates the form.
5. Save — an ERROR growl "Save not wired — no combination-medication save endpoint exists".
   This is INTENTIONAL (the finding), NOT a regression.

**PASS looks like:** the screen loads with no red toasts; the Generic select populates; the
network tab shows the three calls (/api/globals/generics/1, /IsCombination/1,
/api/global/uom/units/1) all HTTP 200; the Save click shows the honest "not wired" notice;
the empty grid shows "No combination medications found".

**FAIL looks like:** the guard denies (FINAL URL /accessDenied — module-472 permission
missing from the store); a red toast on load ("…load failed"); a non-2xx on any of the
three calls.

**Known divergences (do not mistake for regressions):**
1. The Strength field is a NUMBER input (the legacy's select had no options and no data
   source; the rebuild binds m_combination_generic.strength_unit — a double).
2. The Search box filters the loaded list client-side (the legacy's was inert).
3. The grid's detail cells (Generic/Strength/CUM/Display name) fill from the edit's detail
   fetch; the IsCombination list rows carry only id/code/name/status.
4. Save is intentionally not wired (no endpoint exists — the finding); the legacy's save
   anchor was a dead link to a nonexistent modal too.
5. The rebuild ADDS behaviour the legacy never had (the legacy is a static shell) — the
   sign-off list is in the screen-4 session report: dropdowns load real data, the grid
   renders the IsCombination list, the status toggle calls the PATCH, the edit fetches the
   detail, the search filters, and the Save reports honestly.
