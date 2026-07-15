# Review: remaining small modules exception-handling fix (RabbitMQ, LinenLaundry, AssetManagement, Inventory, LIS, Entity)

*Maker session, 2026-07-06, branch `fix/remaining-small-modules-exceptions`. Closes out the last 6 of the 9 modules identified in `docs/AUDIT_*_exceptions.md` (Procurement, Abdm, and HIS-WAR were fixed in prior sessions — BR-030, BR-028, BR-029). This is the missing summary document for that session; no code was changed while writing it.*

## Summary table

| Module | Findings | Fixed as ROUTINE (throw) | Held back / interim log-only | Files touched | Billing-critical | Compile |
|---|---|---|---|---|---|---|
| RabbitMQ | 1 | 1 | 0 | 1 | 0 | BUILD SUCCESS |
| LinenLaundry | 37 | 37 | 0 | 9 | 0 | BUILD SUCCESS |
| AssetManagement | 222 | 222 | 0 | 29 | 0 | BUILD SUCCESS |
| Inventory | 298 | 297 | 1 (`WitnessApprovalService.java`) | 57 | 0 | BUILD SUCCESS |
| LIS | 118 | 118 | 0 | 43 | 12 (5 files) | BUILD SUCCESS |
| Entity | 56 | 0 (architecturally blocked — see below) | 56 (log-only) | 11 | 41 (3 files) | BUILD SUCCESS |
| **Total** | **732** | **675** | **57** | **150** | **53** | **all 6 green** |

All 6 modules were verified together in one serial build: `mvn -pl RabbitMQ,LinenLaundry,AssetManagement,Inventory,LIS,Entity -am compile -DskipTests -o` (run from `legacy/`, after all parallel sub-sessions completed) — **BUILD SUCCESS across all 6 modules**. Run serially (not in parallel per-module) specifically to avoid the shared `target/classes` corruption risk of concurrent Maven builds observed mid-session during the AssetManagement/Inventory/LIS parallel batches.

## RabbitMQ — 1/1 fixed

`docs/AUDIT_RabbitMQ_exceptions.md` found 5 raw catch occurrences, only 1 structurally live (the other 4 sit inside two entirely commented-out, never-compiled controller files). `HisService.java:24`'s `publish` had a `catch (Exception ex) { return null; }` (status-masks-failure) — replaced with `logger.error("publish failed", ex); throw new ServiceException(ex.getMessage());`, adding a class-level `log4j` logger (none existed). This method's only caller in the repo, `RabbitMQController.publishHis()`, is itself inside a fully commented-out dead file — so the fix has no effect on any currently-reachable path today. Fixed anyway per this series' mechanical definition (fixing dead code carries no production risk), flagged for owner awareness, not held back.

**Files touched (1):** `legacy/RabbitMQ/src/main/java/com/param/rabbitmq/service/HisService/HisService.java`

## LinenLaundry — 37/37 fixed

`docs/AUDIT_LinenLaundry_exceptions.md` found 37 structurally-parsed catch blocks across 9 controller files — **100% of structurally-valid catches were findings, the first module in this series where that's true.** 0 billing-critical. Every catch followed one uniform shape (`catch (Exception ex) { return new ResponseEntity<Object>(new ErrorResponse(code, message, ex), HttpStatus.INTERNAL_SERVER_ERROR); }`) — replaced with `logger.error(...); throw new ServiceException(ex.getMessage());`, adding a class-level logger to all 9 files. `RejectedLinenAcceptanceController.java` has 1 live finding plus 3 additional raw `catch(` occurrences inside `/* ... */`-fenced dead code, confirmed left untouched. Since every fixed catch was the terminal statement of its method, no now-unreachable trailing `return` statements resulted. An earlier scripted attempt at `RejectedLinenAcceptanceController.java` was caught having touched 3 catches inside dead-code comments and was reverted and redone by hand — this is why all 6 modules' catch-count-preservation was independently re-verified against each pre-fix file before considering the session done.

**Files touched (9):** under `legacy/LinenLaundry/src/main/java/com/param/linenLaundry/{linenIndent,linenIndentApproval,linenIssue,linenIssueAcceptance,linenIssueList,linenReturn,linenReturnAcceptance,rejectedLinenAcceptance,sendToCondemnate}/controller/`

## AssetManagement — 222/222 fixed

