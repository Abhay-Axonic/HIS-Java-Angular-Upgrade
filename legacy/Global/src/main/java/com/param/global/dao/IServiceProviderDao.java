package com.param.global.dao;

import com.param.global.common.Response;
import com.param.global.dto.ServiceProviderDto;
import com.param.global.model.ServiceProvider;

import in.param.common.dao.IGenericDao;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IServiceProviderDao extends IGenericDao<ServiceProvider, Integer>{

	
	Response getServiceProviderByName(ServiceProviderDto serviceProviderDto)throws ApplicationException;

	Response saveServiceProvider(ServiceProviderDto serviceProviderDto)throws ApplicationException;

	Response getServiceProviderList(ServiceProviderDto serviceProviderDto)throws ApplicationException;
	
	Response getServiceProviderActiveList(ServiceProviderDto serviceProviderDto) throws ApplicationException;

	Response getSingleServiceProvider(ServiceProviderDto serviceProviderDto)throws ApplicationException;

	Response updateServiceProvider(ServiceProviderDto serviceProviderDto) throws ApplicationException;

	Response getServiceProviderByID(ServiceProviderDto serviceProviderDto) throws ApplicationException;

	Response changeServiceProviderStatus(ServiceProviderDto serviceProviderDto) throws ApplicationException;



	

}
