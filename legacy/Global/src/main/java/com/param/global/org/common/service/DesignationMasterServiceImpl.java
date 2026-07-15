package com.param.global.org.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.common.dao.IDesignationMasterDao;
import com.param.global.org.common.dto.DesignationMasterDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class DesignationMasterServiceImpl implements IDesignationMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(DesignationMasterServiceImpl.class);


	@Autowired
	IDesignationMasterDao iDesignationMasterDao;

	@Override
	public Response saveDesignationMaster(DesignationMasterDto designationMasterDto) throws ApplicationException {
		Response response = iDesignationMasterDao.getDesignationMasterByName(designationMasterDto);
		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iDesignationMasterDao.saveDesignationMaster(designationMasterDto);
			}

		} catch (Exception e) {
		logger.error("saveDesignationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDesignationMasterById(int designationId, int orgId) throws ApplicationException {
		try {
			return iDesignationMasterDao.getDesignationMasterById(designationId, orgId);
		} catch (Exception e) {
		logger.error("getDesignationMasterById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDesignationMasterList(DesignationMasterDto designationMasterDto) throws ApplicationException {
		try {
			return iDesignationMasterDao.getDesignationMasterList(designationMasterDto);
		} catch (Exception e) {
		logger.error("getDesignationMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override //minesh
	public Response getActiveDesignationMasterList(DesignationMasterDto designationMasterDto) throws ApplicationException {
		try {
			return iDesignationMasterDao.getActiveDesignationMasterList(designationMasterDto);
		} catch (Exception e) {
		logger.error("getActiveDesignationMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}//minesh

	@Override
	public Response updateDesignationMaster(DesignationMasterDto designationMasterDto) throws ApplicationException {
		Response response = iDesignationMasterDao.getDesignationMasterByNameNotById(designationMasterDto);
		try {
			if (response.getListObject().size() > 0 && response.getListObject() != null) {
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			} else {
				return iDesignationMasterDao.updateDesignationMaster(designationMasterDto);
			}
		} catch (Exception e) {
		logger.error("updateDesignationMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateDesignationMasterStatus(DesignationMasterDto designationMasterDto)
			throws ApplicationException {
		try {
			return iDesignationMasterDao.updateDesignationMasterStatus(designationMasterDto);
		} catch (Exception e) {
		logger.error("updateDesignationMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getDesignationMasterCount(DesignationMasterDto designationMasterDto) throws ApplicationException {
		try {
			return iDesignationMasterDao.getDesignationMasterCount(designationMasterDto);
		} catch (Exception e) {
		logger.error("getDesignationMasterCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
}
