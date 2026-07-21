# Hybrid deployment architecture — proposal (not yet implemented)

*Written in response to the owner's request for a session-continuity / shell-parity / retrofit-cost proposal, before Phase 4 batch 4. Nothing in this document has been built — it is a recommendation for review. See `docs/ANGULAR_REBUILD_PLAYBOOK.md`'s Step 7 and the "Phase 7" references throughout `docs/ANGULAR_PENDING_FINDINGS.md` / `docs/PHASE2_DOMAIN_REVIEW_GUIDE.md` for where this question has been flagged as open, but not designed, until now.*

---

## Summary of the recommendation

1. **Session continuity:** same-origin, path-based reverse proxy (legacy at one path, the rebuild at another, same domain) — **not** a separate session-validation endpoint. This is a smaller change than it sounds: production already runs nginx in front of Tomcat, and legacy's real auth token already lives in a plain cookie, not a server session. No new session-sharing mechanism needs to be invented.
2. **Shell parity:** build one new shared Angular layout component (header + sidebar), driven by small route metadata (`title`, `navId`, `moduleId`) rather than legacy's jQuery/`$rootScope` mechanism — reusing the permission model already built for `moduleAccessGuard`. Start with only the nav items for screens actually rebuilt so far; grow it batch by batch. The CSS foundation for this (Bootstrap + a shared stylesheet) is already mostly in place from Phase 2 — it just needs the actual chrome markup, which doesn't exist at all yet.
3. **Retrofit cost:** **low**, on one condition — the shell must live in the Angular app's root component (wrapping `<router-outlet>`), not inside each screen. Under that design, **zero changes to any of the 40 already-built screens' own files.** The only retrofit is adding a `title`/`navId` field to each of the ~40 existing route entries in `app.routes.ts` — a mechanical, few-hours task. This can happen anytime, including after batch 4, without extra rework, **as long as the shell is built as a wrapping layout from day one and not bolted onto individual screens later.**

The one thing worth doing *before* batch 4, not after: deciding this design now, so batch 4's routes get their `title`/`navId` metadata added at build time instead of retrofitted later. That's a small marginal cost per batch now vs. a bigger one-time backfill later — not a blocking decision, just a cheaper time to make it.

---

## Part 1 — Session continuity

### What actually holds the session today

This matters more than it might seem, because it changes the whole shape of the problem:

