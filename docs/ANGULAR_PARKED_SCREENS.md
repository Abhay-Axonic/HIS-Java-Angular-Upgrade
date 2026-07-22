# Angular Rebuild — Parked Screens (needs individual verification before batch selection)

*Regenerated 2026-07-22 from a fresh full-pool sweep (`tools/angular-audit/check_backend_endpoints.py`, no `--only`/`--limit`), after the original list from the 2026-07-21 sweep became unrecoverable — its output lived only in the gitignored `tools/angular-audit/data/` directory and was silently overwritten by later per-batch targeted runs. **This file is the fix**: it's a tracked, committed snapshot, not regeneratable working data. See the bottom of this file for the regeneration procedure and where this fits going forward.*

## What this list is

Screens flagged by the automated sweep as either (a) having at least one endpoint that 404'd (a possible missing-backend gap, the `modalityTypeMaster`/entry 67 pattern) or (b) containing clinical-keyword hits in their real API-call/field surface (a possible mistagged-as-low-risk screen, the `doorToNeedle`/entry 66 pattern) — **that have not yet been individually investigated and resolved**. This is explicitly NOT the full sweep output; it excludes every screen this project has already investigated and resolved one way or the other (see "Already-resolved screens excluded from this list" below). Per standing project discipline, none of the screens below should be selected into a batch without first individually verifying them (same treatment the original 2026-07-21 parked list received).

