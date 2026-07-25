# Decision Needed: `DepositMatrixConfigMaster`'s duplicate-check has an off-by-one threshold that lets the first duplicate row through, in the current legacy backend

**Prepared:** 2026-07-25, escalated per owner direction as a priority proposal from the `PENDING-systemic-duplicate-check-sweep` entry in `docs/ANGULAR_PENDING_FINDINGS.md` — logged there as finding #2 under "New sub-classes discovered this sweep" ("Off-by-one on an otherwise well-built check"). Re-verified directly against source this session (entity, DAO, service, DTO, controller, and frontend callers read directly, not relayed), and the dev-DB data check that was previously inconclusive was attempted again, per explicit instruction. No application code has been touched to produce this document — proposal only.

**Headline — the very first time two users independently create the same deposit-matrix combination (same org, unit, patient type, payment entitlement, bed category, and deposit-context), the duplicate-check fails to catch it, and the second, colliding row is silently persisted alongside the first.** The check is otherwise built correctly — it is org/unit-scoped and validates every identifying field — but its pass/fail threshold compares the existing-row count against `1` instead of `0`, so a genuine duplicate (`existCount == 1`) reads as "no duplicate" and is waved through. Only a *second* duplicate attempt (`existCount == 2`) would ever be correctly rejected, and by then a bad row is already sitting in the table. Because the code that reads this configuration back (at real patient admission and bed-reservation time) picks whichever row an unordered SQL query happens to return first, this isn't just a latent data-integrity gap — it is a plausible mechanism for a patient silently being charged the wrong deposit amount. This has apparently been the behavior since this screen was built; it is not something a rebuild introduces (no Angular rebuild of this screen exists yet).

---

## 1. The bug, confirmed exactly

DAO method `checkDepositMatrixExist` — `legacy/Billing/src/main/java/com/param/billing/unit/dao/DepositMatrixConfigDaoImpl.java:198`:

```java
Long existCount = (Long) sessionFactory.getCurrentSession()
                            .getNamedQuery("GET_DEPOSIT_MATRIX_EXIST_OR_NOT")
                            .setParameter("mDepositMatrixId", dto.getmDepositMatrixId()==null?0:dto.getmDepositMatrixId())
                            .setParameter("orgId", dto.getOrganisationId())
                            .setParameter("unitId", dto.getUnitId())
                            .setParameter("patientTypeId", dto.getPatientTypeId())
                            .setParameter("paymentEntitlementId", dto.getPaymentEntitlementId())
                            .setParameter("bedCategoryId", dto.getBedCategoryId())
                            .setParameter("againstDeposit", dto.getAgainstDeposit())
                            .uniqueResult();

  if (existCount>1) {
    return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null,existCount);
  } else {
    return new Response(ERROR, SUCCESS_CODE, SUCCESS_MESSAGE, null, 0L);
  }
```

The underlying named query, `GET_DEPOSIT_MATRIX_EXIST_OR_NOT` — entity `legacy/Entity/src/main/java/com/param/billing/global/model/DepositMatrixConfigMaster.java:44-57`:

```java
@NamedQuery(name = "GET_DEPOSIT_MATRIX_EXIST_OR_NOT",
        query = "SELECT COUNT(*)"
                + " FROM DepositMatrixConfigMaster depositMatrixConfigMaster"
                + " WHERE depositMatrixConfigMaster.unitId=:unitId "
                + " AND depositMatrixConfigMaster.organisationId=:orgId"
                + " AND depositMatrixConfigMaster.patientTypeId=:patientTypeId"
                + " AND depositMatrixConfigMaster.paymentEntitlementId=:paymentEntitlementId"
                + " AND depositMatrixConfigMaster.bedCategoryId=:bedCategoryId"
                + " AND depositMatrixConfigMaster.againstDeposit=:againstDeposit"
                    /*
                     * + " AND depositMatrixConfigMaster.surgical=:surgical" +
                     * " AND depositMatrixConfigMaster.nonSurgical=:nonsurgical"
                     */
                + " AND depositMatrixConfigMaster.mDepositMatrixId!=:mDepositMatrixId"),
```

**Both confirmed exactly as reported by the sweep.** Walking the actual outcomes of `existCount>1`:

- **`existCount == 0`** (no existing match) → `false` → DAO returns `0L` → service treats this as "not a duplicate," correctly proceeds to save. **Correct.**
- **`existCount == 1`** (exactly one existing row already matches every identifying field) → `false` (1 is not greater than 1) → DAO returns `0L`, the *same* response as the "no duplicate" case → service treats this as "not a duplicate" and proceeds to save a second, colliding row. **Wrong — this is the actual bug.**
- **`existCount == 2`** (two colliding rows already exist) → `true` → DAO returns the real count → service correctly reports `ALREADY_EXIST` and blocks the request. **Correct, but only because the damage from the first collision is already done.**

Caller confirms this is genuinely wired into both create and update, not a dead code path — `DepositMatrixConfigServiceImpl.java`:

