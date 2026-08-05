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

<!-- ============ END OF QA_FINDINGS — new entries append BEFORE this marker ============ -->
