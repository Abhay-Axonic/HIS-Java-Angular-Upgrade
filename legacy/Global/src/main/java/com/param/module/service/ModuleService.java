package com.param.module.service;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.param.global.common.Response;
import com.param.module.dao.IModuleDao;

@Service
public class ModuleService implements IModuleService {

	@Autowired
	private IModuleDao moduleDao;

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public Response getAllModules() {
		return moduleDao.getAllModules();
	}

	@Override
	public Response getZeroParentModule() {
		return moduleDao.getZeroParentModule();
	}

	@Override
	@Transactional
	public Response getAllModulesByOrgId(int orgId) throws JsonParseException, JsonMappingException, IOException {
		return moduleDao.getAllModulesByOrgId(orgId);
	}

	@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public Response getBIModuleList() {
		return moduleDao.getBIModuleList();
	}
	
	/*@SuppressWarnings("rawtypes")
	@Override
	@Transactional
	public Response getTreeView() {
		
		List<ModuleDto> allModuleList=moduleService.getAllModules().getListObject();
		List<SubModuleDto> allSubModuleList=subModuleService.getAllSubModule().getListObject();
		
		for (ModuleDto moduleDto : allModuleList) {
			
			for (SubModuleDto subModuleDto : allSubModuleList) {
				
				if(moduleDto.getModuleId()==subModuleDto.getParentId())
				{
					
				}
				
			}
			
		}
		
		
		return moduleDao.getAllModules();
	}*/

}
