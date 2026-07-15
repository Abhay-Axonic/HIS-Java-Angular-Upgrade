# Audit: Billing Correctness Risks Beyond Exception Handling — legacy/Billing, legacy/Global, legacy/Entity

*Read-only audit, 2026-07-04. Part of the whole-codebase optimization-readiness audit (see also: AUDIT_WholeCodebase_patterns.md, AUDIT_TestCoverage.md, AUDIT_JavaUpgradeBlockers.md, AUDIT_Angular18Blockers.md, AUDIT_SQLRisk.md). No file under legacy/ was modified. The cross-category "what to fix first" recommendation for the whole audit set is at the end of this document.*

**Scope:** billing-math and billing-logic correctness risks in `legacy/Billing` (558 files), plus the shared billing-relevant code it depends on in `legacy/Global` (2,669 files) and `legacy/Entity` (1,422 files). This audit deliberately excludes exception-handling/silent-failure patterns — those 767 findings are fully catalogued in `docs/AUDIT_Billing_exceptions.md` and are not repeated here.

**Method:** read-only. Grep-verified pattern sweeps across all three modules' `src/` trees (compiled sources only; stray `.java~` backups excluded from counts), with manual reading of the highest-risk methods (`collectPayment`, `saveBill`, `consumeDeposit`, deposit/refund DAO writes, the Razorpay amount-conversion blocks).

**Framing:** every behavior documented below is *current legacy behavior*. Per `RULES.md`, fixing any of them — even where the current output is provably wrong — is a **Behavioral Change** requiring its own `docs/BUSINESS_RULE_MANIFEST.md` entry and owner approval; the modernization's shadow-testing circuit breaker (any amount differing from legacy by more than $0.01) means several of these must be understood *before* shadow testing, because a correct new implementation would legitimately diverge from legacy output and trip the breaker. Findings are clustered by root cause and ranked by (a) occurrence count, (b) severity — anything that can silently produce a wrong charged amount ranks highest, (c) fixability.

---

## Ranked risk clusters

### RISK-1 — Currency arithmetic is performed in binary floating point (`double`/`Double`), not `BigDecimal` — systemic, ~270+ live arithmetic sites

The dominant pattern across all three modules is: money is stored in DB/DTO fields (often `BigDecimal`), but the moment any *calculation* happens, values are unwrapped via `.doubleValue()` and the actual math — discount %, tax split, co-pay share, refund balance, deposit consumption, package-cap remaining — runs in `double`/`Double`, then is re-wrapped for persistence. Binary floating point cannot represent most decimal fractions exactly, and long accumulation loops compound the error; this is the classic mechanism for invoices that are off by a cent.

**Counts (grep-verified):**
- Primitive `double`/`float` declarations with money-suggestive names (amount/price/charge/balance/total/discount/tax/deposit/refund/due/bill/…): **178 in Billing, 257 in Global, 386 in Entity**. Including boxed `Double`/`Float` and accessor signatures, the raw footprint is ~1,082 hits across 122 Billing files, ~1,946 across 189 Global files, ~2,177 across 217 Entity files.
- Sites performing **real arithmetic** (`+=`, `-=`, `x = x ± y`, `*`, `/`) on these money variables: **56 in Billing, 217 in Global, 0 in Entity** (Entity is pure pass-through storage).
- `BigDecimal` appears widely (972 refs in Billing, 1,618 in Global) — but predominantly as a storage type. At the dangerous call sites, `BigDecimal` fields are unwrapped to `double` before the math happens.
- Compounding this, the **noise-carrying `new BigDecimal(double)` constructor** (instead of `BigDecimal.valueOf` or a String) is used ~**249 times in Billing and ~335 in Global**, so even values "returned to" BigDecimal carry raw binary noise.

