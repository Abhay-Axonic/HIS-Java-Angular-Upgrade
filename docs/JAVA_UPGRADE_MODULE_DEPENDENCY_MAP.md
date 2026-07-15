# Java Upgrade Module Dependency Map — Billing/Global Exposure

*Read-only investigation, 2026-07-15. Companion to `docs/AUDIT_JavaUpgradeBlockers.md` (which catalogues JDK-17 compile blockers) — this document answers a different question: **which of the ~18 Maven modules under `legacy/` can start Java 21 upgrade work now (Track A), and which touch money/billing code and must wait for the Billing/Global correctness work?** No files were modified, no build tool was run. Per `CLAUDE.md`, billing accuracy and database work must be proven correct before any version-upgrade work touches billing-critical code — this is preparatory mapping, not a green light.*

**Scope:** 18 Maven modules under `legacy/` (root aggregator `HIS-POM` not counted separately): Abdm, ADT, AngularSpring, AssetManagement, Billing, BloodBank, CSSDMaster, DietAndKitchen, Entity, Global, HIS-WAR, Inventory, LinenLaundry, LIS, OTIMS, Pharmacy, Procurement, RabbitMQ. Verified against the actual directory tree (`find legacy -maxdepth 2 -name pom.xml`) and the root `legacy/pom.xml`'s `<modules>` block — both list exactly these 18, no undeclared/orphan modules found, no stray `pom.xml` anywhere under `Database/`, `Deployment/`, `devops/`, `his-maven-jars/`, `Testing/`, or the dot-directories.

## Critical methodology finding, read before the table

A naive `grep import com.param.billing.` / `import com.param.global.` count is **not** a reliable cross-module-dependency signal in this codebase, because the package names `com.param.billing.*` and `com.param.global.*` are not exclusive to the `Billing` and `Global` Maven modules:

- The **`Entity`** module itself physically hosts classes under `com.param.billing.global.model.*` (e.g. `BankMaster`, `CashInOut`, `CompanyReceiptMaster`, `DepositMaster`, `PaymentDepositConsumption`, `PaymentRefundMaster`, `CostEstimate`, `CreditNoteReasonMaster`, `CurrencyMaster` — real financial JPA entities) and `com.param.global.*` (generic model classes). Any other module importing one of these classes is pulling from **`Entity`** (which every module already formally depends on), not from `Billing` or `Global`.
- The **`DietAndKitchen`** module hosts its own DAOs/DTOs/models under `com.param.global.master.*` (diet templates, dish masters, meal masters) — 54 of its 61 `com.param.global.*`-matching import lines are **self-references to its own code**, not a dependency on the `Global` module at all.
- `Procurement` and `Pharmacy` similarly each define one class (`PaymentReceiptDetailsDto1`, `MultiItemsRequest`) inside their own tree under a `com.param.global.*`-prefixed package.

