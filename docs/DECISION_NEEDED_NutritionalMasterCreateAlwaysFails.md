# Decision Needed: `nutritionalMaster`'s Create (Save) operation returns HTTP 500 on every attempt, in the current live legacy backend -- root cause now confirmed: entity/schema drift

**Status: RESOLVED, 2026-07-25.** Owner approved Option A below. Fixed on dedicated branch `fix/nutritionalmaster-is-calories-column` via a single additive `ALTER TABLE kitchen.m_nutrition_master ADD COLUMN IF NOT EXISTS is_calories varchar(1);`. Verified live, before and after, covering Create, Update, AND status-toggle on both real legacy (`localhost:8080`) and the rebuild (`localhost:4200`) -- all three now return genuine HTTP 200 successes on both apps, no Angular code changes needed. Full verification detail in §7 below. Held on its fix branch, not yet merged to `main` -- merge requires separate explicit owner approval per standing `CLAUDE.md` discipline. Sections 1-6 below are retained unmodified as the original diagnostic record.

---

**Original status (retained for the record): ROOT CAUSE CONFIRMED, not fixed.** Found during Batch 19 Step 5 (real interaction testing) of the Angular rebuild, 2026-07-25. Root cause isolated via direct schema inspection shortly after this document was first drafted (see §2 below, updated). No application code or database schema has been touched to produce this document -- confirmed via direct `curl` requests against the real, running, unmodified legacy backend, independent of the rebuild entirely, and via read-only `psql` schema inspection.

---

## 1. What's confirmed

`POST /api/kitchen/master/nutritionalMaster` -- the real, live Create endpoint -- returns HTTP 500 on every attempted create, tested twice directly against the running Tomcat instance with a valid, freshly-issued auth token (not through either frontend):

```
$ curl -X POST http://localhost:8080/api/kitchen/master/nutritionalMaster \
    -H "Authorization: Bearer <valid token>" -H "userId: 1" -H "unitId: 1" \
    -d '{"nutritionCode":"CURLTEST","nutritionName":"Curl Test","organizationId":1,"unitId":1,"status":"A","createdBy":1,"createdDate":"25-07-2026 16:45:23","updatedBy":1,"updatedDate":"25-07-2026 16:45:23"}'
→ HTTP 500 {"status":"error","code":"500","message":"Something went wrong. Reference: ERR-f6337269", ...}
```

Repeated with the date-string fields omitted entirely (to test one hypothesis) -- still HTTP 500, different reference (`ERR-2bdb0d72`), same generic message.

**Update and status-toggle ALSO fail, confirmed by live testing (not just source-reading) in the rebuild against the real backend.** Since Create doesn't work, a test row was seeded directly via SQL (`INSERT INTO kitchen.m_nutrition_master ...`, bypassing the broken Java write path entirely) to get something to edit. Against that seeded row:
- `PUT /api/kitchen/master/nutritionalMaster` (Update) → HTTP 500.
- `PUT /api/kitchen/master/nutritionalMaster/updateStatus` (status-toggle) → HTTP 500.

All three write operations for this screen are broken, not just Create. This is independently reproducible in the Angular rebuild too, for the obvious reason that it calls the same live endpoints -- this is not a rebuild-introduced defect.

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

**Same root cause explains Update and status-toggle failing too.** `updateNutritionalMaster`/`updateNutritionMasterStatus` (`NutritionalMasterDaoImpl.java:132-154, 173+`) both call `session.load(NutritionalMaster.class, id)` to fetch the full entity before mutating and re-saving it -- unlike the read-only `getAll`/`getActive`/`getNutritionByName` queries, which are hand-written JPQL that explicitly project only the real, existing columns and never touch `is_calories` at all. Loading (or flushing changes to) the full mapped entity via `session.load`/`session.update` requires Hibernate to reference every mapped column, including the nonexistent one -- so every entity-level write operation fails the same way, while every column-projected read continues to work fine.

