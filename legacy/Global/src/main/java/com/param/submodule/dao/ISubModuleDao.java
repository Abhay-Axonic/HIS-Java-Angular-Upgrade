package com.param.submodule.dao;

import java.util.List;

import com.param.actionmaster.model.ActionMaster;
import com.param.global.common.Response;

public interface ISubModuleDao {

	public List<ActionMaster> getAllSubModulesByModuleName(String moduleName);

	public Response getAllSubModulesByModuleId(Integer moduleId);
	
	public Response getAllSubModule();
	
	public List<ActionMaster> getAllSubModules();
}
