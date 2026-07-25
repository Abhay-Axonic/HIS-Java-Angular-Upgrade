**STATUS: APPROVED AND APPLIED (narrow fix only), 2026-07-25 — held on `fix/nurseallocationmaster-visittype-scope`, not merged into `main`, not pushed.** Restored the already-written-but-commented-out `visitTypeId` (and active-only `status='A'`) scope clause in both the create-check and update-check named queries, plus the corresponding DAO parameter binding. Live-verified before/after against real, unmodified legacy: before the fix, allocating the same nurse to a second, genuinely different visit-type was rejected as "Already Exists"; after, it succeeds, and a true duplicate (same user, same visit-type) is still correctly rejected. `mvn -pl Entity,ADT -am test`: 122/122 pass, no regression. **The broader open question — whether ward/speciality/doctor/role should also distinguish two allocations, beyond visitTypeId — remains explicitly undecided, logged separately, not resolved by this fix** (see `docs/ANGULAR_PENDING_FINDINGS.md`, `PENDING-nurseallocationmaster-broader-scope-question`).

---

# Decision Needed: `NurseAllocationMaster`'s duplicate-check rejects any second allocation for the same nurse, blocking a routine real-world action, in the current legacy backend

**Prepared:** 2026-07-25, fast-tracked per explicit owner direction — a Tier 1 "active pain today" finding from the systemic duplicate-check sweep, same process as `identificationMaster`/`currencyMaster`. Found during that sweep, then independently verified directly against source by the orchestrating session (entity, DAO, and service all read in full, not relayed). No application code has been touched to produce this document — proposal only.

**Headline — once a nurse has been allocated to anything at all, allocating that same nurse to a second, genuinely different ward/visit-type/speciality/doctor/role is unconditionally rejected as "already exists."** The check that should distinguish two different allocations for the same nurse only ever looks at *who* the nurse is, never *what* they're being allocated to. If nurses at this hospital routinely cover more than one context (a very ordinary staffing pattern), this screen is likely already blocking that today, not just carrying a latent risk. This has apparently been the behavior since this screen was built; it is not something a rebuild introduces (no Angular rebuild of this screen exists yet).

---

## 1. The bug, confirmed exactly

Entity `legacy/Entity/src/main/java/com/param/adt/master/unit/model/NurseAllocationMaster.java`, the CREATE-time duplicate-check (`:170-186`):

```java
@NamedQuery(name="GET_NURSE_ALLOCATION_MASTER_LIST_BY_USER_ID",
query="SELECT nurseAllocationMaster.nurseAllocationId AS nurseAllocationId, ..."
        + " FROM NurseAllocationMaster nurseAllocationMaster "
        + " WHERE nurseAllocationMaster.organizationId=:orgId"
        + " AND nurseAllocationMaster.unitId=:unitId "
        + " AND nurseAllocationMaster.userId=:userId "),
        //+ " AND nurseAllocationMaster.status='A' "),
    //  + " AND nurseAllocationMaster.visitTypeId=:visitTypeId"),
```

The UPDATE-time check (`:189-204`) is scoped identically narrowly — org + unit + `userId` + `nurseAllocationId != :id` — with no equivalent commented-out clause at all (it was apparently never scoped any wider even as an original intent).

DAO (`legacy/ADT/src/main/java/com/param/adt/master/unit/dao/NurseAllocatioinDaoImpl.java`) confirms neither `visitTypeId` nor `status` is bound to the create-check query (`:37-44`, the `.setParameter("visitTypeId", ...)` line is itself commented out) — the query and the DAO's binding are consistent with each other, this isn't a partial-fix situation.

Service (`legacy/ADT/src/main/java/com/param/adt/master/unit/service/NurseAllocationServiceImpl.java:21-34` for create, `:47-59` for update) confirms both checks are genuinely wired in and gate the real save/update: a match on `userId` alone (regardless of any other field) returns `ALREADY_EXIST` and blocks the request entirely.

The entity itself has 6 other columns that meaningfully distinguish one allocation from another — `visitTypeId`, `wardId`, `nursingStationId`, `specialityId`, `doctorId`, `roleId` — none of which the duplicate-check considers. The frontend (`legacy/HIS-WAR/src/main/webapp/resources/assets/js/controller/master/unit/nurseAllocationMsasterController.js`) collects and submits real, distinct values for all of these (`roleId`, `userId`, `specialityId`, `doctorId`, `wardId`, `nursingStationId`, `visitTypeId` — separate IP/ER/DayCare ward lists, `:83-91,321-332`) — this is a genuinely multi-dimensional allocation record, not a simple code+name lookup.

## 2. Why this looks like a real, unintentional defect

