# Cross-tenant IDOR sweep — Phase 1 results (2026-07-31)

**Investigation only. Nothing in this document has been fixed except where explicitly marked "Already fixed" — those are prior BR entries, re-confirmed as fixed by this sweep's own detection methodology, not new work. No application code was changed to produce this document.**

**Status: Phase 1 (scoping and ranking) is complete. Phase 2 (fixing) has not started and needs a separate, explicit owner decision on scope and sequencing — see the proposal at the end of this document.**

## Context

BR-121 established that `organizationId` is never cryptographically verified anywhere in this application — only `unitId` is signed into the JWT and checked by `JwtTokenFilter`. `VerifiedIdentityService` derives the real, verified org/unit from that JWT-checked header. Five stopgap fixes (BR-121 through BR-125/126) plus a further seven found during ad-hoc candidate research tonight (BR-127 through BR-133) each closed one screen's instance of the same underlying bug shape. After the fifth ad-hoc discovery in a row, the pattern's hit rate made one-at-a-time discovery clearly worse than a systematic sweep, so a full-codebase Phase 1 scoping pass was proposed and approved.

**Two bug patterns, both approved by the owner before the sweep started:**

- **Pattern A** — a DAO/Service method takes a `unitId`/`orgId`/`organizationId` parameter (from a `@RequestBody` DTO field, a raw `@PathVariable`, or a raw `@RequestHeader`) and uses it directly in `Restrictions.eq(...)`, `.setParameter(...)`, or a raw HQL/SQL WHERE clause, with **no** `VerifiedIdentityService` reference anywhere in that method's call chain. The value is trusted, not derived.
- **Pattern B** — a DAO/Service method loads a single entity by primary key alone (`findById(id)` / `session.get(...)` / a single-ID Criteria load, no org/unit predicate on the load itself), then applies setters and persists (`.update()`/`.save()`/`.executeUpdate()`), with **no comparison anywhere** of the loaded entity's own org/unit against the caller's identity. More severe than Pattern A in the strict sense — there is no filter to bypass, only raw ID-enumeration.

A third, informal shape recurred often enough during the sweep to be worth naming even though it wasn't in the original two-pattern definition: **Pattern C — INSERT-time tenant injection** (a brand-new row is written with client-supplied org/unit, as opposed to reading/mutating an existing one). Not separately counted below; flagged per-instance where found.

## Methodology

Two full-codebase passes, each split across parallel background agents scoped by module, using the pattern definitions above verbatim. The first pass (14 top-level agents, some recursively splitting further) ran across an earlier portion of this session; when a mid-session context compaction meant that pass's raw per-finding detail (file/line/endpoint) was no longer available — only aggregate counts and a few named highlights survived — every one of those chunks was **re-run from scratch** rather than reported from memory, so every number in this document is backed by a live, traceable agent report, not a recollection. One exception: the Inventory module's Pattern A sweep was independently re-run twice (once as part of a combined Pharmacy/Procurement/Inventory chunk, once as its own dedicated 4-way nested sweep) — this is flagged as a known, not-yet-reconciled overlap in the totals below, not silently merged.

Every agent confirmed `VerifiedIdentityService` usage (or its absence) via direct `grep -rl` before treating any hit as a finding, so already-fixed code is excluded by construction, not by memory of what's been fixed. Several agents hit a session-level concurrent-subagent ceiling mid-sweep and fell back to direct, non-delegated file-by-file review rather than skipping coverage — noted per-module where it happened.

## Scale

| | Pattern A findings | Pattern B findings |
|---|---:|---:|
| ADT | 477 | 91 |
| LIS | 569 | 73 |
| Billing | ~500+ raw hits, 60+ files (no single clean per-method count given — see Billing detail) | ~52 (44 confirmed + 5 medium + 3 derivative) |
| Global | 17 (module fully covered) | ~117 (81 + ~36) |
| Pharmacy | 21 (combined chunk only) | 34 confirmed + 10 medium (**partial coverage — see caveat below**) |
| Procurement | 21 (combined chunk only) | 19 root methods / ~55 write endpoints |
| Inventory | ~78–99 (two overlapping sweeps, not yet reconciled) | 67 |
| AssetManagement | 20 | 32 |
| BloodBank | ~90 call sites / 29 files | 42 |
| CSSDMaster | ~55 call sites / 11 files | 38 |
| DietAndKitchen | 34 | 13 (+1 unreachable-unconfirmed) |
| OTIMS | ~30 (no single clean count given) | 9 |
| LinenLaundry | ~20+ | 10 (+1 medium) |
| Abdm | 20 (2 already fixed, BR-131) | 0 (architectural — no load-then-mutate shape in this module) |
| HIS-WAR | 2 | 0 (architectural) |
| RabbitMQ | 0 | 0 |
| Entity | 0 direct (2,344 named-query blocks flagged as candidates requiring cross-module tracing — not independently actionable) | 0 |

