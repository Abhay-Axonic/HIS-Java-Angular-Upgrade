package com.param.global.unit.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import in.param.exception.ApplicationException;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dao.IWingMasterDao;
import com.param.global.unit.dto.WingMasterDto;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings("rawtypes")
@Service
public class WingMasterServiceImpl implements IWingMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(WingMasterServiceImpl.class);


	@Autowired
	IWingMasterDao iWingMasterDao;

	@Override
	public Response getWingMasterListByOrgAndUnit(WingMasterDto wingMasterDto) throws ApplicationException {
		try {
			return iWingMasterDao.getWingMasterListByOrgAndUnit(wingMasterDto);
		} catch (Exception e) {
		logger.error("getWingMasterListByOrgAndUnit failed", e);
		}
		return new Response<>(ERROR, null, null, null, null);
	}

	@Override
	public Response saveWingMaster(WingMasterDto wingMasterDto) throws ApplicationException {
		Response res=iWingMasterDao.getWingMasterListByName(wingMasterDto);
		try {
			if(res.getListObject().size()>0 && res.getListObject()!=null){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iWingMasterDao.saveWingMaster(wingMasterDto);
			}

		} catch (Exception e) {
		logger.error("saveWingMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateWingMaster(WingMasterDto wingMasterDto) throws ApplicationException {
		Response res=iWingMasterDao.getWingMasterListByNameNotById(wingMasterDto);
		try {
			if(res.getListObject().size()>0 && res.getListObject()!=null){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iWingMasterDao.updateWingMaster(wingMasterDto);
			}
		} catch (Exception e) {
		logger.error("updateWingMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateWingMasterStatus(WingMasterDto wingMasterDto) throws ApplicationException {
		try {
			return iWingMasterDao.updateWingMasterStatus(wingMasterDto);
		} catch (Exception e) {
		logger.error("updateWingMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWingMasterList(WingMasterDto wingMasterDto) throws ApplicationException {
		try {
			return iWingMasterDao.getWingMasterList(wingMasterDto);
		} catch (Exception e) {
		logger.error("getWingMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWingMasterListCount(WingMasterDto wingMasterDto) throws ApplicationException {
		try {
			return iWingMasterDao.getWingMasterListCount(wingMasterDto);
		} catch (Exception e) {
		logger.error("getWingMasterListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getWingMasterListById(Integer wingId) throws ApplicationException {
		try {
			return iWingMasterDao.getWingMasterListById(wingId);
		} catch (Exception e) {
		logger.error("getWingMasterListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
