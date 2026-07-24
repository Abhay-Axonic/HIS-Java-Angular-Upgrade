# Decision Needed: `currencyMaster`'s UPDATE flow silently drops the Country field every time, in the current legacy backend

**Prepared:** 2026-07-24, escalated per this project's clinical/billing-tier addendum discipline (same class of finding as batch 15's `identificationMaster` CREATE-flow flag reset, `docs/DECISION_NEEDED_IdentificationMasterCreateFlagReset.md`) rather than treated as a routine logged-and-faithfully-reproduced quirk. Found during batch 16's Step 0/1 audit of the `currencyMaster` screen. No application code has been touched to produce this document — proposal only.

**Headline — editing an existing currency record and changing its associated Country, then clicking Update, silently has NO effect on the persisted row's country. The user sees a normal success toast and a "refreshed" list, but the currency's country in the database never changes.** The CREATE flow does not have this defect — a currency's country is set correctly when first created. This has apparently been the behavior since this screen was built; it is not something the rebuild introduces.

---

## 1. The bug, confirmed exactly

`CurrencyMasterDaoImpl.updateCurrencyMaster` (`legacy/Global/src/main/java/com/param/global/billing/dao/CurrencyMasterDaoImpl.java:120-145`):

```java
CurrencyMaster currencyMaster = findById(currencyMasterDto.getCurrencyId());
currencyMaster.setCurrencyName(currencyMasterDto.getCurrencyName());
currencyMaster.setCurrencyCode(currencyMasterDto.getCurrencyCode());
currencyMaster.setOrgnisationId(currencyMasterDto.getOrgnisationId());
//currencyMaster.setUnitId(currencyMasterDto.getUnitId()); //dilip
//currencyMaster.setCreatedBy(currencyMasterDto.getCreatedBy()); //dilip
currencyMaster.setUpdatedBy(currencyMasterDto.getUpdatedBy());
//currencyMaster.setCreatedDate(...); //dilip
currencyMaster.setUpdatedDate(...);
update(currencyMaster);
```

There is no `currencyMaster.setCountryId(currencyMasterDto.getCountryId())` call anywhere in this method — `countryId` is simply never read off the incoming DTO. Since `findById` returns the entity with whatever `country_id` is already persisted, and nothing subsequently touches that field, the row's country is left completely untouched by every update, regardless of what the DTO carried.

This is **not** a case of the frontend simply never sending the value. The frontend's `updateCurrency()` (`legacy/HIS-WAR/.../controller/master/global/billing/currencyMasterController.js:368-381`) builds its real POST payload as:

```javascript
var data = {
    currencyName : $scope.currencyMaster.currencyName,
    currencyCode : $scope.currencyMaster.currencyCode,
    currencyId : $scope.currencyMaster.currencyId,
    countryId : $scope.currencyMaster.countryId,
    updatedBy : $scope.userId,
    updatedDate : $scope.currentDate,
    orgnisationId : $scope.orgnisationId
}
```

`countryId` is right there, on the wire, every time. And the Country `<select>` (`currency-master.html:16-24`) is the exact same, fully-editable dropdown in both create and edit mode — there is no "Country is locked once created" affordance or disabled state anywhere in the view. A user editing a record can, and reasonably would, assume changing the Country and clicking Update actually changes the Country.

The **create** path, `CurrencyMasterDaoImpl.saveCurrencyMaster` (same file, lines 37-62), does call `currencyMaster.setCountryId(currencyMasterDto.getCountryId())` (line 44) — so a currency's country is set correctly at creation time; the defect is confined entirely to the update path.

## 2. Why this looks like a real, unintentional defect