**Method used to correct for this:** every one of the 566 distinct `com.param.billing.*` / `com.param.global.*` import statements found across the 14 non-Billing/non-Global modules (plus Entity's own self-imports) was resolved against a full index of every `.java` file's actual package+class name across all 18 module source trees, to determine which module **physically defines** the imported class, not just which package prefix it uses. Results: **372 resolve to a class physically in `Global`, 125 to `Entity`, 54 to `DietAndKitchen` (self), 12 to `Billing`, and 2 to the importing module's own tree (self)** — zero unresolved. This is the basis for every "resolves to" claim below.

Because of this, the summary table below reports both the raw package-prefix grep count (what a naive check would show) and the resolved-module-source count (what's actually true).

---

## Modules confirmed safe for Java 21 Track A

**AngularSpring, BloodBank, CSSDMaster, DietAndKitchen, LinenLaundry, RabbitMQ** (6 modules) — no Billing/Global dependency of any kind, or only confirmed non-financial shared utility types (generic `Response<T>` wrapper, `ICommonConstants` status codes, `ServiceException`, date/JSON utilities, generic master-data DTOs with no money fields).

## Modules blocked pending Billing/Global correctness work

**Billing, Global, HIS-WAR, ADT, Abdm, Pharmacy, Procurement, OTIMS, Inventory** (9 modules) — formal and/or source-level dependency on real financial logic (billing services, deposit/payment DAOs, tax-amount calculation, doctor-share/contract-consumption, vendor rate configs).

## Modules needing owner review (ambiguous/mixed — see detail section)

**Entity, AssetManagement, LIS** (3 modules) — no formal Billing/Global *module* dependency, but each has a real nuance that a blanket "safe" verdict would hide.

18 modules total: 6 safe, 9 blocked, 3 needing review.

---

## Summary table

| Module | Formal pom.xml dep on Billing/Global | Source-level `com.param.billing.*`/`com.param.global.*` imports (raw unique count) | Resolved-source breakdown (which module the imported classes actually live in) | Real financial-logic imports found? | Verdict |
|---|---|---|---|---|---|
| Abdm | Global (Y, no version pin) | billing: 1, global: 41 | Entity: 8, Global: 34 | **Yes** — `BillListResponse`/`ItemDetailsResponse` (discount/concession-amount fields) used to construct bill-item payloads | **Blocked** |
| ADT | Global (Y, versioned) | billing: 3, global: 123 | Global: 110, Entity: 16 | **Yes** — `IBillingService`, `IDepositMasterService`, `IContractConsumptionService`, `RequestDiscountDto` autowired and used directly | **Blocked** |
| AngularSpring | None (no `<dependency>` block references either) | N/A — no `src/main/java` at all (webapp-only WAR shell: `index.jsp`, `web.xml`) | N/A | No | **Confirmed safe** |
| AssetManagement | Global (Y, versioned) | billing: 0, global: 25 | Global: 22, Entity: 3 | Mixed — imports `IWalkInSalesDao` (a Global-hosted DAO interface that mixes real billing methods like `saveBill`/`updateDepositDetails` with pure inventory methods), but the only method actually called is `saveStockTransaction()` (non-financial) | **Needs owner review** |
| Billing | — (is the module) | — | — | — | N/A — this module **is** the billing-correctness work itself |
| BloodBank | Global (Y, no version pin) | billing: 1, global: 21 | Entity: 7, Global: 15 | No — the one `billing`-prefixed hit is `PatientTypeMaster` (Entity, non-financial); `IBillingCommonConstants` is imported but is a constants-only interface (no logic) | **Confirmed safe** |
| CSSDMaster | Global (Y, versioned) | billing: 0, global: 10 | Entity: 5, Global: 5 | No — equipment/priority/slot master data only | **Confirmed safe** |
| DietAndKitchen | Global (Y, versioned) | billing: 0, global: 61 | DietAndKitchen (self): 54, Entity: 5, Global: 2 | No — 54 of 61 hits are the module's own diet/meal/dish classes, mislabeled by package prefix; the 7 true cross-module imports are generic (`CabinMaster`, `CountryMaster`, `ServiceException`, `CabinMasterDto`) | **Confirmed safe** |
| Entity | None (no `<dependency>` on Billing or Global) | billing: 28, global: 83 | Entity (self): 111 of 111 | **No true cross-module dependency** — all 111 hits resolve to Entity's own tree | **Needs owner review** (see detail — Entity itself hosts the financial data model) |
| Global | — (is the module) | — | — | — | N/A — this module **is** the billing-correctness work itself |
| HIS-WAR | Billing (Y, versioned) + Global (Y, versioned) | billing: 25, global: 42 | Billing: 12, Global: 47, Entity: 8 | **Yes, most concentrated of any module** — direct DAO/service autowiring of doctor-share, billing-master/-details DAOs | **Blocked** |
| Inventory | **None** (no formal dep on Billing or Global despite real usage — mismatch) | billing: 0, global: 40 | Global: 37, Entity: 3, Inventory (self, ambiguous dup class): 1 | **Yes** — `ITaxComponentDetailsDao`/`TaxComponentDetailsDto` used to actively **calculate and persist tax amounts** on goods-receipt returns; also imports `IWalkInSalesDao` | **Blocked** |
| LinenLaundry | Global (Y, versioned) | billing: 0, global: 9 | Global: 9 | No — generic batch/item/store DTOs and `IGlobalDao`/`Util` only | **Confirmed safe** |
| LIS | Global (Y, versioned; note pom also has the LIS/Pharmacy Spring-Security dual-version issue documented in `AUDIT_JavaUpgradeBlockers.md`) | billing: 0, global: 52 | Global: 48, Entity: 4 | Ambiguous — imports `BillingMasterDto` (real bill-amount fields: `netAmount`, `discountAmount`, `paidAmount`) but the import is **dead/unused** in the one file that has it; also `implements IBillingCommonConstants` (constants only, no logic) in two service classes | **Needs owner review** |
| OTIMS | Global (Y, versioned) | billing: 0, global: 95 | Global: 92, Entity: 3 | **Yes** — `IContractConsumptionService` autowired and `AdmissionListIpdChargesResDto` (IPD charges) actively cast/used in `ProcedureTemplateServiceImpl` | **Blocked** |
| Pharmacy | Global (Y, versioned) | billing: 6, global: 133 | Global: 124, Entity: 14, Pharmacy (self): 1 | **Yes, heaviest exposure of any non-HIS-WAR module** — `IBillingDao`, `IDepositMasterDao`, `IPaymentDepositDetailsDao`, `IPOSMachineResponseMapperDao` all autowired and used; imports Entity's `DepositMaster`/`PaymentDepositConsumption`/`PaymentRefundMaster` financial entities directly | **Blocked** |
| Procurement | Global (Y, versioned) | billing: 0, global: 72 | Global: 66, Procurement (self): 1, Entity: 5 | **Yes** — `IVendorServiceRateConfigDao` autowired, `PaymentReceiptDetailsDto` imported, plus the same `ITaxComponentDetailsDao` tax-amount-calculation dependency as Inventory | **Blocked** |
| RabbitMQ | Global (Y, versioned) | billing: 0, global: 1 | Global: 1 | No — single import is `ServiceException` | **Confirmed safe** |

---

## Per-module detail — everything NOT in the "confirmed safe" bucket

### HIS-WAR — Blocked (formal + actual, most concentrated real dependency)
- **Formal:** `HIS-WAR/pom.xml` declares both `<artifactId>Billing</artifactId>` and `<artifactId>Global</artifactId>` as versioned dependencies (`0.0.1-SNAPSHOT`). HIS-WAR is the **only** module in the reactor with a real (non-commented) formal dependency on `Billing`.
- **Real classes imported, physically defined in `Billing`:** `com.param.billing.common.Response`, `com.param.billing.global.dto.EncounterPaymentDto`, `com.param.billing.global.transaction.dao.{IBillingDetailsDao, IBillingMasterDao, IDoctorReferralShareDao, IDoctorShareDao}`, `com.param.billing.global.transaction.dto.{MDoctorReferralShareDto, TDoctorReferralShareDto, TDoctorShareRespDto}`, `com.param.billing.global.transaction.service.ITDoctorShareService`, `com.param.billing.unit.dao.IMDoctorReferralShareDao`, `com.param.billing.unit.dto.MDoctorReferralShareReqDto`.
- **Usage:** all 12 of these are consumed exclusively by `HIS-WAR/src/main/java/com/param/batchjobs/service/TDoctorShareImpl.java` and related batch-job/scheduler classes (`MidNightBatchWithTimeZone.java`, `RoomRentChargesBatch.java`) — doctor referral/revenue-share computation is a batch financial process, not a peripheral touch.
- Also imports 47 `Global`-sourced classes including `com.param.global.billing.controller.BillingException`, `IAutoPostingConfigDao`, `AutoPostingServicesConfigDto`, `PaymentRecieptMasterDto`.

### ADT — Blocked
- **Formal:** `Global` dependency declared, versioned.
- **Real financial imports, confirmed in-use (not just declared):** `com.param.global.billing.service.IBillingService` and `IDepositMasterService` are autowired fields in both `ADT/src/main/java/com/param/adt/admission/service/BedAllocationServiceImpl.java:73,99` and `.../controller/BedAllocationController.java`; `com.param.global.billing.dto.RequestDiscountDto` is imported in `DischargeFieldUnitWiseDaoImpl.java`. Also `IAutoPostingConfigDao`, `IContractConsumptionDao/Service`, `PaymentDepositDetailsDto`, `BillPayeeDetailsResDto`.
- Bed allocation at admission time directly calls into billing/deposit services — this is not incidental exposure.

### Abdm — Blocked
- **Formal:** `Global` dependency declared (no version, inherits from `dependencyManagement`).
- **Real financial imports:** `com.param.global.billing.controller.BillingException`, `com.param.global.billing.dto.BillListResponse` and `ItemDetailsResponse`. `BillListResponse` carries real money fields (`totalDiscountAmount`, `concession`); `AbdmCredentialDaoImpl.java:7995-8683` actively constructs `List<ItemDetailsResponse>` objects and reads them off `encounterMasterDto.getItems()` to build ABDM (insurance/interoperability) payloads — this is genuine billing-item data being reshaped for an external integration, not a pass-through wrapper.

### Pharmacy — Blocked (heaviest non-HIS-WAR exposure)
- **Formal:** `Global` dependency declared, versioned.
- **Real financial imports, confirmed in-use:** `com.param.global.billing.dao.{IBillingDao, IContractConsumptionDao, IDepositMasterDao, IPaymentDepositDetailsDao, IPOSMachineResponseMapperDao}` — `IDepositMasterDao` is an autowired field in `Pharmacy/src/main/java/com/param/pharmacy/op/service/billList/BillListService.java:90`. Also `com.param.global.billing.dto.{BillBadDebitListResponse, BillListResponse, DepositMasterDto, PaymentDepositDetailsDto/List/Dto, PharmacyBillCommonDto, POSMachineResponseMapperDto, SalePayeeDetailsDto}` and services `IBillingService`, `IDepositMasterService`.
- **Also imports Entity's financial entities directly:** `com.param.billing.global.model.{BankMaster, CardTypeMaster, DepositMaster, PaymentDepositConsumption, PaymentDepositConsumptionDetails}` and `com.param.billing.global.transaction.model.PaymentRefundMaster` (all physically defined in `Entity`, but they are the JPA entity classes for pharmacy point-of-sale billing — deposits, refunds, card payments).
- This is the most billing-saturated module outside Billing/Global/HIS-WAR: pharmacy walk-in sales bill against deposits, POS machine payment mapping, and bad-debit tracking are core financial flows.

### Procurement — Blocked
- **Formal:** `Global` dependency declared, versioned.
- **Real financial imports, confirmed in-use:** `com.param.global.billing.dao.IVendorServiceRateConfigDao` is an autowired field in `Procurement/src/main/java/com/param/procurement/workOrder/service/workOrderCreation/ServiceWorkOrderCreationServiceImpl.java:34` (vendor pricing/rate configuration). `com.param.global.billing.dto.PaymentReceiptDetailsDto` is also imported.
- **Also has the same tax-amount-calculation dependency as Inventory** (see below) via `com.param.global.org.dao.ITaxComponentDetailsDao` / `com.param.global.dto.TaxComponentDetailsDto` (fields include `taxPercentage`, `taxAmount`, `taxComponentAmount`) — used for GRN (Goods Receipt Note) tax handling, which is procurement-side financial computation.

### OTIMS — Blocked
- **Formal:** `Global` dependency declared, versioned.
- **Real financial imports, confirmed in-use:** `com.param.global.billing.service.IContractConsumptionService` is an autowired field (`OTIMS/src/main/java/com/param/otims/service/ProcedureTemplateServiceImpl.java:138`); `com.param.global.billing.dto.AdmissionListIpdChargesResDto` (IPD charges response) is actively cast and read at line 449 of the same file. Also `com.param.global.common.IBillingCommonConstants` and `BillingException`.
- The bulk of OTIMS's 95 `Global`-sourced imports are its own OT-scheduling domain classes that happen to be physically hosted in the `Global` module's `com.param.global.otims.*` package (an architectural oddity worth flagging separately — OTIMS's actual DAOs/services live in `Global`, not in `OTIMS` itself — but that is a structural note, not a financial-exposure one); the billing-specific subset above is what drives the "Blocked" verdict.

### Inventory — Blocked (and a formal/source-level mismatch)
- **Formal:** **No** `<dependency>` on `Billing` or `Global` in `Inventory/pom.xml` — verified by direct grep, no match at all. This is a genuine mismatch: Inventory compiles today only because `Global` is transitively available via the shared reactor / other dependencies, not because it's declared.
- **Real financial imports, confirmed in-use with an actual money calculation:** `com.param.global.org.dao.ITaxComponentDetailsDao` and `com.param.global.dto.TaxComponentDetailsDto` are used in `Inventory/src/main/java/com/param/inventory/store/service/returnableGatepass/ReturnableGatepassService.java:1011-1031`, which **computes** `taxComponentAmount = (natAmount / 100) * taxDetail.getTaxComponentPercentage()` and persists it via `iTaxComponentDetailsDao` — this is inline tax-amount arithmetic on goods-return transactions, i.e. real financial-calculation code, not a passive DTO carry-through.
- Also imports `com.param.global.pharmacy.dao.IWalkInSalesDao` (autowired in `WitnessApprovalService.java:60`), a Global-hosted DAO interface whose full surface includes `saveBill`, `updateDepositDetails`, and payment-mode-consumption methods — though the only method Inventory actually calls off it is `saveStockTransaction()` (non-financial).
- Recommend: (1) add the missing formal `Global` dependency declaration to make the real dependency explicit, and (2) treat this module as blocked because of the tax-amount calculation, independent of the `IWalkInSalesDao` question.

---

### Entity — Needs owner review (no Billing/Global dependency, but IS the money-data source)
- **Formal:** No `<dependency>` on `Billing` or `Global` anywhere in `Entity/pom.xml` — confirmed by direct inspection of the file.
- **Source-level:** All 111 of Entity's own `com.param.billing.*`/`com.param.global.*`-matching import lines resolve to classes physically defined **inside Entity itself** — i.e., by the strict "does this module depend on Billing or Global" test, the answer is unambiguously **no**.
- **Why this still needs owner sign-off before Track A, despite a clean formal answer:** Entity is the one module every other module (all 17 of them, formally, via `<artifactId>Entity</artifactId>`) depends on for its JPA entity classes. Entity physically hosts the real financial entity model under `com.param.billing.global.model.*` and `com.param.billing.packages.*` — `BankBranchMaster`, `BankMaster`, `BillFundSanctionDetails`, `BillingGroupMaster`, `BillStatusMaster`, `CashHandoverMaster`, `CashInOut`, `CompanyReceiptMaster/Detail/Expense`, `CostEstimate(Details)`, `CouponTemplateMaster/Details`, `CreditNoteReasonMaster`, `CurrencyMaster`, `DepositMaster`, `PaymentDepositConsumption(Details)`, `PaymentRefundMaster`, etc. (confirmed by direct directory listing of `Entity/src/main/java/com/param/billing/global/model/`). Several other modules (Pharmacy, BloodBank, Abdm, HIS-WAR, Procurement) import these Entity-hosted financial entities directly.
- This is exactly the "looks safe by the letter of the dependency check, but touches money code" case the owner asked to have flagged: a compile-driven Java 21 change to Entity's persistence annotations or class structure (even something as apparently mechanical as an annotation rename in a later phase) directly touches the billing-critical data model that every other module's financial code reads and writes through. Recommend owner explicitly confirm whether Entity is in-scope for Track A now, or should wait alongside Billing/Global, given this containment relationship — the pom-dependency graph alone understates the risk.

### AssetManagement — Needs owner review
- **Formal:** `Global` dependency declared, versioned. No `Billing` dependency.
- **Source-level:** 25 unique imports, 22 resolve to `Global`, 3 to `Entity`; **zero** hits under any `com.param.global.billing.*` package.
- **The one nuance:** `com.param.global.pharmacy.dao.IWalkInSalesDao` is imported and autowired (`AssetManagement/src/main/java/com/param/assetManagement/maintenance/service/CorrectiveMaintenanceScheduleServiceImpl.java:59`). This interface (physically defined in `Global`) mixes real billing methods (`saveBill`, `updateDepositDetails`, `updateSalePaymentModeIdInDepositMaster`, `getSalePaymentModeDetailsConsumsedAmount`) with pure inventory methods (`saveStockTransaction`, `getBatchAllocation`) in a single interface. AssetManagement's actual call site (line 144) invokes **only** `saveStockTransaction(stockTransaction)` — a non-financial inventory-transaction write.
- **The risk is architectural, not behavioral today:** because this shared interface will very likely be touched (method signatures, implementation) during the Billing/Global correctness work (it's a pharmacy billing DAO by primary purpose), any Track A change to `CorrectiveMaintenanceScheduleServiceImpl.java` that also needs to compile against a modified `IWalkInSalesDao` could collide with in-flight Billing/Global work. Everything else in this module is clean generic inventory/asset DTOs (`com.param.global.dto.global.*`, `com.param.global.dto.items.*`) with no money fields (verified by reading `ItemListResponse.java` — no price/tariff/amount fields present, only id/name/category/quantity).

### LIS — Needs owner review
- **Formal:** `Global` dependency declared, versioned.
- **Source-level:** 52 unique imports, 48 resolve to `Global`, 4 to `Entity`; zero hits under `com.param.global.billing.*`.
- **The nuance:** `com.param.global.dto.BillingMasterDto` (real money fields: `netAmount`, `discountAmount`, `grossAmount`, `totalBillAmount`, `paidAmount`, `outstanding`) is imported in `LIS/src/main/java/com/param/lis/transaction/dao/BioChemResultEntryDaoImpl.java:31` — but grep confirms this is the **only** occurrence of "Billing" in that file; the import is dead/unused code, not an active dependency. Separately, `com.param.global.common.IBillingCommonConstants` (a constants-only interface — deposit-status codes, document-numbering-sequence IDs, no executable logic) is `implements`-ed by two service classes (`LabTransactionServiceImpl.java`, `SpecimanReceiptServiceImpl.java`) for its numbering constants, not for any billing calculation.
- Net effect: no live financial-logic dependency was found in use, but the dead `BillingMasterDto` import signals someone previously wired (or intended to wire) a bill-status check into lab result entry, and the `IBillingCommonConstants` inheritance is a minor but real coupling to Global's billing-numbering scheme. Recommend the owner decide whether "imported but unused" is acceptable to treat as safe, or whether it should be cleaned up (removing a truly dead import is a zero-risk, non-behavioral change) before this module is marked clear.

---

## Modules confirmed safe — detail (brief, since nothing here is ambiguous)

- **AngularSpring**: `packaging=war`, `src/main/webapp` only (`index.jsp`, `web.xml`); no `src/main/java` directory exists at all. No possible Billing/Global exposure.
- **BloodBank**: formal `Global` dep; 22 unique cross-module imports, all generic (org/unit/kin/date/JSON utilities, `GenderMasterDto`, `SubSpecialityMasterDto`, donor/cabin models). One `IBillingCommonConstants` import (constants only, not implemented/used for logic in the grepped context beyond generic status codes).
- **CSSDMaster**: formal `Global` dep; 10 unique imports, all equipment/priority/slot master data.
- **DietAndKitchen**: formal `Global` dep; 61 raw import lines but 54 are self-referential (its own `com.param.global.master.*`-packaged diet/meal/dish classes); the 7 genuine cross-module imports are non-financial (`CabinMaster`, `CountryMaster`, `ServiceException`, `CabinMasterDto`).
- **LinenLaundry**: formal `Global` dep; 9 unique imports, all generic batch/item/store DTOs plus `IGlobalDao`/`Util`.
- **RabbitMQ**: formal `Global` dep; exactly 1 import (`com.param.global.common.ServiceException`) — a generic exception type.

---

## Notes on formal-vs-actual mismatches (as requested)

| Module | Formal dep declared? | Real cross-module usage found? | Mismatch direction |
|---|---|---|---|
| Inventory | No | Yes (tax-amount calculation via `Global`) | Under-declared — depends in practice, not on paper |
| Entity | No | No (all self-referential) | None — formal and actual agree (Entity does not depend on Billing/Global) |
| AssetManagement | Yes | Yes, but only a non-financial method off a mixed-concern interface | Formal declaration is broader than the actual risk surface |
| All other formally-Global-dependent modules (Abdm, ADT, BloodBank, CSSDMaster, DietAndKitchen, HIS-WAR, LinenLaundry, LIS, OTIMS, Pharmacy, Procurement, RabbitMQ) | Yes | Consistent with formal declaration | None |

Inventory is the only module where the formal pom.xml under-states a real, financially-relevant dependency — worth fixing as a build-hygiene item independent of the Java 21 question.
