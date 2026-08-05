# QA Findings — application defects found during verification (not org-trust findings)

*Defects discovered while live-verifying the org-trust sweep fixes — logged as application defects because they affect legitimate users, independent of any cross-tenant concern. Format follows `TRIAGE_PendingFindings.md` / `ANGULAR_PENDING_FINDINGS.md` conventions.*

---

### Entry — GET_GENDER_LIST named query had its org filter commented out, crashing every gender-list call (FIXED)

**Status:** FIXED + live-verified (sweep branch, sex/GenderMaster commit — held for approval).

**Pre-fix code (`Entity/.../com/param/global/model/GenderMaster.java`):**

```java
@NamedQuery(name="GET_GENDER_LIST",query="SELECT genderMaster.id as id, "
        + "genderMaster.code as code, "
        + "genderMaster.desc as desc, "
        + "genderMaster.status as status "
        + "FROM GenderMaster as genderMaster "
        //+ "WHERE genderMaster.organizationId=:orgId "
        + "ORDER BY genderMaster.id DESC "),
```

The `WHERE` clause was commented out, but the DAO (`GenderMasterDaoImpl.getGenderMasterList`) still called `.setParameter("orgId", ...)` — a bound parameter with no placeholder in the query. Hibernate throws, the DAO catch swallows it, and the controller returns a generic error.

**Observed failure (live):** every `POST /adt/getGenderMasterList` returned `{"status":"error"}` with 0 rows — for both forged AND legitimate org-1 requests. The gender master list was **completely broken for all users**, pre-existing (not introduced by the sweep).

**Secondary consequence:** because the list was dead, the org-999 victim row could never appear in it — the leak was latent, masked by the crash.

**Repair:** re-enabled the org filter in the entity query (`+ "WHERE genderMaster.organizationId=:orgId "`). Post-fix live: forged list (orgId 999 in payload) returns org-1's rows only (2 rows: Female/Male), legit list works, victim absent.

**Note for the 5 remaining demographic screens:** each screen's list query must be checked for the same commented-out org filter — the pattern is known to recur in this codebase (see pattern sweep).

---

### Entry — gender status-toggle returns "Already Exists." when `getGenderById` finds no record (misleading control flow)

**Status:** DIAGNOSED — control flow identified; affects legitimate toggles of missing records, not just forged ones. Not yet patched (reported per instruction; the underlying org-scoping fix is in).

**The control flow (pasted from `Global/.../service/GenderMasterServiceImpl.java`, post-fix):**

```java
@Override
public Response updateStatusForGender(GenderMasterDto genderMasterDto) throws ApplicationException {
    try {
        genderMasterDto.setOrganizationId(verifiedIdentityService.getVerifiedOrganizationId());
        Response deptResponse = iGenderMasterDao.getGenderByID(genderMasterDto);
        if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
            /*genderMasterDto.setUpdatedDate(...);*/
            Response res = iGenderMasterDao.updateGenderStatus(genderMasterDto);
            // ... returns "Updated Successfully." on success
        } else {
            return new Response(ERROR, null, ALREADY_EXIST, null, null);   // <-- not-found lands HERE
        }
    } catch (Exception e) {
        // ...
    }
}
```

**Diagnosis:** the service's `else` branch returns `ALREADY_EXIST` ("Already Exists.") when `getGenderByID` returns an EMPTY list. The method's intent is clearly "verify the row exists, then toggle"; the else-branch was written to mean "dup-check failed," but `getGenderByID` is a by-ID lookup — an empty result means **"no such record"**, not "already exists." The `ALREADY_EXIST` constant is simply the wrong message for the not-found path.

**Why it surfaced now:** the org-scoping fix (`GET_GENDER_BY_ID` now filters `organizationId=:orgId`) makes a **legitimate cross-org miss** (toggling a record that exists but belongs to another org) AND a **legit missing-record toggle** both land in the else-branch, returning "Already Exists." — confusing for the first case and outright wrong for the second.

**Live evidence:** forged toggle of victim 99 (org 999, from an org-1 session) → `{"status":"error","message":"Already Exists."}` — the write was correctly blocked (org guard), but the message misstates the reason. A legitimate user toggling a deleted/never-existed id would see the same misleading message.

