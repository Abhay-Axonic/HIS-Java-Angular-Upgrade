# QUOTABLE_SCREEN_DASH_INVENTORY — every remaining dashed cell across the 19 QUOTABLE screens

One row per flagged cell that still renders as a dash after the second apply-pass (commit to be
named in SESSION_LOG). Reason classes: MASTER_LOOKUP (the desc needs a component-side master
lookup — service-layer, not a template fix), FEED_UNLOCATED (the supplying endpoint/DAO was not
found by name), STILL_UNKNOWN (one more read needed), NOT_SUPPLIED (no backend field exists —
client-side computed or absent), ALREADY_BOUND-CONVENTION (bound to a conventional name whose
backend quote is pending). The resolutions below are the union the first apply-pass lacked.

## Billing family (bill-list, cancelled-bill-list, credit-note, debit-note-list, company-invoice-creation, financial-request-approval)

| screen | column | reason class | what would resolve it |
|---|---|---|---|
| bill-list | visitTypeName | RESOLVED — CLIENT_CONSTANT (2026-08-20) | bound via shared/legacy-lookups.ts legacyGetVisitTypeById (the legacy rootScope helper, app.js:~1688: 1=OP 2=IP 3=Day Care 4=ER); the row carries visitTypeId (BillingMasterDaoImpl) |
| bill-list | status | RESOLVED — CLIENT_CONSTANT (2026-08-20) | the column ADDED (the legacy renders it — bill-list.html:296 {{getBillStatusById(bill.billStatusId)}}); bound via legacyGetBillStatusById (app.js:1702: 1=Pending 2=OnHold 3=Settled) |
| bill-list | paymentEntitlementDesc | MASTER_LOOKUP (genuine) | the row carries paymentEntitlementId only; the desc needs the real master fetch /adt/getAvtivePaymentEntitlementList — NOT in the client-constant set, stays dashed |
| cancelled-bill-list | visitTypeName | RESOLVED — CLIENT_CONSTANT (2026-08-20) | legacyGetVisitTypeById(info.visitTypeId) |
| credit-note | visitTypeName | RESOLVED — CLIENT_CONSTANT (2026-08-20) | legacyGetVisitTypeById(info.visitTypeId) |
| debit-note-list | visitTypeName | RESOLVED — CLIENT_CONSTANT (2026-08-20) | legacyGetVisitTypeById(info.visitTypeId) |
| debit-note-list | status | RESOLVED — CLIENT_CONSTANT (2026-08-20) | the column ADDED (the legacy debit-list.html renders getBillStatusById); bound via legacyGetBillStatusById(info.billStatusId) |
| company-invoice-creation | visitTypeName | RESOLVED — CLIENT_CONSTANT (2026-08-20) | legacyGetVisitTypeById(info.visitTypeId) |
| financial-request-approval | visitTypeName | STILL DASHED — no visitTypeId on the row | FundSanctionRequestDto carries no visit-type id; the column cannot use the lookup without an id in the row |
| financial-request-approval | encounterNumber | NOT_SUPPLIED | no encounter field on FundSanctionRequestDto (the fund-sanction family carries patientId/uhidNumber only) |
| financial-request-approval | requestDiscAmt | NOT_SUPPLIED | the fund-sanction amount props are fundrequestAmount/fundrequestStatusId; a discount-specific amount is not in the DTO |
| pre-authorization-request | (realigned 2026-08-20) | RESOLVED — the positional shift fixed | the legacy's own row binds info.visitTypeCode for the Visit Type column (pre-authorization-request.html, quoted) — the query carries the CODE, so the column is real: bound visitTypeCode, and every cell realigned to its header (Visit Date|No. = visitDate + admissionNo; Insurance No. = the legacy's companyTypeId==2 ternary). The earlier "no visitTypeId" note was about the ID; the code is a different, present field |
| pre-authorization-request | visitDate | MASTER_LOOKUP | the row carries authDate (IPreAuthorizationRequestDaoImpl) |

## Refund family

| screen | column | reason class | what would resolve it |
|---|---|---|---|
| refund-against-cr | visitNo | ALREADY_BOUND-CONVENTION | bound to info.visitNo but RefundCreditNoteResDto does not carry it (the CR search row lacks visitNo) — confirm against the live feed or find the second call |
| refund-list | refundAgainst | BOUND (client-computed) | bound this pass — the exact legacy ternary (depositTypeId == 1 ? 'Regular' : == 2 ? 'Encounter' : '-') |

## Franchise / loyalty

| screen | column | reason class | what would resolve it |
|---|---|---|---|
| franchise-deposit | RESOLVED 2026-08-20 (4 bound) + 4 header-artifacts | the handler: Global/.../billing/controller/FranchiseDepositController.java — getFrenchiseDeposit (method /all; the endpoint splits class+method segments, which is why the literal grep failed). Row shape = the Master DTO names the legacy view actually binds (franchise-deposit.html): consumedDeposit, refundAmount, availableDeposit, createdBy — NOT the Details DTO's consumedAmount/balanceAmount (the task's caution was right). BOUND: consumedAmount→info.consumedDeposit, refundAmt→info.refundAmount, totalBalanceAmount→info.availableDeposit, collectedBy→info.createdBy. paymentMode + panCardDetails + print + transactionType have NO grid binding in the legacy view (header-only artifacts) — flagged in the template, not dashes |
| assign-loyalty-card | print, transactionType | FEED_UNLOCATED | same family |

