# Blood Issue (blood bank) — flow map (T0–T6 record)

Rebuilt 2026-08-13 on `rebuild/lis-and-compounding`. Legacy:
`views/master/bloodBankDonor/issue-bloodBag.html` +
`detailsIssueBloodBag` state (app.js:18516 — **ungated**, no resolve; parity
route, the cross-match precedent).

## T0 — the write (DQ-060 → DQ-070, BUILDABLE 2026-08-13)

`POST /global/bloodBank/saveIssueBloodBag` (IssueBloodBagController). The
chain peeled SEVEN layers before the write landed — the compounding-seed
demonstration:
1. the BLOOD_BAG_ISSUE_NUMBER config (screen 149 — the 4th prefix-config
   screen, seeded),
2. the bag + segment (the detail's bagNo/segmentNo),
3. the DONOR (the bag's donor_id — the seed's DON-SEED),
4. the stock's updateLoanInStatus (the detail's bloodBankStockId — the
   seeded SMOKE-BAG1),
5. the master's contract (btoId/issuerId/receivedId/bloodUnitQty/
   bloodBagIssueNo/remarks),
6. the referenced CROSS-MATCH (the service's line 114 loads it by
   crossMatchRegisterId) — supplied by the cross-match flow's OWN output
   (the persisted fixture) — the FIRST time seeding COMPOUNDED,
7. the INSERT — "Added Successfully." + t_issue_blood_bag + details rows
   landed (verified 14/14 fixture-aware; teardown covers the master + the
   details + the fixtures — the TEARDOWN RULE).
The t_issue_blood_bag drift flag (cross_match_register_id) was KILLED by
the write — a DDL-present artifact — the first demotion (SUSPECT 175->174).

## The new screen

A single form: the patient, the issuer/received users, the issue date, the
qty, and the bag (the seeded stock context). Honest state: the screen saves
against the demo context (the seeded bag + donor + cross-match reference);
the number comes from the seeded config.

## Parity note (prominent)

The legacy `issueBloodBag` state is **ungated** (no resolve block — the
blood-bank cluster's pattern). The new route carries no guard — exact parity.
The menu-level module gate is the legacy's only gate; the module id for the
blood-bank issue menu node was not resolved this session.
