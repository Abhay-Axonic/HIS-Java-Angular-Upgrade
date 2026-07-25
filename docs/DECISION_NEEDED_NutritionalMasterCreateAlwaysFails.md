# Decision Needed: `nutritionalMaster`'s Create (Save) operation returns HTTP 500 on every attempt, in the current live legacy backend

**Status: CONFIRMED, not fixed.** Found during Batch 19 Step 5 (real interaction testing) of the Angular rebuild, 2026-07-25. No application code has been touched to produce this document -- confirmed via direct `curl` requests against the real, running, unmodified legacy backend, independent of the rebuild entirely.

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

## 2. What's NOT confirmed -- the exact root cause

The DAO's save path (`NutritionalMasterDaoImpl.saveNutritionalMaster`, `:65-80`) uses a named Dozer mapping (`NutritionalMasterDto_to_NutritionalMaster`, `dozer-bean-mappings.xml:8-11`) configured with `date-format="dd-MM-yyyy"` -- a date-only pattern, while every screen in this app (including this one's own `createdDate`/`updatedDate` fields) sends `DD-MM-YYYY HH:mm:ss` datetime strings. This looked like a strong candidate, but a follow-up test that omitted the date fields entirely *also* failed with the same generic 500 -- ruling out a clean "Dozer chokes on the extra time portion" explanation as the sole or complete cause.

The exact exception was not captured: the Tomcat instance's log4j-configured output file (`${catalina.home}/logs/myapp.log`, per `HIS-WAR/target/classes/log4j.properties`) does not exist on disk in this environment, and `myapp.log` does not exist there either. `logs/localhost.<date>.log` and `catalina.out`-equivalent files contain only Tomcat lifecycle messages, no application-level stack trace. Getting the real root cause would need either working log output configured, or a debugger/breakpoint attached to a live save attempt -- neither was available in this investigation.

## 3. What this does and doesn't affect

- **Edit, Update, and status-toggle are unaffected** -- confirmed both by source read (`updateNutritionalMaster`, `:132-154`, does NOT use the Dozer mapper at all; it loads the entity by id and sets fields individually) and by live interaction testing in the rebuild (both work correctly).
- **The failure is silent to the end user in current legacy** -- `saveNutrition()`'s `GenericService.serviceAction(...).then(function(response){...})` call has no failure/error callback (confirmed, and confirmed separately that no global `$http` `responseError` interceptor exists anywhere in this app to catch it generically). An AngularJS promise rejection with no failure handler is silently swallowed -- a user clicking Save today sees no error, no success toast, nothing. The record simply never saves. This is the same failure class as the already-logged `PENDING-nutritionalmaster-count-endpoint-crash` (silent, no observable signal), not a "loud" one.
- **No data corruption risk** -- the opposite happens: no row is ever written, so there's nothing to corrupt. This is a pure availability/functionality gap (the feature doesn't work), not a data-integrity risk.

## 4. Rebuild treatment (already decided, applied)

Per owner direction (2026-07-25): faithfully reproduce the real, confirmed legacy failure rather than apply the "invisible bug -> implement corrected behavior" precedent, since fixing the actual failure requires a real backend fix (this frontend cannot write a database row the backend refuses to persist). The rebuild's `NutritionalMaster.doSave()` already has no error-path handling (matching legacy's own missing failure callback) -- no code change was needed to achieve this; it already matches. Edit/Update/status-toggle are left exactly as their own (working) audited behavior.

## 5. What we need from you

1. **Root-cause investigation**: worth a dedicated follow-up session with real Tomcat log access (or a local debug-attach) to actually find and fix the underlying exception? Or leave this flagged and deprioritized until it naturally resurfaces?
2. **Nothing to approve for a code fix yet** -- no root cause, no proposed diff. This document exists to record the confirmed symptom and hand off the investigation, not to propose a fix.

No code has been touched to produce this document. This finding is logged in `docs/ANGULAR_PENDING_FINDINGS.md` under `PENDING-nutritionalmaster-create-endpoint-crash`.
