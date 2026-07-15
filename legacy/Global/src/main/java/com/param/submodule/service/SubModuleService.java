package com.param.submodule.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.actionmaster.model.ActionMaster;
import com.param.global.common.Response;
import com.param.privilege.Logger.Logger;
import com.param.screen.service.IScreenService;
import com.param.submodule.dao.ISubModuleDao;

@Service
public class SubModuleService implements ISubModuleService {

	@Autowired
	private ISubModuleDao subModuleDao;

	@Autowired
	private IScreenService iScreenService;

	@Override
	@Transactional
	public List<ActionMaster> getAllSubModulesByModuleName(String moduleName) {
		return subModuleDao.getAllSubModulesByModuleName(moduleName);
	}

	@Override
	@Transactional
	public Response getAllSubModulesByModuleId(Integer moduleId) {
		return subModuleDao.getAllSubModulesByModuleId(moduleId);
	}
	
	@Override
	@Transactional
	public Response getAllSubModule() {
		return subModuleDao.getAllSubModule();
	}
	
	@Override
	@Transactional
	public List<Map<String,Object>> getAllSubModulesForTreeView(int id) {
		List<ActionMaster> actionMasterList = null;
		
		Map<String,Object> subModuleRecord;
		List<Map<String,Object>> subModuleRecordList=new ArrayList<>();
		
			actionMasterList = subModuleDao.getAllSubModules();
			for (ActionMaster actionMaster : actionMasterList) {
				subModuleRecord=new HashMap<>();
				subModuleRecord.put("subModuleId", actionMaster.getAction_id());
				subModuleRecord.put("subModuleName", actionMaster.getAction_name());
				subModuleRecord.put("children", iScreenService.getAllScreens(actionMaster.getAction_id()));
				subModuleRecordList.add(subModuleRecord);
			}
			
		return subModuleRecordList;
	}
	
	@Override
	@Transactional
	public List<ActionMaster> getAllSubModules() {
		return subModuleDao.getAllSubModules();
	}
}
