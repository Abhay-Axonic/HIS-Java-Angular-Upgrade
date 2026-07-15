package com.param.global.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.unit.dao.IAmenityMasterDao;
import com.param.global.unit.dto.AmenityMasterDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
@SuppressWarnings("rawtypes")
public class AmenityMasterServiceImpl implements IAmenityMasterService,ICommonConstants {
	final static Logger logger = Logger.getLogger(AmenityMasterServiceImpl.class);


	@Autowired 
	IAmenityMasterDao iAmenityMasterDao;
	
	@Override
	public Response saveAmenityMaster(AmenityMasterDto amenityMasterDto) throws ApplicationException {
		Response res=iAmenityMasterDao.getAmenityMasterListByName(amenityMasterDto);
		try {
			if(res.getListObject().size()>0&&res.getListObject()!=null){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iAmenityMasterDao.saveAmenityMaster(amenityMasterDto);
			}

		} catch (Exception e) {
		logger.error("saveAmenityMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAmenityMaster(AmenityMasterDto amenityMasterDto) throws ApplicationException {
		Response res=iAmenityMasterDao.getAmenityMasterListByNameNotById(amenityMasterDto);
		try {
			if(res.getListObject().size()>0&&res.getListObject()!=null){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iAmenityMasterDao.updateAmenityMaster(amenityMasterDto);
			}
		} catch (Exception e) {
		logger.error("updateAmenityMaster failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateAmenityMasterStatus(AmenityMasterDto amenityMasterDto) throws ApplicationException {
		try {
			return iAmenityMasterDao.updateAmenityMasterStatus(amenityMasterDto);
		} catch (Exception e) {
		logger.error("updateAmenityMasterStatus failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAmenityMasterList(AmenityMasterDto amenityMasterDto) throws ApplicationException {
		try {
			return iAmenityMasterDao.getAmenityMasterList(amenityMasterDto);
		} catch (Exception e) {
		logger.error("getAmenityMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getActiveAmenityMasterList(AmenityMasterDto amenityMasterDto) throws ApplicationException {
		try {
			return iAmenityMasterDao.getActiveAmenityMasterList(amenityMasterDto);
		} catch (Exception e) {
		logger.error("getActiveAmenityMasterList failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAmenityMasterListCount(AmenityMasterDto amenityMasterDto) throws ApplicationException {
		try {
			return iAmenityMasterDao.getAmenityMasterListCount(amenityMasterDto);
		} catch (Exception e) {
		logger.error("getAmenityMasterListCount failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getAmenityMasterListById(Integer amenityId) throws ApplicationException {
		try {
			return iAmenityMasterDao.getAmenityMasterListById(amenityId);
		} catch (Exception e) {
		logger.error("getAmenityMasterListById failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
