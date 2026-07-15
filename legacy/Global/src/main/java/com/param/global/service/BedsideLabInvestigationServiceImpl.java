package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IBedsideLabInvestigationDao;
import com.param.global.dto.BedsideLabInvestigationDto;
import com.param.global.unit.dao.ICabinMasterDao;
import com.param.global.unit.dto.CabinMasterDto;
import com.param.global.unit.service.ICabinMasterService;

import org.apache.log4j.Logger;

import in.param.exception.ApplicationException;
@Service

public class BedsideLabInvestigationServiceImpl implements IBedsideLabInvestigationService ,ICommonConstants {

	private static final Logger logger = Logger.getLogger(BedsideLabInvestigationServiceImpl.class);

	@Autowired
	IBedsideLabInvestigationDao iBedsideLabInvestigationDao;
	
	

	@Override
	public Response saveBedsideLabInvestigation(BedsideLabInvestigationDto bedsideLabInvestigationDto)
			throws ApplicationException {
		Response res=iBedsideLabInvestigationDao.getBedsideLabInvestigationByName(bedsideLabInvestigationDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iBedsideLabInvestigationDao.saveBedsideLabInvestigation(bedsideLabInvestigationDto);
			}

		} catch (Exception e) {
			logger.error("saveBedsideLabInvestigation failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getBedsideLabInvestigationByName(BedsideLabInvestigationDto bedsideLabInvestigationDto) throws ApplicationException{
		try {
			
			return iBedsideLabInvestigationDao.getBedsideLabInvestigationByName(bedsideLabInvestigationDto);

	} catch (Exception e) {
		logger.error("getBedsideLabInvestigationByName failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateBedsideLabInvestigation(BedsideLabInvestigationDto bedsideLabInvestigationDto) throws ApplicationException {
		Response res=iBedsideLabInvestigationDao.getBedsideLabInvestigationNotId(bedsideLabInvestigationDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iBedsideLabInvestigationDao.updateBedsideLabInvestigation(bedsideLabInvestigationDto);
			}

		} catch (Exception e) {
			logger.error("updateBedsideLabInvestigation failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateBedsideLabInvestigationStatus(BedsideLabInvestigationDto bedsideLabInvestigationDto) throws ApplicationException {
		try {
			
			return iBedsideLabInvestigationDao.updateBedsideLabInvestigationStatus(bedsideLabInvestigationDto);

	} catch (Exception e) {
		logger.error("updateBedsideLabInvestigationStatus failed", e);
	}
return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getBedsideLabInvestigationById(Integer bedsideLabId, Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			
			return iBedsideLabInvestigationDao.getBedsideLabInvestigationById(bedsideLabId, organizationId, unitId);

	} catch (Exception e) {
		logger.error("getBedsideLabInvestigationById failed", e);
	}
return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getBedsideLabInvestigationList(BedsideLabInvestigationDto bedsideLabInvestigationDto) throws ApplicationException {
		try {
			
			return iBedsideLabInvestigationDao.getBedsideLabInvestigationList(bedsideLabInvestigationDto);

	} catch (Exception e) {
		logger.error("getBedsideLabInvestigationList failed", e);
	}
return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getBedsideLabInvestigationCount(BedsideLabInvestigationDto bedsideLabInvestigationDto) throws ApplicationException {
		try {
			
			return iBedsideLabInvestigationDao.getBedsideLabInvestigationCount(bedsideLabInvestigationDto);

	} catch (Exception e) {
		logger.error("getBedsideLabInvestigationCount failed", e);
	}
return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getActiveBedsideLabInvestigation(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			
			return iBedsideLabInvestigationDao.getActiveBedsideLabInvestigation(organizationId, unitId);

	} catch (Exception e) {
		logger.error("getActiveBedsideLabInvestigation failed", e);
	}
return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getBedsideLabInvestigationNotId(BedsideLabInvestigationDto bedsideLabInvestigationDto) throws ApplicationException{
		try {
			
			return iBedsideLabInvestigationDao.getBedsideLabInvestigationNotId(bedsideLabInvestigationDto);

	} catch (Exception e) {
		logger.error("getBedsideLabInvestigationNotId failed", e);
	}
return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	
}
