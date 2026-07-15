package com.param.billing.unit.dao;

import com.param.billing.unit.dto.InsuranceTPAMapperDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings("rawtypes")
public interface IInsuranceTPAMapperDao {
	
	Response saveInsuranceTPAMapper(InsuranceTPAMapperDto insuranceTPAMapperDto) throws ApplicationException;

	Response getInsuranceTPAMapperList(InsuranceTPAMapperDto insuranceTPAMapperDto)  throws ApplicationException;

	Response getInsuranceTPAMapperListCount(InsuranceTPAMapperDto insuranceTPAMapperDto) throws ApplicationException;

	Response updateInsuranceTPAMapperStatus(InsuranceTPAMapperDto insuranceTPAMapperDto) throws ApplicationException;

	Response getInsuranceTPAMapperListById(InsuranceTPAMapperDto insuranceTPAMapperDto) throws ApplicationException;

	Response getInsuranceListNotMappedToMapper(InsuranceTPAMapperDto insuranceTPAMapperDto) throws ApplicationException;
}
