package com.param.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureConfigurationChecklistDto;
import com.param.global.procedure.dto.ProcedureConfigurationDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IProcedureConfigurationChecklistDao {

	Response saveProcedureConfigChecklist(List<ProcedureConfigurationChecklistDto> checklistDetailList,
			int procedureConfigId)throws ApplicationException;

	Response getChecklistListByProcedureConfigId(ProcedureConfigurationDto reqDto) throws ApplicationException;

}
