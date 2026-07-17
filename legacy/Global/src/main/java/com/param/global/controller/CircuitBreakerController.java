package com.param.global.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import org.apache.log4j.Logger;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dto.CircuitBreakerStateDto;
import com.param.global.service.ICircuitBreakerStateService;

/**
 * Tier 0 of the circuit-breaker/monitoring proposal
 * (docs/DECISION_NEEDED_CircuitBreakerMonitoringGap.md). Read/flip the
 * single system-wide breaker flag. Today this has exactly one meaningful
 * consumer: CircuitBreakerMonitorScheduler (Tier 1) auto-flipping it on
 * a trip condition -- nothing yet reads this flag to change routing
 * behavior, since no legacy/new-path split exists yet.
 */
@RestController
@RequestMapping("global/circuitbreaker")
@SuppressWarnings({ "rawtypes", "unchecked" })
public class CircuitBreakerController implements ICommonConstants {

	private static final Logger logger = Logger.getLogger(CircuitBreakerController.class);

	@Autowired
	private ICircuitBreakerStateService iCircuitBreakerStateService;

	@RequestMapping(value = "/state", method = RequestMethod.GET)
	public Response getState() {
		try {
			return iCircuitBreakerStateService.getState();
		} catch (Exception e) {
			logger.error("Error while fetching circuit breaker state", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/flip", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
	public Response flipToLegacy(@RequestBody CircuitBreakerStateDto circuitBreakerStateDto,
			@RequestHeader(value = "userId", required = false) Integer userId) {
		try {
			return iCircuitBreakerStateService.flipToLegacy(circuitBreakerStateDto.getTripReason(), userId);
		} catch (Exception e) {
			logger.error("Error while flipping circuit breaker to LEGACY", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@RequestMapping(value = "/reset", method = RequestMethod.POST)
	public Response resetToNormal(@RequestHeader(value = "userId", required = false) Integer userId) {
		try {
			return iCircuitBreakerStateService.resetToNormal(userId);
		} catch (Exception e) {
			logger.error("Error while resetting circuit breaker to NORMAL", e);
		}
		return new Response(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
