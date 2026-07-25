**STATUS: APPROVED AND APPLIED (both layers), 2026-07-25 — held on `fix/nursingstationwardmapper-broken-picker-and-check`, not merged into `main`, not pushed.** Layer 1 (picker) fixed exactly as proposed. Layer 2 (save-time check) turned out more involved than originally scoped during implementation: the actual over-blocking bug came from a station-level-only pre-check in the SERVICE layer (`checkIsNursingStationAlreadyAssignedOrNot`, called once per request before the per-ward loop even runs) that structurally could never be made ward-specific, since it only ever receives the whole-request DTO, not per-ward data. Fix: removed that pre-check as a blocking gate entirely, and repurposed the (now correctly ward-scoped) query for the DAO's own per-ward loop instead, which already had the right per-ward logic shape once its own check was fixed. The other, differently-scoped shared check method (used by an unrelated status-update flow) was left completely untouched, out of scope. Live-verified before/after against real, unmodified legacy (2 test wards seeded locally, only 1 existed): before, mapping a station to a second, genuinely different ward was rejected and the picker still offered an already-mapped ward; after, the second ward maps successfully, an exact duplicate is still correctly rejected, and the picker correctly excludes both mapped wards. `mvn -pl Entity,ADT -am test`: 122/122 pass, no regression.

---

# Decision Needed: `NursingStationWardMapper`'s picker never filters already-mapped wards, and its save-time check blocks any second ward mapped to a station, in the current legacy backend

**Prepared:** 2026-07-25, fast-tracked per explicit owner direction — a Tier 1 "active pain today" finding from the systemic duplicate-check sweep, same process as `identificationMaster`/`currencyMaster`. Found during that sweep, then independently verified directly against source by the orchestrating session (entity, DAO, and service all read in full for both layers, not relayed). No application code has been touched to produce this document — proposal only.

**Headline — this screen's own equivalent of `departmentMasterUnit`'s server-filtered picker (a proven, working pattern elsewhere in this codebase) is non-functional here: the ward picker shows every active ward regardless of what's already mapped, and the save-time check rejects any second ward mapped to a nursing station that already has one, even a completely different ward.** Any nursing station that needs more than one ward (a very ordinary configuration — most nursing stations cover multiple wards) hits a wall the moment a second ward is added. This has apparently been the behavior since this screen was built; it is not something a rebuild introduces (no Angular rebuild of this screen exists yet).

---

## 1. The bug, confirmed exactly — two separate, independently broken layers

### Layer 1: the picker never excludes already-mapped wards

`WardMaster.java:121-131` (`GET_WARD_LIST_NOT_MAPPED_TO_NURSING_STATION`, a native SQL query):

```java
@NamedNativeQuery(name="GET_WARD_LIST_NOT_MAPPED_TO_NURSING_STATION",
    query="SELECT wrdMstr.ward_id AS \"wardId\", wrdMstr.ward_code AS \"wardCode\", wrdMstr.ward_name AS \"wardName\" "
        + " FROM adt.m_ward_master wrdMstr "
//      + " WHERE wrdMstr.ward_id NOT IN(SELECT nursingWrdMapper.ward_id FROM adt.t_nursing_station_ward_mapper nursingWrdMapper "
//      + " WHERE nursingWrdMapper.nursing_station_id=:nursingId )"
        + " WHERE wrdMstr.organization_id=:orgId"
        + " AND wrdMstr.unit_id=:unitId "
        + " AND wrdMstr.status='A'"
    ),
```

The entire exclusion subquery — the part that would actually implement "not mapped to nursing station" — is commented out. What remains is just "every active ward in this org/unit," full stop. `WardMasterDaoImpl.java:213-221` confirms the `nursingId` parameter is never bound either (`.setParameter("nursingId", ...)` is itself commented out, consistent with the disabled query text — not a partial fix). The picker the frontend (`nursingStationWardMappingController.js:78-79`) uses to populate "which wards can I map to this station" returns every ward every time, mapped or not.

### Layer 2: the save-time check blocks the wrong thing

`NursingStationWardMapper.java:41-49` (`CHECK_IS_NURSING_STATION_ALREADY_ASSIGNED`, the create-time check):

```java
@NamedQuery(name="CHECK_IS_NURSING_STATION_ALREADY_ASSIGNED",
query="SELECT nursingWardId as nursingWardId FROM NursingStationWardMapper"
//      + " WHERE wardId=:wardId "
        + " WHERE isDeleted='N' "
        + " AND nursingStationId=:nursingStationId "
        + " AND status='A' "
        + " AND organizationId=:orgId "
        + " AND unitId=:unitId "
        ),
```

`wardId` is commented out of the WHERE clause; `NursingStationWardMapperDaoImpl.java:230` confirms it's never bound either (`.setParameter("wardId", ...)` commented out, `:128` shows the same for a raw-SQL variant). The query as written asks only "does this nursing station have *any* active mapping at all" — not "is *this specific ward* already mapped to it." `NursingStationWardMapperServiceImpl.java:28-40` (`saveNursingStationWardMapper`) confirms this check genuinely gates the real save: any match at all (regardless of which ward) returns `ALREADY_EXIST` and blocks the request.

