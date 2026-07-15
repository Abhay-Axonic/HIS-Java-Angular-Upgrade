package com.param.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureConfigurationPatientEducationDto;
import com.param.global.procedure.dto.ProcedureConfigurationDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IProcedureConfigurationPatientEducationDao {

	Response saveProcedureConfigPatientEducation(ProcedureConfigurationPatientEducationDto patientEduObj,
			int procedureConfigId)throws ApplicationException;

	Response getPatientEducationByProcedureConfigId(ProcedureConfigurationDto reqDto)throws ApplicationException;
	
}
