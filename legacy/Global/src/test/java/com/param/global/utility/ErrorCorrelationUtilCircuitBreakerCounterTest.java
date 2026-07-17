package com.param.global.utility;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

/**
 * Covers the Tier 1 addition to ErrorCorrelationUtil (rolling 24h occurrence
 * counter backing RULES.md circuit-breaker condition 7). See
 * docs/DECISION_NEEDED_CircuitBreakerMonitoringGap.md.
 *
 * Does not test the 24h pruning boundary itself (would require injecting a
 * clock; ErrorCorrelationUtil intentionally has none, matching its existing
 * static-utility shape) -- only that the counter increments per call and
 * starts from whatever the class's current count is, since other tests in
 * the same JVM may also call newCorrelationId() against the same static state.
 */
class ErrorCorrelationUtilCircuitBreakerCounterTest {

    @Test
    void countInLast24Hours_incrementsOncePerNewCorrelationIdCall() {
        int before = ErrorCorrelationUtil.countInLast24Hours();

        ErrorCorrelationUtil.newCorrelationId();
        ErrorCorrelationUtil.newCorrelationId();
        ErrorCorrelationUtil.newCorrelationId();

        assertThat(ErrorCorrelationUtil.countInLast24Hours()).isEqualTo(before + 3);
    }

    @Test
    void newCorrelationId_stillReturnsExpectedFormat() {
        String id = ErrorCorrelationUtil.newCorrelationId();

        assertThat(id).startsWith("ERR-");
        assertThat(id).hasSize(12);
    }
}
