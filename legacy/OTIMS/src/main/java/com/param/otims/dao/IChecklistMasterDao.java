package com.param.otims.dao;

import com.param.global.common.Response;
import com.param.global.otims.dto.ChecklistMasterDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IChecklistMasterDao {

	Response saveChecklistMaster(ChecklistMasterDto checklistMasterDto) throws ApplicationException;

	Response updateChecklistMasterStatus(ChecklistMasterDto checklistMasterDto) throws ApplicationException;

	Response getAllChecklistMasterList(ChecklistMasterDto checklistMasterDto) throws ApplicationException;
	
	Response getChecklistMasterByStageId(ChecklistMasterDto dto)throws ApplicationException;

	Response getAllChecklistMasterListCount(ChecklistMasterDto checklistMasterDto)throws ApplicationException;

}
