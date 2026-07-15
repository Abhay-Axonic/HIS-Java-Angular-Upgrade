package com.param.role.dao;

import java.util.List;

import com.param.adt.master.global.model.RoleMaster;




public interface IRoleDao {

	public List<RoleMaster> getAllRoles(Integer organizationId,Integer unitId);

}
