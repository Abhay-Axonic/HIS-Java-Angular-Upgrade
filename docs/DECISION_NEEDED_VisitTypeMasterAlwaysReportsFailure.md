# Decision Needed: `VisitTypeMaster`'s CREATE and UPDATE flows report failure to the user 100% of the time, regardless of what actually happened, in the current legacy backend

**Prepared:** 2026-07-25, fast-tracked per explicit owner direction — "the clearest, most confirmed issue in the whole [duplicate-check] sweep" — same process as `identificationMaster`/`currencyMaster`. Found during the systemic duplicate-check sweep, then independently confirmed directly against source by the orchestrating session (both methods read in full, not relayed). No application code has been touched to produce this document — proposal only.

**Headline — creating or updating a Visit Type always returns an error response to the user, even when the operation genuinely succeeded and the row was correctly persisted.** There is no code path in either method that can ever report success. This has apparently been the behavior since this method was written; it is not something the rebuild introduces (no Angular rebuild of this screen exists yet).

---

## 1. The bug, confirmed exactly

`VisitTypeMasterServiceImpl.saveVisitType` (`legacy/Global/src/main/java/com/param/global/service/VisitTypeMasterServiceImpl.java:118-135`):

```java
@Override
public Response saveVisitType(VisitTypeMasterDto visitTypeMasterDto) throws ApplicationException {
    Response res = iVisitTypeMasterDao.getVisitTypeByName(visitTypeMasterDto);
    try {
        if (res.getListObject().size() > 0 && res.getListObject() != null) {
            return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
        } else {
            Response saveRes = iVisitTypeMasterDao.saveVisitType(visitTypeMasterDto);
            if (saveRes.getStatus().equals(SUCCESS)) {
                vistiTypeMasterCache.invalidateAll();
            }
        }
    } catch (Exception e) {
        logger.error("saveVisitType failed", e);
    }
    return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
```

`updateVisitType` (`:89-105`) has the byte-identical shape with `updateRes` in place of `saveRes`.

`saveRes` (the DAO's real, correct result — the actual insert genuinely happens via `iVisitTypeMasterDao.saveVisitType(...)`) is obtained and used **only** to conditionally invalidate a cache. It is never returned, never checked for `SUCCESS` to drive the method's own return value. Both branches of the `if/else` fall through to the same unconditional `return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null)` at the very end of the method — the one and only `return` statement reachable after a genuine attempt to save. **There is no code path in either method that can ever report success to the caller.** The early-return for a genuine duplicate-name rejection (the `if` branch) is correctly shaped on its own terms — that specific path already returns the right thing — but a user has no way to tell that path apart from the guaranteed-false-failure path, since both surface as the same generic error class to the frontend.

## 2. Why this looks like a real, unintentional defect

- Sibling screens in the same codebase (e.g. `hospitalAccountMaster`, `transactionTypeMaster`) follow the identical structural pattern — obtain a duplicate-check result, branch on it, then call the DAO's save/update and **return that call's own result** on the non-duplicate path. `VisitTypeMaster`'s methods do everything the same way except the final `return` — they compute `saveRes`/`updateRes`, use it for a cache-invalidation side effect, and then discard it. This reads as a straightforward missed `return saveRes;` (respectively `updateRes`) at the end of the `else` block, not a deliberate "always report an error" design — there is no comment, no conditional, nothing suggesting intent.
- The DAO methods being called (`iVisitTypeMasterDao.saveVisitType`/`.updateVisitType`) are real, complete implementations that genuinely insert/update the row — this isn't a case where the underlying persistence is itself broken and the error is arguably "honest." The persistence works; only the reporting of it is wrong.
- The bug is total and unconditional — not a rare edge case, not something that depends on data shape or timing. It fires on literally every call, which is itself a signal this was never noticed in isolation (a partial/conditional bug can hide; this one cannot) and instead has likely been worked around by end users in practice (assuming this screen sees real use) — e.g., learning to ignore the error toast and reload the list to check whether the change actually took.

## 3. What this does and doesn't affect

- **Not a data-integrity bug** — the actual row is written correctly; no wrong data is ever persisted because of this specific defect. The harm is entirely in what the user is told about it.
- **Real, likely-active operational cost:** a user who sees "failure" after a successful save has no reliable way to know their change went through without manually reloading the list and checking. Two plausible behavioral consequences, neither confirmed with usage data but both mechanically real: (a) the user re-attempts the identical save, which correctly gets rejected the second time by the (separately logged, and separately scoped-too-loosely) duplicate-check, so no duplicate row results from an identical retry within the same organization; (b) a user who assumes the failure was data-related might make a small unintended edit before retrying (e.g., tweak the name slightly "in case that was the problem"), which could produce an unintended near-duplicate or a Visit Type the user didn't actually mean to create as worded.
- **Separate, already-logged issue this interacts with but is not the same defect:** `VisitTypeMaster`'s own duplicate-check query has no organization scope at all (`docs/ANGULAR_PENDING_FINDINGS.md`, `PENDING-systemic-duplicate-check-sweep`) — mentioned here only because a user's retry-after-false-failure behavior is the one place these two bugs' effects would actually interact; fixing this defect does not fix that one, and vice versa.
- **No Angular rebuild of `VisitTypeMaster` exists yet** — this is a pure legacy-backend finding, discovered incidentally during the sweep, not during a rebuild attempt on this specific screen.

## 4. Proposed fix (not implemented — for review)

**Minimal, narrowly-scoped change — return the DAO's own result instead of discarding it:**

```java
// saveVisitType
} else {
    Response saveRes = iVisitTypeMasterDao.saveVisitType(visitTypeMasterDto);
    if (saveRes.getStatus().equals(SUCCESS)) {
        vistiTypeMasterCache.invalidateAll();
    }
    return saveRes;   // <-- added
}

// updateVisitType
} else {
    Response updateRes = iVisitTypeMasterDao.updateVisitType(visitTypeMasterDto);
    if (updateRes.getStatus().equals(SUCCESS)) {
        vistiTypeMasterCache.invalidateAll();
    }
    return updateRes;   // <-- added
}
```

This is the same shape every correctly-behaving sibling screen already uses — no new logic invented, just closing the gap between "compute the real result" and "tell the caller about it." This is application code (`legacy/`), so per `CLAUDE.md` it would need its own dedicated branch (Maker-Checker), separate from any future Angular rebuild of this screen, verified before/after (a real create and a real update, each confirmed to now return `status: "success"` with the row genuinely persisted, same as it already is today) and held for explicit approval before merging — consistent with this session's other legacy fixes.

No alternative options are offered here (unlike the `identificationMaster`/`currencyMaster`/`hospitalAccountMaster` precedents) because there is no faithful-reproduction question to weigh — the current behavior is not a business rule anyone could plausibly want to keep, and there is no rebuild yet to decide a target behavior for.

## 5. What we need from you

1. **Approve the fix above for a dedicated legacy branch?** If yes, it will be proposed as a Maker-Checker diff (this exact 2-line change), verified live (real create + real update, confirmed success is now reported correctly), and held for your explicit merge approval — no different from this session's other legacy fixes.
2. Nothing else is blocked on this decision — there is no rebuild of this screen in flight to hold pending your answer.

No code has been touched to produce this document. This finding is logged in `docs/ANGULAR_PENDING_FINDINGS.md` (`PENDING-visittypemaster-always-reports-failure`) as its own item, per your instruction, separate from the duplicate-check sweep entry it was found alongside.