- Every other field the update form sends and the view lets the user edit (`currencyName`, `currencyCode`) IS correctly applied in `updateCurrencyMaster`. Only `countryId` — despite being present in the DTO, present in the payload, and present as an editable form field — is skipped. There's no comment, no conditional, nothing suggesting a deliberate "country is immutable after creation" business rule; it reads as a simple missed field when this method was written or last touched (the `//dilip` comments nearby suggest this method has been edited piecemeal over time, commenting out `unitId`/`createdBy`/`createdDate` deliberately while never adding `countryId` at all).
- The bug is invisible from the frontend/template alone. Nothing in the Angular controller or view hints at it — the response is a normal `{status: "success"}`, the same success growl fires, and `initCurrencyMasterList()` reloads the list afterward (unconditionally, on both success and failure — see the controller's `updateCurrency()` lines 409-413). A user has no way to notice their country change didn't take, short of specifically re-opening that same record for edit again and checking.
- This is exactly the kind of thing Steps 0-5 (frontend read + interaction testing alone) cannot catch — it was only found by reading the DAO layer directly during this batch's audit, matching this project's Clinical/Billing-Tier Addendum's explicit distinction between "matches legacy" and "legacy is actually correct."

## 3. What this does and doesn't affect

- **Currency-to-country association is used for display (country name/code shown alongside each currency row, per the `GET_ALL_CURRENCY_MASTER_LIST`/`GET_CURRENCY_MASTER_BY_ID` named queries' `INNER JOIN` to `CountryMaster`) and potentially by any other billing screen that looks up a currency's country** (this document does not trace every downstream consumer of `m_currency_master.country_id` — flagging the mechanism, not a specific downstream billing-calculation claim).
- **Not itself a monetary calculation** — this bug doesn't change a currency's code, name, symbol, or status; it only means the country tag stays stale after an attempted correction.
- **No data is lost that wasn't already stale** — the original country value stays exactly as it was; nothing gets corrupted or nulled. The harm is a silent failure-to-correct, not data destruction.
- Given `currencyMaster` is a foundational lookup table for a **billing-tier** application per this project's addendum, an operator who thinks they've corrected a misconfigured currency's country (e.g., after initial data-entry error) and has not, could have that stale association reappear anywhere else in the app that reads a currency's country going forward — worth the owner's attention even though the immediate blast radius looks narrow.

## 4. Options (not implemented — for review)

1. **Fix in legacy**: add `currencyMaster.setCountryId(currencyMasterDto.getCountryId());` to `CurrencyMasterDaoImpl.updateCurrencyMaster`, letting country changes on update persist like every other editable field already does. This is application code (`legacy/`), not local-dev-DB-only, so per `CLAUDE.md` it would need its own dedicated branch (Maker-Checker), separate from the Angular rebuild.
2. **Leave legacy as-is; rebuild faithfully reproduces the current (broken) behavior** — the Angular update form would send `countryId` on the wire (matching the real request shape) but the persisted country would never actually change, exactly mirroring today's silent failure. This is what the rebuild currently does while awaiting this decision (see `new/src/app/features/currency-master/currency-master.models.ts`'s `CurrencyUpdatePayload` doc comment and `currency-master.ts`'s class doc comment) — chosen as the safe default specifically so as not to silently drift from legacy's real behavior without your sign-off, not because it's assumed to be the right long-term answer.
3. **Fix only the rebuild, leave legacy broken** — not recommended and inconsistent with this project's usual parity discipline (the rebuild is supposed to match legacy's *current* behavior unless explicitly instructed otherwise), but noted as a hypothetical option in case the owner would rather the new implementation simply behave correctly regardless of legacy's own fix timeline, treating this explicitly as a disclosed, approved improvement rather than silent drift.

## 5. What we need from you

1. **Should legacy itself be fixed?** If yes, this is a separate legacy-code fix track (its own branch, per Maker-Checker), independent of whether/how the Angular rebuild proceeds.
2. **Which behavior should the Angular rebuild of `currencyMaster` match** — faithfully reproduce the current update-flow silent failure-to-persist-country (option 2, what it does today), or implement the corrected behavior now as a disclosed, approved improvement (option 3) regardless of legacy's own fix timeline?
3. **Does this need a downstream-consumer trace** before deciding severity — i.e., should someone check what else in Billing/Global reads a currency's `country_id` and whether a stale value there causes any real downstream miscalculation or mis-display? Not done as part of this document; flagging as an open question rather than assuming either way.

No code has been touched to produce this document. Batch 16's Step 0/1 audit findings for `currencyMaster` are otherwise logged normally alongside this batch's other screens; this specific finding is called out here instead of folded into a routine log, matching the precedent set for `identificationMaster` in batch 15. Awaiting your direction before this screen's Update flow is considered final.
