package com.param.global.unit.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.unit.dao.ICabinMasterDao;
import com.param.global.unit.dao.INursingOrderMasterDao;
import com.param.global.unit.dto.NursingOrderMasterDto;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings({"rawtypes"})


public class NursingOrderMasterServiceImpl implements INursingOrderMasterService ,ICommonConstants {

	private static final Logger logger = Logger.getLogger(NursingOrderMasterServiceImpl.class);

	@Autowired
	INursingOrderMasterDao iNursingOrderMasterDao ;

	@Override
	public Response saveNursingOrderMaster(NursingOrderMasterDto nursingOrderMasterDto) throws ApplicationException {
		Response res=iNursingOrderMasterDao.getNursingOrderMasterByName(nursingOrderMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){

				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iNursingOrderMasterDao.saveNursingOrderMaster(nursingOrderMasterDto);
			}

		} catch (Exception e) {
			logger.error("saveNursingOrderMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNursingOrderMaster(NursingOrderMasterDto nursingOrderMasterDto) throws ApplicationException {
		Response res=iNursingOrderMasterDao.getNursingOrderMasterByNameNotId(nursingOrderMasterDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){

				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iNursingOrderMasterDao.updateNursingOrderMaster(nursingOrderMasterDto);
			}

		} catch (Exception e) {
			logger.error("updateNursingOrderMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNursingOrderMasterStatus(NursingOrderMasterDto nursingOrderMasterDto)
			throws ApplicationException {
		try {

			return iNursingOrderMasterDao.updateNursingOrderMasterStatus(nursingOrderMasterDto);

	} catch (Exception e) {
		logger.error("updateNursingOrderMasterStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getNursingOrderMasterById(Integer nursingOrderId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {

			return iNursingOrderMasterDao.getNursingOrderMasterById(nursingOrderId, organizationId, unitId);

	} catch (Exception e) {
		logger.error("getNursingOrderMasterById failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getNursingOrderMasterList(NursingOrderMasterDto nursingOrderMasterDto) throws ApplicationException {
		try {

			return iNursingOrderMasterDao.getNursingOrderMasterList(nursingOrderMasterDto);

	} catch (Exception e) {
		logger.error("getNursingOrderMasterList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getNursingOrderMasterCount(NursingOrderMasterDto nursingOrderMasterDto)
			throws ApplicationException {try {

				return iNursingOrderMasterDao.getNursingOrderMasterCount(nursingOrderMasterDto);

		} catch (Exception e) {
			logger.error("getNursingOrderMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);}

	@Override
	public Response getActiveNursingOrderMaster(Integer organizationId, Integer unitId) throws ApplicationException {
		try {

			return iNursingOrderMasterDao.getActiveNursingOrderMaster(organizationId, unitId);

	} catch (Exception e) {
		logger.error("getActiveNursingOrderMaster failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getNursingOrderMasterByName(NursingOrderMasterDto nursingOrderMasterDto)
			throws ApplicationException {
		try {

			return iNursingOrderMasterDao.getNursingOrderMasterByName(nursingOrderMasterDto);

	} catch (Exception e) {
		logger.error("getNursingOrderMasterByName failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getNursingOrderMasterByNameNotId(NursingOrderMasterDto nursingOrderMasterDto) {
		try {

			return iNursingOrderMasterDao.getNursingOrderMasterByNameNotId(nursingOrderMasterDto);

	} catch (Exception e) {
		logger.error("getNursingOrderMasterByNameNotId failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getNursingOrderMasterListSearch(NursingOrderMasterDto nursingOrderMasterDto)
			throws ApplicationException {
     try {

			return iNursingOrderMasterDao.getNursingOrderMasterListSearch(nursingOrderMasterDto);

	} catch (Exception e) {
		logger.error("getNursingOrderMasterListSearch failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}



}