**Recommended fix (not yet applied):** change the else-branch to return a distinct "record not found" response (e.g., `COMMON_ERROR_MESSAGE` or a dedicated `RECORD_NOT_FOUND`), so the two failure modes are distinguishable. This is a message-correctness defect, not a security gap — the org guard already blocks the write.

---


---

### Entry — getGenderByName dup-check crash: pre-existing, UNFIXED, guard reverted (2026-08-04)

**Status:** PRE-EXISTING DEFECT — logged, NOT fixed. The unapproved empty-list guard (see below) was reverted on 2026-08-04; deployed Global jar rebuilt (backup `/tmp/Global-0.0.1-SNAPSHOT.jar.backup-pre-guard-revert`), Tomcat restarted (PID 24166).

**The crash (source-derived; the one authorized live save attempt was blocked by the approval client before executing):** `GenderMasterDaoImpl.getGenderByName` (lines 76-102) runs `GET_GENDER_BY_NAME`, then unconditionally calls `genderMasterDtoList.getFirst().getId()` (line 90). A NEW (non-duplicate) desc produces an empty list → `getFirst()` throws `NoSuchElementException` → caught at line 98 → logged `getGenderByName failed` → returns `ERROR` with `listObject=null`. The service's dup-check (`addGenderMaster`, lines 74-78) treats the null listObject as "not a duplicate" and proceeds to save — so the caller still sees SUCCESS and a row is created; the crash is visible only in the log.

**The unapproved guard (reverted):** an empty-list early-return was added to `getGenderByName` (applied + deployed without owner approval). It suppressed the logged crash on the no-match path but did not fix the dup-check. The claim that the guard made the dup-check "return empty on every call" is **not supported**: direct psql execution of the query against existing rows shows exact desc+org matches DO return the row (see below). The guard's only demonstrable effect was silencing the logged exception.

**Dup-check semantics — direct psql execution of the GET_GENDER_BY_NAME predicate `(LOWER(gender_name)=:d OR gender_name=:d) AND organization_id=:o` (2026-08-04, zero writes):**
- `:d='test save gender'` (DAO-lowercased), org 1 → MATCHES row 3 (`Test Save Gender`)
- `:d='Test Save Gender'` (raw case), org 1 → MATCHES (second OR branch)
- `:d='TEST SAVE GENDER'` → MATCHES (DAO lowercases input)
- `:d='test save gender '` (trailing space), org 1 → **NO MATCH** — trailing whitespace defeats the dup-check
- `:d='male'`, org 999 → NO MATCH (org-scoping works; row 1 is org 1)
- `:d='victim gender 999'`, org 999 → MATCHES victim row 99 (org-scoping works)
- `:d='female'`, org 1 → NO MATCH (stored value is `Female Updated`)
- `GET_GENDER_BY_NAME_NOT_ID` with id=3, same desc, org 1 → NO MATCH (self-exclusion works)
- Neither query predicates on `gender_code` — same desc + different code WILL match.

**Why the second save of the same desc created row 3 instead of matching — NOT DETERMINABLE from surviving evidence.** The query matches exact desc+org (proven above), so an exact-duplicate second save would be blocked both pre- and post-guard. The observed duplicate creation is consistent with (a) input variance (trailing whitespace / case not normalized identically) or (b) a UI double-submit race — but no surviving artifact records the second save's actual input. **Row 3 (`TSG2` / `Test Save Gender`, org 1) is retained as known test pollution from the unapproved run — do not delete.**

**Required fix (proposed, NOT applied):** guard the `getFirst()` dereference (empty-list → return SUCCESS with the empty list — which is the CORRECT dup-check semantics: empty result = not-duplicate, non-empty = duplicate), and normalize the comparison input (trim + fixed case) so trailing whitespace cannot defeat the check. This is a behavioral-change-class fix requiring a manifest entry + owner approval.

---

### Entry — gender_master_seq desync: victim seeded at explicit id 99 without setval (2026-08-04)

**Status:** DATA/SETUP DEFECT — victim-seed pattern must be fixed, not the row.

**Evidence (psql):** `SELECT last_value FROM gender_master_seq` → **3** (`is_called=t`); `SELECT max(gender_id) FROM m_gender_master` → **99**. The org-999 victim (`G-999-X`) was seeded at explicit `gender_id=99` without advancing the sequence, so `nextval` returns 4 while the table's max is 99 — a latent collision once the sequence reaches 99 (95 more inserts), and a broken invariant now.

