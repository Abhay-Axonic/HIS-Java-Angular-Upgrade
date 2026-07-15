package com.param.equipmentchecklist.service;

import javax.transaction.Transactional;

import com.param.equipmentchecklist.Dto.EquipmentChecklistDto;
import com.param.equipmentchecklist.Dto.EquipmentChecklistMapperDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IEquipmentChecklistMapperService {

	Response saveChecklistEquipmentMapperMaster(EquipmentChecklistMapperDto equipmentChecklistMapperDto) throws ApplicationException;

	Response getAllEquipmentChecklistMapperMasterList(EquipmentChecklistMapperDto equipmentChecklistMapperDto) throws ApplicationException;

	Response updateChecklistEquipmentMapperStatus(EquipmentChecklistMapperDto equipmentChecklistMapperDto) throws ApplicationException;

//	Response getEquipmentChecklistNameListById(EquipmentChecklistMapperDto equipmentChecklistMapperDto) throws ApplicationException;
	

}
