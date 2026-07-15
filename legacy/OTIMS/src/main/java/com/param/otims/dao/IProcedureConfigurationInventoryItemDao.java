package com.param.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.otims.dto.ProcedureConfigurationInventoryItemDto;
import com.param.global.procedure.dto.ProcedureConfigurationDto;

import in.param.exception.ApplicationException;
@SuppressWarnings("rawtypes")
public interface IProcedureConfigurationInventoryItemDao {

	
	Response saveProcedureConfigInventoryItem(List<ProcedureConfigurationInventoryItemDto> inventoryItemList,
			int procedureConfigId)throws ApplicationException;

	Response getInventoryItemsByProcedureConfigId(ProcedureConfigurationDto reqDto) throws ApplicationException;

}