- Legacy's real bearer token is written to a **cookie**, not `sessionStorage` or a server-side session. `loginController.js:369,397` (`$cookies.putObject('cookieObject', cookieObject, {domain: 'localhost', secure: false})`) sets a cookie containing `authToken`, `userId`, `unitId`, `organizationId`. (There's a *second*, separate copy written to `sessionStorage` at `loginController.js:365` — but that one is a dead end for our purposes: `sessionStorage` doesn't cross tabs/windows even on the same origin, so it can't be the basis for cross-app continuity regardless of deployment topology.)
- Every real backend call in legacy goes through `GenericService.serviceAction` (`resources/assets/js/service/GenericService.js:4-13`), which reads `authToken`/`userId`/`unitId` straight off that cookie and attaches them as `Authorization: Bearer <token>` / `userId` / `unitId` headers. This is not a server session — there's no `session-config` in `web.xml`, no server-set session cookie. The cookie *is* the credential.
- `new/src/app/core/session.service.ts` and `auth.interceptor.ts` already read this exact same cookie, by design (already documented in `session.service.ts`'s own header comment, written during Phase 2). **This part of the architecture is already correct and already built** — the gap isn't the session mechanism itself, it's that the rebuilt app currently never runs anywhere the cookie is visible to it.

Cookies are visible across tabs/windows on the same origin (same scheme+host+port), and across paths on that origin. They are **not** visible across different ports on `localhost` in this dev setup (which is exactly why testing this batch needed a synthetic bypass-proxy instead of a real cookie) — but that's a dev-environment quirk, not a production constraint, since production doesn't run two apps on two different ports of the same bare hostname.

### The three options considered

**Option A — same-origin, path-based reverse proxy (recommended).** Legacy stays at (say) `/`, the rebuilt Angular app is served at a distinct path prefix (say `/app/*`), both behind the same nginx, same domain, same port. The cookie's domain is already the whole host (`domain: 'localhost'` today; the real prod domain in production), so it's visible to both paths automatically — no code change to how the cookie is set or read, on either side.

- **Why this is less new work than it sounds:** production already runs exactly this shape of reverse proxy. `legacy/.platform/nginx/conf.d/elasticbeanstalk/00_application.conf:1-14` already proxies `location /` to Tomcat on `127.0.0.1:8080` and serves `/resources/` as a static alias — Elastic Beanstalk's nginx layer already exists and already does path-based routing. Adding one more `location /app/ { ... }` block pointing at wherever the built Angular static files are served from is an extension of infrastructure that's already there, not a new pattern.
- **Tradeoff:** this only cleanly covers a single-domain deployment. If legacy and the rebuild ever need to live on genuinely different domains (not just different paths of one domain) in some future topology, this stops working and something like Option B becomes necessary. Nothing found in this repo suggests that's the current plan, but it's worth confirming with whoever owns the real prod deployment topology before treating this as final — same caveat the Phase 2 domain-review guide already raised and left to "whoever owns the dev/staging environment."

**Option B — shared session-validation endpoint / token exchange.** A small backend endpoint both apps call to validate/refresh a token, decoupling them from needing to be same-origin at all.

- More resilient to a multi-domain future, but it's new server-side work with no existing scaffolding (no such endpoint exists today), and it doesn't remove the cookie-domain problem so much as add a second mechanism alongside it. Given the token is already a plain JWT-bearing cookie and nothing in the codebase suggests a multi-domain requirement, this looks like solving a problem the deployment doesn't currently have, at real cost.

**Option C — do nothing, keep them as separate apps a user re-logs into.** Technically works today (this is close to how this dev environment already runs), but it directly conflicts with "seamless navigation," which is the whole point of this investigation, so it isn't a real option once shell parity is also being fixed.

**Recommendation: Option A.** It reuses cookie plumbing that's already correct, reuses reverse-proxy infrastructure that already exists in production, and needs no new backend code. Its only real risk is an assumption (single-domain deployment) that should be confirmed, not designed around speculatively.

### One adjacent finding, flagged but out of scope here

`legacy/HIS-WAR/src/main/java/com/param/filter/CORSFilter.java:24-51` reflects whatever `Origin` header a request sends back verbatim, with `Access-Control-Allow-Credentials: true` — an intended allowlist exists in the code (`localhost:8081/8090/8080`, a `*.his.axonichealth.com` staging host) but is commented out and unused (line 43). This is a real, pre-existing security gap (effectively wildcard CORS with credentials), independent of this proposal. Worth its own decision at some point — not fixed here, not part of this recommendation, but Option A (same-origin) has the side benefit of not depending on this weak CORS filter at all for the rebuild's own traffic, whereas a multi-domain approach (Option B) would.

---

## Part 2 — Visual/shell parity

### What's already there, and what's genuinely missing

The CSS foundation is in better shape than "ad hoc per-screen" — `new/angular.json`'s global `styles` array already loads `bootstrap.min.css` and a shared `src/styles.scss` (280 lines, explicitly documented as "shared across every rebuilt screen") for every route, not per-screen. Each of the 40 screens' own `.scss` files are small (3-62 lines), scoped to genuine screen-specific layout, not full stylesheets. The one wrinkle: one screen-specific file (`book-appointment.css`) got added to the *global* styles list rather than scoped to its own component — a small, easily-corrected drift, not a systemic problem.

**What's completely absent: any header or sidebar at all.** `new/src/app/app.html` — the root shell every route renders inside — is just a notification tray, a global loader, the pharmacy store-selection modal, and `<router-outlet />`. No nav, no header, no branding. Every rebuilt screen today is a bare, chromeless page. This confirms the premise of the ask directly.

### What legacy's real shell looks like (so scope is realistic)

- `views/common/top-bar.html`: 4,885 lines. The *live* nav bar itself is a manageable slice (roughly lines 163-317): logo, collapse toggles, a page title bound to `$state.current.title`, a subscription-expiry banner, unit/user display, a notification bell, and ~7 icon-links each gated by a numeric permission check (`checkAccessT(991)`, `checkAccessT(48)`, etc.). The rest of the file is ~15 mostly-commented-out modal definitions accumulated over time — not live chrome to replicate.
- `views/common/new-side-bar.html`: 5,361 lines, ~1,023 `<li>` menu entries, 965 permission checks, 784 distinct module links spanning the entire hospital's module list. This is the expensive part to replicate faithfully in full — and it doesn't need to be replicated in full on day one (see below).
- The nav-highlighting mechanism is jQuery, not Angular state: `sidebar-master.js:42-53` does `$(".sidebar-nav li")` iteration comparing `href` against `location.pathname.substr(14)` — a hardcoded string-offset, DOM-manipulation pattern that a modern Angular shell has no reason to imitate; Angular Router's own active-route state is the natural replacement.
- Permission-gating in the sidebar is the same numeric-module-ID pattern (`checkAccessT(id)`) this rebuild has *already* solved once, for `moduleAccessGuard`/`SessionService.hasModuleAccess()`. A modern sidebar can reuse that exact mechanism instead of re-deriving permission logic from scratch.

### Recommendation: build the shell, reuse existing plumbing, scope the sidebar incrementally

1. **New shared layout component** (e.g. `new/src/app/shell/`), rendering a header (logo, title, user/unit display, logout) and a sidebar, wrapping `<router-outlet>` — either as the new root (`App` component grows to include it) or via an Angular Router layout-route pattern (a parent route with the shell as its component, all screen routes nested as children). Either works; the layout-route pattern is slightly more flexible if a chrome-less route (e.g. a future login page or `/accessDenied`) needs to opt out.
2. **Drive title/active-nav from route data, not legacy's mechanism.** Add `title` and `navId` fields to each route's `data` (already home to `moduleId`/`requireSuperUser`) instead of porting `$state.current.title` binding or the jQuery highlighter. Angular Router's own `routerLinkActive` directive replaces the jQuery pathname-matching entirely.
3. **Reuse `SessionService.hasModuleAccess()` for nav-item visibility**, the same check already gating routes via `moduleAccessGuard` — one permission model, not two.
4. **Build the sidebar's *content* incrementally, scoped to what's actually rebuilt.** Nothing requires all 784 legacy nav links to exist before this ships — start with the ~40 items for screens already built (plus maybe a "Legacy" escape-hatch link/section for everything not yet rebuilt, so the sidebar doesn't look broken), and add entries batch by batch alongside the screens themselves. This turns "replicate a 5,361-line file" into "add a few list entries per batch," which is a fundamentally different, much smaller ask.

---

## Part 3 — Retrofit cost for the 40 already-built screens

**If the shell is built as a wrapping layout** (per the recommendation above — outside and above each feature component, not inside any of them):

- **Zero changes to any of the 40 screens' own `.ts`/`.html`/`.scss` files.** They already render as plain routed components; wrapping them in a parent layout is purely additive at the routing layer.
- **The only retrofit: add `title`/`navId` to each of the ~40 existing entries in `app.routes.ts`.** This is mechanical (look up each screen's legacy page title, same as what's already been done for `moduleId` per screen) and low-risk — it's metadata, not logic, and can't introduce a functional regression in an already-shipped screen.
- Rough sizing: at the pace already demonstrated for other per-screen route metadata (`moduleId` additions took seconds each), 40 entries is comfortably a single sitting, not a multi-day undertaking.

**What would make retrofit expensive, and why the proposal avoids it:** if a shell were instead built *into* each screen (e.g. every component individually importing header/sidebar markup, or a per-screen header partial like legacy's `MasterController`-driven chrome), then yes — all 40 (and every screen after) would need individual rework, and the cost would compound with every future batch. The recommendation above is specifically structured to make that scenario avoidable: keep the shell at the root/layout level, keep screens chrome-agnostic, exactly as they are today.

**Does this need to happen before batch 4?** No, but there's a small, real cost to waiting: every additional batch built without route `title`/`navId` metadata is a few more entries to backfill later. That backfill stays cheap and mechanical regardless of when it happens (it's still just metadata), so this is a "cheaper now than later, but not blocking" situation, not a "must decide before proceeding" one. The one thing that *would* need to be decided before it's too late is the shell's *architectural placement* (wrapping layout vs. per-screen) — and this proposal's recommendation (wrapping layout) is exactly the choice that keeps that decision from ever becoming urgent, since it's compatible with adding the actual chrome markup at any point without touching already-built screens either way.

---

## Open questions for the owner (not resolved by this proposal)

1. **Is a single-domain, path-based deployment actually the real production topology**, or do legacy and the rebuild need to eventually live on different domains? This changes whether Option A (recommended) is sufficient or whether Option B's extra work becomes necessary. Nothing in this repo answers that — it's an infrastructure/ops decision outside the codebase.
2. **Should the CORS filter's wildcard-reflected-origin behavior get its own fix track?** Flagged as a real, pre-existing gap, unrelated to whether Option A or B is chosen, but adjacent enough to be worth a decision at some point.
3. **How much of the legacy sidebar's 784 links needs to exist in the interim, if at all**, versus a "everything else lives in legacy, here's a link back" escape hatch? This proposal assumes the latter (incremental growth) but that's a product decision, not a technical one.
