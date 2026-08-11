# Token Display — screen 51 (module 747)

## Identity

- Legacy state: `tokenDisplay` (`legacy/.../config/app.js`) — url `/tokenDisplay` (with a resolve block)
- Legacy template: `legacy/HIS-WAR/src/main/webapp/views/Nursing/token-display.html` (110 lines; corrected-regex check: 2 REAL Angular directives — ng-repeat over `tokenList`, ng-if filter — + 4 `{{}}` bindings; the screen-702 CSS-class artifact does NOT apply: these are genuine directives)
- Legacy controller: `tokenMasterController` (`controller/opd/transection/tokenMasterController.js`, loaded in index.html)
- Menu: node 747 "Token Display" (parent 2 Opd, kiosk-gated `ng-if="isKioskForMenu == 'Y'"`) — **NO actionLink** — DANGLE class (owner's BUILD ALL SEVEN decision)
- Backend: `EncounterController` — `/api/opd/token/getTokenList`, `/api/opd/token/getTokenMsgList`
- Rebuild route: `tokenDisplay` (moduleId 747)

## Class

DANGLE — state + controller + template + backend all real. Menu node 747 without
actionLink (kiosk-gated sidebar entry). Distinct module id from 760 Cabin Display
(checkAccess(747) vs checkAccess(760), separate states/controllers/urls) — NOT the
SECOND-VIEW shape.

## Endpoint truth (live calls, dev token)

| Endpoint | Result |
|---|---|
| `POST /api/opd/token/getTokenList` `{organizationId, unitId, currentDate}` | **200 success, dev-empty list** — requires `currentDate` (YYYY-MM-DD); without it the call 501s (screen-40 class: routes on required fields) |
| `POST /api/opd/token/getTokenMsgList` `{organizationId, unitId, appointmentTypeId}` | **200 success, dev-empty list** — requires `appointmentTypeId` (1), not currentDate |

## Rebuild scope

- Token list table (UHID / counter / token) + broadcast messages, both live
- Honest dev-empty state rendered explicitly
- Refresh button re-fires both feeds

## Gates

- Preflight: C5–C8 PASS pre-build; final 9/9 with files present
- `ng build`: EXIT 0
- Probe: `tools/probe_token_display.py` — PASS in real browser