**Approximate grand total: on the order of 2,000–2,400 individual findings**, heavily concentrated in ADT, LIS, and Billing. This is a deliberately conservative framing, not a precise count — seven separate sweep agents independently noted the same three caveats that make an exact total meaningless to chase further:
1. Some numbers above are "raw hit counts" or "call sites," not cleanly deduplicated distinct methods (Billing Pattern A, OTIMS Pattern A, BloodBank/CSSDMaster Pattern A).
2. Inventory Pattern A was covered by two independent sweeps that have not been cross-reconciled.
3. Pharmacy Pattern B covered only 433 of 758 files exhaustively (`master/`, `qms/`); the remaining 325 files (`ip/`, `nursing/`, `op/`, `narcotics/`, `inPatient/`) use HQL rather than Criteria, were caught only by a secondary pass mid-sweep, and got 4 fully-traced findings plus 10 more flagged at the load-site only, not walked to their endpoints. **Pharmacy's true Pattern B count is understated** in the table above.

The purpose of this document is not a precise finding count — it's an accurate severity-ranked list of what needs fixing first, below.

## Severity Tier 1 — account takeover, privilege escalation, credential/secret disclosure

The most severe findings of the sweep. Each lets an attacker with any valid session act as, or against, a different tenant's users, roles, or infrastructure credentials — not just read or corrupt a business record.

| Finding | File | Why it's tier 1 |
|---|---|---|
| `updatePassword` | `legacy/Global/.../dao/UserMasterDaoImpl.java:1070` | Loads any user by `userId`, sets the new password, persists — zero org/unit check anywhere in the chain. **Any authenticated caller can set any other user's password, in any organization, with one request.** |
| `updateUserRoleMapper`/`.Status` | `legacy/Global/.../unit/dao/UserRoleMapperDaoImpl.java:80,101` | Reassigns a user's role mapping cross-tenant by ID. Privilege escalation. |
| `updateUserRoleMaster`/`.Status` | `legacy/Global/.../unit/dao/UserRoleMasterDaoImpl.java:105,158` | Edits the role-definition master itself (dashboard access, role type, vaccination-role mappings) cross-tenant by ID. Privilege escalation. |
| `saveUnitApplicationBillingConfig` | `legacy/Global/.../dao/UnitApplicationBillingConfigurationDaoImpl.java:76` | ~110 setters including `organizationId`/`unitId` straight from the client DTO. Lets a caller repoint or overwrite any tenant's entire billing-application config — **payment gateway keys, Razorpay secret, SMTP credentials, cash limits.** |
| `getCredentialDetails` + `addGetServiceAbdm` | `legacy/Abdm/.../AbdmCredentialDaoImpl.java` | **Already fixed, BR-131.** Live-verified exploit chain: forged org/unit leaked another org's live ABDM API credentials and access token; those stolen credentials were then usable against the victim's own external endpoint. |
| `updateOrganizationStatus` + record-level `GlobalOrganisationMaster` endpoints | `legacy/Global/.../org/dao/GlobalOrganisationMasterDaoImpl.java` | **Already fixed, BR-132.** Let any authenticated user deactivate any other tenant's entire organization via a zero-body GET. |
| `getAiTranscriptionCreds` | `legacy/OTIMS/.../dao/PreOperativeDetailsDaoImpl.java` | Leaks a third-party AI transcription service's API key, scoped only by client-supplied org/unit. |
| `OTReservationDaoImpl.getEquipmentTyoeAndAssetItem` | `legacy/OTIMS/.../dao/OTReservationDaoImpl.java:78` | Bare URL path variables (`{orgId}/{unitId}`), GET, zero indirection — the single most trivially exploitable variant found in the whole sweep. |

## Severity Tier 2 — billing-accuracy-critical

Direct, unauthorized mutation of financial records — Mission #1's explicit top concern. Ranked roughly by direct dollar-figure exposure.

