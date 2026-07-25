**Status: OPEN — awaiting owner decision.** No code has been touched to produce this document — proposal only.

---

# Decision Needed: `companyMaster` and `tariffMaster`'s duplicate-check queries validate Name/Description but never check the Code field, letting two rows with the same Code and different Name/Description both save with no rejection

**Prepared:** 2026-07-25, following the systemic duplicate-check sweep that first flagged both screens (`docs/ANGULAR_PENDING_FINDINGS.md`), then independently re-confirmed directly against source this session (every named query and DAO method read in full, not relayed) — same process as the `identificationMaster`/`currencyMaster`/`hospitalAccountMaster`/`visitTypeMaster` precedents. Consolidated into one document rather than two because both screens share the exact same defect shape (a Hibernate named query that checks org+unit+description/name but omits the code column entirely) in the same billing-adjacent master-data family, and a real downstream-consumer trace was run for both Code fields together since the question ("is Code used as a lookup key anywhere?") and its answer ("no — echoed as a display column only") turned out to be identical for both. `tariffMaster` carries one additional, structurally separate question (its own Clinical/Billing-Tier Addendum Step 6b rebuild-readiness status) that does not apply to `companyMaster` — that is broken out into its own numbered section (6) at the end, explicitly independent of the fix decision in sections 1-5.

**Headline — a user can save (or edit) a Company or a Tariff Scheme with a Code that's identical to an existing row's Code, as long as the Name/Description differs even slightly, and the backend will accept it silently, no error, no warning.** Both screens' existing duplicate-check logic is real and does fire correctly for the fields it does check (Name/Description, plus Email and Fax for Company) — this is not a case of the check being broken, it's a case of the check's own field list being incomplete. This has apparently been the behavior since both DAOs were written; neither has an Angular rebuild yet, so nothing about a rebuild is affected by this document either way.

---

## 1. The bug, confirmed exactly

### `companyMaster`

The save-path check, `CompanyMasterServiceImpl.saveCompanyMaster` (`legacy/Global/src/main/java/com/param/global/service/CompanyMasterServiceImpl.java:78-95`), calls `iCompanyMasterDao.getCompanyMasterListByName(companyMasterDto)` and rejects only if that returns rows. The update-path check, `updateCompanyMaster` (`:120-159`), calls `getCompanyMasterListByNameNotById` the same way.

Both DAO methods (`legacy/Global/src/main/java/com/param/global/dao/CompanyMasterDaoImpl.java:254-271`, `:274-292`) run the named queries defined on the entity, `legacy/Entity/src/main/java/com/param/global/model/CompanyMaster.java:77-89` — verified verbatim against source:

```java
@NamedQuery(name="GET_COMPANY_MASTER_LIST_BY_NAME", 
   query=" SELECT company.companyId AS companyId "	   		
   		+ " FROM CompanyMaster company "	   		
   		+ " WHERE company.unitId=:unitId AND company.organizationId=:orgId "
   		+ " AND LOWER(company.companyDesc)=:companyName AND LOWER(company.emailId)=:emailId "
   		+ " AND company.faxNo=:faxNo  AND company.isTrust='I'  "),

@NamedQuery(name="GET_COMPANY_MASTER_LIST_BY_NAME_NOT_BY_ID", 
   query=" SELECT company.companyId AS companyId "	   		
   		+ " FROM CompanyMaster company "	   		
   		+ " WHERE company.unitId=:unitId AND company.organizationId=:orgId "
   		+ " AND LOWER(company.companyDesc)=:companyName AND LOWER(company.emailId)=:emailId "
   		+ " AND company.faxNo=:faxNo "
   		+ " AND company.companyId!=:companyId AND company.isTrust='I'  "),
```

Both queries filter on `unitId`, `organizationId`, `companyDesc` (lowercased), `emailId` (lowercased), and `faxNo` — **`companyCode` never appears in either query, or anywhere else in `CompanyMasterDaoImpl.java`, `CompanyMasterServiceImpl.java`, or the entity's other named queries.** A grep of every reference to `companyCode`/`company_code` across `Entity/`, `Global/`, and `Billing/` confirms it is only ever a projected/displayed column or a plain DTO getter/setter — never a `WHERE` predicate.

