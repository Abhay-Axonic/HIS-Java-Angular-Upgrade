package com.param.global.service;

import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface ICircuitBreakerStateService {

	Response getState() throws ApplicationException;

	Response flipToLegacy(String tripReason, Integer updatedBy) throws ApplicationException;

	Response resetToNormal(Integer updatedBy) throws ApplicationException;

}
