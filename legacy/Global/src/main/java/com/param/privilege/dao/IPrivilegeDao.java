package com.param.privilege.dao;

import java.util.List;

import com.param.privilege.model.Privilege;

public interface IPrivilegeDao {

	public Integer addPrivilege(Privilege p);

	public void updatePrivilege(Privilege p);
	
	public void updatePrivilegeByRoleId(Privilege p);
	
	public List<Privilege> listPrivileges(Integer organizationId,Integer unitId);

	public Privilege getPrivilegeById(int id);
	
	public List<Privilege> getPrivilegeByRoleId(int organizationId,int unitId,int id);

	public void removePrivilege(int id);
	
	public void removePrivilegeByRoleId(int organizationId,int unitId,int id);

}