### `tariffMaster`

The save-path check, `TariffMasterServiceImpl.saveTariffMaster` (`legacy/Billing/src/main/java/com/param/billing/unit/service/TariffMasterServiceImpl.java:44-72`), calls `iTariffMasterDao.getTariffListByName(tariffMasterDto)`. The update-path check, `updateTariffMaster` (`:104-146`), calls `getTariffListByNameNotId`.

Both DAO methods (`legacy/Billing/src/main/java/com/param/billing/unit/dao/TariffMasterDaoImpl.java:240-257`, `:338-356`) run the named queries on the entity, `legacy/Entity/src/main/java/com/param/billing/global/transaction/model/TariffMaster.java:25-29,81-91` — verified verbatim against source:

```java
@NamedQuery(name="GET_TARIFF_MASTER_LIST_BY_NAME",query="SELECT tariff.tariffId as tariffId "
		+ "FROM TariffMaster tariff "
		+ "WHERE tariff.organizationId=:orgId "
		+ "AND tariff.unitId=:unitId "
		+ "AND tariff.tariffDesc=:tariffDesc "),

@NamedQuery(name="GET_TARIFF_MASTER_LIST_BY_NAME_NOT_ID",query="SELECT tariff.tariffDesc as tariffDesc,"
		+ "tariff.tariffId as tariffId, "
		+ "tariff.tariffCode as tariffCode, "
		+ "to_char(tariff.validFrom,'MM/dd/yyyy') as validFrom, "
		+ "to_char(tariff.validTo,'MM/dd/yyyy') as validTo, "
		+ "tariff.status as status "
		+ "FROM TariffMaster tariff "
		+ "WHERE tariff.tariffId!=:tariffId "
		+ "AND tariff.tariffDesc=:tariffDesc "
		+ "AND tariff.organizationId=:orgId "
		+ "AND tariff.unitId=:unitId "),
```

`tariffCode` appears in the second query only as a **projected/returned column**, never as a filter predicate — the actual `WHERE` clause only tests `tariffId`, `tariffDesc`, `organizationId`, `unitId`. Note also that unlike Company's check (which lowercases `companyDesc`), Tariff's own `tariffDesc` check is plain, case-sensitive equality (no `LOWER()`) — this project's existing convention differs screen-to-screen, which matters for section 4's fix shape.

**Both gaps are total, not conditional** — there is no branch, flag, or comment anywhere suggesting Code was deliberately excluded; it is simply absent from the query text.

## 2. Why this looks like a real, unintentional defect

- Both screens' own duplicate checks already prove the developer's intent was "prevent two rows that are really the same thing" — `companyMaster` checks three separate fields (Description, Email, Fax) together, and `tariffMaster` checks Description scoped to org+unit. Neither screen skipped uniqueness checking altogether; both simply stopped one field short of covering the field a Code screen's own users would most obviously expect to be unique (Code is, by construction, the terse identifier a human types to distinguish otherwise-similar rows).
- This is not a one-off in this codebase: the same systemic sweep that flagged `companyMaster`/`tariffMaster` also found the identical gap independently in `encounterTypeMaster` (no uniqueness check on `encounterTypeCode` at all) and `consentTypeMaster` (no code-uniqueness check) — `docs/ANGULAR_PENDING_FINDINGS.md` entry 149. Four screens sharing the exact same specific omission (check Name/Description, forget Code) reads as a repeated developer habit across this codebase's master-data screens, not a considered decision that Code doesn't need to be unique on any of them.
- The omission is invisible from the frontend alone — `corporateMasterController.js` and `tariffMasterController.js` both submit whatever Code the user typed with no client-side uniqueness check of their own, so nothing hints to a user that Code isn't actually being protected the way Description is.

