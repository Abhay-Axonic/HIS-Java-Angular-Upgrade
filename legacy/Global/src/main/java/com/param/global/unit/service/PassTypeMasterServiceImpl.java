package com.param.global.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.unit.dao.IPassTypeMasterDao;
import com.param.global.unit.dto.PassTypeMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class PassTypeMasterServiceImpl implements IPassTypeMasterService, ICommonConstants {
	final static Logger logger = Logger.getLogger(PassTypeMasterServiceImpl.class);


	@Autowired 
	IPassTypeMasterDao iPassTypeMasterDao;
	
	@Override
	public Response savePassTypeMaster(PassTypeMasterDto dto) throws ApplicationException {
		try {
			Response res =iPassTypeMasterDao.checkPassTypeMasterAlreadyExistOrNot(dto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iPassTypeMasterDao.savePassTypeMaster(dto);
			}
			
		} catch (Exception e) {
		logger.error("savePassTypeMaster failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response updateDiscountCategory(PassTypeMasterDto dto) {
		try {
			Response res = iPassTypeMasterDao.checkPassTypeNotById(dto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iPassTypeMasterDao.updateDiscountCategory(dto);
			}
			
		} catch (Exception e) {
		logger.error("updateDiscountCategory failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updatePassTypeMasterStatus(PassTypeMasterDto dto) {
		try {
			return iPassTypeMasterDao.updatePassTypeMasterStatus(dto);
		} catch (Exception e) {
		logger.error("updatePassTypeMasterStatus failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getPassTypeMasterList(PassTypeMasterDto dto) {
		try {
			return iPassTypeMasterDao.getPassTypeMasterList(dto);
		} catch (Exception e) {
		logger.error("getPassTypeMasterList failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getPassTypeMasterCount(PassTypeMasterDto dto) {
		try {
			return iPassTypeMasterDao.getPassTypeMasterCount(dto);
		} catch (Exception e) {
		logger.error("getPassTypeMasterCount failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getPassTypeMasterById(PassTypeMasterDto dto) {
		try {
			return iPassTypeMasterDao.getPassTypeMasterById(dto);
		} catch (Exception e) {
		logger.error("getPassTypeMasterById failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}


	@Override
	public Response getActivePassTypeMasterList(PassTypeMasterDto dto) throws ApplicationException {
		try{
			return iPassTypeMasterDao.getActivePassTypeMasterList(dto);
		}catch(Exception e){
		logger.error("getActivePassTypeMasterList failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

}
