package com.param.submodule.service;

import java.util.List;
import java.util.Map;

import com.param.actionmaster.model.ActionMaster;
import com.param.global.common.Response;

@SuppressWarnings("rawtypes")
public interface ISubModuleService {

	public List<ActionMaster> getAllSubModulesByModuleName(String moduleName);
	
	public Response getAllSubModulesByModuleId(Integer moduleId);
	
	public Response getAllSubModule();
	
	public List<ActionMaster> getAllSubModules();
	public List<Map<String,Object>> getAllSubModulesForTreeView(int id);
}
