# Angular rebuild: pending cleanup candidates

*Frontend equivalent of `docs/TRIAGE_PendingFindings.md` — things noticed during screen-by-screen characterization that are worth knowing about but are explicitly NOT being fixed now. Nothing here blocks the current phase; this list exists so ideas don't get lost or silently bundled into a "parity" rebuild without a decision.*

---

## 1. insuranceMaster has two redundant, inconsistent validation paths for the same fields

**Where:** `resources/assets/js/controller/master/unit/insuranceMasterController.js`, `save()` (~line 373) and `update()` (~line 730). Found during the Phase 2 pilot audit (`docs/PHASE2_PILOT_AUDIT.md`, section 2).

**What it is, plainly:** the same set of required fields (insurance code, description, country/state/district/city/area, zip, email) is validated twice, by two different mechanisms, in a different order, with different specificity:
- The HTML form itself (`required` attributes + `show-errors` + `insuranceMasterForm.$valid`).
- A separate, manual JS block that re-checks the same fields one-by-one with individual `growl.warning()` messages, runs *before* the HTML-level check, and additionally does `isNaN()` checks on the geography IDs that the HTML-level validation doesn't have.

Email regex, fax regex, and the file-extension allowlist are enforced *only* in the manual JS path, not reflected as HTML5 `pattern` attributes.

**Why this probably isn't deliberate:** this reads like accretion over time — one validation layer added at some point without the earlier one being removed — rather than a designed two-tier system. There's no comment or structural reason found for keeping both.

**Owner decision, per pilot instruction:** preserve both layers exactly for the Phase 2 pilot rebuild (true parity, no behavior change bundled in). This entry exists so the redundancy is a known, tracked cleanup candidate for later — collapsing to one validation layer would be a small, low-risk simplification whenever there's appetite for it, but it is a behavior change (the manual JS path's stricter `isNaN()` checks and regex validation aren't currently expressed in the HTML layer) and needs the same sign-off any other behavior change gets, not a silent fix.

**My read:** low priority, no urgency — this doesn't produce wrong data, it's pure duplication. Worth a look whenever `insuranceMaster` (or its rebuilt equivalent) gets touched for other reasons, not worth scheduling time for on its own.
