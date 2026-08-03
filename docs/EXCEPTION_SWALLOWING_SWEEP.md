# Exception/Error-Swallowing & False-Success-Masking Sweep — Live Working Document

Status: ACTIVE (Phase A triage complete; Category 1 live-verification in progress)
Owner-approved scope: 2026-08-03. Same methodology as the cross-tenant IDOR Phase 1 sweep.
Standing rule: nothing fixed without individual live verification; nothing merged/pushed without explicit approval.

---

## 1. Methodology

Grep the relevant code shapes across `legacy/` (all modules, excluding `target/`), triage by
reading each candidate, rank by fan-in/blast radius, and distinguish security-relevant instances
(Category 1: silent tenant/data-integrity masks; Category 2: masked backend failures presented as
success) from pure-correctness instances (Category 3). Each candidate fix gets the full
live-verify standard: real session, seeded real row, trigger the failure path, psql-confirm
(never trust the HTTP response), fix, before/after matrix, held for approval.

Three code shapes:
- **S1** empty/comment-only catch blocks
- **S2** DAO methods: write (`update/save/persist/merge`) + unguarded `return SUCCESS` (no ERROR
  path, no result-check, no rethrow)
- **S3** service methods: call DAO then return SUCCESS unconditionally (the DoseMaster class)

## 2. Census (verified numbers, 2026-08-03)

**Raw grep counts (informed estimates pending real triage — NOT findings):**
- `catch` blocks total: ~23,000 across 8,294 java files
- empty/comment-only catch blocks (raw): ~2,700 (line-anchored verification reduced to **46**;
  the brace-matcher over-counted `catch` inside strings/comments)
- DAO methods with write + SUCCESS return: ~700 raw -> **208** after line-anchored verification
  -> **188** after result-check filter (20 were guarded by `if (result != null)` etc. — not bugs)
- service methods calling DAO + returning SUCCESS: ~600 raw -> **297** candidates (heuristic;
  per-method reads will prune further)

**Category 3 estimate: ~380 instances** (informed estimate pending real triage):
- 46 empty-catch->ERROR (visible failure, non-diagnosable) — correctness-only
- 73 catch-returns-null — correctness-only
- ~260 remaining service-layer swallows outside the billing/clinical priority set
Labeled as an ESTIMATE: the IDOR Phase 1 sweep showed raw->real is a 5-10x reduction; expect
30-60 real findings from ~485 candidates once Category 1/2 triage completes.

## 3. Category classification

- **Category 1 — silent tenant/data-integrity masks (security-adjacent, IDOR tier):** writes on
  tenant-scoped data where a masked failure (or masked cross-tenant write) hides integrity
  problems. Even where an IDOR is already fixed, a masked write can hide the same class.
- **Category 2 — masked backend failures presented as success (financial/clinical priority):**
  the client believes a charge/save succeeded when it didn't. Ranked by real-world consequence:
  cash/refund/receipt > patient-billing > clinical-result > master data.
- **Category 3 — swallowed exceptions with no user-facing consequence:** deferred, larger
  later cleanup effort. Not urgent.

## 4. getBillMasterObject — CONFIRMED NON-ISSUE (masking) + SEPARATE OPEN SECURITY GAP

### 4a. Masked-failure concern: NON-ISSUE (verified live, 2026-08-03)

**Concern raised (owner):** a bill calculation silently failing while returning stale/wrong data
with an apparent success signal.

**Verification result:** `getBillMasterObject` correctly propagates real errors. Both the
Billing-module (`BillingMasterDaoImpl`, L2752 — IDOR-fixed, throws on cross-tenant/null,
catch-rethrow) and Global-module (`BillingDaoImpl`, L66) implementations rethrow; the service
layers (`postServiceToEncounterBill` in both modules, `postServicesIntoExistingBill`) catch and
`throw new BillingException(e.getMessage())`; the controllers rethrow. Live test (seeded full
org-999 victim billing chain, forged org-1 session POST `/api/billing/post/bill/service`): the
endpoint returned **HTTP 500 with a real error reference** (`ERR-5cab1510`) and the victim bill
was untouched (psql-confirmed). **On genuine failure the client receives a proper HTTP error
status and the failure propagates; SUCCESS is only returned on real calculation results.**
The "apparent success on stale data" reading was a false alarm — no masking on this path.

### 4b. SEPARATE FINDING (not part of the non-issue): Global `BillingDaoImpl.getBillMasterObject` has NO ownership check

The live verification that cleared the masking concern simultaneously proved a **separate,
real security gap** that must not be buried by the "non-issue" label:

- The live billing path (`postServicesIntoExistingBill` -> called from
  `BillingMasterController:1315` `/api/billing/post/bill/service` and `InvestigationController:303`)
  uses **Global `BillingDaoImpl.getBillMasterObject`** — a bare `findById` + return with **zero
  `VerifiedIdentityService`/ownership check** (confirmed in the deployed jar bytecode).
