package com.param.equipmentchecklist.dao;
import com.param.equipmentchecklist.Dto.EquipmentChecklistDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IEquipmentChecklistDao {

	Response saveEquipmentChecklist(EquipmentChecklistDto equipmentChecklistDto) throws ApplicationException;

	Response getAllEquipmentChecklistMasterList(EquipmentChecklistDto equipmentChecklistDto) throws ApplicationException;

	Response updateChecklistEquipmentMasterStatus(EquipmentChecklistDto equipmentChecklistDto) throws ApplicationException;

	Response getEquipmentChecklistNameListById(EquipmentChecklistDto equipmentChecklistDto) throws ApplicationException;

	Response getAllMealMasterListPaginationList(EquipmentChecklistDto equipmentChecklistDto) throws ApplicationException;

	Response getAllMealMasterListPaginationListCount(EquipmentChecklistDto equipmentChecklistDto) throws ApplicationException;

}
