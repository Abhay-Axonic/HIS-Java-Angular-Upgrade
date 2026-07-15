package com.param.global.procedure.service;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.procedure.dto.ProcedureTypeMasterDto;
@Transactional
@SuppressWarnings("rawtypes")
public interface IProcedureTypeMasterService {
	
	public Response getProcedureTypeList(int orgId, int unitId)throws ApplicationException;
	
	public Response saveProcedureTypeMaster(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException;
	
	public Response updateProcedureTypeMaster(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException;
	
	public Response updateProcedureTypeMasterStatus(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException;
	
	public Response getListProcedureTypeMaster(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException;
	
	public Response getProcedureTypeMasterById(Integer procedureTypeId, Integer orgId, Integer unitId) throws ApplicationException;
	
	public Response getProcedureTypeMasterCount(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException;
	
	public Response getProcedureTypeMasterByName(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException;
	
	public Response getProcedureTypeMasterByNameNotId(ProcedureTypeMasterDto procedureTypeMasterDto) throws ApplicationException;
}
