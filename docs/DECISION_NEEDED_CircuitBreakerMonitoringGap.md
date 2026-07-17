# Decision needed: the RULES.md circuit-breaker/monitoring system doesn't exist yet

*Blueprint (docs only, no code, no infrastructure changed). 2026-07-17. Found while closing out Step 5 of the Java 21 upgrade (final full-system verification) — confirmed via a full-codebase search (no `CircuitBreaker`, `FeatureFlag`, or monitoring-daemon class anywhere in `legacy/`) and confirmed independently already flagged once before, in `docs/AUDIT_BillingRisk.md` (2026-07-04): "the modernization's shadow-testing circuit breaker... means several of these must be understood before shadow testing." Proposal only — nothing below should be built without your sign-off.*

---

## 1. What RULES.md actually specifies

`RULES.md`'s "Circuit breaker — emergency stop conditions" section describes two things: a set of 7 trip conditions, and a response mechanism.

**The 7 conditions** (any ONE trips it):
1. Any invoice or billing amount differs from the legacy calculation by more than $0.01.
2. The feature flag service is unavailable for more than 5 minutes.
3. Shadow test failure rate is greater than 0% on any run.
4. Database replication lag exceeds 30 seconds.
5. Database CPU utilization exceeds 80% for more than 2 minutes.
6. A `try/catch` block is removed without a documented alternative error path in the centralized handler.
7. Unhandled exception count in the dashboard goes above 5 in a 24-hour window.

**The response mechanism**: "A monitoring daemon should check these conditions every 60 seconds, flip the feature flag to LEGACY automatically if any condition is violated, and alert the owner. The owner should never need to be the one to notice a problem first."

**What I confirmed does not exist, anywhere in this codebase, as of today:**
- No feature-flag service or class of any kind (`grep -rl "FeatureFlag"` across all 18 modules: zero matches). There is currently nothing *to* flip to LEGACY.
- No monitoring daemon, no scheduled health-check job checking any of the 7 conditions.
- No CI pipeline at all — no `.github/workflows`, no `Jenkinsfile`, no `.gitlab-ci.yml`. The 141-test reactor suite this session built exists and passes, but nothing runs it automatically on a push, a schedule, or a merge. Every test run this whole session has been manually triggered.
- No dashboard, no metrics pipeline (no Prometheus/Grafana/Datadog wiring, no Micrometer), and no DB replica (single local Postgres instance) — conditions 4, 5, and 7 as literally written all assume infrastructure that isn't provisioned yet, separate from any application code question.

This isn't a new finding — `docs/AUDIT_BillingRisk.md` flagged the same gap on 2026-07-04, in the context of billing-math work specifically. This document extends that same flag to the full 7-condition list, now that the shadow-test suite the circuit breaker depends on actually exists.

---

## 2. A realistic minimal-viable version — not a monitoring platform

The 7 conditions split cleanly into three tiers by what they actually require. Building all 7 as literally specified would mean standing up a DB replica, a metrics/dashboard platform, and a feature-flag service before writing a single line of monitoring code — that's a real infrastructure project, not something to start today. But most of the *value* RULES.md is asking for is reachable much sooner, because two of the conditions can build directly on work this session already did.

### Tier 0 — foundational, blocks everything else
**A minimal feature-flag mechanism.** Without this, there's nothing for a monitor to flip, and condition 2 ("is the flag service available") has no subject. The smallest version that satisfies RULES.md's own requirement ("must be flippable without a code deploy") is *not* a vendor service (LaunchDarkly, etc.) — it's a single config row or properties value the app polls on an interval (seconds, not deploy cycles), read by whatever decides LEGACY-vs-new-path at each of the handful of places that distinction will ever matter. Given every billing entry point today *is* legacy (nothing has been replaced yet — this whole session was characterization, not replacement), this flag currently has exactly one meaningful state. It becomes load-bearing the day a real behavioral fix or rewrite ships alongside the old path.

### Tier 1 — cheapest, builds on what this session already built
- **Condition 3 (shadow-test failure rate > 0%).** The 141-test characterization suite already exists and already asserts exact-match, zero-tolerance current behavior (confirmed throughout this session — every characterization test pins precise values, not approximate ones). The only missing piece is *automation*: a CI job that runs `mvn clean test` on every push and treats any failure as the trip condition. This is close to free — the expensive part (writing the tests) is already done; what's missing is wiring, not test-writing.
- **Condition 7 (unhandled exception count > 5 in 24h).** `GlobalExceptionHandler.java`'s centralized catch-all already tags every routed exception with a correlation ID via the existing `ErrorCorrelationUtil` (`legacy/Global/src/main/java/com/param/global/utility/ErrorCorrelationUtil.java` — already generates `ERR-xxxxxxxx` IDs and logs them consistently). A minimal version needs no new dashboard: a small scheduled script (or a Spring `@Scheduled` job inside the app itself) that counts `[ERR-` occurrences in the log over a rolling 24h window and alerts past the threshold. This is log-parsing plus a counter, not an observability platform. Worth noting: **this same tagging pattern is what surfaced the `GlobalExceptionHandler` null-crash bug found during this session's Step 5 smoke pass** (`BR-PENDING-globalexceptionhandler-null-customexception-crash`) — that bug currently makes this specific counting approach *unreliable* for exceptions that hit the generic catch-all branch, since the handler crashes before finishing its own logging in that exact path. Fixing that bug (a separate, already-logged, already-pending decision) is a practical prerequisite for condition 7's counter to be trustworthy, not just a nice-to-have.
- **Condition 6 (try/catch removed without centralized routing).** Not really a *runtime* monitor — it's a code-review-time check. A minimal version is a small script run in CI on every PR diff: flag any removed `catch` block and require the diff to show a corresponding `GlobalExceptionHandler`/routing change nearby, or fail the check. Lightweight, mechanical, no new infrastructure.

