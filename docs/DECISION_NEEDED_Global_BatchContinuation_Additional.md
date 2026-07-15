# Decision Needed (Informational): Additional Batch-Continuation Findings on fix/global-peripheral-exceptions

**Status:** Interim safe-default fixes already applied directly (no owner sign-off needed — matches `ScheduledLogsScheduler`/HIS-WAR/Procurement/Inventory precedent). This doc is a record for the owner and for Checker, not a pending decision blocking further work.
**Related:** `docs/DECISION_NEEDED_ScheduledLogsScheduler_BatchContinuation.md` (the precedent this fix follows), `docs/AUDIT_Global_exceptions.md`, `docs/REVIEW_global-peripheral-exceptions.md`, `docs/BUSINESS_RULE_MANIFEST.md` BR-PENDING-global-peripheral-exceptions.

## What triggered this review

The Checker flagged that 6 files on `fix/global-peripheral-exceptions` — `SlotMasterServiceImpl.java`, `OrderDetailsMasterDaoImpl.java`, `WalkInSalesService.java`, `UnknownPatientRegistrationDaoImpl.java`, `EncounterMasterServiceImpl.java`, `NewBornAssessmentServiceImpl.java` — might carry the same batch-continuation risk as `ScheduledLogsScheduler.java` (a catch inside a loop, or called from an unprotected loop elsewhere, where converting a swallowed exception to `throw` aborts processing of sibling records) but were fixed with a `throw` anyway, without the same careful treatment.

Each file's diff against master was read in full, and every method whose catch was converted from empty/`printStackTrace()`-only to `throw new ServiceException(...)`/`throw new BillingException(...)` was traced to its actual callers across the whole repo (not just Global — Billing, ADT, LIS, HIS-WAR modules too), checking whether any caller loops over a collection of independent records and invokes the method without its own try/catch.

## Result summary

