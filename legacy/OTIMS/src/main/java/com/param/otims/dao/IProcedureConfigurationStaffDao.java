package com.param.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureConfigurationStaffDto;
import com.param.global.procedure.dto.ProcedureConfigurationDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IProcedureConfigurationStaffDao {

	Response saveProcedureConfigStaff(List<ProcedureConfigurationStaffDto> staffDetailList, int procedureConfigId)throws ApplicationException;

	Response getStaffListByProcedureConfigId(ProcedureConfigurationDto reqDto)throws ApplicationException;

}
