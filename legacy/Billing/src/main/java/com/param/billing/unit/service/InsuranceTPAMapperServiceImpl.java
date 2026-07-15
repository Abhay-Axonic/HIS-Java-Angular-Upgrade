package com.param.billing.unit.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.param.billing.unit.dao.IInsuranceTPAMapperDao;
import com.param.billing.unit.dto.InsuranceTPAMapperDto;
import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Service
@SuppressWarnings("rawtypes")
public class InsuranceTPAMapperServiceImpl implements IInsuranceTPAMapperService, ICommonConstants {

	@Autowired 
	IInsuranceTPAMapperDao iInsuranceTPAMapperDao;

	@Override
	public Response saveInsuranceTPAMapper(InsuranceTPAMapperDto insuranceTPAMapperDto) throws ApplicationException {
		try {
			return iInsuranceTPAMapperDao.saveInsuranceTPAMapper(insuranceTPAMapperDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getInsuranceTPAMapperList(InsuranceTPAMapperDto insuranceTPAMapperDto) throws ApplicationException {
		try {
			return iInsuranceTPAMapperDao.getInsuranceTPAMapperList(insuranceTPAMapperDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getInsuranceTPAMapperListCount(InsuranceTPAMapperDto insuranceTPAMapperDto)
			throws ApplicationException {
		try {
			return iInsuranceTPAMapperDao.getInsuranceTPAMapperListCount(insuranceTPAMapperDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response updateInsuranceTPAMapperStatus(InsuranceTPAMapperDto insuranceTPAMapperDto)
			throws ApplicationException {
		try {
			return iInsuranceTPAMapperDao.updateInsuranceTPAMapperStatus(insuranceTPAMapperDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getInsuranceTPAMapperListById(InsuranceTPAMapperDto insuranceTPAMapperDto)
			throws ApplicationException {
		try {
			return iInsuranceTPAMapperDao.getInsuranceTPAMapperListById(insuranceTPAMapperDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

	@Override
	public Response getInsuranceListNotMappedToMapper(InsuranceTPAMapperDto insuranceTPAMapperDto)
			throws ApplicationException {
		try {
			return iInsuranceTPAMapperDao.getInsuranceListNotMappedToMapper(insuranceTPAMapperDto);
		} catch (Exception e) {
			
		}
		return new Response<>(ERROR, COMMON_ERROR_CODE, COMMON_ERROR_MESSAGE, null, null);
	}

}
