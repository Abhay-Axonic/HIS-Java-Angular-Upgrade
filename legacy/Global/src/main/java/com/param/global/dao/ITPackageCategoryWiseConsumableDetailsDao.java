package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TPackageCategoryWiseConsumableDetailsDto;
import com.param.service.global.model.TPackageCategoryWiseConsumableDetails;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface ITPackageCategoryWiseConsumableDetailsDao extends IGenericDao<TPackageCategoryWiseConsumableDetails, Integer>{
	public Response saveTPackageCategoryWiseConsumableDetails(TPackageCategoryWiseConsumableDetailsDto detailsDto)throws ServiceException;
	public Response getTPackageCategoryWiseConsumableDetails(Integer packageId)throws ServiceException;
	
}
