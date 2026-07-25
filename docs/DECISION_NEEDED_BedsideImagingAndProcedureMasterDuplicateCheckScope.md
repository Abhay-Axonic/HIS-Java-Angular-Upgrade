# Decision Needed: `BedsideImagingMaster` and `BedsideProcedureMaster`'s duplicate-check queries have zero organization/unit scope — one org's entry can block every other org from using the same wording, in the current legacy backend

**Prepared:** 2026-07-25, investigated per explicit owner direction as a targeted follow-up to `PENDING-systemic-duplicate-check-sweep` (`docs/ANGULAR_PENDING_FINDINGS.md`), which flagged both `BedsideImagingMaster` and `BedsideProcedureMaster` as having a missing-org/unit-scope duplicate-check defect but did not carry per-screen detail into the consolidated entry. Both screens were independently re-read in full directly against source for this document (entity, DAO, service, controller, and the shared frontend controller), not relayed from the earlier sweep's summary. No application code has been touched to produce this document — proposal only.

**Headline — both screens' duplicate-check queries filter on the entry's description alone, with no `organizationId`/`unitId` predicate anywhere in the query text or its parameter bindings, even though each screen's own List/Active queries correctly scope by both.** Because the check spans the *entire* table with no narrowing at all, its practical effect is not a duplicate silently slipping through — a same-organization, same-unit duplicate would already be caught as a subset of "anywhere in the table." Its practical effect is the opposite: **an administrator in one organization/unit can be wrongly blocked from creating (or editing) a bedside imaging or bedside procedure type merely because some *other* organization or unit already has an identically-worded entry** — a false-positive, over-blocking defect, the same class flagged for `NurseAllocationMaster` earlier in the same sweep. This has apparently been the behavior since both screens were written; no Angular rebuild of either screen exists yet.

**One consolidated document, not two:** both screens live in the same Java module (`com.param.global`), share the same three-tab AngularJS controller, and turn out to have the byte-identical defect shape — same missing predicates, same DAO binding gap, same downstream-consumer trace result (none found).

---

## 1. The bug, confirmed exactly

### `BedsideImagingMaster`

Service layer (`legacy/Global/src/main/java/com/param/global/service/BedsideImagingMasterServiceImpl.java`): `saveBedsideImagingMaster` calls `getBedsideImagingMasterByName`; `updateBedsideImagingMaster` calls `getBedsideImagingMasterByNameNotId` — both as pre-save duplicate gates.

