package com.param.module.dao;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface IModuleDao {
	public Response getAllModules();
	public Response getZeroParentModule();
	public Response getAllModulesByOrgId(int orgId) throws JsonParseException, JsonMappingException, IOException;
	public Response getBIModuleList();
}
