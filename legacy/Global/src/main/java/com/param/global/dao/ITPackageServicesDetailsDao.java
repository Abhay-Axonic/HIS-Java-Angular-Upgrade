package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TPackageServicesDetailsDto;
import com.param.service.global.model.TPackageServicesDetails;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface ITPackageServicesDetailsDao extends IGenericDao<TPackageServicesDetails, Integer>{
	public Response saveTPackageServicesDetails(TPackageServicesDetailsDto detailsDto)throws ServiceException;
	public Response getPackageServicesByPackageId(int packageId)throws ServiceException;
	
	public Response getPackageServiceEditByPackageId(int packageId)throws ServiceException;
	public Response getPackageItemEditByPackageId(int packageId)throws ServiceException;
	
	public Response getPackageEitherOrServiceEditByGroupId(int packageGroupDetailsId)throws ServiceException;
	public Response getPackageServicesFrTransactionByPackageId(Integer packageId, Integer consumptionMasterId)throws ServiceException;
	public Response getPackageEitherOrServicesByGroupIdForTransaction(Integer packageId, Integer consumptionMasterId,Integer groupId)throws ServiceException;
}
