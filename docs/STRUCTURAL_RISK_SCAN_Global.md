# Structural Risk Scan — `legacy/Global` log-only catch blocks

**Read-only analysis. No code was modified to produce this document.**

## Purpose

Branch `fix/global-peripheral-exceptions` currently has every touched catch block in
`legacy/Global` converted to log-only (`logger.error(...)`, no `throw`) — a deliberate
safety decision after repeated Checker-caught regressions from throw-conversion (see
`docs/BUSINESS_RULE_MANIFEST.md`, entry `BR-PENDING-global-peripheral-exceptions`,
"round-3 Checker regression fixes" and "batch-continuation follow-up").

This document classifies every one of those log-only catch blocks by **structural risk
category**, so a future session can decide — case by case, with owner sign-off per
RULES.md — which are safe to convert back to `throw` and which must stay log-only.
This scan makes **no recommendation** on that question; it only maps the terrain.

## Scope and method

- Diff analyzed: `git diff master -- legacy/Global` (739 files changed, 16,526
  insertions / 12,083 deletions).
- Every added `logger.error(...)` line in that diff was located (8,037 sites, one per
  touched catch block — a 1:1 match with the diff's added-`logger.error` line count,
  confirming no catch block in the diff was missed or double-counted).
- For each site, the **current** file on disk (the diff's "new" side, since this branch
  is checked out) was parsed with a brace-depth/interval parser that strips comments and
  string/char literals first, then walks the chain of enclosing blocks (method → class,
  or any intermediate `try`/`catch`/`finally`/`for`/`while`/lambda/etc.) outward from each
  catch site.
- Categories were assigned in priority order **A → B → C → D → E → (F or G)**: the first
  matching structural condition wins, since a block can technically nest inside more than
  one risky construct (e.g., a loop inside a high-fan-in file) and the categories are not
  meant to double-count.
- Category **E** (import fan-in) was computed by grepping `import <fully.qualified.Name>;`
  across the **entire repo** (16,514 `.java` files across all `legacy/*` modules, not just
  Global) and counting distinct importing files, threshold ≥ 20.
- Two sites were manually reclassified from the automated F bucket to **G** after a
  targeted sweep for framework-callback patterns the fixed categories don't literally
  cover (see Category G notes below). That sweep also checked for `extends
  OncePerRequestFilter`/`HandlerInterceptorAdapter` (none found — no false negatives for
  D), `@Around`/`@Before`/`@After` (none found), `@Scheduled` (one file, already caught by
  category B since its catch sites sit inside `for` loops), and
  `InitializingBean`/`DisposableBean`/`BeanPostProcessor`/`Runnable`/`Callable`/`Thread`
  (none found).
- **Known limitation**: category F's definition ("single well-understood caller") implies
  call-site/reachability analysis that this scan does not perform for all 7,969 F sites —
  that would require a full cross-module call graph, infeasible to hand-verify at this
  scale in one pass. F here means *no risky structural pattern was detected* (not in a
  finally/loop/lifecycle-hook/cross-cutting-callback/high-fan-in file). Treat F as "no flag
  raised," not as "individually verified safe." A sample of prior manifest entries
  (BR-006/007/009/010/011) already did deeper per-method review of exactly this layer of
  catch and found it safe to convert — this scan's F bucket is consistent with that
  precedent but does not re-derive it site-by-site.

## Summary — counts per category

| Category | Description | Sites | Distinct files |
|---|---|---:|---:|
| A | Inside a `finally` block | 17 | 12 |
| B | Inside a loop (`for`/`while`/`forEach`/stream) over independent records | 32 | 12 |
| C | Inside a constructor, `@PostConstruct` method, or static initializer | 0 | 0 |
| D | Class implements `Filter`/`HandlerInterceptor`, or `@Aspect`/`@Around`/`@Before`/`@After` | 0 | 0 |
| E | File/class has ≥20 reverse-import fan-in repo-wide | 17 | 2 |
| F | Genuinely safe — top-level Controller/Service/DAO, no risky pattern detected | 7,969 | 734 |
| G | Unclear — flagged for manual review | 2 | 2 |
| **Total** | | **8,037** | **739** |

