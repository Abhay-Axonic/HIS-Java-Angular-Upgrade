# Decision Needed: 5 remaining Tier-3 duplicate-check defects from the systemic sweep — each independently confirmed against real source, none fixed, none of them affects only cosmetic behavior

**Prepared:** 2026-07-25, per explicit owner instruction to investigate and write up the 5 findings still outstanding from `docs/ANGULAR_PENDING_FINDINGS.md`'s `PENDING-systemic-duplicate-check-sweep` entry, after `VisitTypeMaster`, `NurseAllocationMaster`, and `NursingStationWardMapper` were already fast-tracked, approved, and applied on their own branches. No application code has been touched to produce this document — investigation and proposal only, per standing project discipline (`CLAUDE.md`).

**Format note:** this is one consolidated document covering all 5 findings, rather than 5 separate files. All 5 are Tier-3, none of them are on fire the way `VisitTypeMaster`'s 100%-guaranteed-failure was, and reviewing them together — same pattern family, same module clusters, same proposed fix shape (restore/add exactly what the code's own structure already shows was intended) — is faster to approve or reject in one sitting than opening 5 separate files. Each finding below has its own numbered 1–5 sections (bug, why unintentional, downstream impact, fix options) and is called out individually in the final combined "what we need from you" section, so nothing is lost by combining them.

**Headline, across all 5 — every one of these is a genuine, source-confirmed defect, not a misread of the code.** Two (`AutoclaveMaster`, `WrapperExpiryMaster`) have literally no reachable duplicate-check at all on create or update. One (`KitMaster`) has a fully correct, dual-field check that the create/update flow simply never calls. One (`OperationTheatreType`) has a check that validates the wrong field, leaving the field it's named after completely unvalidated. Two (`CorporateStatementChecklistMaster`, `BillingBedCategoryMaster`) validate only one of two fields the UI presents as identifying. One (`RmoAllocationMaster`) has a create-time check that is architecturally incapable of ever running — it throws an exception on every single invocation, which is silently swallowed, so every create is treated as "no duplicate found" regardless of what's actually in the table. The dev database currently holds zero rows in every one of these tables, so nothing below is empirically reproduced against live data with a real duplicate row — every citation below is a direct read of the actual, unmodified source (entity, DAO, service, and — where relevant — the live frontend controller/template).

---

## Finding 1 — `OperationTheatreType` (OTIMS): the query used as "check duplicate Name" never looks at Name

### 1. The bug, confirmed exactly

The named query the create and update flows actually use — `legacy/Entity/src/main/java/com/param/otims/model/OperationTheatreType.java:43-47`, verified verbatim:

```java
@NamedQuery(name="GET_OPERATION_THEATRE_TYPE_BY_NAME",
        query=" SELECT "
                + " operationTheatreType.operationTheatreTypeId AS operationTheatreTypeId, operationTheatreType.theatreTypeDesc AS theatreTypeDesc, operationTheatreType.theatreTypeName AS theatreTypeName"
                + " FROM OperationTheatreType operationTheatreType WHERE operationTheatreType.orgId= :orgId AND operationTheatreType.unitId= :unitId  AND LOWER(operationTheatreType.theatreTypeDesc)= :theatreTypeDesc  "
                ),
```

Despite the query's own name (`..._BY_NAME`) and the DAO method's name (`getOperationTheatreTypeByName`), the `WHERE` clause filters exclusively on `theatreTypeDesc` — `theatreTypeName` is only ever selected/returned, never appears in the `WHERE` clause of this query or its update-time sibling, `GET_OPERATION_THEATRE_TYPE_BY_NAME_NOT_ID` (`:49-54`, same shape, same field). The DAO confirms this is the only parameter actually bound (`legacy/Global/src/main/java/com/param/global/otims/dao/OperationTheatreTypeDaoImpl.java`). The service wires these as the actual pre-save gate (`legacy/Global/src/main/java/com/param/global/otims/service/OperationTheatreTypeServiceImpl.java`).

