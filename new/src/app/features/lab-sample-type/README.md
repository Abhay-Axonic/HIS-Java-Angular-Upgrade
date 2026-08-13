# Lab Sample Type (LIS) — flow map (T0–T6 record)

Rebuilt 2026-08-13 on `rebuild/lis-and-compounding`. Legacy: the LIS
global sample-type screen; the `sampleTypeMediaMappingMaster` state
(app.js:11058) is the media-mapping sibling — the sample-type master's own
state is **ungated** (no resolve); parity route.

## T0 — the write (BUILDABLE 2026-08-13)

`POST /LIS/global/addSample` (the sample-type master writer). The smoke
with orgId, unitId and the epoch dates landed: sample_id=1 in
`lab.m_sample_master` (verified, teardown clean). The seed now persists a
SAMPLE-1 row as the LIS central-receiving's sample-type reference — this
flow's output IS another flow's input (the compounding rule's second
instance after the cross-match → blood-issue).

## The new screen

A single form: the sample code + description + status. Honest state: the
legacy screen manages the full sample-type list; this form saves one row.

## Parity note (prominent)

The legacy sample-type state is **ungated** (the LIS global cluster's
pattern — no resolve block). The new route carries no guard — exact parity.
