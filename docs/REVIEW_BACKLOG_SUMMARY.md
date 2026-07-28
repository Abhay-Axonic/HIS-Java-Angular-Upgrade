# Review Backlog — Consolidated Reference (as of 2026-07-28)

*One-stop reference for working through the outstanding batch reviews and decision docs. Each batch's own `docs/BATCHNN_STEP6_CHECKLIST.md` has the full step-by-step test tables — this document is the index/overview, not a replacement for them.*

---

## Part 1 — Batches awaiting your Step 6 review

| Batch | Screens (count) | Worktree / port | Branch | Status |
|---|---|---|---|---|
| **20** | `call`, `linenReturn`, `accessControl1`, `assetPMSScheduler`, `incidentMaster` (5) | `.claude/worktrees/step6-batch20`, **`http://localhost:4308`** | `rebuild/phase4-batch20` | Held, not merged. Checklist: `docs/BATCH20_STEP6_CHECKLIST.md` |
| **21** | `dietTypeMaster`, `wrapperExpiryMaster`, `occupancyUnitMaster`, `genericScheduleMaster`, `growthChartMaster` (5) | `.claude/worktrees/step6-batch21`, **`http://localhost:4309`** | `rebuild/phase4-batch21` | Held, not merged. Checklist: `docs/BATCH21_STEP6_CHECKLIST.md` |
| **24** | `vitalQualifierMaster`, `checklistEquipmentMaster`, `checklistEquipmentMapper`, `autoclaveMaster`, `checklistItemMaster` (5) | `.claude/worktrees/batch24`, **`http://localhost:4306`** | `rebuild/phase4-batch24` | Held, not merged. Checklist: `docs/BATCH24_STEP6_CHECKLIST.md`. **⚠ See correction below — the checklist's `vitalQualifierMaster` row is stale.** |
| **25** | `aiButtonConfiguration`, `billingBedCategoryMaster`, `icutypeMaster`, `patientSourceMaster`, `referralTypeMaster` (5) | `.claude/worktrees/batch25`, **`http://localhost:4310`** | `rebuild/phase4-batch25` | Held, not merged. Checklist: `docs/BATCH25_STEP6_CHECKLIST.md` |
| **26** | `tariffMaster`, `doctorWise`, `ambulanceTarrifMaster` (3 — deliberately shipped smaller after 2 candidates were disqualified) | `.claude/worktrees/batch26`, **`http://localhost:4311`** | `rebuild/phase4-batch26` | Held, not merged. Checklist: `docs/BATCH26_STEP6_CHECKLIST.md` |

**Total: 23 screens across 5 batches**, all held on their own branches, none merged or pushed.

**⚠ Correction to `docs/BATCH24_STEP6_CHECKLIST.md`:** its `vitalQualifierMaster` row still reads "confirm modal never auto-dismisses" as a faithfully-reproduced quirk. That claim was later disproven — live-verified on real legacy that the modal DOES auto-close on success — and the rebuild code was already corrected (commit `fa811897`, already on the `rebuild/phase4-batch24` branch itself, done during this session's confirm-modal shared-directive re-audit). **The code is correct; the checklist document's prose is what's stale.** When you get to batch 24, treat this as already-fixed, not an open item — full detail in `docs/ANGULAR_PENDING_FINDINGS.md`'s `PENDING-confirmmodal-shared-directive-reaudit` entry.

**Key findings per batch, at a glance:**
- **Batch 20**: no confirm-modal issues. `call`'s "Recall" button is display-only (real bug, awaiting a separate decision); `accessControl1` had a real dead duplicate-role guard, now fixed in the rebuild.
- **Batch 21**: `dietTypeMaster` carries the **SEVERITY-1** below (Update always fails). `growthChartMaster`'s rebuild added working search/pagination where legacy's own are dead — a corrected-and-disclosed deviation, not a faithful reproduction.
- **Batch 24**: `checklistEquipmentMapper` had a **SEVERITY-1 DB fix** (missing `general_ledger_id` column) — already fixed and verified, not a pending item. `autoclaveMaster` has a known duplicate-check gap awaiting a Tier-3 decision.
- **Batch 25**: `aiButtonConfiguration` carries the **SEVERITY-1** below (stale-configId silent overwrite) — both the rebuild fix and the matching legacy fix are done and merged (`c649f1e5`); what's outstanding is your manual walk-through confirmation, not a code decision. `billingBedCategoryMaster` has one disclosed deviation (clean id-based diff replacing a silent reference-equality bug).
- **Batch 26**: `tariffMaster`'s schema-mismatch severity-1 is now **RESOLVED and merged** (`BR-101`) — writes work on both apps now. `doctorWise`'s search-crash severity-1 is **fixed and owner-confirmed** — both closed, nothing outstanding from this batch's severity-1s. Batch 26's own Step 6 rebuild-parity checklist (`docs/BATCH26_STEP6_CHECKLIST.md`) is still open for your review.

---

## Part 2 — Decision docs needing your call

| Doc | One-line ask |
|---|---|
| `docs/DECISION_NEEDED_TariffMasterLetterHeadImageColumnMissing.md` | **RESOLVED (2026-07-28).** Option A approved, fixed (`BR-101`), live-verified, merged to `main`. Nothing further needed. |
| `docs/DECISION_NEEDED_MealMasterInactiveRowsNoRecoveryPath.md` | Approve **Option A** (add a "Show inactive" toggle to `mealMaster`'s list, reusing an already-working but currently-uncalled backend endpoint) vs. **Option B** (separate inactive-rows view) vs. neither — this is a real product gap (no way to reactivate a deactivated meal), not a rebuild defect. |

*(Two more still-open decision docs exist from earlier in the project but weren't raised in this session's conversation — `docs/DECISION_NEEDED_CompanyMasterTariffMasterCodeBlindDuplicateCheck.md` (companyMaster/tariffMaster's Code-blind duplicate-check bug, referenced above as a known-but-unfixed issue) and `docs/DECISION_NEEDED_UnitServiceTariffMasterDaoImpl.md` (unread this session). Flagging their existence for completeness, not summarizing content you haven't asked about yet.)*

---

## Part 3 — Severity-1 findings awaiting your manual confirmation

These are the highest-priority items. Each has already been fixed and independently re-verified at the source/API level — what's outstanding is *your own* hands-on walkthrough, not further engineering:

1. ~~**`aiButtonConfiguration`** (batch 25)~~ — **CONFIRMED by owner (2026-07-28).** Full overwrite scenario tested: config A + config B both created as separate rows, config A left untouched. Legacy fix (merged, `c649f1e5`) and the rebuild's own correction (held on `rebuild/phase4-batch25`) both verified. Closed.
2. ~~**`dietTypeMaster`** (batch 21)~~ — **CONFIRMED by owner (2026-07-28).** Both apps fail identically on Update, row unchanged on both — the correct outcome, since this is a real, faithfully-reproduced (not fixed) legacy bug. Test row deactivated afterward. Closed.
3. ~~**`doctorWise`** (batch 26)~~ — **CONFIRMED by owner and MERGED to `main` (2026-07-28).** Both search combinations (real dates + blank Time; real dates + Time 09:00-17:00, including the full downstream booking write path) tested and work as expected. Closed.
4. ~~**`tariffMaster`** (batch 26)~~ — **RESOLVED.** Fixed (`BR-101`), live-verified, merged to `main`. Closed.

**All 4 original severity-1 findings are now closed.** Remaining work is the routine Step 6 rebuild-parity review for each batch's own checklist (Part 1 above) — no more severity-1-specific items outstanding.
