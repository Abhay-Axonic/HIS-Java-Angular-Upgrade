# The three written screens — the resurrection decision (prepared 2026-08-08)

The three Track A screens written this run are rebuilds of legacy screens a
user cannot open (the owner's build is held). Shipping them resurrects
features that may have been deliberately disabled. One page per screen: the
legacy evidence, the backend, any trace, and a per-screen read. No
recommendation on shipping — the owner decides.

---

## 1. add-new-drug-recall (branch rebuild/add-new-drug-recall, commit 717b1502)

a) Legacy evidence: app.js — **ABSENT** (zero occurrences of the state name or
its template across the whole file — verbatim grep, 0 hits). Menu node —
**PRESENT**: id 139 " Drug Recall", parent 123 (Store) -> 121 (Inventory),
type 1. Template and controller — **on disk**: add-new-drug-recall.html
(1,166 lines) + addNewDrugRecallController.js (28 KB).
b) Backend: DrugRecallController's real handlers for save/update/detail/
search/reason-dropdown/approve/cancel (all verified). Table t_drug_recall
exists in the inventory schema — **0 rows**. One dead endpoint in the screen's
own path (RECALL_DRUG /stores/recall/drug/search — no controller, Q15).
c) Traces: no BR, QA-finding, manifest, or history entry references the
feature. The shape is: fully wired backend + empty table + menu node + never
registered route.
d) My read: **staged, likely intended** — the menu node and the fully-wired
backend say the feature was meant to exist; the absent route looks like an
incomplete last step rather than a deliberate removal (nothing was
commented out — there is no active act to explain). Resurrection risk: low.
Stated as a read; not a recommendation.

---

## 2. details-implant-opening-balance (branch rebuild/details-implant-opening-balance, commit 2f51a794)

a) Legacy evidence: app.js — **COMMENTED** (both implant states sit inside a
wholesale `/* ... */` region alongside KitTypeMaster and others; the blame
attributes the comment to the baseline commit ^96b75c49 — it predates this
repo). Menu node — **ABSENT** (no "Implant Opening Balance" node in the
registry; the closest is 154 " Opening balance" under Stock(153), the general
stock-opening module — not a confirmed match). Template and controller — **on
disk**: details-implant-opening-balance.html (308 lines) +
addNewImplantOpeningBalanceController.js (29 KB).
b) Backend: OpeningBalanceImplantController's real handlers (list/search/
save/getById/update/approve/cancel — all verified). Tables
t_opening_balance_implant + t_opening_balance_implant_detail exist in the
inventory schema — **0 rows each**.
c) Traces: no BR, QA-finding, manifest, or history entry. The commenting is
an active act (unlike screen 1) but unrecorded and pre-baseline.
d) My read: **staged, then actively gated off — risky** — the wholesale
region-comment is a deliberate-ish act, the backend stayed fully wired, and
no decision was recorded. The resurrection risk is higher than screen 1's:
the commenting looks like a gate, and its reason is unknown. Stated as a
read; not a recommendation.

---

## 3. add-new-implant-opening-balance (branch rebuild/add-new-implant-opening-balance, commit 3393320b)

a) Legacy evidence: identical to screen 2 — app.js **COMMENTED** (the same
block, pre-baseline), menu node **ABSENT**, template + controller **on disk**
(add-new-implant-opening-balance.html, 916 lines; the same shared
controller).
b) Backend: identical — OpeningBalanceImplantController's real handlers; the
two implant tables exist, **0 rows**.
c) Traces: identical — nothing recorded; the commenting's an active,
unexplained, pre-baseline act.
d) My read: **same as screen 2 — staged, then gated off — risky**, for the
same reasons, stated as a read. The two implant screens are one feature and
one decision, not two.
