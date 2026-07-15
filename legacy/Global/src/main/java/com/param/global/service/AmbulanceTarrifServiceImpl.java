package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IAmbulanceTarrifDao;
import com.param.global.dao.IAmbulanceTypeDao;
import com.param.global.dto.AmbulanceTarrifDto;
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

public class AmbulanceTarrifServiceImpl implements IAmbulanceTarrifService, ICommonConstants {
	final static Logger logger = Logger.getLogger(AmbulanceTarrifServiceImpl.class);


	@Autowired
	private IAmbulanceTarrifDao iAmbulanceTarrifDao;
	

	@Override
	public Response saveAmbulanceTarrif(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException {
		try {

			Response deptResponse = iAmbulanceTarrifDao.getAmbulanceTarrifByName(ambulanceTarrifDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				iAmbulanceTarrifDao.saveAmbulanceTarrif(ambulanceTarrifDto);
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
		logger.error("saveAmbulanceTarrif failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAmbulanceTarrifList(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException {
		try {
			return iAmbulanceTarrifDao.getAmbulanceTarrifList(ambulanceTarrifDto);
		} catch (Exception e) {
		logger.error("getAmbulanceTarrifList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAmbulanceTarrifActiveList(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException {
		try {
			return iAmbulanceTarrifDao.getAmbulanceTarrifActiveList(ambulanceTarrifDto);
		} catch (Exception e) {
		logger.error("getAmbulanceTarrifActiveList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSingleAmbulanceTarrif(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException {
		try {
			return iAmbulanceTarrifDao.getSingleAmbulanceTarrif(ambulanceTarrifDto);
		} catch (Exception e) {
		logger.error("getSingleAmbulanceTarrif failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateAmbulanceTarrif(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException {
		try {
			Response deptResponse = iAmbulanceTarrifDao.getAmbulanceTarrifByName(ambulanceTarrifDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {
				iAmbulanceTarrifDao.updateAmbulanceTarrif(ambulanceTarrifDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			}
		} catch (Exception e) {
		logger.error("updateAmbulanceTarrif failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response changeAmbulanceTarrifStatus(AmbulanceTarrifDto ambulanceTarrifDto) throws ApplicationException {
		try {
			Response deptResponse = iAmbulanceTarrifDao.getAmbulanceTarrifByID(ambulanceTarrifDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
				iAmbulanceTarrifDao.changeAmbulanceTarrifStatus(ambulanceTarrifDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
		logger.error("changeAmbulanceTarrifStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	

}