C and D are genuinely zero for this diff (verified both via the parser and via a
targeted repo-wide grep sweep for `@PostConstruct`, `static {`-style initializers, and
`implements ... Filter`/`HandlerInterceptor`/`@Aspect` across all 739 changed files — see
Category C/D notes below).

---

## Category A — inside a `finally` block (17 sites, full list)

These are exception handlers wrapping cleanup code (usually stream/resource `close()`
calls) inside a `finally` block. Converting these to `throw` risks overriding an
already-successful result from the `try` block with an exception from cleanup — exactly
the regression already found and reverted once in this branch for `AuthKey.java` (see
manifest entry, round-3 regression fix #1).

| File:Line | Method | Detail |
|---|---|---|
| `legacy/Global/src/main/java/com/param/global/bi/report/dao/BIReportsDaoImpl.java:415` | `getBIReportsAllCollectionList` | catch nested inside a finally block |
| `legacy/Global/src/main/java/com/param/global/common/AuthKey.java:38` | `getAuthProperties` | catch nested inside a finally block (the known, previously-reverted case) |
| `legacy/Global/src/main/java/com/param/global/common/HealthCardPdf.java:161` | `generateHealthCard` | catch nested inside a finally block |
| `legacy/Global/src/main/java/com/param/global/dao/OrderDetailsMasterDaoImpl.java:630` | `updateOrderDetailsBetterPerformance` | catch nested inside a finally block |
| `legacy/Global/src/main/java/com/param/global/dao/OrderDetailsMasterDaoImpl.java:785` | `saveLog` | catch nested inside a finally block |
| `legacy/Global/src/main/java/com/param/global/dao/OrderDetailsMasterDaoImpl.java:2366` | `saveOrderDetailsMappers` | catch nested inside a finally block |
| `legacy/Global/src/main/java/com/param/global/dao/PatientRegistrationDaoImpl.java:2148` | `saveCustomPatientDetails` | catch nested inside a finally block |
| `legacy/Global/src/main/java/com/param/global/dao/UnitServiceMasterDetailsDaoImpl.java:545` | `saveContractServiceDetails` | catch nested inside a finally block |
| `legacy/Global/src/main/java/com/param/global/dao/global/GlobalDao.java:5934` | `updateSaleBatchUpdate` | catch nested inside a finally block |
| `legacy/Global/src/main/java/com/param/global/dao/global/GlobalDao.java:8673` | `executeBatchForMappers` | catch nested inside a finally block |
| `legacy/Global/src/main/java/com/param/global/doctor/dao/DoctorScheduleDaoImpl.java:176` | `saveDoctorSchedule` | catch nested inside a finally block |
| `legacy/Global/src/main/java/com/param/global/org/dao/GlobalOrganisationMasterDaoImpl.java:369` | `getOrganizationListByOrgName` | catch nested inside a finally block |
| `legacy/Global/src/main/java/com/param/global/otims/dao/SurgicalCalendarMasterDaoImpl.java:128` | `saveSurgicalCalendarMaster` | catch nested inside a finally block |
| `legacy/Global/src/main/java/com/param/opd/unit/dao/SlotMasterDaoImpl.java:302` | `generateSlots` | catch nested inside a finally block |
| `legacy/Global/src/main/java/com/param/opd/unit/dao/SlotMasterDaoImpl.java:599` | `generateSlotsDoctorSheduling` | catch nested inside a finally block |
| `legacy/Global/src/main/java/com/param/opd/unit/dao/SlotMasterDaoImpl.java:3873` | `saveBackDatedSlotsToSlotLogs` | catch nested inside a finally block |
| `legacy/Global/src/main/java/com/param/privilege/Logger/Logger.java:44` | `log` | catch nested inside a finally block (the module's own custom logging utility) |

## Category B — inside a loop over independent records (32 sites, full list)

A `throw` here aborts the entire batch/loop on the first bad record instead of skipping
it and continuing — this is precisely the "batch-continuation" risk already identified
and reverted once in this branch for ~20 `OrderDetailsMasterDaoImpl` methods, 2
`EncounterMasterServiceImpl` methods, and 1 `TicketSystemMasterServiceImpl` method (see
manifest, "batch-continuation follow-up"). `ScheduledLogsScheduler.java` and
`SlotMasterServiceImpl.java` reappear here at additional, previously-unreviewed line
numbers — worth checking against that prior review before any throw-conversion decision.

| File:Line | Method | Detail |
|---|---|---|
| `legacy/Global/src/main/java/com/param/global/dao/OrderDetailsMasterDaoImpl.java:569` | `updateOrderDetailsBetterPerformance` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/dao/ProductionReleaseEmailNotificationConfigDaoImpl.java:132` | `sendNotificationOnEmailOfReleaseDetails` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/dao/UnknownPatientRegistrationDaoImpl.java:2030` | `patientSearchByMultipleCriteriaGlobal` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/opd/service/EncounterMasterServiceImpl.java:4226` | `appendCensentData` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/org/service/GlobalUnitMasterServiceImpl.java:643` | `reminderForSubscription` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/pharmacy/service/WalkInSalesService.java:347` | `deleteAllocationScheduler` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/pharmacy/service/WalkInSalesService.java:353` | `deleteAllocationScheduler` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/DoctorConsultationServiceMapperServiceImpl.java:103` | `saveDoctorConsultationServiceMapperService` | loop: WHILE |
| `legacy/Global/src/main/java/com/param/global/service/MessageQueueServiceImpl.java:339` | `getMessageFromQueqe` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/MessageQueueServiceImpl.java:510` | `getMessageFromQueqe` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/MessageQueueServiceImpl.java:569` | `getMessageFromQueqe` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/MessageQueueServiceImpl.java:653` | `getMessageFromQueqe` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/MessageQueueServiceImpl.java:682` | `getMessageFromQueqe` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/NewBornAssessmentServiceImpl.java:63` | `saveNewBornAssessment` | loop: WHILE |
| `legacy/Global/src/main/java/com/param/global/service/ScheduledLogsScheduler.java:168` | `syncSchedulingLogs` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/ScheduledLogsScheduler.java:193` | `syncSchedulingLogs` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/ScheduledLogsScheduler.java:265` | `syncSchedulingLogs` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/ScheduledLogsScheduler.java:368` | `syncSchedulingLogs` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/ScheduledLogsScheduler.java:421` | `syncSchedulingLogs` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/ScheduledLogsScheduler.java:477` | `syncSchedulingLogs` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/ScheduledLogsScheduler.java:568` | `syncSchedulingLogs` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/ScheduledLogsScheduler.java:606` | `syncSchedulingLogs` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/ScheduledLogsScheduler.java:632` | `syncSchedulingLogs` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/ScheduledLogsScheduler.java:665` | `syncSchedulingLogs` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/ScheduledLogsScheduler.java:709` | `syncSchedulingLogs` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/service/ScheduledLogsScheduler.java:746` | `syncSchedulingLogs` | loop: FOR |
| `legacy/Global/src/main/java/com/param/global/ticketofsystem/service/TicketSystemMasterServiceImpl.java:273` | `setTicketSystemMasterissueDisc` | loop: FOREACH_LAMBDA |
| `legacy/Global/src/main/java/com/param/opd/unit/service/SlotMasterServiceImpl.java:553` | `generateSlotsDoctorSheduling` | loop: WHILE |
| `legacy/Global/src/main/java/com/param/opd/unit/service/SlotMasterServiceImpl.java:575` | `generateSlotsDoctorSheduling` | loop: WHILE |
| `legacy/Global/src/main/java/com/param/opd/unit/service/SlotMasterServiceImpl.java:679` | `generateSlotsDoctorSheduling` | loop: WHILE |
| `legacy/Global/src/main/java/com/param/opd/unit/service/SlotMasterServiceImpl.java:703` | `generateSlotsDoctorSheduling` | loop: WHILE |
| `legacy/Global/src/main/java/com/param/opd/unit/service/SlotMasterServiceImpl.java:1657` | `getSlotsNew` | loop: FOR |

## Category C — constructor / `@PostConstruct` / static initializer (0 sites)

None of the 8,037 log-only catch blocks sit inside a constructor, a `@PostConstruct`
method, or a static initializer. Verified two ways: (1) the brace-interval parser found
no enclosing frame of that type for any site; (2) a repo-wide grep for `@PostConstruct`
across the 739 changed files found 18 files with the annotation, but in every case the
annotated method (typically a cache-warming `initCache()`) contains no try/catch of its
own — the catch sites in those same files belong to unrelated, later methods. One file
(`LoginController.java`) has a `@PostConstruct` on `initializeDuoClient`, but it is
commented out (`//	@PostConstruct`) in the current source, so it does not count as an
active lifecycle hook — this method's catch is separately significant for a different
reason (Duo fail-open/fail-closed policy, already handled — see manifest round-3 fix #3)
but is not a Category C constructor/lifecycle risk.

## Category D — Filter / HandlerInterceptor / `@Aspect` (0 sites)

None of the 739 changed files implement `Filter` or `HandlerInterceptor` (via
`implements` or `extends` of Spring base classes like `OncePerRequestFilter` /
`HandlerInterceptorAdapter`), and none are annotated `@Aspect` or carry
`@Around`/`@Before`/`@After`/`@AfterReturning`/`@AfterThrowing` on any method. Verified via
repo-wide grep across all 739 changed files. See Category G below for two related but
not-quite-matching cross-cutting callback classes that were flagged instead of silently
folded into F.

## Category E — high reverse-import fan-in (17 sites, full list)

Both files are widely-shared date/time utilities. A behavior change here (even
"observability-only" logging change) has proven precedent for prior caution in this
project (`Util.getDate` is the same method already reverted once in this branch's
round-3 regression fixes, specifically because its many callers rely on its graceful
fallback contract, not because of the logging itself).

| File:Line | Method | Detail |
|---|---|---|
| `legacy/Global/src/main/java/com/param/global/utility/Util.java:204` | `getDate` | imported by 275 other `.java` files repo-wide |
| `legacy/Global/src/main/java/com/param/opd/common/CommonDateUtils.java:55` | `getDate` | imported by 27 other `.java` files repo-wide |
| `legacy/Global/src/main/java/com/param/opd/common/CommonDateUtils.java:82` | `getDate` | imported by 27 other `.java` files repo-wide |
| `legacy/Global/src/main/java/com/param/opd/common/CommonDateUtils.java:98` | `getAvalDate` | imported by 27 other `.java` files repo-wide |
| `legacy/Global/src/main/java/com/param/opd/common/CommonDateUtils.java:112` | `getTime` | imported by 27 other `.java` files repo-wide |
| `legacy/Global/src/main/java/com/param/opd/common/CommonDateUtils.java:147` | `getFirstDateOfMonth` | imported by 27 other `.java` files repo-wide |
| `legacy/Global/src/main/java/com/param/opd/common/CommonDateUtils.java:159` | `getLastDateOfMonth` | imported by 27 other `.java` files repo-wide |
| `legacy/Global/src/main/java/com/param/opd/common/CommonDateUtils.java:193` | `getLastDateOfMonth` | imported by 27 other `.java` files repo-wide |
| `legacy/Global/src/main/java/com/param/opd/common/CommonDateUtils.java:203` | `getLocalDate` | imported by 27 other `.java` files repo-wide |
| `legacy/Global/src/main/java/com/param/opd/common/CommonDateUtils.java:213` | `getDate` | imported by 27 other `.java` files repo-wide |
| `legacy/Global/src/main/java/com/param/opd/common/CommonDateUtils.java:225` | `getStringDate` | imported by 27 other `.java` files repo-wide |
| `legacy/Global/src/main/java/com/param/opd/common/CommonDateUtils.java:237` | `getDayOfWeek` | imported by 27 other `.java` files repo-wide |
| `legacy/Global/src/main/java/com/param/opd/common/CommonDateUtils.java:249` | `getWeekOfMonth` | imported by 27 other `.java` files repo-wide |
| `legacy/Global/src/main/java/com/param/opd/common/CommonDateUtils.java:261` | `getLocalTime` | imported by 27 other `.java` files repo-wide |
| `legacy/Global/src/main/java/com/param/opd/common/CommonDateUtils.java:356` | `getDateByzone` | imported by 27 other `.java` files repo-wide |
| `legacy/Global/src/main/java/com/param/opd/common/CommonDateUtils.java:387` | `getDateByzone` | imported by 27 other `.java` files repo-wide |
| `legacy/Global/src/main/java/com/param/opd/common/CommonDateUtils.java:403` | `getStringDateFromLongDate` | imported by 27 other `.java` files repo-wide |

Note: `Util.java`'s `getDate` at line 204 is a **different** catch site from the one
already reverted in the manifest's round-3 fix (that fix references `Util.getDate`'s
date-parsing fallback contract generically; there may be more than one overload/catch in
this file — this scan lists every log-only catch site independently of prior narrative
descriptions, so cross-check line numbers against the manifest before assuming identity).

## Category F — genuinely safe (7,969 sites — count + representative examples only)

Per task scope, this category does not get an exhaustive file:line list — only a count
and representative examples, since it is the default "no risky structural pattern
detected" bucket (734 distinct files).

**Breakdown by architectural layer** (by filename convention):

| Layer | Sites |
|---|---:|
| DAO (`*DaoImpl.java` / `*Dao.java`) | 3,984 |
| Controller (`*Controller.java`) | 1,934 |
| Service (`*ServiceImpl.java` / `*Service.java`) | 1,872 |
| Other (helpers, DTOs-with-logic, listeners not flagged G, etc.) | 176 |
| Utility (`*Util*.java`, low fan-in) | 3 |

**Representative examples:**

| File:Line | Method | Layer | Fan-in |
|---|---|---|---:|
| `legacy/Global/src/main/java/com/param/actionmaster/controller/ActionMasterController.java:63` | `addActionMaster` | Controller | 0 |
| `legacy/Global/src/main/java/com/param/actionmaster/service/UserActionFavouritesServiceImpl.java:29` | `saveUserActionFavourites` | Service | 0 |
| `legacy/Global/src/main/java/com/param/actionmaster/dao/UserActionFavouritesDaoImpl.java:43` | `saveUserActionFavourites` | DAO | 0 |
| `legacy/Global/src/main/java/com/param/duoAuthentication/dao/DuoAuthenticationDaoImpl.java:56` | `getApplicationPropertyData` | DAO | 0 |
| `legacy/Global/src/main/java/com/param/equipmentchecklist/service/EquipmentChecklistMapperServiceImpl.java:37` | `saveChecklistEquipmentMapperMaster` | Service | 0 |
| `legacy/Global/src/main/java/com/param/global/common/AuthKey.java:31` | `getAuthProperties` | Other | 12 |
| `legacy/Global/src/main/java/com/param/global/common/HealthCardPdf.java:153` | `generateHealthCard` | Other | 12 |
| `legacy/Global/src/main/java/com/param/global/common/Msg91MessageSendNew.java:88` | `msg91Postdata` | Other | 13 |
| `legacy/Global/src/main/java/com/param/actionmaster/DateUtils/DateUtils.java:21` | `getDate` (a distinct, low-fan-in utility of the same name as the Category E ones — not to be confused) | Utility | 2 |

## Category G — unclear, flagged for manual review (2 sites, full list)

| File:Line | Method | Detail |
|---|---|---|
| `legacy/Global/src/main/java/com/param/global/common/CustomSynchEventListener.java:42` | `onApplicationEvent` | Class implements Spring's `ApplicationListener<ScheduleLogsDto>` — invoked by the container on every published event, not a normal single-request call site. Same cross-cutting-risk *spirit* as Category D (Filter/Interceptor/Aspect) but does not literally match that interface list, so it was not auto-assigned to D. |
| `legacy/Global/src/main/java/com/param/global/service/ScheduleLogsListener.java:47` | `valueBound` | Class implements `HttpSessionBindingListener` — invoked by the servlet container on every session attribute bind/unbind. Same reasoning as above. |

## Category H — not in scope, pre-existing, flagged for future work (4 sites)

Found by the Checker during final review of this branch. These are **not** log-only
catch-block conversions from Categories A–G above — they are a structurally distinct
risk pattern (raw resource cleanup and fail-open error handling) that the A–G taxonomy
does not cover. The Checker confirmed all four are byte-identical to `master` — this
branch only added logging at these sites, it did not introduce or alter the underlying
risk. They are listed here for visibility and future remediation, **not** for action on
this branch; per CLAUDE.md/RULES.md Maker-Checker scope, fixing pre-existing structural
issues unrelated to the branch's log-only conversion is out of scope and would require
its own dedicated branch, its own manifest entry, and owner sign-off if it changes
observable behavior.

| File:Line | Method | Pattern | Detail |
|---|---|---|---|
| `legacy/Global/src/main/java/com/param/global/dao/GlobalServiceMapperServiceDaoImpl.java:133-162` | `callProcedure` | Raw JDBC connection, no cleanup on exception branch | Two raw `Connection`s (`conn` opened line 138, `connectionNew` opened line 147) are closed manually in the try body (lines 143, 152) with no `finally`/try-with-resources; if either connection's own statement execution throws before its `close()` call is reached, that connection is leaked — the catch blocks (158, 160) only log, they never close. |
| `legacy/Global/src/main/java/com/param/global/dao/ServiceMasterDaoImpl.java:457-506` | `readCSVFile` | Raw JDBC connection, cleanup gated on a null-check that can miss the connection-open failure branch | `connection` is opened at line 462; the `finally` block (495-504) only closes `statement`/`connection` inside `if (statement != null)`. If `prepareStatement` (line 465) or `setAutoCommit` (line 463) throws before `statement` is assigned, `connection` is never closed — it leaks on that specific exception branch. |
| `legacy/Global/src/main/java/com/param/global/ticketofsystem/util/ProductionSupportAppServiceImpl.java:84-114` | `uploadFile` | HTTP response stream, no cleanup on exception branch, inside a loop | Each loop iteration opens an `okhttp3.Response` (line 103) with no try-with-resources or explicit `close()`, on either the success or exception path; a failure partway through the loop (e.g. on iteration 3 of 5) leaves prior iterations' response bodies/connections unclosed since there is no `finally`. |
| `legacy/Global/src/main/java/com/param/global/opd/dao/AppointmentSchedulingDaoImpl.java:2661-2670` | `isPatientSync` | Fail-open: DB exception swallowed into `return true` | On any exception from the sync-status query, the method returns `true` instead of propagating or returning `false`; three call sites (`AppointmentSchedulingServiceImpl.java:339, 1429, 2080`) treat a `true` return as "patient is synced" and skip a conditional branch as a result — a transient DB error is silently treated as "synced," not as "unknown." |

---

## Caveats for whoever uses this scan next

1. This is a **structural** scan only — it does not re-derive or contradict any prior
   Maker-Checker decision already recorded in `docs/BUSINESS_RULE_MANIFEST.md`. Cross-check
   file:line hits here against that manifest before proposing a throw-conversion, since
   several files (`ScheduledLogsScheduler.java`, `SlotMasterServiceImpl.java`,
   `AuthKey.java`, `Util.java`) already have documented, owner-relevant history.
2. Categories were assigned by priority (A wins over B wins over C, etc.) — a site
   counted under A is not separately counted under E even if its file also has high
   fan-in. If a future session wants to convert an A/B/C/E site to `throw`, re-check
   whether it *also* falls into another risky category before treating the single label
   as the full risk picture.
3. Category F's 7,969 sites were **not** individually call-site-verified — see the
   Scope and method section above. Treat F as "no structural red flag," not "individually
   proven safe." Any future throw-conversion pass should still compile-check and, for
   business-relevant methods, spot-check callers the way prior Billing-module fixes
   (BR-006 et al.) did.
4. Categories C and D came back empty for this diff — that is a genuine finding (not a
   scan failure), double-checked by direct repo-wide grep in addition to the parser.

---

## Step 3 Review Outcome (owner decision, 2026-07-08)

The owner reviewed all 19 flagged sites from this scan — the 17 Category E (high-fan-in)
sites and the 2 Category G (unclear) sites — and decided: **all 19 stay log-only. None
are converted to `throw`.**

**Category E (`Util.java:204`, `CommonDateUtils.java`'s 16 sites) — stay log-only.**
Reasoning: both files are widely-shared date/time utilities with a documented
graceful-fallback contract (return the original/uncomputed value rather than propagate),
and that contract has already been proven load-bearing once on this branch —
`Util.getDate` is the exact method reverted from `throw` back to `return date;` in the
round-3 Checker regression fix (see `docs/BUSINESS_RULE_MANIFEST.md`,
`BR-PENDING-global-peripheral-exceptions`, "round-3 Checker regression fixes," fix #4).
Converting any of these 17 sites to `throw` would repeat that same regression across
275 (`Util.getDate`) and 27 (`CommonDateUtils.java`, all 16 methods) reverse-importing
callers repo-wide, each of which may depend on the fallback behavior in ways not
individually verified. No new evidence changes that risk calculus, so the decision is to
leave the fallback contract intact.

**Category G (`CustomSynchEventListener.java:42`, `ScheduleLogsListener.java:47`) — stay
log-only.** Reasoning: both classes are container-invoked lifecycle callbacks —
`CustomSynchEventListener` implements Spring's `ApplicationListener<ScheduleLogsDto>`
(invoked by the container on every published event), `ScheduleLogsListener` implements
`HttpSessionBindingListener` (invoked by the servlet container on every session attribute
bind/unbind). Neither is a normal single-request call site with a single well-understood
caller; a thrown exception from either could disrupt unrelated events or sessions beyond
the one that triggered the failure. The owner classified this as the same risk class as
Category D (Filter/Interceptor/Aspect, 0 sites in this diff but 0-tolerance for `throw`
by design) even though neither class literally matches that interface list — the scan
itself already flagged this "same risk *spirit*" reasoning when assigning these two sites
to G rather than silently folding them into F.

**Disposition:** no code change results from this review — all 19 sites remain exactly as
they are today (log-only, no `throw`). Per CLAUDE.md's Refactoring vs. Behavioral Change
definition, a decision to leave existing output/control-flow untouched is not itself a
behavioral change; this section documents the completed review and decision only. This
closes Step 3 of the recovery strategy for these 19 sites. The remaining 7,969 Category F
sites (and, separately, the 0 Category C/D sites) are unaffected by this decision and
retain the caveats already stated above (F = "no structural red flag raised," not
"individually proven safe").
