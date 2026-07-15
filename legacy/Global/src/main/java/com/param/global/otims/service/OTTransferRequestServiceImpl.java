package com.param.global.otims.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.otims.dao.IOTTransferRequestDao;
import com.param.global.otims.dto.OTTransferRequestDto;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({"rawtypes","unchecked"})
@Service
public class OTTransferRequestServiceImpl implements IOTTransferRequestService, ICommonConstants {
	final static Logger logger = Logger.getLogger(OTTransferRequestServiceImpl.class);


	
	@Autowired
	IOTTransferRequestDao iOTTransferRequestDao;


	@Override
	public Response saveOTTransferRequest(OTTransferRequestDto oTTransferRequestDto) throws ApplicationException {
		try {
			Response res =  iOTTransferRequestDao.saveOTTransferRequest(oTTransferRequestDto);
			
			if(res.getObject()!=null && oTTransferRequestDto.getScheduleId()!= null) {
				
				iOTTransferRequestDao.updateScheduleProcedureStatus(oTTransferRequestDto);
			}
			return new Response(SUCCESS, SUCCESS_CODE, SUCCESS_MESSAGE, null, null);
		} catch (Exception e) {
		logger.error("saveOTTransferRequest failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getOTTransferRequest(OTTransferRequestDto oTTransferRequestDto) throws ApplicationException {
		try {
			return iOTTransferRequestDao.getOTTransferRequest(oTTransferRequestDto);
		} catch (Exception e) {
		logger.error("getOTTransferRequest failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response updateOTTransferRequest(OTTransferRequestDto oTTransferRequestDto) throws ApplicationException {
		try {
			return iOTTransferRequestDao.updateOTTransferRequest(oTTransferRequestDto);
		} catch (Exception e) {
		logger.error("updateOTTransferRequest failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response getOTTransferByAdmissionId(OTTransferRequestDto oTTransferRequestDto) throws ApplicationException {
		try {
			return iOTTransferRequestDao.getOTTransferByAdmissionId(oTTransferRequestDto);
		} catch (Exception e) {
		logger.error("getOTTransferByAdmissionId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


	@Override
	public Response initOTTransferRequest(OTTransferRequestDto oTTransferRequestDto) throws ApplicationException {
		try {
			return iOTTransferRequestDao.initOTTransferRequest(oTTransferRequestDto);
		} catch (Exception e) {
		logger.error("initOTTransferRequest failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
}
