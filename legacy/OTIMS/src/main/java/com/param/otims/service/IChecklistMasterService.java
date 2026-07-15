package com.param.otims.service;

import javax.transaction.Transactional;

import com.param.global.common.Response;
import com.param.global.otims.dto.ChecklistMasterDetailsDto;
import com.param.global.otims.dto.ChecklistMasterDto;

import in.param.exception.ApplicationException;

@Transactional
@SuppressWarnings("rawtypes")
public interface IChecklistMasterService {

	Response saveChecklistMaster(ChecklistMasterDto checklistMasterDto) throws ApplicationException;

	Response updateChecklistMasterStatus(ChecklistMasterDto checklistMasterDto) throws ApplicationException;

	Response getAllChecklistMasterList(ChecklistMasterDto checklistMasterDto) throws ApplicationException;

	Response getChecklistMasterDetailsByMasterId(ChecklistMasterDetailsDto dto) throws ApplicationException;

	Response getChecklistMasterByStageId(ChecklistMasterDto dto) throws ApplicationException;

}
