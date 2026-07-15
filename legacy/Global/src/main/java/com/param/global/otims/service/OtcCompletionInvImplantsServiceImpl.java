package com.param.global.otims.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dao.IOtcCompletionInvImplantsDao;
import com.param.global.otims.dto.OtcCompletionInvImplantsDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes","unchecked"})
public class OtcCompletionInvImplantsServiceImpl implements IOtcCompletionInvImplantsService, ICommonConstants{
	final static Logger logger = Logger.getLogger(OtcCompletionInvImplantsServiceImpl.class);

	
	@Autowired
	private IOtcCompletionInvImplantsDao iOtcCompletionInvImplantsDao;
	
	
	@Override
	public Response saveInvImplants(OtcCompletionInvImplantsDto otcCompletionInvImplantsDto)
			throws ApplicationException {
		try{
			return iOtcCompletionInvImplantsDao.saveInvImplants(otcCompletionInvImplantsDto);
			
		} catch(Exception e){
		logger.error("saveInvImplants failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}


	@Override
	public Response getImplantIssueAcceptanceData(OtcCompletionInvImplantsDto otcCompletionInvImplantsDto)
			throws ApplicationException {
		try{
			return iOtcCompletionInvImplantsDao.getImplantIssueAcceptanceData(otcCompletionInvImplantsDto);
			
		} catch(Exception e){
		logger.error("getImplantIssueAcceptanceData failed", e);
		}
		return new Response(ERROR, null, null, null,null);
	}
}
