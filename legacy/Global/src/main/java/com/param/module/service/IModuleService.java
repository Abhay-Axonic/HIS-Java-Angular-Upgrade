package com.param.module.service;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.param.actionmaster.model.ActionMaster;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IModuleService {

	public Response getAllModules();
	public Response getZeroParentModule();
	public Response getAllModulesByOrgId(int orgId) throws JsonParseException, JsonMappingException, IOException;
	public Response getBIModuleList();
	
	
	//public Response getTreeView();
	

}
