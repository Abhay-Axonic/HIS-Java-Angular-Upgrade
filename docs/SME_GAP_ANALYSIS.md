# SME-Gap Analysis — August 2026

Produced at the project owner's request as Step A before Step 6 consolidation.
Purpose: make the SME-gap concrete enough to act on — hire someone, or document a risk decision.

---

## Part 1 — The 5 Confirmed SME-Gated Screens

These are the screens explicitly blocked from Angular rebuild (Steps 0-5) pending Step 6b
domain-expert sign-off. All 5 are confirmed **real and buildable** — not broken, not decoys,
not missing backends. The blocker is purely the SME gap.

| # | Screen | Category | Specific Domain Question | Confidence |
|---|--------|----------|--------------------------|------------|
| 1 | **allergyTypeMaster** | Clinical-safety | `allergyTypeId` is a real FK on `AllergyPatientMapper` (patient allergy record, surfaced via coversheet alert icon) AND on `AntibioticTestDoseDto` in the pharmacy nursing-workstation drug-administration module. A misconfigured allergy type could suppress a real allergy alert, directly affecting patient safety. **Question for clinician SME:** Are these 3 fields (code, description, status) the complete allergy-type taxonomy, or are there additional types/severities/contraindication flags the system should surface but currently doesn't? | HIGH — direct patient-safety path confirmed |
| 2 | **doseMaster** | Clinical-safety | `doseId` is the real FK populating dose selection on `MedicationModel`/medication orders. This is the "textbook disguised" case — 2 fields (code + description), simplest-looking screen of all candidates, but its data directly feeds every medication dose dropdown in the system. A missing or misconfigured dose unit could cause a medication error. **Question for clinician SME:** Does the current dose taxonomy match clinical practice? Are there missing dose units/formulations that should exist? | HIGH — direct medication-safety path confirmed |
| 3 | **corporateMasterGlobal** | Billing-accuracy | Stores real `creditLimit`/`creditDays` monetary-cap fields — a genuine billing calculation input. Also structurally complex: 1,565-line multi-tab screen (company master + employee/next-of-kin sub-workflow + grade-mapping matrix + document upload). **Question for finance/billing SME:** Are the credit-limit and credit-days fields correctly calculated and applied? Does the corporate entity's billing configuration interact correctly with the package/reallocation/co-pay logic? | HIGH — direct billing-calculation input confirmed |
| 4 | **routeAdministration** | Hedged domain-adjacency | Real, functional, no direct patient data in the screen itself. Its reference data (drug administration routes) is consumed by `addNewOrderSetMaster` (confirmed clinical order-set builder). No confirmed downstream safety FK linkage beyond `formulationTypeROAMaster` itself. Additionally had a cross-tenant IDOR (BR-139 — now fixed and merged). **Question for clinician SME:** Do the route-of-administration values match clinical practice? Are there routes that should exist but don't, or routes defined that aren't actually used? | MODERATE — domain-adjacent, no direct patient data, but feeds clinical screens |
| 5 | **formulationTypeROAMaster** | Hedged domain-adjacency | Same category as routeAdministration. Real, functional, no direct patient data. Its data feeds the same clinical order-set screen. Also had the same BR-139 IDOR (now fixed). **Question for clinician SME:** Do the formulation-type + ROA mappings match clinical practice for each drug type? | MODERATE — same tier as routeAdministration |

**Important:** No Steps 0-5 work has been performed on any of these 5. No Angular rebuild code
exists. The project has no clinical or billing/finance SME currently available — this is
a confirmed resourcing gap, not a research gap.

---

## Part 2 — The Broader Population

### Screen counts by risk tier (from the authoritative Phase 4 sequencing analysis)

| Tier | Count | Share |
|------|------:|------:|
| Both clinical-safety AND billing | 81 | 5% |
| Clinical-safety only | 521 | 33% |
| Billing only | 602 | 38% |
| Low risk (neither) | 345 | 22% |
| Unclear / needs runtime verification | 18 | 1% |
| **TOTAL** | **1,567** | 100% |

### What "SME-gated" realistically means for this population

**Important correction:** The sub-category estimates below (30-40% simple masters, etc.) are
informed estimates based on the tier breakdown and general HIS system patterns — NOT verified
per-screen classifications. The only screens individually content-read for domain risk are
the 5 confirmed SME-gated screens above and the ~31 screens from the parked-screens re-sweep
(July 31 session). The remaining ~1,199 clinical/billing-tier screens have only been tagged at
the screen-name/module level in the Phase 0 inventory — the same method this project has
repeatedly proven unreliable in both directions (clinical-tagged screens that turned out to be
simple masters, "low-risk" screens that turned out to have real patient data).

To produce a genuine classification, every screen in the clinical/billing tier would need the
same entity-level check applied to routeAdministration and formulationTypeROAMaster tonight:
read the actual backend DAO/entity for FK relationships to patient tables, medication tables,
or billing calculation tables — then categorize based on real data flow, not naming. That's a
~1,199-screen audit. The categories below describe what such an audit would surface, not what
has been verified.

Not all 1,204 clinical/billing-tier screens need the same level of SME review. The 5 confirmed
above represent the highest tier — screens where domain errors directly affect patient safety
or billing accuracy. Most of the remaining 1,199 fall into lower-risk categories within their tier:

