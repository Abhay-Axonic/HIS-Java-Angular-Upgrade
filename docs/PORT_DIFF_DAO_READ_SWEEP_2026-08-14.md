# PORT-DIFF DAO-READ SWEEP — 2026-08-14 (62-row consolidated table)

Method: 3 subagents traced every save/update endpoint of 62 rebuilt features
(21 + 21 + 20) from the feature's `.service.ts` → legacy controller → service/DAO
impl, quoting the `getDate(` lines that consume created/updated dates. Class
discriminator: does the save-DAO parse **payload** dates
(`GlobalCommonDateUtils.getDate(dto.getXxxDate(), ...)`) or **server-computed**
dates (`getDate(TimeZoneComponent.getDateByzone(...))`)? Only the former is the
port-diff class (NPE-able when the rebuild omits the field).

Sample-verified by the parent on 5 rows (insurance-master full chain, delivery-type
L107, patient-type-details L131, user-store-mapper L48/50, store-type NEITHER
control L56-57).

## The count

| bucket | rows | meaning |
|---|---|---|
| **48 of 62** | excluded for the server-computed-date reason (TimeZoneComponent.getDateByzone / setCreateDefaults / BaseEntity) | dates computed server-side — a rebuild omitting them cannot NPE |
| 2 of 62 | excluded, no date consumption at all (ai-button-configuration, ai-button-master) | save/update DAO methods never touch dates |
| 6 of 62 | NO-WRITE (read-only rebuilds / local stubs) | no save path to inspect |
| **6 of 62** | **IN-CLASS (DAO parses payload dates)** | the port-diff class shape |

## The 6 in-class rows

| feature | verdict | DAO evidence (quoted, file:line) | rebuild status |
|---|---|---|---|
| insurance-master | DEMANDS-BOTH | InsuranceMasterDaoImpl.java:42/43/55/57 `getDate(dto.getStartDate()/getEndDate()/getCreatedDate()/getUpdatedDate(), "dd-M-yyyy HH:mm:ss")` (+ :98 update) | **BROKEN — omits the audit+identity set (DQ-125)**; fixed this session |
| delivery-type-master | DEMANDS-UPDATED | DeliveryTypeMasterDaoImpl.java:106-107 `getDate(dto.getUpdatedDate(), ...)` (update path) | **live NPE risk** — update payload omits updatedDate |
| patient-type-details | DEMANDS-UPDATED | PatientTypeServiceWiseDetailsDaoImpl.java:131 `getDate(dto.getUpdatedDate(), ...)` (update-status path) | **live NPE risk** on the status path (save path safe — computed dates) |
| user-store-mapper | DEMANDS-BOTH | UserStoreMapperDaoImpl.java:48/50/86/87 `getDate(dto.getAddedDate()/getUpdatedDate(), ...)` | survives — rebuild sends addedDate/updatedDate formatted |
| discount-category-master | DEMANDS-BOTH | DiscountTypeMasterDaoImpl.java:48/50/75 `getDate(dto.getAddedDate()/getUpdatedDate(), ...)` | survives — rebuild sends both |
| details-corrective-maintenance | DEMANDS-UPDATED | CorrectiveMaintenanceDaoImpl.java:76/104/123 `getDate(dto.getUpdatedDate()/getCreatedDate(), ...)` | survives — rebuild sends updatedDate (create branch never hit) |

## The 50 excluded rows (48 server-computed + 2 no-date)

Agent A (21): asset-category-master, asset-return-acceptance-details, asset-type-master,
autoclave-master, box-masters, consumable-barcode-config, coupon-template-master,
department-store-mapper, details-asset-issue-acceptance, dish-master, fixed-asset-master,
general-insurance-master, generic-master, implant-master, key-ingredients-master
(server-computed: 15) + ai-button-configuration, ai-button-master (no dates: 2) +
blood-group-matching-master (NO-WRITE: 1).

Agent B (21): linen-issue-pending-details, machine-parameter-master, manufacturer-master,
meal-master, organization-master, pass-type-master, patient-type-master, periodicity-service,
pharmacy-formulation-type-master, pharmacy-strength-unit-master, pharmacy-tariff-master,
prefix-configuration, qms-counter-master, qms-department-master, qms-kiosk-master, rack-master,
rack-masters, reset-pwd-confirm-pwd, room-modality, route-administration (server-computed: 20).

Agent C (20): row-masters, sterilization-type-master, storage-unit-master, store-type-master,
surgical-calendar, tax-component-master, ticket-system-master, ward-store-mapper,
add-new-drug-recall, fixed-asset-type, formulation-type-roa, frequency-master,
pharmacy-formulation-type-master (server-computed: 13) + cancellation-master, mis-reports,
multicounter-package, pharman, vaccination-master (NO-WRITE: 5).

## Caveats

- patient-type-master's status endpoint (PatientTypeDaoImpl.java:103) is the same
  DEMANDS-UPDATED shape but was excluded from the verdicts per the method (status
  endpoints); the parent's sample did not re-check it.
- NO-WRITE rows were excluded from the class count but blood-group-matching-master's
  DAO would NPE if its save were ever wired (Agent A note).
