package com.param.global.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.param.exception.ApplicationException;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.IAllergyTypeMasterDao;
import com.param.global.dto.AllergyTypeMasterDto;

@Service
@SuppressWarnings("rawtypes")
public class AllergyTypeMasterServiceImpl implements IAllergyTypeMasterService, ICommonConstants {

	private static final Logger logger = Logger.getLogger(AllergyTypeMasterServiceImpl.class);

	@Autowired
	private IAllergyTypeMasterDao iAllergyTypeMasterDao;

	@Override
	public Response getListOfAllergyTypeMaster(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException {
		return iAllergyTypeMasterDao.getListOfAllergyTypeMaster(allergyTypeMasterDto);
	}

	@Override
	public Response saveAllergyTypeMaster(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException {
		Response res = iAllergyTypeMasterDao.getAllergyTypeMasterListByName(allergyTypeMasterDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				
					Response res1 = iAllergyTypeMasterDao.getAllergyTypeMasterCheckIsDrug(allergyTypeMasterDto);
					
					if (res1.getListObject().size() > 0 && res1.getListObject() != null && allergyTypeMasterDto.getIsGeneric() == 'Y') {
						return new Response<>(ERROR, ALREADY_EXIST_CODE, "Only one entry is allowed to be generic", null, null);
					} else {
					
					return iAllergyTypeMasterDao.saveAllergyTypeMaster(allergyTypeMasterDto);
				}
			}
		} catch (Exception e) {
			logger.error("saveAllergyTypeMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAllergyTypeMaster(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException {
		Response res = iAllergyTypeMasterDao.getAllergyTypeMasterListByNameNotById(allergyTypeMasterDto);
		try {
			if (res.getListObject().size() > 0 && res.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				
				Response res1 = iAllergyTypeMasterDao.getAllergyTypeMasterCheckIsDrugById(allergyTypeMasterDto);
				
				if (res1.getListObject().size() > 0 && res1.getListObject() != null && allergyTypeMasterDto.getIsGeneric() == 'Y') {
					return new Response<>(ERROR, ALREADY_EXIST_CODE, "Only one entry is allowed to be generic", null, null);
				} else {
				
					return iAllergyTypeMasterDao.updateAllergyTypeMaster(allergyTypeMasterDto);
				}
			}
		} catch (Exception e) {
			logger.error("updateAllergyTypeMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAllergyTypeMasterStatus(AllergyTypeMasterDto allergyTypeMasterDto)
			throws ApplicationException {
		try {
			return iAllergyTypeMasterDao.updateAllergyTypeMasterStatus(allergyTypeMasterDto);
		} catch (Exception e) {
		logger.error("updateAllergyTypeMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyTypeMasterList(AllergyTypeMasterDto allergyTypeMasterDto) throws ApplicationException {
		try {
			return iAllergyTypeMasterDao.getAllergyTypeMasterList(allergyTypeMasterDto);
		} catch (Exception e) {
		logger.error("getAllergyTypeMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyTypeMasterListCount(AllergyTypeMasterDto allergyTypeMasterDto)
			throws ApplicationException {
		try {
			return iAllergyTypeMasterDao.getAllergyTypeMasterListCount(allergyTypeMasterDto);
		} catch (Exception e) {
		logger.error("getAllergyTypeMasterListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAllergyTypeMasterListById(Integer allergyTypeId, int orgId, int unitId)
			throws ApplicationException {
		try {
			return iAllergyTypeMasterDao.getAllergyTypeMasterListById(allergyTypeId, orgId, unitId);
		} catch (Exception e) {
		logger.error("getAllergyTypeMasterListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
