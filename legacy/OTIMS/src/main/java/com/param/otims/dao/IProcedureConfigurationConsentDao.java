package com.param.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureConfigurationConsentDto;
import com.param.global.procedure.dto.ProcedureConfigurationDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IProcedureConfigurationConsentDao {

	
	Response saveProcedureConfigConsent(List<ProcedureConfigurationConsentDto> consentDetailList,
			int procedureConfigId)throws ApplicationException;

	Response getConsentListByProcedureConfigId(ProcedureConfigurationDto reqDto) throws ApplicationException;


	
}