## 3. What this does and doesn't affect

**A real downstream-consumer trace was run for both Code fields, not assumed:**

- **Database level:** neither `public.t_company_master.company_code` nor `service.m_tariff_master.tariff_code` has a unique index or constraint — confirmed via `\d public.t_company_master` and `\d service.m_tariff_master` against the dev DB. Only the surrogate primary keys (`company_id`, `tariff_id`) are enforced unique. A query for existing same-code-different-row duplicates returned zero rows for both tables in this dev environment, but that reflects the dev DB's very sparse seed data (1 row in `t_company_master`, 0 rows in `m_tariff_master`) rather than any protective mechanism — nothing in the schema or the application would stop a duplicate from being created going forward.
- **Foreign-key trace:** every table that references `t_company_master` (`t_associated_company_master`, `t_bill_payee_details`, `m_deposit_master`, `t_insurance_tpa_mapper`, `t_patient_sponsor_details`, etc.) and every table referencing `service.m_tariff_master` (`m_package_master`, `m_company_contract_master`, `t_package_consumption_master`, `m_unit_service_tariff_master`, `t_tariff_payment_entitlement_mapper`, `t_unit_application_billing_configuration`) does so via the numeric surrogate key (`company_id`/`tariff_id`) — **never via the Code column.** A duplicate Code cannot corrupt any of these relationships, because none of them look the row up by Code.
- **Java-level trace:** grepped every reference to `getCompanyCode()`/`company_code` and `getTariffCode()`/`tariff_code` across the entire `legacy/` tree. Both fields are used only as (a) plain DTO/entity getter-setter pass-through, and (b) projected/joined display columns in a small number of unrelated read queries (e.g. `InsuranceTPAMapper.java:44` selects `company_code AS companyCode` joined via `company_id`, not code; a billing-configuration report query joins `service.m_tariff_master` via the numeric FK and merely displays `tariff_code` as a label). **Neither Code field is ever used as a `WHERE`-clause lookup key anywhere in the codebase outside the two duplicate-check queries this document is about.**
- **One candidate live external-integration consumer was found and ruled out:** `RoomRentChargesBatch.java:347` builds a `'payer_code':'"+insuranceTPAMapperDto.getCompanyCode()+"'` JSON payload that looks exactly like an external claims/insurer-integration key. On inspection, this entire code block (`legacy/HIS-WAR/src/main/java/com/param/batchjobs/scheduler/RoomRentChargesBatch.java:286-422`) sits inside a `/* ... */` comment block — **it is dead code**, an old alternate implementation of `claimSendToSmartSystem()`. The live version of that method calls `batchDao.clainSentToSmartCard()`, whose real implementation (`legacy/HIS-WAR/src/main/java/com/param/batchjobs/service/BatchDaoImpl.java:142-144`) is an empty no-op method body. So even this one plausible-looking external-system consumer does not run today.
- **Net conclusion: as far as this codebase's own live logic is concerned, both Code fields are cosmetic/display-only today.** The practical, currently-observable harm is narrower than a typical "duplicate key breaks an integration" scenario: an admin managing Companies or Tariff Schemes has no reliable way to tell two rows apart by Code (the field that exists specifically to be a short, distinguishing identifier), and could plausibly create a genuine duplicate believing Code is protected the same way Description already visibly is. This does not rule out a future consumer (a planned billing export, an insurer integration not yet built) starting to rely on Code as a unique key later, at which point today's silently-allowed duplicates would become live-breaking.
- **`companyMaster`'s own entity is the same one already flagged in `docs/ANGULAR_PENDING_FINDINGS.md` entry 148 as `corporateMasterGlobal`** — disqualified from the batch-15 pure-lookup pool specifically because it stores `creditLimit`/`creditDays` (a real monetary cap), a `CALCULATION-INPUT` needing Step 6b before any *rebuild* of this screen. That prior finding is about the rebuild-readiness of `companyMaster` as a whole and is **unrelated to and unaffected by** the narrow duplicate-check fix proposed here.

