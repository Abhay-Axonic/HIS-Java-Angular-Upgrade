package com.param.global.dao;

import com.param.billing.packages.model.TPackageBedCategory;
import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TPackageBedCategoryDto;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface ITPacakgeBedCategoryDao  extends IGenericDao<TPackageBedCategory, Integer>{
	public Response saveTPackageBedCategory(TPackageBedCategoryDto detailsDto)throws ServiceException;
	public Response getPackageBedCategoryForBilling(int packageId)throws ServiceException;
}
