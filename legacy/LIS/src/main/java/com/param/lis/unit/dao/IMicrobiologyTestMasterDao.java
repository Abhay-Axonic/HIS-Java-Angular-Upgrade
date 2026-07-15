package com.param.lis.unit.dao;

import com.param.lis.global.common.Response;
import com.param.lis.unit.dto.MicrobiologyTestMasterDto;

import in.param.exception.ApplicationException;
@SuppressWarnings({"rawtypes"})
public interface IMicrobiologyTestMasterDao {
	
	public Response saveMicrobiologyTestMaster(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException; 
	
	public Response updateMicrobiologyTestMaster(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException; 
	
	public Response updateMicrobiologyTestMasterStatus(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException; 
	
	public Response getMicrobiologyTestMasterList(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException; 
	
	public Response getMicrobiologyTestMasterById(Integer microTestId, Integer orgId, Integer orgUnitId) throws ApplicationException; 
	
	public Response getMicrobiologyTestMasterCount(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException; 
	
	public Response getActiveMicrobiologyTestMasterList(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException;
	
	public Response getMicrobiologyTestMasterByName(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException;
	
	public Response getMicrobiologyTestMasterByNameNotId(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException;

	public Response getMicrobiologyTestMasterByCode(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException;

	public Response checkMicrobiologyTestMasterByCode(MicrobiologyTestMasterDto microbiologyTestMasterDto) throws ApplicationException;
}
