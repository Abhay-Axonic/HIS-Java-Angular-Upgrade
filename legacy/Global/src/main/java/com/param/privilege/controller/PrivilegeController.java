package com.param.privilege.controller;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.json.Json;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonReader;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.param.privilege.Logger.Logger;
import com.param.privilege.constants.Constants;
import com.param.privilege.model.Privilege;
import com.param.privilege.service.PrivilegeService;

import com.param.global.common.ServiceException;
@Controller
@RequestMapping("/privilege")
public class PrivilegeController {
	final static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(PrivilegeController.class);


	@Autowired
	private PrivilegeService PrivilegeService;

	/*
	 * This method returns the List of privileges.
	 */
	@RequestMapping(value="/{organizationId}/{unitId}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> listPrivileges(@PathVariable("organizationId") int organizationId,@PathVariable("unitId") int unitId) throws JSONException {
		List<JSONObject> privilegeJSONList = new ArrayList<JSONObject>();
		String privilegeJSONString;
		Map<String, Object> responseObject;
		try{
			List<Privilege> privileges = PrivilegeService.listPrivileges(organizationId,unitId);
			if(!privileges.isEmpty())
			{
			for (Privilege privilege : privileges) 
				{
				privilegeJSONString = "{ \"privilegeId\" : " + privilege.getPrivilegeId() + ", \"roleId\" : "
						+ privilege.getRoleId() + ", \"modules\" : " + privilege.getModules() +", \"organizationId\" : " + privilege.getOrganisationId() +
						", \"unitId\" : " + privilege.getUnitId() + ", \"moduleType\" : " + privilege.getModuleType()  +" }";
				JSONParser parser = new JSONParser();

			
				JSONObject privilegeJSONObject = (JSONObject) parser.parse(privilegeJSONString);
				privilegeJSONList.add(privilegeJSONObject);
				}
			
			responseObject = new HashMap<>();
			responseObject.put("status", "success");
			responseObject.put("privileges", privilegeJSONList);
			}else{
				responseObject = new HashMap<>();
				responseObject.put("status", "fail");
				responseObject.put("error", "No record is available.");
			}
			} catch (Exception e) {
			logger.error("listPrivileges failed", e);
				responseObject = new HashMap<>();
				responseObject.put("status", "fail");
				responseObject.put("error", e.toString());
				return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
			}

		
		
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}