| File | Verdict |
|---|---|
| `SlotMasterServiceImpl.java` | Safe as converted, with one **latent** (currently dormant) risk noted below. No code change. |
| `OrderDetailsMasterDaoImpl.java` | **~20 methods reverted** to log-only (see table). Rest of the ~50 converted methods left as `throw` — confirmed safe (single-request callers, or the old code already crashed with an equivalent NPE at the same point, so the throw doesn't change what actually aborts). |
| `WalkInSalesService.java` | Safe as converted. All 17 of its own converted catches are single-request/single-transaction calls or dead code. (It also *calls into* `OrderDetailsMasterDaoImpl`'s methods from a couple of its own loops — that risk is fixed by the DAO-side reverts above, not by touching this file.) |
| `UnknownPatientRegistrationDaoImpl.java` | Safe as converted. Every method is a single-request DAO call; the one method with an internal loop (`patientSearchByMultipleCriteriaGlobal`) already correctly kept its per-row catch log-only and only converted the outer, once-per-request catch. |
| `EncounterMasterServiceImpl.java` | **2 methods reverted** (`appendCensentData`, `saveChildEncounter`). Rest of the ~70 converted methods are single-encounter/single-request calls — safe. |
| `NewBornAssessmentServiceImpl.java` | Safe as converted. All 3 converted methods are single-request calls; the one internal loop (apgar records) already correctly kept its catch log-only. |

No other `@Scheduled`-annotated class in `legacy/Global` was touched by this fix cycle besides `ScheduledLogsScheduler.java` (already handled) — confirmed by grepping every `@Scheduled` class in the codebase against the full list of files this fix touched.

---

## SlotMasterServiceImpl.java — safe, with one latent risk noted

All converted methods are called exactly once per HTTP request from `SlotMasterController`. Two mid-loop catches inside `generateSlotsDoctorSheduling` and one inside `getSlotsNew` were correctly left log-only (not converted), since they sit inside per-schedule/per-slot loops.

**Latent risk (not fixed, dormant):** `copyBackdatedSlots`, `saveBackDatedSlotsToSlotLogs`, and `deleteBackdatedSlotsFromSlotMaster` are called, once per timezone/unit group, inside a `for (Map.Entry<...> entry : timeZoneAndUnitsMap.entrySet())` loop in `RevertAllocatedQuantity.ForceFullyMidNightBatch()` (`legacy/HIS-WAR/.../batchjobs/scheduler/RevertAllocatedQuantity.java`). If one timezone group's backdated-slot processing throws, the whole nightly-force-batch method aborts, skipping `midnightBatch`/`postRoomRent`/`doctorShare`/`batchJobToUpdateAppointmentStatus` for every remaining unit. This method's only call site (`ExecuteBatchForcefully.java:57`, the manual "force batch" endpoint) is currently commented out, so the risk cannot fire in production today. **No code change made** — flagging so that if that manual endpoint is ever re-enabled, these three throws should be reconsidered first (e.g., catch locally inside the per-timezone loop and continue).

---

## OrderDetailsMasterDaoImpl.java — reverted methods

For every method below, a caller elsewhere in the codebase (Billing, Global, or ADT module) loops over a `List`/`Set` of independent business records — order lines, credit-note lines, bill line-items, discharge cancellations — calling the DAO method with **no per-iteration try/catch**, and the *old* code's silent-ERROR/`null`/`0` return was either ignored or merely status-checked, so the loop continued to the next record on failure. Converting these to `throw` meant one bad record now aborts every other pending record in the same request/transaction, in several cases with **already-partially-committed changes** left inconsistent (e.g. a credit note marked cancelled in the DB while some of its linked order-detail lines never got their doctor-share reversed).

All of these were reverted to the pre-fix shape: catch logs via `logger.error(...)` (kept — this is a genuine, permanent improvement, since these failures were previously either silently swallowed or only `printStackTrace()`'d), but no longer throws; the original fallback return (`Response(ERROR,...)`, `null`, or `0`, matching what was there before this fix cycle) is restored.

| Method | Representative loop-caller found | 
|---|---|
| `saveOrderDetailsMaster` (1-arg) | `RoomRentChargesBatch` (`@Scheduled` nightly job) → `RoomRentBatchChargesServiceImpl.postRoomRentCharges()` → `OrderMasterServiceImpl.saveOrderMaster`, looping over up to ~1000 admissions per run |
| `saveOrderDetailsMaster` (2-arg, `masterDto,list`) | `WalkInSalesService.updatePayeePharamcy` cart-item loop; `TDoctorShareServiceImpl.processDocShareForAlreadyBilledServices` bulk bill-detail endpoint |
| `saveOrderDetailsMaster` (3-arg, `masterDto,list,pckgMpprList`) | `OrderMasterServiceImpl.saveOrderMasterPharmacy`/`saveOrderMasterOt`/`getDocShareForLineItem`, each looping over one order's line items |
| `updateOrderDetails` | `InvestigationServiceImpl.saveOrdersFromInvestigation`, `CoversheetTemaplateServiceImpl.saveCoversheetAsTemaplate`, `NewEMRTemplateServiceImpl.saveNewEMRTemplate` — nested loops over order-detail lines |
| `updateIsBilledStatus` | `BillingServiceImpl.postServicesIntoExistingBill(List<Integer> orderDetailsIdsList)` — a bulk endpoint accepting the ID list directly from the request body |
| `orderDetailsLogAfterUpdatePayee` | `BillingMasterServiceImpl.updateOrderDetailsForPayee` loop (currently no live caller of that method — dormant, reverted anyway since the cost is zero) |
| `getOrderPayeeDetailsByOrderDetailsId` (both overloads) | `BillingMasterServiceImpl.getRenderedServicesByEncounterIdForBillSave` (4-arg) and `BillingServiceImpl.cancelPendingService` via a discharge-cancellation loop in `BillingMasterController` |
| `getAvailableRefundAmountCreditNote` | `BillPayeeDetailsServiceImpl.getBillPayeeDetailsByBillIdCr`, two loops over bill line items |
| `updateReviewedServiceStatus` | `EncounterMasterServiceImpl.saveEncounterMaster` loop over `orderDetailsIds` |
| `cancelUnRenderedServices` | `BillingServiceImpl.cancelPendingService` ← `BillingMasterController` discharge-cancellation loop |
| `getOrderDetailsPackageMapperByOrderDetailsId` (both overloads) | `BillingMasterServiceImpl.getPatientServiceOrderList`/`cancelPendingService`, `InvestigationServiceImpl.getCurrentInvestigationsByProcedureIdAndVisitId`, `BillPayeeDetailsServiceImpl.getDebitNoteDetailsByBillMasterId` |
| `updateOrderDetailsObj` | `TDoctorShareServiceImpl.updateDocShareAfterCreditNote(Cancellation)` credit-note-line loop (already-confirmed regression from the earlier commit) |
| `updateOrderDetailsPayeeMapper` | `BillingMasterServiceImpl.updateBillFromConcessionManager` nested bill/order-line loop |
| `cancelProcedureSerivicesByProcedure` | `ProcedureOrderServiceImpl.saveProcedureList` loop over procedure orders |
| `requestDiscountDetails` | `OrderDetailsMasterServiceImpl.updateOrderDetailsWithDiscount`, `OrderMasterServiceImpl.saveOrderMasterWithDiscount` — core bill-save loops |
| `saveOrderDetailsMasterList` | `CoversheetTemaplateServiceImpl.saveTemplateListDataForNewEMR` lab-order loop |
| `updateFreeOrderService` | `OrderMasterServiceImpl.saveOrderMasterWithDiscount` order-line loop |
| `updateFreeTextInvestigations` | Same `CoversheetTemaplateServiceImpl` loop as `saveOrderDetailsMasterList` |
| `getDoctorShare` / `getDoctorShareVac` / `getReferShare` / `getReferShareVac` | `CreditNoteMasterServiceImpl.saveCreditNoteAgainstService`/`saveCreditNote` credit-note-detail loops; `RequestDiscountServiceImpl.cancelRequest`/`CreditNoteMasterServiceImpl.cancelCreditNote` via `OrderDetailsMasterServiceImpl.opdateOrderDetailsAndDoctorShare` |
| `updateDoctorShare` / `updateReferShare` | Same `CreditNoteMasterServiceImpl.saveCreditNote` loop |
| `updateOrderDetailsForDoctorShare` | `CreditNoteMasterServiceImpl.saveCreditNote` loop |
| `getOrderAppointmentDetails` | `CreditNoteMasterServiceImpl.saveCreditNoteAgainstService` loop, via `OrderDetailsMasterServiceImpl.cancelServices` |
| `checkConsultationService` | `BillingMasterServiceImpl.cancelBill` payment-receipt loop |
| `getEhcOrderedServices` | `BillingServiceImpl.postServicesIntoExistingBill` bulk order-detail-ID endpoint |

**Left as `throw` (confirmed safe):** `updateOrderDetailsBetterPerformance` and `saveOrderDetailsMappers` (the batch loop is *internal* to the method — one call already represents "the whole batch," with its own `BatchUpdateException` per-chunk handling correctly untouched), `getOrderDetailsByIdForPayeeUpdate` (nearly all callers immediately dereference the result with no null-check, so the old code already crashed with an equivalent NPE at the same point — the one genuine risk, via `EncounterMasterServiceImpl.saveChildEncounter`, is fixed by reverting `saveChildEncounter` itself, see below), and the remaining ~30 methods, which are either single-request/single-record calls or dead code with no live caller anywhere in the repo.

---

## EncounterMasterServiceImpl.java — reverted methods

- **`saveChildEncounter`** — `EncounterController`'s `/encounterEhc` endpoint accepts `List<EncounterMasterDto> encounterList` and loops over it with no per-iteration try/catch, calling `saveChildEncounter` once per child encounter. Internally, `saveChildEncounter` calls `OrderDetailsMasterDaoImpl.getOrderDetailsByIdForPayeeUpdate` and only conditionally dereferences the result (inside an `if (mpperDto.getIsPackageService()=='Y')` branch), so the old code did **not** always crash on a null/failed fetch — it would previously complete "successfully" and let the controller loop continue to the next child encounter. Reverted to log-only + restore the original `Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null)` fallback.
- **`appendCensentData`** — its own inner per-placeholder catch was already correctly left log-only. The *outer* catch (converted to throw) is reachable from `legacy/LIS/.../LabTransactionServiceImpl.printBarcodeLab1(List<RadiologyOrdersListDto> list)`, which loops over a batch of independent lab-sample barcode requests, calling `appendCensentData` once per sample with no per-iteration try/catch, and returns the accumulated list only after the loop completes — one bad sample's consent-template fill would abort barcode generation for every other sample in that batch. Reverted to log-only + restore `Response(ERROR, null, null, null, null)`.

**Left as `throw` (confirmed safe):** `updateAutorenderServices`/`updateFutureOrderedServices` (their internal loops iterate pending services *within one encounter's single save/render action* — an atomic user action, not an independent batch, so aborting the whole action on failure is correct, not a regression), and the remaining ~65 converted methods (single-encounter/single-request calls, or dead code such as `postCyclicOrderDetail` which has zero callers anywhere in the repo).

---

## Additional finding from the broader legacy/Global diff sweep

**`TicketSystemMasterServiceImpl.setTicketSystemMasterissueDisc`** (`legacy/Global/src/main/java/com/param/global/ticketofsystem/service/TicketSystemMasterServiceImpl.java`) had a per-ticket `catch (JSONException err)` inside a `listObject.forEach(...)` converted to `throw new ServiceException(...)`. One ticket with malformed legacy JSON in its issue-description field would now abort the `forEach` and fail the entire ticket-list search for every caller. **Reverted to log-only** (same shape as the ScheduledLogsScheduler fix — logging added, no throw, loop continues).

One borderline case was found and left alone: `AppointmentSchedulingController.rescheduleCyclicAppointment` wraps its entire `for` loop over `SlotMasterDto`s in one outer try/catch (no per-iteration catch) — this was already the case before this fix cycle; the diff only changed what the outer catch does (fabricated ERROR response → logged throw), not the loop-abort behavior itself, which was already present. This looks like the already-tracked fabricated-success pattern rather than a new batch-continuation regression — no code change made, flagging for Checker's awareness only.

No other `@Scheduled` class in the codebase besides `ScheduledLogsScheduler.java` was touched by this fix cycle.

---

## Build verification

`mvn -pl Global -am compile -DskipTests -o` (run from `legacy/`): **BUILD SUCCESS**, no unreachable-statement or other compile errors after all reverts.
