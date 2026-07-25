# Decision Needed: `hospitalAccountMaster`'s UPDATE flow silently wipes the org+unit's real default-account flag every time, in the current legacy backend

**Prepared:** 2026-07-25, escalated per this project's clinical/billing-tier addendum discipline (same class of finding as batch 15's `identificationMaster` CREATE-flow flag reset and batch 16's `currencyMaster` UPDATE-flow country drop — `docs/DECISION_NEEDED_IdentificationMasterCreateFlagReset.md`, `docs/DECISION_NEEDED_CurrencyMasterUpdateCountryIdDropped.md`) rather than treated as a routine logged-and-faithfully-reproduced finding. Found while building batch 18's `hospitalAccountMaster` rebuild, then independently verified directly against source, then live-confirmed on real, unmodified legacy. No application code has been touched to produce this document — proposal only.

**Headline — editing ANY hospital account for ANY reason (even just fixing a typo in its Description, unrelated to which account is "default") silently resets whichever OTHER account in that org+unit was marked default back to "not default," with no error, no warning, and no visible signal of any kind.** The CREATE flow does not have this defect — a new account's default status is applied correctly and only affects other accounts when the new one is itself being marked default. This has apparently been the behavior since this screen was built; it is not something the rebuild introduces.

---

## 1. The bug, confirmed exactly

`HospitalAccountMasterServiceImpl.updateHospitalAccountMaster` (`legacy/Billing/src/main/java/com/param/billing/global/service/HospitalAccountMasterServiceImpl.java:82-101`):

```java
public Response updateHospitalAccountMaster(HospitalAccountMasterDto hospitalAccountMasterDto) {
    Response response = iHospitalAccountMasterDao.getHospitalAccountMasterByNameNotId(hospitalAccountMasterDto);
    if (response.getListObject().size() > 0 && response.getListObject() != null) {
        return new Response(ERROR, null, ALREADY_EXIST, null, null);
    } else {
        iHospitalAccountMasterDao.updateAllHospitalAccount(hospitalAccountMasterDto);
        Response updateRes = iHospitalAccountMasterDao.updateHospitalAccountMaster(hospitalAccountMasterDto);
        ...
```

`updateAllHospitalAccount` (`HospitalAccountMasterDaoImpl.java:213-232`):

```java
public void updateAllHospitalAccount(HospitalAccountMasterDto hospitalAccountMasterDto) {
    sessionFactory.getCurrentSession().createQuery(
        "update HospitalAccountMaster set isDefault='N' " +
        "WHERE orgnizationId=:orgId and unitId=:unitId")
      .setParameter("orgId", hospitalAccountMasterDto.getOrgnizationId())
      .setParameter("unitId", hospitalAccountMasterDto.getUnitId())
      .executeUpdate();
}
```

This runs **unconditionally on every single call to `updateHospitalAccountMaster`**, for **every account in the org+unit**, regardless of the submitted record's own `isDefault` value — immediately before the specific edited record's own fields (including its own `isDefault`) are applied via `iHospitalAccountMasterDao.updateHospitalAccountMaster(hospitalAccountMasterDto)` (the actual per-record field update).

Contrast with **Create** (`HospitalAccountMasterServiceImpl.java:32-57`):

```java
public Response saveHospitalAccountMaster(HospitalAccountMasterDto hospitalAccountMasterDto) {
    Response response = iHospitalAccountMasterDao.getHospitalAccountMasterByName(hospitalAccountMasterDto);
    if (response.getListObject().size() > 0 && response.getListObject() != null) {
        return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
    } else {
        if (hospitalAccountMasterDto.getIsDefault() == 'Y') {
            iHospitalAccountMasterDao.updateAllHospitalAccount(hospitalAccountMasterDto);
        }
        Response saveRes = iHospitalAccountMasterDao.saveHospitalAccountMaster(hospitalAccountMasterDto);
        ...
```

Create correctly gates the exact same bulk-reset call behind `if (hospitalAccountMasterDto.getIsDefault() == 'Y')` — the reset only fires when the NEW record is itself being made the default, which is the only case where resetting every other account actually makes sense. **Update has no equivalent gate at all** — it calls the identical bulk-reset unconditionally, whether the edited record's `isDefault` is `'Y'`, `'N'`, or anything else.

**Live-confirmed this session** (real authenticated session, real unmodified legacy, both this and the Angular rebuild against the same database): set account `HA1`'s `isDefault` to `Y` via a real Update — confirmed `HA1.isDefault = "Y"` in the list. Then edited a completely unrelated account, `HA2`, changing only its Description, via a real Update — confirmed immediately after: `HA2`'s description updated correctly, **and `HA1`'s `isDefault` silently reverted to `"N"`**, with no error, no warning, no toast indicating anything unusual. This is not a source-level inference; it is a reproduced, observed defect on real data.

## 2. Why this looks like a real, unintentional defect

- The reset-then-apply sequencing on Update is structurally identical to Create's — same DAO method (`updateAllHospitalAccount`) called from the same position in the flow — except Create remembers to gate it on the submitted record's own `isDefault`, and Update simply doesn't. This reads as a straightforward missed condition when the Update method was written (likely copy-pasted from Create's shape without carrying over the `if` check), not a deliberate "every edit re-evaluates the whole org+unit's default" business rule — there is no comment, no conditional, nothing suggesting intent.
- If the unconditional reset on Update were actually intentional, the natural companion behavior would be for the edited record's own submitted `isDefault` to then correctly re-establish a default when the user intends it — and it does, for that ONE record. But the reset itself doesn't check whether the user was even touching the default-status field at all; it fires identically whether the edit form's `isDefault` checkbox was touched or not, on every single Update, for every field change, including ones with nothing to do with "default" (Code, Description).
- The bug is invisible from the frontend/template alone — nothing in the Angular controller or view hints at it. The response is a normal `{status: "success"}`, the same success growl fires (`response.data.message`), and the list simply reloads afterward with the now-silently-changed data. A user has no way to notice unless they specifically know to check every other account's default status after an unrelated edit.
- This is exactly the kind of thing Steps 0-5 (frontend read + interaction testing alone) cannot catch on their own — it was only found by reading the DAO/service layer directly while implementing the rebuild's Update flow faithfully, then confirmed live by deliberately watching for it. Matches this project's Clinical/Billing-Tier Addendum's explicit distinction between "matches legacy" and "legacy is actually correct."