| Sub-category | Estimated share | SME need |
|---|---|---|
| **Simple reference-data masters** (e.g., a "blood group list" — clinical tier because it's used in patient records, but the screen itself has 2 fields and zero calculation logic) | ~30-40% (~360-480 screens) | LOW — once the taxonomy is confirmed correct by a single SME pass, these are safe to rebuild mechanically |
| **Workflow screens with patient context** (e.g., nursing notes, vitals entry — clinical tier because they display patient-identifiable data) | ~25-35% (~300-420 screens) | MODERATE — SME needed to confirm the workflow logic is correct, but the rebuild standard remains "parity with legacy," so the audit catches deviations |
| **Billing calculation screens** (e.g., tariff masters, package definitions, discount configurations — billing tier because they feed financial calculations) | ~15-25% (~180-300 screens) | HIGH — every billing calculation screen needs finance SME verification because a rounding error or misconfigured discount/packaging rule directly affects revenue |
| **High-risk clinical decision-support screens** (e.g., medication order sets, allergy management, dose configuration — like the 5 confirmed above) | ~5-10% (~60-120 screens) | CRITICAL — same tier as allergyTypeMaster/doseMaster, needs clinician sign-off per screen |

**Key insight:** The ~345 low-risk screens are now exhausted (confirmed via two independent sweeps).
The remaining ~1,204 clinical/billing screens are not equally risky — but they ALL sit behind
the same SME gate. The distinction the project needs to make is: which of these can proceed
with a one-time "taxonomy review" by an SME (confirm the reference data is correct, then
rebuild mechanically), vs. which need per-screen Step 6b review.

---

## Part 3 — Already-Fixed Security Findings With Open Domain Questions

The 3 Tier-2 findings merged tonight fixed cross-tenant IDOR vulnerabilities (tenant scoping).
They did NOT change business logic. However, because these code paths are now fresh in the
codebase, they're the natural first items for SME review if one becomes available:

| Finding | What was fixed | Open domain question (separate from the now-fixed scoping issue) |
|---------|---------------|------------------------------------------------------------------|
| **DiscountRightsMasterDaoImpl** | `updateDiscountRightsMaster` + `updateStatus` now verify tenant ownership before mutating discount-rights records | **Billing accuracy:** Are the discount-rights calculations correct? Does `updateStatus` correctly handle status transitions (Active → Inactive, etc.)? Are there edge cases where a status change should cascade to related billing records but currently doesn't? This screen touches concession billing — a domain where calculation errors are revenue-impacting. |
| **changePatientClassId** (BillingMasterDaoImpl) | Patient class change now verifies the admission belongs to the caller's tenant before updating | **Billing accuracy:** Does changing a patient's class correctly recalculate all downstream billing? The method updates `adt.t_admission.patient_class_id` — does the system automatically re-rate existing charges? If a patient moves from General to Private mid-stay, are existing charges re-classed correctly? This is the single highest-impact billing decision point among the 3 fixes. |
| **IPreAuthorizationRequestDaoImpl** | `updateAuthorizationRequest`, `cancelNewPreAuthorizationRequest`, `updateIsSplitStatus` now verify tenant ownership before mutating preauth records | **Clinical + billing:** (a) Does the authorization amount validation correctly enforce payer limits? (b) Does the cancel logic correctly adjust `MPreauthMaster` balance amounts (lines 1083-1088 in the DAO)? (c) Does `updateIsSplitStatus` correctly handle admission/encounter reassignment? These are pre-authorization workflows — errors here could mean patients receiving unapproved treatments or payers being billed for unauthorized amounts. |

**None of these 3 domain questions are urgent or blocking** — the security fixes are correct
and complete as-is. These are open questions about the LEGACY business logic that the Angular
rebuild will need to replicate faithfully when these screens are eventually rebuilt. They're
listed here so the first SME review pass can target the highest-impact code paths first.

---

## Part 4 — Recommendation

1. **Immediate (no SME needed):** Clear the Step 6 review backlog (~33 screens already built
   through Step 5, awaiting the owner's own manual click-through). This produces genuinely
   complete, mergeable progress with zero new resources.

2. **Short-term (SME needed):** Recruit ONE clinician (can be part-time/consulting) and ONE
   billing/finance reviewer. Start with the 5 confirmed screens above as a trial run — if
   the process works, expand to the broader population.

3. **Risk-decision alternative:** If no SME can be recruited, make an explicit, documented
   decision about which tiers proceed without SME review and at what risk. Options:
   - Proceed with billing-tier screens on a "rebuild to parity, disclose any discovered
     deviations" basis (lower clinical risk, higher financial risk)
   - Hold all clinical-safety screens until SME is available (higher patient-safety risk
     of proceeding without review)
   - Accept the ~345 low-risk screen pool as fully exhausted and pause new-batch work
     until the SME gap is resolved

4. **SME review order (when available):** Start with the 3 already-fixed DAO paths
   (discount rights, patient class change, preauthorization) since they're fresh and
   their domain questions are well-defined, then proceed to the 5 confirmed screens above,
   then to the broader clinical/billing population tier by tier.
