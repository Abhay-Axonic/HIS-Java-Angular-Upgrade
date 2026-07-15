package com.param.equipmentchecklist.service;

import javax.transaction.Transactional;

import com.param.equipmentchecklist.Dto.EquipmentChecklistDto;
import com.param.global.common.Response;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IEquipmentChecklistService {

	Response saveEquipmentChecklist(EquipmentChecklistDto equipmentChecklistDto) throws ApplicationException;

	Response getAllEquipmentChecklistMasterList(EquipmentChecklistDto equipmentChecklistDto) throws ApplicationException;

	Response updateChecklistEquipmentMasterStatus(EquipmentChecklistDto equipmentChecklistDto) throws ApplicationException;

	Response getEquipmentChecklistNameListById(EquipmentChecklistDto equipmentChecklistDto) throws ApplicationException;

	Response getAllEquipmentChecklistMasterListPaginationList(EquipmentChecklistDto equipmentChecklistDto) throws ApplicationException;

	Response getAllEquipmentChecklistMasterListPaginationListCount(EquipmentChecklistDto equipmentChecklistDto)throws ApplicationException;
	

}
