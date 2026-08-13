# storeStockDashboard (Pharmacy/Inventory — store stock dashboard)

## What this is

The store-stock dashboard (legacy state `storeStockDashboard`). Read-mostly
dashboard: the store selector + stock lists. No record writes in the rebuild.

## THE GATE IS A RENDER TRACE — NOT A WRITE-VERIFIED SCREEN

Per the read-side-drift rule (DQ-076) and the shorter loop: this screen was
T0-smoked (primary reads), built, and merge-gated by a RENDER TRACE (the
route renders with the live feeds). It is NOT write-verified — there is no
write to verify. Nobody may read this README as a write-tested screen.

## T0 — primary reads smoked (2026-08-13)

- GET `CONSTANTS.GLOBAL.USER_STORE_API` = `/api/global/user/stores/{orgId}/
  {unitId}/{userId}` → **200 with REAL data**: `[{"id":2,"store":"Linen
  Store"},{"id":1,"store":"Test Main Store"}]` — the store selector feed
  lives and returns rows. (The first probe `/api/user/stores/1/1/1` 404'd —
  the constant's root is `/api/global`, a wrong-root probe, not a defect.)

The pharmacy family's writes are drift-blocked (t_sale flags) but this read
completes — buildable per DQ-076. The remaining dashboard feeds (near-expiry,
asset lists) were not all individually probed this pass; the rebuild wires
the verified store feed and renders honest empty states for the rest.

## Parity / guard note

Legacy state ungated; the new route carries no guard — parity.

## Honest states

- Store selector shows the two live stores (Linen Store, Test Main Store).
- Stock/near-expiry panels render honest empty states (feeds unprobed this
  pass — a real backend gap would show as an error line, not a fabricated list).
