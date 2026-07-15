package com.param.global.unit.dao;

import in.param.exception.ApplicationException;

import com.param.global.common.Response;
import com.param.global.unit.dto.ActionMasterDto;

@SuppressWarnings({"rawtypes"})

public interface IActionMasterDaoGlobal {
	
	public Response saveActionMaster(ActionMasterDto actionMasterDto) throws ApplicationException;
	
	public Response updateActionMaster(ActionMasterDto actionMasterDto) throws ApplicationException;
	
	public Response updateActionMasterStatus(ActionMasterDto actionMasterDto) throws ApplicationException;
	
	public Response getActionMasterList(Integer offset, Integer noOfRecordsPerPage) throws ApplicationException;
	
	public Response getActionMasterById(Integer actionId) throws ApplicationException;
	
	public Response getActionMasterCount() throws ApplicationException;
	
	public Response getActiveActionMaster() throws ApplicationException;
	
	public Response getActionMasterByName(ActionMasterDto actionMasterDto) throws ApplicationException;
	
	public Response getActionMasterByNameNotId(ActionMasterDto actionMasterDto) throws ApplicationException;
	
	public Response searchActionMasterByParentActionName(ActionMasterDto actionMasterDto) throws ApplicationException;
	
	public Response getActionId(ActionMasterDto actionMasterDto) throws ApplicationException;
	
	public Response getParentId() throws ApplicationException;

}
