package com.param.billing.global.transaction.service;


import javax.transaction.Transactional;

import org.springframework.web.multipart.MultipartFile;

import com.param.billing.common.Response;
import com.param.billing.global.transaction.dto.InsuranceMasterDto;

import in.param.exception.ApplicationException;
@Transactional
@SuppressWarnings({"rawtypes"})
public interface IInsuranceMasterService {
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

	public Response saveInsuranceImage(int orgId, int unitId, int insuranceId, MultipartFile inputFile) throws ApplicationException;
	
}
