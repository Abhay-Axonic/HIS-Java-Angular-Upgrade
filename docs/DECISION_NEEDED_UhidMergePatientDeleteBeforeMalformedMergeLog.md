# Decision Needed: UHID-merge approval deletes a patient record before calling a stored procedure with a malformed SQL string — the failure is currently silently swallowed

**Prepared:** 2026-07-30, found during triage of `RequestDiscountController.java`'s exception-swallowing fast-track fix (see BR-105 precedent, `docs/BUSINESS_RULE_MANIFEST.md`). No application code has been touched to produce this document — proposal only.

**Headline — approving a UHID (patient) merge request deletes the "from" patient's record first, then calls a stored procedure whose SQL string is malformed and very likely fails every time. Because the failure is caught and discarded, the delete can commit even though the merge-log call that was supposed to run alongside it never succeeds.**

---

## 1. The bug, confirmed exactly

`RequestDiscountDaoImpl.getUHIDMergeReuestApprovalData` (`legacy/Billing/src/main/java/com/param/billing/global/transaction/config/dao/RequestDiscountDaoImpl.java:598-634`):

```java
if(fromPatientId!=null && fromPatientId >0)
{
    String hql = "DELETE FROM PatientDetails WHERE patientId = :patientId";
    int deletedEntities = sessionFactory.getCurrentSession().createQuery(hql)
            .setParameter("patientId", fromPatientId)
            .executeUpdate();
}

int flag = (int)sessionFactory.getCurrentSession().createSQLQuery(
        "CALL patient.patient_merge_log(:fromPatientId, :toPatientId, :reqSentBy :, :reqSentTo, :reqId)")
        .setParameter("fromPatientId", fromPatientId)
        .setParameter("toPatientId", toPatientId)
        .setParameter("reqSentBy", reqSentBy)
        .setParameter("reqSentTo", reqSentTo)
        .setParameter("reqId", reqId)
    .uniqueResult();

return new Response<>(SUCCESS, SUCCESS_CODE, null, null, flag);

} catch (HibernateException he) {
} catch (Exception e) {

}
return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
```

Two independent problems, confirmed by direct read (not inferred):

1. **The `CALL` string has a stray `:` before the comma** — `:reqSentBy :,` instead of `:reqSentBy,`. This is very likely a syntax/parse error on every real invocation (unverified against a live DB call — this dev environment has no reachable trigger scenario confirmed yet, see §3).
2. **Both catch blocks are empty** — `catch (HibernateException he) {}` and `catch (Exception e) {}` — so whatever exception the malformed `CALL` throws is discarded with no logging, and the method falls through to a generic fabricated error `Response`. This is the same catch-body-return exception-swallowing pattern being fixed in this file's other 13 methods (BR-105-style), but this specific method has a materially worse consequence than "the caller sees a fake error" — see next point.
3. **No transaction boundary visibly protects the delete.** The enclosing service method (`RequestDiscountServiceImpl.getUHIDMergeReuestApprovalData`, lines 335-344) does not appear to wrap this DAO call in an explicit rollback-on-exception boundary beyond whatever Spring's default `@Transactional` propagation provides at the method level — and because the DAO's own catch block swallows the exception before it can propagate, Spring's transactional-rollback machinery may never see a reason to roll back in the first place. The `DELETE FROM PatientDetails` can therefore commit even when the very next statement (the merge-log call) fails.

## 2. Why this matters more than a typical swallowed-exception finding

Every other instance found in this file (and in BR-105's TrustMaster) is "a real error gets hidden and a fake response is returned instead" — annoying, and worth fixing for CLAUDE.md's #1 priority, but not itself destructive. This one is different: **a real, irreversible delete (`DELETE FROM PatientDetails`) happens unconditionally before the operation that's supposed to accompany it, and nothing currently guarantees they succeed or fail together.** If the merge-log call reliably fails (per the malformed SQL), every UHID merge approval today may be silently deleting the source patient's record without ever successfully writing the merge-audit log — a genuine, ongoing data-integrity risk for merge history/audit trail, not just a UX annoyance.

## 3. What has and hasn't been verified

- **Confirmed via direct source read:** the malformed SQL string, the empty catch blocks, and the unconditional delete-before-call ordering.
- **Not yet confirmed:** whether `CALL patient.patient_merge_log(...)` actually throws on the current schema (the malformed parameter list may or may not be syntactically fatal depending on exact JDBC/driver parsing behavior — this needs a live test), and whether any UHID merge has actually been approved in this dev environment (if none have, this may be a latent bug rather than one that has already caused data loss here).
- **Not yet confirmed:** whether `patient.patient_merge_log` the stored procedure itself exists and is callable at all in this dev DB, independent of the malformed parameter list.

## 4. Options for the owner

1. **Fix the SQL string only** (remove the stray `:`) as a separate, tightly-scoped additive change, verified live before/after (does the CALL now succeed?), independent of the exception-swallowing fix.
2. **Fix the SQL string AND wrap both statements in an explicit all-or-nothing transaction boundary** so a failure in the merge-log call now correctly rolls back the patient delete too — the more complete fix, but a larger, more deliberate change to transactional behavior that may warrant its own Maker-Checker review separate from BR-105-style error-propagation fixes.
3. **Hold this specific method out of the RequestDiscountController exception-swallowing fast-track** entirely until one of the above is decided, fixing only the other 13 methods in that file now (matching this project's established pattern of holding out a genuinely different-shaped finding rather than mechanically fixing everything in a file at once).

No fix has been proposed or implemented for this yet. Recommend option 3 for the immediate RequestDiscountController fast-track (don't block 13 legitimate fixes on this one), with options 1/2 as separate follow-up work once the owner decides how thorough the transactional fix should be.
