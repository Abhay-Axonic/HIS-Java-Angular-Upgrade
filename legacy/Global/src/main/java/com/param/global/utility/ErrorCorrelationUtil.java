package com.param.global.utility;

import java.util.UUID;

/**
 * Shared helper for the correlation-ID error pattern: the real exception is logged
 * server-side tagged with a short ID, and only that ID (plus a generic message) goes
 * back to the client -- never the raw exception message.
 */
public class ErrorCorrelationUtil {

	private ErrorCorrelationUtil() {
	}

	public static String newCorrelationId() {
		return "ERR-" + UUID.randomUUID().toString().substring(0, 8);
	}

	public static String tag(String correlationId, String logMessage) {
		return "[" + correlationId + "] " + logMessage;
	}

	public static String clientMessage(String correlationId) {
		return "Something went wrong. Reference: " + correlationId;
	}
}
