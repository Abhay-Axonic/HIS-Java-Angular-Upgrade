package com.param.lis.unit.service;

import javax.transaction.Transactional;

import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.MicrobiologyTestMasterDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings({"rawtypes"})
public interface IMicrobiologyTestMasterService {
	
	public Response saveMicrobiologyTestMaster(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException; 
	
	public Response updateMicrobiologyTestMaster(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException; 
	
	public Response updateMicrobiologyTestMasterStatus(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException; 
	
	public Response getMicrobiologyTestMasterList(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException; 
	
	public Response getMicrobiologyTestMasterById(Integer microTestId, Integer orgId, Integer orgUnitId) throws ApplicationException; 
	
	public Response getMicrobiologyTestMasterCount(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException; 
	
	public Response getActiveMicrobiologyTestMasterList(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException;
	
	public Response getMicrobiologyTestMasterByName(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException;
	
	public Response getMicrobiologyTestMasterByNameNotId(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException;
	
}