```java
// saveDepositMatrixConfig, and updateDepositMatrixConfig -- identical shape
Response response = this.checkDepositMatrixExist(depositMatrixConfigMasterDto);
long count = (long) response.getObject();
if (count > 0L) {
    return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
} else {
    return iDepositMatrixConfigDao.saveDepositMatrixConfig(depositMatrixConfigMasterDto); // or updateDepositMatrixConfig
}
```

Both `save` and `update` gate on the *same* broken `checkDepositMatrixExist`, and because `object` is `0L` in both the true "no duplicate" case and the broken "exactly one duplicate" case, the service-layer `count>0L` check can never tell the difference — the bug is fully baked one layer below the service and is invisible to it.

**Confirmed genuinely correctly scoped, contrary to what "off-by-one" might suggest is the whole story** — the query validates org (`organisationId`), unit (`unitId`), and all four real identifying business fields: `patientTypeId`, `paymentEntitlementId`, `bedCategoryId`, `againstDeposit` (deposit context — reservation vs. admission, see §3), plus excludes the record's own id (`mDepositMatrixId!=:mDepositMatrixId`, defaulted to `0` on create, which safely never collides with a real generated id). `surgical`/`nonSurgical` — the two configured deposit *amounts* — are deliberately commented out of the identity check, correctly, since those are the values being configured, not part of what defines "the same combination." This is a single wrong comparison operator sitting inside an otherwise well-built check, not a wider scoping defect.

## 2. Why this looks like a real, unintentional defect

- `>` vs `>0` against a `COUNT(*)` that excludes the record's own id is a classic one-character slip — the natural-language intent of a query literally named `GET_DEPOSIT_MATRIX_EXIST_OR_NOT` is unambiguous ("does one already exist"), and `existCount>1` simply does not answer that question; `existCount>0` does.
- The same `count>1` shape recurs one method away in the same file — `getDepositMatrixConfigCount` (`DepositMatrixConfigDaoImpl.java`), a completely unrelated pagination-count check used only to size the list pager on the config screen. That method's own bug (a unit/org combination with exactly one config row would report a pager count of `0`) is a separate, out-of-scope defect, but its presence right next to the duplicate-check suggests `>1` was evidently a default template for "does more than nothing exist" copy-pasted into a place where it silently breaks the specific semantics a duplicate-check needs. This reads as a systemic copy/paste habit in this one file, not two independent, unrelated mistakes.
- Unlike the sweep's "over-blocking" findings (`NurseAllocationMaster`, `NursingStationWardMapper`), this is a **silent under-blocking** defect — nothing errors, nothing warns, the save simply succeeds when it should have been rejected. That is exactly the shape of bug this project's duplicate-check sweep was built to catch.
- There is no comment, flag, or surrounding logic anywhere suggesting "allow exactly one duplicate before blocking" was ever an intended business rule — no downstream code treats a lone duplicate as meaningfully different from a second one; the fix target (`>0`) is the only threshold consistent with the query's own name and the service's own binary `count>0L` interpretation of the result.

## 3. What this does and doesn't affect

**What the configuration actually is:** each `m_deposit_matrix_config` row maps a specific combination — patient type × payment entitlement × billing bed category × deposit context (`against_deposit`: `1` = at bed reservation, `2` = at admission) — to two configured amounts, `surgical` and `nonSurgical`. This is a genuine rate-table/pricing-matrix entity, not cosmetic metadata.

**A real downstream-consumer trace was run, not assumed.** Grepped the entire Java codebase for `DepositMatrixConfig` and the entire frontend for `depositMatrix` outside the config screen itself:

- The only read path other than the config screen's own list is `DepositMatrixConfigServiceImpl.getDepositMatrixConfigList` → `DepositMatrixConfigDaoImpl.getDepositMatrixConfigList`, a raw native SQL query joining `billing.m_deposit_matrix_config` to `adt.m_billing_bed_category_master`, filtered on the same identifying combination. **This query has no `ORDER BY` and no `LIMIT`** — if more than one row matches (exactly the state this bug allows), which row Postgres returns first is not guaranteed by anything in the query itself.
- Served via `POST /api/billing/unit/depositMatrixConfig`, consumed by real, live transaction screens, confirmed by direct reading:
  - `pendingAdmissionDischargeController.js:866-1016` (bed reservation, `againstDeposit: 1`) — `$scope.depositPending = depositMatrixList[0].reservationDeposit; $scope.depositAmount = depositMatrixList[0].reservationDeposit;` (verified at those exact lines) — submitted as the deposit amount due for the reservation.
  - `admissionController.js:5242-5377` (admission, `againstDeposit: 2`) — the identical `depositMatrixList[0].reservationDeposit` pattern (verified at lines 5278/5283/5285/5365/5376/5377) — the actual required deposit amount presented to the admitting clerk for a real patient admission.
  - `admittedPatientListController.js:4754-4803` — confirmed this screen independently calls the same `/api/billing/unit/depositMatrixConfig` endpoint and stores the response into its own `$scope.depositMatrixList` for the admitted-patient list's own deposit display; the exact indexing pattern used there was not independently re-verified line-by-line, flagged honestly rather than assumed identical.
