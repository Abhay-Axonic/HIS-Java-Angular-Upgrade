# DECISIONS_QUEUE — owner review items queued during the campaign cycles

Each entry: what was found, why it needs a decision (not an autonomous fix), the
options, and the recommendation. Nothing here is acted on without the owner.

---

## DQ-2026-08-08-001 — Campaign 1 (drift): 35 phantom TABLES need per-case classification

Found: 35 entity-mapped tables are absent from BOTH the live DB and the
reference DDL, and are referenced by at least one query (DAO raw SQL or entity
named query). Every reference is a runtime failure today (relation does not
exist). Examples:
  - billing.t_cc_avenue_log — UPDATE path in MpesaMasterDaoImpl (CCAvenue is a
    LIVE partner config — BR-012 retracted — this path may be in use)
  - m_razorpay_order_log_details (schema-less @Table) — UPDATE in
    MpesaMasterDaoImpl (razorpay)
  - procurement.t_grn_bonus_batch_expiry — DELETE in GrnBonusBatchExpiryDao
  - opd.ai_appointment_queue — SELECT count in AiAppointmentQueueDaoImpl
  - inventory.t_returnable_bonus_batch_expiry — DELETE
  - emr.t_discharge_sum_tab_columns_print_mapper — UPDATE (ADT discharge)
  Module spread: public 12, billing 4, abdm 3, adt 3, doctor 2, emr 2, opd 2,
  procurement 2, service 2, inventory 1, patient 1, schema-less 1.

Why a decision: there is NO entity-side fix that preserves behavior. Options per
table: (a) the DAO path is dead code -> remove entity + DAO method + query
(BR-183 pattern, safe); (b) the table SHOULD exist -> schema change (NOT
permitted this campaign) -> the defect is real and the endpoint is broken for
all callers; (c) the query is dead -> remove the query only. Each of the 35
needs the caller trace (who calls the DAO method, is it reachable).

Recommendation: batch the caller trace for all 35, split dead vs should-exist,
owner decides the should-exist set (schema add is a Phase-2/3 question).

## DQ-2026-08-08-002 — Campaign 1 (drift): 60 typo-class phantom columns

Found: 60 @Column names across 15 tables carry a trailing-space/typo so the
mapped name ("patient_id ", "code ", "amount ") does not exist in live/DDL,
while the stripped name does. All 60 are query-referenced. Fix = correct the
@Column name — BUT the corrected mapping changes behavior: any Hibernate
WRITE to the field currently fails (column "patient_id " does not exist) and
would start succeeding. That is a behavioral change -> BR entry per item (or
one BR for the class) + owner sign-off, per the constitution.

Recommendation: one BR for the class (60 items, same pattern), verification =
the write endpoints execute for legitimate callers pre/post.

## DQ-2026-08-08-003 — Campaign 1 (drift): 240 true phantom columns fix plan

The 240 query-referenced phantom columns (BR-183 class) are the mechanical
fixable set: remove the phantom field + @Column + every query reference,
entity-side only. Each needs the BR-183-style Rule-10 trace (field -> all
references -> removal). NOT a decision item — executing per the mandate — listed
here for the count.

## DQ-2026-08-08-004 — Batch 28 gating: the six batch services carry 62/73
unswept org-trust methods (Campaign 2 scope). Not a decision — execution order
per the mandate.
