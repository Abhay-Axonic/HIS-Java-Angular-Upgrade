package com.param.global.otims.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
@Transactional
public interface IProcedureOrderStatusService {


	Response getActiveProcedureOrderStatus() throws ApplicationException;

}
