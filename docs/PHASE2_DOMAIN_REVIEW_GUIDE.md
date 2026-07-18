# Phase 2 pilot — domain-expert review guide

*For whoever is doing the manual review of the 5 rebuilt screens. This is the last step of the per-screen process (audit → golden-trace → rebuild → re-audit → **domain-expert review**) before the pilot's results go back to the owner and Phase 3 planning starts. Phase 3 is explicitly on hold until this review is complete and its results have been reported back.*

---

## Read this first: what's already been verified, and what hasn't

**Verified already, automated, not your job to re-check:** every screen's *initial-load* API activity was captured live against the running legacy app (`tools/golden-trace/`) and then re-diffed against what the rebuilt Angular component actually fires on construction (`docs/PHASE2_PILOT_AUDIT.md` section 9). All 5 screens match, call for call, on load. One real gap was found this way (`insuranceMaster`'s ledger-name backfill) and fixed; one apparent mismatch (`organizationMaster`'s `/privilege/NaN/NaN`) was traced to a capture-harness artifact, not a real bug.

**Not verified by any of that, and squarely your job:** *everything that happens after the screen loads.* Golden-trace capture only observes what fires automatically when a screen is entered — it has no way to click a button, fill a form, or open a modal. That means the following are **completely unverified by automation** on all 5 screens, and need you to actually exercise them by hand, side by side against the legacy screen:

- Every field's validation behavior (required-field messages, format checks, cascading-dropdown resets)
- Create / save flows
- Edit / update flows, including whatever gets pre-filled when you open a record for editing
- Search, filter, and pagination
- Status toggle (active/inactive)
- Every modal
- File uploads, where applicable
- Anything the audit already flagged as worth extra attention (see the per-screen checklists below)

Don't treat "the screen loads and looks right" as the review. That part is already covered. Your value here is in the parts automation can't reach.

---

## How to run both side by side

You'll need a browser session with a valid login against a real (dev/staging) backend for **both** apps to show real data — neither app does anything useful against an empty database, and this review environment has no live backend/DB wired up on the side where this rebuild was built, so you'll need to point both at wherever your team's real dev/staging deployment lives.

1. **Legacy screen:** log into the existing app normally, in one browser tab. Each screen's URL is its ui-router state name as a hash route, e.g. `https://<your-dev-host>/#/insuranceMaster`. State names for the 5 pilot screens: `MISReports`, `insuranceMaster`, `inputOutputCharts`, `organizationMaster`, `billingGroupMaster`.

2. **Rebuilt screen:** from `new/`, run `npx ng serve`. By default this serves on `http://localhost:4200` and routes match the legacy state names directly, e.g. `http://localhost:4200/insuranceMaster`.

3. **The part that needs setup, not just running a command — session/auth:** `new/src/app/core/session.service.ts` reads the exact same `cookieObject` cookie the legacy app writes on login, by design (so it'll work unmodified once ngUpgrade wires it in later). That means the rebuilt app **only shows real data if the browser tab it's running in can see a cookie set by a real legacy login** — which typically means same domain, or a dev proxy that makes the two look same-origin to the browser. Two ways to get there, pick whichever fits your infra:
   - **Proxy approach (quicker for a one-off review):** point `ng serve`'s dev-server proxy at your dev backend so API calls go same-origin (a starter file is at `new/proxy.conf.example.json` — copy to `proxy.conf.json`, fill in your dev backend host, run `npx ng serve --proxy-config proxy.conf.json`), and log into the *legacy* app on that same dev host in the same browser first so the cookie is set before you open the rebuilt app's tab.
   - **Reverse-proxy approach (better if you're doing this more than once):** serve `new/dist` (via `npx ng build`) behind the same reverse proxy / domain as the legacy dev deployment, at a different path, so cookies are naturally shared.
   
   Whoever owns the dev/staging environment will know which of these is less friction — this is an infrastructure setup step outside what could be wired up in the sandbox this rebuild was written in.

4. **Compare with two windows open side by side**, same logged-in user/org/unit in both, same underlying data. Pick a few real records (not brand-new ones you just created) so you're comparing against real production-shaped data, not just a clean-slate test case.

---

## Comparison method

For each screen, don't just eyeball it — walk the **same concrete record** through the **same sequence of actions** on both screens and compare outcomes at each step:

1. Load the list. Compare: row count, column values, sort/default order, status indicators.
2. Search/filter with the same keyword on both. Compare result sets.
3. Page through results (if pagination applies). Compare page counts and page-size behavior.
4. Open an existing record for edit. Compare: every field's pre-filled value, especially any field that involves a cascading lookup (geography dropdowns, typeaheads) — confirm the cascade populates correctly, not just the top-level value.
5. Make a small, reversible edit and save. Compare: success message wording, what happens to the list afterward, whether the form resets.
6. Try to save with a required field missing. Compare: which fields get flagged, in what order, and the exact validation message wording (both screens have real, sometimes redundant, validation logic — see the per-screen notes below for known quirks, not bugs).
7. Toggle status (active/inactive) on a row. Compare the visible result.
8. Open every modal the screen has. Compare contents and behavior.
9. If the screen uploads a file, upload one on both and compare the result (and, for `organizationMaster`, the progress-bar behavior — see its checklist item below).

## How to report what you find

For anything that doesn't match, log it as a row in `docs/PHASE2_DOMAIN_REVIEW_RESULTS.md` (skeleton provided, one table per screen) rather than fixing it yourself or filing it elsewhere — this keeps findings in the same place the rest of this pilot's audit trail lives, and keeps the Maker/Checker discipline intact (nobody should push a fix straight into `new/` off a review note without it going through the same flagged-decision process the rest of this project has used). Please note severity honestly — "text is one word off from legacy" and "this deletes the wrong record" are not the same urgency, and mixing them together makes the summary harder to act on.

---

## Deliberate, known, non-bug deviations from legacy — don't re-report these

These were already investigated and are documented decisions, not gaps you need to rediscover:

- **`insuranceMaster`**: keeps legacy's redundant two-layer validation (HTML + manual JS) exactly as-is — this is intentional, not a bug (`docs/ANGULAR_PENDING_FINDINGS.md` entry 1). The "Grade Details" modal isn't rebuilt because its trigger is unreachable in the legacy view too.
- **`organizationMaster`**: uses the already-fixed `$state`-injection guard rather than reproducing the old crash-on-missing-cookie bug. The commented-out licence-details sub-table and the BI/OTIMS module-access tabs aren't rebuilt because they're unreachable UI in legacy too. Mobile-number-length validation is checked only on update, not save — that's a real legacy asymmetry, kept visible on purpose, not harmonized away.
- **`inputOutputCharts`**: Print/PDF generation is stubbed (shows a warning, doesn't generate anything) — no report-rendering backend to integrate against in this pilot. Patient search uses a minimal inline implementation rather than the full shared search partials, which were explicitly out of scope for Phase 2's per-screen characterization.
- **`MISReports`**: `Servicesearch`/`DiagnosiSearch`/`VenderSearch` typeaheads render but don't return real results (resolve to "nothing selected") — those specific endpoints weren't independently audited in this pilot. Only `ItemSearch` (medication) and the generic patient text search are fully wired.
- **`insuranceMaster`'s general-ledger backfill** (see its checklist item below): the rebuild's version is provably *not* byte-for-byte identical to legacy in one specific race condition — this is flagged and tracked (`docs/ANGULAR_PENDING_FINDINGS.md` entry 4), not something you need to independently discover, though your real-data testing on it is still valuable (see below).

If you think one of these decisions was wrong, that's useful feedback — but report it as "I disagree with decision X" against the entry above, not as a fresh bug.

---

## Per-screen checklists

### billingGroupMaster

Legacy path: `#/billingGroupMaster`. Rebuilt path: `/billingGroupMaster`.

**Specifically flagged for extra attention:** this screen's legacy wiring is unusual — its view embeds `ng-controller="BillingGroupMasterController"` directly in the HTML, layering the real controller as a *child scope* on top of the generic `MasterController` the route itself points at. That's a different pattern from the other 4 screens (which have their own dedicated controller directly on the route). Things that could specifically go missing in a rebuild of *this* pattern, that wouldn't show up in a simpler screen:
- **Page chrome that came from `MasterController`, not `BillingGroupMasterController`.** `MasterController`'s `customEvent` listener maps known master-screen names to sidebar/page titles — confirm the page title and sidebar highlight for this screen still say the right thing and highlight the right nav item. The rebuild is a standalone Angular route with no equivalent wrapping controller, so if any title/chrome was coming from that mechanism rather than the view itself, it could be silently missing. (This may turn out to be a legitimate open question for Phase 7's ngUpgrade host-page design rather than a same-screen bug — flag it either way, don't assume.)
- Tab-level state or date-picker init that `MasterController` provides generically — confirm nothing on this screen was quietly depending on that scaffolding.
- Standard checklist: speciality dual-list picker (add/remove between the two columns), the "view specialities by group" modal, search/pagination, create/edit/status-toggle.

### insuranceMaster

Legacy path: `#/insuranceMaster`. Rebuilt path: `/insuranceMaster`.

**Specifically flagged for extra attention — the general-ledger backfill fix, across real data, not just this pilot's small sample:**
- Load the list with real production-shaped data (ideally an org/unit with many insurance records tied to a variety of different general ledgers, not a handful of test rows) and confirm the "General Ledger" column shows the correct name for every row, not blank, on a **fresh page load** (not just after some other interaction warms up the data).
- Reload the page a few times in a row. The rebuild is designed to always end up correct regardless of which of its two startup network calls finishes first (a real, documented difference from legacy's behavior in that same race — see `docs/ANGULAR_PENDING_FINDINGS.md` entry 4) — if you ever see a blank ledger name that doesn't resolve itself, that's worth reporting as a real bug, not the known/accepted race-condition difference.
- If you can, compare against legacy under the same repeated-reload test — legacy is the one with the *theoretical* risk of showing blanks depending on network timing; if you can actually catch legacy doing that, it's useful confirmation of the finding (not something you need to chase hard, just worth a note if you happen to see it).
- Standard checklist: cascading geography dropdowns (country → state → district → city → area, with auto-filled zip), ledger typeahead search, file upload with extension validation, both validation layers (HTML + the manual JS-style checks), address-details modal, create/edit/status-toggle.

### inputOutputCharts

Legacy path: `#/inputOutputCharts`. Rebuilt path: `/inputOutputCharts`.

**Specifically flagged for extra attention — ward-scoping via `sessionStorage.nursingStationsWardsList`:** this is the easiest thing on this screen to have silently missed, because it was invisible to the original Phase 0 static scan entirely — it only surfaced because this pilot went and read the controller source directly. The legacy sidebar writes a list of the current nurse's assigned wards into `sessionStorage` under this key; 30+ nursing/pharmacy controllers (including this one) read it to scope patient search results. The rebuild reads the same key the same way, on the theory that ngUpgrade hybrid mode will share the same browser session storage — **but this can only be genuinely tested once there's a real hybrid session to test against**, i.e. once you've navigated to the rebuilt screen from within a live legacy session that actually has this key populated (not a rebuilt-app-only session with nothing populating that key). Specifically check:
- Confirm `sessionStorage.getItem('nursingStationsWardsList')` is actually populated (open devtools) when you reach the rebuilt screen via a real legacy session.
- Search for the same patient/keyword on both screens as a nurse whose ward assignment should exclude some patients — confirm both screens return the **same, correctly ward-scoped** result set, not just "some" results.
- If your test user has no ward restriction (sees everyone), this check won't be meaningful — use a test account with an actual ward assignment if one exists.
- Standard checklist: intake/output repeatable rows (add/remove, running totals), fluid-type cascading dropdown, shift-by-time cascading dropdown, time validation warn-and-auto-correct behavior (try entering a future time), Start/Stop I/O confirm modals, save.
- Print/PDF button is a known stub (see "deliberate deviations" above) — don't re-report it.

### organizationMaster

Legacy path: `#/organizationMaster`. Rebuilt path: `/organizationMaster`.

**Specifically flagged for extra attention — the `fileProgress`/logo-upload coupling with `hospitalMasterController`:** legacy's `imageUploadFactory.js` broadcasts upload progress (`fileProgress`) to whichever controller is listening — both `organizationController.js` (this screen) and `hospitalMasterController.js` (a *different* screen, not part of this pilot) listen to the same broadcast. The rebuild's `UploadProgressService` (`new/src/app/core/upload-progress.service.ts`) is the documented rebuild-side equivalent of that shared broadcast mechanism.

**Important limit on what's actually testable right now, be honest about this in your report rather than assuming it's fully covered:** because `hospitalMasterController`'s screen hasn't been rebuilt in this pilot, there is no rebuilt counterpart to test the *cross-screen* half of this coupling against yet — you can't verify "does a logo upload on the rebuilt `organizationMaster` correctly interoperate with `hospitalMasterController`'s upload progress display," because the latter doesn't exist in Angular yet. What you **can and should** verify now:
- Upload a logo on the rebuilt screen and confirm the progress indicator behaves correctly on its own (advances, completes, resets, doesn't get stuck or double-count on a second upload).
- Note explicitly in your findings that the *cross-screen* half of this coupling remains unverified until `hospitalMasterController` itself is rebuilt (or ngUpgrade-bridged) — this isn't something you're expected to solve, just don't let it silently read as "confirmed working" when only the single-screen half was actually testable.
- Standard checklist: cascading geography dropdowns, timezone select, module-access modal (checkbox tree, save permissions — note the BI/OTIMS tabs are intentionally not rebuilt, don't re-report), create/edit/status-toggle, the asymmetric mobile-number-length validation (update-only, not save — intentional, don't re-report).

### MISReports

Legacy path: `#/MISReports`. Rebuilt path: `/MISReports`.

This is architecturally the most complex of the 5 despite its "low-risk" tag — it's a dynamic report-parameter renderer driven entirely by backend metadata (`controlName` per parameter), not a fixed form. Checklist:
- Report group/report tab tree: selecting a group loads its reports; selecting a report loads that report's specific parameter set. Confirm both levels behave the same as legacy for several different reports, not just one — different reports render genuinely different parameter sets, so one report "working" doesn't mean the rendering engine is right for all of them.
- For each parameter control type you can find test reports for: date pickers (from/to/expiry), time pickers, patient search (`text`), radio, checkbox, numeric text box, multi-select list, and the `list`/`unit`/`user`/`org` dropdown family — confirm the right control type renders and behaves correctly per parameter.
- `ItemSearch` (medication) typeahead should return real results; `Servicesearch`/`DiagnosiSearch`/`VenderSearch` are known-unwired stubs (see "deliberate deviations" above) — don't re-report those as bugs, but do confirm they at least render (disabled/inert) rather than error out.
- View/Export buttons and the format `<select>` — confirm PDF generation and non-PDF export both produce a real downloaded file matching legacy's output for the same report/parameters, if you have a way to compare file contents (even a byte-count or page-count sanity check is useful signal here).

---

## What happens after this review

Report results back (via `docs/PHASE2_DOMAIN_REVIEW_RESULTS.md`) for the owner to see before Phase 3 (refining the rebuild method based on pilot learnings) starts. Anything found here that constitutes a real behavior gap should be treated the same way every other finding in this pilot has been — flagged and logged, not silently patched — so the owner can decide fix-now vs. track-for-later with full context, the same discipline `docs/ANGULAR_PENDING_FINDINGS.md` has followed throughout.
