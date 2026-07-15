package com.param.global.org.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.common.dao.IRejectionMasterDao;
import com.param.global.org.common.dto.RejectionMasterDto;
import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class RejectionMasterServiceImpl implements IRejectionMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(RejectionMasterServiceImpl.class);


	@Autowired
	IRejectionMasterDao iRejectionMasterDao;

	@Override
	public Response saveRejectionMaster(RejectionMasterDto rejectionMasterDto) throws ApplicationException {
		Response response = iRejectionMasterDao.getRejectionMasterByName(rejectionMasterDto);
		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iRejectionMasterDao.saveRejectionMaster(rejectionMasterDto);
			}

		} catch (Exception e) {
		logger.error("saveRejectionMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRejectionMasterById(int rejectId, int orgId) throws ApplicationException {
		try {
			return iRejectionMasterDao.getRejectionMasterById(rejectId, orgId);
		} catch (Exception e) {
		logger.error("getRejectionMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRejectionMasterList(RejectionMasterDto rejectionMasterDto) throws ApplicationException {
		try {
			return iRejectionMasterDao.getRejectionMasterList(rejectionMasterDto);
		} catch (Exception e) {
		logger.error("getRejectionMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateRejectionMaster(RejectionMasterDto rejectionMasterDto) throws ApplicationException {
		Response response = iRejectionMasterDao.getRejectionMasterByNameNotById(rejectionMasterDto);
		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iRejectionMasterDao.updateRejectionMaster(rejectionMasterDto);
			}
		} catch (Exception e) {
		logger.error("updateRejectionMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateRejectionMasterStatus(RejectionMasterDto rejectionMasterDto) throws ApplicationException {
		try {
			return iRejectionMasterDao.updateRejectionMasterStatus(rejectionMasterDto);
		} catch (Exception e) {
		logger.error("updateRejectionMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getRejectionMasterCount(RejectionMasterDto rejectionMasterDto) throws ApplicationException {
		try {
			return iRejectionMasterDao.getRejectionMasterCount(rejectionMasterDto);
		} catch (Exception e) {
		logger.error("getRejectionMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
