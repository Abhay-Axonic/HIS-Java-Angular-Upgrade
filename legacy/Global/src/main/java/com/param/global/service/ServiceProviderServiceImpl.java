package com.param.global.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.global.dao.IServiceProviderDao;
import com.param.global.dto.ServiceProviderDto;

import in.param.exception.ApplicationException;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@SuppressWarnings({ "rawtypes", "unchecked" })
@Service

public class ServiceProviderServiceImpl implements IServiceProviderService, ICommonConstants {
	final static Logger logger = Logger.getLogger(ServiceProviderServiceImpl.class);


	@Autowired
	private IServiceProviderDao iServiceProviderDao;
	

	@Override
	public Response saveServiceProvider(ServiceProviderDto serviceProviderDto) throws ApplicationException {
		try {

			Response deptResponse = iServiceProviderDao.getServiceProviderByName(serviceProviderDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {

				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			} else {
				iServiceProviderDao.saveServiceProvider(serviceProviderDto);
				return new Response(SUCCESS, null, COMMON_SUCCESS, null, null);
			}
		} catch (Exception e) {
		logger.error("saveServiceProvider failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getServiceProviderList(ServiceProviderDto serviceProviderDto) throws ApplicationException {
		try {
			return iServiceProviderDao.getServiceProviderList(serviceProviderDto);
		} catch (Exception e) {
		logger.error("getServiceProviderList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getServiceProviderActiveList(ServiceProviderDto serviceProviderDto) throws ApplicationException {
		try {
			return iServiceProviderDao.getServiceProviderActiveList(serviceProviderDto);
		} catch (Exception e) {
		logger.error("getServiceProviderActiveList failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response getSingleServiceProvider(ServiceProviderDto serviceProviderDto) throws ApplicationException {
		try {
			return iServiceProviderDao.getSingleServiceProvider(serviceProviderDto);
		} catch (Exception e) {
		logger.error("getSingleServiceProvider failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response updateServiceProvider(ServiceProviderDto serviceProviderDto) throws ApplicationException {
		try {
			/*Response deptResponse = iServiceProviderDao.getServiceProviderByName(serviceProviderDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);
			} else {*/
				iServiceProviderDao.updateServiceProvider(serviceProviderDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);
			//}
		} catch (Exception e) {
		logger.error("updateServiceProvider failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	@Override
	public Response changeServiceProviderStatus(ServiceProviderDto serviceProviderDto) throws ApplicationException {
		try {
			Response deptResponse = iServiceProviderDao.getServiceProviderByID(serviceProviderDto);
			if (deptResponse.getListObject() != null && deptResponse.getListObject().size() > 0) {
				
				iServiceProviderDao.changeServiceProviderStatus(serviceProviderDto);
				return new Response(SUCCESS, null, COMMON_UPDATE, null, null);

			} else {
				return new Response(ERROR, null, ALREADY_EXIST, null, null);

			}
		} catch (Exception e) {
		logger.error("changeServiceProviderStatus failed", e);
		}
		return new Response(ERROR, null, null, null, null);
	}

	

}
