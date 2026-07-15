# Test Coverage Audit — legacy/ Maven Modules

*Read-only audit, 2026-07-04. Part of the whole-codebase optimization-readiness audit (see also: AUDIT_WholeCodebase_patterns.md, AUDIT_JavaUpgradeBlockers.md, AUDIT_Angular18Blockers.md, AUDIT_SQLRisk.md, AUDIT_BillingRisk.md). No code was modified.*

## Summary

Of the **18 Maven modules** under `legacy/` (Abdm, ADT, AngularSpring, AssetManagement, Billing, BloodBank, CSSDMaster, DietAndKitchen, Entity, Global, HIS-WAR, Inventory, LinenLaundry, LIS, OTIMS, Pharmacy, Procurement, RabbitMQ), **16 of 18 (89%) have zero test files of any kind** — no `src/test/java`, no test files anywhere else. The remaining **2 modules (AssetManagement, BloodBank)** each have exactly one Java file under `src/test/java`, and both were opened and read in full: **both are the unmodified `mvn archetype:generate` boilerplate stub** — a JUnit 3 `TestCase` subclass named `AppTest` with a single method `testApp()` whose entire body is `assertTrue(true)`. They test nothing about the application.

**Verified substantive test count: 0 of 18 modules.** No module has a single test that exercises real application logic, a real assertion against real behavior, or a real Mockito/Spring-context test — despite five modules declaring a full modern test stack (Mockito, Spring Test, JSON-path-assert) in their `pom.xml`.

Separately, the search for `*Test*.java` files turned up three files that are **not tests at all** but stray scratch/dead-code files sitting inside `src/main/java` (Global, OTIMS, Billing) — see notes below. All other `Test`-named hits across ADT, Billing, Entity, LIS, Pharmacy are **domain vocabulary**, not software tests: this codebase models hospital lab/diagnostic "tests" (blood tests, serology tests, microbiology tests), so classes like `TestMaster`, `BloodTestMasterController`, `SensitivityTestResultMaster` are business entities, not unit tests — a false-positive pattern worth flagging so it isn't mistaken for coverage in future scans.

Every module — even the 13 with no test dependency of their own — inherits `junit:junit` version `3.8.1` from the root `legacy/pom.xml`'s top-level `<dependencies>` block (not `<dependencyManagement>`, and with **no `<scope>` specified**, i.e. default `compile` scope). This means JUnit 3.8.1 is compiled into and shipped inside every module's production artifact, not just a test-time dependency.

## Module-by-module table