**Representative arithmetic sites (all money, all floating point):**
- `legacy/Billing/.../transaction/service/BillingMasterServiceImpl.java:1879-1886, 1947-1949, 1977, 2027-2029` — `dueAmount`, `totalSelfDueAmount`, `totalCreditDueAmount` accumulated as `double` inside `collectPayment` itself.
- `legacy/Billing/.../transaction/service/PaymentRecieptMasterServiceImpl.java:50-51,147-148` — `amountCollected += ...; dueAmount = payeeDueAmount - amountCollected;` in `Double`.
- `legacy/Billing/.../transaction/service/PaymentRefundDetailsServiceImpl.java:121-162, 383-390, 560-567` — refund amounts accumulated in `Double` loops.
- `legacy/Billing/.../transaction/service/RefundCNMasterServiceImpl.java:97-127` — `BigDecimal getRefundAmount()` unwrapped to `double` for refund balance math.
- `legacy/Billing/src/main/java/com/param/procedure/service/ProcedureOrderServiceImpl.java:2700` — `public double calculateAmountOnPercent(double percentage, double amount) { double discount = (amount / 100) * percentage; ... }`.
- `legacy/Global/src/main/java/com/param/global/common/BillingCommonMethods.java:322-335` — the shared co-pay/pre-auth-cap/self-vs-credit-payable split utility (`updatePreAuthAndServiceCapAmountForSecondContract`) is entirely `Double`-typed; the same second-round co-share block is duplicated near-verbatim across ~150+ sites in `ContractConsumptionServiceImpl.java` and `ContractConsumptionForCostEstimationServiceImpl.java`.
- `legacy/Global/src/main/java/com/param/global/service/MPackageMasterServiceImpl.java` — package-cap remaining computed as `double capAmount = srv.getAvailableCap() - ...doubleValue();` repeated ~13 times.
- `legacy/Billing/.../config/service/CompanyInvoiceCreationServiceImpl.java:399` — inner sums correctly use `BigDecimal.add()`, then both results are unwrapped to `double` and added in floating point.

**Severity:** highest — this is the mechanism most likely to already be producing sub-cent-to-cent discrepancies on real invoices, and it will make byte-identical shadow-test reproduction of legacy output *mandatory in `double`* until the owner approves a precision migration. **Fixability:** well-understood but large; must be migrated flow-by-flow with shadow comparison, and any output change is a Behavioral Change. (See also AUDIT_SQLRisk.md one-off C: the DB columns themselves are `double precision`, the storage-layer counterpart of this same root cause.)

### RISK-2 — Four different rounding engines coexist, including whole-currency-unit rounding, with contradictions inside single files — ~190 sites

There is no canonical "round a bill amount" rule. Multiple mechanisms are live simultaneously:

| Mechanism | Count | Effective rule |
|---|---|---|
| `BigDecimal.setScale(2, ROUND_HALF_EVEN)` | 45 (41 Billing + 4 Global) | banker's rounding to cents |
| `BigDecimal.setScale(2, ROUND_HALF_UP)` | 1 (`OrderDetailsPayeeMapperDto.java:178` — baked into the setter, applied to every `payeeAmount` ever assigned) | half-up to cents |
| `Math.round(x*100.0)/100.0` | 15 | half-up-on-positives to cents, in `double` |
| `Math.round(x)` — **no `*100` scaling** | 8, all Billing (`BillingMasterController.java:222,224,226,238,240,242`; `PaymentDepositConsumptionDaoImpl.java:480,482`) | rounds money to the nearest **whole currency unit**, discarding all fractional currency |
| `DecimalFormat("#.##")` format-then-reparse (`new Double(df2.format(x))`) | 116 `df2.format` uses (44 Billing + 69 Global + 3 Entity), of which ~66 are the full `new Double(df2.format(...))` round-trip | HALF_EVEN to ≤2 decimals via a **string round-trip**, using the JVM default locale |
| `String.format("%.2f", ...)` then re-parse | 3 (incl. `TPeriodicityServiceOrdersServiceImpl.java:243` on a tax amount) | half-up to cents via string, plus a float round-trip |

