package com.param.role.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.param.adt.master.global.model.RoleMaster;
import com.param.role.service.IRoleService;

import org.apache.log4j.Logger;
import com.param.global.common.ServiceException;
@Controller
@RequestMapping("/role")
public class RoleController {
	final static Logger logger = Logger.getLogger(RoleController.class);


	@Autowired
	private IRoleService roleService;

	/*
	 * This method returns the List of Roles.
	 */
	@RequestMapping(value="/{organizationId}/{unitId}",method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getAllRoles(@PathVariable("organizationId") int organizationId,@PathVariable("unitId") int unitId) throws JSONException 
	{
		Map<String,Object> responseObject;
		
		List<Map<String,Object>> roleRecordList=new ArrayList<>();
		List<RoleMaster> roles = null;
		try {
			roles = roleService.getAllRoles(organizationId,unitId);
			for (RoleMaster roleMaster : roles)
			{
				Map<String,Object> roleRecord=new HashMap<>();
				roleRecord.put("roleId",roleMaster.getRoleId());
				roleRecord.put("roleDesc", roleMaster.getRoleDesc());
				////System.out.println("roleDesc"+roleMaster.getRoleDesc());
				roleRecordList.add(roleRecord);
			}
			
			responseObject=new HashMap<>();
			responseObject.put("status", "success");
			responseObject.put("roles", roleRecordList);
		} catch (Exception e) {
			logger.error("getAllRoles failed", e);
			responseObject = new HashMap<>();
			responseObject.put("status", "fail");
			responseObject.put("error", e.toString());
			return new ResponseEntity<>(responseObject, HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(responseObject, HttpStatus.OK);
	}
}