**Ruled out:** the DAO's save path also uses a named Dozer mapping (`NutritionalMasterDto_to_NutritionalMaster`, `dozer-bean-mappings.xml:8-11`) configured with `date-format="dd-MM-yyyy"`, a date-only pattern, while this screen's own `createdDate`/`updatedDate` send full `DD-MM-YYYY HH:mm:ss` datetime strings -- a plausible-looking candidate that was tested and ruled out (omitting the date fields entirely still produced the same 500).

The exact stack trace was never captured (the Tomcat instance's configured log output, `${catalina.home}/logs/myapp.log`, does not exist on disk in this environment) -- not needed now that the root cause is independently confirmed via schema inspection.

## 3. What this does and doesn't affect

- **Edit is unaffected** -- `editNutrition(nutritionObj)` copies the clicked row's values straight into the form; it never calls the server at all (confirmed by source and by live testing).
- **Create, Update, and status-toggle are ALL broken** -- see the correction at the top of this document. Effectively, this screen is currently **read-only** in practice: you can view the list, but every write action fails silently.
- **The failure is silent to the end user in current legacy, for all three operations** -- `saveNutrition()`/`updateNutrition()`/`updateStatus()` all call `GenericService.serviceAction(...).then(function(response){...})` with no failure/error callback (confirmed for `saveNutrition()`/`updateNutrition()`; `updateStatus()` follows the same pattern), and no global `$http` `responseError` interceptor exists anywhere in this app to catch it generically. An AngularJS promise rejection with no failure handler is silently swallowed -- a user clicking Save, Update, or the status toggle today sees no error, no success toast, nothing. This is the same failure class as this screen's own already-logged `PENDING-nutritionalmaster-count-endpoint-crash` (silent, no observable signal), not a "loud" one.
- **No data corruption risk** -- the opposite happens: no row is ever written or modified, so there's nothing to corrupt. This is a pure availability/functionality gap (the feature doesn't work), not a data-integrity risk.

## 4. Rebuild treatment (owner-decided 2026-07-25, now applying to all three operations, not just Create)

Per owner direction: faithfully reproduce the real, confirmed legacy failure rather than apply the "invisible bug -> implement corrected behavior" precedent, since fixing the actual failure requires a real backend fix (this frontend cannot write a database row the backend refuses to persist). The rebuild's `doSave()`/`update()`/`toggleStatus()` all already have no error-path handling (matching legacy's own missing failure callbacks) -- no code change was needed to already match, for any of the three.

## 5. Proposed fix (not applied -- for review)

Two mutually-exclusive options, both purely additive (Expand-only, per `RULES.md`):

