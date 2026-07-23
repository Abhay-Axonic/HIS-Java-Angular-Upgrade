# Decision Needed: `identificationMaster`'s CREATE flow silently discards 3 user-set flags every time, in the current legacy backend

**Prepared:** 2026-07-23, escalated per the owner's explicit direction during batch 15 (this project's first clinical/billing-tier rebuild batch) rather than treated as a routine logged-and-faithfully-reproduced finding. Found during batch 15's Step 0/1 audit of the `identificationMaster` screen. No application code has been touched to produce this document — proposal only.

**Headline — creating a new Identification Card Master record silently ignores whatever the user checked for "Expiry Required" / "Numerical" / "Alphanumeric" and always persists all three as `N`, regardless of the form.** The only way any of these three flags ever takes effect on a real record is to immediately edit it afterward and save again — the update path does not have this defect. This has apparently been the behavior since this screen was built; it is not something the rebuild introduces.

---

## 1. The bug, confirmed exactly

`IdentificationMasterDaoImpl.addIdentificationMaster` (`legacy/ADT/src/main/java/com/param/adt/master/global/dao/IdentificationMasterDaoImpl.java:63-80`):

```java
IdentificationMaster identificationMaster = mapper.map(identificationMasterDto, IdentificationMaster.class,"IdentificationMasterDto_to_IdentificationMaster");
identificationMaster.setCreatedDate(TimeZoneComponent.getDateByzone(0));
identificationMaster.setUpdatedDate(TimeZoneComponent.getDateByzone(0));
identificationMaster.setStatus('A');
identificationMaster.setIsExpiryRequired('N');
identificationMaster.setIsAlphanumeric('N');
identificationMaster.setIsNumerical('N');
identificationMaster = save(identificationMaster);
```

The Dozer mapping used here (`legacy/HIS-WAR/src/main/resources/ADT/dozer-bean-mappings.xml:66-72`, map-id `IdentificationMasterDto_to_IdentificationMaster`) is a plain field-name-matching class-to-class mapping with no exclusions — so it correctly maps whatever the user checked in the form onto the new `IdentificationMaster` object. The three explicit setter calls immediately after then **unconditionally overwrite all three back to `'N'`**, discarding the just-mapped real values. `definedLength` is not one of the overridden fields, so that value does persist correctly on create — only the three boolean-style flags are affected.

The **update** path, `IdentificationMasterDaoImpl.updateIdentificationMaster` (same file, lines 123-148), has no equivalent override — it applies the DTO's real values as-is. So a record created with, say, "Numerical" checked will show `isNumerical = N` in the database immediately after creation; if a user then opens that same record for edit and clicks Update without changing anything, it will correctly save as `isNumerical = Y`.

## 2. Why this looks like a real, unintentional defect

- The override values (`'N'`/`'N'`/`'N'`) exactly match the DTO's own default/unset state — this has the shape of leftover debug/scaffolding code (e.g. someone testing "always create inactive-flag records" during initial development) that was never removed, rather than a deliberate business rule. There is no comment, no conditional, and no corresponding frontend messaging telling the user their checkbox selections on create will be ignored.
- The frontend (`identificationMasterController.js`) does real client-side validation on these three fields at create time (the mutually-exclusive Numerical/Alphanumeric check, the length-required-if-`isLength` check) — validation that would be pointless if the values were always going to be discarded server-side. This strongly suggests the frontend was built assuming the values would actually persist.
- The bug is invisible from the frontend/template alone — nothing in the Angular controller or view hints at it. It was only found by reading the DAO layer directly during this batch's audit, which is exactly the kind of thing Steps 0-5 (frontend read + interaction testing) cannot catch on their own, and part of why this project's new Clinical/Billing-Tier Addendum treats "matches legacy" and "legacy is actually correct" as two different questions.

## 3. What this does and doesn't affect

- **Not itself a billing calculation or a direct clinical-safety action** — these three flags configure format-validation metadata for an identification-document type (e.g., "Passport numbers must be alphanumeric, 8 characters, no expiry"), used elsewhere to *validate* how a patient's ID document number is entered. A wrong flag doesn't itself misapply a charge or administer a medication.
- **It does mean the format-validation rules a hospital configures for an ID type silently don't take effect until someone happens to re-save the record** — which could plausibly let non-conforming ID numbers be entered/accepted elsewhere in ADT for a real identification type, for however long the record goes un-re-edited. This project has not traced every downstream consumer of these three flags to confirm the practical severity of that gap — flagging the mechanism, not claiming a specific downstream harm.
- **No patient data or existing records are affected retroactively** — this only affects what gets written at the moment of create.

## 4. Options (not implemented — for review)

1. **Fix in legacy**: remove the three `setIsExpiryRequired('N')`/`setIsAlphanumeric('N')`/`setIsNumerical('N')` overrides from `addIdentificationMaster`, letting the Dozer-mapped real values persist on create (matching how update already behaves). This is application code (`legacy/`), not local-dev-DB-only, so per `CLAUDE.md` it would need its own dedicated branch (Maker-Checker), separate from the Angular rebuild.
2. **Leave legacy as-is; rebuild faithfully reproduces the current (broken) behavior** — the Angular create form would need to either also silently discard these three flags after "saving" (matching the real observable behavior a user would see if they created a record and reloaded it), which feels like actively porting a bug forward, or the rebuild could show the correct values in the UI immediately after create (since the response objects the frontend receives are whatever it just submitted, not necessarily a fresh re-fetch — worth checking) while the underlying persisted data is still wrong — this needs your call on which observable behavior is the one to match.
3. **Fix only the rebuild, leave legacy broken** — not recommended and inconsistent with this project's usual parity discipline (the rebuild is supposed to match legacy's *current* behavior unless explicitly instructed otherwise), but noted as a hypothetical option since the three flags might matter enough downstream that the owner would rather the new implementation simply behave correctly regardless of what legacy does, treating this explicitly as a disclosed, approved improvement rather than silent drift.

## 5. What we need from you

1. **Should legacy itself be fixed?** If yes, this is a separate legacy-code fix track (its own branch, per Maker-Checker), independent of whether/how the Angular rebuild proceeds.
2. **Which behavior should the Angular rebuild of `identificationMaster` match** — faithfully reproduce the current create-flow data loss (option 2), or implement the corrected behavior now as a disclosed, approved improvement (option 3) regardless of legacy's own fix timeline?
3. **Does this need a downstream-consumer trace** before deciding severity — i.e., should someone check what actually happens elsewhere in ADT when an identification type's format flags are wrong (blocks a real validation, or is genuinely inert everywhere it's checked)? Not done as part of this document; flagging as an open question rather than assuming either way.

No code has been touched to produce this document. Batch 15's Step 0/1 audit findings for `identificationMaster` are otherwise logged normally in `docs/ANGULAR_PENDING_FINDINGS.md`; this specific finding is called out here instead of folded into that routine log, per your direction. Awaiting your direction before Step 2+ proceeds on this screen.
