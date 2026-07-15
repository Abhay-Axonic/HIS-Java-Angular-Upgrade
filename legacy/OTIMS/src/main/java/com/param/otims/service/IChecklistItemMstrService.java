package com.param.otims.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.ChecklistItemMasterDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IChecklistItemMstrService {

	
	Response saveChecklistItemMaster(ChecklistItemMasterDto checklistItemMasterdto) throws ApplicationException;

	Response updateChecklistItemMasterStatus(ChecklistItemMasterDto checklistItemMasterdto)throws ApplicationException;

	Response getChecklistItemMasterList(ChecklistItemMasterDto checklistItemMasterdto)throws ApplicationException;

	Response searchChecklistItemName(ChecklistItemMasterDto checklistItemMasterdto) throws ApplicationException;

}
