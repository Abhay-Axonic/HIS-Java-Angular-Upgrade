package com.param.global.procedure.service;

import in.param.exception.ApplicationException;


import com.param.common.dto.CommonResponse;
import com.param.global.common.Response;
import com.param.global.procedure.dto.ProcedureConfigurationDto;

@SuppressWarnings("rawtypes")
public interface IProcedureConfigurationService {
	public CommonResponse saveProcedureConfiguration(ProcedureConfigurationDto dto)throws ApplicationException;
	public Response getProcedureConfigList(int orgId,int unitId)throws ApplicationException;
	public Response updateProcedureConfigStatus(int id,char status)throws ApplicationException;
	public CommonResponse updateProcedureConfiguration(ProcedureConfigurationDto dto)throws ApplicationException;
	public Response getFuturedOrderProcedureByEncounter(int patientId,int doctorId) throws ApplicationException;
	public Response convertProcedure(int admissionId,int visitTypeId,int proReqId) throws ApplicationException;
	public Response getorderDetailsIdByProcedureReqId(Integer proReqId)throws ApplicationException;
	public Response getFuturedOrderProcedureByAdmission(Integer admissionId)throws ApplicationException;
	public Response getNotesListByProConfigId(Integer orgId, Integer unitId, Integer proConfigId)throws ApplicationException;
}
