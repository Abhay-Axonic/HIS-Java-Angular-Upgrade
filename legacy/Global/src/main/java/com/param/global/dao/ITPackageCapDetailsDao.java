package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TPackageCapDetailsDto;
import com.param.service.global.model.TPackageCapDetails;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface ITPackageCapDetailsDao extends IGenericDao<TPackageCapDetails, Integer>{
	public Response saveTPackageCapDetails(TPackageCapDetailsDto detailsDto)throws ServiceException;
	public Response getPackageCapDetailsByPackageIdForBilling(int packageId)throws ServiceException;
	public Response getPackageCapDetailsByPackageId(int packageId)throws ServiceException;
	
	//packageChage
	public Response getPackageGroupCapDetailsByPackageId(int packageId)throws ServiceException;
	public Response getPackageConsumbaleCapDetailsByPackageId(int packageId)throws ServiceException;
	
	public Response getPackageCapDetailsByPackageIdForTransaction(int packageConsumptionId)throws ServiceException;
	
	
	
}