| Finding | File | Notes |
|---|---|---|
| `updateBillFromConcessionManager` | `legacy/Global/.../billing/service/BillingMasterServiceImpl.java:4943` | Full recomputation of any tenant's bill totals — `netAmount`, `outstanding`, `gst`, `concessionAmount`, `billStatusId`, etc. — cross-tenant, all client-controlled. The Billing Pattern B sweep's own assessment: "most severe finding in the sweep." |
| `postServiceToEncounterBill`/`postServicesIntoExistingBill` + `getBillMasterObject`/`updateBillMaster` | `legacy/Global/.../billing/service/BillingServiceImpl.java`, `.../dao/BillingDaoImpl.java` | Loads any bill by ID with zero check, directly accumulates onto and persists `netAmount`, `totalBillAmount`, `outstanding`, `selfPayable`, `creditPayable`. |
| `updateAuthorizationRequest`/`cancelNewPreAuthorizationRequestByPreAuthorizationId` | `legacy/Billing/.../ipd/dao/IPreAuthorizationRequestDaoImpl.java:447,1065` | Sets insurance pre-authorization amount from the client, cascades to increment/decrement `MPreauthMaster`'s authorized/balance amounts. Insurance-fraud-shaped, flagged by its own sweep as "most financially dangerous finding in this batch." |
| `updateGrnNetAmt` + `getGoodReceiptNote` (shared root) | `legacy/Procurement/.../dao/goodReceiptNote/GoodReceiptNoteDao.java` | Already flagged and fast-tracked into this tier by the owner earlier tonight (raw string-concatenated SQL mutating GRN paid/balance amounts); the Pattern B sweep found the DAO's shared `getGoodReceiptNote` root is reused across 5 separate write flows, all with the same zero-scoping problem — broader than the single method originally flagged. |
| `updateProcedureOrder` | `legacy/Billing/.../procedure/dao/ProcedureOrderDaoImpl.java:1411` | Zero tenant scoping at all — not even a trusted client value. Directly mutates a procedure order's billed `amount` and status for any tenant's order. Also has empty `catch` blocks silently swallowing exceptions — a direct, incidentally-discovered hit against CLAUDE.md's #1 mission priority. |
| `updateOrderDtlsByOrderDtlsId` | `legacy/Global/.../dao/OrderDetailsMasterDaoImpl.java:847` | Recomputes/persists `ordTotalAmt`/`netAmount`/`serviceAmt` from a client-supplied `serviceAmt`, zero scoping. |
| `updateDiscountRightsMaster`/`.Status` | `legacy/Global/.../billing/global/dao/DiscountRightsMasterDaoImpl.java` (confirmed independently by both the Pattern A and Pattern B Billing sweeps) | Cross-tenant rewrite of a user's discount/bad-debit/credit-note/refund approval-limit thresholds — financial-authority data, closer to privilege escalation than plain record leakage. |
| `TDocShareVoucherDaoImpl.updateDocShareVoucher`, `TDoctorReferralShareDaoImpl.updateDoctorReferralShare` | `legacy/Billing/.../dao/` | Directly decrement/manipulate doctor-share and referral-share money fields, cross-tenant. |
| `changePatientClassId` | `legacy/Billing/.../billing/global/transaction/dao/BillingMasterDaoImpl.java:2938` | Cross-tenant IDOR **and** a confirmed SQL-injection-adjacent pattern in the same statement (raw-concatenated `patientClassId`/`reasonIdForChangePatientClass` into an `UPDATE`). |

## Severity Tier 3 — patient/clinical-data-critical

Not financial, but directly touches clinical care — result integrity, sample tracking, or destructive data loss.

- **LIS `transaction`/`microbiology`/`serology` write findings (Category A of the LIS Pattern B sweep, ~22 methods)** — lab sample accept/reject, result entry, report release, sensitivity-test-result deletion, serology recollect/retest, all cross-tenant. `BiochemReportReleaseDaoImpl.releaseNormanReports` releases biochem lab reports scoped by client-supplied `orgId`.
- **ADT's 13 "scale-score" DAOs sharing one controller** (`legacy/ADT/.../admission/dao/*ScaleScoreDaoImpl.java`) — org/unit taken as raw URL path variables, reading patient GCS/APACHE/SOFA/Bishop/Braden/Aldrete/APGAR/Mallampatti/fall-risk/nutritional-assessment scores cross-tenant.
- **`DietTemplateMapperDaoImpl.updateDietTemplateMapper`** (`legacy/DietAndKitchen/.../global/master/dao/`) — cross-tenant **destructive delete** (removes `MealDetails`/`DietTemplateDietPreferenceMapper`/`DietTemplate` rows by ID). Its own sweep's assessment: "most severe finding in this module."
- **BloodBank's 29-file "master template" family** — donor/blood-stock/cross-match/transfusion records, zero verified-identity anywhere except the two already-fixed files (BR-133).
- **`RMOAllocatioinDaoImpl`/`NurseAllocatioinDaoImpl`/`DischargeSummaryTemplateDaoImpl`/`MrdFileManagementDaoImpl`** (ADT Pattern B) — each explicitly *overwrites* the loaded entity's own org/unit with client-supplied values, letting an attacker reassign clinical-record ownership outright, not just view/edit in place.

