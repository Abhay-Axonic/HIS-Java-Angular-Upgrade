package com.param.global.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dao.ICabinMasterDao;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.dto.CoversheetAccessMasterDto;
import com.param.global.unit.dto.UHIDMergeDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings({"rawtypes"})

public class UHIDMergeRequestServiceImpl implements IUHIDMergeRequestService, ICommonConstants{
	final static Logger logger = Logger.getLogger(UHIDMergeRequestServiceImpl.class);


	@Autowired
	IUHIDMergeRequestDao iUHIDMergeRequestDao;
	
	@Override
	public Response getCabinMasterList(CabinMasterDto cabinMasterDto) throws ApplicationException {
		return null;
	}
	

	@Override
	public Response getUserUHIDMergeList(UHIDMergeDto uHIDMergeDto) throws ApplicationException{
			try {
				return iUHIDMergeRequestDao.getUserUHIDMergeList(uHIDMergeDto);
				
		 } catch (Exception  e)  {
		 logger.error("getUserUHIDMergeList failed", e);
		 }
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveUHIDMergeMaster(UHIDMergeDto uHIDMergeDto) {
		try {
			return iUHIDMergeRequestDao.saveUHIDMergeMaster(uHIDMergeDto);
		} catch (Exception e){
		logger.error("saveUHIDMergeMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}
