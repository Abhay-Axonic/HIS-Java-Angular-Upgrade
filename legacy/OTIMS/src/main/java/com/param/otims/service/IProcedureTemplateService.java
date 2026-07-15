package com.param.otims.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.procedure.dto.ProcedureConfigurationDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IProcedureTemplateService {

	Response saveProcedureConfigTemplate(ProcedureConfigurationDto reqDto)throws ApplicationException;

	Response getProcedureConfigTemplate(ProcedureConfigurationDto reqDto) throws ApplicationException;

	Response getProcedureConfigList(int orgId, int unitId)throws ApplicationException;

	Response getOTTThetreAndTableByServiceId(int serviceId, int orgId, int unitId)throws ApplicationException;

	Response getProcedureConfigureOnOTBillingCompletion(int admissionId, int encounterId, int serviceId, int orgId, int unitId, String date, String fromTime, String toTime, String toDate);


}
