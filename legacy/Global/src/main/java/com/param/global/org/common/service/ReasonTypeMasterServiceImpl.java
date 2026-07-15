package com.param.global.org.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.common.dao.IReasonTypeMasterDao;
import com.param.global.org.common.dto.ReasonTypeMasterDto;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class ReasonTypeMasterServiceImpl implements IReasonTypeMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(ReasonTypeMasterServiceImpl.class);


	@Autowired
	IReasonTypeMasterDao iReasonTypeMasterDao;

	@Override
	public Response getReasonTypeMasterById(int reasonTypeId, int orgId) {

		try {
			return iReasonTypeMasterDao.getReasonTypeMasterById(reasonTypeId, orgId);
		} catch (Exception e) {
		logger.error("getReasonTypeMasterById failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReasonTypeMasterList(ReasonTypeMasterDto reasonTypeMasterDto) {
		try {
			return iReasonTypeMasterDao.getReasonTypeMasterList(reasonTypeMasterDto);
		} catch (Exception e) {
		logger.error("getReasonTypeMasterList failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateReasonTypeMaster(ReasonTypeMasterDto reasonTypeMasterDto) {
		
		try {
			Response response = iReasonTypeMasterDao.getReasonTypeMasterByNameNotById(reasonTypeMasterDto);
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iReasonTypeMasterDao.updateReasonTypeMaster(reasonTypeMasterDto);
			}
		} catch (Exception e) {
		logger.error("updateReasonTypeMaster failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateReasonTypeMasterStatus(ReasonTypeMasterDto reasonTypeMasterDto) {
		try {
			return iReasonTypeMasterDao.updateReasonTypeMasterStatus(reasonTypeMasterDto);
		} catch (Exception e) {
		logger.error("updateReasonTypeMasterStatus failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getReasonTypeMasterCount(ReasonTypeMasterDto reasonTypeMasterDto) {
		try {
			return iReasonTypeMasterDao.getReasonTypeMasterCount(reasonTypeMasterDto);
		} catch (Exception e) {
		logger.error("getReasonTypeMasterCount failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response saveReasonTypeMaster(ReasonTypeMasterDto reasonTypeMasterDto) {
		Response response = iReasonTypeMasterDao.getReasonTypeMasterByName(reasonTypeMasterDto);
		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iReasonTypeMasterDao.saveReasonTypeMaster(reasonTypeMasterDto);
			}

		} catch (Exception e) {
		logger.error("saveReasonTypeMaster failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
