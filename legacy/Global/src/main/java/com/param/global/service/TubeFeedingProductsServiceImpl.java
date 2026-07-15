package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dao.ITubeFeedingProductsDao;
import com.param.global.dto.TubeFeedingProductsDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
@SuppressWarnings({"rawtypes"})
@Service
public class TubeFeedingProductsServiceImpl implements ITubeFeedingProductsService, ICommonConstants {
	private static final Logger logger = Logger.getLogger(TubeFeedingProductsServiceImpl.class);

	@Autowired
	ITubeFeedingProductsDao iTubeFeedingProductsDao;
	
	@Override
	public Response saveTubeFeedingProducts(TubeFeedingProductsDto tubeFeedingProductsDto) throws ApplicationException {
		Response res=iTubeFeedingProductsDao.checkTubeFeedingProductsAvailable(tubeFeedingProductsDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iTubeFeedingProductsDao.saveTubeFeedingProducts(tubeFeedingProductsDto);
			}

		} catch (Exception e) {
			logger.error("saveTubeFeedingProducts failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateTubeFeedingProducts(TubeFeedingProductsDto tubeFeedingProductsDto)
			throws ApplicationException {
		Response res=iTubeFeedingProductsDao.getTubeFeedingProductsByNameNotId(tubeFeedingProductsDto);
		try {
			if(res.getListObject()!=null && res.getListObject().size()>0){
				
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}else{
				return iTubeFeedingProductsDao.updateTubeFeedingProducts(tubeFeedingProductsDto);
			}

		} catch (Exception e) {
			logger.error("updateTubeFeedingProducts failed", e);
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateTubeFeedingProductsStatus(TubeFeedingProductsDto tubeFeedingProductsDto)
			throws ApplicationException {
		try {
			return iTubeFeedingProductsDao.updateTubeFeedingProductsStatus(tubeFeedingProductsDto);

	} catch (Exception e) {
		logger.error("updateTubeFeedingProductsStatus failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getTubeFeedingProductsList(TubeFeedingProductsDto tubeFeedingProductsDto)
			throws ApplicationException {
		try {
			return iTubeFeedingProductsDao.getTubeFeedingProductsList(tubeFeedingProductsDto);

	} catch (Exception e) {
		logger.error("getTubeFeedingProductsList failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getTubeFeedingProductsById(Integer tubeFeedingProductsId, Integer organisationId, Integer unitId)
			throws ApplicationException {
		try {
			return iTubeFeedingProductsDao.getTubeFeedingProductsById( tubeFeedingProductsId, organisationId, unitId);

	} catch (Exception e) {
		logger.error("getTubeFeedingProductsById failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}
	@Override
	public Response getActiveTubeFeedingProducts(TubeFeedingProductsDto tubeFeedingProductsDto)
			throws ApplicationException {
		try {
			return iTubeFeedingProductsDao.getActiveTubeFeedingProducts(tubeFeedingProductsDto);

	} catch (Exception e) {
		logger.error("getActiveTubeFeedingProducts failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

	@Override
	public Response getTubeFeedingProductsCount(TubeFeedingProductsDto tubeFeedingProductsDto)
			throws ApplicationException {
		try {
			return iTubeFeedingProductsDao.getTubeFeedingProductsCount(tubeFeedingProductsDto);

	} catch (Exception e) {
		logger.error("getTubeFeedingProductsCount failed", e);
	}
	return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
}

}