## Severity Tier 4 — everything else (the bulk of the volume)

The large majority of both patterns' findings are "master data" CRUD — reference tables (nationality, occupation, consent type, bed type, discharge type, admission type, religion, race, ICU type, encounter type, various antibiotic/organism/media/specimen masters, etc.) where the same six-method template (`save`/`update`/`updateStatus`/`list`/`getById`/`getActive`/`count`) is copy-pasted across dozens of files per module, each repeating the identical unverified-org/unit mistake. Individually lower severity (usually a reference-data record, not money or clinical data), but the volume itself is the finding: this is now confirmed as the dominant coding pattern across ADT, LIS, Billing, BloodBank, CSSDMaster, AssetManagement, and Global, not an occasional lapse.

**Structural signal for Phase 2 sizing:** three genuinely different remediation shapes were found, and they cost very differently to fix:

1. **Single shared chokepoint** (cheapest) — LIS's Pattern B findings almost all route through exactly two base classes (`in.param.common.dao.GenericDao`, `com.param.lis.global.common.AbstractDao`); Abdm's `getCredentialDetails` was a single shared method reused 86 times. Fixing the chokepoint closes most or all of the module's findings in one change.
2. **Shared root within a feature, several call sites** (moderate) — Procurement's `GoodReceiptNoteDao.getGoodReceiptNote`, Inventory's `UploadStockDao.getUploadStock`/`ComputeStockDao.getComputeStock` — each reused across 3-8 write flows.
3. **Copy-pasted template, no shared root** (most expensive) — BloodBank (29 files), CSSDMaster, ADT's master/global cluster (36 files), the LIS master-data family — each file independently reimplements the same bug from what looks like a shared original template, so each needs its own patch; no single fix closes more than one file.

## Known limitations of this pass (explicit, not silently dropped)

- **Inventory Pattern A double coverage, not reconciled.** Covered once inside the combined Pharmacy/Procurement/Inventory chunk (findings #16-21 of that report) and once as its own dedicated 4-way nested sweep (nursing+scrap/store×2/stockAdjustments). At least one exact duplicate was confirmed directly (`ConsumptionDao.getConsumptionDetailsByTemplateTransactionId`). The table above reports both totals rather than guessing a merged number.
- **Pharmacy Pattern B is understated.** 433 of 758 files (`master/`, `qms/`) are exhaustively covered and authoritative. The remaining 325 files use HQL, were only caught by a secondary pass, and got partial treatment (4 fully-traced, 10 more flagged but not walked to an endpoint). Needs a dedicated follow-up sweep of `ip/`, `nursing/`, `op/`, `narcotics/`, `inPatient/` specifically.
- **Entity module's 2,344 named-query candidate pool is unresolved signal, not a finding list.** No live query-execution code exists in the Entity module itself (it's pure JPA POJOs), but 2,344 `@NamedQuery`/`@NamedNativeQuery` blocks across 626 files bind org/unit parameters — whether each is exploitable depends on the calling DAO in another module. Breakdown: Global 581, LIS-entity 543, ADT 435, OPD 251, Billing 159, OTIMS 138, BloodBank 100. This suggests the true total exposure surface is larger than even the ~2,000-2,400 counted above — a genuine "at least this many, likely more" caveat.
- **A related-but-distinct third bug shape recurred across many modules and was consistently excluded per the strict Pattern A/B definitions, but is real and worth its own sweep:** raw bulk `UPDATE ... WHERE id = <client id>` with **no entity load at all** (arguably worse than Pattern B — no load step to even check against). Confirmed instances: `NewWorkOrderCreationDaoImpl` (Procurement, 2200+ lines), `BarcodeDao.savePrePrintedBarcode`, `ConsignmentStockPOInvoiceDaoImpl.updateConsignmentStockPOInvoiceDetail`, `DepositMasterDaoImpl`/`BillingDaoImpl`'s bulk deposit/refund methods (Global/Billing — zero org/unit predicate in the WHERE clause at all), several OTIMS/HIS-WAR/CSSDMaster raw-SQL sites.
- **Several agents hit a session-level concurrent-subagent ceiling** mid-sweep (too many parallel agents running across this whole re-sweep effort at once) and fell back to direct, non-delegated file-by-file review to maintain coverage rather than skipping files. Noted per-chunk in the original agent reports; did not reduce coverage, only method.
- **"Medium confidence" tags throughout the underlying agent reports** (not reproduced individually in this summary) mean one of: the endpoint routing was resolved by a name-matching heuristic rather than a direct read, the method's only known caller is itself unreached from any live controller, or persistence happens via Hibernate's implicit dirty-checking flush rather than a literal `.update()` call in the same method. None of these downgrade the underlying vulnerability's confidence — only the endpoint-mapping or reachability detail.
- **`updateOrganizationStatus`'s sibling cross-org admin-console endpoints** (`createOrgFolderManually`/`createunitfoldermanually`, `allArganizationList`/`organization/active`) remain deliberately unfixed and untouched by this sweep — already tracked separately in `docs/DECISION_NEEDED_GlobalOrganisationMasterCrossOrgUtilitiesAndListEndpoints.md`, not duplicated here.