**Grep-verified contradictions (same quantity, different rule):**
- `BillingMasterServiceImpl.java:1978` rounds `dueAmount` via `Math.round(x*100)/100`, and line `2206` re-rounds the same `dueAmount` via `setScale(2, ROUND_HALF_EVEN)` moments later in the same method.
- `BillingMasterController.java` rounds `netAmount`/`selfPayable`/`creditPayable` to the nearest whole unit at lines 222-242 (for the save-bill integrity check, see ONE-OFF-2) while the same fields in the same class are rounded to cents with HALF_EVEN at lines 366-369, 405-407, 444-499, 2971-2997, 3764-3770.
- The concept "amount payable to a payee" gets `ROUND_HALF_UP` forced by one DTO's setter (`OrderDetailsPayeeMapperDto.java:178`) but **no rounding at all** in the sibling pharmacy DTO (`SaleDetailPayeeMapperDto.java:85-86`), whose consumer additionally passes it through `new BigDecimal(double)` (`WalkInSalesService.java:4482-4483`), carrying binary noise into the stored value.
- The `df2` `DecimalFormat("#.##")` used for the round-trip pattern is **defined three separate times** (`Global/.../OrderMasterServiceImpl.java:57`, `Global/.../OrderDetailsMasterServiceImpl.java:73`, `Entity/.../GlobalCommonDateUtils.java:57`) rather than once; `DecimalFormat` is also locale-sensitive (a comma-decimal default locale would make `new Double(df2.format(x))` throw or misparse) and `DecimalFormat` instances are not thread-safe, and these are `public static final` fields shared across requests.

**Severity:** high — HALF_EVEN vs HALF_UP vs whole-unit rounding produce visibly different amounts on the same input; the whole-unit sites are ±0.50 per value. **Fixability:** good — a single shared rounding utility is a small change per site, but *every* site whose output changes is a Behavioral Change.

### RISK-3 — No idempotency keys and no database-level concurrency control on any money mutation; the only guard is single-JVM `synchronized` controllers, and one payment path bypasses even that — 6/6 payment paths unguarded

**Idempotency:** all **6** payment-collection/settlement entry points found (`BillingMasterServiceImpl.collectPayment` :1877, `collectPaymentFromEncounter` :2981, `PaymentRecieptMasterServiceImpl.savePaymentReciept` :48, `BillingPaymentDetailsDaoImpl.savePayment` :45 (currently unwired), `BillingMasterController.collectPayment` :743, `RazorPayBillService.settleBill` :27) insert payment records with **no unique-transaction-ID / idempotency-key check**. `collectPayment`'s only duplicate defenses are a bill-cancelled check and a comparison of the **client-echoed** `outstanding` value against the DB (`BillingMasterServiceImpl.java:1963-1964`) — incidental protection that fails for partial payments, for two requests that both read `outstanding` before either writes, or on multi-instance deployment.

