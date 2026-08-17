# DUPLICATE-TAB GUARD — mechanism report (2026-08-16, nothing applied)

## The mechanism
The legacy frontend's duplicate-tab guard lives in the SIDEBAR controller
(sidebarController.js:369-408), not a dedicated run block:

1. **Detection — a localStorage flag.** On boot: if `localStorage.getItem('appOpen')`
   is absent, this is the first tab -> `localStorage.setItem('appOpen', 'true')`.
   If it is present, this tab is a duplicate -> the guard may redirect.
2. **The cross-tab listener.** `window.addEventListener('storage', ...)` (:398-408):
   when any other tab sets `appOpen`, the storage event fires in this tab and
   the same duplicate check runs.
3. **The gate — a CONFIG value, not a hard block.** Both redirects are wrapped in
   `if ($scope.checkDuplicateTab == false)` (:389, :402), where
   `checkDuplicateTab = cookieObjectAppConfigSecond.duplicateTab` (:373) — the
   unit's application configuration (UnitApplicationConfigurationDto.duplicateTab,
   backend DTO, per-unit config). **duplicateTab = true already disables the guard
   with zero code change.**
4. **The redirect.** `$state.go("duplicate-tab")` -> the state (app.js:21037),
   the template (views/AppointmentScheduling/duplicate-tab.html:116: "If you are
   seeing this message, it means this page is already open in another tab."),
   and duplicateTabController.

## The sticky-flag detail
`appOpen` is NEVER cleared (no unload/beforeunload handler) — it is set once by
the first tab and persists in localStorage for the origin. Consequence: even
SEQUENTIAL tabs (first tab closed, second opens) hit the duplicate check. The
flag is the trigger; the config is the only thing standing between the user and
the redirect.

## What removing it touches
1. The sidebarController.js:373-408 block — the check + the storage listener.
   The sessionStorage check at :375-378 (`sessionStorage.length === 0 ->
   logout`) is a SEPARATE mechanism (dead-session logout) and must stay.
2. The `appOpen` localStorage flag's lifecycle — irrelevant once the guard is
   off; no unload handler exists to maintain.
3. The duplicate-tab state (app.js:21037) + template + duplicateTabController —
   dead code after removal.
4. The config: the per-unit `duplicateTab` value. The cleanest removal is the
   config (set it true per unit); the code removal is the fallback. The unit's
   app-config write sites (erPatientListController.js:957, mapOfBedERController.
   js:1586) also seed `cookieObjectAppConfigSecond` from the backend.

## Bottom line
Multi-tab is one config flag away: the guard is already gated on
`UnitApplicationConfigurationDto.duplicateTab`. Setting it true per unit removes
the blocker without touching code; removing the code block (sidebarController
:373-408 + the dead state/template/controller) is the permanent variant, keeping
the sessionStorage logout check intact.
