package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IAmbulanceTypeDao;
import com.param.global.dto.AmbulanceMasterDto;
import com.param.global.dto.AmbulanceTarrifDto;
import com.param.global.dto.AmbulanceTypeDto;
import com.param.global.model.AmbulanceType;
import com.param.global.model.CountryMaster;
import com.param.rabbitmq.dto.base.Payload;
import com.param.rabbitmq.dto.base.Request;
import com.param.rabbitmq.service.EmrService.IEmrService;
import com.param.rabbitmq.service.OtimsService.IOtimsService;
import com.param.rabbitmq.util.constants.MasterKeyConstants;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service

public class AmbulanceTypeServiceImpl implements IAmbulanceTypeService, ICommonConstants {
	final static Logger logger = Logger.getLogger(AmbulanceTypeServiceImpl.class);


	@Autowired
	private IAmbulanceTypeDao iAmbulanceTypeDao;
	
	@Override
	public Response addAmbulanceType(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException {
		try {

			Response deptResponse = iAmbulanceTypeDao.getAmbulanceByName(ambulanceTypeDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				iAmbulanceTypeDao.addAmbulanceType(ambulanceTypeDto);
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
		logger.error("addAmbulanceType failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAmbulanceTypeList(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException {
		try {
			return iAmbulanceTypeDao.getAmbulanceTypeList(ambulanceTypeDto);
		} catch (Exception e) {
		logger.error("getAmbulanceTypeList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getAmbulanceTypeActiveList(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException {
		try {
			return iAmbulanceTypeDao.getAmbulanceTypeActiveList(ambulanceTypeDto);
		} catch (Exception e) {
		logger.error("getAmbulanceTypeActiveList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response getSingleAmbulanceType(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException {
		try {
			return iAmbulanceTypeDao.getSingleAmbulanceType(ambulanceTypeDto);
		} catch (Exception e) {
		logger.error("getSingleAmbulanceType failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response updateAmbulanceType(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException {
		try {
			Response deptResponse = iAmbulanceTypeDao.getAmbulanceByName(ambulanceTypeDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				iAmbulanceTypeDao.updateAmbulanceType(ambulanceTypeDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		logger.error("updateAmbulanceType failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}
	
	@Override
	public Response changeAmbulanceStatus(AmbulanceTypeDto ambulanceTypeDto) throws ApplicationException {
		try {
			Response deptResponse = iAmbulanceTypeDao.getAmbulanceByID(ambulanceTypeDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
				iAmbulanceTypeDao.changeAmbulanceStatus(ambulanceTypeDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
		logger.error("changeAmbulanceStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}


}