The UI (`legacy/HIS-WAR/src/main/webapp/views/master/GlobalMaster/OTIMS/operation-theatre-type.html`) presents Code, Name, and Description as three separate required fields — this is a genuinely multi-field record, not a case where Name and Description are the same underlying concept under two labels.

### 2. Why this looks like a real, unintentional defect

- The query and method are both named "by name," strongly suggesting the field being checked was meant to be `theatreTypeName` — the code checks `theatreTypeDesc` instead, which reads as a pasted-in query built from a similar sibling screen's shape.
- `theatreTypeName` is a real column, collected by the frontend, submitted on every create and update, and never referenced by any duplicate-check query anywhere in this entity.
- `theatreTypeCode` is also never checked by any duplicate-check query in this entity — so this screen is arguably validating only 1 of its 3 identifying fields.

### 3. What this does and doesn't affect

- **Real downstream consumption of `theatreTypeName`, confirmed by grep across the whole codebase:** rendered in operation-theatre-type dropdowns and lists across multiple OT-reservation and procedure-template views/controllers, and persisted directly into Billing's own schedule table (`ProcedureOrderDaoImpl` writes `theatreTypeName` into `otims.t_procedure_schedule.ot_type` via raw SQL).
- **Practical risk:** two OT Types with identical Names but different Descriptions would currently be accepted without complaint, producing two visually-identical entries in every OT-reservation/procedure-template dropdown — a scheduler choosing between two identically-labeled entries has no reliable way to tell them apart, risking a procedure being scheduled against the wrong OT type.
- **Not a referential-integrity break** — each row still has its own unique id; the risk is entirely in a human picking the wrong visually-identical row.

### 4. Proposed fix (not implemented — for review)

Add `theatreTypeName` to both queries' `WHERE` clauses, and bind the corresponding parameter in the DAO.

- **Option A (narrow):** add `AND LOWER(operationTheatreType.theatreTypeName)=:theatreTypeName` alongside the existing Desc check in both queries, so a genuine duplicate is rejected if *either* Name or Desc collides.
- **Option B:** rename the existing query/method to reflect what it actually does (`..._BY_DESC`) and add a second, new, genuinely name-scoped query/method, called from the same save/update gate.

Either option is application code (`legacy/`), so per `CLAUDE.md` it needs its own dedicated branch (Maker-Checker), verified live before/after (duplicate Name with different Desc correctly rejected; duplicate Desc with different Name still correctly rejected; a genuinely new Name+Desc pair still succeeds).

---

## Finding 2 — `AutoclaveMaster` and `WrapperExpiryMaster` (CSSD): no duplicate-check is reachable in either screen's create/update flow, for two different underlying reasons

### 1. The bug, confirmed exactly