- **Option A (recommended): add the missing column to the real table.** `ALTER TABLE kitchen.m_nutrition_master ADD COLUMN IF NOT EXISTS is_calories varchar(1);` (or whatever type/length matches the entity's `String isCalories` field and this app's other single-character-flag columns) on the local dev DB. Zero Java code changes -- the entity already expects this column; the database is what's missing it. Matches this project's established precedent for exactly this class of gap (see `docs/SESSION_LOG.md`'s BR-081 entry, which added missing columns the same way).
- **Option B: remove the unused `is_calories` field from the entity**, if nothing actually reads or needs it (the DTO/frontend do carry an `isCalories` field, but neither the view nor the controller's save/update logic appear to populate or use it meaningfully -- would need a full grep-confirm before removing, not yet done here). Removes a column the app doesn't seem to use rather than adding one it never had.

Option A is lower-risk (purely additive, no behavior change, matches how this exact defect class has been fixed before in this project) and is what's recommended, pending your approval. Either way, this needs its own dedicated branch/DB change and live before/after verification -- and now that the root cause is confirmed to break all three write operations, before/after verification should cover all three: attempt a Create, Update, and status-toggle before the fix (confirm all three 500); apply the fix; attempt all three again (confirm all three succeed and the row reflects the changes) -- held for explicit approval before merging, per standing `CLAUDE.md` discipline.

## 6. What we need from you

1. **Approve Option A** (add the missing `is_calories` column) for a dedicated fix branch with live before/after verification covering Create, Update, AND status-toggle? Or Option B, or neither for now?
2. Once approved and verified, this closes `nutritionalMaster`'s entire write-path finding and, incidentally, may be worth checking whether any other DietAndKitchen-module entity has the same drift pattern (not checked here, flagging for awareness only).

No code or schema has been touched to produce this document. This finding is logged in `docs/ANGULAR_PENDING_FINDINGS.md` under `PENDING-nutritionalmaster-create-endpoint-crash`.

---

## 7. Resolution and live verification (2026-07-25)

Owner approved Option A. Applied on dedicated branch `fix/nutritionalmaster-is-calories-column`:

```sql
ALTER TABLE kitchen.m_nutrition_master ADD COLUMN IF NOT EXISTS is_calories varchar(1);
```

Type/length matched to the entity's `String isCalories` field and this app's convention for other single-character `is_*` flag columns. Schema confirmed via `psql \d kitchen.m_nutrition_master` immediately after: all 10 original columns unchanged, `is_calories` (`character varying(1)`) added, no default, nullable -- purely additive, nothing dropped or altered.

**Verified live on real legacy (`localhost:8080`), via the actual UI (not `curl`), before deleting the test row:**
- Create: entered code `FIXVERIFY01` / name "Fix Verify Test", clicked Save, confirmed the modal, and got a genuine success toast ("Nutrition Added Successfully...!!"). Network: `POST /api/kitchen/master/nutritionalMaster` -> **200** (previously 500). Row appeared in the list.
- Update: clicked Edit on that row, changed the name to "Fix Verify Test Updated", saved, confirmed. Network: `PUT /api/kitchen/master/nutritionalMaster` -> **200** (previously 500), genuine "Nutrition Updated Successfully...!!" toast, list reflected the new name.
- Status-toggle: clicked the status toggle on that row. Network: `PUT /api/kitchen/master/nutritionalMaster/updateStatus` -> **200** (previously 500), toggle visually flipped and persisted.
- Test row (`FIXVERIFY01`) deleted via `psql` after verification.
- Note: `getNutritionMasterCount` (a separate, already-logged, unrelated bug -- `PENDING-nutritionalmaster-count-endpoint-crash`) still returns 500 as expected; this fix does not touch it, and legacy's own list rendering doesn't depend on it succeeding.

**Verified live on the rebuild (`localhost:4200`), same three operations, same method:**
- Create: `POST /api/kitchen/master/nutritionalMaster` -> **200**, real "Nutrition Added Successfully...!!" toast, row appeared.
- Update: `PUT /api/kitchen/master/nutritionalMaster` -> **200**, real "Nutrition Updated Successfully...!!" toast, name change reflected.
- Status-toggle: `PUT /api/kitchen/master/nutritionalMaster/updateStatus` -> **200**, toggle flipped and persisted.
- No Angular code changes were needed -- the rebuild already called the same live endpoints; its lack of `error:` callbacks in `doSave()`/`update()`/`toggleStatus()` is now moot for the success path (those callbacks were only ever needed to *not* silently match legacy's prior broken state, which no longer exists).
- Test row (`REBUILDFIX01`) deleted via `psql` after verification.

**Held, not merged.** `fix/nutritionalmaster-is-calories-column` awaits explicit owner approval to merge into `main`, per standing `CLAUDE.md` discipline. Full entry in `docs/BUSINESS_RULE_MANIFEST.md` under `BR-local-dev-db-drift-nutritionalmaster-iscalories`. `docs/ANGULAR_PENDING_FINDINGS.md`'s corresponding entry renamed `RESOLVED-nutritionalmaster-create-endpoint-crash`. `docs/BATCH19_STEP6_CHECKLIST.md`'s `nutritionalMaster` section updated to test the now-working flow.
