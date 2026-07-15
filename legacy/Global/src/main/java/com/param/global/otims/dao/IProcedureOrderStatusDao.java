package com.param.global.otims.dao;

import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IProcedureOrderStatusDao {
	
	Response getActiveProcedureOrderStatus() throws ApplicationException;
}
