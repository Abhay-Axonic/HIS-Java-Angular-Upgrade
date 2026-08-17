# SPEC — BLOOD BANK MODULE (the last clinical module, batched)

SERVED-BRANCH: `fix/patient-type-details-status` @ 0dec41c1. INVENTORY FIRST:
35 blood-bank clinical/workflow states from the legacy → 3 already covered by
existing routes (donorRegistration — confirmed rendering, crossMatchRegister,
transfusionTransmittedDiseases) → **32 genuinely new** (this build). The 15
blood-bank masters: 11 already rebuilt (bloodGroupMaster, bloodBagType,
bloodDonortype, bloodBagCapacity, screeningTest, donorRejectionMaster,
bloodBankMaster, bloodBankTypeComponent, bloodGroupMatchingMaster, bloodTest,
reasonOfDiscard), 4 deferred (bloodComponentMaster, bloodTestResult,
donorScreeningMaster, transfusionPrecheckMaster — the owner's ~25 count covers
the clinical).

Core scope: registration and issue forms carry save; worklists read-only;
stock management read-only in the first build. DATE CONVENTION: String dates
via formatLegacyNow (the registration/prescription convention, NOT the
encounter's Long). Contract: the corrected BloodBank module — the
DonorRegistrationController (class api/bloodbank: donorRegistration POST,
donorSearch, getDonorDetails), the CrossMatchRegisterController (class
/api/bloodbank/crossMatch), the BloodBagCollectionController, and the
/api/bloodbank/ family.

## The 32 (one line each)

1. donorScreeningWorklist — the donor-screening worklist (/api/bloodbank
   donorScreeningWorklist); read-only.
2. donorScreening — the donor-screening form; save.
3. bloodBagCollectionWorklist — the collection worklist; read-only.
4. bloodBagCollection — the collection form (the BloodBagCollectionController
   family); save.
5. bloodBankComponentSeparationWorklist — the component-separation worklist;
   read-only.
6. detailsBloodBankComponentSeparationWorklist — the separation details;
   read-only.
7. detailsTransfusionTransmittedDiseases — the TTD details; read-only.
8. detailsCrossMatchRegister — the cross-match details; read-only.
9. bloodBagReservation — the bag-reservation worklist; read-only.
10. issueBloodBag — the blood-issue form (the issue family); save.
11. detailsIssueBloodBag — the issue details; read-only.
12. bloodGroupConfirmation — the group-confirmation worklist; read-only.
13. detailsBloodGroupConfirmation — the confirmation details; read-only.
14. bloodIssueAcceptanceWorklist — the issue-acceptance worklist; read-only.
15. patientRequisitionWorklist — the patient-requisition worklist
    (/api/bloodbank requisition); read-only.
16. detailsPatientRequisition — the requisition details; read-only.
17. bloodBagStockUpdation — the stock-update worklist; read-only (stock
    management read-only in the first build).
18. loanInBloodBank — the loan-in worklist; read-only.
19. addNewLoanInBloodBank — the loan-in form; save.
20. detailsLoanInBloodBank — the loan-in details; read-only.
21. loanInBloodBankApproval — the loan-in approval; read-only.
22. loanOutBloodBank — the loan-out worklist; read-only.
23. addNewLoanOutBloodBank — the loan-out form; save.
24. detailsLoanOutBloodBank — the loan-out details; read-only.
25. loanOutBloodBankApproval — the loan-out approval; read-only.
26. redCellGroupingWorklist — the red-cell grouping worklist; read-only.
27. detailsRedCellGroupingWorklist — the grouping details; read-only.
28. redCellSerumWorklist — the red-cell serum worklist; read-only.
29. detailsRedCellSerumWorklist — the serum details; read-only.
30. patientConsumption — the patient-consumption worklist; read-only.
31. patientConsumptionDetails — the consumption details; read-only.
32. pendingTransfusionList — the pending-transfusion list; read-only.

All 32: tsc clean, routes registered (the legacy state names), ONE commit,
verified with git show.
