# Decision Needed: SQL Injection in `dietUnitMater`'s duplicate-check queries — confirmed live and exploitable, same pattern found elsewhere in the codebase

## 1. The vulnerability itself

`DietUnitMasterDaoImpl.java`, both `checkAvailableUnitByName` (lines 218-244) and `checkAvailableUnitById` (lines 247-274), build a raw SQL string by directly concatenating the user-submitted unit name into the query text, inside a string literal, instead of parameter-binding it — while every other value in the same queries (`organizationId`, `uomId`) *is* correctly parameter-bound:

```java
String query = "select ... from kitchen.m_unit_of_measurment_master uom " +
    "where uom.organization_id =:organizationId " +
    "and lower(uom.uom_name) = '" + unitOfMeasurmentMasterDto.getUomName() + "'" +
    "and uom.status = 'A'";
List<UnitOfMeasurmentMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
    .setParameter("organizationId", unitOfMeasurmentMasterDto.getOrganizationId())
    // uomName is NOT bound as a parameter -- it's already embedded in the query string above
    .setResultTransformer(Transformers.aliasToBean(UnitOfMeasurmentMasterDto.class)).list();
```

Both methods are called unconditionally, as the very first thing that happens, on every Create (`DietUnitMasterServiceImpl.java:27`) and every Update (`:42`) — there is no earlier gate or validation that could prevent a malicious value from reaching this code.

## 2. Exploitability — confirmed empirically, not assumed from source

**Client-side: no blocking.** The unit-name field (`diet-unit-master.html:14`) is a plain `<input type="text" ... required>` — `required` only blocks an *empty* submission. There is no `ng-pattern`, no `ng-maxlength`, no character restriction of any kind. Any text, including SQL metacharacters, types and submits normally through the real UI.

**Server-side: no blocking.** `DietUnitMasterController.java`'s `saveDietUnitMaster`/`updateDietUnitMaster` methods take `@RequestBody UnitOfMeasurmentMasterDto` with no `@Valid` annotation, and the DTO itself has zero Bean Validation annotations. Nothing sanitizes or rejects the value before it reaches the DAO.

**Access control: this screen has no module gate at all** (confirmed in the Step 1 audit — no `resolve`/`data` block on the `dietUnitMater` state), so any authenticated user, regardless of role, can reach it.

**Live proof-of-concept, run against the real running backend** (`his_local`, read-only/non-destructive, table restored to its original empty state afterward):

1. Seeded one legitimate row via the real `saveDietUnitMaster` endpoint: `uom_name = 'SECRET_MARKER_XYZ'`.
2. Called `saveDietUnitMaster` again with a **different, non-matching** name containing injected SQL: `uomName: "totallydifferent' OR '1'='1"`.
3. Result: `{"status":"error","message":"Already Exists."}` — the save was rejected as a duplicate, which is only possible if the injected `' OR '1'='1` closed the string literal early and the resulting `OR '1'='1'` clause matched the existing row regardless of the actual name comparison. This is confirmed, working SQL injection via the real endpoint, not a theoretical read of the code.
4. Test row deleted afterward; table confirmed back to 0 rows.

I did not attempt further escalation (e.g. `UNION SELECT`-based data exfiltration from other tables) — the boolean-bypass result above is sufficient to prove the vulnerability is real and live; a full exfiltration PoC would be a larger, more invasive test than needed to answer the question, and wasn't attempted per the instruction to investigate and propose only.

## 3. Proposed minimal fix

Bind `uomName` as a query parameter the same way `organizationId`/`uomId` already are — no other change needed:

```java
String query = "select ... from kitchen.m_unit_of_measurment_master uom " +
    "where uom.organization_id =:organizationId " +
    "and lower(uom.uom_name) = :uomName " +
    "and uom.status = 'A'";
List<UnitOfMeasurmentMasterDto> list = sessionFactory.getCurrentSession().createSQLQuery(query)
    .setParameter("organizationId", unitOfMeasurmentMasterDto.getOrganizationId())
    .setParameter("uomName", unitOfMeasurmentMasterDto.getUomName().toLowerCase())
    .setResultTransformer(Transformers.aliasToBean(UnitOfMeasurmentMasterDto.class)).list();
```

