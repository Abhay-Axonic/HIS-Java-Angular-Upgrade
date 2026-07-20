# Screen rebuild scaffold

Starting point for rebuilding one legacy screen in modern Angular. Built in Phase 3 from what both Phase 2 pilot batches (10 screens) actually got wrong the first time — see `docs/ANGULAR_REBUILD_PLAYBOOK.md` for the full process this fits into, and `docs/ANGULAR_PENDING_FINDINGS.md` for the underlying bug writeups referenced below.

**This folder is not a real screen and is not routed.** The leading underscore keeps it from being mistaken for one. Do not add a route pointing at it.

## How to use this

1. Complete the playbook's Step 0 and Step 1 first (confirm the screen is real, audit the actual controller/view, confirm the exact `moduleId`/`requireSuperUser` from the live legacy route, capture a golden trace). Do not start from this scaffold before that's done — it exists to remove *boilerplate* risk, not audit risk.
2. Copy this whole folder to `new/src/app/features/<screen-name>/`, and rename every file's `_screen-template` prefix to the real screen's kebab-case name (drop the leading underscore).
3. Rename the exported symbols: `ScreenTemplate` → `<ScreenName>`, `ScreenTemplateService` → `<ScreenName>Service`, `ScreenTemplateRecord` → `<ScreenName>Record`, `emptyScreenTemplateRecord` → `empty<ScreenName>`.
4. Replace every `__legacyModulePath__` endpoint path in `.service.ts` with the real paths from the legacy controller's `GenericService`/`BillingGenericService` calls — read them, don't guess.
5. Replace the record shape in `.models.ts` with the real DTO fields.
6. Rebuild the real form/list/detail markup in `.html`/`.ts`, matching legacy's audited behavior field-by-field — the search bar, table, and pagination in the template file are only wiring demonstrations, not a real finished layout.
7. Register the route in `app.routes.ts`, following the existing pattern exactly:

   ```ts
   {
     path: '<legacyStateUrl>',
     canActivate: [moduleAccessGuard],
     data: { moduleId: <realModuleId> /* , requireSuperUser: true */ },
     loadComponent: () => import('./features/<screen-name>/<screen-name>').then((m) => m.<ScreenName>),
   },
   ```

   Only include `requireSuperUser: true` if the legacy resolve block actually checks it. Only omit `data.moduleId` entirely if the legacy state genuinely has no resolve block at all (confirmed by reading `app.js`/the state's own `.config($stateProvider)` block directly — see `billingGroupMaster`/`periodicityService` as real examples of screens with no module gate). Guessing either way reproduces either finding 13 (too permissive) or a new, invented restriction legacy never had.

8. Delete any part of the template you didn't end up needing (e.g. if the real screen has no status toggle, remove `<app-status-toggle>`/`onToggleStatus`/the `StatusToggle` import entirely rather than leaving dead wiring).

## What's already handled for you and why

| Concern | Where it's handled | Why it's not per-screen anymore |
|---|---|---|
| Module/role access control | `moduleAccessGuard` via route `data`, Step 7 above | Entry 13: legacy's *only* access-control layer is its frontend router; nothing on the backend checks module/role permission. Every route must set this correctly — there's no server-side fallback if it's wrong. |
| Auth headers on every API call | `authInterceptor`, wired once in `app.config.ts` | Nothing to do per screen — every `HttpClient` call automatically gets `Authorization`/`userId`/`unitId`. |
| Status-toggle visual + data bug | `<app-status-toggle>` (`shared/status-toggle/`) | Entry 12: the old pattern (raw `.switch`/`.slider` markup + in-place row mutation) silently looked broken even when the underlying save worked. The component ships correct CSS itself and only emits an event — the host still must use `.update()` with a new object, as shown in `_screen-template.ts`'s `onToggleStatus`. |
| Multi-field-optional search | `buildOptionalParams()` (`shared/search/optional-params.ts`) | Entry 15: sending every search field with the same value, when the backend AND-combines non-empty fields, breaks search for virtually any real input. This helper sends only what's actually filled in — but it does not know whether the real backend query is AND or OR; confirm that against the real DAO during Step 1, don't assume. |

## What this scaffold does not, and cannot, handle for you

- Whether the screen is real (Step 0) — three Phase 2 pilot picks looked like real screens and weren't (dead route, non-functional mockup, stale clone — entries 2, 6, 7).
- The recurring missing-`$state`-DI crash bug pattern in legacy (entries 8, 10) — a legacy-code issue, not something this Angular-side scaffold can fix; just don't port a literal crash into the rebuild (use a defensive session check, the way every rebuilt screen so far already does).
- Anything found only through real interaction testing (Playbook Step 5) or domain review (Step 6) — this scaffold prevents 4 *specific*, already-known bug classes from recurring. It is not a substitute for testing the screen you actually build from it.
