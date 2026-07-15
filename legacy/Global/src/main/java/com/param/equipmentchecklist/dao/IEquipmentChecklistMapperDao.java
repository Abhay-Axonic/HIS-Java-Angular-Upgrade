package com.param.equipmentchecklist.dao;
import com.param.equipmentchecklist.Dto.EquipmentChecklistMapperDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IEquipmentChecklistMapperDao {

	Response saveChecklistEquipmentMapperMaster(EquipmentChecklistMapperDto equipmentChecklistMapperDto) throws ApplicationException;

	Response getAllEquipmentChecklistMapperMasterList(EquipmentChecklistMapperDto equipmentChecklistMapperDto) throws ApplicationException;

	Response updateChecklistEquipmentMapperStatus(EquipmentChecklistMapperDto equipmentChecklistMapperDto) throws ApplicationException;

	/*Response getEquipmentChecklistNameListById(EquipmentChecklistMapperDto equipmentChecklistMapperDto) throws ApplicationException;*/

}
