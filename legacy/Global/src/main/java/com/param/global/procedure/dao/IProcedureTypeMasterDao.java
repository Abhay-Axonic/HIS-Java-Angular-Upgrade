package com.param.global.procedure.dao;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.procedure.dto.ProcedureTypeMasterDto;
import com.param.otims.model.ProcedureTypeMaster;

import in.param.common.dao.IGenericDao;

@SuppressWarnings("rawtypes")
public interface IProcedureTypeMasterDao extends IGenericDao<ProcedureTypeMaster, Integer>{
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