DAO layer (`legacy/Global/src/main/java/com/param/global/dao/BedsideImagingMasterDaoImpl.java`) binds only `bedsideImagingDesc` (and, for update, the row's own id) — no `organizationId`/`unitId` parameter anywhere.

The named queries themselves confirm there's no such predicate to bind — `legacy/Entity/src/main/java/com/param/global/model/BedsideImagingMaster.java:49-60`, verified verbatim:

```java
@NamedQuery(name="GET_BEDSIDE_IMAGING_MASTER_BY_NAME",
	query="SELECT bedsideImagingMaster.bedsideImagingId AS bedsideImagingId "
				+ "	FROM BedsideImagingMaster bedsideImagingMaster"
				+ " WHERE LOWER(bedsideImagingMaster.bedsideImagingDesc)= :bedsideImagingDesc"
				),

@NamedQuery(name="GET_BEDSIDE_IMAGING_MASTER_BY_NAME_NOT_ID",
	query="SELECT bedsideImagingMaster.bedsideImagingId AS bedsideImagingId"
				+ " FROM BedsideImagingMaster bedsideImagingMaster"
				+ " WHERE LOWER(bedsideImagingMaster.bedsideImagingDesc)= :bedsideImagingDesc"
				+ " AND bedsideImagingMaster.bedsideImagingId != :bedsideImagingId"
				),
```

Both queries filter the *entire* `m_bedside_imaging_master` table by description alone — no organization, no unit, no status filter of any kind.

**Contrast with this same entity's List/Active queries, confirmed verbatim, which prove the module already knows how to scope by org+unit** (`:19-32`, `:34-...`): `GET_BEDSIDE_IMAGING_MASTER_LIST` and `GET_ACTIVE_BEDSIDE_IMAGING_MASTER` both select and filter on `organizationId`/`unitId`, correctly bound in the DAO. Only the two duplicate-check queries lack this.

### `BedsideProcedureMaster` — the identical shape, independently confirmed

Service layer (`legacy/Global/src/main/java/com/param/global/service/BedsideProcedureMasterServiceImpl.java`): `saveBedsideProcedureMaster`/`updateBedsideProcedureMaster` call `getBedsideProcedureMasterByName`/`getBedsideProcedureMasterByNameNotId` the same way.

Named queries (`legacy/Entity/src/main/java/com/param/global/model/BedsideProcedureMaster.java:47-58`, verified verbatim):

```java
@NamedQuery(name="GET_BEDSIDE_PROCEDURE_MASTER_BY_NAME",
	query="SELECT bedsideProcedureMaster.bedsideProcedureId AS bedsideProcedureId "
				+ "	FROM BedsideProcedureMaster bedsideProcedureMaster"
				+ " WHERE LOWER(bedsideProcedureMaster.bedsideProcedureDesc)= :bedsideProcedureDesc"
				),

@NamedQuery(name="GET_BEDSIDE_PROCEDURE_MASTER_BY_NAME_NOT_ID",
	query="SELECT bedsideProcedureMaster.bedsideProcedureId AS bedsideProcedureId"
				+ " FROM BedsideProcedureMaster bedsideProcedureMaster"
				+ " WHERE LOWER(bedsideProcedureMaster.bedsideProcedureDesc)= :bedsideProcedureDesc"
				+ " AND bedsideProcedureMaster.bedsideProcedureId != :bedsideProcedureId"
				),
```

Again, no `organizationId`/`unitId` predicate, while this entity's own `GET_BEDSIDE_PROCEDURE_MASTER_LIST`/`GET_ACTIVE_BEDSIDE_PROCEDURE_MASTER` both correctly filter on `organizationId`/`unitId`, correctly bound in the DAO.

**Both screens independently checked, not assumed identical — they turn out to be identical:** field names differ (`bedsideImagingDesc` vs. `bedsideProcedureDesc`, plus `subSpecialityId` on the imaging side and `isAnaesthesia`/`duration` on the procedure side), but the missing-predicate shape, the missing DAO bindings, and the working List/Active queries proving the module *could* scope correctly but didn't for these two queries, are the same on both screens, line-for-line.

**Also confirmed, so as not to overstate the finding:** the check is missing *both* org and unit scope simultaneously — there's no partial scoping to compare against. Also confirmed: neither duplicate-check considers the entry's Code field at all (only Desc) — a separate, narrower completeness gap (an unchecked field, not a scope gap), not addressed by the fix proposed below.

## 2. Why this looks like a real, unintentional defect

- Each entity file places the correctly org+unit-scoped List/Active queries directly alongside the unscoped duplicate-check queries, in the same `@NamedQueries` block, in the obviously same hand (identical formatting, identical string-concatenation style). This isn't a case of the author never having learned to scope by org/unit — the same file demonstrates it, immediately above and below the two queries that don't.
- The pattern matches this project's already-documented "missing-org-scope" defect class found repeatedly across 33 of 38 sampled pure-lookup-master screens project-wide (`docs/ANGULAR_PENDING_FINDINGS.md`, `PENDING-systemic-duplicate-check-sweep`) — the single most common defect shape this sweep uncovered, and these two screens fit it exactly.
- There is no plausible intentional reading of "a bedside imaging/procedure type name must be globally unique across every hospital organization and unit" for a screen whose own List/Active queries treat each org+unit's catalog as its own independent, unit-scoped list. A duplicate-check *stricter* than the screen's own browsing scope is an internal inconsistency, not a deliberate cross-tenant uniqueness rule.
- Both screens are configured through the same shared AngularJS controller (Imaging tab and Procedures tab), built by the same author in the same file at the same time — a very plausible explanation for why the identical gap shows up in both.

## 3. What this does and doesn't affect

**A real downstream-consumer trace was run, not assumed.** Searched the entire Java codebase for any reference to `bedsideImagingId`/`BedsideImagingMaster`/`bedsideProcedureId`/`BedsideProcedureMaster` outside each screen's own CRUD stack (entity, DTO, DAO, service, controller) — **found none.** `SubSpecialityMaster.java` also references `BedsideImagingMaster`, but only as the inverse side of the entity's own declared join relation (used by that screen's own List query), not an independent consumer. No other Java module, service, DAO, or controller reads either lookup's id or fields to drive any other automated decision.

Searched the frontend the same way — the only files referencing either screen's fields are the shared controller and its own view ("Ward Bedside Investigations Master," feature id 947). One superficially similar term, `currentBedSideEncounterOrderLists` in the ER "Patient Orders Workbench," was checked and ruled out — generic point-of-care terminology, no reference to either entity/table, no shared code path.

**Conclusion: as of today, nothing else in this codebase automatically reads or depends on either lookup's data or ids.** These two screens currently populate only their own list view — they don't yet feed a clinical ordering workflow, despite that reading like their evident eventual purpose. This does not rule out something outside the Java/Angular layers this trace covers, but no evidence of one was found.

**Dev-database check — not currently live-reproducible with existing data, but the SQL-level defect is unconditional:** `m_bedside_imaging_master` and `m_bedside_procedure_master` both have zero rows in the dev database; `m_organization_master` and `m_unit_master` each currently have exactly one row (org 1/unit 1 only). **Practical consequence: today, in this dev database, there is no second org or unit for a same-worded entry to collide with, and no existing rows to collide with in the first place** — the false-positive cannot currently be demonstrated live against real dev data, because the multi-tenant scenario the bug depends on doesn't yet exist in this environment. The bug is 100% real at the SQL level, independent of any data, but is not yet an observed, reproduced defect against live dev data — it would require seeding a second organization/unit to demonstrate directly, which was not done here (investigation-only, no data seeded, no code touched).

The moment a second organization or unit is provisioned and both attempt to configure a bedside imaging/procedure catalog — an ordinary scenario for a multi-tenant hospital system, plausibly using overlapping standard wording ("Chest X-Ray," "Portable X-Ray," "Wound Dressing," "Suturing") — this defect becomes immediately, deterministically live.

**No data-integrity risk in the false-negative direction:** because the check is over-inclusive rather than under-inclusive, a genuine in-scope duplicate (same org, same unit, same wording) is still caught. This is purely a false-positive/over-blocking risk, and no wrong data is ever written as a result of this specific defect.

## 4. Proposed fix (not implemented — for review)

**Minimal, narrowly-scoped change — mirror the org+unit scoping each screen's own List/Active queries already use, applied to the two duplicate-check queries and their DAO bindings.**

For `BedsideImagingMaster`:

```java
@NamedQuery(name="GET_BEDSIDE_IMAGING_MASTER_BY_NAME",
	query="SELECT bedsideImagingMaster.bedsideImagingId AS bedsideImagingId "
				+ "	FROM BedsideImagingMaster bedsideImagingMaster"
				+ " WHERE LOWER(bedsideImagingMaster.bedsideImagingDesc)= :bedsideImagingDesc"
				+ " AND bedsideImagingMaster.organizationId= :organizationId "   // <-- added
				+ " AND bedsideImagingMaster.unitId= :unitId"                    // <-- added
				),
```
(and the matching addition to `GET_BEDSIDE_IMAGING_MASTER_BY_NAME_NOT_ID`), with `.setParameter("organizationId", ...)`/`.setParameter("unitId", ...)` added in the corresponding DAO methods — the DTO already carries and populates both fields on every Create/Update call (confirmed in the shared frontend controller, which already sends `organizationId`/`unitId` on every request).

The identical change, field names substituted, applies to `BedsideProcedureMaster.java` and `BedsideProcedureMasterDaoImpl.java`.

This is not new logic — it's copying the exact `organizationId`/`unitId` scoping clause each entity's own List/Active queries already contain, into the two queries missing it. No new fields, no schema change, no frontend change required.

This is application code (`legacy/`), so per `CLAUDE.md` it would need its own dedicated branch (Maker-Checker), verified before/after — at minimum, seeding a second organization/unit in the dev database to demonstrate: before the fix, Org B's Create of a same-worded entry as Org A's is wrongly rejected; after the fix, it succeeds, while a true same-org/same-unit duplicate is still correctly rejected.

**Alternative not recommended:** leaving both screens as-is. Since no downstream consumer currently depends on either lookup's data (§3), there is no risk of "fixing" something another module secretly relies on behaving the current way.

## 5. What we need from you

1. **Approve the fix above (both screens, same shape) for a dedicated legacy branch?** Four query-text edits + four DAO parameter-binding additions total (two screens × two methods each), verified live against a seeded second org/unit, held for your explicit merge approval.
2. **Given the dev database currently has only one organization and one unit, and both tables are empty**, this is a real, unconditional SQL-level defect but not yet a live-observed one in this environment. Does that change how urgently you want this addressed relative to `NurseAllocationMaster`'s already-fixed, already-reproduced finding — or fix it now regardless, since it will become live-reproducible the moment this system has more than one organization/unit?
3. **Should the separately-noted gap — the duplicate-check ignoring each screen's Code field entirely — be scoped into the same fix pass, or logged and decided separately?** Not proposed here as part of this document's fix (different defect shape), flagging it so it isn't lost.

No code has been touched to produce this document. This finding refines and supersedes the brief mention of `BedsideImagingMaster`/`BedsideProcedureMaster` implied by the aggregate count in `docs/ANGULAR_PENDING_FINDINGS.md`'s `PENDING-systemic-duplicate-check-sweep` entry.