**`AutoclaveMaster` — no duplicate-check query exists at all, anywhere.** Its entity (`legacy/Entity/src/main/java/com/param/CSSDMaster/model/AutoclaveMaster.java`) defines exactly two named queries — both plain listing queries, neither filtered by `machine_name` (the screen's one identifying field). `createAutoclave`/`updateAutoclave` (`legacy/CSSDMaster/src/main/java/com/param/CSSDMaster/dao/AutoclaveMasterDaoImpl.java`) go straight from the DTO to `save`/`session.update` with zero prior existence check. The service layer (`AutoclaveMasterServiceImpl.java`) just forwards to those DAO calls directly.

**`WrapperExpiryMaster` — a query exists, but it's dead code, never invoked by anything reachable.** `GET_ACTIVE_WRAPPER_EXPIRY_LIST_BY_CODE` (`legacy/Entity/src/main/java/com/param/CSSDMaster/model/WrapperExpiryMaster.java`) is itself misleadingly named — despite "BY_CODE," its `WHERE` clause filters `lower(wrapperExpiryMaster.name) LIKE :code`, i.e. a fuzzy, `LIKE`-based match on `name`, never `code`. The one DAO method that runs it is exposed via a real endpoint — but a codebase-wide grep for that constant and method name found zero call sites anywhere in the frontend JS. It is wired end-to-end on the backend but never actually called by anything a user can trigger. Create/update go straight to `save`/`update` with no pre-check of any kind.

**Frontend corroboration on both screens, independently:** both controllers contain hardcoded "already exists" error text on the else-branch of their save/update calls (`autoClaveMasterController.js`, `wrapperExpiryMasterController.js`). These messages can never actually be triggered by a genuine duplicate rejection, because the backend performs no such rejection.

### 2. Why this looks like a real, unintentional defect

- Every sibling CSSD screen investigated in this sweep (`KitMaster`, below) does have a real, correctly-shaped duplicate-check query — `AutoclaveMaster`/`WrapperExpiryMaster` are the outliers with none reachable.
- The frontend's hardcoded "already exists" messages on both screens are the clearest signal of intent: someone built the UI to handle a rejection response that the backend was supposed to send and never actually does.
- `WrapperExpiryMaster`'s one candidate query is both mis-named (`_BY_CODE` filtering on `name`) and structurally wrong for a duplicate-check (a `LIKE` search, not an exact match) — reads like a half-finished search-autocomplete feature abandoned before the frontend ever called it.

### 3. What this does and doesn't affect

- **Real downstream consumption, confirmed by entity-reference grep:** `AutoclaveMaster` is referenced by `AutoclaveLog`/`LoadBatch` — a specific autoclave machine record is what a sterilization batch log is tied to. `WrapperExpiryMaster` is referenced by `LoadBatch`/`PreSterilizationProcess` — the wrapper's expiry duration feeds into how a sterilized pack's expiry is tracked. Both are genuine CSSD sterilization-tracking inputs, not decorative lookups.
- **Not a referential-integrity break** — the risk is entirely in a human picking the wrong row from a list, a genuine CSSD operational/infection-control traceability risk (which physical machine/wrapper was actually used for a given sterilization load), not merely cosmetic.
- **No live data currently exists to demonstrate this concretely** — both tables are empty in the dev DB.

### 4. Proposed fix (not implemented — for review)

**`AutoclaveMaster`:** add a new named query scoped by org+unit+`machine_name` (case-insensitive, matching this module's existing convention), an update-time "not by id" sibling, and call both from `createMaster`/`updateMaster` before the existing save/update call — the same shape `KitMaster`'s own query already uses as a working reference.

**`WrapperExpiryMaster`:** two sub-options —
- **Option A:** build a proper exact-match, org/unit-scoped duplicate-check query on `name` (and/or `code`, per Finding 4's same question), wire it into create/update, leave the existing dead query alone or remove it.
- **Option B:** if the dead query was meant to become a live autocomplete/search feature, that's a separate, larger product decision outside this document's scope — flagged for awareness, not proposed here.

Both are application code (`legacy/`), each needing its own dedicated branch (Maker-Checker), verified live before/after.

---

## Finding 3 — `KitMaster` (CSSD): a real, correct, dual-field duplicate-check exists and works — but `createMaster`/`updateMaster` never call it

### 1. The bug, confirmed exactly

The query itself is genuinely well-built: `SEARCH_KIT_LIST_BY_KIT_NAME_AND_CODE` (`legacy/Entity/src/main/java/com/param/CSSDMaster/model/KitMaster.java`) — exact-match (not `LIKE`), both `name` and `code` checked together, correctly org/unit-scoped. One of the better-built duplicate-checks found across the whole sweep, on its own terms.

**But `KitMasterServiceImpl.createMaster`/`.updateMaster` never call it** — confirmed directly: `getKitByNameAndCode` (the DAO method that runs this query) does not appear anywhere in `KitMasterServiceImpl.java`. `createMaster`/`updateMaster` go straight to `iMasterDao.createKit`/`updateKit`.

**The only caller of this query anywhere in the codebase** (confirmed via grep for `getKitByNameAndCode` across every `.java` file) is `PreSterilizationProcessServiceImpl.saveInstrumentAsKit` — an entirely different workflow module. It uses the query as a **get-or-create lookup**, not a rejection gate: if a matching kit exists, it reuses that kit's ID; if not, it silently creates a brand-new `KitMaster` row (calling the same unguarded DAO method `KitMaster`'s own screen uses) and proceeds. It never rejects anything.

**Frontend corroboration, same pattern as Finding 2:** `kitCreationController.js` has hardcoded "Kit already exits...!!" / "Updated Kit already exits...!!" messages that can never actually fire from this screen's own create/update calls.

### 2. Why this looks like a real, unintentional defect

- The query is too well-built (dual-field, exact-match, org/unit-scoped) to have been written for anything other than a duplicate-check. It reads as a query written for `KitMaster`'s own screen, then never actually wired into that screen's save path, while a *different* module later found it and repurposed it for its own get-or-create need.
- The frontend's hardcoded "already exists" messages on both create and update are the clearest signal: the UI was built to handle a server-side rejection that never actually happens.

### 3. What this does and doesn't affect

- **Real downstream consumption of `KitMaster` rows:** `KitInstrumentMapper` (equipment composition of a kit) and the pre-sterilization workflow both operate on `kitId` — a genuine CSSD instrument-kit definition used in real sterilization/inspection workflows.
- **Practical risk:** two kits with identical Name+Code accepted via the admin screen would each get their own independently-configured instrument list — a user selecting from a picker with two identically-labeled entries has no way to know which one has the equipment list they actually mean, a genuine CSSD/OTIMS operational risk (wrong instrument set logged against a sterilization cycle), not merely cosmetic.
- **No data-integrity break in the traditional sense** — each duplicate still gets its own valid, unique id; the harm is entirely in a human being unable to distinguish two identically-labeled rows.
- **No live data currently exists to demonstrate this concretely** — the table is empty in the dev DB.

### 4. Proposed fix (not implemented — for review)

Call the existing `getKitByNameAndCode` DAO method (already correct, already used successfully elsewhere) from `KitMasterServiceImpl.createMaster` and `.updateMaster`, before the existing `iMasterDao.createKit`/`updateKit` calls, rejecting with the standard `ALREADY_EXIST` response on a match. For `updateMaster`, the existing query would need a "not by id" variant added (it currently has none, since its only caller today never needed to exclude the record being edited) — a small, mechanical addition following the same pattern as every other master's `..._NOT_BY_ID` sibling query in this codebase.

This is application code (`legacy/`), needing its own dedicated branch (Maker-Checker), verified live before/after (duplicate name+code correctly rejected on create; correctly rejected on update, excluding the record being edited; a genuinely new kit still saves normally; the unrelated `PreSterilizationProcessServiceImpl.saveInstrumentAsKit` get-or-create path continues to work unchanged, since it calls the same DAO method that isn't being altered, only newly *also* called from `KitMaster`'s own screen).

---

## Finding 4 — `CorporateStatementChecklistMaster` and `BillingBedCategoryMaster` (ADT unit): both check Description, neither checks Code, and both present Code as a separately required, identifying field

### 1. The bug, confirmed exactly

**`CorporateStatementChecklistMaster`:** its by-name and by-name-not-by-id duplicate-check queries (`legacy/Entity/src/main/java/com/param/adt/master/unit/model/CorporateStatementChecklistMaster.java`) both filter only on `LOWER(cscm.checkListDesc)` — `checkListCode` never appears in either query. These gate `CorporateStatementChecklistMasterServiceImpl.saveCorporateStatementChecklistMaster`/`.updateCorporateStatementChecklistMaster`.

**`BillingBedCategoryMaster`:** same shape exactly — both duplicate-check queries (`legacy/Entity/src/main/java/com/param/adt/master/unit/model/BillingBedCategoryMaster.java`) filter only on `LOWER(bbc.billingBedDesc)` — `billingBedCode` never appears in either.

**Both screens present Code and Description as two separate, independently required fields**, confirmed directly in the templates (both fields `required="required"`, both echoed in each screen's list table). Both frontend controllers collect and submit both fields on every create/update.

### 2. Why this looks like a real, unintentional defect

- Both screens are structurally identical to each other (same module, same naming convention, same query shape) — one shared underlying pattern across two entities, strongly suggesting whatever query template these were built from simply never included a Code check for either.
- Presenting Code as a separate, required, independently-displayed field is a strong signal it's meant to be a real identifying value a user chooses deliberately.
- Neither screen's Code field is checked by any query in its entity, active or commented-out — unlike Finding 5 below, this isn't a case of a disabled clause showing prior intent; it looks like Code was simply never included when the Desc-only check was originally written, on both entities alike.

### 3. What this does and doesn't affect

**A downstream-consumer trace was run for both:**

- Grepped the entire codebase for any read of `billingBedCode`/`checkListCode` outside each entity's own CRUD module — found none driving any automated decision elsewhere.
- **`BillingBedCategoryMaster` is a heavily-referenced FK target** — `BedMaster`, `AdmissionDetails`, `Transfer`, `RoomMaster`, `MPackageMaster`, `MCompanyContractMaster`, `TariffBillingBedCategoryMapper`, `SurgeryResourceConfig` all join to it by numeric id, not by Code or Description — so a duplicate Description doesn't corrupt any join directly. The practical risk is a human one: staff configuring Room/Bed/Tariff mappings pick a "Billing Bed Category" from a list rendered by Description (and Code) — an ambiguous duplicate risks a real billing-tier misconfiguration (the wrong tariff or room mapped to what the user thought was a specific bed category), adjacent to this project's Priority #1 (billing accuracy).
- **`CorporateStatementChecklistMaster`** is a narrower-blast-radius screen (feeds a corporate/TPA billing-statement checklist mapper) — same "human picks the wrong visually-identical row" risk, smaller downstream footprint.
- **No live data currently exists to demonstrate this concretely** — both tables are empty/inaccessible in the dev DB in this session.

### 4. Proposed fix (not implemented — for review)

For both entities, independently (they may not need the same answer): add `checkListCode`/`billingBedCode` to the existing duplicate-check queries' `WHERE` clauses, alongside the existing Desc check:

- **Option A (narrow, matches this project's "restore/add exactly what the structure shows" style):** reject if *either* Code or Desc collides.
- **Option B:** reject only if *both* Code and Desc collide together (mirroring `KitMaster`'s own correctly-built dual-field query as the in-codebase precedent).

Both are application code (`legacy/`), each needing its own dedicated branch (Maker-Checker) if approved, verified live before/after.

---

## Finding 5 — `RmoAllocationMaster` (ADT unit): the create-time duplicate-check throws on every single call, is silently swallowed, and is therefore incapable of ever rejecting anything

### 1. The bug, confirmed exactly

The CREATE-time duplicate-check gate, `RMOAllocationServiceImpl.saveRmoAllocationMaster`:

```java
public Response saveRmoAllocationMaster(RmoAllocationDto rmoAllocationDto) throws ApplicationException {
    try {
        Response res=iRMOAllocationDao.getRmoAllocationByRmoDoctorId(rmoAllocationDto);
        if(res.getStatus().equals("success")){
            return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
        }else{
        return iRMOAllocationDao.saveRmoAllocationMaster(rmoAllocationDto);
        }
        } catch (Exception e) {
        }
    return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
```

`getRmoAllocationByRmoDoctorId` (`legacy/ADT/src/main/java/com/param/adt/master/unit/dao/RMOAllocatioinDaoImpl.java:40-63`, verified verbatim) runs the named query and binds **four** parameters, including `visitTypeId`:

```java
RmoAllocationDto dto=(RmoAllocationDto)sessionFactory.getCurrentSession()
        .getNamedQuery("GET_RMO_ALLOCATION_MASTER_LIST_BY_RMODOCTOR_ID")
        .setParameter("rmoDoctorId", rmoAllocationDto.getRmoDoctorId())
        .setParameter("orgId", rmoAllocationDto.getOrganizationId())
        .setParameter("unitId", rmoAllocationDto.getUnitId())
        .setParameter("visitTypeId", rmoAllocationDto.getVisitTypeId())
        ...
```

But the query's own text (`legacy/Entity/src/main/java/com/param/adt/master/unit/model/RmoAllocationMaster.java:135-147`, verified verbatim) has its `visitTypeId` clause **commented out**:

```java
@NamedQuery(name="GET_RMO_ALLOCATION_MASTER_LIST_BY_RMODOCTOR_ID",
query="SELECT rmoAllocationMaster.rmoAllocationId AS rmoAllocationId,"
        + " rmoAllocationMaster.visitTypeId AS visitTypeId,"
        + " rmoAllocationMaster.specialityId AS specialityId,"
        + " rmoAllocationMaster.rmoDoctorId AS rmoDoctorId,"
        + " rmoAllocationMaster.wardId AS wardId,"
        + " rmoAllocationMaster.status AS status "
        + " FROM RmoAllocationMaster rmoAllocationMaster "
        + " WHERE rmoAllocationMaster.organizationId=:orgId"
        + " AND rmoAllocationMaster.unitId=:unitId "
        + " AND rmoAllocationMaster.rmoDoctorId=:rmoDoctorId "
        + " AND rmoAllocationMaster.status='A' "
        /*+ " AND rmoAllocationMaster.visitTypeId=:visitTypeId "*/),
```

Calling `.setParameter("visitTypeId", ...)` against a Hibernate named query whose text contains no `:visitTypeId` placeholder throws at runtime on every single invocation — this is caught by the DAO's own blanket, empty `catch (HibernateException he) {} catch (Exception e) {}`, silently swallowed, and the method falls through to `return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null)`. Back in the service, `res.getStatus().equals("success")` is therefore always false, so the `else` branch — proceeding straight to save — runs on every single create, unconditionally, regardless of whether a genuine duplicate exists.

**Corroborating evidence this is a stale/orphaned parameter binding, not a coincidence:** the exact same named query is called successfully elsewhere in this same DAO file — `getActiveRmoAllocationMasterByRMODoctorId` — **without** binding `visitTypeId`, and works fine, because it only binds the three parameters the query text actually declares.

### 2. Why this looks like a real, unintentional defect

- The commented-out `AND rmoAllocationMaster.visitTypeId=:visitTypeId` clause is direct evidence someone intended this check to be scoped by visit-type at some point — it was disabled, and the DAO's parameter binding for it was never cleaned up to match, leaving an orphaned `.setParameter` call that guarantees a crash.
- The blanket, empty `catch (HibernateException he) {} catch (Exception e) {}` is exactly the kind of silent error-swallowing `CLAUDE.md` explicitly calls out as this project's #1 problem to fix — this is the specific mechanism that hid this bug until source was read directly.
- The update-time sibling check does *not* have this defect: `GET_RMO_ALLOCATION_MASTER_BY_NAME_NOT_BY_ID` has an **active, uncommented** `visitTypeId` clause, and its DAO method binds exactly the parameters the query text declares — no mismatch. **This asymmetry between create and update is itself evidence the create-time query's clause being commented out was an accident**, not an intended design.

### 3. What this does and doesn't affect

- **The create-time duplicate-check is architecturally incapable of ever succeeding** — every single create silently bypasses it, 100% of the time, regardless of what's already in the table.
- **Real downstream consumption, confirmed:** the legacy login controller calls `getActiveRmoAllocationMasterByRMODoctorId` at login time — an RMO (Resident Medical Officer) doctor's ward/speciality/visit-type allocation genuinely drives real clinical routing at login, not a decorative admin-only lookup.
- **Explicit parallel to `NurseAllocationMaster`, and where it diverges:** both are ADT-unit-module allocation screens with a `visitTypeId` scoping clause commented out of a duplicate-check query — same module, same evident original intent. But the failure mode is the mirror image: `NurseAllocationMaster`'s DAO binding was *also* commented out, consistently, so its check ran successfully but too coarsely (over-blocking, already fixed). `RmoAllocationMaster`'s DAO binding was left *active* against a query missing that clause — inconsistently disabled — so its check crashes and is swallowed on every call, under-protecting, never rejecting anything.
- **Update is not affected by this specific mechanism** — this document does not claim update's check has this same defect. Whether update's check is scoped correctly (e.g., whether it should also consider ward/speciality, the same open question left unresolved for `NurseAllocationMaster`) is a separate question this document does not attempt to resolve.
- **No live data currently exists to demonstrate this concretely** — the table is empty in the dev DB.

### 4. Proposed fix (not implemented — for review)

Uncomment the `AND rmoAllocationMaster.visitTypeId=:visitTypeId` clause in `GET_RMO_ALLOCATION_MASTER_LIST_BY_RMODOCTOR_ID` so it matches the DAO's binding, which is already correct and already active — no new business rule invented.

**This one query is shared by two call sites** (the duplicate-check, and the unrelated login-time lookup, which does *not* bind `visitTypeId` today) — uncommenting the clause without also binding a value at that second call site would break it. Two ways to reconcile, your call:
- **Option A:** uncomment the clause, and add a `visitTypeId` binding to the login-time call site too — but that changes login's own current "all of this doctor's allocations, any visit-type" behavior to "only this specific visit-type," a second, separate behavioral question this document does not assume the answer to.
- **Option B (narrower, recommended as the minimal fix consistent with this project's style):** leave the shared query alone, give the duplicate-check its own new, separately-named query (a straightforward copy with the clause uncommented) — fixing only the create-time duplicate-check's own defect without touching login-time behavior at all, the same "don't invent new scope, don't touch what isn't broken" approach used for the `NurseAllocationMaster` fix.

Either way, this is application code (`legacy/`), needing its own dedicated branch (Maker-Checker) if approved, verified live before/after (create an RMO allocation for a doctor+visit-type+ward; attempt an identical second create for the same doctor+visit-type — correctly rejected; a second, genuinely different visit-type for the same doctor — correctly succeeds) — and, separately, confirming the login-time lookup's behavior is unchanged if Option B is chosen.

---

## What we need from you

Each finding is independent — approving or rejecting one does not block or imply anything about the others.

1. **Finding 1 (`OperationTheatreType`):** approve adding a `theatreTypeName` check to the existing duplicate-check queries (Option A: check both Name and Desc; Option B: keep them as two separately-named/scoped checks)? Or reject/defer?
2. **Finding 2 (`AutoclaveMaster` / `WrapperExpiryMaster`):** approve adding a genuine duplicate-check to `AutoclaveMaster`'s create/update flow (new query, following the `KitMaster` pattern), and separately approve/choose an option for `WrapperExpiryMaster`? Or reject/defer either or both?
3. **Finding 3 (`KitMaster`):** approve wiring the existing, already-correct `SEARCH_KIT_LIST_BY_KIT_NAME_AND_CODE` query into `createMaster`/`updateMaster` (plus a new `..._NOT_BY_ID` sibling for update)? Or reject/defer?
4. **Finding 4 (`CorporateStatementChecklistMaster` / `BillingBedCategoryMaster`):** approve adding a Code check to one, both, or neither — and if approved, Option A (either field triggers rejection) or Option B (both fields must match)? These two may reasonably get different answers given `BillingBedCategoryMaster`'s materially larger downstream FK footprint.
5. **Finding 5 (`RmoAllocationMaster`):** approve restoring the commented-out `visitTypeId` clause for the create-time check specifically — and if so, Option A (also fix the shared login-time query's scope) or Option B (a new, separately-scoped query touching only the duplicate-check, leaving login's current behavior untouched)?

No code has been touched to produce this document. All 5 findings remain logged under `docs/ANGULAR_PENDING_FINDINGS.md`'s `PENDING-systemic-duplicate-check-sweep` entry; this document proposes the specific fix for each, per your instruction, following the same investigation-then-proposal discipline as the 3 findings already fast-tracked from the same sweep.
