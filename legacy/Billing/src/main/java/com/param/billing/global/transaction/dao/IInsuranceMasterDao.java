package com.param.billing.global.transaction.dao;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.InsuranceMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})

public interface IInsuranceMasterDao {

	public  Response saveInsuranceMaster(InsuranceMasterDto insuranceMasterDto) throws ApplicationException;
	
	public  Response updateInsuranceMaster(InsuranceMasterDto insuranceMasterDto) throws ApplicationException;
	
	public  Response updateInsuranceMasterStatus(InsuranceMasterDto insuranceMasterDto) throws ApplicationException;
	
	public  Response getInsuranceMasterById(Integer insuranceId, Integer orgId, Integer unitId) throws ApplicationException;
	
	public  Response getInsuranceMasterList(InsuranceMasterDto insuranceMasterDto) throws ApplicationException;
	
	public  Response getInsuranceMasterCount(InsuranceMasterDto insuranceMasterDto) throws ApplicationException;
	
	public  Response getInsuranceMasterByName(InsuranceMasterDto insuranceMasterDto) throws ApplicationException;
	
	public  Response getInsuranceMasterByNameNotId(InsuranceMasterDto insuranceMasterDto) throws ApplicationException;
	
	public  Response getActiveInsuranceMasterList(InsuranceMasterDto insuranceMasterDto) throws ApplicationException;

	public Response getMappedGradeToInsuerance(InsuranceMasterDto insuranceMasterDto) throws ApplicationException;

	public Response updateInsuranceImage(InsuranceMasterDto dto)throws ApplicationException;
}
