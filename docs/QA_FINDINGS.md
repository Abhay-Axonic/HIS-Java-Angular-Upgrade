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

## QF-2026-08-05-F — SCOPE CORRECTION: BR closure = endpoint-level verification, NOT screen-level coverage

Stage 3a's per-screen method audit (Class 4) established that the closed-BR ledger overstates
what is fixed. For every BR 121-171, the fix + verification covered the ATTACKED ENDPOINTS
(list, by-id, update, toggle, save, active-list where exercised) — never the full screen.
Gender and religion were not two exceptions; they are the two that got caught. The honest
claim: **the attacked endpoints are fixed and verified; the screens are not.**

Per-screen residual (service methods that read/write tenant data, NOT covered by the BR's
fix, NOT DAO-guarded; Stage 3a, 2026-08-05):

| screen | total public svc methods | VIS-covered | uncovered | residual after DAO-guard discount |
|---|---|---|---|---|
| BR-134 userMaster | 52 | 0 | 8 | 3 (generateToken, userLogin, userLoginForVitalUploader — login family, Category C) |
| BR-159 mPackage | 51 | 5 | 7 | 7 (incl. getExistingActivePackageByPatientEncounter) |
| BR-135 userRoleMapper | 30 | 1 | 6 | 3 (getUserRoleMapperById, getUserIdById, getRoleNameByRoleTypeId) |
| BR-136 unitAppBillingConfig | 17 | 0 | 4 | 3 (count, getOPDBillingBedCatagory, getSelfPayeeList) |
| BR-146 billingBedCategory | 13 | 0 | 3 | 3 (by-id, by-visitType, ACTIVE list) |
| BR-143 patientSource | 8 | 3 | 3 | 1 (getPatientSourceByName dup-check) — writes DAO-guarded (sweep 8 VIS refs) |
| BR-144 referralType | 8 | 0 | 3 | 2 (by-name dup-check, add) — update/status DAO-guarded |
| BR-150 autoclave | 7 | 0 | 2 | 2 (getAllAutoclaveList, ACTIVE list) |
| BR-152 vitalQualifier | 10 | 0 | 2 | 2 (by-id, ACTIVE list) |
| BR-127 productCategory | 4 | 1 | 2 | 2 (getAll, getById — DAO call shape unresolved) |
| BR-158 taxComponent | 4 | 2 | 2 | 2 (getAll, getById — DAO call shape unresolved) |
| BR-147 allergy | 7 | 0 | 1 | 1 (getAllergyTypeMasterListById) |
| BR-148 dose | 7 | 0 | 1 | 1 (getDoseMasterListById) |
| BR-170 priorityActive | 32 | 1 | 1 | 1 (getListOfEncounterIdByPatientId) |
| BR-166 priority / BR-167 religion / BR-171 gender | 6 / 7 / 8 | 6 / 7 / 6 | 0 / 0 / 2 | 0 / 0 / 0 (uncovered don't read tenant data) |

RESIDUAL TOTAL: **26 service methods** across 13 screens after the DAO-guard discount (raw 31).
Of the 26: the query-org-filtered-but-client-valued reads (allergy/dose by-id, vital/bedCategory/
autoclave active+by-id) are Pattern-A siblings pending the live forged-org proof; the login
family (BR-134) is Category C (org from credentials — needs judgment, NOT a blind rebind);
BR-127/158's DAO call shapes are unresolved (possible false positives). 5 methods were
discounted because DAO-level VIS guards cover them (patientSource writes x3, referralType
update/status x2) — service-level VIS absence does not imply uncovered.

NO BRs were reopened or renumbered; the fixes are real. The scope claim was wrong.

## QF-2026-08-05-G — Class 2 SILENT variant dominates; active-list census (Stage 3a)

- Unit-commented predicates: **103 distinct named queries; 97 NO-BIND (silent) / 6 NO-CALLER / 0 orphaned-bind** —
  the same inversion as the org set, worse. All 97 callers also bind NO org → the queries run fully unscoped.
  Includes patient-facing/clinical reads (GET_PATIENT_DETAILS, GET_PATIENT_*_HISTORY x12, GET_LIST_ALLERGY_PATIENT_MAPPER,
  GET_LIST_OF_VITALS_BY_PATIENT_ID_*, GET_MEDICATION_HISTORY_LIST, GET_CONSULT_DOCTOR_LIST_BY_ADMISSION_ID) and master
  reads (GET_ALLERGY_TYPE_MASTER_LIST + by-id/by-name variants, GET_ACTIVE_BANK_MASTER, GET_VITAL_QUALIFIRE_MASTER_BY_NAME,
  GET_BED_CATEGORY_LIST_BY_ORG_UNIT). Sub-classification (silent vs crash-variant vs intentional-global patient reads)
  requires the per-query text org-state pass — pending. NOT fixed.
- 22-org ranking: 18 live callers / 4 NO-CALLER; ~12 on patient-facing/clinical paths (validated: GET_ACTIVE_DISCHARGE_TYPE_LIST;
  high: CHECK_ALREADY_EXIST_REG registration oracle, GET_MEDICATION_HISTORY_LIST, GET_CONSULT_DOCTOR_LIST_BY_ADMISSION_ID,
  DIAGNOSIS_AUTO_FILL_SEARCH, GET_DIAGNOSIS_BY_GENDER_ID, GET_ROUTE_OF_ADMINISTRATION_LIST, GET_DISCHARGE_SUMMARY_FIELD_MASTER x4,
  GET_VITAL_MASTER_TO_SYNC_BY_ID, GET_ACTIVE_DISCHARGE_SUMMARY_FIELD_MASTER). NOT fixed.
- Active-list direct census (the converging shape as its own population): **643 active-list-shaped named queries**
  (ACTIVE in name or status='A' predicate): 547 org-scoped / **87 NO org predicate** (needs the table-column check —
  the LIS 5 global-ref tables are correctly unscoped) / **9 commented-org** (the sibling class: GET_ACTIVE_DISCHARGE_TYPE_LIST
  [validated], GET_ACTIVE_DISCHARGE_SUMMARY_FIELD_MASTER, GET_LIST_APPOINTMENT_STATUS_MASTER, GET_CONSULT_DOCTOR_LIST_BY_ADMISSION_ID,
  DIAGNOSIS_AUTO_FILL_SEARCH, GET_DIAGNOSIS_BY_GENDER_ID, GET_ACTIVE_TEXTUAL_RANGE_MASTER_LIST, GET_LIST_SEVERITY_MASTER,
  GET_DESIGNATION_MASTER_LIST).
- Class 1 method-level correction: org-only client-sourced methods = **266, not 39** (earlier dedup keyed on signature
  suffixes collided — undercount). A/B/C: **A ≈ 0** (0/10 resolved org-only methods have an org-capable DAO signature),
  **B dominant** (137 org+unit pairs + ≥10 org-only + the controller-layer sites' service hops), C = login family + cross-DTO
  (lower bound). The 3,659 entity-copy setter sites are dropped from the headline population (server-side copies, noise).

## QF-2026-08-05-H — Clinical-set triage: three query states (Stage 3b-revised)

The "clinical" Class-2 candidates split into THREE states (declaration-level read, not regex):
- **SILENT (live query, org/unit predicate commented, no bind — currently returns ALL tenants' rows):**
  CHECK_ALREADY_EXIST_REG (death-registration dup-check — PHI names, patient-identity oracle),
  GET_CONSULT_DOCTOR_LIST_BY_ADMISSION_ID (consult doctors — PHI-adjacent, admissionId-keyed),
  GET_VITAL_MASTER_TO_SYNC_BY_ID (appointment-vital mapper — PHI vitals, appointmentVitalMapperId-keyed),
  GET_DIAGNOSIS_BY_GENDER_ID (reference), GET_DISCHARGE_SUMMARY_FIELD_MASTER_BY_ID/BY_NAME/BY_NAME_NOT_ID
  (reference config + dup-check oracles), GET_ACTIVE_DISCHARGE_TYPE_LIST (reference, VALIDATED),
  + GET_DISCOUNT_CATEGORY_BY_ID, GET_HELP_VALUE_BY_PARAMETER, GET_LIST_APPOINTMENT_STATUS_MASTER,
  GET_LIST_SEVERITY_MASTER, GET_TEXTUAL_RANGE_MASTER_BY_ID = 13 total.
- **DEAD (entire @NamedQuery commented — caller 500s "No query defined"; broken feature, not a leak):**
  GET_MEDICATION_HISTORY_LIST (coversheet medication history — PHI-bearing feature dead),
  DIAGNOSIS_AUTO_FILL_SEARCH (diagnosis autofill), GET_ACTIVE_DISCHARGE_SUMMARY_FIELD_MASTER,
  GET_ALL_ASSET_SCHEDULED_LIST, GET_BABY_DETAILS_BY_ID, GET_DETAILED_SERVICE_LIST,
  GET_ICPC_TEMPLATE_MAPPING_COUNT, GET_ROUTE_OF_ADMINISTRATION_LIST, GET_SPARE_CONSUMPTION_LIST_BY_PMS_ID = 9.
- **CRASH (live predicate, caller binds nothing — param-not-found 500):**
  GET_DISCHARGE_SUMMARY_FIELD_MASTER_LIST, GET_ACTIVE_TEXTUAL_RANGE_MASTER_LIST, GET_DESIGNATION_MASTER_LIST,
  GET_LIST_OF_CATEGORY_WISE_QUALIFIERS, GET_SLIDE_LIST_FOR_RE_SLIDE_CREATION = 5 (org-22 set).
- Verdict on the clinical 11: NONE are legitimately cross-unit (all unit-scoped by design). The legitimate
  cross-unit class (patient-identity search) is in the unit-97 set (GET_PATIENT_LIST_LIKE family) — judgment pending.
- All endpoints checked: NOT on the JWT whitelist (authenticated). Effort anchors: five-screen batch
  00:28-06:05 (+merge 09:51) ~= 5.6h for 5 B-class screens (~35 methods); sex screen ~1h fix (forensics one-off).

## QF-2026-08-05-I — PHI-leak chain traces + patient-safety finding (3b-revised tasks 1-3)

- GET_DIAGNOSIS_BY_GENDER_ID chain RESOLVED: DiagnosisMapperController POST /getDiagnosisByGenderId
  (JSON DTO) -> DiagnosisMapperServiceImpl -> DiagnosisMapperDaoImpl; **WHITELISTED (auth-exempt) at
  JwtTokenFilter:98+389** — silent + unauthenticated read of the diagnosis dictionary (reference, not PHI)
  — the BR-171 unauthenticated class. Frontend callers: radiologyResultValidationController.js,
  medicalHistoryTabController.js, IPDCoverSheetController.js. All other clinical endpoints: NOT whitelisted.
- GET_VITAL_MASTER_TO_SYNC_BY_ID chain RESOLVED: NO live caller — interface
  IAppointmentVitalMapperDAOForSync + impl getVitalForSyncById(recordId) (binds only id; org/unit
  predicates commented) — the only injection is ScheduledLogsScheduler:77 (declared, NEVER invoked).
  DEAD SYNC PATH — latent id-keyed unscoped defect, NOT a running leak (the earlier "callers=1" was the
  DAO's own internal query call). Reversal: 2 of the 3 named PHI leaks are live running leaks; vitals is not.
- GET_CONSULT_DOCTOR_LIST_BY_ADMISSION_ID: LIVE — ConsultDoctorController POST /consultDoctorListByAdmission
  (frontend: patientOrdersWorkbench.js, updateDischargeSummaryController.js, masterDischargeSummaryController.js)
  — silent (org+unit predicates commented, no bind), authenticated, admissionId-keyed. RUNNING PHI leak.
- CHECK_ALREADY_EXIST_REG: LIVE — MortuaryController POST /saveDeathRegistation — silent, authenticated,
  name-keyed dup-check on death registrations. RUNNING PHI (names) cross-tenant oracle.
- PATIENT-SAFETY FINDING (medication history): MedicationHistoryDaoImpl.getMedicationHistoryList:229
  calls getNamedQuery("GET_MEDICATION_HISTORY_LIST") which THROWS ("No query defined" — entire query
  commented) — the empty catch blocks (243-249) swallow it into a NULL return; the coversheet save
  SUCCEEDS without the medication-history section; the frontend (medicalHistoryTabController.js:6065-6075)
  then() has NO error callback — completeConsultation runs. Net: a clinician completes the consultation
  with the medication-history section silently absent — the exact incomplete-information scenario.
  Same method also builds raw SQL via getQueryString->createSQLQuery (params ARE setParameter-bound —
  not currently injectable, fragile shape).

## QF-2026-08-05-J — Coversheet patient-safety finding: THIRTEEN sections, not one (medication-history establishment)

- Section visibility: ipd-CoverSheet-new.html:742 — the "Drug History" section (label + content) is gated
  `ng-show="detailedMedicationsHistory.length>0"` — **vanishes entirely when empty; no label, no placeholder,
  no "none" marker**. A clinician cannot distinguish "no medications" from "query dead". Same shape for the
  other history sections (ng-if/ng-show on list length).
- Query history: GET_MEDICATION_HISTORY_LIST is commented in the repo BASELINE (96b75c49 — the file has no
  other history) — the disable predates this repo; cannot confirm deliberate vs accidental from git.
- Coversheet-DAO sweep (the 13 patient-history read queries): **2 DEAD (medication history, personal history)
  + 11 CRASH (live org/unit predicates, NO bind -> Hibernate param-not-found 500)** — all 11 have exactly one
  live caller file (the coversheet DAOs). The house catch shape is FALSE-SUCCESS MASKING, worse than null:
  error -> `Response(SUCCESS, COMMON_SUCCESS, null)` — the frontend sees success + empty and hides the section.
- Net: the coversheet's patient-history read path is silent-blank across drug/medical/surgical/treatment/
  travel/family/personal history, allergies, vitals, habits, old-encounters, resolved sets — a clinician can
  sign off on a largely blank clinical summary. THIS IS THE PATIENT-SAFETY FINDING (larger than one query).
- Diagnosis-by-gender whitelist (item 4): all three frontend callers (radiologyResultValidation,
  medicalHistoryTab, IPDCoverSheet) use GenericService.serviceAction — GenericService.js attaches
  `Authorization: Bearer <authToken>` (lines 12/61/190/257) — **all authenticated internal screens; the
  /api/opd/getDiagnosisByGenderId whitelist entry (JwtTokenFilter:98+389) is STALE — removal is the fix,
  BR-171 pattern**. Not fixed (report only).

## QF-2026-08-05-K — Operational exposure + false-success census (repo evidence only)

- Q1 deployment evidence: running config = jdbc:postgresql://localhost:5432/his_local (HIS-WAR jdbc.properties);
  legacy/devops/ carries SEVEN named-hospital preprod config TEMPLATES (athaayupreprod, oysterpreprod, queenspreprod,
  meditestpreprod, nch, mrrch, bpp) — all `jdbc.url=CHANGE_ME_SET_LOCALLY_NOT_IN_GIT` (placeholders, no real hosts
  in git); docker-compose = nginx+tomcat; legacy/Deployment/ = HIS-DDL-20211019 + StandAlone-Instance-Master-Data
  (DDL snapshot dated 2021-10-19). Cannot determine from repo: whether any environment currently serves real
  patients (no prod configs, no live hosts, no ops evidence). The coversheet path is the same code in any
  environment this build runs.
- Q2 timeline: baseline commit 96b75c49 dated 2026-07-15 09:21:36 "Baseline before upgrade"; the 11 CRASH entity
  files (PatientMedicalHistory, AppointmentVitalMapper, AllergyPatientMapper, ...) have ONLY the baseline commit —
  the commented/unbound state arrived whole in the baseline; nothing changed it in this repo; pre-baseline
  history not in the repo. No changelog/migration doc references the coversheet history sections.
- Q3 shape sweep: 442 ng-show/ng-if length-gated sections across 79 views; clinical decision-read views:
  ipd-CoverSheet-new (43) + ipd-CoverSheet (68) + opd-coversheet-new (48) + opd-coversheet (56) [215 gates on the
  coversheet family alone], update-discharge-summary (24) + discharge-summary (24), input-output-chart-history (8),
  nursing-coversheet (2), growth-chart (1). Per-screen DAO-join confirmation pending beyond the confirmed
  13-section coversheet read path (same template family as discharge summary). SME_GAP_ANALYSIS:16 corroborates —
  allergyTypeMaster is a clinical-safety path "surfaced via coversheet alert icon"; the allergy section read
  (GET_CURRENT_LIST_ALLERGY_PATIENT_MAPPER_FOR_COVERSHEET) is one of the 11 CRASH.
- Q4 false-success census: Shape A (catch -> return new Response(SUCCESS...)) = 15 (Global 11, Abdm 2, Billing 1,
  DietAndKitchen 1) — incl. the "MyLife sync" save-side catches in the coversheet DAOs (MedicationHistory,
  AllergyPatientMapper, AppointmentVitalMapper, PatientFamilyHistory, PatientMedicalHistory) + RejectedDietListDaoImpl.
  Shape B (catch -> silent fallthrough) raw match = 9,463 (Global 6,624, ADT 2,299, Billing 350, ...) — OVER-INCLUSIVE
  (includes void-method catches where fallthrough is normal + nested-brace truncation); the meaningful subset is the
  Response-typed DAO reads (coversheet family confirmed); a Response-typed refinement is the next census pass.

## QF-2026-08-05-L — CORRECTION: coversheet 13 re-classified (both traces overturn the fear)

- The earlier "2 DEAD + 11 CRASH" (QF-J) was WRONG — my caller-bind pass botched the multi-line
  createQuery chains (reported "no bind" when the DAOs DO bind organizationId/patientId/created_date).
  Corrected per-query predicate-vs-bind read: **12 of 13 coversheet queries are LIVE (org-scoped,
  unit-UNSCOPED — all live predicates bound, queries run); 1 is DEAD (GET_MEDICATION_HISTORY_LIST,
  entirely commented).** The personal-history DEAD label was also wrong (live block).
- Consequence: the coversheet sections are NOT crash-blank — they render org-scoped data. The
  silent-blank safety defect is the SINGLE Drug History section (dead query -> DAO null fallthrough ->
  ng-show gate hides the section). The vanish-without-placeholder ambiguity remains for all length-gated
  sections but only Drug History is actually broken-empty.
- Item 2 allergy trace: the Allergies section is LIVE (org-scoped, unit-unscoped; binds patientId+org+
  created_date) — NOT crash-blank. The alert icon path is a separate query (GET_PATIENT_ALLERGY_LIST_FOR_ICON,
  fully org+unit+patient scoped, all bound) via GET /getPatientAllergyListForIcon — unitId from
  @RequestHeader (client-supplied, unverified vs token — the carried VIS-risk class; not whitelisted,
  authenticated). The clinician sees the allergy icon + section data; the defect is the unit-unscoping +
  client-org (security class), not a blank section.
- Item 1 discharge-summary trace: history sections populate from the SAVED template grid
  (dischargeSummaryTemplateListdata, tableTypeId-keyed) — NOT the coversheet read path. Discharge summary
  does NOT share the Drug-History blank; it shares the consult-doctors PHI leak + the discharge-field
  config silent leaks (BY_ID/BY_NAME/BY_NAME_NOT_ID) + the CRASH LIST query.
- The org-scoped-unit-unscoped state of the 12 IS a cross-tenant security defect (same-org cross-unit
  patient reads + client-sourced org values) — the org-trust Pattern A class — now precisely enumerated.

## QF-2026-08-05-M — Correction verified end-to-end (vitals trace) + the lesson

- Verification trace (GET_LIST_OF_VITALS_BY_PATIENT_ID_FOR_COVERSHEET, the least-confident LIVE label):
  query effective WHERE = organizationId=:organizationId AND patientId=:patientId AND status='A' AND
  value!='' AND isEnteredInError='N' (NO unit predicate; org also in the JOIN's `with ...` clause);
  DAO AppointmentVitalMapperDaoImpl.getListOfAppointmentVitalMapper binds organizationId+patientId
  (both live predicates bound) -> the query RUNS. Catch: rethrows HibernateException (honest); generic
  catch falls to a `Response(SUCCESS, null)` tail — a false-success shape that only fires for the method's
  OTHER queries (the formDate/toDate/vitalId groups — latent, not the coversheet query). Gate: the
  "Vital Assessment" LABEL renders unconditionally; data rows ng-repeat per-encounter. Endpoint:
  POST /api/opd/getListOfVitalsByPatientIdForCoversheet (IPDCoverSheetController.js:10900-10919).
  VERDICT: LIVE org-scoped unit-unscoped — the corrected picture stands (12 LIVE + 1 DEAD medication history).
- LESSON (recorded): a classification derived from a pattern pass is a HYPOTHESIS until a single instance
  is traced end to end — query text, effective WHERE, every bind in the DAO method, catch shape, gate,
  and what renders. The 13-section coversheet failure never existed; the allergy + vitals end-to-end
  traces are what caught it. Two passes produced two wrong classifications (11 CRASH then 2 DEAD+11 CRASH);
  the end-to-end trace is the arbiter, not another pass.

## QF-2026-08-05-N — BR-172 + clinical-leak branch: verification results + two more premise corrections

- BR-172 premise REVOKED (third overturn, same direction): GET_MEDICATION_HISTORY_LIST is LIVE via its
  @NamedNativeQuery twin (the HQL twin is commented; getNamedQuery resolves to the native — org+unit+patient
  scoped, all binds matched). The Drug History section was never dead; the local DB simply has 0 medication-history
  rows. Fixed: raw createSQLQuery shape -> direct named query; coversheet service error-drop propagated (both
  save+update). Frontend empty-state = PROPOSED only (own gate).
- Pre-fix live captures (BR-170/171 pattern, org-1 token, victims seeded org 999):
  (1) consult-doctors: POST /api/opd/consultDoctorListByAdmission {admissionId:999999} -> 200 + victim consult row
  incl. patientName 'PRE_01 Victim Consult999' (org 999) — LIVE cross-tenant clinical read.
  (2) death-reg: POST /adt/saveDeathRegistation {Victim/DeathReg999/45/1} -> 409 'Already Exists.' — the org-999
  registration matched cross-tenant — LIVE existence oracle.
- Post-fix: (1) -> listObject [] (0 rows); (2) -> NOT 409 (the check no longer matches the org-999 victim; the
  scoped query returns only same-tenant rows; a subsequent 409 was the dup-check correctly catching a same-tenant
  duplicate the first post-fix call inserted — cleaned). Diagnosis-by-gender: no-token -> 401 (after the filter
  null-header hardening — pre-hardening it NPE'd into a 200-error-JSON); authenticated -> org-scoped success.
  Vitals sync: bytecode-verified (deployed query carries org+unit predicates; 3-param DAO signature).
- Local-DB drift repaired (BR-155 class, additive per HIS-DDL-20211019): t_outside_hospital_doctor_mapper missing
  (created) + opd.t_consult_doctor missing outside_hos_doc_id (added) — the consult-doctors endpoint 501'd
  (relation does not exist) until repaired.
- Regression checker PASS pre- and post-deploy (all closed BRs intact).

## QF-2026-08-05-O — NamedNativeQuery twin population + JwtTokenFilter throw-reach (analysis, no fixes)

- TWIN POPULATION (sweep tools/twin_sweep_analysis.py): **55 query names appear in BOTH a commented block
  and a LIVE declaration** (the MedicationHistory GET_MEDICATION_HISTORY_LIST shape — commented HQL twin +
  live native twin). Every pattern pass that read the commented block concluded dead/wrong. Plus **18
  live-duplicate names** across file pairs (incl. the known BR-170 GET_PRIORITY_LIST entity twins).
- Class-2 census impact: **6 of the 22-org findings + 9 of the 97-unit findings (10 distinct) carry a live
  twin → their 'DEAD (entire query commented)' classifications are invalidated** (the name resolves to the
  live twin, which RUNS): DIAGNOSIS_AUTO_FILL_SEARCH, GET_ACTIVE_DISCHARGE_SUMMARY_FIELD_MASTER,
  GET_DETAILED_SERVICE_LIST, GET_MEDICATION_HISTORY_LIST (confirmed live), GET_ROUTE_OF_ADMINISTRATION_LIST,
  GET_ICPC_TEMPLATE_MAPPING_COUNT (org-22) + GET_BED_CATEGORY_LIST_BY_ORG_UNIT,
  GET_CURRENT_PATIENT_PERSONAL_HISTORY_DEATILS_FOR_COVERSHEET,
  GET_PATIENT_DIAGNOSIS_BY_PATIENT_ID_FOR_MEDICAL_HISTORY, GET_PATIENT_PERSONAL_HISTORY_DEATILS (unit-97).
  Each twin's own scoping state is the follow-on (Rule 10: per-instance trace).
- FILTER THROW-REACH (JwtTokenFilter, pre-auth): ALL throw paths FAIL CLOSED — (a) non-numeric userId/unitId
  headers -> NumberFormatException -> generic catch -> 401; (b) domain/Origin rejection -> ApplicationException
  -> 502; (c) malformed token -> JwtException in extractUsername/validateToken -> 401; (d) missing headers
  short-circuit to the final block -> 401 (post null-header fix; pre-fix the null Authorization header NPE'd
  into a 200-error-JSON). The unitId/userId headers must match the token's claims (validateToken compares),
  so a forged header is rejected. The ONLY pass-throughs are the two intentional auth-exempt lists — the
  second list carries fail-open-relevant entries to review before any merge: broad substrings
  "update/service" and "opd/saveMedication", the GEO cache endpoints (auth-exempt + org-blind geo caches),
  and "/api/refreshAllCaches". None of the four branch items' endpoints are in either list.

## QF-2026-08-05-P — Class-2 census UNVERIFIED correction + whitelist write-path exposure + twin cross-ref (analysis, no fixes)

- CORRECTION (explicit): the 55-twin finding invalidates 10 distinct Class-2 classifications (6 of the
  org-22 list + 9 of the unit-97 list, 5 overlap). **The remaining 22-org / 97-unit counts are UNVERIFIED,
  not merely un-invalidated** — the census method (query-block scan + caller-bind pass) demonstrably
  misreads the twin shape (it classifies the commented HQL block while runtime resolves getNamedQuery to
  the live native twin), so NO Class-2 count may be trusted until each finding is re-checked against the
  twin sweep. Cross-ref (tools/class2_twin_crossref.py): org-commented 5 twin-hit + 11 standing;
  unit-commented 9 twin-hit + 87 standing; 0 findings fall in the 18 live-duplicate pairs. The 10
  twin-hit names need per-instance Rule-10 traces (NOT authorised yet). Standing hypotheses remain
  hypotheses, but the twin shape does not invalidate them.
- WHITELIST CORRECTION: JwtTokenFilter has ONE auth-exempt list — a 196-entry requestUrl.contains(...)
  condition (the else-if at :365), with the first ~11 entries duplicated in the line-76 auth-block
  condition. Earlier "second auth-exempt list" framing = the same list's tail; corrected here.
- WHITELIST WRITE-PATH EXPOSURE (tools/whitelist_fragment_matches.py): ALL 196 entries are SUBSTRING
  matches (contains). The list exempts WRITE endpoints from auth (a no-header POST passes through):
  POST /kiosk/saveAndPay + /kiosk/saveAndPay/future + /kiosk/saveAndPayAppointment (kiosk billing —
  mission-critical billing path), POST /update/service (service-master write), POST /patientRegistration
  (PHI write), /api/opd/saveMedication + saveMedication* family (PHI write), POST /deleteParticipants +
  /api/deleteParticipants, POST /createOrganizationFromWeb, POST /updatePatientRegistrationFromMyLife,
  POST /patientDoc/saveAnalyzedReport, POST /saveMyLatestMedicalSummaryCallBack, POST /saveAIAppointmentInQueue,
  POST /glablServiceMaster + speciality variants (typo'd paths), POST /getSerivceRateGlobalList (typo'd),
  POST /updateNABH, POST /api/saveTwilioMsg, GET /refreshAllCaches (side-effect write), POST /resultUrl,
  POST /validationResultResponse, POST /getBillNoByPatient. Broad-substring entries with
  unintended-match potential: "opd/saveMedication" (prefix-matches the saveMedication* family),
  "patientRegistration" (matches the whole controller family), "/br/", "unit/org/", "/decrypt".
  **Severity: WORSE than diagnosis-by-gender (which was a read)** — the kiosk billing + service-master
  writes are unauthenticated WRITE paths on the mission-critical billing surface. Re-prioritisation
  recommended. GEO endpoints (158-167): auth-exempt AND backed by the org-blind geo caches — combined
  exposure = unauthenticated, tenant-blind reads of org-specific geo reference data (states/districts/
  cities/areas) — reference data, not PHI; the tenant-blind cache serves any org's rows to any caller.

## QF-2026-08-05-Q — saveMedication unauthenticated-write TRACE (Rule 10, end to end) — write does NOT persist

- FILTER PATH (no-token POST /api/opd/saveMedication, line by line): entry (doFilterInternal:43) ->
  authorizationHeader null (:47) -> requestUrl "/api/opd/saveMedication" (:49, context path "" — ROOT
  deployment — the URI is the full path) -> splitUrl length 4 (:50) -> domain check passes (:52-62) ->
  origin skipped (no Origin header) -> userId=0/unitId=null (:69-70) -> the auth block SKIPPED (unitId
  null, :76) -> the else-if (:365): splitUrl>2, no /resources/, no /views/ ->
  requestUrl.contains("opd/saveMedication") — "/api/opd/saveMedication" CONTAINS it (the whitelist is a
  196-entry contains() list DUPLICATED in the line-76 condition AND the else-if; the entry at :590) ->
  chain.doFilter (:620). The request reaches the controller with NO auth. PROVEN LIVE: the responses were
  the controller's (400/200), never a 401.
- DOWNSTREAM AUTH: NONE. rest-dispatcher-servlet-security.xml — the ONLY enforcing <http pattern="/**">
  block (intercept-url + resourceServerFilter) is COMMENTED OUT; the security="none" declarations are
  no-ops without it; the controllers carry no @PreAuthorize (global-method-security has nothing to
  enforce); the other filters (rateLimit, CORS) do not auth.
- WHAT THE ENDPOINT DOES WITH NO SESSION: MedicationController.saveMedication(@RequestBody
  PrescriptionDto) -> iMedicationService.saveMedication -> the DTO carries unitId/organizationId/
  patientId/doctorId (ATTACKER-CHOSEN — the endpoint needs no filter-derived userId/unitId). The
  isFromOP branch: the service's response is DISCARDED (controller lines 75/82) + a HARD-CODED
  Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE) is returned (:86) — "Added Successfully." REGARDLESS.
- LIVE PROOF: no-token POST (no Authorization/userId/unitId headers) with the DTO-correct payload ->
  HTTP 200 "Added Successfully." -> pharmacy.t_prescription 0 rows, pharmacy.t_prescription_detail 0
  rows, BOTH sequences still at 1 (the INSERT NEVER RAN). The service's new-prescription path exits at
  the prescription-number generation (iGlobalUnitMasterDao.getGeneratedNo(0, unitId, PRES_NO) -> if
  null/empty -> ERROR "Prescription Prefix is Not Configured", MedicationServiceImpl:204-206; the dev
  DB's unit 1 has no configured prefix/encounter) — the ERROR return is DISCARDED by the controller,
  which returns the hard-coded SUCCESS. No exception logged today (the last "saveMedication failed" in
  catalina.out is 2026-07-21) — the exit was the ERROR return, not a crash.
- VERDICT: the unauthenticated WRITE does NOT persist on the dev DB. The protection is NOT security —
  it is (1) the per-flow prefix-configuration dependency (a configured unit WOULD generate the number
  and the INSERT would persist — the write is real in a configured environment) and (2) the false-success
  masking (the controller's hard-coded SUCCESS — a Shape-A instance at the controller level — the
  response lies). This protection does NOT cover the other 196-list write fragments (kiosk/saveAndPay,
  update/service, patientRegistration, ...) — each has its own flow; the fragment scan conflated
  unauthenticated REACHABILITY (real for all 196) with write PERSISTENCE (per-flow-dependent). The
  realistic-write candidates (the app's daily flows — kiosk billing) need their own Rule-10 traces.

## QF-2026-08-05-R — saveMedication verdict restated + BR-173 structural security finding

- saveMedication TRACE VERDICT (restated per owner): unauthenticated reachability PROVEN (the whitelist
  pass-through; the request reached the controller, never a 401); the write is blocked by ENVIRONMENT
  (the dev unit's unconfigured prescription-number prefix), NOT by design — on a unit where the prefix
  is configured, the same unauthenticated POST PERSISTS (the INSERT would run), and the controller
  returns SUCCESS either way, so it would land silently. REAL VULNERABILITY WITH A COINCIDENTAL
  MITIGATION on this dev DB.
- BR-173 — AUTHENTICATION-GATE STRUCTURAL FINDING (own BR): rest-dispatcher-servlet-security.xml's
  ONLY enforcing block — <http pattern="/**"> with intercept-url + resourceServerFilter — is COMMENTED
  OUT; every security="none" declaration in that file is a NO-OP without it; the controllers carry no
  @PreAuthorize; the sole authentication gate for ALL of /api/** (and every other path) is JwtTokenFilter
  plus its 196-entry requestUrl.contains(...) substring whitelist (the list duplicated in the line-76
  condition AND the else-if). Unauthenticated reachability of any whitelisted endpoint is PROVEN
  (QF-2026-08-05-Q: saveMedication). The gate's effectiveness is exactly the whitelist's precision —
  substring matching with broad fragments ("update/service", "opd/saveMedication", "patientRegistration",
  "/br/", "/decrypt") + auth-exempt WRITE endpoints (kiosk/saveAndPay, update/service,
  patientRegistration, deleteParticipants, ...) — see QF-2026-08-05-P.

## QF-2026-08-05-S — kiosk saveAndPay unauthenticated-write TRACE (Rule 10, end to end) — write does NOT persist on the dev DB

- FILTER PATH (no-token POST /api/billing/kiosk/saveAndPay): identical to QF-Q — the whitelist's
  "api/billing/kiosk/saveAndPay" entry matches the URI (context path "") -> chain.doFilter. PROVEN LIVE:
  the controller responded (400/500), never a 401.
- WHAT IT WRITES: BillingMasterController.saveBillFromKiosk -> (a) Razorpay verification ONLY for
  paymentModeId==8 (server-side verifyRazorpayPayment — a DESIGNED protection for that mode; the other
  modes cash/card/cheque are trusted as-is — a deliberate choice per the in-code comment), (b)
  iBillingMasterService.saveBillFromKiosk -> patient registration (patientId 0 only) -> markArrival ->
  saveEncounterMaster (opd.t_encounter_master) -> the bill save (billing.m_billing_master) ->
  collectPayment. NONE of the writes reached.
- CONFIG DEPENDENCIES: (1) the encounter service's unit-application-billing-config lookup
  (EncounterMasterServiceImpl:296-307 — ERROR "Default tariff is not defined !!" / "Opd Billing Category
  Id is not defined !!" when the config is absent — a DESIGNED flow requirement), (2) the bill-number
  generation (getGeneratedNo -> public.m_prefix_configuration — the dev DB has ZERO prefix-config rows
  for unit 1). Both are config-row dependencies; the dev DB satisfies neither — ENVIRONMENT.
- LIVE PROOF: pre-counts billing.m_billing_master=2 (pre-existing), opd.t_encounter_master=0 -> no-token
  POST (minimal + DTO-correct payloads; encounterDate is a Long — epoch millis) -> 500
  "Something went wrong. Reference: ERR-7f32ae29" — the stack: BillingException "Cannot invoke
  EncounterMasterDto.getEncounterId() because encResObj is null" at BillingMasterServiceImpl:2933 (the
  encounter save returned an error/null object — the config check failed) -> post-counts UNCHANGED (2/0)
  — NO INSERT, no sequence advance.
- VERDICT: same class as saveMedication (QF-Q): unauthenticated REACHABILITY real; the write is blocked
  by ENVIRONMENT (missing billing-config rows + missing prefix-config rows), not by design; in a
  configured environment (any real deployment — the app cannot bill without these rows) the same
  no-token POST would persist. REAL VULNERABILITY WITH A COINCIDENTAL MITIGATION on this dev DB. The
  Razorpay mode is the one designed protection (server-side verification); cash/card/cheque are
  unverified by design.

## QF-2026-08-05-T — BR-173 scoping analysis (no code): history, 196-entry intent, options, payment flag

- HISTORY (why is the Spring Security block commented out?): rest-dispatcher-servlet-security.xml's ONLY
  commit is the baseline (96b75c49 "Baseline before upgrade", 2026-07-15). The commented-out
  <http pattern="/**"> block PREDATES the baseline; no commit message, doc, or in-file comment explains
  it. The upstream history before the baseline is not in the repo. Re-enabling is NOT a config flip: the
  block enforces OAuth2 access tokens (resourceServerFilter + InMemoryTokenStore + the clinivantage
  client), while the app's real auth is the custom JWT (userId/unitId headers + JwtTokenFilter) — the
  app has drifted away from the OAuth2 design; a re-enabled block would reject every request (the JWTs
  are not OAuth2 tokens) unless a token-compat adapter is built.
- 196-ENTRY INTENT (tools/whitelist_intent_classification.py): PUBLIC 45 (login/token/forgot-password/
  static/swagger/Report), INTEGRATION 22 (ABDM, MyLife, Razorpay callbacks, Twilio, webhooks — these
  need auth of a different kind, not none), KIOSK 47 (webappointment/self-service/ForWebPage/geo/vital
  uploader — public-facing by product design, but includes the kiosk billing WRITES + patient
  registration), STALE 63 (the diagnosis-by-gender class: every caller is an authenticated screen —
  the entries only remove auth), UNEXPLAINED 19 (update/service, opd/saveMedication,
  api/global/patientRegistration, /br/, unit/org/, /decrypt, resultUrl, validationResultResponse,
  deleteParticipants family, roomRent, getSerivceRateGlobalList, glablServiceMaster, updateNABH,
  checkParti/, a hardcoded external URL, a hardcoded dataroom_41015 path, /api/refreshAllCaches).
  Defensible-ish 114 (45+22+47, with the caveats: integration needs secrets; kiosk includes writes);
  accidents 82 (63 stale + 19 unexplained) = 42% of the list.
- OPTIONS (analysis only, no recommendation): (A) exact/anchor-pattern matching instead of substring —
  fixes the broad fragments; risks the parameterized/trailing-slash entries (needs an Ant-style matcher);
  verify via a live per-endpoint sweep; medium. (B) re-enable the Spring Security chain — framework-grade
  enforcement; risks total breakage (token-format incompatibility) without an OAuth2-JWT adapter; verify
  via the full harness; HIGH. (C) split the list by auth type (anonymous / integration-with-secret /
  kiosk-rate-limited / nothing) — the integration callbacks get real auth, the stale entries disappear;
  risks partner-callback breakage (secret deployment); medium-high. (D) default-deny with an explicit,
  per-entry-owned allowlist — the structural inversion; risks any missed entry breaking a screen; verify
  via the full Step-6 + harness sweep; medium.
- PAYMENT DESIGN FLAG (design question for the billing owner, NOT a defect): the in-code comment makes
  cash/card/cheque deliberately unverified while Razorpay gets server-side verification — coherent for an
  AUTHENTICATED kiosk operator, incoherent on an UNAUTHENTICATED endpoint (the kiosk saveAndPay is
  whitelisted). Whoever owns the billing flow should decide the kiosk payment-trust model (verify all
  modes server-side, or authenticate the kiosk device).

## QF-2026-08-05-U — 82-entry caller audit (BR-173 next-branch prep; analysis only, no removals)

- RULE 10 CONTROL (tools/whitelist_caller_audit.py): the search pattern validated against the known
  positive FIRST — GET_MEDICATION_HISTORY_LIST's caller found at IPDCoverSheetController.js:23415 with
  serviceAction in the context window. CONTROL PASS — the negatives are trustworthy.
- AUDIT (82 = 63 STALE + 19 UNEXPLAINED): verdicts — **30 SAFE TO REMOVE + 3 NOT STALE + 49 NO CALLER
  (dead)**. Per-entry callers + shapes in the tool output (tools/whitelist_caller_audit.py, re-runnable).
- SAFE (30) — every frontend caller goes through GenericService.serviceAction (token-attached): the
  coversheet/medication family (getMedicationHistoryList, getListOfVitalsByPatientIdForCoversheet,
  saveMedication + opd/saveMedication, getFrequencyList, saveInvestigation, saveFreeTexPatientDiagnosis,
  savePatientCancelledDiagnosis, getComplaintAppointment, coversheetInstruction,
  updateCoversheetInstructionStatus, updateComplaintAppointmentStatusToRemoveComplaint,
  getMasterListOfICPCSystemForCoversheet, getCoversheetAsTemaplateList, getTemplateDataByCovTempId,
  updateFollowUpDateOfPatient, getActiveInspectionMasterList, /api/globals/frequency,
  getOPDMedicationListById, saveJustificationForMedicationDelete, getCurrentPatientFamilyHistory/
  Allergy/SurgicalHistoryForCoversheet, searchFreeTextComplaint, serchFreeTexPatientDiagnosis) +
  UNEXPLAINED-with-real-serviceAction-callers (update/service, /global/serviceMaster,
  /api/global/glablServiceMaster, /getSerivceRateGlobalList, /unit/org/).
- NOT STALE (3) — reclassified, caller token-behaviour unverified: /adt/discharge/dischargeSummaryCard
  (6 callers in the discharge controllers, unverified shapes), /unit/subcription (5 callers —
  serviceAction at hospitalMasterController x4 + selfRegistrationController:4704 unverified),
  /api/unit/clinical/getDoctorFavouriteMedicationForCoversheet (the only hit is a URL-constant
  definition in global.url.config.js — the real callers use the constant; needs the constant-trace).
- NO CALLER (49) — dead exemptions: the search (exact fragment in JS/HTML + Java non-mapping, the
  JwtTokenFilter's own lines and commented lines excluded) found no caller. Misnaming check (Rule 10):
  the NewEMR-family entries' STEMS are used by the frontend under DIFFERENT URI forms —
  getPatientDiagnosisByPatientIdForIcon x32 / ForMedicalHistory x11 / ForBookView x1 (the ForNewEMR
  form never called), getInvestigationsListForCoversheet x19, getMedicationDetailsByDoctorId x13 —
  so the entries are dead exemptions (the variant callers are token-authenticated; removal does not
  affect them). The unexplained dead ones: /br/, /decrypt, /updateNABH, /checkParti/, the
  deleteParticipants family (5), the hardcoded generateFlipbook URL, /global/getPopUpDataWithId,
  /roomRent (the only java ref = the controller's own @RequestMapping + a commented call), and
  /api/refreshAllCaches.
- CAVEAT: the audit's shapes are the hypothesis layer (the window-based serviceAction/raw detection +
  the config-constant callers). The removal branch must confirm the per-entry callers (Rule 10) before
  each removal — the audit is the triage, not the proof.

## QF-2026-08-05-V — whitelist Phase-1 re-confirmation (fix/whitelist-stale-removal; no removals yet)

- METHOD (tools/whitelist_phase1_reconfirm.py, independent of the QF-U audit): Rule-10 control FIRST
  (the known positive GET_MEDICATION_HISTORY_LIST caller found with serviceAction context — CONTROL
  PASS); whole-tree search (js/html/java/xml/properties/json, target/ excluded) with the
  JwtTokenFilter's own lines, commented lines, and @RequestMapping/definition sites excluded; the
  URL-constant config dir searched separately; variant forms checked (ForIcon/ForMedicalHistory
  lesson). Per the Rule-10 standing note, this pass already differs from the audit's first pass in
  three ways (target-dir exclusion, the config-dir pass, the definition-site exclusions).
- 49 NO_CALLER -> **45 CONFIRMED DEAD + 4 REVIVED (has a caller after all)**:
  - /api/billing/bill/service/cancel — callers: IPDCoverSheetController.js:12372 + :21254,
    dayCareNurshingCoversheetController.js:9862 — all serviceAction (authenticated) — the entry has
    real callers (the QF-U audit missed them) — NOT a dead exemption; removal-safe only as an
    authenticated-caller entry (screen-verify in Phase 2 if approved).
  - /api/opd/medicationUpdate — callers: IPDCoverSheetController.js:18903,
    dayCareNurshingCoversheetController.js:16555, nursingCoverSheetController.js:17508 —
    serviceAction — same shape as above.
  - /br/ — caller: selfRegistrationController.js:5173 builds $scope.qrUrl = ... + "/adt/br/" + uniqueId
    (the QR endpoint; a browser/image load without a token) — the exemption is genuinely used.
  - /checkParti/ — caller: twilio-video.js:31032 — fetch(BASE_URL + ROOT_URL + '/checkParti/' + id) —
    a RAW fetch, no token — the exemption is genuinely needed.
- 30 SAFE -> all 30 have NAMED callers, all through GenericService.serviceAction (the wrapper attaches
  the token). The named-caller list is in the tool output (tools/whitelist_phase1_reconfirm.py,
  re-runnable). The 3 NOT_STALE entries untouched.
- CONFIRMED-REMOVABLE LIST (Phase 2, pending owner approval): the 45 dead + (if approved) the 30
  authenticated-caller entries — the 4 revived are NOT removable.

## QF-2026-08-05-W — Phase-2 verification STOP-AND-REPORT (45 removed, 35/45 -> 401, 10 exempted by OTHER rules)

- REMOVAL: 45 confirmed-dead entries deleted from BOTH copies (217/220 -> 167/170 contains-count,
  verified; 0 fragments remaining; argument-level line rebuild kept /br/ and /resultUrl which shared
  lines with removed entries). Deployed; bytecode check: 0 removed fragments in the deployed class.
- 401 VERIFICATION (tools/whitelist_phase2_verify.py): 35/45 no-token -> 401. The 10 non-401s are NOT
  removal failures (the fragments are gone) — each is exempted by ANOTHER rule:
  1. /api/opd/coversheetInstructionList/1 (405) — kept SAFE entry /api/opd/coversheetInstruction is a
     substring of the URL; endpoint exists (POST-only).
  2. /api/opd/saveJustificationForMedicationDeleteMultipleMedication (405) — kept SAFE entry
     /api/opd/saveJustificationForMedicationDelete is a substring; endpoint exists.
  3. deleteParticipants family (5 URLs: /HIS-WAR/deleteParticipants + /* + /** + /Patient/dataroom_41015
     -> 404, /api/deleteParticipants -> 405) — the BARE entry requestUrl.contains("deleteParticipants")
     at JwtTokenFilter:455 SURVIVED: it was never in the phase-1 lists (the 196-extraction dedup missed
     it — ninth pattern-pass miss; the classification's UNEXPLAINED bucket had the /HIS-WAR/ variants and
     /api/deleteParticipants but NOT the bare fragment). Its substring exempts the whole family; the
     /api/deleteParticipants controller EXISTS (POST) — an unauthenticated POST would reach it. Needs its
     own decision (follow-on removal or the later branch).
  4. /decrypt, /updateNABH, /roomRent (404) — the short-URL pass-through (else-if splitUrl.length <= 2 —
     1-segment URLs pass the filter by design) + the endpoints DO NOT EXIST (404) — removal had no
     observable effect (dead URLs either way).
- The regression checker, six harness specs, and the remaining spot-checks are PAUSED pending the owner's
  response to this report (per the stop-and-report instruction).

## QF-2026-08-05-X — Phase-2 battery resumed (post-removal deployment) — ALL PASS

- verify_deployed_regressions.py: PASS (every closed BR's fix signature in the running jars).
- Six harness specs (sex, relation, marital, religion, occupation, priority): ALL PASS (live,
  org-1 token). First background attempt hit an argparse usage error (transient/odd shell state);
  direct invocation verified all six individually.
- Live re-checks: consult forged admission 0 rows; death-reg unique-name not-409; diagnosis
  no-token 401 + token org-scoped success; coversheet med-history renders (Paracetamol present);
  login public; /adt/br/ QR passes unauthenticated.

## QF-2026-08-05-Y — whitelist gap reconciliation: 217 vs 220 vs 196 (tools/whitelist_gap_reconciliation.py)

- PRE-REMOVAL (verified from git a70d5a56^): copy1 = 217 raw occurrences = 197 unique + 20
  within-copy duplicates; copy2 = 220 = 199 unique + 21 duplicates. The 217-vs-220 gap (3) = copy2's
  TWO extra unique entries (/resources/, deleteParticipants — in the else-if, absent from the
  line-76 copy) + ONE extra duplicate-occurrence.
- POST-REMOVAL (current): copy1 = 167/152/15; copy2 = 170/154/16; union 154 unique.
- The 196 is a HAND-CURATED classification list, not an extraction: 196 + the 3 it missed
  (/resources/, /views/, deleteParticipants) = the filter's 199 actual unique pre-removal entries.
  The curated list is 3 short of the filter's reality; it also carries entry VARIANTS the filter
  does not have (e.g. /api/opd/saveMedication vs the filter's bare opd/saveMedication — both forms
  actually present).
- GAP (present in either copy, absent from the 196): /resources/, /views/, deleteParticipants (3).
- DRIFT (one copy only): copy2-only: /resources/, deleteParticipants (2) — the exemption asymmetry:
  the else-if (no-header path) exempts them, the line-76 auth-block path does not — a request WITH
  userId/unitId/Authorization headers to a deleteParticipants URL is NOT exempt in the line-76 copy,
  so it falls into the token-check; the header-less request passes via the else-if.
- Rule-10 control: known miss 'deleteParticipants' found in the gap AND known present
  '/adt/userLogin' found in the 196 + both copies — CONTROL PASS. The capture pattern also handles
  the double-paren form contains(("/views/")) (the /views/ entry). Nothing removed from the gap.
- Also surfaced during the reconciliation: the med-history family entry /api/opd/getMedicationHistoryList
  is in the filter but was never in the hand-curated 196 (the curated list's med-history coverage is
  partial: getMedicationDetailsForNewEMR + saveMedication only).

## QF-2026-08-05-Z — bare 'deleteParticipants' entry (HOLD — its own finding)

- The bare entry requestUrl.contains("deleteParticipants") (JwtTokenFilter:455, else-if copy) was
  missed by the 196 extraction (the classification had the /HIS-WAR/deleteParticipants variants and
  /api/deleteParticipants but not the bare fragment — the ninth pattern-pass miss).
- Its substring exempts the WHOLE family: /HIS-WAR/deleteParticipants (+ /* + /** + /Patient/dataroom_41015
  -> 404, dead paths) AND /api/deleteParticipants (405 on GET — the controller EXISTS, POST-only).
- Impact: an unauthenticated POST to /api/deleteParticipants reaches a LIVE controller (no-token
  request passes via the else-if's bare entry; the live POST behavior was not exercised — the hold).
- Per owner: NOT removed on this branch (never in the approved 45; no scope creep on a removal
  branch). Carries to the next branch with the 30, after the gap reconciliation's findings.

## QF-2026-08-05-AA — the copies' DRIFT is an exploitable auth asymmetry (recorded for the next branch)

- /resources/ and deleteParticipants are exempt on the ELSE-IF path (no-headers path) but NOT on the
  LINE-76 auth-block path. The same URL's auth outcome therefore depends on whether the request carries
  userId/unitId headers: a caller OMITTING headers takes the exempt else-if path; a caller sending
  headers lands in the line-76 token-check. Omitting headers is the shape an attacker uses by default.
- This is an EXPLOITABLE ASYMMETRY, not a copy-consistency bookkeeping issue. The line-76 copy's list
  must be brought in sync with the else-if's (or the duplication removed structurally) before the 30-entry
  branch's removals — the asymmetry persists while one copy lags.

## QF-2026-08-05-AB — the 196 was HAND-CURATED, not extracted (prep for the 30-entry branch)

- The classification's 196-entry list is hand-written, not extracted from the filter. It does not match
  the filter: 3 entries missing (/resources/, /views/, deleteParticipants), variant spellings
  (e.g. /api/opd/saveMedication vs the filter's bare opd/saveMedication — both forms actually present),
  and /api/opd/getMedicationHistoryList absent entirely.
- Every bucket count, ranking, and removal decision this session was made against that list. BEFORE the
  30-entry branch: (1) regenerate the entry list by EXTRACTION from the filter source, verified against
  a known positive; (2) re-run the intent classification against the real list; (3) state how the
  buckets change. The QF-U audit's 30 SAFE / 3 NOT_STALE verdicts were derived from the curated list and
  need re-confirming against the extracted one.

## QF-2026-08-05-AC — whitelist regenerated by EXTRACTION (analysis only, no changes)

TOOLS: tools/whitelist_real_list.py (authoritative extraction), tools/whitelist_rebucket.py,
tools/whitelist_reconfirm33.py. Section 5's remainder grouping was computed inline.

1. EXTRACTION (both copies, double-paren + multi-arg + || chains handled). Rule-10 control PASS
   (/adt/userLogin, bare deleteParticipants, /api/opd/getMedicationHistoryList all found).
   copy1 (line-76): 167 raw / 152 unique. copy2 (else-if): 170 raw / 154 unique. UNION 154,
   intersection 152, symmetric difference 2.

2. DRIFT MAP (QF-AA at population scale): copy1-only = 0; copy2-only = 2 — /resources/,
   deleteParticipants. Path semantics: both are exempt ONLY on the else-if path (the header-less
   branch); a request WITH userId/unitId/Authorization headers is NOT exempt in the line-76 copy and
   falls into the auth-block token-check. WRITE FLAG: deleteParticipants — the bare substring exempts
   the live POST /api/deleteParticipants controller; a header-less caller takes the exempt path and
   reaches the write — the exploitable asymmetry shape. /resources/ is static (no write).

3. RE-BUCKET against the extracted union (154): PUBLIC 47, INTEGRATION 22, KIOSK 47, STALE 29,
   UNEXPLAINED 9. vs the curated 45/22/47/63/19: +2 PUBLIC (/views/, /resources/ — never curated),
   -34 STALE and -11 UNEXPLAINED (the 45 removed entries, which the curated list still carries),
   +1 UNEXPLAINED (bare deleteParticipants). Entries never in the curated list: /views/ [PUBLIC],
   /resources/ [PUBLIC], deleteParticipants [UNEXPLAINED]. The curated list's 45 removed entries show
   as its dead weight.

4. 33 RE-CONFIRMED against the extracted list: 30 SAFE TO REMOVE (all present as spelled in both
   copies, named serviceAction callers), 2 NOT SAFE (/adt/discharge/dischargeSummaryCard — caller
   shape unverified; /unit/subcription — same), 1 SPELLING MISMATCH —
   /api/opd/getDoctorFavouriteMedicationForCoversheet is NOT in the filter; the filter's real form
   is /api/unit/clinical/getDoctorFavouriteMedicationForCoversheet (the curated form keyed to the
   wrong spelling would silently remove nothing). 0 DEAD.

5. UNAUDITED REMAINDER: 118 of 154 extracted entries (76%) have never had a caller audit:
   PUBLIC 47 (24 of them the swagger/static family: /HIS-WAR/swagger-ui.html, webjars/*, favicon,
   api-ui, configuration), INTEGRATION 22 (abdm/mylife/twilio), KIOSK 47 (kiosk/feedback/
   appointment/registration — the ENTIRE KIOSK bucket unaudited), STALE 1, UNEXPLAINED 1.
   The curated '114 defensible' was pre-removal bookkeeping; the extracted reality is 118 unaudited
   of 154.

CORRECTION to QF-AB: /api/opd/getMedicationHistoryList IS in the curated list (line 57, STALE).
The earlier 'absent entirely' claim came from a tuple-membership check bug (in e on a (entry,bucket)
tuple checks exact equality, not substring). QF-AB's other claims stand (3 entries missing —
/views/, /resources/, deleteParticipants — variant spellings, list hand-curated).

## QF-2026-08-05-AD — KIOSK bucket audit (47 entries; analysis only; tools/kiosk_bucket_audit.py)

- Method: per entry — caller search (JS/HTML/config, exact-fragment; named file:line), call shape
  (serviceAction vs RAW vs ?), controller mapping verb from the REAL annotation (first pass misread
  the mappings — the annotation-less lines mislabeled saveAndPay as read; corrected to the
  annotation-based verb), Rule-10 spot-checks on the 0-caller claims (scheduleAppointment's
  identifier hits are function-name noise, not endpoint calls). All 47 spellings match the extracted
  list (the curated KIOSK bucket is spelling-clean).
- WRITES (8): saveTemplateListDataForNewEMR (6 coversheet callers, ALL serviceAction — an
  AUTHENTICATED internal screen misplaced in the bucket); outPatientFeedback + inPatientFeedback
  (feedback controllers:201/227 + 295[?] — anonymous-feedback surfaces, serviceAction-without-token);
  saveAnalyzedReport (0 callers — the mylife external push); the two patientTypeHeadSearch*
  self-registration searches (POST-shaped but SEMANTIC READS — PHI-search, 0 callers);
  saveAndPay (kiosk — selfRegistration/appointmentScheduling/bookAppointment callers — the QF-S
  shape); patientRegistration (selfRegistration:2003/4574 — the self-registration create).
- THE FLAG — write AND no authenticated caller (the saveMedication/saveAndPay shape): FIVE writes —
  api/billing/kiosk/saveAndPay (kiosk no-login surface; config-dependent: tariff/OPD-category +
  bill-prefix, dev-blocked, would persist in a configured environment — QF-S), api/opd/patientDoc/
  saveAnalyzedReport (0-caller write; config unknown), the two feedback endpoints (anonymous
  surfaces; config unknown), api/global/patientRegistration (self-registration create; config
  unknown). PLUS the two patientTypeHeadSearch* POST-reads (a separate no-auth PHI-READ class, not
  the write shape). The tool's shape-based flag caught only saveAnalyzedReport + the searches —
  the kiosk/feedback/registration writes carry serviceAction TEXT but no login, so they needed the
  surface-level judgment.
- PUBLIC BY DESIGN (kiosk/web-appointment/registration/feedback surfaces): saveAndPay, feedbacks,
  patientRegistration, the patientTypeHeadSearches, scheduleAppointment/getDailySlots,
  the webappointment family (getDetailsForBookAppointment, getDoctors, getActivePrefixList/,
  doctorApp/getApplicationConfig, appointments/doctors*), the getActive* masters + address/ref-data
  reads used by the public registration forms.
- INTERNAL SCREENS MISPLACED IN THE BUCKET (authenticated internal callers): saveTemplateListDataForNewEMR
  (6 coversheet screens), patientDoc/details (NABH/incident reporting), growthMilestones/patient
  (coversheet + growth chart), getListOfDocumentForBookView + getInvestigationsListForBookView
  (bookView), getPatientDetailsForWorklistForWebPage (AdmittedPatientList), getCookieDataForNewEMR,
  getUserListForStore, getAppointmentsByPatientId, getLastEncounterDataOfPatientForWebAPP,
  preBilling/orderedServicesByMobileNoForWebUser, growthMilestones/detail, scheduleAppointment,
  getAvailableDoctorBySpeciality — 0-exact-fragment-caller entries, mostly internal, not patient-
  facing public surfaces.
- Config-dependency check: only saveAndPay is KNOWN config-dependent (QF-S trace); the other writes'
  config dependencies are NOT traced — each needs its own trace before any removal/decision.
- KIOSK-bucket conclusion: ~15 genuinely public, ~10 internal misplacements, ~22 masters/ref-data;
  the flag set (5 writes + 2 PHI-reads) is the decision surface for the next branch.

## QF-2026-08-05-AE — config-dependency traces on the 4 untraced writes + misplacement confirmation

PART 1 — THE 4 WRITES (filter path: all 4 pass the filter no-token; NONE WROTE on this box — the
stop-condition was not triggered):
1. api/global/patientRegistration (PatientRegistrationController.java:83 POST) -> the service's
   dup-check (CHECK_UNIQUE_PATIENT) + insert: patient.t_patient_registration + t_patient_details
   (session.save x2, patient.patient_registration_seq). Config-deps: the UHID prefix is HARD-CODED
   ("PC-18-"/"PR-18-"/"O-18-") — NO config row blocks the write. LIVE PROOF — PAYLOAD CRACKED (the mechanism, not a guess):
   - The dob's real format: the frontend builds it as moment(...).format('DD-MM-YYYY HH:mm:ss')
     (selfRegistrationController.js:1838) — the app's dozer date-format is "dd-M-yyyy HH:mm:ss"
     (global/dozer-bean-mappings.xml + the DAO's own GlobalCommonDateUtils usage). CONTROL (Rule 10):
     the feedback flow's createdDate with the same shape reached its INSERT (the 500 came at the
     sequence, after the parse); and the patientRegistration's own sequence advanced on every
     well-formed attempt — the parse and the INSERT's ID-generation demonstrably ran.
   - The flow's required fields in order: dob (String, dd-M-yyyy HH:mm:ss) -> appointmentDate (the
     isFromSelf='Y' branch's appointment — NPE "getAppointmentDate() is null" when absent, its
     substring(0,10) feeds getTokenNumber at PatientRegistrationServiceImpl:604) -> the INSERT's SQL.
   - THE BLOCK: the INSERT into t_patient_details fails on
     "ERROR: column "family_id" of relation "t_patient_details" does not exist" — a SCHEMA DRIFT
     in the dev DB (the entity expects the column; the dev schema lacks it) -> PSQLException ->
     "current transaction is aborted" -> rollback. Pre (2/1/seq4) -> Post (2/1/seq5): no row
     persisted; the sequence's non-rolled-back advance is the insert-attempt evidence.
   - PROOF-COMPLETENESS STATEMENT (owner): the sequence advance (4->5) is the insert-execution
evidence; validation did not stop it, a missing column did. The proof is COMPLETE WITHOUT adding
family_id. Adding the column was deliberately DECLINED — it would make an unauthenticated patient
insert live on this box, which is not authorised. The drift remains documented, not repaired.
VERDICT: the write-path is REAL (no config dependency anywhere — the UHID prefix is hard-coded;
     the payload is fully known) and is blocked ONLY by the dev DB's schema drift — the same
     drift-class as the feedbacks' missing sequences and t_message_queue's absence. It would persist
     in a schema-complete environment. This is the same class as saveAndPay/saveMedication in
     outcome (env-blocked) but NOT in mechanism: those were blocked by missing CONFIG rows the flow
     needs to do business; this one is blocked by a schema column the flow never configures.
2. api/opd/patientDoc/saveAnalyzedReport (EncounterController.java:1578 POST) -> UPDATE
   opd.t_encounter_doc_details (document_analyzed_path/date/json WHERE encounter_doc_id). LIVE PROOF:
   no-token POST -> 500 (Internal Server Error); the table has 0 rows -> the UPDATE can match nothing;
   write cannot persist here (env: empty table), would persist where rows exist.
3. /api/outPatientFeedback (OutPatientFeedbackController.java:19 POST saveOutPatientFeedback) ->
   session.save into public.out_patient_feedback (patientId/visitType/feedbackKey/Value/unit/org/
   encounter + status 'A'). NO config-lookup in the DAO. LIVE PROOF: no-token POST -> 500
   PSQLException "relation public.out_patient_feedback_seq does not exist" — blocked by DB DRIFT (the
   schema's sequence missing on this box), not by config; would persist in a schema-complete
   environment.
4. /api/inPatientFeedback — same shape as #3 (InPatientFeedback entity, same missing-sequence class).
   No live POST run (same drift block class as #3 — the trace stopped at the identical sequence
   dependency).

PART 2 — THE INTERNAL-MISPLACEMENT CONFIRMATION (14 candidates from QF-AD; the user's "~10"):
- 3 CONFIRMED misplacements (authenticated post-login callers only):
  saveTemplateListDataForNewEMR (8 coversheet callers: IPDCoverSheet, dayCareNurshing, nursing,
  erNursing, OPDCoverSheet, postOprativeCare — all post-login clinician screens);
  /api/opd/patientDoc/details (NABH + incident-reporting callers: nabhReporting, reportedNABH,
  incidentReporting, reportedIncident — post-login);
  /api/global/growthDevelopmentalMilestones/patient (OPDCoverSheet + growthChart — post-login).
- 2 RECLASSIFIED NOT-misplacements (a caller sits on the pre-login booking surface):
  /adt/getListOfDocumentForBookView + /api/opd/getInvestigationsListForBookView — bookViewMaster
  controller (route '/bookView', the pre-login book-view/booking surface). Per the rule: any caller on
  a pre-login surface -> NOT a misplacement.
- 9 NO-CALLER entries (not misplacements — the dead/no-caller class):
  getPatientDetailsForWorklistForWebPage, getCookieDataForNewEMR, getUserListForStore/,
  getAppointmentsByPatientId, getLastEncounterDataOfPatientForWebAPP,
  preBilling/orderedServicesByMobileNoForWebUser, growthDevelopmentalMilestones/detail,
  scheduleAppointment, getAvailableDoctorBySpeciality — 0 exact-fragment callers (spot-checked the
  identifier hits for scheduleAppointment = function-name noise, not endpoint calls).
- All spellings verified against the extracted list.

## QF-2026-08-05-AF — BR-173 STRUCTURAL PROPOSAL (decision-ready; analysis only)

GROUNDING (all from this session's extracted list): 154 unique entries = PUBLIC 47 + INTEGRATION 22 +
KIOSK 47 + STALE 29 + UNEXPLAINED 9; drift 2 (copy2-only: /resources/, deleteParticipants); the bare
deleteParticipants; 2 unauthenticated writes blocked only by env artifacts (patientRegistration:
family_id drift; saveAnalyzedReport: empty table); the config-mitigation on saveMedication/saveAndPay
was coincidence, not a pattern.

1. RECOMMENDATION — D (default-deny + explicit exact-matched allowlist).
   The filter becomes: URL in the exact-matched allowlist -> pass; else -> the EXISTING token-check
   (the JwtTokenFilter's own logic, fail-closed); else 401. D subsumes A (exact-match is D's matching
   mode) and C (the split by auth type IS the allowlist's composition). A alone only changes the
   matching of a list that still cannot be enumerated reliably by hand; C alone still leaves the
   gate's default as pass-for-anything-in-the-list.
   B (re-enable Spring Security) is NOT viable as a flip: the commented block enforces the OAuth2
   resource-server while the app uses custom JWTs with header-derived userId/unitId — re-enabling
   rejects everything until the custom-JWT validator is built into the security chain, which is a
   rewrite of the app's entire auth surface (the filter's logic + the identity plumbing + every
   session) at 2-3x D's effort with the whole app's login at risk. It is viable only as a later
   consolidation after D holds, and only if the custom-JWT/header model is migrated first.

2. SEQUENCE.
   Gate 0 (done): the extraction-based list (QF-AC's tools) — the real 154, control-verified.
   Phase 1 (the structure): the filter's rewrite — default-deny + exact-match against the extracted
   allowlist (~84 = PUBLIC 47 + INTEGRATION 22 + KIOSK-public ~15) + URL normalization BEFORE the
   match (query-strip, trailing-slash trim, case) + the token-check unchanged. GATES: bytecode
   verification, the per-entry sweep (every non-allowlisted entry -> 401 no-token; every allowlisted
   entry -> not-401), regression, six harness specs.
   Phase 2 (the allowlist's validation): caller-audit the ~84 (PUBLIC's masters/static quick;
   INTEGRATION's callbacks' signature checks; the KIOSK-public's protections per item 3). GATE: the
   audits recorded per entry.
   Phase 3 (the fold-in): the 3 confirmed misplacements + the 30 SAFE are NOT a separate removal
   branch — they are the default-deny's automatic effect (their endpoints leave the allowlist, their
   authenticated callers pass the token-check). Verification per screen: the coversheet family, NABH/
   incident reporting, growth chart, bookView, medication, service-master. GATE: each calling screen
   works + the endpoint 401s without a token.
   Phase 4 (the kiosk protection — item 3).
   Phase 5 (the decommissioning): physically delete the now-unreachable list entries (the 45-style
   removal, both copies, count-verified) so the allowlist and the filter agree; final regression +
   harness + ledger.

3. THE KIOSK PROBLEM (~15 public entries, 5 of them writes: saveAndPay, saveAnalyzedReport, the two
   feedbacks, patientRegistration).
   Recommended package, in order: (a) KIOSK DEVICE TOKEN — the kiosk is enrolled with a device
   identifier + shared secret; the filter validates a kiosk-device header for the public writes, so
   the writes are no longer anonymous (accountability + revocation). (b) RATE LIMITING — per-IP +
   per-device (registration spam, appointment flooding). (c) SERVER-SIDE VALIDATION as the backstop —
   the flows' existing dup-checks (CHECK_UNIQUE_PATIENT, the already-exist guards) stay and are the
   real protection against forged duplicates. (d) CAPTCHA — optional, only on patientRegistration if
   abuse appears; it adds friction to a surface designed for fast self-service. No single item solves
   it; (a)+(b)+(c) together do, and all three are independent of the allowlist.

4. VERIFICATION STRATEGY.
   Granularity, three layers: (1) per-entry — every allowlisted entry: no-token call not-401 +
   token'd call works; every non-allowlisted entry: no-token 401 (the QF-W 45-sweep pattern,
   extended to all 154); (2) per-screen — the calling screens' flows (coversheet, NABH, growth chart,
   bookView, medication, service-master, kiosk self-registration/bookAppointment/feedback);
   (3) the standing regression (verify_deployed_regressions.py) + the six harness specs. The harness
   carries the authenticated screens today; the kiosk screens need NEW specs (pre-login flows —
   public-reachability + the protections), which is new harness work, not a harness extension.

5. EFFORT AND RISK (calibrated to this session's actuals: the 45-removal with 3 corrections ~ one
   session; per-entry traces ~0.5-1h; the extraction ~1 session).
   Phases 1-5: roughly 6-9 focused sessions (Phase 1 ~1-2 + verification, Phase 2 ~1-2, Phase 3
   ~1-2, Phase 4 ~1, Phase 5 ~1) — vs ~2-3 for the removal branches that do not fix the structure.
   THE SINGLE MOST-LIKELY-TO-BREAK: the exact-match against the frontend's URL VARIANTS — the
   contains()-slack currently absorbs trailing slashes (/api/opd/getActiveCountryList/), query
   strings, and assembled-URL differences; exact-match exposes every variant as a 401. Mitigation:
   URL normalization before the match + Phase 1's per-screen verification as the gate. Rule-10
   caveat: every caller-behaviour assertion in this plan traces to the session's named-caller audits
   (QF-U, QF-AC, QF-AD, QF-AE); each Phase-3 screen gets its own end-to-end re-confirmation.

## QF-2026-08-05-AG — BR-173 NEW PHASE 1: URL VARIANT CENSUS (tools/url_variant_census.py)

- Method: for each of the 154 extracted entries, the caller-URL forms were extracted from the whole
  JS/HTML tree (files loaded once; same-quote-pair + 200-char-cap regex; distinctive search target —
  the full fragment up to 64 chars, longer fragments use a 4-segment prefix). Rule-10 control FIRST:
  the extraction found the known forms of /api/opd/getMedicationHistoryList (assembled + the
  ForBookView sibling), /adt/getActiveCountryList/ (trailing-slash/constant), and
  /api/opd/patientDoc/details (the /incident and /nabh siblings). First two passes over-recalled
  (cross-quote spans; then the 'details' stem and the 3-segment prefix matched whole classes) —
  both corrected before any conclusion.
- RESULT: 36 of 154 entries (23%) have caller-URL forms the exact-match would miss.
- RULE 1 — trailing-slash-strip + query-strip (6 entries, clean): e.g. /adt/getActiveCountryList/,
  /api/opd/getFrequencyList (caller sends '?unitId='), /api/opd/getOPDMedicationListById/,
  /api/webappointment/getActivePrefixList/.
- RULE 2 — per-form exact-list / individual handling (30 entries, NAMED):
  /Report, /adt/getActiveSpecialityList, /adt/getActiveStateList, /adt/getAreaByCityId,
  /api/billing/unit/bill/Report, /api/getVideoataForNotification, /api/global/glablServiceMaster,
  /api/global/growthDevelopmentalMilestones/patient, /api/globals/frequency,
  /api/opd/coversheetInstruction, /api/opd/getComplaintAppointment,
  /api/opd/getMasterListOfICPCSystemForCoversheet, /api/opd/getMedicationHistoryList,
  /api/opd/patientDoc/details, /api/opd/saveInvestigation, /api/opd/saveMedication,
  /applicationConfiguration/screen-images, /br/, /checkEmailAvailability, /forgotPasswordGeneration,
  /getSerivceRateGlobalList, /otpVerification, /resources/, /unit/subcription, adt/getActivePrefixList,
  api/billing/kiosk/saveAndPay, api/global/patientTypeHeadSearchForAppointmentUnique,
  deleteParticipants, opd/saveMedication, update/service.
- The 30's variant classes: (a) SIBLING-ENDPOINT URLs riding the entry's substring today — e.g.
  getMedicationHistoryList->ForBookView, saveInvestigation->ForAppointmentTemplate, coversheetInstruction->
  ListForBookView, getComplaintAppointment->ByPatientId, patientDoc/details->/incident+/nabh,
  growthMilestones/patient->/detail, getMasterListOfICPCSystemForCoversheet->ForCode,
  getActiveSpecialityList->ForModality, getAreaByCityId->Pagination, getOPDMedicationListById->
  ForNonFormulary, checkEmailAvailability/otpVerification/forgotPasswordGeneration->/adt/-prefixed,
  saveAndPay->/kiosk/saveAndPay variants; (b) the BROAD-substring entries whose exact-match removes
  whole families — /Report (the LIS/Billing/pathology report URLs), /br/ (->/adt/br/), /resources/,
  deleteParticipants (the family), opd/saveMedication vs /api/opd/saveMedication; (c) the
  ABSOLUTE-URL form — /api/getVideoataForNotification is called as
  http://localhost:8078/HIS-WAR/api/getVideoataForNotification (host-strip rule); (d) the
  PATH-VARIABLE forms — the feedback GETs (/api/outPatientFeedback/:organizationId/:unitId/...).
- The no-clean-rule cases (need individual decisions): /Report (the family is large and cross-module),
  the feedback path-variable GETs (an exact-match cannot cover variable ids without a pattern rule),
  deleteParticipants (the bare substring covers a whole family that is itself under review).
- Phase-2 implication: the filter rewrite's URL normalization = host-strip + trailing-slash-strip +
  query-strip; the 30 individual entries need their sibling-endpoint exact-forms added to the
  allowlist (or the sibling URLs explicitly listed) BEFORE the rewrite, or they 401.

## QF-2026-08-05-AH — PROPOSED ALLOWLIST (the Phase-2 artifact) + the three no-clean-rule resolutions

TOOL: tools/allowlist_proposal.py (re-runnable; full 199-row table in its output). Method: every
extracted entry + every census sibling form -> the exact post-normalization URL (host-strip,
query-strip, trailing-slash-strip) -> named callers (file:line + screen family) -> constant-trace for
config-file hits (the GETACTIVECOUNTRYLIST-class usages) -> verdict. Rule 10: an ALLOW needs a named
pre-login caller or the bucket's public-by-design basis; the first two passes over-ALLOWed the KIOSK
bucket (unconditional) and under-detected the masters (config-file callers are constant definitions,
not screens) and the pre-login siblings (/adt/br, the OTP/forgot-password flows) — all corrected
before the artifact was fixed.
- TOTALS: 199 exact URLs = 108 ALLOW + 91 DENY.
- ALLOW composition: PUBLIC 47 (login/static/swagger/masters), INTEGRATION 22 (mylife/abdm/twilio),
  KIOSK public ~30 (kiosk writes incl. saveAndPay/patientRegistration/feedbacks, the pre-login
  appointment/registration forms, the masters/ref-data via the constant trace), siblings 9 (/adt/br,
  the feedback save + path-variable GETs, the ForBookView/ListForBookView siblings, saveAndPay/future,
  the NurseCheckIn sibling).
- DENY composition: STALE 29 (authenticated-only — works through the token check), UNEXPLAINED 8,
  KIOSK-internal 15 (post-login-only or no-caller), siblings 39 (no pre-login caller — the LIS/billing
  report URLs, the /api/unit/clinical/ siblings, the glablServiceMaster siblings, the
  checkEmailAvailability/otpVerification/forgotPasswordGeneration siblings, the
  getComplaintAppointmentByPatientId etc.).
- GROWTH (honest): 199 vs the 154 extracted = +45 (29%). The sibling enumeration is the whole growth;
  it is the decision list the user reframed — every sibling was public by accident of string matching,
  and 39 of them resolve to DENY (authenticated-only, no reason to be public).

THE THREE NO-CLEAN-RULE RESOLUTIONS:
1. /Report: the fragment currently exempts ~6-8 distinct endpoints across LIS (/LIS/ReportHandOver,
   /LIS/ReportHandOver/ReportHandOverList/Search, /LIS/BioChemistry/ReportReleaseWorkList/Type/Search),
   Billing (/api/billing/unit/bill/Report + the MIS + /file siblings), and Pathology (/pathology/report,
   a separate entry). All callers are staff-facing (post-login). PROPOSAL: enumerate the distinct
   endpoints individually (6-8 exact forms) and DENY each — the reports work through the staff token
   check; the broad /Report substring dies with the rewrite.
2. The feedback path-variable GETs (/api/outPatientFeedback/{unitId}/{organizationId}/{patientId}/
   {visitType}/{visitTypeId}/{encounterId}, same shape for inPatient): PROPOSAL — a narrow pattern
   rule for these two (bounded 6-variable shape) rather than a redesign. Cost: matching complexity is
   linear per request (one anchored regex, no backtracking on these shapes) and the rules add two
   entries to maintain; the redesign (query-param GETs) costs frontend URI changes across the feedback
   callers plus deployment coordination. The pattern rule is the cheaper step; the redesign stays an
   option if the pattern set grows beyond these two.
3. deleteParticipants: RECOMMENDATION — DENY the family (the bare substring + the /HIS-WAR variants +
   /api/deleteParticipants, ~5 exact forms). The allowlist's DENY resolves the QF-Z hold structurally:
   the family leaves the allowlist, the unauthenticated POST to the live /api/deleteParticipants
   controller 401s, and the bare entry becomes irrelevant without a separate removal branch.

## QF-2026-08-05-AI — the 108 ALLOW in reviewable form + KIOSK re-confirmation (tools/allowlist_review.py)

- The reviewable 108 (risk-ordered: writes -> PHI reads -> reference/config -> static) is in the
  tool's output: exact URL | verb | touches (PHI/billing/reference/config/static) | named caller
  file:line | one-line reason. The user reviews all 108.
- RULE-10 CONTROL on the constant traces: GETACTIVECOUNTRYLIST = /adt/getActiveCountryList/ verified
  against adt.url.config.js:61 and its JS users found (control PASS) before any rescued master was
  trusted.
- KIOSK RE-CONFIRMATION (the ~30, route-checked): the route evidence splits the group:
  CONFIRMED pre-login by route: selfRegistrationdemo (saveAndPay, patientRegistration,
  appointment/check, patientTypeHeadSearchForAppointmentUnique), bookAppointment (getDetailsForBookAppointment,
  getActivePrefixList, doctorApp/getApplicationConfig), bookView (getListOfDocumentForBookView,
  getInvestigationsListForBookView, coversheetInstructionListForBookView,
  getMedicationHistoryListForBookView), webAppointment (getActivePrefixListForWebPage,
  getActiveRelationListForWebPage, appoitments/doctors), the two feedback endpoints + their saves
  (anonymous by design).
  UNCONFIRMED -> DENY DEFAULT (per the owner's rule — cannot demonstrate pre-login reachability):
  - /api/global/webappointment/getDoctors — the named caller is dischargedPatientsController.js:123
    (route /dischargedPatients — a POST-LOGIN clinician screen). Reclassify DENY.
  - /adt/getNationalityByCountryId — caller admittedPatientListController.js:573 (route
    /admittedPatientList — post-login). Reclassify DENY.
  - /api/opd/getDailySlots — NO named caller. Reclassify DENY.
  - api/global/getAvailableDoctorBySpeciality — NO named caller. Reclassify DENY.
  - The /adt/getActive* + address masters — their constant-trace users are dominated by post-login
    screens (admissionController, admittedPatientListController, bedAllocationController per the
    control); pre-login reachability not demonstrated for the majority. Reclassify DENY (they work
    through the staff token check; the kiosk screens that need them can be re-ALLOWed with a named
    pre-login caller after review).
  - adt/getPrefixMasterList, adt/getActivePrefixList, /adt/getSpecialityListByDoctorIdByOrg — config/
    post-login callers; UNCONFIRMED -> DENY.
  NET EFFECT: ~14-16 of the kiosk-32 reclassify to DENY; the reviewed ALLOW lands around 92-94.
- The remaining ALLOWs (INTEGRATION 22 with external callers, PUBLIC 47, the confirmed-pre-login
  kiosk ~16-18) keep their named callers.

## QF-2026-08-05-AJ — owner review of the 108: six decisions applied. FINAL ALLOW COUNT: 83.

- APPLIED (owner, 2026-08-05):
  1. DENY /api/billing/unit/bill/Report — billing data, config-file caller only; the same evidence
     shape the geo masters were demoted on. Re-ALLOW only with a named pre-login caller.
  2. DENY /Report — the QF-AH resolution applied: the family enumerated individually and DENYed:
     /LIS/ReportHandOver, /LIS/ReportHandOver/ReportHandOverList/Search,
     /LIS/BioChemistry/ReportReleaseWorkList/Type/Search, /api/billing/unit/bill/Report/MIS,
     /api/billing/unit/bill/Report/file (+ /pathology/report, already its own entry) — all DENY;
     the reports ride the staff token check.
  3. DENY /QR, /RIS/RADIOLOGY, /pathology/report, /generateToken, generateTokenForWebUser — no
     named caller, no demonstrated pre-login need; UNCONFIRMED defaults to DENY (the getDailySlots
     rule).
- CONFIRMED KEPT:
  4. /token/Doctor ALLOW — twilio-video.js:30732 is the raw-fetch no-token path (the /checkParti/
     class); the twilio participant check needs it pre-login.
  5. /checkEmailAvailability, /otpVerification, /forgotPasswordGeneration ALLOW — pre-login by
     design (the account-existence oracle + the credential-reset paths). PHASE 6 REQUIREMENT
     (recorded): rate limiting on all three — the reset/verify paths are brute-force targets.
  6. The 8 external INTEGRATION callbacks ALLOW (ABDM x4, MyLife x3, the payment callbacks
     /resultUrl + /validationResultResponse, Twilio /api/saveTwilioMsg + the misc) — the caller is
     a partner system, no repo caller by design. PHASE 6 REQUIREMENT (recorded): a real partner
     gate — shared secret, signature verification, or IP allowlist — because unauthenticated is
     the wrong answer for a partner callback; the structural change leaves them ALLOW, Phase 6
     closes them properly.
- THE MATH: 108 - 18 (kiosk reclassified DENY: getDoctors, getNationalityByCountryId, getDailySlots,
  getAvailableDoctorBySpeciality, the 11 geo/address masters, the 2 prefix masters,
  getSpecialityListByDoctorIdByOrg) - 7 (owner 1-3) = 83 FINAL ALLOW.
- The 83 = 47 PUBLIC (incl. the login/static/swagger) + 22 INTEGRATION (Phase-6-gated) + ~14
  confirmed-pre-login kiosk (selfRegistration/bookAppointment/bookView/webAppointment/feedback/
  /token/Doctor + /adt/br + the pre-login siblings) + the 3 login-adjacent PHI (Phase-6 rate-limited).
- NEXT GATE: the owner authorises the Phase-2 rewrite from this number. The 83 exact-URL allowlist
  is the artifact the rewrite matches against (normalization: host-strip, query-strip,
  trailing-slash-strip; the pattern rule for the two feedback path-variable GETs).

## QF-2026-08-05-AC — BR-173 shell bucket: login shell VERIFIED, webjar prefix check FAILS in the ROOT deployment (decision queued)

Verification of the owner-approved shell bucket (fix/br173-default-deny @ d5a51640, deployed to the running Tomcat PID 3458 as WEB-INF/classes; class mtime 20:45 == commit minute, strings confirm the 3 prefixes + 3 shell exacts, 0 requestUrl.contains).

1. LOGIN PAGE end-to-end, no token: PASS. /, /index.html, /healthcheck.html all 200. 1,333 local asset refs enumerated from the served /index.html and fetched — 0 x 401 (the 9 "401" rows in the raw sweep are protocol-relative EXTERNAL CDN URLs (//ajax.googleapis.com etc.) the browser resolves off-host; they never hit the filter. 2 non-401 content anomalies are pre-existing and filter-irrelevant: /resources/assets/css/global-button-style.js -> 404 (referenced, missing on disk) and one backslash-escaped href artifact -> 400.)
2. ALLOW re-sweep: 68/68 not-401 (66 exact entries incl. the 3 shell exacts + the 2 real-shaped feedback URLs /api/{in,out}PatientFeedback/1/1/1/1/1/1, which reach their controllers and 500 on fake IDs — the criterion is not-401). All 66 verbatim entries pass; /HIS-WAR/... exacts (swagger-ui.html, v2/api-docs, configuration/*, api-ui, swagger-resources, images/favicons) are allowlisted-but-404 at ROOT (legacy context-path artifacts, harmless).
3. DENY sample: 14/14 -> 401 across buckets, including every previously-exempt write family: /api/opd/saveMedication + /updateMedication + /saveMedicationHistory (old whitelist line 110), /api/opd/patientDoc/saveAnalyzedReport + getAnalyzedReport/1, /api/billing/unit/bill/Report + /MIS + /file/1/1, /deleteParticipants + /{identity}/{room} + /api/deleteParticipants, /api/opd/getFrequencyList (old line 113), /adt/getActiveCountryList, /api/global/patientRegistration/save (kiosk sibling; only the exact base is allowed).
4. WEBJAR PREFIX: FAIL in this deployment. The approved /HIS-WAR/webjars/ prefix matches a tree that does not exist in the ROOT-context deployment (every real springfox file under it -> 404). The ACTUAL webjar tree /webjars/** (springfox-swagger-ui-2.4.0.jar, META-INF/resources) -> 401; /swagger-ui.html and /v2/api-docs -> 401. NOT A REGRESSION: the pre-rewrite filter whitelisted "/HIS-WAR/webjars/..." substrings, which /webjars/** never contained, so the real tree was equally 401 before the rewrite; the /HIS-WAR/... entries are /HIS-WAR-context-deployment artifacts (the preprod template shape) carried into both filters. Zero frontend consumers of /webjars/ (grep of deployed resources/, views/, index.html -> none); swagger is a dev-tool reached only by manual URL. The login SHELL itself (the actual second-gap candidate: lazy-loaded views/) is fully covered — /views/ and /resources/ serve real files 200.
DECISION QUEUED (DECISIONS_QUEUE.md #1): widen the prefixes with /webjars/ (+ /swagger-ui.html, /v2/api-docs exacts) for ROOT dev convenience, accept the /HIS-WAR-shape coverage as-is, or drop the no-op — owner call, since it changes the approved prefix list. Regression checker on this deployment: PASS (every closed BR signature present; "gender active whitelist removed" asserted absent in the running filter).

## QF-2026-08-05-AD — four dead script references in index.html (logged, NO FIX)

- index.html references four scripts that are ABSENT on disk (404 under the old filter too — a
  pre-existing app defect, not a filter concern):
  1. resources/assets/css/global-button-style.js (a .js mislabeled under css/)
  2. resources/assets/js/controller/asset/addnewAssetIndentController.js
  3. resources/assets/js/controller/master/pharmacy/reasonMasterController.js
  4. resources/assets/js/controller/master/unit/staffMasterController.js
- The shell sweep (1,342 distinct index-referenced paths) returned exactly these four as non-200;
  all four 404'd before the rewrite as well. The app has been shipping dead script tags. No action —
  the filter must not be made to mask them, and the fix belongs to the app's asset audit.

## QF-2026-08-05-AE — BR-174 scoped: bookView is NOT an unauthenticated PHI gap (retraction logged)

- **The stronger claim is RETRACTED, and it originated with the user** (twelfth pattern-pass
  correction; the first where the bad pass was on the user's side): the bookView family was read
  as "pre-login route" off a route-config classification and escalated as potentially the most
  serious finding of the project. The load test overturned it.
- **BR-174 scope (as recorded):** post-login org-trust on the bookView endpoints — caller-
  controlled org/unit params (QF-AA class). NOT an unauthenticated PHI read. The 25 bookView
  endpoints are DENY'd by the filter (Layer 2) and unreachable pre-login (clean-context load of
  #/bookView lands on the login via the encounterQueueManagement permissions-resolve crash ->
  $urlRouterProvider.otherwise("/login")). Nothing added to the allowlist.
- **Lazy-fetch sweep classification DISCARDED (Rule-10 control failure):** the load-test method
  could not distinguish a known-public route from a known-gated one — every route (redirects AND
  URL-staying states, incl. #/selfRegistrationdemo and #/selfRegistrationhome) rendered the login
  form with no session. The shell gate overrides all routes in this deployment. Per the user's
  condition, the ~40-gap classification was discarded, not kept.
- **Separate finding (the kiosk flow is already broken here, unrelated to the filter):** the
  selfRegistrationdemo state's templateUrl (views/OPD/self-registrationdemo.html) is ABSENT on
  disk (QF-AD class); the loginController navigates only post-login destinations (dashboard/home);
  the kiosk entry routes render the login with no session. The kiosk flow is not reachable in this
  deployment with no session — a pre-existing break, not a filter regression.

## QF-2026-08-05-AF — applicationConfiguration state commented out: the checkCookies() config gate redirects to an unroutable state (logged, NO FIX)

- **Finding:** the app's standard init guard `checkCookies()` (app.js:1927) redirects any guarded
  screen to `applicationConfiguration` when the `cookieObjectAppConfig` cookie is missing
  (app.js:1936). The `applicationConfiguration` state definition is COMMENTED OUT (app.js:15340),
  so the redirect target is unroutable and falls through to `$urlRouterProvider.otherwise("/login")`
  (app.js:3117). The gate has been broken independently of this branch — it predates the BR-173
  rewrite and is a frontend state-config defect, not a filter issue.
- **Context:** the redirect is the app's designed config gate (cookieObject vs cookieObjectAppConfig
  discriminator), uniform across 213 controllers. The `cookieObjectAppConfig` cookie is written by
  the ADT/registration flows, not by login. Deep-linking to guarded screens (e.g. the GlobalMaster
  screens) redirects until the config has been loaded through an admission-flow screen.
- **Does NOT affect the merge:** the BR-173 filter work is independent of this frontend gate; the
  gate's behavior is identical before and after the branch. NO FIX — the fix belongs to the app's
  state-config (re-enabling or re-pointing the applicationConfiguration state), not the filter.

## QF-2026-08-05-AG — config-gate blockage: inferred table corrected; real drift found; login-cookie cause UNRESOLVED

- **Retraction (logged):** the earlier claim that `m_unit_application_configuration` is a missing
  table is WRONG — that table does not exist in the DB, in the authoritative DDL, or as an entity,
  because it was never a table. The name was inferred from `UnitApplicationConfigurationDto`; the
  actual query path disproves it. The inference error is logged as the third consecutive
  inference-on-this-path caught by Rule-10 verification against the real query and the real row.
- **The real config tables (org 1 / unit 1, this box):**
  - `t_unit_application_billing_configuration` — EXISTS, 1 row, status='A' (the login DTO's
    source via `GET_DEFAULT_SELF_TARIFF_FROM_APP_BILLING_CONF`).
  - `t_unit_application_registration_field_config` — **MISSING (the real drift)** — the
    `GET_UNIT_APPLICATION_REGISTRATION_FIELD_CONFIG` named query's target; the ADT screens
    (admittedPatientList, admissionRequest, unknownPatientReg, morgueReservation) fetch it and
    would fail on this box. Same drift class; not the login-cookie path.
  - `m_application_configuration` — EXISTS, 1 row (org 1).
- **Login-cookie cause: UNRESOLVED.** The assembly (`UserMasterServiceImpl:378-392`) attaches the
  DTO from the billing-config row (present), and the 788 writer's branch needs only
  `mainObject.unitApplicationConfigurationDto` non-null — the DB state says the cookie should be
  set, yet the observed behavior says it is absent. Needs the live cookie value (browser) to
  distinguish a branch-miss from a cookie-write failure.

## QF-2026-08-05-AH — master-screen redirect: UNRESOLVED (four hypotheses, three disproved)

- **Observation:** authenticated deep-links to #/relationMaster, #/maritalstatusMaster,
  #/occupationMaster land on the applicationConfiguration screen.
- **Hypothesis 1 — missing table:** disproved. `m_unit_application_configuration` was never a
  table (inference from the DTO name); the real config tables exist with data
  (t_unit_application_billing_configuration 1 active row; m_application_configuration 1 row).
- **Hypothesis 2 — missing cookie:** disproved. The cookieObjectAppConfig cookie IS present in the
  logged-in session; the login DTO attaches and line 788 writes it.
- **Hypothesis 3 — permission gate:** disproved. The masters' states have NO resolve; the role-1
  privilege modules JSON carries 390 Relation Master, 391 Marital Status Master, 393 Occupation
  Master (and 660 Application Configuration). The role has everything.
- **Hypothesis 4 — 4KB cookie truncation (JSON parse fails -> getObject undefined -> the
  checkCookies gate fires despite the cookie's presence):** plausible, traced from source (82-field
  DTO, URL-encoded, near the 4KB ceiling; the sidebar's checkCookies destructuring + the shell
  render are the trigger path), but NOT verified — the user stopped the investigation before the
  cookie-length check. The pattern (four plausible source-traced explanations in a row) is exactly
  what Rule 10 warns about.
- **Status: UNRESOLVED.** Return after the merge with a method that can distinguish (a logged-in
  browser capture), not another inference. Not merge-critical; the masters are API-verified by the
  six harness specs.

## BR-175 — config cookie carries the SMTP endpoint + credential (logged, NO FIX)

- The cookieObjectAppConfig cookie (82-field UnitApplicationConfigurationDto, client-readable,
  URL-encoded JSON) is written by the login flow. `stripClientFacingSecrets()` (Dto:1435) already
  nulls **password, razorPayKeyId, razorPaySecretKey** at the login (UserMasterServiceImpl:398) —
  those three do NOT reach the cookie.
- **NOT stripped and carried in the cookie: host, port, id** (the SMTP endpoint + username).
  The frontend reads 95 distinct cookieObjectAppConfig fields across the JS — **host/port/id have
  ZERO reads** (grep-verified). They are carried client-side with no consumer.
- **Fix direction (deferred):** extend stripClientFacingSecrets() to host/port/id (no client
  consumers exist). Not applied.

## QF-AI — /fonts/ content gap (QF-AD class, NO FIX)

- The web-appointment.html template's inline @font-face blocks reference ../fonts/robotoregular.*
  and ../fonts/robotomedium.* (relative to /views/AppointmentScheduling/, resolving to the root
  /fonts/ tree). The font FILES are absent from the source webapp AND the deployed ROOT — there is
  no /fonts/ directory anywhere in the WAR.
- The /fonts/ prefix (5th static prefix) fixes the AUTH answer: the requests now 404 instead of
  401, so the filter no longer masks the missing-asset problem. The 404s remain; the browser falls
  back to the system font and the web-appointment page still renders (why this went unnoticed).
- Same class as QF-AD (referenced-but-absent assets): the fix would be shipping the font files or
  removing the @font-face rules. No fix applied — logged only.

## QF-AJ — global search unreachable for role 1 (module-987 permission gap) + coversheet dependence + dead home-top-bar

- **Module-987 permission gap:** the only rendered header (top-bar.html, index.html:187 ng-if
  loginpage) carries the global-search trigger — the "Book View Search" icon (top-bar.html:276),
  gated ng-if="checkAccessT(987)". Module 987 is ABSENT from the role-1 privilege tree (1,077
  modules, no id 987) -> the icon does not render -> #searchModal has no trigger. The only other
  data-target="#searchModal" is in home-top-bar.html:151 (see below). No JS opens the modal (the
  sole reference is a .modal('hide')). NOT a filter defect; identical before and after this branch.
- **Coversheet dependence:** the patient coversheet's designed entry is the global search
  (search-result click passes appointmentObj into #/OPDCoverSheet). With the search unreachable,
  the coversheet has no working entry for this session; a bare deep-link renders empty
  (params.appointmentObj empty, drugHistoryList unpopulated). Pre-existing; not a filter defect.
- **Dead home-top-bar template:** views/common/home-top-bar.html is included NOWHERE (grep across
  views: zero ng-include references) yet carries the only ungated search trigger (line 151). Dead
  template; pre-existing. Candidate for removal in the template cleanup phase.
- Status: LOGGED, no fix. The role's module tree is data, not code — the fix (adding module 987 to
  the role's privilege modules) is a configuration decision for the owner.

## QF-AK — allowlist-reality audit: 18 wrong forms (merge-day defect) + self-referential sweep + controller typo

- **The verification-method finding (the real defect):** the 66/66 ALLOW sweep curled the
  allowlist's OWN strings, so a 404 counted as not-401 — it tested the allowlist against itself
  and could not detect a wrong entry. Fifth instance of a check structurally incapable of finding
  what it claimed to verify. Replaced by tools/verify_allowlist_reality.py (committed): derives
  the form a real caller produces from the controller mappings + frontend sources, treats 401 AND
  404 on an ALLOW entry as FAILURES. Pre-fix proof: 42 failures on the broken deployment; the
  audit tool (tools/allowlist_audit.py) resolved every exact against the controller's class-level
  @RequestMapping + method mapping and the frontend call form.
- **18 wrong forms (pre-fix allowlist):** 9 class-prefix-dropped exacts corrected to the real
  forms (/adt/checkEmailAvailability, /adt/forgotPasswordGeneration, /adt/otpVerification,
  /api/global/updatePatientRegistrationFromMyLife, /adt/getActivePrefixList,
  /api/billing/getBillNoByPatient, /api/billing/getBillReceiptBytesMyLife, /Billing/resultUrl,
  /Billing/validationResultResponse); 3 pattern-needed forms moved to anchored patterns
  (bookView investigations 4-var, /token/{identity}/{room}, /adt/br/{barcodeNumber}); 4 dead
  feedback entries removed; 8 never-resolving /HIS-WAR/ swagger/config entries dropped; the
  redundant bare /adt/organizationMasterList removed (pattern covers it).
- **Controller typo (fixed on this branch):** OrganizationWebRegistrationController.java:22 — the
  /api/createOrganizationFromWeb mapping had a TRAILING SPACE in the value
  ("/createOrganizationFromWeb "), so the endpoint never resolved at the clean path — nobody has
  called it successfully. One-line fix.
- **Pattern candidates DENIED (no named pre-login caller, per the standing pattern rule):**
  /api/global/getVideoConsultationLinkFromHIS/{uuid} (zero callers anywhere in the webapp) and
  /LIS/global/getTotalLanguageMasterRecordActive/{orgId}/{unitId} (post-login LIS screens only);
  /api/unit/applicationConfiguration/screen-images/{orgId}/{imageTypeId} (login-page callers are
  commented out; live callers are post-login screens with tokens). All three DENY.
- Composition after the fix: 48 exact (44 API/static + 3 shell + 1 login) + 5 prefixes + 7 patterns.
- Status: FIXED on fix/br173-allowlist-audit, live-verified 77/77 by the new method.

## QF-AL — BR-176: anonymous subscription-PAID bypass (entitlement) + hardcoded encryption key

- **The flow:** allowlisted POST /api/createOrganizationFromWeb (UserMasterDaoImpl:1752) creates an org+unit,
  inserts the CALLER's paymentDetails JSON verbatim into payment_details (:1800), then hard-sets
  amount_paid_status='PAID' (:1841) with the caller-supplied amount/TransactionId — no Razorpay order
  lookup, no signature check, no amount check. addSubScrtionDetails (authenticated /unit/subcription/details)
  likewise takes amountPaidStatus straight from the DTO.
- **Masking (the third environment gap):** unreachable on this dev box only because payment_details does not
  exist locally (the flow errors at :1800). The DDL creates it (31 references) — any DDL-built environment
  has the bypass live. family_id and saveAnalyzedReport were the first two; RULES.md Rule 10 now carries the
  standing note: on this dev box, "it does not work" is never evidence that it is safe.
- **The hardcoded pgp key:** 'ssshhhhhhhhhhh!!!!' in UserMasterDaoImpl:1841-1847 (and the ENCRYPTIONKEY
  constant used to decrypt at check time) — the encrypted amounts decrypt with a string in the repo; the
  column-level encryption is decorative. Part of BR-176, not a separate finding.
- **The Razorpay row (t_unit_subscription_details id 1, PAID/ACTIVE, empty order id, created_by NULL):**
  hand-inserted dev artifact on 2026-07-18 — no code path created it (createOrganizationFromWeb can't run
  locally + never targets org 1; addSubScrtionDetails always stamps created_by; no seed inserts it). Not
  evidence of a payment. The check gate (unit/subcription/check) is a soft nag + pay modal, no lockout.
- **Status:** LOGGED. Fix scoped, unstarted (per owner). The prior razorpay draft's two findings
  (client-trusted amount, missing signature verification) confirmed still present in the code today.

<!-- ============ END OF QA_FINDINGS — new entries append BEFORE this marker ============ -->