- The commented-out `AND nurseAllocationMaster.visitTypeId=:visitTypeId` line, sitting directly in the query source, is direct evidence someone already intended this check to be scoped by visit-type at some point — it was written, then disabled, and never re-enabled or replaced with a broader intentional design. There's no comment explaining why, and the DAO's binding was correspondingly (consistently) disabled alongside it — this reads as an incomplete edit, not a considered decision to check only `userId`.
- Checking only `userId` for a screen whose entire purpose is to map a nurse into a *specific* ward/visit-type/speciality/doctor/role context doesn't match what the rest of the entity, DTO, and frontend clearly treat as meaningful, distinguishing data. If "one nurse, one allocation, ever" were the real intended business rule, the screen wouldn't need to collect ward/speciality/doctor/role at all for a second allocation attempt — it would simply never let you try.
- This is squarely an **over-blocking** defect, not a silent one — unlike most of this sweep's findings (which let bad data quietly through), this one actively stops a plausible, everyday action the moment anyone attempts it a second time for the same nurse.

## 3. What this does and doesn't affect

- **Real operational impact, not just latent risk:** if nurses at this hospital are ever allocated to more than one ward, visit-type, or speciality context (a common real-world staffing pattern), every attempt to add that second allocation via this screen fails today, with a generic "already exists" message that doesn't explain why — the user has no way to know the rejection is spurious rather than a genuine conflict.
- **Not a data-integrity defect in the traditional sense** — no wrong data gets written; the opposite happens, a *correct*, intended write is blocked.
- **Scope of the proposed fix is a real business question, not purely mechanical:** the commented-out code shows clear intent to scope by `visitTypeId` (and originally `status='A'`, i.e. only compare against *active* allocations) — but whether `visitTypeId` alone is the right full boundary, or whether `wardId`/`specialityId`/`doctorId`/`roleId` should also distinguish two allocations for the same nurse+visit-type, is a nursing-operations question this document does not resolve unilaterally. Proposing the narrowest, most clearly-intended fix below; flagging the wider question for your (or a nursing-ops SME's) confirmation rather than guessing at a business rule beyond what the code's own history already shows.

## 4. Proposed fix (not implemented — for review)

**Minimal fix, restoring the check's own already-written intent — add back `visitTypeId` scoping (and re-enable the active-only filter) to both queries, consistently:**

```java
// GET_NURSE_ALLOCATION_MASTER_LIST_BY_USER_ID (create-check)
+ " WHERE nurseAllocationMaster.organizationId=:orgId"
+ " AND nurseAllocationMaster.unitId=:unitId "
+ " AND nurseAllocationMaster.userId=:userId "
+ " AND nurseAllocationMaster.visitTypeId=:visitTypeId "      // re-enabled
+ " AND nurseAllocationMaster.status='A' "                    // re-enabled

// GET_NURSE_ALLOCATION_MASTER_BY_NAME_NOT_BY_ID (update-check) -- add for consistency, not previously present even as intent
+ " WHERE nurseAllocationMaster.organizationId=:orgId"
+ " AND nurseAllocationMaster.unitId=:unitId "
+ " AND nurseAllocationMaster.userId=:userId "
+ " AND nurseAllocationMaster.visitTypeId=:visitTypeId "      // added
+ " AND nurseAllocationMaster.status='A' "                    // added
+ " AND nurseAllocationMaster.nurseAllocationId!=:nurseAllocationId"
```

plus binding `.setParameter("visitTypeId", nurseAllocationDto.getVisitTypeId())` (re-enabling the already-commented-out line) and `.setParameter` for status if that literal isn't hardcoded in the query text, in both DAO methods.

**Open question for you (or a nursing-ops SME), not resolved by this proposal:** should the check stop at `userId + visitTypeId`, or should it also require matching `wardId`/`specialityId`/`doctorId`/`roleId` before treating two allocations as "the same"? The narrow fix above (restoring exactly what the code already shows was once intended) is the safe, minimal starting point; a broader scope is a straightforward follow-up to the same lines if you determine it's needed, not a separate architectural change.

This is application code (`legacy/`), so per `CLAUDE.md` it would need its own dedicated branch (Maker-Checker), verified before/after (allocate one nurse to two genuinely different visit-types, confirm both succeed; attempt the exact same visit-type twice, confirm the second is still correctly rejected) and held for explicit approval before merging.

## 5. What we need from you

1. **Approve the minimal fix (restore `visitTypeId` + active-only scoping) for a dedicated legacy branch?**
2. **Does the check need to go further than `visitTypeId`** — should ward, speciality, doctor, or role also distinguish two allocations for the same nurse+visit-type? This may need a nursing-ops SME's input, not just a code read.
3. Nothing else is blocked on this decision — there is no rebuild of this screen in flight to hold pending your answer.

No code has been touched to produce this document. This finding remains logged in `docs/ANGULAR_PENDING_FINDINGS.md`'s `PENDING-systemic-duplicate-check-sweep` entry; this document proposes the specific fix for it, per your instruction.
