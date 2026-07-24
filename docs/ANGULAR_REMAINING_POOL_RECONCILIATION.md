# Angular Rebuild — Remaining-Pool Reconciliation (2026-07-24)

*A consolidation of everything already discovered across this project's findings docs into one current, trustworthy estimate of how many screens are REALLY still buildable. This is not new research — every figure below is pulled from `docs/ANGULAR_PENDING_FINDINGS.md`, `docs/ANGULAR_PARKED_SCREENS.md`, and `docs/ANGULAR_PHASE4_SEQUENCING_RECOMMENDATION.md`, cited by entry/line number, plus a small amount of direct verification against the actual codebase where the docs' own arithmetic was ambiguous or hedged. Written so this doesn't need to be re-derived from scratch again — update this doc, don't recompute it, next time a batch changes these numbers.*

---

## 1. Total original corrected inventory: **1,567 screens**

Source: `docs/ANGULAR_PHASE4_SEQUENCING_RECOMMENDATION.md:9`, citing `docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md` (corrected 2026-07-18) — the real total, up from an original undercounted 1,098, once 469 self-registering screens (states declared via their own `.config($stateProvider)` outside `app.js`) were found and added.

**Risk-tier breakdown of the full 1,567** (`ANGULAR_PHASE4_SEQUENCING_RECOMMENDATION.md:34-40`, the authoritative tagging — sums exactly to 1,567):

| Tier | Count | Share |
|---|---:|---:|
| Both clinical-safety AND billing | 81 | 5% |
| Clinical-safety only | 521 | 33% |
| Billing only | 602 | 38% |
| Low risk (neither) | 345 | 22% |
| Unclear / needs runtime verification | 18 | 1% |

This tagging was computed **before** several later findings (below) moved specific screens between tiers or off the board entirely — those adjustments are layered on top of this baseline in the sections that follow, not yet reflected in the table itself.

---

## 2. Screens complete: **157**

**Verified directly against the codebase, not derived from doc arithmetic.** `new/src/app/features/` contains 158 directories; one (`_screen-template`) is the Phase-3 scaffold placeholder, not a real screen, leaving **157**. Independently cross-checked against `new/src/app/app.routes.ts`, which has exactly 157 distinct `loadComponent:` references — the two counts agree.

This *corrects* two earlier approximate figures used in this project's own conversation history (~151 before batch 16, ~156 after) — both were built from hedged, individually-cited-but-never-summed per-batch mentions in `ANGULAR_PENDING_FINDINGS.md` (e.g. "~141 across batches 1a-15"). Summing those same per-batch citations precisely (89 + 10 + 10 + 15 + 7 + 6 + 9 + 5 + 5 = 156 for Phase 4 batches 1a–16 alone, +10 pilot = 166) does not match either the prior approximate figures or the direct code count — the discrepancy is not resolved here, because **the direct code count (157) is ground truth and the doc-arithmetic path is not**; going forward, treat `new/src/app/features/` minus `_screen-template` as the authoritative "screens complete" count, not a re-sum of batch mentions.

---

## 3. Confirmed dead / mockup / orphaned / unbuildable-as-is

### 3a. The `MasterController` shared-controller cluster — the single largest bucket

- **353 screens** total route through the shared `MasterController` project-wide, across all risk tiers (`ANGULAR_PENDING_FINDINGS.md:61,73`).
- Of those 353, **70** are tagged low-risk (neither clinical nor billing) and not yet built — the precise, recomputed population as of entry 146, **superseding** the earlier looser "~90-100+" estimate floated in entries 140/142 (`ANGULAR_PENDING_FINDINGS.md:2226,2262` vs. the recount at `:2385`).
- **37 of those 70 (53%) were individually sampled**, stratified across every module, across 3 independent research passes (entry 146, `:2383-2397`):
  - **0 alive**
  - **30 confirmed genuine dead/mockup** (static HTML, no live bindings anywhere)
  - **5 confirmed "decoy-routed"** (entry 147, `:2399-2412`) — real, working functionality for the same feature exists elsewhere in the app under a *different* route/controller name (`newAdmissionRequest`, `procedureConfig`, `sendToCondemnation`, `staffExchangeLinen`, `issueDirtyLinen`). These are not worth independently rebuilding as their own screens — the real functionality either is, or should be, captured under its actual working name instead.
  - **2 ambiguous** (`appointments`, `MPShahPatientList`) — not close to alive, but not cleanly dead-mockup either; would need individual Step-0 treatment.
