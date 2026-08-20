# Screen endpoint call-sets — the 54 real-template screens

Machine-readable deliverable: `docs/SCREEN_ENDPOINT_CALLSETS.json` (one row per screen per legacy
endpoint call: constant, resolved literal path, legacy controller file:line, purpose, shared-layer
coverage). The table below is the owner's read of the same data.

## The numbers

- **54 screens in scope; 12 are the controller-less shells** (state -> generic `MasterController`, no
  ng-controller in the view — nursing-worklist, nursing-progress-note, admission-rejection-list,
  absconding-patient, bed-under-maintenance, icu-bed-booking-status, pending-nursing-order, vitals,
  nursing-order, pre-authorization-request, company-invoice-creation, franchise-deposit and their
  siblings). They have **no legacy controller to read** and are excluded from the counts.
- **42 screens with quotable controllers carry 277 endpoint calls** (legacy avg 6.6/screen).
- **The shared feed layer covers 100 of those** (~1/3) — the 11 clustered feeds.
- **Per-screen residue after the shared layer: 177 calls** remain, and the distribution is what
  planning needs: most screens sit at 0–5 residue; the tail (admitted-patient-list 26,
  ipd-coversheet 23, bed-allocation 15, pending-admission-discharge 9) is the real cost.

## Closest to complete (fewest non-shared missing calls — the natural first verification
candidates when the backend is free)

- **Zero residue:** the 12 shells (nothing to wire — decide shell-vs-real live), plus
  nursing-order-view, day-care-discharge-summary, discharge-summary, update-discharge-summary,
  credit-note (their calls are all shared-layer feeds).
- **1–2 residue:** cancel-for-discharge, bed-availability, encounter-data, bill-list,
  cancelled-bill-list, debit-note-list, financial-request-approval, the refund trio,
  assign-loyalty-card, day-care-final-discharge, discharged-patients.

## Screens with no grid-load call at all (cannot display anything regardless of other fixes)

nursing-order-view, cancel-for-discharge, day-care-discharge-summary, day-care-final-discharge, discharge-summary, discharged-patients, bed-allocation, death-registration, update-discharge-summary, bill-list, cancelled-bill-list, credit-note, debit-note-list, cost-estimation, refund-against-cr, refund-against-deposit, refund-list, assign-loyalty-card, debit-note

## Purpose legend

GRID = the list load · DROPDOWN = select feed · FILTER = search/filter · ACTION_PRIMARY = the
screen's main save/accept/reject · ACTION_SECONDARY = other actions · LOOKUP = by-id/details lookups.
(Note: the billing family's `*ByFilter` calls are the grid loads — the legacy loads the grid via
the search call.)
