# AngularJS 1.5 → Angular 18 Migration Blockers Audit

*Read-only audit, 2026-07-04. Part of the whole-codebase optimization-readiness audit (see also: AUDIT_WholeCodebase_patterns.md, AUDIT_TestCoverage.md, AUDIT_JavaUpgradeBlockers.md, AUDIT_SQLRisk.md, AUDIT_BillingRisk.md). No code was modified. Per CLAUDE.md, version upgrades — including this migration — are the LOWEST priority and happen LAST; this is preparatory research only.*

## Scope: what was found and where

- **`legacy/HIS-WAR/src/main/webapp`** is the one and only real AngularJS 1.5 frontend (`angular.min.js` confirms v1.5.11). It contains 1,606 `.js` files, 1,762 `.html`/`.jsp` template files, ~1,377 images, and 80 CSS files, spread across ~50 `views/` subdirectories (OPD, IPD, Billing, LIS, Pharmacy, ADT, OTIMS, BloodBank, Asset Management, etc.). Total controller-directory JS alone is ~348,000 lines.
- **`legacy/AngularSpring/src/main/webapp`** is effectively an empty shell — only an `index.jsp` that renders "Hello World!" and a `web.xml`. It is not a second working AngularJS app; nothing to migrate there.
- No other module directory under `legacy/` (ADT, Pharmacy, Inventory, Billing, BloodBank, CSSDMaster, DietAndKitchen, LinenLaundry, Procurement, OTIMS, LIS, Abdm, AssetManagement) has its own `webapp`/UI tree — they are Java backend modules only. All AngularJS UI is consolidated in HIS-WAR.
- **CLAUDE.md's claim verified**: there is no `package.json`, `webpack.config`, `gulpfile`, or `Gruntfile` anywhere under `legacy/HIS-WAR`. JS is loaded via 1,352 raw `<script>` tags in `index.html` (plus a near-duplicate, apparently orphaned `index-old.html` with 1,257 tags). This confirms "one-off CSS/HTML per screen, no shared component library" — there are 0 uses of Angular's `.component(` API anywhere in the codebase.
- `legacy-codewhale-attempt/` exists and contains an enormous (600+) file dump of self-issued "certification"/"gate"/"sprint" reports plus an `his-new-frontend` Angular 18 app with its own `package.json`. Per standing project guidance this is confirmed present but was **not** used as a basis for any finding below — flagged out of scope / do-not-trust.

---

## Ranked, Clustered Migration Blockers

### 1. `$scope`-based two-way data binding as the core architecture
**1,224 files / 362,011 occurrences of `$scope`; 1,266 files / 23,803 `ng-model` bindings.**
This is the dominant pattern across nearly the entire codebase (1,224 of ~1,606 JS files touch `$scope` directly). Angular 2+ has no `$scope` equivalent — every controller must be re-expressed as a class with `@Input`/`@Output`/signals or reactive forms. Largest surface area of any blocker found; mechanical on a per-binding basis but requires a full architectural rewrite of state flow, not a find-replace.

