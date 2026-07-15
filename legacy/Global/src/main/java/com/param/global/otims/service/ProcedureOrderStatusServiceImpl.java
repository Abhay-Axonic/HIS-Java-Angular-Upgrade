package com.param.global.otims.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dao.IProcedureOrderStatusDao;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings("rawtypes")
@Service
public class ProcedureOrderStatusServiceImpl implements IProcedureOrderStatusService,ICommonConstants {
	final static Logger logger = Logger.getLogger(ProcedureOrderStatusServiceImpl.class);


	@Autowired 
	IProcedureOrderStatusDao iProcedureOrderStatusDao;
	
	@Override
	public Response getActiveProcedureOrderStatus() throws ApplicationException {
		try{
			return iProcedureOrderStatusDao.getActiveProcedureOrderStatus();
		}catch(Exception e){
		logger.error("getActiveProcedureOrderStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
