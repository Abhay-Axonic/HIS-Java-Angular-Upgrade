package com.param.billing.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.global.billing.dao.IServiceBasePriceMasterDao;
import com.param.global.billing.dto.ServiceBasePriceMasterDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
@Service
public class ServiceBasePriceMasterServiceImpl implements IServiceBasePriceMasterService, ICommonConstants {

	@Autowired
	IServiceBasePriceMasterDao iServiceBasePriceMasterDao;

	@Override
	public Response getServiceBasePriceList(ServiceBasePriceMasterDto serviceBasePriceMasterDto)
			throws ApplicationException {
		try {
			return iServiceBasePriceMasterDao.getServiceBasePriceList(serviceBasePriceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getServiceBasePriceListById(Integer serviceBasePriceId) throws ApplicationException {
		try {
			return iServiceBasePriceMasterDao.getServiceBasePriceListById(serviceBasePriceId);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateServiceBasePriceMaster(ServiceBasePriceMasterDto serviceBasePriceMasterDto)
			throws ApplicationException {
		try {
			return iServiceBasePriceMasterDao.updateServiceBasePriceMaster(serviceBasePriceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	
	@Override
	public Response updateServiceBasePriceMasterStatus(ServiceBasePriceMasterDto serviceBasePriceMasterDto)
			throws ApplicationException {
		try {
			return iServiceBasePriceMasterDao.updateServiceBasePriceMasterStatus(serviceBasePriceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response countServiceBasePriceMaster(ServiceBasePriceMasterDto serviceBasePriceMasterDto)
			throws ApplicationException {
		try {
			return iServiceBasePriceMasterDao.countServiceBasePriceMaster(serviceBasePriceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getServiceBasePriceListSearch(ServiceBasePriceMasterDto serviceBasePriceMasterDto)
			throws ApplicationException {
		try {
			return iServiceBasePriceMasterDao.getServiceBasePriceListSearch(serviceBasePriceMasterDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
