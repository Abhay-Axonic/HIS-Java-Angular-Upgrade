# MENU-GENERATION FEASIBILITY — 2026-08-14 (scope decision, NOT built)

Question: what would it take to drive the rebuild's menu from the SAME module
data the legacy uses, so every rebuilt screen appears where a user expects it?

## Q1 — is there a menu table in the database?

**NO.** The legacy menu tree exists ONLY in the hardcoded template
`views/common/new-side-bar.html` (661 menu items, 122 collapsible sections, 4
top headings — HIMS / ERP / Ancillaries / Master). The database holds only the
ACCESS list: `privilege.privilege.modules` — a JSON blob per role of
`{id, name, moduleType}` (the demo role has 1 row). `checkAccess(id)` filters
that list (`id == id && moduleType == 1`). No table carries groups, order,
labels, or item-to-module mappings. The grouping + ordering + per-item PNG
icons live nowhere but the template.

## Q2 — can the menu be generated from the route table + the legacy grouping?

**YES — with the ROUTE PATH as the anchor key** (not the moduleId alone):

- **241 of 382 routes' paths match a legacy item's href** — those place
  themselves in the legacy's section/heading/order automatically.
- The id spaces ALIGN for anchored items (proven: the legacy's Insurance
  Master item is `checkAccess(571)` href `#/insuranceMaster`; the rebuild's
  route is `moduleId: 571` path `insuranceMaster`) — so the route's existing
  `data.moduleId` drives the permission gate unchanged
  (`SessionService.hasModuleAccess`, the same mechanism as today), and the
  legacy's per-item gate id is the same id.
- The mechanism: (1) a one-time parse of the legacy template → a
  `href → {section, heading, order, gateModule, label, icon}` map; (2) merge
  with the route table (`path → moduleId/title/component`); (3) emit the
  generated nav (a checked-in data file or a build step) replacing the
  hand-curated `SHELL_NAV_SECTIONS`. The permission filter stays.
- The moduleId alone is NOT a sufficient key (658 unique gate ids across 661
  items — one module gates many items); the path is the reliable key, with a
  small alias table for the renamed set (below).

## Q3 — what breaks

1. **The 92 routes with no `data.moduleId`** (incl. billingGroupMaster,
   periodicityService — deliberately un-gated per the routes-file comment):
   a moduleId-gated menu would HIDE them. They need either the legacy gate id
   assigned (a data task — the legacy DOES gate their equivalents) or an
   explicit always-visible rule.
2. **The second-view states** — viewAssetScheduler/assetScheduler,
   detailScheduleWorklist/scheduleWorklist, the case variants
   (frequencyMaster/frequencymaster, MISReports/misreports): one route anchors,
   the other does not. The generator must pick the canonical route per legacy
   item or the menu duplicates.
3. **The renamed paths** (~141 routes have no legacy anchor; minus the
   infrastructure/public/dupe sets, most of the rest are rebuilt screens whose
   route paths differ from the legacy hrefs — organizationMaster, the
   corrective-maintenance family, pharman, bookView…): they need an alias
   table (path → legacy item). 26 of the 141 have a moduleId in the legacy
   gate set, but the module is ambiguous when it gates many items — the path
   alias is the reliable key.
4. **The rebuild-only screens** (no legacy counterpart at all): no legacy
   position — a scope decision (a "New/Other" section, the moduleId's nearest
   section, or route-reachable-only).
5. **The public/param routes** (5: display/:orgId…, webAppointmentBooking,
   the feedback pair, commonMaster/:url): not legacy sidebar items — exclude.
6. **The infrastructure routes** (accessDenied, home, the dashboards):
   not legacy menu items (home is the legacy's header button, not a sidebar
   item) — exclude.
7. **The legacy groups never rebuilt** (the sections whose items are all
   unanchored — NABH, Crash Cart, HouseKeeping, LineL…): the generator emits
   only sections with ≥1 rebuilt anchor — the empty sections drop, which is
   correct (nothing to navigate to). The rebuild's sidebar becomes the
   legacy's collapsible section hierarchy (122 groups) instead of the current
   flat curated sections — a real shell change, which is the parity work
   anyway.

## What it costs

- The extraction + generation tooling (one script + a checked-in generated
  nav file, or a build-time step).
- The **141-route placement triage** (the real work): classify infra/public/
  dupes (~30) + write the alias table for the renamed set (~60-80 one-time
  mappings) + decide the rebuild-only screens.
- The **92 no-moduleId routes**: assign the legacy gate ids (a data task on
  app.routes.ts).
- The shell UI change: the collapsible section hierarchy.
- Out of scope by default: the per-item PNG icons (the legacy's images exist
  and are servable — a choice) and the legacy's typeahead search (needs the
  action-list data source, separate from the menu generation).

## The decision for the owner

The menu is generatable from the route table + the legacy template, with the
path as the anchor. The cost is bounded: the generation script + the alias
table + the moduleId assignments + the shell's section-hierarchy change. The
open choices: the rebuild-only screens' placement, the icons, the search, and
whether the no-moduleId routes get ids or an always-visible rule.
