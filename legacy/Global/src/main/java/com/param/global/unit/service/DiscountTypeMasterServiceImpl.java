package com.param.global.unit.service;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.ServiceException;
import com.param.global.unit.dao.IDiscountTypeMasterDao;
import com.param.global.unit.dto.DiscountCategoryTypeMasterDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
@Service
public class DiscountTypeMasterServiceImpl implements IDiscountTypeMasterService, ICommonConstants {

	final static Logger logger = Logger.getLogger(DiscountTypeMasterServiceImpl.class);

	@Autowired
	IDiscountTypeMasterDao iDiscountTypeMasterDao;
	@Override
	public Response saveDiscountCategory(DiscountCategoryTypeMasterDto dto) throws ApplicationException {
		try {
			Response res= iDiscountTypeMasterDao.getDiscountCategoryExistOrNotByName(dto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}
			return iDiscountTypeMasterDao.saveDiscountCategory(dto);
		} catch (Exception e) {
			logger.error("saveDiscountCategory failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response updateDiscountCategory(DiscountCategoryTypeMasterDto dto) throws ApplicationException {
		try {
			Response res= iDiscountTypeMasterDao.getDiscountCategoryExistOrNotByNameNotById(dto);
			if(res.getListObject()!=null && res.getListObject().size()>0){
				return new Response<>(ERROR, ALREADY_EXIST_CODE, ALREADY_EXIST, null, null);
			}
			return iDiscountTypeMasterDao.updateDiscountCategory(dto);
		} catch (Exception e) {
			logger.error("updateDiscountCategory failed", e);
			throw new ServiceException(e.getMessage());
		}
	}


	@Override
	public Response updateDiscountCategoryStatus(DiscountCategoryTypeMasterDto dto) throws ApplicationException {
		try {

			return iDiscountTypeMasterDao.updateDiscountCategoryStatus(dto);
		} catch (Exception e) {
			logger.error("updateDiscountCategoryStatus failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDiscountCategoryList(DiscountCategoryTypeMasterDto dto) throws ApplicationException {
		try {
			return iDiscountTypeMasterDao.getDiscountCategoryList(dto);
		} catch (Exception e) {
			logger.error("getDiscountCategoryList failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDiscountCategoryCount(DiscountCategoryTypeMasterDto dto) throws ApplicationException {
		try {
			return iDiscountTypeMasterDao.getDiscountCategoryCount(dto);
		} catch (Exception e) {
			logger.error("getDiscountCategoryCount failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

	@Override
	public Response getDiscountCategoryById(DiscountCategoryTypeMasterDto dto) throws ApplicationException {
		try {
			return iDiscountTypeMasterDao.getDiscountCategoryById(dto);
		} catch (Exception e) {
			logger.error("getDiscountCategoryById failed", e);
			throw new ServiceException(e.getMessage());
		}
	}

}