	/*
	 * This method returns the privilege by privilegeId.
	 */
	@RequestMapping(value = Constants.GET_BY_ID, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPrivilege(@PathVariable("id") int id) throws JSONException {
		String privilegeJSONString;
		Map<String, Object> responseObject;
		JSONObject privilegeJSONObject;
		try {
			Privilege privilege = PrivilegeService.getPrivilegeById(id);
			if(privilege!=null){
			privilegeJSONString = "{ \"privilegeId\" : " + privilege.getPrivilegeId() + ", \"roleId\" : "
					+ privilege.getRoleId() + ", \"modules\" : " + privilege.getModules() +" }";
			JSONParser parser = new JSONParser();
			privilegeJSONObject = (JSONObject) parser.parse(privilegeJSONString);
			responseObject = new HashMap<>();
			responseObject.put("status", "success");
			responseObject.put("privilege", privilegeJSONObject);
			}
			else{
				responseObject = new HashMap<>();
				responseObject.put("status", "fail");
				responseObject.put("error", "No record is available.");
			}
		} catch (Exception e) {
		logger.error("getPrivilege failed", e);
			responseObject = new HashMap<>();
			responseObject.put("status", "fail");
			responseObject.put("error", e.toString());
			return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}
	
	/*
	 * This method returns the privilege by organizationId,unitId and roleId.
	 */
	@RequestMapping(value = Constants.GET_BY_ROLE_ID, method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPrivilegeByRoleID(@PathVariable("organizationId") int organizationId,@PathVariable("unitId") int unitId,@PathVariable("roleId") int id) throws JSONException {
		List<JSONObject> privilegeJSONList = new ArrayList<JSONObject>();
		String privilegeJSONString = null;
		JSONArray obj=null;
		Map<String, Object> responseObject;
		try {
		List<Privilege> privileges = PrivilegeService.getPrivilegeByRoleID(organizationId,unitId,id);
			if(!privileges.isEmpty()){
			for (Privilege privilege : privileges) {
				JSONParser parser = new JSONParser();
				String modules = privilege.getModules();
				obj = (JSONArray) parser.parse(modules);
				privilegeJSONString = "{ \"privilegeId\" : " + privilege.getPrivilegeId() + ", \"roleId\" : "
						+ privilege.getRoleId() + ", \"modules\" : " + privilege.getModules()  + ", \"moduleType\" : " + privilege.getModuleType()  +" }";
				
				JSONObject privilegeJSONObject = (JSONObject) parser.parse(privilegeJSONString);
				privilegeJSONList.add(privilegeJSONObject);
				}
			
			
			List<JSONObject> responseList=PrivilegeService.prepareResponse(obj);
			
			
			
			responseObject = new HashMap<>();
			responseObject.put("status", "success");
			responseObject.put("modules", privilegeJSONList);
			}else{
				responseObject = new HashMap<>();
				responseObject.put("status", "fail");
				responseObject.put("error", "No record is available.");
			}
			} catch (Exception e) {
			logger.error("getPrivilegeByRoleID failed", e);
				responseObject = new HashMap<>();
				responseObject.put("status", "fail");
				responseObject.put("error", e.toString());
				return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
			}
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}
	
	
	/*
	 * This method returns the permissions of modules and submodules on the
	 * basis of roleId array.
	 */
	@RequestMapping(value = Constants.GET_BY_ROLE_ID_LIST, method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPrivilegesByRoleIdArray(HttpEntity<String> httpEntity) throws ParseException {
		String json = httpEntity.getBody();
		Map<String, Object> responseObject;
		JSONArray moduleList = new JSONArray();
		
		try {
			JsonReader jsonReader = Json.createReader(new StringReader(json));
			JsonObject reader = jsonReader.readObject();
			JsonArray roleIds = reader.getJsonArray("roleIds");
			Integer organizationId = reader.getInt("organizationId");
			Integer unitId = reader.getInt("unitId");
			for (int i = 0; i < roleIds.size(); i++) {
				int roleId = roleIds.getInt(i);
				List<Privilege> privileges = PrivilegeService.getPrivilegeByRoleID(organizationId,unitId,roleId);
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
			
			List<JSONObject> responseList=PrivilegeService.prepareResponse(moduleList);
			
			responseObject = new HashMap<>();
			responseObject.put("status", "success");
			responseObject.put("modules", responseList);
			

		} catch (Exception e) {
		logger.error("getPrivilegesByRoleIdArray failed", e);
			responseObject = new HashMap<>();
			responseObject.put("status", "fail");
			responseObject.put("error", e.toString());
			return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}
	

	/*
	 * This method creates a new privilege.
	 */
	@RequestMapping(method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> addPrivilege(HttpEntity<String> httpEntity) throws ParseException {
		String json = httpEntity.getBody();
		Map<String, Object> responseObject;
		try {
			
			JsonReader jsonReader = Json.createReader(new StringReader(json));
			JsonObject reader = jsonReader.readObject();
			String modules=null;
			if(PrivilegeService.getPrivilegeByRoleID(reader.getInt("organisationId"),reader.getInt("unitId"),reader.getInt(Constants.JSON_PROPERTY_ROLEID)).isEmpty())
			{
				if(!reader.getJsonArray(Constants.JSON_PROPERTY_MODULES).isEmpty())
				{
					modules = reader.getJsonArray(Constants.JSON_PROPERTY_MODULES).toString();	
				}
				else
				{
					modules= "[]";
				}
				
				
				Privilege privilege = new Privilege();
				privilege.setRoleId(reader.getInt(Constants.JSON_PROPERTY_ROLEID));
				privilege.setModuleType(reader.getInt("moduleType"));
				privilege.setModules(modules);
				privilege.setOrganisationId(reader.getInt("organisationId"));
				privilege.setUnitId(reader.getInt("unitId"));
				Integer id = PrivilegeService.addPrivilege(privilege);

				responseObject = new HashMap<>();
				responseObject.put("status", "success");
				responseObject.put("id", id);
				
			}
			else
			{
				responseObject = new HashMap<>();
				responseObject.put("error", "Privilge for Role already exists.");
			}
			

		} catch (Exception e) {
		logger.error("addPrivilege failed", e);
			responseObject = new HashMap<>();
			responseObject.put("status", "fail");
			responseObject.put("error", e.toString());
			return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responseObject, HttpStatus.CREATED);
	}
	
	

	/*
	 * This method deletes a particular privilege on the basis of privilegeId.
	 */
	@RequestMapping(value = Constants.DELETE_BY_ID, method = RequestMethod.DELETE)
	public ResponseEntity<?> removePrivilege(@PathVariable("id") int id) throws ParseException {
		Map<String, Object> responseObject;
		try {
			PrivilegeService.removePrivilege(id);
			responseObject = new HashMap<>();
			responseObject.put("status", "success");
		} catch (Exception e) {
		logger.error("removePrivilege failed", e);
			responseObject = new HashMap<>();
			responseObject.put("status", "fail");
			responseObject.put("error", e.toString());
			return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}

	/*
	 * This method deletes a particular privilege on the basis of roleId.
	 */
	@RequestMapping(value = Constants.DELETE_BY_ROLE_ID, method = RequestMethod.DELETE)
	public ResponseEntity<?> removePrivilegeByRoleId(@PathVariable("organizationId") int organizationId,@PathVariable("unitId") int unitId,@PathVariable("roleId") int id) throws ParseException {
		Map<String, Object> responseObject;
		try {
			PrivilegeService.removePrivilegeByRoleId(organizationId,unitId,id);
			responseObject = new HashMap<>();
			responseObject.put("status", "success");
		} catch (Exception e) {
		logger.error("removePrivilegeByRoleId failed", e);
			responseObject = new HashMap<>();
			responseObject.put("status", "fail");
			responseObject.put("error", e.toString());
			return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}

	/*
	 * This method updates a particular privilege on the basis of privilegeId.
	 */
	@RequestMapping(method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updatePrivilege(HttpEntity<String> httpEntity) throws ParseException {
		String json = httpEntity.getBody();
		Map<String, Object> responseObject;
		try {
			JsonReader jsonReader = Json.createReader(new StringReader(json));
			JsonObject reader = jsonReader.readObject();
			String modules=null;
			
			if(!reader.getJsonArray(Constants.JSON_PROPERTY_MODULES).isEmpty())
			{
				modules = reader.getJsonArray(Constants.JSON_PROPERTY_MODULES).toString();	
			}
			else
			{
				modules= "[]";
			}
			Privilege privilege = new Privilege();
			privilege.setPrivilegeId(reader.getInt("privilegeId"));
			privilege.setRoleId(reader.getInt(Constants.JSON_PROPERTY_ROLEID));
			privilege.setModules(modules);
			
			PrivilegeService.updatePrivilege(privilege);
			responseObject = new HashMap<>();
			responseObject.put("status", "success");
		} catch (Exception e) {
		logger.error("updatePrivilege failed", e);
			responseObject = new HashMap<>();
			responseObject.put("status", "fail");
			responseObject.put("error", e.toString());
			return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}
	
	
	/*
	 * This method updates a particular privilege on the basis of roleId.
	 */
	@RequestMapping(value=Constants.UPDATE_BY_ROLE_ID,method = RequestMethod.PUT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updatePrivilegeByRoleId(HttpEntity<String> httpEntity) throws ParseException {
		String json = httpEntity.getBody();
		Map<String, Object> responseObject;
		try {
			JsonReader jsonReader = Json.createReader(new StringReader(json));
			JsonObject reader = jsonReader.readObject();
			String modules = null;
		
			
			if(!reader.getJsonArray(Constants.JSON_PROPERTY_MODULES).isEmpty())
			{
				modules = reader.getJsonArray(Constants.JSON_PROPERTY_MODULES).toString();	
			}
			else
			{
				modules= "[]";
			}
			
			Privilege privilege = new Privilege();
			privilege.setRoleId(reader.getInt(Constants.JSON_PROPERTY_ROLEID));
			privilege.setOrganisationId(reader.getInt(Constants.JSON_PROPERTY_ORG_ID));
			privilege.setUnitId(reader.getInt(Constants.JSON_PROPERTY_UNIT_ID));
			privilege.setModuleType(reader.getInt(Constants.JSON_PROPERTY_MODULETYPE));

			privilege.setModules(modules);
			PrivilegeService.updatePrivilegeByRoleId(privilege);
			responseObject = new HashMap<>();
			responseObject.put("status", "success");
		} catch (Exception e) {
		logger.error("updatePrivilegeByRoleId failed", e);
			responseObject = new HashMap<>();
			responseObject.put("status", "fail");
			responseObject.put("error", e.toString());
			return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}
}