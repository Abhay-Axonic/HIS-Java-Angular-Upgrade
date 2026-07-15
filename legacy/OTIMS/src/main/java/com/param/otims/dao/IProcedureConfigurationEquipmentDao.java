package com.param.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureConfigurationEquipmentDto;
import com.param.global.procedure.dto.ProcedureConfigurationDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IProcedureConfigurationEquipmentDao {

	
	Response saveProcedureConfigEquipment(List<ProcedureConfigurationEquipmentDto> inventoryItemList,
			int procedureConfigId)throws ApplicationException;

	Response getEquipmentByProcedureConfigId(ProcedureConfigurationDto reqDto) throws ApplicationException;

}