`docs/AUDIT_AssetManagement_exceptions.md` found 227 structurally-parsed catch blocks across 153 files — 222 findings across 29 files, 5 properly-rethrowing catches excluded. 0 billing-critical, including a close judgment call around `CorrectiveMaintenanceScheduleServiceImpl.java`'s call into a shared pharmacy stock-transaction DAO, investigated and excluded. Applied via 6 parallel Maker sub-sessions, each a disjoint pre-computed (file, line) list. Three sub-patterns: 82 controller findings (`ErrorResponse` pattern), 72 DAO/service findings (`HibernateException`-then-`Exception` double-catch template), 68 plain empty catches — all fixed with `logger.error(...); throw new ServiceException(...)`, adding a class-level logger to all 29 touched files (none had one). 4 files with dead/commented-out scaffolding catches were independently diffed byte-identical in their dead regions after the fix; the 5 pre-existing proper rethrows in `CorrectiveMaintenanceWorklistDaoImpl.java` were re-verified untouched. Every now-unreachable trailing `return` was resolved via per-method reachability verification by each of the 6 sub-sessions before the final central compile.

**Files touched (29):** under `legacy/AssetManagement/src/main/java/com/param/assetManagement/{auctionRequisition,auctionVendorApproval,fixedAssetOpeningBalance,indent,itemForAuction,maintenance,nonReturnableGatepass,pmsfrequency}/{controller,dao,service}/`

## Inventory — 297/298 fixed, 1 held back

`docs/AUDIT_Inventory_exceptions.md` found 304 structurally-parsed catch blocks across 299 files — 298 findings across 57 files, 6 properly-rethrowing catches excluded. 0 billing-critical, despite scrap/disposal/consumption often feeding billing elsewhere — checked against all four established criteria, none hit. Applied via 8 parallel Maker sub-sessions. Dominant sub-pattern: 231/298 (77.5%) the `ErrorResponse`-in-response-body shape; also 6 `status-masks-failure` (`return null;`-only — first nonzero count since RabbitMQ) and 5 `printStackTrace()`/`System.out.println` findings. No fabricated-SUCCESS pattern found anywhere in the module. All fixed catches got `logger.error(...); throw new ServiceException(...)`, with a class-level logger added to all 57 files.

**Held back:** `WitnessApprovalService.java`'s `updateWitnessApproval` (line 118, nested per-item disposal-approval loop) — same batch-continuation risk already identified for HIS-WAR (commit `6ca3732d`) and Procurement's `LoanInOutService`. The method is `@Transactional` and marks each worklist detail updated before the stock-allocation step that could fail; converting to `throw` would roll back every other item's genuinely-successful approval in the same request. Interim safe default applied instead: `logger.error(...)` added, no `throw`, existing swallow-and-continue behavior otherwise unchanged — did not require owner sign-off since it's strictly no worse than current production behavior. Full reasoning and 3 owner options (keep log-only permanently / accept fail-fast / fix the underlying data-integrity gap by reordering the update) are in `docs/DECISION_NEEDED_WitnessApprovalService_BatchContinuation.md`.

Dead/commented-out scaffolding catches (6 files, 7 occurrences) confirmed untouched via catch-count-preservation check.

**Files touched (57):** under `legacy/Inventory/src/main/java/com/param/inventory/{nursing,scrap,stock,stockAdjustments,store}/`

## LIS — 118/118 fixed, 12 billing-critical

`docs/AUDIT_LIS_exceptions.md` found 2,459 structurally-parsed catch blocks across 631 files — only 118 findings across 43 files (the vast majority already properly log and/or rethrow — the highest real-logger rate of any module in this series). 12 billing-critical findings across 5 files, reached via calls into a shared `IUnitApplicationBillingConfigurationDao`/`Service` for report-hosting/PDF-generation URL config. The 5 billing-critical files (`BioChemResultEntryController.java`, `BiochemReportReleaseController.java`, `LabTransactionController.java`, `BioChemResultEntryServiceImpl.java`, `LabTransactionServiceImpl.java`) were personally read and fixed by the lead session, matching the Pharmacy/OTIMS/Abdm precedent for billing-critical files. The remaining 106 findings were applied via 6 parallel Maker sub-sessions.

One audit row (`LabTransactionServiceImpl.java:1452`) was found on direct read to already satisfy this series' non-finding definition (already called `logger.error("Exection", e)`) and was correctly left untouched — the genuine 1452-numbered finding was a *different*, still-silently-swallowing catch (`getSpecialityAndSubSpecialityListByUserIdByUnitId`) that a first pass initially mis-skipped due to a line-number-drift editing error, caught and corrected via a `grep -rc "throw new ServiceException"` count reconciliation before the module was considered done. `CommonUtils.java`'s 4 status-masks-failure findings and `TTemplateResultDaoImpl.java`'s 2 `printStackTrace()`-only findings were fixed the same routine way. One missing-import fix in `BioChemistryDaoImpl.java` was caught by the final central compile. Dead/commented-out scaffolding catches (16 files per the audit) confirmed untouched.

