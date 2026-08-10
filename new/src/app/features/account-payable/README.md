# accountPayable (580) — classification CORRECTION (NOT built)

DO-NOT-BUILD finding, 2026-08-10. The Billing sweep classified 580 as LIVE
("shares the controller family with 579") — that classification was WRONG,
and the walk must not build 580 as a screen. This file documents the
correction + the evidence (the audit trail; no component was written).

## THE CORRECTION: 580 is DECOY-ROUTED (the SIXTH reachability bucket)

Menu node 580 'Account Payable' (parent 570 Billing -> 531 Unit Master)
reaches a MasterController SHELL, while the REAL account-payable screen is
579 (Account Payable Master, rebuilt as screen 26).

Evidence (all verified):
1. State: app.js:10308 `.state('accountPayable')` — url /accountPayable,
   templateUrl views/master/UnitMaster/billingMaster/account-payable.html,
   controller: "MasterController" (the generic shell), title 'Account
   Payable'. NO resolve gate.
2. Template: account-payable.html (164 lines) has ZERO Angular bindings —
   0 of ng-model/ng-click/ng-bind/ng-repeat anywhere. Static raw HTML
   (selects/inputs with no model) — the shell signature.
3. Controller JS: NO dedicated controller loaded for 580. index.html:886
   loads accountPayableMasterController.js — that is 579's controller.
4. Backend: NO distinct 580 backend. The only account-payable Java
   controller is AccountPayableMasterController (Billing/global, /api/global/
   saveAccountPayable + getAccountPayable + getActiveAccountPayable +
   updateAccountPayableStatus) — 579's, live-verified on screen 26.
5. The REAL screen: 579 Account Payable Master — its own controller JS +
   the backend above — was rebuilt as screen 26 (merged 738e9667) and
   serves the full CRUD.

## WHY THE SWEEP GOT IT WRONG (the lesson)

The sweep's LIVE flag for 580 came from the backend grep matching
`class AccountPayable...` -> AccountPayableMasterController, which is 579's
backend, not 580's. The sweep did NOT verify that 580 has its OWN
controller JS + bindings — the DECOY test. Same failure mode as the OTIMS
sweep's consentType mislabel (corrected by the template-binding test there);
the billing sweep inherited the same gap. The DECOY test (template bindings
+ dedicated controller JS) must run for every LIVE candidate before build —
it is what separated 579 (real: bindings + controller + backend) from 580
(shell: 0 bindings + MasterController + no backend of its own).

## DECISION NEEDED (per the SIXTH bucket: do not default to build)

- Option A: point node 580 at the rebuilt 579 route (a redirect/alias —
  580 'Account Payable' is effectively a duplicate menu entry for the same
  screen the master covers).
- Option B: leave 580 as-is (dead shell in the legacy menu; the rebuild
  simply does not register it) and record the decoy.
- Option C: build 580 as a SECOND-VIEW of 579's module if the owner wants
  the 'Account Payable' entry to be a distinct view — but there is no
  distinct backend or template behind it, so this is the least defensible.
