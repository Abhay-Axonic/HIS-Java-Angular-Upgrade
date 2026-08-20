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
| bill-list | visitTypeName | MASTER_LOOKUP | the row carries visitTypeId only (BillingMasterDaoImpl billlist query); a component master-lookup from visitTypeId |
| bill-list | paymentEntitlementDesc | MASTER_LOOKUP | the row carries paymentEntitlementId only; same lookup pattern |
| cancelled-bill-list | visitTypeName | MASTER_LOOKUP | same as bill-list |
| credit-note | visitTypeName | MASTER_LOOKUP | same |
| debit-note-list | visitTypeName | MASTER_LOOKUP | same |
| company-invoice-creation | visitTypeName | MASTER_LOOKUP | same |
| financial-request-approval | visitTypeName | MASTER_LOOKUP | same |
| financial-request-approval | encounterNumber | NOT_SUPPLIED | no encounter field on FundSanctionRequestDto (the fund-sanction family carries patientId/uhidNumber only) |
| financial-request-approval | requestDiscAmt | NOT_SUPPLIED | the fund-sanction amount props are fundrequestAmount/fundrequestStatusId; a discount-specific amount is not in the DTO |
| pre-authorization-request | visitTypeCode | MASTER_LOOKUP | not in the pre-auth row (IPreAuthorizationRequestDaoImpl carries authDate/companyDesc/contractName/insuranceDesc/insuranceNumber) |
| pre-authorization-request | visitDate | MASTER_LOOKUP | the row carries authDate (IPreAuthorizationRequestDaoImpl) |

## Refund family

| screen | column | reason class | what would resolve it |
|---|---|---|---|
| refund-against-cr | visitNo | ALREADY_BOUND-CONVENTION | bound to info.visitNo but RefundCreditNoteResDto does not carry it (the CR search row lacks visitNo) — confirm against the live feed or find the second call |
| refund-list | refundAgainst | BOUND (client-computed) | bound this pass — the exact legacy ternary (depositTypeId == 1 ? 'Regular' : == 2 ? 'Encounter' : '-') |

## Franchise / loyalty

| screen | column | reason class | what would resolve it |
|---|---|---|---|
| franchise-deposit | paymentMode, consumedAmount, refundAmt, totalBalanceAmount, collectedBy, panCardDetails | FEED_UNLOCATED | the searchDeposit feed's DAO was not found by the frDepositNo/franchise greps — one chain read (controller → service → DAO) |
| assign-loyalty-card | print, transactionType | FEED_UNLOCATED | same family |

## New-born

| screen | column | reason class | what would resolve it |
|---|---|---|---|
| new-born-registration | motherName | STILL_UNKNOWN | NewBornBabyDto carries fatherName; the mother is the admitted-patient join, likely in the babyDetails sub-object — one read |

## Consult / short-leave

| screen | column | reason class | what would resolve it |
|---|---|---|---|
| pending-consult-order-request | note | STILL_UNKNOWN | the consult-request row query not located by name (ADT grep empty) |
| pending-short-leave | stringFromTime, stringToTime | NOT_SUPPLIED | AdmittedPatientListDaoImpl carries no such aliases — the legacy computes the time strings client-side from the date+time fields |

## Bound this pass (for the record — resolved-but-not-applied was the failure mode)

visitNo, doctorName, amountPaid, selfPayable (balance), againstbillNumber, associateCompanyDesc (company),
totalBillAmount (totalAmount) — BillingMasterDaoImpl billlist query; totalAmt (finalAmount) —
OrderDetailsByEncounterIdDto; refundAmount, remark, visitTypeName (refund family), invoiceNo, crAmount,
crDueAmount, selfPayable (refund-against-CR) — RefundCnResDto / RefundCreditNoteResDto; babyPrefix,
weight, dob (timeOfBirth), gestationalAge, birthTerm (birthType) — NewBornBabyDto; visitDetails
(client-computed visitDate+visitNo concat); refundAgainst (the exact legacy ternary).
