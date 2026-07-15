package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.common.ServiceException;
import com.param.global.dto.TPackageIncExcDetailsDto;
import com.param.service.global.model.TPackageIncExcDetails;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface ITPackageIncExcDetailsDao extends IGenericDao<TPackageIncExcDetails, Integer>{
	public Response saveTPackageIncExcDetails(TPackageIncExcDetailsDto detailsDto)throws ServiceException;
	public Response getTPackageIncExcDetailsForBillingByPackage(int packageId)throws ServiceException;
	
	
	//Parent Package
	public Response getTPackageIncExcDetailsForServiceByPacakgeId(int packageId)throws ServiceException;
	public Response getTPackageIncExcDetailsForItemsByPacakgeId(int packageId)throws ServiceException;
	
	public Response getTPackageIncExcDetailsForBillingByPackageConsumptionId(int packageConsumptionMasterId)throws ServiceException;
	
	
}
