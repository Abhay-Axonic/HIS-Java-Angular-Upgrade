package com.param.batchjobs.scheduler;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.param.entity.model.master.CircuitBreakerState;
import com.param.global.common.Response;
import com.param.global.service.ICircuitBreakerStateService;
import com.param.global.utility.ErrorCorrelationUtil;

/**
 * RULES.md circuit-breaker Tier 1, condition 7 only: "Unhandled exception
 * count in the dashboard goes above 5 in a 24-hour window." Runs every 60
 * seconds, matching RULES.md's monitoring-daemon interval literally.
 *
 * Deliberately narrow: this checks condition 7 only (the one Tier 1 scope
 * covers). Conditions 1/2/4/5 are not implemented -- see
 * docs/DECISION_NEEDED_CircuitBreakerMonitoringGap.md Tiers 2/3. Condition 3
 * (shadow-test failure rate) is covered separately by CI
 * (.github/workflows/reactor-tests.yml), not by this scheduler.
 *
 * Only flips NORMAL -> LEGACY; never auto-resets. Per RULES.md, "the owner
 * should never need to be the one to notice a problem first" -- the flip
 * itself is the alert (a distinctly-tagged CIRCUIT_BREAKER_TRIP log line,
 * logged once by CircuitBreakerStateDaoImpl.flipToLegacy). No external
 * alert channel (email/Slack) is wired yet -- that's an open decision
 * named in the proposal doc, not invented here. Returning to NORMAL is a
 * deliberate owner action via POST /global/circuitbreaker/reset, not
 * something this scheduler decides on its own.
 */
@Service
public class CircuitBreakerMonitorScheduler {

	private static final Logger logger = LoggerFactory.getLogger(CircuitBreakerMonitorScheduler.class);

	private static final int UNHANDLED_EXCEPTION_THRESHOLD_24H = 5;

	@Autowired
	private ICircuitBreakerStateService iCircuitBreakerStateService;

	@Scheduled(fixedRate = 60000)
	@Transactional
	public void checkUnhandledExceptionCount() {
		try {
			int count = ErrorCorrelationUtil.countInLast24Hours();
			if (count <= UNHANDLED_EXCEPTION_THRESHOLD_24H) {
				return;
			}

			Response current = iCircuitBreakerStateService.getState();
			Object stateObj = current != null ? current.getObject() : null;
			if (!(stateObj instanceof CircuitBreakerState currentState)) {
				logger.error("CircuitBreakerMonitorScheduler: could not read current state, skipping check");
				return;
			}
			if (CircuitBreakerState.STATE_LEGACY.equals(currentState.getState())) {
				return;
			}

			String tripReason = "Unhandled exception count in last 24h (" + count
					+ ") exceeded threshold (" + UNHANDLED_EXCEPTION_THRESHOLD_24H + ")";
			iCircuitBreakerStateService.flipToLegacy(tripReason, null);
		} catch (Exception e) {
			logger.error("CircuitBreakerMonitorScheduler.checkUnhandledExceptionCount failed", e);
		}
	}
}
