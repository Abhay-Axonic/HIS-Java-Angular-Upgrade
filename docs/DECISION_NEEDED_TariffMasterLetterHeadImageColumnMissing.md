# Decision Needed: `tariffMaster`'s Create, Update, AND status-toggle all return HTTP 500 on every attempt, on real, unmodified live legacy -- root cause confirmed: entity/schema drift, same bug class as `nutritionalMaster` (RESOLVED) / `ProductCategory` / `UnitMaster.discharge_summary_header_logo`

**Status: RESOLVED — Option A approved and applied (2026-07-28).** Logged as `BR-101` in `docs/BUSINESS_RULE_MANIFEST.md`/`BR_INDEX.md`. Fix held on `fix/tariffmaster-letterheadimagetariff-column-missing`, live-verified before/after covering all three `tariffMaster` operations plus the RIS Workbench query shape (see BR-101 for the full verification log). Awaiting merge approval separately, per standing Maker-Checker discipline. Original finding confirmed via direct authenticated `fetch` calls against the real, running, unmodified legacy backend (bypassing the frontend's own toast-swallowing) and independently via the live Tomcat `catalina.out` stack trace, plus a direct `psql \d` schema check and a check of the tracked DDL source (`legacy/Database/HIS-SME-DDL_20210430_150255584.sql`).

---

## 1. What's confirmed

All three of `tariffMaster`'s write operations fail on real, unmodified legacy today:

- `POST /api/unit/saveTariffMaster` (Create) → HTTP 500, `{"status":"error","code":"500","message":"Something went wrong. Reference: ERR-xxxxxxxx", ...}`.
- `POST /api/unit/updateTariffMaster` (Update) → HTTP 500, same generic shape.
- `POST /api/unit/updateTariffMasterStatus` (status-toggle) → HTTP 500, same generic shape.

Since Create doesn't work, a test row was seeded directly via SQL (`INSERT INTO service.m_tariff_master ...`, bypassing the broken Java write path entirely, `tariff_id=4`) to exercise Update and status-toggle against something real. Both failed identically against that seeded row.

## 2. Root cause -- confirmed via direct schema inspection and live stack trace

`TariffMaster.java` (the JPA entity, `legacy/Entity/src/main/java/com/param/billing/global/transaction/model/TariffMaster.java:224-225`) maps:

```java
@Column(name="letter_head_image_tariff")
private Character letterHeadImageTariff;
```

The real table, `service.m_tariff_master`, has no such column -- confirmed via `psql \d service.m_tariff_master` (12 columns present: `tariff_id`, `organization_id`, `unit_id`, `tariff_code`, `tariff_desc`, `valid_from`, `valid_to`, `created_by`, `created_date`, `status`, `updated_by`, `updated_date` -- exactly the entity's other 12 `@Column` fields, `letter_head_image_tariff` is the sole, isolated mismatch). **This is not local-dev drift** -- the tracked DDL source of truth itself (`legacy/Database/HIS-SME-DDL_20210430_150255584.sql:80198-80210`) also has no such column in its `CREATE TABLE service.m_tariff_master (...)` statement. The column has apparently never existed anywhere, in any tracked schema snapshot; only the Java entity ever declared it.

**Confirmed live via `catalina.out`:**
```
Caused by: org.postgresql.util.PSQLException: ERROR: column "letter_head_image_tariff" of relation "m_tariff_master" does not exist
```
on Create (`TariffMasterDaoImpl.saveTariffMaster` → `save(tariffMaster)`, a full-entity Hibernate insert), and:
```
ERROR: column tariffmast0_.letter_head_image_tariff does not exist
```
on Update, thrown from `TariffMasterDaoImpl.java:361` -- the `findById(tariffMasterDto.getTariffId())` call at the very top of `updateTariffMaster()`, which loads the full mapped entity (including the phantom column) before applying the explicit HQL `UPDATE`. `updateStatusForTariff()` has the identical `findById(...)` call at its own top (`TariffMasterDaoImpl.java:429`) and fails the same way -- confirmed live (not just read from source).

The read-only, explicitly-projected named queries (`GET_TARIFF_MASTER_LIST`, `GET_TARIFF_MASTER_LIST_BY_ID`, `GET_TARIFF_MASTER_LIST_BY_NAME`, `GET_TARIFF_MASTER_LIST_BY_NAME_NOT_ID`, etc.) never select `letter_head_image_tariff`, so the list view, the duplicate-check queries, and edit-load (`getTariffListById`, which uses its own named query, not entity load) all continue to work fine -- explaining why only the three entity-level writes fail while everything else on this screen looks healthy.

**Correction (2026-07-28, orchestrating session): this IS referenced elsewhere in the codebase — the original "fully orphaned" claim above was wrong.** `RISWorkbenchDaoImpl.java` (`legacy/Global/src/main/java/com/param/workbench/dao/RISWorkbenchDaoImpl.java:2441`) runs a raw SQL query, `select letter_head_image_tariff from service.m_tariff_master where ...`, decoupled entirely from the JPA entity mapping (a plain string-literal column reference, not an ORM field access). It's narrowly gated — only reached when a unit's `other_org_logo_required` config is `'Y'` AND a real `tariffId` is present on the radiology order — for building a RIS report's letterhead image. This means the column isn't dead/orphaned: a second, unrelated feature (RIS radiology report header generation) also depends on it, and would independently 500 under the same root cause whenever that narrow condition is met. `TariffMasterDto.java` still has no matching field, and no controller/view binds it — the RIS reference is raw SQL only, invisible to a DTO-shaped or ORM-field-shaped grep, which is why the original check missed it.

## 3. What this does and doesn't affect

- **The list view, search, and edit-load are unaffected** -- all backed by explicitly-projected named queries that never touch the phantom column.
- **Create, Update, and status-toggle are ALL broken** -- this screen is currently **read-only** in practice: you can view and filter existing tariffs, but no new tariff can ever be created, no existing tariff can ever be edited, and no tariff can ever be activated/deactivated.
- **The failure is silent-ish to the end user in current legacy** -- `saveTariff()`/`updateTariff()`/`updateStatus()` all go through `GenericService.serviceAction(...).then(...)`, whose shared failure handler (`GenericService.js`) does growl a generic error (`NETWORK_ERROR_MSG` or the response's own message) rather than swallowing it completely -- so the user does see an "ERROR!! Something went wrong" toast, just with no indication of the real cause.
- **No data corruption risk** -- no row is ever written or modified by any of the three broken paths, so there is nothing to corrupt.
- **Interacts with, but is separate from, the already-known duplicate-check code-blind bug** (`GET_TARIFF_MASTER_LIST_BY_NAME`/`_NOT_ID` check `tariffDesc` only, never `tariffCode` -- see `docs/DECISION_NEEDED_CompanyMasterTariffMasterCodeBlindDuplicateCheck.md` §6). That duplicate-check bug is confirmed still real and independently reproducible (a same-Code/different-Desc create attempt correctly passes the duplicate check and only THEN fails on the broken INSERT below) -- but because Create's actual write always 500s regardless, the duplicate-code bug can never currently result in an actual duplicate-code row being persisted in this environment. Both bugs are real and independently disclosed; fixing this column-missing bug alone would make the already-known duplicate-code bug immediately live/exploitable again (i.e., that decision doc's proposal becomes relevant again once this one is fixed).

## 4. Rebuild treatment (this batch, 2026-07-28)

Per this project's established precedent for this exact bug class (`nutritionalMaster`, RESOLVED 2026-07-25, `docs/DECISION_NEEDED_NutritionalMasterCreateAlwaysFails.md`): faithfully reproduce the real, confirmed legacy failure rather than invent client-side special-casing, since the actual database write fails server-side and no Angular code can fix that. The rebuild's `confirmSave()`/`update()`/`toggleStatus()` call the real endpoints normally, with ordinary success/error branches (matching legacy's own error-growl behavior) -- no code change is needed to "reproduce" this; it reproduces automatically because both apps hit the identical real backend. Once this column is fixed (Option A below), both apps should start succeeding immediately with zero further code changes, same as the `nutritionalMaster` precedent.

## 5. Proposed fix (not applied -- for review)

Two mutually-exclusive options, both purely additive where applicable (Expand-only, per `RULES.md`):

- **Option A (recommended): add the missing column to the real table.** `ALTER TABLE service.m_tariff_master ADD COLUMN IF NOT EXISTS letter_head_image_tariff character(1);` (matching the entity's `Character` type and this app's other single-character-flag columns, e.g. `status`). Zero Java code changes -- the entity already expects this column; only the database is missing it. Matches this project's established precedent (`nutritionalMaster`'s `is_calories` fix, `BR-local-dev-db-drift-nutritionalmaster-iscalories`) for exactly this class of gap.
- **Option B (WITHDRAWN, see correction above): remove the unused `letterHeadImageTariff` field from the `TariffMaster` entity.** Originally proposed on the belief that nothing outside `TariffMaster.java` referenced this field. That's now known to be wrong — `RISWorkbenchDaoImpl.java` independently queries the same column via raw SQL, decoupled from the entity mapping. Removing the JPA `@Column` would fix `tariffMaster`'s three write operations but would NOT fix the RIS Workbench code path, which would keep failing under the same root cause whenever its narrow gating condition is met. Option B alone is no longer a complete fix and should not be selected without also addressing the RIS Workbench consumer.

**Only Option A (add the missing column) fixes both known consumers** and is now the sole recommended option. This needs its own dedicated branch and live before/after verification covering all three `tariffMaster` operations (Create, Update, status-toggle) AND the RIS Workbench letterhead path (or at minimum a source-level re-check that adding the column resolves its query too) on real legacy -- held for explicit approval before any fix branch is opened, per standing `CLAUDE.md`/`RULES.md` discipline. This decision is out of scope for the batch-26 Angular-rebuild task that discovered it (Phase 4, explicitly the lowest-priority track per `CLAUDE.md`); flagging here rather than silently fixing it.

## 6. What we need from you

1. **Approve Option A** (add the missing `letter_head_image_tariff` column) — the only complete fix — or neither for now?
2. If approved, who opens the dedicated fix branch and performs before/after live verification (Create, Update, status-toggle, on both real legacy and the `tariffMaster` Angular rebuild once merged)?

## 7. Verification log (this session, 2026-07-28)

- `POST /api/unit/saveTariffMaster` with a fresh, never-used code/desc → HTTP 500, `catalina.out` shows `PSQLException: column "letter_head_image_tariff" of relation "m_tariff_master" does not exist`.
- Seeded `tariff_id=4` (`TESTPROBE1` / `Test Probe Desc`) directly via `psql` insert (bypassing the broken write path).
- `showUpdateBtn(4)` (edit-load) → works fine, form populates correctly (explicitly-projected query, doesn't touch the phantom column).
- `updateTariff()` on the seeded row → HTTP 500, `catalina.out` shows the same column error at `TariffMasterDaoImpl.java:361` (`findById` inside `updateTariffMaster`).
- `updateStatus(4, 'I')` on the seeded row → HTTP 500, same underlying cause (`findById` inside `updateStatusForTariff`).
- Duplicate-check re-verified independently of the write-path bug, via direct authenticated `fetch`:
  - Same Code (`TESTPROBE1`) as the seeded row, different Desc → duplicate check correctly does NOT block (confirms the already-known code-blind duplicate bug is still real) → then 500s on the actual INSERT, a separate failure.
  - Same Desc (`Test Probe Desc`, exact case) as the seeded row, different Code → correctly blocked, HTTP 200 `{"status":"error","message":"Already Exists."}` (duplicate check works as designed for Desc).
  - Same Desc in a different case (`test probe desc`) → NOT blocked (case-sensitive match confirmed, consistent with the named query using no `LOWER()`).
