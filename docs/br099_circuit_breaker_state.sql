-- BR-PENDING-circuit-breaker-tier0-tier1 (Circuit-breaker/monitoring Tier 0)
-- New, additive table only -- nothing existing is altered or dropped,
-- per CLAUDE.md's Expand-Migrate-Contract discipline.
--
-- A single-row, system-wide (not per-org/per-unit) flag recording whether
-- the circuit breaker described in RULES.md has tripped. "System-wide" is
-- deliberate: a circuit-breaker trip is about the shared Billing/Global
-- codebase's health, not a per-hospital-branch business setting, so this
-- table is intentionally NOT scoped by organization_id/unit_id the way
-- most master tables in this schema are.
--
-- state: 'NORMAL' (default -- no trip, proceed as normal) or 'LEGACY'
-- (breaker tripped -- RULES.md's "flip the feature flag to LEGACY").
-- Given nothing has been rewritten alongside the legacy implementation
-- yet (this whole project has been characterization, not replacement),
-- this flag has exactly one meaningful consumer today: the monitoring
-- scheduler that flips it. It becomes load-bearing for request-routing
-- decisions the day a real behavioral fix or rewrite ships alongside an
-- old path -- see docs/DECISION_NEEDED_CircuitBreakerMonitoringGap.md.

CREATE TABLE public.m_circuit_breaker_state (
    id serial4 NOT NULL,
    state varchar(20) NOT NULL DEFAULT 'NORMAL',
    trip_reason text NULL,
    is_active bool NOT NULL DEFAULT true,
    is_deleted bool NOT NULL DEFAULT false,
    added_by int4 NULL,
    added_date timestamp NULL DEFAULT now(),
    updated_by int4 NULL,
    updated_date timestamp NULL DEFAULT now(),
    CONSTRAINT m_circuit_breaker_state_pkey PRIMARY KEY (id),
    CONSTRAINT m_circuit_breaker_state_state_check CHECK (state IN ('NORMAL', 'LEGACY'))
);

COMMENT ON TABLE public.m_circuit_breaker_state IS
    'RULES.md circuit-breaker Tier 0: single-row, system-wide trip state. See docs/DECISION_NEEDED_CircuitBreakerMonitoringGap.md.';

-- Seed the single row every environment needs. The app assumes exactly
-- one row exists; the DAO always reads/writes id=1.
INSERT INTO public.m_circuit_breaker_state (id, state, trip_reason)
VALUES (1, 'NORMAL', NULL);

-- Keep the sequence consistent with the explicit id=1 insert above, so a
-- future insert (there should never be one, by design) doesn't collide.
SELECT setval('public.m_circuit_breaker_state_id_seq', 1, true);