**Concurrency:** across all three modules there are **0** `@Version` columns, **0** `SELECT ... FOR UPDATE` on any balance (the only 2 `FOR UPDATE` in scope lock the receipt-number sequence in `GlobalUnitMasterDaoImpl.java:366,529`), and 0 explicit locks. The entire strategy is `synchronized` on **25** Spring singleton controller methods in Billing (`saveBill` :185, `collectPayment` :743, `settleCompanyInvoices`, etc.) — which serializes *all* users through one JVM-wide lock per endpoint, provides nothing across multiple app-server instances, and is bypassed entirely by:
- `RazorPayBillService.settleBill` (`legacy/Billing/.../transaction/service/RazorPayBillService.java:27-31`) — an `@Async` method invoked from `RazorPaymentController.confirmAppointment` (:286) that calls `iBillingMasterService.collectPayment(...)` **directly at the service layer**. Payment-gateway callbacks routinely fire more than once; each firing is a concurrent, unsynchronized, unguarded duplicate-payment-recording opportunity. (Note: this is the same `confirmAppointment` path flagged for logging-only under BR-012's `PAYMENT_RECONCILIATION_GAP` marker — the reconciliation risk logged there and the duplicate-recording risk here are two facets of the same unguarded flow.)

**Lost-update evidence (read-modify-write with absolute SET):**
- `PaymentDepositConsumptionServiceImpl.consumeDeposit` (:38-116) reads deposit details, computes the new balance in Java `double`s, and writes back via `DepositMasterDaoImpl.updateDepositDetails` (`Global/.../billing/dao/DepositMasterDaoImpl.java:351-371`) — an **absolute** `SET depo.balanceAmount = :balanceAmount` with no version/lock. Two concurrent payments consuming the same deposit can both read the same stale balance; the later write silently erases the earlier decrement (double-spend of deposit funds).
- Same pattern in the refund flow: `RefundCNMasterServiceImpl.java:77-136` → absolute `SET depo.availableDeposit = :availableDeposit` (`DepositMasterDaoImpl.java:303-348`).
- By contrast some updates *are* written relatively and atomically (`consumed_deposit = consumed_deposit - :amt`, `PaymentDepositConsumptionDaoImpl.java:170-175`; `available_cap = coalesce(available_cap,0) + :capAmount`, `TPackageConsumptionCapDetailsDaoImpl.java`), proving both idioms coexist with no convention.

**Severity:** high — double-click, gateway-callback retry, or a second app instance can double-record a payment or double-consume a deposit *today*; unlike RISK-1's sub-cent drift, this is whole-payment-sized. **Fixability:** moderate — unique constraint on a transaction reference + relative SQL updates or optimistic locking; behavioral semantics of the duplicate case need owner definition.

### RISK-4 — All billing date handling runs on legacy `Date`/`Calendar`/`SimpleDateFormat` with zero explicit timezones and zero explicit locales — 180/180 unsafe formatter instantiations

- **0 of 180** `new SimpleDateFormat(...)` instantiations (27 Billing, 124 Global, 29 Entity) call `.setTimeZone(...)`, and **0 of 180** pass a `Locale`. Every parse/format silently uses the JVM default — the classic mechanism for charges landing on the wrong billing day across midnight/DST when server zone ≠ business zone.
- `import java.util.Date`: 76 Billing files / 284 Global / 906 Entity. `java.time` exists in only ~12 Billing files and is absent from every invoice-date/tariff-validity/period-arithmetic path found.
- The system's own "what timezone is this server in" detection (`Entity/.../GlobalCommonDateUtils.java:276-291` and duplicated at `Global/.../opd/common/CommonDateUtils.java:27-40`) matches `getRawOffset()` — **DST-unaware** — against the first alphabetical zone ID at class-load, then feeds that guess into all unit-timezone conversion (`TimeZoneComponent.getDateByzone`) used to stamp billing dates.
- `Entity/.../utility/TimeZoneComponent.java:175-179` (`convertToLocalDateToDate`): computes the unit's configured zone, then **ignores it** and converts with `ZoneId.systemDefault()`; `getDateByzoneWithDayEndTime` (:226-235) likewise ignores its `unitId` parameter. The `"Asia/Calcutta"` fallback is hardcoded in ~20 places across 5 files.
- Billing-period arithmetic is naive millisecond math: `GlobalCommonDateUtils.getDateDifference` (:366-370) does `diff / (24*60*60*1000)` (wrong across DST transitions); `TPeriodicityServiceOrdersServiceImpl.java:191-208, 338-354` computes periodic-service billing ranges the same way after parsing with zone-less `SimpleDateFormat`.
- Real pattern-letter bugs exist in shared Global code on billing-adjacent flows: `"yyyy-mm-dd HH:mm:ss"` (`mm` = minutes, not month — `CoversheetTemaplateServiceImpl.java:1585,2351`) and `"DD-MM-YYYY HH:mm:ss"` (`DD` = day-of-year, `YYYY` = week-based year, diverging near year end — `PatientRegistrationServiceImpl.java:2252,2279`, `sync/AppointmentServiceImpl.java:147,181`).
- Within Billing itself, tariff validity dates (`TariffMasterDaoImpl.java:86-87,129-130,171-172,215-216,372-375` and 8 more sites) and invoice dates (`MCompanyInvoiceMasterDaoImpl.java:49,64-65,114-115`) all go through zone-less `SimpleDateFormat` string round-trips, with `"dd-M-yyyy"` and `"dd-MM-yyyy"` patterns mixed in the same file.

**Severity:** medium-high — silent day-boundary corruption risk concentrated exactly where `RULES.md` demands synthetic edge-case injection (month-end, Dec 31, timezone-shift variants). **Fixability:** moderate; per-flow migration to explicit-zone `java.time`, each date shift a Behavioral Change.

### RISK-5 — Sign discipline for reversals/credits is a caller-side convention (`* -1` at ~40+ call sites), and a shared helper silently strips signs with `Math.abs` — 63 callers

- No "adjust amount" method in scope branches on an internal debit/credit flag (so no internal sign-inversion bug exists to trip). Instead, the additive cap/consumption updaters (`TPackageConsumptionCapDetailsDaoImpl.updatePackageCap/updatePackageCapItem/updatePackageServiceCap/...`, `ContractConsumptionDaoImpl.updatePharmacyCapQuantity`) execute `available_cap = coalesce(available_cap,0) + :capAmount` and rely on **every caller pre-negating** for consumption vs reversal — done via `* -1` / `.negate()` at ~40+ call sites (`BillingMasterServiceImpl.java:3035`, `TPeriodicityServiceOrdersServiceImpl.java:606`, `MPackageMasterServiceImpl.java` ~dozens, `BillingCommonMethods.java:192,211,717,734,830-850`, `WalkInSalesService.java:2478,2855,2958,3058,4282`). One forgotten or doubled `-1` at any site adds instead of subtracts — and nothing structural would catch it.
- Meanwhile `Entity/.../GlobalCommonDateUtils.java:69-71` defines `getAmount(Double)` as `new Double(df2.format(Math.abs(amount)))` — it **silently converts any negative amount to positive** — and has **63 callers** (all in Global, including `WalkInSalesService` line-total/tax accumulation and `GlobalService` price recalculations). Its sibling `getAmountCap` (:73-74, 15 callers) does *not* take the absolute value. Any negative value (a return, credit, or downward adjustment) routed through `getAmount` instead of `getAmountCap` flips direction silently; the two near-identical names invite exactly that mistake.

**Severity:** medium — no confirmed live wrong-sign defect found, but the design guarantees a wrong-sign bug is one call site away, and `Math.abs` on money in a 63-caller shared helper is a direction-error factory. **Fixability:** good (centralize signing; split/rename the abs helper), pure-structure changes verifiable by byte-identical output.

### RISK-6 — Hardcoded rates: largely CLEAN, with one benign repeated literal — 12 occurrences of `100` across 4 files

Genuinely good news, grep-verified: **no hardcoded tax/discount/GST rate literals were found anywhere in scope.** Every percentage applied in billing arithmetic is DB-sourced (`tax.getTaxPercentage()`, `dto.getCoShare()`, `variableFactorDto.getMultiplicationFactor()`, slab bounds from `getSlabFrom()/getSlabTo()`, etc.); all `* 0.01` / `/100` literals are unit conversion of those DB values, and no bare amount thresholds gate any discount/tax branch. The single cross-file repeat is the literal `100` as the "single payer pays 100%" co-pay fallback, hardcoded 12 times across 4 files (`OrderMasterServiceImpl.java:1651,1757`; `EncounterMasterServiceImpl.java:1344`; `WalkInSalesService.java:3860,5207,6035,6183,6218`; `OrderDetailsMasterDaoImpl.java:90,328,666,2427`) — mathematically definitional rather than tunable, but 12 unshared copies of one business default. **Severity:** low. **Fixability:** trivial (one shared constant), byte-identical.

---

## One-off findings

### ⚠ URGENT — ONE-OFF-1: Razorpay rupee→paise conversion via string-splitting produces a 10x-wrong charge for any amount with one decimal digit — live wrong-amount bug, duplicated twice

> **STATUS CORRECTION (2026-07-15): This bug is already fixed and owner-approved.** The fix landed later on the same day this audit was written (see `docs/BUSINESS_RULE_MANIFEST.md` BR-018, `docs/REVIEW_razorpay-amount-fix.md`) but this section was never revisited to reflect that. Re-verified present in the current source as of 2026-07-15 — see `docs/DECISION_NEEDED_RazorPay10xUndercharge.md` for full detail, current status, and the parts of this finding (historical reconciliation, deployment verification, and the two client-trusted-amount endpoints noted below) that are still genuinely open. The description below is preserved as the original historical finding, not current behavior.

`legacy/Billing/src/main/java/com/param/billing/global/controller/RazorPaymentController.java:396-409` (inside `razorPayPaymentLink`) and, byte-for-byte copy-pasted, `:508-519` (inside `getOrderIdForRazorPayPaymentOnBilling`):

```java
String s = reqDto.getRazorAmount().toString();
Integer manupulateAmount = 0;
String[] array = s.split("\\.");
String SecondData = "0";
if(array.length > 1 && !array[1].equals(SecondData)) {
    s = array[0]+array[1];
    manupulateAmount = Integer.parseInt(s);
} else {
    s = array[0]+"00";
    manupulateAmount = Integer.parseInt(s);
}
```

This converts rupees to paise by concatenating the fractional digits onto the integer part instead of multiplying by 100. When the amount's string form has **one** fractional digit — e.g. `"123.4"`, which is exactly what `Double.toString()` produces for such values — it yields `1234` paise (₹12.34) instead of `12340` (₹123.40): the customer is charged **one tenth of the bill**. An amount like `150.5` → charged ₹15.05. The wrong `manupulateAmount` flows directly into the Razorpay order/payment-link payload (`orderRequest.put("amount", manupulateAmount)` :523; `Mpesa.RazorPaytransactionStatus(...)` :417). Because this is the *charged* amount, the discrepancy is not sub-cent — it is 90% of the invoice, silently, today, on any bill whose paise component is a multiple of 10 (e.g. ₹xxx.10/.20/…/.90). Two additional endpoints in the same file (`getOrderIdForRazorPayPayment` :108, `getOrderIdUnitSubscriptionPay` :130) use a **third** approach — no server-side conversion at all, trusting a client-supplied pre-converted `Integer` (`getRazorPayBillAmount`, computed nowhere server-side per full-tree grep) — so the same file contains two buggy conversions and one unvalidated client-trusted conversion, and zero robust ones. *(Note: this is the same `RazorPaymentController.java` whose exception handling was fixed under BR-012; these amount-conversion defects are a separate, unaddressed class of bug in the same file.)*

### ⚠ URGENT — ONE-OFF-2: The save-bill integrity gate tolerates a ±1.00 whole-currency-unit mismatch — 100x the project's own $0.01 circuit-breaker tolerance

`legacy/Billing/src/main/java/com/param/billing/global/transaction/controller/BillingMasterController.java:206-283` (`saveBill`): client-submitted order totals are summed in `Double`, rounded to the nearest **whole unit** (`Math.round(netAmount)` :222-226), compared against the server-side sums (also whole-unit-rounded :238-242) — first by **truncating each side to its integer part via string-splitting** (`Integer.parseInt(String.valueOf(netAmountSave).split("\\.")[0])` :246, and for self/credit payable :263-264), and if that fails, by accepting any difference within **±1** (`if(diffNetamount <= 1 && diffNetamount >= -1)` :254, :272). Net effect: a bill whose client-side total disagrees with the server's stored order sum by up to a full currency unit saves without complaint. The same integer-part-truncation comparison idiom appears once more in `PaymentDepositConsumptionDaoImpl.java:480-484` (deposit-consumption sum check, `Math.round` to whole units then compare). This is the codebase's own admission that RISK-1's floating-point drift exists — papered over with a tolerance 100x looser than the `RULES.md` circuit breaker, at the exact point where a genuine amount-tampering or calculation bug should have been caught.

### ONE-OFF-3: Invoice-date range filter built by concatenating `Date.toString()` into raw SQL

`legacy/Billing/src/main/java/com/param/billing/global/transaction/dao/MCompanyInvoiceMasterDaoImpl.java:75`: `query.append(" AND im.invoice_date <='"+fromDate+"' AND im.invoice_date >='"+toDate+"'")` — a `java.util.Date` is string-concatenated into the query (producing JVM-default-locale/zone text like `Wed Jul 01 00:00:00 IST 2026`, not a SQL date literal), for a company-invoice date filter. Both an injection surface (see AUDIT_SQLRisk.md pattern #1) and a correctness risk; note also the comparison directions pair `<= fromDate` with `>= toDate`.

### ONE-OFF-4: Deposit `availableDeposit` written from the client request payload

In the `consumeDeposit` flow, `DepositMasterDaoImpl.updateDepositMaster` (`legacy/Global/.../billing/dao/DepositMasterDaoImpl.java:213-233`) executes an absolute `SET depo.availableDeposit = :availableDeposit` where the value comes from `reqDto.getPaymentDepositDetailsList()` — i.e., the caller's payload, not a value re-read from the DB (`PaymentDepositConsumptionServiceImpl.java:62`). The adjacent `consumedDeposit` column *is* updated relatively and safely in the same statement. A stale or manipulated client value directly overwrites a patient's available deposit balance.

### ONE-OFF-5: Tax multiplier built as `new BigDecimal(0.01)`

`legacy/Billing/.../transaction/service/InvestigationServiceImpl.java:274`: `taxAmt = serviceAmt.multiply(orderDetailsMasterDto.getTaxPer().multiply(new BigDecimal(0.01)))` — the double-constructor makes the multiplier exactly `0.01000000000000000020816681711721685…`, so even this all-BigDecimal tax computation is contaminated by binary noise; one of the ~249 Billing instances of the noise-carrying constructor (RISK-1), called out because it sits directly in a tax-amount formula.

---

## Cross-category recommendation: what to fix first (whole audit set)

*This synthesizes all six audits (this one plus AUDIT_WholeCodebase_patterns, AUDIT_TestCoverage, AUDIT_JavaUpgradeBlockers, AUDIT_Angular18Blockers, AUDIT_SQLRisk). The brief asked for a recommendation based on "which pattern has the highest count and lowest risk." Two answers, because the honest answer splits: the highest-count/lowest-risk *pattern* to grind down is not the same as the thing that most urgently needs a human decision.*

**The highest-count, lowest-risk, most-mechanical pattern to fix first — the continuation of the work already underway — is the codebase-wide silent-exception-swallow / no-logger cluster.** The whole-codebase audit found **12,356 empty catch blocks outside Billing** (dwarfing the 767 already-catalogued Billing findings) and **only 2.2% of files (182 of 8,256) have any logger at all**, with **12 of 17 modules having zero logging**. This is the exact same defect the BR-006 through BR-017 work has been mechanically fixing inside Billing, one file at a time, with a proven Maker→Checker→owner-approve pipeline. It is the project's stated #1 priority, it is the largest single count in the entire audit set, it is the most mechanical to fix (log-and-route to the centralized handler), and — critically — it is the prerequisite for everything else: none of the billing-correctness risks below can be safely diagnosed in production until failures actually surface in a log instead of vanishing. **Recommended first move: extend the existing routine-fix queue from Billing to the other 16 modules, starting with `Global` (7,545 empty catches, and the module every other module depends on), after first standing up a shared logger in the 12 modules that have none.**

**However, three findings should jump the queue regardless of the count-based ranking, because they are live and money-affecting today:**

1. **The Razorpay 10x-undercharge bug (BILLING ONE-OFF-1)** is charging patients one-tenth of the bill on any amount ending in .10/.20/…/.90, silently, right now. This is not a "readiness" issue — it is an active revenue-loss and billing-accuracy defect of exactly the kind RULES.md's circuit breaker exists to catch, and it sits in the payment path. It warrants its own BUSINESS_RULE_MANIFEST entry and an immediate owner decision, ahead of any bulk cleanup.
2. **The hardcoded live credentials** (whole-codebase #4 and SQL one-off A: AWS keys, a SQL Server `sa` password to an external host, Razorpay live keys, 10 preprod DB password files, SMTP/Twilio secrets) are a standing security exposure that is independent of the modernization phasing and should be rotated and externalized regardless of where it ranks by count.
3. **The ±1.00 save-bill tolerance (BILLING ONE-OFF-2)** means the system's own integrity check is 100x looser than the project's stated $0.01 tolerance — worth an owner's awareness now, because it changes what "shadow testing passes" even means.

**What must NOT be first, and why:** the Java upgrade (all of AUDIT_JavaUpgradeBlockers) and the Angular migration (all of AUDIT_Angular18Blockers) are enormous and are correctly the lowest priority per CLAUDE.md — the audit confirms the code compiles and runs fine on JDK 17 today, so there is no forcing function to touch them yet. And the billing floating-point migration (RISK-1), despite being the highest-severity *billing* cluster, cannot go first because there is **no test suite** (AUDIT_TestCoverage: 0 of 18 modules have a substantive test, and CI skips tests entirely) — changing currency math with no shadow-test harness in place would be changing the thing most likely to trip the circuit breaker with no way to verify the change is correct. The sequence the evidence supports is: **(1) surface failures everywhere (logging + exception routing, the mechanical bulk-fix), (2) build the shadow-test harness RULES.md already requires, (3) then and only then touch billing math (RISK-1/2/4) under that harness — with the three urgent items above pulled forward as their own owner-approved one-offs immediately.**