## 3. What this does and doesn't affect

**A real downstream-consumer trace was run before writing this document** (not skipped, unlike the more open questions left in the `identificationMaster`/`currencyMaster` precedents):

- Searched the entire Java codebase for any read of `HospitalAccountMaster.isDefault` outside its own CRUD module (entity, DTO, DAO, service, controller) — **found none.** No other Java module, service, or DAO reads this flag to make any automated decision (e.g., auto-selecting a default account for a payment posting).
- Searched the entire frontend for any live use of an `isDefault == 'Y'` pattern tied to a hospital account auto-selection — **found 3 instances of a superficially similar pattern** (`billListController.js:912`, `cancelledBillListController.js:768`, `outsourcedServiceWorkOrderController.js:1396`), but all 3 are: (a) **entirely commented out** (dead code, not live), and (b) operate on a **different entity** — `getHospitalBankList()`'s own `/api/billing/hospitalBank/{orgId}/{unitId}` endpoint, served by `BankMasterController.java`, not `HospitalAccountMasterController.java`. These are not consumers of the flag this bug affects, even if they were live.
- **Conclusion: as far as this codebase's own logic is concerned today, `HospitalAccountMaster.isDefault` currently drives no automated downstream behavior anywhere.** It is displayed (the "Is Default Account" column on this screen's own table, and echoed back in the create/update payloads) but not consumed elsewhere to auto-select, auto-populate, or gate anything. This does not rule out a report, view, or stored procedure outside the Java/Angular application layers that this trace didn't cover, but no evidence of one was found.
- **What it does affect:** the flag is a real, admin-configured piece of business metadata ("which hospital account is the default one for this org+unit") that a hospital administrator sets deliberately, presumably to use as a reference when reconciling accounts or configuring other systems manually, or in anticipation of future features that would consume it. Its value silently drifting to "no account is default" after routine, unrelated edits means that configuration is not durable — an administrator who set a default weeks ago has no way of knowing it's gone, and would only discover it by manually re-checking.
- **No data is lost that wasn't already a boolean flag** — this doesn't corrupt Code, Description, status, or any monetary field; the harm is confined to the `is_default` boolean silently drifting to `'N'` org+unit-wide.

## 4. Options (not implemented — for review)

1. **Fix in legacy**: gate `updateAllHospitalAccount`'s call inside `updateHospitalAccountMaster` behind the same condition Create already uses — `if (hospitalAccountMasterDto.getIsDefault() == 'Y') { iHospitalAccountMasterDao.updateAllHospitalAccount(hospitalAccountMasterDto); }` — so an edit only resets other accounts' default status when the edited record is itself being made the new default, exactly mirroring Create's already-correct logic. This is application code (`legacy/`), not local-dev-DB-only, so per `CLAUDE.md` it would need its own dedicated branch (Maker-Checker), separate from the Angular rebuild.
2. **Leave legacy as-is; rebuild faithfully reproduces the current (broken) behavior** — the Angular rebuild's Update flow calls the same real, unmodified endpoint, so this is what it currently does while awaiting this decision (see `new/src/app/features/hospital-account-master/hospital-account-master.ts`'s class doc comment). Chosen as the safe default specifically so as not to silently drift from legacy's real behavior without your sign-off, not because it's assumed to be the right long-term answer.
3. **Fix only the rebuild, leave legacy broken** — not recommended and inconsistent with this project's usual parity discipline, but noted as a hypothetical option since, per the downstream trace above, no other system currently depends on this flag's correctness, so a rebuild-only fix would not cause the two apps to disagree on anything else that matters today. Still means the rebuild and legacy would diverge for as long as legacy stays unfixed.

## 5. What we need from you

1. **Should legacy itself be fixed?** If yes, this is a separate legacy-code fix track (its own branch, per Maker-Checker), independent of whether/how the Angular rebuild proceeds. Given the fix is a single, narrow, already-precedented change (mirroring Create's own existing gate exactly), this looks like a low-risk, high-confidence fix if approved.
2. **Which behavior should the Angular rebuild of `hospitalAccountMaster` match** — faithfully reproduce the current update-flow silent default-wipe (option 2, what it does today), or implement the corrected behavior now as a disclosed, approved improvement (option 3) regardless of legacy's own fix timeline?
3. **Given the downstream trace above found no current automated consumer of this flag**, does this change your read of severity relative to the `identificationMaster`/`currencyMaster` precedents (which involved fields with confirmed or plausible downstream reads)? Flagging this honestly rather than either inflating or downplaying it — the defect is 100% real and 100% guaranteed to fire on every edit, but its *current* practical blast radius appears to be confined to this screen's own displayed "Is Default Account" column, not a hidden miscalculation elsewhere.

No code has been touched to produce this document. Batch 18's Step 1/3/5 findings for `hospitalAccountMaster` are otherwise logged normally in `docs/ANGULAR_PENDING_FINDINGS.md`; this specific finding is called out here instead of folded into that routine log, matching the precedent set for `identificationMaster` and `currencyMaster`. Awaiting your direction before this screen's Update flow is considered final.