### Tier 2 — needs Tier 0, otherwise modest
- **Condition 2 (feature-flag service unavailable > 5 min).** Once Tier 0's flag mechanism exists, this is just a health check on whatever it reads from (a file, a DB row, a config endpoint) — small.
- **Condition 1 (>$0.01 divergence from legacy).** This one is structurally different from the others: it requires *two* implementations running side by side to diverge from each other, and today there's only one (legacy) — nothing has been rewritten yet, by design, per this project's whole sequencing. In the meantime, the existing characterization tests' zero-tolerance exact-match discipline is the de facto substitute. The real version of this condition is the "wave 2" record/replay work already identified as blocked on a real-data-access decision in `docs/DECISION_NEEDED_ShadowTestHarnessApproach.md` (Section 2) — this isn't a new blocker, it's the same one, restated in circuit-breaker terms.

### Tier 3 — real infrastructure/ops work, not application code
- **Conditions 4 and 5 (DB replication lag, DB CPU utilization).** These assume a DB replica and a metrics pipeline that don't exist yet even at the infrastructure level — this environment runs a single local Postgres instance with no replication topology. Typically these come from a cloud provider's built-in database metrics (e.g., RDS/CloudWatch) or a Postgres exporter feeding Prometheus, once a staging/production environment with real replication exists. This is genuinely a different kind of work (infrastructure/ops provisioning) than anything the codebase itself can solve — flagging it as out of scope for an application-level "monitoring daemon," not deprioritizing it.

---

## 3. Rough scope/effort estimate

Ballpark only — no team-velocity data to calibrate against, so treat these as relative sizing, not commitments:

| Tier | Item | Rough size | Why |
|---|---|---|---|
| 0 | Minimal feature flag (single config source + poll) | Small (a few hours to a day) | One new small class/config source, no new infra |
| 1 | CI running the existing 141-test suite on push | Small (a few hours) | Tests already exist; this is pipeline config |
| 1 | Exception-count scheduled counter + alert | Small–Medium (part of a day) | Builds on existing correlation-ID logging; needs an alert channel (email/Slack webhook) decided |
| 1 | try/catch-removal PR check | Small (a few hours) | Mechanical diff-scanning script |
| 2 | Feature-flag-service health check | Trivial once Tier 0 exists | |
| 2 | $0.01 divergence comparison | Not startable yet | Blocked on the same real-data-access decision as `DECISION_NEEDED_ShadowTestHarnessApproach.md` |
| 3 | DB replication lag / CPU monitoring | Out of scope for this doc | Infrastructure provisioning, not app code |

**Everything in Tier 0 + Tier 1 together is genuinely small** — on the order of a few days total, not a project — because it's assembling existing pieces (the test suite, the correlation-ID logging) rather than building new observability infrastructure. Tier 2's feature-flag health check rides along almost free once Tier 0 exists. The one real open item (the $0.01 condition) isn't new scope created by this document — it's the same data-access decision already sitting open from the shadow-test harness planning.

---

## 4. What existing pieces this can build on

Nothing here needs to be invented from scratch — this session's work (and a bit of what predates it) already provides most of the raw material:

- **The 141-test characterization suite** (Entity/Global/Billing/Procurement/Inventory/AssetManagement/BloodBank) — the direct input for condition 3. Zero-tolerance, exact-match by construction already.
- **`ErrorCorrelationUtil`** and `GlobalExceptionHandler.java`'s existing correlation-ID tagging — the direct input for condition 7, once the null-crash bug in that same handler is resolved.
- **`docs/BUSINESS_RULE_MANIFEST.md` / `docs/BR_INDEX.md`** — every known, owner-triaged finding from this entire project lives here already, machine-greppable. A monitoring/alerting layer could cross-reference against this list so it doesn't re-alarm on things already known and accepted (e.g., a characterization test that's *supposed* to pin a known-buggy value shouldn't look like a new circuit-breaker trip if it's already logged as `BR-PENDING`/accepted).
- **`docs/GLOSSARY.json`** — for whatever alert/dashboard copy eventually gets written, the money-term definitions already exist there rather than needing to be invented again for user-facing alert text.

---

## Summary of what needs your decision before anything is built

1. **Scope for a first pass:** Tier 0 + Tier 1 as described (feature flag, CI-wired test suite, exception counter, try/catch PR check) — or a different subset?
2. **Alert channel:** where should "the owner should never need to be the one to notice a problem first" actually land — email, Slack, something else?
3. **Sequencing against the `GlobalExceptionHandler` null-crash bug** (`BR-PENDING-globalexceptionhandler-null-customexception-crash`): fix that first, since it undermines condition 7's counter reliability, or build the counter now and note the gap?
4. **Tier 2/3 timing:** confirm the $0.01 condition stays parked on the existing data-access decision, and Tier 3 (DB replication/CPU) is explicitly out of scope for this round, pending real staging/production infrastructure existing at all.

No code has been written. No infrastructure has been provisioned. This is a proposal only.