## Already fixed (validation, not new work)

Every re-run sweep agent independently confirmed via `grep -rl VerifiedIdentityService` that the following are genuinely remediated, not just believed to be: `CompanyMasterServiceImpl`, `DepartmentNursingStationMapperServiceImpl`, `UserNursingStationMapperServiceImpl`, `GlobalService.getInsuranceCompanyList`, `GlobalOrganizationMasterServiceImpl` (record-level methods), `MainStoreService`, `StorewiseGenericMapperService`, `ProductCategoryService`, `KeyIngredientsMasterDaoImpl`, `BloodComponentMasterDaoImpl`/`ServiceImpl`, `AbdmCredentialDaoImpl` (`getCredentialDetails`/`addGetServiceAbdm` specifically). This is useful independent confirmation that BR-121 through BR-133's fixes hold under a detection methodology that wasn't built with foreknowledge of which files were already fixed.

---

## Phase 2 proposal — sizing and sequencing (2026-07-31)

**Status: proposal only. No fixing has started. Owner approval required before any Phase 2 work begins, per the explicit hold placed on this sweep from the start.**

**The honest framing:** this is not a short punch-list. The same bug shape is now confirmed as the dominant DAO-writing convention across essentially the entire backend — every module examined showed it, at similar density. Treating this as "a batch of screens to fix" the way BR-121 through BR-133 were each handled individually would take, at current pace (roughly one screen per 20-30 minutes including full live before/after verification), a genuinely long time measured in many dozens of fix-and-verify cycles for Tier 1-2 alone, before even starting the Tier 4 volume.

**Recommended sequencing, if approved:**

1. **Tier 1 first, individually, same full-verification standard as tonight's BR-121–133 fixes.** Six items, all already named above. `updatePassword` in particular should not wait — it is full cross-tenant account takeover in one request.
2. **Tier 2 next**, prioritizing the shared-chokepoint items (`GoodReceiptNoteDao`, `BillingServiceImpl`/`BillingDaoImpl`'s shared bill-loading helpers) since fixing the chokepoint closes several listed findings at once.
3. **Tier 3**, likely alongside or shortly after Tier 2 given its patient-safety dimension is a different kind of risk than financial, not strictly lower priority.
4. **Tier 4 needs a scoping decision, not a fix-it-now default.** Given the "three remediation shapes" observation above: (a) chokepoint fixes (LIS's two base classes) should be scoped and attempted first, since one change plausibly closes hundreds of findings at once and is worth prototyping before committing to per-file remediation elsewhere; (b) the copy-pasted-template modules (BloodBank, CSSDMaster, ADT master/global, LIS master-data) have no shortcut — each is a real per-file cost, and the owner should decide whether to batch these the way batch-34-style screen work has been done, spread across many sessions, or accept the exposure at lower-severity reference-data as a longer-tail cleanup.
5. **The three explicitly-flagged follow-up sweeps** (Pharmacy's uncovered 325 files, the "blind bulk update, no load" third pattern, Entity's 2,344-candidate pool) should be scoped as their own Phase 1.5 scans before Phase 2 execution reaches those areas, so Phase 2 doesn't start fixing an incomplete picture the way Pharmacy's partial coverage would otherwise cause.

No numeric time estimate is offered here — the honest answer is that it depends entirely on which of the above the owner chooses to greenlight, and at what pace relative to the rest of this project's Phase 2/3/4 work (per `MASTER_PLAN.md`'s stated priority order). Recommend the owner review this document, then have a direct conversation about scope before any Phase 2 branch is opened.