| Module | `src/test/java` exists? | # test files | Test framework declared in pom? | Verified substantive? | Notes |
|---|---|---|---|---|---|
| Abdm | No | 0 | No (inherits parent's junit 3.8.1, compile scope) | N/A | Nothing test-related found. |
| ADT | No | 0 | No (inherits parent junit) | N/A | `find`-matches on "Test" are domain classes (`CorporateStatementChecklistMaster...`) — false positives, not tests. |
| AngularSpring | No | 0 | **Yes** — `junit:junit:3.8.1`, `scope=test`, declared explicitly in the module's own `pom.xml` (redundant with parent) | N/A | Dependency is entirely unused — module has no `src/test` directory at all. |
| AssetManagement | **Yes** | 1 | **Yes** — `junit:junit:4.4` declared with **no `<scope>` tag** (default = `compile`, ships in the WAR), plus `spring-test`, `hamcrest-all`, `mockito-core:1.10.19`, `json-path-assert` all at `scope=test` | **Read in full — NOT substantive** | `AssetManagement/src/test/java/com/param/AssetManagement/AppTest.java`: `public class AppTest extends TestCase` (JUnit 3 style, `junit.framework.TestCase`), one method `public void testApp() { assertTrue( true ); }`. Verbatim Maven archetype placeholder. Despite a full modern mocking/Spring-test stack being declared, none of it is invoked anywhere in the module. |
| Billing | No | 0 | No (inherits parent junit) | N/A | Found a stray file `src/main/java/com/param/billing/global/transaction/config/dto/test.java` (lowercase class `test`) — not a unit test. It's a `main()`-only scratch class that builds a trust-all `SSLContext` and calls a live SMS-gateway URL with a **hardcoded password (`Naharaccount123`) embedded in the query string**. Security-relevant but out of scope for test-coverage counting; not a test file. |
| BloodBank | **Yes** | 1 | No — module's own `pom.xml` declares no test framework directly (relies solely on inherited parent junit 3.8.1) | **Read in full — NOT substantive** | `BloodBank/src/test/java/org/BloodBank/AppTest.java` is byte-for-byte the same archetype stub as AssetManagement's (`package org.BloodBank; ... extends TestCase; testApp() { assertTrue(true); }`). All other `*Test*.java` matches in this module (`BloodTestMasterController`, `ScreeningTestMasterServiceImpl`, etc.) are medical-domain "blood test" classes, not software tests. |
| CSSDMaster | No | 0 | No | N/A | — |
| DietAndKitchen | No | 0 | No | N/A | — |
| Entity | No | 0 | No | N/A | All "Test" hits (`TestMaster`, `TestCostHead`, `SerologyTestResult`, etc.) are lab/diagnostic-test domain entities. |
| Global | No | 0 | No | N/A | Stray file `src/main/java/Test.java` (default package) — its **entire body is one `/* ... */` comment block** (a dead PDF-generation experiment using iText). Nothing in it compiles or runs. Not a test; dead commented-out code. |
| HIS-WAR | No | 0 | **Yes** — `junit`, `scope=test`, version inherited from parent `dependencyManagement` (3.8.1) | N/A | This is the WAR-assembly module; declared dependency is unused, no `src/test` dir. |
| Inventory | No | 0 | **Yes** — `junit:junit:4.4` (no `<scope>`, i.e. compile scope) + `spring-test`/`hamcrest-all`/`mockito-core:1.10.19`/`json-path-assert` (all `scope=test`) | N/A | Identical, copy-pasted dependency block to AssetManagement/LinenLaundry/Pharmacy/Procurement; no test files exist to use any of it. |
| LinenLaundry | No | 0 | **Yes** — same block as Inventory | N/A | Same pattern as Inventory. |
| LIS | No | 0 | No (inherits parent junit) | N/A | All "Test" hits (`TestCostHead`, `IncubationTestMaster`, `SensitivityTestResultMaster`, `MicrobiologyTestMaster`, etc.) are lab-test domain masters. |
| OTIMS | No | 0 | No (inherits parent junit) | N/A | Stray file `src/main/java/com/param/test/Test.java` — **not dead code**: it is a live `@Controller` (`@RequestMapping("/otims")`) exposing a real GET endpoint `/otims/test` that returns `"Hello"`, wrapped in a try/catch whose **catch block is completely empty** (silent exception swallowing — the exact anti-pattern CLAUDE.md names as the project's #1 problem). It also carries an unrelated `main()` method with scratch stream/map code. This is a developer sandbox class shipped into production source, not a test. |
| Pharmacy | No | 0 | **Yes** — same block as Inventory | N/A | Same dependency pattern; also has one unrelated domain hit (`AntibioticTestDoseDaoImpl` — medical "test dose," not a software test). |
| Procurement | No | 0 | **Yes** — same block as Inventory | N/A | Same pattern. |
| RabbitMQ | No | 0 | No | N/A | — |

## legacy/Testing — what it actually is

`legacy/Testing` is **not source code and is not wired into anything**. Its full contents:

- `HISTestCases.jar` — a compiled, self-contained ("fat") jar, built 2022-01-11, `Main-Class: com.his.testcases.TestRunner`. Its class list shows a **Selenium WebDriver UI-automation suite** (bundles `org.openqa.selenium.*` classes directly inside the jar) built with a Page-Object-Model structure (`.../pages/*.class`) and TestNG-style reporting (`com/his/ExtentReportListener/ExtentReportNG.class` — ExtentReports is a TestNG/JUnit HTML-report library). It covers roughly seven UI regression flows: `BillableConsumptionTest`, `PatientConsumableIndentTest`, `PendingIssueTest`, `LISConsolidated`, `WalkInSalesTest`, `IPDWalkInSalesReturnTest`, `MaterialIssueReturnTest`.
- `src/main/config/config.properties` — the runner's config: target URL `https://qa.his.clinivantage.dev/#/login`, browser=chrome, page-load/implicit-wait timeouts, and **hardcoded login credentials** (`username = PUJA`, `password = PUJA1960`) plus assorted test-data fixtures (UHID, item names, quantities) used by the bundled test classes.
- `src/main/Drivers/chromedriver.exe` — a Windows ChromeDriver binary for local execution.
- `screenshots/*.png` — three auto-captured screenshots from an actual run: the HIS login page (Pro.Care-HIS / Clinivantage branding, "Ver: HIS-1.26.0") and the same page with username `PUJA` typed into the field. These are Selenium run evidence, not documentation.
- `.gitkeep` — empty placeholder.

**No `.java` source is present anywhere in this directory** — only compiled `.class` files inside the jar. It cannot be modified, extended, or even code-reviewed without decompiling it. It is not referenced by `.gitlab-ci.yml`, any `devops/` script, or any other file in the repo (confirmed via repo-wide grep for `HISTestCases` and `Testing/`) — it is not run automatically anywhere. Given the January 2022 timestamps and today's date (2026-07-04), this is a roughly 4.5-year-old, unmaintained, source-less Selenium regression pack for a small slice of UI workflows — effectively dead weight sitting in the tree, not an active or runnable test suite.

## CI / test-execution wiring

There is a `legacy/.gitlab-ci.yml` with stages `maven-package-war → postgres → build-docker-images → deploy → approval-* → build-war → production → smetwo-production → finish`. **There is no test stage anywhere in this pipeline**, and no job runs `mvn test` or references JUnit/Surefire reports.

More importantly, every build script the pipeline actually invokes explicitly disables tests. All active `maven-package-war-*` and `build-war` jobs shell out to scripts in `legacy/devops/` (`smepreprod`, `smetwo`, `smetwopreprod`, `smeone`, `awsstaging`, `his-qa`, `meditestpreprod`, `mrrchpreprod`, `nchpreprod`, `oysterpreprod`, `queenspreprod`, `athaayupreprod`, `bpppreprod`), and **every single one of these scripts** runs:

```
mvn -ntp clean
mvn -ntp package -Dmaven.test.skip=true
```

(`his-qa` uses `mvn -U -ntp package -Dmaven.test.skip=true`.) `-Dmaven.test.skip=true` skips both compiling and running tests. This means that even the two trivial `AppTest` stubs in AssetManagement and BloodBank never execute in any deployment pipeline — dev, staging, QA, or production — because every single build path that produces a deployable WAR passes this flag. The `approval-qa`/`approval-deployment`/`approval-db`/`approval-management` stages gate on **named-user manual sign-off** (hardcoded usernames like `qa_user1`, `deploy_manager1` compared against `$GITLAB_USER_LOGIN`), not on any automated test or coverage result — there is nothing for CI to gate on, since nothing runs.

No `.github/workflows` directory, no `Jenkinsfile`, and no other CI/build config exists anywhere in `legacy/` beyond the single `.gitlab-ci.yml` and the `devops/` shell scripts described above.
