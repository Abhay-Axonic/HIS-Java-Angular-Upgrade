package com.param.global.utility;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Shared helper for the correlation-ID error pattern: the real exception is logged
 * server-side tagged with a short ID, and only that ID (plus a generic message) goes
 * back to the client -- never the raw exception message.
 *
 * Also backs RULES.md's circuit-breaker condition 7 ("unhandled exception count
 * in the dashboard goes above 5 in a 24-hour window") -- see
 * docs/DECISION_NEEDED_CircuitBreakerMonitoringGap.md, Tier 1. Every call to
 * newCorrelationId() records an occurrence; countInLast24Hours() is polled by
 * CircuitBreakerMonitorScheduler. Correlation IDs are generated before any
 * downstream handler logic runs, so this counts each original error event even
 * in cases where the handler itself later fails while building the response
 * (e.g. BR-PENDING-globalexceptionhandler-null-customexception-crash).
 */
public class ErrorCorrelationUtil {

	private static final ConcurrentLinkedDeque<Instant> OCCURRENCES = new ConcurrentLinkedDeque<>();

	private ErrorCorrelationUtil() {
	}

	public static String newCorrelationId() {
		OCCURRENCES.addLast(Instant.now());
		pruneOlderThan24Hours();
		return "ERR-" + UUID.randomUUID().toString().substring(0, 8);
	}

	public static String tag(String correlationId, String logMessage) {
		return "[" + correlationId + "] " + logMessage;
	}

	public static String clientMessage(String correlationId) {
		return "Something went wrong. Reference: " + correlationId;
	}

	/**
	 * Count of newCorrelationId() calls in the trailing 24 hours, pruning
	 * anything older as a side effect.
	 */
	public static int countInLast24Hours() {
		pruneOlderThan24Hours();
		return OCCURRENCES.size();
	}

	private static void pruneOlderThan24Hours() {
		Instant cutoff = Instant.now().minus(24, ChronoUnit.HOURS);
		Instant oldest;
		while ((oldest = OCCURRENCES.peekFirst()) != null && oldest.isBefore(cutoff)) {
			OCCURRENCES.pollFirst();
		}
	}
}
