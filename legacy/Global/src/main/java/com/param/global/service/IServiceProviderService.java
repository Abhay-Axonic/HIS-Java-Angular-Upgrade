package com.param.global.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.dto.ServiceProviderDto;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IServiceProviderService {
	@Transactional
	Response saveServiceProvider(ServiceProviderDto serviceProviderDto) throws ApplicationException;

	@Transactional
	Response getServiceProviderList(ServiceProviderDto serviceProviderDto) throws ApplicationException;

	@Transactional
	Response getServiceProviderActiveList(ServiceProviderDto serviceProviderDto) throws ApplicationException;

	@Transactional
	Response getSingleServiceProvider(ServiceProviderDto serviceProviderDto) throws ApplicationException;

	@Transactional
	Response updateServiceProvider(ServiceProviderDto serviceProviderDto) throws ApplicationException;

	@Transactional
	Response changeServiceProviderStatus(ServiceProviderDto serviceProviderDto) throws ApplicationException;


	



}

