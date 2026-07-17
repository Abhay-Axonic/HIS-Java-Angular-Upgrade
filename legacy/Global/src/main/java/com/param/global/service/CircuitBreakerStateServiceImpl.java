package com.param.global.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.ICircuitBreakerStateDao;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class CircuitBreakerStateServiceImpl implements ICircuitBreakerStateService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(CircuitBreakerStateServiceImpl.class);

	@Autowired
	ICircuitBreakerStateDao iCircuitBreakerStateDao;

	@Override
	public Response getState() throws ApplicationException {
		try {
			return iCircuitBreakerStateDao.getState();
		} catch (Exception e) {
			logger.error("getState failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response flipToLegacy(String tripReason, Integer updatedBy) throws ApplicationException {
		try {
			return iCircuitBreakerStateDao.flipToLegacy(tripReason, updatedBy);
		} catch (Exception e) {
			logger.error("flipToLegacy failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response resetToNormal(Integer updatedBy) throws ApplicationException {
		try {
			return iCircuitBreakerStateDao.resetToNormal(updatedBy);
		} catch (Exception e) {
			logger.error("resetToNormal failed", e);
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