(Note: `.toLowerCase()` on the bound parameter is added here because the query's `lower(uom.uom_name)` only lowers the *column*, not the incoming value — this is the separate case-insensitivity bug already logged as its own finding below. Fixing the injection alone, without also lowering the parameter, would silently break the duplicate-check further by making it case-*sensitive* in the wrong direction. The two fixes are correctly done together, but they are two distinct behavioral changes and should be reviewed as such.)

Same pattern applies to `checkAvailableUnitById`. Both are small, additive, mechanical changes — no schema change, no API contract change, no observable behavior change beyond closing the injection hole and (if bundled) correcting the case-insensitivity bug.

## 4. Is this isolated to `dietUnitMater`, or systemic?

**Not isolated.** A grep for the same textual shape (a SQL string literal quote immediately concatenated with a Java value: `= '" +` / `+"'"`) across every `legacy/*/src/main/java` DAO file returned **~100 files** across nearly every module (Billing, ADT, BloodBank, LIS, Procurement, OTIMS, Global, CSSDMaster, Abdm, and more).

I spot-checked three of these to calibrate how many are real (given time constraints, I did not verify all ~100 — that would be the sweep itself):

- **`BillingMasterDaoImpl.java:4783`** — false positive. The vulnerable-looking line is inside a commented-out block (`/* ... */`); the live, executed version of the same query (a few lines above) correctly uses `.setParameter("applicableDate", applicableDate)`.
- **`PatientRegistrationDaoImpl.java:2585`** (`savePatientRemarksForNewEMR`) — **confirmed real, live, and arguably worse**: a raw `UPDATE patient.t_patient_registration SET patient_remark = '...'` with the remark text directly concatenated, executed via `.executeUpdate()` (a write, not just a read) against patient records. Not commented out; a genuinely callable method.
- The same file has at least three more instances at lines 3054/3081-3083 (`uhidNumber`, `skypeId`, `gender`, `middleName` all concatenated into a patient-record UPDATE) that I did not individually trace to a live call site given time constraints, but they're the identical shape.

**This is a real, systemic pattern, not a one-off mistake** — confirmed present in at least a second major area (patient registration, not just diet-nutrition config) on first spot-check, with roughly 100 more candidate files unreviewed. I recommend this become its own dedicated grep-sweep-and-triage effort (same shape as the schema-drift sweep and the duplicate-check sweep already run this session) rather than something resolved as a side effect of this one screen's fix.

## 5. Severity assessment

- **This finding (`dietUnitMater`)**: confirmed, live, exploitable via the real UI by any authenticated user (no module gate). Currently limited to a boolean-bypass on a duplicate-check query in a low-value config table — but the mechanism (arbitrary SQL execution via `createSQLQuery` + string concatenation) does not inherently limit an attacker to boolean logic; `UNION`-based exfiltration of other tables' data is architecturally plausible given the query structure, though not tested here.
- **The `PatientRegistrationDaoImpl` instance**: same mechanism, but on a write path touching patient records — higher stakes by nature of the data, not yet empirically proven exploitable the way `dietUnitMater` was (I read the code but did not run a live PoC against patient data).
- Given the breadth suggested by the ~100-file grep and the second confirmed instance on patient data, I'd put this at the same tier as the JWT signing-key finding — not because `dietUnitMater` alone is catastrophic, but because of what it's likely representative of across the codebase.

## 6. What we need from you

1. Approve (or amend) the minimal fix in §3 for `dietUnitMater` specifically — to be applied on its own dedicated branch with full Maker-Checker discipline, same as every other legacy backend fix this session.
2. Decide whether to authorize a dedicated grep-sweep-and-triage pass across all ~100 flagged files (own effort, own branch(es), own findings doc) — this is a meaningfully larger undertaking than the single-screen fix above and shouldn't be assumed in scope without your explicit go-ahead.
3. Confirm whether `dietUnitMater`'s Step 3 rebuild should proceed once the backend fix is approved (build against corrected behavior) or should wait for the fix to actually be merged first.
