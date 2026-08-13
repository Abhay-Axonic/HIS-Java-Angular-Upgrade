# Nursing Order (ADT) — flow map (T0–T6 record)

Rebuilt 2026-08-13 on `rebuild/lis-and-compounding`. Legacy:
`views/Nursing/nursing-order.html` + `nursingOrder` state (app.js:4719 —
**ungated**, no resolve; parity route, the cross-match precedent).

## T0 — the write (DQ-071, BUILDABLE 2026-08-13)

`POST /api/opd/saveNursingOrderMapper` (NursingOrderMapperController,
class mapping `/api/opd`). The minimal payload returned the masked
"Something went wrong!!" 200-envelope. The mechanism was a TWO-LAYER
contract NPE, not drift:
1. NursingOrderMapperDaoImpl:54 iterates the DTO's
   `nursingOrderMapperDtoList` (null -> the masked NPE),
2. :62-63 read `startDate`/`stopDate` (null -> the same masked NPE — the
   DAO's empty catch swallows both).
With the list + the dates in the payload, the write LANDED: "Added
Successfully." + a t_nursing_order_mapper row (verified, teardown clean).
The table has NO drift flags — the INSERT passed clean. Upgraded
MASKED-PARK -> BUILDABLE — the 6th BUILDABLE in the sweep.

## The new screen

A single form: the patient + one nursing-order line (order, frequency,
instruction, start/stop dates). Honest state: the screen saves a single
nursing-order line; the legacy screen manages lists.

## Parity note (prominent)

The legacy `nursingOrder` state is **ungated** (no resolve block — the
Nursing cluster's pattern). The new route carries no guard — exact parity.
