package com.param.service.dao;

import com.param.billing.common.Response;
import com.param.service.dto.ContractGradeMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IContractGradeMasterDao {
	
	public Response saveContractGradeMaster(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException;
	
	public Response updateContractGradeMaster(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException;
	
	public Response updateContractGradeMasterStatus(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException;
	
	public Response getContractGradeMasterById(Integer contractGradeId, Integer orgId, Integer unitId) throws ApplicationException;
	
	public Response getContractGradeMasterList(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException;
	
	public Response getContractGradeMasterCount(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException;
	
	public Response getContractGradeMasterByName(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException;
	
	public Response getContractGradeMasterByNameNotId(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException;
	
	public Response getActiveContractGradeMasterList(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException;
	
	public Response getContractGradeMasterNotMappedInMapper(ContractGradeMasterDto contractGradeMasterDto) throws ApplicationException;
}
