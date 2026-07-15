package com.param.global.org.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.common.dao.IQualificationMasterDao;
import com.param.global.org.common.dto.QualificationMasterDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")

public class QualificationMasterServiceImpl implements IQualificationMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(QualificationMasterServiceImpl.class);

	@Autowired
	IQualificationMasterDao iQualificationMasterDao;

	@Override
	public Response saveQualificationMaster(QualificationMasterDto qualificationMasterDto) throws ApplicationException {
		Response response = iQualificationMasterDao.getQualificationMasterByName(qualificationMasterDto);
		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iQualificationMasterDao.saveQualificationMaster(qualificationMasterDto);
			}
		} catch (Exception e) {
		logger.error("saveQualificationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getQualificationMasterById(int qualificationId, int orgId) throws ApplicationException {
		try {
			return iQualificationMasterDao.getQualificationMasterById(qualificationId, orgId);
		} catch (Exception e) {
		logger.error("getQualificationMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getQualificationMasterList(QualificationMasterDto qualificationMasterDto)
			throws ApplicationException {
		try {
			return iQualificationMasterDao.getQualificationMasterList(qualificationMasterDto);
		} catch (Exception e) {
		logger.error("getQualificationMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateQualificationMaster(QualificationMasterDto qualificationMasterDto)
			throws ApplicationException {
		Response response = iQualificationMasterDao.getQualificationMasterByNameNotById(qualificationMasterDto);
		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iQualificationMasterDao.updateQualificationMaster(qualificationMasterDto);
			}
		} catch (Exception e) {
		logger.error("updateQualificationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateQualificationMasterStatus(QualificationMasterDto qualificationMasterDto)
			throws ApplicationException {
		try {
			return iQualificationMasterDao.updateQualificationMasterStatus(qualificationMasterDto);
		} catch (Exception e) {
		logger.error("updateQualificationMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getQualificationMasterCount(QualificationMasterDto qualificationMasterDto)
			throws ApplicationException {
		try {
			return iQualificationMasterDao.getQualificationMasterCount(qualificationMasterDto);
		} catch (Exception e) {
		logger.error("getQualificationMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getQualificationlist(QualificationMasterDto qualificationMasterDto) throws ApplicationException {
		try {
			return iQualificationMasterDao.getQualificationlist(qualificationMasterDto);
		} catch (Exception e) {
		logger.error("getQualificationlist failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
