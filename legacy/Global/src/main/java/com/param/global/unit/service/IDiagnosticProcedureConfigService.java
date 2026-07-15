package com.param.global.unit.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.unit.dto.DiagnosticProcedureConfigDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IDiagnosticProcedureConfigService {

	public Response saveDiagnosticProcedureConfig(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto)throws ApplicationException;

	public Response getDiagnosticProcedureConfigList(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto)throws ApplicationException;

	public Response updateDiagnosticProcedureConfig(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto)throws ApplicationException;

	public Response updateDiagnosticProcedureConfigStatus(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto)throws ApplicationException;

	public Response getDiagnosticProcedureConfigCount(DiagnosticProcedureConfigDto diagnosticProcedureConfigDto)throws ApplicationException;

	public Response getDiagnosticProcedureConfigById(Integer diagnosticProcedureConfigId, Integer organizationId, Integer unitId);


}
