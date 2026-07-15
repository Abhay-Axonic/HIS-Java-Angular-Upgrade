package com.param.privilege.service;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.param.global.common.ICommonConstants;
import com.param.global.common.Response;
import com.param.module.service.IModuleService;
import com.param.privilege.dao.IPrivilegeDao;
import com.param.privilege.dto.ModuleDto;
import com.param.privilege.model.Privilege;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Service
public class PrivilegeServiceImpl implements PrivilegeService,ICommonConstants {
	final static Logger logger = Logger.getLogger(PrivilegeServiceImpl.class);


	@Autowired
	private IPrivilegeDao PrivilegeDAO;
	
	@Autowired
	private IModuleService iModuleService;
	
	

	public void setPrivilegeDAO(IPrivilegeDao PrivilegeDAO) {
		this.PrivilegeDAO = PrivilegeDAO;
	}

	@Override
	@Transactional
	public Integer addPrivilege(Privilege p) {
		return this.PrivilegeDAO.addPrivilege(p);
	}

	@Override
	@Transactional
	public void updatePrivilege(Privilege p) {
		this.PrivilegeDAO.updatePrivilege(p);
	}

	@Override
	@Transactional
	public void updatePrivilegeByRoleId(Privilege p) {
		this.PrivilegeDAO.updatePrivilegeByRoleId(p);
	}
	
	@Override
	@Transactional
	public List<Privilege> listPrivileges(Integer organizationId,Integer unitId) {
		return this.PrivilegeDAO.listPrivileges(organizationId,unitId);
	}

	@Override
	@Transactional
	public Privilege getPrivilegeById(int id) {
		return this.PrivilegeDAO.getPrivilegeById(id);
	}

	@Override
	@Transactional
	public List<Privilege> getPrivilegeByRoleID(int organizationId,int unitId,int id) {
		return this.PrivilegeDAO.getPrivilegeByRoleId(organizationId,unitId,id);
	}
	
	@Override
	@Transactional
	public void removePrivilege(int id) {
		this.PrivilegeDAO.removePrivilege(id);
	}
	
	@Override
	@Transactional
	public void removePrivilegeByRoleId(int organizationId,int unitId,int id) {
		this.PrivilegeDAO.removePrivilegeByRoleId(organizationId,unitId,id);
	}

	@SuppressWarnings("unchecked")
	@Override
	@Transactional
	public List<JSONObject> prepareResponse(JSONArray obj){
	
		@SuppressWarnings("rawtypes")
		Response resp=iModuleService.getAllModules();
		@SuppressWarnings("unchecked")
		
		List<ModuleDto> allModuleList=resp.getListObject();
		
		
		
		List<JSONObject> response= new ArrayList<>();
		

		
		for(int i=0;i<obj.size();i++)
		{
			JSONObject item=(JSONObject) obj.get(i);
			response.add(item);
			getParent(allModuleList,response,item);
		}
		
		
		
				
		return response;
	}
	
	
	@SuppressWarnings("unchecked")
	private void getParent(List<ModuleDto> allModuleList, List<JSONObject> response, JSONObject item){
		for (int i=0;i< allModuleList.size();i++) {
			ModuleDto module=allModuleList.get(i);
			if(module.getModuleId() == Integer.parseInt(item.get("parentId").toString())){
				JSONObject responseItem = new JSONObject();
				responseItem.put("id", module.getModuleId());
				responseItem.put("name", module.getModuleName());
				responseItem.put("parentId", module.getParentId());
				responseItem.put("moduleType", module.getModuleType());
				response.add(responseItem);
				getParent(allModuleList,response,responseItem);
				break;
			}
				
		}
		
		
	}
	
	
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Response getPrivilegesByRoleIdList(List<Integer> roleIds,Integer organizationId,Integer unitId) {
		
		Map<String, Object> responseObject;
		JSONArray moduleList = new JSONArray();
		
		try {
			
			for (int i = 0; i < roleIds.size(); i++) {
				int roleId = roleIds.get(i);
				List<Privilege> privileges = this.getPrivilegeByRoleID(organizationId,unitId,roleId);
				if (!privileges.isEmpty()) {
					for (Privilege privilege : privileges) {

						String modulesJSONString = privilege.getModules();
						
						JsonReader modulesJsonReader = Json
								.createReader(new StringReader("{ \"modules\" : " + modulesJSONString + " }"));
						JsonObject modulesArrayJsonObject = modulesJsonReader.readObject();
						JsonArray moduleJSONArray = modulesArrayJsonObject.getJsonArray("modules");
						
						

						for (int j = 0; j < moduleJSONArray.size(); j++) {
							JsonObject moduleJSONObject = (JsonObject) moduleJSONArray.get(j);
							String jsonModule = moduleJSONObject.toString();
							JSONParser parser1 = new JSONParser();
							JSONObject jsonModuleObject = (JSONObject) parser1.parse(jsonModule);
							if(!moduleList.contains(jsonModuleObject))
							{
								moduleList.add(jsonModuleObject);
							}
						}

						
					}

				}

			}
			
			List<JSONObject> responseList=this.prepareResponse(moduleList);
			
			responseObject = new HashMap<>();
			responseObject.put("status", "success");
			responseObject.put("modules", responseList);
			

		} catch (Exception e) {
		logger.error("getPrivilegesByRoleIdList failed", e);
			responseObject = new HashMap<>();
			responseObject.put("status", "fail");
			responseObject.put("error", e.toString());
			return new Response(ERROR, null, null, null,null);
		}
		return new Response(SUCCESS, null, null, null,responseObject);
	}
	
	
	

}






























//Set<Integer> onlyIds=new HashSet<Integer>();
//Set<Integer> finalIds=new HashSet<Integer>();
//Set<Integer> finalparentIds=new HashSet<Integer>();
//
//for(int i=0;i<obj.size();i++)
//{
//	JSONObject item=(JSONObject) obj.get(i);
//	onlyIds.add(Integer.parseInt(item.get("id").toString()));
//}
//
//for (Integer id : onlyIds) {
//	for (ModuleDto moduleDto : allModuleList) {
//		finalIds.add(id);
//		if(moduleDto.getModuleId() == id ){
//			finalIds.add(moduleDto.getParentId());
//		}
//	}
//}
//
//for (Integer id : finalIds) {
//	for (ModuleDto moduleDto : allModuleList) {
//		finalparentIds.add(id);
//		if(moduleDto.getModuleId() == id ){
//			finalparentIds.add(moduleDto.getParentId());
//			
//		}
//	}
//}
//
//
//for (ModuleDto md : allModuleList) {
//	for (Integer fid : finalparentIds) {
//		if(md.getModuleId()==fid)
//		{
//			JSONObject responseItem = new JSONObject();
//			responseItem.put("id", md.getModuleId());
//			responseItem.put("name", md.getModuleName());
//			response.add(responseItem);
//		}
//		
//	}
//	
//}	