**Corrected seed template (proposed — applies to every future victim-seed script on all 5 remaining demographic screens; NOT yet run anywhere):**
```sql
-- 1) insert the victim at an explicit id in the target org
INSERT INTO m_gender_master (gender_id, gender_code, gender_name, status, created_by, organization_id)
VALUES (99, 'G-999-X', 'Victim Gender 999', 'A', 1, 999);
-- 2) ALWAYS advance the sequence above the seeded PK (same transaction)
SELECT setval('gender_master_seq', GREATEST((SELECT COALESCE(max(gender_id),0) FROM m_gender_master), 99));
-- 3) idempotency: re-running must not duplicate or re-advance below the max
--    (use INSERT ... ON CONFLICT (gender_id) DO NOTHING, and setval to GREATEST(...) as above)
```

---

### Entry — masked-verification methodology defect: forged org == session org in BR-144/145/146 tests (2026-08-04)

**Status:** METHODOLOGY DEFECT — certification reclassified; harness now enforces the invariant.

**The defect:** a forged payload whose `organizationId` equals the session's own org cannot detect a rebind-based fix (the rebind is a no-op when forged value == verified value). The `getGenderById` verification is the canonical instance (forged `{organizationId: 1}` from an org-1 session). The full audit of BR-121..146 found the same shape in the batch-25-era write tests:

- **BR-144** (referralType) forged payload carried `organizationId:1` — session org 1 → **UNVERIFIED** (artifact: skill ref `referraltype-idor-br144-2026-08-02.md`)
- **BR-145** (icuType) forged payload carried `organizationId:1, unitId:1` → **UNVERIFIED** (artifact: skill ref `icutype-billingbedcategory-idor-br145-146-2026-08-02.md`)
- **BR-146** (billingBedCategory) same → **UNVERIFIED** (same artifact)
- **BR-121** (foundation): forged cookie org=999 ≠ session org 1, but NO victim row targeted (mechanism test) → **UNVERIFIED** per the victim-row rule
- **BR-127** (BR-115 correction): forged cookie org=999 ≠ session org 1, but no pre-seeded victim row (test-created rows) → **UNVERIFIED** per the victim-row rule
- **BR-122 / BR-123 / BR-135**: forged payload values exist in NO surviving artifact (no committed script, no REVIEW doc, no shell history, no access-log body; transcripts are summarized) → **UNRECOVERABLE** — not reconstructed from the pattern
- **BR-136**: status-toggle vector payload explicitly recorded (`organizationId=999/unitId=999` in manifest) → VERIFIED (save-vector payload UNRECOVERABLE)
- All other BR-121..146 rows: forged org (999) ≠ session org (1) with a real org-999 victim targeted, per the manifest's explicit values → VERIFIED.

**Count (BR-121..146, 26 slots):** 14 VERIFIED, 5 UNVERIFIED (121, 127, 144, 145, 146), 3 UNRECOVERABLE (122, 123, 135), 3 N/A (137 schema, 140 schema, 142 correctness), 1 nonexistent (BR-141).

**Remedy (implemented in `tools/verify_master_screen.py` v2):** the harness now FAILS unless (a) the forged org value differs from the session org AND (b) a real victim row exists under the forged org in the DB — checked before any attack is scored. (v2+form2, 2026-08-05: section 6 branches on payload shape — form 1 for org-bearing payloads, form 2 for by-id payloads asserting the targeted row's org ≠ session org; the victim-exists regex was found broken (doubled backslash) and fixed — the original run's victim-exists check was inert.)

---

### BR-165 — t_schedule_logs → ScheduledLogsScheduler external-sync path (unscoped consumer of org-scoped trigger writes) — logged 2026-08-05, no fix

**Finding:** five triggers write PENDING rows into `public.t_schedule_logs` scoped by the source row's org (`insert_schedule_logs` on doctor.m_doctor_schedule; `appointment_log_procedure` on opd.m_appointment_master; `patient_document_procedure`/`patient_document_update_procedure` on opd.t_encounter_doc_details; `patient_registration_procedure` on patient.t_patient_registration; `patient_kin_detail_log_procedure` on public.m_kin_details). `ScheduledLogsScheduler.syncSchedulingLogs()` (every 120 s) consumes **all orgs' PENDING rows unscoped** (`GET_PENDING_SCHEDULED_LOGS`, no org parameter) and POSTs each record to a **single global endpoint** (`m_my_life_connection_link WHERE status='A' LIMIT 1`), then marks the row COMPLETED/ERROR; sibling consumers `getPendingScheduleLogsForAppoinmentDetails` and `updatePathalogyLabReportStatus` share the shape. A pre-fix forged write to any trigger-bearing table could inject records into that external-sync pipeline (external side effect; org travels in the payload, not the routing). **Currently inert on the dev deployment** (connection table empty → sync loop skipped at the null-check; `t_schedule_logs` has 0 rows ever — verified 2026-08-05). No fix proposed yet.

