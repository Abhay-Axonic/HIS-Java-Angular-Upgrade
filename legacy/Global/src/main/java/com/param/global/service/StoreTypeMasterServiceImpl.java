package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.StoreTypeMasterDto;
import com.param.global.dao.IStoreTypeMasterDao;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class StoreTypeMasterServiceImpl implements IStoreTypeMasterService,ICommonConstants {
	final static Logger logger = Logger.getLogger(StoreTypeMasterServiceImpl.class);


	@Autowired
	IStoreTypeMasterDao iStoreTypeMasterDao;

	@Override
	public Response saveStoreType(StoreTypeMasterDto dto) throws ApplicationException {
		try {
			Response res=iStoreTypeMasterDao.getStoreTypeAlreadyExistOrNot(dto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iStoreTypeMasterDao.saveStoreType(dto);
			}
			
		} catch (Exception e) {
		logger.error("saveStoreType failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateStoreType(StoreTypeMasterDto dto) throws ApplicationException {
		try {
			Response res=iStoreTypeMasterDao.getStoreTypeAlreadyExistOrNotById(dto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iStoreTypeMasterDao.updateStoreType(dto);
			}
		} catch (Exception e) {
		logger.error("updateStoreType failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response updateStoreTypeStatus(StoreTypeMasterDto dto) throws ApplicationException {
		try {
			return iStoreTypeMasterDao.updateStoreTypeStatus(dto);
		} catch (Exception e) {
		logger.error("updateStoreTypeStatus failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getStoreTypeList(StoreTypeMasterDto dto) throws ApplicationException {
		try {
			return iStoreTypeMasterDao.getStoreTypeList(dto);
		} catch (Exception e) {
		logger.error("getStoreTypeList failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	@Override
	public Response getStoreTypeCount(StoreTypeMasterDto dto) throws ApplicationException {
		try {
			return iStoreTypeMasterDao.getStoreTypeCount(dto);
		} catch (Exception e) {
		logger.error("getStoreTypeCount failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

	
	@Override
	public Response getStoreTypeById(StoreTypeMasterDto dto) throws ApplicationException {
		try {
			return iStoreTypeMasterDao.getStoreTypeById(dto);
		} catch (Exception e) {
		logger.error("getStoreTypeById failed", e);
		}
	return new Response<>(ERROR, ERROR_CODE, null, null, null);
	}

}
