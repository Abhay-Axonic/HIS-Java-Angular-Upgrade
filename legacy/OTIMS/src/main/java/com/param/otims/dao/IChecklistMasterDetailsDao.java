package com.param.otims.dao;

import java.util.List;

import com.param.global.common.Response;
import com.param.global.otims.dto.ChecklistMasterDetailsDto;

import in.param.exception.ApplicationException;

@SuppressWarnings({"rawtypes"})
public interface IChecklistMasterDetailsDao {

	Response saveChecklistMasterDetails(List<ChecklistMasterDetailsDto> checklistMasterDetails, Integer checklistMasterId) throws ApplicationException;

	Response updateChecklistMasterDetails(Integer checklistMasterId)throws ApplicationException;

	Response getChecklistMasterDetailsByMasterId(ChecklistMasterDetailsDto dto) throws ApplicationException;

}
