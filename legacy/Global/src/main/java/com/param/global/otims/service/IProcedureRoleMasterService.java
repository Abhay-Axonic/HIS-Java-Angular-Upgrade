package com.param.global.otims.service;

import javax.transaction.Transactional;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureRoleMasterDto;
@Transactional
@SuppressWarnings({"rawtypes"})
public interface IProcedureRoleMasterService{
	
	public Response saveProcedureRoleMaster(ProcedureRoleMasterDto procedureRoleMasterDto) throws ApplicationException;
	
	public Response updateProcedureRoleMaster(ProcedureRoleMasterDto procedureRoleMasterDto) throws ApplicationException;
	
	public Response updateProcedureRoleMasterStatus(ProcedureRoleMasterDto procedureRoleMasterDto) throws ApplicationException;
	
	public Response getProcedureRoleMasterList(ProcedureRoleMasterDto procedureRoleMasterDto) throws ApplicationException;
	
	public Response getProcedureRoleMasterById(Integer procedureRoleId, Integer orgId, Integer unitId) throws ApplicationException;
	
	public Response getProcedureRoleMasterCount(ProcedureRoleMasterDto procedureRoleMasterDto) throws ApplicationException;
	
	public Response getActiveProcedureRoleMasterList(ProcedureRoleMasterDto procedureRoleMasterDto) throws ApplicationException;
	
	public Response getProcedureRoleMasterByName(ProcedureRoleMasterDto procedureRoleMasterDto) throws ApplicationException;
	
	public Response getProcedureRoleMasterByNameNotId(ProcedureRoleMasterDto procedureRoleMasterDto) throws ApplicationException;
	
}