- **Extrapolating the sample's 94.6% dead-or-decoy rate (35/37) across the full 70:** ~66 of 70 are likely dead/non-buildable-as-is, ~4 likely ambiguous. **This is an extrapolation, not an individual finding** — the remaining 33 of the 70 have not been individually sampled.
- **The other 283 of 353** (353 − 70) carry clinical or billing tags and have **not been sampled or characterized at all**. The 94.6% dead rate has explicitly **not** been extrapolated to this subset (confirmed no entry claims otherwise). Given the same structural argument applies regardless of risk tag (`common-controller.js` has zero per-state branching and never defines `vm`/`controllerAs`, per entry 146), it's plausible a meaningful fraction of these 283 are similarly dead — but this is **speculation, not yet tested**, and should not be assumed either way until a comparable sample is run against this subset specifically (naturally, as clinical/billing-tier batches reach screens that happen to route through `MasterController`).

**Recommended treatment:** exclude ~66 (extrapolated) from the low-risk "real remaining buildable" count; treat ~4 as needing individual Step-0 verification before scheduling; treat the 283 clinical/billing-tagged `MasterController` screens as **entirely unresolved** — not counted as dead, not counted as confirmed-buildable.

### 3b. Individually-confirmed dead/mockup/broken screens, own dedicated controllers (outside the `MasterController` cluster)

| Screen(s) | Entry | Finding |
|---|---|---|
| `careplanMaster`, `interventionsMaster`, `activitiesMaster`, `scoreMaster` (4-screen "homecare" cluster) | 77 | Confirmed dead backend (genuine 404s) AND frontend never even attempts the call. |
| `diagnosisMaster` | 148 | Confirmed dead backend, same defect class; explicitly recommended as a 5th homecare-cluster member — **not yet formally appended to entry 77 itself, owner's call.** Counted here as a 5th, pending that formal merge. |
| `modalityTypeMaster` | 67 | Confirmed dead backend — all 6 endpoints 404 on real, unmodified legacy too (not a rebuild-only gap); frontend is real and correctly wired, backend was simply never built. |
| `SurgicalSiteInfection` | 6 | Confirmed non-functional copy-pasted mockup, zero live bindings on its 58 SSI-specific fields. |
| `OTBillingCompletion` | 7 | Wired view is a stale clone of a different screen's template; ~35 template tokens don't resolve against the actually-wired controller. Flagged as unresolved-which-controller-is-real, not firmly "dead" — excluded from the buildable count pending that decision, not asserted dead outright. |
| `bedBook` | 53 | Static mockup grid, no live bindings, wired to a mismatched controller. |
| `doctorPayOuts` | 138 | Confirmed non-functional mockup, wired to a mismatched controller (`roomModalityController`, not `MasterController`). |

**Subtotal: 10 screens** (4 homecare + 1 diagnosisMaster + 1 modalityTypeMaster + 1 SurgicalSiteInfection + 1 OTBillingCompletion + 1 bedBook + 1 doctorPayOuts).

### 3c. Screens with possible overlap with the `MasterController` cluster (3a) — flagged, not double-counted