**Net effect of both layers together:** a nursing station's *first* ward mapping succeeds normally. The picker for the *second* ward mapping shows every ward (correctly or incorrectly mapped, no way to tell which), and attempting to save that second, genuinely different ward is unconditionally rejected as a duplicate — because the save-time check only sees "this station already has something," never "is it this particular ward."

## 2. Why this looks like a real, unintentional defect

- Both layers show the exact same shape: the correct, intended filtering logic is written directly in the query source, then disabled with a comment, with the DAO's parameter binding disabled to match. This is not two independent oversights — it's the same author (or the same edit pass) disabling both the picker's exclusion and the save-time check's ward-specificity at the same time, which suggests a single change (or a single misunderstanding) broke both halves of what should be one coherent feature.
- `departmentMasterUnit`, a structurally near-identical screen in the same module (unit-level speciality-to-department mapping via a check-then-move picker), has this exact pattern **working correctly** — its own picker genuinely excludes already-mapped items server-side. `NursingStationWardMapper` is very likely intended to work the same way and simply doesn't, rather than being a screen where "show everything, block all but the first" was ever the deliberate design.
- A nursing station covering only one ward, ever, would be an unusual real-world configuration for most hospitals — the screen's own multi-select "check → move → save" picker UI (built for choosing several wards at once) only makes sense as a UI if adding more than one ward per station is the normal case, not an edge case.

## 3. What this does and doesn't affect

- **Real operational impact, not just latent risk:** any nursing station that needs a second ward mapped will hit this today, on the very first attempt to add it. The picker not filtering is a milder, confusing-but-not-blocking symptom (staff can't tell what's already mapped just by looking); the save-time rejection is the actual blocker.
- **Not a data-integrity defect** — no wrong data is written; a *correct*, intended mapping is prevented instead.
- **Narrow double-click gap noted separately, not the focus of this document:** the sweep also flagged that neither the picker nor the save button has an in-flight/disable-while-saving guard — the same narrow TOCTOU class already logged for `departmentMasterUnit`. Not proposing a fix for that here; it's a much lower-severity, harder-to-trigger issue than the two layers above and doesn't need the same urgency.

## 4. Proposed fix (not implemented — for review)

**Layer 1 — restore the picker's exclusion filter, uncommenting exactly what's already written:**

```java
@NamedNativeQuery(name="GET_WARD_LIST_NOT_MAPPED_TO_NURSING_STATION",
    query="SELECT wrdMstr.ward_id AS \"wardId\", wrdMstr.ward_code AS \"wardCode\", wrdMstr.ward_name AS \"wardName\" "
        + " FROM adt.m_ward_master wrdMstr "
        + " WHERE wrdMstr.ward_id NOT IN(SELECT nursingWrdMapper.ward_id FROM adt.t_nursing_station_ward_mapper nursingWrdMapper "
        + " WHERE nursingWrdMapper.nursing_station_id=:nursingId )"
        + " AND wrdMstr.organization_id=:orgId"
        + " AND wrdMstr.unit_id=:unitId "
        + " AND wrdMstr.status='A'"
    ),
```
plus re-enabling `.setParameter("nursingId", wardMasterDto.getNursingStationId())` in `WardMasterDaoImpl.java`.

**Layer 2 — restore the save-time check's ward-specificity, uncommenting exactly what's already written:**

```java
@NamedQuery(name="CHECK_IS_NURSING_STATION_ALREADY_ASSIGNED",
query="SELECT nursingWardId as nursingWardId FROM NursingStationWardMapper"
        + " WHERE wardId=:wardId "
        + " AND isDeleted='N' "
        + " AND nursingStationId=:nursingStationId "
        + " AND status='A' "
        + " AND organizationId=:orgId "
        + " AND unitId=:unitId "
        ),
```
plus re-enabling `.setParameter("wardId", nursingStationWardMapperDto.getWardId())` in `NursingStationWardMapperDaoImpl.java`, and the same treatment for the sibling `CHECK_IS_NURSINGSTATION_ALREADY_ASSIGNED_OR_NOT` query used by the status-update path (`:51-58`), which has the identical gap.

Both changes restore exactly what the code already shows was once written and intended — no new logic invented, no business-rule guesswork. This is application code (`legacy/`), so per `CLAUDE.md` it would need its own dedicated branch (Maker-Checker), verified before/after (map two different wards to the same nursing station, confirm both succeed and the picker correctly stops offering an already-mapped ward a second time; attempt to map the *same* ward to the same station twice, confirm that's still correctly rejected) and held for explicit approval before merging.

## 5. What we need from you

1. **Approve both fixes (picker exclusion + save-time ward-specific check) for a dedicated legacy branch?** They're closely related (one feature, two broken halves) — proposing them together, but happy to split into two branches if you'd prefer to review them separately.
2. Nothing else is blocked on this decision — there is no rebuild of this screen in flight to hold pending your answer.

No code has been touched to produce this document. This finding remains logged in `docs/ANGULAR_PENDING_FINDINGS.md`'s `PENDING-systemic-duplicate-check-sweep` entry; this document proposes the specific fix for it, per your instruction.
