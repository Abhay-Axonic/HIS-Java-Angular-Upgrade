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
