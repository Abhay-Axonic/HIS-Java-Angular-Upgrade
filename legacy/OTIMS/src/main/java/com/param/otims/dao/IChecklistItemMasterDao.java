package com.param.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.ChecklistItemMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IChecklistItemMasterDao {

	Response saveChecklistItemMaster(ChecklistItemMasterDto checklistItemMasterdto) throws ApplicationException;

	Response updateChecklistItemMasterStatus(ChecklistItemMasterDto checklistItemMasterdto)throws ApplicationException;

	Response getChecklistItemMasterList(ChecklistItemMasterDto checklistItemMasterdto)throws ApplicationException;

	Response searchChecklistItemName(ChecklistItemMasterDto checklistItemMasterdto) throws ApplicationException;

	Response getChecklistItemMasterListCount(ChecklistItemMasterDto checklistItemMasterdto) throws ApplicationException;

}