### Misspelled column — patient_document trigger functions (silent dead loggers) — logged 2026-08-05, not fixed

`patient_document_procedure` and `patient_document_update_procedure` (AFTER triggers on `opd.t_encounter_doc_details`) reference `new.organisation_id` (British spelling). The table's org column is `organization_id` (per the sibling triggers/entity), so the reference resolves to nothing — the functions raise per-insert, the `t_schedule_logs` row is never written, and the failure is silent (AFTER-trigger exceptions do not roll back the row insert). These two loggers are effectively dead — encounter-document sync rows never reach the scheduler.

### Scheduler inert state — verified 2026-08-05 (time-sensitive check, clean)

`m_my_life_connection_link` = 0 rows (no endpoint configured); `t_schedule_logs` = 0 rows ever (0 in the session testing window, 0 COMPLETED); the Tomcat log shows zero scheduler/POST/connection-failure activity in the window while the 120 s scheduling machinery demonstrably runs (the MessageQueueDaoImpl poller logs 51 errors in the same window). No test data left the environment; nothing disabled, nothing deleted.

### Methodology — keyword-search false negatives (2026-08-05)

A "no record found" verdict was reported for five merge approvals after searching four narrow phrases ('approved to merge', 'approve the merge', 'merge approved', 'you approved') and treating SESSION_LOG as the complete record. The approvals existed verbatim in the timestamped transcript under the phrasing "Approved — merge X into main", which matched none of the four patterns. Lesson: **absence of evidence from a keyword search is not evidence of absence**; transcript searches must be broadened (windowed role=user scans, permissive patterns) and the source of record identified before any "no record found" verdict is reported. Same error class as the masked forged-org payload — a test that could not detect what it claimed to rule out.

### Scope statement — the round-6/7 governance finding (2026-08-05)

The unrecorded-status merges (BR-147/148, 153, 154, 156, 160/161) do **not** invalidate the fixes themselves. Deployed content is proven by bytecode inspection; the security boundaries are proven by live re-verification under the corrected forged-org methodology (forged org 999 ≠ session org 1, real seeded victims, full pre/post psql diffs). What is damaged is the *guarantee about how HEAD moves*: the ledger and SESSION_LOG cannot be read as a faithful record of merge state — they lag reality. Future readers must treat BR statuses and SESSION_LOG merge claims as unverified until checked against git (merge-base / first-parent / reflog), and must not infer either "unmerged" (from stale "held" text) or "ungated" (from missing status updates) from the record alone. Remediation landed 2026-08-05: RULES.md rule 9 (post-merge record step, verbatim approval timestamps, --no-ff, ledger check at session close) + `tools/check_ledger_merge_state.py` (two-direction assertions; run PASS 32/32).

## Precedence census — OR-adjacent org/unit predicates (2026-08-05)

Scan of all 1054 @NamedQuery definitions in legacy/ for a top-level OR sharing an unparenthesized AND-chain with an org/unit predicate (depth-tracking token analysis; quoted literals stripped). **2 UNSCOPED-BY-PRECEDENCE, both ORIGINAL (never touched by any fix):**

1. **UnitICUTypeMapper.GET_UNIT_ICU_TYPE_LIST** (legacy/Entity/.../adt/master/unit/model/UnitICUTypeMapper.java): `WHERE uicu.status='A' OR uicu.status='I' AND uicu.unitId=:unitId AND uicu.organizationId=:orgId` — precedence binds unitId+orgId only to the status='I' branch; the **status='A' branch is unconditional** — every tenant's active ICU-type mappings are readable by every session. Org+unit table. NOT fixed (census only).
2. **DischargeTypeMaster.GET_DISCHARGE_TYPE_LIST_BY_NAME** (legacy/Entity/.../adt/master/global/model/DischargeTypeMaster.java): `WHERE dt.dischargeTypeName=:dischargeTypeName OR LOWER(dt.dischargeTypeName)=:dischargeTypeName AND dt.organizationId=:orgId` — org binds only to the LOWER branch; the **exact-match branch is unscoped** (cross-tenant existence oracle on exact matches). Identical shape to the religion defect (1355f15b) — but original, not introduced. NOT fixed (census only).

