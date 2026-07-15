package com.param.privilege.service;

import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.param.global.common.Response;
import com.param.privilege.model.Privilege;

public interface PrivilegeService {

	public Integer addPrivilege(Privilege p);

	public void updatePrivilege(Privilege p);
	
	public void updatePrivilegeByRoleId(Privilege p);

	public List<Privilege> listPrivileges(Integer organizationId,Integer unitId);

	public Privilege getPrivilegeById(int id);

	public List<Privilege> getPrivilegeByRoleID(int organizationId,int unitId,int id);
	
	public void removePrivilege(int id);
	
	public void removePrivilegeByRoleId(int organizationId,int unitId,int id);
	
	public List<JSONObject> prepareResponse(JSONArray obj);
	
	Response getPrivilegesByRoleIdList(List<Integer> roleIds,Integer organizationId,Integer unitId);

}