## 4. Proposed fix (not implemented — for review)

Minimal, narrowly-scoped change for each screen — add the missing Code equality condition to both the save-check and update-check named queries, in the exact shape each screen's own existing field-checks already use (deliberately not homogenizing the two screens' shapes with each other, since they currently differ and this fix is not the place to reconcile that):

**`companyMaster`** — mirror the existing `LOWER(...)` treatment already used for `companyDesc`/`emailId`:

```java
@NamedQuery(name="GET_COMPANY_MASTER_LIST_BY_NAME", 
   query=" SELECT company.companyId AS companyId "	   		
   		+ " FROM CompanyMaster company "	   		
   		+ " WHERE company.unitId=:unitId AND company.organizationId=:orgId "
   		+ " AND LOWER(company.companyDesc)=:companyName AND LOWER(company.emailId)=:emailId "
   		+ " AND company.faxNo=:faxNo AND LOWER(company.companyCode)=:companyCode "   // <-- added
   		+ " AND company.isTrust='I'  "),
```
(and the matching addition to `GET_COMPANY_MASTER_LIST_BY_NAME_NOT_BY_ID`), with `.setParameter("companyCode", companyMasterDto.getCompanyCode().toLowerCase())` added in the corresponding DAO methods, in the same position as the existing `emailId`/`faxNo` parameters.

**`tariffMaster`** — mirror the existing plain, case-sensitive treatment already used for `tariffDesc` in these same two queries:

```java
@NamedQuery(name="GET_TARIFF_MASTER_LIST_BY_NAME",query="SELECT tariff.tariffId as tariffId "
		+ "FROM TariffMaster tariff "
		+ "WHERE tariff.organizationId=:orgId "
		+ "AND tariff.unitId=:unitId "
		+ "AND tariff.tariffDesc=:tariffDesc "
		+ "AND tariff.tariffCode=:tariffCode "),   // <-- added
```
(and the matching addition to `GET_TARIFF_MASTER_LIST_BY_NAME_NOT_ID`), with `.setParameter("tariffCode", tariffMasterDto.getTariffCode())` added in the corresponding DAO methods.

**A secondary option for both screens**, worth flagging rather than deciding here: whether the fix should reject on Code-match alone (Code duplicated regardless of Description) versus the AND-combination shown above (Code duplicated only when Description also matches). The shape above keeps the fix minimal and additive — it only tightens the existing check. Making Code independently unique (its own dedicated check) would be a stronger, more defensible fix given Code's role as a unique short identifier, but is a slightly larger behavioral change than "add one field to the existing AND chain," called out here for your judgment rather than assumed.

Both are legacy application code changes (`legacy/`), so per `CLAUDE.md` each would need its own dedicated branch (Maker-Checker), verified before/after (a real create attempt with a duplicated Code confirmed to now be rejected, and the existing legitimate paths still working), held for explicit approval before merging.

## 5. What we need from you

