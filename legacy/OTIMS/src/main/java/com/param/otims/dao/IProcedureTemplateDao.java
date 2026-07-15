package com.param.otims.dao;

import com.param.global.common.Response;
import com.param.global.procedure.dto.ProcedureConfigurationDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IProcedureTemplateDao {

	Response saveProcedureConfigTemplate(ProcedureConfigurationDto reqDto)throws ApplicationException;

	boolean validProcConfByServiceOrgUnitConfigId(Integer serviceId, Integer orgId, Integer unitId,
			int procedureConfigId)throws ApplicationException;

	Response getProcedureConfigList(int orgId, int unitId)throws ApplicationException;

	Response getOTT(Integer procedureConfigId)throws ApplicationException;

	boolean validProcConfByServiceOrgUnitServiceId(Integer serviceId, Integer orgId, Integer unitId);

	Response getOTTThetreAndTableByServiceId(int serviceId, int orgId, int unitId);

}
