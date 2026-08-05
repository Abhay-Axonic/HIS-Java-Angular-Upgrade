# MERGE PROTOCOL — recomposed-system merges into main (authorization pending)

Owner constraints (2026-08-05, verbatim intent):
1. The sweep merge is the risky one (merge-base 6c279f29, several eras back). Dry-run first:
   `git merge --no-commit --no-ff` on the sweep branch, report the conflict set, then abort.
   Resolve NOTHING on the dry run.
2. "Take composed-all's version" is NOT a blanket resolution. For every conflicted file:
   state the expected final counts BEFORE resolving, resolve by hand file by file,
   verify against the stated counts after. getGenderById went missing during a CLEAN
   apply once — never trust any apply, clean or conflicted.
3. Post-merge proof: `git diff deploy/composed-all main -- legacy/` must be empty.
   Then verify_deployed_regressions.py, then rebuild+redeploy from merged main and a
   second regression run — jars must be reproducible from main alone.

## Merge order (all `--no-ff`, Rule 9)

1. `fix/five-screens-org-trust` (batch, 6 commits) — expected clean (main never touched those files; merge-base aa7736c9).
2. `rebuild/phase-sweep-lis-common` (sweep, 5 commits) — DRY RUN FIRST (below). Verified read-only
   2026-08-05: branch changed exactly 11 app-code files vs its base; ZERO of those files were also
   changed by main since the base → no true 3-way conflict candidates. Precondition: the 17 dirty
   working-tree files must be cleared first (all byte-identical to the sweep branch — verified — so
   `git checkout -- <files>` discards nothing unique).
3. `fix/priority-active-list-unconditional` (BR-170, 1 commit) — expected clean vs batch+sweep (disjoint;
   the batch-tip cherry-pick was conflict-free). PriorityMaster.java may conflict here (batch+BR-170
   both touched it) — resolve per the counts below.
4. `fix/gender-active-list-org-scope` (BR-171, 1 commit) — EXPECTED CONFLICTS on exactly the 3 gender
   files (entity/DAO/service). Resolve by hand per the counts below. Never let git resolve.

## Dry-run procedure (constraint 1 — sweep, at authorization time)

```
git checkout -- <17 dirty files>          # precondition (content-identical to sweep branch)
git merge --no-commit --no-ff rebuild/phase-sweep-lis-common
git status --short                        # report the conflict set (expected: empty or minimal)
git merge --abort                         # do NOT resolve anything
```
Also report what the merge touches beyond the 5 commits' files (verified read-only: nothing — 11 files, all from the 5 commits).

## Resolution spec — expected final counts BEFORE resolving (constraint 2)

Source of truth for every resolution: the `deploy/composed-all` versions (the proven hand-merge,
deployed + verified 2026-08-05 by the sex spec and the regression checker).

### GenderMasterServiceImpl.java (conflict: sweep vs BR-171)
- `getVerifiedOrganizationId` refs: **6** (sweep 5: getGenderMasterList, addGenderMaster,
  getGenderById, updateGenderMaster, updateStatusForGender + BR-171: getActiveGenderList)
- VIS field: **1** (`private VerifiedIdentityService verifiedIdentityService;`); VIS import: **1**
- public Response methods: **7** (incl. the DTO-overload getActiveGenderList = misnamed count alias, untouched)
- cache key: `"genderMaster:" + orgId` — **1**; `invalidateAll()`: **3**; literal
  `invalidate("genderMaster")`: **0**
- DAO call: `iGenderMasterDao.getActiveGenderList(orgId)` — **1**

### GenderMasterDaoImpl.java (conflict: sweep vs BR-171)
- `getVerifiedOrganizationId` refs: **2** (sweep update/updateStatus guards)
- `getActiveGenderList(Integer orgId)` signature: **present**; `setParameter("orgId", orgId)` in it: **1**;
  `.setResultTransformer(...)` in it: **1** (no duplicate line)

### GenderMaster.java (entity; conflict: sweep vs BR-171)
- GET_GENDER_LIST: org filter FIRST in WHERE — **1**; GET_GENDER_BY_ID: `id=:id AND organizationId=:orgId` — **1**;
  GET_ACTIVE_GENDER_LIST: `status='A' AND organizationId=:orgId` — **1**
- total `organizationId=:orgId` constant-pool occurrences: **6** (3 new + 3 pre-existing by-name/not-id paths)

### JwtTokenFilter.java (BR-171 only — expected clean, verify anyway)
- `"/adt/getActiveGenderList"` occurrences: **0** (removed from BOTH whitelist blocks)

### PriorityMaster.java (Global twin; conflict: batch vs BR-170, only if it fires)
- `unitId=:unitId` occurrences: **5** (batch's 4 scoped queries + BR-170's GET_PRIORITY_LIST); GET_PRIORITY_LIST org+unit — **1**

### PriorityMaster.java (ADT twin; BR-170 only)
- `unitId=:unitId`: **1** (GET_PRIORITY_LIST); `unitId` field + accessors: **1**

## Post-merge proof (constraint 3 — in order)

1. `git diff deploy/composed-all main -- legacy/` → **must be empty**. If not: STOP, report what was lost.
2. `python3 tools/verify_deployed_regressions.py` → **RESULT: PASS** (every closed BR).
3. Rebuild from merged main (`mvn -q package -DskipTests -o -pl Entity,Global,ADT,Billing,LIS,HIS-WAR -am`),
   redeploy, restart, then run the regression checker AGAIN → **PASS**. This proves the jars are
   reproducible from main alone — the property the session lacked (sweep fixes existed only as dirty
   worktree state / unmerged branch).
4. (Optional confirmation of nothing-lost at file level: the six-spec harness re-run.)

## Not in main after these merges (expected, correct)
BR-162/163/164 (no fixes exist), BR-165 (scheduler, report-only), BR-168/169 (queued, not fixed),
geo-cache class + cache track (census-only). Pushing main (ahead 15 of origin) is a separate owner decision.

---

## BR-173 merge — fix/br173-default-deny (authorization pending, added 2026-08-05 21:45 BST)

Branch state at merge request:
- Diff vs main = ONE application file: `legacy/HIS-WAR/src/main/java/com/param/filter/JwtTokenFilter.java`
  (+174/-430). This session's governance docs (QA_FINDINGS QF-2026-08-05-AC, SESSION_LOG entry,
  this section, DECISIONS_QUEUE.md) ride the branch per the CLAUDE.md docs exception (no
  Maker-Checker needed; they document the verification, not application behavior).
- Merge mechanics: `git merge --no-ff fix/br173-default-deny`. Zero conflict candidates (single file,
  main has not touched JwtTokenFilter since the 45-entry removal merge 0e31ef06; verify with a
  dry-run `--no-commit --no-ff` + abort per owner constraint 1 before resolving anything).
- Post-merge proof (same order as constraint 3): regression checker on the current deployment (PASS
  already confirmed 2026-08-05 against the branch filter) → rebuild from merged main → redeploy →
  second regression run → six harness specs + four clinical live checks (token-gated, see
  DECISIONS_QUEUE.md #2) → push.
- OPEN CAVEAT for the owner's approval: QF-2026-08-05-AC / DECISIONS_QUEUE.md #1 — the
  /HIS-WAR/webjars/ prefix covers a tree that does not exist in the ROOT-context dev deployment
  (real webjar tree /webjars/** is 401, swagger-ui does not load; identical pre-rewrite, not a
  regression). Approve the merge with the finding open, or bundle a decision on the prefix list.