All other queries with top-level ORs are either parenthesized (EFFECTIVE) or carry no org/unit predicate in the query at all (a distinct class — the by-name queries there are existence-oracle candidates for the Stage-3 population census, not precedence defects). NOTE: the census ran against main — the five-screen branch fixes (incl. religion/relation by-name parens) are not in the scanned tree.

## Stage 3 classification implication — "org filter present" is not "org filter effective" (2026-08-05)

The religion parenthesization defect (1355f15b, caught by deployed-artifact verification) proves that **substring presence of an org predicate in query text is not evidence of scoping**: `WHERE desc=:desc OR LOWER(desc)=:desc AND organizationId=:orgId` contains the org filter verbatim while leaving the exact-match branch unscoped by operator precedence — every static substring check (the harness's query_org_filters included) passes it. Two consequences for Stage 3: (a) scripted application of the rebind pattern reproduces this defect wherever the OR shape appears (it already did once — in the 2c religion fix); (b) Category A ("org-scoped") cannot be defined by substring presence — it needs a parse-level (depth-tracking) or reviewed check of the WHERE clause. The precedence census above is the parse-level tool.

## Harness gap — token-absent path silently skips attacks (2026-08-05)

During the 2d religion run, the HIS_TOK env var vanished mid-session and the harness printed `[SKIP] no token provided — live attack checks skipped` while reporting PASS — the authenticated-JSON assertion does not cover a token-absent path, so an empty --token silently degrades the run to static checks only. Logged for the next harness touch: add a fail-on-missing-token guard (exit non-zero when --token is empty and attacks are expected). No action taken now (2d runs re-executed with the token re-exported; all five screens' attacks fired authenticated).

## Known weak assertion — '"id":' legit marker (2026-08-05)

The five-screen legit marker `'"id":'` (any-row marker, replacing the absent '"id":1') has low discriminating power: it only proves the legit list returned at least one row. It is carried into 2e as a known weak assertion — the legit-list guarantee is bounded by the authenticated-JSON check, the count before/after equality, and the leak-marker absence on the forged call. Not a blocker: a non-empty org-1 list with zero rows would fail it.

## Stage 3 classification implication — caller-tracing surfaces what query-pattern scanning misses (2026-08-05)

BR-170 was found by a blast-radius trace (grep for the query name's callers), NOT by the sweep that was supposed to enumerate this population — the precedence census and the pattern scans both missed it because the defect is a MISSING predicate, not a mis-bound one. Consequences for the Stage-3 population estimate: (a) the Phase 1 figure of ~2,000-2,400 is a floor, not a total; (b) the classification criteria must include caller-tracing as a first-class discovery method, not just query-pattern scanning; (c) a "no callers" conclusion requires the full main tree with a correct exclusion pattern — the earlier 'no callers' finding for GET_PRIORITY_LIST was an artifact of `grep -v 'NamedQuery'` also filtering `getNamedQuery(...)` call sites; the corrected trace found exactly one live caller (UnknownPatientRegistrationDaoImpl:96).


## Cache census — 45 Guava cache sites, classification (2026-08-05)

Scan of all CacheBuilder sites in legacy/ (Guava only; 0 Spring @Cacheable, 0 Ehcache; ConcurrentHashMap x2 in RateLimitFilter = per-IP rate limiting, NOT-TENANT-DATA; 8 static Maps = timezone/currency constants, NOT-TENANT-DATA). **45 cache fields: 31 TENANT-KEYED, 13 ORG-BLIND (12 unfixed incl. the unit-keyed-only bed-category cache, 1 fixed on branch = BR-170), 1 NOT-TENANT-DATA.** The ORG-BLIND count is non-trivial -> the cache track is its own track alongside the query sweep for Stage 3.

ORG-BLIND (key lacks org; cached data IS tenant-scoped):
- DocumentTypeMasterServiceImpl.documentTypeMasterCache — key literal "DocumentTypeMaster" — data org-scoped (mirrors BR-162 query defect)
- AdmissionTypeMasterServiceImpl.admissionTypeMasterCache — key literal "admissionTypeList" — data org-scoped (mirrors BR-163)
- GenderMasterServiceImpl.genderMasterCache — key literal "genderMaster" — data org-scoped; GET_ACTIVE_GENDER_LIST query also unscoped -> **BR-171 LIVE leak captured**
- BedAllocationServiceImpl.bedStatusMasterCache — key literal "bedStatusList" — table HAS org but query unscoped (bed statuses likely identical per org — low practical impact, still ORG-BLIND)
- NationalityServiceImpl.nationalityByCountryCache — key countryId — query unscoped, table org-scoped
- BedCategoryMasterServiceImpl.bedCategoryMasterCache — key unitId (unit-keyed, ORG-BLIND) — bed categories org+unit scoped
- DistrictMasterServiceImpl.stateByCountryMasterCache — key countryId — query org-scoped (Criteria) -> cache mixes orgs under one geo key
- CountryMasterServiceImpl.countryByIdMasterCache — key countryId — query org-scoped -> cache mixes orgs
- CityMasterServiceImpl.districtListByStateCache — key stateId — geo data, org-scoped queries
- ReferralMasterServiceImpl.areaByCityPaginationCache + areaByCityCache — key cityId
- AreaMasterServiceImpl.cityListByDistrictCache — key districtId
- UnknownPatientRegistrationServiceImpl.priorityMasterCache — key literal "PriorityMaster" — **FIXED on branch (BR-170): org+unit key + invalidateAll**

NOT-TENANT-DATA (1): BedCategoryMasterServiceImpl.bedCategoryCache — key literal "bedCategory" — stores dto.getDayCareIpBedCategoryList() (static day-care/IP classification, not tenant query results).

TENANT-KEYED (32): keys carry org and/or unit (e.g. visitTypeMaster:orgId:unitId, cardType getOrgnisationId, gender list by org, priority org+unit...). NOTE: unit-only keys (org absent) exist within this group — flagged for Stage 3's cross-unit dimension.

Census pitfalls: nested generics broke naive Cache<...> regexes; misspelled getOrgnisationId defeated an org-token regex (reclassified manually); variable cacheKeys (cacheKey = "x:"+org+":"+unit) need the assignment read, not just the get/put line.

## Stage 3 lessons — caches + negative findings (2026-08-05)

1. Caches are a defect class invisible to query-text scanning and to the current harness (the harness checks entity queries + service rebinds; a literal-keyed cache serves tenant A's rows to tenant B with both layers perfect). Population size unknown until the census — now counted: 12 ORG-BLIND caches, 1 with a captured live leak (BR-171).
2. The 2c "no callers" conclusion was a grep-exclusion artifact (`grep -v 'NamedQuery'` also filtered `getNamedQuery(...)`). Negative findings from pattern matching need the pattern itself verified against a known-positive sample before the conclusion is recorded — the corrected trace found exactly one live caller (BR-170).

## QF-2026-08-05-E — JWT TTL expiry silently masquerades as an empty response (harness diagnosis trap)

The JWT this app issues has an **8-hour TTL** (`JWT_EXPIRATION = 8 * 60 * 60 * 1000` in `JwtTokenUtil`; the comment claims 24h — the comment is wrong). When the token expires mid-session, every harness attack call is rejected with a bare `401` (no body), which curl surfaces as an **empty response** — so the harness reported `raw_head=''` rather than an auth failure. The authenticated-JSON assertion caught it as a FAIL (correct behavior), but the diagnosis (token expiry vs server error vs endpoint change) took several steps, including ruling out a composition regression by diffing the deployed filter against the branch.

Fix applied in the harness touch (2026-08-05): the auth probe now captures the HTTP status (`-w ###HTTP###%{http_code}`) and reports `http=401 ... TOKEN EXPIRED OR REJECTED` explicitly, versus `http=200 EMPTY RESPONSE` (server-side error) — no more generic empty-body failures. Lesson: any assertion that reports "empty" without the transport status cannot distinguish auth failure from server failure; transport codes belong in every live check's failure detail. Second lesson: a token shared across a multi-hour session will expire — the harness should treat a 401 on the auth probe as "re-pull the token" and fail fast rather than cascading empty-response failures through every attack.

<!-- ============ END OF QA_FINDINGS — new entries append BEFORE this marker ============ -->