- **In the two fully-verified call sites, the frontend unconditionally takes `depositMatrixList[0]`** — the first element of whatever the unordered backend query returned — with no de-duplication, no tie-breaking, no warning if more than one row came back.

**Conclusion on impact:** if this bug has ever let a second, colliding config row be persisted, the deposit amount actually charged to a patient at reservation or admission becomes **silently non-deterministic** — it depends on Postgres's unspecified row-return order for an unordered query, which can differ across runs, query-plan changes, or vacuum. This is a plausible mechanism for a patient being asked for the wrong deposit amount, with no error and no indication anything is wrong. This is a concrete, real-money downstream effect, not a purely theoretical data-integrity nit.

**What it does not affect:** the check's own scoping is correct (§1) — this is not a missing-org/missing-unit-style defect, and it does not corrupt any other field. A hospital that has only ever entered each combination once (no operator ever attempted the same combination twice) would never have observed this bug regardless of how long the screen has been in use — it only fires at the moment of an actual collision attempt.

**Dev-DB check, re-attempted per instruction — still inconclusive, but for a specific, now-understood reason, not a dead end:**

```
billing.m_deposit_matrix_config      → 0 rows
billing.seq_m_deposit_matrix_config  → last_value=1, is_called=false  (never advanced -- zero inserts have ever happened via this table in this dev database)
adt.m_billing_bed_category_master    → 0 rows  (bed_category_id is required on every deposit-matrix row; with zero bed categories configured, no row could ever have been created here)
public.m_patient_category_master     → 1 row
public.m_payment_entitlement_master  → 3 rows
```

Compare with other billing master-data sequences in the same schema that *have* been exercised in this dev environment (`billing_group_master_seq` at 2, `hospital_accounts_seq` at 6, `card_type_master_seq` at 15) — this dev database is not broadly empty; it has real seed/test activity elsewhere in `billing`. The `m_deposit_matrix_config` table specifically has never had a row, because one of its own required inputs (`adt.m_billing_bed_category_master`) is itself empty in this environment, making it structurally impossible for anyone to have exercised this screen here at all. **This means the "no data" result is a fact about this dev environment's seed data, not a signal about whether duplicates exist in production** — a production instance with real billing-bed-category configuration and real deposit-matrix entries would not share this specific blocker, and this check cannot rule in or rule out whether the bug has already fired there. Reporting exactly what was found rather than overstating it either way.

## 4. Proposed fix (not implemented — for review)

**Minimal fix, closing exactly the gap the code's own structure shows — change the comparison operator, nothing else:**

```java
// DepositMatrixConfigDaoImpl.java, checkDepositMatrixExist
- if (existCount>1) {
+ if (existCount>0) {
    return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null,existCount);
  } else {
    return new Response(ERROR, SUCCESS_CODE, SUCCESS_MESSAGE, null, 0L);
  }
```

No other logic changes: the query's field scoping (§1) is already correct and is not part of this proposal; the service layer's `count>0L` interpretation is already correct and needs no change once the DAO reports the true count. This mirrors the same "single operator/threshold correction, no new logic invented" style already applied and approved for this project's other duplicate-check fixes (`NurseAllocationMaster`'s restored `visitTypeId` scope, `HospitalAccountMaster`'s restored `isDefault` gate).

This is application code (`legacy/`), so per `CLAUDE.md` it would need its own dedicated branch (Maker-Checker), live-verified before/after (attempt the same combination twice today and confirm the second attempt is currently accepted; after the fix, confirm the second attempt is correctly rejected as `ALREADY_EXIST`, and that a genuinely different combination still saves normally) and the relevant module's test suite run clean, before being considered for merge.

**No alternative fix option is being proposed** — unlike the `NurseAllocationMaster` and `HospitalAccountMaster` findings, there is no adjacent business-rule ambiguity here (no commented-out field to reconsider, no "how far should scoping go" question). The only question is the threshold, and the query's own name and every caller's own interpretation of its result agree on what that threshold should be.

## 5. What we need from you

1. **Approve the minimal fix (`existCount>1` → `existCount>0`) for a dedicated legacy branch?**
2. **Should any existing production data be checked for already-collided rows before or immediately after this fix ships?** The dev-DB check above could not confirm or rule out existing duplicates (§3) — if there is a way to query production (or a production-equivalent snapshot) for `m_deposit_matrix_config` rows sharing `(organisation_id, unit_id, patient_type_id, payment_entitlement_id, bed_category_id, against_deposit)`, that would resolve the open suspicion this document was specifically prioritized over — this proposal does not have access to that data and is not assuming an answer either way.
3. **If duplicates are found in production**, a separate decision would be needed on how to reconcile them (which row is authoritative, whether affected historical admissions/reservations need review) — out of scope for this document, which only proposes preventing new collisions going forward.

No code has been touched to produce this document. This finding remains logged in `docs/ANGULAR_PENDING_FINDINGS.md`'s `PENDING-systemic-duplicate-check-sweep` entry; this document proposes the specific fix for it, per your instruction.
