# Self Registration — screen 48 (module 703)

## Identity

- Legacy state: `selfRegistration` (`legacy/.../config/app.js`) — url `/selfRegistration`, params `{ selfReg: {} }` (+ sibling states `selfRegistrationdemo`, `selfRegistrationhome`)
- Legacy template: `legacy/HIS-WAR/src/main/webapp/views/OPD/self-registration.html` (359 lines, 119 ng-bindings, 7 buttons)
- Legacy controller: `selfRegistrationController` (loaded in index.html)
- Menu: node 703 "Self Registration" (parent 2 Opd) — **NO actionLink in the privilege tree** — DANGLE class (the 477/478/479 shape, owner's BUILD ALL SEVEN decision)
- Backend: ADT reference feeds + `BillingMasterController` (`/api/billing/kiosk/saveAndPay`)
- Rebuild route: `selfRegistration` (moduleId 703)

## Class

DANGLE — state + controller + template + backend all real; the menu node carries
no actionLink; the rebuild registers the route.

## Endpoint truth (live calls, dev token)

| Endpoint | Result |
|---|---|
| `GET /adt/getActiveCountryList/{orgId}` | **200 success, 2 real rows** (India/IN + 1) — path-param orgId + legacy headers |
| `GET /adt/getActiveGenderList` | **200 success, 3 real rows** (Female Updated …) |
| `GET /adt/getActiveIdentificationList` | **200 success, 4 real rows** (Angular Fix …) |
| `GET /adt/getActiveMaritalStatusList` | **200 success, 2 real rows** (Married …) |
| `GET /adt/getActivePrefixList` | **200 success, 1 real row** (Mr.) |
| `POST /adt/getActiveSpecialityList` | 405 on GET — POST-only endpoint, not used by this screen's feeds |
| `POST /api/billing/kiosk/saveAndPay` | kiosk payment save (M-Pesa STK / card) — exists in BillingMasterController; payment-gated, NOT exercised in dev, documented in README |

All GET feeds bind org/unit via the LEGACY `unit_id`/`organization_id` header pair
(the screen-47 header-contract split class) — the service adds the pair on those
calls; the authInterceptor supplies userId/unitId for the token filter.

## Rebuild scope

- Patient self-registration form surface (prefix/name/gender/dob/country/phone/
  identification/marital status) with all reference feeds live
- Reference feeds rendered honestly (real dev rows)
- Payment-enabled save path documented, not exercised (nothing written by this surface)

## Gates

- Preflight: C5–C8 PASS, C1–C4/C9 UNVALIDATED pre-build (files then exist) — final run 9/9
- `ng build`: EXIT 0
- Probe: `tools/probe_self_registration.py` — PASS in real browser