| Screen | Entry | Note |
|---|---|---|
| `addItemPOCreation` | 2 | Wired to `MasterController` per its own entry — may already be part of the 353/70 population above rather than a separate, additional screen. The real functionality exists under a different route (`addItemPO`), same decoy-routing shape as entry 147's 5. |
| `medicineInfo` | 140 | Broken via a lowercase `"masterController"` controller-reference typo (never registered) — would throw on load. Ambiguous whether the 353-count's census (likely an exact-string match on `"MasterController"`) picked this up or missed it due to the typo. |
| `roomModulityMapper` | 138 | Explicitly wired to shared `MasterController` — likely already part of the 353/70 population, found incidentally during batch 12 (before entry 146's formal census), not independently confirmed as additional. |

**Not summed into any total below** — flagged here specifically so they aren't silently double-counted against 3a in a future pass.

---

## 4. Screens removed from the low-risk pool for mistagging, but real and buildable — future clinical/billing-tier candidates

- **Entry 78, 15 screens**, confirmed real and buildable, reclassified from low-risk to clinical (`ANGULAR_PENDING_FINDINGS.md:1201-1211`), based on live API calls carrying unambiguous clinical-data keywords, not name/URL heuristics:
  - OPD cover-sheet tab family (7): `OPDCoverSheet`, `OPDcoversheetNew`, `medicalHistoryTab`, `ordersTab`, `complaintsTab`, `socialHistoryTab`, `letterTab`
  - ADT admission/bed cluster (8): `admission`, `admittedPatientList`, `admissionRequest`, `pendingAdmissionDischarge`, `bedManagement`, `bedReservationList`, `mapOfBedForER`, `passBedCategoryMapper`
- **+ 7 more, not yet individually confirmed:** `docs/ANGULAR_PARKED_SCREENS.md:13-15` found 7 additional OPD-tab-family screens showing the identical clinical-keyword pattern (`drugHistoryTab`, `vitalsTab`, `examinationTab`, `notesTab`, `adviceTab`, `follwUpTab`, `attachToCasesheetTab`) — flagged "high-suspicion-clinical by default," **not yet given the same individual confirmation entry 78's 15 received.**
- **+ at least 1 more independent instance of the same mistagging defect class**, found via `serviceMaster` (entry 103, `:1589-1593`) — explicitly a *different* screen matching the same pattern, not part of the 15-screen cluster itself, not yet consolidated into any combined count.

**These 15 (confirmed) + up to 7 (probable, unconfirmed) + ≥1 (independent instance) are REAL, BUILDABLE screens.** They were originally counted inside the 345-screen low-risk tier in the Section 1 table; they now belong in the clinical-tier pool instead. Not dead, not lost, just moved — and not yet re-added to any clinical-tier running total, since that reconciliation hasn't been done anywhere before this doc.

---

## 5. Screens parked pending SME or other blockers — real, just not yet selectable

- **`allergyTypeMaster`, `doseMaster`** (entry 148, `:2426-2427`) — real, working, simple-looking screens that are actually safety-adjacent (real FKs into patient allergy records / medication dosing). Require Step 6b clinician sign-off. **No SME currently exists on this project for 6b** (confirmed project-wide, not screen-specific).
- **`corporateMasterGlobal`** (entry 148, `:2420`) — same shape, but for a billing reason: real `creditLimit`/`creditDays` monetary-cap fields, a genuine calculation-input requiring Step 6b (finance SME), not clinician.
- **34 screens on `docs/ANGULAR_PARKED_SCREENS.md`'s active list** (regenerated 2026-07-22 after the original list was lost) — 2 missing-backend suspects + clinical-keyword hits (with a 2-screen overlap between the two lists), genuinely unresolved, needing individual Step-0 verification before they can be called either dead or buildable. **Not confirmed dead, not confirmed buildable — a real, open queue**, distinct from both the confirmed-dead lists above and the confirmed-real-but-reclassified list in Section 4.
- **`supplyDaysMaster`, `mailConfiguration`** (entries 102, 119) — 2 more screens with their own pending owner decisions, explicitly **not** risk-tagging issues (a different kind of blocker each) — not investigated further in this pass, flagged only.
- **`aiButtonConfiguration`** — a false-positive clinical flag, already resolved (traces to `DoctorMasterDto`, not patient data) — deferred from batch 14 purely by owner caution, **not because verification is open.** Functionally available for building now; not a real blocker, listed here only because it lives on the parked list.
- **2 structural non-candidates**, `login`/`logout` — flagged on the parked list as "core auth flow, not a real batch-selection candidate regardless of verification outcome." Not part of the remaining-buildable pool at all.

---

## 6. The actual remaining REAL BUILDABLE estimate

### Low-risk tier

Original tag: **345 screens** (22% of 1,567). This project already made a standing decision (documented going into batch 15) that **the administrative/low-risk pool is confirmed exhausted** for practical rebuild purposes — this reconciliation now puts a number behind that conclusion rather than just restating it:

- Of the 70 low-risk `MasterController` screens specifically, an estimated **~66 are dead/non-buildable-as-is** (Section 3a extrapolation), leaving perhaps **~4** genuinely uncertain.
- The 15 (+ up to 7 probable, + ≥1 independent) mistagged screens have moved OUT of this tier into the clinical-tier candidate pool (Section 4) — they no longer count toward "low-risk remaining" at all.
- The 10 screens in Section 3b and the 3 flagged in Section 3c further reduce whatever's left, to the extent they're low-risk-tagged (not all confirmed to be — several are plausibly clinical, e.g. the homecare cluster).
- A genuinely unknown-but-likely-small number of low-risk screens outside the `MasterController` cluster remain unswept (some fraction of the 34 on the parked list may turn out to be low-risk rather than clinical, once individually verified).

**Rough estimate: realistically a small number — single digits to perhaps 15-20 screens — not the naive "345 minus what's built" figure.** This is a rough estimate, explicitly hedged: no one has swept 100% of the low-risk tier individually, only the 70-screen `MasterController` slice and scattered incidental finds. But every piece of evidence gathered so far (the 94.6% dead sample, the standing "exhausted" decision, the absence of any large intact low-risk cluster still undiscovered) points to this being a small, not a large, number.

### Clinical/billing tier

Original tag: **1,204 screens** (81 both + 521 clinical-only + 602 billing-only = 77% of 1,567).

- Minus **10 already built** from this tier (batch 15: `identificationMaster`, `cardTypeMaster`, `deliveryTypeMaster`, `encounterTypeMaster`, `consentTypeMaster`; batch 16: `currencyMaster`, `currencydenomination`, `incidentCategoryMaster`, `mortuaryBedMaster`, `nursingStationMaster`).
- Plus the **15 (+ up to 7 probable, + ≥1 independent) reclassified-in** screens from Section 4, which were NOT part of the original 1,204 tag (they were miscounted as low-risk at the time) — these add to this tier's real total, not yet reflected in the 1,204 figure anywhere.
- Includes the **283 uncharacterized `MasterController` screens** tagged clinical/billing (already part of the 1,204) — genuinely unknown aliveness, no sample has been run.
- Includes the **34 parked-list screens** (Section 5) and the **3 SME-blocked screens** (Section 5) — all real, all clinical/billing-tagged, currently un-actionable for different reasons (unverified vs. no-SME) rather than dead.

**Rough estimate: roughly 1,190–1,215 screens, extremely low confidence.** This is barely more than the original 1,204 tag minus what's built — it is **not** a reconciled, trustworthy figure the way the low-risk estimate above is, because:
1. No representative dead/mockup sample has ever been run against the clinical/billing tier — the 94.6% low-risk dead rate cannot be assumed to transfer (clinical/billing screens are plausibly less likely to be abandoned utility screens, but this is a guess, not evidence).
2. The 283 `MasterController`-routed clinical/billing screens alone could shift this number by anywhere from a handful to several hundred, entirely depending on an aliveness rate nobody has measured yet.
3. The 34 parked + up to 22 reclassified screens all need individual Step-0 confirmation before any of them can be called truly "real and buildable" rather than "probably real, not yet checked."

**If a trustworthy clinical/billing-tier estimate matters before batch 17, running the same kind of stratified sample entry 146 ran on the low-risk `MasterController` slice — but against the 283 clinical/billing-tagged `MasterController` screens specifically — would be the highest-value next research step.** Not done as part of this reconciliation; flagged as a candidate for a future session, not blocking batch 17 (which draws from screens with their own dedicated controllers, verified individually per the existing Step 0 process, not from this uncharacterized bucket).

---

## Summary table

| Category | Count | Confidence |
|---|---:|---|
| Total corrected inventory | 1,567 | High (cited, corrected 2026-07-18) |
| Screens complete | 157 | High (direct code count, cross-checked 2 ways) |
| `MasterController` cluster, total | 353 | High (cited) |
| — low-risk, unbuilt subset | 70 | High (recomputed, entry 146) |
| — of which confirmed dead/decoy (sampled 37) | 35 | High (individually sampled) |
| — of which extrapolated dead (remaining 33) | ~31 | **Extrapolated, not individually verified** |
| — of which clinical/billing-tagged, uncharacterized | 283 | **Entirely unknown** |
| Individually-confirmed dead (own controllers) | 10 | High (individually cited) |
| Flagged possible overlap w/ MasterController cluster | 3 | Flagged, not summed |
| Reclassified low-risk→clinical, confirmed | 15 | High (entry 78) |
| Reclassified low-risk→clinical, probable/unconfirmed | ≤7 | **Unconfirmed** |
| Parked, needing individual verification | 34 | Open queue |
| SME-blocked (real, unselectable) | 3 | High (identity), blocked status stable |
| **Low-risk tier, real remaining buildable** | **~5-20 (rough)** | **Low-moderate confidence, but well-supported directionally** |
| **Clinical/billing tier, real remaining buildable** | **~1,190-1,215 (rough)** | **Very low confidence — largely untested** |

---

*Maintained as a docs-governance file per `CLAUDE.md` (no Maker-Checker required). Update this doc directly when a future batch, sweep, or characterization pass changes any of these figures — don't let a new estimate live only in a chat conversation.*
