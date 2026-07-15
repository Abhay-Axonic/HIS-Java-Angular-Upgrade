package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IAmbulanceFacilitiesDao;
import com.param.global.dto.AmbulanceFacilitiesDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service

public class AmbulanceFacilitiesServiceImpl implements IAmbulanceFacilitiesService, ICommonConstants {
	final static Logger logger = Logger.getLogger(AmbulanceFacilitiesServiceImpl.class);


	@Autowired
	private IAmbulanceFacilitiesDao iAmbulanceFacilitiesDao;
	

	@Override
	public Response saveAmbulanceFacilities(AmbulanceFacilitiesDto resourceAllocationDto) throws ApplicationException {
		try {

			Response deptResponse = iAmbulanceFacilitiesDao.getAmbulanceFacilitiesByName(resourceAllocationDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				iAmbulanceFacilitiesDao.saveAmbulanceFacilities(resourceAllocationDto);
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
		logger.error("saveAmbulanceFacilities failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAmbulanceFacilitiesList(AmbulanceFacilitiesDto resourceAllocationDto) throws ApplicationException {
		try {
			return iAmbulanceFacilitiesDao.getAmbulanceFacilitiesList(resourceAllocationDto);
		} catch (Exception e) {
		logger.error("getAmbulanceFacilitiesList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getAmbulanceFacilitiesActiveList(AmbulanceFacilitiesDto resourceAllocationDto) throws ApplicationException {
		try {
			return iAmbulanceFacilitiesDao.getAmbulanceFacilitiesActiveList(resourceAllocationDto);
		} catch (Exception e) {
		logger.error("getAmbulanceFacilitiesActiveList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSingleAmbulanceFacilities(AmbulanceFacilitiesDto resourceAllocationDto) throws ApplicationException {
		try {
			return iAmbulanceFacilitiesDao.getSingleAmbulanceFacilities(resourceAllocationDto);
		} catch (Exception e) {
		logger.error("getSingleAmbulanceFacilities failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateAmbulanceFacilities(AmbulanceFacilitiesDto resourceAllocationDto) throws ApplicationException {
		try {
			/*Response deptResponse = iAmbulanceFacilitiesDao.getAmbulanceFacilitiesByName(resourceAllocationDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {*/
				iAmbulanceFacilitiesDao.updateAmbulanceFacilities(resourceAllocationDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			//}
		} catch (Exception e) {
		logger.error("updateAmbulanceFacilities failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response changeAmbulanceFacilitiesStatus(AmbulanceFacilitiesDto resourceAllocationDto) throws ApplicationException {
		try {
			Response deptResponse = iAmbulanceFacilitiesDao.getAmbulanceFacilitiesByID(resourceAllocationDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
				iAmbulanceFacilitiesDao.changeAmbulanceFacilitiesStatus(resourceAllocationDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
		logger.error("changeAmbulanceFacilitiesStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	

}