### 2. `$rootScope` used as global mutable state
**1,241 files / 33,439 occurrences.**
`$rootScope` is used pervasively as an ambient global store (e.g., `app.js`'s `.run()` block sets `$rootScope.loginpage`, `$rootScope.selectedPageName`, `$rootScope.doctorIdFlagId`, etc., directly consumed elsewhere). This is a harder problem than plain `$scope` binding because it's cross-cutting: any component anywhere may read or mutate it. No direct Angular equivalent — requires introducing services/stores (or NgRx-style state) and tracing every read/write site. Given this is a billing-adjacent app, uncontrolled global state is also a correctness risk independent of the migration.

### 3. Zero component-based structure; everything is controller/directive/factory/service
**1,210 files use `.controller(`; 59 use `.directive(`; 31 use `.factory(`; 30 use `.service(`; 0 use `.component(` anywhere.**
Angular 2+'s base unit is the component; this codebase has never adopted even AngularJS's own `.component()` API, let alone Angular's. Every one of these ~1,330 registrations needs to be re-architected into components, injectable services, or directive-equivalents. This is the most structurally invasive blocker — it's not portable code, it's a different programming model.

### 4. No build tooling / module bundler — raw `<script>` tag loading
**No `package.json`/webpack/gulp/grunt anywhere under HIS-WAR; 1,352 `<script>` tags in `index.html` alone (plus a near-duplicate 1,257-tag `index-old.html`).**
There is no module system (no ES modules, no npm dependency graph). Every JS file — app code and vendor libraries alike — is manually enumerated as a script tag in a monolithic HTML shell. This is a foundational blocker: introducing Angular's CLI/build pipeline, resolving load order, and converting global-script files into importable modules must happen before component work can start.

### 5. jQuery mixed directly into controllers, with multiple redundant jQuery versions vendored
**580 files / 6,532 occurrences of `$(` inside `.js` files that also use Angular.**
Direct DOM querying/manipulation via jQuery is interleaved with Angular controller logic rather than isolated behind directives. Compounding this, at least 7 different jQuery builds are bundled simultaneously (`jquery.min.js`, `jquery-1.4.4.min.js`, `jquery-ui-1.8.6.custom.min.js`, `jquery-new1.min.js`, `jquery-new2.min.js`, `jquery-clockpicker.min.js`, plus separate copies under `css/` and `ckeditor/adapters/`), indicating version drift with no single interop strategy. High risk if ported carelessly since it bypasses Angular's change detection entirely.

### 6. Raw DOM manipulation bypassing Angular's rendering model
**`document.getElementById`: 199 files / 968 occurrences. `.innerHTML`: 95 files / 780 occurrences. `document.querySelector`/`getElementsByTagName`/`getElementsByClassName`: 56 files.**
Smaller file count than jQuery usage but the single riskiest category — these calls read/write the DOM outside Angular's/Zone's awareness entirely, and `.innerHTML` assignment is also an XSS-relevant pattern that must be re-verified (not just re-platformed) during migration.

### 7. Third-party AngularJS-ecosystem libraries with no Angular 18 equivalent
`app.js`'s root module declares dependencies including `ui.router`, `ngAnimate`, `ngStorage`, `ngCookies`, `angular-growl`, `ui.bootstrap`, `ui.mask`, `datatables` (angular-datatables), `ngSanitize`, `angularjs-dropdown-multiselect`(-day), `checklist-model`, `chart.js`/`angular-chart`, `angular.filter`, `barcode`, `katex`, `webcam`, `kendo.directives`, `ui.tree`, `angularjs-datetime-picker`, `ivh.treeview`, `pubnub.angular.service`, `infinite-scroll` — plus separately-loaded `angular-froala`, `angular-translate`, `angular-file-upload`, `angular-messages`. Two are used extensively enough to matter on their own:
- **ui-router**: `$stateProvider`/`ui-sref`/`$state.go` referenced in **1,109 files**.
- **ui-bootstrap**: `uib-*`/`$uibModal` referenced in **666 files**.
All of these are abandoned or AngularJS-only projects; each requires a replacement library (Angular Router, Angular Material/CDK, etc.) or a custom rewrite — none ports mechanically.

### 8. Manual digest-cycle triggers (`$watch`/`$digest`/`$apply`)
**`$watch`: 54 files / 362 occurrences. `$apply`: 48 files / 235 occurrences. `$digest`: 11 files / 108 occurrences.**
Smaller footprint than the above clusters, but these represent deep, explicit coupling to AngularJS's dirty-checking loop. Angular's zone-based (or signal-based, in v18) change detection has no direct equivalent — each site requires understanding *why* the manual trigger was needed (usually to reconcile with a non-Angular async callback, often jQuery or a `setTimeout`) before it can be safely removed, making this a redesign task, not a mechanical port. Given the mandate to prove billing-calculation correctness before any UI change, sites where `$apply`/`$digest` wrap billing-related callbacks deserve special scrutiny.

### 9. God-controller single-file monoliths
A concentrated subset of files account for a disproportionate share of the `$scope` count above:

- `OPDCoverSheetController.js` — 46,669 lines, 15,670 `$scope` references
- `IPDCoverSheetController.js` — 15,319 `$scope` references
- `erNursingCoverSheetController.js` — 12,461
- `nursingCoverSheetController.js` — 11,923
- `dayCareNurshingCoversheetController.js` — 11,669
- `OPDcoversheetNewController.js` — 11,499
- `newOPDBillingController.js` — 6,672
- `IPDChargesController.js` — 5,898

These are not one-off outliers scattered evenly across the codebase — they cluster in the OPD/IPD coversheet and billing-transaction areas, meaning the single highest-effort, highest-risk migration units overlap directly with the billing domain this project is explicitly most protective of.

### 10. Global-scope pollution via top-level `var` URL constants loaded as raw scripts
**2,044 top-level `var NAME = ...` declarations across 11 `config/*.url.config.js` files** (e.g., `global.url.config.js`: 249, `adt.url.config.js`: 563, `lis.url.config.js`: 603, `opd.url.config.js`: 218, `billing.url.config.js`: 114), none wrapped in an IIFE or module — because there's no module system (Blocker 4), these become genuine `window`-scope globals with collision risk. Additionally, **32 controller files hardcode `http://localhost` or `127.0.0.1`** directly, bypassing this config layer entirely.

### 11. Heavy inline logic embedded in `ng-click` template attributes
**26,535 total `ng-click` attributes; 7,153 of them contain multiple statements (`;`) or inline boolean logic (`&&`) directly in the markup** (e.g., `ng-click="getOverBookSlotCheckPopup(info) && checkDateIsBefore(info)!='bg-grey cursor-pointer day-slot-div'"`). This violates Angular's template/class separation and doesn't translate to TypeScript-compiled templates without first extracting this logic into component methods.

### 12. High-volume but mechanically-portable structural directives
**`ng-repeat`: 1,262 files / 9,882 occurrences. `ng-if`: 1,158 files / 19,226 occurrences. `ng-show`: 499 files / 3,066 occurrences.**
These map fairly directly to Angular's `*ngFor`/`*ngIf`/`[hidden]` (or the newer `@for`/`@if` control-flow syntax in Angular 17+). Lowest difficulty/risk of any cluster here, but the sheer volume across ~1,762 template files means the scoping and QA burden is still substantial.

---

## One-offs

- **`AngularSpring` module is a dead shell** — only a "Hello World" `index.jsp` and `web.xml`; not a real second AngularJS app and needs no migration effort, just a decision to retire or repurpose it.
- **Two competing entry points** — `index.html` (1,352 `<script>` tags) and `index-old.html` (1,257 tags) both exist in the same directory; a grep for references to `index-old` found none, suggesting it's orphaned. Should be resolved/removed before building a script-to-module migration map, or it will double-count work.
- **Hardcoded secret-like value on `$rootScope`** in `app.js`: `$rootScope.secretKeyEmr = "YourSecretKey12345";`. Not a migration blocker per se, but adjacent and worth a line — this is global client-side state holding a literal placeholder-looking secret.
- **Multiple redundant/conflicting jQuery builds vendored simultaneously** (7+ distinct copies/versions found, see Cluster 5) — needs a single consolidation decision independent of, but before, any jQuery-interop work in the Angular migration.
- **No other module (ADT, Pharmacy, Inventory, Billing, BloodBank, etc.) has its own frontend** — confirms the entire UI surface to migrate is contained within `HIS-WAR`; no parallel per-module AngularJS apps exist.
- **`legacy-codewhale-attempt/` confirmed present and out of scope** — contains hundreds of self-issued "certification"/"gate"/"sprint" markdown reports and its own `his-new-frontend` (Angular 18, has `package.json`). Per standing guidance this was not treated as ground truth and none of the findings above draw on its contents.
