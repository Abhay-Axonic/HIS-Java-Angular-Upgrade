package com.param.global.org.common.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.org.common.dao.IShiftMasterDao;
import com.param.global.org.common.dto.ShiftMasterDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class ShiftMasterServiceImpl implements IShiftMasterService,ICommonConstants{
	final static Logger logger = Logger.getLogger(ShiftMasterServiceImpl.class);


	@Autowired
	IShiftMasterDao iShiftMasterDao;
	
	@Override
	public Response saveShiftMaster(ShiftMasterDto shiftMasterDto) throws ApplicationException {
		try {
			Response res=iShiftMasterDao.getByShiftName(shiftMasterDto);
			if(res.getStatus().equals("success")){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
			return iShiftMasterDao.saveShiftMaster(shiftMasterDto);
			}
			} catch (Exception e) {
			logger.error("saveShiftMaster failed", e);
			}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getShiftMasterList(ShiftMasterDto shiftMasterDto) throws ApplicationException {
		try {
			return iShiftMasterDao.getShiftMasterList(shiftMasterDto);
		}catch (Exception e) {
		logger.error("getShiftMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}
	
	@Override
	public Response updateShiftMaster(ShiftMasterDto shiftMasterDto) throws ApplicationException {
		try {
			Response res=iShiftMasterDao.getShiftMasterByNameNotId(shiftMasterDto);
			if(res.getStatus().equals("success")){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
			return iShiftMasterDao.updateShiftMaster(shiftMasterDto);
			}
		}catch (Exception e) {
		logger.error("updateShiftMaster failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
		
	
	@Override
	public Response updateShiftMasterStatus(ShiftMasterDto shiftMasterDto) throws ApplicationException {
		try {
			return iShiftMasterDao.updateShiftMasterStatus(shiftMasterDto);
		}catch (Exception e) {
		logger.error("updateShiftMasterStatus failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}
	
	@Override
	public Response getShiftMasterCount(ShiftMasterDto shiftMasterDto) throws ApplicationException {
		try {
			return iShiftMasterDao.getShiftMasterCount(shiftMasterDto);
		}catch (Exception e) {
		logger.error("getShiftMasterCount failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getShiftMasterById(Integer shiftId, Integer organizationId, Integer unitId)
			throws ApplicationException {
		try {
			return iShiftMasterDao.getShiftMasterById(shiftId,organizationId,unitId);
		}catch (Exception e) {
		logger.error("getShiftMasterById failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveShiftMasterList(Integer organizationId, Integer unitId) throws ApplicationException {
		try {
			return iShiftMasterDao.getActiveShiftMasterList(organizationId,unitId);
		}catch (Exception e) {
		logger.error("getActiveShiftMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
		}

	@Override
	public Response getShiftlistByTime(ShiftMasterDto shiftMasterDto) throws ApplicationException {
		try {
			return iShiftMasterDao.getShiftlistByTime(shiftMasterDto);
		}catch (Exception e) {
		logger.error("getShiftlistByTime failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
