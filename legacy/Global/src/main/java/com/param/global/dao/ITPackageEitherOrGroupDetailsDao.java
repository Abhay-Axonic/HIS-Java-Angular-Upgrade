package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TPackageEitherorGroupDetailsDto;
import com.param.service.global.model.TPackageEitherorGroupDetails;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface ITPackageEitherOrGroupDetailsDao extends IGenericDao<TPackageEitherorGroupDetails, Integer>{
	public Response savePackageEitherOrGroupDetails(TPackageEitherorGroupDetailsDto detailsDto)throws ServiceException;
	public Response getPackageEitherOrGroupDetailsByPackageId(int packageId)throws ServiceException;
	
	public Response getPackageEitherOrGroupDetailsByConsumptionIdForTransaction(int consumptionId)throws ServiceException;
}
