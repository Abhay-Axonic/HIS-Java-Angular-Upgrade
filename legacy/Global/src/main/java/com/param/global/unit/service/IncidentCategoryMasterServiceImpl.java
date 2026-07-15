package com.param.global.unit.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dao.IIncidentCategoryMasterDaoImpl;
import com.param.global.unit.dto.IncidentCategoryMasterDto;

import in.param.exception.ApplicationException;


@Service
@SuppressWarnings({"rawtypes","unused"})

public class IncidentCategoryMasterServiceImpl implements IIncidentCategoryMasterServiceImpl, ICommonConstants {

	private static final Logger logger = Logger.getLogger(IncidentCategoryMasterServiceImpl.class);

	@Autowired
	IIncidentCategoryMasterDaoImpl iIncidentCategoryMasterDaoImpl;


	@Override
	public Response saveIncidentCategoryMaster(IncidentCategoryMasterDto incidentCategoryMasterDto)
			throws ApplicationException {
		// TODO Auto-generated method stub
		Response res=iIncidentCategoryMasterDaoImpl.getIncidentCategoryByName(incidentCategoryMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){

				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iIncidentCategoryMasterDaoImpl.saveIncidentCategoryMaster(incidentCategoryMasterDto);
			}

		} catch (Exception e) {
			logger.error("saveIncidentCategoryMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getIncidentCategoryByName(IncidentCategoryMasterDto incidentCategoryMasterDto) throws ApplicationException{
			try {

				return iIncidentCategoryMasterDaoImpl.getIncidentCategoryByName(incidentCategoryMasterDto);

		} catch (Exception e) {
			logger.error("getIncidentCategoryByName failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateIncidentCategoryMaster(IncidentCategoryMasterDto incidentCategoryMasterDto)
			throws ApplicationException {
		Response res=iIncidentCategoryMasterDaoImpl.getIncidentCategoryMasterByNameNotId(incidentCategoryMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){

				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iIncidentCategoryMasterDaoImpl.updateIncidentCategoryMaster(incidentCategoryMasterDto);
			}

		} catch (Exception e) {
			logger.error("updateIncidentCategoryMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateIncidentCategoryMasterStatus(IncidentCategoryMasterDto incidentCategoryMasterDto)
			throws ApplicationException {
		try {

			return iIncidentCategoryMasterDaoImpl.updateIncidentCategoryMasterStatus(incidentCategoryMasterDto);

	} catch (Exception e) {
		logger.error("updateIncidentCategoryMasterStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getIncidentCategoryMasterById(Integer incidentCategoryId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {

			return iIncidentCategoryMasterDaoImpl.getIncidentCategoryMasterById(incidentCategoryId, organizationId, unitId);

	} catch (Exception e) {
		logger.error("getIncidentCategoryMasterById failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getIncidentCategoryMasterList(IncidentCategoryMasterDto incidentCategoryMasterDto)
			throws ApplicationException {
		try {

			return iIncidentCategoryMasterDaoImpl.getIncidentCategoryMasterList(incidentCategoryMasterDto);

	} catch (Exception e) {
		logger.error("getIncidentCategoryMasterList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getIncidentCategoryMasterCount(IncidentCategoryMasterDto incidentCategoryMasterDto)
			throws ApplicationException {
		try {

			return iIncidentCategoryMasterDaoImpl.getIncidentCategoryMasterCount(incidentCategoryMasterDto);

	} catch (Exception e) {
		logger.error("getIncidentCategoryMasterCount failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getActiveIncidentCategoryMaster(Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {

			return iIncidentCategoryMasterDaoImpl.getActiveIncidentCategoryMaster(organizationId, unitId);

	} catch (Exception e) {
		logger.error("getActiveIncidentCategoryMaster failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getIncidentCategoryMasterByNameNotId(IncidentCategoryMasterDto incidentCategoryMasterDto) throws ApplicationException{
			try {

				return iIncidentCategoryMasterDaoImpl.getIncidentCategoryMasterByNameNotId(incidentCategoryMasterDto);

		} catch (Exception e) {
			logger.error("getIncidentCategoryMasterByNameNotId failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