## New-born

| screen | column | reason class | what would resolve it |
|---|---|---|---|
| new-born-registration | motherName | NOT_SUPPLIED as a grid column — RECLASSIFIED (2026-08-20) | the legacy view does NOT render the mother's name as a grid cell: the radio row calls setMothersName(info.patientId) (new-born-registration.html, quoted) and the field is populated CLIENT-SIDE from the selected admitted-patient row. The NewBornBabyDto carries fatherName only (babyDetails is a recursive List<NewBornBabyDto>). Not a grid column -> the template's motherName cell should be flagged accordingly, not awaited as a feed field |

## Consult / short-leave

| screen | column | reason class | what would resolve it |
|---|---|---|---|
| pending-consult-order-request | note | STILL_UNKNOWN | the consult-request row query not located by name (ADT grep empty) — next hop: the IConsultOrderRequestDao family in ADT |
| pending-short-leave | stringFromTime, stringToTime | NOT_SUPPLIED | AdmittedPatientListDaoImpl carries no such aliases — the legacy computes the time strings client-side from the date+time fields |

## Bound this pass (for the record — resolved-but-not-applied was the failure mode)

visitNo, doctorName, amountPaid, selfPayable (balance), againstbillNumber, associateCompanyDesc (company),
totalBillAmount (totalAmount) — BillingMasterDaoImpl billlist query; totalAmt (finalAmount) —
OrderDetailsByEncounterIdDto; refundAmount, remark, visitTypeName (refund family), invoiceNo, crAmount,
crDueAmount, selfPayable (refund-against-CR) — RefundCnResDto / RefundCreditNoteResDto; babyPrefix,
weight, dob (timeOfBirth), gestationalAge, birthTerm (birthType) — NewBornBabyDto; visitDetails
(client-computed visitDate+visitNo concat); refundAgainst (the exact legacy ternary).

## CONVENTION-screen resolutions (2026-08-20, the six one-read-away screens)

- pending-transfer-request + acceptance-of-transfer-of-care — RESOLVED: GET /adt/getTransferRequestList -> MapOfBedDaoImpl.getTransferRequestList, transformer target AdmissionDto.class (quoted). The transfer row shape: UHIDNumber, admittingDoctorName, admittingSpecialityName, adtReasonDesc/adtReasonId, doctorName, doctorReasonDesc, doctorRejectReason, bedCategoryDesc, bedCode, bedId, bedStatusId, admissionStatus, adtNote... The screens are buildable from these fields.
- pass-issued — the handler is PassIssuedController (ADT module; the class mapping is /getVisitorTypeList): the grid feeds are /getAdmittedPatientListByMultipleCriteria + /getVisitorPassList (the franchise lesson applied — the endpoint's constant name did not match the handler's). Row shape: the AdmittedPatientListDaoImpl / BedManagementDto family quoted earlier.
- deposit — NO_GRID / controller-less shell: no depositController.js exists in the webapp (only depositMatrixConfigController.js) and deposit.html has NO ng-controller. The index's controller name is a heuristic guess; the screen is unwired like the 12 MasterController shells.
- discount-request-worklist, doctor-desk, update-payee-information — NO_GRID confirmed: their controllers assign dropdown lists only (reasonList, requestDiscUsersList, allEncounterList / dischargeTypeList, reasonList, encounterStatusList / sexMasterList, identificationTypeList, documentTypeList — quoted earlier), no grid feed. They are form/dropdown screens, not list screens.

## The master-lookup feeds (item 3 — which feed supplies each desc; NOT wired)

- visitTypeName / visitTypeCode -> GETVISITTYPEBYID = API+GLOBAL+"/getVisitTypeById" (config quoted); the active list feed GETACTIVEVISITTYPEMASTER = API+GLOBAL+"/visitTypeMaster". Row: the VisitTypeMaster (visitTypeId/visitTypeCode/visitTypeDesc).
- paymentEntitlementDesc -> GETACTIVEPAYMENTENTITLEMENT = "/adt/getAvtivePaymentEntitlementList" (the legacy's own "Avtive" typo, quoted) + GETPAYMENTENTITLEMENTBYID = GLOBAL+"/getPaymententitlementById".
- status (the billing family's bill-status desc) -> no GETBILLSTATUS constant matched the config grep — the next hop is the billing config's status-list constant (one grep).
- pre-auth's visitDate — RECLASSIFIED: not a master lookup — the pre-auth row carries authDate (IPreAuthorizationRequestDaoImpl, quoted); the column maps to the row field.
