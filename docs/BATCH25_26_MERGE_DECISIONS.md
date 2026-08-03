# Batch 25 / Batch 26 — Merge Decision Document (for owner review)

Status: PENDING OWNER DECISION. Prepared 2026-08-03. Nothing merged, nothing pushed.
Both branches are held. This document consolidates what is actually pending on each so the
owner can decide with clean information.

---

## Summary table

| Branch | Tip | vs main | Content | Merge blockers | Owner decision needed |
|---|---|---|---|---|---|
| `rebuild/phase4-batch25` | `8b33f445` | 33 ahead / 5 behind | 5 Angular rebuild screens + error-callback pass | None technical; needs rebase (5 behind) + shell-nav.data.ts conflict resolution | Merge? Push? |
| `rebuild/phase4-batch26` | `c4d4849b` | 34 ahead / 4 behind | 2 Angular rebuild screens (tariffMaster, ambulanceTarrifMaster) + callbacks | Uncommitted files in main-repo worktree must be sorted first; rebase (4 behind) + shell-nav.data.ts conflict | Merge? Push? + legacy-Java-file decision |

---

## Batch 25 — `rebuild/phase4-batch25` (tip `8b33f445`)

**What's in it (committed, vs main):** 22 files, +2956/−26. Angular rebuilds of:
- patientSourceMaster (Phase 4 batch 25)
- billingBedCategoryMaster
- icuTypeMaster
- referralTypeMaster
- aiButtonConfiguration (callbacks commit `1ee726e2`, earlier)

**Error-callback pass (commit `8b33f445`):** all 3 remaining screens verified this session —
6 subscribes = 6 error handlers each on icu-type-master, billing-bed-category-master,
referral-type-master. aiButtonConfiguration's 6 callbacks were committed earlier (1ee726e2).
**Callback completeness: CONFIRMED (24 subscribes / 24 handlers across the batch).**

**Known limitation (documented, not a blocker):** aiButtonConfiguration org/unit-mismatch
behavior is a same-tenant correctness bug, not cross-tenant IDOR (owner-classified earlier),
recorded as BR-PENDING-aibuttonconfiguration-org-unit-mismatch.

**Merge mechanics:** 5 behind main (BR-153/154 and earlier docs commits). Needs rebase onto
main; the shared `new/src/app/shell/shell-nav.data.ts` will conflict (batch 25 adds 4 nav
entries, main unchanged there since batch 24) — resolve keeping both sides (established
pattern). Worktree `.claude/worktrees/batch25` has 2 uncommitted doc files
(ANGULAR_PENDING_FINDINGS.md, SESSION_LOG.md) from that branch's era — these are branch-local
doc edits, to be committed to the branch or dropped per your preference before merge.

**Risk read: LOW.** Pure Angular additions, no backend change, callbacks complete, no legacy
Java touched. The only real work is the rebase + nav conflict.

---

## Batch 26 — `rebuild/phase4-batch26` (tip `c4d4849b`)

**What's in it (committed, vs main):** 13 files, +1953/−26. Angular rebuilds of:
- tariffMaster (commit `5ef77b27`)
- ambulanceTarrifMaster (commit `5ef77b27` area; callbacks `c4d4849b`: ambulance-tarrif 4,
  tariff-master 3 = 7 error callbacks)

**Step 6 sign-off:** both screens' manual review was completed earlier (tariffMaster all
checks passed; ambulanceTarrifMaster all checks passed). Branch was held pending the items
below, not for screen defects.

**Merge mechanics:**
1. **REBASE needed** (4 behind main). Shared `shell-nav.data.ts` conflict (batch 26 adds 5
   nav entries) — keep both sides.
2. **Uncommitted files in the main-repo worktree must be sorted FIRST.** Current state:
   - `legacy/Global/.../AmbulanceTarrifDaoImpl.java` — MODIFIED (null-guard on
     `.toString()` calls at L182-185). **This is a legacy Java change that must NOT ride the
     rebuild branch** (standing rule: no legacy Java on rebuild branches). Decision: commit
     it to a separate legacy-fix branch, or revert it — owner's call. NOTE: it looks like a
     genuine NPE-safety improvement; if kept, it should be its own branch/BR.
   - `new/src/app/features/ai-button-configuration/*` (5 files, staged) — these belong to
     the **batch-25 aiButtonConfiguration** ship (the `aibutton-ship` worktree exists), NOT
     batch 26. They should not ride batch 26.
   - `new/angular.json` (analytics:false) + docs files — tooling/docs, safe either way.
3. **SlotMasterDaoImpl revert hazard: RESOLVED.** The earlier hazard (branch carrying a
   pre-fix SlotMasterDaoImpl that would revert main's doctorWise fix) is GONE — verified this
   session: `git diff main...rebuild/phase4-batch26 -- SlotMasterDaoImpl.java` is EMPTY. The
   rebase that dropped the stale file already cleaned it.

**Risk read: LOW for the committed content** (pure Angular + callbacks, sign-off complete).
The work is in the worktree hygiene: sort the 3 uncommitted groups above, rebase, resolve
nav conflict, then merge.

---

## Recommended merge order (for owner approval)

1. **Batch 25 first** (5 screens, callbacks complete, only rebase+nav conflict to handle).
2. **Batch 26 second** — after the uncommitted-file triage above. The aiButton files fold
   into the batch-25 ship; the AmbulanceTarrifDaoImpl null-guard goes to its own branch
   (recommend: yes, it's a real NPE-safety fix — separate BR, same rigor).
3. Both merges **held for explicit approval** before push, per standing rule.

## Files this document covers
- `rebuild/phase4-batch25` tip `8b33f445` — 33 ahead / 5 behind
- `rebuild/phase4-batch26` tip `c4d4849b` — 34 ahead / 4 behind
- Uncommitted main-repo worktree state (3 groups: legacy Java / aiButton Angular / tooling-docs)
- `docs/ANGULAR_PENDING_FINDINGS.md`, `docs/SESSION_LOG.md` (both branches' doc edits)

## Owner decisions requested
1. Approve batch-25 rebase + merge (+ push after build verification)?
2. Approve batch-26 uncommitted-file triage: (a) AmbulanceTarrifDaoImpl null-guard → separate
   branch as a BR? (b) aiButton files → batch-25 ship? (c) angular.json/docs → keep or drop?
3. After triage, approve batch-26 rebase + merge (+ push)?
