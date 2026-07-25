# Decision Needed: `nutritionalMaster`'s Create (Save) operation returns HTTP 500 on every attempt, in the current live legacy backend -- root cause now confirmed: entity/schema drift

**Status: ROOT CAUSE CONFIRMED, not fixed.** Found during Batch 19 Step 5 (real interaction testing) of the Angular rebuild, 2026-07-25. Root cause isolated via direct schema inspection shortly after this document was first drafted (see §2 below, updated). No application code or database schema has been touched to produce this document -- confirmed via direct `curl` requests against the real, running, unmodified legacy backend, independent of the rebuild entirely, and via read-only `psql` schema inspection.

---

## 1. What's confirmed

`POST /api/kitchen/master/nutritionalMaster` -- the real, live save endpoint -- returns HTTP 500 on every attempted create, tested twice directly against the running Tomcat instance with a valid, freshly-issued auth token (not through either frontend):

```
$ curl -X POST http://localhost:8080/api/kitchen/master/nutritionalMaster \
    -H "Authorization: Bearer <valid token>" -H "userId: 1" -H "unitId: 1" \
    -d '{"nutritionCode":"CURLTEST","nutritionName":"Curl Test","organizationId":1,"unitId":1,"status":"A","createdBy":1,"createdDate":"25-07-2026 16:45:23","updatedBy":1,"updatedDate":"25-07-2026 16:45:23"}'
→ HTTP 500 {"status":"error","code":"500","message":"Something went wrong. Reference: ERR-f6337269", ...}
```

Repeated with the date-string fields omitted entirely (to test one hypothesis) -- still HTTP 500, different reference (`ERR-2bdb0d72`), same generic message. Both payloads match the real shape `saveNutrition()` (`nutritionalMasterController.js`) actually sends.

This is independently reproducible in the Angular rebuild too, for the obvious reason that it calls the same live endpoint -- this is not a rebuild-introduced defect.

## 2. Root cause -- confirmed via direct schema inspection

`NutritionalMaster.java` (the JPA entity, `legacy/DietAndKitchen/.../model/NutritionalMaster.java:83`) maps a field to `@Column(name = "is_calories")`. The real database table, `kitchen.m_nutrition_master`, **has no such column** -- confirmed twice, once via `psql \d kitchen.m_nutrition_master` and again independently via `information_schema.columns`:

```
$ psql -d his_local -c "SELECT column_name FROM information_schema.columns WHERE table_schema='kitchen' AND table_name='m_nutrition_master' ORDER BY column_name;"
   column_name
-----------------
 created_by
 created_date
 nutrition_code
 nutrition_id
 nutrition_name
 organization_id
 status
 unit_id
 updated_by
 updated_date
(10 rows)
```
Exactly the 10 columns the entity's other 10 `@Column` annotations declare -- `is_calories` is the sole, isolated mismatch (confirmed by grepping every `@Column` on the entity, which lists exactly 11: the 10 real columns plus this one). When `saveNutritionalMaster` calls `save(nutritionalMaster)` (`NutritionalMasterDaoImpl.java:71`), Hibernate generates an `INSERT` including every mapped column -- including `is_calories`, which the database rejects because the column doesn't exist. This single mismatch, not a date-format issue, is what produces the 500 on every attempt, with or without date fields in the request (explaining why omitting the date strings didn't change the outcome -- see the ruled-out hypothesis below).

**Ruled out:** the DAO's save path also uses a named Dozer mapping (`NutritionalMasterDto_to_NutritionalMaster`, `dozer-bean-mappings.xml:8-11`) configured with `date-format="dd-MM-yyyy"`, a date-only pattern, while this screen's own `createdDate`/`updatedDate` send full `DD-MM-YYYY HH:mm:ss` datetime strings -- a plausible-looking candidate that was tested and ruled out (omitting the date fields entirely still produced the same 500).

The exact stack trace was never captured (the Tomcat instance's configured log output, `${catalina.home}/logs/myapp.log`, does not exist on disk in this environment) -- not needed now that the root cause is independently confirmed via schema inspection.

## 3. What this does and doesn't affect

- **Edit, Update, and status-toggle are unaffected** -- confirmed both by source read (`updateNutritionalMaster`, `:132-154`, does NOT use the Dozer mapper at all; it loads the entity by id and sets fields individually) and by live interaction testing in the rebuild (both work correctly).
- **The failure is silent to the end user in current legacy** -- `saveNutrition()`'s `GenericService.serviceAction(...).then(function(response){...})` call has no failure/error callback (confirmed, and confirmed separately that no global `$http` `responseError` interceptor exists anywhere in this app to catch it generically). An AngularJS promise rejection with no failure handler is silently swallowed -- a user clicking Save today sees no error, no success toast, nothing. The record simply never saves. This is the same failure class as the already-logged `PENDING-nutritionalmaster-count-endpoint-crash` (silent, no observable signal), not a "loud" one.
- **No data corruption risk** -- the opposite happens: no row is ever written, so there's nothing to corrupt. This is a pure availability/functionality gap (the feature doesn't work), not a data-integrity risk.

## 4. Rebuild treatment (already decided, applied)

Per owner direction (2026-07-25): faithfully reproduce the real, confirmed legacy failure rather than apply the "invisible bug -> implement corrected behavior" precedent, since fixing the actual failure requires a real backend fix (this frontend cannot write a database row the backend refuses to persist). The rebuild's `NutritionalMaster.doSave()` already has no error-path handling (matching legacy's own missing failure callback) -- no code change was needed to achieve this; it already matches. Edit/Update/status-toggle are left exactly as their own (working) audited behavior.

## 5. Proposed fix (not applied -- for review)

Two mutually-exclusive options, both purely additive (Expand-only, per `RULES.md`):

- **Option A (recommended): add the missing column to the real table.** `ALTER TABLE kitchen.m_nutrition_master ADD COLUMN IF NOT EXISTS is_calories varchar(1);` (or whatever type/length matches the entity's `String isCalories` field and this app's other single-character-flag columns) on the local dev DB. Zero Java code changes -- the entity already expects this column; the database is what's missing it. Matches this project's established precedent for exactly this class of gap (see `docs/SESSION_LOG.md`'s BR-081 entry, which added missing columns the same way).
- **Option B: remove the unused `is_calories` field from the entity**, if nothing actually reads or needs it (the DTO/frontend do carry an `isCalories` field, but neither the view nor the controller's save/update logic appear to populate or use it meaningfully -- would need a full grep-confirm before removing, not yet done here). Removes a column the app doesn't seem to use rather than adding one it never had.

Option A is lower-risk (purely additive, no behavior change, matches how this exact defect class has been fixed before in this project) and is what's recommended, pending your approval. Either way, this needs its own dedicated branch/DB change and live before/after verification (attempt a Create before the fix -- confirm 500; apply the fix; attempt the same Create -- confirm success and a real row appears), held for explicit approval before merging, per standing `CLAUDE.md` discipline.

## 6. What we need from you

1. **Approve Option A** (add the missing `is_calories` column) for a dedicated fix branch with live before/after verification? Or Option B, or neither for now?
2. Once approved and verified, this closes both `nutritionalMaster`'s Create-crash finding and, incidentally, may be worth checking whether any other DietAndKitchen-module entity has the same drift pattern (not checked here, flagging for awareness only).

No code or schema has been touched to produce this document. This finding is logged in `docs/ANGULAR_PENDING_FINDINGS.md` under `PENDING-nutritionalmaster-create-endpoint-crash`.
