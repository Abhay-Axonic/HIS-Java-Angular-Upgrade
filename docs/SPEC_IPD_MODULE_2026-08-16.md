# SPEC — IPD MODULE (the genuinely-new screens, batched)

SERVED-BRANCH: `fix/patient-type-details-status` @ eded6b65. INVENTORY FIRST
(as ordered): 84 IPD candidate states from the legacy → 7 already covered by
existing routes (cashOpeningBalance, chargeConversion, discountRights,
interimHandover, loyaltyCardCreation, loyaltyCardConfiguration, shiftClosing)
→ **77 genuinely new** (this build). Already covered under other names (NOT
rebuilt): admission (confirmed working), patientAcceptance / patientForDischarge
/ readyForBilling / finalDischarge / shortLeaveDischarge / patientDischargedList
(Nursing), the ER's 6, the radiology/OT/lab ladders, the pharmacy's 6.

Core scope: admission forms carry save; worklists read-only; the billing
screens match the OPD billing pattern (BillPayload + Long-free String dates);
the discharge ladder screens are status-display. DATE CONVENTION: String
dates via formatLegacyNow (the registration/prescription convention, NOT the
encounter's Long). Contract: the corrected ADT family (/adt — the verified
getDischagePatientList / getPatientReadyForBillingList / getERBedList /
getDataForMapOfBed + the admission's saveUnknownPatient family), the Billing
family (/api/billing — saveBill + the deposit/refund/credit-note/debit-note
families), the OPD/encounter family (/api/opd — the IPD coversheet's vitals +
nursing orders).

## The 77 (one line each — the family + the core scope)

**ADT admission/bed/ward (the 4200-6200 region):**
1. admissionRequest — the admission request worklist (/adt); read-only.
2. pendingAdmissionDischarge — the pending admission/discharge list; read-only.
3. bedBook — the bed booking worklist (/adt); read-only.
4. bedReservationList — the ward bed reservation list; read-only.
5. bedAvailability — the reservation wait list; read-only.
6. bedAllocation — the bed allocation worklist; read-only.
7. admittedPatientList — the admitted-patients list (/adt); read-only.
8. passIssued — the issued-pass list; read-only.
9. intiateTransferCare — the transfer-of-care worklist (/adt); read-only.
10. bloodRequest — the blood-request worklist; read-only.
11. requestInterConsult — the inter-consult request worklist; read-only.
12. pendingInterConsult — the pending inter-consult list; read-only.
13. dueForDischarge — the IPD due-for-discharge list (/adt/getDischagePatientList); status-display.
14. dayCareDueForDischarge — the day-care due-for-discharge; status-display.
15. abscondingPatient — the absconding-patients list; read-only.
16. medicationAcceptance — the medication-acceptance worklist; read-only.
17. nursingOrderView — the nursing-order view; read-only.
18. pendingNursingOrder — the pending nursing orders (/api/opd pendingOrders family); read-only.
19. houseKeeping — the housekeeping worklist (/adt); read-only.
20. cleanedBedAccept — the cleaned-bed acceptance; read-only.
21. transferToICU — the ICU transfer worklist (/adt); read-only.
22. pendingTransferRequest — the pending transfer list; read-only.
23. acceptanceOfTransferOfCare — the transfer-of-care acceptance; read-only.
24. pendingConsultOrderRequest — the pending consult-order list; read-only.
25. deathRegistration — the death-registration worklist; read-only.
26. cancelForDischarge — the discharge-cancel worklist; read-only.
27. cancelDischarge — the cancel-discharge list; status-display.
28. costEstimation — the cost-estimation worklist; read-only.
29. dayCarePatientForDischarge — the day-care mark-for-discharge; status-display.
30. dischargedPatients — the hospital-discharged list (/adt); status-display.
31. updateDischargeSummary — the discharge-summary update worklist; read-only.
32. erInitiateBedTransfer — the ER bed-transfer worklist (/adt); read-only.
33. erTransferRequestAcceptance — the ER transfer acceptance; read-only.
34. dayCareWorklist — the day-care worklist; read-only.
35. dayCareFinalDischarge — the day-care final discharge; status-display.
36. bedUnderMaintenance — the under-maintenance beds; read-only.
37. pendingShortLeave — the pending short-leave list; read-only.
38. dischargeSummary — the discharge-summary worklist; read-only.
39. dayCareDischargeSummary — the day-care discharge summary; read-only.
40. shortLeaveStatus — the short-leave status list; read-only.
41. icuBedBookingStatus — the ICU bed-booking status; read-only.
42. newBornRegistration — the new-born registration form (save; the
    PatientRegistrationDto-style model).
43. admissionRejectionList — the admission-rejection list; read-only.

**Billing/deposit/refund (the 9000-10800 + 15500-16000 regions — the OPD
billing pattern):**
44. refund — the refund worklist (/api/billing); read-only.
45. deposit — the deposit screen (the DepositController family
    /api/billing/unit/deposit); the deposit form carries save.
46. franchiseDeposit — the franchise deposit; save.
47. findBillList — the bill-search list; read-only.
48. updatePayeeInformation — the payee update form; save.
49. autoChargeReview — the auto-charge review worklist; read-only.
50. writeOff — the write-off worklist; read-only.
51. writeOffDetails — the write-off details; read-only.
52. reminderForNOK — the NOK reminder worklist; read-only.
53. companyInvoiceCreation — the company-invoice creation; read-only.
54. paymentCollection — the payment-collection worklist (/api/billing); read-only.
55. billList — the bill list (/api/billing saveBill family); read-only.
56. cancelledBillList — the cancelled-bill list; read-only.
57. debitNote — the debit-note form; save.
58. debitNoteList — the debit-note list; read-only.
59. creditNote — the credit-note form; save.
60. creditNoteList — the credit-note list; read-only.
61. preAuthorizationRequest — the pre-auth request worklist
    (/api/insurance family); read-only.
62. addNewPreAuthorizationRequest — the pre-auth request form; save.
63. refundAgainstDeposit — the refund-against-deposit worklist; read-only.
64. refundList — the refund list; read-only.
65. refundAgainstCR — the refund-against-CR worklist; read-only.
66. splitBill — the split-bill worklist; read-only.
67. discountRequestWorklist — the discount-request worklist; read-only.
68. financialRequestApproval — the financial request approval; read-only.
69. assignloyaltyCard — the loyalty-card assignment form; save.

**IPD clinical (the 17000-18000 region):**
70. IPDCoverSheet — the IPD coversheet (/api/opd — the encounter +
    vitals family); the vitals form carries save.
71. vitals — the vitals entry form (the AppointmentVitalMapperDto family —
    the encounter's String-date vitals); save.
72. addNewVitals — the new-vitals form; save.
73. nursingProgressNote — the nursing progress-note form; save.
74. doctorDesk — the doctor-desk worklist; read-only.
75. nursingWorklist — the nursing worklist; read-only.
76. encounterData — the encounter-data view; read-only.

**The remaining:**
77. entitlementCoversion — the entitlement-conversion worklist; read-only.

All 77: tsc clean, routes registered (the legacy state names), ONE commit,
verified with git show.