**Sweep population:** 159 screens (built screens, `doorToNeedle`, and shared×353-controller screens excluded from the sweep itself, per the tool's own design). **35 screens remain genuinely unresolved** after excluding everything already investigated: 2 missing-backend hits, 35 clinical-keyword hits (overlap between the two lists: 2 screens).

**Caveat carried over from the original list:** this sweep ran without a live auth token (none could be safely obtained — see prior session notes on why raw token extraction is out of scope). Some endpoint prefixes 401 at a security filter before Spring MVC routing even runs, making 'route missing' and 'route exists, needs auth' indistinguishable — the missing-backend count may under-report. Confirmed via source-level Controller/Service/DAO checks remains the more reliable method for any individual screen, same as every batch's own Step 0 process already does.

## New discovery while regenerating this list — entry 78's OPD tab family was undercounted

`docs/ANGULAR_PENDING_FINDINGS.md` entry 78 named 7 OPD cover-sheet tab screens as confirmed clinical-tier. This fresh sweep found **7 more** screens in the same OPD module showing the identical clinical-keyword pattern (`encounter`/`patient`/`vital`/`diagnos`/`medicat` in their real API-call surface), not individually named in entry 78: `drugHistoryTab`, `vitalsTab`, `examinationTab`, `notesTab`, `adviceTab`, `follwUpTab`, `attachToCasesheetTab`. These are listed below as their own flagged group — **treat as high-suspicion-clinical by default, not neutral-parked**, given how closely they pattern-match the already-confirmed 7. Recommend a quick confirmation pass (the same content-based check that resolved entry 78) before ever selecting any of them, but not full parity with the neutral "unverified" status of the other entries below.

## Screens needing individual verification

| Screen | Module | Missing-backend? | Clinical-keyword hits | Note |
|---|---|---|---|---|
| addNewNabhReporting | Admin - Org/Unit Master |  | patient |  |
| addNewOrderSetMaster | Admin - Org/Unit Master |  | doctor, medicat |  |
| adviceTab | OPD |  | encounter, patient, vital | **High-suspicion clinical — likely 8th-14th members of entry 78's OPD tab family, see above** |
| aiButtonConfiguration | Admin - Org/Unit Master |  | doctor |  |
| applicationConfiguration | Admin - Configuration |  | bed, consult, patient |  |
| appointmentScheduling | Appointment Scheduling |  | doctor, patient |  |
| appointmentSchedulingUnitMaster | Appointment Scheduling |  | doctor |  |
| appointments.services | Common/Shared |  | admission, bed, diagnos, nursing, patient, surger, ward |  |
| attachToCasesheetTab | OPD |  | encounter, patient, vital | **High-suspicion clinical — likely 8th-14th members of entry 78's OPD tab family, see above** |
| bedAllocation | ADT - Bed Management |  | admission, bed, nursing, patient, ward |  |
| bedAvailability | ADT - Bed Management |  | bed |  |
| bedBook | ADT - Bed Management |  | admission |  |
| bedMaster | Admin - Org/Unit Master |  | bed, ward |  |
| doctorWise | Appointment Scheduling |  | doctor |  |
| drugHistoryTab | OPD |  | admission, doctor, encounter, medicat, patient, vital | **High-suspicion clinical — likely 8th-14th members of entry 78's OPD tab family, see above** |
| examinationTab | OPD |  | encounter, patient, vital | **High-suspicion clinical — likely 8th-14th members of entry 78's OPD tab family, see above** |
| follwUpTab | OPD |  | admission, encounter, patient, vital | **High-suspicion clinical — likely 8th-14th members of entry 78's OPD tab family, see above** |
| formFSelfId | Common/Shared |  | patient |  |
| inPatientFeedback | Appointment Scheduling | 1/2 | patient |  |
| initialAssessmentForICU | Admin - Org/Unit Master |  | patient, vital |  |
| login | Common/Shared |  | clinical, doctor | Core auth flow, not a real batch-selection candidate regardless of verification outcome |
| logout | Common/Shared |  | doctor | Core auth flow, not a real batch-selection candidate regardless of verification outcome |
| nabhReporting | Admin - Org/Unit Master |  | patient |  |
| notesTab | OPD |  | doctor, encounter, nursing, patient, vital | **High-suspicion clinical — likely 8th-14th members of entry 78's OPD tab family, see above** |
| outPatientFeedback | Appointment Scheduling | 1/2 | patient |  |
| passIssued | ADT - Admission |  | bed, patient, ward |  |
| patientCategory | Admin - Global Master (general) |  | patient |  |
| patientList | Common/Shared |  | encounter, patient |  |
| reportedNabh | Admin - Org/Unit Master |  | patient |  |
| scheduleWise | Appointment Scheduling |  | doctor |  |
| tallyInterfacing | Admin - Unit Master (tally-interfacing.html) |  | doctor |  |
| userRoleManagement | Admin - User Management |  | clinical |  |
| vitalsTab | OPD |  | encounter, patient, vital | **High-suspicion clinical — likely 8th-14th members of entry 78's OPD tab family, see above** |
| wardStoreMapper | Asset Management |  | ward |  |
| webAppointment | Appointment Scheduling |  | doctor, encounter |  |

## Already-resolved screens excluded from this list

Every screen this project has already individually investigated and resolved, one way or the other, is deliberately absent from the table above even if the raw sweep still flags it (its own internal risk-tag data hasn't been updated to match these manual corrections): `doorToNeedle` (entry 66), the 4-screen homecare cluster (entry 77), the 15-screen OPD-tab/ADT-bed cluster (entry 78), the `*Dashboard*` family (entry 79, confirmed FINE, stays in the normal pool), the 6-screen disconnected-wiring cluster (entry 80) plus its 8 wider-sweep candidates (entry 85), `serviceMaster` (entry 103) and 6 more mistagged screens (entry 118), `supplyDaysMaster` (entry 102) and `mailConfiguration` (entry 119, both excluded pending an owner decision, not a risk-tag issue), and all 25 screens already selected into batches 9-11.

## Regeneration procedure (for whoever runs this next)

```
cd tools/angular-audit
python3 extract_states.py && python3 build_controller_index.py && python3 classify_risk.py
# do NOT run generate_inventory.py or run_audit.py here -- both unconditionally overwrite
# docs/ANGULAR_PHASE0_SCREEN_INVENTORY.md from raw classification, silently discarding every
# manual correction layered on top since (this happened once, 2026-07-22, caught and reverted
# via git checkout before it was committed -- see docs/SESSION_LOG.md).
cd ../..
python3 tools/angular-audit/check_backend_endpoints.py
# tools/angular-audit/data/endpoint_check_report.json now has the fresh raw sweep. Filter out
# every screen already listed in "Already-resolved screens excluded from this list" below (update
# that set first with anything resolved since), keep the rest, and update this file's table by hand.
# There's no dedicated script for this filtering step -- it's intentionally a manual, reviewed step
# each time, not a fully automated one, so a real human (or an agent under real instruction) has to
# look at what's newly flagged before it gets marked "needs verification" vs. silently dropped.
```

This file lives at `docs/ANGULAR_PARKED_SCREENS.md` going forward — a tracked, committed markdown file, specifically NOT under `tools/angular-audit/data/` (gitignored, ephemeral, gets silently overwritten by every targeted per-batch sweep run). Update it by hand after each full-pool regeneration; don't expect any script to write here automatically.