**Files touched (43):** under `legacy/LIS/src/main/java/com/param/lis/{transaction,microbiology,histopathology,unit,global,common}/{controller,dao,service}/`

## Entity — 56/56 log-only, architecturally cannot use the standard fix

`docs/AUDIT_Entity_exceptions.md` found 57 structurally-parsed catch blocks across 1,422 files (Entity is the shared JPA entity/DTO host nearly every other module depends on — 98.6% of files are pure data classes with no exception-handling surface). 56 findings across 12 catch-bearing files, 41 billing-critical concentrated in 3 files (`GlobalCommonDateUtils.java` 32, `TimeZoneComponent.java` 8, `MpesaUtility.java` 1) — by far the largest blast radius of any fix in this series, since these 3 files are reverse-imported by 78+90 files in `legacy/Billing` and 331+232 files in `legacy/Global`.

**This module cannot use the standard `ServiceException`-based pattern.** Two blockers discovered mid-fix, confirmed by attempting the standard fix and reading the resulting compiler errors before reverting:

1. `legacy/Entity` has zero dependency on `legacy/Global` (confirmed in `Entity/pom.xml`) — `com.param.global.common.ServiceException` is physically unreachable without a circular `Entity → Global → Entity` dependency.
2. `legacy/Entity` has no logging framework dependency at all — no `log4j`, no `slf4j`.

**Interim fix applied to all 56 findings:** every catch now logs the real exception via `java.util.logging.Logger` (JDK built-in, no new dependency needed) instead of discarding it silently. Nothing was converted to `throw` — every method's existing return-value/control-flow behavior is completely unchanged, since no compliant unchecked exception type reachable from Entity would actually route to `BillingExceptionController`. Given the exceptional blast radius, converting these to `throw` was deliberately not attempted even experimentally, unlike every other module's mechanical fix-first approach. Full reasoning and 4 candidate long-term architectures (permanent log-only; a new Entity-local unchecked exception + extend `BillingExceptionController`; push the fix outward to each of the 400+ call sites instead; or formally accept log-only as permanent policy for foundational modules) are in `docs/DECISION_NEEDED_Entity_NoRouteToCentralizedHandler.md` — **this is the one document in this batch that genuinely needs the owner's architectural decision before the 41 billing-critical findings can ever be converted to a real `throw`.**

Dead/commented-out scaffolding catches (7 occurrences, all in `GlobalCommonDateUtils.java`) confirmed untouched. `ServiceIdentityDaoImpl.java` (the module's 12th catch-bearing file) had 0 findings — its 1 catch already properly rethrows — and was not touched.

**Files touched (11):** `GlobalCommonDateUtils.java` (32), `TimeZoneComponent.java` (8), `RequestJsonCheck.java` (2), `TimeZoneDaoImpl.java` (2), `TimeZoneServiceImpl.java` (2), `DateConverter.java` (2), `com/param/global/common/LocalTimeConverter.java` (2), `com/param/entity/lis/common/LocalTimeConverter.java` (2), `com/param/entity/lis/common/LocalTimeStampConvertor.java` (2), `MpesaUtility.java` (1), `GenaratePDFFromHtml.java` (1)

## Held-back / decision documents produced this session

- `docs/DECISION_NEEDED_WitnessApprovalService_BatchContinuation.md` — Inventory's 1 held-back finding; interim log-only applied, owner decision wanted on the long-term fail-fast-vs-continue-vs-fix-the-gap choice.
- `docs/DECISION_NEEDED_Entity_NoRouteToCentralizedHandler.md` — Entity's architectural gap; interim log-only applied to all 56 findings, owner decision wanted on 4 candidate long-term architectures.

## Manifest and sign-off status

6 provisional `BR-PENDING-*-exceptions` entries were added to `docs/BUSINESS_RULE_MANIFEST.md` (`rabbitmq`, `linenlaundry`, `assetmanagement`, `inventory`, `lis`, `entity`) — all marked **Pending**, since no owner interaction occurred during the fixing session. Real sequential `BR-XXX` numbers will be assigned at merge time, per RULES.md item 8. Maker-Checker review (RULES.md item 6) is still required before this branch merges, same as every prior module in this series.

This closes out module coverage for the exception-handling audit series started in `docs/AUDIT_...` for the 9 remaining modules identified in commit `d812be2f` — every `legacy/` module in that sweep has now been fixed or given a documented interim treatment.
