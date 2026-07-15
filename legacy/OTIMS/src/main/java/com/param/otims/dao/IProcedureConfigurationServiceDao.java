package com.param.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureConfigurationServiceDto;
import com.param.global.procedure.dto.ProcedureConfigurationDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IProcedureConfigurationServiceDao {

	Response saveProcedureConfigService(List<ProcedureConfigurationServiceDto> serviceList, int procedureConfigId) throws ApplicationException;

	Response getServiceListByProcedureConfigId(ProcedureConfigurationDto dto) throws ApplicationException;
}
