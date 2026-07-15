package com.param.global.service;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.param.exception.ApplicationException;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.INursingStationDao;
import com.param.global.dto.NursingStationMasterDto;

@Service
@SuppressWarnings("rawtypes")
public class NursingStationServiceImpl implements INursingStationService,ICommonConstants {

	private static final Logger logger = Logger.getLogger(NursingStationServiceImpl.class);

	@Autowired
	private INursingStationDao iNursingStationDao;

	
	@Override	
	public Response getListOfNursingStationMaster(NursingStationMasterDto nursingStationMasterDto)
			throws ApplicationException {
		return iNursingStationDao.getListOfNursingStationMaster(nursingStationMasterDto);
	}


	@Override
	public Response getListOfNursingStationMasterByClinicId(
			NursingStationMasterDto nursingStationMasterDto)
			throws ApplicationException {
		return iNursingStationDao.getListOfNursingStationMasterByClinicId(nursingStationMasterDto);
	}



	@Override
	public Response saveNursingStationMaster(NursingStationMasterDto nursingStationMasterDto)
			throws ApplicationException {
		Response res = iNursingStationDao.getNursingStationMasterByName(nursingStationMasterDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iNursingStationDao.saveNursingStationMaster(nursingStationMasterDto);
			}

		} catch (Exception e) {
			logger.error("saveNursingStationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingStationMasterById(int nursingId, int orgId, int unitId) throws ApplicationException {
		try {
			return iNursingStationDao.getNursingStationMasterById(nursingId, orgId, unitId);
		} catch (Exception e) {
			logger.error("getNursingStationMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingStationMasterList(NursingStationMasterDto nursingStationMasterDto)
			throws ApplicationException {
		try {
			return iNursingStationDao.getNursingStationMasterList(nursingStationMasterDto);
		} catch (Exception e) {
			logger.error("getNursingStationMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNursingStationMaster(NursingStationMasterDto nursingStationMasterDto)
			throws ApplicationException {
		Response res = iNursingStationDao.getNursingStationMasterByNameNotById(nursingStationMasterDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iNursingStationDao.updateNursingStationMaster(nursingStationMasterDto);
			}
		} catch (Exception e) {
			logger.error("updateNursingStationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateNursingStationMasterStatus(NursingStationMasterDto nursingStationMasterDto)
			throws ApplicationException {
		try {
			return iNursingStationDao.updateNursingStationMasterStatus(nursingStationMasterDto);
		} catch (Exception e) {
			logger.error("updateNursingStationMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getNursingStationMasterCount(NursingStationMasterDto nursingStationMasterDto)
			throws ApplicationException {
		try {
			return iNursingStationDao.getNursingStationMasterCount(nursingStationMasterDto);
		} catch (Exception e) {
			logger.error("getNursingStationMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}


}
