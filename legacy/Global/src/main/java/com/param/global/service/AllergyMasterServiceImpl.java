package com.param.global.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.param.exception.ApplicationException;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IAllergyMasterDao;
import com.param.global.dto.AllergyMasterDto;

@Service
@SuppressWarnings("rawtypes")
public class AllergyMasterServiceImpl implements IAllergyMasterService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(AllergyMasterServiceImpl.class);

	@Autowired
	private IAllergyMasterDao iAllergyMasterDao;

	@Override
	public Response getListOfAllergyMaster(AllergyMasterDto allergyMasterDto) throws ApplicationException {
		return iAllergyMasterDao.getListOfAllergyMaster(allergyMasterDto);
	}

	@Override
	public Response saveAllergyMaster(AllergyMasterDto allergyMasterDto) throws ApplicationException {
		Response res = iAllergyMasterDao.getAllergyMasterListByName(allergyMasterDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iAllergyMasterDao.saveAllergyMaster(allergyMasterDto);
			}
		} catch (Exception e) {
			logger.error("saveAllergyMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAllergyMaster(AllergyMasterDto allergyMasterDto) throws ApplicationException {
		Response res = iAllergyMasterDao.getAllergyMasterListByNameNotById(allergyMasterDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iAllergyMasterDao.updateAllergyMaster(allergyMasterDto);
			}
		} catch (Exception e) {
			logger.error("updateAllergyMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAllergyMasterStatus(AllergyMasterDto allergyMasterDto) throws ApplicationException {
		try {
			return iAllergyMasterDao.updateAllergyMasterStatus(allergyMasterDto);
		} catch (Exception e) {
			logger.error("updateAllergyMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyMasterList(AllergyMasterDto allergyMasterDto) throws ApplicationException {
		try {
			return iAllergyMasterDao.getAllergyMasterList(allergyMasterDto);
		} catch (Exception e) {
			logger.error("getAllergyMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyMasterListCount(AllergyMasterDto allergyMasterDto) throws ApplicationException {
		try {
			return iAllergyMasterDao.getAllergyMasterListCount(allergyMasterDto);
		} catch (Exception e) {
			logger.error("getAllergyMasterListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyMasterListById(Integer allergyId, int orgId, int unitId) throws ApplicationException {
		try {
			return iAllergyMasterDao.getAllergyMasterListById(allergyId, orgId, unitId);
		} catch (Exception e) {
			logger.error("getAllergyMasterListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
