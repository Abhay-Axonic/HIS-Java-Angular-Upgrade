# Java Upgrade Blockers Audit — `legacy/` Maven Modules

*Read-only audit, 2026-07-04. Part of the whole-codebase optimization-readiness audit (see also: AUDIT_WholeCodebase_patterns.md, AUDIT_TestCoverage.md, AUDIT_Angular18Blockers.md, AUDIT_SQLRisk.md, AUDIT_BillingRisk.md). No files were modified. Per CLAUDE.md, version upgrade is the LOWEST priority and happens LAST, after billing-accuracy fixes and DB work are proven — this is preparatory research only, not a green light to upgrade.*

**Scope:** All 18 Maven modules under `legacy/` (root aggregator `HIS-POM` + Abdm, ADT, AngularSpring, AssetManagement, Billing, BloodBank, CSSDMaster, DietAndKitchen, Entity, Global, HIS-WAR, Inventory, LinenLaundry, LIS, OTIMS, Pharmacy, Procurement, RabbitMQ). All counts below are grep-verified directly against the poms/source; where noted, compile behavior was empirically verified by running `mvn compile` (Maven 3.8.7 / JDK 17.0.19, offline resolution against the environment's local repo).

## Confirmed current baseline

- **Spring**: root `legacy/pom.xml` pins `<spring.version>4.0.5.RELEASE</spring.version>` (released 2014, long EOL). This is not the whole picture: six child modules (Procurement, Inventory, RabbitMQ, LinenLaundry, Pharmacy, AssetManagement) instead define a *second*, differently-named property `<springframework.version>4.0.5.RELEASE</springframework.version>` — same value, different key, used by a disjoint set of `spring-*` artifacts within those same modules. On top of that, several `spring-jdbc`/`spring-orm`/`spring-context-support` dependencies are hardcoded to literal versions instead of any property (`4.0.5.RELEASE`, `4.0.7.RELEASE`, `4.3.6.RELEASE` all appear), so a single module's classpath can carry three different Spring 4.x point releases at once.
- **Java/compiler**: the root pom declares no `maven-compiler-plugin`, no `java.version`/`maven.compiler.*` property, and no `pluginManagement` for the compiler at all — every child module repeats its own compiler config independently. Of 17 child poms with a compiler setting: **13 already declare `maven.compiler.source/target = 17`** as bare properties (ADT, Billing, BloodBank, CSSDMaster, DietAndKitchen, Entity, Global, HIS-WAR, LIS, OTIMS), **6 instead pin an explicit `maven-compiler-plugin` version `3.5`** (released 2015) with `<source>17</source>/<target>17</target>` (Procurement, AssetManagement, LinenLaundry, Pharmacy, Inventory, RabbitMQ — note some modules appear in both counts because they set the property *and* the explicit plugin), and **Abdm alone is still pinned to `1.8`**, the one straggler in the reactor.
- **Verified empirically**: `mvn compile` was run in this sandbox (JDK 17, Maven 3.8.7) against Entity, Global, Billing, Abdm, and Inventory (the last two covering the `1.8`-pinned outlier and the old-compiler-plugin-3.5 group respectively). **All five compiled cleanly with zero errors.** This means the current `javax.*`/Spring‑4/Hibernate‑4 stack is *not* today blocked by JDK 17 — `javax.persistence`, `javax.servlet`, `javax.transaction`, etc. are ordinary library jars on the classpath, not JDK-bundled modules, so they resolve fine under a modern JDK. The real forcing function is entirely downstream: moving to **Spring 5/6 + Hibernate 6 + a Jakarta Servlet 5+ container**, all of which hard-require the `jakarta.*` namespace and will not compile against this code as-is.
- **Runtime container**: `legacy-codewhale-attempt/Dockerfile.tomcat` (sibling directory documenting the intended deploy target for this WAR) builds with `eclipse-temurin:17-jdk-jammy` and deploys to `tomcat:9.0-jdk17-temurin-jammy` — i.e. **JDK 17 + Tomcat 9**, which is exactly the transitional combination that still uses `javax.servlet` (Tomcat 10 is the first version requiring `jakarta.servlet`). The same file's `devops/setenv.sh` already hardcodes `--add-opens java.base/java.time=ALL-UNNAMED` plus four `java.base/java.security*` opens as required `JAVA_OPTS` — i.e. JPMS illegal-reflective-access workarounds are **already live and necessary today**, not a hypothetical future problem. (Note: `legacy-codewhale-attempt/` is the prior unverified upgrade attempt flagged do-not-trust elsewhere; these two files are cited only as evidence of the intended deploy target, not as reusable work.)

---

## Ranked, clustered blockers

### 1. `javax.persistence.*` → `jakarta.persistence.*`, gated by a Hibernate 4→6 major-version jump
- **Scope**: 1,582 files import `javax.persistence.*` (Entity: 1,385; Global: 110; CSSDMaster: 21; DietAndKitchen: 18; Procurement: 13; ADT: 12; Pharmacy: 11; Billing: 5; Inventory: 3; Abdm: 2; LIS: 2). Nearly every module depends on `Entity`, so this is effectively universal.
- **Root dependency**: `hibernate-entitymanager` / `hibernate-ehcache` are pinned to `4.3.5.Final` (released 2014, EOL) in 17 of 18 module poms (every module except AngularSpring).
- **Fix difficulty**: the annotation rename itself is a mechanical find/replace, but it cannot be done in isolation — `jakarta.persistence` annotations don't exist for Hibernate 4.x at all; you must first jump to a Hibernate release line that ships them (5.x still `javax`, 6.x is `jakarta`-only). Hibernate 6 also drops the old `javassist`-based proxy generation for ByteBuddy and changes default naming strategies, criteria API shape, and some type-coercion edge cases — under CLAUDE.md's rules, any resulting difference in a persisted/queried value is a **Behavioral Change** requiring a `BUSINESS_RULE_MANIFEST.md` entry and owner sign-off, not a "safe refactor."
- **Blocks**: compilation entirely, once the underlying jar is swapped (not today).

### 2. Spring Security stack frozen at `3.2.5.RELEASE` (+ dead `spring-security-oauth2:1.0.0.RELEASE`)
- **Scope**: `spring-security-web`/`spring-security-config` pinned to `3.2.5.RELEASE` (2014) across essentially all 17 modules; `spring-security-oauth2:1.0.0.RELEASE` (a project Spring itself deprecated and folded into Spring Authorization Server years ago) is declared alongside it in the same set of modules.
- **Notable inconsistency**: **LIS and Pharmacy each declare BOTH `4.0.1.RELEASE` and `3.2.5.RELEASE`** for `spring-security-web`/`config` in different dependency blocks within the *same* pom.xml — genuinely ambiguous which version wins for those two modules; every other module is consistently `3.2.5.RELEASE`.
- **Fix difficulty**: requires a real Spring Security major-version jump (3.x → 6.x) coincident with the Spring Framework jump; security/session/CSRF configuration semantics changed multiple times across that span, so this is a behavior-sensitive upgrade, not mechanical.
- **Blocks**: compilation once Spring itself is bumped (security config APIs changed shape across 4→5→6).

### 3. `javax.transaction.Transactional` → `jakarta.transaction.Transactional`
- **Scope**: 695 files import `javax.transaction.*` across every module except AngularSpring (Global: 318, ADT: 81, Pharmacy: 53, BloodBank: 38, Billing: 42, Inventory: 34, LIS: 29, Procurement: 19, DietAndKitchen: 19, AssetManagement: 18, CSSDMaster: 15, OTIMS: 14, LinenLaundry: 7, HIS-WAR: 5, Abdm: 1, Entity: 2).
- **Fix difficulty**: mechanically a package rename plus a dependency swap (`javax.transaction:jta:1.1` → `jakarta.transaction:jakarta.transaction-api`), but because this is the JTA spec annotation (not Spring's own `@Transactional`), the exact propagation/rollback semantics must be re-verified after the swap — and per CLAUDE.md, transaction/rollback behavior changes are billing-critical and require the manifest + owner sign-off, since it touches the same code paths as the "excessive try/catch swallowing errors around billing" problem the project is already trying to fix.
- **Blocks**: compilation, once the JTA API jar is swapped.

### 4. `javax.servlet.*` → `jakarta.servlet.*`, gated by a Tomcat 9→10+ container upgrade, plus web.xml rewrite
- **Scope**: 98 files import `javax.servlet.*` (Global: 45, ADT: 16, LIS: 14, HIS-WAR: 9, Billing: 5, OTIMS: 3, Abdm: 2, Pharmacy: 2, Inventory: 1, Procurement: 1). `javax.servlet:javax.servlet-api` is pinned inconsistently at `3.0.1` (CSSDMaster, Global) vs `3.1.0` (most others); `javax.servlet:servlet-api:3.0-alpha-1` (a distinct, pre-rename artifact coordinate from ~2009) is *also* separately declared in Entity, OTIMS, CSSDMaster, and Global, alongside the newer coordinate.
- **Verified**: `legacy/HIS-WAR/src/main/webapp/WEB-INF/web.xml` and `legacy/AngularSpring/src/main/webapp/WEB-INF/web.xml` both still declare the **Servlet 2.3 DTD** (`"-//Sun Microsystems, Inc.//DTD Web Application 2.3//EN"`, from ~2001) even though the app already runs on Tomcat 9/JDK 17.
- **Fix difficulty**: the servlet API rename is mechanical, but it cannot ship until the runtime container moves from Tomcat 9 (javax) to Tomcat 10+ (jakarta) — a coordinated deploy-target change, not a code-only change. The `web.xml` files additionally need a structural rewrite (DOCTYPE → namespaced XSD schema for `jakarta.servlet`, minimum schema version 5.0/6.0), not a text substitution — the current 2.3 DTD is two spec generations behind even the servlet-api jars actually on the classpath (3.0/3.1).
- **Blocks**: compilation and deployment once the container is swapped.

### 5. Duplicate/legacy JSON and XML-binding libraries riding alongside their modern replacements
- **Scope**: `org.codehaus.jackson:jackson-mapper-asl:1.9.10` (old "Jackson 1.x," last released 2013, a different Maven coordinate entirely from FasterXML Jackson 2.x) is declared in the **same poms** as `com.fasterxml.jackson.core:jackson-{core,databind,annotations}` in 16 modules — i.e. two full generations of the same JSON library are simultaneously on the classpath everywhere. Fasterxml Jackson itself is inconsistent: `2.9.4` (2018) almost everywhere vs `2.12.5` in Abdm only.
- `javax.xml.bind` (JAXB, removed from the JDK in Java 11+) is already worked around by an explicit `jaxb-api` dependency, but split between `2.2.6` (Billing) and `2.1` (Procurement, Inventory, RabbitMQ, LinenLaundry, Pharmacy, AssetManagement) — 14 files actually `import javax.xml.bind.*` (1 in Billing, 1 in Inventory, 12 DTOs under `Global/src/main/java/com/param/billing/global/smartcard/dto/`).
- **Fix difficulty**: mechanical package rename to `jakarta.xml.bind`, but requires adding a runtime JAXB implementation dependency (the API/impl split is stricter under Jakarta), and the old `org.codehaus.jackson` dependency should simply be deleted (it appears to be dead weight riding alongside the real Jackson) — that's a low-risk, low-effort cleanup, but confirming it's truly unused needs the same file-by-file check CLAUDE.md requires for any removal.
- **Blocks**: compilation only for the JAXB half, once the JDK's built-in JAXB assumption is exercised on a newer JDK without the workaround jar present; the Jackson-1.x duplication is a hygiene/CVE-exposure issue, not a compile blocker.

### 6. Springfox/Swagger stack — abandoned, version-inconsistent, and known-incompatible with modern Spring
- **Scope**: `io.springfox:springfox-swagger2` appears at three different versions across modules — `2.4.0` (HIS-WAR), `2.6.1` (OTIMS, CSSDMaster, Global), `2.9.2` (Procurement, Entity, Inventory, LIS, RabbitMQ, LinenLaundry, Pharmacy, AssetManagement). The much older precursor `com.mangofactory:swagger-springmvc:0.8.2` (2013) is *also* declared in 9 of those same poms (Procurement, OTIMS, Inventory, RabbitMQ, LinenLaundry, CSSDMaster, Global, Pharmacy, AssetManagement) alongside its Springfox successor.
- **Fix difficulty**: Springfox has been unmaintained since ~2020 and does not work with Spring 5.3+/newer Spring MVC path-matching — this is a real replace-the-library blocker (typically springdoc-openapi), not a version bump.
- **Blocks**: compilation/bean-wiring once Spring is upgraded past the version Springfox's last release supports.

### 7. Other individually-ancient/EOL dependencies pinned directly in poms
Each of these is declared identically (same exact old version) across many modules, so grouping as one pattern per library:

- `log4j:log4j:1.2.17` (EOL 2015, superseded by Log4j2) — root pom + 15 modules.
- `commons-httpclient:commons-httpclient:3.1` (2007-era, superseded by Apache HttpComponents years ago; HttpComponents `4.5`/`4.5.3` is *also* present in the dependency graph, so two generations of HTTP client coexist) — 16 modules.
- `com.sun.jersey:jersey-server:1.10` (2012, JAX-RS 1.x, EOL) — Procurement, OTIMS, Inventory, RabbitMQ, LinenLaundry, Global, Pharmacy, AssetManagement (8 modules).
- `net.sf.ehcache:ehcache-core:2.6.9` (2013) — Billing, Abdm, BloodBank, HIS-WAR, LIS, ADT, DietAndKitchen (7-8 modules).
- `net.sf.dozer:dozer:5.4.0` (project's last real release, 2016; now archived; does heavy reflection-based field copying) — 17 modules.
- `org.apache.poi:poi:3.11` / `poi-ooxml:3.11` (2014) — Abdm, BloodBank, HIS-WAR, LIS, ADT, DietAndKitchen, Global.
- `net.sf.jasperreports:jasperreports:5.6.0` (2014, current major is 6.x/7.x) — Abdm, BloodBank, HIS-WAR, LIS, ADT, DietAndKitchen.
- `org.codehaus.groovy:groovy:2.4.3` (2015, EOL) — Abdm, BloodBank, HIS-WAR, LIS, ADT, DietAndKitchen.
- `net.sf.flexjson:flexjson:2.0` (effectively abandoned since ~2011) — Procurement, Inventory, LinenLaundry, Pharmacy, AssetManagement.
- `org.json:json:20090211` (2009!) — 16 modules; Billing and Entity additionally pull in `20171018` in a second dependency block within the same pom, so both generations coexist there.
- `commons-lang:commons-lang:2.5` (EOL, superseded by `commons-lang3:3.12.0`, which is *also* declared at the root) — Billing, Abdm, BloodBank, HIS-WAR, LIS, ADT, DietAndKitchen.
- `commons-fileupload:1.3`/`1.3.1` — versions ≤1.3.2 carry a known RCE (CVE-2016-1000031); several modules pin exactly `1.3` or `1.3.1`.
- **Fix difficulty**: mechanical version bumps for most (POI, Jasper, Groovy, Ehcache, HttpClient replacement), each independently low-risk in isolation, but there are ~10 of them and several (Jasper, POI, Dozer) have had breaking API changes across their major-version gaps, so "bump the version number" alone will not compile clean.
- **Blocks**: mix — some are pure deprecation-warning/CVE-exposure issues today (log4j 1.x, commons-fileupload), others will fail to compile once forced to a jakarta-namespace peer library that only ships a newer major (e.g. any Jasper/POI version that itself depends on jakarta-based POI/iText).

### 8. Version-split / inconsistent pinning of the same dependency (build-hygiene issue that compounds every other cluster)
Not a single blocker but a pattern that will make every above cluster harder to fix coherently, since there's no single source of truth to bump:

- `guava`: `20.0` (HIS-WAR) / `29.0-jre` (root, Billing, ADT, and twice in Global) / `32.1.1-jre` (once, also in Global) — Global alone carries three different Guava versions across its own dependency blocks.
- `mysql-connector-java`: `5.1.4` (HIS-WAR, OTIMS, Global) / `5.1.6` (Entity) / `5.1.44` (Billing) — notable because this is a **Postgres application per CLAUDE.md**; this driver is likely vestigial and should be checked for actual use before touching it, not assumed dead.
- `com.google.code.gson:gson`: `2.2.2` / `2.8.0` / `2.8.2` across different modules.
- `ca.uhn.hapi.fhir:hapi-fhir-structures-r4`/`hapi-fhir-validation`: Abdm alone declares both `6.4.3` and `5.7.0` for the same artifacts in different blocks (plus a commented-out `4.2.0` reference) — three FHIR library generations referenced in one file; Global/HIS-WAR are consistently pinned to `5.7.0`.
- **Fix difficulty**: not code-hard, but process-hard — any coordinated version bump needs a full audit of every pom individually since there is no parent `dependencyManagement` enforcing single versions.
- **Blocks**: nothing today (Maven's nearest-wins resolution silently picks one), but is a latent source of `NoSuchMethodError`/`ClassNotFoundException` at runtime once any one copy is bumped without the others.

---

## One-offs (single-module, do not generalize)

- **Abdm** is the only module still pinned to `maven.compiler.source/target = 1.8`, out of step with the rest of the reactor's `17`. Verified this compiles fine as-is today; it's a reactor-consistency issue, not a live break.
- **Root `legacy/pom.xml`** declares no `maven-compiler-plugin`, no `java.version`/`maven.compiler.*` property, and no `pluginManagement` at all for the compiler — every one of the 17 child modules re-declares compiler settings independently, which is how Abdm was able to silently drift to `1.8` unnoticed.
- **LIS and Pharmacy** each declare `spring-security-web`/`spring-security-config` at both `4.0.1.RELEASE` and `3.2.5.RELEASE` in different blocks of the same pom — every other module is consistently `3.2.5.RELEASE`.
- **Global** module's own pom carries three different `com.google.guava:guava` versions (`29.0-jre` twice, `32.1.1-jre` once) in separate dependency blocks.
- **Abdm** module's own pom carries three different HAPI FHIR library generations (`6.4.3`, `5.7.0`, and a commented-out `4.2.0`) for `hapi-fhir-structures-r4`/`hapi-fhir-validation`.
- **Entity and Billing** each declare `org.json:json` at both `20090211` (2009) and `20171018` (2017) simultaneously; every other module has only the 2009 pin.
- **Global** is the sole consumer of `net.oauth.core:oauth:20100527` — an OAuth 1.0a client library whose version string is literally a release date from May 2010, with no subsequent release.
- **Root `legacy/pom.xml`** declares `xml-apis:xml-apis-ext:1.3.04` with no clear consumer identified in this pass — flagged for follow-up confirmation rather than asserted as dead, per the "if in doubt, treat it as in-use" rule.
- No use was found anywhere in `legacy/` of `sun.*` internal APIs, `Thread.stop`/`suspend`/`resume`, `finalize()` overrides, or `SecurityManager` — these commonly-cited JDK-removal blockers are **not present** in this codebase.
- No direct `cglib`/raw ASM imports were found in application code (any bytecode-manipulation exposure is transitive, via Hibernate 4's javassist-based proxies and Dozer, both already covered above).