- The BR-121-class IDOR fix (`44627287`, "BillingMasterDaoImpl cross-tenant IDOR — getBillById,
  getBillMasterObject, changePatientClassId") patched ONLY the **Billing-module**
  `BillingMasterDaoImpl` twin. The Global DAO (same method name, different file) was missed.
- Live proof of the cross-tenant READ: the org-1 session's query chain (SQL log, 13:09:52)
  successfully joined the org-999 admission chain and resolved `billId` = 99901 before the run
  hit an environmental error.
- The WRITE was not completed in this dev DB only because `t_service_identity` does not exist
  (schema drift — the table is absent from `pg_tables` though `ServiceIdentity.java` exists in
  source). In a properly-migrated DB, the cross-tenant-loaded bill would proceed to calculation
  and write.

**Status: OPEN — separate fix proposed** (same VerifiedIdentityService ownership check as the
Billing-module twin; plus record/fix the `t_service_identity` schema drift; plus the two
calculation defects found on the Billing-module twin at L3481 outstanding-doubling and
L3597-3598 dueAmount-base). Held for owner approval — NOT part of the confirmed non-issue.

## 5. Category 2 ranked list (shape-verified in source, 2026-08-03)

Money-path first (consequence-ranked); every instance needs live verification before fix:

1. `CashHandoverMasterDaoImpl.saveCashHandoverMaster` — SUCCESS x2, no ERROR return (cash desk)
2. `CompanyReceiptMasterDaoImpl.saveCompanyReceiptMaster` — save->unconditional SUCCESS (receipts)
3. `CompanyReceiptMasterDaoImpl.mergeCompanyReceipt` — write->unguarded SUCCESS
4. `CompanyReceiptDetailDaoImpl.saveCompanyReceiptDetail` — write->unguarded SUCCESS
5. `BillingPaymentDetailsDaoImpl.saveBillingPaymentDetails` — write->unguarded SUCCESS
6. `RequestDiscountDaoImpl.requestDiscount` / requestDiscountBillList / requestDiscountDetails
7. `CashInOutDaoImpl.saveCashInOut`
8. `CashHandoverMasterDaoImpl.updateStatusToComplete`
9. `FranchiseShareDaoImpl.saveFranchiseShare` / updateStatusGroupWise
10. `FundInFlowMasterDaoImpl.saveFundDeatils`
11. `MPeriodicityServiceConfigDaoImpl.save/updateMPeriodicityServiceConfig`
12. `TrustMasterDaoImpl.saveTrust` / updateTrustMaster
13. `OTServiceDaoImpl.saveOTService`
14. `LIS SerologyResultDaoImpl.ResultEntryOrReleaseEntry` (clinical result — owner decides ordering)
15. `LIS FrozenSectionReqDaoImpl.acceptFrozensectionReq` (clinical)

Service-layer (need confirm-read before live-verify): CreditNoteMasterServiceImpl
(saveCreditNote / saveCreditNoteAgainstDeposit / saveCreditNoteAgainstService),
PaymentRefundDetailsServiceImpl (saveRefundAgainstCreditNote / saveRefundAgainstPharamacy),
RefundCNMasterServiceImpl.saveRefundCreditNote, PaymentDepositConsumptionServiceImpl.consumeDeposit.

## 6. Category 1 instances (5 in current live-verification batch)

1. `AllergyPatientMapperDaoImpl.saveCurrentAllergyPatientMapper` — SUCCESS x2, no ERROR (patient allergy; BR-147 domain)
2. `PatientDiagnosisDaoImpl.saveDiagnosisPatientList` (patient diagnosis)
3. `ObservationDaoImpl.savefPatientObservation` (patient observation)
4. `MedicationHistoryDaoImpl.saveCurrentMedicationHistory` (patient medication history)
5. `VitalDaoImpl.saveVitals` (patient vitals)

Each gets its own live verification — no assumption that the shared root pattern holds for any
of them (icuType lesson). Per-instance status will be recorded here as verification completes.

---

*Honest-caveat discipline: raw counts and the ~380 Category-3 estimate are informed estimates
pending real triage, exactly like the SME-gap analysis. Verified numbers are labeled as such.*

## 7. CLOSED — consultation-charge candidates: NO ACTION NEEDED (2026-08-03, owner-confirmed)

Both candidates for the "updateDoctorConsultationCharge" item were investigated and ruled out
as financial-correctness bugs — closed as no-action-needed:

1. `OPConsultationConfigurationMasterDaoImpl.updateOPConsultationConfigurationMaster`
   (Global, L135-154): updates followup/secondary visit counts + days for an OP consultation
   config row. No charge/financial value is written; the `charges` concept lives in a different
   table (`doctor.t_doctor_consultation_charges_master`) whose DAO is read-only (single getter).
   No silent-revert of financial values on this path.
2. `doctor.t_doctor_consultation_charges_master` write path: no update method exists at all —
   the DAO exposes only `getDoctorConsultationChargesByDoctorIdAndVisitTypeAndClass`. The
   consultation-charge *config* write is the OP-consultation-config path above. No live-verifiable
   financial-correctness issue.

Verdict: neither candidate has a real, live-verifiable financial-correctness bug. Logged as
closed per owner instruction — no fix, no BR number, no manifest entry.