1. **Approve the fix shape above (add Code to the existing AND-chain, matching each screen's own existing case-folding convention) for both screens?** If yes, proposed as two Maker-Checker diffs (one per screen — `Global` for `companyMaster`, `Billing` for `tariffMaster`), each verified live and held for your explicit merge approval.
2. **Or do you want the stronger version instead** — Code treated as independently unique, not just as one more AND-condition alongside Description — for either or both screens?
3. Nothing else is blocked on this decision for `companyMaster` — no rebuild of that screen is in flight (and, per section 3's cross-reference, `companyMaster`/`corporateMasterGlobal` is already separately gated behind its own Step 6b question per entry 148, independent of this fix). For `tariffMaster`, see section 6 immediately below — its rebuild-readiness question is independent of whether you approve this fix.

---

## 6. Independent finding: `tariffMaster`'s own Step 6b status under the Clinical/Billing-Tier Addendum

**This section answers a separate question from sections 1-5 above — whether a future rebuild of `tariffMaster` requires Step 6b sign-off — and does not gate or depend on the duplicate-check fix decision.** Per `docs/ANGULAR_REBUILD_PLAYBOOK.md`'s addendum, Step 6b is required for any screen touching an actual billing calculation (amount, discount, tax, cap, threshold, rounding).

**`tariffMaster`'s own entity was read directly, field by field**, both in Java (`legacy/Entity/src/main/java/com/param/billing/global/transaction/model/TariffMaster.java:184-403`) and against the live dev DB schema. Its own persisted columns are: `tariff_id` (PK), `organization_id`, `unit_id`, `tariff_code`, `tariff_desc`, `valid_from`, `valid_to`, `created_by`, `created_date`, `status`, `updated_by`, `updated_date`, `letter_head_image_tariff` — **verified directly against the entity's `@Column` annotations, confirmed independently this session.** There is no rate, amount, price, discount, tax, cap, threshold, or formula column anywhere on this entity or its backing table.

Its `@OneToMany` relations (`TariffPaymentEntitlementMapper`, `TariffVisitTypeMapper`, `TariffPatientTypeMapper`, `TariffBillingBedCategoryMapper`, `MCompanyContractMaster`, `MPackageMaster`, `SurgeryResourceConfig`) are all *applicability* mappings — which payment-entitlement/visit-type/patient-type/bed-category this named, date-ranged scheme applies to — not rate data. The DAO methods that manage these mappers only ever set `tariffId`, the mapped foreign ID, `status`, and audit columns — no amount field is read or written by any of them.

**This confirms the note already on file** (`docs/ANGULAR_PENDING_FINDINGS.md` entry 148): `tariffMaster` is a named, date-ranged "tariff scheme" **container**, not a rate table. Its own live DB foreign-key graph (`service.m_unit_service_tariff_master` references `tariff_id`, alongside `m_package_master`, `m_company_contract_master`, `t_package_consumption_master`, `t_tariff_payment_entitlement_mapper`, `t_unit_application_billing_configuration`) confirms `unitServiceTariffMaster` — a separate table/screen entirely — is the actual many-to-many rate-mapping child anchored by this scheme's ID, not a field on this scheme itself. `serviceTariffMaster` is `tariffMaster`'s other named sibling per the same prior note (not independently re-confirmed by this pass — not needed for this specific verdict).

One adjacent, non-rate observation worth noting: `TariffMasterDaoImpl.updateStatusForTariff` blocks deactivating a tariff scheme currently referenced by an active row in `t_order_details` or `service.m_company_contract_master` (a referential-integrity guard, returning "Processing Transaction" instead of applying the status change) — this shows the scheme's *status* interacts with billing-adjacent tables, but this is a usage-in-progress guard, not a calculation, and doesn't change the verdict below.

**Verdict: `tariffMaster` itself does NOT require Step 6b before a future rebuild.** Every field it actually owns is classification/scheduling metadata (a code, a description, a validity window, a status, an image flag) — the textbook shape of a PURE-LOOKUP screen per the addendum's own criteria, no different in kind from `identificationMaster`/`cardTypeMaster` in batch 15. **The real rate/pricing risk lives entirely in its child sub-screens, `unitServiceTariffMaster` and `serviceTariffMaster`** — confirmed genuinely separate entities/tables, not fields folded into `tariffMaster` — and those two screens would need their own, separate Step 6b assessment if and when either is ever picked for a future rebuild batch. Picking `tariffMaster` for a rebuild does not, by itself, put its child sub-screens in scope, and picking either child sub-screen later must independently trigger its own addendum screening rather than inheriting `tariffMaster`'s clean PURE-LOOKUP status.

No code has been touched to produce this document. Both duplicate-check findings are logged in `docs/ANGULAR_PENDING_FINDINGS.md`'s systemic duplicate-check sweep entry; this document escalates them for an explicit fix decision, per the same process already used for `identificationMaster`, `currencyMaster`, `hospitalAccountMaster`, and `visitTypeMaster`.
