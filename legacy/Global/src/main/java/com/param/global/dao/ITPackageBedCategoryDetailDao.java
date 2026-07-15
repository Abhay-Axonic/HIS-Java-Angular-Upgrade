package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TPackageBedCategoryDetailDto;
import com.param.service.global.model.TPackageBedCategoryDetail;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface ITPackageBedCategoryDetailDao extends IGenericDao<TPackageBedCategoryDetail, Integer>{
	public Response saveTPackageBedCategoryDetail(TPackageBedCategoryDetailDto detailDto)throws ServiceException;
	public Response getTPackageBedCategoryDetail(Integer packageId)throws ServiceException;
	public Response getTPackageBedCategoryDetailBilling(Integer  packageConsumptionMasterId)throws ServiceException;
	public Response getPatientApplicableDaysByAdmissionId(Integer admissionId)throws ServiceException;
}
